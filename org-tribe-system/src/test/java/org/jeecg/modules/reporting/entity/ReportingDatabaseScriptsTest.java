package org.jeecg.modules.reporting.entity;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ReportingDatabaseScriptsTest {

    @Test
    public void inventoryCoversEveryJarDataWarehouseDependency() throws IOException {
        String sql = readResource("db/reporting/01_schema_object_inventory.sql").toLowerCase();
        List<String> requiredObjects = Arrays.asList(
                "agent_key_file.agent_treatury_config",
                "agent_key_file.agent_keyfile_pending",
                "agent_key_file.agent_file_income",
                "agent_key_file.agent_file_payout",
                "agent_key_file.agent_file_stock",
                "agent_key_file.agent_file_back",
                "agent_key_file.tims_file_pending",
                "agent_key_file.tims_file_income",
                "agent_key_file.tims_file_payout",
                "agent_key_file.tims_file_stock",
                "stg.trs_tmis_budget_income",
                "stg.trs_tmis_budget_payout",
                "stg.trs_tmis_stock",
                "edw.cm_guoku_dimnsn",
                "edw.income_report_detail_stat",
                "edw.payout_report_detail_stat",
                "edw.reprot_update_record",
                "etl.guoku_lib_report_all_log",
                "adm.p_guoku_lib_report_all"
        );

        for (String objectName : requiredObjects) {
            assertTrue("Inventory is missing " + objectName, sql.contains(objectName));
        }
    }

    private String readResource(String path) throws IOException {
        InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        assertNotNull("Missing resource " + path, input);
        try (InputStream stream = input; ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[4096];
            int length;
            while ((length = stream.read(buffer)) >= 0) {
                output.write(buffer, 0, length);
            }
            return new String(output.toByteArray(), StandardCharsets.UTF_8);
        }
    }
}
