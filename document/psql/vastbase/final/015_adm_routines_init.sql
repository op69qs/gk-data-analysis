-- Generated from document/psql/mysql/adm.sql; do not edit by hand.
CREATE SCHEMA IF NOT EXISTS adm;
SET search_path TO adm, public;

DROP PROCEDURE IF EXISTS adm.p_ana_sust_mth_credit_rece_pay_area;

CREATE PROCEDURE adm.p_ana_sust_mth_credit_rece_pay_area(in V_DATA_DATE varchar(10))
AS
BEGIN
/*信贷收支总览   地图*/
delete FROM adm.ana_sust_mth_credit_rece_pay_area WHERE DATA_DATE=DATE_FORMAT(V_DATA_DATE,'%Y-%m-01');
insert into adm.ana_sust_mth_credit_rece_pay_area
SELECT
	DATE_FORMAT(CONCAT(a.data_date,'01'),'%Y-%m-01')      AS DATA_DATE,
	DATE_FORMAT(CONCAT(a.data_date,'01'),'%Y%m')          AS DATA_MTH,
	REVERSE(cast(REVERSE(b.area_id) as signed))           AS ISO_ID,
	b.area_dscr					      AS AREA_DSCR,
	100000000*(a.depb_bal)				      as DEPB_BAL,
	100000000*(a.loan_bal)				      as LOAN_BAL
FROM
	ods.glr_mpa_all_dist_depb_loan_dtl1 a
LEFT JOIN
	dmcode.t_area_code_temp b
ON a.org_name = b.area_dscr
WHERE a.data_date = date_format(V_DATA_DATE,'%Y%m');
	END;
/

DROP PROCEDURE IF EXISTS adm.p_ana_sust_mth_credit_rece_pay_struc;

CREATE PROCEDURE adm.p_ana_sust_mth_credit_rece_pay_struc(in v_data_date varchar(10))
AS
BEGIN
/*存贷主要项目在各期余额*/
DELETE FROM adm.ana_sust_mth_credit_rece_pay_struc WHERE DATA_DATE=DATE_FORMAT(V_DATA_DATE,'%Y-%m-01');
insert into adm.ana_sust_mth_credit_rece_pay_struc
SELECT
DATE_FORMAT(CONCAT(a.data_date,'01'),'%Y-%m-01')      AS DATA_DATE,
DATE_FORMAT(CONCAT(a.data_date,'01'),'%Y%m')          AS DATA_MTH,
b.service_type 					      AS SERVICE_TYPE,
b.item_category_code				      AS ITEM_CATEGORY_CODE,
b.item_category,
(a.depb_balance)*100000000			      AS DEPB_BALANCE
FROM ods.glr_mpa_all_cny_credit_dtl1 a
LEFT JOIN
adm.ana_sust_cm_service_type b
ON a.project1 LIKE CONCAT('%',b.item_category)
WHERE a.data_date = DATE_FORMAT(v_data_date,'%Y%m')
AND (a.project1 LIKE '%住户存款'
OR a.project1 LIKE '%非金融企业存款'
OR a.project1 LIKE '%机关团体存款'
OR a.project1 LIKE '%财政性存款'
OR a.project1 LIKE '%非银行业金融机构存款'
OR a.project1 LIKE '%境外存款')
UNION ALL
SELECT
DATE_FORMAT(CONCAT(a.data_date,'01'),'%Y-%m-01')      AS DATA_DATE,
DATE_FORMAT(CONCAT(a.data_date,'01'),'%Y%m')          AS DATA_MTH,
b.service_type 					      AS SERVICE_TYPE,
b.item_category_code				      AS ITEM_CATEGORY_CODE,
b.item_category,
(a.loan_balance)*100000000			      AS LOAN_BALANCE
FROM ods.glr_mpa_all_cny_credit_dtl1 a
LEFT JOIN
adm.ana_sust_cm_service_type b
ON a.project2 LIKE CONCAT('%',b.item_category)
WHERE a.data_date = DATE_FORMAT(v_data_date,'%Y%m')
AND (a.project2 LIKE '%住户贷款'
OR a.project2 LIKE '%非金融企业及机关团体贷款'
OR a.project2 LIKE '%非银行业金融机构贷款'
OR a.project2 LIKE '%境外贷款'
)
UNION ALL
SELECT
DATE_FORMAT(CONCAT(a.data_date,'01'),'%Y-%m-01')      AS DATA_DATE,
DATE_FORMAT(CONCAT(a.data_date,'01'),'%Y%m')          AS DATA_MTH,
b.service_type 					      AS SERVICE_TYPE,
b.item_category_code				      AS ITEM_CATEGORY_CODE,
b.item_category,
(a.balance)*100000000				      AS BALANCE
FROM ods.glr_eds_spe_data_indus_dtl1 a
LEFT JOIN
adm.ana_sust_cm_service_type b
ON a.project LIKE CONCAT('%',b.item_category)
WHERE a.data_date = DATE_FORMAT(v_data_date,'%Y%m')
UNION ALL
SELECT
DATE_FORMAT(CONCAT(a.data_date,'01'),'%Y-%m-01')      AS DATA_DATE,
DATE_FORMAT(CONCAT(a.data_date,'01'),'%Y%m')          AS DATA_MTH,
b.service_type 					      AS SERVICE_TYPE,
b.item_category_code				      AS ITEM_CATEGORY_CODE,
b.item_category,
(a.balance)*100000000				      AS BALANCE
FROM ods.sust_institutions_containing_foreign_capital a
JOIN
adm.ana_sust_cm_service_type b
ON a.project_desc LIKE CONCAT('%',b.item_category)
WHERE a.data_date = DATE_FORMAT(v_data_date,'%Y%m');
	END;
/

DROP PROCEDURE IF EXISTS adm.p_ana_sust_mth_credit_rece_pay_times;

