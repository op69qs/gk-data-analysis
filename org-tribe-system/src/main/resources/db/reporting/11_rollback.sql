-- 目标：回滚本次迁移新建的菜单与七张技术表；绝不删除 JAR 原表、STG、EDW、ETL 或 ADM 对象。
-- 可重复执行：是，但属于破坏性脚本；必须先停服务、备份新跟踪表并由负责人确认后手工执行。
-- Schema："jeecg-boot-os"、agent_key_file。原始 ZIP 文件不由 SQL 删除，需按归档清单另行受控处理。

set search_path to "jeecg-boot-os", public;

delete from sys_role_permission
where permission_id in (
  select id from sys_permission
  where id = md5('sys_permission:/reporting')
     or parent_id = md5('sys_permission:/reporting')
     or parent_id in (select id from sys_permission where parent_id = md5('sys_permission:/reporting'))
);

delete from sys_permission
where parent_id in (select id from sys_permission where parent_id = md5('sys_permission:/reporting'));
delete from sys_permission where parent_id = md5('sys_permission:/reporting');
delete from sys_permission where id = md5('sys_permission:/reporting');

drop table if exists agent_key_file.report_process_call;
drop table if exists agent_key_file.report_parse_error;
drop table if exists agent_key_file.report_task_log;
drop table if exists agent_key_file.report_task;
drop table if exists agent_key_file.report_file;
drop table if exists agent_key_file.report_batch;
drop table if exists agent_key_file.report_runtime_lock;
