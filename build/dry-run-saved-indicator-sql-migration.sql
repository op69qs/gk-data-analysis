\set ON_ERROR_STOP on
\ir ../document/指标库方案页面fix/saved_scheme_sql_vastbase_converter.sql

DO $$
DECLARE
    item RECORD;
    converted_sql TEXT;
    checked_count INTEGER := 0;
BEGIN
    FOR item IN
        SELECT id, scheme_sql FROM indicators_lib.lib_index_scheme
    LOOP
        converted_sql := pg_temp.to_vastbase_indicator_scheme_sql(item.scheme_sql);

        IF converted_sql = item.scheme_sql THEN
            RAISE EXCEPTION 'scheme % was not converted', item.id;
        END IF;
        IF position('AS "ACCOUNT_DATE"' IN converted_sql) = 0
           OR position('AS "ACCOUNT_PERIOD"' IN converted_sql) = 0
           OR position('AS "CODE"' IN converted_sql) = 0
           OR position('AS "GK"' IN converted_sql) = 0 THEN
            RAISE EXCEPTION 'scheme % does not expose quoted uppercase fixed columns', item.id;
        END IF;

        EXECUTE 'SELECT * FROM (' || converted_sql || ') converted_scheme LIMIT 0';
        checked_count := checked_count + 1;
    END LOOP;

    RAISE NOTICE 'PASS: dry-run converted and planned % indicator-library SQL rows', checked_count;
END
$$;
