-- ETL Event entry procedures adapted from all_event.sql.

CREATE SCHEMA IF NOT EXISTS etl;
CREATE SCHEMA IF NOT EXISTS dm;
CREATE SCHEMA IF NOT EXISTS dmcode;
CREATE SCHEMA IF NOT EXISTS ods_temp;

CREATE TABLE IF NOT EXISTS etl.t_run_log (
    run_label TEXT,
    consume_time BIGINT,
    run_time TIMESTAMP
);

DROP PROCEDURE IF EXISTS etl.entrance_merge_dimnsn_data;
CREATE PROCEDURE etl.entrance_merge_dimnsn_data()
AS
    forms VARCHAR(40);
    v_parm VARCHAR(40);
    v_parm_1 VARCHAR(40);
    v_sqltxt TEXT;
    CURSOR tmps IS
        SELECT DISTINCT form_id FROM dmcode.t_jrtj_dimnsn;
BEGIN
    OPEN tmps;
    <<tmps_loop>>
    LOOP
        FETCH tmps INTO forms;
        EXIT WHEN tmps%NOTFOUND;

        v_parm := LOWER(CONCAT('t_jrtj_dimnsn_', forms));
        IF NOT EXISTS (
            SELECT 1
              FROM information_schema.tables
             WHERE table_name = v_parm
               AND table_schema = 'dmcode'
        ) THEN
            v_sqltxt := CONCAT(
                'CREATE TABLE dmcode.', v_parm, ' (',
                'id BIGSERIAL PRIMARY KEY, ',
                'fiscal_yr VARCHAR(10), ',
                'form_id VARCHAR(10), ',
                'dim_id VARCHAR(10), ',
                'dim_dscr VARCHAR(300), ',
                'seq_no VARCHAR(10))'
            );
            EXECUTE IMMEDIATE v_sqltxt;
        END IF;

        v_sqltxt := CONCAT(
            'INSERT INTO dmcode.t_jrtj_dimnsn_', forms,
            ' (fiscal_yr, form_id, dim_id, dim_dscr, seq_no) ',
            'SELECT fiscal_yr, form_id, dim_id, dim_dscr, seq_no ',
            'FROM dmcode.t_jrtj_dimnsn a WHERE form_id=''', forms, ''' ',
            'AND NOT EXISTS (SELECT 1 FROM dmcode.t_jrtj_dimnsn_', forms,
            ' b WHERE a.fiscal_yr = b.fiscal_yr)'
        );
        EXECUTE IMMEDIATE v_sqltxt;

        v_parm_1 := LOWER(CONCAT('v_jrtj_dimnsn_', forms));
        IF NOT EXISTS (
            SELECT 1
              FROM information_schema.views
             WHERE table_schema = 'dm'
               AND table_name = v_parm_1
        ) THEN
            v_sqltxt := CONCAT(
                'CREATE VIEW dm.', v_parm_1, ' AS ',
                'SELECT DISTINCT dim_id, ',
                'REPLACE(dim_dscr,''  '',''　'') AS dim_dscr, ',
                'REPLACE(REPLACE(dim_dscr,''　'',''''),'' '','''') AS dim_dscr_1 ',
                'FROM dmcode.t_jrtj_dimnsn_', forms,
                ' WHERE form_id=''', forms, ''''
            );
            EXECUTE IMMEDIATE v_sqltxt;
        END IF;
    END LOOP tmps_loop;
    CLOSE tmps;
END;
/

DROP PROCEDURE IF EXISTS etl.entrance_merge_t_jrtj_dim_value_data;
CREATE PROCEDURE etl.entrance_merge_t_jrtj_dim_value_data()
AS
    v_times VARCHAR(50);
    v_timex VARCHAR(50);
    v_table VARCHAR(100);
    v_parm VARCHAR(100);
    v_sqltxt TEXT;
    v_lock BOOLEAN := FALSE;
    v_error_code TEXT;
    v_error_msg TEXT;
    CURSOR c_stamp IS SELECT etl_stamp FROM ods_temp.t_etl_stamp;
BEGIN
    SELECT pg_try_advisory_lock(hashtext('ENTRANCE_MERGE_T_JRTJ_DIM_VALUE_DATA'))
      INTO v_lock;

    IF NOT v_lock THEN
        INSERT INTO etl.t_run_log (run_label, run_time)
        VALUES ('NO GET_LOCK : ENTRANCE_MERGE_T_JRTJ_DIM_VALUE_DATA', NOW());
        RETURN;
    END IF;

    DROP TABLE IF EXISTS ods_temp.temp_dimnsn;
    CREATE UNLOGGED TABLE ods_temp.temp_dimnsn (
        fiscal_yr VARCHAR(4),
        sheet_id VARCHAR(5),
        PRIMARY KEY (fiscal_yr, sheet_id)
    );

    OPEN c_stamp;
    <<lpst>>
    LOOP
        FETCH c_stamp INTO v_times;
        EXIT WHEN c_stamp%NOTFOUND;

        DROP TABLE IF EXISTS ods_temp.temp_dim_valuex;
        CREATE UNLOGGED TABLE ods_temp.temp_dim_valuex (
            time_values VARCHAR(6) PRIMARY KEY
        );

        v_sqltxt := CONCAT(
            'INSERT INTO ods_temp.temp_dim_valuex(time_values) ',
            'SELECT DISTINCT DATE_FORMAT(fiscal_priod,''%Y%m'') ',
            'FROM ods_temp.t_sheet_subject_temp_', v_times
        );
        EXECUTE IMMEDIATE v_sqltxt;

        FOR v_timex IN SELECT time_values FROM ods_temp.temp_dim_valuex
        LOOP
            v_parm := LOWER(CONCAT('t_sheet_subject_', v_timex));
            IF NOT EXISTS (
                SELECT 1
                  FROM information_schema.tables
                 WHERE table_name = v_parm
                   AND table_schema = 'ods_temp'
            ) THEN
                v_sqltxt := CONCAT(
                    'CREATE TABLE ods_temp.', v_parm, ' (',
                    'id BIGSERIAL PRIMARY KEY, ',
                    'sub_id BIGINT, task_id BIGINT, fiscal_priod VARCHAR(10), ',
                    'batch_id INT, frequenc_type SMALLINT, batch SMALLINT, ',
                    'sheet_id VARCHAR(5), org_id VARCHAR(4), area_id VARCHAR(7), ',
                    'seq_no INT, business_id VARCHAR(1), subject_id VARCHAR(5), ',
                    'subject_name VARCHAR(100), attribute_name VARCHAR(50), ',
                    'value DECIMAL(28,4), add_date TIMESTAMP)'
                );
                EXECUTE IMMEDIATE v_sqltxt;
                v_sqltxt := CONCAT(
                    'CREATE INDEX idx_', v_parm, '_lookup ON ods_temp.', v_parm,
                    ' (fiscal_priod, frequenc_type, sheet_id, org_id, area_id)'
                );
                EXECUTE IMMEDIATE v_sqltxt;
            END IF;

            v_sqltxt := CONCAT(
                'DELETE FROM ods_temp.', v_parm, ' a USING ',
                'ods_temp.t_sheet_subject_temp_', v_times, ' b ',
                'WHERE a.fiscal_priod = b.fiscal_priod ',
                'AND a.frequenc_type = b.frequenc_type ',
                'AND a.sheet_id = b.sheet_id ',
                'AND a.org_id = b.org_id ',
                'AND a.area_id = b.area_id'
            );
            EXECUTE IMMEDIATE v_sqltxt;

            v_sqltxt := CONCAT(
                'INSERT INTO ods_temp.', v_parm,
                ' (sub_id, task_id, fiscal_priod, batch_id, frequenc_type, batch, ',
                'sheet_id, org_id, area_id, seq_no, business_id, subject_id, ',
                'subject_name, attribute_name, value, add_date) ',
                'SELECT ss.sub_id, ss.task_id, ss.fiscal_priod, ss.batch_id, ',
                'ss.frequenc_type, ss.batch, ss.sheet_id, ss.org_id, ss.area_id, ',
                'ss.seq_no, ss.business_id, ss.subject_id, ss.subject_name, ',
                'ss.attribute_name, ss.value, ss.add_date ',
                'FROM ods_temp.t_sheet_subject_temp_', v_times, ' ss ',
                'LEFT JOIN dmcode.t_data_attri_info ta ',
                'ON ta.attribute_name = ss.attribute_name ',
                'WHERE DATE_FORMAT(ss.fiscal_priod,''%Y%m'') = ''', v_timex, ''' ',
                'AND ta.attribute_name IS NULL'
            );
            EXECUTE IMMEDIATE v_sqltxt;
        END LOOP;

        DROP TABLE IF EXISTS ods_temp.temp_dim_valuex;
        DELETE FROM ods_temp.temp_dimnsn;

        v_sqltxt := CONCAT(
            'INSERT INTO ods_temp.temp_dimnsn(fiscal_yr, sheet_id) ',
            'SELECT DISTINCT DATE_FORMAT(fiscal_priod,''%Y''), sheet_id ',
            'FROM ods_temp.t_sheet_subject_temp_', v_times
        );
        EXECUTE IMMEDIATE v_sqltxt;

        IF NOT EXISTS (
            SELECT 1
              FROM dmcode.t_jrtj_dimnsn a
              JOIN ods_temp.temp_dimnsn b
                ON a.fiscal_yr = b.fiscal_yr
               AND a.form_id = b.sheet_id
        ) THEN
            v_sqltxt := CONCAT(
                'INSERT INTO dmcode.t_jrtj_dimnsn ',
                '(fiscal_yr, form_id, dim_id, dim_dscr, seq_no) ',
                'SELECT DISTINCT DATE_FORMAT(fiscal_priod,''%Y''), sheet_id, ',
                'subject_id, subject_name, seq_no ',
                'FROM ods_temp.t_sheet_subject_temp_', v_times
            );
            EXECUTE IMMEDIATE v_sqltxt;
        END IF;

        v_table := LOWER(CONCAT('t_sheet_subject_temp_', v_times));
        EXECUTE IMMEDIATE CONCAT('DROP TABLE IF EXISTS ods_temp.', v_table);
        DELETE FROM ods_temp.t_etl_stamp WHERE etl_stamp = v_times;
    END LOOP lpst;
    CLOSE c_stamp;

    DROP TABLE IF EXISTS ods_temp.temp_dim_valuex;
    DROP TABLE IF EXISTS ods_temp.temp_dimnsn;
    INSERT INTO etl.t_run_log (run_label, run_time)
    VALUES ('ENTRANCE_MERGE_T_JRTJ_DIM_VALUE_DATA', NOW());
    SELECT pg_advisory_unlock(hashtext('ENTRANCE_MERGE_T_JRTJ_DIM_VALUE_DATA'))
      INTO v_lock;
EXCEPTION
    WHEN OTHERS THEN
        GET DIAGNOSTICS CONDITION 1
            v_error_code = RETURNED_SQLSTATE,
            v_error_msg = MESSAGE_TEXT;
        INSERT INTO etl.t_run_log (run_label, run_time)
        VALUES (
            CONCAT('ERR_NO : ', v_error_code, '; ERR_MSG : ', v_error_msg, '; ', v_sqltxt),
            NOW()
        );
        DROP TABLE IF EXISTS ods_temp.temp_dim_valuex;
        DROP TABLE IF EXISTS ods_temp.temp_dimnsn;
        IF v_lock THEN
            SELECT pg_advisory_unlock(hashtext('ENTRANCE_MERGE_T_JRTJ_DIM_VALUE_DATA'))
              INTO v_lock;
        END IF;
END;
/
