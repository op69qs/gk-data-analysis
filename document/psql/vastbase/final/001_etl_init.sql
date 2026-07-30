-- Compile-unblocker stubs for ETL logging procedures.
-- These are placeholders until the real ETL source procedures are recovered.

CREATE SCHEMA IF NOT EXISTS etl;

DROP TABLE IF EXISTS etl.proc_log_stub;
CREATE TABLE IF NOT EXISTS etl.proc_log_stub (
    id BIGSERIAL PRIMARY KEY,
    log_type VARCHAR(20) NOT NULL,
    acct_id VARCHAR(64),
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    proc_name VARCHAR(200),
    step_id INT,
    payload_a TEXT,
    payload_b TEXT,
    created_at TIMESTAMP DEFAULT NOW()
);

DROP PROCEDURE IF EXISTS etl.edw_proc_trace_log;
CREATE PROCEDURE etl.edw_proc_trace_log(
    IN p_acct_id VARCHAR(64),
    IN p_start_time TIMESTAMP,
    IN p_end_time TIMESTAMP,
    IN p_proc_name VARCHAR(200),
    IN p_step_id INT,
    IN p_row_count BIGINT
)
AS
BEGIN
    INSERT INTO etl.proc_log_stub (
        log_type,
        acct_id,
        start_time,
        end_time,
        proc_name,
        step_id,
        payload_a
    ) VALUES (
        'TRACE',
        p_acct_id,
        p_start_time,
        p_end_time,
        p_proc_name,
        p_step_id,
        CAST(p_row_count AS TEXT)
    );
END;
/

DROP PROCEDURE IF EXISTS etl.edw_proc_error_log;
CREATE PROCEDURE etl.edw_proc_error_log(
    IN p_acct_id VARCHAR(64),
    IN p_start_time TIMESTAMP,
    IN p_end_time TIMESTAMP,
    IN p_proc_name VARCHAR(200),
    IN p_step_id INT,
    IN p_return_code TEXT,
    IN p_error_msg TEXT
)
AS
BEGIN
    INSERT INTO etl.proc_log_stub (
        log_type,
        acct_id,
        start_time,
        end_time,
        proc_name,
        step_id,
        payload_a,
        payload_b
    ) VALUES (
        'ERROR',
        p_acct_id,
        p_start_time,
        p_end_time,
        p_proc_name,
        p_step_id,
        p_return_code,
        p_error_msg
    );
END;
/
