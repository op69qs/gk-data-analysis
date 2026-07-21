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
   OR (table_schema = 'edw'
       AND table_name = 'cm_guoku_dimnsn')
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
SELECT n.nspname AS routine_schema,
       p.proname AS routine_name,
       pg_get_function_identity_arguments(p.oid) AS identity_arguments,
       pg_get_function_result(p.oid) AS data_type
FROM pg_proc p
JOIN pg_namespace n ON n.oid = p.pronamespace
WHERE n.nspname = 'visual_screen'
  AND lower(p.proname) = lower('f_get_IndexName')
ORDER BY p.proname, identity_arguments;

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
            ('indicators_lib', 'lib_index_relation'),
            ('edw', 'cm_guoku_dimnsn')
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

DO $required_columns$
DECLARE
    required_column record;
BEGIN
    FOR required_column IN
        SELECT required.table_schema, required.table_name, required.column_name
        FROM (VALUES
            ('visual_screen', 'vs_lib_index_scheme', 'id'),
            ('visual_screen', 'vs_lib_index_scheme', 'scheme_descr'),
            ('visual_screen', 'vs_lib_index_scheme', 'scheme_sql'),
            ('visual_screen', 'vs_lib_index_scheme', 'scheme_colums'),
            ('visual_screen', 'vs_lib_index_scheme', 'scheme_conditon'),
            ('visual_screen', 'vs_lib_index_scheme', 'add_userid'),
            ('visual_screen', 'vs_lib_index_scheme', 'add_date'),
            ('visual_screen', 'vs_gallery_info', 'id'),
            ('visual_screen', 'vs_gallery_info', 'option'),
            ('visual_screen', 'vs_gallery_info', 'query_path'),
            ('visual_screen', 'vs_gallery_info', 'content'),
            ('visual_screen', 'vs_gallery_info', 'type'),
            ('visual_screen', 'vs_gallery_info', 'title'),
            ('visual_screen', 'vs_gallery_info', 'sort'),
            ('visual_screen', 'vs_gallery_info', 'state'),
            ('visual_screen', 'vs_gallery_info', 'business_id'),
            ('visual_screen', 'vs_gallery_info', 'time_type'),
            ('visual_screen', 'vs_gallery_info', 'dimension_type'),
            ('visual_screen', 'sys_user', 'id'),
            ('visual_screen', 'sys_user', 'realname'),
            ('indicators_lib', 'lib_index_relation', 'index_id'),
            ('indicators_lib', 'lib_index_relation', 'index_name'),
            ('indicators_lib', 'lib_index_relation', 'index_type'),
            ('indicators_lib', 'lib_index_relation', 'index_corre_table'),
            ('edw', 'cm_guoku_dimnsn', 'guoku_dscr'),
            ('edw', 'cm_guoku_dimnsn', 'guoku_id'),
            ('edw', 'cm_guoku_dimnsn', 'area_dscr'),
            ('edw', 'cm_guoku_dimnsn', 'area_no_id')
        ) AS required(table_schema, table_name, column_name)
    LOOP
        IF NOT EXISTS (
            SELECT 1
            FROM information_schema.columns actual
            WHERE actual.table_schema = required_column.table_schema
              AND actual.table_name = required_column.table_name
              AND actual.column_name = required_column.column_name
        ) THEN
            RAISE EXCEPTION 'missing required production column: %',
                required_column.table_schema || '.' ||
                required_column.table_name || '.' ||
                required_column.column_name;
        END IF;
    END LOOP;
END;
$required_columns$ LANGUAGE plpgsql;

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
    scheme_columns text)
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

-- Preserve the historical MySQL-compatible varchar entry point.  Keep this
-- argument anonymous because CREATE OR REPLACE must not rename a deployed input
-- parameter; the explicit cast selects the text implementation.
CREATE OR REPLACE FUNCTION visual_screen.f_get_IndexName(
    varchar(1000))
RETURNS varchar(2000)
AS $compatibility$
BEGIN
    RETURN visual_screen.f_get_IndexName($1::text);
END;
$compatibility$
LANGUAGE plpgsql
STABLE;

SELECT 'AFTER: required production tables' AS checkpoint;
SELECT table_schema, table_name
FROM information_schema.tables
WHERE (table_schema = 'visual_screen'
       AND table_name IN ('vs_lib_index_scheme', 'vs_gallery_info', 'sys_user'))
   OR (table_schema = 'indicators_lib'
       AND table_name = 'lib_index_relation')
   OR (table_schema = 'edw'
       AND table_name = 'cm_guoku_dimnsn')
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
SELECT n.nspname AS routine_schema,
       p.proname AS routine_name,
       pg_get_function_identity_arguments(p.oid) AS identity_arguments,
       pg_get_function_result(p.oid) AS data_type
FROM pg_proc p
JOIN pg_namespace n ON n.oid = p.pronamespace
WHERE n.nspname = 'visual_screen'
  AND lower(p.proname) = lower('f_get_IndexName')
ORDER BY p.proname, identity_arguments;

DO $required_index_name_signatures$
BEGIN
    IF to_regprocedure(
        'visual_screen.f_get_indexname(text)') IS NULL THEN
        RAISE EXCEPTION 'missing required index-name function signature: %',
            'visual_screen.f_get_indexname(text)';
    END IF;

    IF to_regprocedure(
        'visual_screen.f_get_indexname(character varying)') IS NULL THEN
        RAISE EXCEPTION 'missing required index-name function signature: %',
            'visual_screen.f_get_indexname(character varying)';
    END IF;
END;
$required_index_name_signatures$ LANGUAGE plpgsql;
