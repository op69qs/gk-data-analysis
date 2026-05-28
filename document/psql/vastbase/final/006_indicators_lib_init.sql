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
/
SET search_path TO indicators_lib, public;

DROP PROCEDURE IF EXISTS indicators_lib.P_TRS_BUDGET_INCOME_COMPARE_XIN;
CREATE PROCEDURE indicators_lib.P_TRS_BUDGET_INCOME_COMPARE_XIN(IN V_DATA_DATE VARCHAR(6))
BEGIN
    DELETE FROM edw.trs_budget_income_compare
    WHERE data_date IN (
        V_DATA_DATE,
        CONCAT(YEAR(CONCAT(V_DATA_DATE, '01')), 'Q', QUARTER(CONCAT(V_DATA_DATE, '01')))
    );

    INSERT INTO edw.trs_budget_income_compare
    WITH month_t010101 AS (
        SELECT
            a.data_date,
            CASE
                WHEN a.tredscr = '大连市' THEN '辽宁'
                WHEN a.tredscr = '青岛市' THEN '山东省'
                WHEN a.tredscr = '深圳市' THEN '广东省'
                WHEN a.tredscr = '宁波市' THEN '浙江省'
                WHEN a.tredscr = '厦门市' THEN '福建省'
                WHEN a.tredscr IN ('新疆兵团', '新疆区') THEN '新疆区'
                ELSE a.tredscr
            END AS project,
            (SUM(a.year_amt) - SUM(b.year_amt)) / NULLIF(SUM(b.year_amt), 0) AS t010101_rate,
            MAX(a.rows_id) AS rows_id
        FROM stg.trs_tmis_budget_income_provinces a
        LEFT JOIN stg.trs_tmis_budget_income_provinces b
          ON b.data_date = DATE_FORMAT(DATE_SUB(CONCAT(a.data_date, '01'), INTERVAL 1 YEAR), '%Y%m')
         AND a.trecode = b.trecode
         AND a.subject_code = b.subject_code
        WHERE a.data_date = V_DATA_DATE
          AND a.subject_code = 'T010101'
        GROUP BY
            a.data_date,
            CASE
                WHEN a.tredscr = '大连市' THEN '辽宁'
                WHEN a.tredscr = '青岛市' THEN '山东省'
                WHEN a.tredscr = '深圳市' THEN '广东省'
                WHEN a.tredscr = '宁波市' THEN '浙江省'
                WHEN a.tredscr = '厦门市' THEN '福建省'
                WHEN a.tredscr IN ('新疆兵团', '新疆区') THEN '新疆区'
                ELSE a.tredscr
            END
    ),
    month_101 AS (
        SELECT
            a.data_date,
            CASE
                WHEN a.tredscr = '大连市' THEN '辽宁'
                WHEN a.tredscr = '青岛市' THEN '山东省'
                WHEN a.tredscr = '深圳市' THEN '广东省'
                WHEN a.tredscr = '宁波市' THEN '浙江省'
                WHEN a.tredscr = '厦门市' THEN '福建省'
                WHEN a.tredscr IN ('新疆兵团', '新疆区') THEN '新疆区'
                ELSE a.tredscr
            END AS project,
            (SUM(a.year_amt) - SUM(b.year_amt)) / NULLIF(SUM(b.year_amt), 0) AS rate_101,
            MAX(a.rows_id) AS rows_id
        FROM stg.trs_tmis_budget_income_provinces a
        LEFT JOIN stg.trs_tmis_budget_income_provinces b
          ON b.data_date = DATE_FORMAT(DATE_SUB(CONCAT(a.data_date, '01'), INTERVAL 1 YEAR), '%Y%m')
         AND a.trecode = b.trecode
         AND a.subject_code = b.subject_code
        WHERE a.data_date = V_DATA_DATE
          AND a.subject_code = '101'
        GROUP BY
            a.data_date,
            CASE
                WHEN a.tredscr = '大连市' THEN '辽宁'
                WHEN a.tredscr = '青岛市' THEN '山东省'
                WHEN a.tredscr = '深圳市' THEN '广东省'
                WHEN a.tredscr = '宁波市' THEN '浙江省'
                WHEN a.tredscr = '厦门市' THEN '福建省'
                WHEN a.tredscr IN ('新疆兵团', '新疆区') THEN '新疆区'
                ELSE a.tredscr
            END
    ),
    month_union AS (
        SELECT
            data_date,
            project,
            t010101_rate,
            ROW_NUMBER() OVER (PARTITION BY data_date ORDER BY t010101_rate DESC NULLS LAST) AS t010101_rank,
            CAST(NULL AS DECIMAL(18, 6)) AS rate_101,
            CAST(NULL AS BIGINT) AS rank_101,
            rows_id
        FROM month_t010101

        UNION ALL

        SELECT
            data_date,
            project,
            CAST(NULL AS DECIMAL(18, 6)) AS t010101_rate,
            CAST(NULL AS BIGINT) AS t010101_rank,
            rate_101,
            ROW_NUMBER() OVER (PARTITION BY data_date ORDER BY rate_101 DESC NULLS LAST) AS rank_101,
            rows_id
        FROM month_101
    )
    SELECT
        data_date,
        project,
        ROUND(SUM(COALESCE(t010101_rate, 0)), 4),
        SUM(COALESCE(t010101_rank, 0)),
        ROUND(SUM(COALESCE(rate_101, 0)), 4),
        SUM(COALESCE(rank_101, 0)),
        MAX(rows_id)
    FROM month_union
    GROUP BY data_date, project;

    INSERT INTO edw.trs_budget_income_compare
    WITH quarter_t010101 AS (
        SELECT
            CONCAT(YEAR(CONCAT(a.data_date, '01')), 'Q', QUARTER(CONCAT(a.data_date, '01'))) AS data_date,
            CASE
                WHEN a.tredscr = '大连市' THEN '辽宁'
                WHEN a.tredscr = '青岛市' THEN '山东省'
                WHEN a.tredscr = '深圳市' THEN '广东省'
                WHEN a.tredscr = '宁波市' THEN '浙江省'
                WHEN a.tredscr = '厦门市' THEN '福建省'
                WHEN a.tredscr IN ('新疆兵团', '新疆区') THEN '新疆区'
                ELSE a.tredscr
            END AS project,
            (SUM(a.year_amt) - SUM(b.year_amt)) / NULLIF(SUM(b.year_amt), 0) AS t010101_rate,
            MAX(a.rows_id) AS rows_id
        FROM stg.trs_tmis_budget_income_provinces a
        LEFT JOIN stg.trs_tmis_budget_income_provinces b
          ON b.data_date = DATE_FORMAT(DATE_SUB(CONCAT(a.data_date, '01'), INTERVAL 1 YEAR), '%Y%m')
         AND a.trecode = b.trecode
         AND a.subject_code = b.subject_code
        WHERE a.data_date = V_DATA_DATE
          AND a.subject_code = 'T010101'
        GROUP BY
            CONCAT(YEAR(CONCAT(a.data_date, '01')), 'Q', QUARTER(CONCAT(a.data_date, '01'))),
            CASE
                WHEN a.tredscr = '大连市' THEN '辽宁'
                WHEN a.tredscr = '青岛市' THEN '山东省'
                WHEN a.tredscr = '深圳市' THEN '广东省'
                WHEN a.tredscr = '宁波市' THEN '浙江省'
                WHEN a.tredscr = '厦门市' THEN '福建省'
                WHEN a.tredscr IN ('新疆兵团', '新疆区') THEN '新疆区'
                ELSE a.tredscr
            END
    ),
    quarter_101 AS (
        SELECT
            CONCAT(YEAR(CONCAT(a.data_date, '01')), 'Q', QUARTER(CONCAT(a.data_date, '01'))) AS data_date,
            CASE
                WHEN a.tredscr = '大连市' THEN '辽宁'
                WHEN a.tredscr = '青岛市' THEN '山东省'
                WHEN a.tredscr = '深圳市' THEN '广东省'
                WHEN a.tredscr = '宁波市' THEN '浙江省'
                WHEN a.tredscr = '厦门市' THEN '福建省'
                WHEN a.tredscr IN ('新疆兵团', '新疆区') THEN '新疆区'
                ELSE a.tredscr
            END AS project,
            (SUM(a.year_amt) - SUM(b.year_amt)) / NULLIF(SUM(b.year_amt), 0) AS rate_101,
            MAX(a.rows_id) AS rows_id
        FROM stg.trs_tmis_budget_income_provinces a
        LEFT JOIN stg.trs_tmis_budget_income_provinces b
          ON b.data_date = DATE_FORMAT(DATE_SUB(CONCAT(a.data_date, '01'), INTERVAL 1 YEAR), '%Y%m')
         AND a.trecode = b.trecode
         AND a.subject_code = b.subject_code
        WHERE a.data_date = V_DATA_DATE
          AND a.subject_code = '101'
        GROUP BY
            CONCAT(YEAR(CONCAT(a.data_date, '01')), 'Q', QUARTER(CONCAT(a.data_date, '01'))),
            CASE
                WHEN a.tredscr = '大连市' THEN '辽宁'
                WHEN a.tredscr = '青岛市' THEN '山东省'
                WHEN a.tredscr = '深圳市' THEN '广东省'
                WHEN a.tredscr = '宁波市' THEN '浙江省'
                WHEN a.tredscr = '厦门市' THEN '福建省'
                WHEN a.tredscr IN ('新疆兵团', '新疆区') THEN '新疆区'
                ELSE a.tredscr
            END
    ),
    quarter_union AS (
        SELECT
            data_date,
            project,
            t010101_rate,
            ROW_NUMBER() OVER (PARTITION BY data_date ORDER BY t010101_rate DESC NULLS LAST) AS t010101_rank,
            CAST(NULL AS DECIMAL(18, 6)) AS rate_101,
            CAST(NULL AS BIGINT) AS rank_101,
            rows_id
        FROM quarter_t010101

        UNION ALL

        SELECT
            data_date,
            project,
            CAST(NULL AS DECIMAL(18, 6)) AS t010101_rate,
            CAST(NULL AS BIGINT) AS t010101_rank,
            rate_101,
            ROW_NUMBER() OVER (PARTITION BY data_date ORDER BY rate_101 DESC NULLS LAST) AS rank_101,
            rows_id
        FROM quarter_101
    )
    SELECT
        data_date,
        project,
        ROUND(SUM(COALESCE(t010101_rate, 0)), 4),
        SUM(COALESCE(t010101_rank, 0)),
        ROUND(SUM(COALESCE(rate_101, 0)), 4),
        SUM(COALESCE(rank_101, 0)),
        MAX(rows_id)
    FROM quarter_union
    GROUP BY data_date, project;
