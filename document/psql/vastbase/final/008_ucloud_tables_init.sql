-- Vastbase migration script for ucloud table structures converted from ucloud_结构.sql
-- Generated from document/psql/mysql/ucloud_结构.sql and upm_结构.sql as applicable.

-- Vastbase migration script for ucloud / upm structures and procedures
-- Source files:
--   - document/psql/mysql/ucloud_结构.sql
--   - document/psql/mysql/upm_结构.sql
-- Generated from MySQL DDL with Vastbase B-compatible procedure syntax.


-- ucloud tables
CREATE SCHEMA IF NOT EXISTS ucloud;
SET search_path TO ucloud, public;

DROP TABLE IF EXISTS ucloud.api_alarm_summary;
CREATE TABLE ucloud.api_alarm_summary (
    ID INT NOT NULL,
    index_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_name varchar(255) DEFAULT NULL,
    resource_ip varchar(100) DEFAULT NULL,
    period varchar(2) DEFAULT NULL,
    time varchar(20) DEFAULT NULL,
    time_date varchar(10) DEFAULT NULL,
    Count varchar(10) DEFAULT NULL,
    add_time varchar(20) DEFAULT NULL,
    table_name varchar(255) DEFAULT NULL,
    PRIMARY KEY (ID)
);
COMMENT ON TABLE ucloud.api_alarm_summary IS '监测器告警汇总表';
-- Secondary indexes from the MySQL exports are omitted in this compile package to keep the dry-run bounded; primary keys are retained.

DROP TABLE IF EXISTS ucloud.api_alarm_summary_copy1;
CREATE TABLE ucloud.api_alarm_summary_copy1 (
    ID INT NOT NULL,
    index_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_name varchar(255) DEFAULT NULL,
    resource_ip varchar(100) DEFAULT NULL,
    period varchar(2) DEFAULT NULL,
    time varchar(20) DEFAULT NULL,
    time_date varchar(10) DEFAULT NULL,
    Count varchar(10) DEFAULT NULL,
    add_time varchar(20) DEFAULT NULL,
    table_name varchar(255) DEFAULT NULL,
    PRIMARY KEY (ID)
);
COMMENT ON TABLE ucloud.api_alarm_summary_copy1 IS '监测器告警汇总表';

DROP TABLE IF EXISTS ucloud.api_forecast_alarm_records;
CREATE TABLE ucloud.api_forecast_alarm_records (
    ID varchar(32) NOT NULL,
    index_id varchar(32) DEFAULT NULL,
    index_name varchar(200) DEFAULT NULL,
    resource_name varchar(500) DEFAULT NULL,
    alarm_time varchar(32) DEFAULT NULL,
    forecast_value varchar(32) DEFAULT NULL,
    actual_value varchar(32) DEFAULT NULL,
    error_value varchar(32) DEFAULT NULL,
    is_shared varchar(2) NOT NULL DEFAULT '0',
    add_time varchar(32) DEFAULT NULL,
    PRIMARY KEY (ID)
);
COMMENT ON TABLE ucloud.api_forecast_alarm_records IS '监测器告警预测记录';

DROP TABLE IF EXISTS ucloud.api_interface_alarm_data;
CREATE TABLE ucloud.api_interface_alarm_data (
    ID varchar(32) NOT NULL,
    index_code varchar(50) DEFAULT NULL,
    resource_name varchar(500) DEFAULT NULL,
    resource_ip varchar(100) DEFAULT NULL,
    alarm_count varchar(10) DEFAULT NULL,
    firstOccurTime varchar(32) DEFAULT NULL,
    lastOccurTime varchar(32) DEFAULT NULL,
    description text NULL,
    add_time varchar(32) DEFAULT NULL,
    PRIMARY KEY (ID)
);
COMMENT ON TABLE ucloud.api_interface_alarm_data IS '优云监测器告警数据';

DROP TABLE IF EXISTS ucloud.api_interface_system_data202004;
CREATE TABLE ucloud.api_interface_system_data202004 (
    id varchar(39) DEFAULT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    TIME varchar(32) DEFAULT NULL,
    content text NULL,
    category varchar(50) DEFAULT NULL,
    level varchar(32) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL
);

DROP TABLE IF EXISTS ucloud.api_interface_system_data202005;
CREATE TABLE ucloud.api_interface_system_data202005 (
    id varchar(39) DEFAULT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    TIME varchar(32) DEFAULT NULL,
    content text NULL,
    category varchar(50) DEFAULT NULL,
    level varchar(32) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL
);

DROP TABLE IF EXISTS ucloud.api_interface_system_data202006;
CREATE TABLE ucloud.api_interface_system_data202006 (
    id varchar(39) DEFAULT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    TIME varchar(32) DEFAULT NULL,
    content text NULL,
    category varchar(50) DEFAULT NULL,
    level varchar(32) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL
);

