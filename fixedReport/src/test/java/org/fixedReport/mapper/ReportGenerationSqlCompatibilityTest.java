package org.fixedReport.mapper;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.druid.wall.spi.PGWallProvider;
import com.alibaba.druid.sql.ast.SQLOrderingSpecification;
import com.alibaba.druid.sql.ast.expr.SQLAggregateExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.statement.SQLSubqueryTableSource;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.logging.nologging.NoLoggingImpl;
import org.apache.ibatis.session.Configuration;
import org.junit.Assume;
import org.junit.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

public class ReportGenerationSqlCompatibilityTest {
    private static final String PREFIX = "org.fixedReport.dao.mapper.fixedReport.";

    @Test
    public void quarterlyGrowthGraphsKeepDescendingRanksAndExplicitOutputOrder() throws Exception {
        for (String name : new String[]{"getGraphParams5", "getGraphParams6"}) {
            SQLSelectQueryBlock query = graphQuery(name);
            SQLAggregateExpr rank = (SQLAggregateExpr) query.getSelectList().get(0).getExpr();
            assertEquals(name, SQLOrderingSpecification.DESC,
                    rank.getOver().getOrderBy().getItems().get(0).getType());
            assertNotNull(name + " must return chart rows in rank order", query.getOrderBy());
            assertEquals("i", query.getOrderBy().getItems().get(0).getExpr().toString());
        }
    }

    @Test
    public void everyReachableGenerationSelectParsesAndPassesThePostgresqlFirewall() throws Exception {
        for (String mapper : new String[]{"ReportMapper", "QuarterReportMapper", "NewsFlashQuarterMapper"}) {
            for (String period : periods(mapper)) {
                for (String statement : statements(mapper)) {
                    String sql = sql(mapper, statement, period);
                    assertEquals(mapper + "." + statement, 1, SQLUtils.parseStatements(sql, JdbcConstants.POSTGRESQL).size());
                    assertTrue(mapper + "." + statement, new PGWallProvider().check(sql).getViolations().isEmpty());
                }
            }
        }
    }

    @Test
    public void reachableQueriesPlanInAnExplicitlyReadOnlyRepositoryDatabaseTransaction() throws Exception {
        String url = System.getenv("FIXED_REPORT_READONLY_JDBC_URL");
        Assume.assumeTrue("Read-only database check is opt-in", url != null);
        Properties properties = new Properties();
        properties.setProperty("user", System.getenv("FIXED_REPORT_READONLY_USER"));
        properties.setProperty("password", System.getenv("FIXED_REPORT_READONLY_PASSWORD"));
        properties.setProperty("options", "-c default_transaction_read_only=on -c statement_timeout=60000");
        properties.setProperty("readOnlyMode", "always");
        properties.setProperty("connectTimeout", "5");
        try (Connection connection = DriverManager.getConnection(url, properties)) {
            connection.setAutoCommit(false);
            connection.setReadOnly(true);
            try {
                try (Statement statement = connection.createStatement();
                     ResultSet result = statement.executeQuery("SELECT current_setting('transaction_read_only')")) {
                    assertTrue(result.next());
                    assertEquals("on", result.getString(1));
                }
                for (String name : new String[]{"getGraphParams3", "getGraphParams4", "getGraphParams5", "getGraphParams6"}) {
                    assertGraphFixtureOrder(connection, name);
                }
                for (String mapper : new String[]{"ReportMapper", "QuarterReportMapper", "NewsFlashQuarterMapper"}) {
                    String period = "ReportMapper".equals(mapper) ? "2025-11" : "2025Q3";
                    for (String name : statements(mapper)) {
                        try (Statement statement = connection.createStatement();
                             ResultSet result = statement.executeQuery("EXPLAIN " + sql(mapper, name, period))) {
                            assertTrue(mapper + "." + name, result.next());
                            System.out.println("Read-only EXPLAIN passed: " + mapper + "." + name + " " + period);
                        }
                    }
                }
            } finally {
                connection.rollback();
            }
        }
    }

