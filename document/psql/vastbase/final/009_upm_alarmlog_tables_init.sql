-- Vastbase migration script for upm alarmlogabnormalbehavior_* table structures converted from upm_结构.sql
-- Generated from document/psql/mysql/ucloud_结构.sql and upm_结构.sql as applicable.

-- upm tables
CREATE SCHEMA IF NOT EXISTS upm;
SET search_path TO upm, public;

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20200505 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20200605 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20200705 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20200805 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20200905 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201002 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201002 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201003 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201003 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201004 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201004 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201005 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201005 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201006 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201006 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201007 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201007 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201008 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201008 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201009 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201009 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201010 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201010 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201011 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201011 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201012 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201012 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201013 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201013 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201014 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201014 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201015 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201015 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201016 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201016 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201017 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201017 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201018 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201018 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201019 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201019 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201020 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201020 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201021 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201021 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201022 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201022 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201024 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201024 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201025 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201025 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201026 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201026 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201027 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201027 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201028 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201028 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201029 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201029 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201030 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201030 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201031 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201031 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201101 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201101 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201102 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201102 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201103 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201103 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201104 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201104 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201105 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201105 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201106 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201106 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201107 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201107 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201108 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201108 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201109 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201109 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201110 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201110 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201111 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201111 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201112 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201112 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201113 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201113 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201114 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201114 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201115 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201115 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201116 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201116 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201117 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201117 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201118 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201118 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201119 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201119 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201120 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201120 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201121 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201121 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201122 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201122 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201123 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201123 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201124 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201124 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201125 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201125 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201126 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201126 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201127 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201127 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201128 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201128 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201129 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201129 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201130 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201130 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201201 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201201 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201202 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201202 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201203 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201203 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201204 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201204 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201205 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201205 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201206 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201206 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201207 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201207 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201208 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201208 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201209 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201209 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201210 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201210 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201211 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201211 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201212 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201212 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201213 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201213 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201214 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201214 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201215 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20201215 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201216 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201217 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201218 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201219 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201220 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201221 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201222 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201223 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201224 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201225 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201226 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201227 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201228 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20201229 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210101 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210102 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210103 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210104 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210105 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210106 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210107 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210108 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210109 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210110 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210111 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210112 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210113 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210114 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210115 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210116 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210117 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210118 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210119 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210120 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210121 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210122 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210123 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210124 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210125 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210126 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210127 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210128 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210129 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210130 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(19) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210131 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210131 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210201 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210201 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210202 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210202 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210203 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210203 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210204 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210204 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210205 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210205 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210206 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210206 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210207 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210207 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210208 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210208 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210209 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210209 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210210 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210210 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210211 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210211 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210212 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210212 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210213 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210213 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210214 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210214 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210215 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210215 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210216 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210216 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210217 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210217 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210218 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210218 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210219 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210219 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210220 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210220 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210221 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210221 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210222 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210222 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210223 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210223 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210224 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210224 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210225 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210225 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210226 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210226 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210227 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210227 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210228 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210228 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210301 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210301 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210302 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210302 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210303 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210303 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210304 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210304 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210305 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210305 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210306 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210306 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210307 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210307 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210308 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210308 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210309 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210309 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210310 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210310 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210311 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210311 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210312 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210312 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210313 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210313 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210314 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210314 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210315 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210315 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210316 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210316 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210317 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210317 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210318 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210318 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210319 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210319 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210320 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210320 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210321 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210321 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210322 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210322 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210323 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210323 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210324 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210324 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210325 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210325 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210326 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210326 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210327 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210327 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210328 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210328 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210329 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210329 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210330 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210330 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210331 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210331 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210401 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210401 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210402 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210402 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210403 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210403 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210404 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210404 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210405 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210405 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210406 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210406 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210407 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210407 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210408 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210408 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210409 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210409 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210410 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210410 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210411 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210411 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210412 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210412 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210413 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210413 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210414 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210414 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210415 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210415 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210416 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210416 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210417 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210417 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210418 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210418 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210419 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210419 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210420 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210420 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210421 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210421 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210422 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210422 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210423 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210423 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210424 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210424 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210425 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210425 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210426 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210426 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210427 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210427 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210428 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210428 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210429 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210429 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210430 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210430 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210501 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210501 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210502 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210502 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210503 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210503 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210504 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210504 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210505 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210505 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210506 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210506 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210507 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210507 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210508 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210508 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210509 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210509 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210510 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210510 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210511 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210511 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210512 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210512 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210513 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210513 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210514 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210514 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210515 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210515 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210516 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210516 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210517 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210517 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210518 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210518 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210519 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210519 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210520 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210520 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210521 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210521 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210522 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210522 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210523 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210523 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210524 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210524 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210525 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210525 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210526 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210526 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210527 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210527 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210528 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210528 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210529 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210529 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210530 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210530 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210531 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210531 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210601 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210601 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210602 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210602 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210603 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210603 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210604 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210604 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210605 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210605 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210606 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210606 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210607 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210607 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210608 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210608 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210609 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210609 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210610 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210610 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210611 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210611 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210612 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210612 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210613 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210613 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210614 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210614 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210615 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210615 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210616 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210616 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210617 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210617 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210618 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210618 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210619 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210619 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210620 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210620 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210621 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210621 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210622 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210622 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210623 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210623 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210624 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210624 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210625 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210625 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210626 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210626 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210627 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210627 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210628 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210628 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210629 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210629 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210630 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210630 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210701 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210701 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210702 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210702 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210703 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210703 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210704 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210704 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210705 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210705 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210706 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210706 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210707 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210707 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210708 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210708 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210709 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210709 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210710 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210710 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210711 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210711 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210712 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210712 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210713 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210713 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210714 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210714 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210715 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210715 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210716 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210716 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210717 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210717 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210718 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210718 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210719 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210719 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210720 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210720 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210721 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210721 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210722 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210722 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210723 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210723 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210724 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210724 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210725 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210725 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210726 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210726 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210727 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210727 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210728 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210728 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210729 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210729 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210730 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210730 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210731 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210731 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210801 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210801 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210802 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210802 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210803 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210803 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210804 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210804 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210805 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210805 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210806 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210806 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210807 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210807 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210808 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210808 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210809 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210809 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210810 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210810 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210811 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210811 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210812 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210812 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210813 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210813 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210814 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210814 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210815 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210815 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210816 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210816 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210817 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210817 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210818 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210818 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210819 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210819 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210820 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210820 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210821 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210821 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210822 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210822 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210823 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210823 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210824 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210824 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210825 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210825 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210826 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210826 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210827 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210827 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210828 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210828 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210829 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210829 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210830 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210830 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210831 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210831 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210901 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210901 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210902 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210902 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210903 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210903 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210904 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210904 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210905 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210905 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210906 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210906 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210907 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210907 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210908 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210908 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210909 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210909 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210910 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210910 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210911 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210911 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210912 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210912 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210913 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210913 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210914 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210914 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210915 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210915 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210916 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210916 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210917 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210917 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210918 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210918 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210919 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210919 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210920 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210920 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210921 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210921 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210922 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210922 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210923 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210923 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210924 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210924 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210925 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210925 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210926 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210926 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210927 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210927 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210928 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210928 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210929 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210929 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20210930 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20210930 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211001 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211001 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211002 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211002 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211003 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211003 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211004 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211004 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211005 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211005 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211006 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211006 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211007 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211007 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211008 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211008 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211009 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211009 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211010 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211010 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211011 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211011 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211012 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211012 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211013 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211013 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211014 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211014 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211015 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211015 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211016 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211016 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211017 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211017 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211018 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211018 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211019 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211019 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211020 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211020 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211021 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211021 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211022 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211022 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211023 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211023 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211024 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211024 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211025 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211025 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211026 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211026 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211027 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211027 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211028 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211028 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211029 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211029 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211030 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211030 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211031 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211031 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211101 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211101 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211102 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211102 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211103 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211103 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211104 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211104 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211105 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211105 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211106 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211106 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211107 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211107 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211108 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211108 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211109 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211109 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211110 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211110 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211111 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211111 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211112 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211112 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211113 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211113 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211114 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211114 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211115 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211115 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211116 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211116 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211117 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211117 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211118 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211118 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211119 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211119 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211120 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211120 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211121 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211121 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211122 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211122 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211123 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211123 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211124 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211124 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211125 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211125 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211126 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211126 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211127 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211127 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211128 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211128 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211129 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211129 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211130 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211130 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211201 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211201 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211202 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211202 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211203 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211203 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211204 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211204 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211205 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211205 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211206 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211206 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211207 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211207 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211208 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211208 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211209 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211209 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211210 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211210 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211211 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211211 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211212 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211212 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211213 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211213 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211214 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211214 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211215 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211215 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211216 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211216 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211217 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211217 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211218 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211218 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211219 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211219 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211220 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211220 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211221 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211221 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211222 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211222 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211223 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211223 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211224 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211224 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211225 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211225 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211226 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211226 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211227 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211227 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211228 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211228 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211229 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211229 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211230 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211230 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20211231 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20211231 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220101 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220101 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220102 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220102 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220103 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220103 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220104 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220104 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220105 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220105 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220106 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220106 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220107 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220107 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220108 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220108 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220109 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220109 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220110 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220110 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220111 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220111 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220112 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220112 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220113 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220113 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220114 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220114 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220115 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220115 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220116 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220116 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220117 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220117 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220118 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220118 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220119 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220119 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220120 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220120 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220121 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220121 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220122 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220122 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220123 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220123 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220124 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220124 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220125 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220125 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220126 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220126 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220127 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220127 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220128 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220128 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220129 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220129 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220130 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220130 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220131 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220131 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220201 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220201 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220202 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220202 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220203 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220203 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220204 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220204 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220205 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220205 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220206 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220206 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220207 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220207 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220208 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220208 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220209 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220209 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220210 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220210 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220211 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220211 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220212 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220212 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220213 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220213 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220214 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220214 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220215 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220215 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220216 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220216 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220217 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220217 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220218 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220218 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220219 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220219 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220220 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220220 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220221 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220221 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220222 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220222 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220223 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220223 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220224 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220224 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220225 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220225 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220226 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220226 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220227 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220227 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220228 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220228 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220301 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220301 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220302 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220302 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220303 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220303 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220304 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220304 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220305 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220305 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220306 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220306 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220307 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220307 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220308 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220308 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220309 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220309 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220310 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220310 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220311 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220311 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220312 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220312 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220313 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220313 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220314 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220314 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220315 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220315 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220316 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220316 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220317 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220317 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20220318 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20220318 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20230913 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20230913 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20230914 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20230914 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20230915 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20230915 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20230916 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20230916 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20230917 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20230917 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20230918 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20230918 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20230919 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20230919 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20230920 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20230920 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20230921 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20230921 IS '异常行为日志表';

CREATE TABLE IF NOT EXISTS upm.alarmlogabnormalbehavior_20230922 (
    id varchar(32) NOT NULL,
    category varchar(100) DEFAULT NULL,
    name varchar(500) DEFAULT NULL,
    clientIpAddr varchar(20) DEFAULT NULL,
    clientPort varchar(10) DEFAULT NULL,
    clientNetsegmentId varchar(20) DEFAULT NULL,
    serverIpAddr varchar(20) DEFAULT NULL,
    serverPort varchar(10) DEFAULT NULL,
    serverNetsegmentId varchar(20) DEFAULT NULL,
    ipAddr varchar(20) DEFAULT NULL,
    netsegmentId varchar(20) DEFAULT NULL,
    level varchar(10) DEFAULT NULL,
    time varchar(32) DEFAULT NULL,
    triggerCondition text NULL,
    probeId varchar(40) DEFAULT NULL,
    ADD_DATE varchar(20) DEFAULT NULL,
    PRIMARY KEY (id)
);
COMMENT ON TABLE upm.alarmlogabnormalbehavior_20230922 IS '异常行为日志表';

