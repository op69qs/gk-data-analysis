package org.jeecg.modules.reporting.entity;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
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

    @Test
    public void deliveryIncludesCompatibilityReconciliationMenuGateAndRollbackScripts() throws IOException {
        for (String file : Arrays.asList(
                "07_mysql_vastbase_sql_compatibility_check.sql",
                "08_data_reconciliation_check.sql", "09_menu_permission_seed.sql",
                "10_process_dependency_check.sql", "11_rollback.sql",
                "12_stg_performance_check.sql")) {
            String sql = readResource("db/reporting/" + file).toLowerCase();
            assertTrue(file + " must declare its target", sql.contains("目标"));
            assertTrue(file + " must declare whether it is repeatable", sql.contains("可重复执行"));
        }
    }

    @Test
    public void menuScriptReservesUploaderReviewerOperatorAndAdministratorButtons() throws IOException {
        String sql = readResource("db/reporting/09_menu_permission_seed.sql").toLowerCase();
        for (String permission : Arrays.asList(
                "reporting:batch:upload", "reporting:file:download", "reporting:batch:retry",
                "reporting:batch:process", "reporting:batch:delete", "reporting:batch:audit",
                "reporting:treasury:add", "reporting:treasury:edit",
                "reporting:archive:cleanup")) {
            assertTrue("Menu script is missing reserved permission " + permission, sql.contains(permission));
        }
        assertFalse(sql.contains("('changes', '报送调整记录'"));
        assertFalse(sql.contains("('changes', '新增调整记录'"));
        assertFalse(sql.contains("reportchangerecord"));
    }

    @Test
    public void processCallsHaveDatabaseLevelActiveScopeMutex() throws IOException {
        String sql = readResource("db/reporting/06_report_indexes_constraints.sql").toLowerCase();
        assertTrue(sql.contains("unique index"));
        assertTrue(sql.contains("uk_report_process_call_active"));
        assertTrue(sql.contains("where status in ('queued', 'processing')"));
    }

    @Test
    public void runtimeLockIsDeliveredInCreateUpgradeIndexAndRollbackScripts() throws IOException {
        for (String file : Arrays.asList(
                "05_report_tracking_tables.sql", "06_report_indexes_constraints.sql", "11_rollback.sql")) {
            String sql = readResource("db/reporting/" + file).toLowerCase();
            assertTrue(file + " must include report_runtime_lock", sql.contains("report_runtime_lock"));
        }
        String create = readResource("db/reporting/05_report_tracking_tables.sql").toLowerCase();
        assertTrue(create.contains("'tims_load'"));
    }

    @Test
    public void processDependencyGateDocumentsManualCallOnly() throws IOException {
        String sql = readResource("db/reporting/10_process_dependency_check.sql").toLowerCase();
        assertTrue(sql.contains("人工调用"));
        assertFalse(sql.contains("reporting_auto_process_enabled"));
    }

    @Test
    public void stgPerformanceScriptIsReadOnlyAndCoversAllThreeTargets() throws IOException {
        String sql = readResource("db/reporting/12_stg_performance_check.sql").toLowerCase();
        for (String table : Arrays.asList(
                "stg.trs_tmis_budget_income", "stg.trs_tmis_budget_payout", "stg.trs_tmis_stock")) {
            assertTrue(sql.contains(table));
        }
        String executable = sql.replaceAll("(?m)^\\s*--.*$", "");
        assertTrue(executable.contains("explain"));
        assertFalse(executable.matches("(?s).*\\b(create|alter|drop|insert|update|truncate)\\b.*"));
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
