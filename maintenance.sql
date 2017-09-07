/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 50718
 Source Host           : 127.0.0.1
 Source Database       : maintenance

 Target Server Type    : MySQL
 Target Server Version : 50718
 File Encoding         : utf-8

 Date: 09/06/2017 00:58:28 AM
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `T_CMDB_ATTR`
-- ----------------------------
DROP TABLE IF EXISTS `T_CMDB_ATTR`;
CREATE TABLE `T_CMDB_ATTR` (
  `ATTR_ID` varchar(40) NOT NULL COMMENT '属性编号',
  `ATTR_NAME` varchar(80) DEFAULT NULL COMMENT '属性名称',
  `ATTR_DEF` varchar(200) DEFAULT NULL COMMENT '属性定义',
  PRIMARY KEY (`ATTR_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='属性管理';

-- ----------------------------
--  Table structure for `T_CMDB_CATG`
-- ----------------------------
DROP TABLE IF EXISTS `T_CMDB_CATG`;
CREATE TABLE `T_CMDB_CATG` (
  `CATG_ID` varchar(120) NOT NULL COMMENT '分类编号',
  `CATG_NAME` varchar(80) DEFAULT NULL COMMENT '分类名称',
  `PATH` varchar(40) DEFAULT NULL COMMENT '路径编码',
  `IMAGE` varchar(40) DEFAULT NULL COMMENT '显示图标',
  PRIMARY KEY (`CATG_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='分类管理';

-- ----------------------------
--  Table structure for `T_CMDB_CATG2KPI`
-- ----------------------------
DROP TABLE IF EXISTS `T_CMDB_CATG2KPI`;
CREATE TABLE `T_CMDB_CATG2KPI` (
  `CATG_ID` varchar(40) NOT NULL COMMENT '分类编号',
  `KPI_ID` varchar(40) NOT NULL COMMENT '指标编号',
  `ATTR_FLAG` varchar(5) DEFAULT NULL COMMENT '是否关键属性：Y:是，N否',
  PRIMARY KEY (`CATG_ID`,`KPI_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='分类指标关联';

-- ----------------------------
--  Table structure for `T_CMDB_CI`
-- ----------------------------
DROP TABLE IF EXISTS `T_CMDB_CI`;
CREATE TABLE `T_CMDB_CI` (
  `CI_ID` varchar(20) NOT NULL COMMENT '配置项编号',
  `CI_NAME` varchar(80) DEFAULT NULL COMMENT '配置项名称',
  `CATG_ID` varchar(20) DEFAULT NULL COMMENT '分类编号',
  `ITEM_NO` varchar(20) DEFAULT NULL COMMENT '资产编号',
  `START_DATE` varchar(20) DEFAULT NULL COMMENT '启用时间',
  `DISABLED_DATE` varchar(20) DEFAULT NULL COMMENT '停用时间',
  `CI_DESC` varchar(200) DEFAULT NULL COMMENT '配置项描述',
  PRIMARY KEY (`CI_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='配置项管理';

-- ----------------------------
--  Table structure for `T_CMDB_CI_ATTR_VAL`
-- ----------------------------
DROP TABLE IF EXISTS `T_CMDB_CI_ATTR_VAL`;
CREATE TABLE `T_CMDB_CI_ATTR_VAL` (
  `CI_ID` varchar(20) NOT NULL COMMENT '配置项编号',
  `ATTR_ID` varchar(40) NOT NULL COMMENT '属性编号',
  `VAL` varchar(200) DEFAULT NULL COMMENT '属性值',
  `REC_TIME` varchar(20) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`CI_ID`,`ATTR_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='配置项属性值管理';

-- ----------------------------
--  Table structure for `T_CMDB_KPI`
-- ----------------------------
DROP TABLE IF EXISTS `T_CMDB_KPI`;
CREATE TABLE `T_CMDB_KPI` (
  `KPI_ID` varchar(100) NOT NULL COMMENT '指标编号',
  `KPI_NAME` varchar(100) DEFAULT NULL COMMENT '指标名称',
  `ORDER_NO` varchar(10) DEFAULT NULL COMMENT '排序',
  `KPI_TYPE` varchar(10) DEFAULT NULL COMMENT '指标类别：1:平台指标2:应用指标3：安全指标',
  PRIMARY KEY (`KPI_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='指标管理';

-- ----------------------------
--  Table structure for `T_CMDB_KPI_RULE`
-- ----------------------------
DROP TABLE IF EXISTS `T_CMDB_KPI_RULE`;
CREATE TABLE `T_CMDB_KPI_RULE` (
  `RULE_ID` varchar(120) NOT NULL COMMENT '规则编号',
  `RULE_NAME` varchar(200) DEFAULT NULL COMMENT '规则名称',
  `CATG_ID` varchar(120) DEFAULT NULL COMMENT '分类编号',
  `KPI_ID` varchar(100) DEFAULT NULL COMMENT '指标编号',
  `RULE_DESC` varchar(800) DEFAULT NULL COMMENT '规则描述',
  `ALARM_LVL` decimal(10,0) DEFAULT NULL COMMENT '预警级别：0正常，1一级预警，2，二级预警，3三级预警，4四级预警',
  `RULE_VAL` varchar(100) DEFAULT NULL COMMENT '阀值',
  `AVAILABLE_FLAG` varchar(2) DEFAULT NULL COMMENT '规则有效标志，0-无效，1-有效',
  PRIMARY KEY (`RULE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='指标预警规则管理';

-- ----------------------------
--  Table structure for `T_CMDB_RELA_CATG2ATTR`
-- ----------------------------
DROP TABLE IF EXISTS `T_CMDB_RELA_CATG2ATTR`;
CREATE TABLE `T_CMDB_RELA_CATG2ATTR` (
  `CATG_ID` varchar(40) NOT NULL COMMENT '分类编号',
  `ATTR_ID` varchar(40) NOT NULL COMMENT '属性编号',
  PRIMARY KEY (`CATG_ID`,`ATTR_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='分类属性关联';

-- ----------------------------
--  Table structure for `T_CMDB_REPOSITOTY`
-- ----------------------------
DROP TABLE IF EXISTS `T_CMDB_REPOSITOTY`;
CREATE TABLE `T_CMDB_REPOSITOTY` (
  `REPO_ID` varchar(10) NOT NULL COMMENT '知识编号',
  `REPO_NAME` varchar(100) DEFAULT NULL COMMENT '知识名称',
  `REPO_TYPE` varchar(2) DEFAULT NULL COMMENT '知识类型;1通用，2特色',
  `R_ID` varchar(40) DEFAULT NULL COMMENT '知识所属类型:repo_type为2（特色知识）时为分类编号，1（通用知识）为配置项编号',
  `REPO_LEVEL` varchar(10) DEFAULT NULL COMMENT '事件级别（级别越高影响越大）',
  `REPO_DESC` text COMMENT '事件描述',
  `BUSI_INVOLED` text COMMENT '业务影响',
  `TECH_INVOLED` text COMMENT '技术影响',
  `START_CONDITIO` text COMMENT '启动条件',
  `SCENE_PROTECTION` text COMMENT '现场保护',
  `RISK_CONSTRAINTS` text COMMENT '风险约束',
  `OPER_STEPS` text COMMENT '操作步骤',
  `CHECK_STEPS` text COMMENT '验证步骤',
  `REVISE_DATE` varchar(30) DEFAULT NULL COMMENT '修订日期',
  `REVISER` varchar(12) DEFAULT NULL COMMENT '修订人',
  `REVIEW` varchar(2) DEFAULT NULL COMMENT '审核状态1、审核中 2、审核通过3、拒绝审核;0-未提交',
  `REPO_STAT` varchar(2) DEFAULT NULL COMMENT '知识状态:1有效，2停用',
  PRIMARY KEY (`REPO_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='知识库管理';

-- ----------------------------
--  Table structure for `T_EVENT_CLOSE`
-- ----------------------------
DROP TABLE IF EXISTS `T_EVENT_CLOSE`;
CREATE TABLE `T_EVENT_CLOSE` (
  `EVENT_ID` varchar(20) NOT NULL COMMENT '故障编号',
  `EVENT_CAUSE` varchar(4000) DEFAULT NULL COMMENT '故障发生原因',
  `RESOLVE_DESC` varchar(4000) DEFAULT NULL COMMENT '故障解决方法描述',
  `RESOLVE_TIME` varchar(30) DEFAULT NULL COMMENT '故障解决时间',
  PRIMARY KEY (`EVENT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='故障办结关闭';

-- ----------------------------
--  Table structure for `T_EVENT_INFO`
-- ----------------------------
DROP TABLE IF EXISTS `T_EVENT_INFO`;
CREATE TABLE `T_EVENT_INFO` (
  `EVENT_ID` varchar(40) NOT NULL COMMENT '故障编号',
  `EVENT_NAME` varchar(100) DEFAULT NULL COMMENT '故障名称',
  `EVENT_LEVEL` varchar(2) DEFAULT NULL COMMENT '故障等级：1一级预警，2，二级预警，3三级预警，4四级预警',
  `EVENT_DESC` varchar(400) DEFAULT NULL COMMENT '故障描述',
  `EVENT_TYPE` varchar(2) DEFAULT NULL COMMENT '故障类别：1:平台故障2:应用故障3：安全事件',
  `SOURCE` varchar(20) DEFAULT NULL COMMENT '故障来源S02:大数据平台，S03云平台，S04安全平台',
  `HAPPEN_TIME` varchar(30) DEFAULT NULL COMMENT '故障发生时间',
  `CREATE_TIME` varchar(30) DEFAULT NULL COMMENT '故障建立时间',
  `CI_IDS` varchar(4000) DEFAULT NULL COMMENT '关联配置项列表',
  `EVENT_FLAG` varchar(2) DEFAULT NULL COMMENT '故障标志:0，未销障，1已解决',
  PRIMARY KEY (`EVENT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='故障管理';

-- ----------------------------
--  Table structure for `T_EVENT_USER`
-- ----------------------------
DROP TABLE IF EXISTS `T_EVENT_USER`;
CREATE TABLE `T_EVENT_USER` (
  `EVENT_ID` varchar(20) NOT NULL COMMENT '故障编号',
  `USER_ID` varchar(20) NOT NULL COMMENT '用户编号',
  `USER_TYPE` varchar(2) DEFAULT NULL COMMENT '用户类型 1业务关系人 2运维人员 ',
  PRIMARY KEY (`EVENT_ID`,`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='故障干系人管理';

-- ----------------------------
--  Table structure for `t_application`
-- ----------------------------
DROP TABLE IF EXISTS `t_application`;
CREATE TABLE `t_application` (
  `app_id` int(4) NOT NULL,
  `app_name` varchar(30) DEFAULT NULL,
  `node_id` int(4) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `entity_id` int(4) DEFAULT NULL,
  PRIMARY KEY (`app_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_application`
-- ----------------------------
BEGIN;
INSERT INTO `t_application` VALUES ('1', 'keystone', '1', '2017-09-05 09:59:53', '2017-09-05 09:59:57', '3'), ('2', 'nova-api', '1', '2017-09-05 10:00:52', '2017-09-05 10:00:55', '6'), ('3', 'nova-consoleauth', '1', '2017-09-05 10:01:46', '2017-09-05 10:01:48', '7'), ('4', '11', '2', '2017-09-05 10:02:30', '2017-09-05 10:02:33', '11');
COMMIT;

-- ----------------------------
--  Table structure for `t_application_type`
-- ----------------------------
DROP TABLE IF EXISTS `t_application_type`;
CREATE TABLE `t_application_type` (
  `app_type_id` int(4) NOT NULL,
  `app_name` varchar(30) DEFAULT NULL,
  `platfrom_id` int(4) DEFAULT NULL,
  PRIMARY KEY (`app_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `t_dictionary`
-- ----------------------------
DROP TABLE IF EXISTS `t_dictionary`;
CREATE TABLE `t_dictionary` (
  `id` int(4) NOT NULL AUTO_INCREMENT,
  `entity_id` int(4) NOT NULL,
  `name` varchar(30) DEFAULT NULL,
  `dic_text` varchar(30) DEFAULT NULL,
  `dic_value` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_dictionary`
-- ----------------------------
BEGIN;
INSERT INTO `t_dictionary` VALUES ('1', '19', '云平台服务状态', 'OK', '1'), ('2', '19', '云平台服务状态', 'ERR', '0');
COMMIT;

-- ----------------------------
--  Table structure for `t_entity`
-- ----------------------------
DROP TABLE IF EXISTS `t_entity`;
CREATE TABLE `t_entity` (
  `entity_id` int(4) NOT NULL AUTO_INCREMENT,
  `parent_id` int(4) NOT NULL,
  `entity_name` varchar(50) DEFAULT NULL,
  `hierarchy` int(4) NOT NULL,
  `description` varchar(50) DEFAULT NULL,
  `dic_text` varchar(20) DEFAULT NULL,
  `dic_value` varchar(20) DEFAULT NULL,
  `type` char(1) DEFAULT NULL COMMENT '0:普通类型，1:字典类型，2:状态类型父节点，3:状态类型',
  PRIMARY KEY (`entity_id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_entity`
-- ----------------------------
BEGIN;
INSERT INTO `t_entity` VALUES ('1', '-1', '物理机节点', '0', null, null, null, '0'), ('2', '-1', '云平台服务', '0', null, null, null, '0'), ('3', '2', 'keystone', '1', null, null, null, '0'), ('4', '2', 'glance-api', '1', null, null, null, '0'), ('5', '2', 'glance-registry', '1', null, null, null, '0'), ('6', '2', 'nova-api', '1', null, null, null, '0'), ('7', '2', 'nova-consoleauth', '1', null, null, null, '0'), ('8', '2', 'nova-scheduler', '1', null, null, null, '0'), ('9', '2', 'nova-conductor', '1', null, null, null, '0'), ('10', '2', 'nova-novncproxy', '1', null, null, null, '0'), ('11', '2', 'nova-compute', '1', null, null, null, '0'), ('12', '2', 'neutron-server', '1', null, null, null, '0'), ('13', '2', 'neutron-linuxbridge-agent', '1', null, null, null, '0'), ('14', '2', 'neutron-dhcp-agent', '1', null, null, null, '0'), ('15', '2', 'neutron-metadata-agent', '1', null, null, null, '0'), ('16', '2', 'openstack-cinder-api', '1', null, null, null, '0'), ('17', '2', 'cinder-scheduler', '1', null, null, null, '0'), ('19', '-1', '云平台服务状态', '0', null, null, null, '1'), ('22', '-1', '各节点物理服务器CPU使用情况', '0', null, null, null, '2'), ('23', '22', '使用百分比', '1', null, null, null, '3');
COMMIT;

-- ----------------------------
--  Table structure for `t_node`
-- ----------------------------
DROP TABLE IF EXISTS `t_node`;
CREATE TABLE `t_node` (
  `node_id` int(4) NOT NULL AUTO_INCREMENT,
  `union_id` varchar(20) DEFAULT NULL,
  `ip` varchar(20) DEFAULT NULL,
  `node_name` varchar(30) DEFAULT NULL,
  `location_name` varchar(30) DEFAULT NULL,
  `node_type_id` int(4) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`node_id`),
  UNIQUE KEY `union_id` (`union_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_node`
-- ----------------------------
BEGIN;
INSERT INTO `t_node` VALUES ('1', '1111', '10.0.01', '控制节点', '云平台机房', null, '2017-08-31 15:32:28', '2017-08-31 15:35:20'), ('2', null, '192.168.1.1', '计算节点', '云平台机房', null, '2017-08-31 17:15:50', '2017-08-31 17:15:50'), ('3', null, '192.168.1.2', '镜像节点', '云平台机房', null, '2017-09-01 16:04:26', '2017-09-01 16:04:26'), ('4', null, '192.168.1.3', '储蓄节点', '云平台机房', null, '2017-09-05 09:22:06', '2017-09-05 09:22:06');
COMMIT;

-- ----------------------------
--  Table structure for `t_status`
-- ----------------------------
DROP TABLE IF EXISTS `t_status`;
CREATE TABLE `t_status` (
  `status_id` int(8) NOT NULL AUTO_INCREMENT,
  `group_id` int(8) DEFAULT NULL,
  `state` varchar(4) DEFAULT NULL,
  `entity_id` int(4) DEFAULT NULL,
  PRIMARY KEY (`status_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `t_status`
-- ----------------------------
BEGIN;
INSERT INTO `t_status` VALUES ('1', null, '0', '19'), ('2', null, '0', '19'), ('3', null, '0', '19'), ('4', null, '1', '19'), ('5', null, '0', '19'), ('6', null, '1', '19'), ('7', null, '0', '19');
COMMIT;

-- ----------------------------
--  Table structure for `t_status_group`
-- ----------------------------
DROP TABLE IF EXISTS `t_status_group`;
CREATE TABLE `t_status_group` (
  `group_id` int(8) NOT NULL AUTO_INCREMENT,
  `up_id` int(4) DEFAULT NULL,
  `entity_id` int(4) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
