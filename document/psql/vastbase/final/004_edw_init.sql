SET search_path TO edw, public;

DROP PROCEDURE IF EXISTS edw.P_TRS_BUDGET_INCOME_COMPARE;
CREATE PROCEDURE edw.P_TRS_BUDGET_INCOME_COMPARE(IN P_DATA_DATE VARCHAR(10))
AS
BEGIN
    DELETE FROM edw.trs_budget_income_compare
    WHERE data_date IN (
        SELECT data_date
        FROM (
            SELECT DISTINCT data_date
            FROM stg.trs_tmis_budget_income_provinces
            WHERE batch_date = P_DATA_DATE

            UNION

            SELECT CONCAT(YEAR(CONCAT(data_date, '01')), 'Q', QUARTER(CONCAT(data_date, '01')))
            FROM (
                SELECT DISTINCT data_date
                FROM stg.trs_tmis_budget_income_provinces
                WHERE batch_date = P_DATA_DATE
            ) src_dates
        ) targets
    );

    INSERT INTO edw.trs_budget_income_compare
    WITH monthly_dates AS (
        SELECT DISTINCT data_date
        FROM stg.trs_tmis_budget_income_provinces
        WHERE batch_date = P_DATA_DATE
    ),
    monthly_t010101 AS (
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
        FROM monthly_dates d
        JOIN stg.trs_tmis_budget_income_provinces a
          ON a.data_date = d.data_date
         AND a.subject_code = 'T010101'
        LEFT JOIN stg.trs_tmis_budget_income_provinces b
          ON b.data_date = DATE_FORMAT(DATE_SUB(CONCAT(a.data_date, '01'), INTERVAL 1 YEAR), '%Y%m')
         AND a.trecode = b.trecode
         AND a.subject_code = b.subject_code
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
    monthly_101 AS (
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
        FROM monthly_dates d
        JOIN stg.trs_tmis_budget_income_provinces a
          ON a.data_date = d.data_date
         AND a.subject_code = '101'
        LEFT JOIN stg.trs_tmis_budget_income_provinces b
          ON b.data_date = DATE_FORMAT(DATE_SUB(CONCAT(a.data_date, '01'), INTERVAL 1 YEAR), '%Y%m')
         AND a.trecode = b.trecode
         AND a.subject_code = b.subject_code
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
    monthly_union AS (
        SELECT
            data_date,
            project,
            t010101_rate,
            ROW_NUMBER() OVER (PARTITION BY data_date ORDER BY t010101_rate DESC NULLS LAST) AS t010101_rank,
            CAST(NULL AS DECIMAL(18, 6)) AS rate_101,
            CAST(NULL AS BIGINT) AS rank_101,
            rows_id
        FROM monthly_t010101

        UNION ALL

        SELECT
            data_date,
            project,
            CAST(NULL AS DECIMAL(18, 6)) AS t010101_rate,
            CAST(NULL AS BIGINT) AS t010101_rank,
            rate_101,
            ROW_NUMBER() OVER (PARTITION BY data_date ORDER BY rate_101 DESC NULLS LAST) AS rank_101,
            rows_id
        FROM monthly_101
    )
    SELECT
        data_date,
        project,
        ROUND(SUM(COALESCE(t010101_rate, 0)), 4),
        SUM(COALESCE(t010101_rank, 0)),
        ROUND(SUM(COALESCE(rate_101, 0)), 4),
        SUM(COALESCE(rank_101, 0)),
        MAX(rows_id)
    FROM monthly_union
    GROUP BY data_date, project;

    INSERT INTO edw.trs_budget_income_compare
    WITH monthly_dates AS (
        SELECT DISTINCT data_date
        FROM stg.trs_tmis_budget_income_provinces
        WHERE batch_date = P_DATA_DATE
    ),
    quarter_dates AS (
        SELECT quarter_label, data_date
        FROM (
            SELECT
                data_date,
                CONCAT(YEAR(CONCAT(data_date, '01')), 'Q', QUARTER(CONCAT(data_date, '01'))) AS quarter_label,
                ROW_NUMBER() OVER (
                    PARTITION BY CONCAT(YEAR(CONCAT(data_date, '01')), 'Q', QUARTER(CONCAT(data_date, '01')))
                    ORDER BY data_date DESC
                ) AS rn
            FROM monthly_dates
        ) ranked_quarters
        WHERE rn = 1
    ),
    quarter_t010101 AS (
        SELECT
            q.quarter_label AS data_date,
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
        FROM quarter_dates q
        JOIN stg.trs_tmis_budget_income_provinces a
          ON a.data_date = q.data_date
         AND a.subject_code = 'T010101'
        LEFT JOIN stg.trs_tmis_budget_income_provinces b
          ON b.data_date = DATE_FORMAT(DATE_SUB(CONCAT(a.data_date, '01'), INTERVAL 1 YEAR), '%Y%m')
         AND a.trecode = b.trecode
         AND a.subject_code = b.subject_code
        GROUP BY
            q.quarter_label,
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
            q.quarter_label AS data_date,
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
        FROM quarter_dates q
        JOIN stg.trs_tmis_budget_income_provinces a
          ON a.data_date = q.data_date
         AND a.subject_code = '101'
        LEFT JOIN stg.trs_tmis_budget_income_provinces b
          ON b.data_date = DATE_FORMAT(DATE_SUB(CONCAT(a.data_date, '01'), INTERVAL 1 YEAR), '%Y%m')
         AND a.trecode = b.trecode
         AND a.subject_code = b.subject_code
        GROUP BY
            q.quarter_label,
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
