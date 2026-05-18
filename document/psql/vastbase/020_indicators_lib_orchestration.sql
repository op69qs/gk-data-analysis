-- Vastbase migration script for the first batch of indicators_lib orchestration procedures
-- Verified target syntax: MySQL-compatible CREATE PROCEDURE ... BEGIN ... END
-- Notes:
--   1. This script prioritizes procedures directly referenced by source MySQL events.
--   2. report.*, edw.*, etl.* routines are still pending in target Vastbase.
--   3. lib_index_formula.exe_* columns are blob in target Vastbase; this script decodes them with CAST(... AS TEXT).

CREATE SCHEMA IF NOT EXISTS indicators_lib;

DROP PROCEDURE IF EXISTS indicators_lib.p_exe_formula_hand;
CREATE PROCEDURE indicators_lib.p_exe_formula_hand(IN v_data_date VARCHAR(20), IN v_index_id VARCHAR(32))
BEGIN
    DECLARE v_proc_name VARCHAR(80) DEFAULT 'INDICATORS_LIB.P_EXE_FORMULA_HAND.PRC';
    DECLARE v_start_time CHAR(19) DEFAULT NOW();
    DECLARE v_step_id INT DEFAULT 0;
    DECLARE p_data_date VARCHAR(8) DEFAULT DATE_FORMAT(v_data_date, '%Y-%m');
    DECLARE done INT DEFAULT 0;
    DECLARE index_id VARCHAR(32);
    DECLARE table_name VARCHAR(200);
    DECLARE delete_sql TEXT;
    DECLARE sesin TEXT;
    DECLARE insert_str TEXT;
    DECLARE where_str TEXT;
    DECLARE sql_text CURSOR FOR
        SELECT
            id,
            index_corre_table,
            CAST(exe_del AS TEXT),
            CAST(exe_insert AS TEXT),
            CAST(exe_sql AS TEXT),
            CAST(exe_where AS TEXT)
        FROM indicators_lib.lib_index_formula
        WHERE id = v_index_id;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

    OPEN sql_text;
    WHILE done <> 1 DO
        FETCH sql_text INTO index_id, table_name, delete_sql, insert_str, sesin, where_str;
        IF done <> 1 THEN
            SET @LAST_DATE = DATE_SUB(v_data_date, INTERVAL 1 YEAR);
            SET @DATA_DATE = v_data_date;
            SET @data_date = v_data_date;
            SET @ID = index_id;

            SET v_step_id = 1;
            SET @dele_sql = delete_sql;
            PREPARE stmt_del FROM @dele_sql;
            EXECUTE stmt_del;
            DEALLOCATE PREPARE stmt_del;

            SET v_step_id = 2;
            SET @insert_sql = CONCAT_WS('', insert_str, sesin, where_str);
            PREPARE stmt_ins FROM @insert_sql;
            EXECUTE stmt_ins;
            DEALLOCATE PREPARE stmt_ins;
        END IF;
    END WHILE;
    CLOSE sql_text;
END;

DROP PROCEDURE IF EXISTS indicators_lib.init_report01;
CREATE PROCEDURE indicators_lib.init_report01(IN v_data_date VARCHAR(10))
BEGIN
    DECLARE s INT DEFAULT 0;
    DECLARE r_id VARCHAR(255);
    DECLARE report CURSOR FOR
        SELECT id
        FROM indicators_lib.lib_index_formula
        ORDER BY (identity_property + 0);
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET s = 1;

    OPEN report;
    FETCH report INTO r_id;
    WHILE s <> 1 DO
        CALL indicators_lib.p_exe_formula_hand(DATE_FORMAT(v_data_date, '%Y%m%d'), r_id);
        FETCH report INTO r_id;
    END WHILE;
    CLOSE report;
END;

DROP PROCEDURE IF EXISTS indicators_lib.init_report03;
CREATE PROCEDURE indicators_lib.init_report03(IN v_data_date VARCHAR(10))
BEGIN
    CALL indicators_lib.p_exe_formula_hand(v_data_date, '7a9dc86298ce11eab404000c298a21af');
    CALL indicators_lib.p_exe_formula_hand(v_data_date, '7a9dc7a698ce11eab404000c298a21af');
    CALL indicators_lib.p_exe_formula_hand(v_data_date, 'e34ac17098ce11eab404000c298a21af');
    CALL indicators_lib.p_exe_formula_hand(v_data_date, 'e34ac13e98ce11eab404000c298a21af');
    CALL indicators_lib.p_exe_formula_hand(v_data_date, '696fc66d98ce11eab404000c298a21af');
    CALL indicators_lib.p_exe_formula_hand(v_data_date, 'e34ac11198ce11eab404000c298a21af');
    CALL indicators_lib.p_exe_formula_hand(v_data_date, 'e34ac0dd98ce11eab404000c298a21af');
    CALL indicators_lib.p_exe_formula_hand(v_data_date, '696fc6a598ce11eab404000c298a21af');

    -- Dependency: indicators_lib.p_trs_budget_income_compare_xin
    CALL indicators_lib.p_trs_budget_income_compare_xin(DATE_FORMAT(v_data_date, '%Y%m'));
END;

