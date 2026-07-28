-- 目标：只读导出 agent_key_file 下 JAR 明确依赖十表的字段、类型、默认值和空值约束。
-- 用法：将结果与原 MySQL DDL 或 Vastbase 内网 DDL 逐表核对；本脚本不推断缺失字段。

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
 where lower(table_schema) = 'agent_key_file'
   and lower(table_name) in (
       'agent_treatury_config',
       'agent_keyfile_pending',
       'agent_file_income',
       'agent_file_payout',
       'agent_file_stock',
       'agent_file_back',
       'tims_file_pending',
       'tims_file_income',
       'tims_file_payout',
       'tims_file_stock'
   )
 order by table_name, ordinal_position;

select table_schema, table_name, constraint_name, constraint_type
  from information_schema.table_constraints
 where lower(table_schema) = 'agent_key_file'
   and lower(table_name) in (
       'agent_treatury_config', 'agent_keyfile_pending', 'agent_file_income',
       'agent_file_payout', 'agent_file_stock', 'agent_file_back',
       'tims_file_pending', 'tims_file_income', 'tims_file_payout', 'tims_file_stock'
   )
 order by table_name, constraint_type, constraint_name;
