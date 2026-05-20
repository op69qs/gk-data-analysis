package org.jeecg.modules.audit.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.audit.component.DataAnalysisBizAuditMqProducer;
import org.jeecg.modules.audit.entity.DataAnalysisBizAuditLog;
import org.jeecg.modules.audit.mapper.DataAnalysisBizAuditLogMapper;
import org.jeecg.modules.audit.service.IDataAnalysisBizAuditService;
import org.jeecg.modules.audit.vo.DataAnalysisBizAuditPayload;
import org.jeecg.modules.audit.vo.DataAnalysisMenuEntryAuditRequest;
import org.jeecg.modules.shiro.vo.DefContants;
import org.jeecg.modules.system.entity.SysUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DataAnalysisBizAuditServiceImpl implements IDataAnalysisBizAuditService {

    private static final Logger logger = LoggerFactory.getLogger(DataAnalysisBizAuditServiceImpl.class);
    private static final String RESULT_SUCCESS = "SUCCESS";
    private static final String RESULT_FAIL = "FAIL";
    private static final String SYNC_PENDING = "PENDING";
    private static final String SYNC_SUCCESS = "SUCCESS";
    private static final String SYNC_FAILED = "FAILED";
    private static final String SOURCE_TYPE_EXTERNAL = "EXTERNAL";
    private static final String NEXUS_PORTAL_USER_ID_PREFIX = "PREFIX_NEXUS_PORTAL_USER_ID_";
    private static final String NEXUS_PORTAL_ACCESS_TOKEN_PREFIX = "PREFIX_NEXUS_PORTAL_ACCESS_TOKEN_";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private DataAnalysisBizAuditLogMapper auditLogMapper;

    @Autowired
    private DataAnalysisBizAuditMqProducer mqProducer;

    @Autowired
    private RedisUtil redisUtil;

    @Value("${gk-data-analysis.biz-audit.enabled:true}")
    private boolean auditEnabled;

    @Value("${spring.application.name:systemdj}")
    private String sourceApp;

    @Value("${gk-data-analysis.biz-audit.max-retry-attempts:5}")
    private int maxRetryAttempts;

    @Value("${gk-data-analysis.biz-audit.retry-batch-size:100}")
    private int retryBatchSize;

    @Value("${gk-data-analysis.biz-audit.system-code:GK_DATA_ANALYSIS}")
    private String systemCode;

    @Value("${gk-data-analysis.biz-audit.system-name:数据分析平台}")
    private String systemName;

    @Override
    public void recordOAuthLoginSuccess(String portalUsername,
                                        String portalUserId,
                                        SysUser localUser,
                                        HttpServletRequest request,
                                        String portalAccessToken) {
        if (!auditEnabled) {
            return;
        }
        try {
            DataAnalysisBizAuditLog log = new DataAnalysisBizAuditLog();
            populateBaseLog(log, request);
            log.setMenuCode("oauth-login");
            log.setMenuName("OAuth登录");
            log.setModuleCode("sso-oauth");
            log.setModuleName("OAuth登录");
            log.setEntityType("LOGIN");
            log.setActionType("LOGIN");
            log.setActionCode("OAUTH_DOWNSTREAM_LOGIN");
            log.setActionName("OAuth下游系统登录");
            log.setNexusUserId(firstNonBlank(portalUserId, decodeClaim(portalAccessToken, "sub")));
            log.setNexusUsername(firstNonBlank(portalUsername, decodeClaim(portalAccessToken, "username")));
            log.setLocalUserId(localUser == null ? null : firstNonBlank(localUser.getId(), localUser.getUsername()));
            log.setLocalUsername(localUser == null ? null : firstNonBlank(localUser.getRealname(), localUser.getUsername()));
            log.setRequestUri(request == null ? null : request.getRequestURI());
            log.setRequestMethod(request == null ? null : request.getMethod());
            log.setResultStatus(RESULT_SUCCESS);
            log.setBizKey(firstNonBlank(portalUserId, portalUsername));
            log.setBizNo(firstNonBlank(localUser == null ? null : localUser.getUsername(), portalUsername));
            Map<String, Object> extraData = new LinkedHashMap<String, Object>();
            extraData.put("loginMethod", "oauth_authorization_code");
            extraData.put("portalAccessTokenSubject", decodeClaim(portalAccessToken, "sub"));
            log.setExtraData(writeJson(extraData));
            persistAndSend(log);
        } catch (Exception ex) {
            logger.warn("Failed to record successful OAuth business audit: {}", ex.getMessage(), ex);
        }
    }

    @Override
    public void recordOAuthLoginFailure(String portalUsername,
                                        String portalUserId,
                                        String errorMessage,
                                        HttpServletRequest request,
                                        String portalAccessToken) {
        if (!auditEnabled) {
            return;
        }
        try {
            DataAnalysisBizAuditLog log = new DataAnalysisBizAuditLog();
            populateBaseLog(log, request);
            log.setMenuCode("oauth-login");
            log.setMenuName("OAuth登录");
            log.setModuleCode("sso-oauth");
            log.setModuleName("OAuth登录");
            log.setEntityType("LOGIN");
            log.setActionType("LOGIN");
            log.setActionCode("OAUTH_DOWNSTREAM_LOGIN");
            log.setActionName("OAuth下游系统登录");
            log.setNexusUserId(firstNonBlank(portalUserId, decodeClaim(portalAccessToken, "sub")));
            log.setNexusUsername(firstNonBlank(portalUsername, decodeClaim(portalAccessToken, "username")));
            log.setRequestUri(request == null ? null : request.getRequestURI());
            log.setRequestMethod(request == null ? null : request.getMethod());
            log.setResultStatus(RESULT_FAIL);
            log.setErrorMessage(abbreviate(errorMessage, 1000));
            log.setBizKey(firstNonBlank(portalUserId, portalUsername));
            log.setBizNo(portalUsername);
            Map<String, Object> extraData = new LinkedHashMap<String, Object>();
            extraData.put("loginMethod", "oauth_authorization_code");
            extraData.put("portalAccessTokenSubject", decodeClaim(portalAccessToken, "sub"));
            log.setExtraData(writeJson(extraData));
            persistAndSend(log);
        } catch (Exception ex) {
            logger.warn("Failed to record failed OAuth business audit: {}", ex.getMessage(), ex);
        }
    }

    @Override
    public void recordMenuEntry(DataAnalysisMenuEntryAuditRequest request,
                                HttpServletRequest servletRequest) {
        if (!auditEnabled || request == null || !StringUtils.isNotBlank(request.getMenuId())) {
            return;
        }
        try {
            DataAnalysisBizAuditLog log = new DataAnalysisBizAuditLog();
            populateBaseLog(log, servletRequest);
            log.setMenuCode(request.getMenuId());
            log.setMenuName(request.getMenuTitle());
            log.setModuleCode(firstNonBlank(request.getMenuPath(), request.getRoutePath()));
            log.setModuleName(request.getMenuTitle());
            log.setEntityType("MENU");
            log.setActionType("VIEW");
            log.setActionCode("MENU_ENTER");
            log.setActionName("菜单进入");

            String localToken = servletRequest == null ? null : servletRequest.getHeader(DefContants.X_ACCESS_TOKEN);
            LoginUser currentUser = currentLoginUser();
            log.setLocalUserId(firstNonBlank(currentUser == null ? null : currentUser.getUsername(),
                resolveUsernameByToken(localToken)));
            log.setLocalUsername(firstNonBlank(currentUser == null ? null : currentUser.getRealname(),
                currentUser == null ? null : currentUser.getUsername(),
                resolveUsernameByToken(localToken)));
            log.setNexusUserId(resolvePortalUserId(localToken));
            log.setNexusUsername(resolvePortalUsername(localToken));
            log.setRequestUri(firstNonBlank(request.getFullPath(), request.getRoutePath(), request.getMenuPath()));
            log.setRequestMethod("ROUTE");
            log.setResultStatus(RESULT_SUCCESS);
            log.setBizKey(request.getMenuId());
            log.setBizNo(firstNonBlank(request.getFullPath(), request.getRoutePath()));
            log.setQuerySummary(request.getFullPath());

            Map<String, Object> extraData = new LinkedHashMap<String, Object>();
            extraData.put("menuId", request.getMenuId());
            extraData.put("menuTitle", request.getMenuTitle());
            extraData.put("menuPath", request.getMenuPath());
            extraData.put("routePath", request.getRoutePath());
            extraData.put("fullPath", request.getFullPath());
            extraData.put("routeName", request.getRouteName());
            if (servletRequest != null) {
                extraData.put("ingressEndpoint", servletRequest.getRequestURI());
            }
            log.setExtraData(writeJson(extraData));
            persistAndSend(log);
        } catch (Exception ex) {
            logger.warn("Failed to record menu-entry business audit: {}", ex.getMessage(), ex);
        }
    }

    @Override
    public void retryFailedAudits() {
        if (!auditEnabled) {
            return;
        }
        List<DataAnalysisBizAuditLog> candidates =
            auditLogMapper.findRetryCandidates(sourceApp, maxRetryAttempts, retryBatchSize);
        for (DataAnalysisBizAuditLog candidate : candidates) {
            trySend(candidate);
        }
    }

    private void persistAndSend(DataAnalysisBizAuditLog log) {
        auditLogMapper.insert(log);
        trySend(log);
    }

    private void trySend(DataAnalysisBizAuditLog log) {
        Timestamp syncTime = nowTimestamp();
        try {
            mqProducer.send(buildPayload(log));
            auditLogMapper.updateSyncResult(log.getId(), SYNC_SUCCESS, syncTime, null);
        } catch (Exception ex) {
            logger.warn("Failed to sync data-analysis business audit, id={}, eventId={}, reason={}",
                log.getId(), log.getEventId(), ex.getMessage());
            auditLogMapper.updateSyncResult(log.getId(), SYNC_FAILED, syncTime,
                abbreviate(ex.getMessage(), 1000));
        }
    }

    private DataAnalysisBizAuditPayload buildPayload(DataAnalysisBizAuditLog log) {
        DataAnalysisBizAuditPayload payload = new DataAnalysisBizAuditPayload();
        payload.setEventId(log.getEventId());
        payload.setTraceId(log.getEventId());
        payload.setSystemCode(systemCode);
        payload.setSystemName(systemName);
        payload.setModule(firstNonBlank(log.getModuleCode(), log.getMenuCode()));
        payload.setOperation(firstNonBlank(log.getActionCode(), log.getActionType()));
        payload.setOperationName(firstNonBlank(log.getActionName(), log.getActionType()));
        payload.setResultStatus(log.getResultStatus());
        payload.setResponseStatus(RESULT_SUCCESS.equals(log.getResultStatus()) ? 200 : 500);
        payload.setErrorMessage(log.getErrorMessage());
        payload.setUserId(firstNonBlank(log.getNexusUserId(), log.getLocalUserId()));
        payload.setUsername(firstNonBlank(log.getNexusUsername(), log.getLocalUsername()));
        payload.setIpAddress(log.getIpAddress());
        payload.setUserAgent(log.getUserAgent());
        payload.setRequestMethod(log.getRequestMethod());
        payload.setRequestUrl(log.getRequestUri());
        payload.setRequestParams(log.getQuerySummary());
        payload.setDurationMs(log.getDurationMs());
        payload.setEventTime(formatTimestamp(log.getEventTime()));
        payload.setSourceType(SOURCE_TYPE_EXTERNAL);
        payload.setExtraData(buildExtraData(log));
        return payload;
    }

    private Map<String, Object> buildExtraData(DataAnalysisBizAuditLog log) {
        Map<String, Object> extraData = new LinkedHashMap<String, Object>();
        extraData.put("sourceApp", log.getSourceApp());
        extraData.put("menuCode", log.getMenuCode());
        extraData.put("menuName", log.getMenuName());
        extraData.put("moduleCode", log.getModuleCode());
        extraData.put("moduleName", log.getModuleName());
        extraData.put("entityType", log.getEntityType());
        extraData.put("actionType", log.getActionType());
        extraData.put("actionCode", log.getActionCode());
        extraData.put("bizKey", log.getBizKey());
        extraData.put("bizNo", log.getBizNo());
        extraData.put("localUserId", log.getLocalUserId());
        extraData.put("localUsername", log.getLocalUsername());
        extraData.put("nexusUserId", log.getNexusUserId());
        extraData.put("nexusUsername", log.getNexusUsername());
        if (StringUtils.isNotBlank(log.getExtraData())) {
            try {
                Map<String, Object> saved = objectMapper.readValue(log.getExtraData(),
                    new TypeReference<Map<String, Object>>() {});
                extraData.putAll(saved);
            } catch (Exception ignored) {
                extraData.put("rawExtraData", log.getExtraData());
            }
        }
        return extraData;
    }

    private void populateBaseLog(DataAnalysisBizAuditLog log, HttpServletRequest request) {
        log.setEventId(UUID.randomUUID().toString());
        log.setSourceApp(sourceApp);
        log.setIpAddress(resolveClientIp(request));
        log.setUserAgent(request == null ? null : request.getHeader("User-Agent"));
        log.setDurationMs(0L);
        log.setEventTime(nowTimestamp());
        log.setSyncStatus(SYNC_PENDING);
        log.setSyncAttempts(0);
    }

    private LoginUser currentLoginUser() {
        try {
            Object principal = SecurityUtils.getSubject().getPrincipal();
            if (principal instanceof LoginUser) {
                return (LoginUser) principal;
            }
        } catch (Exception ignored) {
            // ignore missing subject context
        }
        return null;
    }

    private String resolvePortalUserId(String localToken) {
        if (!StringUtils.isNotBlank(localToken)) {
            return null;
        }
        Object value = redisUtil.get(NEXUS_PORTAL_USER_ID_PREFIX + localToken);
        return value == null ? null : value.toString();
    }

    private String resolvePortalUsername(String localToken) {
        String portalAccessToken = resolvePortalAccessToken(localToken);
        return decodeClaim(portalAccessToken, "username");
    }

    private String resolvePortalAccessToken(String localToken) {
        if (!StringUtils.isNotBlank(localToken)) {
            return null;
        }
        Object value = redisUtil.get(NEXUS_PORTAL_ACCESS_TOKEN_PREFIX + localToken);
        return value == null ? null : value.toString();
    }

    private String resolveUsernameByToken(String token) {
        if (!StringUtils.isNotBlank(token)) {
            return null;
        }
        try {
            return JwtUtil.getUsername(token);
        } catch (Exception ignored) {
            return null;
        }
    }

    private String decodeClaim(String token, String claimName) {
        if (!StringUtils.isNotBlank(token) || !StringUtils.isNotBlank(claimName)) {
            return null;
        }
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            String value = decodedJWT.getClaim(claimName).asString();
            if (StringUtils.isNotBlank(value)) {
                return value;
            }
            if ("sub".equals(claimName)) {
                return decodedJWT.getSubject();
            }
        } catch (Exception ignored) {
            return null;
        }
        return null;
    }

    private String resolveClientIp(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotBlank(forwardedFor)) {
            return forwardedFor.split(",")[0].trim();
        }
        String realIp = request.getHeader("X-Real-IP");
        if (StringUtils.isNotBlank(realIp)) {
            return realIp;
        }
        return request.getRemoteAddr();
    }

    private String writeJson(Map<String, Object> data) {
        if (data == null || data.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(data);
        } catch (Exception ex) {
            logger.warn("Failed to serialize audit extra data: {}", ex.getMessage());
            return null;
        }
    }

    private Timestamp nowTimestamp() {
        return Timestamp.from(Instant.now());
    }

    private String formatTimestamp(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        return OffsetDateTime.ofInstant(timestamp.toInstant(), ZoneOffset.ofHours(8))
            .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    private String abbreviate(String value, int maxLength) {
        if (!StringUtils.isNotBlank(value) || value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength);
    }

    private String firstNonBlank(String... values) {
        if (values == null) {
            return null;
        }
        for (String value : values) {
            if (StringUtils.isNotBlank(value)) {
                return value;
            }
        }
        return null;
    }
}
