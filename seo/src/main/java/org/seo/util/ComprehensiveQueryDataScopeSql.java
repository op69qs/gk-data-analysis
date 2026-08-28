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
        String safeSubjectCode = requirePortalSubjectCode(subjectCode);
        if (StringUtils.isBlank(treasuryColumn)
                || !treasuryColumn.matches("[A-Za-z0-9_]+(?:\\.[A-Za-z0-9_]+)+")) {
            throw new IllegalArgumentException("国库字段格式非法");
        }
        return " inner join (select distinct scope.CID"
                + " from seo.cm_guoku_dimnsn scope"
                + " inner join dmcode.cm_guoku_dimnsn root"
                + " on scope.PID = root.GUOKU_ID"
                + " where root.BOOKORGCODE = '" + safeSubjectCode + "') scope_rows"
                + " on " + treasuryColumn + " = scope_rows.CID";
    }
}
