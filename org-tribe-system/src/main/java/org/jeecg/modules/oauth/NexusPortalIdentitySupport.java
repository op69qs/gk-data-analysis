package org.jeecg.modules.oauth;

import org.apache.commons.lang.StringUtils;
import org.jeecg.common.util.RedisUtil;
import org.springframework.stereotype.Component;

/**
 * Shared portal-identity helpers for OAuth-backed sessions.
 * Portal subject_code is cached under PREFIX_NEXUS_PORTAL_SUBJECT_CODE_{localToken}.
 */
@Component
public class NexusPortalIdentitySupport {

    public static final String SUBJECT_CODE_CACHE_PREFIX = "PREFIX_NEXUS_PORTAL_SUBJECT_CODE_";

    private final RedisUtil redisUtil;

    public NexusPortalIdentitySupport(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    /**
     * @return portal JWT subject_code (bookorgcode) when present, otherwise null/blank
     */
    public String resolveSubjectCode(String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        Object value = redisUtil.get(SUBJECT_CODE_CACHE_PREFIX + token);
        if (value == null) {
            return null;
        }
        String subjectCode = String.valueOf(value);
        return StringUtils.isBlank(subjectCode) ? null : subjectCode.trim();
    }
}
