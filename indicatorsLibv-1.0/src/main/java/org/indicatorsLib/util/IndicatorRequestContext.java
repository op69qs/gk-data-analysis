package org.indicatorsLib.util;

import org.apache.commons.lang.StringUtils;

/**
 * Trusted identity context injected by org-tribe-system after authentication.
 */
public final class IndicatorRequestContext {

    private final String userId;
    private final String subjectCode;
    private final String guokuId;

    public IndicatorRequestContext(String userId, String subjectCode, String guokuId) {
        this.userId = requireSafe("当前用户ID", userId);
        this.subjectCode = optionalSafe("门户所属国库编码", subjectCode);
        this.guokuId = optionalSafe("本地所属国库编码", guokuId);
    }

    public static IndicatorRequestContext portal(String userId, String subjectCode) {
        return new IndicatorRequestContext(userId, subjectCode, null);
    }

    public static IndicatorRequestContext local(String userId, String guokuId) {
        return new IndicatorRequestContext(userId, null, guokuId);
    }

    public String getUserId() {
        return userId;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public String getGuokuId() {
        return guokuId;
    }

    public boolean isPortalUser() {
        return StringUtils.isNotBlank(subjectCode);
    }

    private static String requireSafe(String name, String value) {
        String safe = optionalSafe(name, value);
        if (safe == null) {
            throw new IllegalArgumentException(name + "缺失");
        }
        return safe;
    }

    private static String optionalSafe(String name, String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        String trimmed = value.trim();
        if (trimmed.length() > 128 || !trimmed.matches("[A-Za-z0-9_.:-]+")) {
            throw new IllegalArgumentException(name + "格式非法");
        }
        return trimmed;
    }
}
