SET search_path TO indicators_lib, public;

DROP PROCEDURE IF EXISTS indicators_lib.p_exe_formula_hand;
CREATE PROCEDURE indicators_lib.p_exe_formula_hand(IN v_data_date VARCHAR(20), IN v_index_id VARCHAR(32))
BEGIN
    DECLARE v_proc_name VARCHAR(80) DEFAULT 'INDICATORS_LIB.P_EXE_FORMULA_HAND.PRC';
    DECLARE v_start_time CHAR(19) DEFAULT NOW();
    DECLARE v_step_id INT DEFAULT 0;
    DECLARE p_data_date VARCHAR(8) DEFAULT DATE_FORMAT(v_data_date, '%Y-%m');
    DECLARE index_id VARCHAR(32);
    DECLARE table_name VARCHAR(200);
    DECLARE delete_sql TEXT;
    DECLARE sesin TEXT;
    DECLARE insert_str TEXT;
    DECLARE where_str TEXT;
    DECLARE delete_exec TEXT;
    DECLARE insert_exec TEXT;
    DECLARE quoted_data_date TEXT;
    DECLARE quoted_last_date TEXT;
    DECLARE quoted_id TEXT;
    DECLARE v_return_code TEXT;
    DECLARE v_error_msg TEXT;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        GET DIAGNOSTICS CONDITION 1 v_return_code = RETURNED_SQLSTATE, v_error_msg = MESSAGE_TEXT;
        CALL ETL.EDW_PROC_ERROR_LOG(p_data_date, v_start_time, NOW(), v_proc_name, v_step_id, v_return_code, v_error_msg);
    END;

    SELECT
        id,
        index_corre_table,
        CAST(exe_del AS TEXT),
        CAST(exe_insert AS TEXT),
        CAST(exe_sql AS TEXT),
        CAST(exe_where AS TEXT)
    INTO index_id, table_name, delete_sql, insert_str, sesin, where_str
    FROM indicators_lib.lib_index_formula
    WHERE id = v_index_id
    LIMIT 1;

    SET quoted_data_date = CONCAT('''', DATE_FORMAT(v_data_date, '%Y-%m-%d'), '''');
    SET quoted_last_date = CONCAT('''', DATE_FORMAT(DATE_SUB(v_data_date, INTERVAL 1 YEAR), '%Y-%m-%d'), '''');
    SET quoted_id = CONCAT('''', v_index_id, '''');

    SET delete_exec = COALESCE(delete_sql, 'SELECT 1');
    SET delete_exec = REPLACE(delete_exec, '@LAST_DATE', quoted_last_date);
    SET delete_exec = REPLACE(delete_exec, '@DATA_DATE', quoted_data_date);
    SET delete_exec = REPLACE(delete_exec, '@data_date', quoted_data_date);
    SET delete_exec = REPLACE(delete_exec, '@ID', quoted_id);

    SET insert_exec = COALESCE(CONCAT_WS('', insert_str, sesin, where_str), 'SELECT 1');
    SET insert_exec = REPLACE(insert_exec, '@LAST_DATE', quoted_last_date);
    SET insert_exec = REPLACE(insert_exec, '@DATA_DATE', quoted_data_date);
    SET insert_exec = REPLACE(insert_exec, '@data_date', quoted_data_date);
    SET insert_exec = REPLACE(insert_exec, '@ID', quoted_id);

    SET v_step_id = 1;
    EXECUTE IMMEDIATE delete_exec;
    CALL ETL.EDW_PROC_TRACE_LOG(p_data_date, v_start_time, NOW(), v_proc_name, v_step_id, ROW_COUNT());

    SET v_step_id = 2;
    EXECUTE IMMEDIATE insert_exec;
    CALL ETL.EDW_PROC_TRACE_LOG(p_data_date, v_start_time, NOW(), v_proc_name, v_step_id, ROW_COUNT());
END
;

DROP PROCEDURE IF EXISTS indicators_lib.p_exe_formula;
CREATE PROCEDURE indicators_lib.p_exe_formula(IN V_DATA_DATE VARCHAR(20))
BEGIN
    DECLARE v_sql TEXT DEFAULT '';

    SET v_sql = COALESCE((
        SELECT string_agg(stmt, ' ')
        FROM (
            SELECT CONCAT(
                'CALL indicators_lib.p_exe_formula_hand(''',
                DATE_FORMAT(V_DATA_DATE, '%Y-%m-%d'),
                ''',''',
                id,
                ''');'
            ) AS stmt
            FROM indicators_lib.lib_index_formula
            ORDER BY identity_property
        ) call_queue
    ), 'SELECT 1');

    EXECUTE IMMEDIATE v_sql;
END
;

DROP PROCEDURE IF EXISTS indicators_lib.init_report01;
CREATE PROCEDURE indicators_lib.init_report01(IN v_data_date VARCHAR(10))
BEGIN
    CALL indicators_lib.p_exe_formula(DATE_FORMAT(v_data_date, '%Y%m%d'));
END
;

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
    CALL indicators_lib.P_TRS_BUDGET_INCOME_COMPARE_XIN(DATE_FORMAT(v_data_date, '%Y%m'));
END
;

DROP PROCEDURE IF EXISTS indicators_lib.p_xunhuan_formula;
CREATE PROCEDURE indicators_lib.p_xunhuan_formula(IN v_batch_date VARCHAR(10))
BEGIN
    DECLARE v_proc_name VARCHAR(80) DEFAULT 'INDICATORS_LIB.P_XUNHUAN_FORMULA.PRC';
    DECLARE v_start_time CHAR(19) DEFAULT NOW();
    DECLARE v_step_id INT DEFAULT 0;
    DECLARE p_data_date VARCHAR(8) DEFAULT DATE_FORMAT(v_batch_date, '%Y-%m');
    DECLARE v_daily_sql TEXT DEFAULT '';
    DECLARE v_month_sql TEXT DEFAULT '';
    DECLARE v_return_code TEXT;
    DECLARE v_error_msg TEXT;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        GET DIAGNOSTICS CONDITION 1 v_return_code = RETURNED_SQLSTATE, v_error_msg = MESSAGE_TEXT;
        CALL ETL.EDW_PROC_ERROR_LOG(p_data_date, v_start_time, NOW(), v_proc_name, v_step_id, v_return_code, v_error_msg);
    END;

    SET v_step_id = 1;
    UPDATE STG.TRS_TMIS_BUDGET_INCOME
    SET D_ACCT = CASE
        WHEN LENGTH(D_ACCT) = 6 THEN DATE_FORMAT(CONCAT(D_ACCT, '01'), '%Y-%m-%d')
        ELSE DATE_FORMAT(D_ACCT, '%Y-%m-%d')
    END;

    SET v_step_id = 2;
    UPDATE STG.TRS_TMIS_BUDGET_PAYOUT
    SET D_ACCT = CASE
        WHEN LENGTH(D_ACCT) = 6 THEN DATE_FORMAT(CONCAT(D_ACCT, '01'), '%Y-%m-%d')
        ELSE DATE_FORMAT(D_ACCT, '%Y-%m-%d')
    END;

    SET v_step_id = 3;
    UPDATE STG.TRS_TMIS_STOCK
    SET TRECODE = '2200000000',
        TREDSCR = '国家金库重庆市分库'
    WHERE TRECODE = 'NNNNNNNNNN';

    SET v_step_id = 4;
    CALL edw.P_TRS_BUDGET_INCOME_COMPARE(DATE_FORMAT(v_batch_date, '%Y%m%d'));
    CALL indicators_lib.P_TRS_BUDGET_INCOME_COMPARE_XIN(DATE_FORMAT(v_batch_date, '%Y%m'));

    SET v_step_id = 5;
    SET v_daily_sql = COALESCE((
        SELECT string_agg(stmt, ' ')
        FROM (
            SELECT CONCAT(
                'CALL indicators_lib.p_exe_formula_hand(''',
                d_acct,
                ''',''e34ac06898ce11eab404000c298a21af''); ',
                CASE
                    WHEN d_acct = CONCAT(DATE_FORMAT(d_acct, '%Y-%m'), '-01') THEN CONCAT(
                        'CALL indicators_lib.p_exe_formula(''', d_acct, '''); ',
                        'CALL report.P_QUARTER_REPORT_TEXT(''', CONCAT(YEAR(d_acct), 'Q', QUARTER(d_acct)), '''); ',
                        'CALL report.P_NEWS_FLASH_QUARTER_REPORT_TEXT(''', CONCAT(YEAR(d_acct), 'Q', QUARTER(d_acct)), '''); '
                    )
                    ELSE ''
                END
            ) AS stmt
            FROM (
                SELECT DISTINCT
                    IF(
                        DATE_FORMAT(d_acct, '%Y-%m-%d') IS NULL,
                        DATE_FORMAT(CONCAT(d_acct, '01'), '%Y-%m-%d'),
                        DATE_FORMAT(d_acct, '%Y-%m-%d')
                    ) AS d_acct
                FROM (
                    SELECT D_ACCT
                    FROM STG.TRS_TMIS_BUDGET_INCOME
                    WHERE BATCH_DATE = v_batch_date

                    UNION ALL

                    SELECT D_ACCT
                    FROM STG.TRS_TMIS_BUDGET_PAYOUT
                    WHERE BATCH_DATE = v_batch_date

                    UNION ALL

                    SELECT D_ACCT
                    FROM STG.TRS_TMIS_STOCK
                    WHERE BATCH_DATE = v_batch_date
                ) raw_dates
            ) distinct_dates
            ORDER BY d_acct
        ) call_queue
    ), 'SELECT 1');
    EXECUTE IMMEDIATE v_daily_sql;

    SET v_step_id = 6;
    SET v_month_sql = COALESCE((
        SELECT string_agg(stmt, ' ')
        FROM (
            SELECT CONCAT(
                'CALL report.P_MONTH_REPORT_TEXT(''', d_acct, '''); ',
                'CALL report.P_NEWS_FLASH_MONTH_REPORT_TEXT(''', d_acct, '''); ',
                'CALL report.P_NEWS_FLASH_MONTH_TEXT_NUMBER(''', CONCAT(d_acct, '-01'), '''); '
            ) AS stmt
            FROM (
                SELECT DISTINCT
                    IF(
                        DATE_FORMAT(d_acct, '%Y-%m-%d') IS NULL,
                        DATE_FORMAT(CONCAT(d_acct, '01'), '%Y-%m'),
                        DATE_FORMAT(d_acct, '%Y-%m')
                    ) AS d_acct
                FROM (
                    SELECT D_ACCT
                    FROM STG.TRS_TMIS_BUDGET_INCOME
                    WHERE BATCH_DATE = v_batch_date

                    UNION ALL

                    SELECT D_ACCT
                    FROM STG.TRS_TMIS_BUDGET_PAYOUT
                    WHERE BATCH_DATE = v_batch_date

                    UNION ALL

                    SELECT D_ACCT
                    FROM STG.TRS_TMIS_STOCK
                    WHERE BATCH_DATE = v_batch_date
                ) raw_dates
            ) distinct_months
            ORDER BY d_acct
        ) call_queue
    ), 'SELECT 1');
    EXECUTE IMMEDIATE v_month_sql;
END
;
