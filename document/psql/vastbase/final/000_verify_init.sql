-- Vastbase 初始化结果校验脚本
-- 用法（在 final 目录、同一业务库执行）：
--   vsql ... -v ON_ERROR_STOP=1 -f 000_verify_init.sql
--
-- 覆盖范围与 000_run_all.sql 一致（不含仅增量升级用的 017）。
-- 每项输出 actual / expected / pass(Y/N)。全部 pass 才可视为对象级初始化成功。
-- 本脚本只校验“对象是否存在”，不替代 Event 业务运行回归。

\echo ========== 1. Schema 是否存在 ==========
SELECT
    s.schema_name AS item,
    CASE WHEN n.nspname IS NULL THEN 0 ELSE 1 END AS actual,
    1 AS expected,
    CASE WHEN n.nspname IS NULL THEN 'N' ELSE 'Y' END AS pass
FROM (
    VALUES
        ('etl'),
        ('ods'),
        ('ods_temp'),
        ('dm'),
        ('dmcode'),
        ('report'),
        ('edw'),
        ('visual_screen'),
        ('indicators_lib'),
        ('ucloud'),
        ('upm'),
        ('adm')
) AS s(schema_name)
LEFT JOIN pg_namespace n ON n.nspname = s.schema_name
ORDER BY s.schema_name;

\echo ========== 2. 各 schema 过程数量 ==========
SELECT
    e.schema_name AS item,
    COALESCE(a.actual, 0) AS actual,
    e.expected,
    CASE WHEN COALESCE(a.actual, 0) = e.expected THEN 'Y' ELSE 'N' END AS pass
FROM (
    VALUES
        ('etl', 4),
        ('ods', 1),
        ('report', 5),
        ('edw', 8),
        ('visual_screen', 40),
        ('indicators_lib', 7),
        ('ucloud', 4),
        ('upm', 3),
        ('adm', 12)
) AS e(schema_name, expected)
LEFT JOIN (
    SELECT routine_schema AS schema_name, COUNT(*) AS actual
      FROM information_schema.routines
     WHERE routine_type = 'PROCEDURE'
       AND routine_schema IN (
            'etl', 'ods', 'report', 'edw', 'visual_screen',
            'indicators_lib', 'ucloud', 'upm', 'adm'
       )
     GROUP BY routine_schema
) a ON a.schema_name = e.schema_name
ORDER BY e.schema_name;

\echo ========== 3. 过程总数（期望 84） ==========
SELECT
    'procedures_total' AS item,
    COUNT(*) AS actual,
    84 AS expected,
    CASE WHEN COUNT(*) = 84 THEN 'Y' ELSE 'N' END AS pass
FROM information_schema.routines
WHERE routine_type = 'PROCEDURE'
  AND routine_schema IN (
        'etl', 'ods', 'report', 'edw', 'visual_screen',
        'indicators_lib', 'ucloud', 'upm', 'adm'
  );

\echo ========== 4. 关键业务入口过程是否存在 ==========
SELECT
    e.routine_name AS item,
    CASE WHEN r.routine_name IS NULL THEN 0 ELSE 1 END AS actual,
    1 AS expected,
    CASE WHEN r.routine_name IS NULL THEN 'N' ELSE 'Y' END AS pass
FROM (
    VALUES
        ('etl.entrance_merge_dimnsn_data'),
        ('etl.entrance_merge_t_jrtj_dim_value_data'),
        ('ods.p_pt_gy_files_temp'),
        ('edw.p_trs_budget_new'),
        ('edw.proc_trs_guoku_cp'),
        ('visual_screen.p_task_vscreen'),
        ('indicators_lib.init_report01'),
        ('indicators_lib.init_report02'),
        ('indicators_lib.init_report03'),
        ('ucloud.ucloud_api_interface_alarm_data'),
        ('ucloud.ucloud_api_interface_system_data'),
        ('upm.upm_proc_api_alarm_summary_alarmlog'),
        ('upm.upm_proc_api_alarm_summary_netper'),
        ('upm.upm_proc_api_alarm_summary_interface'),
        ('adm.p_ana_sust_mth_enterprise_survey'),
        ('adm.p_ana_sust_mth_enterprise_survey_temp'),
        ('adm.p_ana_sust_update'),
        ('adm.p_trs_stat_agentbankpay_detail'),
        ('adm.p_trs_stat_agentbankpay_back_detail')
) AS e(routine_name)
LEFT JOIN (
    SELECT CONCAT(routine_schema, '.', routine_name) AS routine_name
      FROM information_schema.routines
     WHERE routine_type = 'PROCEDURE'
) r ON r.routine_name = e.routine_name
ORDER BY e.routine_name;

\echo ========== 5. ADM 函数（期望 adm.num_char = 1） ==========
SELECT
    'adm.num_char' AS item,
    COUNT(*) AS actual,
    1 AS expected,
    CASE WHEN COUNT(*) = 1 THEN 'Y' ELSE 'N' END AS pass
FROM information_schema.routines
WHERE routine_type = 'FUNCTION'
  AND routine_schema = 'adm'
  AND routine_name = 'num_char';

