package org.jeecg.modules.reporting.service;

import org.jeecg.modules.reporting.parser.TimsBusinessType;
import org.jeecg.modules.reporting.parser.TimsExcelParser;
import org.jeecg.modules.reporting.util.SafeZipExtractor;
import org.junit.Assume;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/** Optional local regression: samples remain outside Git and are only read when explicitly supplied. */
public class TimsProvidedSamplesTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void parsesEveryProvidedIncomePayoutAndStockWorkbook() throws Exception {
        String configured = System.getProperty("reporting.samples");
        Assume.assumeTrue("Set -Dreporting.samples=<directory> for the local sanitized ZIP regression",
                configured != null && Files.isDirectory(Paths.get(configured)));

        Map<String, Sample> samples = new LinkedHashMap<>();
        samples.put("收入.zip", new Sample(TimsBusinessType.INCOME, 4, 0));
        samples.put("支出.zip", new Sample(TimsBusinessType.PAYOUT, 2, 0));
        // One stock row deliberately contains an N-only redacted treasury name; the JAR rule must reject it.
        samples.put("库存.zip", new Sample(TimsBusinessType.STOCK, 3, 1));

        SafeZipExtractor extractor = new SafeZipExtractor(2000, 500L * 1024 * 1024, 100L * 1024 * 1024);
        for (Map.Entry<String, Sample> entry : samples.entrySet()) {
            Path zip = Paths.get(configured).resolve(entry.getKey());
            assertTrue("Missing sanitized sample " + entry.getKey(), Files.isRegularFile(zip));
            Path extractRoot = temporaryFolder.newFolder(entry.getValue().type.name()).toPath();
            extractor.extract(zip, extractRoot);
            Path workRoot = temporaryFolder.newFolder(entry.getValue().type.name() + "-work").toPath();
            try (TimsPreparationResult result = new TimsReportPreparationService()
                    .prepare(extractRoot, workRoot, entry.getValue().type, YearMonth.of(2025, 11))) {
                String errors = result.getErrors().stream()
                        .map(error -> error.getFileName() + ":" + error.getSheetName() + ":"
                                + error.getRowNumber() + ":" + error.getColumnName() + ":"
                                + error.getRawValue() + ":" + error.getMessage())
                        .collect(Collectors.joining(" | "));
                assertEquals(entry.getKey() + " errors: " + errors,
                        entry.getValue().expectedRedactionErrors, result.getErrors().size());
                assertEquals(entry.getValue().excelCount, result.getFileCount());
                assertTrue(entry.getKey() + " must contain data rows", result.getRowCount() > 0);
                if (entry.getValue().type == TimsBusinessType.STOCK) {
                    assertTrue(result.getErrors().get(0).getRawValue().matches("N+"));
                    final boolean[] exactDateSeen = {false};
                    try (Stream<Path> paths = Files.walk(extractRoot)) {
                        for (Path file : paths.filter(Files::isRegularFile)
                                .filter(path -> path.getFileName().toString().endsWith(".xls"))
                                .collect(Collectors.toList())) {
                            new TimsExcelParser().parse(file, TimsBusinessType.STOCK, row -> {
                                if (row.getDAcctText() != null
                                        && row.getDAcctText().matches("\\d{4}-\\d{2}-\\d{2}")) {
                                    exactDateSeen[0] = true;
                                }
                            });
                        }
                    }
                    assertTrue("库存原始具体日期必须保留", exactDateSeen[0]);
                }
            }
        }
    }

    private static final class Sample {
        private final TimsBusinessType type;
        private final int excelCount;
        private final int expectedRedactionErrors;

        private Sample(TimsBusinessType type, int excelCount, int expectedRedactionErrors) {
            this.type = type;
            this.excelCount = excelCount;
            this.expectedRedactionErrors = expectedRedactionErrors;
        }
    }
}
