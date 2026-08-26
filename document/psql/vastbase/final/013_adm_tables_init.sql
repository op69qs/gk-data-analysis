-- Generated from document/psql/mysql/adm.sql; do not edit by hand.
CREATE SCHEMA IF NOT EXISTS adm;
SET search_path TO adm, public;

CREATE TABLE IF NOT EXISTS adm.ana_cx_mth_cash_servinfo (
  data_date date NULL DEFAULT NULL,
  month varchar(10) NULL DEFAULT NULL,
  city_code varchar(10) NULL DEFAULT NULL,
  city_name varchar(50) NULL DEFAULT NULL,
  county_code varchar(10) NULL DEFAULT NULL,
  county_name varchar(50) NULL DEFAULT NULL,
  town_name varchar(255) NULL DEFAULT NULL,
  village_name varchar(255) NULL DEFAULT NULL,
  open_account varchar(50) NULL DEFAULT NULL,
  trade_active int NULL DEFAULT NULL,
  trade_ineffict int NULL DEFAULT NULL,
  trade_active_lm int NULL DEFAULT NULL,
  trade_ineffict_lm int NULL DEFAULT NULL,
  trade_active_ly int NULL DEFAULT NULL,
  trade_ineffict_ly int NULL DEFAULT NULL,
  trade_num int NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_cx_mth_cash_servinfo_map (
  data_date date NULL DEFAULT NULL,
  month varchar(10) NULL DEFAULT NULL,
  area_code varchar(10) NULL DEFAULT NULL,
  area_name varchar(50) NULL DEFAULT NULL,
  area_iso_id varchar(10) NULL DEFAULT NULL,
  trade_active int NULL DEFAULT NULL,
  trade_ineffict int NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_cx_mth_cash_servinfo_tmp (
  trade_date varchar(10) NULL DEFAULT NULL,
  area_code char(6) NULL DEFAULT NULL,
  area_name varchar(30) NULL DEFAULT NULL,
  city_code char(6) NULL DEFAULT NULL,
  city_name varchar(30) NULL DEFAULT NULL,
  trade_active double NULL DEFAULT NULL,
  trade_ineffict double NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_cx_qtr_accts (
  data_date date NULL DEFAULT NULL,
  quarter varchar(10) NULL DEFAULT NULL,
  city_code varchar(10) NULL DEFAULT NULL,
  city_name varchar(50) NULL DEFAULT NULL,
  county_code varchar(10) NULL DEFAULT NULL,
  county_name varchar(50) NULL DEFAULT NULL,
  area_type_code varchar(10) NULL DEFAULT NULL,
  type1 varchar(50) NULL DEFAULT NULL,
  type2 varchar(50) NULL DEFAULT NULL,
  bank decimal(28, 8) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_cx_qtr_bank_card (
  data_date date NULL DEFAULT NULL,
  quarter varchar(10) NULL DEFAULT NULL,
  city_code varchar(10) NULL DEFAULT NULL,
  city_name varchar(50) NULL DEFAULT NULL,
  county_code varchar(10) NULL DEFAULT NULL,
  county_name varchar(50) NULL DEFAULT NULL,
  area_type_code varchar(5) NULL DEFAULT NULL,
  business_dscr_2 varchar(100) NULL DEFAULT NULL,
  number int NULL DEFAULT NULL,
  count decimal(30, 6) NULL DEFAULT NULL,
  count_ly decimal(30, 6) NULL DEFAULT NULL,
  money decimal(30, 6) NULL DEFAULT NULL,
  money_ly decimal(30, 6) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_cx_qtr_bank_cards (
  data_date date NULL DEFAULT NULL,
  quarter varchar(10) NULL DEFAULT NULL,
  city_code varchar(10) NULL DEFAULT NULL,
  city_name varchar(50) NULL DEFAULT NULL,
  county_code varchar(10) NULL DEFAULT NULL,
  county_name varchar(50) NULL DEFAULT NULL,
  area_type_code varchar(10) NULL DEFAULT NULL,
  type varchar(50) NULL DEFAULT NULL,
  card decimal(30, 8) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_cx_qtr_bank_dq_card (
  data_date varchar(10) NULL DEFAULT NULL,
  quarter char(8) NULL DEFAULT NULL,
  city_code varchar(20) NULL DEFAULT NULL,
  city_name varchar(100) NULL DEFAULT NULL,
  county_code varchar(20) NOT NULL DEFAULT '',
  county_name varchar(100) NOT NULL DEFAULT '',
  area_type_code varchar(2) NOT NULL DEFAULT '',
  business_dscr_2 varchar(30) NULL DEFAULT NULL,
  number decimal(32, 0) NULL DEFAULT NULL,
  count decimal(50, 8) NULL DEFAULT NULL,
  money decimal(50, 8) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_cx_qtr_cash_servinfo (
  data_date date NULL DEFAULT NULL,
  month varchar(10) NULL DEFAULT NULL,
  city_code varchar(10) NULL DEFAULT NULL,
  city_name varchar(50) NULL DEFAULT NULL,
  county_code varchar(10) NULL DEFAULT NULL,
  county_name varchar(50) NULL DEFAULT NULL,
  trade_type varchar(50) NULL DEFAULT NULL,
  count decimal(30, 6) NULL DEFAULT NULL,
  count_ly decimal(30, 6) NULL DEFAULT NULL,
  count_lm decimal(30, 6) NULL DEFAULT NULL,
  money decimal(30, 6) NULL DEFAULT NULL,
  money_ly decimal(30, 6) NULL DEFAULT NULL,
  money_lm decimal(30, 6) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_cx_qtr_cash_servinfo_tmp (
  data_date date NULL DEFAULT NULL,
  month varchar(10) NULL DEFAULT NULL,
  city_code varchar(10) NULL DEFAULT NULL,
  city_name varchar(50) NULL DEFAULT NULL,
  area_code varchar(10) NULL DEFAULT NULL,
  area_name varchar(50) NULL DEFAULT NULL,
  trade_type varchar(50) NULL DEFAULT NULL,
  trade_num decimal(30, 6) NULL DEFAULT NULL,
  trade_amount decimal(30, 6) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_cx_qtr_cls (
  data_date date NULL DEFAULT NULL,
  quarter varchar(10) NULL DEFAULT NULL,
  city_code varchar(10) NULL DEFAULT NULL,
  city_name varchar(50) NULL DEFAULT NULL,
  county_code varchar(10) NULL DEFAULT NULL,
  county_name varchar(50) NULL DEFAULT NULL,
  area_type_code varchar(2) NULL DEFAULT NULL,
  bank_branch int NULL DEFAULT NULL,
  agency_branch int NULL DEFAULT NULL,
  count decimal(30, 6) NULL DEFAULT NULL,
  count_ly decimal(30, 6) NULL DEFAULT NULL,
  money decimal(30, 6) NULL DEFAULT NULL,
  money_ly decimal(30, 6) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_cx_qtr_country_num (
  data_date date NULL DEFAULT NULL,
  quarter varchar(10) NULL DEFAULT NULL,
  city_code varchar(10) NULL DEFAULT NULL,
  city_name varchar(50) NULL DEFAULT NULL,
  county_code varchar(10) NULL DEFAULT NULL,
  county_name varchar(50) NULL DEFAULT NULL,
  area_type_code varchar(5) NULL DEFAULT NULL,
  no_country int NULL DEFAULT NULL,
  country_rate decimal(30, 6) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_cx_qtr_dq_cls (
  data_date varchar(10) NULL DEFAULT NULL,
  quarter char(8) NULL DEFAULT NULL,
  city_code varchar(20) NULL DEFAULT NULL,
  city_name varchar(100) NULL DEFAULT NULL,
  county_code varchar(20) NOT NULL DEFAULT '',
  county_name varchar(100) NOT NULL DEFAULT '',
  area_type_code varchar(2) NOT NULL DEFAULT '',
  bank_branch decimal(32, 0) NULL DEFAULT NULL,
  agency_branch decimal(32, 0) NULL DEFAULT NULL,
  count decimal(50, 8) NULL DEFAULT NULL,
  money decimal(50, 8) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_cx_qtr_non_cash_dq_pay (
  data_date varchar(10) NULL DEFAULT NULL,
  quarter char(8) NULL DEFAULT NULL,
  city_code varchar(20) NULL DEFAULT NULL,
  city_name varchar(100) NULL DEFAULT NULL,
  county_code varchar(20) NOT NULL DEFAULT '',
  county_name varchar(100) NOT NULL DEFAULT '',
  area_type_code varchar(30) NOT NULL DEFAULT '',
  business_dscr_1 varchar(30) NULL DEFAULT NULL,
  count decimal(50, 8) NULL DEFAULT NULL,
  money decimal(50, 8) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_cx_qtr_non_cash_pay (
  data_date date NULL DEFAULT NULL,
  quarter varchar(10) NULL DEFAULT NULL,
  city_code varchar(10) NULL DEFAULT NULL,
  city_name varchar(50) NULL DEFAULT NULL,
  county_code varchar(10) NULL DEFAULT NULL,
  county_name varchar(50) NULL DEFAULT NULL,
  area_type_code varchar(5) NULL DEFAULT NULL,
  business_dscr_1 varchar(100) NULL DEFAULT NULL,
  count decimal(30, 6) NULL DEFAULT NULL,
  count_ly decimal(30, 6) NULL DEFAULT NULL,
  money decimal(30, 6) NULL DEFAULT NULL,
  money_ly decimal(30, 6) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_cx_qtr_non_cash_pay_kh (
  data_date date NULL DEFAULT NULL,
  quarter varchar(10) NULL DEFAULT NULL,
  city_code varchar(10) NULL DEFAULT NULL,
  city_name varchar(50) NULL DEFAULT NULL,
  county_code varchar(10) NULL DEFAULT NULL,
  county_name varchar(50) NULL DEFAULT NULL,
  area_type_code varchar(10) NULL DEFAULT NULL,
  business_dscr_1 varchar(50) NULL DEFAULT NULL,
  account_number int NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_cx_qtr_static (
  data_date date NULL DEFAULT NULL,
  quarter varchar(10) NULL DEFAULT NULL,
  city_code varchar(10) NULL DEFAULT NULL,
  city_name varchar(50) NULL DEFAULT NULL,
  county_code varchar(10) NULL DEFAULT NULL,
  county_name varchar(50) NULL DEFAULT NULL,
  area_type_code varchar(10) NULL DEFAULT NULL,
  county_num_country int NULL DEFAULT NULL,
  country_num_country int NULL DEFAULT NULL,
  village_num_country int NULL DEFAULT NULL,
  population_num_country decimal(28, 8) NULL DEFAULT NULL,
  outlets_num_country int NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_cx_qtr_trade_all (
  data_date date NULL DEFAULT NULL,
  quarter varchar(10) NULL DEFAULT NULL,
  city_code varchar(10) NULL DEFAULT NULL,
  city_name varchar(50) NULL DEFAULT NULL,
  county_code varchar(10) NULL DEFAULT NULL,
  county_name varchar(50) NULL DEFAULT NULL,
  area_type_code varchar(10) NULL DEFAULT NULL,
  trade_type varchar(50) NULL DEFAULT NULL,
  count decimal(30, 6) NULL DEFAULT NULL,
  count_ly decimal(30, 6) NULL DEFAULT NULL,
  money decimal(30, 6) NULL DEFAULT NULL,
  money_ly decimal(30, 6) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_glr_mth_bad_org_top5 (
  fiscal_date date NULL DEFAULT NULL,
  fiscal_mth varchar(10) NULL DEFAULT NULL,
  org_id varchar(20) NULL DEFAULT NULL,
  org_name varchar(50) NULL DEFAULT NULL,
  bad_loan_remain decimal(30, 6) NULL DEFAULT NULL,
  bad_loan_remain_rank int NULL DEFAULT NULL,
  rank_dscr varchar(15) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_glr_mth_begin_increase_dtop5 (
  fiscal_date date NULL DEFAULT NULL,
  fiscal_mth varchar(10) NULL DEFAULT NULL,
  org_id varchar(25) NULL DEFAULT NULL,
  org_name varchar(50) NULL DEFAULT NULL,
  deposit_remain_increase decimal(30, 5) NULL DEFAULT NULL,
  deposit_remain_end_rank smallint NULL DEFAULT NULL,
  rank_dscr varchar(15) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_glr_mth_begin_increase_top5 (
  fiscal_date date NULL DEFAULT NULL,
  fiscal_mth varchar(10) NULL DEFAULT NULL,
  org_id varchar(25) NULL DEFAULT NULL,
  org_name varchar(50) NULL DEFAULT NULL,
  loan_remain_increase decimal(30, 5) NULL DEFAULT NULL,
  loan_remain_end_rank smallint NULL DEFAULT NULL,
  rank_dscr varchar(15) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_glr_mth_credit (
  fiscal_date date NULL DEFAULT NULL,
  fiscal_mth varchar(10) NULL DEFAULT NULL,
  city_id varchar(10) NULL DEFAULT NULL,
  city_name varchar(20) NULL DEFAULT NULL,
  area_no_id varchar(10) NULL DEFAULT NULL,
  area_dscr varchar(20) NULL DEFAULT NULL,
  org_type_id varchar(25) NULL DEFAULT NULL,
  org_type_name varchar(25) NULL DEFAULT NULL,
  compancy_scale_id varchar(25) NULL DEFAULT NULL,
  compancy_scale_name varchar(50) NULL DEFAULT NULL,
  direction_industry_id_1 varchar(25) NULL DEFAULT NULL,
  direction_industry_name_1 varchar(50) NULL DEFAULT NULL,
  normal_loan_remain decimal(30, 6) NULL DEFAULT NULL,
  attent_loan_remain decimal(30, 6) NULL DEFAULT NULL,
  bad_loan_remain decimal(30, 6) NULL DEFAULT NULL,
  normal_loan_remain_ly decimal(30, 6) NULL DEFAULT NULL,
  attent_loan_remain_ly decimal(30, 6) NULL DEFAULT NULL,
  bad_loan_remain_ly decimal(30, 6) NULL DEFAULT NULL,
  bad_loan_remain_dm decimal(30, 6) NULL DEFAULT NULL,
  bad_loan_remain_dm_ly decimal(30, 6) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_glr_mth_credit_map (
  fiscal_date date NULL DEFAULT NULL,
  fiscal_mth varchar(10) NULL DEFAULT NULL,
  iso_id varchar(20) NULL DEFAULT NULL,
  area_no_id varchar(10) NULL DEFAULT NULL,
  area_dscr varchar(20) NULL DEFAULT NULL,
  bad_loan_remain decimal(30, 6) NULL DEFAULT NULL,
  bad_loan_remain_ly decimal(30, 6) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_glr_mth_credit_monitor (
  fiscal_date date NULL DEFAULT NULL,
  fiscal_mth varchar(10) NULL DEFAULT NULL,
  city_id varchar(10) NULL DEFAULT NULL,
  city_name varchar(20) NULL DEFAULT NULL,
  company_scale_id varchar(25) NULL DEFAULT NULL,
  company_scale_name varchar(50) NULL DEFAULT NULL,
  direction_industry_id varchar(25) NULL DEFAULT NULL,
  direction_industry_name varchar(50) NULL DEFAULT NULL,
  loan_remain decimal(30, 5) NULL DEFAULT NULL,
  attent_loan_remain decimal(30, 5) NULL DEFAULT NULL,
  attent_loan_remainby decimal(30, 5) NULL DEFAULT NULL,
  attent_loan_remain_ly decimal(30, 5) NULL DEFAULT NULL,
  attent_bad_loan decimal(30, 5) NULL DEFAULT NULL,
  attent_bad_loan_by decimal(30, 5) NULL DEFAULT NULL,
  bad_loan_remain decimal(30, 5) NULL DEFAULT NULL,
  bad_loan_remainby decimal(30, 5) NULL DEFAULT NULL,
  bad_loan_remain_ly decimal(30, 5) NULL DEFAULT NULL,
  bad_loan_rate decimal(30, 5) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_glr_mth_deposit_summary (
  fiscal_date date NULL DEFAULT NULL,
  fiscal_mth varchar(10) NULL DEFAULT NULL,
  city_id varchar(10) NULL DEFAULT NULL,
  city_name varchar(20) NULL DEFAULT NULL,
  deposit_type_id varchar(25) NULL DEFAULT NULL,
  deposit_type varchar(25) NULL DEFAULT NULL,
  deposit_remain decimal(30, 5) NULL DEFAULT NULL,
  deposit_remain_ly decimal(30, 5) NULL DEFAULT NULL,
  deposit_remain_zdm decimal(30, 5) NULL DEFAULT NULL,
  deposit_remain_zdm_ly decimal(30, 5) NULL DEFAULT NULL,
  deposit_remain_zby decimal(30, 5) NULL DEFAULT NULL,
  deposit_remain_zby_ly decimal(30, 5) NULL DEFAULT NULL,
  deposit_remaint decimal(30, 5) NULL DEFAULT NULL,
  deposit_remaint_ly decimal(30, 5) NULL DEFAULT NULL,
  deposit_remain_zdmt decimal(30, 5) NULL DEFAULT NULL,
  deposit_remain_zdmt_ly decimal(30, 5) NULL DEFAULT NULL,
  deposit_remain_zbyt decimal(30, 5) NULL DEFAULT NULL,
  deposit_remain_zbyt_ly decimal(30, 5) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_glr_mth_dm_increase_top5 (
  fiscal_date date NULL DEFAULT NULL,
  fiscal_mth varchar(10) NULL DEFAULT NULL,
  org_id varchar(25) NULL DEFAULT NULL,
  org_name varchar(50) NULL DEFAULT NULL,
  deposit_mm_increase decimal(30, 5) NULL DEFAULT NULL,
  deposit_mm_end_rank smallint NULL DEFAULT NULL,
  rank_dscr varchar(15) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_glr_mth_fina_jzfp (
  fiscal_date date NULL DEFAULT NULL,
  fiscal_mth varchar(10) NULL DEFAULT NULL,
  dscr_qua varchar(255) NULL DEFAULT NULL,
  city_id varchar(255) NULL DEFAULT NULL,
  city_name varchar(255) NULL DEFAULT NULL,
  org_type_id varchar(255) NULL DEFAULT NULL,
  org_type_name varchar(255) NULL DEFAULT NULL,
  org_id varchar(255) NULL DEFAULT NULL,
  org_name varchar(255) NULL DEFAULT NULL,
  loan_type_id varchar(255) NULL DEFAULT NULL,
  loan_type_name varchar(255) NULL DEFAULT NULL,
  loan_remain decimal(30, 5) NULL DEFAULT NULL,
  loan_remain_ly decimal(30, 5) NULL DEFAULT NULL,
  loan_ff decimal(30, 5) NULL DEFAULT NULL,
  loan_ff_ly decimal(30, 5) NULL DEFAULT NULL,
  stroke_count int NULL DEFAULT NULL,
  spur_serv_poor_num int NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_glr_mth_fina_jzfp_1 (
  fiscal_date date NULL DEFAULT NULL,
  fiscal_mth varchar(10) NULL DEFAULT NULL,
  dscr_qua varchar(255) NULL DEFAULT NULL,
  city_id varchar(255) NULL DEFAULT NULL,
  city_name varchar(255) NULL DEFAULT NULL,
  loan_type_id varchar(255) NULL DEFAULT NULL,
  loan_type_name varchar(255) NULL DEFAULT NULL,
  loan_remain decimal(30, 5) NULL DEFAULT NULL,
  loan_ff decimal(30, 5) NULL DEFAULT NULL,
  loan_ff_lytb decimal(30, 5) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_glr_mth_lm_increase_top5 (
  fiscal_date date NULL DEFAULT NULL,
  fiscal_mth varchar(10) NULL DEFAULT NULL,
  org_id varchar(25) NULL DEFAULT NULL,
  org_name varchar(50) NULL DEFAULT NULL,
  loan_mm_increase decimal(30, 5) NULL DEFAULT NULL,
  loan_mm_end_rank smallint NULL DEFAULT NULL,
  rank_dscr varchar(15) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_glr_mth_loan_summary (
  fiscal_date date NULL DEFAULT NULL,
  fiscal_mth varchar(10) NULL DEFAULT NULL,
  city_id varchar(10) NULL DEFAULT NULL,
  city_name varchar(20) NULL DEFAULT NULL,
  direction_industry_id varchar(25) NULL DEFAULT NULL,
  direction_industry_name varchar(25) NULL DEFAULT NULL,
  loan_remain decimal(30, 5) NULL DEFAULT NULL,
  loan_remain_ly decimal(30, 5) NULL DEFAULT NULL,
  loan_remain_zdm decimal(30, 5) NULL DEFAULT NULL,
  loan_remain_zdm_ly decimal(30, 5) NULL DEFAULT NULL,
  loan_remain_zby decimal(30, 5) NULL DEFAULT NULL,
  loan_remain_zby_ly decimal(30, 5) NULL DEFAULT NULL,
  loan_remaint decimal(30, 5) NULL DEFAULT NULL,
  loan_remaint_ly decimal(30, 5) NULL DEFAULT NULL,
  loan_remain_zdmt decimal(30, 5) NULL DEFAULT NULL,
  loan_remain_zdmt_ly decimal(30, 5) NULL DEFAULT NULL,
  loan_remain_zbyt decimal(30, 5) NULL DEFAULT NULL,
  loan_remain_zbyt_ly decimal(30, 5) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_glr_mth_loan_summary_1 (
  fiscal_date date NULL DEFAULT NULL,
  fiscal_mth varchar(10) NULL DEFAULT NULL,
  city_id varchar(10) NULL DEFAULT NULL,
  city_name varchar(20) NULL DEFAULT NULL,
  deposit_remain decimal(30, 5) NULL DEFAULT NULL,
  deposit_remain_ly decimal(30, 5) NULL DEFAULT NULL,
  deposit_remain_zdm decimal(30, 5) NULL DEFAULT NULL,
  deposit_remain_zdm_ly decimal(30, 5) NULL DEFAULT NULL,
  deposit_remain_zby decimal(30, 5) NULL DEFAULT NULL,
  deposit_remain_zby_ly decimal(30, 5) NULL DEFAULT NULL,
  deposit_remaint decimal(30, 5) NULL DEFAULT NULL,
  deposit_remaint_ly decimal(30, 5) NULL DEFAULT NULL,
  deposit_remain_zdmt decimal(30, 5) NULL DEFAULT NULL,
  deposit_remain_zdmt_ly decimal(30, 5) NULL DEFAULT NULL,
  deposit_remain_zbyt decimal(30, 5) NULL DEFAULT NULL,
  deposit_remain_zbyt_ly decimal(30, 5) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_glr_mth_pay_increase_top5 (
  fiscal_date date NULL DEFAULT NULL,
  fiscal_mth varchar(10) NULL DEFAULT NULL,
  org_id varchar(25) NULL DEFAULT NULL,
  org_name varchar(50) NULL DEFAULT NULL,
  loan_pay_tb decimal(30, 6) NULL DEFAULT NULL,
  loan_pay_rank smallint NULL DEFAULT NULL,
  rank_dscr varchar(25) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_glr_mth_real_estate_loans (
  fiscal_date date NULL DEFAULT NULL,
  fiscal_mth varchar(10) NULL DEFAULT NULL,
  area_no_id varchar(10) NULL DEFAULT NULL,
  area_dscr varchar(20) NULL DEFAULT NULL,
  real_estate_loans_type1_id varchar(50) NULL DEFAULT NULL,
  loans_type1_dscr varchar(100) NULL DEFAULT NULL,
  service_type varchar(20) NULL DEFAULT NULL,
  loan_amount decimal(30, 5) NULL DEFAULT NULL,
  loan_amount_ly decimal(30, 5) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_glr_mth_real_estate_loans_map (
  fiscal_date date NULL DEFAULT NULL,
  fiscal_mth varchar(10) NULL DEFAULT NULL,
  iso_id varchar(20) NULL DEFAULT NULL,
  area_no_id varchar(10) NULL DEFAULT NULL,
  area_dscr varchar(20) NULL DEFAULT NULL,
  org_type_id varchar(25) NULL DEFAULT NULL,
  org_type_name varchar(25) NULL DEFAULT NULL,
  real_estate_loans_type_id varchar(50) NULL DEFAULT NULL,
  real_estate_loans_type_dscr varchar(100) NULL DEFAULT NULL,
  loan_remain decimal(30, 6) NULL DEFAULT NULL,
  loan_remain_ly decimal(30, 6) NULL DEFAULT NULL,
  loan_remain_by decimal(30, 6) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_glr_mth_real_estate_loans_org (
  fiscal_date date NULL DEFAULT NULL,
  fiscal_mth varchar(10) NULL DEFAULT NULL,
  org_id varchar(25) NULL DEFAULT NULL,
  org_name varchar(25) NULL DEFAULT NULL,
  service_type varchar(20) NULL DEFAULT NULL,
  loan_amount decimal(30, 5) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_glr_mth_real_estate_loans_rate (
  fiscal_date date NULL DEFAULT NULL,
  fiscal_mth varchar(10) NULL DEFAULT NULL,
  service_type varchar(20) NULL DEFAULT NULL,
  real_estate_loans decimal(30, 6) NULL DEFAULT NULL,
  real_estate_loans_dev decimal(30, 6) NULL DEFAULT NULL,
  real_estate_loans_per decimal(30, 6) NULL DEFAULT NULL,
  loans_total decimal(30, 6) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_glr_mth_remain_increase_dtop5 (
  fiscal_date date NULL DEFAULT NULL,
  fiscal_mth varchar(10) NULL DEFAULT NULL,
  org_id varchar(25) NULL DEFAULT NULL,
  org_name varchar(50) NULL DEFAULT NULL,
  deposit_remain_tb decimal(30, 5) NULL DEFAULT NULL,
  deposit_remain_rank int NULL DEFAULT NULL,
  rank_dscr varchar(25) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_glr_mth_remain_increase_top5 (
  fiscal_date date NULL DEFAULT NULL,
  fiscal_mth varchar(10) NULL DEFAULT NULL,
  org_id varchar(25) NULL DEFAULT NULL,
  org_name varchar(50) NULL DEFAULT NULL,
  loan_remain_tb decimal(30, 5) NULL DEFAULT NULL,
  loan_remain_rank int NULL DEFAULT NULL,
  rank_dscr varchar(25) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_glr_mth_small_micro (
  fiscal_date date NULL DEFAULT NULL,
  fiscal_mth varchar(10) NULL DEFAULT NULL,
  city_id varchar(10) NULL DEFAULT NULL,
  city_name varchar(20) NULL DEFAULT NULL,
  area_no_id varchar(10) NULL DEFAULT NULL,
  area_dscr varchar(20) NULL DEFAULT NULL,
  dim_code varchar(50) NULL DEFAULT NULL,
  dim_dscr varchar(50) NULL DEFAULT NULL,
  dim_type varchar(50) NULL DEFAULT NULL,
  service_type varchar(50) NULL DEFAULT NULL,
  loan_amount decimal(30, 5) NULL DEFAULT NULL,
  loan_amount_ly decimal(30, 5) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_glr_mth_small_micro_bad (
  fiscal_date date NULL DEFAULT NULL,
  fiscal_mth varchar(10) NULL DEFAULT NULL,
  org_id varchar(20) NULL DEFAULT NULL,
  org_name varchar(50) NULL DEFAULT NULL,
  bad_loan_rate decimal(30, 6) NULL DEFAULT NULL,
  bad_loan_rate_rank smallint NULL DEFAULT NULL,
  rank_dscr varchar(15) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_glr_mth_small_micro_map (
  fiscal_date date NULL DEFAULT NULL,
  fiscal_mth varchar(10) NULL DEFAULT NULL,
  iso_id varchar(20) NULL DEFAULT NULL,
  area_no_id varchar(10) NULL DEFAULT NULL,
  area_dscr varchar(20) NULL DEFAULT NULL,
  service_type varchar(20) NULL DEFAULT NULL,
  loan_amount decimal(30, 6) NULL DEFAULT NULL,
  loan_amount_ly decimal(30, 6) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_glr_mth_small_micro_rate (
  fiscal_date date NULL DEFAULT NULL,
  fiscal_mth varchar(10) NULL DEFAULT NULL,
  service_type varchar(20) NULL DEFAULT NULL,
  loans_count decimal(30, 6) NULL DEFAULT NULL,
  loans_total decimal(30, 6) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_glr_mth_summary (
  fiscal_date date NULL DEFAULT NULL,
  fiscal_mth varchar(10) NULL DEFAULT NULL,
  city_id varchar(10) NULL DEFAULT NULL,
  city_name varchar(20) NULL DEFAULT NULL,
  area_no_id varchar(10) NULL DEFAULT NULL,
  area_dscr varchar(20) NULL DEFAULT NULL,
  org_type_id varchar(25) NULL DEFAULT NULL,
  org_type_name varchar(50) NULL DEFAULT NULL,
  dim_code varchar(50) NULL DEFAULT NULL,
  dim_dscr varchar(50) NULL DEFAULT NULL,
  dim_type varchar(50) NULL DEFAULT NULL,
  loan_ff decimal(30, 6) NULL DEFAULT NULL,
  loan_ff_ly decimal(30, 6) NULL DEFAULT NULL,
  loan_remain decimal(30, 6) NULL DEFAULT NULL,
  loan_remain_ly decimal(30, 6) NULL DEFAULT NULL,
  loan_remain_by decimal(30, 6) NULL DEFAULT NULL,
  stroke_count bigint NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_glr_mth_summary_map (
  fiscal_date date NULL DEFAULT NULL,
  fiscal_mth varchar(10) NULL DEFAULT NULL,
  iso_id varchar(20) NULL DEFAULT NULL,
  area_no_id varchar(10) NULL DEFAULT NULL,
  area_dscr varchar(20) NULL DEFAULT NULL,
  loan_remain decimal(30, 6) NULL DEFAULT NULL,
  loan_remain_ly decimal(30, 6) NULL DEFAULT NULL,
  loan_remain_by decimal(30, 6) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_sust_cm_service_type (
  service_type varchar(200) NULL DEFAULT NULL,
  item_category_code varchar(200) NULL DEFAULT NULL,
  item_category varchar(200) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_sust_mth_credit_rece_pay_area (
  data_date date NULL DEFAULT NULL,
  data_mth char(6) NULL DEFAULT NULL,
  iso_id varchar(6) NULL DEFAULT NULL,
  area_dscr varchar(60) NULL DEFAULT NULL,
  dep_bal decimal(18, 2) NULL DEFAULT NULL,
  loan_bal decimal(18, 2) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_sust_mth_credit_rece_pay_struc (
  data_date date NULL DEFAULT NULL,
  data_mth char(6) NULL DEFAULT NULL,
  service_type varchar(10) NULL DEFAULT NULL,
  item_category_code char(1) NULL DEFAULT NULL,
  item_category varchar(60) NULL DEFAULT NULL,
  bal decimal(18, 2) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_sust_mth_credit_rece_pay_times (
  data_date date NULL DEFAULT NULL,
  data_mth char(6) NULL DEFAULT NULL,
  dep_bal decimal(18, 2) NULL DEFAULT NULL,
  dep_bal_lm decimal(18, 2) NULL DEFAULT NULL,
  dep_bal_ly_e decimal(18, 2) NULL DEFAULT NULL,
  dep_bal_tb_rate decimal(18, 4) NULL DEFAULT NULL,
  loan_bal decimal(18, 2) NULL DEFAULT NULL,
  loan_bal_lm decimal(18, 2) NULL DEFAULT NULL,
  loan_bal_ly_e decimal(18, 2) NULL DEFAULT NULL,
  loan_bal_tb_rate decimal(18, 4) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_sust_mth_enterprise_survey (
  data_date varchar(100) NULL DEFAULT NULL,
  enterprise_id varchar(100) NULL DEFAULT NULL,
  enterprise_name varchar(100) NULL DEFAULT NULL,
  standard_code varchar(100) NULL DEFAULT NULL,
  standard_norm varchar(100) NULL DEFAULT NULL,
  money varchar(100) NULL DEFAULT NULL,
  judge varchar(10) NULL DEFAULT NULL,
  rank int NULL DEFAULT NULL,
  add_date datetime(0) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_sust_mth_enterprise_survey_temp (
  data_date varchar(100) NULL DEFAULT NULL,
  enterprise_id varchar(100) NULL DEFAULT NULL,
  enterprise_name varchar(100) NULL DEFAULT NULL,
  norm_name varchar(100) NULL DEFAULT NULL,
  money varchar(100) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_sust_mth_enterprise_uploaded (
  d_acct char(6) NULL DEFAULT NULL,
  e_id char(8) NULL DEFAULT NULL,
  path varchar(50) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_sust_mth_special_struc_agricu (
  data_date date NULL DEFAULT NULL,
  data_mth char(6) NULL DEFAULT NULL,
  loan_purpose varchar(20) NULL DEFAULT NULL,
  loan_bal decimal(18, 2) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_sust_mth_special_struc_enter_scale (
  data_date date NULL DEFAULT NULL,
  data_mth char(6) NULL DEFAULT NULL,
  enter_scale varchar(20) NULL DEFAULT NULL,
  loan_bal decimal(18, 2) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_sust_mth_special_struc_realty (
  data_date date NULL DEFAULT NULL,
  data_mth char(6) NULL DEFAULT NULL,
  item_category varchar(20) NULL DEFAULT NULL,
  loan_bal decimal(18, 2) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_sust_mth_special_times (
  data_date date NULL DEFAULT NULL,
  data_mth char(6) NULL DEFAULT NULL,
  realty_loan_bal decimal(18, 2) NULL DEFAULT NULL,
  realty_loan_bal_tb decimal(18, 4) NULL DEFAULT NULL,
  agricu_loan_bal decimal(18, 2) NULL DEFAULT NULL,
  agricu_loan_bal_tb decimal(18, 4) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_trs_income_functional_type (
  d_date date NULL DEFAULT NULL,
  d_year_mth char(7) NULL DEFAULT NULL,
  guoku_id varchar(50) NULL DEFAULT NULL,
  guoku_dscr varchar(200) NULL DEFAULT NULL,
  flag_code char(1) NULL DEFAULT NULL,
  flag varchar(20) NULL DEFAULT NULL,
  fun_class_code varchar(20) NULL DEFAULT NULL,
  fun_class_dscr varchar(100) NULL DEFAULT NULL,
  budget_income decimal(18, 4) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_trs_income_key_enterprises (
  d_date date NULL DEFAULT NULL,
  d_year_mth char(7) NULL DEFAULT NULL,
  guoku_id varchar(50) NULL DEFAULT NULL,
  guoku_dscr varchar(200) NULL DEFAULT NULL,
  flag_code char(1) NULL DEFAULT NULL,
  flag varchar(20) NULL DEFAULT NULL,
  s_handorgname varchar(100) NULL DEFAULT NULL,
  tax_income decimal(18, 4) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_trs_income_payout_comprate_zl (
  d_date date NULL DEFAULT NULL,
  d_year_mth char(7) NULL DEFAULT NULL,
  guoku_id varchar(50) NULL DEFAULT NULL,
  guoku_dscr varchar(200) NULL DEFAULT NULL,
  city_id char(6) NULL DEFAULT NULL,
  iso_id varchar(12) NULL DEFAULT NULL,
  city_name varchar(200) NULL DEFAULT NULL,
  flag_code char(1) NULL DEFAULT NULL,
  flag varchar(20) NULL DEFAULT NULL,
  income_comp decimal(18, 4) NULL DEFAULT NULL,
  income_quota decimal(18, 4) NULL DEFAULT NULL,
  payout_comp decimal(18, 4) NULL DEFAULT NULL,
  payout_quota decimal(18, 4) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_trs_income_payout_stock_zl (
  d_date date NULL DEFAULT NULL,
  d_year_mth char(7) NULL DEFAULT NULL,
  guoku_id varchar(50) NULL DEFAULT NULL,
  guoku_dscr varchar(200) NULL DEFAULT NULL,
  city_id char(6) NULL DEFAULT NULL,
  iso_id varchar(12) NULL DEFAULT NULL,
  city_name varchar(200) NULL DEFAULT NULL,
  flag_code char(1) NULL DEFAULT NULL,
  flag varchar(20) NULL DEFAULT NULL,
  service_type_code char(1) NULL DEFAULT NULL,
  service_type varchar(20) NULL DEFAULT NULL,
  f_amt decimal(18, 4) NULL DEFAULT NULL,
  f_lastyear_amt decimal(18, 4) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_trs_income_subject_struct (
  d_date date NULL DEFAULT NULL,
  d_year_mth char(7) NULL DEFAULT NULL,
  guoku_id varchar(50) NULL DEFAULT NULL,
  guoku_dscr varchar(200) NULL DEFAULT NULL,
  flag_code char(1) NULL DEFAULT NULL,
  flag varchar(20) NULL DEFAULT NULL,
  sub_lvl_code varchar(20) NULL DEFAULT NULL,
  sub_lvl_dscr varchar(100) NULL DEFAULT NULL,
  budget_income decimal(18, 4) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_trs_income_subject_struct_industry (
  d_date date NULL DEFAULT NULL,
  d_year_mth char(7) NULL DEFAULT NULL,
  guoku_id varchar(50) NULL DEFAULT NULL,
  guoku_dscr varchar(200) NULL DEFAULT NULL,
  flag_code char(1) NULL DEFAULT NULL,
  flag varchar(20) NULL DEFAULT NULL,
  lev_1_id varchar(20) NULL DEFAULT NULL,
  lev_1_dscr varchar(100) NULL DEFAULT NULL,
  budget_income decimal(18, 4) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_trs_income_tax_category (
  d_date date NULL DEFAULT NULL,
  d_year_mth char(7) NULL DEFAULT NULL,
  guoku_id varchar(50) NULL DEFAULT NULL,
  guoku_dscr varchar(200) NULL DEFAULT NULL,
  flag_code char(1) NULL DEFAULT NULL,
  flag varchar(20) NULL DEFAULT NULL,
  sub_lvl_code_2 varchar(20) NULL DEFAULT NULL,
  sub_lvl_dscr_2 varchar(100) NULL DEFAULT NULL,
  tax_income decimal(18, 4) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_trs_income_zk (
  d_date date NULL DEFAULT NULL,
  d_year_mth char(7) NULL DEFAULT NULL,
  guoku_id varchar(50) NULL DEFAULT NULL,
  guoku_dscr varchar(200) NULL DEFAULT NULL,
  flag_code char(1) NULL DEFAULT NULL,
  flag varchar(20) NULL DEFAULT NULL,
  income_total decimal(18, 4) NULL DEFAULT NULL,
  income_total_lm decimal(18, 4) NULL DEFAULT NULL,
  income_total_ly decimal(18, 4) NULL DEFAULT NULL,
  tax_income decimal(18, 4) NULL DEFAULT NULL,
  tax_income_lm decimal(18, 4) NULL DEFAULT NULL,
  tax_income_ly decimal(18, 4) NULL DEFAULT NULL,
  budget_income_total decimal(18, 4) NULL DEFAULT NULL,
  budget_income_total_lm decimal(18, 4) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_trs_payout_budget_unit (
  d_date date NULL DEFAULT NULL,
  d_year_mth char(7) NULL DEFAULT NULL,
  guoku_id varchar(50) NULL DEFAULT NULL,
  guoku_dscr varchar(200) NULL DEFAULT NULL,
  flag_code char(1) NULL DEFAULT NULL,
  flag varchar(20) NULL DEFAULT NULL,
  guoku_id_1 varchar(20) NULL DEFAULT NULL,
  guoku_dscr_1 varchar(100) NULL DEFAULT NULL,
  s_payeename varchar(100) NULL DEFAULT NULL,
  focus_payout decimal(18, 4) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_trs_payout_centra_pay (
  d_date date NULL DEFAULT NULL,
  d_year_mth char(7) NULL DEFAULT NULL,
  guoku_id varchar(50) NULL DEFAULT NULL,
  guoku_dscr varchar(200) NULL DEFAULT NULL,
  flag_code char(1) NULL DEFAULT NULL,
  flag varchar(20) NULL DEFAULT NULL,
  pay_service_type_code varchar(20) NULL DEFAULT NULL,
  pay_service_type_dscr varchar(100) NULL DEFAULT NULL,
  budget_payout decimal(18, 4) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_trs_payout_functional_type (
  d_date date NULL DEFAULT NULL,
  d_year_mth char(7) NULL DEFAULT NULL,
  guoku_id varchar(50) NULL DEFAULT NULL,
  guoku_dscr varchar(200) NULL DEFAULT NULL,
  flag_code char(1) NULL DEFAULT NULL,
  flag varchar(20) NULL DEFAULT NULL,
  fun_class_code varchar(20) NULL DEFAULT NULL,
  fun_class_dscr varchar(100) NULL DEFAULT NULL,
  budget_payout decimal(18, 4) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_trs_payout_subject_struct (
  d_date date NULL DEFAULT NULL,
  d_year_mth char(7) NULL DEFAULT NULL,
  guoku_id varchar(50) NULL DEFAULT NULL,
  guoku_dscr varchar(200) NULL DEFAULT NULL,
  flag_code char(1) NULL DEFAULT NULL,
  flag varchar(20) NULL DEFAULT NULL,
  sub_lvl_code varchar(20) NULL DEFAULT NULL,
  sub_lvl_dscr varchar(100) NULL DEFAULT NULL,
  budget_payout decimal(18, 4) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_trs_payout_zk (
  d_date date NULL DEFAULT NULL,
  d_year_mth char(7) NULL DEFAULT NULL,
  guoku_id varchar(50) NULL DEFAULT NULL,
  guoku_dscr varchar(200) NULL DEFAULT NULL,
  flag_code char(1) NULL DEFAULT NULL,
  flag varchar(20) NULL DEFAULT NULL,
  payout_total decimal(18, 4) NULL DEFAULT NULL,
  payout_total_lm decimal(18, 4) NULL DEFAULT NULL,
  payout_total_ly decimal(18, 4) NULL DEFAULT NULL,
  budget_payout_total decimal(18, 4) NULL DEFAULT NULL,
  budget_payout_total_lm decimal(18, 4) NULL DEFAULT NULL,
  focus_payout decimal(18, 4) NULL DEFAULT NULL,
  focus_payout_lm decimal(18, 4) NULL DEFAULT NULL,
  focus_payout_ly decimal(18, 4) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.ana_trs_stock (
  d_date date NULL DEFAULT NULL,
  d_year_mth char(7) NULL DEFAULT NULL,
  d_riqi varchar(20) NULL DEFAULT NULL,
  guoku_id varchar(50) NULL DEFAULT NULL,
  guoku_dscr varchar(200) NULL DEFAULT NULL,
  flag_code char(1) NULL DEFAULT NULL,
  flag varchar(20) NULL DEFAULT NULL,
  s_acctname varchar(100) NULL DEFAULT NULL,
  f_yesterdaybalance decimal(18, 4) NULL DEFAULT NULL,
  f_todayreceipt decimal(18, 4) NULL DEFAULT NULL,
  f_todaypay decimal(18, 4) NULL DEFAULT NULL,
  f_todaybalance decimal(18, 4) NULL DEFAULT NULL,
  f_lastyearbalance decimal(18, 4) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.clr_ana_pay_mth_area (
  d_date datetime(0) NULL DEFAULT NULL,
  month varchar(20) NULL DEFAULT NULL,
  province varchar(20) NULL DEFAULT NULL,
  new_district_code varchar(20) NULL DEFAULT NULL,
  new_district varchar(50) NULL DEFAULT NULL,
  city_code varchar(20) NULL DEFAULT NULL,
  city_name varchar(50) NULL DEFAULT NULL,
  county_code varchar(20) NULL DEFAULT NULL,
  county varchar(50) NULL DEFAULT NULL,
  org_class varchar(50) NULL DEFAULT NULL,
  account_type varchar(50) NULL DEFAULT NULL,
  in_money double(20, 6) NULL DEFAULT NULL,
  out_money double(20, 6) NULL DEFAULT NULL,
  in_count bigint NULL DEFAULT NULL,
  out_count bigint NULL DEFAULT NULL,
  in_money_last double(20, 6) NULL DEFAULT NULL,
  out_money_last double(20, 6) NULL DEFAULT NULL,
  in_count_last bigint NULL DEFAULT NULL,
  out_count_last bigint NULL DEFAULT NULL,
  all_money double(20, 6) NULL DEFAULT NULL,
  all_count bigint NULL DEFAULT NULL,
  all_money_ly double(20, 6) NULL DEFAULT NULL,
  all_count_ly bigint NULL DEFAULT NULL,
  add_date datetime(0) NULL DEFAULT NULL,
  data_date varchar(20) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.clr_ana_pay_mth_area_map (
  d_date datetime(0) NULL DEFAULT NULL,
  month varchar(20) NULL DEFAULT NULL,
  year varchar(20) NULL DEFAULT NULL,
  count_code varchar(20) NULL DEFAULT NULL,
  county varchar(50) NULL DEFAULT NULL,
  all_money double(35, 6) NULL DEFAULT NULL,
  all_count bigint NULL DEFAULT NULL,
  add_date datetime(0) NULL DEFAULT NULL,
  data_date varchar(20) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.clr_ana_pay_mth_bank_type (
  d_date datetime(0) NULL DEFAULT NULL,
  month varchar(20) NULL DEFAULT NULL,
  org_class varchar(50) NULL DEFAULT NULL,
  org_name varchar(50) NULL DEFAULT NULL,
  business_type_1_code varchar(20) NULL DEFAULT NULL,
  business_type_1 varchar(50) NULL DEFAULT NULL,
  in_money double(20, 6) NULL DEFAULT NULL,
  out_money double(20, 6) NULL DEFAULT NULL,
  in_count bigint NULL DEFAULT NULL,
  out_count bigint NULL DEFAULT NULL,
  in_money_last double(20, 6) NULL DEFAULT NULL,
  out_money_last double(20, 6) NULL DEFAULT NULL,
  in_count_last bigint NULL DEFAULT NULL,
  out_count_last bigint NULL DEFAULT NULL,
  all_money double(20, 6) NULL DEFAULT NULL,
  all_count bigint NULL DEFAULT NULL,
  all_money_ly double(20, 6) NULL DEFAULT NULL,
  all_count_ly bigint NULL DEFAULT NULL,
  add_date datetime(0) NULL DEFAULT NULL,
  data_date varchar(20) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.clr_ana_pay_mth_bank_type_his (
  d_date datetime(0) NULL DEFAULT NULL,
  month varchar(20) NULL DEFAULT NULL,
  org_class varchar(50) NULL DEFAULT NULL,
  org_name varchar(50) NULL DEFAULT NULL,
  business_type_1_code varchar(20) NULL DEFAULT NULL,
  business_type_1 varchar(50) NULL DEFAULT NULL,
  in_money double(20, 6) NULL DEFAULT NULL,
  out_money double(20, 6) NULL DEFAULT NULL,
  in_count bigint NULL DEFAULT NULL,
  out_count bigint NULL DEFAULT NULL,
  in_money_last double(20, 6) NULL DEFAULT NULL,
  out_money_last double(20, 6) NULL DEFAULT NULL,
  in_count_last bigint NULL DEFAULT NULL,
  out_count_last bigint NULL DEFAULT NULL,
  all_money double(20, 6) NULL DEFAULT NULL,
  all_count bigint NULL DEFAULT NULL,
  all_money_ly double(20, 6) NULL DEFAULT NULL,
  all_count_ly bigint NULL DEFAULT NULL,
  add_date datetime(0) NULL DEFAULT NULL,
  data_date varchar(20) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.clr_ana_pay_mth_busi_type (
  d_date datetime(0) NULL DEFAULT NULL,
  month varchar(20) NULL DEFAULT NULL,
  province varchar(20) NULL DEFAULT NULL,
  new_district_code varchar(20) NULL DEFAULT NULL,
  new_district varchar(50) NULL DEFAULT NULL,
  city_code varchar(20) NULL DEFAULT NULL,
  city_name varchar(50) NULL DEFAULT NULL,
  county_code varchar(20) NULL DEFAULT NULL,
  county varchar(50) NULL DEFAULT NULL,
  business_type_1_code varchar(20) NULL DEFAULT NULL,
  business_type_1 varchar(50) NULL DEFAULT NULL,
  in_money double(20, 6) NULL DEFAULT NULL,
  out_money double(20, 6) NULL DEFAULT NULL,
  in_count bigint NULL DEFAULT NULL,
  out_count bigint NULL DEFAULT NULL,
  in_money_last double(20, 6) NULL DEFAULT NULL,
  out_money_last double(20, 6) NULL DEFAULT NULL,
  in_count_last bigint NULL DEFAULT NULL,
  out_count_last bigint NULL DEFAULT NULL,
  all_money double(20, 6) NULL DEFAULT NULL,
  all_count bigint NULL DEFAULT NULL,
  all_money_ly double(20, 6) NULL DEFAULT NULL,
  all_count_ly bigint NULL DEFAULT NULL,
  add_date datetime(0) NULL DEFAULT NULL,
  data_date varchar(20) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.clr_ana_pay_mth_industry (
  d_date datetime(0) NULL DEFAULT NULL,
  month varchar(20) NULL DEFAULT NULL,
  industy_first_code varchar(20) NULL DEFAULT NULL,
  industy_first varchar(50) NULL DEFAULT NULL,
  industy_first_code_order varchar(20) NULL DEFAULT NULL,
  industy_second_code varchar(20) NULL DEFAULT NULL,
  industy_second varchar(50) NULL DEFAULT NULL,
  in_money double(20, 6) NULL DEFAULT NULL,
  out_money double(20, 6) NULL DEFAULT NULL,
  in_count bigint NULL DEFAULT NULL,
  out_count bigint NULL DEFAULT NULL,
  in_money_last double(20, 6) NULL DEFAULT NULL,
  out_money_last double(20, 6) NULL DEFAULT NULL,
  in_count_last bigint NULL DEFAULT NULL,
  out_count_last bigint NULL DEFAULT NULL,
  all_money double(20, 6) NULL DEFAULT NULL,
  all_count bigint NULL DEFAULT NULL,
  all_money_ly double(20, 6) NULL DEFAULT NULL,
  all_count_ly bigint NULL DEFAULT NULL,
  add_date datetime(0) NULL DEFAULT NULL,
  data_date varchar(20) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.clr_ana_pay_mth_industry_bankarea (
  d_date datetime(0) NULL DEFAULT NULL,
  month varchar(20) NULL DEFAULT NULL,
  city_code varchar(20) NULL DEFAULT NULL,
  city_name varchar(50) NULL DEFAULT NULL,
  org_class varchar(50) NULL DEFAULT NULL,
  industy_first varchar(50) NULL DEFAULT NULL,
  all_money double(20, 6) NULL DEFAULT NULL,
  add_date datetime(0) NULL DEFAULT NULL,
  data_date varchar(20) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.clr_ana_pay_mth_region (
  d_date datetime(0) NULL DEFAULT NULL,
  month varchar(20) NULL DEFAULT NULL,
  system varchar(20) NULL DEFAULT NULL,
  province varchar(20) NULL DEFAULT NULL,
  new_district varchar(50) NULL DEFAULT NULL,
  new_district_code varchar(20) NULL DEFAULT NULL,
  city_name varchar(50) NULL DEFAULT NULL,
  city_code varchar(20) NULL DEFAULT NULL,
  county varchar(50) NULL DEFAULT NULL,
  county_code varchar(20) NULL DEFAULT NULL,
  industy_first varchar(50) NULL DEFAULT NULL,
  industy_second varchar(50) NULL DEFAULT NULL,
  org_class varchar(50) NULL DEFAULT NULL,
  org_name varchar(50) NULL DEFAULT NULL,
  business_type_1 varchar(50) NULL DEFAULT NULL,
  business_type_1_code varchar(20) NULL DEFAULT NULL,
  business_type_2 varchar(50) NULL DEFAULT NULL,
  business_type_2_code varchar(20) NULL DEFAULT NULL,
  account_type varchar(50) NULL DEFAULT NULL,
  in_money double(20, 6) NULL DEFAULT NULL,
  out_money double(20, 6) NULL DEFAULT NULL,
  in_count bigint NULL DEFAULT NULL,
  out_count bigint NULL DEFAULT NULL,
  in_money_last double(20, 6) NULL DEFAULT NULL,
  out_money_last double(20, 6) NULL DEFAULT NULL,
  in_count_last bigint NULL DEFAULT NULL,
  out_count_last bigint NULL DEFAULT NULL,
  all_money double(20, 6) NULL DEFAULT NULL,
  all_count bigint NULL DEFAULT NULL,
  all_money_ly double(20, 6) NULL DEFAULT NULL,
  all_count_ly bigint NULL DEFAULT NULL,
  add_date datetime(0) NULL DEFAULT NULL,
  data_date varchar(8) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.clr_ana_pay_mth_region_industry (
  d_date datetime(0) NULL DEFAULT NULL,
  month varchar(20) NULL DEFAULT NULL,
  province varchar(20) NULL DEFAULT NULL,
  new_district_code varchar(50) NULL DEFAULT NULL,
  new_district varchar(20) NULL DEFAULT NULL,
  city_code varchar(50) NULL DEFAULT NULL,
  city_name varchar(20) NULL DEFAULT NULL,
  county_code varchar(50) NULL DEFAULT NULL,
  county varchar(20) NULL DEFAULT NULL,
  industy_first varchar(50) NULL DEFAULT NULL,
  in_money double(20, 6) NULL DEFAULT NULL,
  out_money double(20, 6) NULL DEFAULT NULL,
  in_count bigint NULL DEFAULT NULL,
  out_count bigint NULL DEFAULT NULL,
  in_money_last double(20, 6) NULL DEFAULT NULL,
  out_money_last double(20, 6) NULL DEFAULT NULL,
  in_count_last bigint NULL DEFAULT NULL,
  out_count_last bigint NULL DEFAULT NULL,
  all_money double(20, 6) NULL DEFAULT NULL,
  all_count bigint NULL DEFAULT NULL,
  all_money_ly double(20, 6) NULL DEFAULT NULL,
  all_count_ly bigint NULL DEFAULT NULL,
  add_date datetime(0) NULL DEFAULT NULL,
  data_date varchar(20) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.clr_ana_pay_mth_region_map (
  d_date datetime(0) NULL DEFAULT NULL,
  year varchar(20) NULL DEFAULT NULL,
  month varchar(20) NULL DEFAULT NULL,
  county varchar(50) NULL DEFAULT NULL,
  count_code varchar(20) NULL DEFAULT NULL,
  all_money double(20, 6) NULL DEFAULT NULL,
  all_count bigint NULL DEFAULT NULL,
  add_date datetime(0) NULL DEFAULT NULL,
  data_date varchar(8) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.clr_ana_pay_overview (
  d_date datetime(0) NULL DEFAULT NULL,
  month varchar(20) NULL DEFAULT NULL,
  system varchar(20) NULL DEFAULT NULL,
  direction varchar(20) NULL DEFAULT NULL,
  all_money varchar(50) NULL DEFAULT NULL,
  all_count varchar(20) NULL DEFAULT NULL,
  all_money_last varchar(50) NULL DEFAULT NULL,
  all_count_last varchar(20) NULL DEFAULT NULL,
  all_money_past varchar(50) NULL DEFAULT NULL,
  all_count_past varchar(20) NULL DEFAULT NULL,
  add_date varchar(50) NULL DEFAULT NULL,
  data_date varchar(20) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.clr_ana_pay_regin_in_area (
  d_date datetime(0) NULL DEFAULT NULL,
  month varchar(20) NULL DEFAULT NULL,
  province varchar(50) NULL DEFAULT NULL,
  in_money double(20, 6) NULL DEFAULT NULL,
  out_money double(20, 6) NULL DEFAULT NULL,
  in_count bigint NULL DEFAULT NULL,
  out_count bigint NULL DEFAULT NULL,
  in_money_last double(20, 6) NULL DEFAULT NULL,
  out_money_last double(20, 6) NULL DEFAULT NULL,
  in_count_last bigint NULL DEFAULT NULL,
  out_count_last bigint NULL DEFAULT NULL,
  add_date datetime(0) NULL DEFAULT NULL,
  data_date varchar(8) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.clr_ana_pay_regin_top10_ind (
  d_date datetime(0) NULL DEFAULT NULL,
  month varchar(20) NULL DEFAULT NULL,
  direction varchar(20) NULL DEFAULT NULL,
  lev_1_num varchar(20) NULL DEFAULT NULL,
  industry_first varchar(20) NULL DEFAULT NULL,
  company_name varchar(20) NULL DEFAULT NULL,
  money double(20, 6) NULL DEFAULT NULL,
  count bigint NULL DEFAULT NULL,
  money_last double(20, 6) NULL DEFAULT NULL,
  count_last bigint NULL DEFAULT NULL,
  add_date datetime(0) NULL DEFAULT NULL,
  data_date varchar(8) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.clr_ana_regin_com_tend (
  d_date datetime(0) NULL DEFAULT NULL,
  month varchar(20) NULL DEFAULT NULL,
  com_name varchar(20) NULL DEFAULT NULL,
  in_money double(20, 6) NULL DEFAULT NULL,
  out_money double(20, 6) NULL DEFAULT NULL,
  all_money double(20, 6) NULL DEFAULT NULL,
  in_count bigint NULL DEFAULT NULL,
  out_count bigint NULL DEFAULT NULL,
  all_count bigint NULL DEFAULT NULL,
  in_money_last double(20, 6) NULL DEFAULT NULL,
  out_money_last double(20, 6) NULL DEFAULT NULL,
  all_money_last double(20, 6) NULL DEFAULT NULL,
  in_count_last bigint NULL DEFAULT NULL,
  out_count_last bigint NULL DEFAULT NULL,
  all_count_last bigint NULL DEFAULT NULL,
  add_date datetime(0) NULL DEFAULT NULL,
  data_date varchar(8) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.clr_ana_region_com_in_out (
  d_date datetime(0) NULL DEFAULT NULL,
  month varchar(20) NULL DEFAULT NULL,
  com_name varchar(100) NULL DEFAULT NULL,
  direction varchar(20) NULL DEFAULT NULL,
  in_out_top10 varchar(100) NULL DEFAULT NULL,
  money double(20, 6) NULL DEFAULT NULL,
  count bigint NULL DEFAULT NULL,
  add_date datetime(0) NULL DEFAULT NULL,
  data_date varchar(8) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.clr_form_region_acct_typ_stat (
  date_type varchar(5) NULL DEFAULT NULL,
  data_date varchar(6) NULL DEFAULT NULL,
  system_sort varchar(1) NULL DEFAULT NULL,
  system varchar(20) NULL DEFAULT NULL,
  cush_inflow_code varchar(1) NULL DEFAULT NULL,
  cush_inflow_typ varchar(10) NULL DEFAULT NULL,
  region_code varchar(10) NULL DEFAULT NULL,
  region_name varchar(50) NULL DEFAULT NULL,
  acct_type varchar(3) NULL DEFAULT NULL,
  acct_name varchar(10) NULL DEFAULT NULL,
  count bigint NULL DEFAULT NULL,
  count_duty bigint NULL DEFAULT NULL,
  tb_count_duty bigint NULL DEFAULT NULL,
  hb_count_duty bigint NULL DEFAULT NULL,
  amt double(20, 6) NULL DEFAULT NULL,
  amt_duty double(20, 6) NULL DEFAULT NULL,
  tb_amt_duty double(20, 6) NULL DEFAULT NULL,
  hb_amt_duty double(20, 6) NULL DEFAULT NULL,
  add_date datetime(0) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.clr_form_region_bank_dl_stat (
  date_type varchar(5) NULL DEFAULT NULL,
  data_date varchar(6) NULL DEFAULT NULL,
  system_sort varchar(1) NULL DEFAULT NULL,
  system varchar(20) NULL DEFAULT NULL,
  cush_inflow_code varchar(1) NULL DEFAULT NULL,
  cush_inflow_typ varchar(10) NULL DEFAULT NULL,
  region_code varchar(10) NULL DEFAULT NULL,
  region_name varchar(50) NULL DEFAULT NULL,
  bank_typ_code varchar(1) NULL DEFAULT NULL,
  bank_typ_name varchar(30) NULL DEFAULT NULL,
  count bigint NULL DEFAULT NULL,
  count_duty bigint NULL DEFAULT NULL,
  tb_count_duty bigint NULL DEFAULT NULL,
  hb_count_duty bigint NULL DEFAULT NULL,
  amt double(20, 6) NULL DEFAULT NULL,
  amt_duty double(20, 6) NULL DEFAULT NULL,
  tb_amt_duty double(20, 6) NULL DEFAULT NULL,
  hb_amt_duty double(20, 6) NULL DEFAULT NULL,
  add_date datetime(0) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.clr_form_region_bank_dlm_stat (
  date_type varchar(5) NULL DEFAULT NULL,
  data_date varchar(6) NULL DEFAULT NULL,
  system_sort varchar(1) NULL DEFAULT NULL,
  system varchar(20) NULL DEFAULT NULL,
  cush_inflow_code varchar(1) NULL DEFAULT NULL,
  cush_inflow_typ varchar(10) NULL DEFAULT NULL,
  region_code varchar(10) NULL DEFAULT NULL,
  region_name varchar(50) NULL DEFAULT NULL,
  bank_typ_code varchar(1) NULL DEFAULT NULL,
  bank_typ_name varchar(30) NULL DEFAULT NULL,
  bank_code varchar(3) NULL DEFAULT NULL,
  bank_name varchar(300) NULL DEFAULT NULL,
  count bigint NULL DEFAULT NULL,
  count_duty bigint NULL DEFAULT NULL,
  tb_count_duty bigint NULL DEFAULT NULL,
  hb_count_duty bigint NULL DEFAULT NULL,
  amt double(20, 6) NULL DEFAULT NULL,
  amt_duty double(20, 6) NULL DEFAULT NULL,
  tb_amt_duty double(20, 6) NULL DEFAULT NULL,
  hb_amt_duty double(20, 6) NULL DEFAULT NULL,
  add_date datetime(0) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.clr_form_region_biz_type_stat (
  date_type varchar(5) NULL DEFAULT NULL,
  data_date varchar(6) NULL DEFAULT NULL,
  system_sort varchar(1) NULL DEFAULT NULL,
  system varchar(20) NULL DEFAULT NULL,
  cush_inflow_code varchar(1) NULL DEFAULT NULL,
  cush_inflow_typ varchar(10) NULL DEFAULT NULL,
  region_code varchar(10) NULL DEFAULT NULL,
  region_name varchar(50) NULL DEFAULT NULL,
  biz_type varchar(4) NULL DEFAULT NULL,
  biz_name varchar(100) NULL DEFAULT NULL,
  count bigint NULL DEFAULT NULL,
  amt double(20, 6) NULL DEFAULT NULL,
  add_date datetime(0) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.clr_form_region_ccpc_stat (
  date_type varchar(5) NULL DEFAULT NULL,
  data_date varchar(6) NULL DEFAULT NULL,
  system_sort varchar(1) NULL DEFAULT NULL,
  system varchar(20) NULL DEFAULT NULL,
  cush_inflow_code varchar(1) NULL DEFAULT NULL,
  cush_inflow_typ varchar(10) NULL DEFAULT NULL,
  region_code varchar(10) NULL DEFAULT NULL,
  region_name varchar(20) NULL DEFAULT NULL,
  ccpc varchar(10) NULL DEFAULT NULL,
  ccpc_name varchar(20) NULL DEFAULT NULL,
  count bigint NULL DEFAULT NULL,
  amt double(20, 6) NULL DEFAULT NULL,
  add_date datetime(0) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.clr_form_region_count_stat (
  date_type varchar(5) NULL DEFAULT NULL,
  data_date varchar(6) NULL DEFAULT NULL,
  system_sort varchar(1) NULL DEFAULT NULL,
  system varchar(20) NULL DEFAULT NULL,
  cush_inflow_code varchar(1) NULL DEFAULT NULL,
  cush_inflow_typ varchar(10) NULL DEFAULT NULL,
  region_code varchar(10) NULL DEFAULT NULL,
  region_name varchar(50) NULL DEFAULT NULL,
  count bigint NULL DEFAULT NULL,
  count_duty bigint NULL DEFAULT NULL,
  tb_count_duty bigint NULL DEFAULT NULL,
  hb_count_duty bigint NULL DEFAULT NULL,
  amt double(20, 6) NULL DEFAULT NULL,
  amt_duty double(20, 6) NULL DEFAULT NULL,
  tb_amt_duty double(20, 6) NULL DEFAULT NULL,
  hb_amt_duty double(20, 6) NULL DEFAULT NULL,
  add_date datetime(0) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.clr_form_region_indus_dmd_stat (
  date_type varchar(5) NULL DEFAULT NULL,
  data_date varchar(6) NULL DEFAULT NULL,
  system_sort varchar(1) NULL DEFAULT NULL,
  system varchar(20) NULL DEFAULT NULL,
  cush_inflow_code varchar(1) NULL DEFAULT NULL,
  cush_inflow_typ varchar(10) NULL DEFAULT NULL,
  region_code varchar(10) NULL DEFAULT NULL,
  region_name varchar(50) NULL DEFAULT NULL,
  industry_code_1 varchar(10) NULL DEFAULT NULL,
  industry_name_1 varchar(100) NULL DEFAULT NULL,
  industry_code_2 varchar(10) NULL DEFAULT NULL,
  industry_name_2 varchar(100) NULL DEFAULT NULL,
  count bigint NULL DEFAULT NULL,
  count_duty bigint NULL DEFAULT NULL,
  tb_count_duty bigint NULL DEFAULT NULL,
  hb_count_duty bigint NULL DEFAULT NULL,
  amt double(20, 6) NULL DEFAULT NULL,
  amt_duty double(20, 6) NULL DEFAULT NULL,
  tb_amt_duty double(20, 6) NULL DEFAULT NULL,
  hb_amt_duty double(20, 6) NULL DEFAULT NULL,
  add_date datetime(0) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.clr_form_region_inout_stat (
  date_type varchar(5) NULL DEFAULT NULL,
  data_date varchar(6) NULL DEFAULT NULL,
  system_sort varchar(1) NULL DEFAULT NULL,
  system varchar(20) NULL DEFAULT NULL,
  cush_inflow_code varchar(1) NULL DEFAULT NULL,
  cush_inflow_typ varchar(10) NULL DEFAULT NULL,
  in_region_code varchar(10) NULL DEFAULT NULL,
  in_region_name varchar(20) NULL DEFAULT NULL,
  out_region_code varchar(10) NULL DEFAULT NULL,
  out_region_name varchar(20) NULL DEFAULT NULL,
  count bigint NULL DEFAULT NULL,
  amt double(20, 6) NULL DEFAULT NULL,
  add_date datetime(0) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.clr_key_industry_no_inout (
  data_date varchar(4) NULL DEFAULT NULL,
  enterprise_name varchar(100) NULL DEFAULT NULL,
  enterprise_type varchar(10) NULL DEFAULT NULL,
  industry_code_1 varchar(10) NULL DEFAULT NULL,
  industry_name_1 varchar(100) NULL DEFAULT NULL,
  industry_code_2 varchar(10) NULL DEFAULT NULL,
  industry_name_2 varchar(100) NULL DEFAULT NULL,
  region_code varchar(8) NULL DEFAULT NULL,
  region_name varchar(50) NULL DEFAULT NULL,
  remark varchar(4000) NULL DEFAULT NULL,
  add_date datetime(0) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.clr_key_industry_top10 (
  date_type varchar(5) NULL DEFAULT NULL,
  inflow_type varchar(5) NULL DEFAULT NULL,
  data_date varchar(6) NULL DEFAULT NULL,
  key_enterprise varchar(100) NULL DEFAULT NULL,
  enterprise_name varchar(100) NULL DEFAULT NULL,
  region_code varchar(8) NULL DEFAULT NULL,
  region_name varchar(50) NULL DEFAULT NULL,
  amt double(20, 6) NULL DEFAULT NULL,
  add_date datetime(0) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.cm_clr_reflact_countamt (
  data_date varchar(8) NULL DEFAULT NULL,
  enterprise_name varchar(100) NULL DEFAULT NULL,
  enterprise_code varchar(20) NULL DEFAULT NULL,
  amt decimal(20, 6) NULL DEFAULT NULL,
  count bigint NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.dis_file_process_node (
  f_pnode_id serial NOT NULL,
  file_id int NOT NULL,
  process_id int NULL DEFAULT NULL,
  node_no varchar(20) NULL DEFAULT NULL,
  f_pnode_sn int NOT NULL DEFAULT 1,
  f_pnode_retry_times int NOT NULL DEFAULT 0,
  f_pnode_start timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  f_pnode_stop timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  f_pnode_times int NOT NULL,
  f_pnode_state varchar(20) NOT NULL,
  f_pnode_created timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  f_pnode_updated timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  f_pnode_reset_flag int NULL DEFAULT 0,
  PRIMARY KEY (f_pnode_id)
);

CREATE TABLE IF NOT EXISTS adm.exec_shell_task (
  id varchar(40) NOT NULL,
  task_name varchar(255) NULL DEFAULT NULL,
  shell_path varchar(255) NULL DEFAULT NULL,
  shell_name varchar(255) NULL DEFAULT NULL,
  shell_param varchar(255) NULL DEFAULT NULL,
  cron_id varchar(255) NULL DEFAULT NULL,
  status varchar(3) NULL DEFAULT NULL,
  create_time char(19) NULL DEFAULT NULL,
  create_user varchar(128) NULL DEFAULT NULL,
  update_time char(19) NULL DEFAULT NULL,
  update_user varchar(128) NULL DEFAULT NULL,
  task_type varchar(2) NULL DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS adm.exec_shell_task_run_log (
  id varchar(40) NOT NULL,
  task_id varchar(40) NOT NULL,
  task_name varchar(255),
  task_type varchar(2),
  shell_path varchar(255),
  shell_name varchar(255),
  shell_param varchar(1000),
  status varchar(3) NOT NULL,
  start_time timestamp(0) NOT NULL DEFAULT current_timestamp,
  end_time timestamp(0),
  result_message text,
  create_user varchar(128),
  PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_exec_shell_task_run_log_task_time
  ON adm.exec_shell_task_run_log (task_id, start_time DESC);

CREATE INDEX IF NOT EXISTS idx_exec_shell_task_run_log_status
  ON adm.exec_shell_task_run_log (status);

CREATE TABLE IF NOT EXISTS adm.kpi_value (
  data_month varchar(6) NULL DEFAULT NULL,
  fin_org_dist varchar(30) NULL DEFAULT NULL,
  fin_org_code varchar(14) NULL DEFAULT NULL,
  kpi_code varchar(30) NULL DEFAULT NULL,
  kpi_name varchar(30) NULL DEFAULT NULL,
  loan_indust varchar(30) NULL DEFAULT NULL,
  loan_indust_desc varchar(30) NULL DEFAULT NULL,
  enter_scale varchar(30) NULL DEFAULT NULL,
  enter_scale_desc varchar(30) NULL DEFAULT NULL,
  loan_proper varchar(30) NULL DEFAULT NULL,
  loan_proper_desc varchar(30) NULL DEFAULT NULL,
  loan_guarant varchar(30) NULL DEFAULT NULL,
  loan_guarant_desc varchar(30) NULL DEFAULT NULL,
  loan_kind varchar(30) NULL DEFAULT NULL,
  loan_kind_desc varchar(30) NULL DEFAULT NULL,
  kpi_value1 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value2 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value3 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value4 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value5 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value6 decimal(30, 5) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.mth_foreign_exchange_exception_clue (
  area_no varchar(8) NULL DEFAULT NULL,
  data_date char(8) NULL DEFAULT NULL,
  table_nature varchar(50) NULL DEFAULT NULL,
  org_no varchar(14) NULL DEFAULT NULL,
  rows_id varchar(100) NULL DEFAULT NULL,
  batch_date char(8) NULL DEFAULT NULL,
  sheet_id varchar(100) NULL DEFAULT NULL,
  org_name varchar(200) NULL DEFAULT NULL,
  org_code varchar(200) NULL DEFAULT NULL,
  subject_type varchar(3) NULL DEFAULT NULL,
  subject_name varchar(200) NULL DEFAULT NULL,
  subject_code varchar(200) NULL DEFAULT NULL,
  is_specialarea varchar(3) NULL DEFAULT NULL,
  inspect_item varchar(3) NULL DEFAULT NULL,
  clue_type varchar(3) NULL DEFAULT NULL,
  cross_suboffice varchar(3) NULL DEFAULT NULL,
  inspect_result int NULL DEFAULT NULL,
  deal_count int NULL DEFAULT NULL,
  exception_money decimal(26, 6) NULL DEFAULT NULL,
  clue_type_other varchar(200) NULL DEFAULT NULL,
  summary_biz_exception_clue_type varchar(20) NULL DEFAULT NULL,
  add_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  add_by varchar(10) NULL DEFAULT NULL,
  mod_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  mod_by varchar(10) NULL DEFAULT NULL,
  obj_num int NOT NULL
);

CREATE TABLE IF NOT EXISTS adm.mth_punish_dtl (
  area_no varchar(8) NULL DEFAULT NULL,
  data_date char(8) NULL DEFAULT NULL,
  table_nature varchar(50) NULL DEFAULT NULL,
  org_no varchar(14) NULL DEFAULT NULL,
  rows_id varchar(100) NULL DEFAULT NULL,
  batch_date char(8) NULL DEFAULT NULL,
  sheet_id varchar(100) NULL DEFAULT NULL,
  serial_no varchar(10) NULL DEFAULT NULL,
  org_name varchar(200) NULL DEFAULT NULL,
  org_code varchar(200) NULL DEFAULT NULL,
  illegal_subject_name varchar(200) NULL DEFAULT NULL,
  illegal_subject_code varchar(200) NULL DEFAULT NULL,
  fact_desc varchar(200) NULL DEFAULT NULL,
  illegal_money decimal(26, 6) NULL DEFAULT NULL,
  punish_money decimal(26, 6) NULL DEFAULT NULL,
  illegal_type varchar(3) NULL DEFAULT NULL,
  summarize_punish_type varchar(10) NULL DEFAULT NULL,
  add_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  add_by varchar(10) NULL DEFAULT NULL,
  mod_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  mod_by varchar(10) NULL DEFAULT NULL,
  obj_num int NOT NULL DEFAULT 1
);

CREATE TABLE IF NOT EXISTS adm.mth_punish_dtl_item (
  num int NOT NULL,
  obj_name varchar(100) NOT NULL,
  obj_code varchar(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS adm.mth_violation_deal_dtl (
  area_no varchar(24) NOT NULL,
  data_date char(24) NOT NULL,
  table_nature varchar(150) NULL DEFAULT NULL,
  org_no varchar(42) NOT NULL,
  rows_id varchar(300) NULL DEFAULT NULL,
  batch_date char(24) NULL DEFAULT NULL,
  sheet_id varchar(300) NULL DEFAULT NULL,
  serial_no varchar(600) NULL DEFAULT NULL,
  org_name varchar(600) NULL DEFAULT NULL,
  org_code varchar(600) NULL DEFAULT NULL,
  except_subject_name varchar(600) NULL DEFAULT NULL,
  except_subject_code varchar(600) NULL DEFAULT NULL,
  except_fact_desc varchar(600) NULL DEFAULT NULL,
  except_count int NOT NULL,
  except_money decimal(26, 6) NOT NULL,
  add_date timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP,
  add_by varchar(30) NULL DEFAULT NULL,
  mod_date timestamp(0) NULL DEFAULT NULL,
  mod_by varchar(30) NULL DEFAULT NULL,
  biztype_code varchar(10) NOT NULL,
  deal_yuetan_geren varchar(10) NULL DEFAULT NULL,
  deal_lianhejianguan_chengjie varchar(10) NULL DEFAULT NULL,
  deal_yinhangzilvhangyetongbao varchar(10) NULL DEFAULT NULL,
  deal_zantingyinhangjieshouhuizige varchar(10) NULL DEFAULT NULL,
  deal_yinhangkaohekoufen varchar(10) NULL DEFAULT NULL,
  deal_lianhejianguan_tongbao varchar(10) NULL DEFAULT NULL,
  deal_yijiao varchar(10) NULL DEFAULT NULL,
  deal_guanzhumingdan varchar(10) NULL DEFAULT NULL,
  deal_zhuxiao varchar(10) NULL DEFAULT NULL,
  deal_jiangji varchar(10) NULL DEFAULT NULL,
  deal_yuetan_bank varchar(10) NULL DEFAULT NULL,
  deal_yuetan_qiye varchar(10) NULL DEFAULT NULL,
  deal_pause_biz varchar(10) NULL DEFAULT NULL,
  deal_pause_org_crossfe varchar(10) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.mth_violation_deal_item (
  obj_num tinyint UNSIGNED NOT NULL,
  obj_name varchar(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS adm.px_cx_mth_cash_servinfo (
  trade_date varchar(10) NULL DEFAULT NULL,
  shop_num varchar(50) NULL DEFAULT NULL,
  village_code varchar(20) NULL DEFAULT NULL,
  trade_num decimal(42, 0) NULL DEFAULT NULL,
  trade_type varchar(50) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.py_cx_advt_train (
  data_date char(8) NULL DEFAULT NULL,
  area_id varchar(20) NULL DEFAULT NULL,
  area_name varchar(100) NULL DEFAULT NULL,
  org_id varchar(200) NULL DEFAULT NULL,
  org_name varchar(200) NULL DEFAULT NULL,
  area_type_code varchar(20) NULL DEFAULT NULL,
  compare_type_code varchar(20) NULL DEFAULT NULL,
  publ_time int NULL DEFAULT NULL,
  compare_publ_time int NULL DEFAULT NULL,
  publ_timemark int NULL DEFAULT NULL,
  publ_num int NULL DEFAULT NULL,
  compare_publ_num int NULL DEFAULT NULL,
  publ_nummark int NULL DEFAULT NULL,
  off_time int NULL DEFAULT NULL,
  compare_off_time int NULL DEFAULT NULL,
  off_timemark int NULL DEFAULT NULL,
  train_time int NULL DEFAULT NULL,
  compare_train_time int NULL DEFAULT NULL,
  train_timemark int NULL DEFAULT NULL,
  train_num int NULL DEFAULT NULL,
  compare_train_num int NULL DEFAULT NULL,
  train_nummark int NULL DEFAULT NULL,
  remarks varchar(2000) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.py_cx_area_code11 (
  city_code char(6) NULL DEFAULT NULL,
  city_name varchar(30) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.py_cx_area_tradeinfo (
  data_date varchar(6) NULL DEFAULT NULL,
  area_code char(8) NULL DEFAULT NULL,
  area_name varchar(512) NULL DEFAULT NULL,
  city_code char(8) NULL DEFAULT NULL,
  city_name varchar(512) NULL DEFAULT NULL,
  trade_amout decimal(64, 6) NULL DEFAULT NULL,
  trade_count decimal(42, 0) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.py_cx_basic_info (
  data_date char(8) NULL DEFAULT NULL,
  qtr_date varchar(20) NULL DEFAULT NULL,
  city_area_name varchar(100) NULL DEFAULT NULL,
  city_pop_count varchar(20) NULL DEFAULT NULL,
  city_gdp decimal(20, 6) NULL DEFAULT NULL,
  city_gdp_rec decimal(20, 6) NULL DEFAULT NULL,
  town_area_name varchar(100) NULL DEFAULT NULL,
  town_pop_count varchar(20) NULL DEFAULT NULL,
  town_gdp decimal(20, 6) NULL DEFAULT NULL,
  town_gdp_rec decimal(20, 6) NULL DEFAULT NULL,
  vil_area_name varchar(100) NULL DEFAULT NULL,
  vil_pop_count varchar(20) NULL DEFAULT NULL,
  vil_gdp decimal(20, 6) NULL DEFAULT NULL,
  vil_gdp_rec decimal(20, 6) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.py_cx_bd_shop (
  data_date char(8) NULL DEFAULT NULL,
  order_num int NULL DEFAULT NULL,
  org_name varchar(100) NULL DEFAULT NULL,
  new_bd_user varchar(20) NULL DEFAULT NULL,
  tot_bd_user varchar(20) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.py_cx_cash_servinfo (
  data_date char(8) NULL DEFAULT NULL,
  org_num varchar(14) NULL DEFAULT NULL,
  city_name varchar(100) NULL DEFAULT NULL,
  county_name varchar(100) NULL DEFAULT NULL,
  town_name varchar(100) NULL DEFAULT NULL,
  village_name varchar(100) NULL DEFAULT NULL,
  village_code varchar(14) NULL DEFAULT NULL,
  service_point_name varchar(100) NULL DEFAULT NULL,
  service_point_adds varchar(100) NULL DEFAULT NULL,
  service_point_lon varchar(100) NULL DEFAULT NULL,
  service_point_la varchar(100) NULL DEFAULT NULL,
  termina_num varchar(50) NULL DEFAULT NULL,
  shop_num varchar(50) NULL DEFAULT NULL,
  install_date varchar(50) NULL DEFAULT NULL,
  service_point_res varchar(50) NULL DEFAULT NULL,
  col_org_name varchar(100) NULL DEFAULT NULL,
  shop_tel varchar(100) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.py_cx_cash_trade_thb (
  data_date varchar(6) NULL DEFAULT NULL,
  org_name varchar(200) NULL DEFAULT NULL,
  city_code varchar(8) NULL DEFAULT NULL,
  city_name varchar(500) NULL DEFAULT NULL,
  code_num decimal(10, 0) NULL DEFAULT NULL,
  trade_type varchar(50) NULL DEFAULT NULL,
  trade_amout decimal(64, 6) NULL DEFAULT NULL,
  trade_amout_sq decimal(64, 6) NULL DEFAULT NULL,
  trade_amout_tq decimal(64, 6) NULL DEFAULT NULL,
  trade_count decimal(42, 0) NULL DEFAULT NULL,
  trade_count_sq decimal(42, 0) NULL DEFAULT NULL,
  trade_count_tq decimal(42, 0) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.py_cx_cash_tradeinfo (
  data_date varchar(6) NULL DEFAULT NULL,
  put_org_name varchar(200) NULL DEFAULT NULL,
  village_code varchar(20) NULL DEFAULT NULL,
  village_name varchar(100) NULL DEFAULT NULL,
  area_code char(8) NULL DEFAULT NULL,
  area_name varchar(512) NULL DEFAULT NULL,
  city_code char(8) NULL DEFAULT NULL,
  city_name varchar(512) NULL DEFAULT NULL,
  prov varchar(20) NOT NULL DEFAULT '',
  shop_num varchar(30) NULL DEFAULT NULL,
  org_name varchar(50) NULL DEFAULT NULL,
  trade_type varchar(50) NULL DEFAULT NULL,
  trade_amout decimal(42, 6) NULL DEFAULT NULL,
  trade_count bigint NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS adm.py_cx_cash_tradelog (
  col_org_num varchar(100) NULL DEFAULT NULL,
  shop_name varchar(100) NULL DEFAULT NULL,
  shop_num varchar(50) NULL DEFAULT NULL,
  termina_num varchar(50) NULL DEFAULT NULL,
  opp_account varchar(30) NULL DEFAULT NULL,
  trade_account varchar(20) NULL DEFAULT NULL,
  trade_date varchar(20) NULL DEFAULT NULL,
  trade_time varchar(20) NULL DEFAULT NULL,
  trade_amount decimal(20, 6) NULL DEFAULT NULL,
  trade_tpye varchar(50) NULL DEFAULT NULL,
  trade_num varchar(50) NULL DEFAULT NULL,
  trade_mode varchar(100) NULL DEFAULT NULL,
  trade_region varchar(100) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.py_cx_dw_cash_servinfo (
  data_date varchar(8) NULL DEFAULT NULL,
  area_no varchar(20) NULL DEFAULT NULL,
  org_no varchar(20) NULL DEFAULT NULL,
  bank_type varchar(20) NULL DEFAULT NULL,
  state varchar(10) NULL DEFAULT NULL,
  wd_num decimal(20, 0) NULL DEFAULT NULL,
  tq_wd_num decimal(20, 0) NULL DEFAULT NULL,
  sq_wd_num decimal(20, 0) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.py_cx_dw_cash_servinfo_tmp (
  data_date varchar(6) NULL DEFAULT NULL,
  area_no varchar(14) NULL DEFAULT NULL,
  org_no varchar(20) NULL DEFAULT NULL,
  bank_type varchar(6) NULL DEFAULT NULL,
  state varchar(10) NULL DEFAULT NULL,
  wd_num bigint NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS adm.py_cx_dw_cash_tradelog (
  data_date varchar(8) NULL DEFAULT NULL,
  area_no varchar(20) NULL DEFAULT NULL,
  org_no varchar(20) NULL DEFAULT NULL,
  bank_type varchar(20) NULL DEFAULT NULL,
  tyade_type varchar(10) NULL DEFAULT NULL,
  amout decimal(20, 4) NULL DEFAULT NULL,
  tq_amout decimal(20, 4) NULL DEFAULT NULL,
  sq_amout decimal(20, 4) NULL DEFAULT NULL,
  trade_num decimal(20, 0) NULL DEFAULT NULL,
  tq_trade_num decimal(20, 0) NULL DEFAULT NULL,
  sq_trade_num decimal(20, 0) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.py_cx_dw_cash_tradelog_tmp (
  data_date varchar(6) NULL DEFAULT NULL,
  area_no varchar(14) NULL DEFAULT NULL,
  org_no varchar(14) NULL DEFAULT NULL,
  bank_type varchar(6) NULL DEFAULT NULL,
  trade_num bigint NOT NULL DEFAULT 0,
  amout decimal(42, 6) NULL DEFAULT NULL,
  trade_type varchar(50) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.py_cx_dw_envbld_hf_wd (
  data_date varchar(8) NULL DEFAULT NULL,
  area_no varchar(20) NULL DEFAULT NULL,
  org_no varchar(20) NULL DEFAULT NULL,
  bank_type varchar(20) NULL DEFAULT NULL,
  state varchar(10) NULL DEFAULT NULL,
  wd_num decimal(20, 0) NULL DEFAULT NULL,
  tq_wd_num decimal(20, 0) NULL DEFAULT NULL,
  sq_wd_num decimal(20, 0) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.py_cx_dw_envbld_hf_wd_tmp (
  data_date varchar(6) NULL DEFAULT NULL,
  area_no varchar(20) NULL DEFAULT NULL,
  org_no varchar(200) NULL DEFAULT NULL,
  bank_type varchar(6) NULL DEFAULT NULL,
  state varchar(2) NOT NULL DEFAULT '',
  wd_num bigint NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS adm.py_cx_env_support (
  data_date char(8) NULL DEFAULT NULL,
  area_id varchar(20) NULL DEFAULT NULL,
  area_name varchar(100) NULL DEFAULT NULL,
  org_id varchar(200) NULL DEFAULT NULL,
  org_name varchar(200) NULL DEFAULT NULL,
  area_type_code varchar(20) NULL DEFAULT NULL,
  compare_type_code varchar(20) NULL DEFAULT NULL,
  province_suppert decimal(28, 8) NULL DEFAULT NULL,
  compare_province_suppert decimal(28, 8) NULL DEFAULT NULL,
  province_suppertmark int NULL DEFAULT NULL,
  city_suppert decimal(28, 8) NULL DEFAULT NULL,
  compare_city_suppert decimal(28, 8) NULL DEFAULT NULL,
  city_suppertmark int NULL DEFAULT NULL,
  county_suppert decimal(28, 8) NULL DEFAULT NULL,
  compare_county_suppert decimal(28, 8) NULL DEFAULT NULL,
  county_suppertmark int NULL DEFAULT NULL,
  remarks varchar(2000) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.py_cx_envbld_agent_issue (
  data_date char(8) NULL DEFAULT NULL,
  area_id varchar(20) NULL DEFAULT NULL,
  area_name varchar(100) NULL DEFAULT NULL,
  org_id varchar(200) NULL DEFAULT NULL,
  org_name varchar(200) NULL DEFAULT NULL,
  area_type_code varchar(20) NULL DEFAULT NULL,
  compare_type_code varchar(20) NULL DEFAULT NULL,
  retirement_count decimal(28, 8) NULL DEFAULT NULL,
  compare_retirement_count decimal(28, 8) NULL DEFAULT NULL,
  retirement_countmark int NULL DEFAULT NULL,
  retirement_money decimal(28, 8) NULL DEFAULT NULL,
  compare_retirement_money decimal(28, 8) NULL DEFAULT NULL,
  retirement_moneymark int NULL DEFAULT NULL,
  merge_count decimal(28, 8) NULL DEFAULT NULL,
  compare_merge_count decimal(28, 8) NULL DEFAULT NULL,
  merge_countmark int NULL DEFAULT NULL,
  merge_money decimal(28, 8) NULL DEFAULT NULL,
  compare_merge_money decimal(28, 8) NULL DEFAULT NULL,
  merge_moneymark int NULL DEFAULT NULL,
  subsidy_count decimal(28, 8) NULL DEFAULT NULL,
  compare_subsidy_count decimal(28, 8) NULL DEFAULT NULL,
  subsidy_countmark int NULL DEFAULT NULL,
  subsidy_money decimal(28, 8) NULL DEFAULT NULL,
  compare_subsidy_money decimal(28, 8) NULL DEFAULT NULL,
  subsidy_moneymark int NULL DEFAULT NULL,
  remarks varchar(2000) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.py_cx_envbld_hf_wd (
  data_date char(8) NULL DEFAULT NULL,
  area_id varchar(20) NULL DEFAULT NULL,
  area_name varchar(100) NULL DEFAULT NULL,
  org_id varchar(200) NULL DEFAULT NULL,
  org_name varchar(200) NULL DEFAULT NULL,
  area_type_code varchar(20) NULL DEFAULT NULL,
  compare_type_code varchar(20) NULL DEFAULT NULL,
  farm_drawal_code varchar(16) NULL DEFAULT NULL,
  count int NULL DEFAULT 0,
  compare_count int NULL DEFAULT 0,
  countmark int NULL DEFAULT NULL,
  percent varchar(20) NULL DEFAULT NULL,
  compare_percent varchar(20) NULL DEFAULT NULL,
  percentmark int NULL DEFAULT NULL,
  pen decimal(28, 8) NULL DEFAULT 0.00000000,
  compare_pen decimal(28, 8) NULL DEFAULT 0.00000000,
  penmark int NULL DEFAULT 0,
  amount decimal(28, 8) NULL DEFAULT 0.00000000,
  compare_amount decimal(28, 8) NULL DEFAULT 0.00000000,
  amountmark int NULL DEFAULT 0,
  remarks varchar(2000) NULL DEFAULT '0'
);

CREATE TABLE IF NOT EXISTS adm.py_cx_envbld_hf_wd_mid (
  data_date varchar(11) NULL DEFAULT NULL,
  city_name varchar(6) NULL DEFAULT NULL,
  village_count int NULL DEFAULT NULL,
  service_point_count varchar(50) NULL DEFAULT NULL,
  nh_bank decimal(28, 4) NULL DEFAULT NULL,
  yc_bank decimal(28, 4) NULL DEFAULT NULL,
  nxs_bank decimal(28, 4) NULL DEFAULT NULL,
  qt_bank decimal(28, 4) NULL DEFAULT NULL,
  zf_bank decimal(28, 4) NULL DEFAULT NULL,
  xj_bank decimal(28, 4) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.py_cx_envbld_hf_wd_mid_cs (
  data_date varchar(20) NULL DEFAULT NULL,
  city_name varchar(50) NULL DEFAULT NULL,
  service_point_count varchar(100) NULL DEFAULT NULL,
  nh_bank decimal(28, 4) NULL DEFAULT NULL,
  yc_bank decimal(28, 4) NULL DEFAULT NULL,
  nxs_bank decimal(28, 4) NULL DEFAULT NULL,
  qt_bank decimal(28, 4) NULL DEFAULT NULL,
  zf_bank decimal(28, 4) NULL DEFAULT NULL,
  xj_bank decimal(28, 4) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.py_cx_envbld_hf_wd_mid_order (
  order_num varchar(2) NULL DEFAULT NULL,
  area_no varchar(10) NULL DEFAULT NULL,
  model_area varchar(100) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.py_cx_envbld_nonbnk_innet_pay (
  data_date char(8) NULL DEFAULT NULL,
  area_id varchar(20) NULL DEFAULT NULL,
  area_name varchar(100) NULL DEFAULT NULL,
  org_id varchar(200) NULL DEFAULT NULL,
  org_name varchar(200) NULL DEFAULT NULL,
  area_type_code varchar(20) NULL DEFAULT NULL,
  compare_type_code varchar(20) NULL DEFAULT NULL,
  online_pay_count decimal(28, 8) NULL DEFAULT NULL,
  compare_online_pay_count decimal(28, 8) NULL DEFAULT NULL,
  online_pay_countmark int NULL DEFAULT NULL,
  online_pay_money decimal(28, 8) NULL DEFAULT NULL,
  compare_online_pay_money decimal(28, 8) NULL DEFAULT NULL,
  online_pay_moneymark int NULL DEFAULT NULL,
  move_phone_count decimal(28, 8) NULL DEFAULT NULL,
  compare_move_phone_count decimal(28, 8) NULL DEFAULT NULL,
  move_phone_countmark int NULL DEFAULT NULL,
  move_phone_money decimal(28, 8) NULL DEFAULT NULL,
  compare_move_phone_money decimal(28, 8) NULL DEFAULT NULL,
  move_phone_moneymark int NULL DEFAULT NULL,
  online_retail_count decimal(28, 8) NULL DEFAULT NULL,
  compare_online_retail_count decimal(28, 8) NULL DEFAULT NULL,
  online_retail_countmark int NULL DEFAULT NULL,
  online_retail_money decimal(28, 8) NULL DEFAULT NULL,
  compare_online_retail_money decimal(28, 8) NULL DEFAULT NULL,
  online_retail_moneymark int NULL DEFAULT NULL,
  remarks varchar(2000) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.py_cx_envbld_rural_bnk_accts (
  data_date char(8) NULL DEFAULT NULL,
  area_id varchar(20) NULL DEFAULT NULL,
  area_name varchar(100) NULL DEFAULT NULL,
  org_id varchar(200) NULL DEFAULT NULL,
  org_name varchar(200) NULL DEFAULT NULL,
  area_type_code varchar(20) NULL DEFAULT NULL,
  compare_type_code varchar(20) NULL DEFAULT NULL,
  unit_back int NULL DEFAULT NULL,
  compare_unit_back int NULL DEFAULT NULL,
  unit_backmark int NULL DEFAULT NULL,
  personal_bank_1 decimal(28, 4) NULL DEFAULT NULL,
  compare_personal_bank_1 decimal(28, 4) NULL DEFAULT NULL,
  personal_bank_1mark int NULL DEFAULT NULL,
  personal_bank_2 decimal(28, 4) NULL DEFAULT NULL,
  compare_personal_bank_2 decimal(28, 4) NULL DEFAULT NULL,
  personal_bank_2mark int NULL DEFAULT NULL,
  personal_bank_3 decimal(28, 4) NULL DEFAULT NULL,
  compare_personal_bank_3 decimal(28, 4) NULL DEFAULT NULL,
  personal_bank_3mark int NULL DEFAULT NULL,
  remarks varchar(2000) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.py_cx_envbld_rural_bnk_acctsbk (
  data_date char(8) NULL DEFAULT NULL,
  area_id varchar(20) NULL DEFAULT NULL,
  area_name varchar(100) NULL DEFAULT NULL,
  org_id varchar(200) NULL DEFAULT NULL,
  org_name varchar(200) NULL DEFAULT NULL,
  area_type_code varchar(20) NULL DEFAULT NULL,
  compare_type_code varchar(20) NULL DEFAULT NULL,
  unit_back int NULL DEFAULT NULL,
  compare_unit_back int NULL DEFAULT NULL,
  personal_bank_1 decimal(28, 4) NULL DEFAULT NULL,
  compare_personal_bank_1 decimal(28, 4) NULL DEFAULT NULL,
  personal_bank_2 decimal(28, 4) NULL DEFAULT NULL,
  compare_personal_bank_2 decimal(28, 4) NULL DEFAULT NULL,
  personal_bank_3 decimal(28, 4) NULL DEFAULT NULL,
  compare_personal_bank_3 decimal(28, 4) NULL DEFAULT NULL,
  remarks varchar(2000) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.py_cx_envbld_rural_bnk_cards (
  data_date char(8) NULL DEFAULT NULL,
  area_id varchar(20) NULL DEFAULT NULL,
  area_name varchar(100) NULL DEFAULT NULL,
  org_id varchar(200) NULL DEFAULT NULL,
  org_name varchar(200) NULL DEFAULT NULL,
  area_type_code varchar(20) NULL DEFAULT NULL,
  compare_type_code varchar(20) NULL DEFAULT NULL,
  debit_card decimal(11, 0) NULL DEFAULT NULL,
  compare_debit_card decimal(28, 4) NULL DEFAULT NULL,
  debit_cardmark int NULL DEFAULT NULL,
  credit_card decimal(28, 4) NULL DEFAULT NULL,
  compare_credit_card decimal(28, 4) NULL DEFAULT NULL,
  credit_cardmark int NULL DEFAULT NULL,
  debit_credit decimal(28, 4) NULL DEFAULT NULL,
  compare_debit_credit decimal(28, 4) NULL DEFAULT NULL,
  debit_creditmark int NULL DEFAULT NULL,
  per_capita decimal(28, 4) NULL DEFAULT NULL,
  compare_per_capita decimal(28, 4) NULL DEFAULT NULL,
  per_capitamark int NULL DEFAULT NULL,
  remarks varchar(2000) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.py_cx_envbld_rural_cards_mkt (
  data_date char(8) NULL DEFAULT NULL,
  area_id varchar(20) NULL DEFAULT NULL,
  area_name varchar(100) NULL DEFAULT NULL,
  org_id varchar(200) NULL DEFAULT NULL,
  org_name varchar(500) NULL DEFAULT NULL,
  area_type_code varchar(20) NULL DEFAULT NULL,
  compare_type_code varchar(20) NULL DEFAULT NULL,
  bcard_type_dscr varchar(100) NULL DEFAULT NULL,
  special_merch int NULL DEFAULT NULL,
  compare_special_merch int NULL DEFAULT NULL,
  special_merchmark int NULL DEFAULT NULL,
  number int NULL DEFAULT NULL,
  compare_number int NULL DEFAULT NULL,
  numbermark int NULL DEFAULT NULL,
  count decimal(28, 8) NULL DEFAULT NULL,
  compare_count decimal(28, 8) NULL DEFAULT NULL,
  countmark int NULL DEFAULT NULL,
  money decimal(28, 8) NULL DEFAULT NULL,
  compare_money decimal(28, 8) NULL DEFAULT NULL,
  moneymark int NULL DEFAULT NULL,
  transfer_phone decimal(28, 8) NULL DEFAULT NULL,
  compare_transfer_phone decimal(28, 8) NULL DEFAULT NULL,
  transfer_phonemark int NULL DEFAULT NULL,
  remarks varchar(2000) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.py_cx_envbld_rural_cards_mktbk (
  data_date char(8) NULL DEFAULT NULL,
  area_id varchar(20) NULL DEFAULT NULL,
  area_name varchar(100) NULL DEFAULT NULL,
  org_id varchar(200) NULL DEFAULT NULL,
  org_name varchar(500) NULL DEFAULT NULL,
  area_type_code varchar(20) NULL DEFAULT NULL,
  compare_type_code varchar(20) NULL DEFAULT NULL,
  bcard_type_dscr varchar(100) NULL DEFAULT NULL,
  special_merch int NULL DEFAULT NULL,
  compare_special_merch int NULL DEFAULT NULL,
  number int NULL DEFAULT NULL,
  compare_number int NULL DEFAULT NULL,
  count decimal(28, 8) NULL DEFAULT NULL,
  compare_count decimal(28, 8) NULL DEFAULT NULL,
  money decimal(28, 8) NULL DEFAULT NULL,
  compare_money decimal(28, 8) NULL DEFAULT NULL,
  transfer_phone decimal(28, 8) NULL DEFAULT NULL,
  compare_transfer_phone decimal(28, 8) NULL DEFAULT NULL,
  remarks varchar(2000) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.py_cx_envbld_rural_cls (
  data_date char(8) NULL DEFAULT NULL,
  area_id varchar(20) NULL DEFAULT NULL,
  area_name varchar(100) NULL DEFAULT NULL,
  org_id varchar(200) NULL DEFAULT NULL,
  org_name varchar(200) NULL DEFAULT NULL,
  area_type_code varchar(20) NULL DEFAULT NULL,
  compare_type_code varchar(20) NULL DEFAULT NULL,
  count decimal(28, 8) NULL DEFAULT NULL,
  compare_count decimal(28, 8) NULL DEFAULT NULL,
  countmark int NULL DEFAULT NULL,
  money decimal(28, 8) NULL DEFAULT NULL,
  compare_money decimal(28, 8) NULL DEFAULT NULL,
  moneymark int NULL DEFAULT NULL,
  bank_branch int NULL DEFAULT NULL,
  compare_bank_branch int NULL DEFAULT NULL,
  bank_branchmark int NULL DEFAULT NULL,
  agency_branch int NULL DEFAULT NULL,
  compare_agency_branch int NULL DEFAULT NULL,
  agency_branchmark int NULL DEFAULT NULL,
  coverage varchar(20) NULL DEFAULT NULL,
  compare_coverage varchar(20) NULL DEFAULT NULL,
  coveragemark int NULL DEFAULT NULL,
  remarks varchar(2000) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.py_cx_envbld_rural_ncpi_tltyp (
  data_date char(8) NULL DEFAULT NULL,
  area_id varchar(20) NULL DEFAULT NULL,
  area_name varchar(100) NULL DEFAULT NULL,
  org_id varchar(200) NULL DEFAULT NULL,
  org_name varchar(200) NULL DEFAULT NULL,
  area_type_code varchar(20) NULL DEFAULT NULL,
  compare_type_code varchar(20) NULL DEFAULT NULL,
  pay_code varchar(100) NULL DEFAULT NULL,
  business_code varchar(100) NULL DEFAULT NULL,
  account_number decimal(28, 8) NULL DEFAULT NULL,
  compare_account_number decimal(28, 8) NULL DEFAULT NULL,
  account_numbermark int NULL DEFAULT NULL,
  count decimal(28, 8) NULL DEFAULT NULL,
  compare_count decimal(28, 8) NULL DEFAULT NULL,
  countmark int NULL DEFAULT NULL,
  money decimal(28, 8) NULL DEFAULT NULL,
  compare_money decimal(28, 8) NULL DEFAULT NULL,
  moneymark int NULL DEFAULT NULL,
  remarks varchar(2000) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.py_cx_envbld_rural_static (
  data_date char(8) NULL DEFAULT NULL,
  area_id varchar(20) NULL DEFAULT NULL,
  area_name varchar(100) NULL DEFAULT NULL,
  org_id varchar(200) NULL DEFAULT NULL,
  org_name varchar(200) NULL DEFAULT NULL,
  area_type_code varchar(20) NULL DEFAULT NULL,
  compare_type_code varchar(20) NULL DEFAULT NULL,
  county_num int NULL DEFAULT NULL,
  compare_county_num int NULL DEFAULT NULL,
  country_num int NULL DEFAULT NULL,
  compare_country_num int NULL DEFAULT NULL,
  village_num int NULL DEFAULT NULL,
  compare_village_num int NULL DEFAULT NULL,
  population_num decimal(28, 2) NULL DEFAULT NULL,
  compare_population_num decimal(28, 2) NULL DEFAULT NULL,
  outlets_num int NULL DEFAULT NULL,
  compare_outlets_num int NULL DEFAULT NULL,
  remarks varchar(2000) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.py_cx_error_trade (
  org_num varchar(14) NULL DEFAULT NULL,
  area_num varchar(14) NULL DEFAULT NULL,
  city_name varchar(100) NULL DEFAULT NULL,
  county_name varchar(100) NULL DEFAULT NULL,
  town_name varchar(100) NULL DEFAULT NULL,
  village_name varchar(100) NULL DEFAULT NULL,
  service_point_name varchar(100) NULL DEFAULT NULL,
  shop_num varchar(50) NULL DEFAULT NULL,
  termina_num varchar(50) NULL DEFAULT NULL,
  trade_date varchar(20) NULL DEFAULT NULL,
  trade_mode varchar(50) NULL DEFAULT NULL,
  trade_amount decimal(20, 6) NULL DEFAULT NULL,
  trade_account varchar(20) NULL DEFAULT NULL,
  opp_account varchar(30) NULL DEFAULT NULL,
  trade_num varchar(50) NULL DEFAULT NULL,
  trade_region varchar(100) NULL DEFAULT NULL,
  big_error_type varchar(10) NULL DEFAULT NULL,
  little_error_type varchar(10) NULL DEFAULT NULL,
  remarks varchar(100) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.py_cx_map_fwdcity (
  data_date varchar(14) NULL DEFAULT NULL,
  area_id varchar(14) NULL DEFAULT NULL,
  area_name varchar(30) NULL DEFAULT NULL,
  count varchar(10) NULL DEFAULT '0',
  web_count decimal(20, 8) NULL DEFAULT 0.00000000,
  web_money decimal(20, 8) NULL DEFAULT 0.00000000,
  mob_count decimal(20, 8) NULL DEFAULT 0.00000000,
  mob_money decimal(20, 8) NULL DEFAULT 0.00000000,
  tel_count decimal(20, 8) NULL DEFAULT 0.00000000,
  tel_money decimal(20, 8) NULL DEFAULT 0.00000000,
  yhk_count decimal(20, 8) NULL DEFAULT 0.00000000,
  yhk_money decimal(20, 8) NULL DEFAULT 0.00000000,
  pj_count decimal(20, 8) NULL DEFAULT 0.00000000,
  pj_money decimal(20, 8) NULL DEFAULT 0.00000000,
  ds_count decimal(20, 8) NULL DEFAULT 0.00000000,
  ds_money decimal(20, 8) NULL DEFAULT 0.00000000,
  gr_zh decimal(20, 8) NULL DEFAULT 0.00000000,
  debit_card decimal(20, 8) NULL DEFAULT 0.00000000,
  credit_card decimal(20, 8) NULL DEFAULT 0.00000000,
  debit_credit decimal(20, 8) NULL DEFAULT 0.00000000
);

CREATE TABLE IF NOT EXISTS adm.py_cx_map_zlarea (
  trade_date varchar(20) NULL DEFAULT NULL,
  area_code varchar(20) NULL DEFAULT NULL,
  area_name varchar(100) NULL DEFAULT NULL,
  city_code char(6) NULL DEFAULT NULL,
  city_name varchar(30) NULL DEFAULT NULL,
  trade_num bigint NOT NULL DEFAULT 0,
  trade_amount decimal(42, 6) NULL DEFAULT NULL,
  trade_type varchar(50) NULL DEFAULT NULL,
  org_name varchar(100) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.py_cx_map_zlarea_ycdq (
  trade_date varchar(20) NULL DEFAULT NULL,
  nbh_code char(14) NULL DEFAULT NULL,
  village_code varchar(14) NULL DEFAULT NULL,
  trade_num decimal(42, 0) NULL DEFAULT NULL,
  trade_amount decimal(64, 6) NULL DEFAULT NULL,
  trade_type varchar(50) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.py_cx_map_zlareabk (
  trade_date varchar(20) NULL DEFAULT NULL,
  area_code varchar(20) NULL DEFAULT NULL,
  area_name varchar(100) NULL DEFAULT NULL,
  city_code char(6) NULL DEFAULT NULL,
  city_name varchar(30) NULL DEFAULT NULL,
  trade_num bigint NOT NULL DEFAULT 0,
  trade_amount decimal(42, 6) NULL DEFAULT NULL,
  trade_type varchar(50) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.py_cx_map_zlcity (
  trade_date varchar(20) NULL DEFAULT NULL,
  city_code char(6) NULL DEFAULT NULL,
  city_name varchar(30) NULL DEFAULT NULL,
  trade_num bigint NOT NULL DEFAULT 0,
  trade_amount decimal(42, 6) NULL DEFAULT NULL,
  trade_type varchar(50) NULL DEFAULT NULL,
  org_name varchar(100) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.py_cx_map_zlcityacc (
  trade_date varchar(20) NULL DEFAULT NULL,
  shop_num varchar(50) NULL DEFAULT NULL,
  termina_num varchar(50) NULL DEFAULT NULL,
  village_code varchar(14) NULL DEFAULT NULL,
  nbh_code char(14) NULL DEFAULT NULL,
  city_code char(6) NULL DEFAULT NULL,
  city_name varchar(30) NULL DEFAULT NULL,
  trade_num bigint NOT NULL DEFAULT 0,
  trade_amount decimal(42, 6) NULL DEFAULT NULL,
  trade_type varchar(50) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.py_cx_map_zlcityaccbk (
  trade_date varchar(20) NULL DEFAULT NULL,
  shop_num varchar(50) NULL DEFAULT NULL,
  shop_num1 varchar(50) NULL DEFAULT NULL,
  termina_num varchar(50) NULL DEFAULT NULL,
  termina_num1 varchar(50) NULL DEFAULT NULL,
  village_code varchar(14) NULL DEFAULT NULL,
  nbh_code char(14) NULL DEFAULT NULL,
  city_code char(6) NULL DEFAULT NULL,
  city_name varchar(30) NULL DEFAULT NULL,
  trade_num bigint NOT NULL DEFAULT 0,
  trade_amount decimal(42, 6) NULL DEFAULT NULL,
  trade_type varchar(50) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.py_cx_map_zlcitybk (
  trade_date varchar(20) NULL DEFAULT NULL,
  city_code char(6) NULL DEFAULT NULL,
  city_name varchar(30) NULL DEFAULT NULL,
  trade_num bigint NOT NULL DEFAULT 0,
  trade_amount decimal(42, 6) NULL DEFAULT NULL,
  trade_type varchar(50) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.py_cx_menu_zt (
  menu_id varchar(50) NULL DEFAULT NULL,
  url varchar(2000) NULL DEFAULT NULL,
  url_type varchar(20) NULL DEFAULT NULL,
  url_pro varchar(20) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.py_cx_mod_change_tj (
  data_date char(8) NULL DEFAULT NULL,
  order_num varchar(3) NULL DEFAULT NULL,
  model_area varchar(100) NULL DEFAULT NULL,
  shop_count_num varchar(20) NULL DEFAULT NULL,
  shop_type varchar(20) NULL DEFAULT NULL,
  new_chage_num varchar(20) NULL DEFAULT NULL,
  tot_chage_num varchar(20) NULL DEFAULT NULL,
  curr_sup_rate varchar(20) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.py_cx_mod_trade (
  data_date char(8) NULL DEFAULT NULL,
  order_num varchar(2) NULL DEFAULT NULL,
  model_area varchar(100) NULL DEFAULT NULL,
  shop_type varchar(20) NULL DEFAULT NULL,
  new_trade_num varchar(20) NULL DEFAULT NULL,
  new_trade_amout decimal(20, 6) NULL DEFAULT NULL,
  tot_trade_num varchar(20) NULL DEFAULT NULL,
  tot_trade_amout decimal(20, 6) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.py_cx_mth_cash_active_area (
  trade_date varchar(10) NULL DEFAULT NULL,
  area_code char(6) NULL DEFAULT NULL,
  city_code char(6) NULL DEFAULT NULL,
  num varchar(10) NOT NULL DEFAULT '0',
  act_num varchar(10) NULL DEFAULT NULL,
  no_act_num varchar(10) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.py_cx_mth_cash_servinfo_active (
  trade_date varchar(10) NULL DEFAULT NULL,
  shop_num varchar(50) NULL DEFAULT NULL,
  city_name varchar(50) NULL DEFAULT NULL,
  county_name varchar(50) NULL DEFAULT NULL,
  town_name varchar(50) NULL DEFAULT NULL,
  village_num varchar(20) NULL DEFAULT NULL,
  service_point_name varchar(200) NULL DEFAULT NULL,
  service_point_adds varchar(500) NULL DEFAULT NULL,
  service_point_lon varchar(50) NULL DEFAULT NULL,
  service_point_la varchar(50) NULL DEFAULT NULL,
  active_type varchar(10) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.py_cx_nomatch_areacode (
  village_code varchar(14) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.py_cx_pay_change_tj (
  data_date varchar(20) NULL DEFAULT NULL,
  area_code varchar(20) NULL DEFAULT NULL,
  model_area varchar(100) NULL DEFAULT NULL,
  ind_name varchar(100) NULL DEFAULT NULL,
  new_add_num varchar(20) NULL DEFAULT NULL,
  tot_add_num varchar(20) NULL DEFAULT NULL,
  tot_add_name varchar(1000) NULL DEFAULT NULL,
  new_trade_num varchar(20) NULL DEFAULT NULL,
  new_trade_amount decimal(20, 2) NULL DEFAULT NULL,
  tot_trade_num varchar(20) NULL DEFAULT NULL,
  tot_trade_amount decimal(20, 2) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.py_cx_pay_info (
  data_date char(8) NULL DEFAULT NULL,
  order_num char(5) NULL DEFAULT NULL,
  trade_date varchar(20) NULL DEFAULT NULL,
  shop_num varchar(50) NULL DEFAULT NULL,
  termina_num varchar(50) NULL DEFAULT NULL,
  col_org_name varchar(100) NULL DEFAULT NULL,
  shop_name varchar(100) NULL DEFAULT NULL,
  bel_scene varchar(100) NULL DEFAULT NULL,
  trade_amount decimal(20, 6) NULL DEFAULT NULL,
  trade_channel varchar(100) NULL DEFAULT NULL,
  opp_account varchar(30) NULL DEFAULT NULL,
  opp_name varchar(100) NULL DEFAULT NULL,
  opp_open_org varchar(100) NULL DEFAULT NULL,
  remarks varchar(100) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.py_cx_reg_shop (
  data_date char(8) NULL DEFAULT NULL,
  order_num int NULL DEFAULT NULL,
  city_name varchar(100) NULL DEFAULT NULL,
  new_reg_user int NULL DEFAULT NULL,
  tot_reg_user int NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.py_cx_trade_check_info (
  data_date varchar(10) NULL DEFAULT NULL,
  area_code varchar(10) NULL DEFAULT NULL,
  area_name varchar(50) NULL DEFAULT NULL,
  city_code varchar(10) NULL DEFAULT NULL,
  city_name varchar(50) NULL DEFAULT NULL,
  serv_point decimal(10, 0) NULL DEFAULT 0,
  serv_count_rj decimal(10, 2) NULL DEFAULT 0.00,
  serv_amount_rj decimal(10, 2) NULL DEFAULT 0.00,
  serv_count decimal(10, 0) NULL DEFAULT 0,
  serv_amount decimal(10, 2) NULL DEFAULT 0.00,
  serv_hjl decimal(10, 2) NULL DEFAULT 0.00,
  data_type varchar(1) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.py_cx_warning_analysis (
  data_date char(8) NULL DEFAULT NULL,
  tyade_type varchar(10) NULL DEFAULT NULL,
  trade_pen int NULL DEFAULT NULL,
  trade_amount decimal(20, 2) NULL DEFAULT NULL,
  big_error_type varchar(10) NULL DEFAULT NULL,
  little_error_type varchar(10) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.py_cx_website_info_offline (
  data_date char(8) NULL DEFAULT NULL,
  org_id varchar(20) NULL DEFAULT NULL,
  org_name varchar(200) NULL DEFAULT NULL,
  rmb_count int NULL DEFAULT NULL,
  rmb_amount decimal(25, 2) NULL DEFAULT NULL,
  broken_rmb_count int NULL DEFAULT NULL,
  broken_rmb_amount decimal(25, 2) NULL DEFAULT NULL,
  xqyydj_count int NULL DEFAULT NULL,
  yhfk_count int NULL DEFAULT NULL,
  yhfk_amount decimal(25, 2) NULL DEFAULT NULL,
  bx_xqyydj_count int NULL DEFAULT NULL,
  bx_tb_count int NULL DEFAULT NULL,
  bx_bf_amount decimal(25, 2) NULL DEFAULT NULL,
  bx_amount decimal(25, 2) NULL DEFAULT NULL,
  bxlp_xqyydj_count int NULL DEFAULT NULL,
  bxlp_cgpf_count int NULL DEFAULT NULL,
  bxlp_cgpf_amount decimal(25, 2) NULL DEFAULT NULL,
  shop_dg_count int NULL DEFAULT NULL,
  shop_dg_amount decimal(25, 2) NULL DEFAULT NULL,
  shop_dx_count int NULL DEFAULT NULL,
  shop_dx_amount decimal(25, 2) NULL DEFAULT NULL,
  lccpxs_count int NULL DEFAULT NULL,
  lccpxs_amount decimal(25, 2) NULL DEFAULT NULL,
  gzxs_count int NULL DEFAULT NULL,
  gzxs_amount decimal(25, 2) NULL DEFAULT NULL,
  skjn_count int NULL DEFAULT NULL,
  skjn_amount decimal(25, 2) NULL DEFAULT NULL,
  sb_count int NULL DEFAULT NULL,
  sb_amount decimal(25, 2) NULL DEFAULT NULL,
  jd_count int NULL DEFAULT NULL,
  xczl_count int NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.py_cx_yc_check_big (
  data_date varchar(10) NULL DEFAULT NULL,
  yc_big varchar(50) NULL DEFAULT NULL,
  city_name varchar(100) NULL DEFAULT NULL,
  city_code varchar(14) NULL DEFAULT NULL,
  county_name varchar(100) NULL DEFAULT NULL,
  county_num varchar(14) NULL DEFAULT NULL,
  town_name varchar(100) NULL DEFAULT NULL,
  town_num varchar(14) NULL DEFAULT NULL,
  village_name varchar(100) NULL DEFAULT NULL,
  village_num varchar(14) NULL DEFAULT NULL,
  trade_type varchar(50) NULL DEFAULT NULL,
  trade_count varchar(10) NULL DEFAULT NULL,
  trade_amount decimal(20, 6) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.py_cx_yc_check_detail (
  trade_date varchar(20) NULL DEFAULT NULL,
  yc_big varchar(30) NULL DEFAULT NULL,
  yc_detail varchar(50) NULL DEFAULT NULL,
  org_num varchar(20) NULL DEFAULT NULL,
  org_name varchar(100) NULL DEFAULT NULL,
  city_name varchar(100) NULL DEFAULT NULL,
  city_code varchar(14) NULL DEFAULT NULL,
  area_name varchar(100) NULL DEFAULT NULL,
  area_code varchar(14) NULL DEFAULT NULL,
  town_name varchar(100) NULL DEFAULT NULL,
  town_code varchar(14) NULL DEFAULT NULL,
  village_name varchar(100) NULL DEFAULT NULL,
  village_code varchar(14) NULL DEFAULT NULL,
  service_point_name varchar(100) NULL DEFAULT NULL,
  shop_num varchar(50) NULL DEFAULT NULL,
  termina_num varchar(50) NULL DEFAULT NULL,
  trade_amount decimal(20, 6) NULL DEFAULT NULL,
  trade_account varchar(20) NULL DEFAULT NULL,
  trade_type varchar(50) NULL DEFAULT NULL,
  trade_mode varchar(30) NULL DEFAULT NULL,
  trade_region varchar(100) NULL DEFAULT NULL,
  trade_num varchar(50) NULL DEFAULT NULL,
  remarks varchar(100) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.py_cx_yc_check_tjb (
  data_date varchar(6) NULL DEFAULT NULL,
  org_type_dscr_2 varchar(512) NULL DEFAULT NULL,
  area_code varchar(14) NULL DEFAULT NULL,
  area_name varchar(100) NULL DEFAULT NULL,
  city_code varchar(14) NULL DEFAULT NULL,
  city_name varchar(100) NULL DEFAULT NULL,
  yc_bs bigint NOT NULL DEFAULT 0,
  yc_je decimal(42, 6) NULL DEFAULT NULL,
  yc_big varchar(30) NULL DEFAULT NULL,
  yc_detail varchar(50) NULL DEFAULT NULL,
  remarks varchar(100) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.py_cx_ydfx (
  data_date char(8) NULL DEFAULT NULL,
  "table" varchar(100) NULL DEFAULT NULL,
  table_name varchar(100) NULL DEFAULT NULL,
  order_num int NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.rpt_half_premium_cardinal (
  area_no_id char(8) NULL DEFAULT NULL,
  fiscal_term char(10) NULL DEFAULT NULL,
  org_id char(14) NULL DEFAULT NULL,
  insur_org_id char(14) NULL DEFAULT NULL,
  insur_org_name varchar(512) NULL DEFAULT NULL,
  insur_org_type_code char(8) NULL DEFAULT NULL,
  insur_org_type_dscr varchar(60) NULL DEFAULT NULL,
  premium_cardinal decimal(30, 8) NULL DEFAULT NULL,
  remark varchar(6000) NULL DEFAULT NULL,
  ori_insure_org_code varchar(200) NULL DEFAULT NULL,
  ori_insure_org_name varchar(200) NULL DEFAULT NULL,
  ori_org_type_dscr varchar(100) NULL DEFAULT NULL,
  is_cur_neworg varchar(20) NULL DEFAULT NULL,
  is_cur_changeorg varchar(20) NULL DEFAULT NULL,
  is_cur_merge varchar(20) NULL DEFAULT NULL,
  is_cur_merge_end varchar(20) NULL DEFAULT NULL,
  is_cur_change_end varchar(20) NULL DEFAULT NULL,
  batch_date timestamp(0) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.rpt_half_premium_pay_info (
  area_no_id char(8) NULL DEFAULT NULL,
  fiscal_term char(10) NULL DEFAULT NULL,
  org_id char(14) NULL DEFAULT NULL,
  insur_org_id varchar(512) NULL DEFAULT NULL,
  insur_org_name varchar(512) NULL DEFAULT NULL,
  insur_org_type_code char(8) NULL DEFAULT NULL,
  insur_org_type_dscr varchar(60) NULL DEFAULT NULL,
  payable_premium decimal(30, 5) NULL DEFAULT NULL,
  last_premium_decuct decimal(30, 5) NULL DEFAULT NULL,
  remitter varchar(100) NULL DEFAULT NULL,
  remit_acct varchar(100) NULL DEFAULT NULL,
  remit_amount decimal(30, 5) NULL DEFAULT NULL,
  remit_date varchar(20) NULL DEFAULT NULL,
  remit_remake varchar(200) NULL DEFAULT NULL,
  receipt varchar(200) NULL DEFAULT NULL,
  remakes varchar(200) NULL DEFAULT NULL,
  batch_date timestamp(0) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.rpt_half_risk_diff_rate_revise (
  area_no_id char(8) NULL DEFAULT NULL,
  fiscal_term char(10) NULL DEFAULT NULL,
  org_id char(14) NULL DEFAULT NULL,
  insur_org_id char(14) NULL DEFAULT NULL,
  insur_org_name varchar(512) NULL DEFAULT NULL,
  capital_suff_ratio decimal(30, 5) NULL DEFAULT NULL,
  leverage_ratio decimal(30, 5) NULL DEFAULT NULL,
  loan_bal decimal(30, 5) NULL DEFAULT NULL,
  overdue90_bal decimal(30, 5) NULL DEFAULT NULL,
  loan_prov_bal decimal(30, 5) NULL DEFAULT NULL,
  cost_income_rate decimal(30, 5) NULL DEFAULT NULL,
  flow_cover_rate decimal(30, 5) NULL DEFAULT NULL,
  flow_prop_4 decimal(30, 5) NULL DEFAULT NULL,
  flow_prop_3 decimal(30, 5) NULL DEFAULT NULL,
  flow_prop_2 decimal(30, 5) NULL DEFAULT NULL,
  flow_prop_1 decimal(30, 5) NULL DEFAULT NULL,
  core_debt_rely_4 decimal(30, 5) NULL DEFAULT NULL,
  core_debt_rely_3 decimal(30, 5) NULL DEFAULT NULL,
  core_debt_rely_2 decimal(30, 5) NULL DEFAULT NULL,
  core_debt_rely_1 decimal(30, 5) NULL DEFAULT NULL,
  batch_date timestamp(0) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.rpt_m_estate_stat (
  batch_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  area_no varchar(8) NULL DEFAULT NULL,
  data_date char(8) NULL DEFAULT NULL,
  org_no varchar(53) NULL DEFAULT NULL,
  rows_id int NULL DEFAULT NULL,
  project varchar(255) NULL DEFAULT NULL,
  loan_balance decimal(30, 5) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.rpt_m_farm_loan_stat (
  bach_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  area_no varchar(8) NULL DEFAULT NULL,
  data_date char(8) NULL DEFAULT NULL,
  org_no varchar(53) NULL DEFAULT NULL,
  project varchar(255) NULL DEFAULT NULL,
  project_order int NULL DEFAULT NULL,
  loan_bal decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_zb decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_add_new decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_add_new_zb decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_tb decimal(30, 5) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.rpt_m_loan_dist_org_proj (
  batch_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  data_date char(8) NULL DEFAULT NULL,
  org_no varchar(53) NULL DEFAULT NULL,
  org_desc varchar(255) NULL DEFAULT NULL,
  kpi_code varchar(50) NULL DEFAULT NULL,
  project_code varchar(50) NULL DEFAULT NULL,
  project_info varchar(100) NULL DEFAULT NULL,
  enter_scale varchar(50) NULL DEFAULT NULL,
  enter_scale_desc varchar(255) NULL DEFAULT NULL,
  balance_cny decimal(30, 5) NULL DEFAULT NULL,
  balance_cny_lm decimal(30, 5) NULL DEFAULT NULL,
  balance_cny_y decimal(30, 5) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.rpt_m_loan_indust_dist (
  batch_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  area_no varchar(8) NULL DEFAULT NULL,
  data_date char(8) NULL DEFAULT NULL,
  org_no varchar(53) NULL DEFAULT NULL,
  rows_id int NULL DEFAULT NULL,
  org_name varchar(255) NULL DEFAULT NULL,
  loan_balance_sum decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_real_estate decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_land decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_gov_resver decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_dev_hous decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_live_dev_hous decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_secu_hous decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_comc_dev_hous decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_other_dev_hous decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_purch_hous decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_enter_purch_hous decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_commc_use_hous decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_live_hous decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_team_purch_hous decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_team_commc_hous decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_team_live_hous decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_person decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_commc_person decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_live_person decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_dev_new decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_mortgage decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_retrans decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_stock decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_stock_person decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_stock_other decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_lower_income decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_merge_plan decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_term_merge_plan decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_main_person decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_dev_esta_hb decimal(30, 5) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.rpt_m_loan_indust_org (
  batch_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  area_no varchar(8) NULL DEFAULT NULL,
  data_date char(8) NULL DEFAULT NULL,
  org_no varchar(53) NULL DEFAULT NULL,
  rows_id int NULL DEFAULT NULL,
  org_name varchar(255) NULL DEFAULT NULL,
  loan_bal_total decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_total1 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_total11 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_total111 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_dev_hous decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_dev_hous1 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_dev_hous11 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_dev_hous111 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_dev_land decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_dev_land1 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_dev_land11 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_dev_land111 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_gov_reserv decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_gov_reserv1 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_gov_reserv11 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_gov_reserv111 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_dev_estate decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_dev_esate1 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_dev_estate11 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_dev_estate111 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_live_hous decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_live_hous1 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_live_hous11 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_live_hous111 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_lower_income decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_lower_income1 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_lower_income11 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_lower_income111 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_corp decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_corp1 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_corp11 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_corp111 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_commc_dev decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_commc_dev1 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_commc_dev11 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_commc_dev111 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_dev_hous_o decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_dev_hous_o1 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_dev_hous_o11 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_dev_hous_o111 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_buy_hous decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_buy_hous1 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_buy_hous11 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_buy_hous111 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_enter_buy_hous decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_enter_buy_hous1 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_enter_buy_hous11 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_enter_buy_hous111 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_commc_live decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_commc_live1 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_commc_live11 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_commc_live111 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_live decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_live1 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_live11 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_live111 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_pers_buy_hous decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_pers_buy_hous1 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_pers_buy_hous11 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_pers_buy_hous111 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_commc_pers decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_commc_pers1 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_commc_pers11 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_commc_pers111 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_pers_live decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_pers_live1 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_pers_live11 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_pers_live111 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_dev_new decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_dev_new1 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_dev_new11 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_dev_new111 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_mortage decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_mortage1 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_mortage11 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_mortage111 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_retrans decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_retrans1 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_retrans11 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_retrans111 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_stock decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_stock1 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_stock11 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_stock111 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_stock_pers decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_stock_pers1 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_stock_pers11 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_stock_pers111 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_stock_o decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_stock_o1 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_stock_o11 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_stock_o111 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_lower_income_pers decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_lower_income_pers1 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_lower_income_pers11 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_lower_income_pers111 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_main_pers decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_main_pers1 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_main_pers11 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_main_pers111 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_dev_hous_tb decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_dev_hous_tb1 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_dev_hous_tb11 decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_dev_hous_tb111 decimal(30, 5) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.rpt_m_loan_sum_dist (
  batch_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  data_date char(8) NULL DEFAULT NULL,
  area_no varchar(8) NULL DEFAULT NULL,
  area_name varchar(30) NULL DEFAULT NULL,
  rows_id int NULL DEFAULT NULL,
  kpi_value1 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value2 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value3 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value4 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value5 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value6 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value7 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value8 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value9 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value10 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value11 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value12 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value13 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value14 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value15 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value16 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value17 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value18 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value19 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value20 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value21 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value22 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value23 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value24 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value25 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value26 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value27 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value28 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value29 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value30 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value31 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value32 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value33 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value34 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value35 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value36 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value37 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value38 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value39 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value40 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value41 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value42 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value43 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value44 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value45 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value46 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value47 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value48 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value49 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value50 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value51 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value52 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value53 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value54 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value55 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value56 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value57 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value58 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value59 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value60 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value61 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value62 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value63 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value64 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value65 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value66 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value67 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value68 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value69 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value70 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value71 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value72 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value73 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value74 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value75 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value76 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value77 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value78 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value79 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value80 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value81 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value82 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value83 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value84 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value85 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value86 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value87 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value88 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value89 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value90 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value91 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value92 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value93 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value94 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value95 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value96 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value97 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value98 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value99 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value100 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value101 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value102 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value103 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value104 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value105 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value106 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value107 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value108 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value109 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value110 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value111 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value112 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value113 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value114 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value115 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value116 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value117 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value118 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value119 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value120 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value121 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value122 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value123 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value124 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value125 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value126 decimal(30, 5) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.rpt_m_loan_sum_indus (
  batch_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  area_no varchar(8) NULL DEFAULT NULL,
  data_date char(8) NULL DEFAULT NULL,
  org_no varchar(53) NULL DEFAULT NULL,
  rows_id int NULL DEFAULT NULL,
  industry varchar(200) NULL DEFAULT NULL,
  loan_bal decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_sub_bgy decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_sub_p decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_tb decimal(30, 5) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.rpt_m_loan_sum_insti (
  batch_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  area_no varchar(8) NULL DEFAULT NULL,
  data_date char(8) NULL DEFAULT NULL,
  org_no varchar(53) NULL DEFAULT NULL,
  rows_id int NULL DEFAULT NULL,
  org_name varchar(255) NULL DEFAULT NULL,
  loan_bal decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_sub_bgy decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_sub_p decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_tb decimal(30, 5) NULL DEFAULT NULL,
  unnor_bal decimal(30, 5) NULL DEFAULT NULL,
  unnor_bal_sub_bgy decimal(30, 5) NULL DEFAULT NULL,
  unnor_bal_sub_p decimal(30, 5) NULL DEFAULT NULL,
  unnor_bal_tb decimal(30, 5) NULL DEFAULT NULL,
  unnor_bal_rate decimal(30, 5) NULL DEFAULT NULL,
  unnor_bal_rate_sub_bgy decimal(30, 5) NULL DEFAULT NULL,
  unnor_bal_rate_tb decimal(30, 5) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.rpt_m_loan_sum_org (
  batch_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  data_date char(8) NULL DEFAULT NULL,
  org_no varchar(30) NULL DEFAULT NULL,
  org_name varchar(30) NULL DEFAULT NULL,
  rows_id int NULL DEFAULT NULL,
  kpi_value1 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value2 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value3 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value4 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value5 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value6 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value7 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value8 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value9 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value10 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value11 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value12 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value13 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value14 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value15 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value16 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value17 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value18 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value19 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value20 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value21 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value22 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value23 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value24 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value25 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value26 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value27 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value28 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value29 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value30 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value31 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value32 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value33 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value34 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value35 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value36 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value37 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value38 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value39 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value40 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value41 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value42 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value43 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value44 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value45 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value46 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value47 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value48 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value49 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value50 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value51 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value52 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value53 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value54 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value55 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value56 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value57 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value58 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value59 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value60 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value61 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value62 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value63 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value64 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value65 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value66 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value67 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value68 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value69 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value70 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value71 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value72 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value73 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value74 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value75 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value76 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value77 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value78 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value79 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value80 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value81 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value82 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value83 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value84 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value85 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value86 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value87 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value88 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value89 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value90 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value91 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value92 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value93 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value94 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value95 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value96 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value97 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value98 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value99 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value100 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value101 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value102 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value103 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value104 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value105 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value106 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value107 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value108 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value109 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value110 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value111 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value112 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value113 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value114 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value115 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value116 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value117 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value118 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value119 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value120 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value121 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value122 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value123 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value124 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value125 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value126 decimal(30, 5) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.rpt_m_loan_sum_proj (
  batch_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  area_no varchar(8) NULL DEFAULT NULL,
  data_date char(8) NULL DEFAULT NULL,
  org_no varchar(53) NULL DEFAULT NULL,
  project varchar(30) NULL DEFAULT NULL,
  rows_id varchar(50) NULL DEFAULT NULL,
  loan_bal decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_add_new decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_tb decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_y decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_tb_y decimal(30, 5) NULL DEFAULT NULL,
  loan_count decimal(30, 5) NULL DEFAULT NULL,
  people_count decimal(30, 5) NULL DEFAULT NULL,
  loan_wbal decimal(30, 5) NULL DEFAULT NULL,
  loan_fund decimal(30, 5) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.rpt_m_loan_sum_scale_indus (
  batch_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  area_no varchar(8) NULL DEFAULT NULL,
  data_date char(8) NULL DEFAULT NULL,
  org_no varchar(53) NULL DEFAULT NULL,
  kpi_code varchar(200) NULL DEFAULT NULL,
  enterprise_scale varchar(200) NULL DEFAULT NULL,
  industry varchar(200) NULL DEFAULT NULL,
  actual_bal decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_sub_bgy decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_sub_p decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_tb decimal(30, 5) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.rpt_m_mpa (
  batch_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  data_date varchar(6) NULL DEFAULT NULL,
  fin_org_no varchar(14) NULL DEFAULT NULL,
  fin_org_name varchar(30) NULL DEFAULT NULL,
  total_asset decimal(30, 5) NULL DEFAULT NULL,
  total_indebt decimal(30, 5) NULL DEFAULT NULL,
  kpi_value1 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value2 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value3 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value4 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value5 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value6 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value7 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value8 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value9 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value10 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value11 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value12 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value13 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value14 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value15 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value16 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value17 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value18 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value19 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value20 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value21 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value22 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value23 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value24 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value25 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value26 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value27 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value28 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value29 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value30 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value31 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value32 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value33 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value34 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value35 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value36 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value37 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value38 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value39 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value40 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value41 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value42 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value43 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value44 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value45 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value46 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value47 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value48 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value49 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value50 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value51 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value52 decimal(30, 5) NULL DEFAULT NULL,
  kpi_value53 decimal(30, 5) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.rpt_mth_bank_org_data_statis (
  area_no varchar(8) NULL DEFAULT NULL,
  data_date char(10) NULL DEFAULT NULL,
  table_nature varchar(14) NULL DEFAULT NULL,
  org_no char(14) NULL DEFAULT NULL,
  add_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  rows_id varchar(100) NULL DEFAULT NULL,
  sheet_id varchar(100) NULL DEFAULT NULL,
  batch_date char(8) NULL DEFAULT NULL,
  area_no_id char(8) NULL DEFAULT NULL,
  fiscal_term char(10) NULL DEFAULT NULL,
  org_id char(14) NULL DEFAULT NULL,
  org_name varchar(512) NULL DEFAULT NULL,
  org_type_code char(8) NULL DEFAULT NULL,
  org_type_dscr varchar(60) NULL DEFAULT NULL,
  belong_area_code char(8) NULL DEFAULT NULL,
  delong_area_dscr varchar(200) NULL DEFAULT NULL,
  ad_asset_yi varchar(50) NULL DEFAULT NULL,
  ad_asset varchar(50) NULL DEFAULT NULL,
  ad_loan_bal varchar(50) NULL DEFAULT NULL,
  ad_debt varchar(50) NULL DEFAULT NULL,
  ad_dept_bal varchar(50) NULL DEFAULT NULL,
  ad_save_dept varchar(50) NULL DEFAULT NULL,
  ad_owneqty_yi varchar(50) NULL DEFAULT NULL,
  ad_owneqty varchar(50) NULL DEFAULT NULL,
  ad_few_owneqty_yi varchar(50) NULL DEFAULT NULL,
  ad_few_owneqty varchar(50) NULL DEFAULT NULL,
  ad_si_storage_oppo varchar(50) NULL DEFAULT NULL,
  ad_si_oppo_storage varchar(50) NULL DEFAULT NULL,
  ad_si_lend_oppo varchar(50) NULL DEFAULT NULL,
  ad_si_oppo_lend varchar(50) NULL DEFAULT NULL,
  ad_si_buy_sale varchar(50) NULL DEFAULT NULL,
  ad_si_sale_buy varchar(50) NULL DEFAULT NULL,
  os_nohold_fin_bal varchar(50) NULL DEFAULT NULL,
  os_entrust_loan varchar(50) NULL DEFAULT NULL,
  pa_margin varchar(50) NULL DEFAULT NULL,
  pa_net_margin varchar(50) NULL DEFAULT NULL,
  pa_margin_curm varchar(50) NULL DEFAULT NULL,
  pa_few_owngl varchar(50) NULL DEFAULT NULL,
  pa_jxc_fz varchar(50) NULL DEFAULT NULL,
  pa_jxc_fm varchar(50) NULL DEFAULT NULL,
  pa_jxc_res varchar(50) NULL DEFAULT NULL,
  pa_apr_fz varchar(50) NULL DEFAULT NULL,
  pa_apr_fm varchar(50) NULL DEFAULT NULL,
  pa_apr_res varchar(50) NULL DEFAULT NULL,
  pa_inte_income_total varchar(50) NULL DEFAULT NULL,
  pa_loan_inte_income varchar(50) NULL DEFAULT NULL,
  pa_inte_cost_total varchar(50) NULL DEFAULT NULL,
  pa_loan_inte_cost varchar(50) NULL DEFAULT NULL,
  pa_ginteast_avg_bal varchar(50) NULL DEFAULT NULL,
  pa_ginteast_avgr_fz varchar(50) NULL DEFAULT NULL,
  pa_ginteast_avgr_fm varchar(50) NULL DEFAULT NULL,
  pa_ginteast_avgr_res varchar(50) NULL DEFAULT NULL,
  pa_cinteast_avg_bal varchar(50) NULL DEFAULT NULL,
  pa_cinteast_avgr_fz varchar(50) NULL DEFAULT NULL,
  pa_cinteast_avgr_fm varchar(50) NULL DEFAULT NULL,
  pa_cinteast_avgr_res varchar(50) NULL DEFAULT NULL,
  pa_comm_net_income varchar(50) NULL DEFAULT NULL,
  pa_yield varchar(50) NULL DEFAULT NULL,
  pa_fair_chang_income varchar(50) NULL DEFAULT NULL,
  pa_exchange_income varchar(50) NULL DEFAULT NULL,
  pa_operate_revenue varchar(50) NULL DEFAULT NULL,
  pa_other_buss_income varchar(50) NULL DEFAULT NULL,
  pa_mid_buss_income varchar(50) NULL DEFAULT NULL,
  pa_mbir_fz varchar(50) NULL DEFAULT NULL,
  pa_mbir_fm varchar(50) NULL DEFAULT NULL,
  pa_mbir_res varchar(50) NULL DEFAULT NULL,
  pa_operate_expend varchar(50) NULL DEFAULT NULL,
  pa_tax_addi varchar(50) NULL DEFAULT NULL,
  pa_cir_fz varchar(50) NULL DEFAULT NULL,
  pa_cir_fm varchar(50) NULL DEFAULT NULL,
  pa_cir_res varchar(50) NULL DEFAULT NULL,
  aqp_normal_loan_bal varchar(50) NULL DEFAULT NULL,
  aqp_follow_loan_bal varchar(50) NULL DEFAULT NULL,
  aqp_flr_fz varchar(50) NULL DEFAULT NULL,
  aqp_flr_fm varchar(50) NULL DEFAULT NULL,
  aqp_flr_res varchar(50) NULL DEFAULT NULL,
  aqp_bad_loan_bal varchar(50) NULL DEFAULT NULL,
  aqp_second_loan_bal varchar(50) NULL DEFAULT NULL,
  aqp_dubious_loan_bal varchar(50) NULL DEFAULT NULL,
  aqp_loss_loan_bal varchar(50) NULL DEFAULT NULL,
  aqp_overdue_loan_bal varchar(50) NULL DEFAULT NULL,
  aqp_overdue90_bal varchar(50) NULL DEFAULT NULL,
  aqp_overdue90_fz varchar(50) NULL DEFAULT NULL,
  aqp_overdue90_fm varchar(50) NULL DEFAULT NULL,
  aqp_overdue90_res varchar(50) NULL DEFAULT NULL,
  aqp_blr_fz varchar(50) NULL DEFAULT NULL,
  aqp_blr_fm varchar(50) NULL DEFAULT NULL,
  aqp_blr_res varchar(50) NULL DEFAULT NULL,
  aqp_lpr_fz varchar(50) NULL DEFAULT NULL,
  aqp_lpr_fm varchar(50) NULL DEFAULT NULL,
  aqp_lpr_res varchar(50) NULL DEFAULT NULL,
  aqp_pcr_fz varchar(50) NULL DEFAULT NULL,
  aqp_pcr_fm varchar(50) NULL DEFAULT NULL,
  aqp_pcr_res varchar(50) NULL DEFAULT NULL,
  aqp_big_loan_amt varchar(50) NULL DEFAULT NULL,
  aqp_big_loan_res varchar(50) NULL DEFAULT NULL,
  aqp_big10_loan_amt varchar(50) NULL DEFAULT NULL,
  aqp_big10_loan_amt_res varchar(50) NULL DEFAULT NULL,
  aqp_cash_liquidate varchar(50) NULL DEFAULT NULL,
  aqp_batch_transfer varchar(50) NULL DEFAULT NULL,
  aqp_pay_in_debt varchar(50) NULL DEFAULT NULL,
  aqp_cancel varchar(50) NULL DEFAULT NULL,
  aqp_nlmr_fz varchar(50) NULL DEFAULT NULL,
  aqp_nlmr_fm varchar(50) NULL DEFAULT NULL,
  aqp_nlmr_res varchar(50) NULL DEFAULT NULL,
  aqp_flmr_fz varchar(50) NULL DEFAULT NULL,
  aqp_flmr_fm varchar(50) NULL DEFAULT NULL,
  aqp_flmr_res varchar(50) NULL DEFAULT NULL,
  f_dlr_fz varchar(50) NULL DEFAULT NULL,
  f_dlr_fm varchar(50) NULL DEFAULT NULL,
  f_dlr_res varchar(50) NULL DEFAULT NULL,
  o_buysale_bill varchar(50) NULL DEFAULT NULL,
  o_salebuy_bill varchar(50) NULL DEFAULT NULL,
  o_si_invest_stand varchar(50) NULL DEFAULT NULL,
  o_si_invest_bill varchar(50) NULL DEFAULT NULL,
  o_si_invest_nonstand varchar(50) NULL DEFAULT NULL,
  o_last_finprd_num varchar(50) NULL DEFAULT NULL,
  o_last_finprd_bal varchar(50) NULL DEFAULT NULL,
  o_lfpir_fz varchar(50) NULL DEFAULT NULL,
  o_lfpir_fm varchar(50) NULL DEFAULT NULL,
  o_badast_deal_mode varchar(200) NULL DEFAULT NULL,
  o_badast_deal_amt varchar(50) NULL DEFAULT NULL,
  remakes varchar(200) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.rpt_mth_cabk_compare_templet (
  area_no_id char(8) NULL DEFAULT NULL,
  fiscal_term char(10) NULL DEFAULT NULL,
  org_id char(14) NULL DEFAULT NULL,
  insur_org_id char(14) NULL DEFAULT NULL,
  insur_org_name varchar(512) NULL DEFAULT NULL,
  subject_code varchar(100) NULL DEFAULT NULL,
  subject_name varchar(100) NULL DEFAULT NULL,
  declare_capital decimal(30, 5) NULL DEFAULT NULL,
  declare_interest decimal(30, 5) NULL DEFAULT NULL,
  buss_capital decimal(30, 5) NULL DEFAULT NULL,
  buss_interest decimal(30, 5) NULL DEFAULT NULL,
  decl_buss_capital_d decimal(30, 5) NULL DEFAULT NULL,
  decl_buss_interest_d decimal(30, 5) NULL DEFAULT NULL,
  sheet_capital decimal(30, 5) NULL DEFAULT NULL,
  decl_sheet_capital_d decimal(30, 5) NULL DEFAULT NULL,
  batch_date timestamp(0) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.rpt_mth_czbk_compare_templet (
  area_no_id char(8) NULL DEFAULT NULL,
  fiscal_term char(10) NULL DEFAULT NULL,
  org_id char(14) NULL DEFAULT NULL,
  insur_org_id char(14) NULL DEFAULT NULL,
  insur_org_name varchar(512) NULL DEFAULT NULL,
  subject_code varchar(100) NULL DEFAULT NULL,
  subject_name varchar(100) NULL DEFAULT NULL,
  declare_capital decimal(30, 5) NULL DEFAULT NULL,
  sheet_capital decimal(30, 5) NULL DEFAULT NULL,
  decl_sheet_capital_d decimal(30, 5) NULL DEFAULT NULL,
  batch_date timestamp(0) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.rpt_mth_dffr_finorg_class_kpi (
  area_no varchar(8) NOT NULL,
  data_date char(10) NOT NULL,
  table_nature varchar(14) NULL DEFAULT NULL,
  org_no char(14) NOT NULL,
  add_date char(8) NULL DEFAULT NULL,
  rows_id varchar(100) NULL DEFAULT NULL,
  sheet_id varchar(100) NULL DEFAULT NULL,
  batch_date char(8) NULL DEFAULT NULL,
  area_no_id char(8) NULL DEFAULT NULL,
  fiscal_term char(10) NULL DEFAULT NULL,
  org_id char(14) NULL DEFAULT NULL,
  org_name varchar(512) NULL DEFAULT NULL,
  org_type_code char(8) NULL DEFAULT NULL,
  org_type_dscr varchar(60) NULL DEFAULT NULL,
  belong_area_code char(8) NULL DEFAULT NULL,
  delong_area_dscr varchar(200) NULL DEFAULT NULL,
  bs_asset varchar(50) NULL DEFAULT NULL,
  bs_loan varchar(50) NULL DEFAULT NULL,
  bs_debt varchar(50) NULL DEFAULT NULL,
  bs_deposit varchar(50) NULL DEFAULT NULL,
  bs_save_deposit varchar(50) NULL DEFAULT NULL,
  bs_owneqty varchar(50) NULL DEFAULT NULL,
  bs_profit_curm varchar(50) NULL DEFAULT NULL,
  bs_addup_profit_cury varchar(50) NULL DEFAULT NULL,
  csl_core_capital_net varchar(50) NULL DEFAULT NULL,
  csl_capital_net varchar(50) NULL DEFAULT NULL,
  csl_weight_risk_ast varchar(50) NULL DEFAULT NULL,
  csl_ccsr_fz varchar(50) NULL DEFAULT NULL,
  csl_ccsr_fm varchar(50) NULL DEFAULT NULL,
  csl_ccsr_res varchar(50) NULL DEFAULT NULL,
  csl_csr_fz varchar(50) NULL DEFAULT NULL,
  csl_csr_fm varchar(50) NULL DEFAULT NULL,
  csl_csr_res varchar(50) NULL DEFAULT NULL,
  csl_cfcsr_new_fz varchar(50) NULL DEFAULT NULL,
  csl_cfcsr_new_fm varchar(50) NULL DEFAULT NULL,
  csl_cfcsr_new_res varchar(50) NULL DEFAULT NULL,
  csl_fcsr_new_fz varchar(50) NULL DEFAULT NULL,
  csl_fcsr_new_fm varchar(50) NULL DEFAULT NULL,
  csl_fcsr_new_res varchar(50) NULL DEFAULT NULL,
  csl_csr_new_fz varchar(50) NULL DEFAULT NULL,
  csl_csr_new_fm varchar(50) NULL DEFAULT NULL,
  csl_csr_new_res varchar(50) NULL DEFAULT NULL,
  csl_lr_fz varchar(50) NULL DEFAULT NULL,
  csl_lr_fm varchar(50) NULL DEFAULT NULL,
  csl_lr_res varchar(50) NULL DEFAULT NULL,
  cr_bad_loan_bal varchar(50) NULL DEFAULT NULL,
  cr_blr_fz varchar(50) NULL DEFAULT NULL,
  cr_blr_fm varchar(50) NULL DEFAULT NULL,
  cr_blr_res varchar(50) NULL DEFAULT NULL,
  cr_loan_reserv_gap varchar(50) NULL DEFAULT NULL,
  cr_llrsr_fz varchar(50) NULL DEFAULT NULL,
  cr_llrsr_fm varchar(50) NULL DEFAULT NULL,
  cr_llrsr_res varchar(50) NULL DEFAULT NULL,
  cr_pcr_fz varchar(50) NULL DEFAULT NULL,
  cr_pcr_fm varchar(50) NULL DEFAULT NULL,
  cr_pcr_res varchar(50) NULL DEFAULT NULL,
  cr_plr_fz varchar(50) NULL DEFAULT NULL,
  cr_plr_fm varchar(50) NULL DEFAULT NULL,
  cr_plr_res varchar(50) NULL DEFAULT NULL,
  cr_bgcr10_fz varchar(50) NULL DEFAULT NULL,
  cr_bgcr10_fm varchar(50) NULL DEFAULT NULL,
  cr_bgcr10_res varchar(50) NULL DEFAULT NULL,
  cr_scr_fz varchar(50) NULL DEFAULT NULL,
  cr_scr_fm varchar(50) NULL DEFAULT NULL,
  cr_scr_res varchar(50) NULL DEFAULT NULL,
  bn_cpr_fz varchar(50) NULL DEFAULT NULL,
  bn_cpr_fm varchar(50) NULL DEFAULT NULL,
  bn_cpr_res varchar(50) NULL DEFAULT NULL,
  bn_apr_fz varchar(50) NULL DEFAULT NULL,
  bn_apr_fm varchar(50) NULL DEFAULT NULL,
  bn_apr_res varchar(50) NULL DEFAULT NULL,
  bn_cir_fz varchar(50) NULL DEFAULT NULL,
  bn_cir_fm varchar(50) NULL DEFAULT NULL,
  bn_cir_res varchar(50) NULL DEFAULT NULL,
  bn_mbir_fz varchar(50) NULL DEFAULT NULL,
  bn_mbir_fm varchar(50) NULL DEFAULT NULL,
  bn_mbir_res varchar(50) NULL DEFAULT NULL,
  f_fr_fz varchar(50) NULL DEFAULT NULL,
  f_fr_fm varchar(50) NULL DEFAULT NULL,
  f_fr_res varchar(50) NULL DEFAULT NULL,
  f_rerr_fz varchar(50) NULL DEFAULT NULL,
  f_rerr_fm varchar(50) NULL DEFAULT NULL,
  f_rerr_res varchar(50) NULL DEFAULT NULL,
  f_dlr_fz varchar(50) NULL DEFAULT NULL,
  f_dlr_fm varchar(50) NULL DEFAULT NULL,
  f_dlr_res varchar(50) NULL DEFAULT NULL,
  f_cdrr_fz varchar(50) NULL DEFAULT NULL,
  f_cdrr_fm varchar(50) NULL DEFAULT NULL,
  f_cdrr_res varchar(50) NULL DEFAULT NULL,
  f_fgr_fz varchar(50) NULL DEFAULT NULL,
  f_fgr_fm varchar(50) NULL DEFAULT NULL,
  f_fgr_res varchar(50) NULL DEFAULT NULL,
  f_fcr_fz varchar(50) NULL DEFAULT NULL,
  f_fcr_fm varchar(50) NULL DEFAULT NULL,
  f_fcr_res varchar(50) NULL DEFAULT NULL,
  PRIMARY KEY (org_no, data_date, area_no)
);

CREATE TABLE IF NOT EXISTS adm.rpt_mth_dffr_finorg_subtotal_kpi (
  area_no varchar(8) NULL DEFAULT NULL,
  fiscal_term char(8) NULL DEFAULT NULL,
  table_nature varchar(50) NULL DEFAULT NULL,
  org_no varchar(14) NULL DEFAULT NULL,
  batch_date char(8) NULL DEFAULT NULL,
  serialno varchar(5) NULL DEFAULT NULL,
  org_type varchar(50) NULL DEFAULT NULL,
  asset_total varchar(50) NULL DEFAULT NULL,
  loan_bal_total varchar(50) NULL DEFAULT NULL,
  debt_total varchar(50) NULL DEFAULT NULL,
  deposit_bal_total varchar(50) NULL DEFAULT NULL,
  saving_deposit varchar(50) NULL DEFAULT NULL,
  own_equity varchar(50) NULL DEFAULT NULL,
  this_month_profit varchar(50) NULL DEFAULT NULL,
  year_profit varchar(50) NULL DEFAULT NULL,
  core_capital_net varchar(50) NULL DEFAULT NULL,
  capital_net varchar(50) NULL DEFAULT NULL,
  weight_risk_asset varchar(50) NULL DEFAULT NULL,
  core_capital_suff varchar(50) NULL DEFAULT NULL,
  capital_suff varchar(50) NULL DEFAULT NULL,
  corg_one_suff varchar(50) NULL DEFAULT NULL,
  one_rate varchar(50) NULL DEFAULT NULL,
  suff_rate_new varchar(50) NULL DEFAULT NULL,
  lev_rate varchar(50) NULL DEFAULT NULL,
  bad_loan_bal varchar(50) NULL DEFAULT NULL,
  bad_loan_rate varchar(50) NULL DEFAULT NULL,
  loan_gap varchar(50) NULL DEFAULT NULL,
  loan_loss_rate varchar(50) NULL DEFAULT NULL,
  provision_cover_rate varchar(50) NULL DEFAULT NULL,
  rate varchar(50) NULL DEFAULT NULL,
  capital_profit_rate varchar(50) NULL DEFAULT NULL,
  asset_profit_rate varchar(50) NULL DEFAULT NULL,
  flow_prop varchar(50) NULL DEFAULT NULL,
  exce_reserve_ratio varchar(50) NULL DEFAULT NULL,
  flow_dep_loan_rare varchar(50) NULL DEFAULT NULL,
  unit varchar(30) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.rpt_mth_dffr_finorg_total_kpi (
  area_no varchar(8) NULL DEFAULT NULL,
  data_date char(10) NULL DEFAULT NULL,
  table_nature varchar(14) NULL DEFAULT NULL,
  org_no char(14) NULL DEFAULT NULL,
  add_date char(8) NULL DEFAULT NULL,
  rows_id varchar(100) NULL DEFAULT NULL,
  sheet_id varchar(100) NULL DEFAULT NULL,
  batch_date char(8) NULL DEFAULT NULL,
  area_no_id char(8) NULL DEFAULT NULL,
  fiscal_term char(10) NULL DEFAULT NULL,
  org_id char(14) NULL DEFAULT NULL,
  org_name varchar(512) NULL DEFAULT NULL,
  org_type_code char(8) NULL DEFAULT NULL,
  org_type_dscr varchar(60) NULL DEFAULT NULL,
  belong_area_code char(8) NULL DEFAULT NULL,
  delong_area_dscr varchar(200) NULL DEFAULT NULL,
  bs_asset varchar(50) NULL DEFAULT NULL,
  bs_loan varchar(50) NULL DEFAULT NULL,
  bs_debt varchar(50) NULL DEFAULT NULL,
  bs_deposit varchar(50) NULL DEFAULT NULL,
  bs_save_deposit varchar(50) NULL DEFAULT NULL,
  bs_owneqty varchar(50) NULL DEFAULT NULL,
  bs_profit_curm varchar(50) NULL DEFAULT NULL,
  bs_addup_profit_cury varchar(50) NULL DEFAULT NULL,
  csl_core_capital_net varchar(50) NULL DEFAULT NULL,
  csl_capital_net varchar(50) NULL DEFAULT NULL,
  csl_weight_risk_ast varchar(50) NULL DEFAULT NULL,
  csl_ccsr_fz varchar(50) NULL DEFAULT NULL,
  csl_ccsr_fm varchar(50) NULL DEFAULT NULL,
  csl_ccsr_res varchar(50) NULL DEFAULT NULL,
  csl_csr_fz varchar(50) NULL DEFAULT NULL,
  csl_csr_fm varchar(50) NULL DEFAULT NULL,
  csl_csr_res varchar(50) NULL DEFAULT NULL,
  csl_cfcsr_new_fz varchar(50) NULL DEFAULT NULL,
  csl_cfcsr_new_fm varchar(50) NULL DEFAULT NULL,
  csl_cfcsr_new_res varchar(50) NULL DEFAULT NULL,
  csl_fcsr_new_fz varchar(50) NULL DEFAULT NULL,
  csl_fcsr_new_fm varchar(50) NULL DEFAULT NULL,
  csl_fcsr_new_res varchar(50) NULL DEFAULT NULL,
  csl_csr_new_fz varchar(50) NULL DEFAULT NULL,
  csl_csr_new_fm varchar(50) NULL DEFAULT NULL,
  csl_csr_new_res varchar(50) NULL DEFAULT NULL,
  csl_lr_fz varchar(50) NULL DEFAULT NULL,
  csl_lr_fm varchar(50) NULL DEFAULT NULL,
  csl_lr_res varchar(50) NULL DEFAULT NULL,
  cr_bad_loan_bal varchar(50) NULL DEFAULT NULL,
  cr_blr_fz varchar(50) NULL DEFAULT NULL,
  cr_blr_fm varchar(50) NULL DEFAULT NULL,
  cr_blr_res varchar(50) NULL DEFAULT NULL,
  cr_loan_reserv_gap varchar(50) NULL DEFAULT NULL,
  cr_llrsr_fz varchar(50) NULL DEFAULT NULL,
  cr_llrsr_fm varchar(50) NULL DEFAULT NULL,
  cr_llrsr_res varchar(50) NULL DEFAULT NULL,
  cr_pcr_fz varchar(50) NULL DEFAULT NULL,
  cr_pcr_fm varchar(50) NULL DEFAULT NULL,
  cr_pcr_res varchar(50) NULL DEFAULT NULL,
  cr_plr_fz varchar(50) NULL DEFAULT NULL,
  cr_plr_fm varchar(50) NULL DEFAULT NULL,
  cr_plr_res varchar(50) NULL DEFAULT NULL,
  bn_cpr_fz varchar(50) NULL DEFAULT NULL,
  bn_cpr_fm varchar(50) NULL DEFAULT NULL,
  bn_cpr_res varchar(50) NULL DEFAULT NULL,
  bn_apr_fz varchar(50) NULL DEFAULT NULL,
  bn_apr_fm varchar(50) NULL DEFAULT NULL,
  bn_apr_res varchar(50) NULL DEFAULT NULL,
  f_fr_fz varchar(50) NULL DEFAULT NULL,
  f_fr_fm varchar(50) NULL DEFAULT NULL,
  f_fr_res varchar(50) NULL DEFAULT NULL,
  f_rerr_fz varchar(50) NULL DEFAULT NULL,
  f_rerr_fm varchar(50) NULL DEFAULT NULL,
  f_rerr_res varchar(50) NULL DEFAULT NULL,
  f_dlr_fz varchar(50) NULL DEFAULT NULL,
  f_dlr_fm varchar(50) NULL DEFAULT NULL,
  f_dlr_res varchar(50) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.rpt_mth_fxsgzck_finorg_monitor (
  area_no_id char(8) NOT NULL,
  fiscal_term char(10) NOT NULL,
  table_nature varchar(14) NULL DEFAULT NULL,
  org_id char(14) NOT NULL,
  batch_date char(8) NULL DEFAULT NULL,
  org_name varchar(512) NULL DEFAULT NULL,
  org_type_code char(8) NULL DEFAULT NULL,
  org_type_dscr varchar(60) NULL DEFAULT NULL,
  bs_asset varchar(50) NULL DEFAULT NULL,
  bs_loan varchar(50) NULL DEFAULT NULL,
  bs_debt varchar(50) NULL DEFAULT NULL,
  bs_deposit varchar(50) NULL DEFAULT NULL,
  bs_save_deposit varchar(50) NULL DEFAULT NULL,
  bs_owneqty varchar(50) NULL DEFAULT NULL,
  bs_profit_curm varchar(50) NULL DEFAULT NULL,
  bs_addup_profit_cury varchar(50) NULL DEFAULT NULL,
  csl_core_capital_net varchar(50) NULL DEFAULT NULL,
  csl_1_capital_net varchar(50) NULL DEFAULT NULL,
  csl_capital_net varchar(50) NULL DEFAULT NULL,
  csl_adj_risk_asset varchar(50) NULL DEFAULT NULL,
  csl_ccsr_fz varchar(50) NULL DEFAULT NULL,
  csl_ccsr_fm varchar(50) NULL DEFAULT NULL,
  csl_ccsr_res varchar(50) NULL DEFAULT NULL,
  csl_csr_fz varchar(50) NULL DEFAULT NULL,
  csl_csr_fm varchar(50) NULL DEFAULT NULL,
  csl_csr_res varchar(50) NULL DEFAULT NULL,
  csl_cfcsr_new_fz varchar(50) NULL DEFAULT NULL,
  csl_cfcsr_new_fm varchar(50) NULL DEFAULT NULL,
  csl_cfcsr_new_res varchar(50) NULL DEFAULT NULL,
  csl_fcsr_new_fz varchar(50) NULL DEFAULT NULL,
  csl_fcsr_new_fm varchar(50) NULL DEFAULT NULL,
  csl_fcsr_new_res varchar(50) NULL DEFAULT NULL,
  csl_csr_new_fz varchar(50) NULL DEFAULT NULL,
  csl_csr_new_fm varchar(50) NULL DEFAULT NULL,
  csl_csr_new_res varchar(50) NULL DEFAULT NULL,
  csl_lr_fz varchar(50) NULL DEFAULT NULL,
  csl_lr_fm varchar(50) NULL DEFAULT NULL,
  csl_lr_res varchar(50) NULL DEFAULT NULL,
  cr_bad_loan_bal varchar(50) NULL DEFAULT NULL,
  cr_blr_fz varchar(50) NULL DEFAULT NULL,
  cr_blr_fm varchar(50) NULL DEFAULT NULL,
  cr_blr_res varchar(50) NULL DEFAULT NULL,
  cr_loan_reserv_bal varchar(50) NULL DEFAULT NULL,
  cr_loan_reserv_gap varchar(50) NULL DEFAULT NULL,
  cr_draw_loan_reserv varchar(50) NULL DEFAULT NULL,
  cr_llrsr_fz varchar(50) NULL DEFAULT NULL,
  cr_llrsr_fm varchar(50) NULL DEFAULT NULL,
  cr_llrsr_res varchar(50) NULL DEFAULT NULL,
  cr_pcr_fz varchar(50) NULL DEFAULT NULL,
  cr_pcr_fm varchar(50) NULL DEFAULT NULL,
  cr_pcr_res varchar(50) NULL DEFAULT NULL,
  cr_plr_fz varchar(50) NULL DEFAULT NULL,
  cr_plr_fm varchar(50) NULL DEFAULT NULL,
  cr_plr_res varchar(50) NULL DEFAULT NULL,
  cr_bgrp10_cred_net varchar(50) NULL DEFAULT NULL,
  cr_bgrp_cred_net varchar(50) NULL DEFAULT NULL,
  cr_bgcr10_fz varchar(50) NULL DEFAULT NULL,
  cr_bgcr10_fm varchar(50) NULL DEFAULT NULL,
  cr_bgcr10_res varchar(50) NULL DEFAULT NULL,
  cr_bgcr_fz varchar(50) NULL DEFAULT NULL,
  cr_bgcr_fm varchar(50) NULL DEFAULT NULL,
  cr_bgcr_res varchar(50) NULL DEFAULT NULL,
  bn_cpr_fz varchar(50) NULL DEFAULT NULL,
  bn_cpr_fm varchar(50) NULL DEFAULT NULL,
  bn_cpr_res varchar(50) NULL DEFAULT NULL,
  bn_apr_fz varchar(50) NULL DEFAULT NULL,
  bn_apr_fm varchar(50) NULL DEFAULT NULL,
  bn_apr_res varchar(50) NULL DEFAULT NULL,
  bn_operate_expend varchar(50) NULL DEFAULT NULL,
  bn_operate_revenue varchar(50) NULL DEFAULT NULL,
  bn_tax_addi varchar(50) NULL DEFAULT NULL,
  bn_mid_buss_income varchar(50) NULL DEFAULT NULL,
  bn_cir_fz varchar(50) NULL DEFAULT NULL,
  bn_cir_fm varchar(50) NULL DEFAULT NULL,
  bn_cir_res varchar(50) NULL DEFAULT NULL,
  bn_mbir_fz varchar(50) NULL DEFAULT NULL,
  bn_mbir_fm varchar(50) NULL DEFAULT NULL,
  bn_mbir_res varchar(50) NULL DEFAULT NULL,
  f_flow_asset varchar(50) NULL DEFAULT NULL,
  f_flow_debt varchar(50) NULL DEFAULT NULL,
  f_fr_fz varchar(50) NULL DEFAULT NULL,
  f_fr_fm varchar(50) NULL DEFAULT NULL,
  f_fr_res varchar(50) NULL DEFAULT NULL,
  f_exce_reserve varchar(50) NULL DEFAULT NULL,
  f_cash varchar(50) NULL DEFAULT NULL,
  f_rerr_fz varchar(50) NULL DEFAULT NULL,
  f_rerr_fm varchar(50) NULL DEFAULT NULL,
  f_rerr_res varchar(50) NULL DEFAULT NULL,
  f_dlr_fz varchar(50) NULL DEFAULT NULL,
  f_dlr_fm varchar(50) NULL DEFAULT NULL,
  f_dlr_res varchar(50) NULL DEFAULT NULL,
  f_cdrr_fz varchar(50) NULL DEFAULT NULL,
  f_cdrr_fm varchar(50) NULL DEFAULT NULL,
  f_cdrr_res varchar(50) NULL DEFAULT NULL,
  f_fgr_fz varchar(50) NULL DEFAULT NULL,
  f_fgr_fm varchar(50) NULL DEFAULT NULL,
  f_fgr_res varchar(50) NULL DEFAULT NULL,
  f_fcr_fz varchar(50) NULL DEFAULT NULL,
  f_fcr_fm varchar(50) NULL DEFAULT NULL,
  f_fcr_res varchar(50) NULL DEFAULT NULL,
  PRIMARY KEY (org_id, fiscal_term, area_no_id)
);

CREATE TABLE IF NOT EXISTS adm.rpt_mth_fxsgzck_finorg_monitor_20180816 (
  area_no varchar(8) NULL DEFAULT NULL,
  data_date char(10) NULL DEFAULT NULL,
  table_nature varchar(14) NULL DEFAULT NULL,
  org_no char(14) NULL DEFAULT NULL,
  add_date char(8) NULL DEFAULT NULL,
  rows_id varchar(100) NULL DEFAULT NULL,
  sheet_id varchar(100) NULL DEFAULT NULL,
  batch_date char(8) NULL DEFAULT NULL,
  area_no_id char(8) NULL DEFAULT NULL,
  fiscal_term char(10) NULL DEFAULT NULL,
  org_id char(14) NULL DEFAULT NULL,
  org_name varchar(512) NULL DEFAULT NULL,
  org_type_code char(8) NULL DEFAULT NULL,
  org_type_dscr varchar(60) NULL DEFAULT NULL,
  bs_asset varchar(50) NULL DEFAULT NULL,
  bs_loan varchar(50) NULL DEFAULT NULL,
  bs_debt varchar(50) NULL DEFAULT NULL,
  bs_deposit varchar(50) NULL DEFAULT NULL,
  bs_save_deposit varchar(50) NULL DEFAULT NULL,
  bs_owneqty varchar(50) NULL DEFAULT NULL,
  bs_profit_curm varchar(50) NULL DEFAULT NULL,
  bs_addup_profit_cury varchar(50) NULL DEFAULT NULL,
  csl_core_capital_net varchar(50) NULL DEFAULT NULL,
  csl_1_capital_net varchar(50) NULL DEFAULT NULL,
  csl_capital_net varchar(50) NULL DEFAULT NULL,
  csl_adj_risk_asset varchar(50) NULL DEFAULT NULL,
  csl_ccsr_fz varchar(50) NULL DEFAULT NULL,
  csl_ccsr_fm varchar(50) NULL DEFAULT NULL,
  csl_ccsr_res varchar(50) NULL DEFAULT NULL,
  csl_csr_fz varchar(50) NULL DEFAULT NULL,
  csl_csr_fm varchar(50) NULL DEFAULT NULL,
  csl_csr_res varchar(50) NULL DEFAULT NULL,
  csl_cfcsr_new_fz varchar(50) NULL DEFAULT NULL,
  csl_cfcsr_new_fm varchar(50) NULL DEFAULT NULL,
  csl_cfcsr_new_res varchar(50) NULL DEFAULT NULL,
  csl_fcsr_new_fz varchar(50) NULL DEFAULT NULL,
  csl_fcsr_new_fm varchar(50) NULL DEFAULT NULL,
  csl_fcsr_new_res varchar(50) NULL DEFAULT NULL,
  csl_csr_new_fz varchar(50) NULL DEFAULT NULL,
  csl_csr_new_fm varchar(50) NULL DEFAULT NULL,
  csl_csr_new_res varchar(50) NULL DEFAULT NULL,
  csl_lr_fz varchar(50) NULL DEFAULT NULL,
  csl_lr_fm varchar(50) NULL DEFAULT NULL,
  csl_lr_res varchar(50) NULL DEFAULT NULL,
  cr_bad_loan_bal varchar(50) NULL DEFAULT NULL,
  cr_blr_fz varchar(50) NULL DEFAULT NULL,
  cr_blr_fm varchar(50) NULL DEFAULT NULL,
  cr_blr_res varchar(50) NULL DEFAULT NULL,
  cr_loan_reserv_bal varchar(50) NULL DEFAULT NULL,
  cr_loan_reserv_gap varchar(50) NULL DEFAULT NULL,
  cr_draw_loan_reserv varchar(50) NULL DEFAULT NULL,
  cr_llrsr_fz varchar(50) NULL DEFAULT NULL,
  cr_llrsr_fm varchar(50) NULL DEFAULT NULL,
  cr_llrsr_res varchar(50) NULL DEFAULT NULL,
  cr_pcr_fz varchar(50) NULL DEFAULT NULL,
  cr_pcr_fm varchar(50) NULL DEFAULT NULL,
  cr_pcr_res varchar(50) NULL DEFAULT NULL,
  cr_plr_fz varchar(50) NULL DEFAULT NULL,
  cr_plr_fm varchar(50) NULL DEFAULT NULL,
  cr_plr_res varchar(50) NULL DEFAULT NULL,
  cr_bgrp10_cred_net varchar(50) NULL DEFAULT NULL,
  cr_bgrp_cred_net varchar(50) NULL DEFAULT NULL,
  cr_bgcr10_fz varchar(50) NULL DEFAULT NULL,
  cr_bgcr10_fm varchar(50) NULL DEFAULT NULL,
  cr_bgcr10_res varchar(50) NULL DEFAULT NULL,
  cr_bgcr_fz varchar(50) NULL DEFAULT NULL,
  cr_bgcr_fm varchar(50) NULL DEFAULT NULL,
  cr_bgcr_res varchar(50) NULL DEFAULT NULL,
  bn_cpr_fz varchar(50) NULL DEFAULT NULL,
  bn_cpr_fm varchar(50) NULL DEFAULT NULL,
  bn_cpr_res varchar(50) NULL DEFAULT NULL,
  bn_apr_fz varchar(50) NULL DEFAULT NULL,
  bn_apr_fm varchar(50) NULL DEFAULT NULL,
  bn_apr_res varchar(50) NULL DEFAULT NULL,
  bn_operate_expend varchar(50) NULL DEFAULT NULL,
  bn_operate_revenue varchar(50) NULL DEFAULT NULL,
  bn_tax_addi varchar(50) NULL DEFAULT NULL,
  bn_mid_buss_income varchar(50) NULL DEFAULT NULL,
  bn_cir_fz varchar(50) NULL DEFAULT NULL,
  bn_cir_fm varchar(50) NULL DEFAULT NULL,
  bn_cir_res varchar(50) NULL DEFAULT NULL,
  bn_mbir_fz varchar(50) NULL DEFAULT NULL,
  bn_mbir_fm varchar(50) NULL DEFAULT NULL,
  bn_mbir_res varchar(50) NULL DEFAULT NULL,
  f_flow_asset varchar(50) NULL DEFAULT NULL,
  f_flow_debt varchar(50) NULL DEFAULT NULL,
  f_fr_fz varchar(50) NULL DEFAULT NULL,
  f_fr_fm varchar(50) NULL DEFAULT NULL,
  f_fr_res varchar(50) NULL DEFAULT NULL,
  f_exce_reserve varchar(50) NULL DEFAULT NULL,
  f_cash varchar(50) NULL DEFAULT NULL,
  f_rerr_fz varchar(50) NULL DEFAULT NULL,
  f_rerr_fm varchar(50) NULL DEFAULT NULL,
  f_rerr_res varchar(50) NULL DEFAULT NULL,
  f_dlr_fz varchar(50) NULL DEFAULT NULL,
  f_dlr_fm varchar(50) NULL DEFAULT NULL,
  f_dlr_res varchar(50) NULL DEFAULT NULL,
  f_cdrr_fz varchar(50) NULL DEFAULT NULL,
  f_cdrr_fm varchar(50) NULL DEFAULT NULL,
  f_cdrr_res varchar(50) NULL DEFAULT NULL,
  f_fgr_fz varchar(50) NULL DEFAULT NULL,
  f_fgr_fm varchar(50) NULL DEFAULT NULL,
  f_fgr_res varchar(50) NULL DEFAULT NULL,
  f_fcr_fz varchar(50) NULL DEFAULT NULL,
  f_fcr_fm varchar(50) NULL DEFAULT NULL,
  f_fcr_res varchar(50) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.rpt_mth_high_risk_bank_org (
  area_no varchar(8) NOT NULL,
  data_date char(10) NOT NULL,
  table_nature varchar(14) NULL DEFAULT NULL,
  org_no char(14) NOT NULL,
  batch_date timestamp(0) NULL DEFAULT NULL,
  org_id char(14) NULL DEFAULT NULL,
  org_name varchar(512) NULL DEFAULT NULL,
  insur_org_type_code char(8) NULL DEFAULT NULL,
  insur_org_type_dscr varchar(60) NULL DEFAULT NULL,
  belong_area_dscr varchar(200) NULL DEFAULT NULL,
  aq_blr_fz varchar(50) NULL DEFAULT NULL,
  aq_blr_fm varchar(50) NULL DEFAULT NULL,
  aq_blr_res varchar(50) NULL DEFAULT NULL,
  aq_pcr_fz varchar(50) NULL DEFAULT NULL,
  aq_pcr_fm varchar(50) NULL DEFAULT NULL,
  aq_pcr_res varchar(50) NULL DEFAULT NULL,
  f_dlr_fz varchar(50) NULL DEFAULT NULL,
  f_dlr_fm varchar(50) NULL DEFAULT NULL,
  f_dlr_res varchar(50) NULL DEFAULT NULL,
  f_fr_fz varchar(50) NULL DEFAULT NULL,
  f_fr_fm varchar(50) NULL DEFAULT NULL,
  f_fr_res varchar(50) NULL DEFAULT NULL,
  l_scr_fz varchar(50) NULL DEFAULT NULL,
  l_scr_fm varchar(50) NULL DEFAULT NULL,
  l_scr_res varchar(50) NULL DEFAULT NULL,
  l_bgcr10_fz varchar(50) NULL DEFAULT NULL,
  l_bgcr10_fm varchar(50) NULL DEFAULT NULL,
  l_bgcr10_res varchar(50) NULL DEFAULT NULL,
  as_ccsr_fz varchar(50) NULL DEFAULT NULL,
  as_ccsr_fm varchar(50) NULL DEFAULT NULL,
  as_ccsr_res varchar(50) NULL DEFAULT NULL,
  as_csr_fz varchar(50) NULL DEFAULT NULL,
  as_csr_fm varchar(50) NULL DEFAULT NULL,
  as_csr_res varchar(50) NULL DEFAULT NULL,
  PRIMARY KEY (org_no, data_date, area_no)
);

CREATE TABLE IF NOT EXISTS adm.rpt_mth_insur_declare_attach (
  serialno varchar(50) NULL DEFAULT NULL,
  area_no_id char(8) NULL DEFAULT NULL,
  fiscal_term char(10) NULL DEFAULT NULL,
  org_id char(14) NULL DEFAULT NULL,
  insur_org_id char(14) NULL DEFAULT NULL,
  item_type varchar(200) NULL DEFAULT NULL,
  item varchar(500) NULL DEFAULT NULL,
  item_value varchar(100) NULL DEFAULT NULL,
  unit varchar(100) NULL DEFAULT NULL,
  batch_date timestamp(0) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.rpt_mth_insur_org_info (
  area_no_id char(8) NULL DEFAULT NULL,
  fiscal_term char(10) NULL DEFAULT NULL,
  org_id char(14) NULL DEFAULT NULL,
  insur_org_id char(14) NULL DEFAULT NULL,
  insur_org_name varchar(512) NULL DEFAULT NULL,
  table_nature char(50) NULL DEFAULT NULL,
  insur_org_type_dscr varchar(60) NULL DEFAULT NULL,
  build_date varchar(100) NULL DEFAULT NULL,
  regist_funds decimal(30, 5) NULL DEFAULT NULL,
  regist_addr varchar(200) NULL DEFAULT NULL,
  manage_head_addr varchar(200) NULL DEFAULT NULL,
  legal_leader varchar(200) NULL DEFAULT NULL,
  assets_scale decimal(30, 5) NULL DEFAULT NULL,
  domestic_website_num int NULL DEFAULT NULL,
  employees_num int NULL DEFAULT NULL,
  ori_insure_org_code varchar(200) NULL DEFAULT NULL,
  ori_insure_org_name varchar(200) NULL DEFAULT NULL,
  ori_org_type_dscr varchar(100) NULL DEFAULT NULL,
  is_cur_neworg varchar(20) NULL DEFAULT NULL,
  is_cur_changeorg varchar(20) NULL DEFAULT NULL,
  is_cur_merge varchar(20) NULL DEFAULT NULL,
  is_cur_merge_end varchar(20) NULL DEFAULT NULL,
  is_cur_change_end varchar(20) NULL DEFAULT NULL,
  principal_name varchar(100) NULL DEFAULT NULL,
  principal_tel_phone varchar(100) NULL DEFAULT NULL,
  liaison_name varchar(100) NULL DEFAULT NULL,
  liaison_tel_phone varchar(100) NULL DEFAULT NULL,
  contact_name varchar(100) NULL DEFAULT NULL,
  contact_tel_phone varchar(100) NULL DEFAULT NULL,
  first_holder_name varchar(200) NULL DEFAULT NULL,
  first_holder_fund decimal(30, 5) NULL DEFAULT NULL,
  first_holder_prop decimal(30, 5) NULL DEFAULT NULL,
  second_holder_name varchar(200) NULL DEFAULT NULL,
  second_holder_fund decimal(30, 5) NULL DEFAULT NULL,
  second_holder_prop decimal(30, 5) NULL DEFAULT NULL,
  third_holder_name varchar(200) NULL DEFAULT NULL,
  third_holder_fund decimal(30, 5) NULL DEFAULT NULL,
  third_holder_prop decimal(30, 5) NULL DEFAULT NULL,
  fourth_holder_name varchar(200) NULL DEFAULT NULL,
  fourth_holder_fund decimal(30, 5) NULL DEFAULT NULL,
  fourth_holder_prop decimal(30, 5) NULL DEFAULT NULL,
  fifth_holder_name varchar(200) NULL DEFAULT NULL,
  fifth_holder_fund decimal(30, 5) NULL DEFAULT NULL,
  fifth_holder_prop decimal(30, 5) NULL DEFAULT NULL,
  sixth_holder_name varchar(200) NULL DEFAULT NULL,
  sixth_holder_fund decimal(30, 5) NULL DEFAULT NULL,
  sixth_holder_prop decimal(30, 5) NULL DEFAULT NULL,
  seventh_holder_name varchar(200) NULL DEFAULT NULL,
  seventh_holder_fund decimal(30, 5) NULL DEFAULT NULL,
  seventh_holder_prop decimal(30, 5) NULL DEFAULT NULL,
  eighth_holder_name varchar(200) NULL DEFAULT NULL,
  eighth_holder_fund decimal(30, 5) NULL DEFAULT NULL,
  eighth_holder_prop decimal(30, 5) NULL DEFAULT NULL,
  ninth_holderd_name varchar(200) NULL DEFAULT NULL,
  ninth_holder_fund decimal(30, 5) NULL DEFAULT NULL,
  ninth_holder_prop decimal(30, 5) NULL DEFAULT NULL,
  tenth_holder_name varchar(200) NULL DEFAULT NULL,
  tenth_holder_fund decimal(30, 5) NULL DEFAULT NULL,
  tenth_holder_prop decimal(30, 5) NULL DEFAULT NULL,
  batch_date timestamp(0) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.rpt_mth_insur_org_oper_monitor (
  area_no varchar(8) NOT NULL,
  data_date char(10) NOT NULL,
  table_nature varchar(14) NULL DEFAULT NULL,
  org_no char(14) NOT NULL,
  add_date char(8) NOT NULL,
  rows_id varchar(100) NULL DEFAULT NULL,
  sheet_id varchar(100) NULL DEFAULT NULL,
  batch_date char(8) NULL DEFAULT NULL,
  area_no_id char(8) NULL DEFAULT NULL,
  fiscal_term char(10) NULL DEFAULT NULL,
  org_id char(14) NULL DEFAULT NULL,
  insur_org_id char(14) NULL DEFAULT NULL,
  insur_org_name varchar(512) NULL DEFAULT NULL,
  insur_org_type_code char(8) NULL DEFAULT NULL,
  insur_org_type_dscr varchar(60) NULL DEFAULT NULL,
  belong_area_code char(8) NULL DEFAULT NULL,
  delong_area_dscr varchar(200) NULL DEFAULT NULL,
  ad_asset_yi varchar(50) NULL DEFAULT NULL,
  ad_asset varchar(50) NULL DEFAULT NULL,
  ad_loan_bal varchar(50) NULL DEFAULT NULL,
  ad_debt varchar(50) NULL DEFAULT NULL,
  ad_dept_bal varchar(50) NULL DEFAULT NULL,
  ad_save_dept varchar(50) NULL DEFAULT NULL,
  struct_deposit_bal varchar(50) NULL DEFAULT NULL,
  ncds_total_assets varchar(50) NULL DEFAULT NULL,
  large_sin_interbank_rate varchar(50) NULL DEFAULT NULL,
  ad_owneqty_yi varchar(50) NULL DEFAULT NULL,
  ad_owneqty varchar(50) NULL DEFAULT NULL,
  ad_few_owneqty_yi varchar(50) NULL DEFAULT NULL,
  ad_few_owneqty varchar(50) NULL DEFAULT NULL,
  as_capital_net varchar(50) NULL DEFAULT NULL,
  as_1_capital_net varchar(50) NULL DEFAULT NULL,
  as_core_capital_net varchar(50) NULL DEFAULT NULL,
  as_adj_risk_asset varchar(50) NULL DEFAULT NULL,
  as_csr_fz varchar(50) NULL DEFAULT NULL,
  as_csr_fm varchar(50) NULL DEFAULT NULL,
  as_csr_res varchar(50) NULL DEFAULT NULL,
  as_fcsr_fz varchar(50) NULL DEFAULT NULL,
  as_fcsr_fm varchar(50) NULL DEFAULT NULL,
  as_fcsr_fm_res varchar(50) NULL DEFAULT NULL,
  as_cfcsr_fz varchar(50) NULL DEFAULT NULL,
  as_cfcsr_fm varchar(50) NULL DEFAULT NULL,
  as_cfcsr_fm_res varchar(50) NULL DEFAULT NULL,
  as_adj_sheet_asset varchar(50) NULL DEFAULT NULL,
  as_lr_fz varchar(50) NULL DEFAULT NULL,
  as_lr_fm varchar(50) NULL DEFAULT NULL,
  as_lr_fm_res varchar(50) NULL DEFAULT NULL,
  pa_margin varchar(50) NULL DEFAULT NULL,
  pa_net_margin varchar(50) NULL DEFAULT NULL,
  pa_margin_curm varchar(50) NULL DEFAULT NULL,
  pa_few_owngl varchar(50) NULL DEFAULT NULL,
  pa_jxc_fz varchar(50) NULL DEFAULT NULL,
  pa_jxc_fm varchar(50) NULL DEFAULT NULL,
  pa_jxc_fm_res varchar(50) NULL DEFAULT NULL,
  pa_cpr_fz varchar(50) NULL DEFAULT NULL,
  pa_cpr_fm varchar(50) NULL DEFAULT NULL,
  pa_cpr_fm_res varchar(50) NULL DEFAULT NULL,
  pa_apr_fz varchar(50) NULL DEFAULT NULL,
  pa_apr_fm varchar(50) NULL DEFAULT NULL,
  pa_apr_fm_res varchar(50) NULL DEFAULT NULL,
  pa_inte_income_total varchar(50) NULL DEFAULT NULL,
  pa_loan_inte_income varchar(50) NULL DEFAULT NULL,
  pa_inte_cost_total varchar(50) NULL DEFAULT NULL,
  pa_loan_inte_cost varchar(50) NULL DEFAULT NULL,
  pa_ginteast_avg_bal varchar(50) NULL DEFAULT NULL,
  pa_ginteast_avgr_fz varchar(50) NULL DEFAULT NULL,
  pa_ginteast_avgr_fm varchar(50) NULL DEFAULT NULL,
  pa_ginteast_avgr_fm_res varchar(50) NULL DEFAULT NULL,
  pa_cinteast_avg_bal varchar(50) NULL DEFAULT NULL,
  pa_cinteast_avgr_fz varchar(50) NULL DEFAULT NULL,
  pa_cinteast_avgr_fm_res varchar(50) NULL DEFAULT NULL,
  pa_cinteast_avgr_fm varchar(50) NULL DEFAULT NULL,
  pa_comm_net_income varchar(50) NULL DEFAULT NULL,
  pa_yield varchar(50) NULL DEFAULT NULL,
  pa_fair_chang_income varchar(50) NULL DEFAULT NULL,
  pa_exchange_income varchar(50) NULL DEFAULT NULL,
  pa_other_buss_income varchar(50) NULL DEFAULT NULL,
  pa_mid_buss_income varchar(50) NULL DEFAULT NULL,
  pa_operate_expend varchar(50) NULL DEFAULT NULL,
  pa_tax_addi varchar(50) NULL DEFAULT NULL,
  pa_cir_fz varchar(50) NULL DEFAULT NULL,
  pa_cir_fm varchar(50) NULL DEFAULT NULL,
  pa_cir_fm_res varchar(50) NULL DEFAULT NULL,
  aqp_normal_loan_bal varchar(50) NULL DEFAULT NULL,
  aqp_follow_loan_bal varchar(50) NULL DEFAULT NULL,
  aqp_bad_loan_bal varchar(50) NULL DEFAULT NULL,
  aqp_second_loan_bal varchar(50) NULL DEFAULT NULL,
  aqp_dubious_loan_bal varchar(50) NULL DEFAULT NULL,
  aqp_loss_loan_bal varchar(50) NULL DEFAULT NULL,
  aqp_overdue_loan_bal varchar(50) NULL DEFAULT NULL,
  aqp_overdue90_bal varchar(50) NULL DEFAULT NULL,
  aqp_overdue90_fz varchar(50) NULL DEFAULT NULL,
  aqp_overdue90_fm varchar(50) NULL DEFAULT NULL,
  aqp_overdue90_fm_res varchar(50) NULL DEFAULT NULL,
  aqp_blr_fz varchar(50) NULL DEFAULT NULL,
  aqp_blr_fm varchar(50) NULL DEFAULT NULL,
  aqp_blr_fm_res varchar(50) NULL DEFAULT NULL,
  aqp_bad_credrisk_ast varchar(50) NULL DEFAULT NULL,
  aqp_credrisk_ast varchar(50) NULL DEFAULT NULL,
  aqp_bsr_fz varchar(50) NULL DEFAULT NULL,
  aqp_bsr_fm varchar(50) NULL DEFAULT NULL,
  aqp_bsr_fm_res varchar(50) NULL DEFAULT NULL,
  aqp_loan_reserv_bal varchar(50) NULL DEFAULT NULL,
  aqp_loan_reserv_gap varchar(50) NULL DEFAULT NULL,
  aqp_lpr_fz varchar(50) NULL DEFAULT NULL,
  aqp_lpr_fm varchar(50) NULL DEFAULT NULL,
  aqp_lpr_fm_res varchar(50) NULL DEFAULT NULL,
  aqp_pcr_fz varchar(50) NULL DEFAULT NULL,
  aqp_pcr_fm varchar(50) NULL DEFAULT NULL,
  aqp_pcr_fm_res varchar(50) NULL DEFAULT NULL,
  aqp_bgrp_cred_net varchar(50) NULL DEFAULT NULL,
  aqp_bgcr_fz varchar(50) NULL DEFAULT NULL,
  aqp_bgcr_fm varchar(50) NULL DEFAULT NULL,
  aqp_bgcr_fm_res varchar(50) NULL DEFAULT NULL,
  aqp_big_loan_amt varchar(50) NULL DEFAULT NULL,
  aqp_bslr_fz varchar(50) NULL DEFAULT NULL,
  aqp_bslr_fm varchar(50) NULL DEFAULT NULL,
  aqp_bslr_fm_res varchar(50) NULL DEFAULT NULL,
  aqp_bgrp10_cred_net varchar(50) NULL DEFAULT NULL,
  aqp_bgcr10_fz varchar(50) NULL DEFAULT NULL,
  aqp_bgcr10_fm varchar(50) NULL DEFAULT NULL,
  aqp_bgcr10_fm_res varchar(50) NULL DEFAULT NULL,
  aqp_rela_cred_amt varchar(50) NULL DEFAULT NULL,
  aqp_arr_fz varchar(50) NULL DEFAULT NULL,
  aqp_arr_fm varchar(50) NULL DEFAULT NULL,
  aqp_arr_fm_res varchar(50) NULL DEFAULT NULL,
  f_flow_asset varchar(50) NULL DEFAULT NULL,
  f_flow_debt varchar(50) NULL DEFAULT NULL,
  f_fr_fz varchar(50) NULL DEFAULT NULL,
  f_fr_fm varchar(50) NULL DEFAULT NULL,
  f_fr_fm_res varchar(50) NULL DEFAULT NULL,
  f_ndt_cash_out varchar(50) NULL DEFAULT NULL,
  f_qual_flow_asset varchar(50) NULL DEFAULT NULL,
  f_fcr_fz varchar(50) NULL DEFAULT NULL,
  f_fcr_fm varchar(50) NULL DEFAULT NULL,
  f_fcr_fm_res varchar(50) NULL DEFAULT NULL,
  f_flow_gap varchar(50) NULL DEFAULT NULL,
  f_term90_sheet_asset varchar(50) NULL DEFAULT NULL,
  f_fgr_fz varchar(50) NULL DEFAULT NULL,
  f_fgr_fm varchar(50) NULL DEFAULT NULL,
  f_fgr_fm_res varchar(50) NULL DEFAULT NULL,
  f_core_debt varchar(50) NULL DEFAULT NULL,
  f_cdrr_fz varchar(50) NULL DEFAULT NULL,
  f_cdrr_fm varchar(50) NULL DEFAULT NULL,
  f_cdrr_fm_res varchar(50) NULL DEFAULT NULL,
  f_rerr_fz varchar(50) NULL DEFAULT NULL,
  f_liqui_res varchar(50) NULL DEFAULT NULL,
  f_high_res varchar(50) NULL DEFAULT NULL,
  f_rerr_fm varchar(50) NULL DEFAULT NULL,
  f_rerr_fm_res varchar(50) NULL DEFAULT NULL,
  f_exce_reserve varchar(50) NULL DEFAULT NULL,
  f_cash varchar(50) NULL DEFAULT NULL,
  f_dlr_fz varchar(50) NULL DEFAULT NULL,
  f_dlr_fm varchar(50) NULL DEFAULT NULL,
  f_dlr_fm_res varchar(50) NULL DEFAULT NULL,
  f_supervise_level varchar(50) NULL DEFAULT NULL,
  f_dept_insur_level varchar(50) NULL DEFAULT NULL,
  PRIMARY KEY (org_no, data_date, area_no)
);

CREATE TABLE IF NOT EXISTS adm.rpt_mth_insur_tbjg_list (
  area_no varchar(8) NULL DEFAULT NULL,
  fiscal_term char(8) NULL DEFAULT NULL,
  table_nature varchar(50) NULL DEFAULT NULL,
  org_no varchar(14) NULL DEFAULT NULL,
  batch_date char(8) NULL DEFAULT NULL,
  serialno char(20) NULL DEFAULT NULL,
  city_name varchar(100) NULL DEFAULT NULL,
  org_norm_allname varchar(100) NULL DEFAULT NULL,
  org_type_id varchar(10) NULL DEFAULT NULL,
  org_type_name varchar(100) NULL DEFAULT NULL,
  org_code varchar(100) NULL DEFAULT NULL,
  whether_brnterm_neworg varchar(100) NULL DEFAULT NULL,
  whether_brnterm_chg_neworg varchar(100) NULL DEFAULT NULL,
  ori_insureorg_allname varchar(200) NULL DEFAULT NULL,
  original_org_code varchar(200) NULL DEFAULT NULL,
  original_org_type varchar(100) NULL DEFAULT NULL,
  whether_benterm_merge_org varchar(100) NULL DEFAULT NULL,
  whether_thisterm_merge_end_org varchar(100) NULL DEFAULT NULL,
  whether_thisterm_change_end_org varchar(100) NULL DEFAULT NULL,
  change_new_merge_org_date varchar(100) NULL DEFAULT NULL,
  intend_opened_date varchar(100) NULL DEFAULT NULL,
  whether_insured varchar(100) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.rpt_mth_kpiover_org_num_prop (
  fiscal_term char(10) NULL DEFAULT NULL,
  item char(200) NULL DEFAULT NULL,
  cs_overproof_num int NULL DEFAULT NULL,
  cs_num_res varchar(50) NULL DEFAULT NULL,
  ns_overproof_num int NULL DEFAULT NULL,
  ns_num_res varchar(50) NULL DEFAULT NULL,
  nh_overproof_num int NULL DEFAULT NULL,
  nh_num_res varchar(50) NULL DEFAULT NULL,
  nx_overproof_num int NULL DEFAULT NULL,
  nx_num_res varchar(50) NULL DEFAULT NULL,
  cz_overproof_num int NULL DEFAULT NULL,
  cz_num_res varchar(50) NULL DEFAULT NULL,
  frjg_overproof_num int NULL DEFAULT NULL,
  frjg_num_res varchar(50) NULL DEFAULT NULL,
  batch_date char(8) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.rpt_mth_qnbk_compare_templet (
  area_no_id char(8) NULL DEFAULT NULL,
  fiscal_term char(10) NULL DEFAULT NULL,
  org_id char(14) NULL DEFAULT NULL,
  insur_org_id char(14) NULL DEFAULT NULL,
  insur_org_name varchar(512) NULL DEFAULT NULL,
  subject_code varchar(100) NULL DEFAULT NULL,
  subject_name varchar(100) NULL DEFAULT NULL,
  declare_capital decimal(30, 5) NULL DEFAULT NULL,
  buss_capital decimal(30, 5) NULL DEFAULT NULL,
  decl_buss_capital_d decimal(30, 5) NULL DEFAULT NULL,
  sheet_capital decimal(30, 5) NULL DEFAULT NULL,
  decl_sheet_capital_d decimal(30, 5) NULL DEFAULT NULL,
  batch_date timestamp(0) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.rpt_mth_qtnchz_compare_templet (
  area_no_id char(8) NULL DEFAULT NULL,
  fiscal_term char(10) NULL DEFAULT NULL,
  org_id char(14) NULL DEFAULT NULL,
  insur_org_id char(14) NULL DEFAULT NULL,
  insur_org_name varchar(512) NULL DEFAULT NULL,
  subject_code varchar(100) NULL DEFAULT NULL,
  subject_name varchar(100) NULL DEFAULT NULL,
  declare_capital decimal(30, 5) NULL DEFAULT NULL,
  buss_capital decimal(30, 5) NULL DEFAULT NULL,
  decl_buss_capital_d decimal(30, 5) NULL DEFAULT NULL,
  sheet_capital decimal(30, 5) NULL DEFAULT NULL,
  decl_sheet_capital_d decimal(30, 5) NULL DEFAULT NULL,
  batch_date timestamp(0) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.rpt_mth_xabk_compare_templet (
  area_no_id char(8) NULL DEFAULT NULL,
  fiscal_term char(10) NULL DEFAULT NULL,
  org_id char(14) NULL DEFAULT NULL,
  insur_org_id char(14) NULL DEFAULT NULL,
  insur_org_name varchar(512) NULL DEFAULT NULL,
  subject_code varchar(100) NULL DEFAULT NULL,
  subject_name varchar(100) NULL DEFAULT NULL,
  declare_capital decimal(30, 5) NULL DEFAULT NULL,
  declare_interest decimal(30, 5) NULL DEFAULT NULL,
  buss_capital decimal(30, 5) NULL DEFAULT NULL,
  buss_interest decimal(30, 5) NULL DEFAULT NULL,
  decl_buss_capital_d decimal(30, 5) NULL DEFAULT NULL,
  decl_buss_interest_d decimal(30, 5) NULL DEFAULT NULL,
  sheet_capital decimal(30, 5) NULL DEFAULT NULL,
  decl_sheet_capital_d decimal(30, 5) NULL DEFAULT NULL,
  batch_date timestamp(0) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.rpt_q_farm_loan_all (
  batch_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  area_no varchar(8) NULL DEFAULT NULL,
  data_date char(8) NULL DEFAULT NULL,
  org_no varchar(53) NULL DEFAULT NULL,
  kpi_code varchar(255) NULL DEFAULT NULL,
  kpi_name varchar(255) NULL DEFAULT NULL,
  loan_balance decimal(30, 5) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.rpt_q_farm_loan_org (
  batch_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  area_no varchar(8) NULL DEFAULT NULL,
  data_date char(8) NULL DEFAULT NULL,
  org_no varchar(53) NULL DEFAULT NULL,
  org_desc varchar(100) NULL DEFAULT NULL,
  project varchar(30) NULL DEFAULT NULL,
  project_order int NULL DEFAULT NULL,
  loan_bal decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_b decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_add_bgy decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_pb decimal(30, 5) NULL DEFAULT NULL,
  loan_bal_tb decimal(30, 5) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.rpt_qtr_asset_quality_class (
  area_no varchar(8) NULL DEFAULT NULL,
  data_date char(10) NULL DEFAULT NULL,
  table_nature varchar(14) NULL DEFAULT NULL,
  org_no char(14) NULL DEFAULT NULL,
  add_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  rows_id varchar(100) NULL DEFAULT NULL,
  batch_date char(8) NULL DEFAULT NULL,
  industry_dscr varchar(60) NULL DEFAULT NULL,
  loan varchar(50) NULL DEFAULT NULL,
  bad_loan varchar(50) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.rpt_qtr_bank_finorg_basic_data (
  area_no varchar(8) NULL DEFAULT NULL,
  fiscal_term char(10) NULL DEFAULT NULL,
  table_nature varchar(14) NULL DEFAULT NULL,
  org_no char(14) NULL DEFAULT NULL,
  area_name varchar(50) NULL DEFAULT NULL,
  batch_date char(8) NULL DEFAULT NULL,
  bs_asset varchar(50) NULL DEFAULT NULL,
  bs_loan varchar(50) NULL DEFAULT NULL,
  bs_debt varchar(50) NULL DEFAULT NULL,
  bs_deposit varchar(50) NULL DEFAULT NULL,
  bs_save_dept varchar(50) NULL DEFAULT NULL,
  bs_owneqty varchar(50) NULL DEFAULT NULL,
  bs_addup_profit varchar(50) NULL DEFAULT NULL,
  cr_follow_bal varchar(50) NULL DEFAULT NULL,
  cr_flr varchar(50) NULL DEFAULT NULL,
  cr_bad_bal varchar(50) NULL DEFAULT NULL,
  cr_blr varchar(50) NULL DEFAULT NULL,
  cr_pcr varchar(50) NULL DEFAULT NULL,
  cr_sbr varchar(50) NULL DEFAULT NULL,
  cr_jxc varchar(50) NULL DEFAULT NULL,
  cr_cir varchar(50) NULL DEFAULT NULL,
  cr_mbir varchar(50) NULL DEFAULT NULL,
  f_dlr varchar(50) NULL DEFAULT NULL,
  bs_asset_inc varchar(50) NULL DEFAULT NULL,
  bs_loan_inc varchar(50) NULL DEFAULT NULL,
  bs_debt_inc varchar(50) NULL DEFAULT NULL,
  bs_deposit_inc varchar(50) NULL DEFAULT NULL,
  bs_save_dept_inc varchar(50) NULL DEFAULT NULL,
  bs_owneqty_inc varchar(50) NULL DEFAULT NULL,
  bs_addup_profit_inc varchar(50) NULL DEFAULT NULL,
  cr_follow_bal_inc varchar(50) NULL DEFAULT NULL,
  cr_flr_inc varchar(50) NULL DEFAULT NULL,
  cr_bad_bal_inc varchar(50) NULL DEFAULT NULL,
  cr_blr_inc varchar(50) NULL DEFAULT NULL,
  cr_pcr_inc varchar(50) NULL DEFAULT NULL,
  cr_sbr_inc varchar(50) NULL DEFAULT NULL,
  cr_jxc_inc varchar(50) NULL DEFAULT NULL,
  cr_cir_inc varchar(50) NULL DEFAULT NULL,
  cr_mbir_inc varchar(50) NULL DEFAULT NULL,
  f_dlr_inc varchar(50) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.rpt_qtr_bank_org_data_statis (
  area_no varchar(8) NOT NULL,
  data_date char(10) NOT NULL,
  table_nature varchar(14) NULL DEFAULT NULL,
  org_no char(14) NOT NULL,
  add_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  org_name varchar(512) NULL DEFAULT NULL,
  org_type_code char(8) NULL DEFAULT NULL,
  org_type_dscr varchar(60) NULL DEFAULT NULL,
  belong_area_code char(8) NULL DEFAULT NULL,
  delong_area_dscr varchar(200) NULL DEFAULT NULL,
  ad_asset_yi varchar(50) NULL DEFAULT NULL,
  ad_asset varchar(50) NULL DEFAULT NULL,
  ad_loan_bal varchar(50) NULL DEFAULT NULL,
  ad_debt varchar(50) NULL DEFAULT NULL,
  ad_dept_bal varchar(50) NULL DEFAULT NULL,
  ad_save_dept varchar(50) NULL DEFAULT NULL,
  ad_owneqty_yi varchar(50) NULL DEFAULT NULL,
  ad_owneqty varchar(50) NULL DEFAULT NULL,
  ad_few_owneqty_yi varchar(50) NULL DEFAULT NULL,
  ad_few_owneqty varchar(50) NULL DEFAULT NULL,
  ad_si_storage_oppo varchar(50) NULL DEFAULT NULL,
  ad_si_oppo_storage varchar(50) NULL DEFAULT NULL,
  ad_si_lend_oppo varchar(50) NULL DEFAULT NULL,
  ad_si_oppo_lend varchar(50) NULL DEFAULT NULL,
  ad_si_buy_sale varchar(50) NULL DEFAULT NULL,
  ad_si_sale_buy varchar(50) NULL DEFAULT NULL,
  os_nohold_fin_bal varchar(50) NULL DEFAULT NULL,
  os_entrust_loan varchar(50) NULL DEFAULT NULL,
  pa_margin varchar(50) NULL DEFAULT NULL,
  pa_net_margin varchar(50) NULL DEFAULT NULL,
  pa_margin_curm varchar(50) NULL DEFAULT NULL,
  pa_few_owngl varchar(50) NULL DEFAULT NULL,
  pa_jxc_fz varchar(50) NULL DEFAULT NULL,
  pa_jxc_fm varchar(50) NULL DEFAULT NULL,
  pa_jxc_res varchar(50) NULL DEFAULT NULL,
  pa_apr_fz varchar(50) NULL DEFAULT NULL,
  pa_apr_fm varchar(50) NULL DEFAULT NULL,
  pa_apr_res varchar(50) NULL DEFAULT NULL,
  pa_inte_income_total varchar(50) NULL DEFAULT NULL,
  pa_loan_inte_income varchar(50) NULL DEFAULT NULL,
  pa_inte_cost_total varchar(50) NULL DEFAULT NULL,
  pa_loan_inte_cost varchar(50) NULL DEFAULT NULL,
  pa_ginteast_avg_bal varchar(50) NULL DEFAULT NULL,
  pa_ginteast_avgr_fz varchar(50) NULL DEFAULT NULL,
  pa_ginteast_avgr_fm varchar(50) NULL DEFAULT NULL,
  pa_ginteast_avgr_res varchar(50) NULL DEFAULT NULL,
  pa_cinteast_avg_bal varchar(50) NULL DEFAULT NULL,
  pa_cinteast_avgr_fz varchar(50) NULL DEFAULT NULL,
  pa_cinteast_avgr_fm varchar(50) NULL DEFAULT NULL,
  pa_cinteast_avgr_res varchar(50) NULL DEFAULT NULL,
  pa_comm_net_income varchar(50) NULL DEFAULT NULL,
  pa_yield varchar(50) NULL DEFAULT NULL,
  pa_fair_chang_income varchar(50) NULL DEFAULT NULL,
  pa_exchange_income varchar(50) NULL DEFAULT NULL,
  pa_operate_revenue varchar(50) NULL DEFAULT NULL,
  pa_other_buss_income varchar(50) NULL DEFAULT NULL,
  pa_mid_buss_income varchar(50) NULL DEFAULT NULL,
  pa_mbir_fz varchar(50) NULL DEFAULT NULL,
  pa_mbir_fm varchar(50) NULL DEFAULT NULL,
  pa_mbir_res varchar(50) NULL DEFAULT NULL,
  pa_operate_expend varchar(50) NULL DEFAULT NULL,
  pa_tax_addi varchar(50) NULL DEFAULT NULL,
  pa_cir_fz varchar(50) NULL DEFAULT NULL,
  pa_cir_fm varchar(50) NULL DEFAULT NULL,
  pa_cir_res varchar(50) NULL DEFAULT NULL,
  aqp_normal_loan_bal varchar(50) NULL DEFAULT NULL,
  aqp_follow_loan_bal varchar(50) NULL DEFAULT NULL,
  aqp_flr_fz varchar(50) NULL DEFAULT NULL,
  aqp_flr_fm varchar(50) NULL DEFAULT NULL,
  aqp_flr_res varchar(50) NULL DEFAULT NULL,
  aqp_bad_loan_bal varchar(50) NULL DEFAULT NULL,
  aqp_second_loan_bal varchar(50) NULL DEFAULT NULL,
  aqp_dubious_loan_bal varchar(50) NULL DEFAULT NULL,
  aqp_loss_loan_bal varchar(50) NULL DEFAULT NULL,
  aqp_overdue_loan_bal varchar(50) NULL DEFAULT NULL,
  aqp_overdue90_bal varchar(50) NULL DEFAULT NULL,
  aqp_overdue90_fz varchar(50) NULL DEFAULT NULL,
  aqp_overdue90_fm varchar(50) NULL DEFAULT NULL,
  aqp_overdue90_res varchar(50) NULL DEFAULT NULL,
  aqp_blr_fz varchar(50) NULL DEFAULT NULL,
  aqp_blr_fm varchar(50) NULL DEFAULT NULL,
  aqp_blr_res varchar(50) NULL DEFAULT NULL,
  aqp_lpr_fz varchar(50) NULL DEFAULT NULL,
  aqp_lpr_fm varchar(50) NULL DEFAULT NULL,
  aqp_lpr_res varchar(50) NULL DEFAULT NULL,
  aqp_pcr_fz varchar(50) NULL DEFAULT NULL,
  aqp_pcr_fm varchar(50) NULL DEFAULT NULL,
  aqp_pcr_res varchar(50) NULL DEFAULT NULL,
  aqp_big_loan_amt varchar(50) NULL DEFAULT NULL,
  aqp_bslp_fz varchar(50) NULL DEFAULT NULL,
  aqp_bslp_fm varchar(50) NULL DEFAULT NULL,
  aqp_bslp_res varchar(50) NULL DEFAULT NULL,
  aqp_big10_loan_amt varchar(50) NULL DEFAULT NULL,
  aqp_btlp_fz varchar(50) NULL DEFAULT NULL,
  aqp_btlp_fm varchar(50) NULL DEFAULT NULL,
  aqp_btlp_res varchar(50) NULL DEFAULT NULL,
  aqp_cash_liquidate varchar(50) NULL DEFAULT NULL,
  aqp_batch_transfer varchar(50) NULL DEFAULT NULL,
  aqp_pay_in_debt varchar(50) NULL DEFAULT NULL,
  aqp_cancel varchar(50) NULL DEFAULT NULL,
  aqp_nlmr_fz varchar(50) NULL DEFAULT NULL,
  aqp_nlmr_fm varchar(50) NULL DEFAULT NULL,
  aqp_nlmr_res varchar(50) NULL DEFAULT NULL,
  aqp_flmr_fz varchar(50) NULL DEFAULT NULL,
  aqp_flmr_fm varchar(50) NULL DEFAULT NULL,
  aqp_flmr_res varchar(50) NULL DEFAULT NULL,
  f_dlr_fz varchar(50) NULL DEFAULT NULL,
  f_dlr_fm varchar(50) NULL DEFAULT NULL,
  f_dlr_res varchar(50) NULL DEFAULT NULL,
  o_buysale_bill varchar(50) NULL DEFAULT NULL,
  o_salebuy_bill varchar(50) NULL DEFAULT NULL,
  o_si_invest_stand varchar(50) NULL DEFAULT NULL,
  o_si_invest_bill varchar(50) NULL DEFAULT NULL,
  o_si_invest_nonstand varchar(50) NULL DEFAULT NULL,
  o_last_finprd_num varchar(50) NULL DEFAULT NULL,
  o_last_finprd_bal varchar(50) NULL DEFAULT NULL,
  o_lfpir_fz varchar(50) NULL DEFAULT NULL,
  o_lfpir_fm varchar(50) NULL DEFAULT NULL,
  o_lfpir_res varchar(50) NULL DEFAULT NULL,
  o_badast_deal_mode varchar(50) NULL DEFAULT NULL,
  o_badast_deal_amt varchar(50) NULL DEFAULT NULL,
  remakes varchar(200) NULL DEFAULT NULL,
  PRIMARY KEY (org_no, data_date, area_no)
);

CREATE TABLE IF NOT EXISTS adm.rpt_qtr_insur_org_dept_acct (
  area_no_id char(8) NULL DEFAULT NULL,
  fiscal_term char(10) NULL DEFAULT NULL,
  org_id char(14) NULL DEFAULT NULL,
  serialno int NULL DEFAULT NULL,
  insur_org_id char(14) NULL DEFAULT NULL,
  dept_catagory varchar(100) NULL DEFAULT NULL,
  num_total varchar(200) NULL DEFAULT NULL,
  capital_total decimal(30, 5) NULL DEFAULT NULL,
  interest_total decimal(30, 5) NULL DEFAULT NULL,
  capital_interest decimal(30, 5) NULL DEFAULT NULL,
  limit_less_num int NULL DEFAULT NULL,
  limit_less_capital decimal(30, 5) NULL DEFAULT NULL,
  limit_less_interest decimal(30, 5) NULL DEFAULT NULL,
  limit_less_total decimal(30, 5) NULL DEFAULT NULL,
  limit_more_num int NULL DEFAULT NULL,
  limit_more_capital decimal(30, 5) NULL DEFAULT NULL,
  limit_more_interest decimal(30, 5) NULL DEFAULT NULL,
  limit_more_total decimal(30, 5) NULL DEFAULT NULL,
  batch_date timestamp(0) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.rpt_qtr_insur_org_dept_struct (
  area_no_id char(8) NULL DEFAULT NULL,
  fiscal_term char(10) NULL DEFAULT NULL,
  org_id char(14) NULL DEFAULT NULL,
  insur_org_id char(14) NULL DEFAULT NULL,
  insur_org_name varchar(512) NULL DEFAULT NULL,
  insur_org_type_code char(8) NULL DEFAULT NULL,
  insur_org_type_dscr varchar(60) NULL DEFAULT NULL,
  belong_area_code char(8) NULL DEFAULT NULL,
  delong_area_dscr varchar(200) NULL DEFAULT NULL,
  high_limit_num int NULL DEFAULT NULL,
  insured_num int NULL DEFAULT NULL,
  dept_num varchar(200) NULL DEFAULT NULL,
  insured_total decimal(30, 5) NULL DEFAULT NULL,
  insured_total_1 decimal(30, 5) NULL DEFAULT NULL,
  dept_total decimal(30, 5) NULL DEFAULT NULL,
  ind_high_limit_num int NULL DEFAULT NULL,
  ind_insured_num int NULL DEFAULT NULL,
  ind_dept_num int NULL DEFAULT NULL,
  ind_insured_total decimal(30, 5) NULL DEFAULT NULL,
  ind_insured_total_1 decimal(30, 5) NULL DEFAULT NULL,
  ind_dept_total decimal(30, 5) NULL DEFAULT NULL,
  ind_manager_num int NULL DEFAULT NULL,
  ind_manager_total decimal(30, 5) NULL DEFAULT NULL,
  corp_high_limit_num int NULL DEFAULT NULL,
  corp_insured_num int NULL DEFAULT NULL,
  corp_dept_num int NULL DEFAULT NULL,
  corp_insur_total decimal(30, 5) NULL DEFAULT NULL,
  corp_insured_total_1 decimal(30, 5) NULL DEFAULT NULL,
  corp_dept_total decimal(30, 5) NULL DEFAULT NULL,
  ink_dept_num int NULL DEFAULT NULL,
  ink_dept_total decimal(30, 5) NULL DEFAULT NULL,
  batch_date timestamp(0) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.rpt_qtr_insur_org_oper_monitor (
  area_no varchar(8) NOT NULL,
  data_date char(10) NOT NULL,
  table_nature varchar(14) NULL DEFAULT NULL,
  org_no char(14) NOT NULL,
  batch_date char(8) NULL DEFAULT NULL,
  org_code varchar(50) NULL DEFAULT NULL,
  insur_org_name varchar(512) NULL DEFAULT NULL,
  insur_org_type_code char(8) NULL DEFAULT NULL,
  insur_org_type_dscr varchar(60) NULL DEFAULT NULL,
  delong_area_dscr varchar(200) NULL DEFAULT NULL,
  ad_asset_yi varchar(50) NULL DEFAULT NULL,
  ad_asset varchar(50) NULL DEFAULT NULL,
  ad_loan_bal varchar(50) NULL DEFAULT NULL,
  ad_debt varchar(50) NULL DEFAULT NULL,
  ad_dept_bal_rh varchar(50) NULL DEFAULT NULL,
  ad_dept_bal_yj varchar(50) NULL DEFAULT NULL,
  ad_save_dept varchar(50) NULL DEFAULT NULL,
  ad_deposit_bal varchar(50) NULL DEFAULT NULL,
  ad_trade_liab varchar(50) NULL DEFAULT NULL,
  ad_trade_liab_rate varchar(50) NULL DEFAULT NULL,
  ad_max_prop_fz varchar(50) NULL DEFAULT NULL,
  ad_max_prop_fm varchar(50) NULL DEFAULT NULL,
  ad_max_prop varchar(50) NULL DEFAULT NULL,
  ad_owneqty_yi varchar(50) NULL DEFAULT NULL,
  ad_owneqty varchar(50) NULL DEFAULT NULL,
  ad_few_owneqty_yi varchar(50) NULL DEFAULT NULL,
  ad_few_owneqty varchar(50) NULL DEFAULT NULL,
  as_capital_net varchar(50) NULL DEFAULT NULL,
  as_1_capital_net varchar(50) NULL DEFAULT NULL,
  as_core_capital_net varchar(50) NULL DEFAULT NULL,
  as_mkt_risk_capital varchar(50) NULL DEFAULT NULL,
  as_adj_risk_asset varchar(50) NULL DEFAULT NULL,
  as_csr_fz varchar(50) NULL DEFAULT NULL,
  as_csr_fm varchar(50) NULL DEFAULT NULL,
  as_csr_res varchar(50) NULL DEFAULT NULL,
  as_fcsr_fz varchar(50) NULL DEFAULT NULL,
  as_fcsr_fm varchar(50) NULL DEFAULT NULL,
  as_fcsr_res varchar(50) NULL DEFAULT NULL,
  as_cfcsr_fz varchar(50) NULL DEFAULT NULL,
  as_cfcsr_fm varchar(50) NULL DEFAULT NULL,
  as_cfcsr_res varchar(50) NULL DEFAULT NULL,
  as_adj_sheet_asset varchar(50) NULL DEFAULT NULL,
  as_lr_fz varchar(50) NULL DEFAULT NULL,
  as_lr_fm varchar(50) NULL DEFAULT NULL,
  as_lr_res varchar(50) NULL DEFAULT NULL,
  pa_margin varchar(50) NULL DEFAULT NULL,
  pa_net_margin varchar(50) NULL DEFAULT NULL,
  pa_margin_curm varchar(50) NULL DEFAULT NULL,
  pa_few_owngl varchar(50) NULL DEFAULT NULL,
  pa_jxc_fz varchar(50) NULL DEFAULT NULL,
  pa_jxc_fm varchar(50) NULL DEFAULT NULL,
  pa_jxc_res varchar(50) NULL DEFAULT NULL,
  pa_cpr_fz varchar(50) NULL DEFAULT NULL,
  pa_cpr_fm varchar(50) NULL DEFAULT NULL,
  pa_cpr_res varchar(50) NULL DEFAULT NULL,
  pa_apr_fz varchar(50) NULL DEFAULT NULL,
  pa_apr_fm varchar(50) NULL DEFAULT NULL,
  pa_apr_res varchar(50) NULL DEFAULT NULL,
  pa_inte_income_total varchar(50) NULL DEFAULT NULL,
  pa_loan_inte_income varchar(50) NULL DEFAULT NULL,
  pa_inte_cost_total varchar(50) NULL DEFAULT NULL,
  pa_loan_inte_cost varchar(50) NULL DEFAULT NULL,
  pa_ginteast_avg_bal varchar(50) NULL DEFAULT NULL,
  pa_ginteast_avgr_fz varchar(50) NULL DEFAULT NULL,
  pa_ginteast_avgr_fm varchar(50) NULL DEFAULT NULL,
  pa_ginteast_avgr_res varchar(50) NULL DEFAULT NULL,
  pa_cinteast_avg_bal varchar(50) NULL DEFAULT NULL,
  pa_cinteast_avgr_fz varchar(50) NULL DEFAULT NULL,
  pa_cinteast_avgr_fm varchar(50) NULL DEFAULT NULL,
  pa_cinteast_avgr_res varchar(50) NULL DEFAULT NULL,
  pa_comm_net_income varchar(50) NULL DEFAULT NULL,
  pa_yield varchar(50) NULL DEFAULT NULL,
  pa_fair_chang_income varchar(50) NULL DEFAULT NULL,
  pa_exchange_income varchar(50) NULL DEFAULT NULL,
  pa_other_buss_income varchar(50) NULL DEFAULT NULL,
  pa_mid_buss_income varchar(50) NULL DEFAULT NULL,
  pa_operate_expend varchar(50) NULL DEFAULT NULL,
  pa_tax_addi varchar(50) NULL DEFAULT NULL,
  pa_operate_revenue varchar(50) NULL DEFAULT NULL,
  pa_mbir_fz varchar(50) NULL DEFAULT NULL,
  pa_mbir_fm varchar(50) NULL DEFAULT NULL,
  pa_mbir_res varchar(50) NULL DEFAULT NULL,
  pa_cir_fz varchar(50) NULL DEFAULT NULL,
  pa_cir_fm varchar(50) NULL DEFAULT NULL,
  pa_cir_res varchar(50) NULL DEFAULT NULL,
  aqp_normal_loan_bal varchar(50) NULL DEFAULT NULL,
  aqp_follow_loan_bal varchar(50) NULL DEFAULT NULL,
  aqp_bad_loan_bal varchar(50) NULL DEFAULT NULL,
  aqp_second_loan_bal varchar(50) NULL DEFAULT NULL,
  aqp_dubious_loan_bal varchar(50) NULL DEFAULT NULL,
  aqp_loss_loan_bal varchar(50) NULL DEFAULT NULL,
  aqp_overdue_loan_bal varchar(50) NULL DEFAULT NULL,
  aqp_overdue90_bal varchar(50) NULL DEFAULT NULL,
  aqp_overdue90_fz varchar(50) NULL DEFAULT NULL,
  aqp_overdue90_fm varchar(50) NULL DEFAULT NULL,
  aqp_overdue90_res varchar(50) NULL DEFAULT NULL,
  aqp_blr_fz varchar(50) NULL DEFAULT NULL,
  aqp_blr_fm varchar(50) NULL DEFAULT NULL,
  aqp_blr_res varchar(50) NULL DEFAULT NULL,
  aqp_bad_credrisk_ast varchar(50) NULL DEFAULT NULL,
  aqp_credrisk_ast varchar(50) NULL DEFAULT NULL,
  aqp_bsr_fz varchar(50) NULL DEFAULT NULL,
  aqp_bsr_fm varchar(50) NULL DEFAULT NULL,
  aqp_bsr_res varchar(50) NULL DEFAULT NULL,
  aqp_loan_reserv_bal varchar(50) NULL DEFAULT NULL,
  aqp_loan_reserv_gap varchar(50) NULL DEFAULT NULL,
  aqp_draw_loan_reserv varchar(50) NULL DEFAULT NULL,
  aqp_llrsr_fz varchar(50) NULL DEFAULT NULL,
  aqp_llrsr_fm varchar(50) NULL DEFAULT NULL,
  aqp_llrsr_res varchar(50) NULL DEFAULT NULL,
  aqp_lpr_fz varchar(50) NULL DEFAULT NULL,
  aqp_lpr_fm varchar(50) NULL DEFAULT NULL,
  aqp_lpr_res varchar(50) NULL DEFAULT NULL,
  aqp_pcr_fz varchar(50) NULL DEFAULT NULL,
  aqp_pcr_fm varchar(50) NULL DEFAULT NULL,
  aqp_pcr_res varchar(50) NULL DEFAULT NULL,
  aqp_bgrp_cred_net varchar(50) NULL DEFAULT NULL,
  aqp_bgcr_fz varchar(50) NULL DEFAULT NULL,
  aqp_bgcr_fm varchar(50) NULL DEFAULT NULL,
  aqp_bgcr_res varchar(50) NULL DEFAULT NULL,
  aqp_big_loan_amt varchar(50) NULL DEFAULT NULL,
  aqp_big10_loan_amt varchar(50) NULL DEFAULT NULL,
  aqp_bslr_fz varchar(50) NULL DEFAULT NULL,
  aqp_bslr_fm varchar(50) NULL DEFAULT NULL,
  aqp_bslr_res varchar(50) NULL DEFAULT NULL,
  aqp_bgrp10_cred_net varchar(50) NULL DEFAULT NULL,
  aqp_bgcr10_fz varchar(50) NULL DEFAULT NULL,
  aqp_bgcr10_fm varchar(50) NULL DEFAULT NULL,
  aqp_bgcr10_res varchar(50) NULL DEFAULT NULL,
  aqp_rela_cred_amt varchar(50) NULL DEFAULT NULL,
  aqp_arr_fz varchar(50) NULL DEFAULT NULL,
  aqp_arr_fm varchar(50) NULL DEFAULT NULL,
  aqp_arr_res varchar(50) NULL DEFAULT NULL,
  f_flow_asset varchar(50) NULL DEFAULT NULL,
  f_flow_debt varchar(50) NULL DEFAULT NULL,
  f_fr_fz varchar(50) NULL DEFAULT NULL,
  f_fr_fm varchar(50) NULL DEFAULT NULL,
  f_fr_res varchar(50) NULL DEFAULT NULL,
  f_ndt_cash_out varchar(50) NULL DEFAULT NULL,
  f_qual_flow_asset varchar(50) NULL DEFAULT NULL,
  f_fcr_fz varchar(50) NULL DEFAULT NULL,
  f_fcr_fm varchar(50) NULL DEFAULT NULL,
  f_fcr_res varchar(50) NULL DEFAULT NULL,
  f_flow_gap varchar(50) NULL DEFAULT NULL,
  f_term90_sheet_asset varchar(50) NULL DEFAULT NULL,
  f_fgr_fz varchar(50) NULL DEFAULT NULL,
  f_fgr_fm varchar(50) NULL DEFAULT NULL,
  f_fgr_res varchar(50) NULL DEFAULT NULL,
  f_core_debt varchar(50) NULL DEFAULT NULL,
  f_cdrr_fz varchar(50) NULL DEFAULT NULL,
  f_cdrr_fm varchar(50) NULL DEFAULT NULL,
  f_cdrr_res varchar(50) NULL DEFAULT NULL,
  f_liqui_fz varchar(50) NULL DEFAULT NULL,
  f_liqui_fm varchar(50) NULL DEFAULT NULL,
  f_liqui_res varchar(50) NULL DEFAULT NULL,
  f_high_fz varchar(50) NULL DEFAULT NULL,
  f_high_fm varchar(50) NULL DEFAULT NULL,
  f_high_res varchar(50) NULL DEFAULT NULL,
  f_rerr_fz varchar(50) NULL DEFAULT NULL,
  f_rerr_fm varchar(50) NULL DEFAULT NULL,
  f_rerr_res varchar(50) NULL DEFAULT NULL,
  f_exce_reserve varchar(50) NULL DEFAULT NULL,
  f_cash varchar(50) NULL DEFAULT NULL,
  f_dlr_fz varchar(50) NULL DEFAULT NULL,
  f_dlr_fm varchar(50) NULL DEFAULT NULL,
  f_dlr_res varchar(50) NULL DEFAULT NULL,
  f_supervise_level varchar(50) NULL DEFAULT NULL,
  f_dept_insur_level varchar(50) NULL DEFAULT NULL,
  PRIMARY KEY (org_no, data_date, area_no)
);

CREATE TABLE IF NOT EXISTS adm.rpt_v_area_dimnsn_code (
  area_no_1 char(8) NULL DEFAULT NULL,
  area_dscr_1 varchar(512) NULL DEFAULT NULL,
  area_no_2 char(8) NULL DEFAULT NULL,
  area_dscr_2 varchar(512) NULL DEFAULT NULL,
  area_no_3 char(8) NULL DEFAULT NULL,
  area_dscr_3 varchar(512) NULL DEFAULT NULL,
  area_no char(8) NOT NULL,
  level char(8) NULL DEFAULT NULL,
  lvl_ord int NULL DEFAULT NULL,
  start_date char(8) NULL DEFAULT '19900101',
  end_date char(8) NULL DEFAULT '99991230'
);

CREATE TABLE IF NOT EXISTS adm.rpt_v_half_dimnsn_code (
  fiscal_id int NOT NULL,
  fiscal_half_year smallint NULL DEFAULT NULL,
  fiscal_half_year_name varchar(16) NULL DEFAULT NULL,
  fiscal_year smallint NULL DEFAULT NULL,
  fiscal_year_name varchar(16) NULL DEFAULT NULL,
  is_active char(1) NULL DEFAULT NULL,
  start_date timestamp(0) NOT NULL DEFAULT '1980-01-01 00:00:00'
);

CREATE TABLE IF NOT EXISTS adm.rpt_v_mth_dimnsn_code (
  fiscal_id int NOT NULL,
  fiscal_mth int NOT NULL,
  fiscal_mth_name varchar(15) NULL DEFAULT NULL,
  fiscal_qtr smallint NULL DEFAULT NULL,
  fiscal_qtr_name varchar(16) NULL DEFAULT NULL,
  fiscal_half_year smallint NULL DEFAULT NULL,
  fiscal_half_year_name varchar(16) NULL DEFAULT NULL,
  fiscal_year smallint NULL DEFAULT NULL,
  fiscal_year_name varchar(16) NULL DEFAULT NULL,
  is_active char(1) NULL DEFAULT NULL,
  start_date timestamp(0) NOT NULL DEFAULT '1980-01-01 00:00:00'
);

CREATE TABLE IF NOT EXISTS adm.rpt_v_org_dimnsn_code (
  org_type_id_1 varchar(14) NULL DEFAULT NULL,
  org_type_dscr_1 varchar(512) NULL DEFAULT NULL,
  org_type_id_2 varchar(14) NULL DEFAULT NULL,
  org_type_dscr_2 varchar(512) NULL DEFAULT NULL,
  org_id char(14) NULL DEFAULT NULL,
  org_dscr varchar(512) NULL DEFAULT NULL,
  lvl_ord int NULL DEFAULT NULL,
  start_date char(8) NULL DEFAULT '19900101',
  is_active char(2) NULL DEFAULT '1'
);

CREATE TABLE IF NOT EXISTS adm.rpt_v_qtr_dimnsn_code (
  fiscal_id int NOT NULL,
  fiscal_qtr smallint NULL DEFAULT NULL,
  fiscal_qtr_name varchar(16) NULL DEFAULT NULL,
  fiscal_half_year smallint NULL DEFAULT NULL,
  fiscal_half_year_name varchar(16) NULL DEFAULT NULL,
  fiscal_year smallint NULL DEFAULT NULL,
  fiscal_year_name varchar(16) NULL DEFAULT NULL,
  is_active char(1) NULL DEFAULT NULL,
  start_date timestamp(0) NOT NULL DEFAULT '1980-01-01 00:00:00'
);

CREATE TABLE IF NOT EXISTS adm.rpt_v_year_dimnsn_code (
  fiscal_id int NOT NULL,
  fiscal_year smallint NULL DEFAULT NULL,
  fiscal_year_name varchar(16) NULL DEFAULT NULL,
  is_active char(1) NULL DEFAULT NULL,
  start_date timestamp(0) NOT NULL DEFAULT '1980-01-01 00:00:00'
);

CREATE TABLE IF NOT EXISTS adm.sk_dim_dept_acct (
  dept_catagory_code varchar(3) NULL DEFAULT NULL,
  dept_catagory_name varchar(100) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.sk_dim_dept_catagory (
  dept_catagory_code varchar(3) NOT NULL DEFAULT '',
  dept_catagory_name varchar(100) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.sk_dim_g0107_item (
  item_code varchar(3) NULL DEFAULT NULL,
  item_name varchar(100) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.sk_rpt_mth_dffr_finorg_class_kpi (
  month_date char(10) NOT NULL,
  area_no_id varchar(8) NOT NULL,
  org_id char(14) NOT NULL,
  bs_asset decimal(30, 6) NULL DEFAULT NULL,
  bs_loan decimal(30, 6) NULL DEFAULT NULL,
  bs_debt decimal(30, 6) NULL DEFAULT NULL,
  csl_core_capital_net decimal(30, 6) NULL DEFAULT NULL,
  csl_capital_net decimal(30, 6) NULL DEFAULT NULL,
  cr_bad_loan_bal decimal(30, 6) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.sk_rpt_mth_dffr_finorg_subtotal_kpi (
  month_date char(8) NULL DEFAULT NULL,
  area_no_id varchar(8) NULL DEFAULT NULL,
  org_id varchar(14) NULL DEFAULT NULL,
  asset_total decimal(30, 6) NULL DEFAULT NULL,
  loan_bal_total decimal(30, 6) NULL DEFAULT NULL,
  debt_total decimal(30, 6) NULL DEFAULT NULL,
  deposit_bal_total decimal(30, 6) NULL DEFAULT NULL,
  saving_deposit decimal(30, 6) NULL DEFAULT NULL,
  own_equity decimal(30, 6) NULL DEFAULT NULL,
  this_month_profit decimal(30, 6) NULL DEFAULT NULL,
  year_profit decimal(30, 6) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.sk_rpt_mth_fxsgzck_finorg_monitor (
  month_date char(10) NOT NULL,
  area_no_id char(8) NOT NULL,
  org_id char(14) NOT NULL,
  bs_asset decimal(30, 6) NULL DEFAULT NULL,
  bs_loan decimal(30, 6) NULL DEFAULT NULL,
  bs_debt decimal(30, 6) NULL DEFAULT NULL,
  bs_deposit decimal(30, 6) NULL DEFAULT NULL,
  bs_save_deposit decimal(30, 6) NULL DEFAULT NULL,
  bs_owneqty decimal(30, 6) NULL DEFAULT NULL,
  bs_profit_curm decimal(30, 6) NULL DEFAULT NULL,
  bs_addup_profit_cury decimal(30, 6) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.sk_rpt_mth_g0107 (
  month_date char(8) NULL DEFAULT NULL,
  area_no_id varchar(8) NULL DEFAULT NULL,
  org_id varchar(14) NULL DEFAULT NULL,
  item_code varchar(3) NULL DEFAULT NULL,
  item_name varchar(100) NULL DEFAULT NULL,
  loan_bal decimal(30, 6) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.sk_rpt_mth_high_risk_bank_org (
  month_date char(10) NOT NULL,
  area_no_id varchar(8) NOT NULL,
  org_id char(14) NOT NULL,
  aq_blr_fz decimal(30, 6) NULL DEFAULT NULL,
  aq_blr_fm decimal(30, 6) NULL DEFAULT NULL,
  aq_pcr_fz decimal(30, 6) NULL DEFAULT NULL,
  aq_pcr_fm decimal(30, 6) NULL DEFAULT NULL,
  f_dlr_fz decimal(30, 6) NULL DEFAULT NULL,
  f_dlr_fm decimal(30, 6) NULL DEFAULT NULL,
  f_fr_fz decimal(30, 6) NULL DEFAULT NULL,
  f_fr_fm decimal(30, 6) NULL DEFAULT NULL,
  l_scr_fz decimal(30, 6) NULL DEFAULT NULL,
  l_scr_fm decimal(30, 6) NULL DEFAULT NULL,
  l_bgcr10_fz decimal(30, 6) NULL DEFAULT NULL,
  l_bgcr10_fm decimal(30, 6) NULL DEFAULT NULL,
  as_ccsr_fz decimal(30, 6) NULL DEFAULT NULL,
  as_ccsr_fm decimal(30, 6) NULL DEFAULT NULL,
  as_csr_fz decimal(30, 6) NULL DEFAULT NULL,
  as_csr_fm decimal(30, 6) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.sk_rpt_mth_insur_org_insured_dept (
  month_date char(10) NULL DEFAULT NULL,
  area_no_id char(8) NULL DEFAULT NULL,
  org_id char(14) NULL DEFAULT NULL,
  dept_catagory_code varchar(3) NOT NULL,
  dept_catagory_name varchar(100) NULL DEFAULT NULL,
  capital decimal(30, 6) NULL DEFAULT NULL,
  interest decimal(30, 6) NULL DEFAULT NULL,
  total decimal(30, 6) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.sk_rpt_mth_insur_org_oper_monitor (
  month_date char(10) NOT NULL,
  area_no_id varchar(8) NOT NULL,
  org_id char(14) NOT NULL,
  ad_asset_yi decimal(30, 6) NULL DEFAULT NULL,
  ad_asset decimal(30, 6) NULL DEFAULT NULL,
  ad_loan_bal decimal(30, 6) NULL DEFAULT NULL,
  ad_debt decimal(30, 6) NULL DEFAULT NULL,
  ad_dept_bal decimal(30, 6) NULL DEFAULT NULL,
  ad_save_dept decimal(30, 6) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.sk_rpt_qtr_bank_finorg_basic_data (
  qtr_date char(10) NULL DEFAULT NULL,
  area_no_id varchar(8) NULL DEFAULT NULL,
  org_id char(14) NULL DEFAULT NULL,
  bs_asset decimal(30, 6) NULL DEFAULT NULL,
  bs_loan decimal(30, 6) NULL DEFAULT NULL,
  bs_debt decimal(30, 6) NULL DEFAULT NULL,
  bs_deposit decimal(30, 6) NULL DEFAULT NULL,
  bs_save_dept decimal(30, 6) NULL DEFAULT NULL,
  bs_owneqty decimal(30, 6) NULL DEFAULT NULL,
  bs_addup_profit decimal(30, 6) NULL DEFAULT NULL,
  cr_follow_bal decimal(30, 6) NULL DEFAULT NULL,
  cr_bad_bal decimal(30, 6) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.sk_rpt_qtr_insur_org_dept_acct (
  qtr_date char(10) NULL DEFAULT NULL,
  area_no_id char(8) NULL DEFAULT NULL,
  org_id char(14) NULL DEFAULT NULL,
  dept_catagory_code varchar(3) NULL DEFAULT NULL,
  dept_catagory_name varchar(100) NULL DEFAULT NULL,
  num_total int NULL DEFAULT NULL,
  capital_total decimal(30, 5) NULL DEFAULT NULL,
  interest_total decimal(30, 5) NULL DEFAULT NULL,
  capital_interest decimal(30, 5) NULL DEFAULT NULL,
  limit_less_num int NULL DEFAULT NULL,
  limit_less_capital decimal(30, 5) NULL DEFAULT NULL,
  limit_less_interest decimal(30, 5) NULL DEFAULT NULL,
  limit_less_total decimal(30, 5) NULL DEFAULT NULL,
  limit_more_num int NULL DEFAULT NULL,
  limit_more_capital decimal(30, 5) NULL DEFAULT NULL,
  limit_more_interest decimal(30, 5) NULL DEFAULT NULL,
  limit_more_total decimal(30, 5) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.sust_enterprise (
  enterprise_id varchar(120) NULL DEFAULT NULL,
  enterprise_name varchar(600) NULL DEFAULT NULL,
  enterprise_order varchar(30) NULL DEFAULT NULL,
  add_date varchar(60) NULL DEFAULT NULL,
  add_user varchar(120) NULL DEFAULT NULL,
  last_update_date varchar(60) NULL DEFAULT NULL,
  last_update_user varchar(120) NULL DEFAULT NULL,
  db_components varchar(60) NULL DEFAULT NULL,
  status varchar(6) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.sust_enterprise_norm_plan (
  id varchar(40) NOT NULL,
  enterprise_id varchar(40) NULL DEFAULT NULL,
  norm_id varchar(40) NULL DEFAULT NULL,
  plan_id varchar(40) NULL DEFAULT NULL,
  add_date varchar(40) NULL DEFAULT NULL,
  add_user varchar(200) NULL DEFAULT NULL,
  last_update_date varchar(40) NULL DEFAULT NULL,
  last_update_user varchar(200) NULL DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS adm.sust_main_indicators_of_institutions (
  area_no varchar(8) NULL DEFAULT NULL,
  data_date char(8) NULL DEFAULT NULL,
  table_nature varchar(50) NULL DEFAULT NULL,
  org_no varchar(53) NULL DEFAULT NULL,
  add_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  rows_id varchar(100) NULL DEFAULT NULL,
  batch_date char(8) NULL DEFAULT NULL,
  sheet_id varchar(100) NULL DEFAULT NULL,
  occurrence_org varchar(512) NULL DEFAULT NULL,
  bwbdeposit varchar(40) NULL DEFAULT NULL,
  rmbdeposit varchar(40) NULL DEFAULT NULL,
  savingsdeposit varchar(40) NULL DEFAULT NULL,
  bwbloan varchar(40) NULL DEFAULT NULL,
  rmbloan varchar(40) NULL DEFAULT NULL,
  shorttermloan varchar(40) NULL DEFAULT NULL,
  mediumlongtermloan varchar(40) NULL DEFAULT NULL,
  personalmltermloan varchar(40) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.sust_norm (
  norm_id varchar(40) NOT NULL,
  norm_name varchar(200) NULL DEFAULT NULL,
  enterprise varchar(40) NULL DEFAULT NULL,
  norm_type varchar(2) NULL DEFAULT NULL,
  standard_norm varchar(40) NULL DEFAULT NULL,
  add_user varchar(20) NULL DEFAULT NULL,
  add_date varchar(20) NULL DEFAULT NULL,
  last_update_user varchar(20) NULL DEFAULT NULL,
  last_update_date varchar(20) NULL DEFAULT NULL,
  PRIMARY KEY (norm_id)
);

CREATE TABLE IF NOT EXISTS adm.sust_plan (
  plan_id int NULL DEFAULT NULL,
  indicators_id varchar(120) NULL DEFAULT NULL,
  indicators_name varchar(300) NULL DEFAULT NULL,
  plan_name varchar(300) NULL DEFAULT NULL,
  plan_sql text NULL,
  add_date varchar(120) NULL DEFAULT NULL,
  add_user varchar(600) NULL DEFAULT NULL,
  last_update_date varchar(120) NULL DEFAULT NULL,
  last_update_user varchar(600) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.sust_sk_funds_application (
  data_date char(8) NULL DEFAULT NULL,
  batch_date char(8) NULL DEFAULT NULL,
  y1101 varchar(10) NULL DEFAULT NULL,
  y1201 varchar(100) NULL DEFAULT NULL,
  y2803 varchar(100) NULL DEFAULT NULL,
  y2804 varchar(100) NULL DEFAULT NULL,
  y2807 varchar(100) NULL DEFAULT NULL,
  y21_y22_y23 varchar(100) NULL DEFAULT NULL,
  y2401 varchar(100) NULL DEFAULT NULL,
  y2601 varchar(100) NULL DEFAULT NULL,
  y2605 varchar(100) NULL DEFAULT NULL,
  y2701 varchar(100) NULL DEFAULT NULL,
  y3101 varchar(300) NULL DEFAULT NULL,
  y2502 varchar(15) NULL DEFAULT NULL,
  y2501 varchar(100) NULL DEFAULT NULL,
  y3103 varchar(100) NULL DEFAULT NULL,
  y2603_y2602 varchar(10) NULL DEFAULT NULL,
  y2603_y1101 varchar(10) NULL DEFAULT NULL,
  y2403 decimal(28, 8) NULL DEFAULT NULL,
  y2702 decimal(28, 8) NULL DEFAULT NULL,
  y2703 decimal(28, 8) NULL DEFAULT NULL,
  y2704 decimal(28, 8) NULL DEFAULT NULL,
  y2705 decimal(28, 8) NULL DEFAULT NULL,
  y2706 decimal(28, 8) NULL DEFAULT NULL,
  y2707 decimal(28, 8) NULL DEFAULT NULL,
  y2503 decimal(28, 8) NULL DEFAULT NULL,
  y2504 decimal(28, 8) NULL DEFAULT NULL,
  y2708 decimal(28, 8) NULL DEFAULT NULL,
  y3102 decimal(28, 8) NULL DEFAULT NULL,
  y3104 int NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.sust_sk_funds_source (
  data_date varchar(8) NULL DEFAULT NULL,
  batch_date char(8) NULL DEFAULT NULL,
  l1101 varchar(10) NULL DEFAULT NULL,
  l1201 varchar(32) NULL DEFAULT NULL,
  l2503 varchar(100) NULL DEFAULT NULL,
  l2504 varchar(100) NULL DEFAULT NULL,
  l2507 varchar(100) NULL DEFAULT NULL,
  l21_1 varchar(300) NULL DEFAULT NULL,
  l21_2 varchar(300) NULL DEFAULT NULL,
  l21_3 varchar(300) NULL DEFAULT NULL,
  l2304 varchar(10) NULL DEFAULT NULL,
  l2401 varchar(10) NULL DEFAULT NULL,
  l2202 varchar(10) NULL DEFAULT NULL,
  l2201 varchar(32) NULL DEFAULT NULL,
  l3102 varchar(10) NULL DEFAULT NULL,
  l2302_l2301 varchar(10) NULL DEFAULT NULL,
  l2302_l1101 varchar(10) NULL DEFAULT NULL,
  l2402 decimal(28, 8) NULL DEFAULT NULL,
  l2403 decimal(28, 8) NULL DEFAULT NULL,
  l2203 decimal(28, 8) NULL DEFAULT NULL,
  l3101 decimal(28, 8) NULL DEFAULT NULL,
  l3103 int NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.sust_standard_norm (
  id int NULL DEFAULT NULL,
  norm_id varchar(40) NULL DEFAULT NULL,
  norm_name varchar(200) NULL DEFAULT NULL,
  add_user varchar(20) NULL DEFAULT NULL,
  add_date varchar(20) NULL DEFAULT NULL,
  last_update_user varchar(20) NULL DEFAULT NULL,
  last_update_date varchar(20) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.sust_standard_norm_view_copy1 (
  norm_id varchar(40) NULL DEFAULT NULL,
  norm_name varchar(200) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.t_sys_template (
  template_id varchar(50) NOT NULL,
  template_desc varchar(500) NULL DEFAULT NULL,
  template_tip varchar(50) NULL DEFAULT NULL,
  template_tip_cell varchar(20) NULL DEFAULT NULL,
  is_valid int NULL DEFAULT 1,
  add_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  end_col int NULL DEFAULT NULL,
  end_row int NULL DEFAULT NULL,
  is_change varchar(1) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.t_sys_template_file (
  template_file_id varchar(50) NOT NULL,
  template_file_desc varchar(500) NULL DEFAULT NULL,
  template_file_identification varchar(100) NULL DEFAULT NULL,
  template_file_suffix varchar(20) NULL DEFAULT NULL,
  is_direct_storage int NULL DEFAULT 0,
  is_valid int NULL DEFAULT 1,
  add_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  cycle_type varchar(2) NULL DEFAULT NULL,
  shell_name varchar(100) NULL DEFAULT NULL,
  biz_type_id varchar(50) NULL DEFAULT NULL,
  PRIMARY KEY (template_file_id)
);

CREATE TABLE IF NOT EXISTS adm.t_sys_template_file_dept (
  template_file_id varchar(50) NULL DEFAULT NULL,
  department varchar(50) NULL DEFAULT NULL,
  biz_type_id varchar(50) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.t_sys_template_file_entity (
  entity_id varchar(50) NULL DEFAULT NULL,
  template_file_id varchar(50) NULL DEFAULT NULL,
  template_file_name varchar(500) NULL DEFAULT NULL,
  up_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  user_id varchar(50) NULL DEFAULT NULL,
  data_suffix varchar(20) NULL DEFAULT NULL,
  template_version int NULL DEFAULT NULL,
  template_file_content longblob NULL
);

CREATE TABLE IF NOT EXISTS adm.t_sys_template_file_org (
  template_file_id varchar(50) NULL DEFAULT NULL,
  org_id varchar(50) NULL DEFAULT NULL,
  add_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS adm.t_sys_template_file_relation (
  template_id varchar(50) NOT NULL,
  template_file_id varchar(50) NOT NULL,
  sheet_id varchar(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS adm.t_sys_template_file_role (
  template_file_id varchar(50) NOT NULL,
  role_id varchar(50) NOT NULL,
  add_date timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS adm.t_sys_template_file_sheet (
  sheet_id varchar(50) NULL DEFAULT NULL,
  sheet_rule varchar(500) NULL DEFAULT NULL,
  is_valid int NULL DEFAULT 1,
  add_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_parent int NULL DEFAULT 1,
  run_sql varchar(1000) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.t_sys_template_sheet_relation (
  sheet_id varchar(50) NOT NULL,
  template_id varchar(50) NOT NULL,
  is_verify_area int NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.t_sys_template_table (
  table_name varchar(100) NOT NULL,
  template_id varchar(50) NOT NULL,
  table_type varchar(10) NULL DEFAULT NULL,
  table_desc varchar(500) NULL DEFAULT NULL,
  start_row varchar(11) NULL DEFAULT NULL,
  end_row varchar(11) NULL DEFAULT NULL,
  start_txt varchar(200) NULL DEFAULT NULL,
  start_offset varchar(11) NULL DEFAULT NULL,
  end_txt varchar(200) NULL DEFAULT NULL,
  end_offset varchar(11) NULL DEFAULT NULL,
  exclude_rows varchar(500) NULL DEFAULT NULL,
  include_rows varchar(500) NULL DEFAULT NULL,
  is_create_table int NULL DEFAULT 0,
  table_id varchar(50) NOT NULL,
  add_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS adm.t_sys_template_table_column (
  column_id varchar(50) NULL DEFAULT NULL,
  table_id varchar(100) NULL DEFAULT NULL,
  column_name varchar(100) NULL DEFAULT NULL,
  column_type varchar(20) NULL DEFAULT NULL,
  column_length int NULL DEFAULT NULL,
  column_precision int NULL DEFAULT 0,
  cell varchar(200) NULL DEFAULT NULL,
  cell_type varchar(20) NULL DEFAULT NULL,
  reader_rule varchar(200) NULL DEFAULT NULL,
  in_dtl int NULL DEFAULT NULL,
  is_not_null int NULL DEFAULT NULL,
  column_desc varchar(500) NULL DEFAULT NULL,
  is_loop int NULL DEFAULT NULL,
  add_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS adm.t_sys_template_table_row_exception (
  row_exception_id varchar(50) NULL DEFAULT NULL,
  column_id varchar(50) NULL DEFAULT NULL,
  row_num varchar(200) NULL DEFAULT NULL,
  cell varchar(20) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.t_sys_template_type (
  template_type_id varchar(50) NULL DEFAULT NULL,
  template_type_desc varchar(200) NULL DEFAULT NULL,
  add_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS adm.t_sys_template_verify (
  verify_id varchar(50) NOT NULL,
  verify_name varchar(50) NULL DEFAULT NULL,
  template_id varchar(50) NOT NULL,
  verify_rule varchar(10000) NULL DEFAULT NULL,
  verify_type int NULL DEFAULT NULL,
  veriify_form varchar(10) NULL DEFAULT NULL,
  is_app_verify int NULL DEFAULT NULL,
  is_valid int NULL DEFAULT 1,
  sqltxt varchar(10000) NULL DEFAULT NULL,
  msg varchar(1000) NULL DEFAULT NULL,
  add_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  if_show int NULL DEFAULT 1
);

CREATE TABLE IF NOT EXISTS adm.t_template_ods_dw_etl (
  v_id varchar(50) NOT NULL,
  v_acct_id varchar(50) NOT NULL,
  v_org_id varchar(50) NOT NULL,
  v_area_id varchar(50) NOT NULL,
  v_template_id varchar(50) NOT NULL,
  v_sheet_id varchar(50) NOT NULL,
  v_proc_type varchar(50) NOT NULL,
  v_parm_type varchar(50) NOT NULL,
  add_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  v_template_file_id varchar(50) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.total_business_anyarea_dtl (
  area_no varchar(8) NOT NULL,
  data_date char(8) NOT NULL,
  table_nature varchar(50) NULL DEFAULT NULL,
  org_no varchar(14) NOT NULL,
  add_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  rows_id varchar(100) NULL DEFAULT NULL,
  batch_date char(8) NULL DEFAULT NULL,
  sheet_id varchar(100) NULL DEFAULT NULL,
  unit varchar(100) NULL DEFAULT NULL,
  times varchar(100) NULL DEFAULT NULL,
  body_name varchar(100) NULL DEFAULT NULL,
  body_code varchar(100) NOT NULL,
  business_type varchar(100) NOT NULL,
  total_business decimal(28, 8) NULL DEFAULT NULL,
  PRIMARY KEY (area_no, data_date, org_no, body_code, business_type)
);

CREATE TABLE IF NOT EXISTS adm.trs_budget_function_income_daily (
  d_reportdate varchar(10) NULL DEFAULT '',
  s_trecode char(10) NULL DEFAULT '',
  c_budgetlevel char(1) NULL DEFAULT '',
  subject_code_4 varchar(10) NOT NULL,
  subject_dscr_4 varchar(64) NULL DEFAULT NULL,
  fun_class_code_1 varchar(10) NULL DEFAULT NULL,
  fun_class_dscr_1 varchar(64) NULL DEFAULT NULL,
  fun_class_code_2 varchar(10) NULL DEFAULT NULL,
  fun_class_dscr_2 varchar(64) NULL DEFAULT NULL,
  fun_class_code_3 varchar(10) NULL DEFAULT NULL,
  fun_class_dscr_3 varchar(64) NULL DEFAULT NULL,
  this_period_amt decimal(40, 2) NULL DEFAULT NULL,
  last_period_amt decimal(40, 2) NULL DEFAULT NULL,
  this_period_yearamt decimal(40, 2) NULL DEFAULT NULL,
  last_period_yearamt decimal(40, 2) NULL DEFAULT NULL,
  update_date datetime(0) NOT NULL
);

CREATE TABLE IF NOT EXISTS adm.trs_budget_function_income_daily_20190328 (
  d_reportdate varchar(10) NULL DEFAULT '',
  s_trecode char(10) NULL DEFAULT '',
  c_budgetlevel char(1) NULL DEFAULT '',
  subject_code_4 varchar(10) NOT NULL,
  subject_dscr_4 varchar(64) NULL DEFAULT NULL,
  fun_class_code_1 varchar(10) NULL DEFAULT NULL,
  fun_class_dscr_1 varchar(64) NULL DEFAULT NULL,
  fun_class_code_2 varchar(10) NULL DEFAULT NULL,
  fun_class_dscr_2 varchar(64) NULL DEFAULT NULL,
  fun_class_code_3 varchar(10) NULL DEFAULT NULL,
  fun_class_dscr_3 varchar(64) NULL DEFAULT NULL,
  this_period_amt decimal(40, 2) NULL DEFAULT NULL,
  last_period_amt decimal(40, 2) NULL DEFAULT NULL,
  this_period_yearamt decimal(40, 2) NULL DEFAULT NULL,
  last_period_yearamt decimal(40, 2) NULL DEFAULT NULL,
  update_date datetime(0) NOT NULL
);

CREATE TABLE IF NOT EXISTS adm.trs_budget_function_income_monthly (
  d_reportdate varchar(7) NULL DEFAULT NULL,
  s_trecode char(10) NULL DEFAULT '',
  c_budgetlevel char(1) NULL DEFAULT '',
  subject_code_4 varchar(10) NOT NULL,
  subject_dscr_4 varchar(64) NULL DEFAULT NULL,
  fun_class_code_1 varchar(10) NULL DEFAULT NULL,
  fun_class_dscr_1 varchar(64) NULL DEFAULT NULL,
  fun_class_code_2 varchar(10) NULL DEFAULT NULL,
  fun_class_dscr_2 varchar(64) NULL DEFAULT NULL,
  fun_class_code_3 varchar(10) NULL DEFAULT NULL,
  fun_class_dscr_3 varchar(64) NULL DEFAULT NULL,
  this_period_amt decimal(40, 2) NULL DEFAULT NULL,
  last_period_amt decimal(40, 2) NULL DEFAULT NULL,
  this_period_yearamt decimal(40, 2) NULL DEFAULT NULL,
  last_period_yearamt decimal(40, 2) NULL DEFAULT NULL,
  update_date datetime(0) NOT NULL
);

CREATE TABLE IF NOT EXISTS adm.trs_budget_function_income_yearly (
  d_reportdate varchar(4) NULL DEFAULT NULL,
  s_trecode char(10) NULL DEFAULT '',
  c_budgetlevel char(1) NULL DEFAULT '',
  subject_code_4 varchar(10) NOT NULL,
  subject_dscr_4 varchar(64) NULL DEFAULT NULL,
  fun_class_code_1 varchar(10) NULL DEFAULT NULL,
  fun_class_dscr_1 varchar(64) NULL DEFAULT NULL,
  fun_class_code_2 varchar(10) NULL DEFAULT NULL,
  fun_class_dscr_2 varchar(64) NULL DEFAULT NULL,
  fun_class_code_3 varchar(10) NULL DEFAULT NULL,
  fun_class_dscr_3 varchar(64) NULL DEFAULT NULL,
  this_period_amt decimal(40, 2) NULL DEFAULT NULL,
  last_period_amt decimal(40, 2) NULL DEFAULT NULL,
  this_period_yearamt decimal(40, 2) NULL DEFAULT NULL,
  last_period_yearamt decimal(40, 2) NULL DEFAULT NULL,
  update_date datetime(0) NOT NULL
);

CREATE TABLE IF NOT EXISTS adm.trs_budget_function_payout_daily (
  d_reportdate varchar(10) NULL DEFAULT '',
  s_trecode char(10) NULL DEFAULT '',
  c_budgetlevel int NULL DEFAULT NULL,
  subject_code_4 varchar(10) NOT NULL,
  subject_dscr_4 varchar(64) NULL DEFAULT NULL,
  fun_class_code_1 varchar(10) NULL DEFAULT NULL,
  fun_class_dscr_1 varchar(64) NULL DEFAULT NULL,
  fun_class_code_2 varchar(10) NULL DEFAULT NULL,
  fun_class_dscr_2 varchar(64) NULL DEFAULT NULL,
  fun_class_code_3 varchar(10) NULL DEFAULT NULL,
  fun_class_dscr_3 varchar(64) NULL DEFAULT NULL,
  this_period_amt decimal(40, 2) NULL DEFAULT NULL,
  last_period_amt decimal(40, 2) NULL DEFAULT NULL,
  this_period_yearamt decimal(40, 2) NULL DEFAULT NULL,
  last_period_yearamt decimal(40, 2) NULL DEFAULT NULL,
  update_date datetime(0) NOT NULL
);

CREATE TABLE IF NOT EXISTS adm.trs_budget_function_payout_monthly (
  d_reportdate varchar(7) NULL DEFAULT NULL,
  s_trecode char(10) NULL DEFAULT '',
  c_budgetlevel int NULL DEFAULT NULL,
  subject_code_4 varchar(10) NOT NULL,
  subject_dscr_4 varchar(64) NULL DEFAULT NULL,
  fun_class_code_1 varchar(10) NULL DEFAULT NULL,
  fun_class_dscr_1 varchar(64) NULL DEFAULT NULL,
  fun_class_code_2 varchar(10) NULL DEFAULT NULL,
  fun_class_dscr_2 varchar(64) NULL DEFAULT NULL,
  fun_class_code_3 varchar(10) NULL DEFAULT NULL,
  fun_class_dscr_3 varchar(64) NULL DEFAULT NULL,
  this_period_amt decimal(40, 2) NULL DEFAULT NULL,
  last_period_amt decimal(40, 2) NULL DEFAULT NULL,
  this_period_yearamt decimal(40, 2) NULL DEFAULT NULL,
  last_period_yearamt decimal(40, 2) NULL DEFAULT NULL,
  update_date datetime(0) NOT NULL
);

CREATE TABLE IF NOT EXISTS adm.trs_budget_function_payout_yearly (
  d_reportdate varchar(4) NULL DEFAULT NULL,
  s_trecode char(10) NULL DEFAULT '',
  c_budgetlevel int NULL DEFAULT NULL,
  subject_code_4 varchar(10) NOT NULL,
  subject_dscr_4 varchar(64) NULL DEFAULT NULL,
  fun_class_code_1 varchar(10) NULL DEFAULT NULL,
  fun_class_dscr_1 varchar(64) NULL DEFAULT NULL,
  fun_class_code_2 varchar(10) NULL DEFAULT NULL,
  fun_class_dscr_2 varchar(64) NULL DEFAULT NULL,
  fun_class_code_3 varchar(10) NULL DEFAULT NULL,
  fun_class_dscr_3 varchar(64) NULL DEFAULT NULL,
  this_period_amt decimal(40, 2) NULL DEFAULT NULL,
  last_period_amt decimal(40, 2) NULL DEFAULT NULL,
  this_period_yearamt decimal(40, 2) NULL DEFAULT NULL,
  last_period_yearamt decimal(40, 2) NULL DEFAULT NULL,
  update_date datetime(0) NOT NULL
);

CREATE TABLE IF NOT EXISTS adm.trs_budget_limit_income_daily (
  industry varchar(50) NULL DEFAULT NULL,
  d_accept date NOT NULL,
  s_taxpaycode varchar(20) NULL DEFAULT NULL,
  s_taxpayname varchar(200) NULL DEFAULT NULL,
  s_etpname varchar(200) NULL DEFAULT NULL,
  s_aimtrecode char(10) NOT NULL DEFAULT '',
  guoku_dscr varchar(200) NULL DEFAULT NULL,
  s_bdgsbtcode varchar(30) NOT NULL DEFAULT '',
  s_bdgsbtname varchar(128) NULL DEFAULT NULL,
  this_period_amt decimal(40, 2) NULL DEFAULT NULL,
  taxdate varchar(21) NULL DEFAULT NULL,
  update_date datetime(0) NOT NULL
);

CREATE TABLE IF NOT EXISTS adm.trs_budget_limit_payout_daily (
  d_accept date NOT NULL,
  s_trecode char(10) NOT NULL DEFAULT '',
  s_payeracct varchar(32) NOT NULL DEFAULT '',
  s_payername varchar(60) NOT NULL DEFAULT '',
  s_payeeacct varchar(32) NOT NULL DEFAULT '',
  s_payeename varchar(60) NOT NULL DEFAULT '',
  s_payeeopnbnkno varchar(12) NULL DEFAULT NULL,
  f_amt decimal(40, 2) NULL DEFAULT NULL,
  s_biztype char(6) NOT NULL DEFAULT '',
  biz_type_dscr varchar(3) NOT NULL DEFAULT '',
  s_funcsbtcode varchar(30) NULL DEFAULT NULL,
  funcsbtname varchar(128) NULL DEFAULT NULL,
  purpose varchar(120) NULL DEFAULT NULL,
  basis varchar(120) NULL DEFAULT NULL,
  update_date datetime(0) NOT NULL
);

CREATE TABLE IF NOT EXISTS adm.trs_budget_summary_all_daily (
  d_reportdate date NOT NULL,
  s_trecode char(10) NOT NULL DEFAULT '',
  guoku_dscr varchar(200) NULL DEFAULT NULL,
  c_budgetlevel char(11) NULL DEFAULT NULL,
  s_budgetsubjectcode varchar(30) NOT NULL DEFAULT '',
  s_budgetsubjectname varchar(60) NOT NULL DEFAULT '',
  this_period_amt decimal(40, 2) NULL DEFAULT NULL,
  last_period_amt decimal(40, 2) NULL DEFAULT NULL,
  this_period_yearamt decimal(40, 2) NULL DEFAULT NULL,
  last_period_yearamt decimal(40, 2) NULL DEFAULT NULL,
  update_date datetime(0) NOT NULL
);

CREATE TABLE IF NOT EXISTS adm.trs_budget_summary_all_monthly (
  d_reportdate varchar(7) NULL DEFAULT NULL,
  s_trecode char(10) NOT NULL DEFAULT '',
  guoku_dscr varchar(200) NULL DEFAULT NULL,
  c_budgetlevel char(11) NULL DEFAULT NULL,
  s_budgetsubjectcode varchar(30) NOT NULL DEFAULT '',
  s_budgetsubjectname varchar(60) NOT NULL DEFAULT '',
  this_period_amt decimal(40, 2) NULL DEFAULT NULL,
  last_period_amt decimal(40, 2) NULL DEFAULT NULL,
  this_period_yearamt decimal(40, 2) NULL DEFAULT NULL,
  last_period_yearamt decimal(40, 2) NULL DEFAULT NULL,
  update_date datetime(0) NOT NULL
);

CREATE TABLE IF NOT EXISTS adm.trs_budget_summary_all_yearly (
  d_reportdate varchar(11) NULL DEFAULT NULL,
  s_trecode char(10) NOT NULL DEFAULT '',
  guoku_dscr varchar(200) NULL DEFAULT NULL,
  c_budgetlevel char(11) NULL DEFAULT NULL,
  s_budgetsubjectcode varchar(30) NOT NULL DEFAULT '',
  s_budgetsubjectname varchar(60) NOT NULL DEFAULT '',
  this_period_amt decimal(40, 2) NULL DEFAULT NULL,
  last_period_amt decimal(40, 2) NULL DEFAULT NULL,
  update_date datetime(0) NOT NULL
);

CREATE TABLE IF NOT EXISTS adm.trs_comeout_comprate (
  trecode char(10) NOT NULL,
  report_date varchar(10) NULL DEFAULT NULL,
  income_comp decimal(18, 2) NULL DEFAULT NULL,
  income_quota decimal(18, 2) NULL DEFAULT NULL,
  payout_comp decimal(18, 2) NULL DEFAULT NULL,
  payout_quota decimal(18, 2) NULL DEFAULT NULL,
  batch_date char(8) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.trs_comprehensive_income_month (
  d_acct char(7) NULL DEFAULT NULL,
  s_trecode char(10) NULL DEFAULT NULL,
  s_tredscr varchar(20) NULL DEFAULT NULL,
  subject_code varchar(9) NULL DEFAULT NULL,
  subject_dscr varchar(200) NULL DEFAULT NULL,
  this_amt decimal(18, 4) NULL DEFAULT NULL,
  this_amt_year decimal(18, 4) NULL DEFAULT NULL,
  this_amt_ppt decimal(18, 4) NULL DEFAULT NULL,
  year_amt decimal(18, 4) NULL DEFAULT NULL,
  year_amt_year decimal(18, 4) NULL DEFAULT NULL,
  year_amt_ppt decimal(18, 4) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.trs_comprehensive_income_month_incomemm (
  d_acct char(7) NULL DEFAULT NULL,
  s_trecode char(10) NULL DEFAULT NULL,
  s_tredscr varchar(20) NULL DEFAULT NULL,
  subject_code varchar(9) NULL DEFAULT NULL,
  subject_dscr varchar(200) NULL DEFAULT NULL,
  this_amt decimal(18, 4) NULL DEFAULT NULL,
  this_amt_year decimal(18, 4) NULL DEFAULT NULL,
  this_amt_ppt decimal(18, 4) NULL DEFAULT NULL,
  year_amt decimal(18, 4) NULL DEFAULT NULL,
  year_amt_year decimal(18, 4) NULL DEFAULT NULL,
  year_amt_ppt decimal(18, 4) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.trs_comprehensive_income_quarter (
  d_acct char(6) NULL DEFAULT NULL,
  s_trecode char(10) NULL DEFAULT NULL,
  s_tredscr varchar(20) NULL DEFAULT NULL,
  subject_code varchar(9) NULL DEFAULT NULL,
  subject_dscr varchar(200) NULL DEFAULT NULL,
  this_amt decimal(18, 4) NULL DEFAULT NULL,
  this_amt_year decimal(18, 4) NULL DEFAULT NULL,
  this_amt_ppt decimal(18, 4) NULL DEFAULT NULL,
  year_amt decimal(18, 4) NULL DEFAULT NULL,
  year_amt_year decimal(18, 4) NULL DEFAULT NULL,
  year_amt_ppt decimal(18, 4) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.trs_comprehensive_income_quarter_incomeqq (
  d_acct char(6) NULL DEFAULT NULL,
  s_trecode char(10) NULL DEFAULT NULL,
  s_tredscr varchar(20) NULL DEFAULT NULL,
  subject_code varchar(9) NULL DEFAULT NULL,
  subject_dscr varchar(200) NULL DEFAULT NULL,
  this_amt decimal(18, 4) NULL DEFAULT NULL,
  this_amt_year decimal(18, 4) NULL DEFAULT NULL,
  this_amt_ppt decimal(18, 4) NULL DEFAULT NULL,
  year_amt decimal(18, 4) NULL DEFAULT NULL,
  year_amt_year decimal(18, 4) NULL DEFAULT NULL,
  year_amt_ppt decimal(18, 4) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.trs_comprehensive_payout_month (
  d_acct char(20) NULL DEFAULT NULL,
  s_trecode char(10) NULL DEFAULT NULL,
  s_tredscr varchar(20) NULL DEFAULT NULL,
  subject_code varchar(9) NULL DEFAULT NULL,
  subject_dscr varchar(200) NULL DEFAULT NULL,
  this_amt decimal(18, 4) NULL DEFAULT NULL,
  this_amt_year decimal(18, 4) NULL DEFAULT NULL,
  this_amt_ppt decimal(18, 4) NULL DEFAULT NULL,
  year_amt decimal(18, 4) NULL DEFAULT NULL,
  year_amt_year decimal(18, 4) NULL DEFAULT NULL,
  year_amt_ppt decimal(18, 4) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.trs_comprehensive_payout_month_payoutmm (
  d_acct char(20) NULL DEFAULT NULL,
  s_trecode char(10) NULL DEFAULT NULL,
  s_tredscr varchar(20) NULL DEFAULT NULL,
  subject_code varchar(9) NULL DEFAULT NULL,
  subject_dscr varchar(200) NULL DEFAULT NULL,
  this_amt decimal(18, 4) NULL DEFAULT NULL,
  this_amt_year decimal(18, 4) NULL DEFAULT NULL,
  this_amt_ppt decimal(18, 4) NULL DEFAULT NULL,
  year_amt decimal(18, 4) NULL DEFAULT NULL,
  year_amt_year decimal(18, 4) NULL DEFAULT NULL,
  year_amt_ppt decimal(18, 4) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.trs_comprehensive_payout_quarter (
  d_acct char(6) NULL DEFAULT NULL,
  s_trecode char(10) NULL DEFAULT NULL,
  s_tredscr varchar(20) NULL DEFAULT NULL,
  subject_code varchar(9) NULL DEFAULT NULL,
  subject_dscr varchar(200) NULL DEFAULT NULL,
  this_amt decimal(18, 4) NULL DEFAULT NULL,
  this_amt_year decimal(18, 4) NULL DEFAULT NULL,
  this_amt_ppt decimal(18, 4) NULL DEFAULT NULL,
  year_amt decimal(18, 4) NULL DEFAULT NULL,
  year_amt_year decimal(18, 4) NULL DEFAULT NULL,
  year_amt_ppt decimal(18, 4) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.trs_comprehensive_payout_quarter_payoutqq (
  d_acct char(6) NULL DEFAULT NULL,
  s_trecode char(10) NULL DEFAULT NULL,
  s_tredscr varchar(20) NULL DEFAULT NULL,
  subject_code varchar(9) NULL DEFAULT NULL,
  subject_dscr varchar(200) NULL DEFAULT NULL,
  this_amt decimal(18, 4) NULL DEFAULT NULL,
  this_amt_year decimal(18, 4) NULL DEFAULT NULL,
  this_amt_ppt decimal(18, 4) NULL DEFAULT NULL,
  year_amt decimal(18, 4) NULL DEFAULT NULL,
  year_amt_year decimal(18, 4) NULL DEFAULT NULL,
  year_amt_ppt decimal(18, 4) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.trs_guoku_analysis_processing (
  id int NULL DEFAULT NULL,
  ana_type varchar(10) NULL DEFAULT NULL,
  sql_delete text NULL,
  sql_insert text NULL,
  table_name varchar(50) NULL DEFAULT NULL,
  table_dscr varchar(100) NULL DEFAULT NULL,
  sql_insert_history text NULL,
  sql_delete_history text NULL,
  is_active char(1) NULL DEFAULT NULL,
  frequency_code char(1) NULL DEFAULT NULL,
  frequency_dscr varchar(50) NULL DEFAULT NULL,
  start_date char(10) NULL DEFAULT NULL,
  end_date char(10) NULL DEFAULT NULL,
  add_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS adm.trs_guoku_analysis_processing_20190328 (
  id int NULL DEFAULT NULL,
  ana_type varchar(10) NULL DEFAULT NULL,
  sql_delete text NULL,
  sql_insert text NULL,
  table_name varchar(50) NULL DEFAULT NULL,
  table_dscr varchar(100) NULL DEFAULT NULL,
  sql_insert_history text NULL,
  sql_delete_history text NULL,
  is_active char(1) NULL DEFAULT NULL,
  frequency_code char(1) NULL DEFAULT NULL,
  frequency_dscr varchar(50) NULL DEFAULT NULL,
  start_date char(10) NULL DEFAULT NULL,
  end_date char(10) NULL DEFAULT NULL,
  add_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS adm.trs_guoku_analysis_processing_shenyang (
  id int NULL DEFAULT NULL,
  ana_type varchar(10) NULL DEFAULT NULL,
  sql_delete text NULL,
  sql_insert text NULL,
  table_name varchar(50) NULL DEFAULT NULL,
  table_dscr varchar(100) NULL DEFAULT NULL,
  sql_insert_history text NULL,
  sql_delete_history text NULL,
  is_active char(1) NULL DEFAULT NULL,
  frequency_code char(1) NULL DEFAULT NULL,
  frequency_dscr varchar(50) NULL DEFAULT NULL,
  start_date char(10) NULL DEFAULT NULL,
  end_date char(10) NULL DEFAULT NULL,
  add_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS adm.trs_guoku_kettle (
  id serial NOT NULL,
  sql_insert text NULL,
  sql_second text NULL,
  sql_history text NULL,
  table_name varchar(100) NULL DEFAULT NULL,
  table_dscr varchar(255) NULL DEFAULT NULL,
  sql_dscr varchar(255) NULL DEFAULT NULL,
  is_active varchar(2) NULL DEFAULT NULL,
  add_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  add_by varchar(40) NULL DEFAULT NULL,
  mod_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  mod_by varchar(40) NULL DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS adm.trs_guoku_kettle_20190327 (
  id serial NOT NULL,
  sql_insert text NULL,
  sql_second text NULL,
  sql_history text NULL,
  table_name varchar(100) NULL DEFAULT NULL,
  table_dscr varchar(255) NULL DEFAULT NULL,
  sql_dscr varchar(255) NULL DEFAULT NULL,
  is_active varchar(2) NULL DEFAULT NULL,
  add_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  add_by varchar(40) NULL DEFAULT NULL,
  mod_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  mod_by varchar(40) NULL DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS adm.trs_guoku_saiku_processing (
  id int NULL DEFAULT NULL,
  sql_delete text NULL,
  sql_insert text NULL,
  table_name varchar(50) NULL DEFAULT NULL,
  table_dscr varchar(100) NULL DEFAULT NULL,
  sql_insert_history text NULL,
  sql_delete_history text NULL,
  is_active char(1) NULL DEFAULT NULL,
  start_date char(10) NULL DEFAULT NULL,
  end_date char(10) NULL DEFAULT NULL,
  add_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS adm.trs_kyd_enterprise (
  d_acct varchar(10) NOT NULL,
  s_trecode varchar(30) NOT NULL,
  s_tredscr varchar(50) NULL DEFAULT NULL,
  procode varchar(20) NULL DEFAULT NULL,
  proname varchar(255) NULL DEFAULT NULL,
  f_amt_101 decimal(18, 4) NULL DEFAULT NULL,
  f_amt_101_year decimal(18, 4) NULL DEFAULT NULL,
  f_amt_1010101 decimal(18, 4) NULL DEFAULT NULL,
  f_amt_1010101_year decimal(18, 4) NULL DEFAULT NULL,
  f_amt_1010201 decimal(18, 4) NULL DEFAULT NULL,
  f_amt_1010201_year decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10104 decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10104_year decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10106 decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10106_year decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10107 decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10107_year decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10109 decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10109_year decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10110 decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10110_year decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10111 decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10111_year decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10112 decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10112_year decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10113 decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10113_year decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10116 decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10116_year decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10114 decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10114_year decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10118 decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10118_year decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10119 decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10119_year decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10120 decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10120_year decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10121 decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10121_year decimal(18, 4) NULL DEFAULT NULL,
  f_amt_99999 decimal(18, 4) NULL DEFAULT NULL,
  f_amt_99999_year decimal(18, 4) NULL DEFAULT NULL,
  mark char(1) NOT NULL
);

CREATE TABLE IF NOT EXISTS adm.trs_kyd_enterprise_rank (
  d_acct varchar(10) NOT NULL,
  procode varchar(20) NULL DEFAULT NULL,
  proname varchar(255) NULL DEFAULT NULL,
  f_amt decimal(18, 4) NULL DEFAULT NULL,
  f_amt_tb decimal(18, 4) NULL DEFAULT NULL,
  f_amt_year decimal(18, 4) NULL DEFAULT NULL,
  f_amt_year_tb decimal(18, 4) NULL DEFAULT NULL,
  lev_1_id varchar(50) NULL DEFAULT NULL,
  lev_1_dscr varchar(50) NULL DEFAULT NULL,
  lev_2_id varchar(10) NULL DEFAULT NULL,
  lev_2_dscr varchar(100) NULL DEFAULT NULL,
  lev_4_id varchar(10) NULL DEFAULT NULL,
  lev_4_dscr varchar(100) NULL DEFAULT NULL,
  s_trecode varchar(30) NOT NULL,
  s_tredscr varchar(50) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.trs_kyd_enterprise_rank_copy (
  d_acct varchar(10) NULL DEFAULT NULL,
  procode varchar(20) NULL DEFAULT NULL,
  proname varchar(50) NULL DEFAULT NULL,
  f_amt decimal(18, 4) NULL DEFAULT NULL,
  f_amt_tb decimal(18, 4) NULL DEFAULT NULL,
  f_amt_year decimal(18, 4) NULL DEFAULT NULL,
  f_amt_year_tb decimal(18, 4) NULL DEFAULT NULL,
  lev_2_id varchar(10) NULL DEFAULT NULL,
  lev_2_dscr varchar(100) NULL DEFAULT NULL,
  lev_4_id varchar(10) NULL DEFAULT NULL,
  lev_4_dscr varchar(100) NULL DEFAULT NULL,
  s_trecode varchar(30) NULL DEFAULT NULL,
  s_tredscr varchar(50) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.trs_kyd_industry (
  d_acct varchar(10) NOT NULL,
  s_trecode varchar(30) NOT NULL,
  s_tredscr varchar(50) NULL DEFAULT NULL,
  procode varchar(20) NULL DEFAULT NULL,
  proname varchar(255) NULL DEFAULT NULL,
  f_amt_101 decimal(18, 4) NULL DEFAULT NULL,
  f_amt_101_year decimal(18, 4) NULL DEFAULT NULL,
  f_amt_1010101 decimal(18, 4) NULL DEFAULT NULL,
  f_amt_1010101_year decimal(18, 4) NULL DEFAULT NULL,
  f_amt_1010201 decimal(18, 4) NULL DEFAULT NULL,
  f_amt_1010201_year decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10104 decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10104_year decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10106 decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10106_year decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10107 decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10107_year decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10109 decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10109_year decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10110 decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10110_year decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10111 decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10111_year decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10112 decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10112_year decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10113 decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10113_year decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10116 decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10116_year decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10114 decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10114_year decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10118 decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10118_year decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10119 decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10119_year decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10120 decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10120_year decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10121 decimal(18, 4) NULL DEFAULT NULL,
  f_amt_10121_year decimal(18, 4) NULL DEFAULT NULL,
  f_amt_99999 decimal(18, 4) NULL DEFAULT NULL,
  f_amt_99999_year decimal(18, 4) NULL DEFAULT NULL,
  mark char(1) NOT NULL
);

CREATE TABLE IF NOT EXISTS adm.trs_kyd_industry_comprehensive (
  d_acct varchar(10) NULL DEFAULT NULL,
  s_trecode char(10) NULL DEFAULT NULL,
  s_tredscr varchar(50) NULL DEFAULT NULL,
  area_no varchar(10) NULL DEFAULT NULL,
  area_dscr varchar(20) NULL DEFAULT NULL,
  ent_code varchar(50) NULL DEFAULT NULL,
  ent_name varchar(50) NULL DEFAULT NULL,
  procode varchar(20) NULL DEFAULT NULL,
  proname varchar(50) NULL DEFAULT NULL,
  s_bdgsbtcode varchar(20) NULL DEFAULT NULL,
  s_bdgsbtname varchar(50) NULL DEFAULT NULL,
  f_amt decimal(18, 4) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.trs_kyd_industry_mb (
  "key" varchar(255) NULL DEFAULT NULL,
  name varchar(255) NULL DEFAULT NULL,
  sort int NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.trs_sk_acct_stock (
  reportdate date NULL DEFAULT NULL,
  d_month char(7) NULL DEFAULT NULL,
  d_year int NULL DEFAULT NULL,
  guoku_id char(10) NULL DEFAULT NULL,
  account_code varchar(32) NULL DEFAULT NULL,
  account_name varchar(60) NULL DEFAULT NULL,
  f_yesterdaybalance decimal(18, 2) NULL DEFAULT NULL,
  f_todayreceipt decimal(18, 2) NULL DEFAULT NULL,
  f_todaypay decimal(18, 2) NULL DEFAULT NULL,
  f_todaybalance decimal(18, 2) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.trs_sk_agentbankpay_back_detail (
  s_entrustyrar char(4) NULL DEFAULT NULL,
  s_entrustmoth char(6) NULL DEFAULT NULL,
  s_entrustdate char(8) NULL DEFAULT NULL,
  s_trecode char(10) NULL DEFAULT NULL,
  s_paymode char(1) NULL DEFAULT NULL,
  s_agentbankclass varchar(12) NULL DEFAULT NULL,
  s_bdgorgtrecode varchar(38) NULL DEFAULT NULL,
  s_expfunccode varchar(30) NULL DEFAULT NULL,
  f_payamt decimal(15, 2) NULL DEFAULT NULL,
  f_amount varchar(1) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.trs_sk_agentbankpay_detail (
  s_entrustyrar char(4) NULL DEFAULT NULL,
  s_entrustmoth char(6) NULL DEFAULT NULL,
  s_entrustdate char(8) NULL DEFAULT NULL,
  s_trecode char(10) NULL DEFAULT NULL,
  s_paymode char(1) NULL DEFAULT NULL,
  s_agentbankclass varchar(12) NULL DEFAULT NULL,
  s_bdgorgtrecode varchar(38) NULL DEFAULT NULL,
  s_expfunccode varchar(30) NULL DEFAULT NULL,
  c_autoauditstate varchar(30) NULL DEFAULT NULL,
  c_handauditstate varchar(30) NULL DEFAULT NULL,
  s_auditreason varchar(100) NULL DEFAULT NULL,
  f_payamt decimal(15, 2) NULL DEFAULT NULL,
  f_amount varchar(1) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.trs_sk_subject_dwbk (
  reportdate date NULL DEFAULT NULL,
  d_month char(7) NULL DEFAULT NULL,
  d_year int NULL DEFAULT NULL,
  guoku_id char(10) NULL DEFAULT NULL,
  subject_code_4 varchar(10) NULL DEFAULT NULL,
  budgetlevel_code char(1) NULL DEFAULT NULL,
  budgetlevel_dscr varchar(10) NULL DEFAULT NULL,
  f_dayamt decimal(18, 2) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.trs_sk_subject_income (
  reportdate date NULL DEFAULT NULL,
  d_month char(7) NULL DEFAULT NULL,
  d_year int NULL DEFAULT NULL,
  guoku_id char(10) NULL DEFAULT NULL,
  subject_code_4 varchar(10) NULL DEFAULT NULL,
  budgetlevel_code char(1) NULL DEFAULT NULL,
  budgetlevel_dscr varchar(10) NULL DEFAULT NULL,
  f_dayamt decimal(18, 2) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.trs_sk_subject_payout (
  reportdate date NULL DEFAULT NULL,
  d_month char(7) NULL DEFAULT NULL,
  d_year int NULL DEFAULT NULL,
  guoku_id char(10) NULL DEFAULT NULL,
  subject_code_4 varchar(10) NULL DEFAULT NULL,
  f_dayamt decimal(18, 2) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.trs_stat_agentbankpay_back_detail (
  s_seqno varchar(100) NULL DEFAULT NULL,
  s_id varchar(38) NULL DEFAULT NULL,
  s_admdivcode varchar(9) NULL DEFAULT NULL,
  s_styear char(4) NULL DEFAULT NULL,
  s_bookorgcode char(12) NULL DEFAULT NULL,
  s_trecode char(100) NULL DEFAULT NULL,
  s_entrustdate char(8) NULL DEFAULT NULL,
  s_agentbankclass varchar(12) NULL DEFAULT NULL,
  s_agentbankno char(12) NULL DEFAULT NULL,
  s_agentbankname varchar(120) NULL DEFAULT NULL,
  s_payoutvoutype char(1) NULL DEFAULT NULL,
  s_paymode char(1) NULL DEFAULT NULL,
  s_backtype char(1) NULL DEFAULT NULL,
  s_voucherno varchar(42) NULL DEFAULT NULL,
  s_orivoucherno varchar(42) NULL DEFAULT NULL,
  s_oripayvoudate date NULL DEFAULT NULL,
  s_fundtypecode char(1) NULL DEFAULT NULL,
  s_bdgorgcode varchar(18) NULL DEFAULT NULL,
  s_bdgorgname varchar(120) NULL DEFAULT NULL,
  s_expfunccode varchar(30) NULL DEFAULT NULL,
  s_expfuncname varchar(120) NULL DEFAULT NULL,
  s_expecocode varchar(30) NULL DEFAULT NULL,
  s_expeconame varchar(120) NULL DEFAULT NULL,
  s_projecttypecode varchar(42) NULL DEFAULT NULL,
  s_projecttypename varchar(400) NULL DEFAULT NULL,
  s_orizeroacctno varchar(32) NULL DEFAULT NULL,
  s_orizeroacctname varchar(120) NULL DEFAULT NULL,
  s_orizeroopnbnkname varchar(120) NULL DEFAULT NULL,
  s_oripayeeacctno varchar(32) NULL DEFAULT NULL,
  s_oripayeeacctname varchar(120) NULL DEFAULT NULL,
  s_oripayeeopnbnkname varchar(120) NULL DEFAULT NULL,
  s_oripayeeopnbnkno varchar(12) NULL DEFAULT NULL,
  s_oriclearacctno varchar(32) NULL DEFAULT NULL,
  s_oriclearacctname varchar(120) NULL DEFAULT NULL,
  s_oriclearbankno char(12) NULL DEFAULT NULL,
  s_oriclearbankname varchar(120) NULL DEFAULT NULL,
  s_remark varchar(512) NULL DEFAULT NULL,
  f_payamt decimal(15, 2) NULL DEFAULT NULL,
  c_isaddplan varchar(20) NULL DEFAULT NULL,
  c_checkresult varchar(20) NULL DEFAULT NULL,
  s_hold1 varchar(512) NULL DEFAULT NULL,
  s_hold2 varchar(512) NULL DEFAULT NULL,
  s_hold3 varchar(512) NULL DEFAULT NULL,
  s_hold4 varchar(512) NULL DEFAULT NULL,
  ts_sysupdate varchar(50) NULL DEFAULT NULL,
  c_autoauditstate varchar(20) NULL DEFAULT NULL,
  t_autoaudittime varchar(50) NOT NULL,
  s_auditreason varchar(512) NULL DEFAULT NULL,
  c_handauditstate varchar(20) NULL DEFAULT NULL,
  t_handaudittime varchar(50) NOT NULL,
  s_handreason varchar(512) NULL DEFAULT NULL,
  id serial NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS adm.trs_stat_agentbankpay_back_detail_bak (
  s_seqno varchar(20) NOT NULL,
  s_id varchar(38) NULL DEFAULT NULL,
  s_admdivcode varchar(9) NULL DEFAULT NULL,
  s_styear char(4) NULL DEFAULT NULL,
  s_bookorgcode char(12) NULL DEFAULT NULL,
  s_trecode char(10) NULL DEFAULT NULL,
  s_entrustdate char(8) NULL DEFAULT NULL,
  s_agentbankclass varchar(12) NULL DEFAULT NULL,
  s_agentbankno char(12) NULL DEFAULT NULL,
  s_agentbankname varchar(120) NULL DEFAULT NULL,
  s_payoutvoutype char(1) NULL DEFAULT NULL,
  s_paymode char(1) NULL DEFAULT NULL,
  s_backtype char(1) NULL DEFAULT NULL,
  s_voucherno varchar(42) NULL DEFAULT NULL,
  s_orivoucherno varchar(42) NULL DEFAULT NULL,
  s_oripayvoudate date NULL DEFAULT NULL,
  s_fundtypecode char(1) NULL DEFAULT NULL,
  s_bdgorgcode varchar(18) NULL DEFAULT NULL,
  s_bdgorgname varchar(120) NULL DEFAULT NULL,
  s_expfunccode varchar(30) NULL DEFAULT NULL,
  s_expfuncname varchar(120) NULL DEFAULT NULL,
  s_expecocode varchar(30) NULL DEFAULT NULL,
  s_expeconame varchar(120) NULL DEFAULT NULL,
  s_projecttypecode varchar(42) NULL DEFAULT NULL,
  s_projecttypename varchar(400) NULL DEFAULT NULL,
  s_orizeroacctno varchar(32) NULL DEFAULT NULL,
  s_orizeroacctname varchar(120) NULL DEFAULT NULL,
  s_orizeroopnbnkname varchar(120) NULL DEFAULT NULL,
  s_oripayeeacctno varchar(32) NULL DEFAULT NULL,
  s_oripayeeacctname varchar(120) NULL DEFAULT NULL,
  s_oripayeeopnbnkname varchar(120) NULL DEFAULT NULL,
  s_oripayeeopnbnkno varchar(12) NULL DEFAULT NULL,
  s_oriclearacctno varchar(32) NULL DEFAULT NULL,
  s_oriclearacctname varchar(120) NULL DEFAULT NULL,
  s_oriclearbankno char(12) NULL DEFAULT NULL,
  s_oriclearbankname varchar(120) NULL DEFAULT NULL,
  s_remark varchar(512) NULL DEFAULT NULL,
  f_payamt decimal(15, 2) NULL DEFAULT NULL,
  c_isaddplan varchar(20) NULL DEFAULT NULL,
  c_checkresult varchar(20) NULL DEFAULT NULL,
  s_hold1 varchar(512) NULL DEFAULT NULL,
  s_hold2 varchar(512) NULL DEFAULT NULL,
  s_hold3 varchar(512) NULL DEFAULT NULL,
  s_hold4 varchar(512) NULL DEFAULT NULL,
  ts_sysupdate timestamp(0) NULL DEFAULT NULL,
  c_autoauditstate varchar(20) NULL DEFAULT NULL,
  t_autoaudittime timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  s_auditreason varchar(512) NULL DEFAULT NULL,
  c_handauditstate varchar(20) NULL DEFAULT NULL,
  t_handaudittime timestamp(0) NOT NULL,
  s_handreason varchar(512) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.trs_stat_agentbankpay_detail (
  s_seqno varchar(100) NULL DEFAULT NULL,
  s_id varchar(38) NULL DEFAULT NULL,
  s_admdivcode varchar(9) NULL DEFAULT NULL,
  s_styear char(4) NULL DEFAULT NULL,
  s_bookorgcode char(12) NULL DEFAULT NULL,
  s_trecode char(50) NULL DEFAULT NULL,
  s_entrustdate char(8) NULL DEFAULT NULL,
  s_agentbankclass varchar(12) NULL DEFAULT NULL,
  s_agentbankno char(12) NULL DEFAULT NULL,
  s_agentbankname varchar(120) NULL DEFAULT NULL,
  s_payoutvoutype char(20) NULL DEFAULT NULL,
  s_paymode char(20) NULL DEFAULT NULL,
  d_payvoudate date NULL DEFAULT NULL,
  s_voucherno varchar(42) NULL DEFAULT NULL,
  s_fundtypecode char(20) NULL DEFAULT NULL,
  s_bdgorgcode varchar(18) NULL DEFAULT NULL,
  s_bdgorgname varchar(120) NULL DEFAULT NULL,
  s_expfunccode varchar(30) NULL DEFAULT NULL,
  s_expfuncname varchar(120) NULL DEFAULT NULL,
  s_expecocode varchar(30) NULL DEFAULT NULL,
  s_expeconame varchar(120) NULL DEFAULT NULL,
  s_projecttypecode varchar(42) NULL DEFAULT NULL,
  s_projecttypename varchar(400) NULL DEFAULT NULL,
  s_zeroacctno varchar(32) NULL DEFAULT NULL,
  s_zeroacctname varchar(120) NULL DEFAULT NULL,
  s_zeroopnbnkname varchar(120) NULL DEFAULT NULL,
  s_payeeacctno varchar(32) NULL DEFAULT NULL,
  s_payeeacctname varchar(120) NULL DEFAULT NULL,
  s_payeeopnbnkno varchar(12) NULL DEFAULT NULL,
  s_payeeopnbnkname varchar(120) NULL DEFAULT NULL,
  s_clearacctno varchar(32) NULL DEFAULT NULL,
  s_clearacctname varchar(120) NULL DEFAULT NULL,
  s_clearbankno char(12) NULL DEFAULT NULL,
  s_clearbankname varchar(120) NULL DEFAULT NULL,
  s_remark varchar(512) NULL DEFAULT NULL,
  f_payamt decimal(15, 2) NULL DEFAULT NULL,
  c_autoauditstate char(50) NULL DEFAULT NULL,
  t_autoaudittime varchar(50) NOT NULL,
  s_auditreason varchar(512) NULL DEFAULT NULL,
  c_handauditstate char(20) NULL DEFAULT NULL,
  t_handaudittime varchar(40) NOT NULL,
  s_handreason varchar(512) NULL DEFAULT NULL,
  c_handauditflag char(20) NULL DEFAULT NULL,
  c_isdedplan char(20) NULL DEFAULT NULL,
  c_checkresult char(20) NULL DEFAULT NULL,
  s_hold1 varchar(512) NULL DEFAULT NULL,
  s_hold2 varchar(512) NULL DEFAULT NULL,
  s_hold3 varchar(512) NULL DEFAULT NULL,
  s_hold4 varchar(512) NULL DEFAULT NULL,
  ts_sysupdate varchar(50) NOT NULL,
  id serial NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS adm.trs_stock_change_daily (
  d_reportdate date NOT NULL,
  s_trecode char(10) NOT NULL,
  guoku_dscr varchar(200) NULL DEFAULT NULL,
  s_acctcode varchar(32) NOT NULL,
  s_acctname varchar(100) NULL DEFAULT NULL,
  d_acctdate date NULL DEFAULT NULL,
  sub_acctcode varchar(5) NULL DEFAULT NULL,
  level int NULL DEFAULT NULL,
  this_period_amt decimal(40, 2) NULL DEFAULT NULL,
  last_period_amt decimal(40, 2) NULL DEFAULT NULL,
  update_date datetime(0) NOT NULL
);

CREATE TABLE IF NOT EXISTS adm.trs_stock_change_daily_20190328 (
  d_reportdate date NOT NULL,
  s_trecode char(10) NOT NULL,
  guoku_dscr varchar(200) NULL DEFAULT NULL,
  s_acctcode varchar(32) NOT NULL,
  s_acctname varchar(100) NULL DEFAULT NULL,
  d_acctdate date NULL DEFAULT NULL,
  sub_acctcode varchar(5) NULL DEFAULT NULL,
  level int NULL DEFAULT NULL,
  this_period_amt decimal(40, 2) NULL DEFAULT NULL,
  last_period_amt decimal(40, 2) NULL DEFAULT NULL,
  update_date datetime(0) NOT NULL
);

CREATE TABLE IF NOT EXISTS adm.trs_stock_change_monthly (
  d_reportdate varchar(7) NULL DEFAULT NULL,
  s_trecode char(10) NOT NULL,
  guoku_dscr varchar(200) NULL DEFAULT NULL,
  s_acctcode varchar(32) NOT NULL,
  s_acctname varchar(100) NULL DEFAULT NULL,
  d_acctdate date NULL DEFAULT NULL,
  sub_acctcode varchar(5) NULL DEFAULT NULL,
  level int NULL DEFAULT NULL,
  this_period_amt decimal(40, 2) NULL DEFAULT NULL,
  last_period_amt decimal(40, 2) NULL DEFAULT NULL,
  update_date datetime(0) NOT NULL
);

CREATE TABLE IF NOT EXISTS adm.trs_stock_change_yearly (
  d_reportdate int NULL DEFAULT NULL,
  s_trecode char(10) NOT NULL,
  guoku_dscr varchar(200) NULL DEFAULT NULL,
  s_acctcode varchar(32) NOT NULL,
  s_acctname varchar(100) NULL DEFAULT NULL,
  d_acctdate date NULL DEFAULT NULL,
  sub_acctcode varchar(5) NULL DEFAULT NULL,
  level int NULL DEFAULT NULL,
  this_period_amt decimal(40, 2) NULL DEFAULT NULL,
  last_period_amt decimal(40, 2) NULL DEFAULT NULL,
  update_date datetime(0) NOT NULL
);

CREATE TABLE IF NOT EXISTS adm.trs_stock_influence_income_daily (
  d_reportdate date NOT NULL,
  s_trecode char(10) NOT NULL,
  guoku_dscr varchar(200) NULL DEFAULT NULL,
  s_budgetsubjectcode varchar(30) NOT NULL,
  s_budgetsubjectname varchar(60) NOT NULL,
  this_period_amt decimal(40, 2) NULL DEFAULT NULL,
  last_period_amt decimal(40, 2) NULL DEFAULT NULL,
  this_period_yearamt decimal(40, 2) NULL DEFAULT NULL,
  last_period_yearamt decimal(40, 2) NULL DEFAULT NULL,
  update_date datetime(0) NOT NULL
);

CREATE TABLE IF NOT EXISTS adm.trs_stock_influence_income_monthly (
  d_reportdate varchar(7) NULL DEFAULT NULL,
  s_trecode char(10) NOT NULL,
  guoku_dscr varchar(200) NULL DEFAULT NULL,
  s_budgetsubjectcode varchar(30) NOT NULL,
  s_budgetsubjectname varchar(60) NOT NULL,
  this_period_amt decimal(40, 2) NULL DEFAULT NULL,
  last_period_amt decimal(40, 2) NULL DEFAULT NULL,
  this_period_yearamt decimal(40, 2) NULL DEFAULT NULL,
  last_period_yearamt decimal(40, 2) NULL DEFAULT NULL,
  update_date datetime(0) NOT NULL
);

CREATE TABLE IF NOT EXISTS adm.trs_stock_influence_income_yearly (
  d_reportdate int NULL DEFAULT NULL,
  s_trecode char(10) NOT NULL,
  guoku_dscr varchar(200) NULL DEFAULT NULL,
  s_budgetsubjectcode varchar(30) NOT NULL,
  s_budgetsubjectname varchar(60) NOT NULL,
  this_period_amt decimal(40, 2) NULL DEFAULT NULL,
  last_period_amt decimal(40, 2) NULL DEFAULT NULL,
  update_date datetime(0) NOT NULL
);

CREATE TABLE IF NOT EXISTS adm.trs_stock_influence_payout_daily (
  d_reportdate date NOT NULL,
  s_trecode char(10) NOT NULL,
  guoku_dscr varchar(200) NULL DEFAULT NULL,
  s_budgetsubjectcode varchar(30) NOT NULL,
  s_budgetsubjectname varchar(60) NOT NULL,
  this_period_amt decimal(40, 2) NULL DEFAULT NULL,
  last_period_amt decimal(40, 2) NULL DEFAULT NULL,
  this_period_yearamt decimal(40, 2) NULL DEFAULT NULL,
  last_period_yearamt decimal(40, 2) NULL DEFAULT NULL,
  update_date datetime(0) NOT NULL
);

CREATE TABLE IF NOT EXISTS adm.trs_stock_influence_payout_monthly (
  d_reportdate varchar(7) NULL DEFAULT NULL,
  s_trecode char(10) NOT NULL,
  guoku_dscr varchar(200) NULL DEFAULT NULL,
  s_budgetsubjectcode varchar(30) NOT NULL,
  s_budgetsubjectname varchar(60) NOT NULL,
  this_period_amt decimal(40, 2) NULL DEFAULT NULL,
  last_period_amt decimal(40, 2) NULL DEFAULT NULL,
  this_period_yearamt decimal(40, 2) NULL DEFAULT NULL,
  last_period_yearamt decimal(40, 2) NULL DEFAULT NULL,
  update_date datetime(0) NOT NULL
);

CREATE TABLE IF NOT EXISTS adm.trs_stock_influence_payout_yearly (
  d_reportdate int NULL DEFAULT NULL,
  s_trecode char(10) NOT NULL,
  guoku_dscr varchar(200) NULL DEFAULT NULL,
  s_budgetsubjectcode varchar(30) NOT NULL,
  s_budgetsubjectname varchar(60) NOT NULL,
  this_period_amt decimal(40, 2) NULL DEFAULT NULL,
  last_period_amt decimal(40, 2) NULL DEFAULT NULL,
  update_date datetime(0) NOT NULL
);

CREATE TABLE IF NOT EXISTS adm.v_area_code (
  area_no_id_1 char(8) NULL DEFAULT NULL,
  area_dscr_1 varchar(512) NULL DEFAULT NULL,
  area_no_id_2 char(8) NULL DEFAULT NULL,
  area_dscr_2 varchar(512) NULL DEFAULT NULL,
  area_no_id char(8) NOT NULL,
  area_dscr varchar(512) NOT NULL,
  level char(8) NULL DEFAULT NULL,
  lvl_ord char(8) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.v_cm_guoku_agentbankclass (
  agentbankno varchar(50) NOT NULL,
  agentbankname varchar(200) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.v_cm_guoku_autoauditstate (
  c_autoauditstate varchar(2) NOT NULL,
  c_autoauditstatedscr varchar(64) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.v_cm_guoku_bdgorg (
  s_bookorgcode varchar(40) NOT NULL DEFAULT '',
  s_trecode varchar(10) NOT NULL DEFAULT '',
  s_bdgorgcode varchar(15) NOT NULL DEFAULT '',
  s_bdgorgname varchar(64) NULL DEFAULT NULL,
  s_bdgorgtrecode varchar(32) NULL DEFAULT NULL,
  legal_code varchar(12) NULL DEFAULT NULL,
  isrealdial char(1) NULL DEFAULT NULL,
  unit_nature varchar(10) NULL DEFAULT NULL,
  register_type varchar(10) NULL DEFAULT NULL,
  industry_nature varchar(10) NULL DEFAULT NULL,
  acount_num varchar(10) NULL DEFAULT NULL,
  createtime timestamp(0) NOT NULL
);

CREATE TABLE IF NOT EXISTS adm.v_cm_guoku_dimnsn (
  guoku_id varchar(50) NOT NULL,
  guoku_dscr varchar(100) NULL DEFAULT NULL,
  guoku_shuxing_id varchar(64) NULL DEFAULT NULL,
  guoku_shuxing_dscr varchar(64) NULL DEFAULT NULL,
  level int NULL DEFAULT NULL,
  level_dscr varchar(64) NULL DEFAULT NULL,
  guoku_lvl_id_1 varchar(50) NULL DEFAULT NULL,
  guoku_lvl_dscr_1 varchar(200) NULL DEFAULT NULL,
  guoku_lvl_id_2 varchar(50) NULL DEFAULT NULL,
  guoku_lvl_dscr_2 varchar(200) NULL DEFAULT NULL,
  guoku_lvl_id_3 varchar(50) NULL DEFAULT NULL,
  guoku_lvl_dscr_3 varchar(200) NULL DEFAULT NULL,
  area_no_id varchar(20) NULL DEFAULT NULL,
  area_dscr varchar(200) NULL DEFAULT NULL,
  old_guoku_id varchar(50) NULL DEFAULT NULL,
  old_guoku_name varchar(500) NULL DEFAULT NULL,
  bookorgcode varchar(50) NULL DEFAULT NULL,
  bookorgname varchar(100) NULL DEFAULT NULL,
  pay_bnk_no varchar(40) NULL DEFAULT NULL,
  bookorgcode_1 varchar(50) NULL DEFAULT NULL,
  bookorgname_1 varchar(200) NULL DEFAULT NULL,
  bookorgcode_2 varchar(50) NULL DEFAULT NULL,
  bookorgname_2 varchar(200) NULL DEFAULT NULL,
  bookorgcode_3 varchar(50) NULL DEFAULT NULL,
  bookorgname_3 varchar(200) NULL DEFAULT NULL,
  bookorg_level varchar(50) NULL DEFAULT NULL,
  start_date varchar(20) NULL DEFAULT NULL,
  end_date varchar(20) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.v_cm_guoku_handauditstate (
  c_handauditstate varchar(2) NOT NULL,
  c_handauditstatedscr varchar(64) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.v_cm_guoku_paymode (
  s_paymode varchar(1) NOT NULL,
  s_paymodedscr varchar(200) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.v_cm_guoku_rcpt_dsb (
  subject_code_0 varchar(4) NOT NULL,
  subject_dscr_0 varchar(16) NOT NULL,
  subject_code_1 varchar(10) NOT NULL,
  subject_dscr_1 varchar(32) NOT NULL,
  subject_code_2 varchar(10) NOT NULL,
  subject_dscr_2 varchar(32) NOT NULL,
  subject_code_3 varchar(10) NOT NULL,
  subject_dscr_3 varchar(64) NOT NULL,
  subject_code_4 varchar(10) NOT NULL,
  subject_dscr_4 varchar(64) NULL DEFAULT NULL,
  level char(2) NULL DEFAULT NULL,
  is_finallevel char(1) NOT NULL,
  start_date char(10) NULL DEFAULT NULL,
  end_date char(10) NULL DEFAULT NULL,
  data_src_org char(5) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.v_cm_guoku_rcpt_dsb_income (
  subject_code_0 varchar(4) NOT NULL,
  subject_dscr_0 varchar(16) NOT NULL,
  subject_code_1 varchar(10) NOT NULL,
  subject_dscr_1 varchar(32) NOT NULL,
  subject_code_2 varchar(10) NOT NULL,
  subject_dscr_2 varchar(32) NOT NULL,
  subject_code_3 varchar(10) NOT NULL,
  subject_dscr_3 varchar(64) NOT NULL,
  subject_code_4 varchar(10) NOT NULL,
  subject_dscr_4 varchar(64) NULL DEFAULT NULL,
  level char(2) NULL DEFAULT NULL,
  is_finallevel char(1) NOT NULL,
  start_date char(10) NULL DEFAULT NULL,
  end_date char(10) NULL DEFAULT NULL,
  data_src_org char(5) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.v_cm_guoku_rcpt_dsb_payout (
  subject_code_0 varchar(4) NOT NULL,
  subject_dscr_0 varchar(16) NOT NULL,
  subject_code_1 varchar(10) NOT NULL,
  subject_dscr_1 varchar(32) NOT NULL,
  subject_code_2 varchar(10) NOT NULL,
  subject_dscr_2 varchar(32) NOT NULL,
  subject_code_3 varchar(10) NOT NULL,
  subject_dscr_3 varchar(64) NOT NULL,
  subject_code_4 varchar(10) NOT NULL,
  subject_dscr_4 varchar(64) NULL DEFAULT NULL,
  level char(2) NULL DEFAULT NULL,
  is_finallevel char(1) NOT NULL,
  start_date char(10) NULL DEFAULT NULL,
  end_date char(10) NULL DEFAULT NULL,
  data_src_org char(5) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.v_dim_date (
  pk_date int NULL DEFAULT NULL,
  day varchar(10) NULL DEFAULT NULL,
  day_name varchar(20) NULL DEFAULT NULL,
  year varchar(10) NULL DEFAULT NULL,
  year_name varchar(20) NULL DEFAULT NULL,
  semester varchar(10) NULL DEFAULT NULL,
  semester_name varchar(20) NULL DEFAULT NULL,
  quarter varchar(10) NULL DEFAULT NULL,
  quarter_name varchar(20) NULL DEFAULT NULL,
  month varchar(10) NULL DEFAULT NULL,
  month_name varchar(20) NULL DEFAULT NULL,
  ten_days varchar(10) NULL DEFAULT NULL,
  ten_days_name varchar(20) NULL DEFAULT NULL,
  week varchar(10) NULL DEFAULT NULL,
  week_name varchar(20) NULL DEFAULT NULL,
  weekday varchar(10) NULL DEFAULT NULL,
  weekday_name varchar(20) NULL DEFAULT NULL,
  workday_flag varchar(10) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.v_py_cx_area_code (
  area_no_id_2 char(6) NULL DEFAULT NULL,
  area_dscr_2 varchar(30) NULL DEFAULT NULL,
  area_no_id_3 char(6) NULL DEFAULT NULL,
  area_dscr_3 varchar(30) NULL DEFAULT NULL,
  area_no_id_4 char(9) NULL DEFAULT NULL,
  area_dscr_4 varchar(100) NULL DEFAULT NULL,
  area_no_id_5 char(14) NULL DEFAULT NULL,
  area_dscr_5 varchar(100) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.v_py_cx_org_city_code (
  org_id char(14) NULL DEFAULT NULL,
  org_dscr varchar(512) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.v_py_cx_org_code (
  org_id1 varchar(40) NULL DEFAULT NULL,
  org_name1 varchar(40) NULL DEFAULT NULL,
  org_id varchar(40) NULL DEFAULT NULL,
  org_name varchar(40) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.v_py_cx_state_code (
  state_code varchar(10) NULL DEFAULT NULL,
  state_name varchar(10) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.v_py_cx_trade_code (
  trade_code varchar(10) NULL DEFAULT NULL,
  trade_type varchar(10) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.v_py_cx_verity_table (
  table_name varchar(100) NOT NULL,
  start_row varchar(11) NULL DEFAULT NULL,
  template_id varchar(50) NOT NULL,
  column_name varchar(100) NULL DEFAULT NULL,
  column_type varchar(20) NULL DEFAULT NULL,
  cell_number varchar(10) NULL DEFAULT NULL,
  up_load_table varchar(100) NULL DEFAULT NULL,
  target_description varchar(500) NULL DEFAULT NULL,
  compare_same_previous_begin double NULL DEFAULT NULL,
  compare_same_previous_end double NULL DEFAULT NULL,
  compare_chain_base_begin double NULL DEFAULT NULL,
  compare_chain_base_end double NULL DEFAULT NULL,
  compare_same_previous_fs_begin double NULL DEFAULT NULL,
  compare_same_previous_fs_end double NULL DEFAULT NULL,
  compare_chain_base_fs_begin double NULL DEFAULT NULL,
  compare_chain_base_fs_end double NULL DEFAULT NULL,
  table_id varchar(50) NOT NULL,
  cell varchar(200) NULL DEFAULT NULL,
  "locate('*',c.cell)" bigint NULL DEFAULT NULL,
  dscr varchar(100) NULL DEFAULT NULL,
  where_ text NULL
);

CREATE TABLE IF NOT EXISTS adm.v_py_cx_verity_table_ods_data (
  area_no varchar(8) NULL DEFAULT NULL,
  data_date char(8) NULL DEFAULT NULL,
  org_no varchar(14) NULL DEFAULT NULL,
  org_id varchar(200) NULL DEFAULT NULL,
  add_date timestamp(0) NOT NULL,
  now_data decimal(28, 6) NULL DEFAULT NULL,
  hb_data decimal(28, 6) NULL DEFAULT NULL,
  tb_data decimal(28, 6) NULL DEFAULT NULL,
  hb_data_fs decimal(28, 6) NULL DEFAULT NULL,
  tb_data_fs decimal(28, 6) NULL DEFAULT NULL,
  hb_rate decimal(32, 2) NOT NULL DEFAULT 0.00,
  tb_rate decimal(32, 2) NOT NULL DEFAULT 0.00,
  hb_rate_fs decimal(33, 2) NOT NULL DEFAULT 0.00,
  tb_rate_fs decimal(33, 2) NOT NULL DEFAULT 0.00,
  cell varchar(1) NOT NULL DEFAULT '',
  rows_id varchar(100) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.v_py_zf_dim_date (
  month varchar(10) NULL DEFAULT NULL,
  month_name varchar(20) NULL DEFAULT NULL,
  quarter varchar(10) NULL DEFAULT NULL,
  quarter_name varchar(20) NULL DEFAULT NULL,
  year varchar(10) NULL DEFAULT NULL,
  year_name varchar(20) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.v_sust_biz_category (
  biz_code varchar(30) NULL DEFAULT NULL,
  biz_dscr varchar(50) NULL DEFAULT NULL,
  type varchar(3) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.v_sust_cash_status (
  cs_code varchar(30) NULL DEFAULT NULL,
  cs_dscr varchar(50) NULL DEFAULT NULL,
  type varchar(3) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.v_sust_currency_type (
  type_code varchar(30) NULL DEFAULT NULL,
  type_dscr varchar(50) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.v_sust_data_category (
  data_c_code varchar(30) NULL DEFAULT NULL,
  data_c_dscr varchar(50) NULL DEFAULT NULL,
  type varchar(3) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.v_sust_enterprise_size (
  e_size_code varchar(30) NULL DEFAULT NULL,
  e_size_dscr varchar(50) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.v_sust_fixed_income (
  income_code varchar(30) NULL DEFAULT NULL,
  income_dscr varchar(50) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.v_sust_forward_buyback (
  fb_code varchar(30) NULL DEFAULT NULL,
  fb_dscr varchar(50) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.v_sust_guaranty_style (
  gs_code varchar(30) NULL DEFAULT NULL,
  gs_dscr varchar(50) NULL DEFAULT NULL,
  type varchar(3) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.v_sust_industry (
  lev_1_id varchar(8) NULL DEFAULT NULL,
  lev_1_dscr varchar(64) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.v_sust_investment_style (
  code_3 varchar(31) NULL DEFAULT NULL,
  name_3 varchar(50) NULL DEFAULT NULL,
  code_2 bigint NOT NULL DEFAULT 0,
  name_2 varchar(4) NOT NULL DEFAULT '',
  code_1 bigint NOT NULL DEFAULT 0,
  name_1 varchar(6) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS adm.v_sust_investment_style_a02 (
  code_3 varchar(31) NULL DEFAULT NULL,
  name_3 varchar(50) NULL DEFAULT NULL,
  code_2 bigint NOT NULL DEFAULT 0,
  name_2 varchar(12) NOT NULL DEFAULT '',
  code_1 bigint NOT NULL DEFAULT 0,
  name_1 varchar(4) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS adm.v_sust_joint_investment (
  code varchar(30) NULL DEFAULT NULL,
  dscr varchar(50) NULL DEFAULT NULL,
  type varchar(3) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.v_sust_ownership (
  os_code varchar(30) NULL DEFAULT NULL,
  os_dscr varchar(50) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.v_sust_payment_status (
  status_code varchar(30) NULL DEFAULT NULL,
  status_dscr varchar(50) NULL DEFAULT NULL,
  type varchar(3) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.v_sust_term_category (
  tc_code varchar(30) NULL DEFAULT NULL,
  tc_dscr varchar(50) NULL DEFAULT NULL,
  type varchar(3) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.v_sust_time_interval (
  ti_code varchar(30) NULL DEFAULT NULL,
  ti_dscr varchar(50) NULL DEFAULT NULL,
  type varchar(3) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.v_t_area_code (
  area_no_id_1 char(8) NULL DEFAULT NULL,
  area_dscr_1 varchar(512) NULL DEFAULT NULL,
  area_no_id_2 char(8) NULL DEFAULT NULL,
  area_dscr_2 varchar(512) NULL DEFAULT NULL,
  area_no_id char(8) NOT NULL,
  area_dscr varchar(512) NOT NULL,
  city_village_flag varchar(1) NULL DEFAULT NULL,
  level char(8) NULL DEFAULT NULL,
  lvl_ord char(8) NULL DEFAULT NULL,
  area_dscr_s varchar(512) NULL DEFAULT NULL,
  start_date char(8) NULL DEFAULT NULL,
  end_date char(8) NULL DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS adm.v_t_org_biz_lvl (
  org_type_id_1 varchar(14) NULL DEFAULT NULL,
  org_type_dscr_1 varchar(512) NULL DEFAULT NULL,
  org_type_id_2 varchar(14) NULL DEFAULT NULL,
  org_type_dscr_2 varchar(512) NULL DEFAULT NULL,
  org_id_1 char(14) NULL DEFAULT NULL,
  org_dscr_1 varchar(512) NULL DEFAULT NULL,
  org_id_2 char(14) NULL DEFAULT NULL,
  org_dscr_2 varchar(512) NULL DEFAULT NULL,
  org_id_3 char(14) NULL DEFAULT NULL,
  org_dscr_3 varchar(512) NULL DEFAULT NULL,
  org_id_4 char(14) NULL DEFAULT NULL,
  org_dscr_4 varchar(512) NULL DEFAULT NULL,
  org_id_5 char(14) NULL DEFAULT NULL,
  org_dscr_5 varchar(512) NULL DEFAULT NULL,
  org_id char(14) NULL DEFAULT NULL,
  org_dscr varchar(512) NULL DEFAULT NULL,
  org_id_src char(14) NULL DEFAULT NULL,
  parent_org_id varchar(32) NULL DEFAULT NULL,
  parent_org_desc varchar(512) NULL DEFAULT NULL,
  level int NULL DEFAULT NULL,
  pbc_id char(14) NULL DEFAULT NULL,
  lvl_ord int NULL DEFAULT NULL,
  area_no_id varchar(12) NULL DEFAULT NULL,
  org_dept_mapping_flag varchar(12) NULL DEFAULT NULL,
  reserved_1 varchar(512) NULL DEFAULT NULL,
  reserved_2 varchar(512) NULL DEFAULT NULL,
  reserved_3 varchar(512) NULL DEFAULT NULL,
  reserved_4 varchar(512) NULL DEFAULT NULL,
  start_date char(8) NULL DEFAULT NULL,
  end_date char(8) NULL DEFAULT NULL,
  is_active char(2) NULL DEFAULT NULL
);

DROP VIEW IF EXISTS adm.sust_enterprise_view;
CREATE VIEW adm.sust_enterprise_view AS select distinct ana_sust_mth_enterprise_survey_temp.enterprise_id AS enterprise_id,ana_sust_mth_enterprise_survey_temp.enterprise_name AS enterprise_name from adm.ana_sust_mth_enterprise_survey_temp where (ana_sust_mth_enterprise_survey_temp.data_date = 202607);

-- ----------------------------;

DROP VIEW IF EXISTS adm.sust_standard_norm_view;
CREATE VIEW adm.sust_standard_norm_view AS select sust_standard_norm.norm_id AS norm_id,sust_standard_norm.norm_name AS norm_name from adm.sust_standard_norm order by sust_standard_norm.id;

-- ----------------------------;
