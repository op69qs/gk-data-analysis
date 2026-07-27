package org.jeecg.modules.reporting.parser;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

/**
 * 读取 JAR 支持的旧 XLS 和 XLSX。首行为表头，数据从第二行开始。
 */
public class TimsExcelParser {
    public TimsExcelParseResult parse(Path file, TimsBusinessType type) throws IOException {
        List<TimsReportRecord> records = new ArrayList<>();
        List<TimsExcelParseError> errors = parse(file, type, records::add).getErrors();
        return new TimsExcelParseResult(records, errors);
    }

    public TimsExcelParseResult parse(Path file, TimsBusinessType type,
                                      Consumer<TimsReportRecord> recordConsumer) throws IOException {
        String fileName = file.getFileName().toString();
        if (!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx")) {
            throw new IllegalArgumentException("TIMS 文件必须是小写 .xls 或 .xlsx：" + fileName);
        }
        List<TimsExcelParseError> errors = new ArrayList<>();
        DataFormatter formatter = new DataFormatter(Locale.CHINA);

        try (InputStream input = Files.newInputStream(file)) {
            Workbook workbook = WorkbookFactory.create(input);
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
                parseSheet(workbook.getSheetAt(sheetIndex), fileName, type, formatter, evaluator,
                        recordConsumer, errors);
            }
        } catch (Exception exception) {
            if (exception instanceof UncheckedIOException) throw ((UncheckedIOException) exception).getCause();
            if (exception instanceof IOException) throw (IOException) exception;
            errors.add(new TimsExcelParseError(fileName, null, 0, null, null,
                    "Excel 文件无法读取：" + exception.getMessage()));
        }
        return new TimsExcelParseResult(java.util.Collections.emptyList(), errors);
    }

    private void parseSheet(Sheet sheet, String fileName, TimsBusinessType type,
                            DataFormatter formatter, FormulaEvaluator evaluator,
                            Consumer<TimsReportRecord> recordConsumer,
                            List<TimsExcelParseError> errors) {
        ColumnLayout layout = ColumnLayout.fixed(type);

        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row == null || layout.isBlank(row, formatter, evaluator)) continue;
            try {
                recordConsumer.accept(toRecord(row, layout, type, fileName, sheet.getSheetName(), formatter, evaluator));
            } catch (RowValueException exception) {
                errors.add(new TimsExcelParseError(fileName, sheet.getSheetName(), rowIndex + 1L,
                        exception.columnName, exception.rawValue, exception.getMessage()));
            }
        }
    }

    private TimsReportRecord toRecord(Row row, ColumnLayout layout, TimsBusinessType type,
                                      String fileName, String sheetName, DataFormatter formatter,
                                      FormulaEvaluator evaluator) {
        TimsReportRecord record = new TimsReportRecord();
        String dAcctText = required(display(row, layout.date, formatter, evaluator), "日期");
        record.setDAcctText(dAcctText);
        record.setDAcct(parseDate(row.getCell(layout.date), dAcctText));
        record.setTreCode(required(display(row, layout.treCode, formatter, evaluator), "国库代码"));
        record.setTreasuryName(required(display(row, layout.treasuryName, formatter, evaluator), "国库简称"));
        if (record.getTreasuryName().contains("N")) {
            throw new RowValueException("国库简称", record.getTreasuryName(),
                    "上传文件异常：包含【" + record.getTreasuryName() + "】的国库名称");
        }
        record.setLevel(required(display(row, layout.level, formatter, evaluator), "预算级次"));
        record.setFileName(fileName);
        record.setSheetName(sheetName);
        record.setRowNumber(row.getRowNum() + 1L);

        if (type == TimsBusinessType.INCOME || type == TimsBusinessType.PAYOUT) {
            if (layout.taxOrg >= 0) record.setTaxOrgCode(display(row, layout.taxOrg, formatter, evaluator));
            record.setSubjectCode(required(display(row, layout.subjectCode, formatter, evaluator), "科目代码"));
            record.setSubjectName(required(display(row, layout.subjectName, formatter, evaluator), "科目名称"));
            record.setCurrentAmount(decimal(display(row, layout.currentAmount, formatter, evaluator), "本期执行数"));
            record.setYearAmount(decimal(display(row, layout.yearAmount, formatter, evaluator), "年累计"));
        } else {
            record.setDebitAmount(decimal(display(row, layout.debit, formatter, evaluator), "借方"));
            record.setCreditAmount(decimal(display(row, layout.credit, formatter, evaluator), "贷方"));
            record.setBalance(decimal(display(row, layout.balance, formatter, evaluator), "余额"));
        }
        return record;
    }

    private String display(Row row, int column, DataFormatter formatter, FormulaEvaluator evaluator) {
        if (column < 0) return "";
        Cell cell = row.getCell(column);
        return cell == null ? "" : formatter.formatCellValue(cell, evaluator).trim();
    }

    private String required(String value, String field) {
        if (value == null || value.trim().isEmpty()) {
            throw new RowValueException(field, value, field + "不能为空");
        }
        return value.trim();
    }

    private BigDecimal decimal(String value, String field) {
        if (value == null || value.trim().isEmpty()) return BigDecimal.ZERO;
        String normalized = value.replace(",", "").replace("￥", "").trim();
        try {
            return new BigDecimal(normalized).setScale(2, RoundingMode.HALF_EVEN);
        } catch (NumberFormatException exception) {
            throw new RowValueException(field, value, field + "不是有效金额：" + value);
        }
    }

    private LocalDate parseDate(Cell cell, String text) {
        LocalDate date;
        try {
            if (cell != null && cell.getCellType() == Cell.CELL_TYPE_NUMERIC
                    && DateUtil.isCellDateFormatted(cell)) {
                date = Instant.ofEpochMilli(cell.getDateCellValue().getTime())
                        .atZone(ZoneId.systemDefault()).toLocalDate();
            } else {
                date = parseDateText(text);
            }
        } catch (RuntimeException exception) {
            throw new RowValueException("日期", text, "日期格式不正确：" + text);
        }
        if (date.getYear() < 1970 || date.getYear() > 2038) {
            throw new RowValueException("日期", text, "日期超出 JAR 允许范围 1970-01-01 至 2038-01-19");
        }
        return date;
    }

    private LocalDate parseDateText(String raw) {
        String value = required(raw, "日期").replace('/', '-');
        if (value.matches("\\d{6}")) {
            return YearMonth.parse(value, DateTimeFormatter.ofPattern("yyyyMM")).atDay(1);
        }
        if (value.matches("\\d{4}-\\d{1,2}")) {
            String[] parts = value.split("-");
            return YearMonth.of(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])).atDay(1);
        }
        if (value.matches("\\d{8}")) {
            return LocalDate.parse(value, DateTimeFormatter.BASIC_ISO_DATE);
        }
        if (value.matches("\\d{4}-\\d{1,2}-\\d{1,2}")) {
            String[] parts = value.split("-");
            return LocalDate.of(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
        }
        if (value.matches("\\d{4,5}")) {
            double serial = Double.parseDouble(value);
            return Instant.ofEpochMilli(DateUtil.getJavaDate(serial).getTime())
                    .atZone(ZoneId.systemDefault()).toLocalDate();
        }
        throw new DateTimeParseException("unsupported", value, 0);
    }

    private static class RowValueException extends IllegalArgumentException {
        private final String columnName;
        private final String rawValue;

        private RowValueException(String columnName, String rawValue, String message) {
            super(message);
            this.columnName = columnName;
            this.rawValue = rawValue;
        }
    }

    private static class ColumnLayout {
        private int date = -1;
        private int treCode = -1;
        private int treasuryName = -1;
        private int taxOrg = -1;
        private int level = -1;
        private int subjectCode = -1;
        private int subjectName = -1;
        private int currentAmount = -1;
        private int yearAmount = -1;
        private int debit = -1;
        private int credit = -1;
        private int balance = -1;

        static ColumnLayout fixed(TimsBusinessType type) {
            ColumnLayout layout = new ColumnLayout();
            layout.date = 0;
            layout.treCode = 1;
            layout.treasuryName = 2;
            if (type == TimsBusinessType.INCOME) {
                layout.taxOrg = 3;
                layout.level = 4;
                layout.subjectCode = 5;
                layout.subjectName = 6;
                layout.currentAmount = 7;
                layout.yearAmount = 8;
            } else if (type == TimsBusinessType.PAYOUT) {
                layout.level = 3;
                layout.subjectCode = 4;
                layout.subjectName = 5;
                layout.currentAmount = 6;
                layout.yearAmount = 7;
            } else if (type == TimsBusinessType.STOCK) {
                layout.level = 3;
                layout.debit = 4;
                layout.credit = 5;
                layout.balance = 6;
            } else {
                throw new IllegalArgumentException("不支持的 TIMS 业务类型：" + type);
            }
            return layout;
        }

        boolean isBlank(Row row, DataFormatter formatter, FormulaEvaluator evaluator) {
            for (int column : Arrays.asList(date, treCode, treasuryName, taxOrg, level, subjectCode,
                    subjectName, currentAmount, yearAmount, debit, credit, balance)) {
                if (column >= 0) {
                    Cell cell = row.getCell(column);
                    if (cell != null && !formatter.formatCellValue(cell, evaluator).trim().isEmpty()) return false;
                }
            }
            return true;
        }
    }
}
