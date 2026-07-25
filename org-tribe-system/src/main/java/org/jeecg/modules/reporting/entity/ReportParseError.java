package org.jeecg.modules.reporting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("agent_key_file.report_parse_error")
public class ReportParseError implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.UUID)
    private String id;
    private String batchId;
    private String fileId;
    private String taskId;
    private String businessType;
    private String sheetName;
    private Long rowNumber;
    private String columnName;
    private String rawValue;
    private String errorCode;
    private String errorMessage;
    private Date createTime;
}
