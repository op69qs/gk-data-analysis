-- Vastbase replacement script for all 5 MySQL ADM events.
-- Events are ENABLE to match the MySQL source object state. Use
-- enable_prevent_job_task_startup as the environment-wide execution gate.
CREATE SCHEMA IF NOT EXISTS adm;
SET search_path TO adm, public;

DROP EVENT IF EXISTS adm_enterprise_survey_1;

CREATE EVENT IF NOT EXISTS adm_enterprise_survey_1
ON SCHEDULE
EVERY 1 DAY STARTS '2020-07-21 22:00:00'
ON COMPLETION NOT PRESERVE
ENABLE
COMMENT 'source mysql event adm.ENTERPRISE_SURVEY_1'
DO CALL adm.p_ana_sust_mth_enterprise_survey_temp(DATE_FORMAT(NOW(),'%Y%m'));

DROP EVENT IF EXISTS adm_enterprise_survey_2;

CREATE EVENT IF NOT EXISTS adm_enterprise_survey_2
ON SCHEDULE
EVERY 1 DAY STARTS '2020-07-21 23:00:00'
ON COMPLETION NOT PRESERVE
ENABLE
COMMENT 'source mysql event adm.ENTERPRISE_SURVEY_2'
DO CALL adm.p_ana_sust_mth_enterprise_survey(DATE_FORMAT(NOW(),'%Y%m'),'','');

DROP EVENT IF EXISTS adm_e_sust_update;

CREATE EVENT IF NOT EXISTS adm_e_sust_update
ON SCHEDULE
EVERY 1 DAY STARTS '2021-02-02 22:00:00'
ON COMPLETION PRESERVE
ENABLE
COMMENT 'source mysql event adm.e_sust_update'
DO CALL adm.p_ana_sust_update (CURDATE());

DROP EVENT IF EXISTS adm_p_trs_stat_agentbankpay_back_detail;

CREATE EVENT IF NOT EXISTS adm_p_trs_stat_agentbankpay_back_detail
ON SCHEDULE
EVERY 1 DAY STARTS '2020-10-26 00:00:00'
ON COMPLETION NOT PRESERVE
ENABLE
COMMENT 'source mysql event adm.p_trs_stat_agentbankpay_back_detail'
DO CALL adm.p_trs_stat_agentbankpay_back_detail();

DROP EVENT IF EXISTS adm_p_trs_stat_agentbankpay_detail;

CREATE EVENT IF NOT EXISTS adm_p_trs_stat_agentbankpay_detail
ON SCHEDULE
EVERY 1 DAY STARTS '2020-10-26 00:00:00'
ON COMPLETION NOT PRESERVE
ENABLE
COMMENT 'source mysql event adm.p_trs_stat_agentbankpay_detail'
DO CALL adm.p_trs_stat_agentbankpay_detail();
