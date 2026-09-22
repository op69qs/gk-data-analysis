-- 目标：收入/支出/库存上报后检查 STG、是否具备月末加工条件、vs_* 结果
-- 可重复执行：是（只读）
-- 修改变量后执行。记录：开始 ________  结束 ________  库 ________
--
-- 重要：收入/支出大屏多在 p_task_vscreen_month_end，仅当规范化后的 d_acct = 该月最后一天才自动调用。
-- Excel 若只填 yyyyMM，过程会拼成月初 → 只跑 daily（库存），不跑 month_end。

\set period_key '202607'
\set batch_date '20260923'

SELECT CURRENT_TIMESTAMP AS check_time,
       current_database() AS db_name,
       :'period_key' AS period_key,
       :'batch_date' AS batch_date;

-- ========== A. STG 行数与 batch/d_acct ==========
SELECT 'income' AS biz, COUNT(*) AS cnt,
       COUNT(DISTINCT d_acct) AS distinct_d_acct,
       MIN(d_acct) AS min_d_acct, MAX(d_acct) AS max_d_acct,
       MIN(batch_date) AS min_batch, MAX(batch_date) AS max_batch
  FROM stg.trs_tmis_budget_income
 WHERE data_date = :'period_key' OR batch_date = :'batch_date'
UNION ALL
SELECT 'payout', COUNT(*), COUNT(DISTINCT d_acct), MIN(d_acct), MAX(d_acct), MIN(batch_date), MAX(batch_date)
  FROM stg.trs_tmis_budget_payout
 WHERE data_date = :'period_key' OR batch_date = :'batch_date'
UNION ALL
SELECT 'stock', COUNT(*), COUNT(DISTINCT d_acct), MIN(d_acct), MAX(d_acct), MIN(batch_date), MAX(batch_date)
  FROM stg.trs_tmis_stock
 WHERE data_date LIKE :'period_key' || '%' OR batch_date = :'batch_date';

-- 当日批次下，规范化后的账期日 + 是否等于月末（决定能否进 month_end）
SELECT src, d_acct_raw, d_acct_norm,
       LAST_DAY(d_acct_norm::date) AS month_end_day,
       CASE WHEN d_acct_norm::date = LAST_DAY(d_acct_norm::date) THEN 'Y' ELSE 'N' END AS will_call_month_end
  FROM (
    SELECT 'income' AS src, d_acct AS d_acct_raw,
           CASE
             WHEN DATE_FORMAT(d_acct, '%Y-%m-%d') IS NULL
             THEN DATE_FORMAT(CONCAT(d_acct, '01'), '%Y-%m-%d')
             ELSE DATE_FORMAT(d_acct, '%Y-%m-%d')
           END AS d_acct_norm
      FROM stg.trs_tmis_budget_income
     WHERE batch_date = :'batch_date'
    UNION ALL
    SELECT 'payout', d_acct,
           CASE
             WHEN DATE_FORMAT(d_acct, '%Y-%m-%d') IS NULL
             THEN DATE_FORMAT(CONCAT(d_acct, '01'), '%Y-%m-%d')
             ELSE DATE_FORMAT(d_acct, '%Y-%m-%d')
           END
      FROM stg.trs_tmis_budget_payout
     WHERE batch_date = :'batch_date'
    UNION ALL
    SELECT 'stock', d_acct,
           CASE
             WHEN DATE_FORMAT(d_acct, '%Y-%m-%d') IS NULL
             THEN DATE_FORMAT(CONCAT(d_acct, '01'), '%Y-%m-%d')
             ELSE DATE_FORMAT(d_acct, '%Y-%m-%d')
           END
      FROM stg.trs_tmis_stock
     WHERE batch_date = :'batch_date'
  ) t
 GROUP BY src, d_acct_raw, d_acct_norm
 ORDER BY src, d_acct_norm;

-- ========== B. 异步批次状态（INCOME/PAYOUT/STOCK） ==========
SELECT id, business_type, status, accounting_period, original_file_name,
       success_count, error_count, create_time, update_time
  FROM agent_key_file.report_batch
 WHERE source_domain = 'TIMS'
   AND business_type IN ('INCOME', 'PAYOUT', 'STOCK')
 ORDER BY create_time DESC
 LIMIT 30;

SELECT t.id, t.batch_id, b.business_type, t.status, t.progress_percent, t.result_summary,
       t.create_time, t.start_time, t.end_time, t.update_time
  FROM agent_key_file.report_task t
  JOIN agent_key_file.report_batch b ON b.id = t.batch_id
 WHERE b.business_type IN ('INCOME', 'PAYOUT', 'STOCK')
 ORDER BY t.create_time DESC
 LIMIT 30;

SELECT e.batch_id, b.business_type, e.file_name, e.row_number, e.column_name, e.message, e.create_time
  FROM agent_key_file.report_parse_error e
  JOIN agent_key_file.report_batch b ON b.id = e.batch_id
 WHERE b.business_type IN ('INCOME', 'PAYOUT', 'STOCK')
 ORDER BY e.create_time DESC
 LIMIT 50;

-- ========== C. vs_* 行数（加工后） ==========
SELECT 'vs_inventory_area' AS tbl, COUNT(*) AS cnt FROM visual_screen.vs_inventory_area
UNION ALL SELECT 'vs_inventory_balance', COUNT(*) FROM visual_screen.vs_inventory_balance
UNION ALL SELECT 'vs_inventory_analyze', COUNT(*) FROM visual_screen.vs_inventory_analyze
UNION ALL SELECT 'vs_budget_revenue', COUNT(*) FROM visual_screen.vs_budget_revenue
UNION ALL SELECT 'vs_tax_revenue', COUNT(*) FROM visual_screen.vs_tax_revenue
UNION ALL SELECT 'vs_region_pay', COUNT(*) FROM visual_screen.vs_region_pay
UNION ALL SELECT 'vs_area_pay', COUNT(*) FROM visual_screen.vs_area_pay
UNION ALL SELECT 'vs_income_expenditure', COUNT(*) FROM visual_screen.vs_income_expenditure;

-- ========== D. Event 最近运行 ==========
SELECT job_name, enable, interval,
       last_start_date, last_end_date, last_suc_date, next_run_date,
       failure_count, failure_msg
  FROM pg_job
 WHERE dbname = current_database()
   AND job_name = 'visual_screen_p_task_vs';

-- ========== E. 若 will_call_month_end=N，可手工补跑月末（示例，按需取消注释） ==========
-- CALL visual_screen.p_task_vscreen_month_end('2026-07-31');
-- CALL visual_screen.p_task_vscreen_daily('2026-07-31');
