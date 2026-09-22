-- 目标：收入/支出/库存 → 大屏 联调前检查（依据 final/005+007）
-- 可重复执行：是（只读）
-- 记录：开始 ________  结束 ________  库 ________

SELECT CURRENT_TIMESTAMP AS server_now,
       TO_CHAR(CURRENT_TIMESTAMP, 'YYYYMMDD') AS batch_date_today,
       current_database() AS db_name,
       current_user AS db_user;

-- ========== A. 三张 STG 贴源表 ==========
SELECT e.item,
       to_regclass(e.item) AS exists_regclass,
       CASE WHEN to_regclass(e.item) IS NULL THEN 'N' ELSE 'Y' END AS pass
  FROM (VALUES
        ('stg.trs_tmis_budget_income'),
        ('stg.trs_tmis_budget_payout'),
        ('stg.trs_tmis_stock')
  ) AS e(item);

SELECT table_name, column_name, data_type, character_maximum_length
  FROM information_schema.columns
 WHERE table_schema = 'stg'
   AND table_name IN ('trs_tmis_budget_income', 'trs_tmis_budget_payout', 'trs_tmis_stock')
   AND column_name IN ('batch_date', 'data_date', 'd_acct', 'trecode', 'this_amt', 'year_amt',
                       'f_debitamt', 'f_loanamt', 'f_balance')
 ORDER BY table_name, ordinal_position;

-- ========== B. 编排入口 + 日/月末适配器 ==========
SELECT e.item,
       CASE WHEN EXISTS (
              SELECT 1 FROM pg_proc p
              JOIN pg_namespace n ON n.oid = p.pronamespace
               WHERE n.nspname = split_part(e.item, '.', 1)
                 AND lower(p.proname) = lower(split_part(e.item, '.', 2))
            ) THEN 'Y' ELSE 'N' END AS pass
  FROM (VALUES
        ('visual_screen.p_task_vscreen'),
        ('visual_screen.p_task_vscreen_daily'),
        ('visual_screen.p_task_vscreen_month_end')
  ) AS e(item);

-- 库存日跑叶子
SELECT e.item,
       CASE WHEN EXISTS (
              SELECT 1 FROM pg_proc p
              JOIN pg_namespace n ON n.oid = p.pronamespace
               WHERE n.nspname = 'visual_screen'
                 AND lower(p.proname) = lower(e.item)
            ) THEN 'Y' ELSE 'N' END AS pass
  FROM (VALUES
        ('p_vs_inventory_analyze'),
        ('p_vs_inventory_area'),
        ('p_vs_inventory_balance'),
        ('p_vs_inventory_form')
  ) AS e(item);

-- 收入月末叶子（节选，覆盖大屏常用）
SELECT e.item,
       CASE WHEN EXISTS (
              SELECT 1 FROM pg_proc p
              JOIN pg_namespace n ON n.oid = p.pronamespace
               WHERE n.nspname = 'visual_screen'
                 AND lower(p.proname) = lower(e.item)
            ) THEN 'Y' ELSE 'N' END AS pass
  FROM (VALUES
        ('p_vs_budget_revenue'),
        ('p_vs_tax_revenue'),
        ('p_vs_three_budget_revenue'),
        ('p_vs_public_budget'),
        ('p_vs_growth_public_budget'),
        ('p_vs_income_expenditure'),
        ('p_vs_local_financial_resources'),
        ('p_vs_transfer_income')
  ) AS e(item);

-- 支出月末叶子（节选）
SELECT e.item,
       CASE WHEN EXISTS (
              SELECT 1 FROM pg_proc p
              JOIN pg_namespace n ON n.oid = p.pronamespace
               WHERE n.nspname = 'visual_screen'
                 AND lower(p.proname) = lower(e.item)
            ) THEN 'Y' ELSE 'N' END AS pass
  FROM (VALUES
        ('p_vs_region_pay'),
        ('p_vs_subject_pay'),
        ('p_vs_purpose_pay'),
        ('p_vs_economic_pay'),
        ('p_vs_area_pay'),
        ('p_vs_subject_pay_sub')
  ) AS e(item);

-- ========== C. 关键 vs_* 结果表 ==========
SELECT table_name,
       CASE WHEN table_name IS NOT NULL THEN 'Y' ELSE 'N' END AS pass
  FROM information_schema.tables
 WHERE table_schema = 'visual_screen'
   AND table_name IN (
       'vs_inventory_analyze', 'vs_inventory_area', 'vs_inventory_balance', 'vs_inventory_form',
       'vs_budget_revenue', 'vs_tax_revenue', 'vs_three_budget_revenue', 'vs_public_budget',
       'vs_growth_public_budget', 'vs_income_expenditure',
       'vs_region_pay', 'vs_subject_pay', 'vs_purpose_pay', 'vs_economic_pay', 'vs_area_pay'
   )
 ORDER BY table_name;

-- ========== D. Event / pg_job 时间 ==========
SELECT job_id, dbname, nspname, job_name, enable, job_status, interval,
       start_date, next_run_date, last_start_date, last_end_date, last_suc_date,
       this_run_date, failure_count, failure_msg
  FROM pg_job
 WHERE dbname = current_database()
   AND job_name = 'visual_screen_p_task_vs';

SELECT CASE
         WHEN EXISTS (
           SELECT 1 FROM pg_job
            WHERE dbname = current_database()
              AND job_name = 'visual_screen_p_task_vs'
              AND enable = true
         ) THEN 'Y' ELSE 'N'
       END AS event_enabled_in_current_db,
       CURRENT_TIMESTAMP AS check_time;

-- ========== E. 异步上报跟踪表 ==========
SELECT to_regclass('agent_key_file.report_batch') AS report_batch,
       to_regclass('agent_key_file.report_task') AS report_task,
       to_regclass('agent_key_file.report_parse_error') AS report_parse_error;
