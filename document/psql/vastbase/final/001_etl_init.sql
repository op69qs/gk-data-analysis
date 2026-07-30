-- ETL logging tables and procedures adapted from all_event.sql.

CREATE SCHEMA IF NOT EXISTS etl;

CREATE TABLE IF NOT EXISTS etl.edw_proc_trace_log (
    data_date CHAR(10),
    start_time CHAR(19),
    end_time CHAR(19),
    proc_name VARCHAR(80),
    step_id INT,
    row_count INT
);

CREATE INDEX IF NOT EXISTS idx_edw_proc_trace_log_lookup
    ON etl.edw_proc_trace_log (data_date, proc_name, step_id);

CREATE TABLE IF NOT EXISTS etl.edw_proc_error_log (
    data_date CHAR(10),
    start_time CHAR(19),
    end_time CHAR(19),
    run_time BIGINT,
    proc_name VARCHAR(80),
    step_id INT,
    error_code CHAR(9),
    error_msg VARCHAR(2000)
);

CREATE INDEX IF NOT EXISTS idx_edw_proc_error_log_lookup
    ON etl.edw_proc_error_log (data_date, proc_name, step_id);

DROP PROCEDURE IF EXISTS etl.edw_proc_trace_log;
CREATE PROCEDURE etl.edw_proc_trace_log(
    IN p_data_date CHAR(10),
    IN p_start_time CHAR(19),
    IN p_end_time CHAR(19),
    IN p_proc_name VARCHAR(80),
    IN p_step_id INT,
    IN p_row_count INT
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
        p_data_date, p_start_time, p_end_time, p_proc_name, p_step_id, p_row_count
    );
    COMMIT;
END;
/

DROP PROCEDURE IF EXISTS etl.edw_proc_error_log;
CREATE PROCEDURE etl.edw_proc_error_log(
    IN p_data_date CHAR(10),
    IN p_start_time CHAR(19),
    IN p_end_time CHAR(19),
    IN p_proc_name VARCHAR(80),
    IN p_step_id INT,
    IN p_error_code CHAR(9),
    IN p_error_msg VARCHAR(2000)
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
        p_start_time,
        p_end_time,
        TIMESTAMPDIFF(SECOND, p_start_time, p_end_time),
        p_proc_name,
        p_step_id,
        p_error_code,
        p_error_msg
    );
    COMMIT;
END;
/
