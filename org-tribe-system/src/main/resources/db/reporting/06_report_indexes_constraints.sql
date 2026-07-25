-- 目标：为本模块六张跟踪表创建唯一性约束和常用查询索引。
-- 可重复执行：是。执行前先完成 05_report_tracking_tables.sql。

create unique index if not exists uk_report_batch_no
    on agent_key_file.report_batch(batch_no);
create index if not exists idx_report_batch_query
    on agent_key_file.report_batch(source_domain, accounting_period, treasury_code, status, del_flag);
create index if not exists idx_report_batch_creator
    on agent_key_file.report_batch(create_by, create_time);

create index if not exists idx_report_file_batch
    on agent_key_file.report_file(batch_id, file_role, business_type, del_flag);
create index if not exists idx_report_file_sha256
    on agent_key_file.report_file(sha256);
create index if not exists idx_report_file_parent
    on agent_key_file.report_file(parent_file_id);

create unique index if not exists uk_report_task_attempt
    on agent_key_file.report_task(batch_id, task_type, attempt_no);
create index if not exists idx_report_task_dispatch
    on agent_key_file.report_task(status, sequence_no, create_time);
create index if not exists idx_report_task_batch
    on agent_key_file.report_task(batch_id, sequence_no, attempt_no);

create index if not exists idx_report_task_log_timeline
    on agent_key_file.report_task_log(batch_id, event_time);
create index if not exists idx_report_task_log_task
    on agent_key_file.report_task_log(task_id, event_time);

create index if not exists idx_report_parse_error_query
    on agent_key_file.report_parse_error(batch_id, file_id, row_number);
create index if not exists idx_report_process_call_scope
    on agent_key_file.report_process_call(accounting_period, status, treasury_scope);
create index if not exists idx_report_process_call_batch
    on agent_key_file.report_process_call(batch_id, attempt_no);
