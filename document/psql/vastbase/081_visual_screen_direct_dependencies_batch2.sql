-- Visual screen direct dependency source-port bundle (batch 2)
-- Extracted from source_routines.sql
-- Includes direct callees used by visual_screen.P_task_vscreen*

CREATE DEFINER=`root`@`%` PROCEDURE `p_vs_inventory_analyze`(IN v_data_date VARCHAR(10))
BEGIN
DECLARE V_STEP_ID       INT           DEFAULT 0; 
DECLARE V_PROC_NAME     VARCHAR(80)   DEFAULT 'visual_screen.p_vs_inventory_analyze.PRC';
DECLARE V_START_TIME    CHAR(19)      DEFAULT NOW();
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        GET DIAGNOSTICS CONDITION 1 @V_RETURN_CODE = RETURNED_SQLSTATE ,@V_ERROR_MSG = MESSAGE_TEXT;
        CALL ETL.EDW_PROC_ERROR_LOG(DATE_FORMAT(V_START_TIME,'%Y-%m'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,@V_RETURN_CODE,@V_ERROR_MSG);
        CALL ETL.EDW_PROC_TRACE_LOG(DATE_FORMAT(V_START_TIME,'%Y-%m'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
    END;
set V_STEP_ID = '1';
delete from visual_screen.vs_inventory_analyze where DACCT = v_data_date;
SET V_STEP_ID = '2';
insert into visual_screen.vs_inventory_analyze
    SELECT 
	DATA_DATE,
	PID,
	AREA_NO_ID,
	AREA_DSCR,
	PERIOD_FLAG,
	INDEX_NAME,
	SUM(INDEX_VALUE) ,
	SUM(GROWTH_INDEX_VALUE)
    FROM
	(
	
SELECT 
	A0.D_ACCT			 	AS DATA_DATE,
	a1.PID,
	a1.AREA_NO_ID,
	a1.AREA_DSCR,
	'1'					AS PERIOD_FLAG,
	'库存'					AS INDEX_NAME,
	ROUND(SUM(a0.F_BALANCE), 4) 		AS INDEX_VALUE,
	0					AS GROWTH_INDEX_VALUE
FROM
	stg.trs_tmis_stock a0 
  LEFT JOIN 
    (SELECT DISTINCT 
      p.GUOKU_ID AS PID,
      p.GUOKU_DSCR,
      p.AREA_NO_ID,
      p.AREA_DSCR,
      c.GUOKU_ID AS CID 
    FROM
      edw.cm_guoku_dimnsn p
      JOIN edw.cm_guoku_dimnsn c
        ON p.GUOKU_LVL_ID_2 = c.GUOKU_LVL_ID_1 
        OR p.GUOKU_LVL_ID_3 = c.GUOKU_LVL_ID_2 
        OR p.GUOKU_ID = c.GUOKU_LVL_ID_3) a1 
    ON a0.TRECODE = a1.CID
WHERE A0.D_ACCT = V_DATA_DATE
	AND A0.`TRECODE` NOT IN('2231000000','2232000000','2233000000','2230000000')
GROUP BY 
		A0.D_ACCT,
		a1.AREA_NO_ID
	
	UNION ALL 
	
SELECT 
	A0.D_ACCT			 	AS DATA_DATE,
	a1.PID,
	a1.AREA_NO_ID,
	a1.AREA_DSCR,
	'1'					AS PERIOD_FLAG,
	'库存'					AS INDEX_NAME,
	0 					AS INDEX_VALUE,
	ROUND(SUM(a0.F_BALANCE), 4)		AS GROWTH_INDEX_VALUE
FROM
	stg.trs_tmis_stock a0 
  LEFT JOIN 
    (SELECT DISTINCT 
      p.GUOKU_ID AS PID,
      p.GUOKU_DSCR,
      p.AREA_NO_ID,
      p.AREA_DSCR,
      c.GUOKU_ID AS CID 
    FROM
      edw.cm_guoku_dimnsn p
      JOIN edw.cm_guoku_dimnsn c
        ON p.GUOKU_LVL_ID_2 = c.GUOKU_LVL_ID_1 
        OR p.GUOKU_LVL_ID_3 = c.GUOKU_LVL_ID_2 
        OR p.GUOKU_ID = c.GUOKU_LVL_ID_3) a1 
    ON a0.TRECODE = a1.CID
WHERE A0.D_ACCT = DATE_SUB(V_DATA_DATE,INTERVAL 1 YEAR)
	AND A0.`TRECODE` NOT IN('2231000000','2232000000','2233000000','2230000000')
GROUP BY 
		A0.D_ACCT,
		a1.AREA_NO_ID
	) A
    GROUP BY  
          PID,
          area_no_id;
          
          
          
          
 SET V_STEP_ID=4;
UPDATE visual_screen.vs_inventory_analyze SET AREA_DSCR = '李渡新区' WHERE AREA_CODE= '500997';
UPDATE visual_screen.vs_inventory_analyze SET AREA_DSCR = '彭水县' WHERE AREA_CODE= '500243';
UPDATE visual_screen.vs_inventory_analyze SET AREA_DSCR = '酉阳县' WHERE AREA_CODE= '500242';
UPDATE visual_screen.vs_inventory_analyze SET AREA_DSCR = '秀山县' WHERE AREA_CODE= '500241';
UPDATE visual_screen.vs_inventory_analyze SET AREA_DSCR = '石柱县' WHERE AREA_CODE= '500240';
UPDATE visual_screen.vs_inventory_analyze SET AREA_DSCR = '万盛区' WHERE AREA_CODE= '500131';
SET V_STEP_ID=5;
	END
$$

-- PROCEDURE `visual_screen`.`p_vs_inventory_area`
DROP PROCEDURE IF EXISTS `visual_screen`.`p_vs_inventory_area`;
DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `p_vs_inventory_area`(IN v_data_date VARCHAR(10))
BEGIN
DECLARE V_STEP_ID       INT           DEFAULT 0; 
DECLARE V_PROC_NAME     VARCHAR(80)   DEFAULT 'visual_screen.p_vs_subject_pay.PRC';
DECLARE V_START_TIME    CHAR(19)      DEFAULT NOW();
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        GET DIAGNOSTICS CONDITION 1 @V_RETURN_CODE = RETURNED_SQLSTATE ,@V_ERROR_MSG = MESSAGE_TEXT;
        CALL ETL.EDW_PROC_ERROR_LOG(DATE_FORMAT(V_START_TIME,'%Y-%m'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,@V_RETURN_CODE,@V_ERROR_MSG);
        CALL ETL.EDW_PROC_TRACE_LOG(DATE_FORMAT(V_START_TIME,'%Y-%m'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
    END;
	set V_STEP_ID = '1';
	delete from visual_screen.vs_inventory_area where DACCT = v_data_date;
	
	set V_STEP_ID = '2';
	insert into visual_screen.vs_inventory_area
	SELECT 
		A0.D_ACCT			 	AS DATA_DATE,
		a0.TRECODE,
		cm.area_no_id,
		cm.area_dscr,
		'1'					AS PERIOD_FLAG,
		'库存余额'				AS INDEX_NAME,
		ROUND(SUM(a0.F_BALANCE), 4) 		AS INDEX_VALUE
        FROM
		stg.trs_tmis_stock a0 
	LEFT JOIN dmcode.cm_guoku_dimnsn cm
		ON a0.TRECODE = cm.guoku_id
	WHERE A0.D_ACCT = v_data_date
		AND A0.`TRECODE` NOT IN('2231000000','2232000000','2233000000','2230000000')
	GROUP BY 
		A0.D_ACCT,
		cm.area_no_id;
SET V_STEP_ID=4;
UPDATE visual_screen.vs_inventory_area SET AREA_DSCR = '李渡新区' WHERE AREA_CODE= '500997';
UPDATE visual_screen.vs_inventory_area SET AREA_DSCR = '彭水县' WHERE AREA_CODE= '500243';
UPDATE visual_screen.vs_inventory_area SET AREA_DSCR = '酉阳县' WHERE AREA_CODE= '500242';
UPDATE visual_screen.vs_inventory_area SET AREA_DSCR = '秀山县' WHERE AREA_CODE= '500241';
UPDATE visual_screen.vs_inventory_area SET AREA_DSCR = '石柱县' WHERE AREA_CODE= '500240';
UPDATE visual_screen.vs_inventory_area SET AREA_DSCR = '万盛区' WHERE AREA_CODE= '500131';
SET V_STEP_ID=5;
	END
$$

-- PROCEDURE `visual_screen`.`p_vs_inventory_balance`
DROP PROCEDURE IF EXISTS `visual_screen`.`p_vs_inventory_balance`;
DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `p_vs_inventory_balance`(IN v_data_date VARCHAR(10))
BEGIN
DECLARE V_STEP_ID       INT           DEFAULT 0; 
DECLARE V_PROC_NAME     VARCHAR(80)   DEFAULT 'visual_screen.p_vs_inventory_balance.PRC';
DECLARE V_START_TIME    CHAR(19)      DEFAULT NOW();
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        GET DIAGNOSTICS CONDITION 1 @V_RETURN_CODE = RETURNED_SQLSTATE ,@V_ERROR_MSG = MESSAGE_TEXT;
        CALL ETL.EDW_PROC_ERROR_LOG(DATE_FORMAT(V_START_TIME,'%Y-%m'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,@V_RETURN_CODE,@V_ERROR_MSG);
        CALL ETL.EDW_PROC_TRACE_LOG(DATE_FORMAT(V_START_TIME,'%Y-%m'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
    END;
    
    
    set V_STEP_ID = 1;
    delete from visual_screen.vs_inventory_balance where DACCT = v_data_date;
    
    SET V_STEP_ID = 2;
    
    insert into visual_screen.vs_inventory_balance
    SELECT 
	DATA_DATE,
	TRECODE,
	area_no_id,
	area_dscr,
	PERIOD_FLAG,
	INDEX_NAME,
	SUM(INDEX_VALUE) ,
	SUM(GROWTH_INDEX_VALUE)
    FROM
	(
	
	SELECT 
		A0.D_ACCT			 	AS DATA_DATE,
		a0.TRECODE,
		cm.area_no_id,
		cm.area_dscr,
		'1'					AS PERIOD_FLAG,
		'库存余额'				AS INDEX_NAME,
		ROUND(SUM(a0.F_BALANCE), 4) 		AS INDEX_VALUE,
		0					AS GROWTH_INDEX_VALUE
        FROM
		stg.trs_tmis_stock a0 
	LEFT JOIN dmcode.cm_guoku_dimnsn cm
		ON a0.TRECODE = cm.guoku_id
	WHERE A0.D_ACCT = v_data_date
		AND A0.`TRECODE` NOT IN('2231000000','2232000000','2233000000','2230000000','2200000000')
	GROUP BY 
		A0.D_ACCT,
		a0.TRECODE
	
	UNION ALL 
	
	SELECT 
		A0.D_ACCT			 	AS DATA_DATE,
		a0.TRECODE,
		cm.area_no_id,
		cm.area_dscr,
		'1'					AS PERIOD_FLAG,
		'库存余额'				AS INDEX_NAME,
		0			 		AS INDEX_VALUE,
		ROUND(SUM(a0.F_BALANCE), 4)		AS GROWTH_INDEX_VALUE
	FROM stg.trs_tmis_stock a0 
	LEFT JOIN dmcode.cm_guoku_dimnsn cm
		ON a0.TRECODE = cm.guoku_id
	WHERE A0.D_ACCT = DATE_SUB(v_data_date,INTERVAL 1 YEAR)
		AND A0.`TRECODE` NOT IN('2231000000','2232000000','2233000000','2230000000','2200000000')
	GROUP BY 
		A0.D_ACCT,
		a0.TRECODE
	) A
    GROUP BY  
          TRECODE,
          area_no_id;
  
  
SET V_STEP_ID=4;
UPDATE visual_screen.vs_inventory_balance SET AREA_DSCR = '李渡新区' WHERE AREA_CODE= '500997';
UPDATE visual_screen.vs_inventory_balance SET AREA_DSCR = '彭水县' WHERE AREA_CODE= '500243';
UPDATE visual_screen.vs_inventory_balance SET AREA_DSCR = '酉阳县' WHERE AREA_CODE= '500242';
UPDATE visual_screen.vs_inventory_balance SET AREA_DSCR = '秀山县' WHERE AREA_CODE= '500241';
UPDATE visual_screen.vs_inventory_balance SET AREA_DSCR = '石柱县' WHERE AREA_CODE= '500240';
UPDATE visual_screen.vs_inventory_balance SET AREA_DSCR = '万盛区' WHERE AREA_CODE= '500131';
SET V_STEP_ID=5;
	END
$$

-- PROCEDURE `visual_screen`.`P_vs_inventory_form`
DROP PROCEDURE IF EXISTS `visual_screen`.`P_vs_inventory_form`;
DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `P_vs_inventory_form`(IN v_data_date VARCHAR(10))
BEGIN
DECLARE V_STEP_ID       INT           DEFAULT 0; 
DECLARE V_PROC_NAME     VARCHAR(80)   DEFAULT 'visual_screen.P_vs_inventory_form.PRC';
DECLARE V_START_TIME    CHAR(19)      DEFAULT NOW();
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        GET DIAGNOSTICS CONDITION 1 @V_RETURN_CODE = RETURNED_SQLSTATE ,@V_ERROR_MSG = MESSAGE_TEXT;
        CALL ETL.EDW_PROC_ERROR_LOG(DATE_FORMAT(V_START_TIME,'%Y-%m'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,@V_RETURN_CODE,@V_ERROR_MSG);
        CALL ETL.EDW_PROC_TRACE_LOG(DATE_FORMAT(V_START_TIME,'%Y-%m'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
    END;
    
    
    SET V_STEP_ID = 1;
    DELETE FROM visual_screen.vs_inventory_form WHERE DACCT = V_DATA_DATE;
    INSERT INTO visual_screen.vs_inventory_form
SELECT 
  a0.D_ACCT 				AS DATA_DATE,
  a1.PID,
  a1.AREA_NO_ID,
  a1.AREA_DSCR,
  '1'					AS PERIOD_FLAG,
  '省级库存余额'			AS INDEX_NAME,
  ROUND(SUM(a0.F_BALANCE), 4) 		AS INDEX_VALUE
FROM
  stg.trs_tmis_stock a0 
  LEFT JOIN 
    (SELECT DISTINCT 
      p.GUOKU_ID AS PID,
      p.GUOKU_DSCR,
      p.AREA_NO_ID,
      p.AREA_DSCR,
      c.GUOKU_ID AS CID 
    FROM
      edw.cm_guoku_dimnsn p
      JOIN edw.cm_guoku_dimnsn c
        ON p.GUOKU_LVL_ID_2 = c.GUOKU_LVL_ID_1 
        OR p.GUOKU_LVL_ID_3 = c.GUOKU_LVL_ID_2 
        OR p.GUOKU_ID = c.GUOKU_LVL_ID_3) a1 
    ON a0.TRECODE = a1.CID
WHERE A0.D_ACCT = V_DATA_DATE
   AND A0.LEVEL = '2'
   AND A0.`TRECODE` NOT IN('2231000000','2232000000','2233000000','2230000000')
    AND a1.AREA_NO_ID = '500000'
GROUP BY 
  A0.D_ACCT,
  a1.AREA_NO_ID
  
UNION ALL 
SELECT 
  a0.D_ACCT 				AS DATA_DATE,
  a1.PID,
  a1.AREA_NO_ID,
  a1.AREA_DSCR,
  '1'					AS PERIOD_FLAG,
  '区县级库存余额'			AS INDEX_NAME,
  ROUND(SUM(a0.F_BALANCE), 4) 		AS INDEX_VALUE
FROM
  stg.trs_tmis_stock a0 
  LEFT JOIN 
    (SELECT DISTINCT 
      p.GUOKU_ID AS PID,
      p.GUOKU_DSCR,
      p.AREA_NO_ID,
      p.AREA_DSCR,
      c.GUOKU_ID AS CID 
    FROM
      edw.cm_guoku_dimnsn p
      JOIN edw.cm_guoku_dimnsn c
        ON p.GUOKU_LVL_ID_2 = c.GUOKU_LVL_ID_1 
        OR p.GUOKU_LVL_ID_3 = c.GUOKU_LVL_ID_2 
        OR p.GUOKU_ID = c.GUOKU_LVL_ID_3) a1 
    ON a0.TRECODE = a1.CID
WHERE A0.D_ACCT = V_DATA_DATE
   AND A0.LEVEL IN ('4','5')
   AND A0.`TRECODE` NOT IN('2231000000','2232000000','2233000000','2230000000')
   AND a1.AREA_NO_ID = '500000'
GROUP BY 
  A0.D_ACCT,
  a1.AREA_NO_ID;
  


  
  SET V_STEP_ID=4;
DELETE FROM visual_screen.vs_inventory_form WHERE DACCT = DATE_FORMAT(V_DATA_DATE,'%Y-%m');
INSERT INTO visual_screen.vs_inventory_form
SELECT 
  DATE_FORMAT(a0.D_ACCT,'%Y-%m')	AS DATA_DATE,
  a1.PID,
  a1.AREA_NO_ID,
  a1.AREA_DSCR,
  '2'					AS PERIOD_FLAG,
  '省级库存余额'			AS INDEX_NAME,
  ROUND(SUM(a0.F_BALANCE), 4) 		AS INDEX_VALUE
FROM
  stg.trs_tmis_stock a0 
  LEFT JOIN 
    (SELECT DISTINCT 
      p.GUOKU_ID AS PID,
      p.GUOKU_DSCR,
      p.AREA_NO_ID,
      p.AREA_DSCR,
      c.GUOKU_ID AS CID 
    FROM
      edw.cm_guoku_dimnsn p
      JOIN edw.cm_guoku_dimnsn c
        ON p.GUOKU_LVL_ID_2 = c.GUOKU_LVL_ID_1 
        OR p.GUOKU_LVL_ID_3 = c.GUOKU_LVL_ID_2 
        OR p.GUOKU_ID = c.GUOKU_LVL_ID_3) a1 
    ON a0.TRECODE = a1.CID
WHERE A0.D_ACCT = V_DATA_DATE
   AND A0.LEVEL = '2'
   AND A0.`TRECODE` NOT IN('2231000000','2232000000','2233000000','2230000000')
    AND a1.AREA_NO_ID = '500000'
GROUP BY 
  a1.AREA_NO_ID
  
UNION ALL 
SELECT 
  DATE_FORMAT(a0.D_ACCT,'%Y-%m')	AS DATA_DATE,
  a1.PID,
  a1.AREA_NO_ID,
  a1.AREA_DSCR,
  '2'					AS PERIOD_FLAG,
  '区县级库存余额'			AS INDEX_NAME,
  ROUND(SUM(a0.F_BALANCE), 4) 		AS INDEX_VALUE
FROM
  stg.trs_tmis_stock a0 
  LEFT JOIN 
    (SELECT DISTINCT 
      p.GUOKU_ID AS PID,
      p.GUOKU_DSCR,
      p.AREA_NO_ID,
      p.AREA_DSCR,
      c.GUOKU_ID AS CID 
    FROM
      edw.cm_guoku_dimnsn p
      JOIN edw.cm_guoku_dimnsn c
        ON p.GUOKU_LVL_ID_2 = c.GUOKU_LVL_ID_1 
        OR p.GUOKU_LVL_ID_3 = c.GUOKU_LVL_ID_2 
        OR p.GUOKU_ID = c.GUOKU_LVL_ID_3) a1 
    ON a0.TRECODE = a1.CID
WHERE A0.D_ACCT = V_DATA_DATE
   AND A0.LEVEL IN ('4','5')
   AND A0.`TRECODE` NOT IN('2231000000','2232000000','2233000000','2230000000')
   AND a1.AREA_NO_ID = '500000'
GROUP BY 
  A0.D_ACCT,
  a1.AREA_NO_ID;
 
  
  
  SET V_STEP_ID=5;
UPDATE visual_screen.vs_inventory_form SET AREA_DSCR = '李渡新区' WHERE AREA_CODE= '500997';
UPDATE visual_screen.vs_inventory_form SET AREA_DSCR = '彭水县' WHERE AREA_CODE= '500243';
UPDATE visual_screen.vs_inventory_form SET AREA_DSCR = '酉阳县' WHERE AREA_CODE= '500242';
UPDATE visual_screen.vs_inventory_form SET AREA_DSCR = '秀山县' WHERE AREA_CODE= '500241';
UPDATE visual_screen.vs_inventory_form SET AREA_DSCR = '石柱县' WHERE AREA_CODE= '500240';
UPDATE visual_screen.vs_inventory_form SET AREA_DSCR = '万盛区' WHERE AREA_CODE= '500131';
SET V_STEP_ID=5;
 
	END
$$

-- PROCEDURE `visual_screen`.`P_VS_LAND_TRANSFER`
DROP PROCEDURE IF EXISTS `visual_screen`.`P_VS_LAND_TRANSFER`;
DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `P_VS_LAND_TRANSFER`(IN V_DATA_DATE VARCHAR(10))
BEGIN
DECLARE V_STEP_ID       INT           DEFAULT 0; 
DECLARE V_PROC_NAME     VARCHAR(80)   DEFAULT 'visual_screen.p_vs_land_transfer.PRC';
DECLARE V_START_TIME    CHAR(19)      DEFAULT NOW();
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        GET DIAGNOSTICS CONDITION 1 @V_RETURN_CODE = RETURNED_SQLSTATE ,@V_ERROR_MSG = MESSAGE_TEXT;
        CALL ETL.EDW_PROC_ERROR_LOG(DATE_FORMAT(V_START_TIME,'%Y-%m'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,@V_RETURN_CODE,@V_ERROR_MSG);
        CALL ETL.EDW_PROC_TRACE_LOG(DATE_FORMAT(V_START_TIME,'%Y-%m'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
    END;
	 CALL ETL.EDW_PROC_TRACE_LOG(DATE_FORMAT(V_START_TIME,'%Y%m%d'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
 
SET V_STEP_ID = 1;     
DELETE FROM visual_screen.vs_land_transfer WHERE DACCT = DATE_FORMAT(V_DATA_DATE,'%Y-%m'); 
SET V_STEP_ID = 2;  
INSERT INTO visual_screen.vs_land_transfer 
SELECT	
	A.DACCT, 
	A.PID,
	A.AREA_NO_ID,
	A.AREA_DSCR,
	'2', 
	'土地出让收入金额'        ,  
	(SUM(A.THIS_AMT)-IFNULL(SUM(B.THIS_AMT),0)) AS  THIS_AMT
FROM
(
SELECT 
	DATE_FORMAT(V_DATA_DATE,'%Y-%m')	AS DACCT, 
	b.PID,
	b.AREA_NO_ID,
	b.AREA_DSCR,
	SUM(a.THIS_AMT) 			AS THIS_AMT
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
  AND a.SUBJECT_CODE IN('1030148','1030146','1030147')
  AND a.`LEVEL`>=b.LEVEL
  AND a.`TRECODE` NOT IN('2231000000','2232000000','2233000000','2230000000')
GROUP BY 
  DATE_FORMAT(a.D_ACCT,'%Y-%m'),
  b.AREA_NO_ID
)  A
LEFT JOIN   
(
SELECT 
	DATE_FORMAT(V_DATA_DATE,'%Y-%m')	AS DACCT, 
	b.PID,
	b.AREA_NO_ID,
	b.AREA_DSCR,
	SUM(a.THIS_AMT) 			AS THIS_AMT
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
  AND a.SUBJECT_CODE = '103014898'
  AND a.`LEVEL`>=b.LEVEL
  AND a.`TRECODE` NOT IN('2231000000','2232000000','2233000000','2230000000')
GROUP BY 
  DATE_FORMAT(a.D_ACCT,'%Y-%m'),
  b.AREA_NO_ID
) B
ON A.DACCT = B.DACCT
AND A.PID = B.PID
AND A.AREA_NO_ID = B.AREA_NO_ID
GROUP BY
   A.DACCT,
   A.PID,
   A.AREA_NO_ID;
INSERT INTO visual_screen.vs_land_transfer
	
SELECT
  a.DACCT,
  a.GUOKU_ID,
  a.AREA_CODE,
  a.AREA_DSCR,
  a.PERIOD_FLAG,
  CONCAT(a.INDEX_NAME,'同比'),
  (a.INDEX_VALUE-b.INDEX_VALUE)/b.INDEX_VALUE
FROM visual_screen.vs_land_transfer  a
LEFT JOIN visual_screen.vs_land_transfer  b
       ON b.DACCT = DATE_FORMAT(DATE_SUB(V_DATA_DATE,INTERVAL 1 YEAR),'%Y-%m')
       AND a.GUOKU_ID = b.GUOKU_ID
       AND a.AREA_CODE = b.AREA_CODE
       AND a.INDEX_NAME = b.INDEX_NAME
       AND a.PERIOD_FLAG = b.PERIOD_FLAG
WHERE a.DACCT = DATE_FORMAT(V_DATA_DATE,'%Y-%m')
  AND a.PERIOD_FLAG = '2'
  AND a.INDEX_NAME  = '土地出让收入金额';
	
SET V_STEP_ID = 3;     
DELETE FROM visual_screen.vs_land_transfer WHERE DACCT = left(V_DATA_DATE,4);
SET V_STEP_ID = 4; 
INSERT INTO visual_screen.vs_land_transfer
	
SELECT
  LEFT(a.DACCT,4),
  a.GUOKU_ID,
  a.AREA_CODE,
  a.AREA_DSCR,
  '4'   AS PERIOD_FLAG,
  a.INDEX_NAME,
  SUM(a.INDEX_VALUE)
FROM visual_screen.vs_land_transfer  a
WHERE a.DACCT LIKE CONCAT(LEFT(v_data_date,4),'%')
  AND a.PERIOD_FLAG = '2'
  AND a.INDEX_NAME = '土地出让收入金额'
GROUP BY 
  a.GUOKU_ID,
  a.AREA_CODE,
  a.PERIOD_FLAG,
  a.INDEX_NAME;
INSERT INTO visual_screen.vs_land_transfer
	
SELECT
  a.DACCT,
  a.GUOKU_ID,
  a.AREA_CODE,
  a.AREA_DSCR,
  a.PERIOD_FLAG,
  CONCAT(a.INDEX_NAME,'同比'),
  (a.INDEX_VALUE-b.INDEX_VALUE)/b.INDEX_VALUE
FROM visual_screen.vs_land_transfer  a
LEFT JOIN visual_screen.vs_land_transfer  b
       ON b.DACCT = DATE_FORMAT(DATE_SUB(v_data_date,INTERVAL 1 YEAR),'%Y')
       AND a.GUOKU_ID = b.GUOKU_ID
       AND a.AREA_CODE = b.AREA_CODE
       AND a.INDEX_NAME = b.INDEX_NAME
       AND a.PERIOD_FLAG = b.PERIOD_FLAG
WHERE a.DACCT = DATE_FORMAT(v_data_date,'%Y')
  AND a.PERIOD_FLAG = '4'
  AND a.INDEX_NAME  = '土地出让收入金额';
	
	
  SET V_STEP_ID=4;
UPDATE visual_screen.vs_land_transfer SET AREA_DSCR = '李渡新区' WHERE AREA_CODE= '500997';
UPDATE visual_screen.vs_land_transfer SET AREA_DSCR = '彭水县' WHERE AREA_CODE= '500243';
UPDATE visual_screen.vs_land_transfer SET AREA_DSCR = '酉阳县' WHERE AREA_CODE= '500242';
UPDATE visual_screen.vs_land_transfer SET AREA_DSCR = '秀山县' WHERE AREA_CODE= '500241';
UPDATE visual_screen.vs_land_transfer SET AREA_DSCR = '石柱县' WHERE AREA_CODE= '500240';
UPDATE visual_screen.vs_land_transfer SET AREA_DSCR = '万盛区' WHERE AREA_CODE= '500131';
SET V_STEP_ID=5;
CALL ETL.EDW_PROC_TRACE_LOG(DATE_FORMAT(V_START_TIME,'%Y%m%d'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
	END
$$

-- PROCEDURE `visual_screen`.`P_VS_LOCAL_FINANCIAL_RESOURCES`
DROP PROCEDURE IF EXISTS `visual_screen`.`P_VS_LOCAL_FINANCIAL_RESOURCES`;
DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `P_VS_LOCAL_FINANCIAL_RESOURCES`(IN V_DATA_DATE VARCHAR(10))
BEGIN

DECLARE V_STEP_ID       INT           DEFAULT 0; 
DECLARE V_PROC_NAME     VARCHAR(80)   DEFAULT 'visual_screen.P_VS_LOCAL_FINANCIAL_RESOURCES.PRC';
DECLARE V_START_TIME    CHAR(19)      DEFAULT NOW();
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        GET DIAGNOSTICS CONDITION 1 @V_RETURN_CODE = RETURNED_SQLSTATE ,@V_ERROR_MSG = MESSAGE_TEXT;
        CALL ETL.EDW_PROC_ERROR_LOG(DATE_FORMAT(V_START_TIME,'%Y-%m'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,@V_RETURN_CODE,@V_ERROR_MSG);
        CALL ETL.EDW_PROC_TRACE_LOG(DATE_FORMAT(V_START_TIME,'%Y-%m'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
    END;
	 CALL ETL.EDW_PROC_TRACE_LOG(DATE_FORMAT(V_START_TIME,'%Y%m%d'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
 
SET V_STEP_ID = 1;     
DELETE FROM visual_screen.vs_local_financial_resources WHERE DACCT = DATE_FORMAT(V_DATA_DATE,'%Y-%m');
SET V_STEP_ID = 2; 
INSERT INTO visual_screen.vs_local_financial_resources
SELECT 
	DATE_FORMAT(v_data_date,'%Y-%m'), 
	b.PID,
	b.AREA_NO_ID,
	b.AREA_DSCR,
	'2', 
	'地方债收入本期值'        ,  
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
WHERE DATE_FORMAT(a.D_ACCT,'%Y-%m')=DATE_FORMAT(v_data_date,'%Y-%m')
  AND a.SUBJECT_CODE IN ('T0103','T0105')
  AND a.`LEVEL`>=b.LEVEL
  AND a.`TRECODE` NOT IN('2231000000','2232000000','2233000000','2230000000')
GROUP BY 
  DATE_FORMAT(a.D_ACCT,'%Y-%m'),
  b.AREA_NO_ID;
	
INSERT INTO visual_screen.vs_local_financial_resources
SELECT
  a.DACCT,
  a.GUOKU_ID,
  a.AREA_CODE,
  a.AREA_DSCR,
  a.PERIOD_FLAG,
  CONCAT(a.INDEX_NAME,'同比'),
  (a.INDEX_VALUE-b.INDEX_VALUE)/b.INDEX_VALUE
FROM visual_screen.vs_local_financial_resources  a
LEFT JOIN visual_screen.vs_local_financial_resources  b
       ON b.DACCT = DATE_FORMAT(DATE_SUB(v_data_date,INTERVAL 1 YEAR),'%Y-%m')
       AND a.GUOKU_ID = b.GUOKU_ID
       AND a.AREA_CODE = b.AREA_CODE
       AND a.INDEX_NAME = b.INDEX_NAME
       AND a.PERIOD_FLAG = b.PERIOD_FLAG
WHERE a.DACCT = DATE_FORMAT(v_data_date,'%Y-%m')
  AND a.PERIOD_FLAG = '2'
  AND a.INDEX_NAME  = '地方债收入本期值';
	
	
SET V_STEP_ID = 3;     
DELETE FROM visual_screen.vs_local_financial_resources WHERE DACCT = left(V_DATA_DATE,4);
SET V_STEP_ID = 4; 
INSERT INTO visual_screen.vs_local_financial_resources
SELECT
  LEFT(a.DACCT,4),
  a.GUOKU_ID,
  a.AREA_CODE,
  a.AREA_DSCR,
  '4'   AS PERIOD_FLAG,
  a.INDEX_NAME,
  SUM(a.INDEX_VALUE)
FROM visual_screen.vs_local_financial_resources  a
WHERE a.DACCT LIKE CONCAT(LEFT(v_data_date,4),'%')
  AND a.PERIOD_FLAG = '2'
  AND a.INDEX_NAME = '地方债收入本期值'
GROUP BY 
  a.GUOKU_ID,
  a.AREA_CODE,
  a.PERIOD_FLAG,
  a.INDEX_NAME;
INSERT INTO visual_screen.vs_local_financial_resources	
SELECT
  a.DACCT,
  a.GUOKU_ID,
  a.AREA_CODE,
  a.AREA_DSCR,
  a.PERIOD_FLAG,
  CONCAT(a.INDEX_NAME,'同比'),
  (a.INDEX_VALUE-b.INDEX_VALUE)/b.INDEX_VALUE
FROM visual_screen.vs_local_financial_resources  a
LEFT JOIN visual_screen.vs_local_financial_resources  b
       ON b.DACCT = DATE_FORMAT(DATE_SUB(v_data_date,INTERVAL 1 YEAR),'%Y')
       AND a.GUOKU_ID = b.GUOKU_ID
       AND a.AREA_CODE = b.AREA_CODE
       AND a.INDEX_NAME = b.INDEX_NAME
       AND a.PERIOD_FLAG = b.PERIOD_FLAG
WHERE a.DACCT = DATE_FORMAT(v_data_date,'%Y')
  AND a.PERIOD_FLAG = '4'
  AND a.INDEX_NAME  = '地方债收入本期值';
	
	
  SET V_STEP_ID=4;
UPDATE visual_screen.vs_local_financial_resources SET AREA_DSCR = '李渡新区' WHERE AREA_CODE= '500997';
UPDATE visual_screen.vs_local_financial_resources SET AREA_DSCR = '彭水县' WHERE AREA_CODE= '500243';
UPDATE visual_screen.vs_local_financial_resources SET AREA_DSCR = '酉阳县' WHERE AREA_CODE= '500242';
UPDATE visual_screen.vs_local_financial_resources SET AREA_DSCR = '秀山县' WHERE AREA_CODE= '500241';
UPDATE visual_screen.vs_local_financial_resources SET AREA_DSCR = '石柱县' WHERE AREA_CODE= '500240';
UPDATE visual_screen.vs_local_financial_resources SET AREA_DSCR = '万盛区' WHERE AREA_CODE= '500131';
SET V_STEP_ID=5;
CALL ETL.EDW_PROC_TRACE_LOG(DATE_FORMAT(V_START_TIME,'%Y%m%d'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
	END
$$

-- PROCEDURE `visual_screen`.`P_VS_MUNICIPALITIES_DIRECTLY`
DROP PROCEDURE IF EXISTS `visual_screen`.`P_VS_MUNICIPALITIES_DIRECTLY`;
DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `P_VS_MUNICIPALITIES_DIRECTLY`(IN V_DATA_DATE VARCHAR(10))
BEGIN

DECLARE V_STEP_ID       INT           DEFAULT 0; 
DECLARE V_PROC_NAME     VARCHAR(80)   DEFAULT 'visual_screen.P_VS_MUNICIPALITIES_DIRECTLY.PRC';
DECLARE V_START_TIME    CHAR(19)      DEFAULT NOW();
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        GET DIAGNOSTICS CONDITION 1 @V_RETURN_CODE = RETURNED_SQLSTATE ,@V_ERROR_MSG = MESSAGE_TEXT;
        CALL ETL.EDW_PROC_ERROR_LOG(DATE_FORMAT(V_START_TIME,'%Y-%m'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,@V_RETURN_CODE,@V_ERROR_MSG);
        CALL ETL.EDW_PROC_TRACE_LOG(DATE_FORMAT(V_START_TIME,'%Y-%m'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
    END;
	 CALL ETL.EDW_PROC_TRACE_LOG(DATE_FORMAT(V_START_TIME,'%Y%m%d'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
 
SET V_STEP_ID = 1;     
DELETE FROM visual_screen.vs_municipalities_directly WHERE DACCT = DATE_FORMAT(V_DATA_DATE,'%Y-%m');
SET V_STEP_ID = 2; 
INSERT INTO visual_screen.vs_municipalities_directly
 SELECT
DISTINCT
  DATE_FORMAT(V_DATA_DATE,'%Y-%m'),
  a.AREA_NO,
  a.TRECODE,
  a.TREDSCR,
  '2',
  a.SUBJECT_DSCR,
  a.INDEX_VALUE	,
  a.INDEX_VALUE1,
   b.SUBJECT_DSCR,
  b.INDEX_VALUE,
  b.INDEX_VALUE1
  FROM 
(
SELECT
 DISTINCT
  DATE_FORMAT(V_DATA_DATE,'%Y-%m'),
  a.AREA_NO,
  a.TRECODE,
  a.TREDSCR,
  '2',
  a.SUBJECT_DSCR,
  ROUND(SUM(a.THIS_AMT)*100000000,4) AS INDEX_VALUE,
 ROUND(SUM(a1.THIS_AMT)*100000000,4) AS INDEX_VALUE1
  FROM stg.trs_tmis_budget_income_provinces a 
  LEFT JOIN stg.trs_tmis_budget_income_provinces a1 
  ON a1.DATA_DATE = DATE_FORMAT(DATE_SUB(V_DATA_DATE, INTERVAL 1 YEAR),'%Y%m')
   AND a.TRECODE = a1.TRECODE
   AND a.SUBJECT_CODE = a1.SUBJECT_CODE
  WHERE  a.DATA_DATE=DATE_FORMAT(V_DATA_DATE,'%Y%m')
  AND  a.TRECODE = '0100000000'
  AND a.SUBJECT_CODE = '101'
  ) a 
  LEFT JOIN (
  SELECT
 DISTINCT
  DATE_FORMAT(V_DATA_DATE,'%Y-%m'),
  a.AREA_NO,
  a.TRECODE,
  a.TREDSCR,
  '2',
  a.SUBJECT_DSCR,
  ROUND(SUM(a.THIS_AMT)*100000000,4) AS INDEX_VALUE,
  ROUND(SUM(a1.THIS_AMT)*100000000,4) AS INDEX_VALUE1
  FROM stg.trs_tmis_budget_income_provinces a 
  LEFT JOIN stg.trs_tmis_budget_income_provinces a1 
  ON a1.DATA_DATE = DATE_FORMAT(DATE_SUB(V_DATA_DATE, INTERVAL 1 YEAR),'%Y%m')
   AND a.TRECODE = a1.TRECODE
   AND a.SUBJECT_CODE = a1.SUBJECT_CODE
  WHERE  a.DATA_DATE=DATE_FORMAT(V_DATA_DATE,'%Y%m')
  AND  a.TRECODE = '0100000000'
  AND a.SUBJECT_CODE = 'T010101'
  ) b 
  ON a.TRECODE=b.TRECODE
  
  UNION ALL
  
  SELECT
DISTINCT
  DATE_FORMAT(V_DATA_DATE,'%Y-%m'),
  a.AREA_NO,
  a.TRECODE,
  a.TREDSCR,
  '2',
  a.SUBJECT_DSCR,
  a.INDEX_VALUE	,
  a.INDEX_VALUE1,
   b.SUBJECT_DSCR,
  b.INDEX_VALUE,
  b.INDEX_VALUE1
  FROM 
(
SELECT
 DISTINCT
  DATE_FORMAT(V_DATA_DATE,'%Y-%m'),
  a.AREA_NO,
  a.TRECODE,
  a.TREDSCR,
  '2',
  a.SUBJECT_DSCR,
  ROUND(SUM(a.THIS_AMT)*100000000,4) AS INDEX_VALUE,
  ROUND(SUM(a1.THIS_AMT)*100000000,4) AS INDEX_VALUE1
  FROM stg.trs_tmis_budget_income_provinces a 
  LEFT JOIN stg.trs_tmis_budget_income_provinces a1 
  ON a1.DATA_DATE = DATE_FORMAT(DATE_SUB(V_DATA_DATE, INTERVAL 1 YEAR),'%Y%m')
   AND a.TRECODE = a1.TRECODE
   AND a.SUBJECT_CODE = a1.SUBJECT_CODE
  WHERE  a.DATA_DATE=DATE_FORMAT(V_DATA_DATE,'%Y%m')
  AND  a.TRECODE = '0200000000'
  AND a.SUBJECT_CODE = '101'
  ) a 
  LEFT JOIN (
  SELECT
 DISTINCT
  DATE_FORMAT(V_DATA_DATE,'%Y-%m'),
  a.AREA_NO,
  a.TRECODE,
  a.TREDSCR,
  '2',
  a.SUBJECT_DSCR,
  ROUND(SUM(a.THIS_AMT)*100000000,4) AS INDEX_VALUE,
  ROUND(SUM(a1.THIS_AMT)*100000000,4) AS INDEX_VALUE1
  FROM stg.trs_tmis_budget_income_provinces a 
  LEFT JOIN stg.trs_tmis_budget_income_provinces a1 
  ON a1.DATA_DATE = DATE_FORMAT(DATE_SUB(V_DATA_DATE, INTERVAL 1 YEAR),'%Y%m')
   AND a.TRECODE = a1.TRECODE
   AND a.SUBJECT_CODE = a1.SUBJECT_CODE
  WHERE  a.DATA_DATE=DATE_FORMAT(V_DATA_DATE,'%Y%m')
  AND  a.TRECODE = '0200000000'
  AND a.SUBJECT_CODE = 'T010101'
  ) b 
  ON a.TRECODE=b.TRECODE
  UNION ALL
  SELECT
DISTINCT
  DATE_FORMAT(V_DATA_DATE,'%Y-%m'),
  a.AREA_NO,
  a.TRECODE,
  a.TREDSCR,
  '2',
  a.SUBJECT_DSCR,
  a.INDEX_VALUE	,
  a.INDEX_VALUE1,
   b.SUBJECT_DSCR,
  b.INDEX_VALUE,
  b.INDEX_VALUE1
  FROM 
(
SELECT
 DISTINCT
  DATE_FORMAT(V_DATA_DATE,'%Y-%m'),
  a.AREA_NO,
  a.TRECODE,
  a.TREDSCR,
  '2',
  a.SUBJECT_DSCR,
  ROUND(SUM(a.THIS_AMT)*100000000,4) AS INDEX_VALUE,
  ROUND(SUM(a1.THIS_AMT)*100000000,4) AS INDEX_VALUE1
  FROM stg.trs_tmis_budget_income_provinces a 
  LEFT JOIN stg.trs_tmis_budget_income_provinces a1 
  ON a1.DATA_DATE = DATE_FORMAT(DATE_SUB(V_DATA_DATE, INTERVAL 1 YEAR),'%Y%m')
   AND a.TRECODE = a1.TRECODE
   AND a.SUBJECT_CODE = a1.SUBJECT_CODE
  WHERE  a.DATA_DATE=DATE_FORMAT(V_DATA_DATE,'%Y%m')
  AND  a.TRECODE = '0900000000'
  AND a.SUBJECT_CODE = '101'
  ) a 
  LEFT JOIN (
  SELECT
 DISTINCT
  DATE_FORMAT(V_DATA_DATE,'%Y-%m'),
  a.AREA_NO,
  a.TRECODE,
  a.TREDSCR,
  '2',
  a.SUBJECT_DSCR,
  ROUND(SUM(a.THIS_AMT)*100000000,4) AS INDEX_VALUE,
  ROUND(SUM(a1.THIS_AMT)*100000000,4) AS INDEX_VALUE1
  FROM stg.trs_tmis_budget_income_provinces a 
  LEFT JOIN stg.trs_tmis_budget_income_provinces a1 
  ON a1.DATA_DATE = DATE_FORMAT(DATE_SUB(V_DATA_DATE, INTERVAL 1 YEAR),'%Y%m')
   AND a.TRECODE = a1.TRECODE
   AND a.SUBJECT_CODE = a1.SUBJECT_CODE
  WHERE  a.DATA_DATE=DATE_FORMAT(V_DATA_DATE,'%Y%m')
  AND  a.TRECODE = '0900000000'
  AND a.SUBJECT_CODE = 'T010101'
  ) b 
  ON a.TRECODE=b.TRECODE
  UNION ALL
  SELECT
DISTINCT
  DATE_FORMAT(V_DATA_DATE,'%Y-%m'),
  a.AREA_NO,
  a.TRECODE,
  a.TREDSCR,
  '2',
  a.SUBJECT_DSCR,
  a.INDEX_VALUE	,
  a.INDEX_VALUE1,
   b.SUBJECT_DSCR,
  b.INDEX_VALUE,
  b.INDEX_VALUE1
  FROM 
(
SELECT
 DISTINCT
  DATE_FORMAT(V_DATA_DATE,'%Y-%m'),
  a.AREA_NO,
  a.TRECODE,
  a.TREDSCR,
  '2',
  a.SUBJECT_DSCR,
  ROUND(SUM(a.THIS_AMT)*100000000,4) AS INDEX_VALUE,
   ROUND(SUM(a1.THIS_AMT)*100000000,4) AS INDEX_VALUE1
  FROM stg.trs_tmis_budget_income_provinces a 
  LEFT JOIN stg.trs_tmis_budget_income_provinces a1 
  ON a1.DATA_DATE = DATE_FORMAT(DATE_SUB(V_DATA_DATE, INTERVAL 1 YEAR),'%Y%m')
   AND a.TRECODE = a1.TRECODE
   AND a.SUBJECT_CODE = a1.SUBJECT_CODE
  WHERE  a.DATA_DATE=DATE_FORMAT(V_DATA_DATE,'%Y%m')
  AND  a.TRECODE = '2200000000'
  AND a.SUBJECT_CODE = '101'
  ) a 
  LEFT JOIN (
  SELECT
 DISTINCT
  DATE_FORMAT(V_DATA_DATE,'%Y-%m'),
  a.AREA_NO,
  a.TRECODE,
  a.TREDSCR,
  '2',
  a.SUBJECT_DSCR,
  ROUND(SUM(a.THIS_AMT)*100000000,4) AS INDEX_VALUE,
 ROUND(SUM(a1.THIS_AMT)*100000000,4) AS INDEX_VALUE1
  FROM stg.trs_tmis_budget_income_provinces a 
  LEFT JOIN stg.trs_tmis_budget_income_provinces a1 
  ON a1.DATA_DATE = DATE_FORMAT(DATE_SUB(V_DATA_DATE, INTERVAL 1 YEAR),'%Y%m')
   AND a.TRECODE = a1.TRECODE
   AND a.SUBJECT_CODE = a1.SUBJECT_CODE
  WHERE  a.DATA_DATE=DATE_FORMAT(V_DATA_DATE,'%Y%m')
  AND  a.TRECODE = '2200000000'
  AND a.SUBJECT_CODE = 'T010101'
  ) b 
  ON a.TRECODE=b.TRECODE
	;
SET V_STEP_ID=4;
 UPDATE visual_screen.vs_municipalities_directly SET AREA_DSCR =REPLACE(AREA_DSCR, '市','');
set V_STEP_ID=5;
 UPDATE visual_screen.vs_municipalities_directly SET AREA_DSCR =REPLACE(AREA_DSCR, '省','');
 SET V_STEP_ID=6;
 CALL ETL.EDW_PROC_TRACE_LOG(DATE_FORMAT(V_START_TIME,'%Y%m%d'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
	END
$$

-- PROCEDURE `visual_screen`.`P_VS_PILLAR_INDUSTRIES`
DROP PROCEDURE IF EXISTS `visual_screen`.`P_VS_PILLAR_INDUSTRIES`;
DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `P_VS_PILLAR_INDUSTRIES`(IN V_DATA_DATE VARCHAR(10))
BEGIN

DECLARE V_STEP_ID       INT           DEFAULT 0; 
DECLARE V_PROC_NAME     VARCHAR(80)   DEFAULT 'visual_screen.P_VS_PILLAR_INDUSTRIES.PRC';
DECLARE V_START_TIME    CHAR(19)      DEFAULT NOW();
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        GET DIAGNOSTICS CONDITION 1 @V_RETURN_CODE = RETURNED_SQLSTATE ,@V_ERROR_MSG = MESSAGE_TEXT;
        CALL ETL.EDW_PROC_ERROR_LOG(DATE_FORMAT(V_START_TIME,'%Y-%m'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,@V_RETURN_CODE,@V_ERROR_MSG);
        CALL ETL.EDW_PROC_TRACE_LOG(DATE_FORMAT(V_START_TIME,'%Y-%m'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
    END;
	 CALL ETL.EDW_PROC_TRACE_LOG(DATE_FORMAT(V_START_TIME,'%Y%m%d'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
 
SET V_STEP_ID = 1;     
DELETE FROM visual_screen.vs_pillar_industries WHERE DACCT = DATE_FORMAT(V_DATA_DATE,'%Y-%m');
SET V_STEP_ID = 2; 
INSERT INTO visual_screen.vs_pillar_industries
  SELECT 
	DATE_FORMAT(DET.S_INTREDATE,'%Y-%m') AS DACCT,
	B.PID,
	INDUS.lev_1_id, 
	INDUS.lev_1_dscr,
	'2',
	SUM(F_AMT)	
FROM ODS.TV_FIN_INCOME_DETAIL DET    
LEFT JOIN ODS.TRS_ENTERPRISE_INFOR INFO
       ON DET.TAXPAYCODE = INFO.UNISCID
LEFT JOIN EDW.CM_CLR_INDUSTRY_INFO INDUS
       ON INFO.INDUSTRYCO = INDUS.LEV_4_ID
       LEFT JOIN 
	(
	SELECT 
		DISTINCT 
		p.GUOKU_ID AS PID,
		p.LEVEL,
		p.GUOKU_DSCR,
		c.GUOKU_ID AS CID
	FROM edw.cm_guoku_dimnsn p
	LEFT JOIN edw.cm_guoku_dimnsn c
	ON p.GUOKU_LVL_ID_2 = c.GUOKU_LVL_ID_1 
	OR  p.GUOKU_LVL_ID_3 = c.GUOKU_LVL_ID_2 
	OR  p.GUOKU_ID = c.GUOKU_LVL_ID_3
	) B 
     ON DET.S_TRECODE = B.CID	
WHERE DATE_FORMAT(DET.S_INTREDATE,'%Y-%m') = DATE_FORMAT(V_DATA_DATE,'%Y-%m')
	AND INDUS.LEV_1_ID IN ('C','K','D')
	AND DET.C_BDGLEVEL >= B.LEVEL
GROUP BY 
	DATE_FORMAT(DET.S_INTREDATE,'%Y-%m'),
	INDUS.LEV_1_ID,
	DET.S_TRECODE;
	
  SET V_STEP_ID=4;
UPDATE visual_screen.vs_pillar_industries SET AREA_DSCR = '李渡新区' WHERE AREA_CODE= '500997';
UPDATE visual_screen.vs_pillar_industries SET AREA_DSCR = '彭水县' WHERE AREA_CODE= '500243';
UPDATE visual_screen.vs_pillar_industries SET AREA_DSCR = '酉阳县' WHERE AREA_CODE= '500242';
UPDATE visual_screen.vs_pillar_industries SET AREA_DSCR = '秀山县' WHERE AREA_CODE= '500241';
UPDATE visual_screen.vs_pillar_industries SET AREA_DSCR = '石柱县' WHERE AREA_CODE= '500240';
UPDATE visual_screen.vs_pillar_industries SET AREA_DSCR = '万盛区' WHERE AREA_CODE= '500131';
SET V_STEP_ID=5;
CALL ETL.EDW_PROC_TRACE_LOG(DATE_FORMAT(V_START_TIME,'%Y%m%d'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
	END
$$

-- PROCEDURE `visual_screen`.`P_VS_PUBLIC_BUDGET`
DROP PROCEDURE IF EXISTS `visual_screen`.`P_VS_PUBLIC_BUDGET`;
DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `P_VS_PUBLIC_BUDGET`(IN V_DATA_DATE VARCHAR(10))
BEGIN
DECLARE V_STEP_ID       INT           DEFAULT 0; 
DECLARE V_PROC_NAME     VARCHAR(80)   DEFAULT 'visual_screen.P_VS_PUBLIC_BUDGET.PRC';
DECLARE V_START_TIME    CHAR(19)      DEFAULT NOW();
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        GET DIAGNOSTICS CONDITION 1 @V_RETURN_CODE = RETURNED_SQLSTATE ,@V_ERROR_MSG = MESSAGE_TEXT;
        CALL ETL.EDW_PROC_ERROR_LOG(DATE_FORMAT(V_START_TIME,'%Y-%m'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,@V_RETURN_CODE,@V_ERROR_MSG);
        CALL ETL.EDW_PROC_TRACE_LOG(DATE_FORMAT(V_START_TIME,'%Y-%m'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
    END;
	 CALL ETL.EDW_PROC_TRACE_LOG(DATE_FORMAT(V_START_TIME,'%Y%m%d'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
 
SET V_STEP_ID = 1;     
DELETE FROM visual_screen.vs_public_budget WHERE DACCT = DATE_FORMAT(V_DATA_DATE,'%Y-%m');
SET V_STEP_ID = 2; 
INSERT INTO visual_screen.vs_public_budget
SELECT
	DATE_FORMAT(a.D_ACCT,'%Y-%m'),
	a1.PID,
	a1.AREA_NO_ID,
	a1.AREA_DSCR,
	'2',
	'一般公共预算收入金额',	
	SUM(a.THIS_AMT)
 FROM
  stg.trs_tmis_budget_income a
  LEFT JOIN 
    (SELECT DISTINCT 
      p.GUOKU_ID AS PID,
      p.LEVEL,
      p.GUOKU_DSCR,
      p.AREA_NO_ID,
      p.AREA_DSCR,
      c.GUOKU_ID AS CID 
    FROM
      edw.cm_guoku_dimnsn p
      JOIN edw.cm_guoku_dimnsn c
        ON p.GUOKU_LVL_ID_2 = c.GUOKU_LVL_ID_1 
        OR p.GUOKU_LVL_ID_3 = c.GUOKU_LVL_ID_2 
        OR p.GUOKU_ID = c.GUOKU_LVL_ID_3) a1 
    ON a.TRECODE = a1.CID
WHERE DATE_FORMAT(a.D_ACCT,'%Y-%m')=DATE_FORMAT(V_DATA_DATE,'%Y-%m')
  AND a.SUBJECT_CODE = 'T010101'
  AND a.`LEVEL`>=a1.LEVEL
  AND a.`TRECODE` NOT IN('2231000000','2232000000','2233000000','2230000000')
GROUP BY 
  DATE_FORMAT(V_DATA_DATE,'%Y-%m'),
  a1.AREA_NO_ID;
	
INSERT INTO visual_screen.vs_public_budget
SELECT
	a.DACCT,
	a.GUOKU_ID,
	a.AREA_CODE,
	a.AREA_DSCR,
	a.PERIOD_FLAG,
	'一般公共预算收入金额同比'		     as INDEX_NAME,	
	(a.INDEX_VALUE-c.INDEX_VALUE)/c.INDEX_VALUE  AS INDEX_VALUE
 FROM visual_screen.vs_public_budget a
 LEFT JOIN visual_screen.vs_public_budget c 
         ON c.DACCT = DATE_FORMAT(DATE_SUB(V_DATA_DATE,INTERVAL 1 YEAR),'%Y-%m')
        AND a.AREA_CODE = c.AREA_CODE
        AND a.GUOKU_ID  = c.GUOKU_ID
        AND a.INDEX_NAME = c.INDEX_NAME
        AND a.PERIOD_FLAG = c.PERIOD_FLAG
WHERE a.DACCT=DATE_FORMAT(V_DATA_DATE,'%Y-%m')
        AND a.INDEX_NAME = '一般公共预算收入金额'
        AND a.PERIOD_FLAG = '2';
	
	
	
SET V_STEP_ID = 1;     
DELETE FROM visual_screen.vs_public_budget WHERE DACCT = left(V_DATA_DATE,4);
SET V_STEP_ID = 2; 
INSERT INTO visual_screen.vs_public_budget
SELECT
	DATE_FORMAT(a.D_ACCT,'%Y'),
	a1.PID,
	a1.AREA_NO_ID,
	a1.AREA_DSCR,
	'4',
	'一般公共预算收入金额',	
	SUM(a.THIS_AMT)
 FROM
  stg.trs_tmis_budget_income a
 LEFT JOIN 
    (SELECT DISTINCT 
      p.GUOKU_ID AS PID,
      p.LEVEL,
      p.GUOKU_DSCR,
      p.AREA_NO_ID,
      p.AREA_DSCR,
      c.GUOKU_ID AS CID 
    FROM
      edw.cm_guoku_dimnsn p
      JOIN edw.cm_guoku_dimnsn c
        ON p.GUOKU_LVL_ID_2 = c.GUOKU_LVL_ID_1 
        OR p.GUOKU_LVL_ID_3 = c.GUOKU_LVL_ID_2 
        OR p.GUOKU_ID = c.GUOKU_LVL_ID_3) a1 
    ON a.TRECODE = a1.CID
WHERE DATE_FORMAT(a.D_ACCT,'%Y')=DATE_FORMAT(V_DATA_DATE,'%Y')
  AND a.SUBJECT_CODE = 'T010101'
  AND a.`LEVEL` >= a1.LEVEL
  AND a.`TRECODE` NOT IN('2231000000','2232000000','2233000000','2230000000')
GROUP BY 
  a1.area_no_id;
  
INSERT INTO visual_screen.vs_public_budget
SELECT
	a.DACCT,
	a.GUOKU_ID,
	a.AREA_CODE,
	a.AREA_DSCR,
	a.PERIOD_FLAG,
	'一般公共预算收入金额同比'   		     as INDEX_NAME,	
	(a.INDEX_VALUE-c.INDEX_VALUE)/c.INDEX_VALUE  AS INDEX_VALUE
 FROM visual_screen.vs_public_budget a
 LEFT JOIN visual_screen.vs_public_budget c 
         ON c.DACCT = DATE_FORMAT(DATE_SUB(V_DATA_DATE,INTERVAL 1 YEAR),'%Y')
        AND a.AREA_CODE = c.AREA_CODE
        AND a.GUOKU_ID  = c.GUOKU_ID
        AND a.INDEX_NAME = c.INDEX_NAME
        AND a.PERIOD_FLAG = c.PERIOD_FLAG
WHERE a.DACCT=DATE_FORMAT(V_DATA_DATE,'%Y')
        AND a.INDEX_NAME = '一般公共预算收入金额'
        AND a.PERIOD_FLAG = '4';
        
        
	
SET V_STEP_ID=4;
UPDATE visual_screen.vs_public_budget SET AREA_DSCR = '李渡新区' WHERE AREA_CODE= '500997';
UPDATE visual_screen.vs_public_budget SET AREA_DSCR = '彭水县' WHERE AREA_CODE= '500243';
UPDATE visual_screen.vs_public_budget SET AREA_DSCR = '酉阳县' WHERE AREA_CODE= '500242';
UPDATE visual_screen.vs_public_budget SET AREA_DSCR = '秀山县' WHERE AREA_CODE= '500241';
UPDATE visual_screen.vs_public_budget SET AREA_DSCR = '石柱县' WHERE AREA_CODE= '500240';
UPDATE visual_screen.vs_public_budget SET AREA_DSCR = '万盛区' WHERE AREA_CODE= '500131';
SET V_STEP_ID=5;
CALL ETL.EDW_PROC_TRACE_LOG(DATE_FORMAT(V_START_TIME,'%Y%m%d'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
	END
$$

-- PROCEDURE `visual_screen`.`p_vs_purpose_pay`
DROP PROCEDURE IF EXISTS `visual_screen`.`p_vs_purpose_pay`;
DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `p_vs_purpose_pay`(IN v_data_date VARCHAR(10))
BEGIN
DECLARE V_STEP_ID       INT           DEFAULT 0; 
DECLARE V_PROC_NAME     VARCHAR(80)   DEFAULT 'visual_screen.p_vs_purpose_pay.PRC';
DECLARE V_START_TIME    CHAR(19)      DEFAULT NOW();
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        GET DIAGNOSTICS CONDITION 1 @V_RETURN_CODE = RETURNED_SQLSTATE ,@V_ERROR_MSG = MESSAGE_TEXT;
        CALL ETL.EDW_PROC_ERROR_LOG(DATE_FORMAT(V_START_TIME,'%Y-%m'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,@V_RETURN_CODE,@V_ERROR_MSG);
        CALL ETL.EDW_PROC_TRACE_LOG(DATE_FORMAT(V_START_TIME,'%Y-%m'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
    END;

SET V_STEP_ID = 1;
DELETE FROM visual_screen.vs_purpose_pay WHERE DACCT = DATE_FORMAT(v_data_date,'%Y-%m');
SET V_STEP_ID = 2;
INSERT INTO visual_screen.vs_purpose_pay
SELECT 
  DATE_FORMAT(v_data_date,'%Y-%m') 	AS DATA_DATE,
  a1.PID,
  a1.AREA_NO_ID,
  a1.AREA_DSCR,
  '2'					AS PERIOD_FLAG,
  '教育支出' 			AS INDEX_NAME,
  ROUND(SUM(a0.THIS_AMT), 4) 		AS INDEX_VALUE
FROM
  stg.trs_tmis_budget_payout a0 
  LEFT JOIN 
    (SELECT DISTINCT 
      p.GUOKU_ID AS PID,
      p.GUOKU_DSCR,
      p.AREA_NO_ID,
      p.AREA_DSCR,
      c.GUOKU_ID AS CID 
    FROM
      edw.cm_guoku_dimnsn p
      JOIN edw.cm_guoku_dimnsn c
        ON p.GUOKU_LVL_ID_2 = c.GUOKU_LVL_ID_1 
        OR p.GUOKU_LVL_ID_3 = c.GUOKU_LVL_ID_2 
        OR p.GUOKU_ID = c.GUOKU_LVL_ID_3) a1 
    ON a0.TRECODE = a1.CID
WHERE A0.D_ACCT LIKE CONCAT(DATE_FORMAT(v_data_date,'%Y-%m'),'%')
AND A0.`TRECODE` NOT IN('2231000000','2232000000','2233000000','2230000000')
AND A0.SUBJECT_CODE = '205'
GROUP BY 
  a1.AREA_NO_ID
  
  
  union all
  SELECT 
  DATE_FORMAT(v_data_date,'%Y-%m') 	AS DATA_DATE,
  a1.PID,
  a1.AREA_NO_ID,
  a1.AREA_DSCR,
  '2'					AS PERIOD_FLAG,
  '社会保障和就业支出' 			AS INDEX_NAME,
  ROUND(SUM(a0.THIS_AMT), 4) 		AS INDEX_VALUE
FROM
  stg.trs_tmis_budget_payout a0 
  LEFT JOIN 
    (SELECT DISTINCT 
      p.GUOKU_ID AS PID,
      p.GUOKU_DSCR,
      p.AREA_NO_ID,
      p.AREA_DSCR,
      c.GUOKU_ID AS CID 
    FROM
      edw.cm_guoku_dimnsn p
      JOIN edw.cm_guoku_dimnsn c
        ON p.GUOKU_LVL_ID_2 = c.GUOKU_LVL_ID_1 
        OR p.GUOKU_LVL_ID_3 = c.GUOKU_LVL_ID_2 
        OR p.GUOKU_ID = c.GUOKU_LVL_ID_3) a1 
    ON a0.TRECODE = a1.CID
WHERE A0.D_ACCT LIKE CONCAT(DATE_FORMAT(v_data_date,'%Y-%m'),'%')
AND A0.`TRECODE` NOT IN('2231000000','2232000000','2233000000','2230000000')
AND A0.SUBJECT_CODE = '208'
GROUP BY 
  a1.AREA_NO_ID
  
  
  union all
  SELECT 
  DATE_FORMAT(v_data_date,'%Y-%m') 	AS DATA_DATE,
  a1.PID,
  a1.AREA_NO_ID,
  a1.AREA_DSCR,
  '2'					AS PERIOD_FLAG,
  '卫生健康支出' 			AS INDEX_NAME,
  ROUND(SUM(a0.THIS_AMT), 4) 		AS INDEX_VALUE
FROM
  stg.trs_tmis_budget_payout a0 
  LEFT JOIN 
    (SELECT DISTINCT 
      p.GUOKU_ID AS PID,
      p.GUOKU_DSCR,
      p.AREA_NO_ID,
      p.AREA_DSCR,
      c.GUOKU_ID AS CID 
    FROM
      edw.cm_guoku_dimnsn p
      JOIN edw.cm_guoku_dimnsn c
        ON p.GUOKU_LVL_ID_2 = c.GUOKU_LVL_ID_1 
        OR p.GUOKU_LVL_ID_3 = c.GUOKU_LVL_ID_2 
        OR p.GUOKU_ID = c.GUOKU_LVL_ID_3) a1 
    ON a0.TRECODE = a1.CID
WHERE A0.D_ACCT LIKE CONCAT(DATE_FORMAT(v_data_date,'%Y-%m'),'%')
AND A0.`TRECODE` NOT IN('2231000000','2232000000','2233000000','2230000000')
AND A0.SUBJECT_CODE = '210'
GROUP BY 
  a1.AREA_NO_ID
  
  
  union all
  SELECT 
  DATE_FORMAT(v_data_date,'%Y-%m') 	AS DATA_DATE,
  a1.PID,
  a1.AREA_NO_ID,
  a1.AREA_DSCR,
  '2'					AS PERIOD_FLAG,
  '城乡社区支出' 			AS INDEX_NAME,
  ROUND(SUM(a0.THIS_AMT), 4) 		AS INDEX_VALUE
FROM
  stg.trs_tmis_budget_payout a0 
  LEFT JOIN 
    (SELECT DISTINCT 
      p.GUOKU_ID AS PID,
      p.GUOKU_DSCR,
      p.AREA_NO_ID,
      p.AREA_DSCR,
      c.GUOKU_ID AS CID 
    FROM
      edw.cm_guoku_dimnsn p
      JOIN edw.cm_guoku_dimnsn c
        ON p.GUOKU_LVL_ID_2 = c.GUOKU_LVL_ID_1 
        OR p.GUOKU_LVL_ID_3 = c.GUOKU_LVL_ID_2 
        OR p.GUOKU_ID = c.GUOKU_LVL_ID_3) a1 
    ON a0.TRECODE = a1.CID
WHERE A0.D_ACCT LIKE CONCAT(DATE_FORMAT(v_data_date,'%Y-%m'),'%')
AND A0.`TRECODE` NOT IN('2231000000','2232000000','2233000000','2230000000')
AND A0.SUBJECT_CODE = '212'
GROUP BY 
  a1.AREA_NO_ID
  
  
  union all
  SELECT 
  DATE_FORMAT(v_data_date,'%Y-%m') 	AS DATA_DATE,
  a1.PID,
  a1.AREA_NO_ID,
  a1.AREA_DSCR,
  '2'					AS PERIOD_FLAG,
  '农林水支出' 			AS INDEX_NAME,
  ROUND(SUM(a0.THIS_AMT), 4) 		AS INDEX_VALUE
FROM
  stg.trs_tmis_budget_payout a0 
  LEFT JOIN 
    (SELECT DISTINCT 
      p.GUOKU_ID AS PID,
      p.GUOKU_DSCR,
      p.AREA_NO_ID,
      p.AREA_DSCR,
      c.GUOKU_ID AS CID 
    FROM
      edw.cm_guoku_dimnsn p
      JOIN edw.cm_guoku_dimnsn c
        ON p.GUOKU_LVL_ID_2 = c.GUOKU_LVL_ID_1 
        OR p.GUOKU_LVL_ID_3 = c.GUOKU_LVL_ID_2 
        OR p.GUOKU_ID = c.GUOKU_LVL_ID_3) a1 
    ON a0.TRECODE = a1.CID
WHERE A0.D_ACCT LIKE CONCAT(DATE_FORMAT(v_data_date,'%Y-%m'),'%')
AND A0.`TRECODE` NOT IN('2231000000','2232000000','2233000000','2230000000')
AND A0.SUBJECT_CODE = '213'
GROUP BY 
  a1.AREA_NO_ID
  
  
  union all
  SELECT 
  DATE_FORMAT(v_data_date,'%Y-%m') 	AS DATA_DATE,
  a1.PID,
  a1.AREA_NO_ID,
  a1.AREA_DSCR,
  '2'					AS PERIOD_FLAG,
  '住房保障支出' 			AS INDEX_NAME,
  ROUND(SUM(a0.THIS_AMT), 4) 		AS INDEX_VALUE
FROM
  stg.trs_tmis_budget_payout a0 
  LEFT JOIN 
    (SELECT DISTINCT 
      p.GUOKU_ID AS PID,
      p.GUOKU_DSCR,
      p.AREA_NO_ID,
      p.AREA_DSCR,
      c.GUOKU_ID AS CID 
    FROM
      edw.cm_guoku_dimnsn p
      JOIN edw.cm_guoku_dimnsn c
        ON p.GUOKU_LVL_ID_2 = c.GUOKU_LVL_ID_1 
        OR p.GUOKU_LVL_ID_3 = c.GUOKU_LVL_ID_2 
        OR p.GUOKU_ID = c.GUOKU_LVL_ID_3) a1 
    ON a0.TRECODE = a1.CID
WHERE A0.D_ACCT LIKE CONCAT(DATE_FORMAT(v_data_date,'%Y-%m'),'%')
AND A0.`TRECODE` NOT IN('2231000000','2232000000','2233000000','2230000000')
AND A0.SUBJECT_CODE = '221'
GROUP BY 
  a1.AREA_NO_ID
  
  union all
  
SELECT 
  DATE_FORMAT(v_data_date,'%Y-%m') 	AS DATA_DATE,
  a1.PID,
  a1.AREA_NO_ID,
  a1.AREA_DSCR,
  '2'					AS PERIOD_FLAG,
  '公共服务支出' 			AS INDEX_NAME,
  ROUND(SUM(a0.THIS_AMT), 4) 		AS INDEX_VALUE
FROM
  stg.trs_tmis_budget_payout a0 
  LEFT JOIN 
    (SELECT DISTINCT 
      p.GUOKU_ID AS PID,
      p.GUOKU_DSCR,
      p.AREA_NO_ID,
      p.AREA_DSCR,
      c.GUOKU_ID AS CID 
    FROM
      edw.cm_guoku_dimnsn p
      JOIN edw.cm_guoku_dimnsn c
        ON p.GUOKU_LVL_ID_2 = c.GUOKU_LVL_ID_1 
        OR p.GUOKU_LVL_ID_3 = c.GUOKU_LVL_ID_2 
        OR p.GUOKU_ID = c.GUOKU_LVL_ID_3) a1 
    ON a0.TRECODE = a1.CID
WHERE A0.D_ACCT LIKE CONCAT(DATE_FORMAT(v_data_date,'%Y-%m'),'%')
AND A0.`TRECODE` NOT IN('2231000000','2232000000','2233000000','2230000000')
AND A0.SUBJECT_CODE = '201'
GROUP BY 
  a1.AREA_NO_ID
  
  UNION ALL
  
SELECT 
  DATE_FORMAT(v_data_date,'%Y-%m') 	AS DATA_DATE,
  a1.PID,
  a1.AREA_NO_ID,
  a1.AREA_DSCR,
  '2'					AS PERIOD_FLAG,
  '公共安全支出' 			AS INDEX_NAME,
  ROUND(SUM(a0.THIS_AMT), 4) 		AS INDEX_VALUE
FROM
  stg.trs_tmis_budget_payout a0 
  LEFT JOIN 
    (SELECT DISTINCT 
      p.GUOKU_ID AS PID,
      p.GUOKU_DSCR,
      p.AREA_NO_ID,
      p.AREA_DSCR,
      c.GUOKU_ID AS CID 
    FROM
      edw.cm_guoku_dimnsn p
      JOIN edw.cm_guoku_dimnsn c
        ON p.GUOKU_LVL_ID_2 = c.GUOKU_LVL_ID_1 
        OR p.GUOKU_LVL_ID_3 = c.GUOKU_LVL_ID_2 
        OR p.GUOKU_ID = c.GUOKU_LVL_ID_3) a1 
    ON a0.TRECODE = a1.CID
WHERE A0.D_ACCT LIKE CONCAT(DATE_FORMAT(v_data_date,'%Y-%m'),'%')
AND A0.`TRECODE` NOT IN('2231000000','2232000000','2233000000','2230000000')
AND A0.SUBJECT_CODE = '204'
GROUP BY 
  a1.AREA_NO_ID
  UNION ALL
  
SELECT 
  DATE_FORMAT(v_data_date,'%Y-%m') 	AS DATA_DATE,
  a1.PID,
  a1.AREA_NO_ID,
  a1.AREA_DSCR,
  '2'					AS PERIOD_FLAG,
  '交通运输支出' 			AS INDEX_NAME,
  ROUND(SUM(a0.THIS_AMT), 4) 		AS INDEX_VALUE
FROM
  stg.trs_tmis_budget_payout a0 
  LEFT JOIN 
    (SELECT DISTINCT 
      p.GUOKU_ID AS PID,
      p.GUOKU_DSCR,
      p.AREA_NO_ID,
      p.AREA_DSCR,
      c.GUOKU_ID AS CID 
    FROM
      edw.cm_guoku_dimnsn p
      JOIN edw.cm_guoku_dimnsn c
        ON p.GUOKU_LVL_ID_2 = c.GUOKU_LVL_ID_1 
        OR p.GUOKU_LVL_ID_3 = c.GUOKU_LVL_ID_2 
        OR p.GUOKU_ID = c.GUOKU_LVL_ID_3) a1 
    ON a0.TRECODE = a1.CID
WHERE A0.D_ACCT LIKE CONCAT(DATE_FORMAT(v_data_date,'%Y-%m'),'%')
AND A0.`TRECODE` NOT IN('2231000000','2232000000','2233000000','2230000000')
AND A0.SUBJECT_CODE = '214'
GROUP BY 
  a1.AREA_NO_ID
  UNION ALL
  
SELECT 
  DATE_FORMAT(v_data_date,'%Y-%m') 	AS DATA_DATE,
  a1.PID,
  a1.AREA_NO_ID,
  a1.AREA_DSCR,
  '2'					AS PERIOD_FLAG,
  '节能环保支出' 			AS INDEX_NAME,
  ROUND(SUM(a0.THIS_AMT), 4) 		AS INDEX_VALUE
FROM
  stg.trs_tmis_budget_payout a0 
  LEFT JOIN 
    (SELECT DISTINCT 
      p.GUOKU_ID AS PID,
      p.GUOKU_DSCR,
      p.AREA_NO_ID,
      p.AREA_DSCR,
      c.GUOKU_ID AS CID 
    FROM
      edw.cm_guoku_dimnsn p
      JOIN edw.cm_guoku_dimnsn c
        ON p.GUOKU_LVL_ID_2 = c.GUOKU_LVL_ID_1 
        OR p.GUOKU_LVL_ID_3 = c.GUOKU_LVL_ID_2 
        OR p.GUOKU_ID = c.GUOKU_LVL_ID_3) a1 
    ON a0.TRECODE = a1.CID
WHERE A0.D_ACCT LIKE CONCAT(DATE_FORMAT(v_data_date,'%Y-%m'),'%')
AND A0.`TRECODE` NOT IN('2231000000','2232000000','2233000000','2230000000')
AND A0.SUBJECT_CODE = '211'
GROUP BY 
  a1.AREA_NO_ID
  UNION ALL
  
SELECT 
  DATE_FORMAT(V_DATA_DATE,'%Y-%m') 	AS DATA_DATE,
  a1.PID,
  a1.AREA_NO_ID,
  a1.AREA_DSCR,
  '2'					AS PERIOD_FLAG,
  '其他支出' 			AS INDEX_NAME,
  ROUND(SUM(a0.THIS_AMT), 4) 		AS INDEX_VALUE
FROM
  stg.trs_tmis_budget_payout a0 
  LEFT JOIN 
    (SELECT DISTINCT 
      p.GUOKU_ID AS PID,
      p.GUOKU_DSCR,
      p.AREA_NO_ID,
      p.AREA_DSCR,
      c.GUOKU_ID AS CID 
    FROM
      edw.cm_guoku_dimnsn p
      JOIN edw.cm_guoku_dimnsn c
        ON p.GUOKU_LVL_ID_2 = c.GUOKU_LVL_ID_1 
        OR p.GUOKU_LVL_ID_3 = c.GUOKU_LVL_ID_2 
        OR p.GUOKU_ID = c.GUOKU_LVL_ID_3) a1 
    ON a0.TRECODE = a1.CID
WHERE A0.D_ACCT LIKE CONCAT(DATE_FORMAT(V_DATA_DATE,'%Y-%m'),'%')
AND A0.`TRECODE` NOT IN('2231000000','2232000000','2233000000','2230000000')
AND A0.SUBJECT_CODE IN ('202','203','206','207','209','215','216','217','219','220','222','223','224','227','229')
GROUP BY 
  a1.AREA_NO_ID;
  
    END
$$

-- PROCEDURE `visual_screen`.`P_VS_REGIONAL_TAXATION`
DROP PROCEDURE IF EXISTS `visual_screen`.`P_VS_REGIONAL_TAXATION`;
DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `P_VS_REGIONAL_TAXATION`(IN V_DATA_DATE VARCHAR(10))
BEGIN
DECLARE V_STEP_ID       INT           DEFAULT 0; 
DECLARE V_PROC_NAME     VARCHAR(80)   DEFAULT 'visual_screen.P_VS_REGIONAL_TAXATION.PRC';
DECLARE V_START_TIME    CHAR(19)      DEFAULT NOW();
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        GET DIAGNOSTICS CONDITION 1 @V_RETURN_CODE = RETURNED_SQLSTATE ,@V_ERROR_MSG = MESSAGE_TEXT;
        CALL ETL.EDW_PROC_ERROR_LOG(DATE_FORMAT(V_START_TIME,'%Y-%m'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,@V_RETURN_CODE,@V_ERROR_MSG);
        CALL ETL.EDW_PROC_TRACE_LOG(DATE_FORMAT(V_START_TIME,'%Y-%m'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
    END;
	 CALL ETL.EDW_PROC_TRACE_LOG(DATE_FORMAT(V_START_TIME,'%Y%m%d'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
 
SET V_STEP_ID = 1;     
DELETE FROM visual_screen.vs_regional_taxation WHERE DACCT = DATE_FORMAT(V_DATA_DATE,'%Y-%m');
SET V_STEP_ID = 2; 
INSERT INTO visual_screen.vs_regional_taxation
      SELECT
      DISTINCT
  DATE_FORMAT(V_DATA_DATE,'%Y-%m'),
  CASE WHEN a.TRECODE = '3501000000' THEN '0600000000'
       WHEN a.TRECODE = '3302000000' THEN '1500000000' 
       WHEN a.TRECODE = '3801000000' THEN '1900000000'
       WHEN a.TRECODE = '3600000000' THEN '1100000000'
       WHEN a.TRECODE = '4000000000' THEN '1300000000'
			 WHEN a.TRECODE = '3100000000' THEN '3000000000'
       ELSE a.TRECODE END AS TRECODE,
  CASE WHEN a.TRECODE = '3501000000' THEN '0600000000'
       WHEN a.TRECODE = '3302000000' THEN '1500000000' 
       WHEN a.TRECODE = '3801000000' THEN '1900000000'
       WHEN a.TRECODE = '3600000000' THEN '1100000000'
       WHEN a.TRECODE = '4000000000' THEN '1300000000'
			 WHEN a.TRECODE = '3100000000' THEN '3000000000'
       ELSE a.TRECODE END AS TRECODE,
  CASE WHEN a.TREDSCR = '大连市' THEN '辽宁'
     WHEN a.TREDSCR = '青岛市' THEN '山东省' 
     WHEN a.TREDSCR = '深圳市' THEN '广东省'
     WHEN a.TREDSCR = '宁波市' THEN '浙江省'
     WHEN a.TREDSCR = '厦门市' THEN '福建省'
     WHEN a.TREDSCR = '新疆区' THEN '新疆'
		 WHEN a.TREDSCR = '新疆兵团' THEN '新疆'
     WHEN a.TREDSCR = '西藏自治区' THEN '西藏'
     WHEN a.TREDSCR = '广西壮族自治区' THEN '广西'
     WHEN a.TREDSCR = '宁夏回族自治区' THEN '宁夏'
     ELSE a.TREDSCR
     END  AS TREDSCR,
  '2',
  a.SUBJECT_DSCR,
  ROUND(SUM(a.THIS_AMT)*100000000,4) AS INDEX_VALUE,
  ROUND(SUM(a1.THIS_AMT)*100000000,4) AS INDEX_VALUE1
FROM stg.trs_tmis_budget_income_provinces a
LEFT JOIN stg.trs_tmis_budget_income_provinces a1
ON a1.DATA_DATE = DATE_FORMAT(DATE_SUB(V_DATA_DATE, INTERVAL 1 YEAR),'%Y%m')
AND a.TRECODE = a1.TRECODE
AND a.SUBJECT_CODE = a1.SUBJECT_CODE
WHERE a.DATA_DATE=DATE_FORMAT(V_DATA_DATE,'%Y%m')
  AND a.SUBJECT_CODE = '101'
  GROUP BY 
  a.DATA_DATE,
    CASE WHEN a.TRECODE = '3501000000' THEN '0600000000'
       WHEN a.TRECODE = '3302000000' THEN '1500000000' 
       WHEN a.TRECODE = '3801000000' THEN '1900000000'
       WHEN a.TRECODE = '3600000000' THEN '1100000000'
       WHEN a.TRECODE = '4000000000' THEN '1300000000'
			 WHEN a.TRECODE = '3100000000' THEN '3000000000'
       ELSE a.TRECODE
       END
;
  
SET V_STEP_ID=4;
 UPDATE visual_screen.vs_regional_taxation SET AREA_DSCR =REPLACE(AREA_DSCR, '市','');
SET V_STEP_ID=5;
 UPDATE visual_screen.vs_regional_taxation SET AREA_DSCR =REPLACE(AREA_DSCR, '省','');
 SET V_STEP_ID=6;
 CALL ETL.EDW_PROC_TRACE_LOG(DATE_FORMAT(V_START_TIME,'%Y%m%d'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
	END
$$

-- PROCEDURE `visual_screen`.`p_vs_region_pay`
DROP PROCEDURE IF EXISTS `visual_screen`.`p_vs_region_pay`;
DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `p_vs_region_pay`(IN v_data_date VARCHAR(10))
BEGIN
DECLARE V_STEP_ID       INT           DEFAULT 0; 
DECLARE V_PROC_NAME     VARCHAR(80)   DEFAULT 'visual_screen.p_vs_region_pay.PRC';
DECLARE V_START_TIME    CHAR(19)      DEFAULT NOW();
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        GET DIAGNOSTICS CONDITION 1 @V_RETURN_CODE = RETURNED_SQLSTATE ,@V_ERROR_MSG = MESSAGE_TEXT;
        CALL ETL.EDW_PROC_ERROR_LOG(DATE_FORMAT(V_START_TIME,'%Y-%m'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,@V_RETURN_CODE,@V_ERROR_MSG);
        CALL ETL.EDW_PROC_TRACE_LOG(DATE_FORMAT(V_START_TIME,'%Y-%m'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
    END;
SET V_STEP_ID = 1;
DELETE FROM visual_screen.vs_region_pay WHERE DACCT = DATE_FORMAT(v_data_date,'%Y-%m');
SET V_STEP_ID = 2;
INSERT INTO visual_screen.vs_region_pay
SELECT 
  DATE_FORMAT(v_data_date,'%Y-%m') 	AS DATA_DATE,
  a1.PID,
  a1.AREA_NO_ID,
  a1.AREA_DSCR,
  '2'					AS PERIOD_FLAG,
  '支出合计'  				AS INDEX_NAME,
  ROUND(SUM(a0.INDEX_VALUE), 4) as INDEX_VALUE,
  ROUND(SUM(a2.GROWTH_INDEX_VALUE), 4) as GROWTH_INDEX_VALUE
FROM
  (select D_ACCT, TRECODE, LEVEL, SUBJECT_CODE, sum(THIS_AMT) INDEX_VALUE from stg.trs_tmis_budget_payout group by D_ACCT, TRECODE, LEVEL, SUBJECT_CODE) a0 
left join (select D_ACCT, TRECODE, LEVEL, SUBJECT_CODE, sum(THIS_AMT) GROWTH_INDEX_VALUE from stg.trs_tmis_budget_payout group by D_ACCT, TRECODE, LEVEL, SUBJECT_CODE) a2
      on a2.D_ACCT = date_sub(A0.D_ACCT,interval 1 year)
      and a0.SUBJECT_CODE = a2.SUBJECT_CODE
      AND a0.LEVEL = a2.LEVEL
      and a0.TRECODE = a2.TRECODE
  LEFT JOIN 
    (SELECT DISTINCT * FROM (
				SELECT
					t2.GUOKU_ID AS PID,
					t2.GUOKU_DSCR,
					t2.AREA_NO_ID,
					t2.AREA_DSCR,
					t1.GUOKU_ID AS CID
				FROM edw.cm_guoku_dimnsn t1
					LEFT JOIN edw.cm_guoku_dimnsn t2 ON t1.GUOKU_LVL_ID_2 = t2.GUOKU_LVL_ID_3 
				UNION ALL
				SELECT
					t3.GUOKU_ID AS PID,
					t3.GUOKU_DSCR,
					t3.AREA_NO_ID,
					t3.AREA_DSCR,
					t2.GUOKU_ID AS CID
				FROM edw.cm_guoku_dimnsn t1
					LEFT JOIN edw.cm_guoku_dimnsn t2 ON t1.GUOKU_LVL_ID_2 = t2.GUOKU_LVL_ID_3
					LEFT JOIN edw.cm_guoku_dimnsn t3 ON t1.GUOKU_LVL_ID_1 = t3.GUOKU_LVL_ID_2 
				) t ) a1 
    ON a0.TRECODE = a1.CID
WHERE A0.D_ACCT LIKE CONCAT(DATE_FORMAT(v_data_date,'%Y-%m'),'%')
AND a0.SUBJECT_CODE IN ('T020101','T020201','T020601')
AND A0.`TRECODE` NOT IN('2231000000','2232000000','2233000000','2230000000')
GROUP BY a1.AREA_NO_ID;
SET V_STEP_ID=4;
UPDATE visual_screen.vs_region_pay SET AREA_DSCR = '李渡新区' WHERE AREA_CODE= '500997';
UPDATE visual_screen.vs_region_pay SET AREA_DSCR = '彭水县' WHERE AREA_CODE= '500243';
UPDATE visual_screen.vs_region_pay SET AREA_DSCR = '酉阳县' WHERE AREA_CODE= '500242';
UPDATE visual_screen.vs_region_pay SET AREA_DSCR = '秀山县' WHERE AREA_CODE= '500241';
UPDATE visual_screen.vs_region_pay SET AREA_DSCR = '石柱县' WHERE AREA_CODE= '500240';
UPDATE visual_screen.vs_region_pay SET AREA_DSCR = '万盛区' WHERE AREA_CODE= '500131';
SET V_STEP_ID=5;
	END
$$

-- PROCEDURE `visual_screen`.`p_vs_region_pay_20241014`
DROP PROCEDURE IF EXISTS `visual_screen`.`p_vs_region_pay_20241014`;
DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `p_vs_region_pay_20241014`(IN v_data_date VARCHAR(10))
BEGIN
DECLARE V_STEP_ID       INT           DEFAULT 0; 
DECLARE V_PROC_NAME     VARCHAR(80)   DEFAULT 'visual_screen.p_vs_region_pay.PRC';
DECLARE V_START_TIME    CHAR(19)      DEFAULT NOW();
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        GET DIAGNOSTICS CONDITION 1 @V_RETURN_CODE = RETURNED_SQLSTATE ,@V_ERROR_MSG = MESSAGE_TEXT;
        CALL ETL.EDW_PROC_ERROR_LOG(DATE_FORMAT(V_START_TIME,'%Y-%m'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,@V_RETURN_CODE,@V_ERROR_MSG);
        CALL ETL.EDW_PROC_TRACE_LOG(DATE_FORMAT(V_START_TIME,'%Y-%m'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
    END;
SET V_STEP_ID = 1;
DELETE FROM visual_screen.vs_region_pay WHERE DACCT = DATE_FORMAT(v_data_date,'%Y-%m');
SET V_STEP_ID = 2;
INSERT INTO visual_screen.vs_region_pay
SELECT 
  DATE_FORMAT(v_data_date,'%Y-%m') 	AS DATA_DATE,
  a1.PID,
  a1.AREA_NO_ID,
  a1.AREA_DSCR,
  '2'					AS PERIOD_FLAG,
  '支出合计'  				AS INDEX_NAME,
  ROUND(SUM(a0.THIS_AMT), 4) 		AS INDEX_VALUE,
  ROUND(SUM(a2.THIS_AMT), 4)		AS GROWTH_INDEX_VALUE
FROM
  stg.trs_tmis_budget_payout a0 
left join stg.trs_tmis_budget_payout a2
      on a2.D_ACCT = date_sub(A0.D_ACCT,interval 1 year)
      and a0.SUBJECT_CODE = a2.SUBJECT_CODE
      AND a0.LEVEL = a2.LEVEL
      and a0.TRECODE = a2.TRECODE
  LEFT JOIN 
    (SELECT DISTINCT 
      p.GUOKU_ID AS PID,
      p.GUOKU_DSCR,
      p.AREA_NO_ID,
      p.AREA_DSCR,
      c.GUOKU_ID AS CID 
    FROM
      edw.cm_guoku_dimnsn p
      JOIN edw.cm_guoku_dimnsn c
        ON p.GUOKU_LVL_ID_2 = c.GUOKU_LVL_ID_1 
        OR p.GUOKU_LVL_ID_3 = c.GUOKU_LVL_ID_2 
        OR p.GUOKU_ID = c.GUOKU_LVL_ID_3) a1 
    ON a0.TRECODE = a1.CID
WHERE A0.D_ACCT LIKE CONCAT(DATE_FORMAT(v_data_date,'%Y-%m'),'%')
AND a0.SUBJECT_CODE IN ('T020101','T020201','T020601')
AND A0.`TRECODE` NOT IN('2231000000','2232000000','2233000000','2230000000')
GROUP BY a1.AREA_NO_ID;
SET V_STEP_ID=4;
UPDATE visual_screen.vs_region_pay SET AREA_DSCR = '李渡新区' WHERE AREA_CODE= '500997';
UPDATE visual_screen.vs_region_pay SET AREA_DSCR = '彭水县' WHERE AREA_CODE= '500243';
UPDATE visual_screen.vs_region_pay SET AREA_DSCR = '酉阳县' WHERE AREA_CODE= '500242';
UPDATE visual_screen.vs_region_pay SET AREA_DSCR = '秀山县' WHERE AREA_CODE= '500241';
UPDATE visual_screen.vs_region_pay SET AREA_DSCR = '石柱县' WHERE AREA_CODE= '500240';
UPDATE visual_screen.vs_region_pay SET AREA_DSCR = '万盛区' WHERE AREA_CODE= '500131';
SET V_STEP_ID=5;
	END
$$

-- PROCEDURE `visual_screen`.`P_VS_REVENUE_EXPENDITURE`
DROP PROCEDURE IF EXISTS `visual_screen`.`P_VS_REVENUE_EXPENDITURE`;
DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `P_VS_REVENUE_EXPENDITURE`(IN V_DATA_DATE VARCHAR(10))
BEGIN

DECLARE V_STEP_ID       INT           DEFAULT 0; 
DECLARE V_PROC_NAME     VARCHAR(80)   DEFAULT 'visual_screen.P_VS_REVENUE_EXPENDITURE.PRC';
DECLARE V_START_TIME    CHAR(19)      DEFAULT NOW();
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        GET DIAGNOSTICS CONDITION 1 @V_RETURN_CODE = RETURNED_SQLSTATE ,@V_ERROR_MSG = MESSAGE_TEXT;
        CALL ETL.EDW_PROC_ERROR_LOG(DATE_FORMAT(V_START_TIME,'%Y-%m'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,@V_RETURN_CODE,@V_ERROR_MSG);
        CALL ETL.EDW_PROC_TRACE_LOG(DATE_FORMAT(V_START_TIME,'%Y-%m'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
    END;
	 CALL ETL.EDW_PROC_TRACE_LOG(DATE_FORMAT(V_START_TIME,'%Y%m%d'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
 
SET V_STEP_ID = 1;     
DELETE FROM visual_screen.vs_revenue_expenditure WHERE DACCT = DATE_FORMAT(V_DATA_DATE,'%Y-%m');
SET V_STEP_ID = 2; 
INSERT INTO visual_screen.vs_revenue_expenditure
	SELECT 
	 DISTINCT
	 lib.ACCOUNT_PERIOD, 
	d.guoku_id,
	d.area_no_id,
	d.area_dscr,
	lib.PERIOD_FLAG, 
	'收支缺口' AS   INDEX_NAME  ,  
	(lib.INDEX_VALUE - b.INDEX_VALUE) AS INDEX_VALUE  
	FROM indicators_lib.lib_indicators_000008 lib
	 LEFT JOIN indicators_lib.lib_indicators_000034 b
	 ON lib.ACCOUNT_PERIOD = DATE_FORMAT(V_DATA_DATE,'%Y-%m')
	 AND lib.INDEX_DIM_CODE = b.INDEX_DIM_CODE
	 AND lib.INDEX_DIM_DESCR = b.INDEX_DIM_DESCR
	 AND lib.DIMENSION_FLAG=b.DIMENSION_FLAG
	 AND lib.PERIOD_FLAG = b.PERIOD_FLAG
	 LEFT JOIN  dmcode.cm_guoku_dimnsn d
	ON lib.INDEX_DIM_CODE = d.area_no_id	 
	 WHERE  lib.DIMENSION_FLAG='2'
	 AND  lib.PERIOD_FLAG='2' 
	 AND b.ACCOUNT_PERIOD = DATE_FORMAT(V_DATA_DATE,'%Y-%m');
	 
SET V_STEP_ID = 3;     
DELETE FROM visual_screen.vs_revenue_expenditure WHERE DACCT = year(V_DATA_DATE);
SET V_STEP_ID = 4; 
INSERT INTO visual_screen.vs_revenue_expenditure
 SELECT 
	DISTINCT
	LEFT(lib.ACCOUNT_PERIOD,4), 
	d.guoku_id,
	d.area_no_id,
	d.area_dscr,
	'4' AS PERIOD_FLAG, 
	'收支缺口' AS   INDEX_NAME  ,  
	(SUM(lib.INDEX_VALUE) - SUM(b.INDEX_VALUE)) AS INDEX_VALUE  
	FROM indicators_lib.lib_indicators_000008 lib
	 LEFT JOIN indicators_lib.lib_indicators_000034 b
	 ON LEFT(lib.ACCOUNT_PERIOD,4) = DATE_FORMAT(V_DATA_DATE,'%Y')
	 AND lib.INDEX_DIM_CODE = b.INDEX_DIM_CODE
	 AND lib.INDEX_DIM_DESCR = b.INDEX_DIM_DESCR
	 LEFT JOIN  dmcode.cm_guoku_dimnsn d
	ON lib.INDEX_DIM_CODE = d.area_no_id	 
	 WHERE  lib.DIMENSION_FLAG='2'
	 AND  lib.PERIOD_FLAG='2' 
	and  LEFT(b.ACCOUNT_PERIOD,4) = DATE_FORMAT(V_DATA_DATE,'%Y')
	 GROUP BY 
	lib.INDEX_ID,
	lib.INDEX_DIM_CODE	 
	 ;
SET V_STEP_ID=4;
UPDATE visual_screen.vs_revenue_expenditure SET AREA_DSCR = '李渡新区' WHERE AREA_CODE= '500997';
UPDATE visual_screen.vs_revenue_expenditure SET AREA_DSCR = '彭水县' WHERE AREA_CODE= '500243';
UPDATE visual_screen.vs_revenue_expenditure SET AREA_DSCR = '酉阳县' WHERE AREA_CODE= '500242';
UPDATE visual_screen.vs_revenue_expenditure SET AREA_DSCR = '秀山县' WHERE AREA_CODE= '500241';
UPDATE visual_screen.vs_revenue_expenditure SET AREA_DSCR = '石柱县' WHERE AREA_CODE= '500240';
UPDATE visual_screen.vs_revenue_expenditure SET AREA_DSCR = '万盛区' WHERE AREA_CODE= '500131';
SET V_STEP_ID=5;
CALL ETL.EDW_PROC_TRACE_LOG(DATE_FORMAT(V_START_TIME,'%Y%m%d'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
	END
$$

-- PROCEDURE `visual_screen`.`P_VS_REVENU_DISPLAY`
DROP PROCEDURE IF EXISTS `visual_screen`.`P_VS_REVENU_DISPLAY`;
DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `P_VS_REVENU_DISPLAY`(IN V_DATA_DATE VARCHAR(10))
BEGIN

DECLARE V_STEP_ID       INT           DEFAULT 0; 
DECLARE V_PROC_NAME     VARCHAR(80)   DEFAULT 'visual_screen.P_VS_REVENU_DISPLAY.PRC';
DECLARE V_START_TIME    CHAR(19)      DEFAULT NOW();
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        GET DIAGNOSTICS CONDITION 1 @V_RETURN_CODE = RETURNED_SQLSTATE ,@V_ERROR_MSG = MESSAGE_TEXT;
        CALL ETL.EDW_PROC_ERROR_LOG(DATE_FORMAT(V_START_TIME,'%Y-%m'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,@V_RETURN_CODE,@V_ERROR_MSG);
        CALL ETL.EDW_PROC_TRACE_LOG(DATE_FORMAT(V_START_TIME,'%Y-%m'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
    END;
	 CALL ETL.EDW_PROC_TRACE_LOG(DATE_FORMAT(V_START_TIME,'%Y%m%d'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
 
SET V_STEP_ID = 1;     
DELETE FROM visual_screen.vs_revenu_display WHERE DACCT = DATE_FORMAT(V_DATA_DATE,'%Y-%m');
SET V_STEP_ID = 2; 
INSERT INTO visual_screen.vs_revenu_display
SELECT
  DATE_FORMAT(V_DATA_DATE,'%Y-%m'),
  CASE WHEN a.TRECODE = '3501000000' THEN '0600000000'
       WHEN a.TRECODE = '3302000000' THEN '1500000000' 
       WHEN a.TRECODE = '3801000000' THEN '1900000000'
       WHEN a.TRECODE = '3600000000' THEN '1100000000'
       WHEN a.TRECODE = '4000000000' THEN '1300000000'
			 WHEN a.TRECODE = '3100000000'  THEN '3000000000'
       ELSE a.TRECODE END AS TRECODE,
  CASE WHEN a.TRECODE = '3501000000' THEN '0600000000'
       WHEN a.TRECODE = '3302000000' THEN '1500000000' 
       WHEN a.TRECODE = '3801000000' THEN '1900000000'
       WHEN a.TRECODE = '3600000000' THEN '1100000000'
       WHEN a.TRECODE = '4000000000' THEN '1300000000'
			 WHEN a.TRECODE = '3100000000'  THEN '3000000000'
       ELSE a.TRECODE END AS TRECODE,
  CASE WHEN a.TREDSCR = '大连市' THEN '辽宁'
     WHEN a.TREDSCR = '青岛市' THEN '山东省' 
     WHEN a.TREDSCR = '深圳市' THEN '广东省'
     WHEN a.TREDSCR = '宁波市' THEN '浙江省'
     WHEN a.TREDSCR = '厦门市' THEN '福建省'
      WHEN (a.TREDSCR = '新疆兵团' or a.TREDSCR = '新疆区') THEN '新疆'
     WHEN a.TREDSCR = '西藏自治区' THEN '西藏'
     WHEN a.TREDSCR = '广西壮族自治区' THEN '广西'
     WHEN a.TREDSCR = '宁夏回族自治区' THEN '宁夏'
     ELSE a.TREDSCR
     END  AS TREDSCR,
  '2',
   a.SUBJECT_DSCR,
  SUM(a.THIS_AMT)*100000000  AS INDEX_VALUE
FROM stg.trs_tmis_budget_income_provinces a 
WHERE a.DATA_DATE=DATE_FORMAT(V_DATA_DATE,'%Y%m')
  AND a.SUBJECT_CODE = 'T010101'
  GROUP BY 
  a.DATA_DATE,
  CASE WHEN a.TRECODE = '3501000000' THEN '0600000000'
       WHEN a.TRECODE = '3302000000' THEN '1500000000' 
       WHEN a.TRECODE = '3801000000' THEN '1900000000'
       WHEN a.TRECODE = '3600000000' THEN '1100000000'
       WHEN a.TRECODE = '4000000000' THEN '1300000000'
			 WHEN a.TRECODE = '3100000000'  THEN '3000000000'
       ELSE a.TRECODE
       END
UNION ALL
SELECT
  DATE_FORMAT(V_DATA_DATE,'%Y-%m'),
  CASE WHEN a.TRECODE = '3501000000' THEN '0600000000'
       WHEN a.TRECODE = '3302000000' THEN '1500000000' 
       WHEN a.TRECODE = '3801000000' THEN '1900000000'
       WHEN a.TRECODE = '3600000000' THEN '1100000000'
       WHEN a.TRECODE = '4000000000' THEN '1300000000'
			 WHEN a.TRECODE = '3100000000'  THEN '3000000000'
       ELSE a.TRECODE END AS TRECODE,
  CASE WHEN a.TRECODE = '3501000000' THEN '0600000000'
       WHEN a.TRECODE = '3302000000' THEN '1500000000' 
       WHEN a.TRECODE = '3801000000' THEN '1900000000'
       WHEN a.TRECODE = '3600000000' THEN '1100000000'
       WHEN a.TRECODE = '4000000000' THEN '1300000000'
			 WHEN a.TRECODE = '3100000000'  THEN '3000000000'
       ELSE a.TRECODE END AS TRECODE,
  CASE WHEN a.TREDSCR = '大连市' THEN '辽宁'
     WHEN a.TREDSCR = '青岛市' THEN '山东省' 
     WHEN a.TREDSCR = '深圳市' THEN '广东省'
     WHEN a.TREDSCR = '宁波市' THEN '浙江省'
     WHEN a.TREDSCR = '厦门市' THEN '福建省'
      WHEN (a.TREDSCR = '新疆兵团' or a.TREDSCR = '新疆区') THEN '新疆'
     WHEN a.TREDSCR = '西藏自治区' THEN '西藏'
     WHEN a.TREDSCR = '广西壮族自治区' THEN '广西'
     WHEN a.TREDSCR = '宁夏回族自治区' THEN '宁夏'
     ELSE a.TREDSCR
     END  AS TREDSCR,
  '2',
   '公共预算收入同比',
  (SUM(a.YEAR_AMT)-SUM(b.YEAR_AMT))/SUM(b.YEAR_AMT)  AS INDEX_VALUE
FROM stg.trs_tmis_budget_income_provinces a 
LEFT JOIN stg.trs_tmis_budget_income_provinces b
       ON b.data_Date = DATE_FORMAT(DATE_SUB(CONCAT(a.data_Date,'01'),INTERVAL 1 YEAR),'%Y%m')
       AND a.TRECODE = b.TRECODE
       AND a.SUBJECT_CODE = b.SUBJECT_CODE
WHERE a.DATA_DATE=DATE_FORMAT(V_DATA_DATE,'%Y%m')
  AND a.SUBJECT_CODE = 'T010101'
  GROUP BY 
  a.DATA_DATE,
  CASE WHEN a.TRECODE = '3501000000' THEN '0600000000'
       WHEN a.TRECODE = '3302000000' THEN '1500000000' 
       WHEN a.TRECODE = '3801000000' THEN '1900000000'
       WHEN a.TRECODE = '3600000000' THEN '1100000000'
       WHEN a.TRECODE = '4000000000' THEN '1300000000'
			 WHEN a.TRECODE = '3100000000'  THEN '3000000000'
       ELSE a.TRECODE
       END
       
UNION ALL       
       
SELECT
  DATE_FORMAT(V_DATA_DATE,'%Y-%m'),
  CASE WHEN a.TRECODE = '3501000000' THEN '0600000000'
       WHEN a.TRECODE = '3302000000' THEN '1500000000' 
       WHEN a.TRECODE = '3801000000' THEN '1900000000'
       WHEN a.TRECODE = '3600000000' THEN '1100000000'
       WHEN a.TRECODE = '4000000000' THEN '1300000000'
			 WHEN a.TRECODE = '3100000000'  THEN '3000000000'
       ELSE a.TRECODE END AS TRECODE,
  CASE WHEN a.TRECODE = '3501000000' THEN '0600000000'
       WHEN a.TRECODE = '3302000000' THEN '1500000000' 
       WHEN a.TRECODE = '3801000000' THEN '1900000000'
       WHEN a.TRECODE = '3600000000' THEN '1100000000'
       WHEN a.TRECODE = '4000000000' THEN '1300000000'
			 WHEN a.TRECODE = '3100000000'  THEN '3000000000'
       ELSE a.TRECODE END AS TRECODE,
  CASE WHEN a.TREDSCR = '大连市' THEN '辽宁'
     WHEN a.TREDSCR = '青岛市' THEN '山东省' 
     WHEN a.TREDSCR = '深圳市' THEN '广东省'
     WHEN a.TREDSCR = '宁波市' THEN '浙江省'
     WHEN a.TREDSCR = '厦门市' THEN '福建省'
      WHEN (a.TREDSCR = '新疆兵团' or a.TREDSCR = '新疆区') THEN '新疆'
     WHEN a.TREDSCR = '西藏自治区' THEN '西藏'
     WHEN a.TREDSCR = '广西壮族自治区' THEN '广西'
     WHEN a.TREDSCR = '宁夏回族自治区' THEN '宁夏'
     ELSE a.TREDSCR
     END  AS TREDSCR,
  '2',
   a.SUBJECT_DSCR,
  SUM(a.THIS_AMT)*100000000  AS INDEX_VALUE
FROM stg.trs_tmis_budget_income_provinces a 
WHERE a.DATA_DATE=DATE_FORMAT(V_DATA_DATE,'%Y%m')
  AND a.SUBJECT_CODE = '101'
  GROUP BY 
  a.DATA_DATE,
  CASE WHEN a.TRECODE = '3501000000' THEN '0600000000'
       WHEN a.TRECODE = '3302000000' THEN '1500000000' 
       WHEN a.TRECODE = '3801000000' THEN '1900000000'
       WHEN a.TRECODE = '3600000000' THEN '1100000000'
       WHEN a.TRECODE = '4000000000' THEN '1300000000'
			 WHEN a.TRECODE = '3100000000'  THEN '3000000000'
       ELSE a.TRECODE
       END
UNION ALL
SELECT
  DATE_FORMAT(V_DATA_DATE,'%Y-%m'),
  CASE WHEN a.TRECODE = '3501000000' THEN '0600000000'
       WHEN a.TRECODE = '3302000000' THEN '1500000000' 
       WHEN a.TRECODE = '3801000000' THEN '1900000000'
       WHEN a.TRECODE = '3600000000' THEN '1100000000'
       WHEN a.TRECODE = '4000000000' THEN '1300000000'
			 WHEN a.TRECODE = '3100000000'  THEN '3000000000'
       ELSE a.TRECODE END AS TRECODE,
  CASE WHEN a.TRECODE = '3501000000' THEN '0600000000'
       WHEN a.TRECODE = '3302000000' THEN '1500000000' 
       WHEN a.TRECODE = '3801000000' THEN '1900000000'
       WHEN a.TRECODE = '3600000000' THEN '1100000000'
       WHEN a.TRECODE = '4000000000' THEN '1300000000'
			 WHEN a.TRECODE = '3100000000'  THEN '3000000000'
       ELSE a.TRECODE END AS TRECODE,
  CASE WHEN a.TREDSCR = '大连市' THEN '辽宁'
     WHEN a.TREDSCR = '青岛市' THEN '山东省' 
     WHEN a.TREDSCR = '深圳市' THEN '广东省'
     WHEN a.TREDSCR = '宁波市' THEN '浙江省'
     WHEN a.TREDSCR = '厦门市' THEN '福建省'
      WHEN (a.TREDSCR = '新疆兵团' or a.TREDSCR = '新疆区') THEN '新疆'
     WHEN a.TREDSCR = '西藏自治区' THEN '西藏'
     WHEN a.TREDSCR = '广西壮族自治区' THEN '广西'
     WHEN a.TREDSCR = '宁夏回族自治区' THEN '宁夏'
     ELSE a.TREDSCR
     END  AS TREDSCR,
  '2',
  '税收收入同比',
  (SUM(a.YEAR_AMT)-SUM(b.YEAR_AMT))/SUM(b.YEAR_AMT)  AS INDEX_VALUE
FROM stg.trs_tmis_budget_income_provinces a 
LEFT JOIN stg.trs_tmis_budget_income_provinces b
       ON b.data_Date = DATE_FORMAT(DATE_SUB(CONCAT(a.data_Date,'01'),INTERVAL 1 YEAR),'%Y%m')
       AND a.TRECODE = b.TRECODE
       AND a.SUBJECT_CODE = b.SUBJECT_CODE
WHERE a.DATA_DATE=DATE_FORMAT(V_DATA_DATE,'%Y%m')
  AND a.SUBJECT_CODE = '101'
  GROUP BY 
  a.DATA_DATE,
  CASE WHEN a.TRECODE = '3501000000' THEN '0600000000'
       WHEN a.TRECODE = '3302000000' THEN '1500000000' 
       WHEN a.TRECODE = '3801000000' THEN '1900000000'
       WHEN a.TRECODE = '3600000000' THEN '1100000000'
       WHEN a.TRECODE = '4000000000' THEN '1300000000'
			 WHEN a.TRECODE = '3100000000'  THEN '3000000000'
       ELSE a.TRECODE
       END;
       
       
SET V_STEP_ID=4;
 UPDATE visual_screen.vs_revenu_display SET AREA_DSCR =REPLACE(AREA_DSCR, '市','');
SET V_STEP_ID=5;
 UPDATE visual_screen.vs_revenu_display SET AREA_DSCR =REPLACE(AREA_DSCR, '省','');
 SET V_STEP_ID=6;
 CALL ETL.EDW_PROC_TRACE_LOG(DATE_FORMAT(V_START_TIME,'%Y%m%d'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
	END
$$

-- PROCEDURE `visual_screen`.`p_vs_subject_pay`
DROP PROCEDURE IF EXISTS `visual_screen`.`p_vs_subject_pay`;
DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `p_vs_subject_pay`(IN v_data_date VARCHAR(10))
BEGIN
DECLARE V_STEP_ID       INT           DEFAULT 0; 
DECLARE V_PROC_NAME     VARCHAR(80)   DEFAULT 'visual_screen.p_vs_subject_pay.PRC';
DECLARE V_START_TIME    CHAR(19)      DEFAULT NOW();
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        GET DIAGNOSTICS CONDITION 1 @V_RETURN_CODE = RETURNED_SQLSTATE ,@V_ERROR_MSG = MESSAGE_TEXT;
        CALL ETL.EDW_PROC_ERROR_LOG(DATE_FORMAT(V_START_TIME,'%Y-%m'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,@V_RETURN_CODE,@V_ERROR_MSG);
        CALL ETL.EDW_PROC_TRACE_LOG(DATE_FORMAT(V_START_TIME,'%Y-%m'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
    END;

SET V_STEP_ID = 1;
delete from visual_screen.vs_subject_pay where DACCT = date_format(v_data_date,'%Y-%m');
set V_STEP_ID = 2;
insert into visual_screen.vs_subject_pay
SELECT 
  DATE_FORMAT(v_data_date,'%Y-%m') 	AS DATA_DATE,
  a1.PID,
  a1.AREA_NO_ID,
  a1.AREA_DSCR,
  '2'					AS PERIOD_FLAG,
  '公共预算支出金额' 			AS INDEX_NAME,
  ROUND(SUM(a0.THIS_AMT), 4) 		AS INDEX_VALUE
FROM
  stg.trs_tmis_budget_payout a0 
  LEFT JOIN 
    (SELECT DISTINCT 
      p.GUOKU_ID AS PID,
      p.GUOKU_DSCR,
      p.AREA_NO_ID,
      p.AREA_DSCR,
      c.GUOKU_ID AS CID 
    FROM
      edw.cm_guoku_dimnsn p
      JOIN edw.cm_guoku_dimnsn c
        ON p.GUOKU_LVL_ID_2 = c.GUOKU_LVL_ID_1 
        OR p.GUOKU_LVL_ID_3 = c.GUOKU_LVL_ID_2 
        OR p.GUOKU_ID = c.GUOKU_LVL_ID_3) a1 
    ON a0.TRECODE = a1.CID
WHERE A0.D_ACCT LIKE CONCAT(DATE_FORMAT(v_data_date,'%Y-%m'),'%')
AND A0.`TRECODE` NOT IN('2231000000','2232000000','2233000000','2230000000')
AND A0.SUBJECT_CODE = 'T020101'
GROUP BY 
  a1.AREA_NO_ID,
  a0.SUBJECT_CODE
  
 UNION ALL
 SELECT 
  DATE_FORMAT(v_data_date,'%Y-%m') 	AS DATA_DATE,
  a1.PID,
  a1.AREA_NO_ID,
  a1.AREA_DSCR,
  '2'					AS PERIOD_FLAG,
  '基金预算支出金额' 			AS INDEX_NAME,
  ROUND(SUM(a0.THIS_AMT), 4) 		AS INDEX_VALUE
FROM
  stg.trs_tmis_budget_payout a0 
  LEFT JOIN 
    (SELECT DISTINCT 
      p.GUOKU_ID AS PID,
      p.GUOKU_DSCR,
      p.AREA_NO_ID,
      p.AREA_DSCR,
      c.GUOKU_ID AS CID 
    FROM
      edw.cm_guoku_dimnsn p
      JOIN edw.cm_guoku_dimnsn c
        ON p.GUOKU_LVL_ID_2 = c.GUOKU_LVL_ID_1 
        OR p.GUOKU_LVL_ID_3 = c.GUOKU_LVL_ID_2 
        OR p.GUOKU_ID = c.GUOKU_LVL_ID_3) a1 
    ON a0.TRECODE = a1.CID
WHERE A0.D_ACCT LIKE CONCAT(DATE_FORMAT(v_data_date,'%Y-%m'),'%')
AND A0.`TRECODE` NOT IN('2231000000','2232000000','2233000000','2230000000')
AND A0.SUBJECT_CODE = 'T020201'
GROUP BY 
  a1.AREA_NO_ID,
  a0.SUBJECT_CODE
  
    
 UNION ALL
 SELECT 
  DATE_FORMAT(v_data_date,'%Y-%m') 	AS DATA_DATE,
  a1.PID,
  a1.AREA_NO_ID,
  a1.AREA_DSCR,
  '2'					AS PERIOD_FLAG,
  '国有资本经营预算支出金额' 		AS INDEX_NAME,
  ROUND(SUM(a0.THIS_AMT), 4) 		AS INDEX_VALUE
FROM
  stg.trs_tmis_budget_payout a0 
  LEFT JOIN 
    (SELECT DISTINCT 
      p.GUOKU_ID AS PID,
      p.GUOKU_DSCR,
      p.AREA_NO_ID,
      p.AREA_DSCR,
      c.GUOKU_ID AS CID 
    FROM
      edw.cm_guoku_dimnsn p
      JOIN edw.cm_guoku_dimnsn c
        ON p.GUOKU_LVL_ID_2 = c.GUOKU_LVL_ID_1 
        OR p.GUOKU_LVL_ID_3 = c.GUOKU_LVL_ID_2 
        OR p.GUOKU_ID = c.GUOKU_LVL_ID_3) a1 
    ON a0.TRECODE = a1.CID
WHERE A0.D_ACCT LIKE CONCAT(DATE_FORMAT(v_data_date,'%Y-%m'),'%')
AND A0.`TRECODE` NOT IN('2231000000','2232000000','2233000000','2230000000')
AND A0.SUBJECT_CODE = 'T020601'
GROUP BY 
  a1.AREA_NO_ID,
  a0.SUBJECT_CODE
      
 UNION ALL
 SELECT 
  DATE_FORMAT(v_data_date,'%Y-%m') 	AS DATA_DATE,
  a1.PID,
  a1.AREA_NO_ID,
  a1.AREA_DSCR,
  '2'					AS PERIOD_FLAG,
  '债务支出金额'     		 	AS INDEX_NAME,
  ROUND(SUM(a0.THIS_AMT), 4) 		AS INDEX_VALUE
FROM
  stg.trs_tmis_budget_payout a0 
  LEFT JOIN 
    (SELECT DISTINCT 
      p.GUOKU_ID AS PID,
      p.GUOKU_DSCR,
      p.AREA_NO_ID,
      p.AREA_DSCR,
      c.GUOKU_ID AS CID 
    FROM
      edw.cm_guoku_dimnsn p
      JOIN edw.cm_guoku_dimnsn c
        ON p.GUOKU_LVL_ID_2 = c.GUOKU_LVL_ID_1 
        OR p.GUOKU_LVL_ID_3 = c.GUOKU_LVL_ID_2 
        OR p.GUOKU_ID = c.GUOKU_LVL_ID_3) a1 
    ON a0.TRECODE = a1.CID
WHERE A0.D_ACCT LIKE CONCAT(DATE_FORMAT(v_data_date,'%Y-%m'),'%')
AND A0.`TRECODE` NOT IN('2231000000','2232000000','2233000000','2230000000')
AND A0.SUBJECT_CODE IN  ('T0203','T0205')
GROUP BY 
  a1.AREA_NO_ID,
  a0.SUBJECT_CODE
  
        
 UNION ALL
 SELECT 
  DATE_FORMAT(v_data_date,'%Y-%m') 	AS DATA_DATE,
  a1.PID,
  a1.AREA_NO_ID,
  a1.AREA_DSCR,
  '2'					AS PERIOD_FLAG,
 '转移性支出金额'  		 	AS INDEX_NAME,
  ROUND(SUM(a0.THIS_AMT), 4) 		AS INDEX_VALUE
FROM
  stg.trs_tmis_budget_payout a0 
 LEFT JOIN 
    (SELECT DISTINCT 
      p.GUOKU_ID AS PID,
      p.LEVEL,
      p.GUOKU_DSCR,
      p.AREA_NO_ID,
      p.AREA_DSCR,
      c.GUOKU_ID AS CID 
    FROM
      edw.cm_guoku_dimnsn p
      JOIN edw.cm_guoku_dimnsn c
        ON p.GUOKU_LVL_ID_2 = c.GUOKU_LVL_ID_1 
        OR p.GUOKU_LVL_ID_3 = c.GUOKU_LVL_ID_2 
        OR p.GUOKU_ID = c.GUOKU_LVL_ID_3) a1 
    ON a0.TRECODE = a1.CID
WHERE A0.D_ACCT LIKE CONCAT(DATE_FORMAT(v_data_date,'%Y-%m'),'%')
AND A0.`TRECODE` NOT IN('2231000000','2232000000','2233000000','2230000000')
AND A0.SUBJECT_CODE IN ('T020102','T020202')
GROUP BY 
  a1.AREA_NO_ID;
  
  
  
  
  
SET V_STEP_ID=4;
UPDATE visual_screen.vs_subject_pay SET AREA_DSCR = '李渡新区' WHERE AREA_CODE= '500997';
UPDATE visual_screen.vs_subject_pay SET AREA_DSCR = '彭水县' WHERE AREA_CODE= '500243';
UPDATE visual_screen.vs_subject_pay SET AREA_DSCR = '酉阳县' WHERE AREA_CODE= '500242';
UPDATE visual_screen.vs_subject_pay SET AREA_DSCR = '秀山县' WHERE AREA_CODE= '500241';
UPDATE visual_screen.vs_subject_pay SET AREA_DSCR = '石柱县' WHERE AREA_CODE= '500240';
UPDATE visual_screen.vs_subject_pay SET AREA_DSCR = '万盛区' WHERE AREA_CODE= '500131';
SET V_STEP_ID=5;
	END
$$

-- PROCEDURE `visual_screen`.`p_vs_subject_pay_sub`
DROP PROCEDURE IF EXISTS `visual_screen`.`p_vs_subject_pay_sub`;
DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `p_vs_subject_pay_sub`(IN v_data_date VARCHAR(10))
BEGIN
DECLARE V_STEP_ID       INT           DEFAULT 0; 
DECLARE V_PROC_NAME     VARCHAR(80)   DEFAULT 'visual_screen.p_vs_subject_pay.PRC';
DECLARE V_START_TIME    CHAR(19)      DEFAULT NOW();
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        GET DIAGNOSTICS CONDITION 1 @V_RETURN_CODE = RETURNED_SQLSTATE ,@V_ERROR_MSG = MESSAGE_TEXT;
        CALL ETL.EDW_PROC_ERROR_LOG(DATE_FORMAT(V_START_TIME,'%Y-%m'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,@V_RETURN_CODE,@V_ERROR_MSG);
        CALL ETL.EDW_PROC_TRACE_LOG(DATE_FORMAT(V_START_TIME,'%Y-%m'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
    END;
    
    
SET V_STEP_ID = 1;
DELETE FROM visual_screen.vs_subject_pay_sub WHERE DACCT = DATE_FORMAT(v_data_date,'%Y-%m');
SET V_STEP_ID = 2;
INSERT INTO visual_screen.vs_subject_pay_sub
SELECT 
  DATE_FORMAT(v_data_date,'%Y-%m') 	AS DATA_DATE,
  a1.PID,
  a1.AREA_NO_ID,
  a1.AREA_DSCR,
  '2'					AS PERIOD_FLAG,
  cm.SUBJECT_DSCR_1  			AS INDEX_NAME,
  ROUND(SUM(a0.THIS_AMT), 4) 		AS INDEX_VALUE
FROM
  stg.trs_tmis_budget_payout a0 
LEFT JOIN dmcode.cm_guoku_tstatsub_relation cm
       ON cm.S_BDGSBTVSION = YEAR(v_data_date)
       AND cm.STAT_CODE_0 = '2'
       AND a0.SUBJECT_CODE = cm.SUBJECT_CODE_4
  LEFT JOIN 
    (SELECT DISTINCT 
      p.GUOKU_ID AS PID,
      p.GUOKU_DSCR,
      p.AREA_NO_ID,
      p.AREA_DSCR,
      c.GUOKU_ID AS CID 
    FROM
      edw.cm_guoku_dimnsn p
      JOIN edw.cm_guoku_dimnsn c
        ON p.GUOKU_LVL_ID_2 = c.GUOKU_LVL_ID_1 
        OR p.GUOKU_LVL_ID_3 = c.GUOKU_LVL_ID_2 
        OR p.GUOKU_ID = c.GUOKU_LVL_ID_3) a1 
    ON a0.TRECODE = a1.CID
WHERE A0.D_ACCT LIKE CONCAT(DATE_FORMAT(v_data_date,'%Y-%m'),'%')
AND A0.`TRECODE` NOT IN('2231000000','2232000000','2233000000','2230000000')
AND cm.SUBJECT_CODE_1 <= '229'
GROUP BY 
  a1.AREA_NO_ID,
  cm.SUBJECT_CODE_1;
  
  
SET V_STEP_ID=4;
UPDATE visual_screen.vs_subject_pay_sub SET AREA_DSCR = '李渡新区' WHERE AREA_CODE= '500997';
UPDATE visual_screen.vs_subject_pay_sub SET AREA_DSCR = '彭水县' WHERE AREA_CODE= '500243';
UPDATE visual_screen.vs_subject_pay_sub SET AREA_DSCR = '酉阳县' WHERE AREA_CODE= '500242';
UPDATE visual_screen.vs_subject_pay_sub SET AREA_DSCR = '秀山县' WHERE AREA_CODE= '500241';
UPDATE visual_screen.vs_subject_pay_sub SET AREA_DSCR = '石柱县' WHERE AREA_CODE= '500240';
UPDATE visual_screen.vs_subject_pay_sub SET AREA_DSCR = '万盛区' WHERE AREA_CODE= '500131';
SET V_STEP_ID=5;
	END
$$

-- PROCEDURE `visual_screen`.`P_VS_TAX_REVENUE`
DROP PROCEDURE IF EXISTS `visual_screen`.`P_VS_TAX_REVENUE`;
DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `P_VS_TAX_REVENUE`(IN V_DATA_DATE VARCHAR(10))
BEGIN

DECLARE V_STEP_ID       INT           DEFAULT 0; 
DECLARE V_PROC_NAME     VARCHAR(80)   DEFAULT 'visual_screen.P_VS_TAX_REVENUE.PRC';
DECLARE V_START_TIME    CHAR(19)      DEFAULT NOW();
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        GET DIAGNOSTICS CONDITION 1 @V_RETURN_CODE = RETURNED_SQLSTATE ,@V_ERROR_MSG = MESSAGE_TEXT;
        CALL ETL.EDW_PROC_ERROR_LOG(DATE_FORMAT(V_START_TIME,'%Y-%m'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,@V_RETURN_CODE,@V_ERROR_MSG);
        CALL ETL.EDW_PROC_TRACE_LOG(DATE_FORMAT(V_START_TIME,'%Y-%m'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
    END;
	 CALL ETL.EDW_PROC_TRACE_LOG(DATE_FORMAT(V_START_TIME,'%Y%m%d'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
 
SET V_STEP_ID = 1;     
DELETE FROM visual_screen.vs_tax_revenue WHERE DACCT = DATE_FORMAT(V_DATA_DATE,'%Y-%m');
SET V_STEP_ID = 2; 
INSERT INTO visual_screen.vs_tax_revenue
SELECT
  DATE_FORMAT(V_DATA_DATE,'%Y-%m') AS ACCOUNT_PERIOD,
  b.PID AS  pid,
  b.AREA_NO_ID AS AREA_NO_ID,
  b.AREA_DSCR AS AREA_DSCR,
  '2' AS PERIOD_FLAG,
  '税收收入当期值' , 
  ROUND(SUM(a.THIS_AMT),4) AS INDEX_VALUE
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
  AND a.SUBJECT_CODE = '101'
  AND a.`LEVEL`>=b.LEVEL
  AND a.`TRECODE` NOT IN('2231000000','2232000000','2233000000','2230000000')
GROUP BY 
  DATE_FORMAT(V_DATA_DATE,'%Y-%m'),
  b.AREA_NO_ID
    UNION ALL   
  
  SELECT 
  AA.ACCOUNT_PERIOD,
  AA.pid,
  AA.AREA_NO_ID,
  AA.AREA_DSCR,
  AA.PERIOD_FLAG,
   '非税收收入当期值' , 
  (AA.INDEX_VALUE-BB.INDEX_VALUE)     AS INDEX_VALUE
FROM
(
SELECT
  DATE_FORMAT(V_DATA_DATE,'%Y-%m')     AS ACCOUNT_PERIOD,
  b.PID AS  pid,
  b.AREA_NO_ID AS AREA_NO_ID,
  b.AREA_DSCR AS AREA_DSCR,
  '2'	 			      AS PERIOD_FLAG,
  ROUND(SUM(a.THIS_AMT),4) 	      AS INDEX_VALUE
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
      ))b
  ON a.`TRECODE`=b.CID
WHERE DATE_FORMAT(a.D_ACCT,'%Y-%m')=DATE_FORMAT(V_DATA_DATE,'%Y-%m')
  AND a.SUBJECT_CODE = 'T010101'
  AND a.`LEVEL`>=b.LEVEL
  AND a.`TRECODE` NOT IN('2231000000','2232000000','2233000000','2230000000')
GROUP BY 
  DATE_FORMAT(V_DATA_DATE,'%Y-%m'),
  b.AREA_NO_ID
  )  AA
  LEFT JOIN 
  (
  SELECT
  DATE_FORMAT(V_DATA_DATE,'%Y-%m')	 AS ACCOUNT_PERIOD,
  b.PID AS  pid,
  b.AREA_NO_ID AS AREA_NO_ID,
  b.AREA_DSCR AS AREA_DSCR,
  '2',
  ROUND(SUM(a.THIS_AMT),4)	 AS INDEX_VALUE
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
      ))b
  ON a.`TRECODE`=b.CID
WHERE DATE_FORMAT(a.D_ACCT,'%Y-%m')=DATE_FORMAT(V_DATA_DATE,'%Y-%m')
  AND a.SUBJECT_CODE = '101'
  AND a.`LEVEL`>=b.LEVEL
  AND a.`TRECODE` NOT IN('2231000000','2232000000','2233000000','2230000000')
GROUP BY 
  DATE_FORMAT(V_DATA_DATE,'%Y-%m'),
  b.AREA_NO_ID
  )  BB
  ON AA.ACCOUNT_PERIOD = BB.ACCOUNT_PERIOD
  AND AA.AREA_NO_ID = BB.AREA_NO_ID;
	
INSERT INTO visual_screen.vs_tax_revenue
	
SELECT 
DISTINCT
lib.DACCT, 
lib.GUOKU_ID, 
lib.AREA_CODE, 
lib.AREA_DSCR ,   
lib.PERIOD_FLAG, 
'税收收入同比' AS INDEX_NAME        ,  
lib.INDEX_VALUE - V. INDEX_VALUE
FROM visual_screen.vs_tax_revenue lib 
LEFT JOIN visual_screen.vs_tax_revenue V 
ON V.DACCT =DATE_FORMAT(DATE_SUB(V_DATA_DATE, INTERVAL 1 YEAR),'%Y-%m')
AND lib.AREA_CODE = V.AREA_CODE
AND lib.GUOKU_ID =V.GUOKU_ID
and lib.INDEX_NAME = V.INDEX_NAME
WHERE lib.DACCT = DATE_FORMAT(V_DATA_DATE,'%Y-%m')
    AND  lib.PERIOD_FLAG='2'
    AND  LIB.INDEX_NAME = '税收收入当期值'
    UNION ALL   
  
   SELECT 
DISTINCT
lib.DACCT, 
lib.GUOKU_ID, 
lib.AREA_CODE, 
lib.AREA_DSCR ,   
lib.PERIOD_FLAG, 
'非税收入同比' AS INDEX_NAME        ,  
lib.INDEX_VALUE- V. INDEX_VALUE 
FROM visual_screen.vs_tax_revenue lib 
LEFT JOIN visual_screen.vs_tax_revenue V 
ON V.DACCT =DATE_FORMAT(DATE_SUB(V_DATA_DATE, INTERVAL 1 YEAR),'%Y-%m')
AND LIB.AREA_CODE = V.AREA_CODE
AND LIB.GUOKU_ID =V.GUOKU_ID
AND lib.INDEX_NAME = V.INDEX_NAME
WHERE lib.DACCT = DATE_FORMAT(V_DATA_DATE,'%Y-%m')
    AND  lib.PERIOD_FLAG='2'
    AND  LIB.INDEX_NAME = '非税收收入当期值';
SET V_STEP_ID =7; 
DELETE FROM visual_screen.vs_tax_revenue WHERE DACCT = left(V_DATA_DATE,4); 
SET V_STEP_ID = 8;  
INSERT INTO visual_screen.vs_tax_revenue
SELECT
DISTINCT
	LEFT(lib.DACCT,4), 
	lib.GUOKU_ID, 
	lib.AREA_CODE, 
	lib.AREA_DSCR ,   
	'4' AS PERIOD_FLAG,  
	'税收收入当期值'AS  INDEX_NAME         ,  
	SUM(lib.INDEX_VALUE) 
    FROM visual_screen.vs_tax_revenue lib
	WHERE  LEFT(lib.DACCT,4)= LEFT(V_DATA_DATE,4)
	AND  lib.PERIOD_FLAG='2'
	AND lib.INDEX_NAME= '税收收入当期值'
	GROUP BY 
	lib.AREA_CODE	
    UNION ALL   
  
  SELECT
DISTINCT
	LEFT(lib.DACCT,4), 
	lib.GUOKU_ID, 
	lib.AREA_CODE, 
	lib.AREA_DSCR ,   
	'4' AS PERIOD_FLAG,  
	'非税收收入当期值'AS  INDEX_NAME         ,  
	SUM(lib.INDEX_VALUE) 
    FROM visual_screen.vs_tax_revenue lib
	WHERE  LEFT(lib.DACCT,4)= LEFT(V_DATA_DATE,4)
	AND  lib.PERIOD_FLAG='2'
	AND lib.INDEX_NAME= '非税收收入当期值'
	GROUP BY 
	lib.AREA_CODE;
	
INSERT INTO visual_screen.vs_tax_revenue
	
SELECT 
DISTINCT
lib.DACCT, 
lib.GUOKU_ID, 
lib.AREA_CODE, 
lib.AREA_DSCR ,   
lib.PERIOD_FLAG, 
'税收收入同比' AS INDEX_NAME        ,  
lib.INDEX_VALUE - V.INDEX_VALUE  
FROM visual_screen.vs_tax_revenue lib 
LEFT JOIN visual_screen.vs_tax_revenue V 
ON V.DACCT = DATE_FORMAT(DATE_SUB(V_DATA_DATE, INTERVAL 1 YEAR),'%Y')
AND lib.AREA_CODE = V.AREA_CODE
AND lib.INDEX_NAME = V.INDEX_NAME
AND lib.PERIOD_FLAG = V.PERIOD_FLAG
WHERE lib.DACCT = DATE_FORMAT(V_DATA_DATE,'%Y')
    AND  lib.PERIOD_FLAG='4'
    AND  LIB.INDEX_NAME = '税收收入当期值'
    
    UNION ALL   
  
SELECT 
DISTINCT
lib.DACCT, 
lib.GUOKU_ID, 
lib.AREA_CODE, 
lib.AREA_DSCR ,   
lib.PERIOD_FLAG, 
'非税收收入同比' AS INDEX_NAME        ,  
lib.INDEX_VALUE - V.INDEX_VALUE  
FROM visual_screen.vs_tax_revenue lib 
LEFT JOIN visual_screen.vs_tax_revenue V 
ON V.DACCT = DATE_FORMAT(DATE_SUB(V_DATA_DATE, INTERVAL 1 YEAR),'%Y')
AND lib.AREA_CODE = V.AREA_CODE
AND lib.INDEX_NAME = V.INDEX_NAME
AND lib.PERIOD_FLAG = V.PERIOD_FLAG
WHERE lib.DACCT = DATE_FORMAT(V_DATA_DATE,'%Y')
    AND  lib.PERIOD_FLAG='4'
    AND  LIB.INDEX_NAME = '非税收收入当期值';	
    
    
SET V_STEP_ID=8;
UPDATE visual_screen.vs_tax_revenue SET AREA_DSCR = '李渡新区' WHERE AREA_CODE= '500997';
UPDATE visual_screen.vs_tax_revenue SET AREA_DSCR = '彭水县' WHERE AREA_CODE= '500243';
UPDATE visual_screen.vs_tax_revenue SET AREA_DSCR = '酉阳县' WHERE AREA_CODE= '500242';
UPDATE visual_screen.vs_tax_revenue SET AREA_DSCR = '秀山县' WHERE AREA_CODE= '500241';
UPDATE visual_screen.vs_tax_revenue SET AREA_DSCR = '石柱县' WHERE AREA_CODE= '500240';
UPDATE visual_screen.vs_tax_revenue SET AREA_DSCR = '万盛区' WHERE AREA_CODE= '500131';
SET V_STEP_ID=9;
CALL ETL.EDW_PROC_TRACE_LOG(DATE_FORMAT(V_START_TIME,'%Y%m%d'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
	END
$$

-- PROCEDURE `visual_screen`.`P_VS_TAX_SUBJECT`
DROP PROCEDURE IF EXISTS `visual_screen`.`P_VS_TAX_SUBJECT`;
DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `P_VS_TAX_SUBJECT`(IN V_DATA_DATE VARCHAR(10))
BEGIN

DECLARE V_STEP_ID       INT           DEFAULT 0; 
DECLARE V_PROC_NAME     VARCHAR(80)   DEFAULT 'visual_screen.P_VS_TAX_SUBJECt.PRC';
DECLARE V_START_TIME    CHAR(19)      DEFAULT NOW();
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        GET DIAGNOSTICS CONDITION 1 @V_RETURN_CODE = RETURNED_SQLSTATE ,@V_ERROR_MSG = MESSAGE_TEXT;
        CALL ETL.EDW_PROC_ERROR_LOG(DATE_FORMAT(V_START_TIME,'%Y-%m'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,@V_RETURN_CODE,@V_ERROR_MSG);
        CALL ETL.EDW_PROC_TRACE_LOG(DATE_FORMAT(V_START_TIME,'%Y-%m'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
    END;
	 CALL ETL.EDW_PROC_TRACE_LOG(DATE_FORMAT(V_START_TIME,'%Y%m%d'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
 
SET V_STEP_ID = 1;     
DELETE FROM visual_screen.vs_tax_subject WHERE DACCT = DATE_FORMAT(V_DATA_DATE,'%Y-%m');
SET V_STEP_ID = 2; 
INSERT INTO visual_screen.vs_tax_subject
        SELECT  
	DATE_FORMAT(DET.S_INTREDATE,'%Y-%m'),
	B.PID,
	INDUS.lev_1_id, 
	INDUS.lev_1_dscr,
	'2',
	SUM(F_AMT)	
	FROM ODS.TV_FIN_INCOME_DETAIL DET     
LEFT JOIN ODS.TRS_ENTERPRISE_INFOR INFO
       ON DET.TAXPAYCODE = INFO.UNISCID
LEFT JOIN EDW.CM_CLR_INDUSTRY_INFO INDUS
       ON INFO.INDUSTRYCO = INDUS.LEV_1_ID
        LEFT JOIN 
	(
	SELECT 
		DISTINCT 
		p.GUOKU_ID AS PID,
		p.LEVEL,
		p.GUOKU_DSCR,
		c.GUOKU_ID AS CID
	FROM edw.cm_guoku_dimnsn p
	LEFT JOIN edw.cm_guoku_dimnsn c
	ON p.GUOKU_LVL_ID_2 = c.GUOKU_LVL_ID_1 
	OR  p.GUOKU_LVL_ID_3 = c.GUOKU_LVL_ID_2 
	OR  p.GUOKU_ID = c.GUOKU_LVL_ID_3
	) B 
     ON DET.S_TRECODE = B.CID
WHERE DATE_FORMAT(DET.S_INTREDATE,'%Y%m') = DATE_FORMAT(V_DATA_DATE,'%Y%m')
  AND DET.C_BDGLEVEL >= B.LEVEL
GROUP BY
      DATE_FORMAT(DET.S_INTREDATE,'%Y%m'),
	INDUS.LEV_1_ID,
	DET.S_TRECODE,
	DET.C_BDGLEVEL
	;
SET V_STEP_ID=4;
UPDATE visual_screen.vs_tax_subject SET AREA_DSCR = '李渡新区' WHERE AREA_CODE= '500997';
UPDATE visual_screen.vs_tax_subject SET AREA_DSCR = '彭水县' WHERE AREA_CODE= '500243';
UPDATE visual_screen.vs_tax_subject SET AREA_DSCR = '酉阳县' WHERE AREA_CODE= '500242';
UPDATE visual_screen.vs_tax_subject SET AREA_DSCR = '秀山县' WHERE AREA_CODE= '500241';
UPDATE visual_screen.vs_tax_subject SET AREA_DSCR = '石柱县' WHERE AREA_CODE= '500240';
UPDATE visual_screen.vs_tax_subject SET AREA_DSCR = '万盛区' WHERE AREA_CODE= '500131';
SET V_STEP_ID=5;
CALL ETL.EDW_PROC_TRACE_LOG(DATE_FORMAT(V_START_TIME,'%Y%m%d'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
	END
$$

-- PROCEDURE `visual_screen`.`P_VS_THREE_BUDGET_REVENUE`
DROP PROCEDURE IF EXISTS `visual_screen`.`P_VS_THREE_BUDGET_REVENUE`;
DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `P_VS_THREE_BUDGET_REVENUE`(IN V_DATA_DATE VARCHAR(10))
BEGIN

DECLARE V_STEP_ID       INT           DEFAULT 0; 
DECLARE V_PROC_NAME     VARCHAR(80)   DEFAULT 'visual_screen.P_VS_THREE_BUDGET_REVENUE.PRC';
DECLARE V_START_TIME    CHAR(19)      DEFAULT NOW();
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        GET DIAGNOSTICS CONDITION 1 @V_RETURN_CODE = RETURNED_SQLSTATE ,@V_ERROR_MSG = MESSAGE_TEXT;
        CALL ETL.EDW_PROC_ERROR_LOG(DATE_FORMAT(V_START_TIME,'%Y-%m'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,@V_RETURN_CODE,@V_ERROR_MSG);
        CALL ETL.EDW_PROC_TRACE_LOG(DATE_FORMAT(V_START_TIME,'%Y-%m'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
    END;
	 CALL ETL.EDW_PROC_TRACE_LOG(DATE_FORMAT(V_START_TIME,'%Y%m%d'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
 
SET V_STEP_ID = 1;     
DELETE FROM visual_screen.vs_three_budget_revenue WHERE DACCT = DATE_FORMAT(V_DATA_DATE,'%Y-%m');
SET V_STEP_ID = 2; 
INSERT INTO visual_screen.vs_three_budget_revenue
SELECT 
DATE_FORMAT(v_data_date,'%Y-%m'), 
b.PID,
b.AREA_NO_ID, 
b.AREA_DSCR,
'2',
'一般公共预算收入' AS INDEX_NAME        ,  
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
WHERE DATE_FORMAT(a.D_ACCT,'%Y-%m')=DATE_FORMAT(v_data_date,'%Y-%m')
  AND a.SUBJECT_CODE = 'T010101'
  AND a.`LEVEL`>=b.LEVEL
  AND a.`TRECODE` NOT IN('2231000000','2232000000','2233000000','2230000000')
GROUP BY 
  DATE_FORMAT(v_data_date,'%Y-%m'),
  b.AREA_NO_ID 
  
    UNION ALL   
  
   SELECT 
DATE_FORMAT(v_data_date,'%Y-%m'), 
b.PID,
b.AREA_NO_ID, 
b.AREA_DSCR,
'2',
'基金预算收入' AS INDEX_NAME        ,  
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
WHERE DATE_FORMAT(a.D_ACCT,'%Y-%m')=DATE_FORMAT(v_data_date,'%Y-%m')
  AND a.SUBJECT_CODE = 'T010201'
  AND a.`LEVEL`>=b.LEVEL
  AND a.`TRECODE` NOT IN('2231000000','2232000000','2233000000','2230000000')
GROUP BY 
  DATE_FORMAT(v_data_date,'%Y-%m'),
  b.AREA_NO_ID 
  
	UNION ALL
     SELECT 
DATE_FORMAT(v_data_date,'%Y-%m'), 
b.PID,
b.AREA_NO_ID, 
b.AREA_DSCR,
'2',
'国有资本经营预算收入' AS INDEX_NAME        ,  
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
WHERE DATE_FORMAT(a.D_ACCT,'%Y-%m')=DATE_FORMAT(v_data_date,'%Y-%m')
  AND a.SUBJECT_CODE = 'T010601'
  AND a.`LEVEL`>=b.LEVEL
  AND a.`TRECODE` NOT IN('2231000000','2232000000','2233000000','2230000000')
GROUP BY 
  DATE_FORMAT(v_data_date,'%Y-%m'),
  b.AREA_NO_ID ;
	
INSERT INTO visual_screen.vs_three_budget_revenue
SELECT
  a.DACCT,
  a.GUOKU_ID,
  a.AREA_CODE,
  a.AREA_DSCR,
  a.PERIOD_FLAG,
  concat(a.INDEX_NAME,'同比'),
  (a.INDEX_VALUE-b.INDEX_VALUE)/b.INDEX_VALUE
FROM visual_screen.vs_three_budget_revenue  a
LEFT JOIN visual_screen.vs_three_budget_revenue  b
       ON b.DACCT = DATE_FORMAT(DATE_SUB(v_data_date,INTERVAL 1 YEAR),'%Y-%m')
       AND a.GUOKU_ID = b.GUOKU_ID
       AND a.AREA_CODE = b.AREA_CODE
       AND a.INDEX_NAME = b.INDEX_NAME
       AND a.PERIOD_FLAG = b.PERIOD_FLAG
WHERE a.DACCT = DATE_FORMAT(v_data_date,'%Y-%m')
  AND a.PERIOD_FLAG = '2'
  and a.INDEX_NAME in ('一般公共预算收入','基金预算收入','国有资本经营预算收入');
	
SET V_STEP_ID = 3;     
DELETE FROM visual_screen.vs_three_budget_revenue WHERE DACCT = left(V_DATA_DATE,4);
SET V_STEP_ID = 4; 
INSERT INTO visual_screen.vs_three_budget_revenue
SELECT
  left(a.DACCT,4),
  a.GUOKU_ID,
  a.AREA_CODE,
  a.AREA_DSCR,
  '4'   as PERIOD_FLAG,
  a.INDEX_NAME,
  sum(a.INDEX_VALUE)
FROM visual_screen.vs_three_budget_revenue  a
WHERE a.DACCT LIKE CONCAT(LEFT(v_data_date,4),'%')
  AND a.PERIOD_FLAG = '2'
  AND a.INDEX_NAME IN ('一般公共预算收入','基金预算收入','国有资本经营预算收入')
group by 
  a.GUOKU_ID,
  a.AREA_CODE,
  a.PERIOD_FLAG,
  a.INDEX_NAME;
	 
INSERT INTO visual_screen.vs_three_budget_revenue
SELECT
  a.DACCT,
  a.GUOKU_ID,
  a.AREA_CODE,
  a.AREA_DSCR,
  a.PERIOD_FLAG,
  CONCAT(a.INDEX_NAME,'同比'),
  (a.INDEX_VALUE-b.INDEX_VALUE)/b.INDEX_VALUE
FROM visual_screen.vs_three_budget_revenue  a
LEFT JOIN visual_screen.vs_three_budget_revenue  b
       ON b.DACCT LIKE CONCAT(LEFT(DATE_SUB(v_data_date,INTERVAL 1 YEAR),4),'%')
       AND a.GUOKU_ID = b.GUOKU_ID
       AND a.AREA_CODE = b.AREA_CODE
       AND a.INDEX_NAME = b.INDEX_NAME
       AND a.PERIOD_FLAG = b.PERIOD_FLAG
WHERE a.DACCT  LIKE CONCAT(LEFT(v_data_date,4),'%')
  AND a.PERIOD_FLAG = '4'
  AND a.INDEX_NAME IN ('一般公共预算收入','基金预算收入','国有资本经营预算收入')
GROUP BY 
  a.GUOKU_ID,
  a.AREA_CODE,
  a.PERIOD_FLAG,
  a.INDEX_NAME;
	 
	 
	 
	 
	 
SET V_STEP_ID=4;
UPDATE visual_screen.vs_three_budget_revenue SET AREA_DSCR = '李渡新区' WHERE AREA_CODE= '500997';
UPDATE visual_screen.vs_three_budget_revenue SET AREA_DSCR = '彭水县' WHERE AREA_CODE= '500243';
UPDATE visual_screen.vs_three_budget_revenue SET AREA_DSCR = '酉阳县' WHERE AREA_CODE= '500242';
UPDATE visual_screen.vs_three_budget_revenue SET AREA_DSCR = '秀山县' WHERE AREA_CODE= '500241';
UPDATE visual_screen.vs_three_budget_revenue SET AREA_DSCR = '石柱县' WHERE AREA_CODE= '500240';
UPDATE visual_screen.vs_three_budget_revenue SET AREA_DSCR = '万盛区' WHERE AREA_CODE= '500131';
SET V_STEP_ID=5;
CALL ETL.EDW_PROC_TRACE_LOG(DATE_FORMAT(V_START_TIME,'%Y%m%d'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
	END
$$

-- PROCEDURE `visual_screen`.`P_VS_TRANSFER_INCOME`
DROP PROCEDURE IF EXISTS `visual_screen`.`P_VS_TRANSFER_INCOME`;
DELIMITER $$
