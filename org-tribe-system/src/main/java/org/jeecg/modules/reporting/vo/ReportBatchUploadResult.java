package org.jeecg.modules.reporting.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReportBatchUploadResult {
    private String batchId;
    private String batchNo;
    private String status;
    private String currentStage;
    private Integer progressPercent;
}
