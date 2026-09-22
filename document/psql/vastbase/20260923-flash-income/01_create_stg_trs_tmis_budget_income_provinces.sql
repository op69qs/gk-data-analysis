-- 目标：创建快报收入贴源表（与信创前 stg.sql 对齐）
-- 可重复执行：是（IF NOT EXISTS）
-- 来源：快报收入数据/stg.sql → trs_tmis_budget_income_provinces
-- 执行时间请记录：开始 ________  结束 ________  库 ________

CREATE SCHEMA IF NOT EXISTS stg;

CREATE TABLE IF NOT EXISTS stg.trs_tmis_budget_income_provinces (
  area_no       varchar(8),
  data_date     char(8),
  table_nature  varchar(50),
  org_no        varchar(14),
  add_date      timestamp DEFAULT CURRENT_TIMESTAMP,
  rows_id       varchar(100),
  batch_date    char(8),
  sheet_id      varchar(100),
  trecode       varchar(20),
  tredscr       varchar(200),
  subject_code  varchar(10),
  subject_dscr  varchar(200),
  this_amt      numeric(18, 2),
  year_amt      numeric(18, 2)
);

COMMENT ON TABLE stg.trs_tmis_budget_income_provinces IS '快报收入贴源（DIS/TIMS 快报_收入数据）';

-- 执行后核对
SELECT CURRENT_TIMESTAMP AS check_time,
       current_database() AS db_name,
       to_regclass('stg.trs_tmis_budget_income_provinces') AS table_regclass;

SELECT column_name, data_type, character_maximum_length, numeric_precision, numeric_scale
  FROM information_schema.columns
 WHERE table_schema = 'stg'
   AND table_name = 'trs_tmis_budget_income_provinces'
 ORDER BY ordinal_position;
