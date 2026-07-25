\set ON_ERROR_STOP on

BEGIN;

\ir saved_scheme_sql_vastbase_converter.sql

CREATE TABLE IF NOT EXISTS indicators_lib.lib_index_scheme_sql_backup_20260723 (
    id TEXT PRIMARY KEY,
    scheme_sql TEXT
);

CREATE TABLE IF NOT EXISTS visual_screen.vs_lib_index_scheme_sql_backup_20260723 (
    id TEXT PRIMARY KEY,
    scheme_sql TEXT
);

INSERT INTO indicators_lib.lib_index_scheme_sql_backup_20260723 (id, scheme_sql)
SELECT source.id, source.scheme_sql
  FROM indicators_lib.lib_index_scheme source
 WHERE NOT EXISTS (
       SELECT 1 FROM indicators_lib.lib_index_scheme_sql_backup_20260723 backup
        WHERE backup.id = source.id
 );

INSERT INTO visual_screen.vs_lib_index_scheme_sql_backup_20260723 (id, scheme_sql)
SELECT source.id, source.scheme_sql
  FROM visual_screen.vs_lib_index_scheme source
 WHERE NOT EXISTS (
       SELECT 1 FROM visual_screen.vs_lib_index_scheme_sql_backup_20260723 backup
        WHERE backup.id = source.id
 );

DO $$
DECLARE
    missing_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO missing_count
      FROM indicators_lib.lib_index_scheme source
      LEFT JOIN indicators_lib.lib_index_scheme_sql_backup_20260723 backup USING (id)
     WHERE backup.id IS NULL;
    IF missing_count <> 0 THEN
        RAISE EXCEPTION '% indicator-library SQL rows are not backed up', missing_count;
    END IF;

    SELECT COUNT(*) INTO missing_count
      FROM visual_screen.vs_lib_index_scheme source
      LEFT JOIN visual_screen.vs_lib_index_scheme_sql_backup_20260723 backup USING (id)
     WHERE backup.id IS NULL;
    IF missing_count <> 0 THEN
        RAISE EXCEPTION '% VIS SQL rows are not backed up', missing_count;
    END IF;
END
$$;

UPDATE indicators_lib.lib_index_scheme
   SET scheme_sql = pg_temp.to_vastbase_indicator_scheme_sql(scheme_sql);

UPDATE visual_screen.vs_lib_index_scheme vis
   SET scheme_sql = lib.scheme_sql
  FROM indicators_lib.lib_index_scheme lib
 WHERE vis.id = lib.id;

UPDATE visual_screen.vs_lib_index_scheme
   SET scheme_sql = pg_temp.to_vastbase_indicator_scheme_sql(scheme_sql);

DO $$
DECLARE
    item RECORD;
    invalid_count INTEGER;
    inconsistent_count INTEGER;
    checked_count INTEGER := 0;
BEGIN
    SELECT COUNT(*) INTO invalid_count
      FROM (
            SELECT scheme_sql FROM indicators_lib.lib_index_scheme
            UNION ALL
            SELECT scheme_sql FROM visual_screen.vs_lib_index_scheme
      ) saved
     WHERE scheme_sql ILIKE '%IFNULL(%'
        OR scheme_sql ILIKE '%SUM(IF(%'
        OR scheme_sql ILIKE '%edw.cm_guoku_dimnsn%'
        OR scheme_sql ILIKE '%authorized_guoku%'
        OR scheme_sql ILIKE '%"jeecg-boot-os".sys_user%'
        OR scheme_sql LIKE '%' || chr(96) || '%'
        OR scheme_sql LIKE '%aa.COLID,%';
    IF invalid_count <> 0 THEN
        RAISE EXCEPTION '% saved SQL rows still contain legacy syntax or creator scope', invalid_count;
    END IF;

    SELECT COUNT(*) INTO inconsistent_count
      FROM visual_screen.vs_lib_index_scheme vis
      JOIN indicators_lib.lib_index_scheme lib USING (id)
     WHERE vis.scheme_sql <> lib.scheme_sql;
    IF inconsistent_count <> 0 THEN
        RAISE EXCEPTION '% VIS SQL rows differ from their indicator-library source', inconsistent_count;
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
        EXECUTE 'SELECT * FROM (' || item.scheme_sql || ') verified_scheme LIMIT 0';
        checked_count := checked_count + 1;
    END LOOP;

    RAISE NOTICE 'PASS: backed up, migrated and validated % saved SQL rows', checked_count;
END
$$;

COMMIT;

SELECT 'indicators_lib.lib_index_scheme' AS source_table, COUNT(*) AS rows
  FROM indicators_lib.lib_index_scheme
UNION ALL
SELECT 'visual_screen.vs_lib_index_scheme' AS source_table, COUNT(*) AS rows
  FROM visual_screen.vs_lib_index_scheme;
