-- Vastbase source-port script for indicators_lib.init_report02
-- Strategy: keep MySQL B-compatible body first, then validate and narrow incompatibilities.

DROP PROCEDURE IF EXISTS indicators_lib.init_report02;
CREATE PROCEDURE indicators_lib.init_report02(IN v_data_date VARCHAR(10))
BEGIN

	DELETE FROM indicators_lib.lib_indicators_000204 
WHERE (ACCOUNT_PERIOD = DATE_FORMAT(v_data_date, '%Y-%m') 
 OR ACCOUNT_PERIOD = CONCAT(
      YEAR(v_data_date),
      'Q',
      QUARTER(v_data_date)
    ))
AND INDEX_ID ='cbed93e8b23a11ea8bc1000c29587404';

INSERT INTO indicators_lib.lib_indicators_000204(INDEX_ID,ACCOUNT_PERIOD,INDEX_DIM_CODE,INDEX_DIM_DESCR,DIMENSION_FLAG,PERIOD_FLAG,INDEX_VALUE,ADD_USERID,ADD_DATE)


SELECT
 'cbed93e8b23a11ea8bc1000c29587404',
  DATE_FORMAT(v_data_date,'%Y-%m'),
  b.PID,
  b.GUOKU_DSCR,
  '1',
  '2',
  ROUND(SUM(a.YEAR_AMT),4) AS INDEX_VALUE,
  '-1',
  NOW()
