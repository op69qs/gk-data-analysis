\set ON_ERROR_STOP on

DO $$
DECLARE
    item RECORD;
    legacy_count INTEGER;
    checked_count INTEGER := 0;
BEGIN
    SELECT COUNT(*)
      INTO legacy_count
      FROM (
            SELECT scheme_sql FROM indicators_lib.lib_index_scheme
            UNION ALL
            SELECT scheme_sql FROM visual_screen.vs_lib_index_scheme
      ) saved
     WHERE scheme_sql ILIKE '%IFNULL(%'
        OR scheme_sql ILIKE '%SUM(IF(%'
        OR scheme_sql LIKE '%' || chr(96) || '%'
        OR scheme_sql LIKE '%aa.COLID,%';

    IF legacy_count <> 0 THEN
        RAISE EXCEPTION '% saved indicator SQL rows still use legacy MySQL syntax', legacy_count;
    END IF;

    FOR item IN
        SELECT 'indicators_lib.lib_index_scheme' AS source_table, id, scheme_sql
          FROM indicators_lib.lib_index_scheme
        UNION ALL
        SELECT 'visual_screen.vs_lib_index_scheme' AS source_table, id, scheme_sql
          FROM visual_screen.vs_lib_index_scheme
    LOOP
        IF position('AS "ACCOUNT_DATE"' IN item.scheme_sql) = 0
           OR position('AS "ACCOUNT_PERIOD"' IN item.scheme_sql) = 0
           OR position('AS "CODE"' IN item.scheme_sql) = 0
           OR position('AS "GK"' IN item.scheme_sql) = 0 THEN
            RAISE EXCEPTION 'saved SQL %.% does not expose quoted uppercase fixed columns',
                item.source_table, item.id;
        END IF;

        EXECUTE 'SELECT * FROM (' || item.scheme_sql || ') saved_scheme LIMIT 0';
        checked_count := checked_count + 1;
    END LOOP;

    RAISE NOTICE 'PASS: validated % saved indicator SQL rows', checked_count;
END
$$;

DO $$
DECLARE
    inconsistent_count INTEGER;
BEGIN
    SELECT COUNT(*)
      INTO inconsistent_count
      FROM visual_screen.vs_lib_index_scheme vis
      JOIN indicators_lib.lib_index_scheme lib USING (id)
     WHERE vis.scheme_sql <> lib.scheme_sql;

    IF inconsistent_count <> 0 THEN
        RAISE EXCEPTION '% VIS SQL rows differ from their indicator-library source', inconsistent_count;
    END IF;
END
$$;
