package org.jeecg.modules.reporting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("agent_key_file.report_process_call")
public class ReportProcessCall implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.UUID)
    private String id;
    private String batchId;
    private String taskId;
    private Date accountingPeriod;
    private String treasuryScope;
    private String procedureName;
    private String procedureArgument;
    private String status;
    private Integer attemptNo;
    private String externalLogId;
    private String requestSummary;
    private String resultSummary;
    private String errorMessage;
    private Date startedTime;
    private Date endedTime;
    private Long durationMs;
    private String createBy;
    private Date createTime;
}
