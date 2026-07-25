package org.indicatorsLib.util;

import org.indicatorsLib.service.IndexRelationService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CreateSchemeSQLTest {

    private CreateSchemeSQL sqlBuilder;
    private IndexRelationService relationService;

    @Before
    public void setUp() {
        relationService = mock(IndexRelationService.class);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("COLID", "18dbf0d2b0a211ea8bc1000c29587404");
        metadata.put("TABLENAME", "lib_indicators_000189");
        metadata.put("TYPE", "0");
        when(relationService.getIndicatorsTableName(any(String[].class)))
                .thenReturn(Collections.singletonList(metadata));

        ApplicationContext applicationContext = mock(ApplicationContext.class);
        when(applicationContext.getBean(IndexRelationService.class)).thenReturn(relationService);

        sqlBuilder = new CreateSchemeSQL();
        sqlBuilder.setApplicationContext(applicationContext);
    }

    @Test
    public void generatedSchemeSqlUsesQuotedUppercaseAliasesForFixedColumns() {
        PageData pageData = new PageData();
        pageData.put("columns", "18dbf0d2b0a211ea8bc1000c29587404");
        pageData.put("userId", "user-1");
        pageData.put("mainCondition", "{\"dimensionFlag\":\"1\",\"periodFlag\":\"2\",\"price\":\"1\"}");

        String sql = sqlBuilder.getSchemeSQL(pageData);

        for (String alias : new String[]{"COLID", "VALUE", "ACCOUNT_DATE", "ACCOUNT_PERIOD", "CODE", "GK"}) {
            assertTrue(sql.contains("AS \"" + alias + "\""));
        }
        assertTrue(sql.contains("aa.\"ACCOUNT_DATE\" AS \"ACCOUNT_DATE\""));
        assertTrue(sql.contains("V.\"ACCOUNT_PERIOD\""));
        assertFalse(sql.contains("aa.ACCOUNT_PERIOD"));
        assertFalse(sql.contains("V.ACCOUNT_PERIOD"));
    }

    @Test
    public void helperSqlUsesQuotedUppercaseAliases() {
        Map<String, Object> condition = new HashMap<>();
        condition.put("columns", "18dbf0d2b0a211ea8bc1000c29587404");
        condition.put("dimensionFlag", "1");
        condition.put("periodFlag", "2");

        Map<String, Object> chartCondition = new HashMap<>();
        chartCondition.put("direction", "");

        IndicatorRequestContext requestContext = IndicatorRequestContext.local("user-1", "GK-001");
        String periodSql = sqlBuilder.getAccountPeriodSql(condition, chartCondition, requestContext);
        String dimensionSql = sqlBuilder.getDimensionSQL(condition, chartCondition, requestContext);

        assertTrue(periodSql.contains("AS \"START_DATE\""));
        assertTrue(periodSql.contains("AS \"END_DATE\""));
        assertTrue(dimensionSql.contains("AS \"DIMCODE\""));
        assertTrue(dimensionSql.contains("AS \"DIMDESCR\""));
    }

    @Test
    public void generatedSchemeSqlAcceptsLegacyLowerCamelMetadataKeys() {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("colId", "18dbf0d2b0a211ea8bc1000c29587404");
        metadata.put("tableName", "lib_indicators_000189");
        metadata.put("type", "0");
        when(relationService.getIndicatorsTableName(any(String[].class)))
                .thenReturn(Collections.singletonList(metadata));

        PageData pageData = new PageData();
        pageData.put("columns", "18dbf0d2b0a211ea8bc1000c29587404");
        pageData.put("userId", "user-1");
        pageData.put("mainCondition", "{\"dimensionFlag\":\"1\",\"periodFlag\":\"2\",\"price\":\"1\"}");

        String sql = sqlBuilder.getSchemeSQL(pageData);

        assertTrue(sql.contains("FROM indicators_lib.lib_indicators_000189"));
        assertTrue(sql.contains("AS \"COLID\""));
        assertTrue(sql.contains("AS \"VALUE\""));
    }

    @Test
    public void invalidCategoryWithoutExecutableMetadataDoesNotBuildSql() {
        when(relationService.getIndicatorsTableName(any(String[].class)))
                .thenReturn(Collections.emptyList());

        PageData pageData = new PageData();
        pageData.put("columns", "category-id");
        pageData.put("userId", "user-1");
        pageData.put("mainCondition", "{\"dimensionFlag\":\"1\",\"periodFlag\":\"3\",\"price\":\"1\"}");

        assertNull(sqlBuilder.getSchemeSQL(pageData));
    }

    @Test
    public void mixedCategoryAndIndicatorSelectionDoesNotBuildPartialSql() {
        PageData pageData = new PageData();
        pageData.put("columns", "18dbf0d2b0a211ea8bc1000c29587404,category-id");
        pageData.put("userId", "user-1");
        pageData.put("mainCondition", "{\"dimensionFlag\":\"1\",\"periodFlag\":\"3\",\"price\":\"1\"}");

        assertNull(sqlBuilder.getSchemeSQL(pageData));
    }

    @Test
    public void generatedSchemeSqlDoesNotPersistCurrentUsersDataScope() {
        PageData pageData = new PageData();
        pageData.put("columns", "18dbf0d2b0a211ea8bc1000c29587404");
        pageData.put("userId", "user-1");
        pageData.put("mainCondition", "{\"dimensionFlag\":\"1\",\"periodFlag\":\"2\",\"price\":\"1\"}");

        String sql = sqlBuilder.getSchemeSQL(pageData);

        assertFalse(sql.contains("authorized_guoku"));
        assertFalse(sql.contains("sys_user"));
        assertFalse(sql.contains("user-1"));
    }

    @Test
    public void generatedSchemeSqlDoesNotRequireUserId() {
        PageData pageData = new PageData();
        pageData.put("columns", "18dbf0d2b0a211ea8bc1000c29587404");
        pageData.put("mainCondition", "{\"dimensionFlag\":\"1\",\"periodFlag\":\"2\",\"price\":\"1\"}");

        assertTrue(sqlBuilder.getSchemeSQL(pageData).contains("FROM indicators_lib.lib_indicators_000189"));
    }
}
