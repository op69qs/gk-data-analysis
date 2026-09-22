-- 目标：手工触发大屏日编排（不等待日定时）
-- 可重复执行：是（会重算当日批次对应账期）
-- 说明：与 Event visual_screen_p_task_vs 的 DO 语句一致
-- 参数：传入当天 yyyyMMdd，须与 STG.batch_date 一致
-- 执行时间请记录：开始 ________  结束 ________  库 ________  入参 batch_date ________

SELECT CURRENT_TIMESTAMP AS trigger_start,
       current_database() AS db_name,
       TO_CHAR(CURRENT_TIMESTAMP, 'YYYYMMDD') AS suggested_batch_date;

-- 使用当天日期（与上传日 batch_date 一致时）
CALL visual_screen.p_task_vscreen(TO_CHAR(CURRENT_TIMESTAMP, 'YYYYMMDD'));

SELECT CURRENT_TIMESTAMP AS trigger_end;

-- 触发后再查 STG 当日批次与结果表行数
SELECT COUNT(*) AS stg_rows_today_batch
  FROM stg.trs_tmis_budget_income_provinces
 WHERE batch_date = TO_CHAR(CURRENT_TIMESTAMP, 'YYYYMMDD');

SELECT 'vs_revenu_display' AS tbl, COUNT(*) AS cnt FROM visual_screen.vs_revenu_display
UNION ALL
SELECT 'vs_amount_ranking_by_region', COUNT(*) FROM visual_screen.vs_amount_ranking_by_region
UNION ALL
SELECT 'vs_five_provinces_in_southwest_china', COUNT(*) FROM visual_screen.vs_five_provinces_in_southwest_china;

SELECT job_name, last_start_date, last_end_date, last_suc_date, next_run_date, enable
  FROM pg_job
 WHERE dbname = current_database()
   AND job_name = 'visual_screen_p_task_vs';
