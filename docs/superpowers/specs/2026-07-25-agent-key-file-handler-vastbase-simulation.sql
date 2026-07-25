-- Agent Key File Handler：基于 JAR Mapper 反推的 Vastbase 模拟 DDL
-- 用途：开发环境缺失对象时，用于验证新模块编译、接口、解析和任务链。
-- 禁止直接作为内网/生产迁移脚本执行；执行前须以原库或已迁移对象 DDL 替换本文件中的假设。
-- 证据范围：agentKeyFileHandler-2.1.0.jar 的 MyBatis Mapper、配置与反编译任务逻辑。
-- 已知不可从 JAR 证明：字段长度/精度、主键、索引、分区、默认值、过程内部加工逻辑。

create schema if not exists agent_key_file;
create schema if not exists stg;
create schema if not exists edw;
create schema if not exists etl;
create schema if not exists adm;

-- 以下十表均由 JAR 明确读写；字段由 insert/select SQL 反推。
create table if not exists agent_key_file.agent_treatury_config (
  tre_code varchar(64) primary key,
  tre_name varchar(255) not null,
  start_date date,
  end_date date,
  state varchar(1) not null default '0',
  add_userid varchar(64), add_time timestamp,
  mod_userid varchar(64), mod_time timestamp
);

create table if not exists agent_key_file.agent_keyfile_pending (
  id varchar(64) primary key,
  tre_code varchar(64), biz_date date, zip_name varchar(512) not null,
  sr_name varchar(512), sr_count integer, sr_exception text, sr_name_state varchar(1),
  zc_name varchar(512), zc_count integer, zc_exception text, zc_name_state varchar(1),
  kc_name varchar(512), kc_count integer, kc_exception text, kc_name_state varchar(1),
  tk_name varchar(512), tk_count integer, tk_exception text, tk_name_state varchar(1),
  zip_path text, unzip_dir text, state varchar(1) not null default '1',
  add_userid varchar(64), add_time timestamp, mod_userid varchar(64), mod_time timestamp
);

create table if not exists agent_key_file.agent_file_income (
  d_acct date, trecode varchar(64), subject_code varchar(128), taxorgcode varchar(128),
  budget_type varchar(64), level varchar(32), f_amt numeric(20,2), year_amt numeric(20,2),
  key_zip_name varchar(512), add_time timestamp
);
create table if not exists agent_key_file.agent_file_payout (
  d_acct date, trecode varchar(64), subject_code varchar(128), taxorgcode varchar(128),
  code_type varchar(64), level varchar(32), f_amt numeric(20,2), year_amt numeric(20,2),
  key_zip_name varchar(512), add_time timestamp
);
create table if not exists agent_key_file.agent_file_stock (
  d_acct date, trecode varchar(64), level varchar(32), acount_code varchar(128),
  f_bal numeric(20,2), year_init_bal numeric(20,2), key_zip_name varchar(512), add_time timestamp
);
create table if not exists agent_key_file.agent_file_back (
  d_acct date, trecode varchar(64), subject_code varchar(128), budget_type varchar(64),
  level varchar(32), taxorgcode varchar(128), bckreason varchar(512),
  f_amt numeric(20,2), year_amt numeric(20,2), key_zip_name varchar(512), add_time timestamp
);

create table if not exists agent_key_file.tims_file_pending (
  id varchar(64) primary key,
  tre_code varchar(64), biz_type varchar(1) not null, biz_date date,
  file_name varchar(512) not null, file_path text, zip_file_path text,
  data_count integer, file_exception text, state varchar(1) not null default '1',
  add_userid varchar(64), add_time timestamp, mod_userid varchar(64), mod_time timestamp
);
create table if not exists agent_key_file.tims_file_income (
  d_acct date, trecode varchar(64), tername varchar(255), subject_code varchar(128),
  subject_name varchar(512), level varchar(32), f_amt numeric(20,2), year_amt numeric(20,2),
  tims_file_name varchar(512), add_time timestamp
);
create table if not exists agent_key_file.tims_file_payout (
  d_acct date, trecode varchar(64), tername varchar(255), subject_code varchar(128),
  subject_name varchar(512), level varchar(32), f_amt numeric(20,2), year_amt numeric(20,2),
  tims_file_name varchar(512), add_time timestamp
);
create table if not exists agent_key_file.tims_file_stock (
  d_acct date, trecode varchar(64), tername varchar(255), level varchar(32), account varchar(128),
  debit_amount numeric(20,2), credit_amount numeric(20,2), balance numeric(20,2),
  tims_file_name varchar(512), add_time timestamp
);

