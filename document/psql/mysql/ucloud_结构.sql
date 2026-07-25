/*
 Navicat Premium Data Transfer

 Source Server         : 11.8.160.2
 Source Server Type    : MySQL
 Source Server Version : 50722
 Source Host           : 11.8.160.2:3306
 Source Schema         : ucloud

 Target Server Type    : MySQL
 Target Server Version : 50722
 File Encoding         : 65001

 Date: 25/01/2021 10:09:44
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for api_alarm_summary
-- ----------------------------
DROP TABLE IF EXISTS `api_alarm_summary`;
CREATE TABLE `api_alarm_summary`  (
  `ID` int(32) NOT NULL AUTO_INCREMENT,
  `index_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '指标ID',
  `platform_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '平台ID',
  `resource_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源ID',
  `resource_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源名称',
  `resource_ip` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源ip',
  `period` varchar(2) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '周期(1：半小时；2：一小时；3：日；4：月；5：年)',
  `time` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '时间',
  `time_date` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '插入数据日期',
  `Count` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '内容',
  `add_time` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '新增时间',
  `table_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '数据源表',
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `index_id`(`index_id`) USING BTREE,
  INDEX `platform_id`(`platform_id`) USING BTREE,
  INDEX `resource_id`(`resource_id`) USING BTREE,
  INDEX `period`(`period`) USING BTREE,
  INDEX `time`(`time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1963157 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '监测器告警汇总表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for api_alarm_summary_copy1
-- ----------------------------
DROP TABLE IF EXISTS `api_alarm_summary_copy1`;
CREATE TABLE `api_alarm_summary_copy1`  (
  `ID` int(32) NOT NULL AUTO_INCREMENT,
  `index_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '指标ID',
  `platform_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '平台ID',
  `resource_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源ID',
  `resource_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源名称',
  `resource_ip` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源ip',
  `period` varchar(2) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '周期(1：半小时；2：一小时；3：日；4：月；5：年)',
  `time` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '时间',
  `time_date` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '插入数据日期',
  `Count` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '内容',
  `add_time` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '新增时间',
  `table_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '数据源表',
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `index_id`(`index_id`) USING BTREE,
  INDEX `platform_id`(`platform_id`) USING BTREE,
  INDEX `resource_id`(`resource_id`) USING BTREE,
  INDEX `period`(`period`) USING BTREE,
  INDEX `time`(`time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1671773 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '监测器告警汇总表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for api_forecast_alarm_records
-- ----------------------------
DROP TABLE IF EXISTS `api_forecast_alarm_records`;
CREATE TABLE `api_forecast_alarm_records`  (
  `ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `index_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '指标id',
  `index_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '指标名称',
  `resource_name` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源名称',
  `alarm_time` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '警报时间',
  `forecast_value` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '预测值',
  `actual_value` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '实际值',
  `error_value` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '误差值',
  `is_shared` varchar(2) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '0' COMMENT '是否告警(0：否，1：是)',
  `add_time` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '添加时间',
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `alarm_time`(`alarm_time`) USING BTREE,
  INDEX `is_shared`(`is_shared`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '监测器告警预测记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for api_interface_alarm_data
-- ----------------------------
DROP TABLE IF EXISTS `api_interface_alarm_data`;
CREATE TABLE `api_interface_alarm_data`  (
  `ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `index_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '指标代码',
  `resource_name` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源名称',
  `resource_ip` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源ip',
  `alarm_count` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '告警次数',
  `firstOccurTime` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '第一次告警时间',
  `lastOccurTime` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '最后一次告警时间',
  `description` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '告警内容',
  `add_time` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '新增时间',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '优云监测器告警数据' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for api_interface_system_data202004
-- ----------------------------
DROP TABLE IF EXISTS `api_interface_system_data202004`;
CREATE TABLE `api_interface_system_data202004`  (
  `id` varchar(39) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `system_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '系统ID',
  `platform_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '平台ID',
  `index_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '指标ID',
  `resource_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源ID',
  `resource_code` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源code',
  `resource_ip` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源IP',
  `TIME` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `content` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '内容',
  `category` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '分类',
  `level` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '级别',
  `ADD_DATE` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '新增时间'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for api_interface_system_data202005
-- ----------------------------
DROP TABLE IF EXISTS `api_interface_system_data202005`;
CREATE TABLE `api_interface_system_data202005`  (
  `id` varchar(39) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `system_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '系统ID',
  `platform_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '平台ID',
  `index_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '指标ID',
  `resource_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源ID',
  `resource_code` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源code',
  `resource_ip` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源IP',
  `TIME` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `content` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '内容',
  `category` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '分类',
  `level` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '级别',
  `ADD_DATE` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '新增时间'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for api_interface_system_data202006
-- ----------------------------
DROP TABLE IF EXISTS `api_interface_system_data202006`;
CREATE TABLE `api_interface_system_data202006`  (
  `id` varchar(39) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `system_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '系统ID',
  `platform_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '平台ID',
  `index_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '指标ID',
  `resource_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源ID',
  `resource_code` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源code',
  `resource_ip` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源IP',
  `TIME` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `content` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '内容',
  `category` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '分类',
  `level` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '级别',
  `ADD_DATE` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '新增时间'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for api_interface_system_data202007
-- ----------------------------
DROP TABLE IF EXISTS `api_interface_system_data202007`;
CREATE TABLE `api_interface_system_data202007`  (
  `id` varchar(39) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `system_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '系统ID',
  `platform_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '平台ID',
  `index_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '指标ID',
  `resource_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源ID',
  `resource_code` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源code',
  `resource_ip` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源IP',
  `TIME` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `content` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '内容',
  `category` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '分类',
  `level` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '级别',
  `ADD_DATE` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '新增时间'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for api_interface_system_data202008
-- ----------------------------
DROP TABLE IF EXISTS `api_interface_system_data202008`;
CREATE TABLE `api_interface_system_data202008`  (
  `id` varchar(39) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `system_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '系统ID',
  `platform_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '平台ID',
  `index_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '指标ID',
  `resource_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源ID',
  `resource_code` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源code',
  `resource_ip` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源IP',
  `TIME` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `content` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '内容',
  `category` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '分类',
  `level` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '级别',
  `ADD_DATE` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '新增时间'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for api_interface_system_data202009
-- ----------------------------
DROP TABLE IF EXISTS `api_interface_system_data202009`;
CREATE TABLE `api_interface_system_data202009`  (
  `id` varchar(39) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `system_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '系统ID',
  `platform_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '平台ID',
  `index_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '指标ID',
  `resource_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源ID',
  `resource_code` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源code',
  `resource_ip` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源IP',
  `TIME` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `content` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '内容',
  `category` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '分类',
  `level` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '级别',
  `ADD_DATE` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '新增时间'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for api_interface_system_data202010
-- ----------------------------
DROP TABLE IF EXISTS `api_interface_system_data202010`;
CREATE TABLE `api_interface_system_data202010`  (
  `ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '主键',
  `system_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '系统ID',
  `platform_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '平台ID',
  `index_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '指标ID',
  `resource_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源ID',
  `resource_code` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源code',
  `resource_ip` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源IP',
  `time` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '时间',
  `content` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '内容',
  `ADD_DATE` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '新增时间',
  PRIMARY KEY (`ID`) USING BTREE,
  INDEX `system_id`(`system_id`) USING BTREE,
  INDEX `platform_id`(`platform_id`) USING BTREE,
  INDEX `index_id`(`index_id`) USING BTREE,
  INDEX `time`(`time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '优云系统(202010)' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for api_interface_system_data202010_bak
-- ----------------------------
DROP TABLE IF EXISTS `api_interface_system_data202010_bak`;
CREATE TABLE `api_interface_system_data202010_bak`  (
  `ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '主键',
  `system_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '系统ID',
  `platform_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '平台ID',
  `index_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '指标ID',
  `resource_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源ID',
  `resource_code` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源code',
  `resource_ip` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源IP',
  `time` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '时间',
  `content` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '内容',
  `category` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '分类',
  `level` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '级别',
  `ADD_DATE` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '新增时间'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for api_interface_system_data202011
-- ----------------------------
DROP TABLE IF EXISTS `api_interface_system_data202011`;
CREATE TABLE `api_interface_system_data202011`  (
  `ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '主键',
  `system_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '系统ID',
  `platform_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '平台ID',
  `index_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '指标ID',
  `resource_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源ID',
  `resource_code` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源code',
  `resource_ip` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源IP',
  `time` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '时间',
  `content` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '内容',
  `ADD_DATE` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '新增时间',
  INDEX `system_id`(`system_id`) USING BTREE,
  INDEX `platform_id`(`platform_id`) USING BTREE,
  INDEX `index_id`(`index_id`) USING BTREE,
  INDEX `time`(`time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '优云系统数据(202011)' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for api_interface_system_data202012
-- ----------------------------
DROP TABLE IF EXISTS `api_interface_system_data202012`;
CREATE TABLE `api_interface_system_data202012`  (
  `ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '主键',
  `system_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '系统ID',
  `platform_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '平台ID',
  `index_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '指标ID',
  `resource_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源ID',
  `resource_code` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源code',
  `resource_ip` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源IP',
  `time` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '时间',
  `content` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '内容',
  `ADD_DATE` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '新增时间',
  INDEX `system_id`(`system_id`) USING BTREE,
  INDEX `platform_id`(`platform_id`) USING BTREE,
  INDEX `index_id`(`index_id`) USING BTREE,
  INDEX `time`(`time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '优云系统数据(202012)' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for api_interface_system_data202101
-- ----------------------------
DROP TABLE IF EXISTS `api_interface_system_data202101`;
CREATE TABLE `api_interface_system_data202101`  (
  `ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '主键',
  `system_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '系统ID',
  `platform_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '平台ID',
  `index_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '指标ID',
  `resource_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源ID',
  `resource_code` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源code',
  `resource_ip` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源IP',
  `time` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '时间',
  `content` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '内容',
  `ADD_DATE` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '新增时间',
  INDEX `system_id`(`system_id`) USING BTREE,
  INDEX `platform_id`(`platform_id`) USING BTREE,
  INDEX `index_id`(`index_id`) USING BTREE,
  INDEX `time`(`time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '优云系统数据(202101)' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for api_interface_system_data202102
-- ----------------------------
DROP TABLE IF EXISTS `api_interface_system_data202102`;
CREATE TABLE `api_interface_system_data202102`  (
  `ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '主键',
  `system_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '系统ID',
  `platform_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '平台ID',
  `index_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '指标ID',
  `resource_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源ID',
  `resource_code` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源code',
  `resource_ip` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源IP',
  `time` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '时间',
  `content` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '内容',
  `ADD_DATE` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '新增时间',
  INDEX `system_id`(`system_id`) USING BTREE,
  INDEX `platform_id`(`platform_id`) USING BTREE,
  INDEX `index_id`(`index_id`) USING BTREE,
  INDEX `time`(`time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '优云系统数据(202102)' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for api_interface_system_data202103
-- ----------------------------
DROP TABLE IF EXISTS `api_interface_system_data202103`;
CREATE TABLE `api_interface_system_data202103`  (
  `ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '主键',
  `system_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '系统ID',
  `platform_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '平台ID',
  `index_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '指标ID',
  `resource_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源ID',
  `resource_code` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源code',
  `resource_ip` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源IP',
  `time` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '时间',
  `content` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '内容',
  `ADD_DATE` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '新增时间',
  INDEX `system_id`(`system_id`) USING BTREE,
  INDEX `platform_id`(`platform_id`) USING BTREE,
  INDEX `index_id`(`index_id`) USING BTREE,
  INDEX `time`(`time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '优云系统数据(202103)' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for api_interface_system_data202104
-- ----------------------------
DROP TABLE IF EXISTS `api_interface_system_data202104`;
CREATE TABLE `api_interface_system_data202104`  (
  `ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '主键',
  `system_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '系统ID',
  `platform_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '平台ID',
  `index_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '指标ID',
  `resource_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源ID',
  `resource_code` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源code',
  `resource_ip` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源IP',
  `time` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '时间',
  `content` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '内容',
  `ADD_DATE` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '新增时间',
  INDEX `system_id`(`system_id`) USING BTREE,
  INDEX `platform_id`(`platform_id`) USING BTREE,
  INDEX `index_id`(`index_id`) USING BTREE,
  INDEX `time`(`time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '优云系统数据(202104)' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for api_interface_system_data202309
-- ----------------------------
DROP TABLE IF EXISTS `api_interface_system_data202309`;
CREATE TABLE `api_interface_system_data202309`  (
  `ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '主键',
  `system_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '系统ID',
  `platform_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '平台ID',
  `index_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '指标ID',
  `resource_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源ID',
  `resource_code` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源code',
  `resource_ip` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源IP',
  `time` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '时间',
  `content` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '内容',
  `ADD_DATE` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '新增时间',
  INDEX `system_id`(`system_id`) USING BTREE,
  INDEX `platform_id`(`platform_id`) USING BTREE,
  INDEX `index_id`(`index_id`) USING BTREE,
  INDEX `time`(`time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '优云系统数据(202309)' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for api_system_records
-- ----------------------------
DROP TABLE IF EXISTS `api_system_records`;
CREATE TABLE `api_system_records`  (
  `ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `system_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '系统ID',
  `system_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '系统代码',
  `platform_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '平台ID',
  `platfrom_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '平台代码',
  `index_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '指标(资源)ID',
  `api_contract` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'HTTP协议:HTTP/1.1',
  `api_headers` varchar(2000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'api接口请求头',
  `api_type` varchar(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'api接口类型',
  `api_method` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'api接口方法',
  `api_params` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT 'api接口参数',
  `api_status` varchar(3) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'api接口返回状态',
  `api_count` varchar(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'api接口调用次数',
  `content` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '内容',
  `add_time` date NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for menu_bak
-- ----------------------------
DROP TABLE IF EXISTS `menu_bak`;
CREATE TABLE `menu_bak`  (
  `MENU_ID` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `PARENT_MENU_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `MENU_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `URL` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `MENU_LVL` int(11) NULL DEFAULT NULL,
  `SORT` int(11) NULL DEFAULT NULL,
  `IS_VALID` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `MENU_ICON` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `IS_PROJECT` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for table_name
-- ----------------------------
DROP TABLE IF EXISTS `table_name`;
CREATE TABLE `table_name`  (
  `table_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `pri_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for temp11
-- ----------------------------
DROP TABLE IF EXISTS `temp11`;
CREATE TABLE `temp11`  (
  `ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '主键',
  `system_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '系统ID',
  `platform_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '平台ID',
  `index_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '指标ID',
  `resource_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源ID',
  `resource_code` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源code',
  `resource_ip` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源IP',
  `time` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '时间',
  `content` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '内容',
  `ADD_DATE` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `d_acct` date NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Procedure structure for ucloud_api_interface_alarm_data
-- ----------------------------
DROP PROCEDURE IF EXISTS `ucloud_api_interface_alarm_data`;
delimiter ;;
CREATE DEFINER=`root`@`%` PROCEDURE `ucloud_api_interface_alarm_data`(IN table_name_list VARCHAR(255),IN date_yesterday VARCHAR(10)
    ,OUT P_RESULT   INT)
BEGIN
		DECLARE V_PROC_NAME     VARCHAR(80)   DEFAULT 'ucloud.ucloud_api_interface_alarm_data';
    DECLARE V_START_TIME    CHAR(19)      DEFAULT NOW();
    DECLARE V_STEP_ID       INT           DEFAULT 0;
    DECLARE DATA_DATE       CHAR(8)       DEFAULT DATE_FORMAT(NOW(), '%Y%m%d');
    -- DECLARE table_name_list       CHAR(80)       DEFAULT concat('upm.',table_list);
    
		
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        GET DIAGNOSTICS CONDITION 1 @V_RETURN_CODE = RETURNED_SQLSTATE ,@V_ERROR_MSG = MESSAGE_TEXT;
        CALL ETL.EDW_PROC_ERROR_LOG(DATA_DATE,V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,@V_RETURN_CODE,@V_ERROR_MSG);
        SET P_RESULT = 1;
				
    END;
    SET P_RESULT = 0;
    
    SET V_STEP_ID = 1;
    SET P_RESULT = 0;
     DELETE FROM ucloud.api_alarm_summary WHERE table_name = table_name_list and time_date = date_yesterday;
    CALL ETL.EDW_PROC_TRACE_LOG(DATA_DATE,V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
    
		SET @sql_create_table = CONCAT("INSERT INTO  ucloud.api_alarm_summary(
		     time    -- time
         ,time_date    -- 插入数据日期
         ,index_id    -- 指标id
         ,platform_id    -- 平台id
				,resource_id   --  资源id
				,resource_name   --  资源名称
				,resource_ip   --  资源ip
				,Count  -- 统计
				,table_name  -- 源表名
				,period  -- 周期
					,add_time  -- 添加时间
                 )
(
SELECT a.time,a.time_date,a.index_id,a.platform_id,a.resource_id,a.resource_name,a.resource_ip,sum(a.alarm_count) as Count,a.table_name,a.period,NOW() from (
SELECT  DATE_FORMAT(a.lastOccurTime, '%Y-%m-%d %H:00:00') as time,'",date_yesterday,"' as time_date,c.id as index_id,c.platform_id,b.resource_id,a.resource_name,a.resource_ip,a.alarm_count,'",table_name_list,"' as table_name,'2' as period
    FROM " ,table_name_list,
		"  as a
		LEFT JOIN system_docking.api_system_resource_ip b
		on a.resource_ip = b.ip_address
		LEFT JOIN system_docking.api_platform_index_info c on a.index_code=c.index_code
    WHERE  1=1 and c.is_enable='1'
		and lastOccurTime like '",date_yesterday,"%'
		GROUP BY a.id,a.index_code
)a where a.time is not null
 GROUP BY time,index_id,resource_id
 )

union 
(
SELECT b.time,b.time_date,b.index_id,b.platform_id,b.resource_id,b.resource_name,b.resource_ip,sum(b.alarm_count) as Count,b.table_name,b.period,NOW() from (
SELECT  DATE_FORMAT(a.lastOccurTime, '%Y-%m-%d') as time,'",date_yesterday,"' as time_date,c.id as index_id,c.platform_id,b.resource_id,a.resource_name,a.resource_ip,a.alarm_count,'",table_name_list,"' as table_name,'3' as period
    FROM " ,table_name_list,
		"  as a
		LEFT JOIN system_docking.api_system_resource_ip b
		on a.resource_ip = b.ip_address
		LEFT JOIN system_docking.api_platform_index_info c on a.index_code=c.index_code
    WHERE  1=1 and c.is_enable='1'
		and lastOccurTime like '",date_yesterday,"%'
		GROUP BY a.id,a.index_code
)b where b.time is not null
 GROUP BY time,index_id,resource_id
 )

union
(
SELECT c.time,c.time_date,c.index_id,c.platform_id,c.resource_id,c.resource_name,c.resource_ip,sum(c.alarm_count) as Count,c.table_name,c.period,NOW() from (
SELECT  DATE_FORMAT(a.lastOccurTime, '%Y-%m') as time,'",date_yesterday,"' as time_date,c.id as index_id,c.platform_id,b.resource_id,a.resource_name,a.resource_ip,a.alarm_count,'",table_name_list,"' as table_name,'4' as period
    FROM " ,table_name_list,
		"  as a
		LEFT JOIN system_docking.api_system_resource_ip b
		on a.resource_ip = b.ip_address
		LEFT JOIN system_docking.api_platform_index_info c on a.index_code=c.index_code
    WHERE  1=1 and c.is_enable='1'
		and lastOccurTime like '",date_yesterday,"%'
		GROUP BY a.id,a.index_code
)c where c.time is not null
 GROUP BY time,index_id,resource_id
 )
union
(
SELECT d.time,d.time_date,d.index_id,d.platform_id,d.resource_id,d.resource_name,d.resource_ip,sum(d.alarm_count) as Count,d.table_name,d.period,NOW() from (
SELECT  DATE_FORMAT(a.lastOccurTime, '%Y') as time,'",date_yesterday,"' as time_date,c.id as index_id,c.platform_id,b.resource_id,a.resource_name,a.resource_ip,a.alarm_count,'",table_name_list,"' as table_name,'5' as period
    FROM " ,table_name_list,
		"  as a
		LEFT JOIN system_docking.api_system_resource_ip b
		on a.resource_ip = b.ip_address
		LEFT JOIN system_docking.api_platform_index_info c on a.index_code=c.index_code
    WHERE  1=1 and c.is_enable='1'
		and lastOccurTime like '",date_yesterday,"%'
		GROUP BY a.id,a.index_code
)d where d.time is not null
 GROUP BY time,index_id,resource_id
 )");
  
PREPARE SQLTXT FROM @sql_create_table;
EXECUTE SQLTXT;
DEALLOCATE PREPARE SQLTXT;
		
		
		
    
								 

 CALL ETL.EDW_PROC_TRACE_LOG(DATA_DATE,V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for ucloud_api_interface_alarm_data_copy1
-- ----------------------------
DROP PROCEDURE IF EXISTS `ucloud_api_interface_alarm_data_copy1`;
delimiter ;;
CREATE DEFINER=`root`@`%` PROCEDURE `ucloud_api_interface_alarm_data_copy1`(IN table_name_list VARCHAR(255),IN date_yesterday VARCHAR(10)
    ,OUT P_RESULT   INT)
BEGIN
		DECLARE V_PROC_NAME     VARCHAR(80)   DEFAULT 'ucloud.ucloud_api_interface_alarm_data';
    DECLARE V_START_TIME    CHAR(19)      DEFAULT NOW();
    DECLARE V_STEP_ID       INT           DEFAULT 0;
    DECLARE DATA_DATE       CHAR(8)       DEFAULT DATE_FORMAT(NOW(), '%Y%m%d');
    -- DECLARE table_name_list       CHAR(80)       DEFAULT concat('upm.',table_list);
    
		
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        GET DIAGNOSTICS CONDITION 1 @V_RETURN_CODE = RETURNED_SQLSTATE ,@V_ERROR_MSG = MESSAGE_TEXT;
        CALL ETL.EDW_PROC_ERROR_LOG(DATA_DATE,V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,@V_RETURN_CODE,@V_ERROR_MSG);
        SET P_RESULT = 1;
				
    END;
    SET P_RESULT = 0;
    
    SET V_STEP_ID = 1;
    SET P_RESULT = 0;
     DELETE FROM ucloud.api_alarm_summary WHERE table_name = table_name_list and time_date = date_yesterday;
    CALL ETL.EDW_PROC_TRACE_LOG(DATA_DATE,V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
    
		SET @sql_create_table = CONCAT("INSERT INTO  ucloud.api_alarm_summary(
		     time    -- time
         ,time_date    -- 插入数据日期
         ,index_id    -- 指标id
         ,platform_id    -- 平台id
				,resource_id   --  资源id
				,resource_name   --  资源名称
				,resource_ip   --  资源ip
				,Count  -- 统计
				,table_name  -- 源表名
				,period  -- 周期
					,add_time  -- 添加时间
                 )
SELECT * from (
SELECT  DATE_FORMAT(a.lastOccurTime, '%Y-%m-%d %H:00:00') as time,'",date_yesterday,"' as time_date,c.id as index_id,c.platform_id,b.resource_id,a.resource_name,a.resource_ip,sum(a.alarm_count) as Count,
'",table_name_list,"' as table_name,'2' as period,NOW()
    FROM " ,table_name_list,
		"  as a
		LEFT JOIN system_docking.api_system_resource_ip b
		on a.resource_ip = b.ip_address
		LEFT JOIN system_docking.api_platform_index_info c on a.index_code=c.index_code
    WHERE  1=1 and c.is_enable='1'
		and lastOccurTime like '%",date_yesterday,"%'
    GROUP BY DATE_FORMAT(a.lastOccurTime, '%Y-%m-%d %H:00:00'),a.index_code,b.resource_id
)a where a.time is not null
union 
SELECT * from (
SELECT  DATE_FORMAT(a.lastOccurTime, '%Y-%m-%d') as time,'",date_yesterday,"' as time_date,c.id as index_id,c.platform_id,b.resource_id,a.resource_name,a.resource_ip,sum(a.alarm_count) as Count,
'",table_name_list,"' as table_name,'3' as period,NOW()
    FROM " ,table_name_list,
		"  as a
		LEFT JOIN system_docking.api_system_resource_ip b
		on a.resource_ip = b.ip_address
		LEFT JOIN system_docking.api_platform_index_info c on a.index_code=c.index_code
    WHERE  1=1 and c.is_enable='1'
		and lastOccurTime like '%",date_yesterday,"%'
    GROUP BY DATE_FORMAT(a.lastOccurTime, '%Y-%m-%d'),a.index_code,b.resource_id
)b where b.time is not null
union
SELECT * from (
SELECT  DATE_FORMAT(a.lastOccurTime, '%Y-%m') as time,'",date_yesterday,"' as time_date,c.id as index_id,c.platform_id,b.resource_id,a.resource_name,a.resource_ip,sum(a.alarm_count) as Count,
'",table_name_list,"' as table_name,'4' as period,NOW()
    FROM " ,table_name_list,
		"  as a
		LEFT JOIN system_docking.api_system_resource_ip b
		on a.resource_ip = b.ip_address
		LEFT JOIN system_docking.api_platform_index_info c on a.index_code=c.index_code
    WHERE  1=1 and c.is_enable='1'
		and lastOccurTime like '%",date_yesterday,"%'
    GROUP BY DATE_FORMAT(a.lastOccurTime, '%Y-%m'),a.index_code,b.resource_id
)c where c.time is not null
union
SELECT * from (
SELECT  DATE_FORMAT(a.lastOccurTime, '%Y') as time,'",date_yesterday,"' as time_date,c.id as index_id,c.platform_id,b.resource_id,a.resource_name,a.resource_ip,sum(a.alarm_count) as Count,
'",table_name_list,"' as table_name,'5' as period,NOW()
    FROM " ,table_name_list,
		"  as a
		LEFT JOIN system_docking.api_system_resource_ip b
		on a.resource_ip = b.ip_address
		LEFT JOIN system_docking.api_platform_index_info c on a.index_code=c.index_code
    WHERE  1=1 and c.is_enable='1'
		and lastOccurTime like '%",date_yesterday,"%'
    GROUP BY DATE_FORMAT(a.lastOccurTime, '%Y'),a.index_code,b.resource_id
)d where d.time is not null");
  
PREPARE SQLTXT FROM @sql_create_table;
EXECUTE SQLTXT;
DEALLOCATE PREPARE SQLTXT;
		
		
		
    
								 
 CALL ETL.EDW_PROC_TRACE_LOG(DATA_DATE,V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for ucloud_api_interface_system_data
-- ----------------------------
DROP PROCEDURE IF EXISTS `ucloud_api_interface_system_data`;
delimiter ;;
CREATE DEFINER=`root`@`%` PROCEDURE `ucloud_api_interface_system_data`(IN table_name_list VARCHAR(255),IN date_yesterday VARCHAR(10)
    ,OUT P_RESULT   INT)
BEGIN
		DECLARE V_PROC_NAME     VARCHAR(80)   DEFAULT 'ucloud.ucloud_api_interface_system_data';
    DECLARE V_START_TIME    CHAR(19)      DEFAULT NOW();
    DECLARE V_STEP_ID       INT           DEFAULT 0;
    DECLARE DATA_DATE       CHAR(8)       DEFAULT DATE_FORMAT(NOW(), '%Y%m%d');
    -- DECLARE table_name_list       CHAR(80)       DEFAULT concat('upm.',table_list);
    
		
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        GET DIAGNOSTICS CONDITION 1 @V_RETURN_CODE = RETURNED_SQLSTATE ,@V_ERROR_MSG = MESSAGE_TEXT;
        CALL ETL.EDW_PROC_ERROR_LOG(DATA_DATE,V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,@V_RETURN_CODE,@V_ERROR_MSG);
        SET P_RESULT = 1;
				
    END;
    SET P_RESULT = 0;
    
    SET V_STEP_ID = 1;
    SET P_RESULT = 0;
     DELETE FROM ucloud.api_alarm_summary WHERE table_name = table_name_list and time_date = date_yesterday;
    CALL ETL.EDW_PROC_TRACE_LOG(DATA_DATE,V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
    
		SET @sql_create_table = CONCAT("INSERT INTO  ucloud.api_alarm_summary(
		     time    -- time
         ,time_date    -- 插入数据日期
         ,platform_id    -- 平台id
         ,index_id    -- 指标id
				,resource_id   --  资源id
				,resource_name   --  资源名称
				,resource_ip   --  资源ip
				,Count  -- 统计
				,table_name  -- 源表名
				,period  -- 周期
					,add_time  -- 添加时间
                 )
SELECT * from (
SELECT  DATE_FORMAT(a.time, '%Y-%m-%d %H:00:00') as time,'",date_yesterday,"' as time_date,a.platform_id,a.index_id,a.resource_id,b.resource_desc,a.resource_ip,
max(cast(a.content as DECIMAL(16,6))) as Count,
'",table_name_list,"' as table_name,'2' as period,NOW()
    FROM " ,table_name_list,
		"  as a
		LEFT JOIN system_docking.api_system_resource b on a.resource_id = b.id
    WHERE  1=1 and b.is_enable='1'
		and time like '%",date_yesterday,"%'
    GROUP BY DATE_FORMAT(a.time, '%Y-%m-%d %H:00:00'),a.index_id,a.resource_id,a.resource_ip ORDER BY Hour(time)
)a where a.time is not null
	
union 
SELECT * from (
SELECT  DATE_FORMAT(a.time, '%Y-%m-%d') as time,'",date_yesterday,"' as time_date,a.platform_id,a.index_id,a.resource_id,b.resource_desc,a.resource_ip,
max(cast(a.content as DECIMAL(16,6))) as Count,
'",table_name_list,"' as table_name,'3' as period,NOW()
    FROM " ,table_name_list,
		"  as a
		LEFT JOIN system_docking.api_system_resource b on a.resource_id = b.id
    WHERE  1=1 and b.is_enable='1'
		and time like '%",date_yesterday,"%'
    GROUP BY DATE_FORMAT(a.time, '%Y-%m-%d'),a.index_id,a.resource_id,a.resource_ip ORDER BY Hour(time)
)a where a.time is not null
union
SELECT * from (
SELECT  DATE_FORMAT(a.time, '%Y-%m') as time,'",date_yesterday,"' as time_date,a.platform_id,a.index_id,a.resource_id,b.resource_desc,a.resource_ip,
max(cast(a.content as DECIMAL(16,6))) as Count,
'",table_name_list,"' as table_name,'4' as period,NOW()
    FROM " ,table_name_list,
		"  as a
		LEFT JOIN system_docking.api_system_resource b on a.resource_id = b.id
    WHERE  1=1 and b.is_enable='1'
		and time like '%",date_yesterday,"%'
    GROUP BY DATE_FORMAT(a.time, '%Y-%m'),a.index_id,a.resource_id,a.resource_ip ORDER BY Hour(time)
)a where a.time is not null
union
SELECT * from (
SELECT  DATE_FORMAT(a.time, '%Y') as time,'",date_yesterday,"' as time_date,a.platform_id,a.index_id,a.resource_id,b.resource_desc,a.resource_ip,
max(cast(a.content as DECIMAL(16,6))) as Count,
'",table_name_list,"' as table_name,'5' as period,NOW()
    FROM " ,table_name_list,
		"  as a
		LEFT JOIN system_docking.api_system_resource b on a.resource_id = b.id
    WHERE  1=1 and b.is_enable='1'
		and time like '%",date_yesterday,"%'
    GROUP BY DATE_FORMAT(a.time, '%Y'),a.index_id,a.resource_id,a.resource_ip ORDER BY Hour(time)
)a where a.time is not null");
  
PREPARE SQLTXT FROM @sql_create_table;
EXECUTE SQLTXT;
DEALLOCATE PREPARE SQLTXT;
		
		
		
    
								 
 CALL ETL.EDW_PROC_TRACE_LOG(DATA_DATE,V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for Untitled
-- ----------------------------
DROP PROCEDURE IF EXISTS `Untitled`;
delimiter ;;
CREATE DEFINER=`root`@`%` PROCEDURE `Untitled`()
BEGIN
			DECLARE minDate VARCHAR(10);
			DECLARE maxDate VARCHAR(10);
			
			set minDate = (select min(left(lastOccurTime,10)) from ucloud.api_interface_alarm_data);
			set maxDate = (select max(left(lastOccurTime,10)) from ucloud.api_interface_alarm_data);
			
			while minDate <= maxDate do
				call ucloud.ucloud_api_interface_alarm_data('ucloud.api_interface_alarm_data', minDate, @a);
				set minDate = DATE_FORMAT(date_add(STR_TO_DATE(minDate,'%Y-%m-%d'), interval 1 day),'%Y-%m-%d');
			end WHILE;

END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
