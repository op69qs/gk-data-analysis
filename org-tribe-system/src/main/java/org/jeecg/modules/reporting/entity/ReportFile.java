package org.jeecg.modules.reporting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("agent_key_file.report_file")
public class ReportFile implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.UUID)
    private String id;
    private String batchId;
    private String parentFileId;
    private String fileRole;
    private String businessType;
    private String originalName;
    private String archiveName;
    private String relativePath;
    private String storagePath;
    private String contentType;
    private String fileExtension;
    private Long fileSize;
    private String sha256;
    private String archiveStatus;
    private String extractStatus;
    private String parseStatus;
    private Long totalRowCount;
    private Long successRowCount;
    private Long errorRowCount;
    private String errorSummary;
    private Integer retained;
    private Integer delFlag;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;
}