CREATE PROCEDURE adm.p_ana_sust_mth_credit_rece_pay_times(in v_data_date varchar(10))
AS
BEGIN
/*信贷总览  收支时序表*/
DELETE FROM adm.ana_sust_mth_credit_rece_pay_times WHERE DATA_DATE=DATE_FORMAT(V_DATA_DATE,'%Y-%m-01');
insert into adm.ana_sust_mth_credit_rece_pay_times
SELECT
	DATE_FORMAT(CONCAT(c.data_date,'01'),'%Y-%m-01') 	 AS  data_date,        -- 数据账期
	DATE_FORMAT(CONCAT(c.data_date,'01'),'%Y%m')       	 AS  date_mth,	       -- 月份
	c.bwb_balance*100000000				         AS  DEP_BAL,	       -- 各项存款（本外币）本期余额（元）
	c.bwb_last_month*100000000				 AS  DEP_BAL_LM,       -- 存款比上月（元）
	c.bwb_year_on_year*100000000				 AS  DEP_BAL_LY_E,     -- 存款比年初（元）
	c.bwb_of_year/100					 AS  DEP_BAL_TB_RATE,  -- 存款同比增速
	d.bwb_balance*100000000					 AS  LOAN_BAL,         -- 各项贷款（本外币）本期余额（元）
	d.bwb_last_month*100000000				 AS  LOAN_BAL_LM,      -- 贷款比上月（元）
	d.bwb_year_on_year*100000000	   			 AS  LOAN_BAL_LY_E,    -- 贷款比年初（元）
	d.bwb_of_year/100					 AS  LOAN_BAL_TB_RATE  -- 贷款同比增速
FROM
	ods.major_deposit_items_new  c
JOIN ods.main_securities_projects d
ON c.rows_id = d.rows_id
WHERE c.data_date = date_format(V_DATA_DATE,'%Y%m')
and d.data_date = DATE_FORMAT(V_DATA_DATE,'%Y%m')
AND c.project = '各项存款'
AND d.project = '各项贷款';
	END;
/

DROP PROCEDURE IF EXISTS adm.p_ana_sust_mth_enterprise_survey;

CREATE PROCEDURE adm.p_ana_sust_mth_enterprise_survey(
IN V_DATA_DATE VARCHAR(6),
IN E_ID VARCHAR(8),
IN N_ID VARCHAR(5)
)
AS
	V_PROC_NAME     VARCHAR(80) := 'adm.p_ana_sust_mth_enterprise_survey.PRC';
	V_START_TIME    CHAR(19) := NOW();
	V_STEP_ID       INT := 0;
	P_DATA_DATE     VARCHAR(8) := V_DATA_DATE;
	DONE            INT := FALSE;
	V_NORM_ID       VARCHAR(100);
	V_NORM_NAME     VARCHAR(100);
	V_PLAN_ID       VARCHAR(100);
	ENT_ID          VARCHAR(100);
	ENT_NAME        VARCHAR(100);
	V_SQL_INSERT    LONGTEXT := '';
	CURSOR CUR_ENTERPRISE IS SELECT ENTERPRISE_ID,ENTERPRISE_name FROM adm.sust_enterprise_view where ENTERPRISE_name is not null;
	CURSOR CUR_NORM IS SELECT NORM_ID,NORM_NAME FROM adm.sust_standard_norm_view where norm_id is not null;
    V_RETURN_CODE TEXT;
    V_ERROR_MSG TEXT;
    v_at_DATA_DATE VARCHAR(2000);
    v_at_E_ID VARCHAR(2000);
    v_at_E_NAME VARCHAR(2000);
    v_at_NORM_ID VARCHAR(2000);
    v_at_NORM_NAME VARCHAR(2000);
    v_at_SQLSTR TEXT;
    v_at_SQLTXT TEXT;
    v_at_V_SQLTXT TEXT;
