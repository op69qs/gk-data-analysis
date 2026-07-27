-- 目标：只读核对 Vastbase 是否具备迁移 Mapper 使用的标准函数、窗口函数和过程调用元数据。
-- 可重复执行：是；只读，不修改任何对象。
-- Schema：agent_key_file、edw、etl、adm；不涉及 ods、dmcode、comm_sys、dps。

select current_database() database_name, version() database_version;
select coalesce(null, 'COALESCE_OK') coalesce_check,
       substring('5000000000', 1, 4) substring_check,
       concat('%', '5000', '%') concat_check;

select row_number() over (order by object_name) sequence_no, object_name
from (values ('window-check-a'), ('window-check-b')) as sample(object_name);

select routine_schema, routine_name, routine_type, data_type
from information_schema.routines
where lower(routine_schema) = 'adm'
  and lower(routine_name) = 'p_guoku_lib_report_all';

