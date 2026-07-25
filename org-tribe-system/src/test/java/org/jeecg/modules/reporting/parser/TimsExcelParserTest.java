package org.jeecg.modules.reporting.parser;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TimsExcelParserTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void parsesProvidedIncomeLayoutAndNormalizesMonthToLastDay() throws Exception {
        Path file = workbook("收入1.xls",
                new String[]{"日期", "国库代码", "国库简称", "征收机关", "预算级次", "科目代码", "科目名称", "本期执行数", "年累计", " "},
                new String[]{"202511", "2200000000", "重庆市", "1111111111", "1", "110", "转移性收入", "-4509", "-6872", " "});

        TimsExcelParseResult result = new TimsExcelParser().parse(file, TimsBusinessType.INCOME);

        assertTrue(result.getErrors().isEmpty());
        assertEquals(1, result.getRecords().size());
        TimsReportRecord row = result.getRecords().get(0);
        assertEquals(LocalDate.of(2025, 11, 30), row.getDAcct());
        assertEquals("2200000000", row.getTreCode());
        assertEquals("1111111111", row.getTaxOrgCode());
        assertEquals("1", row.getLevel());
        assertEquals("110", row.getSubjectCode());
        assertEquals(new BigDecimal("-4509"), row.getCurrentAmount());
        assertEquals(new BigDecimal("-6872"), row.getYearAmount());
    }

    @Test
    public void supportsBothJarStockLayoutsWithOrWithoutAccountColumn() throws Exception {
        Path detailed = workbook("库存明细.xls",
                new String[]{"日期", "国库代码", "国库简称", "预算级次", "账户", "借方", "贷方", "余额"},
                new String[]{"202511", "2200000000", "重庆市", "1", "AC01", "10", "3", "7"});
        Path stg = workbook("库存汇总.xls",
                new String[]{"日期", "国库代码", "国库简称", "预算级次", "借方", "贷方", "余额"},
                new String[]{"202511", "2200000000", "重庆市", "1", "20", "4", "16"});

        TimsReportRecord detailedRow = new TimsExcelParser().parse(detailed, TimsBusinessType.STOCK)
                .getRecords().get(0);
        TimsReportRecord stgRow = new TimsExcelParser().parse(stg, TimsBusinessType.STOCK)
                .getRecords().get(0);

        assertEquals("AC01", detailedRow.getAccount());
        assertEquals(new BigDecimal("10"), detailedRow.getDebitAmount());
        assertEquals(null, stgRow.getAccount());
        assertEquals(new BigDecimal("20"), stgRow.getDebitAmount());
    }

    @Test
    public void malformedHeaderAndAmountProduceTraceableErrors() throws Exception {
        Path badHeader = workbook("错误表头.xls",
                new String[]{"错误日期", "国库代码"},
                new String[]{"202511", "2200000000"});
        Path badAmount = workbook("错误金额.xls",
                new String[]{"日期", "国库代码", "国库简称", "预算级次", "科目代码", "科目名称", "本期执行数", "年累计"},
                new String[]{"202511", "2200000000", "重庆市", "1", "101", "税收收入", "不是金额", "24"});

        TimsExcelParseResult headerResult = new TimsExcelParser().parse(badHeader, TimsBusinessType.PAYOUT);
        TimsExcelParseResult amountResult = new TimsExcelParser().parse(badAmount, TimsBusinessType.PAYOUT);

        assertEquals(1, headerResult.getErrors().size());
        assertEquals("错误表头.xls", headerResult.getErrors().get(0).getFileName());
        assertEquals(1L, headerResult.getErrors().get(0).getRowNumber());
        assertEquals(1, amountResult.getErrors().size());
        assertEquals(2L, amountResult.getErrors().get(0).getRowNumber());
        assertTrue(amountResult.getErrors().get(0).getMessage().contains("本期"));
    }

    private Path workbook(String name, String[] headers, String[] values) throws Exception {
        Path path = temporaryFolder.newFile(name).toPath();
        Workbook workbook = new HSSFWorkbook();
        try (FileOutputStream output = new FileOutputStream(path.toFile())) {
            Sheet sheet = workbook.createSheet("数据");
            Row header = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) header.createCell(i).setCellValue(headers[i]);
            Row row = sheet.createRow(1);
            for (int i = 0; i < values.length; i++) row.createCell(i).setCellValue(values[i]);
            workbook.write(output);
        }
        return path;
    }
}
