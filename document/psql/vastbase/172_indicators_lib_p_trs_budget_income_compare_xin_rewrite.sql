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
