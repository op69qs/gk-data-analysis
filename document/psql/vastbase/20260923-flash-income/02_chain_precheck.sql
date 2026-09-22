-- 目标：上报前检查链路对象、过程、定时任务及时间信息是否齐全
-- 可重复执行：是（只读）
-- 执行时间请记录：开始 ________  结束 ________  库 ________
-- 反馈本脚本全部结果集

-- ========== A. 服务器时间与会话 ==========
SELECT CURRENT_TIMESTAMP AS server_now,
       CURRENT_DATE AS server_date,
       TO_CHAR(CURRENT_TIMESTAMP, 'YYYYMMDD') AS batch_date_today,
       current_database() AS db_name,
       current_user AS db_user,
       inet_server_addr() AS server_addr;

-- ========== B. STG 贴源表 ==========
SELECT 'stg.trs_tmis_budget_income_provinces' AS object_name,
       to_regclass('stg.trs_tmis_budget_income_provinces') AS exists_regclass,
       CASE WHEN to_regclass('stg.trs_tmis_budget_income_provinces') IS NULL THEN 'N' ELSE 'Y' END AS pass;

SELECT column_name, data_type, character_maximum_length, numeric_precision, numeric_scale, is_nullable
  FROM information_schema.columns
 WHERE table_schema = 'stg' AND table_name = 'trs_tmis_budget_income_provinces'
 ORDER BY ordinal_position;

-- ========== C. 大屏编排过程（final/005） ==========
SELECT n.nspname AS schema_name,
       p.proname AS proc_name,
       pg_get_userbyid(p.proowner) AS owner,
       CASE WHEN p.prosrc IS NULL OR length(p.prosrc) = 0 THEN 'EMPTY' ELSE 'HAS_BODY' END AS body_flag
  FROM pg_proc p
  JOIN pg_namespace n ON n.oid = p.pronamespace
 WHERE n.nspname = 'visual_screen'
   AND lower(p.proname) IN (
       'p_task_vscreen',
       'p_task_vscreen_daily',
       'p_task_vscreen_month_end',
       'p_vs_revenu_display',
       'p_vs_amount_ranking_by_region',
       'p_vs_regional_taxation',
       'p_vs_municipalities_directly',
       'p_vs_five_provinces_in_southwest_china'
   )
 ORDER BY 1, 2;

-- 期望清单对照
SELECT e.item,
       CASE WHEN EXISTS (
              SELECT 1
                FROM pg_proc p
                JOIN pg_namespace n ON n.oid = p.pronamespace
               WHERE n.nspname = split_part(e.item, '.', 1)
                 AND lower(p.proname) = lower(split_part(e.item, '.', 2))
            ) THEN 'Y' ELSE 'N' END AS pass
  FROM (VALUES
        ('visual_screen.p_task_vscreen'),
        ('visual_screen.p_task_vscreen_daily'),
        ('visual_screen.p_task_vscreen_month_end'),
        ('visual_screen.p_vs_revenu_display'),
        ('visual_screen.p_vs_amount_ranking_by_region'),
        ('visual_screen.p_vs_regional_taxation'),
        ('visual_screen.p_vs_municipalities_directly'),
        ('visual_screen.p_vs_five_provinces_in_southwest_china')
  ) AS e(item)
 ORDER BY e.item;

-- ========== D. vs_* 结果表 ==========
SELECT table_schema, table_name
  FROM information_schema.tables
 WHERE table_schema = 'visual_screen'
   AND table_name IN (
       'vs_revenu_display',
       'vs_amount_ranking_by_region',
       'vs_regional_taxation',
       'vs_municipalities_directly',
       'vs_five_provinces_in_southwest_china'
   )
 ORDER BY table_name;

-- ========== E. 定时任务 / Event（Vastbase pg_job） ==========
-- 关注 visual_screen_p_task_vs：enable、interval、上次成功时间、下次运行时间
SELECT job_id,
       dbname,
       nspname,
       job_name,
       enable,
       job_status,
       interval,
       start_date,
       next_run_date,
       last_start_date,
       last_end_date,
       last_suc_date,
       this_run_date,
       failure_count,
       failure_msg
  FROM pg_job
 WHERE dbname = current_database()
    OR job_name ILIKE '%visual_screen%'
    OR job_name ILIKE '%vscreen%'
    OR job_name ILIKE '%p_task_vs%'
 ORDER BY dbname, job_name;

SELECT CASE
         WHEN EXISTS (
           SELECT 1 FROM pg_job
            WHERE dbname = current_database()
              AND job_name = 'visual_screen_p_task_vs'
              AND enable = true
         ) THEN 'Y'
         ELSE 'N'
       END AS event_visual_screen_p_task_vs_enabled_in_current_db,
       CURRENT_TIMESTAMP AS check_time;

-- ========== F. 上报跟踪表（异步批次，与收支存共用 agent_key_file） ==========
SELECT to_regclass('agent_key_file.report_batch') AS report_batch,
       to_regclass('agent_key_file.report_task') AS report_task,
       to_regclass('agent_key_file.report_parse_error') AS report_parse_error,
       to_regclass('agent_key_file.report_file') AS report_file;

SELECT table_schema, table_name
  FROM information_schema.tables
 WHERE lower(table_name) IN ('report_batch', 'report_task', 'report_parse_error', 'report_file')
 ORDER BY 1, 2;