DROP PROCEDURE IF EXISTS indicators_lib.p_xunhuan_formula;
CREATE PROCEDURE indicators_lib.p_xunhuan_formula(IN v_batch_date VARCHAR(10))
BEGIN
    DECLARE v_proc_name VARCHAR(80) DEFAULT 'EDW.P_TRS_BUDGET_INCOME_COMPARE.PRC';
    DECLARE v_start_time CHAR(19) DEFAULT NOW();
    DECLARE v_step_id INT DEFAULT 0;
    DECLARE p_data_date VARCHAR(8) DEFAULT DATE_FORMAT(v_batch_date, '%Y-%m');
    DECLARE done INT DEFAULT FALSE;
    DECLARE v_data_date VARCHAR(10);

    DECLARE cur_d_acct CURSOR FOR
        SELECT DISTINCT
            IF(
                DATE_FORMAT(d_acct, '%Y-%m-%d') IS NULL,
                DATE_FORMAT(CONCAT(d_acct, '01'), '%Y-%m-%d'),
                DATE_FORMAT(d_acct, '%Y-%m-%d')
            ) AS d_acct
        FROM (
            SELECT d_acct FROM stg.trs_tmis_budget_income WHERE batch_date = v_batch_date
            UNION ALL
            SELECT d_acct FROM stg.trs_tmis_budget_payout WHERE batch_date = v_batch_date
            UNION ALL
            SELECT d_acct FROM stg.trs_tmis_stock WHERE batch_date = v_batch_date
        ) a
        ORDER BY
            IF(
                DATE_FORMAT(d_acct, '%Y-%m-%d') IS NULL,
                DATE_FORMAT(CONCAT(d_acct, '01'), '%Y-%m-%d'),
                DATE_FORMAT(d_acct, '%Y-%m-%d')
            );

    DECLARE cur_month CURSOR FOR
        SELECT DISTINCT
            IF(
                DATE_FORMAT(d_acct, '%Y-%m-%d') IS NULL,
                DATE_FORMAT(CONCAT(d_acct, '01'), '%Y-%m'),
                DATE_FORMAT(d_acct, '%Y-%m')
            ) AS d_acct
        FROM (
            SELECT d_acct FROM stg.trs_tmis_budget_income WHERE batch_date = v_batch_date
            UNION ALL
            SELECT d_acct FROM stg.trs_tmis_budget_payout WHERE batch_date = v_batch_date
            UNION ALL
            SELECT d_acct FROM stg.trs_tmis_stock WHERE batch_date = v_batch_date
        ) a
        ORDER BY
            IF(
                DATE_FORMAT(d_acct, '%Y-%m-%d') IS NULL,
                DATE_FORMAT(CONCAT(d_acct, '01'), '%Y-%m-%d'),
                DATE_FORMAT(d_acct, '%Y-%m-%d')
            );

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    SET v_step_id = 1;
    UPDATE stg.trs_tmis_budget_income
    SET d_acct = CASE
        WHEN LENGTH(d_acct) = 6 THEN DATE_FORMAT(CONCAT(d_acct, '01'), '%Y-%m-%d')
        ELSE DATE_FORMAT(d_acct, '%Y-%m-%d')
    END;

    SET v_step_id = 2;
    UPDATE stg.trs_tmis_budget_payout
    SET d_acct = CASE
        WHEN LENGTH(d_acct) = 6 THEN DATE_FORMAT(CONCAT(d_acct, '01'), '%Y-%m-%d')
        ELSE DATE_FORMAT(d_acct, '%Y-%m-%d')
    END;

    SET v_step_id = 3;
    UPDATE stg.trs_tmis_stock
    SET trecode = '2200000000',
        tredscr = '国家金库重庆市分库'
    WHERE trecode = 'NNNNNNNNNN';

    SET v_step_id = 4;
    CALL edw.p_trs_budget_income_compare(DATE_FORMAT(v_batch_date, '%Y%m%d'));
    CALL edw.p_trs_budget_income_compare_xin(DATE_FORMAT(v_batch_date, '%Y%m'));

    OPEN cur_d_acct;
    day_loop: LOOP
        FETCH cur_d_acct INTO v_data_date;
        IF done THEN
            LEAVE day_loop;
        END IF;

        SET v_step_id = 5;
        CALL indicators_lib.p_exe_formula_hand(v_data_date, 'e34ac06898ce11eab404000c298a21af');

        SET v_step_id = 6;
        IF v_data_date = CONCAT(DATE_FORMAT(v_data_date, '%Y-%m'), '-01') THEN
            CALL indicators_lib.p_exe_formula(v_data_date);
            CALL report.p_quarter_report_text(CONCAT(YEAR(v_data_date), 'Q', QUARTER(v_data_date)));
            CALL report.p_news_flash_quarter_report_text(CONCAT(YEAR(v_data_date), 'Q', QUARTER(v_data_date)));
        END IF;
    END LOOP;
    CLOSE cur_d_acct;

    SET done = FALSE;

    OPEN cur_month;
    month_loop: LOOP
        FETCH cur_month INTO v_data_date;
        IF done THEN
            LEAVE month_loop;
        END IF;

        SET v_step_id = 7;
        CALL report.p_month_report_text(v_data_date);
        CALL report.p_news_flash_month_report_text(v_data_date);
        CALL report.p_news_flash_month_text_number(CONCAT(v_data_date, '-01'));
    END LOOP;
    CLOSE cur_month;
END;

-- Pending in next batch:
--   1. indicators_lib.init_report02
--   2. indicators_lib.p_exe_formula_history_hand
-- These still need full source-porting and targeted validation.
