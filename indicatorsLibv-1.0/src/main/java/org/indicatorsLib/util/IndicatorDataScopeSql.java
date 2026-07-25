package org.indicatorsLib.util;

import org.apache.commons.lang.StringUtils;

/**
 * Applies the current viewer's treasury tree to an executable indicator SQL.
 * Base/saved SQL remains viewer-neutral and is wrapped only at execution time.
 */
public final class IndicatorDataScopeSql {

    private IndicatorDataScopeSql() {
    }

    public static String apply(String sourceSql, String dimensionFlag, IndicatorRequestContext context) {
        if (StringUtils.isBlank(sourceSql)) {
            throw new IllegalArgumentException("指标查询SQL不能为空");
        }
        return "SELECT scoped.* FROM (" + sourceSql + ") scoped WHERE scoped.\"CODE\" IN ("
                + authorizedCodeSql(dimensionFlag, context) + ")";
    }

    static String authorizedCodeSql(String dimensionFlag, IndicatorRequestContext context) {
        if (context == null) {
            throw new IllegalArgumentException("当前用户上下文缺失");
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
        if (context.isPortalUser()) {
            rootPredicate = "d.bookorgcode = '" + context.getSubjectCode() + "'";
        } else if (StringUtils.isNotBlank(context.getGuokuId())) {
            rootPredicate = "d.guoku_id = '" + context.getGuokuId() + "'";
        } else {
            throw new IllegalArgumentException("当前用户未配置所属国库");
        }

        return "WITH RECURSIVE authorized_guoku(guoku_id,area_no_id) AS ("
                + "SELECT d.guoku_id,d.area_no_id FROM dmcode.cm_guoku_dimnsn d WHERE " + rootPredicate
                + " UNION SELECT child.guoku_id,child.area_no_id FROM dmcode.cm_guoku_dimnsn child "
                + "JOIN authorized_guoku parent ON child.guoku_pid=parent.guoku_id"
                + ") SELECT " + authorizedColumn + " FROM authorized_guoku WHERE " + authorizedColumn + " IS NOT NULL";
    }
}
