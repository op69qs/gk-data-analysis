package org.jeecg.modules.reporting.service;

import org.jeecg.modules.reporting.mapper.KeyReportMapper;
import org.jeecg.modules.reporting.parser.KeyReportRecord;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.ArgumentCaptor;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class KeyReportProcessingServiceTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void recursivelyReplacesEachDetectedTypeAndKeepsRowErrors() throws Exception {
        Path root = temporaryFolder.newFolder("extract").toPath();
        Path nested = Files.createDirectories(root.resolve("收入/二级"));
        write(nested.resolve("5000_sr.txt"),
                "2026-07-31\t5000\t101\tTAX\tB\t1\t12.30\t100.00",
                "bad\trow");
        write(root.resolve("5000_zc.txt"),
                "2026-07-31\t5000\t201\tORG\tC\t2\t20.00\t220.00");
        write(root.resolve("5000_kc.txt"),
                "2026-07-31\t5000\t3\tAC01\t300.00\t250.00");
        write(root.resolve("5000_tk.txt"),
                "2026-07-31\t5000\t301\tB\t4\tTAX2\t退库原因\t30.00\t330.00");
        write(root.resolve("ignored.TXT"), "不会被原JAR识别");

        KeyReportMapper mapper = mock(KeyReportMapper.class);
        KeyReportProcessingResult result = new KeyReportProcessingService(mapper).process(root, "key.zip");

        verify(mapper).deleteIncomeByZipName("key.zip");
        verify(mapper).deletePayoutByZipName("key.zip");
        verify(mapper).deleteStockByZipName("key.zip");
        verify(mapper).deleteBackByZipName("key.zip");

        ArgumentCaptor<List<KeyReportRecord>> incomeRows = ArgumentCaptor.forClass(List.class);
        verify(mapper, times(1)).insertIncome(incomeRows.capture());
        assertEquals(1, incomeRows.getValue().size());
        assertEquals(4, result.getSuccessCount());
        assertEquals(1, result.getErrorCount());
        assertEquals(1, result.getTypeResult("sr").getSuccessCount());
        assertEquals(1, result.getTypeResult("sr").getErrorCount());
        assertEquals("5000_sr.txt", result.getErrors().get(0).getFileName());
        assertEquals(2L, result.getErrors().get(0).getLineNumber());
    }

    @Test
    public void noRecognizedKeyFileIsAnErrorAndDoesNotDeleteOldData() throws Exception {
        Path root = temporaryFolder.newFolder("empty-key").toPath();
        write(root.resolve("readme.txt"), "ignored");
        KeyReportMapper mapper = mock(KeyReportMapper.class);

        KeyReportProcessingResult result = new KeyReportProcessingService(mapper).process(root, "key.zip");

        assertEquals(1, result.getErrorCount());
        verifyZeroInteractions(mapper);
    }

    @Test
    public void rowTreasuryMustMatchTreasuryDerivedFromKeyFileName() throws Exception {
        Path root = temporaryFolder.newFolder("key-scope").toPath();
        write(root.resolve("2200_sr.txt"),
                "2026-07-31\t5000\t101\tTAX\tB\t1\t12.30\t100.00");
        KeyReportMapper mapper = mock(KeyReportMapper.class);

        KeyReportProcessingResult result = new KeyReportProcessingService(mapper)
                .process(root, "k2026-07-31t2200.zip", "2200");

        assertEquals(1, result.getErrorCount());
        verifyZeroInteractions(mapper);
    }

    private void write(Path path, String... lines) throws Exception {
        Files.write(path, Arrays.asList(lines), StandardCharsets.UTF_8);
    }
}
