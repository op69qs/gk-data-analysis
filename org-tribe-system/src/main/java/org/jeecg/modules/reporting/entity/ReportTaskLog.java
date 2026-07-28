package org.jeecg.modules.reporting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("agent_key_file.report_task_log")
public class ReportTaskLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.UUID)
    private String id;
    private String batchId;
    private String taskId;
    private String stage;
    private String fromStatus;
    private String toStatus;
    private String message;
    private String detail;
    private String currentFileName;
    private Long processedRowCount;
    private Long successRowCount;
    private Long errorRowCount;
    private String operatorId;
    private String operatorName;
    private Date eventTime;
}
