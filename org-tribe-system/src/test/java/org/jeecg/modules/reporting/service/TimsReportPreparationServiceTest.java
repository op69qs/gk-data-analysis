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
