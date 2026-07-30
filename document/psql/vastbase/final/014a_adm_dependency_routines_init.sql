-- ADM routine dependency closure adapted from the production MySQL routines.
-- Load after 013/014 and before 015_adm_routines_init.sql.

DROP PROCEDURE IF EXISTS edw.P_CM_SUST_UPDATE;

CREATE PROCEDURE edw.P_CM_SUST_UPDATE()
AS
BEGIN

TRUNCATE edw.cm_sust_equity_class;
TRUNCATE edw.cm_sust_crcs;
TRUNCATE edw.cm_sust_other_ways;
TRUNCATE edw.cm_sust_special_purpose;

INSERT INTO edw.cm_sust_equity_class
(
	EC_DSCR,
	TYPE
)
SELECT
	DISTINCT a.L2101 AS EC_DSCR,
	'A01'            AS TYPE
FROM ods.sust_source_of_funds_detailed_a01 a
UNION ALL
SELECT
	DISTINCT a.Y2101 AS EC_DSCR,
	'A02'            AS TYPE
FROM ods.sust_source_of_funds_detailed_a02 a;

INSERT INTO edw.cm_sust_crcs
(
	CRCS_DSCR,
	TYPE
)
SELECT
	DISTINCT a.L2102 AS CRCS_DSCR,
	'A01'            AS TYPE
FROM ods.sust_source_of_funds_detailed_a01 a
UNION ALL
SELECT
	DISTINCT a.Y2102 AS CRCS_DSCR,
	'A02'            AS TYPE
FROM ods.sust_source_of_funds_detailed_a02 a;

INSERT INTO edw.cm_sust_other_ways
(
	DSCR,
	TYPE
)
SELECT
	DISTINCT a.L2103 AS DSCR,
	'A01'            AS TYPE
FROM ods.sust_source_of_funds_detailed_a01 a;

INSERT INTO edw.cm_sust_special_purpose
(
	SP_DSCR,
	TYPE
)
SELECT
	DISTINCT a.Y2103 AS SP_DSCR,
	'A01'            AS TYPE
FROM ods.sust_source_of_funds_detailed_a02 a;
    END;
/

DROP PROCEDURE IF EXISTS edw.P_SUST_SCIENCE_TECHNOLOGY_AND_FINANCE;

CREATE PROCEDURE edw.P_SUST_SCIENCE_TECHNOLOGY_AND_FINANCE(in V_BATCH_DATE varchar(10))
AS
	DONE             INT := FALSE;
	V_DATA_DATE      VARCHAR(10);
	CURSOR CUR_D_ACCT IS 	SELECT DISTINCT DATA_DATE
	FROM ods.sust_science_technology_and_finance
	WHERE BATCH_DATE = V_BATCH_DATE;
BEGIN


    -- NOT FOUND handler replaced by explicit cursor %NOTFOUND checks below.

	OPEN CUR_D_ACCT;
	<<ENTLOOP>>
	LOOP
		FETCH CUR_D_ACCT INTO V_DATA_DATE;
		EXIT WHEN CUR_D_ACCT%NOTFOUND;
		IF DONE THEN
		EXIT ENTLOOP;
		END IF;

DELETE FROM edw.sust_science_technology_and_finance WHERE DATA_DATE = v_data_date;

INSERT INTO edw.sust_science_technology_and_finance
SELECT
	a.AREA_NO,
	a.DATA_DATE,
	a.TABLE_NATURE,
	a.ORG_NO,
	a.ADD_DATE,
	a.ROWS_ID,
	a.BATCH_DATE,
	a.SHEET_ID,
	a.KPINAME,
	a.BALANCE,
	a.PREVIOUSBALANCE,
	a.BALANCE-a.PREVIOUSBALANCE     				AS LAST_MONTH,
	ROUND((a.BALANCE-a.PREVIOUSBALANCE)/a.PREVIOUSBALANCE,2) 	AS CHAIN_RATIO,
	a.BALANCE-IFNULL(b.BALANCE,0)					AS OF_YEAR,
	ROUND((a.BALANCE-b.BALANCE)/b.BALANCE,2) 			AS OF_YEAR_RATE,
	a.CHECKTERM
