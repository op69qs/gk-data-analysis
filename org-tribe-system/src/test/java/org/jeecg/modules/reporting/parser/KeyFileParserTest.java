package org.jeecg.modules.reporting.parser;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class KeyFileParserTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void jarFileNameCodesMapToFourBusinessTypes() {
        assertEquals(KeyFileType.INCOME, KeyFileType.detect("5000_sr.txt").get());
        assertEquals(KeyFileType.PAYOUT, KeyFileType.detect("5000_zc.txt").get());
        assertEquals(KeyFileType.STOCK, KeyFileType.detect("5000_kc.txt").get());
        assertEquals(KeyFileType.BACK, KeyFileType.detect("5000_tk.txt").get());
        assertFalse(KeyFileType.detect("5000_SR.TXT").isPresent());
    }

    @Test
    public void parsesJarFieldOrderForEveryKeyType() throws Exception {
        KeyFileParser parser = new KeyFileParser();

        KeyReportRecord income = onlyRecord(parser, KeyFileType.INCOME,
                "2026-07-31\t5000\t101\tTAX\tB\t1\t12.30\t100.00");
        assertEquals("101", income.getSubjectCode());
        assertEquals("TAX", income.getTaxOrgCode());
        assertEquals("B", income.getBudgetType());
        assertEquals(new BigDecimal("12.30"), income.getFAmount());

        KeyReportRecord payout = onlyRecord(parser, KeyFileType.PAYOUT,
                "2026-07-31\t5000\t201\tORG\tC\t2\t20.00\t220.00");
        assertEquals("C", payout.getCodeType());
        assertEquals(new BigDecimal("220.00"), payout.getYearAmount());

        KeyReportRecord stock = onlyRecord(parser, KeyFileType.STOCK,
                "2026-07-31\t5000\t3\tAC01\t300.00\t250.00");
        assertEquals("AC01", stock.getAccountCode());
        assertEquals(new BigDecimal("300.00"), stock.getFBalance());
        assertEquals(new BigDecimal("250.00"), stock.getYearInitialBalance());

        KeyReportRecord back = onlyRecord(parser, KeyFileType.BACK,
                "2026-07-31\t5000\t301\tB\t4\tTAX2\t退库原因\t30.00\t330.00");
        assertEquals("退库原因", back.getBackReason());
        assertEquals(new BigDecimal("30.00"), back.getFAmount());
    }

    @Test
    public void insufficientFieldsProduceFileAndLineErrorWithoutDroppingGoodRows() throws Exception {
        Path file = write("5000_sr.txt", Arrays.asList(
                "2026-07-31\t5000\t101\tTAX\tB\t1\t12.30\t100.00",
                "2026-07-31\t5000\ttoo-short"
        ));

        KeyFileParseResult result = new KeyFileParser().parse(file, KeyFileType.INCOME, "key.zip");

        assertEquals(1, result.getRecords().size());
        assertEquals(1, result.getErrors().size());
        assertEquals("5000_sr.txt", result.getErrors().get(0).getFileName());
        assertEquals(2L, result.getErrors().get(0).getLineNumber());
        assertTrue(result.getErrors().get(0).getMessage().contains("8"));
    }

    private KeyReportRecord onlyRecord(KeyFileParser parser, KeyFileType type, String line) throws Exception {
        Path file = write("5000_" + type.getFileCode() + ".txt", Arrays.asList(line));
        KeyFileParseResult result = parser.parse(file, type, "key.zip");
        assertTrue(result.getErrors().isEmpty());
        assertEquals(1, result.getRecords().size());
        assertEquals("key.zip", result.getRecords().get(0).getKeyZipName());
        return result.getRecords().get(0);
    }

    private Path write(String name, Iterable<String> lines) throws Exception {
        Path file = temporaryFolder.newFile(name).toPath();
        Files.write(file, lines, StandardCharsets.UTF_8);
        return file;
    }
}
