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

 Date: 10/03/2022 17:29:40
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
  `order_num` tinyint(4) NULL DEFAULT NULL COMMENT '序号',
  `permission` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限标识符， 多个标识符用“逗号”分割',
  `support_transfer` tinyint(1) NULL DEFAULT NULL COMMENT '是否支持转办',
  `transfer` bigint(20) NULL DEFAULT NULL COMMENT '转办人员id， 如果为0，代表未指定',
  `initiator` tinyint(1) NULL DEFAULT NULL COMMENT '是否是发起人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of approve_assignee_config
-- ----------------------------
INSERT INTO `approve_assignee_config` VALUES (1, 1, 1, 1, 'name,desc,xxx', 0, 0, 0);
INSERT INTO `approve_assignee_config` VALUES (2, 2, 4, 1, 'name,desc,xxx', 0, 0, 0);
INSERT INTO `approve_assignee_config` VALUES (3, 3, 5, 1, 'name,desc,xxx', 0, 0, 0);
INSERT INTO `approve_assignee_config` VALUES (4, 4, 5, 2, 'name,desc,xxx', 0, 0, 0);

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
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of approve_model
-- ----------------------------
INSERT INTO `approve_model` VALUES (1, '测试审批', '描述', 0, 0, 'test-approve/', 'test', 'boot-approve');
INSERT INTO `approve_model` VALUES (2, '测试审批2', '描述2', 0, 0, NULL, NULL, 'boot-approve');

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
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of approve_node_config
-- ----------------------------
INSERT INTO `approve_node_config` VALUES (1, 1, 'DIRECT', '技术部审批，检测技术规范', '技术部审批', 1);
INSERT INTO `approve_node_config` VALUES (4, 1, 'DIRECT', '采购部检查物料', '采购部审批', 2);
INSERT INTO `approve_node_config` VALUES (5, 1, 'COUNTERSIGN', '生产部排期', '生产部审批', 3);

SET FOREIGN_KEY_CHECKS = 1;