FROM
	ods.sust_science_technology_and_finance a
LEFT JOIN ods.sust_science_technology_and_finance b
	ON DATE_FORMAT(CONCAT(a.DATA_DATE,'01'),'%Y-%m-%d') = DATE_FORMAT(CONCAT(b.DATA_DATE,'01'),'%Y-01-01')
	AND a.KPINAME = b.KPINAME
WHERE a.DATA_DATE = v_data_date;

DELETE FROM edw.sust_science_technology_and_finance_temp WHERE data_Date = v_data_date;

INSERT INTO edw.sust_science_technology_and_finance_temp
SELECT
	a.AREA_NO,
	a.DATA_DATE,
	a.TABLE_NATURE,
	'000000000000'									AS ORG_NO,
	a.ADD_DATE,
	a.ROWS_ID,
	a.BATCH_DATE,
	a.SHEET_ID,
	a.KPINAME,
	SUM(a.BALANCE) 									AS BALANCE,
	SUM(a.PREVIOUSBALANCE)								AS PREVIOUSBALANCE,
	SUM(a.BALANCE)-SUM(a.PREVIOUSBALANCE)     					AS LAST_MONTH,
	ROUND((SUM(a.BALANCE)-SUM(a.PREVIOUSBALANCE))/SUM(a.PREVIOUSBALANCE),2)		AS CHAIN_RATIO,
	SUM(a.BALANCE)-SUM(b.BALANCE)							AS OF_YEAR,
	ROUND((SUM(a.BALANCE)-SUM(b.BALANCE))/SUM(b.BALANCE),2)				AS OF_YEAR_RATE,
	a.CHECKTERM
FROM
	ods.sust_science_technology_and_finance a
LEFT JOIN ods.sust_science_technology_and_finance b
	ON DATE_FORMAT(CONCAT(a.DATA_DATE,'01'),'%Y-%m-%d') = DATE_FORMAT(CONCAT(b.DATA_DATE,'01'),'%Y-01-01')
	AND a.KPINAME = b.KPINAME
WHERE a.DATA_DATE = v_data_date
GROUP BY
	a.KPINAME;




UPDATE edw.sust_science_technology_and_finance_temp temp
SET temp.BALANCE = (
	SELECT
		ROUND(a.fenzi/b.fenmu,8)
	FROM
	(
		SELECT
			ROUND(SUM(aa.c),8) AS fenzi
		FROM
		(
		SELECT
			SUM(CASE WHEN a.ROWS_ID = '55' THEN a.BALANCE END)*
			SUM(CASE WHEN a.ROWS_ID = '58' THEN a.BALANCE END)/100    AS  c
		FROM ods.sust_science_technology_and_finance a
		WHERE a.DATA_DATE = v_data_date
		GROUP BY
			a.DATA_DATE
			,a.ORG_NO
		) aa
	) a
	,
	(
	SELECT
		SUM(CASE WHEN a.ROWS_ID = '55' THEN a.BALANCE END) AS  fenmu
	 FROM ods.sust_science_technology_and_finance a
	WHERE a.DATA_DATE = v_data_date
	GROUP BY
		a.DATA_DATE
	) b
),

temp.PREVIOUSBALANCE = (
	SELECT
		ROUND(a.fenzi/b.fenmu,8)
	FROM
	(
		SELECT
		ROUND(SUM(aa.c),8) AS fenzi
		FROM
		(
		SELECT
			SUM(CASE WHEN a.ROWS_ID = '55' THEN a.PREVIOUSBALANCE END)*
			SUM(CASE WHEN a.ROWS_ID = '58' THEN a.PREVIOUSBALANCE END)/100    AS  c
		 FROM ods.sust_science_technology_and_finance a
		WHERE a.DATA_DATE = v_data_date
		GROUP BY
		    a.DATA_DATE
		   ,a.ORG_NO
		) aa
	) a
	,
	(
	SELECT
		SUM(CASE WHEN a.ROWS_ID = '55' THEN a.PREVIOUSBALANCE END) AS  fenmu
	 FROM ods.sust_science_technology_and_finance a
	WHERE a.DATA_DATE = v_data_date
	GROUP BY
	    a.DATA_DATE
	) b
)
WHERE
	temp.DATA_DATE = v_data_date
	AND temp.ROWS_ID = '58';



