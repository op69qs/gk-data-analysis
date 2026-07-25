\set ON_ERROR_STOP on

BEGIN;

DO $$
BEGIN
    IF to_regclass('indicators_lib.lib_index_scheme_sql_backup_20260723') IS NULL
       OR to_regclass('visual_screen.vs_lib_index_scheme_sql_backup_20260723') IS NULL THEN
        RAISE EXCEPTION '20260723 backup tables do not exist';
    END IF;
END
$$;

UPDATE indicators_lib.lib_index_scheme target
   SET scheme_sql = backup.scheme_sql
  FROM indicators_lib.lib_index_scheme_sql_backup_20260723 backup
 WHERE target.id = backup.id;

UPDATE visual_screen.vs_lib_index_scheme target
   SET scheme_sql = backup.scheme_sql
  FROM visual_screen.vs_lib_index_scheme_sql_backup_20260723 backup
 WHERE target.id = backup.id;

COMMIT;

SELECT 'rollback complete' AS result;
