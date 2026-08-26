\echo [1/19] loading 001_etl_init.sql
\ir 001_etl_init.sql

\echo [2/19] loading 001a_etl_event_routines_init.sql
\ir 001a_etl_event_routines_init.sql

\echo [3/19] loading 002_ods_init.sql
\ir 002_ods_init.sql

\echo [4/19] loading 003_report_init.sql
\ir 003_report_init.sql

\echo [5/19] loading 004_edw_init.sql
\ir 004_edw_init.sql

\echo [6/19] loading 005_visual_screen_init.sql
\ir 005_visual_screen_init.sql

\echo [7/19] loading 006_indicators_lib_init.sql
\ir 006_indicators_lib_init.sql

\echo [8/19] loading 006a_event_dependency_routines_init.sql
\ir 006a_event_dependency_routines_init.sql

\echo [9/19] loading 007_events_init.sql
\ir 007_events_init.sql

\echo [10/19] loading 008_ucloud_tables_init.sql
\ir 008_ucloud_tables_init.sql

\echo [11/19] loading 009_upm_alarmlog_tables_init.sql
\ir 009_upm_alarmlog_tables_init.sql

\echo [12/19] loading 010_upm_system_data_tables_init.sql
\ir 010_upm_system_data_tables_init.sql

\echo [13/19] loading 011_upm_netperformance_tables_init.sql
\ir 011_upm_netperformance_tables_init.sql

\echo [14/19] loading 012_ucloud_upm_procedures_init.sql
\ir 012_ucloud_upm_procedures_init.sql

\echo [15/19] loading 013_adm_tables_init.sql
\ir 013_adm_tables_init.sql

\echo [16/19] loading 014_adm_indexes_init.sql
\ir 014_adm_indexes_init.sql

\echo [17/19] loading 014a_adm_dependency_routines_init.sql
\ir 014a_adm_dependency_routines_init.sql

\echo [18/19] loading 015_adm_routines_init.sql
\ir 015_adm_routines_init.sql

\echo [19/19] loading 016_adm_events_init.sql
\ir 016_adm_events_init.sql

\echo [verify] loading 000_verify_init.sql
\ir 000_verify_init.sql
