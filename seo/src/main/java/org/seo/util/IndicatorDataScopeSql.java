package org.seo.util;

import org.apache.commons.lang.StringUtils;

/** Applies viewer treasury scope to VIS-pushed indicator scheme SQL. */
public final class IndicatorDataScopeSql {

    private IndicatorDataScopeSql() {
    }

    public static String apply(
            String sourceSql,
            String dimensionFlag,
            String subjectCode,
            String guokuId) {
        if (StringUtils.isBlank(sourceSql)) {
            throw new IllegalArgumentException("指标查询SQL不能为空");
        }
        String authorizedColumn;
        if ("1".equals(dimensionFlag)) {
            authorizedColumn = "guoku_id";
        } else if ("2".equals(dimensionFlag)) {
            authorizedColumn = "area_no_id";
        } else {
            throw new IllegalArgumentException("不支持的指标维度: " + dimensionFlag);
        }

        String rootPredicate;
        if (StringUtils.isNotBlank(subjectCode)) {
            rootPredicate = "d.bookorgcode = '" + safe("门户所属国库编码", subjectCode) + "'";
        } else if (StringUtils.isNotBlank(guokuId)) {
            rootPredicate = "d.guoku_id = '" + safe("本地所属国库编码", guokuId) + "'";
        } else {
            throw new IllegalArgumentException("当前用户未配置所属国库");
        }

        String authorizedSql = "WITH RECURSIVE authorized_guoku(guoku_id,area_no_id) AS ("
                + "SELECT d.guoku_id,d.area_no_id FROM dmcode.cm_guoku_dimnsn d WHERE " + rootPredicate
                + " UNION SELECT child.guoku_id,child.area_no_id FROM dmcode.cm_guoku_dimnsn child "
                + "JOIN authorized_guoku parent ON child.guoku_pid=parent.guoku_id"
                + ") SELECT " + authorizedColumn + " FROM authorized_guoku WHERE "
                + authorizedColumn + " IS NOT NULL";
        return "SELECT scoped.* FROM (" + sourceSql + ") scoped WHERE scoped.\"CODE\" IN ("
                + authorizedSql + ")";
    }

    private static String safe(String name, String value) {
        String trimmed = value.trim();
        if (trimmed.length() > 128 || !trimmed.matches("[A-Za-z0-9_.:-]+")) {
            throw new IllegalArgumentException(name + "格式非法");
        }
        return trimmed;
    }
}
