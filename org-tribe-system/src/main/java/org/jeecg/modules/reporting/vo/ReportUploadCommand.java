package org.jeecg.modules.reporting.vo;

import lombok.Data;

@Data
public class ReportUploadCommand {
    private String sourceDomain;
    private String businessType;
    private String accountingPeriod;
    private String treasuryCode;
    private String treasuryName;
    /** Server-derived prefix; never bound from a request parameter. */
    private String allowedTreasuryPrefix;
}
