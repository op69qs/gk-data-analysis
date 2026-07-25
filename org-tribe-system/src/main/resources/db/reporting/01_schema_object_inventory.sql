-- 目标：Vastbase 开发/内网环境只读对象盘点。
-- 边界：本脚本不创建、不修改任何既有数仓对象；结果中 EXISTS_FLAG=N 的对象需由数据库侧确认。
-- 证据：对象名称均来自 agentKeyFileHandler-2.1.0.jar 的 Mapper、配置或反编译调用逻辑。
-- 完整对象：agent_key_file.agent_treatury_config, agent_key_file.agent_keyfile_pending,
-- agent_key_file.agent_file_income, agent_key_file.agent_file_payout,
-- agent_key_file.agent_file_stock, agent_key_file.agent_file_back,
-- agent_key_file.tims_file_pending, agent_key_file.tims_file_income,
-- agent_key_file.tims_file_payout, agent_key_file.tims_file_stock,
-- stg.trs_tmis_budget_income, stg.trs_tmis_budget_payout, stg.trs_tmis_stock,
-- edw.cm_guoku_dimnsn, edw.income_report_detail_stat, edw.payout_report_detail_stat,
-- edw.reprot_update_record, etl.guoku_lib_report_all_log, adm.p_guoku_lib_report_all。

with expected_object(schema_name, object_name, object_kind, jar_operation, business_purpose) as (
    values
        ('agent_key_file', 'agent_treatury_config', 'TABLE', 'SELECT/INSERT/UPDATE', '代理国库配置'),
        ('agent_key_file', 'agent_keyfile_pending', 'TABLE', 'SELECT/INSERT/UPDATE/DELETE', 'KEY待处理及异常'),
        ('agent_key_file', 'agent_file_income', 'TABLE', 'DELETE/INSERT', 'KEY收入明细'),
        ('agent_key_file', 'agent_file_payout', 'TABLE', 'DELETE/INSERT', 'KEY支出明细'),
        ('agent_key_file', 'agent_file_stock', 'TABLE', 'DELETE/INSERT', 'KEY库存明细'),
        ('agent_key_file', 'agent_file_back', 'TABLE', 'DELETE/INSERT', 'KEY退库明细'),
        ('agent_key_file', 'tims_file_pending', 'TABLE', 'SELECT/INSERT/UPDATE/DELETE', 'TIMS待处理及异常'),
        ('agent_key_file', 'tims_file_income', 'TABLE', 'DELETE/INSERT', 'TIMS收入明细'),
        ('agent_key_file', 'tims_file_payout', 'TABLE', 'DELETE/INSERT', 'TIMS支出明细'),
        ('agent_key_file', 'tims_file_stock', 'TABLE', 'DELETE/INSERT', 'TIMS库存明细'),
        ('stg', 'trs_tmis_budget_income', 'TABLE', 'DELETE/INSERT', 'TIMS收入暂存目标'),
        ('stg', 'trs_tmis_budget_payout', 'TABLE', 'DELETE/INSERT', 'TIMS支出暂存目标'),
        ('stg', 'trs_tmis_stock', 'TABLE', 'DELETE/INSERT', 'TIMS库存暂存目标'),
        ('edw', 'cm_guoku_dimnsn', 'TABLE_OR_VIEW', 'SELECT', '国库层级范围'),
        ('edw', 'income_report_detail_stat', 'TABLE_OR_VIEW', 'SELECT', '收入变更查询基线'),
        ('edw', 'payout_report_detail_stat', 'TABLE_OR_VIEW', 'SELECT', '支出变更查询基线'),
        ('edw', 'reprot_update_record', 'TABLE', 'SELECT/INSERT', '人工变更记录，JAR原拼写reprot'),
        ('etl', 'guoku_lib_report_all_log', 'TABLE', 'SELECT/INSERT', '加工过程运行互斥日志'),
        ('adm', 'p_guoku_lib_report_all', 'PROCEDURE', 'CALL', '按月末账期执行下游加工')
), relation_object as (
    select lower(table_schema) schema_name, lower(table_name) object_name, table_type actual_kind
      from information_schema.tables
), routine_object as (
    select lower(routine_schema) schema_name, lower(routine_name) object_name, routine_type actual_kind
      from information_schema.routines
)
select e.schema_name,
       e.object_name,
       e.object_kind expected_kind,
       coalesce(r.actual_kind, p.actual_kind) actual_kind,
       case when r.object_name is not null or p.object_name is not null then 'Y' else 'N' end exists_flag,
       e.jar_operation,
       e.business_purpose
  from expected_object e
  left join relation_object r
    on r.schema_name = e.schema_name and r.object_name = e.object_name
  left join routine_object p
    on p.schema_name = e.schema_name and p.object_name = e.object_name
 order by e.schema_name, e.object_name;
