-- 目标：按一个待核对批次对比 JAR 原业务表、STG 表与新跟踪表的行数和金额汇总。
-- 可重复执行：是；只读。执行前只修改 params 中的三个核对值，不修改 SQL 对象名。
-- Schema：agent_key_file、stg；结果用于人工逐项核对，不自动判定生产正确性。

with params as (
  select cast('REPLACE_BATCH_ID' as varchar(64)) batch_id,
         cast('REPLACE_KEY_ZIP_NAME.zip' as varchar(512)) key_zip_name,
         cast('209912' as varchar(6)) period_key
)
select 'report_batch' item, count(1)::numeric row_count, null::numeric amount
from agent_key_file.report_batch b, params p where b.id = p.batch_id
union all
select 'report_parse_error', count(1)::numeric, null::numeric
from agent_key_file.report_parse_error e, params p where e.batch_id = p.batch_id
union all
select 'key_income', count(1)::numeric, coalesce(sum(f_amt), 0)
from agent_key_file.agent_file_income d, params p where d.key_zip_name = p.key_zip_name
union all
select 'key_payout', count(1)::numeric, coalesce(sum(f_amt), 0)
from agent_key_file.agent_file_payout d, params p where d.key_zip_name = p.key_zip_name
union all
select 'key_stock', count(1)::numeric, coalesce(sum(f_bal), 0)
from agent_key_file.agent_file_stock d, params p where d.key_zip_name = p.key_zip_name
union all
select 'key_back', count(1)::numeric, coalesce(sum(f_amt), 0)
from agent_key_file.agent_file_back d, params p where d.key_zip_name = p.key_zip_name
union all
select 'stg_income', count(1)::numeric, coalesce(sum(this_amt), 0)
from stg.trs_tmis_budget_income d, params p where d.data_date = p.period_key
union all
select 'stg_payout', count(1)::numeric, coalesce(sum(this_amt), 0)
from stg.trs_tmis_budget_payout d, params p where d.data_date = p.period_key
union all
select 'stg_stock', count(1)::numeric, coalesce(sum(f_balance), 0)
from stg.trs_tmis_stock d, params p where d.data_date like concat(p.period_key, '%');

