package org.jeecg.modules.reporting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("agent_key_file.report_batch")
public class ReportBatch implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.UUID)
    private String id;
    private String batchNo;
    private String sourceDomain;
    private String businessType;
    private Date accountingPeriod;
    private String treasuryCode;
    private String treasuryName;
    private String originalFileName;
    private String currentStage;
    private String status;
    private Integer progressPercent;
    private Integer fileCount;
    private Long successRowCount;
    private Long errorRowCount;
    private Integer autoProcessRequired;
    private String processCallStatus;
    private String retryOfBatchId;
    private String resultSummary;
    private String errorSummary;
    private Integer delFlag;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;
}
