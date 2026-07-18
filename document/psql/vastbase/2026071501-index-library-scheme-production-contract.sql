-- Index-library scheme production database contract for Vastbase.
-- This script fails closed when a required production table is absent.
-- It never creates a business table or changes an existing column type.

\set ON_ERROR_STOP on

SELECT 'BEFORE: required production tables' AS checkpoint;
SELECT table_schema, table_name
FROM information_schema.tables
WHERE (table_schema = 'visual_screen'
       AND table_name IN ('vs_lib_index_scheme', 'vs_gallery_info', 'sys_user'))
   OR (table_schema = 'indicators_lib'
       AND table_name = 'lib_index_relation')
ORDER BY table_schema, table_name;

SELECT 'BEFORE: gallery production columns' AS checkpoint;
SELECT column_name, data_type, character_maximum_length, is_nullable
FROM information_schema.columns
WHERE table_schema = 'visual_screen'
  AND table_name = 'vs_gallery_info'
  AND column_name IN (
      'id', 'option', 'query_path', 'content', 'type', 'title', 'sort', 'state',
      'business_id', 'time_type', 'dimension_type', 'dacct_radio', 'title_old',
      'add_time', 'add_user', 'index_scheme_id', 'index_scheme_name', 'condition')
ORDER BY ordinal_position;

SELECT 'BEFORE: index-name routine' AS checkpoint;
SELECT routine_schema, routine_name, data_type
FROM information_schema.routines
WHERE routine_schema = 'visual_screen'
  AND lower(routine_name) = lower('f_get_IndexName')
ORDER BY routine_name;

DO $contract$
DECLARE
    required_table record;
BEGIN
    FOR required_table IN
        SELECT required.table_schema, required.table_name
        FROM (VALUES
            ('visual_screen', 'vs_lib_index_scheme'),
            ('visual_screen', 'vs_gallery_info'),
            ('visual_screen', 'sys_user'),
            ('indicators_lib', 'lib_index_relation')
        ) AS required(table_schema, table_name)
    LOOP
        IF NOT EXISTS (
            SELECT 1
            FROM information_schema.tables actual
            WHERE actual.table_schema = required_table.table_schema
              AND actual.table_name = required_table.table_name
        ) THEN
            RAISE EXCEPTION 'missing required production table: %',
                required_table.table_schema || '.' || required_table.table_name;
        END IF;
    END LOOP;
END;
$contract$ LANGUAGE plpgsql;

-- The checked production Vastbase structure stores these gallery values as text.
-- This Vastbase release does not accept ADD COLUMN IF NOT EXISTS, so each DDL
-- statement is guarded through information_schema.columns instead.
DO $gallery_columns$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns actual
        WHERE actual.table_schema = 'visual_screen'
          AND actual.table_name = 'vs_gallery_info'
          AND actual.column_name = 'dacct_radio'
    ) THEN
        EXECUTE 'ALTER TABLE visual_screen.vs_gallery_info ADD COLUMN dacct_radio text';
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns actual
        WHERE actual.table_schema = 'visual_screen'
          AND actual.table_name = 'vs_gallery_info'
          AND actual.column_name = 'title_old'
    ) THEN
        EXECUTE 'ALTER TABLE visual_screen.vs_gallery_info ADD COLUMN title_old text';
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns actual
        WHERE actual.table_schema = 'visual_screen'
          AND actual.table_name = 'vs_gallery_info'
          AND actual.column_name = 'add_time'
    ) THEN
        EXECUTE 'ALTER TABLE visual_screen.vs_gallery_info ADD COLUMN add_time text';
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns actual
        WHERE actual.table_schema = 'visual_screen'
          AND actual.table_name = 'vs_gallery_info'
          AND actual.column_name = 'add_user'
    ) THEN
        EXECUTE 'ALTER TABLE visual_screen.vs_gallery_info ADD COLUMN add_user text';
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns actual
        WHERE actual.table_schema = 'visual_screen'
          AND actual.table_name = 'vs_gallery_info'
          AND actual.column_name = 'index_scheme_id'
    ) THEN
        EXECUTE 'ALTER TABLE visual_screen.vs_gallery_info ADD COLUMN index_scheme_id text';
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns actual
        WHERE actual.table_schema = 'visual_screen'
          AND actual.table_name = 'vs_gallery_info'
          AND actual.column_name = 'index_scheme_name'
    ) THEN
        EXECUTE 'ALTER TABLE visual_screen.vs_gallery_info ADD COLUMN index_scheme_name text';
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns actual
        WHERE actual.table_schema = 'visual_screen'
          AND actual.table_name = 'vs_gallery_info'
          AND actual.column_name = 'condition'
    ) THEN
        EXECUTE 'ALTER TABLE visual_screen.vs_gallery_info ADD COLUMN condition text';
    END IF;
END;
$gallery_columns$ LANGUAGE plpgsql;

-- Production MySQL semantics retained where material:
--   * names follow the first position of each comma-delimited index ID;
--   * repeated IDs in scheme_columns do not repeat the relation row;
--   * empty/null input and no matches return an empty string.
CREATE OR REPLACE FUNCTION visual_screen.f_get_IndexName(
    scheme_columns varchar(1000))
RETURNS varchar(2000)
AS $function$
DECLARE
    index_names varchar(2000);
BEGIN
    SELECT CAST(
        COALESCE(
            string_agg(
                r.index_name,
                ',' ORDER BY
                    strpos(
                        ',' || scheme_columns || ',',
                        ',' || r.index_id || ','),
                    r.index_id),
            '')
        AS varchar(2000))
    INTO index_names
    FROM indicators_lib.lib_index_relation r
    WHERE scheme_columns IS NOT NULL
      AND btrim(scheme_columns) <> ''
      AND r.index_id IS NOT NULL
      AND btrim(r.index_id) <> ''
      AND strpos(
          ',' || scheme_columns || ',',
          ',' || r.index_id || ',') > 0;

    RETURN index_names;
END;
$function$
LANGUAGE plpgsql
STABLE;

SELECT 'AFTER: required production tables' AS checkpoint;
SELECT table_schema, table_name
FROM information_schema.tables
WHERE (table_schema = 'visual_screen'
       AND table_name IN ('vs_lib_index_scheme', 'vs_gallery_info', 'sys_user'))
   OR (table_schema = 'indicators_lib'
       AND table_name = 'lib_index_relation')
ORDER BY table_schema, table_name;

SELECT 'AFTER: gallery production columns' AS checkpoint;
SELECT column_name, data_type, character_maximum_length, is_nullable
FROM information_schema.columns
WHERE table_schema = 'visual_screen'
  AND table_name = 'vs_gallery_info'
  AND column_name IN (
      'id', 'option', 'query_path', 'content', 'type', 'title', 'sort', 'state',
      'business_id', 'time_type', 'dimension_type', 'dacct_radio', 'title_old',
      'add_time', 'add_user', 'index_scheme_id', 'index_scheme_name', 'condition')
ORDER BY ordinal_position;

SELECT 'AFTER: index-name routine' AS checkpoint;
SELECT routine_schema, routine_name, data_type
FROM information_schema.routines
WHERE routine_schema = 'visual_screen'
  AND lower(routine_name) = lower('f_get_IndexName')
ORDER BY routine_name;
