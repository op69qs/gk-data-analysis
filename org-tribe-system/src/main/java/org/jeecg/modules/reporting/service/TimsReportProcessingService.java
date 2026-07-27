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

/** Compatibility facade used by the workflow: prepare outside the DB transaction, then atomically load STG. */
@Service
public class TimsReportProcessingService {
    private static final DateTimeFormatter BATCH_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
    private final TimsReportPreparationService preparationService;
    private final TimsAtomicLoadService loadService;

    public TimsReportProcessingService(TimsReportMapper mapper) {
        ReportingProperties properties = new ReportingProperties();
        this.preparationService = new TimsReportPreparationService();
        this.loadService = new TimsAtomicLoadService(mapper, properties.getTimsBatchSize());
    }

    @Autowired
    public TimsReportProcessingService(TimsReportPreparationService preparationService,
                                       TimsAtomicLoadService loadService) {
        this.preparationService = preparationService;
        this.loadService = loadService;
    }

    public TimsReportProcessingResult process(Path extractRoot, TimsBusinessType type,
                                              YearMonth accountingPeriod) throws IOException {
        return process(extractRoot, type, accountingPeriod, null);
    }

    /** allowedTreasuryPrefix is intentionally ignored: the active JAR path loads the aggregate ZIP unchanged. */
    public TimsReportProcessingResult process(Path extractRoot, TimsBusinessType type,
                                              YearMonth accountingPeriod,
                                              String allowedTreasuryPrefix) throws IOException {
        Path parent = extractRoot.toAbsolutePath().normalize().getParent();
        Path workRoot = (parent == null ? extractRoot.toAbsolutePath().normalize() : parent).resolve("work");
        try (TimsPreparationResult prepared = preparationService.prepare(
                extractRoot, workRoot, type, accountingPeriod)) {
            if (!prepared.getErrors().isEmpty()) {
                return new TimsReportProcessingResult(prepared.getFileCount(), 0, prepared.getErrors());
            }
            long committed = loadService.load(prepared, type, accountingPeriod,
                    LocalDate.now().format(BATCH_FORMAT));
            return new TimsReportProcessingResult(prepared.getFileCount(), Math.toIntExact(committed),
                    java.util.Collections.emptyList());
        }
    }
}
