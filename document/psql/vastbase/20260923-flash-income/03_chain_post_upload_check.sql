-- 目标：上报完成后检查 STG / 批次 / vs_* 与时间字段
-- 可重复执行：是（只读）
-- 执行前请修改下方变量，记录：开始 ________  结束 ________  库 ________
-- 反馈本脚本全部结果集 + 批次号

-- ========== 变量（按实际上报修改） ==========
-- 账期 yyyyMM，例如 202607
-- 批次日 yyyyMMdd = 上传当天（与 STG.batch_date 一致）
-- 可选：批次 UUID
\set period_key '202607'
\set batch_date '20260923'
\set batch_id ''

SELECT CURRENT_TIMESTAMP AS check_time,
       current_database() AS db_name,
       :'period_key' AS period_key,
       :'batch_date' AS batch_date,
       NULLIF(:'batch_id', '') AS batch_id;

-- ========== A. STG 快报收入 ==========
SELECT COUNT(*) AS stg_row_count,
       MIN(batch_date) AS min_batch_date,
       MAX(batch_date) AS max_batch_date,
       MIN(data_date) AS min_data_date,
       MAX(data_date) AS max_data_date,
       MIN(add_date) AS min_add_date,
       MAX(add_date) AS max_add_date
  FROM stg.trs_tmis_budget_income_provinces
 WHERE data_date = :'period_key';

SELECT COUNT(*) AS stg_row_count_by_batch_date
  FROM stg.trs_tmis_budget_income_provinces
 WHERE batch_date = :'batch_date';

SELECT trecode, tredscr, subject_code, subject_dscr, this_amt, year_amt,
       data_date, batch_date, table_nature, rows_id, add_date
  FROM stg.trs_tmis_budget_income_provinces
 WHERE data_date = :'period_key'
 ORDER BY trecode, subject_code
 LIMIT 50;

-- ========== B. 异步上报批次（与收支存共用 agent_key_file） ==========
SELECT id, source_domain, business_type, status, accounting_period,
       original_file_name, success_count, error_count,
       create_time, update_time, create_by
  FROM agent_key_file.report_batch
 WHERE business_type = 'FLASH_INCOME'
 ORDER BY create_time DESC
 LIMIT 20;

SELECT id, batch_id, task_type, status, progress_percent, result_summary,
       create_time, update_time, start_time, end_time
  FROM agent_key_file.report_task
 WHERE batch_id IN (
       SELECT id FROM agent_key_file.report_batch
        WHERE business_type = 'FLASH_INCOME'
        ORDER BY create_time DESC
        LIMIT 5
 )
 ORDER BY create_time DESC
 LIMIT 50;

SELECT id, batch_id, file_name, sheet_name, row_number, column_name, raw_value, message, create_time
  FROM agent_key_file.report_parse_error
 WHERE batch_id IN (
       SELECT id FROM agent_key_file.report_batch
        WHERE business_type = 'FLASH_INCOME'
        ORDER BY create_time DESC
        LIMIT 5
 )
 ORDER BY create_time DESC
 LIMIT 100;

-- ========== C. 大屏结果表（加工后才有；未跑 Event 时可能为空） ==========
SELECT 'vs_revenu_display' AS tbl, COUNT(*) AS cnt FROM visual_screen.vs_revenu_display
UNION ALL
SELECT 'vs_amount_ranking_by_region', COUNT(*) FROM visual_screen.vs_amount_ranking_by_region
UNION ALL
SELECT 'vs_regional_taxation', COUNT(*) FROM visual_screen.vs_regional_taxation
UNION ALL
SELECT 'vs_municipalities_directly', COUNT(*) FROM visual_screen.vs_municipalities_directly
UNION ALL
SELECT 'vs_five_provinces_in_southwest_china', COUNT(*) FROM visual_screen.vs_five_provinces_in_southwest_china;

-- 若结果表有 data_date / batch 类字段，按现场列名补充过滤；先抽样看列
SELECT column_name, data_type
  FROM information_schema.columns
 WHERE table_schema = 'visual_screen' AND table_name = 'vs_revenu_display'
 ORDER BY ordinal_position;

SELECT *
  FROM visual_screen.vs_revenu_display
 LIMIT 20;

SELECT *
  FROM visual_screen.vs_amount_ranking_by_region
 LIMIT 20;

SELECT *
  FROM visual_screen.vs_five_provinces_in_southwest_china
 LIMIT 20;

-- ========== D. 定时任务最近运行时间（对照 batch_date） ==========
SELECT job_name, enable, interval,
       last_start_date, last_end_date, last_suc_date, next_run_date,
       failure_count, failure_msg
  FROM pg_job
 WHERE dbname = current_database()
   AND job_name = 'visual_screen_p_task_vs';
