package org.jeecg.modules.reporting.service;

import org.jeecg.modules.reporting.parser.TimsBusinessType;
import org.jeecg.modules.reporting.parser.TimsExcelParseError;
import org.jeecg.modules.reporting.parser.TimsExcelParseResult;
import org.jeecg.modules.reporting.parser.TimsExcelParser;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TimsReportPreparationService {
    private final TimsExcelParser parser;

    public TimsReportPreparationService() {
        this(new TimsExcelParser());
    }

    TimsReportPreparationService(TimsExcelParser parser) {
        this.parser = parser;
    }

    public TimsPreparationResult prepare(Path extractRoot, Path workRoot,
                                         TimsBusinessType type, YearMonth period) throws IOException {
        List<Path> files = findExcelFiles(extractRoot);
        List<TimsExcelParseError> errors = new ArrayList<>();
        Map<String, MutableFileStat> fileStats = new LinkedHashMap<>();
        if (files.isEmpty()) {
            errors.add(packageError("压缩包中没有 Excel 文件"));
            return new TimsPreparationResult(0, 0, errors, java.util.Collections.emptyMap(), null);
        }

        try (TimsSpool.Writer writer = TimsSpool.create(workRoot)) {
            long[] rows = {0};
            for (Path file : files) {
                String fileName = file.getFileName().toString();
                MutableFileStat stat = fileStats.computeIfAbsent(fileName, key -> new MutableFileStat());
                if (hasRecognizableTypeConflict(file, type)) {
                    stat.errorRowCount++;
                    errors.add(new TimsExcelParseError(fileName, null, 0,
                            "文件类型", fileName, "文件名与选择的上报类型不一致"));
                    continue;
                }
                TimsExcelParseResult parsed = parser.parse(file, type, row -> {
                    if (!YearMonth.from(row.getDAcct()).equals(period)) {
                        stat.errorRowCount++;
                        errors.add(new TimsExcelParseError(row.getFileName(), row.getSheetName(), row.getRowNumber(),
                                "日期", row.getDAcctText(), "文件账期与本次上报账期 " + period + " 不一致"));
                        return;
                    }
                    try {
                        writer.write(row);
                        rows[0]++;
                        stat.successRowCount++;
                    } catch (IOException exception) {
                        throw new UncheckedIOException(exception);
                    }
                });
                for (TimsExcelParseError error : parsed.getErrors()) {
                    stat.errorRowCount++;
                    errors.add(error);
                }
            }
            if (rows[0] == 0 && errors.isEmpty()) errors.add(packageError("Excel 中没有有效数据行"));
            if (!errors.isEmpty()) {
                return new TimsPreparationResult(files.size(), rows[0], errors, freeze(fileStats), null);
            }
            TimsSpool spool = writer.finish();
            return new TimsPreparationResult(files.size(), rows[0], errors, freeze(fileStats), spool);
        }
    }

    private Map<String, TimsPreparationResult.FileStat> freeze(Map<String, MutableFileStat> fileStats) {
        Map<String, TimsPreparationResult.FileStat> result = new LinkedHashMap<>();
        for (Map.Entry<String, MutableFileStat> entry : fileStats.entrySet()) {
            MutableFileStat value = entry.getValue();
            result.put(entry.getKey(), new TimsPreparationResult.FileStat(
                    value.successRowCount, value.errorRowCount));
        }
        return result;
    }

    private List<Path> findExcelFiles(Path root) throws IOException {
        try (Stream<Path> paths = Files.walk(root)) {
            return paths.filter(Files::isRegularFile)
                    .filter(path -> {
                        String name = path.getFileName().toString();
                        return name.endsWith(".xls") || name.endsWith(".xlsx");
                    })
                    .sorted(Comparator.comparing(Path::toString))
                    .collect(Collectors.toList());
        }
    }

    private boolean hasRecognizableTypeConflict(Path file, TimsBusinessType expected) {
        String name = file.getFileName().toString();
        TimsBusinessType actual = name.contains("收入") ? TimsBusinessType.INCOME
                : name.contains("支出") ? TimsBusinessType.PAYOUT
                : name.contains("库存") ? TimsBusinessType.STOCK : null;
        return actual != null && actual != expected;
    }

    private TimsExcelParseError packageError(String message) {
        return new TimsExcelParseError("<ZIP>", null, 0, null, null, message);
    }

    private static final class MutableFileStat {
        private long successRowCount;
        private long errorRowCount;
    }
}
