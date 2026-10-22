package org.seo.util;

import org.apache.commons.lang.StringUtils;

/** Builds the treasury scope applied to portal comprehensive queries. */
public final class ComprehensiveQueryDataScopeSql {

    private ComprehensiveQueryDataScopeSql() {
    }

    public static String requirePortalSubjectCode(String subjectCode) {
        if (StringUtils.isBlank(subjectCode)) {
            throw new IllegalArgumentException("门户所属国库编码缺失");
        }
        String trimmed = subjectCode.trim();
        if (trimmed.length() > 128 || !trimmed.matches("[A-Za-z0-9_.:-]+")) {
            throw new IllegalArgumentException("门户所属国库编码格式非法");
        }
        return trimmed;
    }

    public static String portalTreasuryJoin(String treasuryColumn, String subjectCode) {
        String column = requireTreasuryColumn(treasuryColumn);
        return " inner join (" + portalTreasuryScopeSubquery(subjectCode) + ") scope_rows"
                + " on " + column + " = scope_rows.CID";
    }

    /**
     * 国库范围过滤（IN 子查询形式）。
     * 与维度过滤同时存在时不能再拼第二个 JOIN：ClickHouse 20.8 在两个 JOIN 的 ON
     * 都引用同一事实表字段时会报 Code 48「Cannot rewrite JOINs」。
     */
    public static String portalTreasuryInFilter(String treasuryColumn, String subjectCode) {
        String column = requireTreasuryColumn(treasuryColumn);
        return " and " + column + " in (" + portalTreasuryScopeSubquery(subjectCode) + ")";
    }

    private static String portalTreasuryScopeSubquery(String subjectCode) {
        String safeSubjectCode = requirePortalSubjectCode(subjectCode);
        return "select distinct scope.CID"
                + " from seo.cm_guoku_dimnsn scope"
                + " inner join dmcode.cm_guoku_dimnsn root"
                + " on scope.PID = root.guoku_id"
                + " where root.bookorgcode = '" + safeSubjectCode + "'";
    }

    private static String requireTreasuryColumn(String treasuryColumn) {
        if (StringUtils.isBlank(treasuryColumn)
                || !treasuryColumn.matches("[A-Za-z0-9_]+(?:\\.[A-Za-z0-9_]+)+")) {
            throw new IllegalArgumentException("国库字段格式非法");
        }
        return treasuryColumn;
    }
}
