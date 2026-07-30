-- Vastbase replacement script for 10 MySQL events
-- Preconditions:
--   1. sql_compatibility = B
--   2. enable_prevent_job_task_startup = off
--   3. dependent routines have been migrated and validated
--
-- Notes:
--   1. Vastbase does not allow duplicate event names within one database.
--   2. Event names are prefixed with their source schema for operational clarity.
--   3. Events are created ENABLE to match MySQL object state. Use the global
--      enable_prevent_job_task_startup parameter as the environment gate.

SET search_path TO etl, public;

DROP EVENT IF EXISTS etl_evt_etl_dimnsn_data;
CREATE EVENT IF NOT EXISTS etl_evt_etl_dimnsn_data
ON SCHEDULE EVERY 7 DAY STARTS '2017-07-27 15:00:00'
ON COMPLETION PRESERVE
ENABLE
COMMENT 'source mysql event etl.evt_etl_dimnsn_data'
DO CALL etl.entrance_merge_dimnsn_data();

DROP EVENT IF EXISTS etl_evt_etl_ods_to_dm;
CREATE EVENT IF NOT EXISTS etl_evt_etl_ods_to_dm
ON SCHEDULE EVERY 3 MINUTE STARTS '2017-04-27 09:15:00'
ON COMPLETION PRESERVE
ENABLE
COMMENT 'source mysql event etl.evt_etl_ods_to_dm'
DO CALL etl.entrance_merge_t_jrtj_dim_value_data();

SET search_path TO edw, public;

DROP EVENT IF EXISTS edw_evt_trs_call_edw_budget_data;
CREATE EVENT IF NOT EXISTS edw_evt_trs_call_edw_budget_data
ON SCHEDULE EVERY 1 DAY STARTS '2018-05-21 22:30:01'
ON COMPLETION PRESERVE
ENABLE
COMMENT 'source mysql event edw.EVT_TRS_CALL_EDW_BUDGET_DATA'
DO CALL edw.p_trs_budget_new();

DROP EVENT IF EXISTS edw_evt_trs_call_edw_cp;
CREATE EVENT IF NOT EXISTS edw_evt_trs_call_edw_cp
ON SCHEDULE EVERY 1 DAY STARTS '2018-12-06 17:30:00'
ON COMPLETION PRESERVE
ENABLE
COMMENT 'source mysql event edw.EVT_TRS_CALL_EDW_CP'
DO CALL edw.proc_trs_guoku_cp();

SET search_path TO indicators_lib, public;

DROP EVENT IF EXISTS indicators_lib_p_init_report01;
CREATE EVENT IF NOT EXISTS indicators_lib_p_init_report01
ON SCHEDULE EVERY 1 DAY STARTS '2023-09-05 21:00:00'
ON COMPLETION NOT PRESERVE
ENABLE
COMMENT 'source mysql event indicators_lib.p_init_report01'
DO CALL indicators_lib.init_report01(DATE_FORMAT(LAST_DAY(DATE_ADD(CURDATE(), INTERVAL -1 MONTH)), '%Y-%m-%d'));

DROP EVENT IF EXISTS indicators_lib_p_init_report02;
CREATE EVENT IF NOT EXISTS indicators_lib_p_init_report02
ON SCHEDULE EVERY 1 DAY STARTS '2023-09-05 23:00:00'
ON COMPLETION NOT PRESERVE
ENABLE
COMMENT 'source mysql event indicators_lib.p_init_report02'
DO CALL indicators_lib.init_report02(DATE_FORMAT(LAST_DAY(DATE_ADD(CURDATE(), INTERVAL -1 MONTH)), '%Y-%m-%d'));

DROP EVENT IF EXISTS indicators_lib_p_init_report03;
CREATE EVENT IF NOT EXISTS indicators_lib_p_init_report03
ON SCHEDULE EVERY 1 DAY STARTS '2023-09-06 02:00:00'
ON COMPLETION NOT PRESERVE
ENABLE
COMMENT 'source mysql event indicators_lib.p_init_report03'
DO CALL indicators_lib.init_report03(DATE_FORMAT(LAST_DAY(DATE_ADD(CURDATE(), INTERVAL -1 MONTH)), '%Y-%m-%d'));

DROP EVENT IF EXISTS indicators_lib_p_xunhuan_formula;
CREATE EVENT IF NOT EXISTS indicators_lib_p_xunhuan_formula
ON SCHEDULE EVERY 1 DAY STARTS '2021-04-25 18:00:00'
ON COMPLETION NOT PRESERVE
ENABLE
COMMENT 'source mysql event indicators_lib.p_xunhuan_formula'
DO CALL indicators_lib.p_xunhuan_formula(DATE_FORMAT(CURDATE(), '%Y%m%d'));

SET search_path TO ods, public;

DROP EVENT IF EXISTS ods_pt_gy_files_task;
CREATE EVENT IF NOT EXISTS ods_pt_gy_files_task
ON SCHEDULE EVERY 1 DAY STARTS '2021-02-25 01:00:00'
ON COMPLETION NOT PRESERVE
ENABLE
COMMENT 'source mysql event ods.pt_gy_files_task'
DO CALL ods.p_pt_gy_files_temp();

SET search_path TO visual_screen, public;

DROP EVENT IF EXISTS visual_screen_p_task_vs;
CREATE EVENT IF NOT EXISTS visual_screen_p_task_vs
ON SCHEDULE EVERY 1 DAY STARTS '2021-04-25 18:15:00'
ON COMPLETION NOT PRESERVE
ENABLE
COMMENT 'source mysql event visual_screen.P_task_vs'
DO CALL visual_screen.p_task_vscreen(DATE_FORMAT(CURDATE(), '%Y%m%d'));
