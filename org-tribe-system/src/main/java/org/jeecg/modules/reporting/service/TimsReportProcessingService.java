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
        List<Path> files = findExcelFiles(extractRoot);
        List<TimsReportRecord> rows = new ArrayList<>();
        List<TimsExcelParseError> errors = new ArrayList<>();
        for (Path file : files) {
            TimsExcelParseResult parsed = parser.parse(file, type);
            rows.addAll(parsed.getRecords());
            errors.addAll(parsed.getErrors());
        }

        List<TimsReportRecord> accepted = new ArrayList<>();
        for (TimsReportRecord row : rows) {
            if (!YearMonth.from(row.getDAcct()).equals(accountingPeriod)) {
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
        return new TimsReportProcessingResult(files.size(), accepted.size(), errors);
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
