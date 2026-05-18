-- Vastbase source-port script for indicators_lib.P_TRS_BUDGET_INCOME_COMPARE_XIN
-- Strategy: keep MySQL B-compatible body first because this procedure is directly called by init_report03.

-- PROCEDURE `indicators_lib`.`P_TRS_BUDGET_INCOME_COMPARE_XIN`
DROP PROCEDURE IF EXISTS `indicators_lib`.`P_TRS_BUDGET_INCOME_COMPARE_XIN`;
DELIMITER $$
CREATE DEFINER=`dis`@`%` PROCEDURE `P_TRS_BUDGET_INCOME_COMPARE_XIN`(IN V_DATA_DATE VARCHAR(6))
BEGIN
  
  
  
DELETE FROM EDW.TRS_BUDGET_INCOME_COMPARE WHERE DATA_DATE = V_DATA_DATE;

insert into EDW.TRS_BUDGET_INCOME_COMPARE
SELECT 
	 DATA_DATE
	,PROJECT
	,ROUND (SUM(T010101_RATE),4)
	,SUM(T010101_RANK)
	,ROUND (SUM(101_TATE),4)
	,SUM(101_RANK)
	,ROWS_ID
FROM 
(
SELECT 
	AA.DATA_DATE,
	AA.PROJECT,
	AA.T010101_RATE,
	@rank:=@rank+1 AS T010101_RANK,
	AA.101_TATE,
	AA.101_RANK,
	AA.ROWS_ID
FROM 
(
SELECT 
	a.DATA_DATE,
	CASE WHEN a.TREDSCR = '大连市' THEN '辽宁'
             WHEN a.TREDSCR = '青岛市' THEN '山东省' 
	     WHEN a.TREDSCR = '深圳市' THEN '广东省'
             WHEN a.TREDSCR = '宁波市' THEN '浙江省'
             WHEN a.TREDSCR = '厦门市' THEN '福建省'
             WHEN (a.TREDSCR = '新疆兵团' or a.TREDSCR = '新疆区') THEN '新疆区'
	     ELSE a.TREDSCR
	     END  AS PROJECT,
	(SUM(a.YEAR_AMT)-SUM(b.YEAR_AMT))/SUM(b.YEAR_AMT) AS T010101_RATE,
	'' AS 101_TATE,
	'' AS 101_RANK,
        A.ROWS_ID
FROM stg.trs_tmis_budget_income_provinces a
LEFT JOIN stg.trs_tmis_budget_income_provinces b
       ON b.data_Date = DATE_FORMAT(DATE_SUB(CONCAT(a.data_Date,'01'),INTERVAL 1 YEAR),'%Y%m')
       AND a.TRECODE = b.TRECODE
       AND a.SUBJECT_CODE = b.SUBJECT_CODE
WHERE a.data_Date = V_DATA_DATE
AND a.SUBJECT_CODE = 'T010101'
GROUP BY
        CASE WHEN a.TRECODE = '3501000000' THEN '0600000000'
             WHEN a.TRECODE = '3302000000' THEN '1500000000' 
	     WHEN a.TRECODE = '3801000000' THEN '1900000000'
             WHEN a.TRECODE = '3600000000' THEN '1100000000'
             WHEN a.TRECODE = '4000000000' THEN '1300000000'
						 WHEN (a.TRECODE = '3100000000' or a.TRECODE = '3000000000')  THEN '3000000000'
	     ELSE a.TRECODE
	     END
ORDER BY T010101_RATE  DESC
) AA ,(SELECT @rank:=0) BB
UNION ALL
(
SELECT 
	AA.DATA_DATE,
	AA.PROJECT,
	AA.T010101_RATE,
	AA.T010101_RANK,
	AA.101_TATE,
	@rank2:=@rank2+1  AS 101_RANK,
	AA.ROWS_ID
FROM 
(
SELECT 
	a.DATA_DATE,
	CASE WHEN a.TREDSCR = '大连市' THEN '辽宁'
             WHEN a.TREDSCR = '青岛市' THEN '山东省' 
	     WHEN a.TREDSCR = '深圳市' THEN '广东省'
             WHEN a.TREDSCR = '宁波市' THEN '浙江省'
             WHEN a.TREDSCR = '厦门市' THEN '福建省'
             WHEN (a.TREDSCR = '新疆兵团' or a.TREDSCR = '新疆区') THEN '新疆区'
	     ELSE a.TREDSCR
	     END  AS PROJECT,
	'' AS T010101_RATE,
	'' AS T010101_RANK,
	(SUM(a.YEAR_AMT)-SUM(b.YEAR_AMT))/SUM(b.YEAR_AMT) AS 101_TATE,
	A.ROWS_ID
FROM stg.trs_tmis_budget_income_provinces a
LEFT JOIN stg.trs_tmis_budget_income_provinces b
       ON b.data_Date = DATE_FORMAT(DATE_SUB(CONCAT(a.data_Date,'01'),INTERVAL 1 YEAR),'%Y%m')
       AND a.TRECODE = b.TRECODE
       AND a.SUBJECT_CODE = b.SUBJECT_CODE
,(SELECT @tate:=NULL,@rank:=0) c
WHERE A.DATA_DATE = V_DATA_DATE
AND a.SUBJECT_CODE = '101'
GROUP BY
        CASE WHEN a.TRECODE = '3501000000' THEN '0600000000'
             WHEN a.TRECODE = '3302000000' THEN '1500000000' 
	     WHEN a.TRECODE = '3801000000' THEN '1900000000'
             WHEN a.TRECODE = '3600000000' THEN '1100000000'
             WHEN a.TRECODE = '4000000000' THEN '1300000000'
						 WHEN (a.TRECODE = '3100000000' or a.TRECODE = '3000000000')  THEN '3000000000'
	     ELSE a.TRECODE
	     END
ORDER BY 101_TATE DESC
) AA ,(SELECT @rank2:=0) BB
) 
)A
GROUP BY 
        PROJECT;
             
