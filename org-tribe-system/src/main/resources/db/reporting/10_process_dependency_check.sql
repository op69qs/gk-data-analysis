-- 目标：启用生产自动加工前，只读核对 STG 三表、ETL 日志表、ADM 过程、参数和当前运行状态。
-- 可重复执行：是；只读，不调用过程、不写日志。
-- Schema：stg、etl、adm。任一对象缺失或无权限时，不应开始生产上报。

select required.schema_name, required.object_name,
       case when tables.table_name is null then 'MISSING_OR_NO_PRIVILEGE' else 'OK' end check_result
from (values
  ('stg', 'trs_tmis_budget_income'), ('stg', 'trs_tmis_budget_payout'),
  ('stg', 'trs_tmis_stock'), ('etl', 'guoku_lib_report_all_log')
) as required(schema_name, object_name)
left join information_schema.tables tables
  on lower(tables.table_schema) = required.schema_name
 and lower(tables.table_name) = required.object_name;

select routine_schema, routine_name, routine_type, data_type
from information_schema.routines
where lower(routine_schema) = 'adm' and lower(routine_name) = 'p_guoku_lib_report_all';

select specific_schema, specific_name, parameter_name, data_type, parameter_mode, ordinal_position
from information_schema.parameters
where lower(specific_schema) = 'adm' and lower(specific_name) like 'p_guoku_lib_report_all%'
order by ordinal_position;

select count(1) running_count, min(add_time) oldest_running_time
from etl.guoku_lib_report_all_log where state = '1';