FROM
  stg.trs_tmis_budget_income a
  LEFT JOIN 
  (SELECT
    DISTINCT p.`GUOKU_ID` AS PID,
	p.LEVEL,
    p.GUOKU_DSCR,
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
WHERE DATE_FORMAT(a.D_ACCT,'%Y-%m')=DATE_FORMAT(v_data_date,'%Y-%m')
  AND a.SUBJECT_CODE = '11002'
  AND a.`LEVEL`=b.LEVEL
  AND a.`TRECODE` NOT IN('2231000000','2232000000','2233000000','2230000000')
GROUP BY 
  DATE_FORMAT(a.D_ACCT,'%Y-%m'),
  b.PID
  
UNION ALL


 SELECT
 'cbed93e8b23a11ea8bc1000c29587404',
  DATE_FORMAT(v_data_date,'%Y-%m'),
  b.AREA_NO_ID,
  b.AREA_DSCR,
  '2',
  '2',
  ROUND(SUM(a.YEAR_AMT),4) AS INDEX_VALUE,
  '-1',
  NOW()
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
  AND a.SUBJECT_CODE = '11002'
  AND a.`LEVEL`=b.LEVEL
  AND a.`TRECODE` NOT IN('2231000000','2232000000','2233000000','2230000000')
GROUP BY 
  DATE_FORMAT(a.D_ACCT,'%Y-%m'),
  b.AREA_NO_ID
  
  UNION ALL 
  
  
SELECT
 'cbed93e8b23a11ea8bc1000c29587404',
  CONCAT(YEAR(A.D_ACCT),'Q',QUARTER(A.D_ACCT)),
  b.PID,
  b.GUOKU_DSCR,
  '1',
  '3',
  ROUND(SUM(a.YEAR_AMT),4) AS INDEX_VALUE,
  '-1',
  NOW()
FROM
  stg.trs_tmis_budget_income a
  LEFT JOIN 
  (SELECT
    DISTINCT p.`GUOKU_ID` AS PID,
	p.LEVEL,
    p.GUOKU_DSCR,
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
WHERE A.D_ACCT =  CASE WHEN MONTH(v_data_date) IN ('1','2','3') THEN CONCAT(YEAR(v_data_date),'-03-01')
		       WHEN MONTH(v_data_date) IN ('4','5','6') THEN CONCAT(YEAR(v_data_date),'-06-01')
		       WHEN MONTH(v_data_date) IN ('7','8','9') THEN CONCAT(YEAR(v_data_date),'-09-01')
		       ELSE CONCAT(YEAR(v_data_date),'-12-01')  END
  AND a.SUBJECT_CODE IN ('11002')
  AND a.`LEVEL`=b.LEVEL
  AND a.`TRECODE` NOT IN('2231000000','2232000000','2233000000','2230000000')
GROUP BY 
  A.D_ACCT,
  b.PID
  
UNION ALL


 SELECT
 'cbed93e8b23a11ea8bc1000c29587404',
  CONCAT(YEAR(A.D_ACCT),'Q',QUARTER(A.D_ACCT)),
  b.AREA_NO_ID,
  b.AREA_DSCR,
  '2',
  '3',
  ROUND(SUM(a.YEAR_AMT),4) AS INDEX_VALUE,
  '-1',
  NOW()
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
WHERE A.D_ACCT =  CASE WHEN MONTH(v_data_date) IN ('1','2','3') THEN CONCAT(YEAR(v_data_date),'-03-01')
		       WHEN MONTH(v_data_date) IN ('4','5','6') THEN CONCAT(YEAR(v_data_date),'-06-01')
		       WHEN MONTH(v_data_date) IN ('7','8','9') THEN CONCAT(YEAR(v_data_date),'-09-01')
		       ELSE CONCAT(YEAR(v_data_date),'-12-01')  END
  AND a.SUBJECT_CODE IN ('11002')
  AND a.`LEVEL`=b.LEVEL
  AND a.`TRECODE` NOT IN('2231000000','2232000000','2233000000','2230000000')
GROUP BY 
  A.D_ACCT,
  b.AREA_NO_ID;
  
  DELETE FROM indicators_lib.lib_indicators_000205 
WHERE (ACCOUNT_PERIOD = DATE_FORMAT(v_data_date, '%Y-%m') 
 OR ACCOUNT_PERIOD = CONCAT(
      YEAR(v_data_date),
      'Q',
      QUARTER(v_data_date)
    ))
AND INDEX_ID ='4456bd38b23b11ea8bc1000c29587404';

INSERT INTO indicators_lib.lib_indicators_000205(INDEX_ID,ACCOUNT_PERIOD,INDEX_DIM_CODE,INDEX_DIM_DESCR,DIMENSION_FLAG,PERIOD_FLAG,INDEX_VALUE,ADD_USERID,ADD_DATE)

SELECT 
'4456bd38b23b11ea8bc1000c29587404',
DATE_FORMAT(v_data_date,'%Y-%m'),
A.INDEX_DIM_CODE,
A.INDEX_DIM_DESCR,
A.DIMENSION_FLAG,
A.PERIOD_FLAG,
ROUND((A.INDEX_VALUE-B.INDEX_VALUE)/B.INDEX_VALUE,4) AS INDEX_VALUE,
'-1',
NOW()
FROM INDICATORS_LIB.lib_indicators_000204 A
LEFT JOIN 
(
SELECT 
INDEX_DIM_CODE,
INDEX_VALUE
FROM INDICATORS_LIB.lib_indicators_000204 
WHERE ACCOUNT_PERIOD = DATE_FORMAT(DATE_SUB(v_data_date,INTERVAL 1 YEAR),'%Y-%m')
) B
ON A.INDEX_DIM_CODE = B.INDEX_DIM_CODE
WHERE A.ACCOUNT_PERIOD = DATE_FORMAT(v_data_date,'%Y-%m')


UNION ALL 


SELECT 
'4456bd38b23b11ea8bc1000c29587404',
CONCAT(YEAR(v_data_date),'Q',QUARTER(v_data_date)),
A.INDEX_DIM_CODE,
A.INDEX_DIM_DESCR,
A.DIMENSION_FLAG,
A.PERIOD_FLAG,
ROUND((A.INDEX_VALUE-B.INDEX_VALUE)/B.INDEX_VALUE,4) AS INDEX_VALUE,
'-1',
NOW()
FROM INDICATORS_LIB.lib_indicators_000204 A
LEFT JOIN 
(
SELECT 
INDEX_DIM_CODE,
INDEX_VALUE
FROM INDICATORS_LIB.lib_indicators_000204 
WHERE ACCOUNT_PERIOD = CONCAT(YEAR(DATE_SUB(v_data_date,INTERVAL 1 YEAR)),'Q',QUARTER(v_data_date))
) B
ON A.INDEX_DIM_CODE = B.INDEX_DIM_CODE
WHERE A.ACCOUNT_PERIOD =CONCAT(YEAR(v_data_date),'Q',QUARTER(v_data_date));

DELETE FROM indicators_lib.lib_indicators_000206
 WHERE (ACCOUNT_PERIOD = DATE_FORMAT(v_data_date, '%Y-%m')
  OR ACCOUNT_PERIOD = CONCAT(
      YEAR(v_data_date),
      'Q',
      QUARTER(v_data_date)
    ))
 AND INDEX_ID ='d52a325cb23c11ea8bc1000c29587404';
 
 INSERT INTO indicators_lib.lib_indicators_000206(INDEX_ID,ACCOUNT_PERIOD,INDEX_DIM_CODE,INDEX_DIM_DESCR,DIMENSION_FLAG,PERIOD_FLAG,INDEX_VALUE,ADD_USERID,ADD_DATE)

SELECT
 'd52a325cb23c11ea8bc1000c29587404',
  DATE_FORMAT(v_data_date,'%Y-%m'),
  b.PID,
  b.GUOKU_DSCR,
  '1',
  '2',
  ROUND(SUM(a.THIS_AMT),4) AS INDEX_VALUE,
  '-1',
  NOW()
FROM
  stg.trs_tmis_budget_income a
  LEFT JOIN 
  (SELECT
    DISTINCT p.`GUOKU_ID` AS PID,
	p.LEVEL,
    p.GUOKU_DSCR,
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
WHERE DATE_FORMAT(a.D_ACCT,'%Y-%m')=DATE_FORMAT(v_data_date,'%Y-%m')
  AND a.SUBJECT_CODE IN ('T0103','T0105')
  AND a.`LEVEL`>=b.LEVEL
  AND a.`TRECODE` NOT IN('2231000000','2232000000','2233000000','2230000000')
GROUP BY 
  DATE_FORMAT(a.D_ACCT,'%Y-%m'),
  b.PID
  
UNION ALL


 SELECT
 'd52a325cb23c11ea8bc1000c29587404',
  DATE_FORMAT(v_data_date,'%Y-%m'),
  b.AREA_NO_ID,
  b.AREA_DSCR,
  '2',
  '2',
  ROUND(SUM(a.THIS_AMT),4) AS INDEX_VALUE,
  '-1',
  NOW()
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
  b.AREA_NO_ID
  
  
  UNION ALL
  
  
SELECT
 'd52a325cb23c11ea8bc1000c29587404',
  CONCAT(YEAR(a.d_acct),'Q',QUARTER(A.D_ACCT)),
  b.PID,
  b.GUOKU_DSCR,
  '1',
  '3',
  ROUND(SUM(a.THIS_AMT),4) AS INDEX_VALUE,
  '-1',
  NOW()
FROM
  stg.trs_tmis_budget_income a
  LEFT JOIN 
  (SELECT
    DISTINCT p.`GUOKU_ID` AS PID,
	p.LEVEL,
    p.GUOKU_DSCR,
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
WHERE A.D_ACCT =  CASE WHEN MONTH(v_data_date) IN ('1','2','3') THEN CONCAT(YEAR(v_data_date),'-03-01')
		       WHEN MONTH(v_data_date) IN ('4','5','6') THEN CONCAT(YEAR(v_data_date),'-06-01')
		       WHEN MONTH(v_data_date) IN ('7','8','9') THEN CONCAT(YEAR(v_data_date),'-09-01')
		       ELSE CONCAT(YEAR(v_data_date),'-12-01')  END
  AND a.SUBJECT_CODE IN ('T0103','T0105')
  AND a.`LEVEL`>=b.LEVEL
  AND a.`TRECODE` NOT IN('2231000000','2232000000','2233000000','2230000000')
GROUP BY 
  A.D_ACCT,
  b.PID
  
UNION ALL


 SELECT
 'd52a325cb23c11ea8bc1000c29587404',
  CONCAT(YEAR(A.D_ACCT),'Q',QUARTER(A.D_ACCT)),
  b.AREA_NO_ID,
  b.AREA_DSCR,
  '2',
  '3',
  ROUND(SUM(a.THIS_AMT),4) AS INDEX_VALUE,
  '-1',
  NOW()
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
WHERE A.D_ACCT =  CASE WHEN MONTH(v_data_date) IN ('1','2','3') THEN CONCAT(YEAR(v_data_date),'-03-01')
		       WHEN MONTH(v_data_date) IN ('4','5','6') THEN CONCAT(YEAR(v_data_date),'-06-01')
		       WHEN MONTH(v_data_date) IN ('7','8','9') THEN CONCAT(YEAR(v_data_date),'-09-01')
		       ELSE CONCAT(YEAR(v_data_date),'-12-01')  END
  AND a.SUBJECT_CODE IN ('T0103','T0105')
  AND a.`LEVEL`>=b.LEVEL
  AND a.`TRECODE` NOT IN('2231000000','2232000000','2233000000','2230000000')
GROUP BY 
  A.D_ACCT,
  b.AREA_NO_ID;
	
	
	DELETE FROM indicators_lib.lib_indicators_000228 
WHERE (ACCOUNT_PERIOD = DATE_FORMAT(v_data_date, '%Y-%m')
 OR ACCOUNT_PERIOD = CONCAT(
      YEAR(v_data_date),
      'Q',
      QUARTER(v_data_date)
    ))
  AND INDEX_ID = '7af91fc6b53911ea8bc1000c29587404';
	
	INSERT INTO indicators_lib.lib_indicators_000228
(INDEX_ID,
ACCOUNT_PERIOD,
INDEX_DIM_CODE,
INDEX_DIM_DESCR,
DIMENSION_FLAG,
PERIOD_FLAG,
INDEX_VALUE,
ADD_USERID,
ADD_DATE
)
 SELECT
 '7af91fc6b53911ea8bc1000c29587404',
DATE_FORMAT(v_data_date,'%Y-%m') ,
  ind1.INDEX_DIM_CODE,
  ind1.INDEX_DIM_DESCR,
  ind1.DIMENSION_FLAG,
  ind1.PERIOD_FLAG,
  ROUND(IFNULL(ind1.INDEX_VALUE,0) + IFNULL(ind194.INDEX_VALUE,0)  +IFNULL(ind206.INDEX_VALUE,0),4) AS INDEX_VALUE,
  '-1',
  NOW()
  FROM indicators_lib.lib_indicators_000001 ind1
   LEFT JOIN indicators_lib.lib_indicators_000194 ind194
  ON ind1.ACCOUNT_PERIOD=ind194.ACCOUNT_PERIOD
  AND ind1.INDEX_DIM_CODE = ind194.INDEX_DIM_CODE
     LEFT JOIN indicators_lib.lib_indicators_000206 ind206
      ON ind1.ACCOUNT_PERIOD=ind206.ACCOUNT_PERIOD
      AND ind1.INDEX_DIM_CODE = ind206.INDEX_DIM_CODE
  WHERE ind1.ACCOUNT_PERIOD=DATE_FORMAT(v_data_date,'%Y-%m')  
  UNION ALL 
  SELECT
 '7af91fc6b53911ea8bc1000c29587404',
  CONCAT(YEAR(v_data_date),'Q',QUARTER(v_data_date)),
  ind1.INDEX_DIM_CODE,
  ind1.INDEX_DIM_DESCR,
  ind1.DIMENSION_FLAG,
  ind1.PERIOD_FLAG, 
  ROUND(IFNULL(ind1.INDEX_VALUE,0) + IFNULL(ind194.INDEX_VALUE,0)  +IFNULL(ind206.INDEX_VALUE,0),4) AS INDEX_VALUE,
  '-1',
  NOW()
  FROM indicators_lib.lib_indicators_000001 ind1
   LEFT JOIN indicators_lib.lib_indicators_000194 ind194
  ON ind1.ACCOUNT_PERIOD=ind194.ACCOUNT_PERIOD
    AND ind1.INDEX_DIM_CODE = ind194.INDEX_DIM_CODE
     LEFT JOIN indicators_lib.lib_indicators_000206 ind206
      ON ind194.ACCOUNT_PERIOD=ind206.ACCOUNT_PERIOD
        AND ind194.INDEX_DIM_CODE = ind206.INDEX_DIM_CODE
  WHERE ind1.ACCOUNT_PERIOD=CONCAT(YEAR(v_data_date),'Q',QUARTER(v_data_date));
      
      
    DELETE FROM indicators_lib.lib_indicators_000252 

WHERE (ACCOUNT_PERIOD = DATE_FORMAT(v_data_date, '%Y-%m')
 OR ACCOUNT_PERIOD = CONCAT(
      YEAR(v_data_date),
      'Q',
      QUARTER(v_data_date)
    ))
  AND INDEX_ID = '18589785b60f11ea8bc1000c29587404';
INSERT INTO indicators_lib.lib_indicators_000252(
INDEX_ID,
ACCOUNT_PERIOD,
INDEX_DIM_CODE,
INDEX_DIM_DESCR,
DIMENSION_FLAG,
PERIOD_FLAG,
INDEX_VALUE,
ADD_USERID,
ADD_DATE
)
SELECT
  '18589785b60f11ea8bc1000c29587404',
  a.ACCOUNT_PERIOD,
  a.INDEX_DIM_CODE,
  a.INDEX_DIM_DESCR,
  a.DIMENSION_FLAG,
  a.PERIOD_FLAG,
  IFNULL(a.INDEX_VALUE,0)+IFNULL(b.INDEX_VALUE,0) AS INDEX_VALUE,
  '-1',
  NOW()
FROM
  indicators_lib.lib_indicators_000228 a
LEFT JOIN 
   indicators_lib.lib_indicators_000210 b
  ON a.INDEX_DIM_CODE=b.INDEX_DIM_CODE
  AND a.ACCOUNT_PERIOD=b.ACCOUNT_PERIOD
WHERE a.ACCOUNT_PERIOD=DATE_FORMAT(v_data_date,'%Y-%m')

UNION 

SELECT
  '18589785b60f11ea8bc1000c29587404',
  a.ACCOUNT_PERIOD,
  a.INDEX_DIM_CODE,
  a.INDEX_DIM_DESCR,
  a.DIMENSION_FLAG,
  a.PERIOD_FLAG,
  IFNULL(a.INDEX_VALUE,0)+IFNULL(b.INDEX_VALUE,0) AS INDEX_VALUE,
  '-1',
  NOW()
FROM
  indicators_lib.lib_indicators_000228 a
LEFT JOIN 
   indicators_lib.lib_indicators_000210 b
  ON a.INDEX_DIM_CODE=b.INDEX_DIM_CODE
  AND a.ACCOUNT_PERIOD=b.ACCOUNT_PERIOD
WHERE a.ACCOUNT_PERIOD=CONCAT(YEAR(v_data_date),'Q',QUARTER(v_data_date));



	
	
  
  DELETE FROM indicators_lib.lib_indicators_000207
 WHERE (ACCOUNT_PERIOD = DATE_FORMAT(v_data_date, '%Y-%m')
  OR ACCOUNT_PERIOD = CONCAT(
      YEAR(v_data_date),
      'Q',
      QUARTER(v_data_date)
    ))
 AND INDEX_ID ='3a249b1db23d11ea8bc1000c29587404';
 
 INSERT INTO indicators_lib.lib_indicators_000207(INDEX_ID,ACCOUNT_PERIOD,INDEX_DIM_CODE,INDEX_DIM_DESCR,DIMENSION_FLAG,PERIOD_FLAG,INDEX_VALUE,ADD_USERID,ADD_DATE)
SELECT 
'3a249b1db23d11ea8bc1000c29587404',
DATE_FORMAT(v_data_date,'%Y-%m'),
A.INDEX_DIM_CODE,
A.INDEX_DIM_DESCR,
A.DIMENSION_FLAG,
A.PERIOD_FLAG,
CASE WHEN B.INDEX_VALUE = '0'
      THEN '0'
     ELSE
ROUND((A.INDEX_VALUE-B.INDEX_VALUE)/B.INDEX_VALUE,4) END AS INDEX_VALUE,
'-1',
NOW()
FROM INDICATORS_LIB.lib_indicators_000206 A
LEFT JOIN 
(
SELECT 
INDEX_DIM_CODE,
INDEX_VALUE
FROM INDICATORS_LIB.lib_indicators_000206 
WHERE ACCOUNT_PERIOD = DATE_FORMAT(DATE_SUB(v_data_date,INTERVAL 1 YEAR),'%Y-%m')
) B
ON A.INDEX_DIM_CODE = B.INDEX_DIM_CODE
WHERE A.ACCOUNT_PERIOD = DATE_FORMAT(v_data_date,'%Y-%m')


UNION ALL

SELECT 
'3a249b1db23d11ea8bc1000c29587404',
CONCAT(YEAR(v_data_date),'Q',QUARTER(v_data_date)),
A.INDEX_DIM_CODE,
A.INDEX_DIM_DESCR,
A.DIMENSION_FLAG,
A.PERIOD_FLAG,
CASE WHEN B.INDEX_VALUE = '0'
      THEN '0'
     ELSE
ROUND((A.INDEX_VALUE-B.INDEX_VALUE)/B.INDEX_VALUE,4) END AS INDEX_VALUE,
'-1',
NOW()
FROM INDICATORS_LIB.lib_indicators_000206 A
LEFT JOIN 
(
SELECT 
INDEX_DIM_CODE,
INDEX_VALUE
FROM INDICATORS_LIB.lib_indicators_000206 
WHERE ACCOUNT_PERIOD = CONCAT(YEAR(DATE_SUB(v_data_date,INTERVAL 1 YEAR)),'Q',QUARTER(v_data_date))
) B
ON A.INDEX_DIM_CODE = B.INDEX_DIM_CODE
WHERE A.ACCOUNT_PERIOD = CONCAT(YEAR(v_data_date),'Q',QUARTER(v_data_date));

DELETE FROM indicators_lib.lib_indicators_000249 
WHERE (ACCOUNT_PERIOD = DATE_FORMAT(v_data_date, '%Y-%m') 
 OR ACCOUNT_PERIOD = CONCAT(
      YEAR(v_data_date),
      'Q',
      QUARTER(v_data_date)
    ))
AND INDEX_ID ='570e9f50b60d11ea8bc1000c29587404';

INSERT INTO indicators_lib.lib_indicators_000249(INDEX_ID,ACCOUNT_PERIOD,INDEX_DIM_CODE,INDEX_DIM_DESCR,DIMENSION_FLAG,PERIOD_FLAG,INDEX_VALUE,ADD_USERID,ADD_DATE)
SELECT
  '570e9f50b60d11ea8bc1000c29587404',
  a.ACCOUNT_PERIOD,
  a.INDEX_DIM_CODE,
  a.INDEX_DIM_DESCR,
  a.DIMENSION_FLAG,
  a.PERIOD_FLAG,
  ROUND(a.INDEX_VALUE/b.INDEX_VALUE,4) AS INDEX_VALUE,
  '-1',
  NOW()
FROM
  indicators_lib.lib_indicators_000204 a
LEFT JOIN 
   indicators_lib.lib_indicators_000230 b
  ON a.INDEX_DIM_CODE=b.INDEX_DIM_CODE
  AND a.ACCOUNT_PERIOD=b.ACCOUNT_PERIOD
WHERE a.ACCOUNT_PERIOD=DATE_FORMAT(v_data_date,'%Y-%m')

UNION ALL 
SELECT
  '570e9f50b60d11ea8bc1000c29587404',
  a.ACCOUNT_PERIOD,
  a.INDEX_DIM_CODE,
  a.INDEX_DIM_DESCR,
  a.DIMENSION_FLAG,
  a.PERIOD_FLAG,
  ROUND(a.INDEX_VALUE/b.INDEX_VALUE,4) AS INDEX_VALUE,
  '-1',
  NOW()
FROM
  indicators_lib.lib_indicators_000204 a
LEFT JOIN 
   indicators_lib.lib_indicators_000230 b
  ON a.INDEX_DIM_CODE=b.INDEX_DIM_CODE
  AND a.ACCOUNT_PERIOD=b.ACCOUNT_PERIOD
WHERE a.ACCOUNT_PERIOD=CONCAT(YEAR(v_data_date),'Q',QUARTER(v_data_date));

DELETE
FROM
  indicators_lib.lib_indicators_000250
WHERE (ACCOUNT_PERIOD = DATE_FORMAT(v_data_date, '%Y-%m')
 OR ACCOUNT_PERIOD = CONCAT(
      YEAR(v_data_date),
      'Q',
      QUARTER(v_data_date)
    ))
  AND INDEX_ID = 'e7906bc9b60d11ea8bc1000c29587404';
  
  
  INSERT INTO indicators_lib.lib_indicators_000250(INDEX_ID,ACCOUNT_PERIOD,INDEX_DIM_CODE,INDEX_DIM_DESCR,DIMENSION_FLAG,PERIOD_FLAG,INDEX_VALUE,ADD_USERID,ADD_DATE)
SELECT
  'e7906bc9b60d11ea8bc1000c29587404',
  a.ACCOUNT_PERIOD,
  a.INDEX_DIM_CODE,
  a.INDEX_DIM_DESCR,
  a.DIMENSION_FLAG,
  a.PERIOD_FLAG,
  ROUND(a.INDEX_VALUE/b.INDEX_VALUE,4) AS INDEX_VALUE,
  '-1',
  NOW()
FROM
  indicators_lib.lib_indicators_000206 a
LEFT JOIN 
   indicators_lib.lib_indicators_000228 b
  ON a.INDEX_DIM_CODE=b.INDEX_DIM_CODE
  AND a.ACCOUNT_PERIOD=b.ACCOUNT_PERIOD
WHERE a.ACCOUNT_PERIOD=DATE_FORMAT(v_data_date,'%Y-%m')

UNION ALL

SELECT
  'e7906bc9b60d11ea8bc1000c29587404',
  a.ACCOUNT_PERIOD,
  a.INDEX_DIM_CODE,
  a.INDEX_DIM_DESCR,
  a.DIMENSION_FLAG,
  a.PERIOD_FLAG,
  ROUND(a.INDEX_VALUE/b.INDEX_VALUE,4) AS INDEX_VALUE,
  '-1',
  NOW()
FROM
  indicators_lib.lib_indicators_000206 a
LEFT JOIN 
   indicators_lib.lib_indicators_000228 b
  ON a.INDEX_DIM_CODE=b.INDEX_DIM_CODE
  AND a.ACCOUNT_PERIOD=b.ACCOUNT_PERIOD
WHERE a.ACCOUNT_PERIOD=CONCAT(YEAR(v_data_date),'Q',QUARTER(v_data_date));


END;
