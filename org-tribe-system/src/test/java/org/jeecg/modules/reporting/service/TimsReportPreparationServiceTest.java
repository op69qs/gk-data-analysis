package org.jeecg.modules.reporting.service;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecg.modules.reporting.parser.TimsBusinessType;
import org.jeecg.modules.reporting.parser.TimsReportRecord;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TimsReportPreparationServiceTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void preparesMultipleWorkbooksIntoOnePrivateSpoolInSortedOrder() throws Exception {
        Path extracted = temporaryFolder.newFolder("extracted").toPath();
        Path nested = Files.createDirectories(extracted.resolve("收入"));
        workbook(nested.resolve("收入2.xls"), "2200200000");
        workbook(nested.resolve("收入1.xls"), "2200100000");
        workbook(nested.resolve("收入3.xls"), "2200300000");
        Path work = temporaryFolder.newFolder("work").toPath();

        try (TimsPreparationResult result = new TimsReportPreparationService()
                .prepare(extracted, work, TimsBusinessType.INCOME, YearMonth.of(2025, 11))) {
            assertTrue(result.getErrors().isEmpty());
            assertEquals(3, result.getFileCount());
            assertEquals(3L, result.getRowCount());
            assertEquals(1L, result.getFileStats().get("收入1.xls").getSuccessRowCount());
            assertEquals(1L, result.getFileStats().get("收入2.xls").getSuccessRowCount());
            assertEquals(1L, result.getFileStats().get("收入3.xls").getSuccessRowCount());
            assertTrue(result.getSpool().getPath().startsWith(work));
            assertTrue(Files.exists(result.getSpool().getPath()));
            assertEquals(64, result.getSpool().getSha256().length());

            List<String> treCodes = new ArrayList<>();
            result.getSpool().readBatches(2, rows -> {
                assertTrue(rows.size() <= 2);
                for (TimsReportRecord row : rows) treCodes.add(row.getTreCode());
            });
            assertEquals(java.util.Arrays.asList("2200100000", "2200200000", "2200300000"), treCodes);
        }
        assertEquals(0L, Files.list(work).count());
    }

    @Test
    public void wrongPeriodDeletesSpoolAndReturnsTraceableError() throws Exception {
        Path extracted = temporaryFolder.newFolder("wrong-period").toPath();
        workbook(extracted.resolve("收入1.xls"), "2200000000");
        Path work = temporaryFolder.newFolder("wrong-period-work").toPath();

        TimsPreparationResult result = new TimsReportPreparationService()
                .prepare(extracted, work, TimsBusinessType.INCOME, YearMonth.of(2025, 12));

        assertFalse(result.getErrors().isEmpty());
        assertEquals(0L, Files.list(work).count());
        result.close();
    }

    @Test
    public void flashIncomeUsesFormPeriodAndRejectsWhenSelectedAsIncome() throws Exception {
        Path extracted = temporaryFolder.newFolder("flash").toPath();
        flashWorkbook(extracted.resolve("快报_收入数据.xls"));
        Path work = temporaryFolder.newFolder("flash-work").toPath();

        try (TimsPreparationResult ok = new TimsReportPreparationService()
                .prepare(extracted, work, TimsBusinessType.FLASH_INCOME, YearMonth.of(2026, 7))) {
            assertTrue(ok.getErrors().isEmpty());
            assertEquals(1L, ok.getRowCount());
            List<TimsReportRecord> rows = new ArrayList<>();
            ok.getSpool().readBatches(10, rows::addAll);
            assertEquals(LocalDate.of(2026, 7, 1), rows.get(0).getDAcct());
            assertEquals("202607", rows.get(0).getDAcctText());
        }

        TimsPreparationResult conflict = new TimsReportPreparationService()
                .prepare(extracted, temporaryFolder.newFolder("flash-conflict").toPath(),
                        TimsBusinessType.INCOME, YearMonth.of(2026, 7));
        assertFalse(conflict.getErrors().isEmpty());
        assertTrue(conflict.getErrors().get(0).getMessage().contains("不一致"));
        conflict.close();
    }

    private void flashWorkbook(Path path) throws Exception {
        Workbook workbook = new HSSFWorkbook();
        try (FileOutputStream output = new FileOutputStream(path.toFile())) {
            Sheet sheet = workbook.createSheet("收入数据");
            Row header = sheet.createRow(0);
            String[] headers = {"国库代码", "国库简称", "科目代码", "科目名称", "本期执行数", "年累计"};
            for (int i = 0; i < headers.length; i++) header.createCell(i).setCellValue(headers[i]);
            Row row = sheet.createRow(1);
            String[] values = {"2200000000", "重庆市分库", "101", "税收收入", "10", "20"};
            for (int i = 0; i < values.length; i++) row.createCell(i).setCellValue(values[i]);
            workbook.write(output);
        }
    }

    private void workbook(Path path, String treasuryCode) throws Exception {
        Workbook workbook = new HSSFWorkbook();
        try (FileOutputStream output = new FileOutputStream(path.toFile())) {
            Sheet sheet = workbook.createSheet("收入数据");
            Row header = sheet.createRow(0);
            for (int i = 0; i < 9; i++) header.createCell(i).setCellValue("列" + i);
            Row row = sheet.createRow(1);
            String[] values = {"202511", treasuryCode, "重庆市", "111", "1", "101", "收入", "10", "20"};
            for (int i = 0; i < values.length; i++) row.createCell(i).setCellValue(values[i]);
            workbook.write(output);
        }
    }
}