BEGIN




    -- NOT FOUND handler replaced by explicit cursor %NOTFOUND checks below.





	V_STEP_ID := 1;
	DROP VIEW IF EXISTS adm.sust_enterprise_view;

	V_STEP_ID := 2;
	v_at_SQLSTR := 'CREATE VIEW adm.sust_enterprise_view AS ';

	IF E_ID = ''
	THEN
	v_at_SQLSTR := CONCAT(v_at_SQLSTR,'SELECT DISTINCT ENTERPRISE_ID,ENTERPRISE_name FROM adm.ana_sust_mth_enterprise_survey_temp ',' WHERE DATA_DATE = ',V_DATA_DATE,';');
	ELSE
	v_at_SQLSTR := CONCAT(v_at_SQLSTR,'SELECT DISTINCT ENTERPRISE_ID,ENTERPRISE_name FROM adm.ana_sust_mth_enterprise_survey_temp WHERE ENTERPRISE_ID = ''',E_ID,''';');
	END IF;

	EXECUTE IMMEDIATE v_at_SQLSTR;



	V_STEP_ID := 3;

	DROP VIEW IF EXISTS adm.sust_standard_norm_view;

	V_STEP_ID := 4;
	v_at_SQLTXT := 'CREATE VIEW adm.sust_standard_norm_view AS ';
	IF N_ID = ''
	THEN
	v_at_SQLTXT := CONCAT(v_at_SQLTXT,'SELECT NORM_ID,NORM_NAME FROM adm.sust_standard_norm ORDER BY ID;');
	ELSE
	v_at_SQLTXT := CONCAT(v_at_SQLTXT,'SELECT NORM_ID,NORM_NAME FROM adm.sust_standard_norm WHERE NORM_ID = ''',N_ID,''' ORDER BY ID;');
	END IF;

	EXECUTE IMMEDIATE v_at_SQLTXT;

	V_STEP_ID := 5;


	OPEN CUR_ENTERPRISE;
		<<ENTLOOP>>
		LOOP
			FETCH CUR_ENTERPRISE INTO ENT_ID,ENT_NAME;
			EXIT WHEN CUR_ENTERPRISE%NOTFOUND;
			IF DONE THEN
			EXIT ENTLOOP;
			END IF;

			OPEN CUR_NORM;
				<<NORMLOOP>>
				LOOP
					FETCH CUR_NORM INTO V_NORM_ID,V_NORM_NAME;
					EXIT WHEN CUR_NORM%NOTFOUND;
					IF DONE THEN
					EXIT NORMLOOP;
					END IF;


					v_at_DATA_DATE := V_DATA_DATE;
					v_at_E_ID := ENT_ID;
					v_at_E_NAME := ENT_NAME;
					v_at_NORM_ID := V_NORM_ID;
					v_at_NORM_NAME := V_NORM_NAME;


					DELETE FROM adm.ana_sust_mth_enterprise_survey
					WHERE DATA_DATE = v_at_DATA_DATE
					AND ENTERPRISE_ID = v_at_E_ID
					AND STANDARD_CODE = v_at_NORM_ID;











					SELECT
					  PLAN_SQL INTO V_SQL_INSERT
					FROM
					  adm.sust_plan
					WHERE indicators_id = V_NORM_ID;


					v_at_V_SQLTXT := V_SQL_INSERT;
					EXECUTE IMMEDIATE v_at_V_SQLTXT;

			END LOOP NORMLOOP;
			CLOSE CUR_NORM;

			DONE := FALSE;
	END LOOP ENTLOOP;
	CLOSE CUR_ENTERPRISE;




EXCEPTION
    WHEN OTHERS THEN
	GET DIAGNOSTICS CONDITION 1 V_RETURN_CODE = RETURNED_SQLSTATE ,V_ERROR_MSG = MESSAGE_TEXT;
	UPDATE transfer.tf_business_launch_record SET RUN_STATUS = 3 WHERE ACCT_DATE = P_DATA_DATE;
	CALL etl.edw_proc_error_log(P_DATA_DATE,V_START_TIME,NOW(),V_NORM_ID,V_STEP_ID,V_RETURN_CODE,V_ERROR_MSG);
	END;
/

DROP PROCEDURE IF EXISTS adm.p_ana_sust_mth_enterprise_survey_temp;

CREATE PROCEDURE adm.p_ana_sust_mth_enterprise_survey_temp(in V_DATA_DATE varchar(6))
AS
	V_PROC_NAME     VARCHAR(80) := 'adm.p_ana_sust_mth_enterprise_survey_temp.PRC';
	V_START_TIME    CHAR(19) := NOW();
	V_STEP_ID       INT := 0;
	P_DATA_DATE     VARCHAR(8) := V_DATA_DATE;
    V_RETURN_CODE TEXT;
    V_ERROR_MSG TEXT;
BEGIN



V_STEP_ID := 1;
delete from adm.ana_sust_mth_enterprise_survey_temp WHERE DATA_DATE = V_DATA_DATE;
V_STEP_ID := 2;
INSERT INTO adm.ana_sust_mth_enterprise_survey_temp(
  DATA_DATE,
  ENTERPRISE_ID,
  ENTERPRISE_NAME,
  NORM_NAME,
  MONEY
)
SELECT
  DATA_DATE             		AS DATA_DATE,
  E_CODE 				AS ENTERPRISE_ID,
  n2.enterprise_name			AS ENTERPRISE_NAME,
  TRIM(NUM_CHAR(ASSETS)) 		AS NORM_NAME,
  MONEY_1/10000				AS MONEY
FROM
  ods.sust_enterprise_report N1
  LEFT JOIN adm.sust_enterprise N2
    ON n1.e_code = n2.enterprise_id
WHERE n1.data_date = V_DATA_DATE
    AND n1.assets <> ''
    and LENGTH(MONEY_1) = CHAR_LENGTH(MONEY_1)
    and RIGHT(MONEY_1,1) <> '-'
    AND MONEY_1 <> '.'
UNION ALL
SELECT
  DATA_DATE 				AS DATA_DATE,
  E_CODE				AS ENTERPRISE_ID,
  n2.enterprise_name			AS ENTERPRISE_NAME,
  TRIM(NUM_CHAR(n1.liabilities))  	AS NORM_NAME,
  MONEY_2/10000				AS MONEY
FROM
  ods.sust_enterprise_report N1
  LEFT JOIN adm.sust_enterprise N2
    ON n1.e_code = n2.enterprise_id
WHERE n1.data_date = V_DATA_DATE
    AND n1.liabilities <> ''
    AND LENGTH(MONEY_2) = CHAR_LENGTH(MONEY_2)
    AND RIGHT(MONEY_2,1) <> '-'
    AND MONEY_2 <> '.';
V_STEP_ID := 3;
DELETE FROM adm.ana_sust_mth_enterprise_survey_temp WHERE DATA_DATE = V_DATA_DATE AND LENGTH(money) <> CHAR_LENGTH(money);
V_STEP_ID := 4;
DELETE FROM adm.ana_sust_mth_enterprise_survey_temp WHERE DATA_DATE = V_DATA_DATE AND (MONEY = '0' OR MONEY IS NULL OR MONEY = '' OR RIGHT(MONEY,1) = '-' or money = '42');
V_STEP_ID := 5;
CALL adm.p_ana_sust_mth_enterprise_survey(V_DATA_DATE,'','');
EXCEPTION
    WHEN OTHERS THEN
	GET DIAGNOSTICS CONDITION 1 V_RETURN_CODE = RETURNED_SQLSTATE ,V_ERROR_MSG = MESSAGE_TEXT;
	UPDATE transfer.tf_business_launch_record SET RUN_STATUS = 3 WHERE ACCT_DATE = P_DATA_DATE;
	CALL etl.edw_proc_error_log(P_DATA_DATE,V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,V_RETURN_CODE,V_ERROR_MSG);
	END;
/

DROP PROCEDURE IF EXISTS adm.p_ana_sust_mth_special_struc_agricu;

CREATE PROCEDURE adm.p_ana_sust_mth_special_struc_agricu(in v_data_date varchar(10))
AS
BEGIN
DELETE FROM adm.ana_sust_mth_special_struc_agricu WHERE DATA_DATE=DATE_FORMAT(V_DATA_DATE,'%Y-%m-01');
insert into adm.ana_sust_mth_special_struc_agricu
SELECT
DATE_FORMAT(CONCAT(a.data_date,'01'),'%Y-%m-01')      AS DATA_DATE,
DATE_FORMAT(CONCAT(a.data_date,'01'),'%Y%m')          AS DATA_MTH,
substr(a.project,4)				      AS ITEM_CATEGORY,
a.bal_cur*10000					      AS LOAN_BAL
FROM
ods.glr_lfc_spe_data_agri_dtl1 a
WHERE a.data_date = DATE_FORMAT(v_data_date,'%Y%m')
AND (a.project = '（一）农林牧渔业贷款'
OR a.project = '（二）农用物资和农副产品流通贷款'
OR a.project = '（三）农村基础设施建设贷款'
OR a.project = '（四）农产品加工贷款'
OR a.project = '（五）农业生产资料制造贷款'
OR a.project = '（六）农田基本建设贷款'
OR a.project = '（七）农业科技贷款'
OR a.project = '（八）其他');
	END;
/

DROP PROCEDURE IF EXISTS adm.p_ana_sust_mth_special_struc_enter_scale;

CREATE PROCEDURE adm.p_ana_sust_mth_special_struc_enter_scale(in v_data_date varchar(10))
AS
BEGIN
DELETE FROM adm.ana_sust_mth_special_struc_enter_scale WHERE DATA_DATE=DATE_FORMAT(V_DATA_DATE,'%Y-%m-01');
insert into adm.ana_sust_mth_special_struc_enter_scale
SELECT
DATE_FORMAT(CONCAT(a.data_date,'01'),'%Y-%m-01')      AS DATA_DATE,
DATE_FORMAT(CONCAT(a.data_date,'01'),'%Y%m')          AS DATA_MTH,
CASE WHEN a.industry_name LIKE '%境内大型企业贷款合计' THEN '大型企业'
     WHEN a.industry_name LIKE '%境内中型企业贷款合计' THEN '中型企业'
     WHEN a.industry_name LIKE '%境内小型企业贷款合计' THEN '小型企业'
     WHEN a.industry_name LIKE '%境内微型企业贷款合计' THEN '微型企业'
     ELSE NULL
     END
						      AS ENTER_SCALE,
a.bwbbalanceloan				      AS LOAN_BAL
FROM ods.small_and_medium_sized_enterprises a
WHERE a.data_date = DATE_FORMAT(V_DATA_DATE,'%Y%m')
AND (a.industry_name LIKE '%境内大型企业贷款合计'
OR a.industry_name LIKE '%境内中型企业贷款合计'
OR a.industry_name LIKE '%境内小型企业贷款合计'
OR a.industry_name LIKE '%境内微型企业贷款合计');
	END;
/

DROP PROCEDURE IF EXISTS adm.p_ana_sust_mth_special_struc_realty;

CREATE PROCEDURE adm.p_ana_sust_mth_special_struc_realty(in v_data_date varchar(10))
AS
BEGIN
DELETE FROM adm.ana_sust_mth_special_struc_realty WHERE DATA_DATE=DATE_FORMAT(V_DATA_DATE,'%Y-%m-01');
INSERT INTO adm.ana_sust_mth_special_struc_realty
SELECT
	DATE_FORMAT(CONCAT(a.data_date,'01'),'%Y-%m-01')      AS DATA_DATE,
	DATE_FORMAT(CONCAT(a.data_date,'01'),'%Y%m')          AS DATA_MTH,
	RIGHT(a.project,6)				      AS ITEM_CATEGORY,
	(a.loan_balance)*10000				      AS LOAN_BAL
FROM
	ods.glr_rel_estate_loan_monitor_dtl1 a
WHERE a.data_date = date_format(v_data_date,'%Y%m')
	AND (a.project = '1．地产开发贷款'
	OR a.project = '2．房产开发贷款');
	END;
/

DROP PROCEDURE IF EXISTS adm.p_ana_sust_mth_special_times;

CREATE PROCEDURE adm.p_ana_sust_mth_special_times(in v_data_date varchar(10))
AS
BEGIN
DELETE FROM adm.ana_sust_mth_special_times WHERE DATA_DATE=DATE_FORMAT(V_DATA_DATE,'%Y-%m-01');
insert into adm.ana_sust_mth_special_times
SELECT
	DATA_DATE,
	DATA_MTH,
	SUM(REALTY_LOAN_BAL),
	SUM(REALTY_LOAN_BAL_TB),
	SUM(AGRICU_LOAN_BAL),
	SUM(AGRICU_LOAN_BAL_TB)
FROM
(
-- 房地产贷款余额、同比增速
SELECT
	DATE_FORMAT(CONCAT(a.data_date,'01'),'%Y-%m-01')      AS DATA_DATE,
	DATE_FORMAT(CONCAT(a.data_date,'01'),'%Y%m')          AS DATA_MTH,
	(a.loan_balance)*10000 				      AS REALTY_LOAN_BAL,
	(a.loan_balance-b.loan_balance)/b.loan_balance        AS REALTY_LOAN_BAL_TB,
	CAST(0 AS NUMERIC)				      AS AGRICU_LOAN_BAL,
	CAST(0 AS NUMERIC)				      AS AGRICU_LOAN_BAL_TB
FROM ods.glr_rel_estate_loan_monitor_dtl1 a
LEFT JOIN ods.glr_rel_estate_loan_monitor_dtl1 b
ON a.data_date = CONCAT(LEFT(b.data_date,4)-1,RIGHT(b.data_date,2))
AND a.project = b.project
WHERE a.data_date = DATE_FORMAT(v_data_date,'%Y%m')
AND a.project LIKE '合%计'
UNION ALL
-- 涉农贷款余额、同比增速
SELECT
	DATE_FORMAT(v_data_date,'%Y-%m-01')      	      AS DATA_DATE,
	DATE_FORMAT(v_data_date,'%Y%m')          	      AS DATA_MTH,
	CAST(0 AS NUMERIC) 		              AS REALTY_LOAN_BAL,
	CAST(0 AS NUMERIC)                       AS REALTY_LOAN_BAL_TB,
	(SUM(a.bal_cur))*10000			              AS AGRICU_LOAN_BAL,
	MAX(a.yoy_growth)/100			              AS AGRICU_LOAN_BAL_TB
FROM ods.glr_lfc_spe_data_agri_dtl1 a
WHERE a.data_date = DATE_FORMAT(v_data_date,'%Y%m')
AND a.project = '涉农贷款'
) s
GROUP BY s.data_date, s.data_mth;
	END;
/

DROP PROCEDURE IF EXISTS adm.p_ana_sust_update;

CREATE PROCEDURE adm.p_ana_sust_update(IN V_DATA_DATE VARCHAR(10))
AS
BEGIN
-- 维度表更新
CALL edw.p_cm_sust_update();
-- EDW层
CALL edw.p_transdatafromodstoedw_main();
CALL edw.p_sust_source_of_funds_detailed_a01();
CALL edw.p_sust_source_of_funds_detailed_a02();
call edw.p_sust_science_technology_and_finance(date_format(v_DATA_DATE,'%Y%m%d'));
-- ADM层
CALL adm.p_ana_sust_mth_credit_rece_pay_area(V_DATA_DATE);
CALL adm.p_ana_sust_mth_credit_rece_pay_struc(V_DATA_DATE);
CALL adm.p_ana_sust_mth_credit_rece_pay_times(V_DATA_DATE);
CALL adm.p_ana_sust_mth_special_struc_agricu(V_DATA_DATE);
CALL adm.p_ana_sust_mth_special_struc_enter_scale(V_DATA_DATE);
CALL adm.p_ana_sust_mth_special_struc_realty(V_DATA_DATE);
CALL adm.p_ana_sust_mth_special_times(V_DATA_DATE);
    END;
/

DROP PROCEDURE IF EXISTS adm.p_trs_stat_agentbankpay_back_detail;

CREATE PROCEDURE adm.p_trs_stat_agentbankpay_back_detail()
AS
V_STEP_ID       INT := 0;
V_PROC_NAME     VARCHAR(80) := 'adm.p_trs_stat_agentbankpay_back_detail.prc';
V_START_TIME    CHAR(19) := NOW();
v_log_date      CHAR(10);
v_data_date     VARCHAR(10) := CURDATE();
BEGIN
--     AUTHOR     : yeqian
--     NAME       : adm.p_trs_stat_agentbankpay_back_detail.HQL
--     FUNCTIONS  :
--     PURPOSE    : MONTHLY SAVING THE VARIATION DATA FROM stg
--     DATASOURCE : stg.trs_stat_agentbankpay_detail  -- 代理银行发起集中支付退回（退款）明细表
--                :
--     REVISIONS OR COMMENTS
--     VER        DATE       DEVELOPER       TESTER         DESCRIPTION
--   --------  ----------  -------------  -------------  ---------------------
--     1.0     2018-11-29   yeqian                      1.CREATE THE PROCEDURE
--
    /* EXCEPTION HANDLER
    DECLARE V_RETURN_CODE TEXT;
    DECLARE V_ERROR_MSG TEXT;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        GET DIAGNOSTICS CONDITION 1 V_RETURN_CODE = RETURNED_SQLSTATE ,V_ERROR_MSG = MESSAGE_TEXT;
         CALL etl.proc_error_log(DATE_FORMAT(v_log_date,'%Y%m%d'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,V_STEP_ID,V_RETURN_CODE,V_ERROR_MSG);
         CALL etl.proc_trace_log(DATE_FORMAT(v_log_date,'%Y%m%d'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,V_STEP_ID,ROW_COUNT());
    END;
	*/
-- SET v_log_date  = DATE_FORMAT(v_data_date,'%Y-%m-%d');
   v_data_date := DATE_FORMAT(DATE_SUB(v_data_date,INTERVAL 2 MONTH),'%Y-%m');


-- 删除当期的数据
V_STEP_ID := 1;
DELETE FROM adm.trs_stat_agentbankpay_back_detail WHERE DATE_FORMAT(S_ENTRUSTDATE,'%Y-%m') = v_data_date;
V_STEP_ID := 2;
-- 从源表取数到目标表
INSERT INTO adm.trs_stat_agentbankpay_back_detail
(
 S_SEQNO               -- 序号
,S_ID                  -- 支付凭证Id
,S_ADMDIVCODE          -- 行政区划代码
,S_STYEAR              -- 业务年度
,S_BOOKORGCODE         -- 核算主体代码
,S_TRECODE             -- 国库代码
,S_ENTRUSTDATE         -- 委托日期
,S_AGENTBANKCLASS      -- 代理银行行别:1-工商银行，2-农业银行，3-中国银行，4-建设银行，5-交通银行，6-光大银行，7-中信银行，8-平安银行，9-民生银行，10-兴业银行，11-重庆农商行，12-重庆银行，13-重庆三峡银行，14-邮储银行，15-村镇银行，16-其他银行
,S_AGENTBANKNO         -- 代理银行行号
,S_AGENTBANKNAME       -- 代理银行名称
,S_PAYOUTVOUTYPE       -- 支付凭证类型:0-无纸，1-有纸
,S_PAYMODE             -- 支付方式编码:1-直接支付，2-授权转账，3-授权现金
,S_BACKTYPE            -- 退回类型：1-退回（针对支付业务，用于冲销已发送审核的支付业务），2-退款（已支付成功后的退款），3-冲正业务（针对退款业务，用于冲销已发送审核的退款业务）
,S_VOUCHERNO           -- 支付凭证编号
,S_ORIVOUCHERNO        -- 原支付凭证编号
,S_ORIPAYVOUDATE       -- 原支付凭证日期
,S_FUNDTYPECODE        -- 预算种类:1-预算内，2-预算外
,S_BDGORGCODE          -- 预算单位编码
,S_BDGORGNAME          -- 预算单位名称
,S_EXPFUNCCODE         -- 支出功能科目编码
,S_EXPFUNCNAME         -- 支出功能科目名称
,S_EXPECOCODE          -- 支出经济科目编码
,S_EXPECONAME          -- 支出经济科目名称
,S_PROJECTTYPECODE     -- 项目分类编码
,S_PROJECTTYPENAME     -- 项目分类名称
,S_ORIZEROACCTNO       -- 原零余额账户账号
,S_ORIZEROACCTNAME     -- 原零余额账户名称
,S_ORIZEROOPNBNKNAME   -- 原零余额账户开户行行名
,S_ORIPAYEEACCTNO      -- 原收款人账号
,S_ORIPAYEEACCTNAME    -- 原收款人名称
,S_ORIPAYEEOPNBNKNAME  -- 原收款人开户行行号
,S_ORIPAYEEOPNBNKNO    -- 原收款人开户行名称
,S_ORICLEARACCTNO      -- 原清算账户账号
,S_ORICLEARACCTNAME    -- 原清算账户名称
,S_ORICLEARBANKNO      -- 原清算账户开户行行号
,S_ORICLEARBANKNAME    -- 原清算账户开户行名称
,S_REMARK              -- 摘要: 0-审核成功1-超出预算计划2-关注账户提示未通过3-资金支付文件依据不足4-收款账户不符合规定5-其他原因91-报文解析错误92-审核签名错误93-其他错误
,F_PAYAMT              -- 支付金额
,C_ISADDPLAN           -- 是否增加计划
,C_CHECKRESULT         -- 对账结果
,S_HOLD1               -- 预留字段1
,S_HOLD2               -- 预留字段2
,S_HOLD3               -- 预留字段3
,S_HOLD4               -- 预留字段4
,TS_SYSUPDATE          -- 系统更新时间
,C_AUTOAUDITSTATE      -- 自动审核状态
,T_AUTOAUDITTIME       -- 自动审核时间
,S_AUDITREASON         -- 审核不通过原因
,C_HANDAUDITSTATE      -- 人工确认状态
,T_HANDAUDITTIME       -- 人工确认时间
,S_HANDREASON          -- 人工确认原因
)
SELECT
 S_SEQNO               -- 序号
,S_ID                  -- 支付凭证Id
,S_ADMDIVCODE          -- 行政区划代码
,S_STYEAR              -- 业务年度
,S_BOOKORGCODE         -- 核算主体代码
,S_TRECODE             -- 国库代码
,S_ENTRUSTDATE         -- 委托日期
,S_AGENTBANKCLASS      -- 代理银行行别:1-工商银行，2-农业银行，3-中国银行，4-建设银行，5-交通银行，6-光大银行，7-中信银行，8-平安银行，9-民生银行，10-兴业银行，11-重庆农商行，12-重庆银行，13-重庆三峡银行，14-邮储银行，15-村镇银行，16-其他银行
,S_AGENTBANKNO         -- 代理银行行号
,CASE WHEN S_AGENTBANKNAME = 'NULL'
THEN ''
END AS S_AGENTBANKNAME    -- 代理银行名称
,S_PAYOUTVOUTYPE       -- 支付凭证类型:0-无纸，1-有纸
,S_PAYMODE             -- 支付方式编码:1-直接支付，2-授权转账，3-授权现金
,S_BACKTYPE            -- 退回类型：1-退回（针对支付业务，用于冲销已发送审核的支付业务），2-退款（已支付成功后的退款），3-冲正业务（针对退款业务，用于冲销已发送审核的退款业务）
,S_VOUCHERNO           -- 支付凭证编号
,S_ORIVOUCHERNO        -- 原支付凭证编号
,S_ORIPAYVOUDATE       -- 原支付凭证日期
,S_FUNDTYPECODE        -- 预算种类:1-预算内，2-预算外
,S_BDGORGCODE          -- 预算单位编码
,S_BDGORGNAME          -- 预算单位名称
,S_EXPFUNCCODE         -- 支出功能科目编码
,S_EXPFUNCNAME         -- 支出功能科目名称
,S_EXPECOCODE          -- 支出经济科目编码
,S_EXPECONAME          -- 支出经济科目名称
,S_PROJECTTYPECODE     -- 项目分类编码
,S_PROJECTTYPENAME     -- 项目分类名称
,S_ORIZEROACCTNO       -- 原零余额账户账号
,S_ORIZEROACCTNAME     -- 原零余额账户名称
,S_ORIZEROOPNBNKNAME   -- 原零余额账户开户行行名
,S_ORIPAYEEACCTNO      -- 原收款人账号
,S_ORIPAYEEACCTNAME    -- 原收款人名称
,S_ORIPAYEEOPNBNKNAME  -- 原收款人开户行行号
,S_ORIPAYEEOPNBNKNO    -- 原收款人开户行名称
,S_ORICLEARACCTNO      -- 原清算账户账号
,S_ORICLEARACCTNAME    -- 原清算账户名称
,S_ORICLEARBANKNO      -- 原清算账户开户行行号
,S_ORICLEARBANKNAME    -- 原清算账户开户行名称
,CASE WHEN S_REMARK = 'NULL'
THEN ''
END AS S_REMARK         -- 摘要: 0-审核成功1-超出预算计划2-关注账户提示未通过3-资金支付文件依据不足4-收款账户不符合规定5-其他原因91-报文解析错误92-审核签名错误93-其他错误
,F_PAYAMT              -- 支付金额
,C_ISADDPLAN           -- 是否增加计划
,C_CHECKRESULT         -- 对账结果
,S_HOLD1               -- 预留字段1
,S_HOLD2               -- 预留字段2
,S_HOLD3               -- 预留字段3
,S_HOLD4               -- 预留字段4
,TS_SYSUPDATE          -- 系统更新时间
,C_AUTOAUDITSTATE      -- 自动审核状态
,T_AUTOAUDITTIME       -- 自动审核时间
,S_AUDITREASON         -- 审核不通过原因
,C_HANDAUDITSTATE      -- 人工确认状态
,T_HANDAUDITTIME       -- 人工确认时间
,S_HANDREASON          -- 人工确认原因
FROM ods.te_agentbankback_detail_query
WHERE DATE_FORMAT(S_ENTRUSTDATE,'%Y-%m') = v_data_date
and S_SEQNO <> ''
AND LENGTH(S_SEQNO) = CHAR_LENGTH(S_SEQNO);
COMMIT;
  -- CALL etl.proc_trace_log(DATE_FORMAT(v_log_date,'%Y%m%d'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,V_STEP_ID,ROW_COUNT());
	END;
/

DROP PROCEDURE IF EXISTS adm.p_trs_stat_agentbankpay_detail;

CREATE PROCEDURE adm.p_trs_stat_agentbankpay_detail()
AS
V_STEP_ID       INT := 0;
V_PROC_NAME     VARCHAR(80) := 'adm.p_trs_stat_agentbankpay_detail.prc';
V_START_TIME    CHAR(19) := NOW();
v_log_date      CHAR(10);
v_data_date     VARCHAR(10) := CURDATE();
BEGIN
--     AUTHOR     : yeqian
--     NAME       : adm.p_trs_stat_agentbankpay_detail.HQL
--     FUNCTIONS  :
--     PURPOSE    : MONTHLY SAVING THE VARIATION DATA FROM stg
--     DATASOURCE : stg.trs_stat_agentbankpay_detail  -- 代理银行发起集中支付明细表
--                :
--     REVISIONS OR COMMENTS
--     VER        DATE       DEVELOPER       TESTER         DESCRIPTION
--   --------  ----------  -------------  -------------  ---------------------
--     1.0     2018-11-29   yeqian                      1.CREATE THE PROCEDURE
--
    /* EXCEPTION HANDLER
    DECLARE V_RETURN_CODE TEXT;
    DECLARE V_ERROR_MSG TEXT;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        GET DIAGNOSTICS CONDITION 1 V_RETURN_CODE = RETURNED_SQLSTATE ,V_ERROR_MSG = MESSAGE_TEXT;
         CALL etl.proc_error_log(DATE_FORMAT(v_log_date,'%Y%m%d'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,V_STEP_ID,V_RETURN_CODE,V_ERROR_MSG);
         CALL etl.proc_trace_log(DATE_FORMAT(v_log_date,'%Y%m%d'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,V_STEP_ID,ROW_COUNT());
    END;
	*/
-- SET v_log_date  = DATE_FORMAT(v_data_date,'%Y-%m-%d');
   v_data_date := DATE_FORMAT(DATE_SUB(v_data_date,INTERVAL 1 MONTH),'%Y-%m');


-- 删除当期的数据
V_STEP_ID := 1;
DELETE FROM adm.trs_stat_agentbankpay_detail WHERE DATE_FORMAT(S_ENTRUSTDATE,'%Y-%m') = v_data_date;
V_STEP_ID := 2;
-- 从源表取数到目标表
INSERT INTO adm.trs_stat_agentbankpay_detail (
 S_SEQNO            -- 序号
,S_ID               -- 支付凭证Id
,S_ADMDIVCODE       -- 行政区划代码
,S_STYEAR           -- 业务年度
,S_BOOKORGCODE      -- 核算主体代码
,S_TRECODE          -- 国库代码
,S_ENTRUSTDATE      -- 委托日期
,S_AGENTBANKCLASS   -- 代理银行行别:1-工商银行，2-农业银行，3-中国银行，4-建设银行，5-交通银行，6-光大银行，7-中信银行，8-平安银行，9-民生银行，10-兴业银行，11-重庆农商行，12-重庆银行，13-重庆三峡银行，14-邮储银行，15-村镇银行，16-其他银行
,S_AGENTBANKNO      -- 代理银行行号
,S_AGENTBANKNAME    -- 代理银行名称
,S_PAYOUTVOUTYPE    -- 支付凭证类型:0-无纸，1-有纸
,S_PAYMODE          -- 支付方式编码:1-直接支付，2-授权转账，3-授权现金
,D_PAYVOUDATE       -- 支付凭证日期
,S_VOUCHERNO        -- 支付凭证编号
,S_FUNDTYPECODE     -- 预算种类:1-预算内，2-预算外
,S_BDGORGCODE       -- 预算单位编码
,S_BDGORGNAME       -- 预算单位名称
,S_EXPFUNCCODE      -- 支出功能科目编码
,S_EXPFUNCNAME      -- 支出功能科目名称
,S_EXPECOCODE       -- 支出经济科目编码
,S_EXPECONAME       -- 支出经济科目名称
,S_PROJECTTYPECODE  -- 项目分类编码
,S_PROJECTTYPENAME  -- 项目分类名称
,S_ZEROACCTNO       -- 零余额账户账号
,S_ZEROACCTNAME     -- 零余额账户名称
,S_ZEROOPNBNKNAME   -- 零余额账户开户行行名
,S_PAYEEACCTNO      -- 收款人账号
,S_PAYEEACCTNAME    -- 收款人名称
,S_PAYEEOPNBNKNO    -- 收款人开户行行号
,S_PAYEEOPNBNKNAME  -- 收款人开户行名称
,S_CLEARACCTNO      -- 清算账户账号
,S_CLEARACCTNAME    -- 清算账户名称
,S_CLEARBANKNO      -- 清算账户开户行行号
,S_CLEARBANKNAME    -- 清算账户开户行名称
,S_REMARK           -- 摘要: 0-审核成功1-超出预算计划2-关注账户提示未通过3-资金支付文件依据不足4-收款账户不符合规定5-其他原因91-报文解析错误92-审核签名错误93-其他错误
,F_PAYAMT           -- 支付金额
,C_AUTOAUDITSTATE   -- 自动审核状态
,T_AUTOAUDITTIME    -- 自动审核时间
,S_AUDITREASON      -- 审核不通过原因
,C_HANDAUDITSTATE   -- 人工确认状态
,T_HANDAUDITTIME    -- 人工确认时间
,S_HANDREASON       -- 人工确认原因
,C_HANDAUDITFLAG    -- 人工确认标志
,C_ISDEDPLAN        -- 是否扣减计划
,C_CHECKRESULT      -- 对账结果
,S_HOLD1            -- 预留字段1
,S_HOLD2            -- 预留字段2
,S_HOLD3            -- 预留字段3
,S_HOLD4            -- 预留字段4
,TS_SYSUPDATE       -- 系统更新时间
)
SELECT
 S_SEQNO            -- 序号
,S_ID               -- 支付凭证Id
,S_ADMDIVCODE       -- 行政区划代码
,S_STYEAR           -- 业务年度
,S_BOOKORGCODE      -- 核算主体代码
,S_TRECODE          -- 国库代码
,S_ENTRUSTDATE      -- 委托日期
,S_AGENTBANKCLASS   -- 代理银行行别:1-工商银行，2-农业银行，3-中国银行，4-建设银行，5-交通银行，6-光大银行，7-中信银行，8-平安银行，9-民生银行，10-兴业银行，11-重庆农商行，12-重庆银行，13-重庆三峡银行，14-邮储银行，15-村镇银行，16-其他银行
,S_AGENTBANKNO      -- 代理银行行号
,CASE WHEN S_AGENTBANKNAME = 'NULL'
THEN ''
END AS S_AGENTBANKNAME -- 代理银行名称
,S_PAYOUTVOUTYPE    -- 支付凭证类型:0-无纸，1-有纸
,S_PAYMODE          -- 支付方式编码:1-直接支付，2-授权转账，3-授权现金
,D_PAYVOUDATE       -- 支付凭证日期
,S_VOUCHERNO        -- 支付凭证编号
,S_FUNDTYPECODE     -- 预算种类:1-预算内，2-预算外
,S_BDGORGCODE       -- 预算单位编码
,S_BDGORGNAME       -- 预算单位名称
,S_EXPFUNCCODE      -- 支出功能科目编码
,S_EXPFUNCNAME      -- 支出功能科目名称
,S_EXPECOCODE       -- 支出经济科目编码
,S_EXPECONAME       -- 支出经济科目名称
,S_PROJECTTYPECODE  -- 项目分类编码
,S_PROJECTTYPENAME  -- 项目分类名称
,S_ZEROACCTNO       -- 零余额账户账号
,S_ZEROACCTNAME     -- 零余额账户名称
,S_ZEROOPNBNKNAME   -- 零余额账户开户行行名
,S_PAYEEACCTNO      -- 收款人账号
,S_PAYEEACCTNAME    -- 收款人名称
,S_PAYEEOPNBNKNO    -- 收款人开户行行号
,CASE WHEN S_PAYEEOPNBNKNAME = 'NULL'
THEN ''
END AS S_PAYEEOPNBNKNAME -- 收款人开户行名称
,S_CLEARACCTNO      -- 清算账户账号
,S_CLEARACCTNAME    -- 清算账户名称
,S_CLEARBANKNO      -- 清算账户开户行行号
,S_CLEARBANKNAME    -- 清算账户开户行名称
,S_REMARK           -- 摘要: 0-审核成功1-超出预算计划2-关注账户提示未通过3-资金支付文件依据不足4-收款账户不符合规定5-其他原因91-报文解析错误92-审核签名错误93-其他错误
,F_PAYAMT           -- 支付金额
,C_AUTOAUDITSTATE   -- 自动审核状态
,T_AUTOAUDITTIME    -- 自动审核时间
,S_AUDITREASON      -- 审核不通过原因
,C_HANDAUDITSTATE   -- 人工确认状态
,T_HANDAUDITTIME    -- 人工确认时间
,S_HANDREASON       -- 人工确认原因
,C_HANDAUDITFLAG    -- 人工确认标志
,C_ISDEDPLAN        -- 是否扣减计划
,C_CHECKRESULT      -- 对账结果
,S_HOLD1            -- 预留字段1
,S_HOLD2            -- 预留字段2
,S_HOLD3            -- 预留字段3
,S_HOLD4            -- 预留字段4
,TS_SYSUPDATE       -- 系统更新时间
FROM ods.te_agentbankpay_detail_query
WHERE DATE_FORMAT(S_ENTRUSTDATE,'%Y-%m') = v_data_date;
COMMIT;
  -- CALL etl.proc_trace_log(DATE_FORMAT(v_log_date,'%Y%m%d'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,V_STEP_ID,ROW_COUNT());
	END;
/

DROP FUNCTION IF EXISTS adm.num_char;

CREATE FUNCTION adm.num_char(Varstring VARCHAR(1000)) RETURNS varchar(500)
AS $function$
DECLARE
    len INT := 0;
    Tmp VARCHAR(1000) := '';
BEGIN
    len := CHAR_LENGTH(Varstring);
    WHILE len > 0 LOOP
        IF NOT (MID(Varstring, len, 1) REGEXP '^[u0391-uFFE5]') THEN
            Tmp := CONCAT(Tmp, MID(Varstring, len, 1));
        END IF;
        len := len - 1;
    END LOOP;
    RETURN REVERSE(Tmp);
END;
$function$
LANGUAGE plpgsql;
