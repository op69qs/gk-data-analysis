package org.jeecg.modules.audit.vo;

import java.util.Map;
import lombok.Data;

@Data
public class DataAnalysisBizAuditPayload {

    private String eventId;
    private String traceId;
    private String systemCode;
    private String systemName;
    private String module;
    private String operation;
    private String operationName;
    private String resultStatus;
    private Integer responseStatus;
    private String errorMessage;
    private String userId;
    private String username;
    private String ipAddress;
    private String userAgent;
    private String requestMethod;
    private String requestUrl;
    private String requestParams;
    private Long durationMs;
    private String eventTime;
    private String sourceType;
    private Map<String, Object> extraData;
}
