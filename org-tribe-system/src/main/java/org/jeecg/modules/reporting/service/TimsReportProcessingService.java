package org.jeecg.modules.reporting.service;

import org.jeecg.modules.reporting.config.ReportingProperties;
import org.jeecg.modules.reporting.mapper.TimsReportMapper;
import org.jeecg.modules.reporting.parser.TimsBusinessType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/** Compatibility facade used by the workflow: prepare outside the DB transaction, then atomically load STG. */
@Service
public class TimsReportProcessingService {
    private static final DateTimeFormatter BATCH_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
    private final TimsReportPreparationService preparationService;
    private final TimsAtomicLoadService loadService;
    private final ReportRuntimeLockService runtimeLockService;
    private final ReportingProperties properties;

    public TimsReportProcessingService(TimsReportMapper mapper) {
        ReportingProperties properties = new ReportingProperties();
        this.preparationService = new TimsReportPreparationService();
        this.loadService = new TimsAtomicLoadService(mapper, properties.getTimsBatchSize());
        this.runtimeLockService = null;
        this.properties = properties;
    }

    @Autowired
    public TimsReportProcessingService(TimsReportPreparationService preparationService,
                                       TimsAtomicLoadService loadService,
                                       ReportRuntimeLockService runtimeLockService,
                                       ReportingProperties properties) {
        this.preparationService = preparationService;
        this.loadService = loadService;
        this.runtimeLockService = runtimeLockService;
        this.properties = properties;
    }

    public TimsReportProcessingResult process(Path extractRoot, TimsBusinessType type,
                                              YearMonth accountingPeriod) throws IOException {
        return process(extractRoot, type, accountingPeriod, null);
    }

    /** allowedTreasuryPrefix is intentionally ignored: the active JAR path loads the aggregate ZIP unchanged. */
    public TimsReportProcessingResult process(Path extractRoot, TimsBusinessType type,
                                              YearMonth accountingPeriod,
                                              String allowedTreasuryPrefix) throws IOException {
        return process(extractRoot, type, accountingPeriod, allowedTreasuryPrefix, rows -> { });
    }

    public TimsReportProcessingResult process(Path extractRoot, TimsBusinessType type,
                                              YearMonth accountingPeriod,
                                              String allowedTreasuryPrefix,
                                              TimsLoadCommitAction completion) throws IOException {
        Path parent = extractRoot.toAbsolutePath().normalize().getParent();
        Path workRoot = (parent == null ? extractRoot.toAbsolutePath().normalize() : parent).resolve("work");
        String lockOwner = properties.getTaskInstanceId() + ":" + UUID.randomUUID().toString();
        if (runtimeLockService != null && !runtimeLockService.acquireTims(lockOwner)) {
            throw new ReportProcessBusyException("已有 TIMS 批次正在解析或入库，本批次继续排队");
        }
        try {
            try (TimsPreparationResult prepared = preparationService.prepare(
                    extractRoot, workRoot, type, accountingPeriod)) {
                if (!prepared.getErrors().isEmpty()) {
                    return new TimsReportProcessingResult(prepared.getFileCount(), 0, prepared.getErrors());
                }
                if (runtimeLockService != null && !runtimeLockService.acquireTims(lockOwner)) {
                    throw new IllegalStateException("TIMS 全局执行租约在解析后失效");
                }
                long committed = runtimeLockService == null
                        ? loadService.load(prepared, type, accountingPeriod,
                        LocalDate.now().format(BATCH_FORMAT), null, completion)
                        : loadService.load(prepared, type, accountingPeriod,
                        LocalDate.now().format(BATCH_FORMAT), lockOwner, completion);
                return new TimsReportProcessingResult(prepared.getFileCount(), Math.toIntExact(committed),
                        java.util.Collections.emptyList());
            }
        } finally {
            if (runtimeLockService != null) runtimeLockService.releaseTims(lockOwner);
        }
    }
}
