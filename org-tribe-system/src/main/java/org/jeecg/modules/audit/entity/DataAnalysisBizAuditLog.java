package org.jeecg.modules.audit.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.sql.Timestamp;
import lombok.Data;

@Data
@TableName("\"jeecg-boot-os\".sys_data_analysis_biz_audit_log")
public class DataAnalysisBizAuditLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.UUID)
    private String id;

    private String eventId;
    private String sourceApp;
    private String menuCode;
    private String menuName;
    private String moduleCode;
    private String moduleName;
    private String entityType;
    private String actionType;
    private String actionCode;
    private String actionName;
    private String nexusUserId;
    private String nexusUsername;
    private String localUserId;
    private String localUsername;
    private String requestUri;
    private String requestMethod;
    private String ipAddress;
    private String userAgent;
    private String resultStatus;
    private String errorMessage;
    private Long durationMs;
    private String bizKey;
    private String bizNo;
    private String querySummary;
    private String extraData;
    private Timestamp eventTime;
    private String syncStatus;
    private Integer syncAttempts;
    private Timestamp lastSyncTime;
    private String lastSyncError;
}