END
;
/
SET search_path TO indicators_lib, public;

DROP PROCEDURE IF EXISTS indicators_lib.p_exe_formula_hand;
CREATE PROCEDURE indicators_lib.p_exe_formula_hand(IN v_data_date VARCHAR(20), IN v_index_id VARCHAR(32))
BEGIN
    DECLARE v_proc_name VARCHAR(80) DEFAULT 'INDICATORS_LIB.P_EXE_FORMULA_HAND.PRC';
    DECLARE v_start_time CHAR(19) DEFAULT NOW();
    DECLARE v_step_id INT DEFAULT 0;
    DECLARE p_data_date VARCHAR(8) DEFAULT DATE_FORMAT(v_data_date, '%Y-%m');
    DECLARE index_id VARCHAR(32);
    DECLARE table_name VARCHAR(200);
    DECLARE delete_sql TEXT;
    DECLARE sesin TEXT;
    DECLARE insert_str TEXT;
    DECLARE where_str TEXT;
    DECLARE delete_exec TEXT;
    DECLARE insert_exec TEXT;
    DECLARE quoted_data_date TEXT;
    DECLARE quoted_last_date TEXT;
    DECLARE quoted_id TEXT;
    DECLARE v_return_code TEXT;
    DECLARE v_error_msg TEXT;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        GET DIAGNOSTICS CONDITION 1 v_return_code = RETURNED_SQLSTATE, v_error_msg = MESSAGE_TEXT;
        CALL ETL.EDW_PROC_ERROR_LOG(p_data_date, v_start_time, NOW(), v_proc_name, v_step_id, v_return_code, v_error_msg);
    END;

    SELECT
        id,
        index_corre_table,
        CAST(exe_del AS TEXT),
        CAST(exe_insert AS TEXT),
        CAST(exe_sql AS TEXT),
        CAST(exe_where AS TEXT)
    INTO index_id, table_name, delete_sql, insert_str, sesin, where_str
    FROM indicators_lib.lib_index_formula
    WHERE id = v_index_id
    LIMIT 1;

    SET quoted_data_date = CONCAT('''', DATE_FORMAT(v_data_date, '%Y-%m-%d'), '''');
    SET quoted_last_date = CONCAT('''', DATE_FORMAT(DATE_SUB(v_data_date, INTERVAL 1 YEAR), '%Y-%m-%d'), '''');
    SET quoted_id = CONCAT('''', v_index_id, '''');

    SET delete_exec = COALESCE(delete_sql, 'SELECT 1');
    SET delete_exec = REPLACE(delete_exec, '@LAST_DATE', quoted_last_date);
    SET delete_exec = REPLACE(delete_exec, '@DATA_DATE', quoted_data_date);
    SET delete_exec = REPLACE(delete_exec, '@data_date', quoted_data_date);
    SET delete_exec = REPLACE(delete_exec, '@ID', quoted_id);

    SET insert_exec = COALESCE(CONCAT_WS('', insert_str, sesin, where_str), 'SELECT 1');
    SET insert_exec = REPLACE(insert_exec, '@LAST_DATE', quoted_last_date);
    SET insert_exec = REPLACE(insert_exec, '@DATA_DATE', quoted_data_date);
    SET insert_exec = REPLACE(insert_exec, '@data_date', quoted_data_date);
    SET insert_exec = REPLACE(insert_exec, '@ID', quoted_id);

    SET v_step_id = 1;
    EXECUTE IMMEDIATE delete_exec;
    CALL ETL.EDW_PROC_TRACE_LOG(p_data_date, v_start_time, NOW(), v_proc_name, v_step_id, ROW_COUNT());

    SET v_step_id = 2;
    EXECUTE IMMEDIATE insert_exec;
    CALL ETL.EDW_PROC_TRACE_LOG(p_data_date, v_start_time, NOW(), v_proc_name, v_step_id, ROW_COUNT());
END
;
/

DROP PROCEDURE IF EXISTS indicators_lib.p_exe_formula;
CREATE PROCEDURE indicators_lib.p_exe_formula(IN V_DATA_DATE VARCHAR(20))
BEGIN
    DECLARE v_sql TEXT DEFAULT '';

    SET v_sql = COALESCE((
        SELECT string_agg(stmt, ' ')
        FROM (
            SELECT CONCAT(
                'CALL indicators_lib.p_exe_formula_hand(''',
                DATE_FORMAT(V_DATA_DATE, '%Y-%m-%d'),
                ''',''',
                id,
                ''');'
            ) AS stmt
            FROM indicators_lib.lib_index_formula
            ORDER BY identity_property
        ) call_queue
    ), 'SELECT 1');

    EXECUTE IMMEDIATE v_sql;
END
;
/

DROP PROCEDURE IF EXISTS indicators_lib.init_report01;
CREATE PROCEDURE indicators_lib.init_report01(IN v_data_date VARCHAR(10))
BEGIN
    CALL indicators_lib.p_exe_formula(DATE_FORMAT(v_data_date, '%Y%m%d'));
END
;
/

DROP PROCEDURE IF EXISTS indicators_lib.init_report03;
CREATE PROCEDURE indicators_lib.init_report03(IN v_data_date VARCHAR(10))
BEGIN
    CALL indicators_lib.p_exe_formula_hand(v_data_date, '7a9dc86298ce11eab404000c298a21af');
    CALL indicators_lib.p_exe_formula_hand(v_data_date, '7a9dc7a698ce11eab404000c298a21af');
    CALL indicators_lib.p_exe_formula_hand(v_data_date, 'e34ac17098ce11eab404000c298a21af');
    CALL indicators_lib.p_exe_formula_hand(v_data_date, 'e34ac13e98ce11eab404000c298a21af');
    CALL indicators_lib.p_exe_formula_hand(v_data_date, '696fc66d98ce11eab404000c298a21af');
    CALL indicators_lib.p_exe_formula_hand(v_data_date, 'e34ac11198ce11eab404000c298a21af');
    CALL indicators_lib.p_exe_formula_hand(v_data_date, 'e34ac0dd98ce11eab404000c298a21af');
    CALL indicators_lib.p_exe_formula_hand(v_data_date, '696fc6a598ce11eab404000c298a21af');
    CALL indicators_lib.P_TRS_BUDGET_INCOME_COMPARE_XIN(DATE_FORMAT(v_data_date, '%Y%m'));
END
;
/

DROP PROCEDURE IF EXISTS indicators_lib.p_xunhuan_formula;
CREATE PROCEDURE indicators_lib.p_xunhuan_formula(IN v_batch_date VARCHAR(10))
BEGIN
    DECLARE v_proc_name VARCHAR(80) DEFAULT 'INDICATORS_LIB.P_XUNHUAN_FORMULA.PRC';
    DECLARE v_start_time CHAR(19) DEFAULT NOW();
    DECLARE v_step_id INT DEFAULT 0;
    DECLARE p_data_date VARCHAR(8) DEFAULT DATE_FORMAT(v_batch_date, '%Y-%m');
    DECLARE v_daily_sql TEXT DEFAULT '';
    DECLARE v_month_sql TEXT DEFAULT '';
    DECLARE v_return_code TEXT;
    DECLARE v_error_msg TEXT;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        GET DIAGNOSTICS CONDITION 1 v_return_code = RETURNED_SQLSTATE, v_error_msg = MESSAGE_TEXT;
        CALL ETL.EDW_PROC_ERROR_LOG(p_data_date, v_start_time, NOW(), v_proc_name, v_step_id, v_return_code, v_error_msg);
    END;

    SET v_step_id = 1;
    UPDATE STG.TRS_TMIS_BUDGET_INCOME
    SET D_ACCT = CASE
        WHEN LENGTH(D_ACCT) = 6 THEN DATE_FORMAT(CONCAT(D_ACCT, '01'), '%Y-%m-%d')
        ELSE DATE_FORMAT(D_ACCT, '%Y-%m-%d')
    END;

    SET v_step_id = 2;
    UPDATE STG.TRS_TMIS_BUDGET_PAYOUT
    SET D_ACCT = CASE
        WHEN LENGTH(D_ACCT) = 6 THEN DATE_FORMAT(CONCAT(D_ACCT, '01'), '%Y-%m-%d')
        ELSE DATE_FORMAT(D_ACCT, '%Y-%m-%d')
    END;

    SET v_step_id = 3;
    UPDATE STG.TRS_TMIS_STOCK
    SET TRECODE = '2200000000',
        TREDSCR = '国家金库重庆市分库'
    WHERE TRECODE = 'NNNNNNNNNN';

    SET v_step_id = 4;
    CALL edw.P_TRS_BUDGET_INCOME_COMPARE(DATE_FORMAT(v_batch_date, '%Y%m%d'));
    CALL indicators_lib.P_TRS_BUDGET_INCOME_COMPARE_XIN(DATE_FORMAT(v_batch_date, '%Y%m'));

    SET v_step_id = 5;
    SET v_daily_sql = COALESCE((
        SELECT string_agg(stmt, ' ')
        FROM (
            SELECT CONCAT(
                'CALL indicators_lib.p_exe_formula_hand(''',
                d_acct,
                ''',''e34ac06898ce11eab404000c298a21af''); ',
                CASE
                    WHEN d_acct = CONCAT(DATE_FORMAT(d_acct, '%Y-%m'), '-01') THEN CONCAT(
                        'CALL indicators_lib.p_exe_formula(''', d_acct, '''); ',
                        'CALL report.P_QUARTER_REPORT_TEXT(''', CONCAT(YEAR(d_acct), 'Q', QUARTER(d_acct)), '''); ',
                        'CALL report.P_NEWS_FLASH_QUARTER_REPORT_TEXT(''', CONCAT(YEAR(d_acct), 'Q', QUARTER(d_acct)), '''); '
                    )
                    ELSE ''
                END
            ) AS stmt
            FROM (
                SELECT DISTINCT
                    IF(
                        DATE_FORMAT(d_acct, '%Y-%m-%d') IS NULL,
                        DATE_FORMAT(CONCAT(d_acct, '01'), '%Y-%m-%d'),
                        DATE_FORMAT(d_acct, '%Y-%m-%d')
                    ) AS d_acct
                FROM (
                    SELECT D_ACCT
                    FROM STG.TRS_TMIS_BUDGET_INCOME
                    WHERE BATCH_DATE = v_batch_date

                    UNION ALL

                    SELECT D_ACCT
                    FROM STG.TRS_TMIS_BUDGET_PAYOUT
                    WHERE BATCH_DATE = v_batch_date

                    UNION ALL

                    SELECT D_ACCT
                    FROM STG.TRS_TMIS_STOCK
                    WHERE BATCH_DATE = v_batch_date
                ) raw_dates
            ) distinct_dates
            ORDER BY d_acct
        ) call_queue
    ), 'SELECT 1');
    EXECUTE IMMEDIATE v_daily_sql;

    SET v_step_id = 6;
    SET v_month_sql = COALESCE((
        SELECT string_agg(stmt, ' ')
        FROM (
            SELECT CONCAT(
                'CALL report.P_MONTH_REPORT_TEXT(''', d_acct, '''); ',
                'CALL report.P_NEWS_FLASH_MONTH_REPORT_TEXT(''', d_acct, '''); ',
                'CALL report.P_NEWS_FLASH_MONTH_TEXT_NUMBER(''', CONCAT(d_acct, '-01'), '''); '
            ) AS stmt
            FROM (
                SELECT DISTINCT
                    IF(
                        DATE_FORMAT(d_acct, '%Y-%m-%d') IS NULL,
                        DATE_FORMAT(CONCAT(d_acct, '01'), '%Y-%m'),
                        DATE_FORMAT(d_acct, '%Y-%m')
                    ) AS d_acct
                FROM (
                    SELECT D_ACCT
                    FROM STG.TRS_TMIS_BUDGET_INCOME
                    WHERE BATCH_DATE = v_batch_date

                    UNION ALL

                    SELECT D_ACCT
                    FROM STG.TRS_TMIS_BUDGET_PAYOUT
                    WHERE BATCH_DATE = v_batch_date

                    UNION ALL

                    SELECT D_ACCT
                    FROM STG.TRS_TMIS_STOCK
                    WHERE BATCH_DATE = v_batch_date
                ) raw_dates
            ) distinct_months
            ORDER BY d_acct
        ) call_queue
    ), 'SELECT 1');
    EXECUTE IMMEDIATE v_month_sql;
END
;
/
