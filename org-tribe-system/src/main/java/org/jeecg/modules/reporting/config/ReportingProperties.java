package org.jeecg.modules.reporting.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

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

    /** Production follows the original program and automatically starts downstream processing. */
    private boolean autoProcessEnabled = true;
}
