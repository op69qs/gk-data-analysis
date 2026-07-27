package org.jeecg.modules.reporting.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Data
@Component
@ConfigurationProperties(prefix = "reporting")
public class ReportingProperties {

    /** Root dedicated to report archives; user-provided paths are never appended directly. */
    private String archiveRoot = "./data/reporting";
    private long maxUploadBytes = 10L * 1024L * 1024L;
    private int maxZipEntries = 2000;
    private long maxTotalUncompressedBytes = 500L * 1024L * 1024L;
    private long maxSingleEntryBytes = 100L * 1024L * 1024L;

    /** Physical cleanup is disabled until the business confirms a retention period. */
    private int retentionDays = 0;

    /** Must be enabled explicitly only after the real ETL table/procedure contract is verified. */
    private boolean autoProcessEnabled = false;
    private boolean processDependenciesVerified = false;
    private int taskStaleTimeoutMinutes = 30;
    private int processStaleTimeoutMinutes = 360;
    private int taskScanBatchSize = 20;
    private int timsBatchSize = 400;
    private int timsLockLeaseMinutes = 60;
    private String taskInstanceId = UUID.randomUUID().toString();
}