UPDATE edw.sust_science_technology_and_finance_temp a
  JOIN edw.sust_science_technology_and_finance_temp b
    ON DATE_FORMAT(CONCAT(a.DATA_DATE,'01'),'%Y-%m-%d') = DATE_FORMAT(CONCAT(b.DATA_DATE,'01'),'%Y-01-01')
   AND a.KPINAME = b.KPINAME
SET a.LAST_MONTH = (a.BALANCE-a.PREVIOUSBALANCE),
    a.CHAIN_RATIO = round((a.BALANCE-a.PREVIOUSBALANCE)/a.PREVIOUSBALANCE,2),
    a.OF_YEAR = a.BALANCE-b.BALANCE,
    a.OF_YEAR_RATE = round((a.BALANCE-b.BALANCE)/b.BALANCE,2)
WHERE a.data_date = v_data_date
  AND a.ROWS_ID = '58';



UPDATE edw.sust_science_technology_and_finance_temp  a
SET a.BALANCE =
   (
	SELECT
	MIN(r.BALANCE)
	FROM edw.sust_science_technology_and_finance r
	WHERE DATA_DATE = V_DATA_DATE
	AND r.ROWS_ID = '59'
	AND r.ORG_NO <> '000000000000'
    )
WHERE A.DATA_DATE = V_DATA_DATE
AND a.ROWS_ID = '59';

UPDATE edw.sust_science_technology_and_finance_temp  a
SET a.BALANCE =
(
	SELECT
	MAX(r.BALANCE)
	FROM edw.sust_science_technology_and_finance r
	WHERE R.DATA_DATE = V_DATA_DATE
  AND	r.ROWS_ID = '60'
	AND r.ORG_NO <> '000000000000'
)
WHERE A.DATA_DATE = V_DATA_DATE
AND a.ROWS_ID = '60';

DELETE FROM edw.sust_science_technology_and_finance
WHERE DATA_DATE = v_data_date
and ORG_NO = '000000000000';

INSERT INTO edw.sust_science_technology_and_finance
SELECT * FROM edw.sust_science_technology_and_finance_temp
WHERE data_date = v_data_date;
	END LOOP ENTLOOP;
	CLOSE CUR_D_ACCT;
	END;
/

DROP PROCEDURE IF EXISTS edw.p_sust_source_of_funds_detailed_a01;

CREATE PROCEDURE edw.p_sust_source_of_funds_detailed_a01()
AS
  v_data_date char(8) := date_format(now(), '%Y%m%d');
BEGIN
  DELETE FROM edw.SUST_SOURCE_OF_FUNDS_DETAILED_A01
  WHERE DATA_DATE in
    (SELECT
      a.DATA_DATE
    FROM
      ods.SUST_SOURCE_OF_FUNDS_DETAILED_A01 a
    WHERE a.BATCH_DATE = v_data_date
    );
    commit;

   insert into edw.SUST_SOURCE_OF_FUNDS_DETAILED_A01
   select
   *
   from ods.SUST_SOURCE_OF_FUNDS_DETAILED_A01 b
   where b.DATA_DATE IN
    (SELECT
      t.DATA_DATE
    FROM
      ods.SUST_SOURCE_OF_FUNDS_DETAILED_A01 t
    WHERE t.BATCH_DATE = v_data_date
    );
    commit;
END;
/

DROP PROCEDURE IF EXISTS edw.P_SUST_SOURCE_OF_FUNDS_DETAILED_A02;

