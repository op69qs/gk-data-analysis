-- Vastbase migration script for upm api_alarm_summary and api_interface_system_data* table structures converted from upm_结构.sql
-- Generated from document/psql/mysql/ucloud_结构.sql and upm_结构.sql as applicable.

CREATE SCHEMA IF NOT EXISTS upm;
SET search_path TO upm, public;

DROP TABLE IF EXISTS upm.api_alarm_summary;
CREATE TABLE upm.api_alarm_summary (
    id INT NOT NULL,
    category varchar(255) DEFAULT NULL,
    lineId varchar(255) DEFAULT NULL,
    Count varchar(255) DEFAULT NULL,
    time varchar(255) DEFAULT NULL,
    table_name varchar(255) DEFAULT NULL,
    index_id varchar(255) DEFAULT NULL,
    period varchar(2) DEFAULT NULL,
    resource_code varchar(255) DEFAULT NULL,
    add_time TIMESTAMP(0) DEFAULT NULL,
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS upm.api_forecast_alarm_records;
CREATE TABLE upm.api_forecast_alarm_records (
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
COMMENT ON TABLE upm.api_forecast_alarm_records IS '监测器告警预测记录';

DROP TABLE IF EXISTS upm.api_interface_system_data20200505;
CREATE TABLE upm.api_interface_system_data20200505 (
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

DROP TABLE IF EXISTS upm.api_interface_system_data20200605;
CREATE TABLE upm.api_interface_system_data20200605 (
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

DROP TABLE IF EXISTS upm.api_interface_system_data20200705;
CREATE TABLE upm.api_interface_system_data20200705 (
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

DROP TABLE IF EXISTS upm.api_interface_system_data20200805;
CREATE TABLE upm.api_interface_system_data20200805 (
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

DROP TABLE IF EXISTS upm.api_interface_system_data20200905;
CREATE TABLE upm.api_interface_system_data20200905 (
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

DROP TABLE IF EXISTS upm.api_interface_system_data20201001;
CREATE TABLE upm.api_interface_system_data20201001 (
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
COMMENT ON TABLE upm.api_interface_system_data20201001 IS 'UPM系统数据(20201001)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201002;
CREATE TABLE upm.api_interface_system_data20201002 (
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
COMMENT ON TABLE upm.api_interface_system_data20201002 IS 'UPM系统数据(20201002)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201003;
CREATE TABLE upm.api_interface_system_data20201003 (
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
COMMENT ON TABLE upm.api_interface_system_data20201003 IS 'UPM系统数据(20201003)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201004;
CREATE TABLE upm.api_interface_system_data20201004 (
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
COMMENT ON TABLE upm.api_interface_system_data20201004 IS 'UPM系统数据(20201004)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201005;
CREATE TABLE upm.api_interface_system_data20201005 (
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
COMMENT ON TABLE upm.api_interface_system_data20201005 IS 'UPM系统数据(20201005)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201006;
CREATE TABLE upm.api_interface_system_data20201006 (
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
COMMENT ON TABLE upm.api_interface_system_data20201006 IS 'UPM系统数据(20201006)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201007;
CREATE TABLE upm.api_interface_system_data20201007 (
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
COMMENT ON TABLE upm.api_interface_system_data20201007 IS 'UPM系统数据(20201007)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201008;
CREATE TABLE upm.api_interface_system_data20201008 (
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
COMMENT ON TABLE upm.api_interface_system_data20201008 IS 'UPM系统数据(20201008)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201009;
CREATE TABLE upm.api_interface_system_data20201009 (
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
COMMENT ON TABLE upm.api_interface_system_data20201009 IS 'UPM系统数据(20201009)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201010;
CREATE TABLE upm.api_interface_system_data20201010 (
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
COMMENT ON TABLE upm.api_interface_system_data20201010 IS 'UPM系统数据(20201010)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201011;
CREATE TABLE upm.api_interface_system_data20201011 (
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
COMMENT ON TABLE upm.api_interface_system_data20201011 IS 'UPM系统数据(20201011)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201012;
CREATE TABLE upm.api_interface_system_data20201012 (
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
COMMENT ON TABLE upm.api_interface_system_data20201012 IS 'UPM系统数据(20201012)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201013;
CREATE TABLE upm.api_interface_system_data20201013 (
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
COMMENT ON TABLE upm.api_interface_system_data20201013 IS 'UPM系统数据(20201013)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201014;
CREATE TABLE upm.api_interface_system_data20201014 (
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
COMMENT ON TABLE upm.api_interface_system_data20201014 IS 'UPM系统数据(20201014)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201015;
CREATE TABLE upm.api_interface_system_data20201015 (
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
COMMENT ON TABLE upm.api_interface_system_data20201015 IS 'UPM系统数据(20201015)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201016;
CREATE TABLE upm.api_interface_system_data20201016 (
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
COMMENT ON TABLE upm.api_interface_system_data20201016 IS 'UPM系统数据(20201016)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201017;
CREATE TABLE upm.api_interface_system_data20201017 (
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
COMMENT ON TABLE upm.api_interface_system_data20201017 IS 'UPM系统数据(20201017)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201018;
CREATE TABLE upm.api_interface_system_data20201018 (
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
COMMENT ON TABLE upm.api_interface_system_data20201018 IS 'UPM系统数据(20201018)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201019;
CREATE TABLE upm.api_interface_system_data20201019 (
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
COMMENT ON TABLE upm.api_interface_system_data20201019 IS 'UPM系统数据(20201019)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201020;
CREATE TABLE upm.api_interface_system_data20201020 (
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
COMMENT ON TABLE upm.api_interface_system_data20201020 IS 'UPM系统数据(20201020)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201021;
CREATE TABLE upm.api_interface_system_data20201021 (
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
COMMENT ON TABLE upm.api_interface_system_data20201021 IS 'UPM系统数据(20201021)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201022;
CREATE TABLE upm.api_interface_system_data20201022 (
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
COMMENT ON TABLE upm.api_interface_system_data20201022 IS 'UPM系统数据(20201022)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201024;
CREATE TABLE upm.api_interface_system_data20201024 (
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
COMMENT ON TABLE upm.api_interface_system_data20201024 IS 'UPM系统数据(20201024)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201025;
CREATE TABLE upm.api_interface_system_data20201025 (
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
COMMENT ON TABLE upm.api_interface_system_data20201025 IS 'UPM系统数据(20201025)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201026;
CREATE TABLE upm.api_interface_system_data20201026 (
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
COMMENT ON TABLE upm.api_interface_system_data20201026 IS 'UPM系统数据(20201026)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201027;
CREATE TABLE upm.api_interface_system_data20201027 (
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
COMMENT ON TABLE upm.api_interface_system_data20201027 IS 'UPM系统数据(20201027)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201028;
CREATE TABLE upm.api_interface_system_data20201028 (
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
COMMENT ON TABLE upm.api_interface_system_data20201028 IS 'UPM系统数据(20201028)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201029;
CREATE TABLE upm.api_interface_system_data20201029 (
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
COMMENT ON TABLE upm.api_interface_system_data20201029 IS 'UPM系统数据(20201029)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201030;
CREATE TABLE upm.api_interface_system_data20201030 (
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
COMMENT ON TABLE upm.api_interface_system_data20201030 IS 'UPM系统数据(20201030)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201031;
CREATE TABLE upm.api_interface_system_data20201031 (
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
COMMENT ON TABLE upm.api_interface_system_data20201031 IS 'UPM系统数据(20201031)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201101;
CREATE TABLE upm.api_interface_system_data20201101 (
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
COMMENT ON TABLE upm.api_interface_system_data20201101 IS 'UPM系统数据(20201101)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201102;
CREATE TABLE upm.api_interface_system_data20201102 (
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
COMMENT ON TABLE upm.api_interface_system_data20201102 IS 'UPM系统数据(20201102)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201103;
CREATE TABLE upm.api_interface_system_data20201103 (
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
COMMENT ON TABLE upm.api_interface_system_data20201103 IS 'UPM系统数据(20201103)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201104;
CREATE TABLE upm.api_interface_system_data20201104 (
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
COMMENT ON TABLE upm.api_interface_system_data20201104 IS 'UPM系统数据(20201104)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201105;
CREATE TABLE upm.api_interface_system_data20201105 (
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

DROP TABLE IF EXISTS upm.api_interface_system_data20201106;
CREATE TABLE upm.api_interface_system_data20201106 (
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
COMMENT ON TABLE upm.api_interface_system_data20201106 IS 'UPM系统数据(20201106)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201107;
CREATE TABLE upm.api_interface_system_data20201107 (
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
COMMENT ON TABLE upm.api_interface_system_data20201107 IS 'UPM系统数据(20201107)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201108;
CREATE TABLE upm.api_interface_system_data20201108 (
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
COMMENT ON TABLE upm.api_interface_system_data20201108 IS 'UPM系统数据(20201108)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201109;
CREATE TABLE upm.api_interface_system_data20201109 (
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
COMMENT ON TABLE upm.api_interface_system_data20201109 IS 'UPM系统数据(20201109)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201110;
CREATE TABLE upm.api_interface_system_data20201110 (
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
COMMENT ON TABLE upm.api_interface_system_data20201110 IS 'UPM系统数据(20201110)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201111;
CREATE TABLE upm.api_interface_system_data20201111 (
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
COMMENT ON TABLE upm.api_interface_system_data20201111 IS 'UPM系统数据(20201111)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201112;
CREATE TABLE upm.api_interface_system_data20201112 (
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
COMMENT ON TABLE upm.api_interface_system_data20201112 IS 'UPM系统数据(20201112)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201113;
CREATE TABLE upm.api_interface_system_data20201113 (
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
COMMENT ON TABLE upm.api_interface_system_data20201113 IS 'UPM系统数据(20201113)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201114;
CREATE TABLE upm.api_interface_system_data20201114 (
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
COMMENT ON TABLE upm.api_interface_system_data20201114 IS 'UPM系统数据(20201114)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201115;
CREATE TABLE upm.api_interface_system_data20201115 (
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
COMMENT ON TABLE upm.api_interface_system_data20201115 IS 'UPM系统数据(20201115)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201116;
CREATE TABLE upm.api_interface_system_data20201116 (
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
COMMENT ON TABLE upm.api_interface_system_data20201116 IS 'UPM系统数据(20201116)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201117;
CREATE TABLE upm.api_interface_system_data20201117 (
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
COMMENT ON TABLE upm.api_interface_system_data20201117 IS 'UPM系统数据(20201117)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201118;
CREATE TABLE upm.api_interface_system_data20201118 (
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
COMMENT ON TABLE upm.api_interface_system_data20201118 IS 'UPM系统数据(20201118)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201119;
CREATE TABLE upm.api_interface_system_data20201119 (
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
COMMENT ON TABLE upm.api_interface_system_data20201119 IS 'UPM系统数据(20201119)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201120;
CREATE TABLE upm.api_interface_system_data20201120 (
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
COMMENT ON TABLE upm.api_interface_system_data20201120 IS 'UPM系统数据(20201120)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201121;
CREATE TABLE upm.api_interface_system_data20201121 (
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
COMMENT ON TABLE upm.api_interface_system_data20201121 IS 'UPM系统数据(20201121)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201122;
CREATE TABLE upm.api_interface_system_data20201122 (
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
COMMENT ON TABLE upm.api_interface_system_data20201122 IS 'UPM系统数据(20201122)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201123;
CREATE TABLE upm.api_interface_system_data20201123 (
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
COMMENT ON TABLE upm.api_interface_system_data20201123 IS 'UPM系统数据(20201123)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201124;
CREATE TABLE upm.api_interface_system_data20201124 (
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
COMMENT ON TABLE upm.api_interface_system_data20201124 IS 'UPM系统数据(20201124)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201125;
CREATE TABLE upm.api_interface_system_data20201125 (
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
COMMENT ON TABLE upm.api_interface_system_data20201125 IS 'UPM系统数据(20201125)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201126;
CREATE TABLE upm.api_interface_system_data20201126 (
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
COMMENT ON TABLE upm.api_interface_system_data20201126 IS 'UPM系统数据(20201126)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201127;
CREATE TABLE upm.api_interface_system_data20201127 (
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
COMMENT ON TABLE upm.api_interface_system_data20201127 IS 'UPM系统数据(20201127)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201128;
CREATE TABLE upm.api_interface_system_data20201128 (
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
COMMENT ON TABLE upm.api_interface_system_data20201128 IS 'UPM系统数据(20201128)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201129;
CREATE TABLE upm.api_interface_system_data20201129 (
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
COMMENT ON TABLE upm.api_interface_system_data20201129 IS 'UPM系统数据(20201129)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201130;
CREATE TABLE upm.api_interface_system_data20201130 (
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
COMMENT ON TABLE upm.api_interface_system_data20201130 IS 'UPM系统数据(20201130)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201201;
CREATE TABLE upm.api_interface_system_data20201201 (
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
COMMENT ON TABLE upm.api_interface_system_data20201201 IS 'UPM系统数据(20201201)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201202;
CREATE TABLE upm.api_interface_system_data20201202 (
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
COMMENT ON TABLE upm.api_interface_system_data20201202 IS 'UPM系统数据(20201202)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201203;
CREATE TABLE upm.api_interface_system_data20201203 (
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
COMMENT ON TABLE upm.api_interface_system_data20201203 IS 'UPM系统数据(20201203)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201204;
CREATE TABLE upm.api_interface_system_data20201204 (
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
COMMENT ON TABLE upm.api_interface_system_data20201204 IS 'UPM系统数据(20201204)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201205;
CREATE TABLE upm.api_interface_system_data20201205 (
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
COMMENT ON TABLE upm.api_interface_system_data20201205 IS 'UPM系统数据(20201205)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201206;
CREATE TABLE upm.api_interface_system_data20201206 (
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
COMMENT ON TABLE upm.api_interface_system_data20201206 IS 'UPM系统数据(20201206)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201207;
CREATE TABLE upm.api_interface_system_data20201207 (
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
COMMENT ON TABLE upm.api_interface_system_data20201207 IS 'UPM系统数据(20201207)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201208;
CREATE TABLE upm.api_interface_system_data20201208 (
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
COMMENT ON TABLE upm.api_interface_system_data20201208 IS 'UPM系统数据(20201208)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201209;
CREATE TABLE upm.api_interface_system_data20201209 (
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
COMMENT ON TABLE upm.api_interface_system_data20201209 IS 'UPM系统数据(20201209)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201210;
CREATE TABLE upm.api_interface_system_data20201210 (
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
COMMENT ON TABLE upm.api_interface_system_data20201210 IS 'UPM系统数据(20201210)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201211;
CREATE TABLE upm.api_interface_system_data20201211 (
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
COMMENT ON TABLE upm.api_interface_system_data20201211 IS 'UPM系统数据(20201211)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201212;
CREATE TABLE upm.api_interface_system_data20201212 (
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
COMMENT ON TABLE upm.api_interface_system_data20201212 IS 'UPM系统数据(20201212)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201213;
CREATE TABLE upm.api_interface_system_data20201213 (
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
COMMENT ON TABLE upm.api_interface_system_data20201213 IS 'UPM系统数据(20201213)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201214;
CREATE TABLE upm.api_interface_system_data20201214 (
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
COMMENT ON TABLE upm.api_interface_system_data20201214 IS 'UPM系统数据(20201214)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201215;
CREATE TABLE upm.api_interface_system_data20201215 (
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
COMMENT ON TABLE upm.api_interface_system_data20201215 IS 'UPM系统数据(20201215)';

DROP TABLE IF EXISTS upm.api_interface_system_data20201216;
CREATE TABLE upm.api_interface_system_data20201216 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.api_interface_system_data20201217;
CREATE TABLE upm.api_interface_system_data20201217 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.api_interface_system_data20201218;
CREATE TABLE upm.api_interface_system_data20201218 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.api_interface_system_data20201219;
CREATE TABLE upm.api_interface_system_data20201219 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.api_interface_system_data20201220;
CREATE TABLE upm.api_interface_system_data20201220 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.api_interface_system_data20201221;
CREATE TABLE upm.api_interface_system_data20201221 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.api_interface_system_data20201222;
CREATE TABLE upm.api_interface_system_data20201222 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.api_interface_system_data20201223;
CREATE TABLE upm.api_interface_system_data20201223 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.api_interface_system_data20201224;
CREATE TABLE upm.api_interface_system_data20201224 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.api_interface_system_data20201225;
CREATE TABLE upm.api_interface_system_data20201225 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.api_interface_system_data20201226;
CREATE TABLE upm.api_interface_system_data20201226 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.api_interface_system_data20201227;
CREATE TABLE upm.api_interface_system_data20201227 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.api_interface_system_data20201228;
CREATE TABLE upm.api_interface_system_data20201228 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.api_interface_system_data20201229;
CREATE TABLE upm.api_interface_system_data20201229 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.api_interface_system_data20201230;
CREATE TABLE upm.api_interface_system_data20201230 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.api_interface_system_data20201231;
CREATE TABLE upm.api_interface_system_data20201231 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.api_interface_system_data20210101;
CREATE TABLE upm.api_interface_system_data20210101 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.api_interface_system_data20210102;
CREATE TABLE upm.api_interface_system_data20210102 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.api_interface_system_data20210103;
CREATE TABLE upm.api_interface_system_data20210103 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.api_interface_system_data20210104;
CREATE TABLE upm.api_interface_system_data20210104 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.api_interface_system_data20210105;
CREATE TABLE upm.api_interface_system_data20210105 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.api_interface_system_data20210106;
CREATE TABLE upm.api_interface_system_data20210106 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.api_interface_system_data20210107;
CREATE TABLE upm.api_interface_system_data20210107 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.api_interface_system_data20210108;
CREATE TABLE upm.api_interface_system_data20210108 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.api_interface_system_data20210109;
CREATE TABLE upm.api_interface_system_data20210109 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.api_interface_system_data20210110;
CREATE TABLE upm.api_interface_system_data20210110 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.api_interface_system_data20210111;
CREATE TABLE upm.api_interface_system_data20210111 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.api_interface_system_data20210112;
CREATE TABLE upm.api_interface_system_data20210112 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.api_interface_system_data20210113;
CREATE TABLE upm.api_interface_system_data20210113 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.api_interface_system_data20210114;
CREATE TABLE upm.api_interface_system_data20210114 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.api_interface_system_data20210115;
CREATE TABLE upm.api_interface_system_data20210115 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.api_interface_system_data20210116;
CREATE TABLE upm.api_interface_system_data20210116 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.api_interface_system_data20210117;
CREATE TABLE upm.api_interface_system_data20210117 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.api_interface_system_data20210118;
CREATE TABLE upm.api_interface_system_data20210118 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.api_interface_system_data20210119;
CREATE TABLE upm.api_interface_system_data20210119 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.api_interface_system_data20210120;
CREATE TABLE upm.api_interface_system_data20210120 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.api_interface_system_data20210121;
CREATE TABLE upm.api_interface_system_data20210121 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.api_interface_system_data20210122;
CREATE TABLE upm.api_interface_system_data20210122 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.api_interface_system_data20210123;
CREATE TABLE upm.api_interface_system_data20210123 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.api_interface_system_data20210124;
CREATE TABLE upm.api_interface_system_data20210124 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.api_interface_system_data20210125;
CREATE TABLE upm.api_interface_system_data20210125 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.api_interface_system_data20210126;
CREATE TABLE upm.api_interface_system_data20210126 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.api_interface_system_data20210127;
CREATE TABLE upm.api_interface_system_data20210127 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.api_interface_system_data20210128;
CREATE TABLE upm.api_interface_system_data20210128 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.api_interface_system_data20210129;
CREATE TABLE upm.api_interface_system_data20210129 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.api_interface_system_data20210130;
CREATE TABLE upm.api_interface_system_data20210130 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.api_interface_system_data20210131;
CREATE TABLE upm.api_interface_system_data20210131 (
    ID varchar(32) NOT NULL,
    system_id varchar(32) DEFAULT NULL,
    platform_id varchar(32) DEFAULT NULL,
    index_id varchar(32) DEFAULT NULL,
    resource_id varchar(32) DEFAULT NULL,
    resource_code varchar(500) DEFAULT NULL,
    resource_ip varchar(32) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    content text NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.api_interface_system_data20210201;
CREATE TABLE upm.api_interface_system_data20210201 (
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
COMMENT ON TABLE upm.api_interface_system_data20210201 IS 'UPM系统数据(20210201)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210202;
CREATE TABLE upm.api_interface_system_data20210202 (
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
COMMENT ON TABLE upm.api_interface_system_data20210202 IS 'UPM系统数据(20210202)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210203;
CREATE TABLE upm.api_interface_system_data20210203 (
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
COMMENT ON TABLE upm.api_interface_system_data20210203 IS 'UPM系统数据(20210203)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210204;
CREATE TABLE upm.api_interface_system_data20210204 (
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
COMMENT ON TABLE upm.api_interface_system_data20210204 IS 'UPM系统数据(20210204)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210205;
CREATE TABLE upm.api_interface_system_data20210205 (
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
COMMENT ON TABLE upm.api_interface_system_data20210205 IS 'UPM系统数据(20210205)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210206;
CREATE TABLE upm.api_interface_system_data20210206 (
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
COMMENT ON TABLE upm.api_interface_system_data20210206 IS 'UPM系统数据(20210206)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210207;
CREATE TABLE upm.api_interface_system_data20210207 (
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
COMMENT ON TABLE upm.api_interface_system_data20210207 IS 'UPM系统数据(20210207)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210208;
CREATE TABLE upm.api_interface_system_data20210208 (
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
COMMENT ON TABLE upm.api_interface_system_data20210208 IS 'UPM系统数据(20210208)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210209;
CREATE TABLE upm.api_interface_system_data20210209 (
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
COMMENT ON TABLE upm.api_interface_system_data20210209 IS 'UPM系统数据(20210209)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210210;
CREATE TABLE upm.api_interface_system_data20210210 (
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
COMMENT ON TABLE upm.api_interface_system_data20210210 IS 'UPM系统数据(20210210)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210211;
CREATE TABLE upm.api_interface_system_data20210211 (
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
COMMENT ON TABLE upm.api_interface_system_data20210211 IS 'UPM系统数据(20210211)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210212;
CREATE TABLE upm.api_interface_system_data20210212 (
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
COMMENT ON TABLE upm.api_interface_system_data20210212 IS 'UPM系统数据(20210212)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210213;
CREATE TABLE upm.api_interface_system_data20210213 (
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
COMMENT ON TABLE upm.api_interface_system_data20210213 IS 'UPM系统数据(20210213)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210214;
CREATE TABLE upm.api_interface_system_data20210214 (
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
COMMENT ON TABLE upm.api_interface_system_data20210214 IS 'UPM系统数据(20210214)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210215;
CREATE TABLE upm.api_interface_system_data20210215 (
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
COMMENT ON TABLE upm.api_interface_system_data20210215 IS 'UPM系统数据(20210215)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210216;
CREATE TABLE upm.api_interface_system_data20210216 (
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
COMMENT ON TABLE upm.api_interface_system_data20210216 IS 'UPM系统数据(20210216)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210217;
CREATE TABLE upm.api_interface_system_data20210217 (
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
COMMENT ON TABLE upm.api_interface_system_data20210217 IS 'UPM系统数据(20210217)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210218;
CREATE TABLE upm.api_interface_system_data20210218 (
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
COMMENT ON TABLE upm.api_interface_system_data20210218 IS 'UPM系统数据(20210218)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210219;
CREATE TABLE upm.api_interface_system_data20210219 (
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
COMMENT ON TABLE upm.api_interface_system_data20210219 IS 'UPM系统数据(20210219)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210220;
CREATE TABLE upm.api_interface_system_data20210220 (
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
COMMENT ON TABLE upm.api_interface_system_data20210220 IS 'UPM系统数据(20210220)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210221;
CREATE TABLE upm.api_interface_system_data20210221 (
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
COMMENT ON TABLE upm.api_interface_system_data20210221 IS 'UPM系统数据(20210221)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210222;
CREATE TABLE upm.api_interface_system_data20210222 (
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
COMMENT ON TABLE upm.api_interface_system_data20210222 IS 'UPM系统数据(20210222)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210223;
CREATE TABLE upm.api_interface_system_data20210223 (
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
COMMENT ON TABLE upm.api_interface_system_data20210223 IS 'UPM系统数据(20210223)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210224;
CREATE TABLE upm.api_interface_system_data20210224 (
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
COMMENT ON TABLE upm.api_interface_system_data20210224 IS 'UPM系统数据(20210224)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210225;
CREATE TABLE upm.api_interface_system_data20210225 (
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
COMMENT ON TABLE upm.api_interface_system_data20210225 IS 'UPM系统数据(20210225)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210226;
CREATE TABLE upm.api_interface_system_data20210226 (
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
COMMENT ON TABLE upm.api_interface_system_data20210226 IS 'UPM系统数据(20210226)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210227;
CREATE TABLE upm.api_interface_system_data20210227 (
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
COMMENT ON TABLE upm.api_interface_system_data20210227 IS 'UPM系统数据(20210227)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210228;
CREATE TABLE upm.api_interface_system_data20210228 (
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
COMMENT ON TABLE upm.api_interface_system_data20210228 IS 'UPM系统数据(20210228)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210301;
CREATE TABLE upm.api_interface_system_data20210301 (
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
COMMENT ON TABLE upm.api_interface_system_data20210301 IS 'UPM系统数据(20210301)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210302;
CREATE TABLE upm.api_interface_system_data20210302 (
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
COMMENT ON TABLE upm.api_interface_system_data20210302 IS 'UPM系统数据(20210302)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210303;
CREATE TABLE upm.api_interface_system_data20210303 (
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
COMMENT ON TABLE upm.api_interface_system_data20210303 IS 'UPM系统数据(20210303)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210304;
CREATE TABLE upm.api_interface_system_data20210304 (
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
COMMENT ON TABLE upm.api_interface_system_data20210304 IS 'UPM系统数据(20210304)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210305;
CREATE TABLE upm.api_interface_system_data20210305 (
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
COMMENT ON TABLE upm.api_interface_system_data20210305 IS 'UPM系统数据(20210305)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210306;
CREATE TABLE upm.api_interface_system_data20210306 (
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
COMMENT ON TABLE upm.api_interface_system_data20210306 IS 'UPM系统数据(20210306)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210307;
CREATE TABLE upm.api_interface_system_data20210307 (
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
COMMENT ON TABLE upm.api_interface_system_data20210307 IS 'UPM系统数据(20210307)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210308;
CREATE TABLE upm.api_interface_system_data20210308 (
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
COMMENT ON TABLE upm.api_interface_system_data20210308 IS 'UPM系统数据(20210308)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210309;
CREATE TABLE upm.api_interface_system_data20210309 (
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
COMMENT ON TABLE upm.api_interface_system_data20210309 IS 'UPM系统数据(20210309)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210310;
CREATE TABLE upm.api_interface_system_data20210310 (
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
COMMENT ON TABLE upm.api_interface_system_data20210310 IS 'UPM系统数据(20210310)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210311;
CREATE TABLE upm.api_interface_system_data20210311 (
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
COMMENT ON TABLE upm.api_interface_system_data20210311 IS 'UPM系统数据(20210311)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210312;
CREATE TABLE upm.api_interface_system_data20210312 (
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
COMMENT ON TABLE upm.api_interface_system_data20210312 IS 'UPM系统数据(20210312)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210313;
CREATE TABLE upm.api_interface_system_data20210313 (
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
COMMENT ON TABLE upm.api_interface_system_data20210313 IS 'UPM系统数据(20210313)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210314;
CREATE TABLE upm.api_interface_system_data20210314 (
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
COMMENT ON TABLE upm.api_interface_system_data20210314 IS 'UPM系统数据(20210314)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210315;
CREATE TABLE upm.api_interface_system_data20210315 (
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
COMMENT ON TABLE upm.api_interface_system_data20210315 IS 'UPM系统数据(20210315)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210316;
CREATE TABLE upm.api_interface_system_data20210316 (
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
COMMENT ON TABLE upm.api_interface_system_data20210316 IS 'UPM系统数据(20210316)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210317;
CREATE TABLE upm.api_interface_system_data20210317 (
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
COMMENT ON TABLE upm.api_interface_system_data20210317 IS 'UPM系统数据(20210317)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210318;
CREATE TABLE upm.api_interface_system_data20210318 (
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
COMMENT ON TABLE upm.api_interface_system_data20210318 IS 'UPM系统数据(20210318)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210319;
CREATE TABLE upm.api_interface_system_data20210319 (
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
COMMENT ON TABLE upm.api_interface_system_data20210319 IS 'UPM系统数据(20210319)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210320;
CREATE TABLE upm.api_interface_system_data20210320 (
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
COMMENT ON TABLE upm.api_interface_system_data20210320 IS 'UPM系统数据(20210320)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210321;
CREATE TABLE upm.api_interface_system_data20210321 (
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
COMMENT ON TABLE upm.api_interface_system_data20210321 IS 'UPM系统数据(20210321)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210322;
CREATE TABLE upm.api_interface_system_data20210322 (
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
COMMENT ON TABLE upm.api_interface_system_data20210322 IS 'UPM系统数据(20210322)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210323;
CREATE TABLE upm.api_interface_system_data20210323 (
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
COMMENT ON TABLE upm.api_interface_system_data20210323 IS 'UPM系统数据(20210323)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210324;
CREATE TABLE upm.api_interface_system_data20210324 (
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
COMMENT ON TABLE upm.api_interface_system_data20210324 IS 'UPM系统数据(20210324)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210325;
CREATE TABLE upm.api_interface_system_data20210325 (
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
COMMENT ON TABLE upm.api_interface_system_data20210325 IS 'UPM系统数据(20210325)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210326;
CREATE TABLE upm.api_interface_system_data20210326 (
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
COMMENT ON TABLE upm.api_interface_system_data20210326 IS 'UPM系统数据(20210326)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210327;
CREATE TABLE upm.api_interface_system_data20210327 (
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
COMMENT ON TABLE upm.api_interface_system_data20210327 IS 'UPM系统数据(20210327)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210328;
CREATE TABLE upm.api_interface_system_data20210328 (
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
COMMENT ON TABLE upm.api_interface_system_data20210328 IS 'UPM系统数据(20210328)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210329;
CREATE TABLE upm.api_interface_system_data20210329 (
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
COMMENT ON TABLE upm.api_interface_system_data20210329 IS 'UPM系统数据(20210329)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210330;
CREATE TABLE upm.api_interface_system_data20210330 (
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
COMMENT ON TABLE upm.api_interface_system_data20210330 IS 'UPM系统数据(20210330)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210331;
CREATE TABLE upm.api_interface_system_data20210331 (
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
COMMENT ON TABLE upm.api_interface_system_data20210331 IS 'UPM系统数据(20210331)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210401;
CREATE TABLE upm.api_interface_system_data20210401 (
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
COMMENT ON TABLE upm.api_interface_system_data20210401 IS 'UPM系统数据(20210401)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210402;
CREATE TABLE upm.api_interface_system_data20210402 (
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
COMMENT ON TABLE upm.api_interface_system_data20210402 IS 'UPM系统数据(20210402)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210403;
CREATE TABLE upm.api_interface_system_data20210403 (
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
COMMENT ON TABLE upm.api_interface_system_data20210403 IS 'UPM系统数据(20210403)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210404;
CREATE TABLE upm.api_interface_system_data20210404 (
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
COMMENT ON TABLE upm.api_interface_system_data20210404 IS 'UPM系统数据(20210404)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210405;
CREATE TABLE upm.api_interface_system_data20210405 (
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
COMMENT ON TABLE upm.api_interface_system_data20210405 IS 'UPM系统数据(20210405)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210406;
CREATE TABLE upm.api_interface_system_data20210406 (
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
COMMENT ON TABLE upm.api_interface_system_data20210406 IS 'UPM系统数据(20210406)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210407;
CREATE TABLE upm.api_interface_system_data20210407 (
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
COMMENT ON TABLE upm.api_interface_system_data20210407 IS 'UPM系统数据(20210407)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210408;
CREATE TABLE upm.api_interface_system_data20210408 (
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
COMMENT ON TABLE upm.api_interface_system_data20210408 IS 'UPM系统数据(20210408)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210409;
CREATE TABLE upm.api_interface_system_data20210409 (
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
COMMENT ON TABLE upm.api_interface_system_data20210409 IS 'UPM系统数据(20210409)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210410;
CREATE TABLE upm.api_interface_system_data20210410 (
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
COMMENT ON TABLE upm.api_interface_system_data20210410 IS 'UPM系统数据(20210410)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210411;
CREATE TABLE upm.api_interface_system_data20210411 (
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
COMMENT ON TABLE upm.api_interface_system_data20210411 IS 'UPM系统数据(20210411)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210412;
CREATE TABLE upm.api_interface_system_data20210412 (
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
COMMENT ON TABLE upm.api_interface_system_data20210412 IS 'UPM系统数据(20210412)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210413;
CREATE TABLE upm.api_interface_system_data20210413 (
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
COMMENT ON TABLE upm.api_interface_system_data20210413 IS 'UPM系统数据(20210413)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210414;
CREATE TABLE upm.api_interface_system_data20210414 (
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
COMMENT ON TABLE upm.api_interface_system_data20210414 IS 'UPM系统数据(20210414)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210415;
CREATE TABLE upm.api_interface_system_data20210415 (
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
COMMENT ON TABLE upm.api_interface_system_data20210415 IS 'UPM系统数据(20210415)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210416;
CREATE TABLE upm.api_interface_system_data20210416 (
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
COMMENT ON TABLE upm.api_interface_system_data20210416 IS 'UPM系统数据(20210416)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210417;
CREATE TABLE upm.api_interface_system_data20210417 (
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
COMMENT ON TABLE upm.api_interface_system_data20210417 IS 'UPM系统数据(20210417)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210418;
CREATE TABLE upm.api_interface_system_data20210418 (
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
COMMENT ON TABLE upm.api_interface_system_data20210418 IS 'UPM系统数据(20210418)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210419;
CREATE TABLE upm.api_interface_system_data20210419 (
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
COMMENT ON TABLE upm.api_interface_system_data20210419 IS 'UPM系统数据(20210419)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210420;
CREATE TABLE upm.api_interface_system_data20210420 (
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
COMMENT ON TABLE upm.api_interface_system_data20210420 IS 'UPM系统数据(20210420)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210421;
CREATE TABLE upm.api_interface_system_data20210421 (
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
COMMENT ON TABLE upm.api_interface_system_data20210421 IS 'UPM系统数据(20210421)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210422;
CREATE TABLE upm.api_interface_system_data20210422 (
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
COMMENT ON TABLE upm.api_interface_system_data20210422 IS 'UPM系统数据(20210422)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210423;
CREATE TABLE upm.api_interface_system_data20210423 (
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
COMMENT ON TABLE upm.api_interface_system_data20210423 IS 'UPM系统数据(20210423)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210424;
CREATE TABLE upm.api_interface_system_data20210424 (
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
COMMENT ON TABLE upm.api_interface_system_data20210424 IS 'UPM系统数据(20210424)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210425;
CREATE TABLE upm.api_interface_system_data20210425 (
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
COMMENT ON TABLE upm.api_interface_system_data20210425 IS 'UPM系统数据(20210425)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210426;
CREATE TABLE upm.api_interface_system_data20210426 (
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
COMMENT ON TABLE upm.api_interface_system_data20210426 IS 'UPM系统数据(20210426)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210427;
CREATE TABLE upm.api_interface_system_data20210427 (
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
COMMENT ON TABLE upm.api_interface_system_data20210427 IS 'UPM系统数据(20210427)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210428;
CREATE TABLE upm.api_interface_system_data20210428 (
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
COMMENT ON TABLE upm.api_interface_system_data20210428 IS 'UPM系统数据(20210428)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210429;
CREATE TABLE upm.api_interface_system_data20210429 (
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
COMMENT ON TABLE upm.api_interface_system_data20210429 IS 'UPM系统数据(20210429)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210430;
CREATE TABLE upm.api_interface_system_data20210430 (
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
COMMENT ON TABLE upm.api_interface_system_data20210430 IS 'UPM系统数据(20210430)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210501;
CREATE TABLE upm.api_interface_system_data20210501 (
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
COMMENT ON TABLE upm.api_interface_system_data20210501 IS 'UPM系统数据(20210501)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210502;
CREATE TABLE upm.api_interface_system_data20210502 (
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
COMMENT ON TABLE upm.api_interface_system_data20210502 IS 'UPM系统数据(20210502)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210503;
CREATE TABLE upm.api_interface_system_data20210503 (
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
COMMENT ON TABLE upm.api_interface_system_data20210503 IS 'UPM系统数据(20210503)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210504;
CREATE TABLE upm.api_interface_system_data20210504 (
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
COMMENT ON TABLE upm.api_interface_system_data20210504 IS 'UPM系统数据(20210504)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210505;
CREATE TABLE upm.api_interface_system_data20210505 (
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
COMMENT ON TABLE upm.api_interface_system_data20210505 IS 'UPM系统数据(20210505)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210506;
CREATE TABLE upm.api_interface_system_data20210506 (
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
COMMENT ON TABLE upm.api_interface_system_data20210506 IS 'UPM系统数据(20210506)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210507;
CREATE TABLE upm.api_interface_system_data20210507 (
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
COMMENT ON TABLE upm.api_interface_system_data20210507 IS 'UPM系统数据(20210507)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210508;
CREATE TABLE upm.api_interface_system_data20210508 (
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
COMMENT ON TABLE upm.api_interface_system_data20210508 IS 'UPM系统数据(20210508)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210509;
CREATE TABLE upm.api_interface_system_data20210509 (
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
COMMENT ON TABLE upm.api_interface_system_data20210509 IS 'UPM系统数据(20210509)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210510;
CREATE TABLE upm.api_interface_system_data20210510 (
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
COMMENT ON TABLE upm.api_interface_system_data20210510 IS 'UPM系统数据(20210510)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210511;
CREATE TABLE upm.api_interface_system_data20210511 (
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
COMMENT ON TABLE upm.api_interface_system_data20210511 IS 'UPM系统数据(20210511)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210512;
CREATE TABLE upm.api_interface_system_data20210512 (
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
COMMENT ON TABLE upm.api_interface_system_data20210512 IS 'UPM系统数据(20210512)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210513;
CREATE TABLE upm.api_interface_system_data20210513 (
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
COMMENT ON TABLE upm.api_interface_system_data20210513 IS 'UPM系统数据(20210513)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210514;
CREATE TABLE upm.api_interface_system_data20210514 (
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
COMMENT ON TABLE upm.api_interface_system_data20210514 IS 'UPM系统数据(20210514)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210515;
CREATE TABLE upm.api_interface_system_data20210515 (
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
COMMENT ON TABLE upm.api_interface_system_data20210515 IS 'UPM系统数据(20210515)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210516;
CREATE TABLE upm.api_interface_system_data20210516 (
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
COMMENT ON TABLE upm.api_interface_system_data20210516 IS 'UPM系统数据(20210516)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210517;
CREATE TABLE upm.api_interface_system_data20210517 (
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
COMMENT ON TABLE upm.api_interface_system_data20210517 IS 'UPM系统数据(20210517)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210518;
CREATE TABLE upm.api_interface_system_data20210518 (
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
COMMENT ON TABLE upm.api_interface_system_data20210518 IS 'UPM系统数据(20210518)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210519;
CREATE TABLE upm.api_interface_system_data20210519 (
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
COMMENT ON TABLE upm.api_interface_system_data20210519 IS 'UPM系统数据(20210519)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210520;
CREATE TABLE upm.api_interface_system_data20210520 (
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
COMMENT ON TABLE upm.api_interface_system_data20210520 IS 'UPM系统数据(20210520)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210521;
CREATE TABLE upm.api_interface_system_data20210521 (
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
COMMENT ON TABLE upm.api_interface_system_data20210521 IS 'UPM系统数据(20210521)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210522;
CREATE TABLE upm.api_interface_system_data20210522 (
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
COMMENT ON TABLE upm.api_interface_system_data20210522 IS 'UPM系统数据(20210522)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210523;
CREATE TABLE upm.api_interface_system_data20210523 (
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
COMMENT ON TABLE upm.api_interface_system_data20210523 IS 'UPM系统数据(20210523)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210524;
CREATE TABLE upm.api_interface_system_data20210524 (
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
COMMENT ON TABLE upm.api_interface_system_data20210524 IS 'UPM系统数据(20210524)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210525;
CREATE TABLE upm.api_interface_system_data20210525 (
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
COMMENT ON TABLE upm.api_interface_system_data20210525 IS 'UPM系统数据(20210525)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210526;
CREATE TABLE upm.api_interface_system_data20210526 (
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
COMMENT ON TABLE upm.api_interface_system_data20210526 IS 'UPM系统数据(20210526)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210527;
CREATE TABLE upm.api_interface_system_data20210527 (
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
COMMENT ON TABLE upm.api_interface_system_data20210527 IS 'UPM系统数据(20210527)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210528;
CREATE TABLE upm.api_interface_system_data20210528 (
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
COMMENT ON TABLE upm.api_interface_system_data20210528 IS 'UPM系统数据(20210528)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210529;
CREATE TABLE upm.api_interface_system_data20210529 (
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
COMMENT ON TABLE upm.api_interface_system_data20210529 IS 'UPM系统数据(20210529)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210530;
CREATE TABLE upm.api_interface_system_data20210530 (
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
COMMENT ON TABLE upm.api_interface_system_data20210530 IS 'UPM系统数据(20210530)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210531;
CREATE TABLE upm.api_interface_system_data20210531 (
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
COMMENT ON TABLE upm.api_interface_system_data20210531 IS 'UPM系统数据(20210531)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210601;
CREATE TABLE upm.api_interface_system_data20210601 (
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
COMMENT ON TABLE upm.api_interface_system_data20210601 IS 'UPM系统数据(20210601)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210602;
CREATE TABLE upm.api_interface_system_data20210602 (
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
COMMENT ON TABLE upm.api_interface_system_data20210602 IS 'UPM系统数据(20210602)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210603;
CREATE TABLE upm.api_interface_system_data20210603 (
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
COMMENT ON TABLE upm.api_interface_system_data20210603 IS 'UPM系统数据(20210603)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210604;
CREATE TABLE upm.api_interface_system_data20210604 (
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
COMMENT ON TABLE upm.api_interface_system_data20210604 IS 'UPM系统数据(20210604)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210605;
CREATE TABLE upm.api_interface_system_data20210605 (
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
COMMENT ON TABLE upm.api_interface_system_data20210605 IS 'UPM系统数据(20210605)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210606;
CREATE TABLE upm.api_interface_system_data20210606 (
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
COMMENT ON TABLE upm.api_interface_system_data20210606 IS 'UPM系统数据(20210606)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210607;
CREATE TABLE upm.api_interface_system_data20210607 (
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
COMMENT ON TABLE upm.api_interface_system_data20210607 IS 'UPM系统数据(20210607)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210608;
CREATE TABLE upm.api_interface_system_data20210608 (
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
COMMENT ON TABLE upm.api_interface_system_data20210608 IS 'UPM系统数据(20210608)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210609;
CREATE TABLE upm.api_interface_system_data20210609 (
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
COMMENT ON TABLE upm.api_interface_system_data20210609 IS 'UPM系统数据(20210609)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210610;
CREATE TABLE upm.api_interface_system_data20210610 (
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
COMMENT ON TABLE upm.api_interface_system_data20210610 IS 'UPM系统数据(20210610)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210611;
CREATE TABLE upm.api_interface_system_data20210611 (
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
COMMENT ON TABLE upm.api_interface_system_data20210611 IS 'UPM系统数据(20210611)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210612;
CREATE TABLE upm.api_interface_system_data20210612 (
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
COMMENT ON TABLE upm.api_interface_system_data20210612 IS 'UPM系统数据(20210612)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210613;
CREATE TABLE upm.api_interface_system_data20210613 (
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
COMMENT ON TABLE upm.api_interface_system_data20210613 IS 'UPM系统数据(20210613)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210614;
CREATE TABLE upm.api_interface_system_data20210614 (
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
COMMENT ON TABLE upm.api_interface_system_data20210614 IS 'UPM系统数据(20210614)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210615;
CREATE TABLE upm.api_interface_system_data20210615 (
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
COMMENT ON TABLE upm.api_interface_system_data20210615 IS 'UPM系统数据(20210615)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210616;
CREATE TABLE upm.api_interface_system_data20210616 (
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
COMMENT ON TABLE upm.api_interface_system_data20210616 IS 'UPM系统数据(20210616)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210617;
CREATE TABLE upm.api_interface_system_data20210617 (
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
COMMENT ON TABLE upm.api_interface_system_data20210617 IS 'UPM系统数据(20210617)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210618;
CREATE TABLE upm.api_interface_system_data20210618 (
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
COMMENT ON TABLE upm.api_interface_system_data20210618 IS 'UPM系统数据(20210618)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210619;
CREATE TABLE upm.api_interface_system_data20210619 (
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
COMMENT ON TABLE upm.api_interface_system_data20210619 IS 'UPM系统数据(20210619)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210620;
CREATE TABLE upm.api_interface_system_data20210620 (
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
COMMENT ON TABLE upm.api_interface_system_data20210620 IS 'UPM系统数据(20210620)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210621;
CREATE TABLE upm.api_interface_system_data20210621 (
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
COMMENT ON TABLE upm.api_interface_system_data20210621 IS 'UPM系统数据(20210621)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210622;
CREATE TABLE upm.api_interface_system_data20210622 (
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
COMMENT ON TABLE upm.api_interface_system_data20210622 IS 'UPM系统数据(20210622)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210623;
CREATE TABLE upm.api_interface_system_data20210623 (
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
COMMENT ON TABLE upm.api_interface_system_data20210623 IS 'UPM系统数据(20210623)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210624;
CREATE TABLE upm.api_interface_system_data20210624 (
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
COMMENT ON TABLE upm.api_interface_system_data20210624 IS 'UPM系统数据(20210624)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210625;
CREATE TABLE upm.api_interface_system_data20210625 (
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
COMMENT ON TABLE upm.api_interface_system_data20210625 IS 'UPM系统数据(20210625)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210626;
CREATE TABLE upm.api_interface_system_data20210626 (
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
COMMENT ON TABLE upm.api_interface_system_data20210626 IS 'UPM系统数据(20210626)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210627;
CREATE TABLE upm.api_interface_system_data20210627 (
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
COMMENT ON TABLE upm.api_interface_system_data20210627 IS 'UPM系统数据(20210627)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210628;
CREATE TABLE upm.api_interface_system_data20210628 (
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
COMMENT ON TABLE upm.api_interface_system_data20210628 IS 'UPM系统数据(20210628)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210629;
CREATE TABLE upm.api_interface_system_data20210629 (
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
COMMENT ON TABLE upm.api_interface_system_data20210629 IS 'UPM系统数据(20210629)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210630;
CREATE TABLE upm.api_interface_system_data20210630 (
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
COMMENT ON TABLE upm.api_interface_system_data20210630 IS 'UPM系统数据(20210630)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210701;
CREATE TABLE upm.api_interface_system_data20210701 (
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
COMMENT ON TABLE upm.api_interface_system_data20210701 IS 'UPM系统数据(20210701)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210702;
CREATE TABLE upm.api_interface_system_data20210702 (
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
COMMENT ON TABLE upm.api_interface_system_data20210702 IS 'UPM系统数据(20210702)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210703;
CREATE TABLE upm.api_interface_system_data20210703 (
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
COMMENT ON TABLE upm.api_interface_system_data20210703 IS 'UPM系统数据(20210703)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210704;
CREATE TABLE upm.api_interface_system_data20210704 (
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
COMMENT ON TABLE upm.api_interface_system_data20210704 IS 'UPM系统数据(20210704)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210705;
CREATE TABLE upm.api_interface_system_data20210705 (
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
COMMENT ON TABLE upm.api_interface_system_data20210705 IS 'UPM系统数据(20210705)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210706;
CREATE TABLE upm.api_interface_system_data20210706 (
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
COMMENT ON TABLE upm.api_interface_system_data20210706 IS 'UPM系统数据(20210706)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210707;
CREATE TABLE upm.api_interface_system_data20210707 (
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
COMMENT ON TABLE upm.api_interface_system_data20210707 IS 'UPM系统数据(20210707)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210708;
CREATE TABLE upm.api_interface_system_data20210708 (
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
COMMENT ON TABLE upm.api_interface_system_data20210708 IS 'UPM系统数据(20210708)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210709;
CREATE TABLE upm.api_interface_system_data20210709 (
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
COMMENT ON TABLE upm.api_interface_system_data20210709 IS 'UPM系统数据(20210709)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210710;
CREATE TABLE upm.api_interface_system_data20210710 (
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
COMMENT ON TABLE upm.api_interface_system_data20210710 IS 'UPM系统数据(20210710)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210711;
CREATE TABLE upm.api_interface_system_data20210711 (
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
COMMENT ON TABLE upm.api_interface_system_data20210711 IS 'UPM系统数据(20210711)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210712;
CREATE TABLE upm.api_interface_system_data20210712 (
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
COMMENT ON TABLE upm.api_interface_system_data20210712 IS 'UPM系统数据(20210712)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210713;
CREATE TABLE upm.api_interface_system_data20210713 (
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
COMMENT ON TABLE upm.api_interface_system_data20210713 IS 'UPM系统数据(20210713)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210714;
CREATE TABLE upm.api_interface_system_data20210714 (
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
COMMENT ON TABLE upm.api_interface_system_data20210714 IS 'UPM系统数据(20210714)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210715;
CREATE TABLE upm.api_interface_system_data20210715 (
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
COMMENT ON TABLE upm.api_interface_system_data20210715 IS 'UPM系统数据(20210715)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210716;
CREATE TABLE upm.api_interface_system_data20210716 (
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
COMMENT ON TABLE upm.api_interface_system_data20210716 IS 'UPM系统数据(20210716)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210717;
CREATE TABLE upm.api_interface_system_data20210717 (
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
COMMENT ON TABLE upm.api_interface_system_data20210717 IS 'UPM系统数据(20210717)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210718;
CREATE TABLE upm.api_interface_system_data20210718 (
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
COMMENT ON TABLE upm.api_interface_system_data20210718 IS 'UPM系统数据(20210718)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210719;
CREATE TABLE upm.api_interface_system_data20210719 (
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
COMMENT ON TABLE upm.api_interface_system_data20210719 IS 'UPM系统数据(20210719)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210720;
CREATE TABLE upm.api_interface_system_data20210720 (
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
COMMENT ON TABLE upm.api_interface_system_data20210720 IS 'UPM系统数据(20210720)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210721;
CREATE TABLE upm.api_interface_system_data20210721 (
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
COMMENT ON TABLE upm.api_interface_system_data20210721 IS 'UPM系统数据(20210721)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210722;
CREATE TABLE upm.api_interface_system_data20210722 (
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
COMMENT ON TABLE upm.api_interface_system_data20210722 IS 'UPM系统数据(20210722)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210723;
CREATE TABLE upm.api_interface_system_data20210723 (
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
COMMENT ON TABLE upm.api_interface_system_data20210723 IS 'UPM系统数据(20210723)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210724;
CREATE TABLE upm.api_interface_system_data20210724 (
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
COMMENT ON TABLE upm.api_interface_system_data20210724 IS 'UPM系统数据(20210724)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210725;
CREATE TABLE upm.api_interface_system_data20210725 (
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
COMMENT ON TABLE upm.api_interface_system_data20210725 IS 'UPM系统数据(20210725)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210726;
CREATE TABLE upm.api_interface_system_data20210726 (
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
COMMENT ON TABLE upm.api_interface_system_data20210726 IS 'UPM系统数据(20210726)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210727;
CREATE TABLE upm.api_interface_system_data20210727 (
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
COMMENT ON TABLE upm.api_interface_system_data20210727 IS 'UPM系统数据(20210727)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210728;
CREATE TABLE upm.api_interface_system_data20210728 (
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
COMMENT ON TABLE upm.api_interface_system_data20210728 IS 'UPM系统数据(20210728)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210729;
CREATE TABLE upm.api_interface_system_data20210729 (
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
COMMENT ON TABLE upm.api_interface_system_data20210729 IS 'UPM系统数据(20210729)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210730;
CREATE TABLE upm.api_interface_system_data20210730 (
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
COMMENT ON TABLE upm.api_interface_system_data20210730 IS 'UPM系统数据(20210730)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210731;
CREATE TABLE upm.api_interface_system_data20210731 (
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
COMMENT ON TABLE upm.api_interface_system_data20210731 IS 'UPM系统数据(20210731)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210801;
CREATE TABLE upm.api_interface_system_data20210801 (
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
COMMENT ON TABLE upm.api_interface_system_data20210801 IS 'UPM系统数据(20210801)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210802;
CREATE TABLE upm.api_interface_system_data20210802 (
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
COMMENT ON TABLE upm.api_interface_system_data20210802 IS 'UPM系统数据(20210802)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210803;
CREATE TABLE upm.api_interface_system_data20210803 (
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
COMMENT ON TABLE upm.api_interface_system_data20210803 IS 'UPM系统数据(20210803)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210804;
CREATE TABLE upm.api_interface_system_data20210804 (
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
COMMENT ON TABLE upm.api_interface_system_data20210804 IS 'UPM系统数据(20210804)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210805;
CREATE TABLE upm.api_interface_system_data20210805 (
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
COMMENT ON TABLE upm.api_interface_system_data20210805 IS 'UPM系统数据(20210805)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210806;
CREATE TABLE upm.api_interface_system_data20210806 (
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
COMMENT ON TABLE upm.api_interface_system_data20210806 IS 'UPM系统数据(20210806)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210807;
CREATE TABLE upm.api_interface_system_data20210807 (
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
COMMENT ON TABLE upm.api_interface_system_data20210807 IS 'UPM系统数据(20210807)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210808;
CREATE TABLE upm.api_interface_system_data20210808 (
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
COMMENT ON TABLE upm.api_interface_system_data20210808 IS 'UPM系统数据(20210808)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210809;
CREATE TABLE upm.api_interface_system_data20210809 (
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
COMMENT ON TABLE upm.api_interface_system_data20210809 IS 'UPM系统数据(20210809)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210810;
CREATE TABLE upm.api_interface_system_data20210810 (
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
COMMENT ON TABLE upm.api_interface_system_data20210810 IS 'UPM系统数据(20210810)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210811;
CREATE TABLE upm.api_interface_system_data20210811 (
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
COMMENT ON TABLE upm.api_interface_system_data20210811 IS 'UPM系统数据(20210811)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210812;
CREATE TABLE upm.api_interface_system_data20210812 (
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
COMMENT ON TABLE upm.api_interface_system_data20210812 IS 'UPM系统数据(20210812)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210813;
CREATE TABLE upm.api_interface_system_data20210813 (
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
COMMENT ON TABLE upm.api_interface_system_data20210813 IS 'UPM系统数据(20210813)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210814;
CREATE TABLE upm.api_interface_system_data20210814 (
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
COMMENT ON TABLE upm.api_interface_system_data20210814 IS 'UPM系统数据(20210814)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210815;
CREATE TABLE upm.api_interface_system_data20210815 (
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
COMMENT ON TABLE upm.api_interface_system_data20210815 IS 'UPM系统数据(20210815)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210816;
CREATE TABLE upm.api_interface_system_data20210816 (
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
COMMENT ON TABLE upm.api_interface_system_data20210816 IS 'UPM系统数据(20210816)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210817;
CREATE TABLE upm.api_interface_system_data20210817 (
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
COMMENT ON TABLE upm.api_interface_system_data20210817 IS 'UPM系统数据(20210817)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210818;
CREATE TABLE upm.api_interface_system_data20210818 (
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
COMMENT ON TABLE upm.api_interface_system_data20210818 IS 'UPM系统数据(20210818)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210819;
CREATE TABLE upm.api_interface_system_data20210819 (
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
COMMENT ON TABLE upm.api_interface_system_data20210819 IS 'UPM系统数据(20210819)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210820;
CREATE TABLE upm.api_interface_system_data20210820 (
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
COMMENT ON TABLE upm.api_interface_system_data20210820 IS 'UPM系统数据(20210820)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210821;
CREATE TABLE upm.api_interface_system_data20210821 (
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
COMMENT ON TABLE upm.api_interface_system_data20210821 IS 'UPM系统数据(20210821)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210822;
CREATE TABLE upm.api_interface_system_data20210822 (
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
COMMENT ON TABLE upm.api_interface_system_data20210822 IS 'UPM系统数据(20210822)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210823;
CREATE TABLE upm.api_interface_system_data20210823 (
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
COMMENT ON TABLE upm.api_interface_system_data20210823 IS 'UPM系统数据(20210823)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210824;
CREATE TABLE upm.api_interface_system_data20210824 (
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
COMMENT ON TABLE upm.api_interface_system_data20210824 IS 'UPM系统数据(20210824)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210825;
CREATE TABLE upm.api_interface_system_data20210825 (
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
COMMENT ON TABLE upm.api_interface_system_data20210825 IS 'UPM系统数据(20210825)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210826;
CREATE TABLE upm.api_interface_system_data20210826 (
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
COMMENT ON TABLE upm.api_interface_system_data20210826 IS 'UPM系统数据(20210826)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210827;
CREATE TABLE upm.api_interface_system_data20210827 (
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
COMMENT ON TABLE upm.api_interface_system_data20210827 IS 'UPM系统数据(20210827)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210828;
CREATE TABLE upm.api_interface_system_data20210828 (
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
COMMENT ON TABLE upm.api_interface_system_data20210828 IS 'UPM系统数据(20210828)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210829;
CREATE TABLE upm.api_interface_system_data20210829 (
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
COMMENT ON TABLE upm.api_interface_system_data20210829 IS 'UPM系统数据(20210829)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210830;
CREATE TABLE upm.api_interface_system_data20210830 (
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
COMMENT ON TABLE upm.api_interface_system_data20210830 IS 'UPM系统数据(20210830)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210831;
CREATE TABLE upm.api_interface_system_data20210831 (
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
COMMENT ON TABLE upm.api_interface_system_data20210831 IS 'UPM系统数据(20210831)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210901;
CREATE TABLE upm.api_interface_system_data20210901 (
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
COMMENT ON TABLE upm.api_interface_system_data20210901 IS 'UPM系统数据(20210901)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210902;
CREATE TABLE upm.api_interface_system_data20210902 (
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
COMMENT ON TABLE upm.api_interface_system_data20210902 IS 'UPM系统数据(20210902)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210903;
CREATE TABLE upm.api_interface_system_data20210903 (
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
COMMENT ON TABLE upm.api_interface_system_data20210903 IS 'UPM系统数据(20210903)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210904;
CREATE TABLE upm.api_interface_system_data20210904 (
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
COMMENT ON TABLE upm.api_interface_system_data20210904 IS 'UPM系统数据(20210904)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210905;
CREATE TABLE upm.api_interface_system_data20210905 (
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
COMMENT ON TABLE upm.api_interface_system_data20210905 IS 'UPM系统数据(20210905)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210906;
CREATE TABLE upm.api_interface_system_data20210906 (
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
COMMENT ON TABLE upm.api_interface_system_data20210906 IS 'UPM系统数据(20210906)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210907;
CREATE TABLE upm.api_interface_system_data20210907 (
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
COMMENT ON TABLE upm.api_interface_system_data20210907 IS 'UPM系统数据(20210907)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210908;
CREATE TABLE upm.api_interface_system_data20210908 (
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
COMMENT ON TABLE upm.api_interface_system_data20210908 IS 'UPM系统数据(20210908)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210909;
CREATE TABLE upm.api_interface_system_data20210909 (
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
COMMENT ON TABLE upm.api_interface_system_data20210909 IS 'UPM系统数据(20210909)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210910;
CREATE TABLE upm.api_interface_system_data20210910 (
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
COMMENT ON TABLE upm.api_interface_system_data20210910 IS 'UPM系统数据(20210910)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210911;
CREATE TABLE upm.api_interface_system_data20210911 (
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
COMMENT ON TABLE upm.api_interface_system_data20210911 IS 'UPM系统数据(20210911)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210912;
CREATE TABLE upm.api_interface_system_data20210912 (
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
COMMENT ON TABLE upm.api_interface_system_data20210912 IS 'UPM系统数据(20210912)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210913;
CREATE TABLE upm.api_interface_system_data20210913 (
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
COMMENT ON TABLE upm.api_interface_system_data20210913 IS 'UPM系统数据(20210913)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210914;
CREATE TABLE upm.api_interface_system_data20210914 (
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
COMMENT ON TABLE upm.api_interface_system_data20210914 IS 'UPM系统数据(20210914)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210915;
CREATE TABLE upm.api_interface_system_data20210915 (
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
COMMENT ON TABLE upm.api_interface_system_data20210915 IS 'UPM系统数据(20210915)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210916;
CREATE TABLE upm.api_interface_system_data20210916 (
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
COMMENT ON TABLE upm.api_interface_system_data20210916 IS 'UPM系统数据(20210916)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210917;
CREATE TABLE upm.api_interface_system_data20210917 (
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
COMMENT ON TABLE upm.api_interface_system_data20210917 IS 'UPM系统数据(20210917)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210918;
CREATE TABLE upm.api_interface_system_data20210918 (
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
COMMENT ON TABLE upm.api_interface_system_data20210918 IS 'UPM系统数据(20210918)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210919;
CREATE TABLE upm.api_interface_system_data20210919 (
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
COMMENT ON TABLE upm.api_interface_system_data20210919 IS 'UPM系统数据(20210919)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210920;
CREATE TABLE upm.api_interface_system_data20210920 (
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
COMMENT ON TABLE upm.api_interface_system_data20210920 IS 'UPM系统数据(20210920)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210921;
CREATE TABLE upm.api_interface_system_data20210921 (
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
COMMENT ON TABLE upm.api_interface_system_data20210921 IS 'UPM系统数据(20210921)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210922;
CREATE TABLE upm.api_interface_system_data20210922 (
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
COMMENT ON TABLE upm.api_interface_system_data20210922 IS 'UPM系统数据(20210922)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210923;
CREATE TABLE upm.api_interface_system_data20210923 (
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
COMMENT ON TABLE upm.api_interface_system_data20210923 IS 'UPM系统数据(20210923)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210924;
CREATE TABLE upm.api_interface_system_data20210924 (
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
COMMENT ON TABLE upm.api_interface_system_data20210924 IS 'UPM系统数据(20210924)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210925;
CREATE TABLE upm.api_interface_system_data20210925 (
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
COMMENT ON TABLE upm.api_interface_system_data20210925 IS 'UPM系统数据(20210925)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210926;
CREATE TABLE upm.api_interface_system_data20210926 (
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
COMMENT ON TABLE upm.api_interface_system_data20210926 IS 'UPM系统数据(20210926)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210927;
CREATE TABLE upm.api_interface_system_data20210927 (
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
COMMENT ON TABLE upm.api_interface_system_data20210927 IS 'UPM系统数据(20210927)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210928;
CREATE TABLE upm.api_interface_system_data20210928 (
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
COMMENT ON TABLE upm.api_interface_system_data20210928 IS 'UPM系统数据(20210928)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210929;
CREATE TABLE upm.api_interface_system_data20210929 (
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
COMMENT ON TABLE upm.api_interface_system_data20210929 IS 'UPM系统数据(20210929)';

DROP TABLE IF EXISTS upm.api_interface_system_data20210930;
CREATE TABLE upm.api_interface_system_data20210930 (
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
COMMENT ON TABLE upm.api_interface_system_data20210930 IS 'UPM系统数据(20210930)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211001;
CREATE TABLE upm.api_interface_system_data20211001 (
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
COMMENT ON TABLE upm.api_interface_system_data20211001 IS 'UPM系统数据(20211001)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211002;
CREATE TABLE upm.api_interface_system_data20211002 (
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
COMMENT ON TABLE upm.api_interface_system_data20211002 IS 'UPM系统数据(20211002)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211003;
CREATE TABLE upm.api_interface_system_data20211003 (
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
COMMENT ON TABLE upm.api_interface_system_data20211003 IS 'UPM系统数据(20211003)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211004;
CREATE TABLE upm.api_interface_system_data20211004 (
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
COMMENT ON TABLE upm.api_interface_system_data20211004 IS 'UPM系统数据(20211004)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211005;
CREATE TABLE upm.api_interface_system_data20211005 (
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
COMMENT ON TABLE upm.api_interface_system_data20211005 IS 'UPM系统数据(20211005)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211006;
CREATE TABLE upm.api_interface_system_data20211006 (
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
COMMENT ON TABLE upm.api_interface_system_data20211006 IS 'UPM系统数据(20211006)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211007;
CREATE TABLE upm.api_interface_system_data20211007 (
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
COMMENT ON TABLE upm.api_interface_system_data20211007 IS 'UPM系统数据(20211007)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211008;
CREATE TABLE upm.api_interface_system_data20211008 (
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
COMMENT ON TABLE upm.api_interface_system_data20211008 IS 'UPM系统数据(20211008)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211009;
CREATE TABLE upm.api_interface_system_data20211009 (
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
COMMENT ON TABLE upm.api_interface_system_data20211009 IS 'UPM系统数据(20211009)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211010;
CREATE TABLE upm.api_interface_system_data20211010 (
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
COMMENT ON TABLE upm.api_interface_system_data20211010 IS 'UPM系统数据(20211010)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211011;
CREATE TABLE upm.api_interface_system_data20211011 (
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
COMMENT ON TABLE upm.api_interface_system_data20211011 IS 'UPM系统数据(20211011)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211012;
CREATE TABLE upm.api_interface_system_data20211012 (
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
COMMENT ON TABLE upm.api_interface_system_data20211012 IS 'UPM系统数据(20211012)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211013;
CREATE TABLE upm.api_interface_system_data20211013 (
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
COMMENT ON TABLE upm.api_interface_system_data20211013 IS 'UPM系统数据(20211013)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211014;
CREATE TABLE upm.api_interface_system_data20211014 (
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
COMMENT ON TABLE upm.api_interface_system_data20211014 IS 'UPM系统数据(20211014)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211015;
CREATE TABLE upm.api_interface_system_data20211015 (
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
COMMENT ON TABLE upm.api_interface_system_data20211015 IS 'UPM系统数据(20211015)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211016;
CREATE TABLE upm.api_interface_system_data20211016 (
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
COMMENT ON TABLE upm.api_interface_system_data20211016 IS 'UPM系统数据(20211016)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211017;
CREATE TABLE upm.api_interface_system_data20211017 (
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
COMMENT ON TABLE upm.api_interface_system_data20211017 IS 'UPM系统数据(20211017)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211018;
CREATE TABLE upm.api_interface_system_data20211018 (
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
COMMENT ON TABLE upm.api_interface_system_data20211018 IS 'UPM系统数据(20211018)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211019;
CREATE TABLE upm.api_interface_system_data20211019 (
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
COMMENT ON TABLE upm.api_interface_system_data20211019 IS 'UPM系统数据(20211019)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211020;
CREATE TABLE upm.api_interface_system_data20211020 (
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
COMMENT ON TABLE upm.api_interface_system_data20211020 IS 'UPM系统数据(20211020)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211021;
CREATE TABLE upm.api_interface_system_data20211021 (
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
COMMENT ON TABLE upm.api_interface_system_data20211021 IS 'UPM系统数据(20211021)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211022;
CREATE TABLE upm.api_interface_system_data20211022 (
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
COMMENT ON TABLE upm.api_interface_system_data20211022 IS 'UPM系统数据(20211022)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211023;
CREATE TABLE upm.api_interface_system_data20211023 (
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
COMMENT ON TABLE upm.api_interface_system_data20211023 IS 'UPM系统数据(20211023)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211024;
CREATE TABLE upm.api_interface_system_data20211024 (
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
COMMENT ON TABLE upm.api_interface_system_data20211024 IS 'UPM系统数据(20211024)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211025;
CREATE TABLE upm.api_interface_system_data20211025 (
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
COMMENT ON TABLE upm.api_interface_system_data20211025 IS 'UPM系统数据(20211025)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211026;
CREATE TABLE upm.api_interface_system_data20211026 (
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
COMMENT ON TABLE upm.api_interface_system_data20211026 IS 'UPM系统数据(20211026)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211027;
CREATE TABLE upm.api_interface_system_data20211027 (
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
COMMENT ON TABLE upm.api_interface_system_data20211027 IS 'UPM系统数据(20211027)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211028;
CREATE TABLE upm.api_interface_system_data20211028 (
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
COMMENT ON TABLE upm.api_interface_system_data20211028 IS 'UPM系统数据(20211028)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211029;
CREATE TABLE upm.api_interface_system_data20211029 (
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
COMMENT ON TABLE upm.api_interface_system_data20211029 IS 'UPM系统数据(20211029)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211030;
CREATE TABLE upm.api_interface_system_data20211030 (
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
COMMENT ON TABLE upm.api_interface_system_data20211030 IS 'UPM系统数据(20211030)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211031;
CREATE TABLE upm.api_interface_system_data20211031 (
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
COMMENT ON TABLE upm.api_interface_system_data20211031 IS 'UPM系统数据(20211031)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211101;
CREATE TABLE upm.api_interface_system_data20211101 (
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
COMMENT ON TABLE upm.api_interface_system_data20211101 IS 'UPM系统数据(20211101)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211102;
CREATE TABLE upm.api_interface_system_data20211102 (
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
COMMENT ON TABLE upm.api_interface_system_data20211102 IS 'UPM系统数据(20211102)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211103;
CREATE TABLE upm.api_interface_system_data20211103 (
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
COMMENT ON TABLE upm.api_interface_system_data20211103 IS 'UPM系统数据(20211103)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211104;
CREATE TABLE upm.api_interface_system_data20211104 (
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
COMMENT ON TABLE upm.api_interface_system_data20211104 IS 'UPM系统数据(20211104)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211105;
CREATE TABLE upm.api_interface_system_data20211105 (
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
COMMENT ON TABLE upm.api_interface_system_data20211105 IS 'UPM系统数据(20211105)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211106;
CREATE TABLE upm.api_interface_system_data20211106 (
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
COMMENT ON TABLE upm.api_interface_system_data20211106 IS 'UPM系统数据(20211106)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211107;
CREATE TABLE upm.api_interface_system_data20211107 (
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
COMMENT ON TABLE upm.api_interface_system_data20211107 IS 'UPM系统数据(20211107)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211108;
CREATE TABLE upm.api_interface_system_data20211108 (
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
COMMENT ON TABLE upm.api_interface_system_data20211108 IS 'UPM系统数据(20211108)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211109;
CREATE TABLE upm.api_interface_system_data20211109 (
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
COMMENT ON TABLE upm.api_interface_system_data20211109 IS 'UPM系统数据(20211109)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211110;
CREATE TABLE upm.api_interface_system_data20211110 (
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
COMMENT ON TABLE upm.api_interface_system_data20211110 IS 'UPM系统数据(20211110)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211111;
CREATE TABLE upm.api_interface_system_data20211111 (
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
COMMENT ON TABLE upm.api_interface_system_data20211111 IS 'UPM系统数据(20211111)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211112;
CREATE TABLE upm.api_interface_system_data20211112 (
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
COMMENT ON TABLE upm.api_interface_system_data20211112 IS 'UPM系统数据(20211112)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211113;
CREATE TABLE upm.api_interface_system_data20211113 (
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
COMMENT ON TABLE upm.api_interface_system_data20211113 IS 'UPM系统数据(20211113)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211114;
CREATE TABLE upm.api_interface_system_data20211114 (
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
COMMENT ON TABLE upm.api_interface_system_data20211114 IS 'UPM系统数据(20211114)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211115;
CREATE TABLE upm.api_interface_system_data20211115 (
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
COMMENT ON TABLE upm.api_interface_system_data20211115 IS 'UPM系统数据(20211115)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211116;
CREATE TABLE upm.api_interface_system_data20211116 (
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
COMMENT ON TABLE upm.api_interface_system_data20211116 IS 'UPM系统数据(20211116)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211117;
CREATE TABLE upm.api_interface_system_data20211117 (
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
COMMENT ON TABLE upm.api_interface_system_data20211117 IS 'UPM系统数据(20211117)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211118;
CREATE TABLE upm.api_interface_system_data20211118 (
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
COMMENT ON TABLE upm.api_interface_system_data20211118 IS 'UPM系统数据(20211118)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211119;
CREATE TABLE upm.api_interface_system_data20211119 (
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
COMMENT ON TABLE upm.api_interface_system_data20211119 IS 'UPM系统数据(20211119)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211120;
CREATE TABLE upm.api_interface_system_data20211120 (
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
COMMENT ON TABLE upm.api_interface_system_data20211120 IS 'UPM系统数据(20211120)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211121;
CREATE TABLE upm.api_interface_system_data20211121 (
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
COMMENT ON TABLE upm.api_interface_system_data20211121 IS 'UPM系统数据(20211121)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211122;
CREATE TABLE upm.api_interface_system_data20211122 (
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
COMMENT ON TABLE upm.api_interface_system_data20211122 IS 'UPM系统数据(20211122)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211123;
CREATE TABLE upm.api_interface_system_data20211123 (
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
COMMENT ON TABLE upm.api_interface_system_data20211123 IS 'UPM系统数据(20211123)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211124;
CREATE TABLE upm.api_interface_system_data20211124 (
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
COMMENT ON TABLE upm.api_interface_system_data20211124 IS 'UPM系统数据(20211124)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211125;
CREATE TABLE upm.api_interface_system_data20211125 (
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
COMMENT ON TABLE upm.api_interface_system_data20211125 IS 'UPM系统数据(20211125)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211126;
CREATE TABLE upm.api_interface_system_data20211126 (
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
COMMENT ON TABLE upm.api_interface_system_data20211126 IS 'UPM系统数据(20211126)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211127;
CREATE TABLE upm.api_interface_system_data20211127 (
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
COMMENT ON TABLE upm.api_interface_system_data20211127 IS 'UPM系统数据(20211127)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211128;
CREATE TABLE upm.api_interface_system_data20211128 (
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
COMMENT ON TABLE upm.api_interface_system_data20211128 IS 'UPM系统数据(20211128)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211129;
CREATE TABLE upm.api_interface_system_data20211129 (
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
COMMENT ON TABLE upm.api_interface_system_data20211129 IS 'UPM系统数据(20211129)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211130;
CREATE TABLE upm.api_interface_system_data20211130 (
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
COMMENT ON TABLE upm.api_interface_system_data20211130 IS 'UPM系统数据(20211130)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211201;
CREATE TABLE upm.api_interface_system_data20211201 (
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
COMMENT ON TABLE upm.api_interface_system_data20211201 IS 'UPM系统数据(20211201)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211202;
CREATE TABLE upm.api_interface_system_data20211202 (
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
COMMENT ON TABLE upm.api_interface_system_data20211202 IS 'UPM系统数据(20211202)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211203;
CREATE TABLE upm.api_interface_system_data20211203 (
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
COMMENT ON TABLE upm.api_interface_system_data20211203 IS 'UPM系统数据(20211203)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211204;
CREATE TABLE upm.api_interface_system_data20211204 (
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
COMMENT ON TABLE upm.api_interface_system_data20211204 IS 'UPM系统数据(20211204)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211205;
CREATE TABLE upm.api_interface_system_data20211205 (
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
COMMENT ON TABLE upm.api_interface_system_data20211205 IS 'UPM系统数据(20211205)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211206;
CREATE TABLE upm.api_interface_system_data20211206 (
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
COMMENT ON TABLE upm.api_interface_system_data20211206 IS 'UPM系统数据(20211206)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211207;
CREATE TABLE upm.api_interface_system_data20211207 (
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
COMMENT ON TABLE upm.api_interface_system_data20211207 IS 'UPM系统数据(20211207)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211208;
CREATE TABLE upm.api_interface_system_data20211208 (
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
COMMENT ON TABLE upm.api_interface_system_data20211208 IS 'UPM系统数据(20211208)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211209;
CREATE TABLE upm.api_interface_system_data20211209 (
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
COMMENT ON TABLE upm.api_interface_system_data20211209 IS 'UPM系统数据(20211209)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211210;
CREATE TABLE upm.api_interface_system_data20211210 (
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
COMMENT ON TABLE upm.api_interface_system_data20211210 IS 'UPM系统数据(20211210)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211211;
CREATE TABLE upm.api_interface_system_data20211211 (
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
COMMENT ON TABLE upm.api_interface_system_data20211211 IS 'UPM系统数据(20211211)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211212;
CREATE TABLE upm.api_interface_system_data20211212 (
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
COMMENT ON TABLE upm.api_interface_system_data20211212 IS 'UPM系统数据(20211212)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211213;
CREATE TABLE upm.api_interface_system_data20211213 (
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
COMMENT ON TABLE upm.api_interface_system_data20211213 IS 'UPM系统数据(20211213)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211214;
CREATE TABLE upm.api_interface_system_data20211214 (
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
COMMENT ON TABLE upm.api_interface_system_data20211214 IS 'UPM系统数据(20211214)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211215;
CREATE TABLE upm.api_interface_system_data20211215 (
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
COMMENT ON TABLE upm.api_interface_system_data20211215 IS 'UPM系统数据(20211215)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211216;
CREATE TABLE upm.api_interface_system_data20211216 (
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
COMMENT ON TABLE upm.api_interface_system_data20211216 IS 'UPM系统数据(20211216)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211217;
CREATE TABLE upm.api_interface_system_data20211217 (
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
COMMENT ON TABLE upm.api_interface_system_data20211217 IS 'UPM系统数据(20211217)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211218;
CREATE TABLE upm.api_interface_system_data20211218 (
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
COMMENT ON TABLE upm.api_interface_system_data20211218 IS 'UPM系统数据(20211218)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211219;
CREATE TABLE upm.api_interface_system_data20211219 (
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
COMMENT ON TABLE upm.api_interface_system_data20211219 IS 'UPM系统数据(20211219)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211220;
CREATE TABLE upm.api_interface_system_data20211220 (
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
COMMENT ON TABLE upm.api_interface_system_data20211220 IS 'UPM系统数据(20211220)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211221;
CREATE TABLE upm.api_interface_system_data20211221 (
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
COMMENT ON TABLE upm.api_interface_system_data20211221 IS 'UPM系统数据(20211221)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211222;
CREATE TABLE upm.api_interface_system_data20211222 (
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
COMMENT ON TABLE upm.api_interface_system_data20211222 IS 'UPM系统数据(20211222)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211223;
CREATE TABLE upm.api_interface_system_data20211223 (
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
COMMENT ON TABLE upm.api_interface_system_data20211223 IS 'UPM系统数据(20211223)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211224;
CREATE TABLE upm.api_interface_system_data20211224 (
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
COMMENT ON TABLE upm.api_interface_system_data20211224 IS 'UPM系统数据(20211224)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211225;
CREATE TABLE upm.api_interface_system_data20211225 (
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
COMMENT ON TABLE upm.api_interface_system_data20211225 IS 'UPM系统数据(20211225)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211226;
CREATE TABLE upm.api_interface_system_data20211226 (
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
COMMENT ON TABLE upm.api_interface_system_data20211226 IS 'UPM系统数据(20211226)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211227;
CREATE TABLE upm.api_interface_system_data20211227 (
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
COMMENT ON TABLE upm.api_interface_system_data20211227 IS 'UPM系统数据(20211227)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211228;
CREATE TABLE upm.api_interface_system_data20211228 (
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
COMMENT ON TABLE upm.api_interface_system_data20211228 IS 'UPM系统数据(20211228)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211229;
CREATE TABLE upm.api_interface_system_data20211229 (
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
COMMENT ON TABLE upm.api_interface_system_data20211229 IS 'UPM系统数据(20211229)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211230;
CREATE TABLE upm.api_interface_system_data20211230 (
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
COMMENT ON TABLE upm.api_interface_system_data20211230 IS 'UPM系统数据(20211230)';

DROP TABLE IF EXISTS upm.api_interface_system_data20211231;
CREATE TABLE upm.api_interface_system_data20211231 (
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
COMMENT ON TABLE upm.api_interface_system_data20211231 IS 'UPM系统数据(20211231)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220101;
CREATE TABLE upm.api_interface_system_data20220101 (
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
COMMENT ON TABLE upm.api_interface_system_data20220101 IS 'UPM系统数据(20220101)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220102;
CREATE TABLE upm.api_interface_system_data20220102 (
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
COMMENT ON TABLE upm.api_interface_system_data20220102 IS 'UPM系统数据(20220102)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220103;
CREATE TABLE upm.api_interface_system_data20220103 (
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
COMMENT ON TABLE upm.api_interface_system_data20220103 IS 'UPM系统数据(20220103)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220104;
CREATE TABLE upm.api_interface_system_data20220104 (
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
COMMENT ON TABLE upm.api_interface_system_data20220104 IS 'UPM系统数据(20220104)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220105;
CREATE TABLE upm.api_interface_system_data20220105 (
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
COMMENT ON TABLE upm.api_interface_system_data20220105 IS 'UPM系统数据(20220105)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220106;
CREATE TABLE upm.api_interface_system_data20220106 (
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
COMMENT ON TABLE upm.api_interface_system_data20220106 IS 'UPM系统数据(20220106)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220107;
CREATE TABLE upm.api_interface_system_data20220107 (
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
COMMENT ON TABLE upm.api_interface_system_data20220107 IS 'UPM系统数据(20220107)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220108;
CREATE TABLE upm.api_interface_system_data20220108 (
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
COMMENT ON TABLE upm.api_interface_system_data20220108 IS 'UPM系统数据(20220108)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220109;
CREATE TABLE upm.api_interface_system_data20220109 (
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
COMMENT ON TABLE upm.api_interface_system_data20220109 IS 'UPM系统数据(20220109)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220110;
CREATE TABLE upm.api_interface_system_data20220110 (
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
COMMENT ON TABLE upm.api_interface_system_data20220110 IS 'UPM系统数据(20220110)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220111;
CREATE TABLE upm.api_interface_system_data20220111 (
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
COMMENT ON TABLE upm.api_interface_system_data20220111 IS 'UPM系统数据(20220111)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220112;
CREATE TABLE upm.api_interface_system_data20220112 (
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
COMMENT ON TABLE upm.api_interface_system_data20220112 IS 'UPM系统数据(20220112)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220113;
CREATE TABLE upm.api_interface_system_data20220113 (
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
COMMENT ON TABLE upm.api_interface_system_data20220113 IS 'UPM系统数据(20220113)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220114;
CREATE TABLE upm.api_interface_system_data20220114 (
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
COMMENT ON TABLE upm.api_interface_system_data20220114 IS 'UPM系统数据(20220114)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220115;
CREATE TABLE upm.api_interface_system_data20220115 (
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
COMMENT ON TABLE upm.api_interface_system_data20220115 IS 'UPM系统数据(20220115)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220116;
CREATE TABLE upm.api_interface_system_data20220116 (
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
COMMENT ON TABLE upm.api_interface_system_data20220116 IS 'UPM系统数据(20220116)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220117;
CREATE TABLE upm.api_interface_system_data20220117 (
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
COMMENT ON TABLE upm.api_interface_system_data20220117 IS 'UPM系统数据(20220117)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220118;
CREATE TABLE upm.api_interface_system_data20220118 (
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
COMMENT ON TABLE upm.api_interface_system_data20220118 IS 'UPM系统数据(20220118)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220119;
CREATE TABLE upm.api_interface_system_data20220119 (
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
COMMENT ON TABLE upm.api_interface_system_data20220119 IS 'UPM系统数据(20220119)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220120;
CREATE TABLE upm.api_interface_system_data20220120 (
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
COMMENT ON TABLE upm.api_interface_system_data20220120 IS 'UPM系统数据(20220120)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220121;
CREATE TABLE upm.api_interface_system_data20220121 (
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
COMMENT ON TABLE upm.api_interface_system_data20220121 IS 'UPM系统数据(20220121)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220122;
CREATE TABLE upm.api_interface_system_data20220122 (
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
COMMENT ON TABLE upm.api_interface_system_data20220122 IS 'UPM系统数据(20220122)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220123;
CREATE TABLE upm.api_interface_system_data20220123 (
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
COMMENT ON TABLE upm.api_interface_system_data20220123 IS 'UPM系统数据(20220123)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220124;
CREATE TABLE upm.api_interface_system_data20220124 (
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
COMMENT ON TABLE upm.api_interface_system_data20220124 IS 'UPM系统数据(20220124)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220125;
CREATE TABLE upm.api_interface_system_data20220125 (
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
COMMENT ON TABLE upm.api_interface_system_data20220125 IS 'UPM系统数据(20220125)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220126;
CREATE TABLE upm.api_interface_system_data20220126 (
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
COMMENT ON TABLE upm.api_interface_system_data20220126 IS 'UPM系统数据(20220126)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220127;
CREATE TABLE upm.api_interface_system_data20220127 (
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
COMMENT ON TABLE upm.api_interface_system_data20220127 IS 'UPM系统数据(20220127)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220128;
CREATE TABLE upm.api_interface_system_data20220128 (
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
COMMENT ON TABLE upm.api_interface_system_data20220128 IS 'UPM系统数据(20220128)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220129;
CREATE TABLE upm.api_interface_system_data20220129 (
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
COMMENT ON TABLE upm.api_interface_system_data20220129 IS 'UPM系统数据(20220129)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220130;
CREATE TABLE upm.api_interface_system_data20220130 (
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
COMMENT ON TABLE upm.api_interface_system_data20220130 IS 'UPM系统数据(20220130)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220131;
CREATE TABLE upm.api_interface_system_data20220131 (
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
COMMENT ON TABLE upm.api_interface_system_data20220131 IS 'UPM系统数据(20220131)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220201;
CREATE TABLE upm.api_interface_system_data20220201 (
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
COMMENT ON TABLE upm.api_interface_system_data20220201 IS 'UPM系统数据(20220201)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220202;
CREATE TABLE upm.api_interface_system_data20220202 (
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
COMMENT ON TABLE upm.api_interface_system_data20220202 IS 'UPM系统数据(20220202)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220203;
CREATE TABLE upm.api_interface_system_data20220203 (
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
COMMENT ON TABLE upm.api_interface_system_data20220203 IS 'UPM系统数据(20220203)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220204;
CREATE TABLE upm.api_interface_system_data20220204 (
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
COMMENT ON TABLE upm.api_interface_system_data20220204 IS 'UPM系统数据(20220204)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220205;
CREATE TABLE upm.api_interface_system_data20220205 (
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
COMMENT ON TABLE upm.api_interface_system_data20220205 IS 'UPM系统数据(20220205)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220206;
CREATE TABLE upm.api_interface_system_data20220206 (
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
COMMENT ON TABLE upm.api_interface_system_data20220206 IS 'UPM系统数据(20220206)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220207;
CREATE TABLE upm.api_interface_system_data20220207 (
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
COMMENT ON TABLE upm.api_interface_system_data20220207 IS 'UPM系统数据(20220207)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220208;
CREATE TABLE upm.api_interface_system_data20220208 (
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
COMMENT ON TABLE upm.api_interface_system_data20220208 IS 'UPM系统数据(20220208)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220209;
CREATE TABLE upm.api_interface_system_data20220209 (
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
COMMENT ON TABLE upm.api_interface_system_data20220209 IS 'UPM系统数据(20220209)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220210;
CREATE TABLE upm.api_interface_system_data20220210 (
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
COMMENT ON TABLE upm.api_interface_system_data20220210 IS 'UPM系统数据(20220210)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220211;
CREATE TABLE upm.api_interface_system_data20220211 (
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
COMMENT ON TABLE upm.api_interface_system_data20220211 IS 'UPM系统数据(20220211)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220212;
CREATE TABLE upm.api_interface_system_data20220212 (
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
COMMENT ON TABLE upm.api_interface_system_data20220212 IS 'UPM系统数据(20220212)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220213;
CREATE TABLE upm.api_interface_system_data20220213 (
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
COMMENT ON TABLE upm.api_interface_system_data20220213 IS 'UPM系统数据(20220213)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220214;
CREATE TABLE upm.api_interface_system_data20220214 (
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
COMMENT ON TABLE upm.api_interface_system_data20220214 IS 'UPM系统数据(20220214)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220215;
CREATE TABLE upm.api_interface_system_data20220215 (
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
COMMENT ON TABLE upm.api_interface_system_data20220215 IS 'UPM系统数据(20220215)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220216;
CREATE TABLE upm.api_interface_system_data20220216 (
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
COMMENT ON TABLE upm.api_interface_system_data20220216 IS 'UPM系统数据(20220216)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220217;
CREATE TABLE upm.api_interface_system_data20220217 (
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
COMMENT ON TABLE upm.api_interface_system_data20220217 IS 'UPM系统数据(20220217)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220218;
CREATE TABLE upm.api_interface_system_data20220218 (
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
COMMENT ON TABLE upm.api_interface_system_data20220218 IS 'UPM系统数据(20220218)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220219;
CREATE TABLE upm.api_interface_system_data20220219 (
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
COMMENT ON TABLE upm.api_interface_system_data20220219 IS 'UPM系统数据(20220219)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220220;
CREATE TABLE upm.api_interface_system_data20220220 (
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
COMMENT ON TABLE upm.api_interface_system_data20220220 IS 'UPM系统数据(20220220)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220221;
CREATE TABLE upm.api_interface_system_data20220221 (
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
COMMENT ON TABLE upm.api_interface_system_data20220221 IS 'UPM系统数据(20220221)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220222;
CREATE TABLE upm.api_interface_system_data20220222 (
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
COMMENT ON TABLE upm.api_interface_system_data20220222 IS 'UPM系统数据(20220222)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220223;
CREATE TABLE upm.api_interface_system_data20220223 (
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
COMMENT ON TABLE upm.api_interface_system_data20220223 IS 'UPM系统数据(20220223)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220224;
CREATE TABLE upm.api_interface_system_data20220224 (
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
COMMENT ON TABLE upm.api_interface_system_data20220224 IS 'UPM系统数据(20220224)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220225;
CREATE TABLE upm.api_interface_system_data20220225 (
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
COMMENT ON TABLE upm.api_interface_system_data20220225 IS 'UPM系统数据(20220225)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220226;
CREATE TABLE upm.api_interface_system_data20220226 (
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
COMMENT ON TABLE upm.api_interface_system_data20220226 IS 'UPM系统数据(20220226)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220227;
CREATE TABLE upm.api_interface_system_data20220227 (
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
COMMENT ON TABLE upm.api_interface_system_data20220227 IS 'UPM系统数据(20220227)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220228;
CREATE TABLE upm.api_interface_system_data20220228 (
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
COMMENT ON TABLE upm.api_interface_system_data20220228 IS 'UPM系统数据(20220228)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220301;
CREATE TABLE upm.api_interface_system_data20220301 (
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
COMMENT ON TABLE upm.api_interface_system_data20220301 IS 'UPM系统数据(20220301)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220302;
CREATE TABLE upm.api_interface_system_data20220302 (
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
COMMENT ON TABLE upm.api_interface_system_data20220302 IS 'UPM系统数据(20220302)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220303;
CREATE TABLE upm.api_interface_system_data20220303 (
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
COMMENT ON TABLE upm.api_interface_system_data20220303 IS 'UPM系统数据(20220303)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220304;
CREATE TABLE upm.api_interface_system_data20220304 (
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
COMMENT ON TABLE upm.api_interface_system_data20220304 IS 'UPM系统数据(20220304)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220305;
CREATE TABLE upm.api_interface_system_data20220305 (
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
COMMENT ON TABLE upm.api_interface_system_data20220305 IS 'UPM系统数据(20220305)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220306;
CREATE TABLE upm.api_interface_system_data20220306 (
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
COMMENT ON TABLE upm.api_interface_system_data20220306 IS 'UPM系统数据(20220306)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220307;
CREATE TABLE upm.api_interface_system_data20220307 (
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
COMMENT ON TABLE upm.api_interface_system_data20220307 IS 'UPM系统数据(20220307)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220308;
CREATE TABLE upm.api_interface_system_data20220308 (
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
COMMENT ON TABLE upm.api_interface_system_data20220308 IS 'UPM系统数据(20220308)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220309;
CREATE TABLE upm.api_interface_system_data20220309 (
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
COMMENT ON TABLE upm.api_interface_system_data20220309 IS 'UPM系统数据(20220309)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220310;
CREATE TABLE upm.api_interface_system_data20220310 (
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
COMMENT ON TABLE upm.api_interface_system_data20220310 IS 'UPM系统数据(20220310)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220311;
CREATE TABLE upm.api_interface_system_data20220311 (
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
COMMENT ON TABLE upm.api_interface_system_data20220311 IS 'UPM系统数据(20220311)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220312;
CREATE TABLE upm.api_interface_system_data20220312 (
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
COMMENT ON TABLE upm.api_interface_system_data20220312 IS 'UPM系统数据(20220312)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220313;
CREATE TABLE upm.api_interface_system_data20220313 (
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
COMMENT ON TABLE upm.api_interface_system_data20220313 IS 'UPM系统数据(20220313)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220314;
CREATE TABLE upm.api_interface_system_data20220314 (
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
COMMENT ON TABLE upm.api_interface_system_data20220314 IS 'UPM系统数据(20220314)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220315;
CREATE TABLE upm.api_interface_system_data20220315 (
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
COMMENT ON TABLE upm.api_interface_system_data20220315 IS 'UPM系统数据(20220315)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220316;
CREATE TABLE upm.api_interface_system_data20220316 (
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
COMMENT ON TABLE upm.api_interface_system_data20220316 IS 'UPM系统数据(20220316)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220317;
CREATE TABLE upm.api_interface_system_data20220317 (
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
COMMENT ON TABLE upm.api_interface_system_data20220317 IS 'UPM系统数据(20220317)';

DROP TABLE IF EXISTS upm.api_interface_system_data20220318;
CREATE TABLE upm.api_interface_system_data20220318 (
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
COMMENT ON TABLE upm.api_interface_system_data20220318 IS 'UPM系统数据(20220318)';

DROP TABLE IF EXISTS upm.api_interface_system_data20230913;
CREATE TABLE upm.api_interface_system_data20230913 (
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
COMMENT ON TABLE upm.api_interface_system_data20230913 IS 'UPM系统数据(20230913)';

DROP TABLE IF EXISTS upm.api_interface_system_data20230914;
CREATE TABLE upm.api_interface_system_data20230914 (
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
COMMENT ON TABLE upm.api_interface_system_data20230914 IS 'UPM系统数据(20230914)';

DROP TABLE IF EXISTS upm.api_interface_system_data20230915;
CREATE TABLE upm.api_interface_system_data20230915 (
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
COMMENT ON TABLE upm.api_interface_system_data20230915 IS 'UPM系统数据(20230915)';

DROP TABLE IF EXISTS upm.api_interface_system_data20230916;
CREATE TABLE upm.api_interface_system_data20230916 (
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
COMMENT ON TABLE upm.api_interface_system_data20230916 IS 'UPM系统数据(20230916)';

DROP TABLE IF EXISTS upm.api_interface_system_data20230917;
CREATE TABLE upm.api_interface_system_data20230917 (
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
COMMENT ON TABLE upm.api_interface_system_data20230917 IS 'UPM系统数据(20230917)';

DROP TABLE IF EXISTS upm.api_interface_system_data20230918;
CREATE TABLE upm.api_interface_system_data20230918 (
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
COMMENT ON TABLE upm.api_interface_system_data20230918 IS 'UPM系统数据(20230918)';

DROP TABLE IF EXISTS upm.api_interface_system_data20230919;
CREATE TABLE upm.api_interface_system_data20230919 (
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
COMMENT ON TABLE upm.api_interface_system_data20230919 IS 'UPM系统数据(20230919)';

DROP TABLE IF EXISTS upm.api_interface_system_data20230920;
CREATE TABLE upm.api_interface_system_data20230920 (
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
COMMENT ON TABLE upm.api_interface_system_data20230920 IS 'UPM系统数据(20230920)';

DROP TABLE IF EXISTS upm.api_interface_system_data20230921;
CREATE TABLE upm.api_interface_system_data20230921 (
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
COMMENT ON TABLE upm.api_interface_system_data20230921 IS 'UPM系统数据(20230921)';

DROP TABLE IF EXISTS upm.api_interface_system_data20230922;
CREATE TABLE upm.api_interface_system_data20230922 (
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
COMMENT ON TABLE upm.api_interface_system_data20230922 IS 'UPM系统数据(20230922)';

DROP TABLE IF EXISTS upm.api_system_records;
CREATE TABLE upm.api_system_records (
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
