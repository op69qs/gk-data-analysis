SET search_path TO report, public;

DROP PROCEDURE IF EXISTS report.P_NEWS_FLASH_MONTH_TEXT_NUMBER;
CREATE PROCEDURE report.P_NEWS_FLASH_MONTH_TEXT_NUMBER(IN v_data_date VARCHAR(10))
BEGIN
    DECLARE con INT DEFAULT 0;
    DECLARE conn INT DEFAULT 0;
    DECLARE num1 INT DEFAULT 0;
    DECLARE num2 INT DEFAULT 0;
    DECLARE lib56 INT DEFAULT 0;
    DECLARE lib13 INT DEFAULT 0;
    DECLARE lib423 INT DEFAULT 0;
    DECLARE lib345 INT DEFAULT 0;
    DECLARE lib31802 INT DEFAULT 0;
    DECLARE qlib178 INT DEFAULT 0;

    SET con = COALESCE((
        SELECT MAX(prefix_len)
        FROM (
            SELECT
                rn AS prefix_len,
                sign_val,
                current_sign,
                SUM(CASE WHEN sign_val <> current_sign THEN 1 ELSE 0 END)
                    OVER (ORDER BY rn) AS break_cnt
            FROM (
                SELECT
                    ROW_NUMBER() OVER (ORDER BY ACCOUNT_PERIOD DESC) AS rn,
                    CASE
                        WHEN lib_000009 > 0 THEN 1
                        WHEN lib_000009 < 0 THEN -1
                        ELSE 0
                    END AS sign_val
                FROM report.news_flash_month_text
                WHERE CONCAT(ACCOUNT_PERIOD, '-01') <= v_data_date
            ) ordered_rows
            CROSS JOIN (
                SELECT CASE
                    WHEN lib_000009 > 0 THEN 1
                    WHEN lib_000009 < 0 THEN -1
                    ELSE 0
                END AS current_sign
                FROM report.news_flash_month_text
                WHERE ACCOUNT_PERIOD = DATE_FORMAT(v_data_date, '%Y-%m')
                LIMIT 1
            ) cur_sign
        ) streaks
        WHERE current_sign <> 0
          AND sign_val = current_sign
          AND break_cnt = 0
    ), 0);

    DELETE FROM report.news_flash_month_text_number
    WHERE ACCOUNT_PERIOD = DATE_FORMAT(v_data_date, '%Y-%m')
      AND INDEX_NAME = 'lib_000009';
    INSERT INTO report.news_flash_month_text_number
    VALUES (DATE_FORMAT(v_data_date, '%Y-%m'), 'lib_000009', con);

    SET conn = COALESCE((
        SELECT MAX(prefix_len)
        FROM (
            SELECT
                rn AS prefix_len,
                sign_val,
                current_sign,
                SUM(CASE WHEN sign_val <> current_sign THEN 1 ELSE 0 END)
                    OVER (ORDER BY rn) AS break_cnt
            FROM (
                SELECT
                    ROW_NUMBER() OVER (ORDER BY ACCOUNT_PERIOD DESC) AS rn,
                    CASE
                        WHEN lib_000188 > 0 THEN 1
                        WHEN lib_000188 < 0 THEN -1
                        ELSE 0
                    END AS sign_val
                FROM report.news_flash_month_text
                WHERE CONCAT(ACCOUNT_PERIOD, '-01') <= v_data_date
            ) ordered_rows
            CROSS JOIN (
                SELECT CASE
                    WHEN lib_000188 > 0 THEN 1
                    WHEN lib_000188 < 0 THEN -1
                    ELSE 0
                END AS current_sign
                FROM report.news_flash_month_text
                WHERE ACCOUNT_PERIOD = DATE_FORMAT(v_data_date, '%Y-%m')
                LIMIT 1
            ) cur_sign
        ) streaks
        WHERE current_sign <> 0
          AND sign_val = current_sign
          AND break_cnt = 0
    ), 0);

    DELETE FROM report.news_flash_month_text_number
    WHERE ACCOUNT_PERIOD = DATE_FORMAT(v_data_date, '%Y-%m')
      AND INDEX_NAME = 'lib_000188';
    INSERT INTO report.news_flash_month_text_number
    VALUES (DATE_FORMAT(v_data_date, '%Y-%m'), 'lib_000188', conn);

    SET num1 = COALESCE((
        SELECT MAX(PERIOD_DIFF(DATE_FORMAT(v_data_date, '%Y%m'), REPLACE(ACCOUNT_PERIOD, '-', '')))
        FROM (
            SELECT
                ACCOUNT_PERIOD,
                SUM(CASE WHEN lib_000056 < current_value THEN 1 ELSE 0 END)
                    OVER (ORDER BY ACCOUNT_PERIOD DESC) AS break_cnt
            FROM report.news_flash_month_text
            CROSS JOIN (
                SELECT lib_000056 AS current_value
                FROM report.news_flash_month_text
                WHERE ACCOUNT_PERIOD = DATE_FORMAT(v_data_date, '%Y-%m')
                LIMIT 1
            ) cur_val
            WHERE CONCAT(ACCOUNT_PERIOD, '-01') <= v_data_date
        ) streaks
        WHERE break_cnt = 0
    ), 0);

    DELETE FROM report.news_flash_month_text_number
    WHERE ACCOUNT_PERIOD = DATE_FORMAT(v_data_date, '%Y-%m')
      AND INDEX_NAME = 'lib_000056';
    INSERT INTO report.news_flash_month_text_number
    VALUES (DATE_FORMAT(v_data_date, '%Y-%m'), 'lib_000056', num1);

    SET num2 = COALESCE((
        SELECT MAX(FLOOR(PERIOD_DIFF(DATE_FORMAT(v_data_date, '%Y%m'), REPLACE(ACCOUNT_PERIOD, '-', '')) / 12))
        FROM (
            SELECT
                ACCOUNT_PERIOD,
                SUM(CASE WHEN lib_000178 < current_value THEN 1 ELSE 0 END)
                    OVER (ORDER BY ACCOUNT_PERIOD DESC) AS break_cnt
            FROM report.news_flash_month_text
            CROSS JOIN (
                SELECT lib_000178 AS current_value
                FROM report.news_flash_month_text
                WHERE ACCOUNT_PERIOD = DATE_FORMAT(v_data_date, '%Y-%m')
                LIMIT 1
            ) cur_val
            WHERE CONCAT(ACCOUNT_PERIOD, '-01') <= v_data_date
        ) streaks
        WHERE break_cnt = 0
    ), 0);

    DELETE FROM report.news_flash_month_text_number
    WHERE ACCOUNT_PERIOD = DATE_FORMAT(v_data_date, '%Y-%m')
      AND INDEX_NAME = 'lib_000178';
    INSERT INTO report.news_flash_month_text_number
    VALUES (DATE_FORMAT(v_data_date, '%Y-%m'), 'lib_000178', num2);

    SET lib56 = COALESCE((
        SELECT COUNT(*)
        FROM (
            SELECT
                ACCOUNT_PERIOD,
                SUM(CASE WHEN INDEX_VALUE < current_value THEN 1 ELSE 0 END)
                    OVER (ORDER BY ACCOUNT_PERIOD DESC) AS break_cnt
            FROM indicators_lib.lib_indicators_000056
            CROSS JOIN (
                SELECT INDEX_VALUE AS current_value
                FROM indicators_lib.lib_indicators_000056
                WHERE ACCOUNT_PERIOD = CONCAT(YEAR(v_data_date), 'Q', QUARTER(v_data_date))
                  AND PERIOD_FLAG = '3'
                  AND INDEX_DIM_CODE = '500000'
                LIMIT 1
            ) cur_val
            WHERE PERIOD_FLAG = '3'
              AND INDEX_DIM_CODE = '500000'
              AND ACCOUNT_PERIOD <= CONCAT(YEAR(v_data_date), 'Q', QUARTER(v_data_date))
        ) up_streak
        WHERE break_cnt = 0
    ), 0) + COALESCE((
        SELECT COUNT(*)
        FROM (
            SELECT
                ACCOUNT_PERIOD,
                SUM(CASE WHEN INDEX_VALUE > current_value THEN 1 ELSE 0 END)
                    OVER (ORDER BY ACCOUNT_PERIOD DESC) AS break_cnt
            FROM indicators_lib.lib_indicators_000056
            CROSS JOIN (
                SELECT INDEX_VALUE AS current_value
                FROM indicators_lib.lib_indicators_000056
                WHERE ACCOUNT_PERIOD = CONCAT(YEAR(v_data_date), 'Q', QUARTER(v_data_date))
                  AND PERIOD_FLAG = '3'
                  AND INDEX_DIM_CODE = '500000'
                LIMIT 1
            ) cur_val
            WHERE PERIOD_FLAG = '3'
              AND INDEX_DIM_CODE = '500000'
              AND ACCOUNT_PERIOD <= CONCAT(YEAR(v_data_date), 'Q', QUARTER(v_data_date))
        ) down_streak
        WHERE break_cnt = 0
    ), 0);

    DELETE FROM report.news_flash_month_text_number
    WHERE ACCOUNT_PERIOD = CONCAT(YEAR(v_data_date), 'Q', QUARTER(v_data_date))
      AND INDEX_NAME = 'lib_000056';
    INSERT INTO report.news_flash_month_text_number
    VALUES (CONCAT(YEAR(v_data_date), 'Q', QUARTER(v_data_date)), 'lib_000056', lib56);

    SET lib13 = COALESCE((
        SELECT COUNT(*)
        FROM (
            SELECT ACCOUNT_PERIOD,
                   SUM(CASE WHEN INDEX_VALUE < current_value THEN 1 ELSE 0 END)
                       OVER (ORDER BY ACCOUNT_PERIOD DESC) AS break_cnt
            FROM indicators_lib.lib_indicators_000013
            CROSS JOIN (
                SELECT INDEX_VALUE AS current_value
                FROM indicators_lib.lib_indicators_000013
                WHERE ACCOUNT_PERIOD = CONCAT(YEAR(v_data_date), 'Q', QUARTER(v_data_date))
                  AND PERIOD_FLAG = '3'
                  AND INDEX_DIM_CODE = '500000'
                LIMIT 1
            ) cur_val
            WHERE PERIOD_FLAG = '3'
              AND INDEX_DIM_CODE = '500000'
              AND ACCOUNT_PERIOD <= CONCAT(YEAR(v_data_date), 'Q', QUARTER(v_data_date))
        ) up_streak
        WHERE break_cnt = 0
    ), 0) + COALESCE((
        SELECT COUNT(*)
        FROM (
            SELECT ACCOUNT_PERIOD,
                   SUM(CASE WHEN INDEX_VALUE > current_value THEN 1 ELSE 0 END)
                       OVER (ORDER BY ACCOUNT_PERIOD DESC) AS break_cnt
            FROM indicators_lib.lib_indicators_000013
            CROSS JOIN (
                SELECT INDEX_VALUE AS current_value
                FROM indicators_lib.lib_indicators_000013
                WHERE ACCOUNT_PERIOD = CONCAT(YEAR(v_data_date), 'Q', QUARTER(v_data_date))
                  AND PERIOD_FLAG = '3'
                  AND INDEX_DIM_CODE = '500000'
                LIMIT 1
            ) cur_val
            WHERE PERIOD_FLAG = '3'
              AND INDEX_DIM_CODE = '500000'
              AND ACCOUNT_PERIOD <= CONCAT(YEAR(v_data_date), 'Q', QUARTER(v_data_date))
        ) down_streak
        WHERE break_cnt = 0
    ), 0);

    DELETE FROM report.news_flash_month_text_number
    WHERE ACCOUNT_PERIOD = CONCAT(YEAR(v_data_date), 'Q', QUARTER(v_data_date))
      AND INDEX_NAME = 'lib_000013';
    INSERT INTO report.news_flash_month_text_number
    VALUES (CONCAT(YEAR(v_data_date), 'Q', QUARTER(v_data_date)), 'lib_000013', lib13);

    SET lib423 = COALESCE((
        SELECT COUNT(*)
        FROM (
            SELECT ACCOUNT_PERIOD,
                   SUM(CASE WHEN INDEX_VALUE < current_value THEN 1 ELSE 0 END)
                       OVER (ORDER BY ACCOUNT_PERIOD DESC) AS break_cnt
            FROM indicators_lib.lib_indicators_000423
            CROSS JOIN (
                SELECT INDEX_VALUE AS current_value
                FROM indicators_lib.lib_indicators_000423
                WHERE ACCOUNT_PERIOD = CONCAT(YEAR(v_data_date), 'Q', QUARTER(v_data_date))
                  AND PERIOD_FLAG = '3'
                  AND INDEX_DIM_CODE = '500000'
                LIMIT 1
            ) cur_val
            WHERE PERIOD_FLAG = '3'
              AND INDEX_DIM_CODE = '500000'
              AND ACCOUNT_PERIOD <= CONCAT(YEAR(v_data_date), 'Q', QUARTER(v_data_date))
        ) up_streak
        WHERE break_cnt = 0
    ), 0) + COALESCE((
        SELECT COUNT(*)
        FROM (
            SELECT ACCOUNT_PERIOD,
                   SUM(CASE WHEN INDEX_VALUE > current_value THEN 1 ELSE 0 END)
                       OVER (ORDER BY ACCOUNT_PERIOD DESC) AS break_cnt
            FROM indicators_lib.lib_indicators_000423
            CROSS JOIN (
                SELECT INDEX_VALUE AS current_value
                FROM indicators_lib.lib_indicators_000423
                WHERE ACCOUNT_PERIOD = CONCAT(YEAR(v_data_date), 'Q', QUARTER(v_data_date))
                  AND PERIOD_FLAG = '3'
                  AND INDEX_DIM_CODE = '500000'
                LIMIT 1
            ) cur_val
            WHERE PERIOD_FLAG = '3'
              AND INDEX_DIM_CODE = '500000'
              AND ACCOUNT_PERIOD <= CONCAT(YEAR(v_data_date), 'Q', QUARTER(v_data_date))
        ) down_streak
        WHERE break_cnt = 0
    ), 0);

    DELETE FROM report.news_flash_month_text_number
    WHERE ACCOUNT_PERIOD = CONCAT(YEAR(v_data_date), 'Q', QUARTER(v_data_date))
      AND INDEX_NAME = 'lib_423';
    INSERT INTO report.news_flash_month_text_number
    VALUES (CONCAT(YEAR(v_data_date), 'Q', QUARTER(v_data_date)), 'lib_423', lib423);

    SET lib345 = COALESCE((
        SELECT COUNT(*)
        FROM (
            SELECT ACCOUNT_PERIOD,
                   SUM(CASE WHEN INDEX_VALUE < current_value THEN 1 ELSE 0 END)
                       OVER (ORDER BY ACCOUNT_PERIOD DESC) AS break_cnt
            FROM indicators_lib.lib_indicators_000345
            CROSS JOIN (
                SELECT INDEX_VALUE AS current_value
                FROM indicators_lib.lib_indicators_000345
                WHERE ACCOUNT_PERIOD = CONCAT(YEAR(v_data_date), 'Q', QUARTER(v_data_date))
                  AND PERIOD_FLAG = '3'
                  AND INDEX_DIM_CODE = '500000'
                LIMIT 1
            ) cur_val
            WHERE PERIOD_FLAG = '3'
              AND INDEX_DIM_CODE = '500000'
              AND ACCOUNT_PERIOD <= CONCAT(YEAR(v_data_date), 'Q', QUARTER(v_data_date))
        ) up_streak
        WHERE break_cnt = 0
    ), 0) + COALESCE((
        SELECT COUNT(*)
        FROM (
            SELECT ACCOUNT_PERIOD,
                   SUM(CASE WHEN INDEX_VALUE > current_value THEN 1 ELSE 0 END)
                       OVER (ORDER BY ACCOUNT_PERIOD DESC) AS break_cnt
            FROM indicators_lib.lib_indicators_000345
            CROSS JOIN (
                SELECT INDEX_VALUE AS current_value
                FROM indicators_lib.lib_indicators_000345
                WHERE ACCOUNT_PERIOD = CONCAT(YEAR(v_data_date), 'Q', QUARTER(v_data_date))
                  AND PERIOD_FLAG = '3'
                  AND INDEX_DIM_CODE = '500000'
                LIMIT 1
            ) cur_val
            WHERE PERIOD_FLAG = '3'
              AND INDEX_DIM_CODE = '500000'
              AND ACCOUNT_PERIOD <= CONCAT(YEAR(v_data_date), 'Q', QUARTER(v_data_date))
        ) down_streak
        WHERE break_cnt = 0
    ), 0);

    DELETE FROM report.news_flash_month_text_number
    WHERE ACCOUNT_PERIOD = CONCAT(YEAR(v_data_date), 'Q', QUARTER(v_data_date))
      AND INDEX_NAME = 'lib_345';
    INSERT INTO report.news_flash_month_text_number
    VALUES (CONCAT(YEAR(v_data_date), 'Q', QUARTER(v_data_date)), 'lib_345', lib345);

    SET lib31802 = COALESCE((
        SELECT COUNT(*)
        FROM (
            SELECT ACCOUNT_PERIOD,
                   SUM(CASE WHEN INDEX_VALUE < current_value THEN 1 ELSE 0 END)
                       OVER (ORDER BY ACCOUNT_PERIOD DESC) AS break_cnt
            FROM indicators_lib.lib_indicators_00031802
            CROSS JOIN (
                SELECT INDEX_VALUE AS current_value
                FROM indicators_lib.lib_indicators_00031802
                WHERE ACCOUNT_PERIOD = CONCAT(YEAR(v_data_date), 'Q', QUARTER(v_data_date))
                  AND PERIOD_FLAG = '3'
                  AND INDEX_DIM_CODE = '500000'
                LIMIT 1
            ) cur_val
            WHERE PERIOD_FLAG = '3'
              AND INDEX_DIM_CODE = '500000'
              AND ACCOUNT_PERIOD <= CONCAT(YEAR(v_data_date), 'Q', QUARTER(v_data_date))
        ) up_streak
        WHERE break_cnt = 0
    ), 0) + COALESCE((
        SELECT COUNT(*)
        FROM (
            SELECT ACCOUNT_PERIOD,
                   SUM(CASE WHEN INDEX_VALUE > current_value THEN 1 ELSE 0 END)
                       OVER (ORDER BY ACCOUNT_PERIOD DESC) AS break_cnt
            FROM indicators_lib.lib_indicators_00031802
            CROSS JOIN (
                SELECT INDEX_VALUE AS current_value
                FROM indicators_lib.lib_indicators_00031802
                WHERE ACCOUNT_PERIOD = CONCAT(YEAR(v_data_date), 'Q', QUARTER(v_data_date))
                  AND PERIOD_FLAG = '3'
                  AND INDEX_DIM_CODE = '500000'
                LIMIT 1
            ) cur_val
            WHERE PERIOD_FLAG = '3'
              AND INDEX_DIM_CODE = '500000'
              AND ACCOUNT_PERIOD <= CONCAT(YEAR(v_data_date), 'Q', QUARTER(v_data_date))
        ) down_streak
        WHERE break_cnt = 0
    ), 0);

    DELETE FROM report.news_flash_month_text_number
    WHERE ACCOUNT_PERIOD = CONCAT(YEAR(v_data_date), 'Q', QUARTER(v_data_date))
      AND INDEX_NAME = 'lib_31802';
    INSERT INTO report.news_flash_month_text_number
    VALUES (CONCAT(YEAR(v_data_date), 'Q', QUARTER(v_data_date)), 'lib_31802', lib31802);

    SET qlib178 = COALESCE((
        SELECT MAX(
            FLOOR(
                PERIOD_DIFF(
                    DATE_FORMAT(v_data_date, '%Y%m'),
                    CASE RIGHT(ACCOUNT_PERIOD, 1)
                        WHEN '1' THEN CONCAT(LEFT(ACCOUNT_PERIOD, 4), '01')
                        WHEN '2' THEN CONCAT(LEFT(ACCOUNT_PERIOD, 4), '04')
                        WHEN '3' THEN CONCAT(LEFT(ACCOUNT_PERIOD, 4), '07')
                        WHEN '4' THEN CONCAT(LEFT(ACCOUNT_PERIOD, 4), '10')
                    END
                ) / 12
            )
        )
        FROM (
            SELECT ACCOUNT_PERIOD,
                   SUM(CASE WHEN INDEX_VALUE < current_value THEN 1 ELSE 0 END)
                       OVER (ORDER BY ACCOUNT_PERIOD DESC) AS break_cnt
            FROM indicators_lib.lib_indicators_000178
            CROSS JOIN (
                SELECT INDEX_VALUE AS current_value
                FROM indicators_lib.lib_indicators_000178
                WHERE ACCOUNT_PERIOD = CONCAT(YEAR(v_data_date), 'Q', QUARTER(v_data_date))
                  AND PERIOD_FLAG = '3'
                  AND INDEX_DIM_CODE = '500000'
                LIMIT 1
            ) cur_val
            WHERE PERIOD_FLAG = '3'
              AND INDEX_DIM_CODE = '500000'
              AND ACCOUNT_PERIOD <= CONCAT(YEAR(v_data_date), 'Q', QUARTER(v_data_date))
        ) streaks
        WHERE break_cnt = 0
    ), 0);

    DELETE FROM report.news_flash_month_text_number
    WHERE ACCOUNT_PERIOD = CONCAT(YEAR(v_data_date), 'Q', QUARTER(v_data_date))
      AND INDEX_NAME = 'lib_000178';
    INSERT INTO report.news_flash_month_text_number
    VALUES (CONCAT(YEAR(v_data_date), 'Q', QUARTER(v_data_date)), 'lib_000178', qlib178);
END
;