CREATE PROCEDURE edw.P_SUST_SOURCE_OF_FUNDS_DETAILED_A02()
AS
  v_data_date char(8) := date_format(now(), '%Y%m%d');
BEGIN
  DELETE FROM edw.SUST_SOURCE_OF_FUNDS_DETAILED_A02
  WHERE DATA_DATE in
    (SELECT
      a.DATA_DATE
    FROM
      ods.SUST_SOURCE_OF_FUNDS_DETAILED_A02 a
    WHERE a.BATCH_DATE = v_data_date
    );
    commit;

   insert into edw.SUST_SOURCE_OF_FUNDS_DETAILED_A02
   select
   *
   from ods.SUST_SOURCE_OF_FUNDS_DETAILED_A02 b
   where b.DATA_DATE IN
    (SELECT
      t.DATA_DATE
    FROM
      ods.SUST_SOURCE_OF_FUNDS_DETAILED_A02 t
    WHERE t.BATCH_DATE = v_data_date
    );
    commit;
END;
/

DROP PROCEDURE IF EXISTS edw.p_transDataFromOdsToEdw_main;

CREATE PROCEDURE edw.p_transDataFromOdsToEdw_main()
AS
  flag INT := FALSE;
  areaNo VARCHAR (8);
  dataDate CHAR (8);
  TABLE_NATURE VARCHAR (50);
  orgNo VARCHAR (53);
  ADD_DATE TIMESTAMP;
  rowsId VARCHAR (100);
  BATCH_DATE CHAR (8);
  SHEET_ID VARCHAR (100);
  OCCURRENCE_ORG VARCHAR (512);
  BWBDEPOSIT varchar (40);
  RMBDEPOSIT VARCHAR (40);
  SAVINGSDEPOSIT VARCHAR (40);
  BWBLOAN VARCHAR (40);
  RMBLOAN VARCHAR (40);
  SHORTTERMLOAN VARCHAR (40);
  MEDIUMLONGTERMLOAN VARCHAR (40);
  PERSONALMLTERMLOAN VARCHAR (40);
  CURSOR cur IS   SELECT
    *
  FROM
    ods.sust_main_indicators_of_institutions
  WHERE 1 = 1
  ;
BEGIN
    -- NOT FOUND handler replaced by explicit cursor %NOTFOUND checks below.
  OPEN cur;
  <<posLoop>>
  LOOP
    FETCH cur INTO areaNo,
    dataDate,
    TABLE_NATURE,
    orgNo,
    ADD_DATE,
    rowsId,
    BATCH_DATE,
    SHEET_ID,
    OCCURRENCE_ORG,
    BWBDEPOSIT,
    RMBDEPOSIT,
    SAVINGSDEPOSIT,
    BWBLOAN,
    RMBLOAN,
    SHORTTERMLOAN,
    MEDIUMLONGTERMLOAN,
    PERSONALMLTERMLOAN;
    EXIT WHEN cur%NOTFOUND;
    IF flag
    THEN EXIT posLoop;
    END IF;

    DELETE
    FROM
      edw.sust_main_indicators_of_institutions
    WHERE AREA_NO = areaNo
      AND ORG_NO = orgNo
      AND DATA_DATE = dataDate
      and ROWS_ID = rowsId;

    INSERT INTO edw.sust_main_indicators_of_institutions
    VALUES
      (
        areaNo,
        dataDate,
        TABLE_NATURE,
        orgNo,
        ADD_DATE,
        rowsId,
        BATCH_DATE,
        SHEET_ID,
        OCCURRENCE_ORG,
        BWBDEPOSIT,
        RMBDEPOSIT,
        SAVINGSDEPOSIT,
        BWBLOAN,
        RMBLOAN,
        SHORTTERMLOAN,
        MEDIUMLONGTERMLOAN,
        PERSONALMLTERMLOAN
      );
  END LOOP;
  CLOSE cur;
  COMMIT;
END;
/