DELETE FROM EDW.TRS_BUDGET_INCOME_COMPARE WHERE DATA_DATE = concat(year(concat(V_DATA_DATE,'01')),'Q',quarter(CONCAT(V_DATA_DATE,'01')));
 
INSERT INTO EDW.TRS_BUDGET_INCOME_COMPARE
SELECT 
	 DATA_DATE
	,PROJECT
	,ROUND (SUM(T010101_RATE),4)
	,SUM(T010101_RANK)
	,ROUND (SUM(101_TATE),4)
	,SUM(101_RANK)
	,ROWS_ID
FROM 
(
SELECT 
	CONCAT(YEAR(CONCAT(AA.data_Date,'01')),'Q',QUARTER(CONCAT(AA.data_Date,'01'))) AS DATA_DATE,
	AA.PROJECT,
	AA.T010101_RATE,
	@rank:=@rank+1 AS T010101_RANK,
	AA.101_TATE,
	AA.101_RANK,
	AA.ROWS_ID
FROM 
(
SELECT 
	a.DATA_DATE,
	CASE WHEN a.TREDSCR = '大连市' THEN '辽宁'
             WHEN a.TREDSCR = '青岛市' THEN '山东省' 
	     WHEN a.TREDSCR = '深圳市' THEN '广东省'
             WHEN a.TREDSCR = '宁波市' THEN '浙江省'
             WHEN a.TREDSCR = '厦门市' THEN '福建省'
             WHEN (a.TREDSCR = '新疆兵团' or a.TREDSCR = '新疆区') THEN '新疆区'
	     ELSE a.TREDSCR
	     END  AS PROJECT,
	(SUM(a.YEAR_AMT)-SUM(b.YEAR_AMT))/SUM(b.YEAR_AMT) AS T010101_RATE,
	'' AS 101_TATE,
	'' AS 101_RANK,
        A.ROWS_ID
FROM stg.trs_tmis_budget_income_provinces a
LEFT JOIN stg.trs_tmis_budget_income_provinces b
       ON b.data_Date = DATE_FORMAT(DATE_SUB(CONCAT(a.data_Date,'01'),INTERVAL 1 YEAR),'%Y%m')
       AND a.TRECODE = b.TRECODE
       AND a.SUBJECT_CODE = b.SUBJECT_CODE
WHERE a.data_Date = V_DATA_DATE
AND a.SUBJECT_CODE = 'T010101'
GROUP BY
        CASE WHEN a.TRECODE = '3501000000' THEN '0600000000'
             WHEN a.TRECODE = '3302000000' THEN '1500000000' 
	     WHEN a.TRECODE = '3801000000' THEN '1900000000'
             WHEN a.TRECODE = '3600000000' THEN '1100000000'
             WHEN a.TRECODE = '4000000000' THEN '1300000000'
						 WHEN (a.TRECODE = '3100000000' or a.TRECODE = '3000000000')  THEN '3000000000'
	     ELSE a.TRECODE
	     END
ORDER BY T010101_RATE  DESC
) AA ,(SELECT @rank:=0) BB
UNION ALL
(
SELECT 
	CONCAT(YEAR(CONCAT(AA.data_Date,'01')),'Q',QUARTER(CONCAT(AA.data_Date,'01'))) AS DATA_DATE,
	AA.PROJECT,
	AA.T010101_RATE,
	AA.T010101_RANK,
	AA.101_TATE,
	@rank2:=@rank2+1  AS 101_RANK,
	AA.ROWS_ID
FROM 
(
SELECT 
	a.DATA_DATE,
	CASE WHEN a.TREDSCR = '大连市' THEN '辽宁'
             WHEN a.TREDSCR = '青岛市' THEN '山东省' 
	     WHEN a.TREDSCR = '深圳市' THEN '广东省'
             WHEN a.TREDSCR = '宁波市' THEN '浙江省'
             WHEN a.TREDSCR = '厦门市' THEN '福建省'
             WHEN (a.TREDSCR = '新疆兵团' or a.TREDSCR = '新疆区') THEN '新疆区'
	     ELSE a.TREDSCR
	     END  AS PROJECT,
	'' AS T010101_RATE,
	'' AS T010101_RANK,
	(SUM(a.YEAR_AMT)-SUM(b.YEAR_AMT))/SUM(b.YEAR_AMT) AS 101_TATE,
	A.ROWS_ID
FROM stg.trs_tmis_budget_income_provinces a
LEFT JOIN stg.trs_tmis_budget_income_provinces b
       ON b.data_Date = DATE_FORMAT(DATE_SUB(CONCAT(a.data_Date,'01'),INTERVAL 1 YEAR),'%Y%m')
       AND a.TRECODE = b.TRECODE
       AND a.SUBJECT_CODE = b.SUBJECT_CODE
,(SELECT @tate:=NULL,@rank:=0) c
WHERE a.data_Date =V_DATA_DATE
AND a.SUBJECT_CODE = '101'
GROUP BY
        CASE WHEN a.TRECODE = '3501000000' THEN '0600000000'
             WHEN a.TRECODE = '3302000000' THEN '1500000000' 
	     WHEN a.TRECODE = '3801000000' THEN '1900000000'
             WHEN a.TRECODE = '3600000000' THEN '1100000000'
             WHEN a.TRECODE = '4000000000' THEN '1300000000'
						 WHEN (a.TRECODE = '3100000000' or a.TRECODE = '3000000000')  THEN '3000000000'
	     ELSE a.TRECODE
	     END
ORDER BY 101_TATE DESC
) AA ,(SELECT @rank2:=0) BB
) 
)A
GROUP BY 
        PROJECT;
  
	
	
  
  
  
	END
$$


-- PROCEDURE `indicators_lib`.`p_xunhuan_formula`
DROP PROCEDURE IF EXISTS `indicators_lib`.`p_xunhuan_formula`;
DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `p_xunhuan_formula`(in V_BATCH_DATE VARCHAR(10))
BEGIN
