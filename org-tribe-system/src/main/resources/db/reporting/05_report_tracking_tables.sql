-- 目标 Schema：agent_key_file（与原 JAR 业务 Schema 一致）。
-- 作用：仅创建新模块自己的六张跟踪表和一张运行租约表，共七张。
-- 可重复执行：是。回滚仅允许删除以下七张新表，禁止触碰 JAR 原表和数仓对象。

create schema if not exists agent_key_file;

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

create table if not exists agent_key_file.report_batch (
    id varchar(64) primary key,
    batch_no varchar(64) not null,
    source_domain varchar(16) not null,
    business_type varchar(32),
    accounting_period date,
    treasury_code varchar(64),
    treasury_name varchar(255),
    original_file_name varchar(512) not null,
    current_stage varchar(32) not null,
    status varchar(32) not null,
    progress_percent integer not null default 0,
    file_count integer not null default 0,
    success_row_count bigint not null default 0,
    error_row_count bigint not null default 0,
    auto_process_required integer not null default 0,
    process_call_status varchar(32),
    retry_of_batch_id varchar(64),
    result_summary text,
    error_summary text,
    del_flag integer not null default 0,
    create_by varchar(64),
    create_time timestamp not null default current_timestamp,
    update_by varchar(64),
    update_time timestamp,
    constraint ck_report_batch_progress check (progress_percent between 0 and 100),
    constraint ck_report_batch_del check (del_flag in (0, 1)),
    constraint ck_report_batch_auto check (auto_process_required in (0, 1))
);
create table if not exists agent_key_file.report_file (
    id varchar(64) primary key,
    batch_id varchar(64) not null,
    parent_file_id varchar(64),
    file_role varchar(32) not null,
    business_type varchar(32),
    original_name varchar(512) not null,
    archive_name varchar(512),
    relative_path text,
    storage_path text,
    content_type varchar(128),
    file_extension varchar(32),
    file_size bigint not null default 0,
    sha256 varchar(64),
    archive_status varchar(32),
    extract_status varchar(32),
    parse_status varchar(32),
    total_row_count bigint not null default 0,
    success_row_count bigint not null default 0,
    error_row_count bigint not null default 0,
    error_summary text,
    retained integer not null default 1,
    del_flag integer not null default 0,
    create_by varchar(64),
    create_time timestamp not null default current_timestamp,
    update_by varchar(64),
    update_time timestamp,
    constraint fk_report_file_batch foreign key (batch_id) references agent_key_file.report_batch(id),
    constraint ck_report_file_retained check (retained in (0, 1)),
    constraint ck_report_file_del check (del_flag in (0, 1))
);

create table if not exists agent_key_file.report_task (
    id varchar(64) primary key,
    batch_id varchar(64) not null,
    parent_task_id varchar(64),
    retry_of_task_id varchar(64),
    task_type varchar(32) not null,
    sequence_no integer not null,
    attempt_no integer not null default 1,
    status varchar(32) not null,
    progress_percent integer not null default 0,
    executor_key varchar(128),
    lease_owner varchar(128),
    lease_until timestamp,
    request_params text,
    result_summary text,
    error_message text,
    started_time timestamp,
    ended_time timestamp,
    duration_ms bigint,
    create_by varchar(64),
    create_time timestamp not null default current_timestamp,
    update_by varchar(64),
    update_time timestamp,
    constraint fk_report_task_batch foreign key (batch_id) references agent_key_file.report_batch(id),
    constraint ck_report_task_progress check (progress_percent between 0 and 100),
    constraint ck_report_task_attempt check (attempt_no > 0)
);

create table if not exists agent_key_file.report_task_log (
    id varchar(64) primary key,
    batch_id varchar(64) not null,
    task_id varchar(64) not null,
    stage varchar(32) not null,
    from_status varchar(32),
    to_status varchar(32) not null,
    message varchar(1000),
    detail text,
    current_file_name varchar(512),
    processed_row_count bigint not null default 0,
    success_row_count bigint not null default 0,
    error_row_count bigint not null default 0,
    operator_id varchar(64),
    operator_name varchar(128),
    event_time timestamp not null default current_timestamp,
    constraint fk_report_log_batch foreign key (batch_id) references agent_key_file.report_batch(id),
    constraint fk_report_log_task foreign key (task_id) references agent_key_file.report_task(id)
);

create table if not exists agent_key_file.report_parse_error (
    id varchar(64) primary key,
    batch_id varchar(64) not null,
    file_id varchar(64) not null,
    task_id varchar(64) not null,
    business_type varchar(32),
    sheet_name varchar(255),
    row_number bigint,
    column_name varchar(255),
    raw_value text,
    error_code varchar(64),
    error_message text not null,
    create_time timestamp not null default current_timestamp,
    constraint fk_report_error_batch foreign key (batch_id) references agent_key_file.report_batch(id),
    constraint fk_report_error_file foreign key (file_id) references agent_key_file.report_file(id),
    constraint fk_report_error_task foreign key (task_id) references agent_key_file.report_task(id)
);

create table if not exists agent_key_file.report_process_call (
    id varchar(64) primary key,
    batch_id varchar(64) not null,
    task_id varchar(64) not null,
    accounting_period date not null,
    treasury_scope varchar(1000),
    procedure_name varchar(255) not null,
    procedure_argument varchar(255) not null,
    status varchar(32) not null,
    attempt_no integer not null default 1,
    external_log_id varchar(64),
    request_summary text,
    result_summary text,
    error_message text,
    started_time timestamp,
    ended_time timestamp,
    duration_ms bigint,
    create_by varchar(64),
    create_time timestamp not null default current_timestamp,
    constraint fk_report_call_batch foreign key (batch_id) references agent_key_file.report_batch(id),
    constraint fk_report_call_task foreign key (task_id) references agent_key_file.report_task(id),
    constraint ck_report_call_attempt check (attempt_no > 0)
);

comment on table agent_key_file.report_runtime_lock is 'TIMS 多实例全局执行租约';
comment on table agent_key_file.report_batch is '数据上报批次与总体状态';
comment on table agent_key_file.report_file is '上报原件和解压文件跟踪';
comment on table agent_key_file.report_task is '解析、入库和人工加工任务';
comment on table agent_key_file.report_task_log is '上报任务状态变迁时间线';
comment on table agent_key_file.report_parse_error is '文件解析行级异常';
comment on table agent_key_file.report_process_call is '按批次账期手工调用 ADM 的记录';
