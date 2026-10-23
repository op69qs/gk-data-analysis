package org.fixedReport.mapper;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.druid.wall.WallCheckResult;
import com.alibaba.druid.wall.spi.PGWallProvider;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.logging.nologging.NoLoggingImpl;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.fixedReport.dao.mapper.fixedReport.NewsFlashMapper;
import org.fixedReport.util.PageData;
import org.junit.Test;
import org.junit.Assume;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/** Exercises the actual mapper output, including every scalar column in table 2. */
public class NewsFlashSqlCompatibilityTest {

    @Test
    public void reportStateUpdateDefaultsToSuccessAndBindsFailureStatus() throws Exception {
        Configuration configuration = new Configuration();
        String resource = "mybatis/fixedReport/ReportMapper.xml";
        try (InputStream input = Resources.getResourceAsStream(resource)) {
            new XMLMapperBuilder(input, configuration, resource, configuration.getSqlFragments()).parse();
        }
        Map<String, Object> params = new HashMap<>();
        params.put("REPORT_ID", "monthly-test");
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("params", params);
        String id = "org.fixedReport.dao.mapper.fixedReport.ReportMapper.updateEntityReport";
        BoundSql successfulUpdate = configuration.getMappedStatement(id).getBoundSql(arguments);
        assertTrue(successfulUpdate.getSql().contains("'1'"));
        assertEquals("params.REPORT_ID", successfulUpdate.getParameterMappings().get(0).getProperty());

        params.put("STATUS", "2");
        BoundSql failedUpdate = configuration.getMappedStatement(id).getBoundSql(arguments);
        assertEquals(2, failedUpdate.getParameterMappings().size());
        assertEquals("params.STATUS", failedUpdate.getParameterMappings().get(0).getProperty());
        assertEquals("params.REPORT_ID", failedUpdate.getParameterMappings().get(1).getProperty());
        assertEquals(1, SQLUtils.parseStatements(failedUpdate.getSql(), JdbcConstants.POSTGRESQL).size());
    }

    @Test
    public void monthlyFlashGenerationQueriesParseAndPassPostgresqlFirewall() throws Exception {
        for (String period : new String[]{"2026-06", "2025-11"}) {
            for (String statement : new String[]{"getTextAndParams", "getTableParams",
                    "getTableParams2", "getTableParams3"}) {
                String sql = boundSql(statement, period);
                assertEquals(statement, 1, SQLUtils.parseStatements(sql, JdbcConstants.POSTGRESQL).size());
                WallCheckResult result = new PGWallProvider().check(sql);
                assertTrue(statement + ": " + result.getViolations(), result.getViolations().isEmpty());
            }
        }
    }

    @Test
    public void getTableParams3UsesQuotedPhysicalColumnNames() throws Exception {
        String sql = boundSql("getTableParams3", "2026-06");
        assertTrue(sql.contains("\"101_TATE\""));
        assertTrue(sql.contains("\"101_RANK\""));
        assertTrue("must not use mistaken c_101_* aliases", !sql.contains("c_101_tate") && !sql.contains("c_101_rank"));
    }

    /** Optional repository-database check; never creates reports or business records. */
    @Test
    public void monthlyFlashGenerationQueriesPlanAndExecuteInReadOnlyTransaction() throws Exception {
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
            Configuration configuration = configuration();
            configuration.setEnvironment(new Environment("read-only-regression", new JdbcTransactionFactory(),
                    new UnpooledDataSource()));
            SqlSession session = new SqlSessionFactoryBuilder().build(configuration).openSession(connection);
            try {
                try (Statement statement = connection.createStatement();
                     ResultSet result = statement.executeQuery("SELECT current_setting('transaction_read_only')")) {
                    assertTrue(result.next());
                    assertEquals("on", result.getString(1));
                }
                for (String period : new String[]{"2026-06", "2025-11"}) {
                    PageData params = new PageData();
                    params.put("ACCOUNT_PERIOD", period);
                    params.put("AMT_UNIT", "10000");
                    NewsFlashMapper mapper = session.getMapper(NewsFlashMapper.class);
                    for (String name : new String[]{"getTextAndParams", "getTableParams",
                            "getTableParams2", "getTableParams3"}) {
                        try (Statement statement = connection.createStatement();
                             ResultSet result = statement.executeQuery("EXPLAIN " + boundSql(name, period))) {
                            assertTrue(name + " " + period, result.next());
                            System.out.println("Read-only EXPLAIN passed: " + name + " " + period);
                        }
                        if (!"getTableParams2".equals(name) && !"getTableParams3".equals(name)) {
                            continue;
                        }
                        List<Map<String, Object>> rows = "getTableParams2".equals(name)
                                ? mapper.getTableParams2(params) : mapper.getTableParams3(params);
                        System.out.println("Read-only mapper SELECT passed: " + name + " " + period
                                + " rows=" + rows.size() + " nonnullFields="
                                + (rows.isEmpty() ? 0 : rows.get(0).size()));
                    }
                }
            } finally {
                connection.rollback();
                session.close();
            }
        }
    }

    static String boundSql(String statement, String period) throws Exception {
        Configuration configuration = configuration();
        Map<String, Object> params = new HashMap<>();
        params.put("ACCOUNT_PERIOD", period);
        params.put("AMT_UNIT", "10000");
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("params", params);
        return configuration.getMappedStatement("org.fixedReport.dao.mapper.fixedReport.NewsFlashMapper."
                + statement).getBoundSql(arguments).getSql();
    }

    private static Configuration configuration() throws Exception {
        Configuration configuration = new Configuration();
        configuration.setLogImpl(NoLoggingImpl.class);
        String resource = "mybatis/fixedReport/NewsFlashMapper.xml";
        try (InputStream input = Resources.getResourceAsStream(resource)) {
            new XMLMapperBuilder(input, configuration, resource, configuration.getSqlFragments()).parse();
        }
        return configuration;
    }
}
