-- ETL logging tables and procedures adapted from all_event.sql.

CREATE SCHEMA IF NOT EXISTS etl;

CREATE TABLE IF NOT EXISTS etl.edw_proc_trace_log (
    data_date CHAR(10),
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    proc_name VARCHAR(80),
    step_id INT,
    row_count BIGINT
);

-- 仅当旧库仍是 CHAR/VARCHAR 等非时间类型时才迁移；已是 timestamp 则跳过，保证可重复执行。
DO $migrate_trace$
DECLARE
    v_start_type TEXT;
    v_end_type TEXT;
    v_row_type TEXT;
BEGIN
    SELECT data_type INTO v_start_type
      FROM information_schema.columns
     WHERE table_schema = 'etl'
       AND table_name = 'edw_proc_trace_log'
       AND column_name = 'start_time';
    SELECT data_type INTO v_end_type
      FROM information_schema.columns
     WHERE table_schema = 'etl'
       AND table_name = 'edw_proc_trace_log'
       AND column_name = 'end_time';
    SELECT data_type INTO v_row_type
      FROM information_schema.columns
     WHERE table_schema = 'etl'
       AND table_name = 'edw_proc_trace_log'
       AND column_name = 'row_count';

    IF v_start_type IS NOT NULL
       AND v_start_type NOT IN ('timestamp without time zone', 'timestamp with time zone') THEN
        EXECUTE 'ALTER TABLE etl.edw_proc_trace_log ALTER COLUMN start_time DROP DEFAULT';
        EXECUTE 'ALTER TABLE etl.edw_proc_trace_log
            ALTER COLUMN start_time TYPE TIMESTAMP
            USING NULLIF(TRIM(start_time::text), '''')::timestamp';
    END IF;

    IF v_end_type IS NOT NULL
       AND v_end_type NOT IN ('timestamp without time zone', 'timestamp with time zone') THEN
        EXECUTE 'ALTER TABLE etl.edw_proc_trace_log ALTER COLUMN end_time DROP DEFAULT';
        EXECUTE 'ALTER TABLE etl.edw_proc_trace_log
            ALTER COLUMN end_time TYPE TIMESTAMP
            USING NULLIF(TRIM(end_time::text), '''')::timestamp';
    END IF;

    IF v_row_type IS NOT NULL
       AND v_row_type NOT IN ('bigint', 'int8') THEN
        EXECUTE 'ALTER TABLE etl.edw_proc_trace_log
            ALTER COLUMN row_count TYPE BIGINT
            USING NULLIF(TRIM(row_count::text), '''')::bigint';
    END IF;
END
$migrate_trace$;

CREATE INDEX IF NOT EXISTS idx_edw_proc_trace_log_lookup
    ON etl.edw_proc_trace_log (data_date, proc_name, step_id);

CREATE TABLE IF NOT EXISTS etl.edw_proc_error_log (
    data_date CHAR(10),
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    run_time BIGINT,
    proc_name VARCHAR(80),
    step_id INT,
    error_code CHAR(9),
    error_msg VARCHAR(2000)
);

DO $migrate_error$
DECLARE
    v_start_type TEXT;
    v_end_type TEXT;
BEGIN
    SELECT data_type INTO v_start_type
      FROM information_schema.columns
     WHERE table_schema = 'etl'
       AND table_name = 'edw_proc_error_log'
       AND column_name = 'start_time';
    SELECT data_type INTO v_end_type
      FROM information_schema.columns
     WHERE table_schema = 'etl'
       AND table_name = 'edw_proc_error_log'
       AND column_name = 'end_time';

    IF v_start_type IS NOT NULL
       AND v_start_type NOT IN ('timestamp without time zone', 'timestamp with time zone') THEN
        EXECUTE 'ALTER TABLE etl.edw_proc_error_log ALTER COLUMN start_time DROP DEFAULT';
        EXECUTE 'ALTER TABLE etl.edw_proc_error_log
            ALTER COLUMN start_time TYPE TIMESTAMP
            USING NULLIF(TRIM(start_time::text), '''')::timestamp';
    END IF;

    IF v_end_type IS NOT NULL
       AND v_end_type NOT IN ('timestamp without time zone', 'timestamp with time zone') THEN
        EXECUTE 'ALTER TABLE etl.edw_proc_error_log ALTER COLUMN end_time DROP DEFAULT';
        EXECUTE 'ALTER TABLE etl.edw_proc_error_log
            ALTER COLUMN end_time TYPE TIMESTAMP
            USING NULLIF(TRIM(end_time::text), '''')::timestamp';
    END IF;
END
$migrate_error$;

CREATE INDEX IF NOT EXISTS idx_edw_proc_error_log_lookup
    ON etl.edw_proc_error_log (data_date, proc_name, step_id);

DROP PROCEDURE IF EXISTS etl.edw_proc_trace_log;
CREATE PROCEDURE etl.edw_proc_trace_log(
    IN p_data_date CHAR(10),
    IN p_start_time VARCHAR,
    IN p_end_time TIMESTAMP,
    IN p_proc_name VARCHAR(80),
    IN p_step_id INT,
    IN p_row_count BIGINT
)
AS
BEGIN
    DELETE FROM etl.edw_proc_trace_log
     WHERE data_date = p_data_date
       AND proc_name = p_proc_name
       AND step_id = p_step_id;

    INSERT INTO etl.edw_proc_trace_log (
        data_date, start_time, end_time, proc_name, step_id, row_count
    ) VALUES (
        p_data_date, p_start_time::timestamp, p_end_time, p_proc_name, p_step_id, p_row_count
    );
    COMMIT;
END;
/

DROP PROCEDURE IF EXISTS etl.edw_proc_error_log;
CREATE PROCEDURE etl.edw_proc_error_log(
    IN p_data_date CHAR(10),
    IN p_start_time VARCHAR,
    IN p_end_time TIMESTAMP,
    IN p_proc_name VARCHAR(80),
    IN p_step_id INT,
    IN p_error_code TEXT,
    IN p_error_msg TEXT
)
AS
BEGIN
    INSERT INTO etl.edw_proc_error_log (
        data_date,
        start_time,
        end_time,
        run_time,
        proc_name,
        step_id,
        error_code,
        error_msg
    ) VALUES (
        p_data_date,
        p_start_time::timestamp,
        p_end_time,
        TIMESTAMPDIFF(SECOND, p_start_time::timestamp, p_end_time),
        p_proc_name,
        p_step_id,
        p_error_code,
        p_error_msg
    );
    COMMIT;
END;
/
