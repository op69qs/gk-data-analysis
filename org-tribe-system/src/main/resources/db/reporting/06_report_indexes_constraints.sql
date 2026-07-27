-- 目标：为本模块七张新表补齐兼容列、默认值和常用查询索引。
-- 可重复执行：是。执行前先完成 05_report_tracking_tables.sql。

-- 兼容已执行旧版 05 脚本的环境。
alter table agent_key_file.report_task add column if not exists lease_owner varchar(128);
alter table agent_key_file.report_task add column if not exists lease_until timestamp;
alter table agent_key_file.report_batch alter column auto_process_required set default 0;

create table if not exists agent_key_file.report_runtime_lock (
    lock_name varchar(64) primary key,
    lease_owner varchar(128),
    lease_until timestamp,
    update_time timestamp not null default current_timestamp
);
insert into agent_key_file.report_runtime_lock
    (lock_name, lease_owner, lease_until, update_time)
select 'TIMS_LOAD', null, null, current_timestamp
where not exists (
    select 1 from agent_key_file.report_runtime_lock where lock_name = 'TIMS_LOAD'
);
create index if not exists idx_report_runtime_lock_lease
    on agent_key_file.report_runtime_lock(lease_until);

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
    on agent_key_file.report_task(status, lease_until, sequence_no, create_time);
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
-- 原过程使用全局 ETL 运行状态；多实例下同一过程也只允许一个活动加工记录。
create unique index if not exists uk_report_process_call_active
    on agent_key_file.report_process_call(procedure_name)
    where status in ('QUEUED', 'PROCESSING');
