package org.jeecg.modules.reporting.service;

import org.jeecg.modules.reporting.config.ReportingProperties;
import org.jeecg.modules.reporting.mapper.TimsReportMapper;
import org.jeecg.modules.reporting.parser.TimsBusinessType;
import org.jeecg.modules.reporting.parser.TimsReportRecord;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class TimsAtomicLoadService {
    private static final DateTimeFormatter PERIOD_FORMAT = DateTimeFormatter.ofPattern("yyyyMM");
    private final TimsReportMapper mapper;
    private final int batchSize;
    private final ReportRuntimeLockService runtimeLockService;

    @Autowired
    public TimsAtomicLoadService(TimsReportMapper mapper, ReportingProperties properties,
                                 ReportRuntimeLockService runtimeLockService) {
        this(mapper, properties.getTimsBatchSize(), runtimeLockService);
    }

    TimsAtomicLoadService(TimsReportMapper mapper, int batchSize) {
        this(mapper, batchSize, null);
    }

    private TimsAtomicLoadService(TimsReportMapper mapper, int batchSize,
                                  ReportRuntimeLockService runtimeLockService) {
        if (batchSize < 1 || batchSize > 1000) {
            throw new IllegalArgumentException("reporting.tims-batch-size 必须在 1 到 1000 之间");
        }
        this.mapper = mapper;
        this.batchSize = batchSize;
        this.runtimeLockService = runtimeLockService;
    }

    @Transactional(rollbackFor = Exception.class)
    public long load(TimsPreparationResult prepared, TimsBusinessType type,
                     YearMonth period, String batchDate) throws IOException {
        return load(prepared, type, period, batchDate, null, rows -> { });
    }

    @Transactional(rollbackFor = Exception.class)
    public long load(TimsPreparationResult prepared, TimsBusinessType type,
                     YearMonth period, String batchDate, String lockOwner) throws IOException {
        return load(prepared, type, period, batchDate, lockOwner, rows -> { });
    }

    @Transactional(rollbackFor = Exception.class)
    public long load(TimsPreparationResult prepared, TimsBusinessType type,
                     YearMonth period, String batchDate, String lockOwner,
                     TimsLoadCommitAction completion) throws IOException {
        if (prepared == null || prepared.getSpool() == null || !prepared.getErrors().isEmpty()) {
            throw new IllegalArgumentException("TIMS 全包解析成功后才能入库");
        }
        if (runtimeLockService != null) runtimeLockService.assertOwnedForUpdate(lockOwner);
        String periodKey = period.format(PERIOD_FORMAT);
        deletePeriod(type, periodKey);
        long[] inserted = {0};
        prepared.getSpool().readBatches(batchSize, rows -> {
            int affected = insert(type, rows, periodKey, batchDate);
            if (affected != rows.size()) {
                throw new IllegalStateException("TIMS 分批写入数量不一致，期望 " + rows.size() + "，实际 " + affected);
            }
            inserted[0] += affected;
        });
        long stored = countPeriod(type, periodKey);
        if (inserted[0] != prepared.getRowCount() || stored != prepared.getRowCount()) {
            throw new IllegalStateException("TIMS 入库核对失败，解析 " + prepared.getRowCount()
                    + "，写入 " + inserted[0] + "，库内 " + stored);
        }
        completion.afterCommittedRowsLoaded(stored);
        return stored;
    }

    private void deletePeriod(TimsBusinessType type, String periodKey) {
        switch (type) {
            case INCOME: mapper.deleteStgIncome(periodKey); break;
            case PAYOUT: mapper.deleteStgPayout(periodKey); break;
            case STOCK: mapper.deleteStgStock(periodKey); break;
            default: throw new IllegalArgumentException("不支持的 TIMS 业务类型：" + type);
        }
    }

    private int insert(TimsBusinessType type, List<TimsReportRecord> rows,
                       String periodKey, String batchDate) {
        switch (type) {
            case INCOME: return mapper.insertStgIncome(rows, periodKey, batchDate);
            case PAYOUT: return mapper.insertStgPayout(rows, periodKey, batchDate);
            case STOCK: return mapper.insertStgStock(rows, periodKey, batchDate);
            default: throw new IllegalArgumentException("不支持的 TIMS 业务类型：" + type);
        }
    }

    private long countPeriod(TimsBusinessType type, String periodKey) {
        switch (type) {
            case INCOME: return mapper.countStgIncome(periodKey);
            case PAYOUT: return mapper.countStgPayout(periodKey);
            case STOCK: return mapper.countStgStock(periodKey);
            default: throw new IllegalArgumentException("不支持的 TIMS 业务类型：" + type);
        }
    }
}
