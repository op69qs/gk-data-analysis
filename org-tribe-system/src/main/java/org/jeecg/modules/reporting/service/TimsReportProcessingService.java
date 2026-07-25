package org.jeecg.modules.reporting.service;

import org.jeecg.modules.reporting.mapper.TimsReportMapper;
import org.jeecg.modules.reporting.parser.TimsBusinessType;
import org.jeecg.modules.reporting.parser.TimsExcelParseError;
import org.jeecg.modules.reporting.parser.TimsExcelParseResult;
import org.jeecg.modules.reporting.parser.TimsExcelParser;
import org.jeecg.modules.reporting.parser.TimsReportRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class TimsReportProcessingService {
    private static final DateTimeFormatter PERIOD_FORMAT = DateTimeFormatter.ofPattern("yyyyMM");
    private static final DateTimeFormatter BATCH_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final TimsReportMapper mapper;
    private final TimsExcelParser parser;

    public TimsReportProcessingService(TimsReportMapper mapper) {
        this(mapper, new TimsExcelParser());
    }

    TimsReportProcessingService(TimsReportMapper mapper, TimsExcelParser parser) {
        this.mapper = mapper;
        this.parser = parser;
    }

    @Transactional(rollbackFor = Exception.class)
    public TimsReportProcessingResult process(Path extractRoot, TimsBusinessType type,
                                              YearMonth accountingPeriod) throws IOException {
        return process(extractRoot, type, accountingPeriod, null);
    }

    @Transactional(rollbackFor = Exception.class)
    public TimsReportProcessingResult process(Path extractRoot, TimsBusinessType type,
                                              YearMonth accountingPeriod,
                                              String allowedTreasuryPrefix) throws IOException {
        List<Path> files = findExcelFiles(extractRoot);
        List<TimsReportRecord> rows = new ArrayList<>();
        List<TimsExcelParseError> errors = new ArrayList<>();
        if (files.isEmpty()) {
            errors.add(packageError("压缩包中没有 Excel 文件"));
        }
        for (Path file : files) {
            if (hasRecognizableTypeConflict(file, type)) {
                errors.add(new TimsExcelParseError(file.getFileName().toString(), null, 0,
                        "文件类型", file.getFileName().toString(), "文件名与选择的上报类型不一致"));
                continue;
            }
            TimsExcelParseResult parsed = parser.parse(file, type);
            rows.addAll(parsed.getRecords());
            errors.addAll(parsed.getErrors());
        }

        if (!files.isEmpty() && rows.isEmpty() && errors.isEmpty()) {
            errors.add(packageError("Excel 中没有有效数据行"));
        }

        List<TimsReportRecord> accepted = new ArrayList<>();
        for (TimsReportRecord row : rows) {
            if (allowedTreasuryPrefix != null && !row.getTreCode().startsWith(allowedTreasuryPrefix)) {
                errors.add(new TimsExcelParseError(row.getFileName(), row.getSheetName(), row.getRowNumber(),
                        "国库代码", row.getTreCode(), "国库超出当前用户数据范围"));
            } else if (!YearMonth.from(row.getDAcct()).equals(accountingPeriod)) {
                errors.add(new TimsExcelParseError(row.getFileName(), row.getSheetName(), row.getRowNumber(),
                        "日期", row.getDAcct().toString(), "文件账期与本次上报账期 " + accountingPeriod + " 不一致"));
            } else {
                accepted.add(row);
            }
        }

        // 有格式错误时不替换既有数据，避免旧任务“先删后报错”造成整个账期数据被清空。
        if (errors.isEmpty() && !accepted.isEmpty()) {
            replaceIntermediate(type, accepted);
            replaceStg(type, accepted, accountingPeriod.format(PERIOD_FORMAT), LocalDate.now().format(BATCH_FORMAT));
        }
        return new TimsReportProcessingResult(files.size(), accepted.size(), errors, treasuryCounts(accepted));
    }

    private TimsExcelParseError packageError(String message) {
        return new TimsExcelParseError("<ZIP>", null, 0, null, null, message);
    }

    private boolean hasRecognizableTypeConflict(Path file, TimsBusinessType expected) {
        String name = file.getFileName().toString();
        TimsBusinessType actual = name.contains("收入") ? TimsBusinessType.INCOME
                : name.contains("支出") ? TimsBusinessType.PAYOUT
                : name.contains("库存") ? TimsBusinessType.STOCK : null;
        return actual != null && actual != expected;
    }

    private List<TimsReportProcessingResult.TreasuryCount> treasuryCounts(List<TimsReportRecord> rows) {
        Map<String, TimsReportProcessingResult.TreasuryCount> counts = new LinkedHashMap<>();
        for (TimsReportRecord row : rows) {
            String key = row.getDatabaseDate() + "|" + row.getTreCode();
            TimsReportProcessingResult.TreasuryCount previous = counts.get(key);
            counts.put(key, new TimsReportProcessingResult.TreasuryCount(
                    row.getDatabaseDate(), row.getTreCode(), previous == null ? 1 : previous.getRowCount() + 1));
        }
        return new ArrayList<>(counts.values());
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

    private void replaceIntermediate(TimsBusinessType type, List<TimsReportRecord> rows) {
        Set<Date> dateSet = new LinkedHashSet<>();
        Set<String> treasurySet = new LinkedHashSet<>();
        for (TimsReportRecord row : rows) {
            dateSet.add(row.getDatabaseDate());
            treasurySet.add(row.getTreCode());
        }
        List<Date> dates = new ArrayList<>(dateSet);
        List<String> treCodes = new ArrayList<>(treasurySet);
        switch (type) {
            case INCOME:
                mapper.deleteTimsIncome(dates, treCodes);
                mapper.insertTimsIncome(rows);
                break;
            case PAYOUT:
                mapper.deleteTimsPayout(dates, treCodes);
                mapper.insertTimsPayout(rows);
                break;
            case STOCK:
                mapper.deleteTimsStock(dates, treCodes);
                mapper.insertTimsStock(rows);
                break;
            default:
                throw new IllegalArgumentException("不支持的 TIMS 业务类型：" + type);
        }
    }

    private void replaceStg(TimsBusinessType type, List<TimsReportRecord> rows,
                            String periodKey, String batchDate) {
        switch (type) {
            case INCOME:
                mapper.deleteStgIncome(periodKey);
                mapper.insertStgIncome(rows, periodKey, batchDate);
                break;
            case PAYOUT:
                mapper.deleteStgPayout(periodKey);
                mapper.insertStgPayout(rows, periodKey, batchDate);
                break;
            case STOCK:
                mapper.deleteStgStock(periodKey);
                mapper.insertStgStock(rows, periodKey, batchDate);
                break;
            default:
                throw new IllegalArgumentException("不支持的 TIMS 业务类型：" + type);
        }
    }
}
