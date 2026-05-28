-- Vastbase migration script for upm netperformanceeventlog_* table structures converted from upm_结构.sql
-- Generated from document/psql/mysql/ucloud_结构.sql and upm_结构.sql as applicable.

CREATE SCHEMA IF NOT EXISTS upm;
SET search_path TO upm, public;

DROP TABLE IF EXISTS upm.netperformanceeventlog_20200505;
CREATE TABLE upm.netperformanceeventlog_20200505 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL
);

DROP TABLE IF EXISTS upm.netperformanceeventlog_20200605;
CREATE TABLE upm.netperformanceeventlog_20200605 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL
);

DROP TABLE IF EXISTS upm.netperformanceeventlog_20200705;
CREATE TABLE upm.netperformanceeventlog_20200705 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL
);

DROP TABLE IF EXISTS upm.netperformanceeventlog_20200805;
CREATE TABLE upm.netperformanceeventlog_20200805 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL
);

DROP TABLE IF EXISTS upm.netperformanceeventlog_20200905;
CREATE TABLE upm.netperformanceeventlog_20200905 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL
);

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201002;
CREATE TABLE upm.netperformanceeventlog_20201002 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201002 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201003;
CREATE TABLE upm.netperformanceeventlog_20201003 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201003 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201004;
CREATE TABLE upm.netperformanceeventlog_20201004 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201004 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201005;
CREATE TABLE upm.netperformanceeventlog_20201005 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201005 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201006;
CREATE TABLE upm.netperformanceeventlog_20201006 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201006 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201007;
CREATE TABLE upm.netperformanceeventlog_20201007 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201007 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201008;
CREATE TABLE upm.netperformanceeventlog_20201008 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201008 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201009;
CREATE TABLE upm.netperformanceeventlog_20201009 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201009 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201010;
CREATE TABLE upm.netperformanceeventlog_20201010 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201010 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201011;
CREATE TABLE upm.netperformanceeventlog_20201011 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201011 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201012;
CREATE TABLE upm.netperformanceeventlog_20201012 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201012 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201013;
CREATE TABLE upm.netperformanceeventlog_20201013 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201013 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201014;
CREATE TABLE upm.netperformanceeventlog_20201014 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201014 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201015;
CREATE TABLE upm.netperformanceeventlog_20201015 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201015 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201016;
CREATE TABLE upm.netperformanceeventlog_20201016 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201016 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201017;
CREATE TABLE upm.netperformanceeventlog_20201017 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201017 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201018;
CREATE TABLE upm.netperformanceeventlog_20201018 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201018 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201019;
CREATE TABLE upm.netperformanceeventlog_20201019 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201019 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201020;
CREATE TABLE upm.netperformanceeventlog_20201020 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201020 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201021;
CREATE TABLE upm.netperformanceeventlog_20201021 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201021 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201022;
CREATE TABLE upm.netperformanceeventlog_20201022 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201022 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201024;
CREATE TABLE upm.netperformanceeventlog_20201024 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201024 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201025;
CREATE TABLE upm.netperformanceeventlog_20201025 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201025 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201026;
CREATE TABLE upm.netperformanceeventlog_20201026 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201026 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201027;
CREATE TABLE upm.netperformanceeventlog_20201027 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201027 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201028;
CREATE TABLE upm.netperformanceeventlog_20201028 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201028 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201029;
CREATE TABLE upm.netperformanceeventlog_20201029 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201029 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201030;
CREATE TABLE upm.netperformanceeventlog_20201030 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201030 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201031;
CREATE TABLE upm.netperformanceeventlog_20201031 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201031 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201101;
CREATE TABLE upm.netperformanceeventlog_20201101 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201101 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201102;
CREATE TABLE upm.netperformanceeventlog_20201102 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201102 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201103;
CREATE TABLE upm.netperformanceeventlog_20201103 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201103 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201104;
CREATE TABLE upm.netperformanceeventlog_20201104 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201104 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201105;
CREATE TABLE upm.netperformanceeventlog_20201105 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201105 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201106;
CREATE TABLE upm.netperformanceeventlog_20201106 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201106 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201107;
CREATE TABLE upm.netperformanceeventlog_20201107 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201107 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201108;
CREATE TABLE upm.netperformanceeventlog_20201108 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201108 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201109;
CREATE TABLE upm.netperformanceeventlog_20201109 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201109 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201110;
CREATE TABLE upm.netperformanceeventlog_20201110 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201110 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201111;
CREATE TABLE upm.netperformanceeventlog_20201111 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201111 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201112;
CREATE TABLE upm.netperformanceeventlog_20201112 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201112 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201113;
CREATE TABLE upm.netperformanceeventlog_20201113 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201113 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201114;
CREATE TABLE upm.netperformanceeventlog_20201114 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201114 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201115;
CREATE TABLE upm.netperformanceeventlog_20201115 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201115 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201116;
CREATE TABLE upm.netperformanceeventlog_20201116 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201116 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201117;
CREATE TABLE upm.netperformanceeventlog_20201117 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201117 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201118;
CREATE TABLE upm.netperformanceeventlog_20201118 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201118 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201119;
CREATE TABLE upm.netperformanceeventlog_20201119 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201119 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201120;
CREATE TABLE upm.netperformanceeventlog_20201120 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201120 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201121;
CREATE TABLE upm.netperformanceeventlog_20201121 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201121 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201122;
CREATE TABLE upm.netperformanceeventlog_20201122 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201122 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201123;
CREATE TABLE upm.netperformanceeventlog_20201123 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201123 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201124;
CREATE TABLE upm.netperformanceeventlog_20201124 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201124 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201125;
CREATE TABLE upm.netperformanceeventlog_20201125 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201125 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201126;
CREATE TABLE upm.netperformanceeventlog_20201126 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201126 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201127;
CREATE TABLE upm.netperformanceeventlog_20201127 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201127 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201128;
CREATE TABLE upm.netperformanceeventlog_20201128 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201128 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201129;
CREATE TABLE upm.netperformanceeventlog_20201129 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201129 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201130;
CREATE TABLE upm.netperformanceeventlog_20201130 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201130 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201201;
CREATE TABLE upm.netperformanceeventlog_20201201 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201201 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201202;
CREATE TABLE upm.netperformanceeventlog_20201202 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201202 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201203;
CREATE TABLE upm.netperformanceeventlog_20201203 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201203 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201204;
CREATE TABLE upm.netperformanceeventlog_20201204 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201204 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201205;
CREATE TABLE upm.netperformanceeventlog_20201205 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201205 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201206;
CREATE TABLE upm.netperformanceeventlog_20201206 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201206 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201207;
CREATE TABLE upm.netperformanceeventlog_20201207 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201207 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201208;
CREATE TABLE upm.netperformanceeventlog_20201208 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201208 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201209;
CREATE TABLE upm.netperformanceeventlog_20201209 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201209 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201210;
CREATE TABLE upm.netperformanceeventlog_20201210 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201210 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201211;
CREATE TABLE upm.netperformanceeventlog_20201211 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201211 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201212;
CREATE TABLE upm.netperformanceeventlog_20201212 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201212 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201213;
CREATE TABLE upm.netperformanceeventlog_20201213 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201213 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201214;
CREATE TABLE upm.netperformanceeventlog_20201214 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201214 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201215;
CREATE TABLE upm.netperformanceeventlog_20201215 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20201215 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201216;
CREATE TABLE upm.netperformanceeventlog_20201216 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201217;
CREATE TABLE upm.netperformanceeventlog_20201217 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201218;
CREATE TABLE upm.netperformanceeventlog_20201218 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201219;
CREATE TABLE upm.netperformanceeventlog_20201219 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201220;
CREATE TABLE upm.netperformanceeventlog_20201220 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201221;
CREATE TABLE upm.netperformanceeventlog_20201221 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201222;
CREATE TABLE upm.netperformanceeventlog_20201222 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201223;
CREATE TABLE upm.netperformanceeventlog_20201223 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201224;
CREATE TABLE upm.netperformanceeventlog_20201224 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201225;
CREATE TABLE upm.netperformanceeventlog_20201225 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201226;
CREATE TABLE upm.netperformanceeventlog_20201226 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201227;
CREATE TABLE upm.netperformanceeventlog_20201227 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201228;
CREATE TABLE upm.netperformanceeventlog_20201228 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201229;
CREATE TABLE upm.netperformanceeventlog_20201229 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201230;
CREATE TABLE upm.netperformanceeventlog_20201230 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.netperformanceeventlog_20201231;
CREATE TABLE upm.netperformanceeventlog_20201231 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210103;
CREATE TABLE upm.netperformanceeventlog_20210103 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210104;
CREATE TABLE upm.netperformanceeventlog_20210104 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210105;
CREATE TABLE upm.netperformanceeventlog_20210105 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210106;
CREATE TABLE upm.netperformanceeventlog_20210106 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210107;
CREATE TABLE upm.netperformanceeventlog_20210107 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210108;
CREATE TABLE upm.netperformanceeventlog_20210108 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210109;
CREATE TABLE upm.netperformanceeventlog_20210109 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210110;
CREATE TABLE upm.netperformanceeventlog_20210110 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210111;
CREATE TABLE upm.netperformanceeventlog_20210111 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210112;
CREATE TABLE upm.netperformanceeventlog_20210112 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210113;
CREATE TABLE upm.netperformanceeventlog_20210113 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210117;
CREATE TABLE upm.netperformanceeventlog_20210117 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210118;
CREATE TABLE upm.netperformanceeventlog_20210118 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210119;
CREATE TABLE upm.netperformanceeventlog_20210119 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210120;
CREATE TABLE upm.netperformanceeventlog_20210120 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210121;
CREATE TABLE upm.netperformanceeventlog_20210121 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210122;
CREATE TABLE upm.netperformanceeventlog_20210122 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210123;
CREATE TABLE upm.netperformanceeventlog_20210123 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210124;
CREATE TABLE upm.netperformanceeventlog_20210124 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210125;
CREATE TABLE upm.netperformanceeventlog_20210125 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210126;
CREATE TABLE upm.netperformanceeventlog_20210126 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210127;
CREATE TABLE upm.netperformanceeventlog_20210127 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210128;
CREATE TABLE upm.netperformanceeventlog_20210128 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210129;
CREATE TABLE upm.netperformanceeventlog_20210129 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210131;
CREATE TABLE upm.netperformanceeventlog_20210131 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210131 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210201;
CREATE TABLE upm.netperformanceeventlog_20210201 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210201 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210202;
CREATE TABLE upm.netperformanceeventlog_20210202 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210202 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210203;
CREATE TABLE upm.netperformanceeventlog_20210203 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210203 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210204;
CREATE TABLE upm.netperformanceeventlog_20210204 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210204 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210205;
CREATE TABLE upm.netperformanceeventlog_20210205 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210205 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210206;
CREATE TABLE upm.netperformanceeventlog_20210206 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210206 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210207;
CREATE TABLE upm.netperformanceeventlog_20210207 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210207 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210208;
CREATE TABLE upm.netperformanceeventlog_20210208 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210208 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210209;
CREATE TABLE upm.netperformanceeventlog_20210209 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210209 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210210;
CREATE TABLE upm.netperformanceeventlog_20210210 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210210 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210211;
CREATE TABLE upm.netperformanceeventlog_20210211 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210211 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210212;
CREATE TABLE upm.netperformanceeventlog_20210212 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210212 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210213;
CREATE TABLE upm.netperformanceeventlog_20210213 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210213 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210214;
CREATE TABLE upm.netperformanceeventlog_20210214 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210214 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210215;
CREATE TABLE upm.netperformanceeventlog_20210215 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210215 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210216;
CREATE TABLE upm.netperformanceeventlog_20210216 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210216 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210217;
CREATE TABLE upm.netperformanceeventlog_20210217 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210217 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210218;
CREATE TABLE upm.netperformanceeventlog_20210218 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210218 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210219;
CREATE TABLE upm.netperformanceeventlog_20210219 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210219 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210220;
CREATE TABLE upm.netperformanceeventlog_20210220 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210220 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210221;
CREATE TABLE upm.netperformanceeventlog_20210221 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210221 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210222;
CREATE TABLE upm.netperformanceeventlog_20210222 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210222 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210223;
CREATE TABLE upm.netperformanceeventlog_20210223 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210223 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210224;
CREATE TABLE upm.netperformanceeventlog_20210224 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210224 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210225;
CREATE TABLE upm.netperformanceeventlog_20210225 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210225 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210226;
CREATE TABLE upm.netperformanceeventlog_20210226 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210226 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210227;
CREATE TABLE upm.netperformanceeventlog_20210227 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210227 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210228;
CREATE TABLE upm.netperformanceeventlog_20210228 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210228 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210301;
CREATE TABLE upm.netperformanceeventlog_20210301 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210301 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210302;
CREATE TABLE upm.netperformanceeventlog_20210302 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210302 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210303;
CREATE TABLE upm.netperformanceeventlog_20210303 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210303 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210304;
CREATE TABLE upm.netperformanceeventlog_20210304 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210304 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210305;
CREATE TABLE upm.netperformanceeventlog_20210305 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210305 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210306;
CREATE TABLE upm.netperformanceeventlog_20210306 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210306 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210307;
CREATE TABLE upm.netperformanceeventlog_20210307 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210307 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210308;
CREATE TABLE upm.netperformanceeventlog_20210308 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210308 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210309;
CREATE TABLE upm.netperformanceeventlog_20210309 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210309 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210310;
CREATE TABLE upm.netperformanceeventlog_20210310 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210310 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210311;
CREATE TABLE upm.netperformanceeventlog_20210311 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210311 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210312;
CREATE TABLE upm.netperformanceeventlog_20210312 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210312 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210313;
CREATE TABLE upm.netperformanceeventlog_20210313 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210313 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210314;
CREATE TABLE upm.netperformanceeventlog_20210314 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210314 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210315;
CREATE TABLE upm.netperformanceeventlog_20210315 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210315 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210316;
CREATE TABLE upm.netperformanceeventlog_20210316 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210316 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210317;
CREATE TABLE upm.netperformanceeventlog_20210317 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210317 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210318;
CREATE TABLE upm.netperformanceeventlog_20210318 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210318 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210319;
CREATE TABLE upm.netperformanceeventlog_20210319 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210319 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210320;
CREATE TABLE upm.netperformanceeventlog_20210320 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210320 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210321;
CREATE TABLE upm.netperformanceeventlog_20210321 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210321 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210322;
CREATE TABLE upm.netperformanceeventlog_20210322 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210322 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210323;
CREATE TABLE upm.netperformanceeventlog_20210323 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210323 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210324;
CREATE TABLE upm.netperformanceeventlog_20210324 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210324 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210325;
CREATE TABLE upm.netperformanceeventlog_20210325 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210325 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210326;
CREATE TABLE upm.netperformanceeventlog_20210326 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210326 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210327;
CREATE TABLE upm.netperformanceeventlog_20210327 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210327 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210328;
CREATE TABLE upm.netperformanceeventlog_20210328 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210328 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210329;
CREATE TABLE upm.netperformanceeventlog_20210329 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210329 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210330;
CREATE TABLE upm.netperformanceeventlog_20210330 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210330 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210331;
CREATE TABLE upm.netperformanceeventlog_20210331 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210331 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210401;
CREATE TABLE upm.netperformanceeventlog_20210401 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210401 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210402;
CREATE TABLE upm.netperformanceeventlog_20210402 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210402 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210403;
CREATE TABLE upm.netperformanceeventlog_20210403 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210403 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210404;
CREATE TABLE upm.netperformanceeventlog_20210404 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210404 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210405;
CREATE TABLE upm.netperformanceeventlog_20210405 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210405 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210406;
CREATE TABLE upm.netperformanceeventlog_20210406 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210406 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210407;
CREATE TABLE upm.netperformanceeventlog_20210407 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210407 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210408;
CREATE TABLE upm.netperformanceeventlog_20210408 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210408 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210409;
CREATE TABLE upm.netperformanceeventlog_20210409 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210409 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210410;
CREATE TABLE upm.netperformanceeventlog_20210410 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210410 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210411;
CREATE TABLE upm.netperformanceeventlog_20210411 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210411 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210412;
CREATE TABLE upm.netperformanceeventlog_20210412 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210412 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210413;
CREATE TABLE upm.netperformanceeventlog_20210413 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210413 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210414;
CREATE TABLE upm.netperformanceeventlog_20210414 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210414 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210415;
CREATE TABLE upm.netperformanceeventlog_20210415 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210415 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210416;
CREATE TABLE upm.netperformanceeventlog_20210416 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210416 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210417;
CREATE TABLE upm.netperformanceeventlog_20210417 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210417 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210418;
CREATE TABLE upm.netperformanceeventlog_20210418 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210418 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210419;
CREATE TABLE upm.netperformanceeventlog_20210419 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210419 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210420;
CREATE TABLE upm.netperformanceeventlog_20210420 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210420 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210421;
CREATE TABLE upm.netperformanceeventlog_20210421 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210421 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210422;
CREATE TABLE upm.netperformanceeventlog_20210422 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210422 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210423;
CREATE TABLE upm.netperformanceeventlog_20210423 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210423 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210424;
CREATE TABLE upm.netperformanceeventlog_20210424 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210424 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210425;
CREATE TABLE upm.netperformanceeventlog_20210425 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210425 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210426;
CREATE TABLE upm.netperformanceeventlog_20210426 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210426 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210427;
CREATE TABLE upm.netperformanceeventlog_20210427 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210427 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210428;
CREATE TABLE upm.netperformanceeventlog_20210428 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210428 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210429;
CREATE TABLE upm.netperformanceeventlog_20210429 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210429 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210430;
CREATE TABLE upm.netperformanceeventlog_20210430 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210430 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210501;
CREATE TABLE upm.netperformanceeventlog_20210501 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210501 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210502;
CREATE TABLE upm.netperformanceeventlog_20210502 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210502 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210503;
CREATE TABLE upm.netperformanceeventlog_20210503 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210503 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210504;
CREATE TABLE upm.netperformanceeventlog_20210504 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210504 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210505;
CREATE TABLE upm.netperformanceeventlog_20210505 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210505 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210506;
CREATE TABLE upm.netperformanceeventlog_20210506 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210506 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210507;
CREATE TABLE upm.netperformanceeventlog_20210507 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210507 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210508;
CREATE TABLE upm.netperformanceeventlog_20210508 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210508 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210509;
CREATE TABLE upm.netperformanceeventlog_20210509 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210509 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210510;
CREATE TABLE upm.netperformanceeventlog_20210510 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210510 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210511;
CREATE TABLE upm.netperformanceeventlog_20210511 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210511 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210512;
CREATE TABLE upm.netperformanceeventlog_20210512 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210512 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210513;
CREATE TABLE upm.netperformanceeventlog_20210513 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210513 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210514;
CREATE TABLE upm.netperformanceeventlog_20210514 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210514 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210515;
CREATE TABLE upm.netperformanceeventlog_20210515 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210515 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210516;
CREATE TABLE upm.netperformanceeventlog_20210516 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210516 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210517;
CREATE TABLE upm.netperformanceeventlog_20210517 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210517 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210518;
CREATE TABLE upm.netperformanceeventlog_20210518 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210518 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210519;
CREATE TABLE upm.netperformanceeventlog_20210519 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210519 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210520;
CREATE TABLE upm.netperformanceeventlog_20210520 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210520 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210521;
CREATE TABLE upm.netperformanceeventlog_20210521 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210521 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210522;
CREATE TABLE upm.netperformanceeventlog_20210522 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210522 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210523;
CREATE TABLE upm.netperformanceeventlog_20210523 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210523 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210524;
CREATE TABLE upm.netperformanceeventlog_20210524 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210524 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210525;
CREATE TABLE upm.netperformanceeventlog_20210525 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210525 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210526;
CREATE TABLE upm.netperformanceeventlog_20210526 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210526 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210527;
CREATE TABLE upm.netperformanceeventlog_20210527 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210527 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210528;
CREATE TABLE upm.netperformanceeventlog_20210528 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210528 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210529;
CREATE TABLE upm.netperformanceeventlog_20210529 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210529 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210530;
CREATE TABLE upm.netperformanceeventlog_20210530 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210530 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210531;
CREATE TABLE upm.netperformanceeventlog_20210531 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210531 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210601;
CREATE TABLE upm.netperformanceeventlog_20210601 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210601 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210602;
CREATE TABLE upm.netperformanceeventlog_20210602 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210602 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210603;
CREATE TABLE upm.netperformanceeventlog_20210603 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210603 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210604;
CREATE TABLE upm.netperformanceeventlog_20210604 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210604 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210605;
CREATE TABLE upm.netperformanceeventlog_20210605 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210605 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210606;
CREATE TABLE upm.netperformanceeventlog_20210606 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210606 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210607;
CREATE TABLE upm.netperformanceeventlog_20210607 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210607 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210608;
CREATE TABLE upm.netperformanceeventlog_20210608 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210608 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210609;
CREATE TABLE upm.netperformanceeventlog_20210609 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210609 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210610;
CREATE TABLE upm.netperformanceeventlog_20210610 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210610 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210611;
CREATE TABLE upm.netperformanceeventlog_20210611 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210611 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210612;
CREATE TABLE upm.netperformanceeventlog_20210612 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210612 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210613;
CREATE TABLE upm.netperformanceeventlog_20210613 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210613 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210614;
CREATE TABLE upm.netperformanceeventlog_20210614 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210614 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210615;
CREATE TABLE upm.netperformanceeventlog_20210615 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210615 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210616;
CREATE TABLE upm.netperformanceeventlog_20210616 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210616 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210617;
CREATE TABLE upm.netperformanceeventlog_20210617 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210617 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210618;
CREATE TABLE upm.netperformanceeventlog_20210618 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210618 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210619;
CREATE TABLE upm.netperformanceeventlog_20210619 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210619 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210620;
CREATE TABLE upm.netperformanceeventlog_20210620 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210620 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210621;
CREATE TABLE upm.netperformanceeventlog_20210621 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210621 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210622;
CREATE TABLE upm.netperformanceeventlog_20210622 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210622 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210623;
CREATE TABLE upm.netperformanceeventlog_20210623 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210623 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210624;
CREATE TABLE upm.netperformanceeventlog_20210624 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210624 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210625;
CREATE TABLE upm.netperformanceeventlog_20210625 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210625 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210626;
CREATE TABLE upm.netperformanceeventlog_20210626 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210626 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210627;
CREATE TABLE upm.netperformanceeventlog_20210627 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210627 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210628;
CREATE TABLE upm.netperformanceeventlog_20210628 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210628 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210629;
CREATE TABLE upm.netperformanceeventlog_20210629 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210629 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210630;
CREATE TABLE upm.netperformanceeventlog_20210630 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210630 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210701;
CREATE TABLE upm.netperformanceeventlog_20210701 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210701 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210702;
CREATE TABLE upm.netperformanceeventlog_20210702 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210702 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210703;
CREATE TABLE upm.netperformanceeventlog_20210703 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210703 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210704;
CREATE TABLE upm.netperformanceeventlog_20210704 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210704 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210705;
CREATE TABLE upm.netperformanceeventlog_20210705 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210705 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210706;
CREATE TABLE upm.netperformanceeventlog_20210706 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210706 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210707;
CREATE TABLE upm.netperformanceeventlog_20210707 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210707 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210708;
CREATE TABLE upm.netperformanceeventlog_20210708 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210708 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210709;
CREATE TABLE upm.netperformanceeventlog_20210709 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210709 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210710;
CREATE TABLE upm.netperformanceeventlog_20210710 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210710 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210711;
CREATE TABLE upm.netperformanceeventlog_20210711 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210711 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210712;
CREATE TABLE upm.netperformanceeventlog_20210712 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210712 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210713;
CREATE TABLE upm.netperformanceeventlog_20210713 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210713 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210714;
CREATE TABLE upm.netperformanceeventlog_20210714 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210714 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210715;
CREATE TABLE upm.netperformanceeventlog_20210715 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210715 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210716;
CREATE TABLE upm.netperformanceeventlog_20210716 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210716 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210717;
CREATE TABLE upm.netperformanceeventlog_20210717 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210717 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210718;
CREATE TABLE upm.netperformanceeventlog_20210718 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210718 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210719;
CREATE TABLE upm.netperformanceeventlog_20210719 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210719 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210720;
CREATE TABLE upm.netperformanceeventlog_20210720 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210720 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210721;
CREATE TABLE upm.netperformanceeventlog_20210721 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210721 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210722;
CREATE TABLE upm.netperformanceeventlog_20210722 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210722 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210723;
CREATE TABLE upm.netperformanceeventlog_20210723 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210723 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210724;
CREATE TABLE upm.netperformanceeventlog_20210724 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210724 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210725;
CREATE TABLE upm.netperformanceeventlog_20210725 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210725 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210726;
CREATE TABLE upm.netperformanceeventlog_20210726 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210726 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210727;
CREATE TABLE upm.netperformanceeventlog_20210727 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210727 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210728;
CREATE TABLE upm.netperformanceeventlog_20210728 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210728 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210729;
CREATE TABLE upm.netperformanceeventlog_20210729 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210729 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210730;
CREATE TABLE upm.netperformanceeventlog_20210730 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210730 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210731;
CREATE TABLE upm.netperformanceeventlog_20210731 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210731 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210801;
CREATE TABLE upm.netperformanceeventlog_20210801 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210801 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210802;
CREATE TABLE upm.netperformanceeventlog_20210802 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210802 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210803;
CREATE TABLE upm.netperformanceeventlog_20210803 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210803 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210804;
CREATE TABLE upm.netperformanceeventlog_20210804 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210804 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210805;
CREATE TABLE upm.netperformanceeventlog_20210805 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210805 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210806;
CREATE TABLE upm.netperformanceeventlog_20210806 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210806 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210807;
CREATE TABLE upm.netperformanceeventlog_20210807 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210807 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210808;
CREATE TABLE upm.netperformanceeventlog_20210808 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210808 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210809;
CREATE TABLE upm.netperformanceeventlog_20210809 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210809 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210810;
CREATE TABLE upm.netperformanceeventlog_20210810 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210810 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210811;
CREATE TABLE upm.netperformanceeventlog_20210811 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210811 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210812;
CREATE TABLE upm.netperformanceeventlog_20210812 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210812 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210813;
CREATE TABLE upm.netperformanceeventlog_20210813 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210813 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210814;
CREATE TABLE upm.netperformanceeventlog_20210814 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210814 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210815;
CREATE TABLE upm.netperformanceeventlog_20210815 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210815 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210816;
CREATE TABLE upm.netperformanceeventlog_20210816 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210816 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210817;
CREATE TABLE upm.netperformanceeventlog_20210817 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210817 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210818;
CREATE TABLE upm.netperformanceeventlog_20210818 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210818 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210819;
CREATE TABLE upm.netperformanceeventlog_20210819 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210819 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210820;
CREATE TABLE upm.netperformanceeventlog_20210820 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210820 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210821;
CREATE TABLE upm.netperformanceeventlog_20210821 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210821 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210822;
CREATE TABLE upm.netperformanceeventlog_20210822 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210822 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210823;
CREATE TABLE upm.netperformanceeventlog_20210823 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210823 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210824;
CREATE TABLE upm.netperformanceeventlog_20210824 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210824 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210825;
CREATE TABLE upm.netperformanceeventlog_20210825 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210825 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210826;
CREATE TABLE upm.netperformanceeventlog_20210826 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210826 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210827;
CREATE TABLE upm.netperformanceeventlog_20210827 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210827 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210828;
CREATE TABLE upm.netperformanceeventlog_20210828 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210828 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210829;
CREATE TABLE upm.netperformanceeventlog_20210829 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210829 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210830;
CREATE TABLE upm.netperformanceeventlog_20210830 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210830 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210831;
CREATE TABLE upm.netperformanceeventlog_20210831 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210831 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210901;
CREATE TABLE upm.netperformanceeventlog_20210901 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210901 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210902;
CREATE TABLE upm.netperformanceeventlog_20210902 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210902 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210903;
CREATE TABLE upm.netperformanceeventlog_20210903 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210903 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210904;
CREATE TABLE upm.netperformanceeventlog_20210904 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210904 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210905;
CREATE TABLE upm.netperformanceeventlog_20210905 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210905 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210906;
CREATE TABLE upm.netperformanceeventlog_20210906 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210906 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210907;
CREATE TABLE upm.netperformanceeventlog_20210907 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210907 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210908;
CREATE TABLE upm.netperformanceeventlog_20210908 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210908 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210909;
CREATE TABLE upm.netperformanceeventlog_20210909 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210909 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210910;
CREATE TABLE upm.netperformanceeventlog_20210910 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210910 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210911;
CREATE TABLE upm.netperformanceeventlog_20210911 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210911 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210912;
CREATE TABLE upm.netperformanceeventlog_20210912 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210912 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210913;
CREATE TABLE upm.netperformanceeventlog_20210913 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210913 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210914;
CREATE TABLE upm.netperformanceeventlog_20210914 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210914 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210915;
CREATE TABLE upm.netperformanceeventlog_20210915 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210915 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210916;
CREATE TABLE upm.netperformanceeventlog_20210916 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210916 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210917;
CREATE TABLE upm.netperformanceeventlog_20210917 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210917 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210918;
CREATE TABLE upm.netperformanceeventlog_20210918 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210918 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210919;
CREATE TABLE upm.netperformanceeventlog_20210919 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210919 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210920;
CREATE TABLE upm.netperformanceeventlog_20210920 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210920 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210921;
CREATE TABLE upm.netperformanceeventlog_20210921 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210921 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210922;
CREATE TABLE upm.netperformanceeventlog_20210922 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210922 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210923;
CREATE TABLE upm.netperformanceeventlog_20210923 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210923 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210924;
CREATE TABLE upm.netperformanceeventlog_20210924 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210924 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210925;
CREATE TABLE upm.netperformanceeventlog_20210925 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210925 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210926;
CREATE TABLE upm.netperformanceeventlog_20210926 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210926 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210927;
CREATE TABLE upm.netperformanceeventlog_20210927 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210927 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210928;
CREATE TABLE upm.netperformanceeventlog_20210928 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210928 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210929;
CREATE TABLE upm.netperformanceeventlog_20210929 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210929 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20210930;
CREATE TABLE upm.netperformanceeventlog_20210930 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20210930 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211001;
CREATE TABLE upm.netperformanceeventlog_20211001 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211001 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211002;
CREATE TABLE upm.netperformanceeventlog_20211002 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211002 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211003;
CREATE TABLE upm.netperformanceeventlog_20211003 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211003 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211004;
CREATE TABLE upm.netperformanceeventlog_20211004 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211004 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211005;
CREATE TABLE upm.netperformanceeventlog_20211005 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211005 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211006;
CREATE TABLE upm.netperformanceeventlog_20211006 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211006 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211007;
CREATE TABLE upm.netperformanceeventlog_20211007 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211007 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211008;
CREATE TABLE upm.netperformanceeventlog_20211008 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211008 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211009;
CREATE TABLE upm.netperformanceeventlog_20211009 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211009 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211010;
CREATE TABLE upm.netperformanceeventlog_20211010 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211010 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211011;
CREATE TABLE upm.netperformanceeventlog_20211011 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211011 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211012;
CREATE TABLE upm.netperformanceeventlog_20211012 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211012 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211013;
CREATE TABLE upm.netperformanceeventlog_20211013 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211013 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211014;
CREATE TABLE upm.netperformanceeventlog_20211014 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211014 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211015;
CREATE TABLE upm.netperformanceeventlog_20211015 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211015 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211016;
CREATE TABLE upm.netperformanceeventlog_20211016 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211016 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211017;
CREATE TABLE upm.netperformanceeventlog_20211017 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211017 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211018;
CREATE TABLE upm.netperformanceeventlog_20211018 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211018 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211019;
CREATE TABLE upm.netperformanceeventlog_20211019 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211019 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211020;
CREATE TABLE upm.netperformanceeventlog_20211020 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211020 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211021;
CREATE TABLE upm.netperformanceeventlog_20211021 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211021 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211022;
CREATE TABLE upm.netperformanceeventlog_20211022 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211022 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211023;
CREATE TABLE upm.netperformanceeventlog_20211023 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211023 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211024;
CREATE TABLE upm.netperformanceeventlog_20211024 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211024 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211025;
CREATE TABLE upm.netperformanceeventlog_20211025 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211025 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211026;
CREATE TABLE upm.netperformanceeventlog_20211026 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211026 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211027;
CREATE TABLE upm.netperformanceeventlog_20211027 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211027 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211028;
CREATE TABLE upm.netperformanceeventlog_20211028 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211028 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211029;
CREATE TABLE upm.netperformanceeventlog_20211029 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211029 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211030;
CREATE TABLE upm.netperformanceeventlog_20211030 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211030 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211031;
CREATE TABLE upm.netperformanceeventlog_20211031 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211031 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211101;
CREATE TABLE upm.netperformanceeventlog_20211101 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211101 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211102;
CREATE TABLE upm.netperformanceeventlog_20211102 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211102 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211103;
CREATE TABLE upm.netperformanceeventlog_20211103 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211103 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211104;
CREATE TABLE upm.netperformanceeventlog_20211104 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211104 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211105;
CREATE TABLE upm.netperformanceeventlog_20211105 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211105 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211106;
CREATE TABLE upm.netperformanceeventlog_20211106 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211106 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211107;
CREATE TABLE upm.netperformanceeventlog_20211107 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211107 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211108;
CREATE TABLE upm.netperformanceeventlog_20211108 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211108 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211109;
CREATE TABLE upm.netperformanceeventlog_20211109 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211109 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211110;
CREATE TABLE upm.netperformanceeventlog_20211110 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211110 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211111;
CREATE TABLE upm.netperformanceeventlog_20211111 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211111 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211112;
CREATE TABLE upm.netperformanceeventlog_20211112 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211112 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211113;
CREATE TABLE upm.netperformanceeventlog_20211113 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211113 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211114;
CREATE TABLE upm.netperformanceeventlog_20211114 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211114 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211115;
CREATE TABLE upm.netperformanceeventlog_20211115 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211115 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211116;
CREATE TABLE upm.netperformanceeventlog_20211116 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211116 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211117;
CREATE TABLE upm.netperformanceeventlog_20211117 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211117 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211118;
CREATE TABLE upm.netperformanceeventlog_20211118 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211118 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211119;
CREATE TABLE upm.netperformanceeventlog_20211119 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211119 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211120;
CREATE TABLE upm.netperformanceeventlog_20211120 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211120 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211121;
CREATE TABLE upm.netperformanceeventlog_20211121 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211121 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211122;
CREATE TABLE upm.netperformanceeventlog_20211122 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211122 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211123;
CREATE TABLE upm.netperformanceeventlog_20211123 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211123 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211124;
CREATE TABLE upm.netperformanceeventlog_20211124 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211124 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211125;
CREATE TABLE upm.netperformanceeventlog_20211125 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211125 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211126;
CREATE TABLE upm.netperformanceeventlog_20211126 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211126 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211127;
CREATE TABLE upm.netperformanceeventlog_20211127 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211127 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211128;
CREATE TABLE upm.netperformanceeventlog_20211128 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211128 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211129;
CREATE TABLE upm.netperformanceeventlog_20211129 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211129 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211130;
CREATE TABLE upm.netperformanceeventlog_20211130 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211130 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211201;
CREATE TABLE upm.netperformanceeventlog_20211201 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211201 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211202;
CREATE TABLE upm.netperformanceeventlog_20211202 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211202 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211203;
CREATE TABLE upm.netperformanceeventlog_20211203 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211203 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211204;
CREATE TABLE upm.netperformanceeventlog_20211204 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211204 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211205;
CREATE TABLE upm.netperformanceeventlog_20211205 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211205 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211206;
CREATE TABLE upm.netperformanceeventlog_20211206 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211206 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211207;
CREATE TABLE upm.netperformanceeventlog_20211207 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211207 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211208;
CREATE TABLE upm.netperformanceeventlog_20211208 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211208 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211209;
CREATE TABLE upm.netperformanceeventlog_20211209 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211209 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211210;
CREATE TABLE upm.netperformanceeventlog_20211210 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211210 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211211;
CREATE TABLE upm.netperformanceeventlog_20211211 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211211 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211212;
CREATE TABLE upm.netperformanceeventlog_20211212 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211212 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211213;
CREATE TABLE upm.netperformanceeventlog_20211213 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211213 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211214;
CREATE TABLE upm.netperformanceeventlog_20211214 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211214 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211215;
CREATE TABLE upm.netperformanceeventlog_20211215 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211215 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211216;
CREATE TABLE upm.netperformanceeventlog_20211216 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211216 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211217;
CREATE TABLE upm.netperformanceeventlog_20211217 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211217 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211218;
CREATE TABLE upm.netperformanceeventlog_20211218 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211218 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211219;
CREATE TABLE upm.netperformanceeventlog_20211219 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211219 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211220;
CREATE TABLE upm.netperformanceeventlog_20211220 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211220 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211221;
CREATE TABLE upm.netperformanceeventlog_20211221 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211221 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211222;
CREATE TABLE upm.netperformanceeventlog_20211222 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211222 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211223;
CREATE TABLE upm.netperformanceeventlog_20211223 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211223 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211224;
CREATE TABLE upm.netperformanceeventlog_20211224 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211224 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211225;
CREATE TABLE upm.netperformanceeventlog_20211225 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211225 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211226;
CREATE TABLE upm.netperformanceeventlog_20211226 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211226 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211227;
CREATE TABLE upm.netperformanceeventlog_20211227 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211227 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211228;
CREATE TABLE upm.netperformanceeventlog_20211228 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211228 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211229;
CREATE TABLE upm.netperformanceeventlog_20211229 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211229 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211230;
CREATE TABLE upm.netperformanceeventlog_20211230 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211230 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20211231;
CREATE TABLE upm.netperformanceeventlog_20211231 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20211231 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220101;
CREATE TABLE upm.netperformanceeventlog_20220101 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220101 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220102;
CREATE TABLE upm.netperformanceeventlog_20220102 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220102 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220103;
CREATE TABLE upm.netperformanceeventlog_20220103 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220103 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220104;
CREATE TABLE upm.netperformanceeventlog_20220104 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220104 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220105;
CREATE TABLE upm.netperformanceeventlog_20220105 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220105 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220106;
CREATE TABLE upm.netperformanceeventlog_20220106 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220106 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220107;
CREATE TABLE upm.netperformanceeventlog_20220107 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220107 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220108;
CREATE TABLE upm.netperformanceeventlog_20220108 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220108 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220109;
CREATE TABLE upm.netperformanceeventlog_20220109 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220109 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220110;
CREATE TABLE upm.netperformanceeventlog_20220110 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220110 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220111;
CREATE TABLE upm.netperformanceeventlog_20220111 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220111 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220112;
CREATE TABLE upm.netperformanceeventlog_20220112 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220112 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220113;
CREATE TABLE upm.netperformanceeventlog_20220113 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220113 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220114;
CREATE TABLE upm.netperformanceeventlog_20220114 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220114 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220115;
CREATE TABLE upm.netperformanceeventlog_20220115 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220115 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220116;
CREATE TABLE upm.netperformanceeventlog_20220116 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220116 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220117;
CREATE TABLE upm.netperformanceeventlog_20220117 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220117 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220118;
CREATE TABLE upm.netperformanceeventlog_20220118 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220118 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220119;
CREATE TABLE upm.netperformanceeventlog_20220119 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220119 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220120;
CREATE TABLE upm.netperformanceeventlog_20220120 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220120 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220121;
CREATE TABLE upm.netperformanceeventlog_20220121 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220121 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220122;
CREATE TABLE upm.netperformanceeventlog_20220122 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220122 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220123;
CREATE TABLE upm.netperformanceeventlog_20220123 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220123 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220124;
CREATE TABLE upm.netperformanceeventlog_20220124 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220124 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220125;
CREATE TABLE upm.netperformanceeventlog_20220125 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220125 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220126;
CREATE TABLE upm.netperformanceeventlog_20220126 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220126 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220127;
CREATE TABLE upm.netperformanceeventlog_20220127 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220127 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220128;
CREATE TABLE upm.netperformanceeventlog_20220128 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220128 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220129;
CREATE TABLE upm.netperformanceeventlog_20220129 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220129 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220130;
CREATE TABLE upm.netperformanceeventlog_20220130 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220130 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220131;
CREATE TABLE upm.netperformanceeventlog_20220131 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220131 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220201;
CREATE TABLE upm.netperformanceeventlog_20220201 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220201 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220202;
CREATE TABLE upm.netperformanceeventlog_20220202 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220202 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220203;
CREATE TABLE upm.netperformanceeventlog_20220203 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220203 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220204;
CREATE TABLE upm.netperformanceeventlog_20220204 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220204 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220205;
CREATE TABLE upm.netperformanceeventlog_20220205 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220205 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220206;
CREATE TABLE upm.netperformanceeventlog_20220206 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220206 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220207;
CREATE TABLE upm.netperformanceeventlog_20220207 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220207 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220208;
CREATE TABLE upm.netperformanceeventlog_20220208 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220208 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220209;
CREATE TABLE upm.netperformanceeventlog_20220209 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220209 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220210;
CREATE TABLE upm.netperformanceeventlog_20220210 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220210 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220211;
CREATE TABLE upm.netperformanceeventlog_20220211 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220211 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220212;
CREATE TABLE upm.netperformanceeventlog_20220212 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220212 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220213;
CREATE TABLE upm.netperformanceeventlog_20220213 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220213 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220214;
CREATE TABLE upm.netperformanceeventlog_20220214 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220214 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220215;
CREATE TABLE upm.netperformanceeventlog_20220215 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220215 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220216;
CREATE TABLE upm.netperformanceeventlog_20220216 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220216 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220217;
CREATE TABLE upm.netperformanceeventlog_20220217 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220217 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220218;
CREATE TABLE upm.netperformanceeventlog_20220218 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220218 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220219;
CREATE TABLE upm.netperformanceeventlog_20220219 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220219 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220220;
CREATE TABLE upm.netperformanceeventlog_20220220 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220220 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220221;
CREATE TABLE upm.netperformanceeventlog_20220221 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220221 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220222;
CREATE TABLE upm.netperformanceeventlog_20220222 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220222 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220223;
CREATE TABLE upm.netperformanceeventlog_20220223 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220223 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220224;
CREATE TABLE upm.netperformanceeventlog_20220224 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220224 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220225;
CREATE TABLE upm.netperformanceeventlog_20220225 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220225 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220226;
CREATE TABLE upm.netperformanceeventlog_20220226 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220226 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220227;
CREATE TABLE upm.netperformanceeventlog_20220227 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220227 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220228;
CREATE TABLE upm.netperformanceeventlog_20220228 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220228 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220301;
CREATE TABLE upm.netperformanceeventlog_20220301 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220301 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220302;
CREATE TABLE upm.netperformanceeventlog_20220302 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220302 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220303;
CREATE TABLE upm.netperformanceeventlog_20220303 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220303 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220304;
CREATE TABLE upm.netperformanceeventlog_20220304 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220304 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220305;
CREATE TABLE upm.netperformanceeventlog_20220305 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220305 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220306;
CREATE TABLE upm.netperformanceeventlog_20220306 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220306 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220307;
CREATE TABLE upm.netperformanceeventlog_20220307 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220307 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220308;
CREATE TABLE upm.netperformanceeventlog_20220308 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220308 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220309;
CREATE TABLE upm.netperformanceeventlog_20220309 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220309 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220310;
CREATE TABLE upm.netperformanceeventlog_20220310 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220310 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220311;
CREATE TABLE upm.netperformanceeventlog_20220311 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220311 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220312;
CREATE TABLE upm.netperformanceeventlog_20220312 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220312 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220313;
CREATE TABLE upm.netperformanceeventlog_20220313 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220313 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220314;
CREATE TABLE upm.netperformanceeventlog_20220314 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220314 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220315;
CREATE TABLE upm.netperformanceeventlog_20220315 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220315 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220316;
CREATE TABLE upm.netperformanceeventlog_20220316 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220316 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220317;
CREATE TABLE upm.netperformanceeventlog_20220317 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220317 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20220318;
CREATE TABLE upm.netperformanceeventlog_20220318 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20220318 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20230913;
CREATE TABLE upm.netperformanceeventlog_20230913 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20230913 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20230914;
CREATE TABLE upm.netperformanceeventlog_20230914 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20230914 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20230915;
CREATE TABLE upm.netperformanceeventlog_20230915 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20230915 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20230916;
CREATE TABLE upm.netperformanceeventlog_20230916 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20230916 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20230917;
CREATE TABLE upm.netperformanceeventlog_20230917 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20230917 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20230918;
CREATE TABLE upm.netperformanceeventlog_20230918 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20230918 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20230919;
CREATE TABLE upm.netperformanceeventlog_20230919 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20230919 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20230920;
CREATE TABLE upm.netperformanceeventlog_20230920 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20230920 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20230921;
CREATE TABLE upm.netperformanceeventlog_20230921 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20230921 IS '网络/SDN性能警报日志';

DROP TABLE IF EXISTS upm.netperformanceeventlog_20230922;
CREATE TABLE upm.netperformanceeventlog_20230922 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    lineId varchar(100) DEFAULT NULL,
    netsegmentId1 varchar(20) DEFAULT NULL,
    netsegmentId2 varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.netperformanceeventlog_20230922 IS '网络/SDN性能警报日志';
