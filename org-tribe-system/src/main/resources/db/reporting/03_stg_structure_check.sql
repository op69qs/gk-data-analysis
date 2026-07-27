-- 目标：只读核对 TIMS 自动任务写入的三张 STG 表。
-- JAR 证据：配置 DATABASE=stg，代码唯一指向以下 trs_tmis_* 对象。
-- 结构确认后继续执行 12_stg_performance_check.sql；本项目不自动修改 STG 结构或索引。

select table_schema,
       table_name,
       ordinal_position,
       column_name,
       data_type,
       character_maximum_length,
       numeric_precision,
       numeric_scale,
       is_nullable,
       column_default
  from information_schema.columns
 where lower(table_schema) = 'stg'
   and lower(table_name) in (
       'trs_tmis_budget_income',
       'trs_tmis_budget_payout',
       'trs_tmis_stock'
   )
 order by table_name, ordinal_position;

select table_schema, table_name, constraint_name, constraint_type
  from information_schema.table_constraints
 where lower(table_schema) = 'stg'
   and lower(table_name) in ('trs_tmis_budget_income', 'trs_tmis_budget_payout', 'trs_tmis_stock')
 order by table_name, constraint_type, constraint_name;
