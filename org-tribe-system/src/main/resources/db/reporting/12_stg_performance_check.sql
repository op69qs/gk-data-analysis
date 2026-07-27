-- 目标：只读输出三张 TIMS STG 的列、索引、分区以及整周期删除执行计划。
-- 可重复执行：是；仅查询和 EXPLAIN，不执行删除，不变更任何对象。
-- 使用前将示例周期 209901 替换为一个可代表真实数据量的测试周期，交由 DBA 评估。

select table_schema, table_name, ordinal_position, column_name, data_type,
       character_maximum_length, numeric_precision, numeric_scale, is_nullable
  from information_schema.columns
 where lower(table_schema) = 'stg'
   and lower(table_name) in (
       'trs_tmis_budget_income', 'trs_tmis_budget_payout', 'trs_tmis_stock'
   )
 order by table_name, ordinal_position;

select schemaname, tablename, indexname, indexdef
  from pg_indexes
 where lower(schemaname) = 'stg'
   and lower(tablename) in (
       'trs_tmis_budget_income', 'trs_tmis_budget_payout', 'trs_tmis_stock'
   )
 order by tablename, indexname;

select ns.nspname schema_name, parent.relname table_name,
       strategy.partstrat partition_strategy, child.relname partition_name
  from pg_partitioned_table strategy
  join pg_class parent on parent.oid = strategy.partrelid
  join pg_namespace ns on ns.oid = parent.relnamespace
  left join pg_inherits inheritance on inheritance.inhparent = parent.oid
  left join pg_class child on child.oid = inheritance.inhrelid
 where lower(ns.nspname) = 'stg'
   and lower(parent.relname) in (
       'trs_tmis_budget_income', 'trs_tmis_budget_payout', 'trs_tmis_stock'
   )
 order by parent.relname, child.relname;

explain delete from stg.trs_tmis_budget_income where data_date = '209901';
explain delete from stg.trs_tmis_budget_payout where data_date = '209901';
explain delete from stg.trs_tmis_stock where data_date like '209901%';