create index if not exists idx_key_pending_state on agent_key_file.agent_keyfile_pending(state, biz_date, tre_code);
create index if not exists idx_tims_pending_state on agent_key_file.tims_file_pending(state, biz_date, tre_code, biz_type);
create index if not exists idx_tims_income_scope on agent_key_file.tims_file_income(d_acct, trecode);
create index if not exists idx_tims_payout_scope on agent_key_file.tims_file_payout(d_acct, trecode);
create index if not exists idx_tims_stock_scope on agent_key_file.tims_file_stock(d_acct, trecode);

-- JAR 配置 DATABASE=stg 且反编译代码唯一拼接出这三张表。
-- JAR 实际写入 BATCH_DATE=yyyyMMdd、DATA_DATE=yyyyMM，脱敏收入样例的 D_ACCT 也是 yyyyMM，
-- 因此模拟环境按 varchar(8) 保证原值可执行；真实数仓字段类型仍须用 03_stg_structure_check.sql 核实。
create table if not exists stg.trs_tmis_budget_income (
  batch_date varchar(8), data_date varchar(8), d_acct varchar(8), trecode varchar(64), tredscr varchar(255),
  tax_org_code varchar(128), level varchar(32), subject_code varchar(128), subject_dscr varchar(512),
  this_amt numeric(20,2), year_amt numeric(20,2)
);
create table if not exists stg.trs_tmis_budget_payout (
  batch_date varchar(8), data_date varchar(8), d_acct varchar(8), trecode varchar(64), tredscr varchar(255),
  level varchar(32), subject_code varchar(128), subject_dscr varchar(512),
  this_amt numeric(20,2), year_amt numeric(20,2)
);
create table if not exists stg.trs_tmis_stock (
  batch_date varchar(8), data_date varchar(8), d_acct varchar(8), trecode varchar(64), tredscr varchar(255),
  level varchar(32), f_debitamt numeric(20,2), f_loanamt numeric(20,2), f_balance numeric(20,2)
);

-- EDW 对象仅覆盖 JAR 查询到的字段，用于开发环境页面和 Mapper 验证；不能替代真实数仓定义。
create table if not exists edw.cm_guoku_dimnsn (
  guoku_id varchar(64) primary key, guoku_dscr varchar(255), level integer
);
create table if not exists edw.income_report_detail_stat (
  s_bookorgcode varchar(128), d_acct date, s_biztype varchar(64), s_trasrlno varchar(128),
  s_tratrecode varchar(64), guoku_lvl varchar(32), s_taxorgcode varchar(128),
  month_f_amt numeric(20,2), f_amt numeric(20,2), year_f_amt numeric(20,2),
  s_bdgsbtcode varchar(128), sort_id varchar(64), type_id varchar(64), class_id varchar(64),
  flitting_flag varchar(32), statistics_code varchar(128), t_sub varchar(128), c_bdglevel varchar(32),
  s_vouno varchar(128), table_name varchar(255), jurisdiction varchar(128)
);
create table if not exists edw.payout_report_detail_stat (like edw.income_report_detail_stat including defaults);
create table if not exists edw.reprot_update_record (
  id varchar(64) primary key, type varchar(16) not null, s_trecode varchar(64), d_acct date,
  statistics_code varchar(128), c_bdglevel varchar(32), old_f_amt numeric(20,2),
  new_f_amt numeric(20,2), diff_f_amt numeric(20,2), update_date timestamp, update_user varchar(64)
);

-- JAR 对该表 INSERT 时不指定列名，因此此列顺序仅为可运行模拟假设，必须以内网 DDL 校正。
create table if not exists etl.guoku_lib_report_all_log (
  id varchar(64) primary key, proc_name varchar(255), d_acct date, state varchar(1),
  add_userid varchar(64), add_time timestamp, end_time timestamp
);

-- 仅让开发环境可执行 CALL；真实 ADM 加工逻辑无法从 JAR 反推，内网必须以真实过程定义替换。
create or replace procedure adm.p_guoku_lib_report_all(in p_data_date date)
language plpgsql
as $$
begin
  raise notice 'SIMULATION ONLY: P_GUOKU_LIB_REPORT_ALL called for %', p_data_date;
end;
$$;
