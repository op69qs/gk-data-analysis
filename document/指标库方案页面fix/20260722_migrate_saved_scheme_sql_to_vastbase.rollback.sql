\set ON_ERROR_STOP on

BEGIN;

DO $$
BEGIN
    IF to_regclass('indicators_lib.lib_index_scheme_sql_backup_20260722') IS NULL THEN
        RAISE EXCEPTION 'indicator-library SQL backup table does not exist';
    END IF;
    IF to_regclass('visual_screen.vs_lib_index_scheme_sql_backup_20260722') IS NULL THEN
        RAISE EXCEPTION 'VIS SQL backup table does not exist';
    END IF;
END
$$;

UPDATE indicators_lib.lib_index_scheme target
   SET scheme_sql = backup.scheme_sql
  FROM indicators_lib.lib_index_scheme_sql_backup_20260722 backup
 WHERE target.id = backup.id;

UPDATE visual_screen.vs_lib_index_scheme target
   SET scheme_sql = backup.scheme_sql
  FROM visual_screen.vs_lib_index_scheme_sql_backup_20260722 backup
 WHERE target.id = backup.id;

COMMIT;

SELECT 'indicators_lib.lib_index_scheme' AS source_table, COUNT(*) AS restored_rows
  FROM indicators_lib.lib_index_scheme target
  JOIN indicators_lib.lib_index_scheme_sql_backup_20260722 backup USING (id)
 WHERE target.scheme_sql = backup.scheme_sql
UNION ALL
SELECT 'visual_screen.vs_lib_index_scheme' AS source_table, COUNT(*) AS restored_rows
  FROM visual_screen.vs_lib_index_scheme target
  JOIN visual_screen.vs_lib_index_scheme_sql_backup_20260722 backup USING (id)
 WHERE target.scheme_sql = backup.scheme_sql;