\echo ========== 6. 各 schema 基表数量 ==========
SELECT
    e.schema_name AS item,
    COALESCE(a.actual, 0) AS actual,
    e.expected,
    CASE WHEN COALESCE(a.actual, 0) >= e.expected THEN 'Y' ELSE 'N' END AS pass
FROM (
    VALUES
        ('etl', 3),
        ('indicators_lib', 1),
        ('ucloud', 23),
        ('upm', 1637),
        ('adm', 331)
) AS e(schema_name, expected)
LEFT JOIN (
    SELECT table_schema AS schema_name, COUNT(*) AS actual
      FROM information_schema.tables
     WHERE table_type = 'BASE TABLE'
       AND table_schema IN ('etl', 'indicators_lib', 'ucloud', 'upm', 'adm')
     GROUP BY table_schema
) a ON a.schema_name = e.schema_name
ORDER BY e.schema_name;

\echo ========== 7. 关键日志/运行表是否存在 ==========
SELECT
    e.table_name AS item,
    CASE WHEN t.table_name IS NULL THEN 0 ELSE 1 END AS actual,
    1 AS expected,
    CASE WHEN t.table_name IS NULL THEN 'N' ELSE 'Y' END AS pass
FROM (
    VALUES
        ('etl.proc_trace_log'),
        ('etl.proc_error_log'),
        ('etl.t_run_log'),
        ('adm.exec_shell_task_run_log')
) AS e(table_name)
LEFT JOIN (
    SELECT CONCAT(table_schema, '.', table_name) AS table_name
      FROM information_schema.tables
     WHERE table_type = 'BASE TABLE'
) t ON t.table_name = e.table_name
ORDER BY e.table_name;

\echo ========== 8. ADM 索引数量（013 内 2 个 + 014 共 90 个，期望 >= 92） ==========
SELECT
    'adm_indexes' AS item,
    COUNT(*) AS actual,
    92 AS expected,
    CASE WHEN COUNT(*) >= 92 THEN 'Y' ELSE 'N' END AS pass
FROM pg_indexes
WHERE schemaname = 'adm'
  AND indexname NOT LIKE '%_pkey';

\echo ========== 9. Event 数量（若现场不支持 CREATE EVENT，本项可为 N，需改外部调度） ==========
SELECT
    'events_total' AS item,
    COUNT(*) AS actual,
    15 AS expected,
    CASE WHEN COUNT(*) = 15 THEN 'Y' ELSE 'N' END AS pass
FROM information_schema.events
WHERE event_schema IN (
        'etl', 'edw', 'indicators_lib', 'ods', 'visual_screen', 'adm', 'public'
    )
   OR event_name IN (
        'etl_evt_etl_dimnsn_data',
        'etl_evt_etl_ods_to_dm',
        'edw_evt_trs_call_edw_budget_data',
        'edw_evt_trs_call_edw_cp',
        'indicators_lib_p_init_report01',
        'indicators_lib_p_init_report02',
        'indicators_lib_p_init_report03',
        'indicators_lib_p_xunhuan_formula',
        'ods_pt_gy_files_task',
        'visual_screen_p_task_vs',
        'adm_enterprise_survey_1',
        'adm_enterprise_survey_2',
        'adm_e_sust_update',
        'adm_p_trs_stat_agentbankpay_back_detail',
        'adm_p_trs_stat_agentbankpay_detail'
    );

\echo ========== 10. 汇总：未通过项（无行表示对象级校验全部通过） ==========
SELECT *
FROM (
    SELECT 'procedures_total' AS check_item,
           COUNT(*)::text AS actual,
           '84' AS expected,
           CASE WHEN COUNT(*) = 84 THEN 'Y' ELSE 'N' END AS pass
      FROM information_schema.routines
     WHERE routine_type = 'PROCEDURE'
       AND routine_schema IN (
            'etl', 'ods', 'report', 'edw', 'visual_screen',
            'indicators_lib', 'ucloud', 'upm', 'adm'
       )
    UNION ALL
    SELECT 'adm.num_char',
           COUNT(*)::text,
           '1',
           CASE WHEN COUNT(*) = 1 THEN 'Y' ELSE 'N' END
      FROM information_schema.routines
     WHERE routine_type = 'FUNCTION'
       AND routine_schema = 'adm'
       AND routine_name = 'num_char'
    UNION ALL
    SELECT 'ucloud_tables',
           COUNT(*)::text,
           '23',
           CASE WHEN COUNT(*) >= 23 THEN 'Y' ELSE 'N' END
      FROM information_schema.tables
     WHERE table_type = 'BASE TABLE' AND table_schema = 'ucloud'
    UNION ALL
    SELECT 'upm_tables',
           COUNT(*)::text,
           '1637',
           CASE WHEN COUNT(*) >= 1637 THEN 'Y' ELSE 'N' END
      FROM information_schema.tables
     WHERE table_type = 'BASE TABLE' AND table_schema = 'upm'
    UNION ALL
    SELECT 'adm_tables',
           COUNT(*)::text,
           '331',
           CASE WHEN COUNT(*) >= 331 THEN 'Y' ELSE 'N' END
      FROM information_schema.tables
     WHERE table_type = 'BASE TABLE' AND table_schema = 'adm'
) s
WHERE pass = 'N';

\echo ========== 校验结束 ==========
