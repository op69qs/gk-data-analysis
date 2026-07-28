-- 目标：只读核对 EDW 查询对象、ETL 运行日志和 ADM 过程。
-- 门禁：未取得以下查询结果和真实 DDL 前，不启用 EDW 变更写入，不允许手工 ADM 调用。

select table_schema,
       table_name,
       table_type
  from information_schema.tables
 where (lower(table_schema) = 'edw' and lower(table_name) in (
            'cm_guoku_dimnsn', 'income_report_detail_stat',
            'payout_report_detail_stat', 'reprot_update_record'
       ))
    or (lower(table_schema) = 'etl' and lower(table_name) = 'guoku_lib_report_all_log')
 order by table_schema, table_name;

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
 where (lower(table_schema) = 'edw' and lower(table_name) in (
            'cm_guoku_dimnsn', 'income_report_detail_stat',
            'payout_report_detail_stat', 'reprot_update_record'
       ))
    or (lower(table_schema) = 'etl' and lower(table_name) = 'guoku_lib_report_all_log')
 order by table_schema, table_name, ordinal_position;

select routine_schema,
       routine_name,
       routine_type,
       data_type,
       external_language,
       parameter_style
  from information_schema.routines
 where lower(routine_schema) = 'adm'
   and lower(routine_name) = 'p_guoku_lib_report_all';

select specific_schema,
       specific_name,
       ordinal_position,
       parameter_mode,
       parameter_name,
       data_type
  from information_schema.parameters
 where lower(specific_schema) = 'adm'
   and lower(specific_name) like 'p_guoku_lib_report_all%'
 order by specific_name, ordinal_position;
