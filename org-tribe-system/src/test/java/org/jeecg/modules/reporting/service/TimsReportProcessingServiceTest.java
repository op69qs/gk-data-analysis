package org.jeecg.modules.reporting.service;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecg.modules.reporting.mapper.TimsReportMapper;
import org.jeecg.modules.reporting.parser.TimsBusinessType;
import org.jeecg.modules.reporting.parser.TimsReportRecord;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.ArgumentCaptor;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.YearMonth;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class TimsReportProcessingServiceTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void recursivelyWritesOnlyStgUsingJarPeriodKeyAndRawBusinessDate() throws Exception {
        Path root = temporaryFolder.newFolder("extract").toPath();
        Path nested = Files.createDirectories(root.resolve("收入/二级"));
        workbook(nested.resolve("收入1.xls"),
                new String[]{"日期", "国库代码", "国库简称", "征收机关", "预算级次", "科目代码", "科目名称", "本期执行数", "年累计"},
                new String[]{"202511", "2200000000", "重庆市", "1111111111", "1", "110", "转移性收入", "-4509", "-6872"});

        TimsReportMapper mapper = mock(TimsReportMapper.class);
        when(mapper.insertStgIncome(anyList(), org.mockito.ArgumentMatchers.eq("202511"), anyString()))
                .thenReturn(1);
        when(mapper.countStgIncome("202511")).thenReturn(1L);
        TimsReportProcessingResult result = new TimsReportProcessingService(mapper)
                .process(root, TimsBusinessType.INCOME, YearMonth.of(2025, 11));

        ArgumentCaptor<List<TimsReportRecord>> rows = ArgumentCaptor.forClass(List.class);
        verify(mapper).deleteStgIncome("202511");
        verify(mapper).insertStgIncome(rows.capture(), org.mockito.ArgumentMatchers.eq("202511"), anyString());
        assertEquals(1, rows.getValue().size());
        assertEquals("202511", rows.getValue().get(0).getDAcctText());
        assertEquals(1, result.getFileCount());
        assertEquals(1, result.getSuccessCount());
        assertEquals(0, result.getErrorCount());
    }

    @Test
    public void invalidRowDoesNotDeleteExistingPeriodData() throws Exception {
        Path root = temporaryFolder.newFolder("invalid").toPath();
        workbook(root.resolve("收入错误.xls"),
                new String[]{"日期", "国库代码", "国库简称", "征收机关", "预算级次", "科目代码", "科目名称", "本期执行数", "年累计"},
                new String[]{"202511", "2200000000", "重庆市", "1111111111", "1", "110", "转移性收入", "错误金额", "-6872"});
        TimsReportMapper mapper = mock(TimsReportMapper.class);

        TimsReportProcessingResult result = new TimsReportProcessingService(mapper)
                .process(root, TimsBusinessType.INCOME, YearMonth.of(2025, 11));

        assertEquals(1, result.getErrorCount());
        verifyZeroInteractions(mapper);
    }

    @Test
    public void noExcelFileIsAnErrorAndNeverTouchesExistingData() throws Exception {
        Path root = temporaryFolder.newFolder("no-excel").toPath();
        Files.write(root.resolve("readme.txt"), java.util.Collections.singletonList("nothing"));
        TimsReportMapper mapper = mock(TimsReportMapper.class);

        TimsReportProcessingResult result = new TimsReportProcessingService(mapper)
                .process(root, TimsBusinessType.INCOME, YearMonth.of(2025, 11));

        assertEquals(1, result.getErrorCount());
        assertEquals(0, result.getSuccessCount());
        verifyZeroInteractions(mapper);
    }

    @Test
    public void headerOnlyWorkbookIsAnErrorAndNeverTouchesExistingData() throws Exception {
        Path root = temporaryFolder.newFolder("header-only").toPath();
        workbook(root.resolve("收入1.xls"),
                new String[]{"日期", "国库代码", "国库简称", "征收机关", "预算级次", "科目代码", "科目名称", "本期执行数", "年累计"},
                null);
        TimsReportMapper mapper = mock(TimsReportMapper.class);

        TimsReportProcessingResult result = new TimsReportProcessingService(mapper)
                .process(root, TimsBusinessType.INCOME, YearMonth.of(2025, 11));

        assertEquals(1, result.getErrorCount());
        verifyZeroInteractions(mapper);
    }

    @Test
    public void authenticatedTreasuryPrefixDoesNotFilterAggregateJarFile() throws Exception {
        Path root = temporaryFolder.newFolder("outside-scope").toPath();
        workbook(root.resolve("收入1.xls"),
                new String[]{"日期", "国库代码", "国库简称", "征收机关", "预算级次", "科目代码", "科目名称", "本期执行数", "年累计"},
                new String[]{"202511", "5000000000", "其他国库", "111", "1", "110", "收入", "1", "2"});
        TimsReportMapper mapper = mock(TimsReportMapper.class);
        when(mapper.insertStgIncome(anyList(), org.mockito.ArgumentMatchers.eq("202511"), anyString()))
                .thenReturn(1);
        when(mapper.countStgIncome("202511")).thenReturn(1L);

        TimsReportProcessingResult result = new TimsReportProcessingService(mapper)
                .process(root, TimsBusinessType.INCOME, YearMonth.of(2025, 11), "2201");

        assertEquals(0, result.getErrorCount());
        assertEquals(1, result.getSuccessCount());
        verify(mapper).deleteStgIncome("202511");
    }

    private void workbook(Path path, String[] headers, String[] values) throws Exception {
        Workbook workbook = new HSSFWorkbook();
        try (FileOutputStream output = new FileOutputStream(path.toFile())) {
            Sheet sheet = workbook.createSheet("收入数据");
            Row header = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) header.createCell(i).setCellValue(headers[i]);
            if (values != null) {
                Row row = sheet.createRow(1);
                for (int i = 0; i < values.length; i++) row.createCell(i).setCellValue(values[i]);
            }
            workbook.write(output);
        }
    }
}
