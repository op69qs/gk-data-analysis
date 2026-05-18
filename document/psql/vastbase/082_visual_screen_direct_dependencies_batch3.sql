-- Visual screen direct dependency source-port bundle (batch 3)
-- Extracted from source_routines.sql
-- Includes direct callees used by visual_screen.P_task_vscreen*

CREATE DEFINER=`root`@`%` PROCEDURE `P_VS_TRANSFER_INCOME`(IN V_DATA_DATE VARCHAR(10))
BEGIN
DECLARE V_STEP_ID       INT           DEFAULT 0; 
DECLARE V_PROC_NAME     VARCHAR(80)   DEFAULT 'visual_screen.P_VS_TRANSFER_INCOME.PRC';
DECLARE V_START_TIME    CHAR(19)      DEFAULT NOW();
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        GET DIAGNOSTICS CONDITION 1 @V_RETURN_CODE = RETURNED_SQLSTATE ,@V_ERROR_MSG = MESSAGE_TEXT;
        CALL ETL.EDW_PROC_ERROR_LOG(DATE_FORMAT(V_START_TIME,'%Y-%m'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,@V_RETURN_CODE,@V_ERROR_MSG);
        CALL ETL.EDW_PROC_TRACE_LOG(DATE_FORMAT(V_START_TIME,'%Y-%m'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
    END;
	 CALL ETL.EDW_PROC_TRACE_LOG(DATE_FORMAT(V_START_TIME,'%Y%m%d'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
 
SET V_STEP_ID = 1;     
DELETE FROM visual_screen.vs_transfer_income WHERE DACCT = DATE_FORMAT(V_DATA_DATE,'%Y-%m');
SET V_STEP_ID = 2; 
INSERT INTO visual_screen.vs_transfer_income
SELECT 
	DATE_FORMAT(V_DATA_DATE,'%Y-%m'), 
	b.PID,
	b.AREA_NO_ID,
	b.AREA_DSCR,
	'2', 
	'转移性收入金额'        ,  
	SUM(a.THIS_AMT) 
FROM
  stg.trs_tmis_budget_income a
  LEFT JOIN 
  (SELECT
    DISTINCT p.`GUOKU_ID` AS PID,
	p.LEVEL,
    p.GUOKU_DSCR,
    p.AREA_NO_ID,
    p.AREA_DSCR,
    c.`GUOKU_ID` AS CID
    FROM
     edw.`cm_guoku_dimnsn` p
    JOIN
     edw.`cm_guoku_dimnsn` c
    ON(
        p.`GUOKU_LVL_ID_2` = c.`GUOKU_LVL_ID_1`
    OR  p.`GUOKU_LVL_ID_3` = c.`GUOKU_LVL_ID_2`
    OR  p.`GUOKU_ID` = c.`GUOKU_LVL_ID_3`
      )) b
  ON a.`TRECODE`=b.CID
WHERE DATE_FORMAT(a.D_ACCT,'%Y-%m')=DATE_FORMAT(V_DATA_DATE,'%Y-%m')
  AND a.SUBJECT_CODE IN ('11001','11002')
  AND a.`LEVEL`=b.LEVEL
  AND a.`TRECODE` NOT IN('2231000000','2232000000','2233000000','2230000000')
GROUP BY 
  DATE_FORMAT(a.D_ACCT,'%Y-%m'),
  b.AREA_NO_ID;
	
INSERT INTO visual_screen.vs_transfer_income
SELECT
  a.DACCT,
  a.GUOKU_ID,
  a.AREA_CODE,
  a.AREA_DSCR,
  a.PERIOD_FLAG,
  CONCAT(a.INDEX_NAME,'同比'),
  (a.INDEX_VALUE-b.INDEX_VALUE)/b.INDEX_VALUE
FROM visual_screen.vs_transfer_income  a
LEFT JOIN visual_screen.vs_transfer_income  b
       ON b.DACCT = DATE_FORMAT(DATE_SUB(V_DATA_DATE,INTERVAL 1 YEAR),'%Y-%m')
       AND a.GUOKU_ID = b.GUOKU_ID
       AND a.AREA_CODE = b.AREA_CODE
       AND a.INDEX_NAME = b.INDEX_NAME
       AND a.PERIOD_FLAG = b.PERIOD_FLAG
WHERE a.DACCT = DATE_FORMAT(V_DATA_DATE,'%Y-%m')
  AND a.PERIOD_FLAG = '2'
  AND a.INDEX_NAME  = '转移性收入金额';
	
	
	
SET V_STEP_ID = 3;     
DELETE FROM visual_screen.vs_transfer_income WHERE DACCT = left(V_DATA_DATE,4);
SET V_STEP_ID = 4; 
INSERT INTO visual_screen.vs_transfer_income
SELECT
  LEFT(a.DACCT,4),
  a.GUOKU_ID,
  a.AREA_CODE,
  a.AREA_DSCR,
  '4'   AS PERIOD_FLAG,
  a.INDEX_NAME,
  SUM(a.INDEX_VALUE)
FROM visual_screen.vs_transfer_income  a
WHERE a.DACCT LIKE CONCAT(LEFT(v_data_date,4),'%')
  AND a.PERIOD_FLAG = '2'
  AND a.INDEX_NAME = '转移性收入金额'
GROUP BY 
  a.GUOKU_ID,
  a.AREA_CODE,
  a.PERIOD_FLAG,
  a.INDEX_NAME;
	
INSERT INTO visual_screen.vs_transfer_income
	
SELECT
  a.DACCT,
  a.GUOKU_ID,
  a.AREA_CODE,
  a.AREA_DSCR,
  a.PERIOD_FLAG,
  CONCAT(a.INDEX_NAME,'同比'),
  (a.INDEX_VALUE-b.INDEX_VALUE)/b.INDEX_VALUE
FROM visual_screen.vs_transfer_income  a
LEFT JOIN visual_screen.vs_transfer_income  b
       ON b.DACCT = DATE_FORMAT(DATE_SUB(v_data_date,INTERVAL 1 YEAR),'%Y')
       AND a.GUOKU_ID = b.GUOKU_ID
       AND a.AREA_CODE = b.AREA_CODE
       AND a.INDEX_NAME = b.INDEX_NAME
       AND a.PERIOD_FLAG = b.PERIOD_FLAG
WHERE a.DACCT = DATE_FORMAT(v_data_date,'%Y')
  AND a.PERIOD_FLAG = '4'
  AND a.INDEX_NAME  = '转移性收入金额';
	
SET V_STEP_ID=5;
UPDATE visual_screen.vs_transfer_income SET AREA_DSCR = '李渡新区' WHERE AREA_CODE= '500997';
UPDATE visual_screen.vs_transfer_income SET AREA_DSCR = '彭水县' WHERE AREA_CODE= '500243';
UPDATE visual_screen.vs_transfer_income SET AREA_DSCR = '酉阳县' WHERE AREA_CODE= '500242';
UPDATE visual_screen.vs_transfer_income SET AREA_DSCR = '秀山县' WHERE AREA_CODE= '500241';
UPDATE visual_screen.vs_transfer_income SET AREA_DSCR = '石柱县' WHERE AREA_CODE= '500240';
UPDATE visual_screen.vs_transfer_income SET AREA_DSCR = '万盛区' WHERE AREA_CODE= '500131';
SET V_STEP_ID=6;
CALL ETL.EDW_PROC_TRACE_LOG(DATE_FORMAT(V_START_TIME,'%Y%m%d'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
	END
$$

-- PROCEDURE `visual_screen`.`trs_kyd_enterprise`
DROP PROCEDURE IF EXISTS `visual_screen`.`trs_kyd_enterprise`;
DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `trs_kyd_enterprise`(IN V_DATA_DATE VARCHAR(10))
BEGIN

delete from visual_screen.trs_kyd_enterprise where DACCT = date_format(V_DATA_DATE,'%Y-%m');
insert into visual_screen.trs_kyd_enterprise
select 
	date_format(a.d_acct,'%Y-%m'),
	a.s_trecode,
	a.S_TRECODE,
	b.area_no_id,
	b.area_dscr,
	'2',
	a.ETPCODE,
	a.ETPNAME,
	sum(a.F_AMT),
	sum(DET2.F_AMT)
from edw.trs_kyd_industry a
left join EDW.trs_kyd_industry DET2
       ON DET2.D_ACCT = DATE_SUB(a.D_ACCT,INTERVAL 1 YEAR)
			AND a.S_TRECODE = DET2.S_TRECODE
		  AND a.ETPNAME = DET2.ETPNAME
		  AND a.LEV_4_ID = DET2.LEV_4_ID
			AND a.S_BDGSBTCODE = DET2.S_BDGSBTCODE
left join dmcode.cm_guoku_dimnsn b
		  ON a.S_TRECODE = b.guoku_id
where date_format(a.d_acct,'%Y-%m') between date_format(V_DATA_DATE,'%Y-01') and date_format(V_DATA_DATE,'%Y-%m')
  and a.S_BDGSBTCODE like '101%'
	and a.LEV_4_ID <> 'null'
group by
     a.S_TRECODE,
		 b.area_no_id,
		 a.ETPNAME;


COMMIT;
	END
$$
DELIMITER ;

-- PROCEDURE `visual_screen`.`trs_kyd_enterprise_rank`
DROP PROCEDURE IF EXISTS `visual_screen`.`trs_kyd_enterprise_rank`;
DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `trs_kyd_enterprise_rank`(IN V_DATA_DATE VARCHAR(10))
BEGIN

delete from visual_screen.trs_kyd_enterprise_rank where DACCT = date_format(V_DATA_DATE,'%Y-%m');
insert into visual_screen.trs_kyd_enterprise_rank
select 
	date_format(a.d_acct,'%Y-%m'),
	a.s_trecode,
	a.S_TRECODE,
	b.area_no_id,
	b.area_dscr,
	'2',
	case when a.LEV_1_ID in ('C','K','E','F','J') then a.LEV_1_ID
	     else 'Z' END      as index_code,
	case when a.LEV_1_ID in ('C','K','E','F','J') then a.LEV_1_DSCR
	     else '其他行业' END   as index_name,
	sum(a.F_AMT)
from edw.trs_kyd_industry a
left join dmcode.cm_guoku_dimnsn b
		  ON a.S_TRECODE = b.guoku_id
where a.d_acct like concat(date_format(V_DATA_DATE,'%Y-%m'),'%')
group by
     a.S_TRECODE,
		 b.area_no_id,
		 case when a.LEV_1_ID in ('C','K','E','F','J') then a.LEV_1_ID
	     else 'Z'  END ;


COMMIT;
	END
$$

-- PROCEDURE `visual_screen`.`trs_kyd_industry`
DROP PROCEDURE IF EXISTS `visual_screen`.`trs_kyd_industry`;
DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `trs_kyd_industry`(IN V_DATA_DATE VARCHAR(10))
BEGIN

delete from visual_screen.trs_kyd_industry where DACCT = date_format(V_DATA_DATE,'%Y-%m');
insert into visual_screen.trs_kyd_industry
select 
	date_format(a.d_acct,'%Y-%m'),
	a.s_trecode,
	a.S_TRECODE,
	b.area_no_id,
	b.area_dscr,
	'2',
	a.LEV_1_ID,
	case when a.LEV_1_ID = 'C' then '汽车制造业' 
	     else a.LEV_1_DSCR end as LEV_1_DSCR,
	sum(a.F_AMT),
	sum(DET2.F_AMT)
from edw.trs_kyd_industry a
left join EDW.trs_kyd_industry DET2
       ON DET2.D_ACCT = DATE_SUB(a.D_ACCT,INTERVAL 1 YEAR)
			AND a.S_TRECODE = DET2.S_TRECODE
		  AND a.ETPNAME = DET2.ETPNAME
		  AND a.LEV_4_ID = DET2.LEV_4_ID
			AND a.S_BDGSBTCODE = DET2.S_BDGSBTCODE
left join dmcode.cm_guoku_dimnsn b
		  ON a.S_TRECODE = b.guoku_id
where a.d_acct like concat(date_format(V_DATA_DATE,'%Y-%m'),'%')
  and a.LEV_4_ID <> 'null'
  and (a.LEV_1_ID in ('K','J')
  or a.LEV_2_ID = '36')
	and a.S_BDGSBTCODE like '101%'
group by
     a.S_TRECODE,
		 b.area_no_id,
		 a.LEV_1_ID;


COMMIT;
	END
$$
DELIMITER ;
