import tempfile
import unittest
from pathlib import Path
import sys

sys.path.insert(0, str(Path(__file__).resolve().parent))
from convert_adm_mysql_dump import convert_dump, convert_table_block, normalize_etl_log_calls


class ConvertAdmMysqlDumpTest(unittest.TestCase):
    def test_converts_table_to_lowercase_vastbase_ddl(self):
        source = """DROP TABLE IF EXISTS `Demo_Task`;
CREATE TABLE `Demo_Task`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `TABLE` varchar(20) NULL,
  `LOCATE('*',c.cell)` bigint(11) NULL,
  `Updated_At` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `Payload` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL,
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `idx_updated`(`Updated_At`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin;
"""

        actual = convert_table_block(source)

        self.assertIn("DROP TABLE IF EXISTS adm.demo_task;", actual)
        self.assertIn("CREATE TABLE adm.demo_task", actual)
        self.assertIn("id bigserial NOT NULL", actual)
        self.assertIn('"table" varchar(20) NULL', actual)
        self.assertIn('"locate(\'*\',c.cell)" bigint NULL', actual)
        self.assertIn("updated_at timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP", actual)
        self.assertIn("payload text NULL", actual)
        self.assertIn("PRIMARY KEY (id)", actual)
        self.assertIn("CREATE INDEX demo_task_idx_updated ON adm.demo_task (updated_at);", actual)
        for mysql_only in ("`", "ENGINE", "CHARACTER SET", "COLLATE", "ON UPDATE", "USING BTREE"):
            self.assertNotIn(mysql_only, actual)

    def test_splits_dump_and_generates_all_object_groups(self):
        source = """SET NAMES utf8mb4;
-- Table structure for Exec_Shell_Task
DROP TABLE IF EXISTS `Exec_Shell_Task`;
CREATE TABLE `Exec_Shell_Task` (`ID` varchar(40) NOT NULL, PRIMARY KEY (`ID`) USING BTREE) ENGINE=InnoDB;
-- View structure for Task_View
DROP VIEW IF EXISTS `Task_View`;
CREATE ALGORITHM = UNDEFINED DEFINER = `dis`@`%` SQL SECURITY DEFINER VIEW `Task_View` AS select `ID` from `Exec_Shell_Task`;
-- Procedure structure for Run_Task
DROP PROCEDURE IF EXISTS `Run_Task`;
delimiter ;;
CREATE DEFINER=`root`@`%` PROCEDURE `Run_Task`(IN P_ID VARCHAR(40))
BEGIN
  ## MySQL-only line comment
  SELECT P_ID;
END
;;
delimiter ;
-- Function structure for Task_Name
DROP FUNCTION IF EXISTS `Task_Name`;
delimiter ;;
CREATE DEFINER=`root`@`%` FUNCTION `Task_Name`() RETURNS varchar(20) CHARSET utf8
BEGIN
  RETURN 'ok';
END
;;
delimiter ;
-- Event structure for Run_Daily
DROP EVENT IF EXISTS `Run_Daily`;
delimiter ;;
CREATE DEFINER = `root`@`%` EVENT `Run_Daily`
ON SCHEDULE EVERY '1' DAY STARTS '2020-01-01 00:00:00'
DO CALL adm.Run_Task('1')
;;
delimiter ;
SET FOREIGN_KEY_CHECKS = 1;
"""
        with tempfile.TemporaryDirectory() as directory:
            source_path = Path(directory) / "adm.sql"
            output_dir = Path(directory) / "final"
            source_path.write_text(source, encoding="utf-8")

            counts = convert_dump(source_path, output_dir)
            tables = (output_dir / "013_adm_tables_init.sql").read_text(encoding="utf-8")
            indexes = (output_dir / "014_adm_indexes_init.sql").read_text(encoding="utf-8")
            routines = (output_dir / "015_adm_routines_init.sql").read_text(encoding="utf-8")
            events = (output_dir / "016_adm_events_init.sql").read_text(encoding="utf-8")

        self.assertEqual({"tables": 1, "views": 1, "procedures": 1, "functions": 1, "events": 1}, counts)
        self.assertIn("CREATE VIEW adm.task_view AS select id from adm.exec_shell_task", tables)
        self.assertNotIn("CREATE INDEX", tables)
        self.assertEqual("", "\n".join(line for line in indexes.splitlines() if line.startswith("CREATE INDEX")))
        self.assertIn("CREATE PROCEDURE adm.run_task", routines)
        self.assertIn("CREATE FUNCTION adm.task_name", routines)
        self.assertNotIn("DEFINER", routines)
        self.assertNotIn("delimiter", routines.lower())
        self.assertNotIn("## MySQL-only", routines)
        self.assertIn("-- MySQL-only line comment", routines)
        self.assertIn("\n/\n", routines)
        self.assertIn("END;\n/", routines)
        self.assertIn("CREATE EVENT adm_run_daily", events)
        self.assertIn("DISABLE", events)
        self.assertIn("DO CALL adm.run_task('1')", events)
        self.assertNotIn("FOREIGN_KEY_CHECKS", events)

    def test_normalizes_etl_log_calls_without_splitting_nested_function_arguments(self):
        source = """CALL etl.edw_proc_error_log(V_ACCT_ID,V_AREA_ID,V_ORG_ID,V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,V_CODE,V_MSG);
CALL etl.edw_proc_trace_log(DATE_FORMAT(s_date,'%Y%m%d'),V_ORG_ID,V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());"""

        actual = normalize_etl_log_calls(source)

        self.assertIn(
            "CALL etl.edw_proc_error_log(V_ACCT_ID,V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,V_CODE,V_MSG);",
            actual,
        )
        self.assertIn(
            "CALL etl.edw_proc_trace_log(DATE_FORMAT(s_date,'%Y%m%d'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());",
            actual,
        )

    def test_full_dump_inventory_is_preserved(self):
        source_path = Path(__file__).resolve().parents[1] / "mysql" / "adm.sql"
        if not source_path.exists():
            self.skipTest("adm.sql is not present")

        with tempfile.TemporaryDirectory() as directory:
            counts = convert_dump(source_path, Path(directory))

        self.assertEqual(
            {"tables": 330, "views": 2, "procedures": 117, "functions": 2, "events": 5},
            counts,
        )


if __name__ == "__main__":
    unittest.main()
