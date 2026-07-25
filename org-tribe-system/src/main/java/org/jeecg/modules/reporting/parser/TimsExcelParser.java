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
import java.math.BigDecimal;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 读取 JAR 支持的旧 XLS 和 XLSX。首行为表头，数据从第二行开始。
 */
public class TimsExcelParser {
    public TimsExcelParseResult parse(Path file, TimsBusinessType type) throws IOException {
        String fileName = file.getFileName().toString();
        if (!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx")) {
            throw new IllegalArgumentException("TIMS 文件必须是小写 .xls 或 .xlsx：" + fileName);
        }
        List<TimsReportRecord> records = new ArrayList<>();
        List<TimsExcelParseError> errors = new ArrayList<>();
        DataFormatter formatter = new DataFormatter(Locale.CHINA);

        try (InputStream input = Files.newInputStream(file)) {
            Workbook workbook = WorkbookFactory.create(input);
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
                parseSheet(workbook.getSheetAt(sheetIndex), fileName, type, formatter, evaluator, records, errors);
            }
        } catch (Exception exception) {
            if (exception instanceof IOException) throw (IOException) exception;
            errors.add(new TimsExcelParseError(fileName, null, 0, null, null,
                    "Excel 文件无法读取：" + exception.getMessage()));
        }
        return new TimsExcelParseResult(records, errors);
    }

    private void parseSheet(Sheet sheet, String fileName, TimsBusinessType type,
                            DataFormatter formatter, FormulaEvaluator evaluator,
                            List<TimsReportRecord> records,
                            List<TimsExcelParseError> errors) {
        Row header = sheet.getRow(0);
        ColumnLayout layout = ColumnLayout.detect(header, type, formatter, evaluator);
        if (!layout.valid) {
            errors.add(new TimsExcelParseError(fileName, sheet.getSheetName(), 1,
                    "表头", layout.headerText, layout.errorMessage));
            return;
        }

        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row == null || layout.isBlank(row, formatter, evaluator)) continue;
            try {
                records.add(toRecord(row, layout, type, fileName, sheet.getSheetName(), formatter, evaluator));
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
        record.setDAcct(parseDate(row.getCell(layout.date), display(row, layout.date, formatter, evaluator)));
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
            if (layout.account >= 0) record.setAccount(display(row, layout.account, formatter, evaluator));
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
            return new BigDecimal(normalized);
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
        return date.withDayOfMonth(date.lengthOfMonth());
    }

    private LocalDate parseDateText(String raw) {
        String value = required(raw, "日期").replace('/', '-');
        if (value.matches("\\d{6}")) {
            return YearMonth.parse(value, DateTimeFormatter.ofPattern("yyyyMM")).atEndOfMonth();
        }
        if (value.matches("\\d{4}-\\d{1,2}")) {
            String[] parts = value.split("-");
            return YearMonth.of(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])).atEndOfMonth();
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
        private boolean valid;
        private String headerText;
        private String errorMessage;
        private int date = -1;
        private int treCode = -1;
        private int treasuryName = -1;
        private int taxOrg = -1;
        private int level = -1;
        private int subjectCode = -1;
        private int subjectName = -1;
        private int currentAmount = -1;
        private int yearAmount = -1;
        private int account = -1;
        private int debit = -1;
        private int credit = -1;
        private int balance = -1;

        static ColumnLayout detect(Row header, TimsBusinessType type,
                                   DataFormatter formatter, FormulaEvaluator evaluator) {
            ColumnLayout layout = new ColumnLayout();
            if (header == null) {
                layout.errorMessage = "第一个工作表缺少表头";
                return layout;
            }
            Map<String, Integer> columns = new HashMap<>();
            List<String> values = new ArrayList<>();
            for (int i = 0; i < header.getLastCellNum(); i++) {
                Cell cell = header.getCell(i);
                String value = cell == null ? "" : formatter.formatCellValue(cell, evaluator).trim();
                values.add(value);
                columns.put(normalize(value), i);
            }
            layout.headerText = values.toString();
            layout.date = find(columns, "日期", "账期", "d_acct");
            layout.treCode = find(columns, "国库代码", "trecode");
            layout.treasuryName = find(columns, "国库简称", "国库名称", "tername", "tredscr");
            layout.level = find(columns, "预算级次", "级次", "level");

            if (type == TimsBusinessType.INCOME || type == TimsBusinessType.PAYOUT) {
                layout.taxOrg = find(columns, "征收机关", "征收机关代码", "tax_org_code", "taxorgcode");
                layout.subjectCode = find(columns, "科目代码", "subject_code");
                layout.subjectName = find(columns, "科目名称", "subject_name", "subject_dscr");
                layout.currentAmount = find(columns, "本期执行数", "本期金额", "this_amt", "f_amt");
                layout.yearAmount = find(columns, "年累计", "年累计金额", "year_amt");
                layout.valid = allPresent(layout.date, layout.treCode, layout.treasuryName, layout.level,
                        layout.subjectCode, layout.subjectName, layout.currentAmount, layout.yearAmount);
            } else {
                layout.account = find(columns, "账户", "account");
                layout.debit = find(columns, "借方", "借方金额", "debit_amount", "f_debitamt");
                layout.credit = find(columns, "贷方", "贷方金额", "credit_amount", "f_loanamt");
                layout.balance = find(columns, "余额", "balance", "f_balance");
                layout.valid = allPresent(layout.date, layout.treCode, layout.treasuryName, layout.level,
                        layout.debit, layout.credit, layout.balance);
            }
            if (!layout.valid) {
                layout.errorMessage = "表头不符合 " + type.getDescription() + " 文件要求：" + layout.headerText;
            }
            return layout;
        }

        boolean isBlank(Row row, DataFormatter formatter, FormulaEvaluator evaluator) {
            for (int column : Arrays.asList(date, treCode, treasuryName, taxOrg, level, subjectCode,
                    subjectName, currentAmount, yearAmount, account, debit, credit, balance)) {
                if (column >= 0) {
                    Cell cell = row.getCell(column);
                    if (cell != null && !formatter.formatCellValue(cell, evaluator).trim().isEmpty()) return false;
                }
            }
            return true;
        }

        private static int find(Map<String, Integer> columns, String... aliases) {
            for (String alias : aliases) {
                Integer index = columns.get(normalize(alias));
                if (index != null) return index;
            }
            return -1;
        }

        private static boolean allPresent(int... indices) {
            for (int index : indices) if (index < 0) return false;
            return true;
        }

        private static String normalize(String value) {
            return value == null ? "" : value.trim().replace(" ", "").toLowerCase(Locale.ROOT);
        }
    }
}
