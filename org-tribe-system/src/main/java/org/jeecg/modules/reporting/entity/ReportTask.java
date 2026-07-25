package org.jeecg.modules.reporting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("agent_key_file.report_task")
public class ReportTask implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.UUID)
    private String id;
    private String batchId;
    private String parentTaskId;
    private String retryOfTaskId;
    private String taskType;
    private Integer sequenceNo;
    private Integer attemptNo;
    private String status;
    private Integer progressPercent;
    private String executorKey;
    private String requestParams;
    private String resultSummary;
    private String errorMessage;
    private Date startedTime;
    private Date endedTime;
    private Long durationMs;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;
}
