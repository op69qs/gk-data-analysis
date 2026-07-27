package org.jeecg.modules.reporting.mapper;

import org.junit.Assume;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Arrays;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * 内网可选只读检查：-Dreporting.vastbase.it=true，并提供三个 REPORTING_VASTBASE_* 环境变量。
 */
public class TimsReportMapperVastbaseIT {
    @Test
    public void requiredTimsTargetsExistInVastbase() throws Exception {
        Assume.assumeTrue("未启用内网 Vastbase 集成检查",
                Boolean.parseBoolean(System.getProperty("reporting.vastbase.it", "false")));
        String url = System.getenv("REPORTING_VASTBASE_JDBC_URL");
        String user = System.getenv("REPORTING_VASTBASE_JDBC_USER");
        String password = System.getenv("REPORTING_VASTBASE_JDBC_PASSWORD");
        assertNotNull("缺少 REPORTING_VASTBASE_JDBC_URL", url);
        assertNotNull("缺少 REPORTING_VASTBASE_JDBC_USER", user);
        assertNotNull("缺少 REPORTING_VASTBASE_JDBC_PASSWORD", password);

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            DatabaseMetaData metadata = connection.getMetaData();
            for (String qualified : Arrays.asList(
                    "agent_key_file.tims_file_income", "agent_key_file.tims_file_payout",
                    "agent_key_file.tims_file_stock", "stg.trs_tmis_budget_income",
                    "stg.trs_tmis_budget_payout", "stg.trs_tmis_stock")) {
                String[] parts = qualified.split("\\.");
                try (ResultSet tables = metadata.getTables(null, parts[0], parts[1], new String[]{"TABLE", "VIEW"})) {
                    assertTrue("Vastbase 缺少对象：" + qualified, tables.next());
                }
            }
        }
    }
}
