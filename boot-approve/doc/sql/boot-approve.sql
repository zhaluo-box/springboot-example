/*
 Navicat Premium Data Transfer

 Source Server         : dev-env
 Source Server Type    : MySQL
 Source Server Version : 50737
 Source Host           : 192.168.13.10:3306
 Source Schema         : boot-approve

 Target Server Type    : MySQL
 Target Server Version : 50737
 File Encoding         : 65001

 Date: 17/03/2022 14:33:10
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for approve_assignee_config
-- ----------------------------
DROP TABLE IF EXISTS `approve_assignee_config`;
CREATE TABLE `approve_assignee_config`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `assignee` bigint(20) NULL DEFAULT NULL COMMENT '审批人id',
  `approve_node_id` bigint(20) NULL DEFAULT NULL COMMENT '审批节点配置的ID',
  `approve_node_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审批配置节点名称',
  `order_num` tinyint(4) NULL DEFAULT NULL COMMENT '序号',
  `permission` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限标识符， 多个标识符用“逗号”分割',
  `support_transfer` tinyint(1) NULL DEFAULT NULL COMMENT '是否支持转办',
  `transfer_candidates` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '转办候选人员id， 多个人员之间采用逗号分割',
  `initiator` tinyint(1) NULL DEFAULT NULL COMMENT '是否是发起人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 39 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '审批人员配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of approve_assignee_config
-- ----------------------------
INSERT INTO `approve_assignee_config` VALUES (1, 1, 1, '技术部审批', 1, 'name,desc,xxx', 0, '1,2', 0);
INSERT INTO `approve_assignee_config` VALUES (2, 2, 4, '采购部审批', 1, 'name,desc,xxx', 0, '1,2', 0);
INSERT INTO `approve_assignee_config` VALUES (3, 3, 5, '生产部审批', 1, 'name,desc,xxx', 0, '1,2', 0);
INSERT INTO `approve_assignee_config` VALUES (4, 4, 5, '生产部审批', 2, 'name,desc,xxx', 0, '1,2', 0);
INSERT INTO `approve_assignee_config` VALUES (35, 1, 42, '技术部审批', 1, 'name,desc,xxx', 0, '1,2', 0);
INSERT INTO `approve_assignee_config` VALUES (36, 2, 43, '采购部审批', 1, 'name,desc,xxx', 0, '1,2', 0);
INSERT INTO `approve_assignee_config` VALUES (37, 3, 44, '生产部审批', 1, 'name,desc,xxx', 0, '1,2', 0);
INSERT INTO `approve_assignee_config` VALUES (38, 4, 44, '生产部审批', 2, 'name,desc,xxx', 0, '1,2', 0);

-- ----------------------------
-- Table structure for approve_instance
-- ----------------------------
DROP TABLE IF EXISTS `approve_instance`;
CREATE TABLE `approve_instance`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `result` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审批结果类型  待审批 审批中 审批结束',
  `approve_model_id` bigint(20) NULL DEFAULT NULL COMMENT '审批模板Id',
  `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发起审批的原因',
  `initiator` bigint(20) NULL DEFAULT NULL COMMENT '发起者Id',
  `create_time` date NULL DEFAULT NULL COMMENT '发起时间',
  `param_id` bigint(20) NULL DEFAULT NULL COMMENT '发起业务的ID',
  `next_node_id` bigint(20) NULL DEFAULT NULL COMMENT '下一待审批节点记录的ID',
  `next_node_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '下一审待审批节点名称',
  `next_assignees` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '下一审批节点审批人员，多个人员之间采用逗号分割',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称继承审批模板的名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '审批实例记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of approve_instance
-- ----------------------------
INSERT INTO `approve_instance` VALUES (9, 'PENDING_APPROVED', 55, '用来测试', 100, '2022-03-17', 1003, 57, '技术部审批', '1', NULL);

-- ----------------------------
-- Table structure for approve_model
-- ----------------------------
DROP TABLE IF EXISTS `approve_model`;
CREATE TABLE `approve_model`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板名称: 系统自动发现',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `disabled` tinyint(1) NULL DEFAULT NULL COMMENT ' 是否禁用',
  `version` tinyint(3) NULL DEFAULT NULL COMMENT '版本号 由程序维护递增',
  `uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '前端相对路径',
  `detail_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '前端组件ID',
  `service_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务名称 系统自动发现 取值 spring.application.name',
  `status` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模板状态  草稿 历史 官方正式启用版',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 56 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '审批模板表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of approve_model
-- ----------------------------
INSERT INTO `approve_model` VALUES (1, '测试审批', '描述', 1, 0, 'test-approve/', 'test', 'boot-approve', 'DRAFT');
INSERT INTO `approve_model` VALUES (55, '测试审批', '描述', 0, 1, 'test-approve/', 'test', 'boot-approve', 'OFFICIAL_EDITION');

-- ----------------------------
-- Table structure for approve_node_config
-- ----------------------------
DROP TABLE IF EXISTS `approve_node_config`;
CREATE TABLE `approve_node_config`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `approve_model_id` bigint(20) NULL DEFAULT NULL COMMENT '审批模板Id',
  `type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审批类型 直签 会签 异或',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审批节点名称',
  `level` tinyint(3) NULL DEFAULT NULL COMMENT '审批等级',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 45 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '审批节点配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of approve_node_config
-- ----------------------------
INSERT INTO `approve_node_config` VALUES (1, 1, 'DIRECT', '技术部审批，检测技术规范', '技术部审批', 1);
INSERT INTO `approve_node_config` VALUES (4, 1, 'DIRECT', '采购部检查物料', '采购部审批', 2);
INSERT INTO `approve_node_config` VALUES (5, 1, 'COUNTERSIGN', '生产部排期', '生产部审批', 3);
INSERT INTO `approve_node_config` VALUES (42, 55, 'DIRECT', '技术部审批，检测技术规范', '技术部审批', 1);
INSERT INTO `approve_node_config` VALUES (43, 55, 'DIRECT', '采购部检查物料', '采购部审批', 2);
INSERT INTO `approve_node_config` VALUES (44, 55, 'COUNTERSIGN', '生产部排期', '生产部审批', 3);

-- ----------------------------
-- Table structure for approve_node_record
-- ----------------------------
DROP TABLE IF EXISTS `approve_node_record`;
CREATE TABLE `approve_node_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `result` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '节点审批结果 通过 不通过 转办 驳回',
  `instance_id` bigint(20) NULL DEFAULT NULL COMMENT '审批实例Id',
  `node_config_id` bigint(20) NULL DEFAULT NULL COMMENT '审批配置节点Id',
  `type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审批类型 会签 直签 异或',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审批节点描述',
  `level` int(255) NULL DEFAULT NULL COMMENT '审批节点等级 ',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审批节点名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 60 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '审批节点记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of approve_node_record
-- ----------------------------
INSERT INTO `approve_node_record` VALUES (57, 'PENDING_APPROVED', 9, 42, 'DIRECT', '技术部审批，检测技术规范', 1, '技术部审批');
INSERT INTO `approve_node_record` VALUES (58, 'PENDING_APPROVED', 9, 43, 'DIRECT', '采购部检查物料', 2, '采购部审批');
INSERT INTO `approve_node_record` VALUES (59, 'PENDING_APPROVED', 9, 44, 'COUNTERSIGN', '生产部排期', 3, '生产部审批');

-- ----------------------------
-- Table structure for approve_running_record
-- ----------------------------
DROP TABLE IF EXISTS `approve_running_record`;
CREATE TABLE `approve_running_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `node_record_id` bigint(20) NULL DEFAULT NULL COMMENT '审批节点记录ID',
  `node_record_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审批节点记录名称',
  `instance_Id` bigint(20) NULL DEFAULT NULL COMMENT '审批实例Id',
  `assignee` bigint(20) NULL DEFAULT NULL COMMENT '审批人员Id',
  `remarks` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '审批意见',
  `result` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审批结果 同意 驳回 拒绝 转办',
  `permission` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限标识符 具体数据取决于业务',
  `order_num` int(11) NULL DEFAULT NULL COMMENT '序号 针对的是同一个节点',
  `transfer` bigint(20) NULL DEFAULT NULL COMMENT '转办人员的ID ，转办人员来自配置的候选人员',
  `transfer_mark` tinyint(1) NULL DEFAULT NULL COMMENT '转办标识，默认为false, 点击了转办变为true\r\n',
  `support_transfer` tinyint(1) NULL DEFAULT NULL COMMENT '是否支持转办',
  `transfer_candidates` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '转办候选人',
  `transfer_record_id` bigint(20) NULL DEFAULT NULL COMMENT '转办记录ID 自关联',
  `reject_node_level` int(2) NULL DEFAULT NULL COMMENT '驳回节点的等级',
  `reject_node_id` bigint(20) NULL DEFAULT NULL COMMENT '驳回节点的ID',
  `create_time` date NULL DEFAULT NULL COMMENT '创建时间',
  `last_modify_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 51 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '审批运行记录表， 具体记录了每一条审批信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of approve_running_record
-- ----------------------------
INSERT INTO `approve_running_record` VALUES (47, 57, NULL, 0, 1, '', 'PENDING_APPROVED', 'name,desc,xxx', 1, 0, 0, 0, '1,2', 0, 0, 0, '2022-03-16', '2022-03-17 06:29:47');
INSERT INTO `approve_running_record` VALUES (48, 58, NULL, 0, 2, '', 'NONE', 'name,desc,xxx', 1, 0, 0, 0, '1,2', 0, 0, 0, '2022-03-17', '2022-03-17 06:29:47');
INSERT INTO `approve_running_record` VALUES (49, 59, NULL, 0, 3, '', 'NONE', 'name,desc,xxx', 1, 0, 0, 0, '1,2', 0, 0, 0, '2022-03-17', '2022-03-17 06:29:47');
INSERT INTO `approve_running_record` VALUES (50, 59, NULL, 0, 4, '', 'NONE', 'name,desc,xxx', 2, 0, 0, 0, '1,2', 0, 0, 0, '2022-03-17', '2022-03-17 06:29:47');

SET FOREIGN_KEY_CHECKS = 1;