    private static SQLSelectQueryBlock graphQuery(String name) throws Exception {
        SQLSelectStatement parsed = (SQLSelectStatement) SQLUtils.parseSingleStatement(
                sql("NewsFlashQuarterMapper", name, "2025Q3"), JdbcConstants.POSTGRESQL);
        return (SQLSelectQueryBlock) parsed.getSelect().getQuery();
    }

    private static void assertGraphFixtureOrder(Connection connection, String name) throws Exception {
        SQLSelectQueryBlock query = graphQuery(name);
        // Replace only the data source: execute the mapper's real ranking, filter and final ordering.
        SQLSelectStatement fixture = (SQLSelectStatement) SQLUtils.parseSingleStatement(
                "SELECT 'low' AS area_dscr, '500106' AS area_no_id, 10 AS index_value "
                        + "UNION ALL SELECT 'high', '500107', 30 UNION ALL SELECT 'middle', '500103', 20",
                JdbcConstants.POSTGRESQL);
        ((SQLSubqueryTableSource) query.getFrom()).setSelect(new SQLSelect(fixture.getSelect().getQuery()));
        int[] expected = ("getGraphParams5".equals(name) || "getGraphParams6".equals(name))
                ? new int[]{30, 20, 10} : new int[]{10, 20, 30};
        try (Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(SQLUtils.toSQLString(query, JdbcConstants.POSTGRESQL))) {
            for (int index = 0; index < expected.length; index++) {
                assertTrue(name + " missing fixture row", result.next());
                assertEquals(name, index + 1, result.getInt("i"));
                assertEquals(name, expected[index], result.getInt("value"));
            }
            assertTrue(name + " unexpected extra rows", !result.next());
        }
        System.out.println("Read-only graph ordering fixture passed: " + name
                + " values=" + Arrays.toString(expected) + " ranks=[1, 2, 3]");
    }

    private static List<String> statements(String mapper) {
        if ("ReportMapper".equals(mapper)) {
            return Arrays.asList("getTextAndParams", "getTableParams", "getTableParams2", "getTableParams3",
                    "getAreaParams", "getNumParams", "getGraphParams", "getGraphParams2", "getGraphParams3",
                    "getGraphParams4", "getGraphParams5");
        }
        if ("QuarterReportMapper".equals(mapper)) {
            return Arrays.asList("getTextAndParams", "getTableParams", "getTableParams2", "getTableParams3",
                    "getGraphParams", "getGraphParams2", "getGraphParams3", "getGraphParams4", "getGraphParams5",
                    "getGraphParams6", "getGraphParams7", "getGraphParams8", "getGraphParams9", "getGraphParams10");
        }
        return Arrays.asList("getTextAndParams", "getTableParams", "getTableParams2", "getTableParams3",
                "getGraphParams", "getGraphParams2", "getGraphParams3", "getGraphParams4", "getGraphParams5", "getGraphParams6");
    }

    private static String[] periods(String mapper) {
        return "ReportMapper".equals(mapper) ? new String[]{"2026-06", "2025-11"}
                : new String[]{"2025Q1", "2025Q2", "2025Q3", "2025Q4"};
    }

    private static String sql(String mapper, String statement, String period) throws Exception {
        Configuration configuration = new Configuration();
        configuration.setLogImpl(NoLoggingImpl.class);
        String resource = "mybatis/fixedReport/" + mapper + ".xml";
        try (InputStream input = Resources.getResourceAsStream(resource)) {
            new XMLMapperBuilder(input, configuration, resource, configuration.getSqlFragments()).parse();
        }
        Map<String, Object> params = new HashMap<>();
        params.put("ACCOUNT_PERIOD", period);
        params.put("AMT_UNIT", "10000");
        params.put("DIM_VALUE", "500000");
        params.put("monthDate", "ReportMapper".equals(mapper) ? "-11"
                : new String[]{"-03", "-06", "-09", "-12"}[period.charAt(5) - '1']);
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("params", params);
        return configuration.getMappedStatement(PREFIX + mapper + "." + statement).getBoundSql(arguments).getSql();
    }
}