DROP TABLE IF EXISTS ucloud.api_interface_system_data202007;
CREATE TABLE ucloud.api_interface_system_data202007 (
    id varchar(39) DEFAULT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    TIME varchar(32) DEFAULT NULL,
    content text NULL,
    category varchar(50) DEFAULT NULL,
    level varchar(32) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL
);

DROP TABLE IF EXISTS ucloud.api_interface_system_data202008;
CREATE TABLE ucloud.api_interface_system_data202008 (
    id varchar(39) DEFAULT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    TIME varchar(32) DEFAULT NULL,
    content text NULL,
    category varchar(50) DEFAULT NULL,
    level varchar(32) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL
);

DROP TABLE IF EXISTS ucloud.api_interface_system_data202009;
CREATE TABLE ucloud.api_interface_system_data202009 (
    id varchar(39) DEFAULT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    TIME varchar(32) DEFAULT NULL,
    content text NULL,
    category varchar(50) DEFAULT NULL,
    level varchar(32) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL
);

DROP TABLE IF EXISTS ucloud.api_interface_system_data202010;
CREATE TABLE ucloud.api_interface_system_data202010 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (ID)
);
COMMENT ON TABLE ucloud.api_interface_system_data202010 IS '优云系统(202010)';

DROP TABLE IF EXISTS ucloud.api_interface_system_data202010_bak;
CREATE TABLE ucloud.api_interface_system_data202010_bak (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    category varchar(50) DEFAULT NULL,
    level varchar(32) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL
);

DROP TABLE IF EXISTS ucloud.api_interface_system_data202011;
CREATE TABLE ucloud.api_interface_system_data202011 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(20) DEFAULT NULL
);
COMMENT ON TABLE ucloud.api_interface_system_data202011 IS '优云系统数据(202011)';

DROP TABLE IF EXISTS ucloud.api_interface_system_data202012;
CREATE TABLE ucloud.api_interface_system_data202012 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(20) DEFAULT NULL
);
COMMENT ON TABLE ucloud.api_interface_system_data202012 IS '优云系统数据(202012)';

DROP TABLE IF EXISTS ucloud.api_interface_system_data202101;
CREATE TABLE ucloud.api_interface_system_data202101 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(20) DEFAULT NULL
);
COMMENT ON TABLE ucloud.api_interface_system_data202101 IS '优云系统数据(202101)';

DROP TABLE IF EXISTS ucloud.api_interface_system_data202102;
CREATE TABLE ucloud.api_interface_system_data202102 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(20) DEFAULT NULL
);
COMMENT ON TABLE ucloud.api_interface_system_data202102 IS '优云系统数据(202102)';

DROP TABLE IF EXISTS ucloud.api_interface_system_data202103;
CREATE TABLE ucloud.api_interface_system_data202103 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(20) DEFAULT NULL
);
COMMENT ON TABLE ucloud.api_interface_system_data202103 IS '优云系统数据(202103)';

DROP TABLE IF EXISTS ucloud.api_interface_system_data202104;
CREATE TABLE ucloud.api_interface_system_data202104 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(20) DEFAULT NULL
);
COMMENT ON TABLE ucloud.api_interface_system_data202104 IS '优云系统数据(202104)';

DROP TABLE IF EXISTS ucloud.api_interface_system_data202309;
CREATE TABLE ucloud.api_interface_system_data202309 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(20) DEFAULT NULL
);
COMMENT ON TABLE ucloud.api_interface_system_data202309 IS '优云系统数据(202309)';

DROP TABLE IF EXISTS ucloud.api_system_records;
CREATE TABLE ucloud.api_system_records (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    system_code varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    platfrom_code varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    api_contract varchar(32) DEFAULT NULL,
    api_headers varchar(2000) DEFAULT NULL,
    api_type varchar(1) DEFAULT NULL,
    api_method varchar(255) DEFAULT NULL,
    api_params text NULL,
    api_status varchar(3) DEFAULT NULL,
    api_count varchar(1) DEFAULT NULL,
    content text NULL,
    add_time date DEFAULT NULL,
    PRIMARY KEY (ID)
);

DROP TABLE IF EXISTS ucloud.menu_bak;
CREATE TABLE ucloud.menu_bak (
    MENU_ID varchar(50) DEFAULT NULL,
    PARENT_MENU_ID varchar(32) DEFAULT NULL,
    MENU_NAME varchar(200) DEFAULT NULL,
    URL varchar(2000) DEFAULT NULL,
    MENU_LVL INT DEFAULT NULL,
    SORT INT DEFAULT NULL,
    IS_VALID varchar(1) DEFAULT NULL,
    MENU_ICON varchar(1000) DEFAULT NULL,
    IS_PROJECT varchar(1) NULL DEFAULT '0'
);

DROP TABLE IF EXISTS ucloud.table_name;
CREATE TABLE ucloud.table_name (
    table_name varchar(100) DEFAULT NULL,
    pri_name varchar(100) DEFAULT NULL
);

DROP TABLE IF EXISTS ucloud.temp11;
CREATE TABLE ucloud.temp11 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    d_acct date DEFAULT NULL
);

