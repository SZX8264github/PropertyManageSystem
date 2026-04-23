/*
Navicat MySQL Data Transfer

Source Server         : 本地连接
Source Server Version : 50505
Source Host           : localhost:3306
Source Database       : db_property_manage_system

Target Server Type    : MYSQL
Target Server Version : 50505
File Encoding         : 65001

Date: 2024-06-15 22:39:43
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `announce`
-- ----------------------------
DROP TABLE IF EXISTS `announce`;
CREATE TABLE `announce` (
  `id` char(8) NOT NULL DEFAULT '' COMMENT '公告id',
  `content` varchar(1024) NOT NULL DEFAULT '' COMMENT '公告内容',
  `user_id` char(8) NOT NULL DEFAULT '' COMMENT '公告所属用户id',
  `create_time` datetime NOT NULL COMMENT '发布时间',
  `top` int(2) NOT NULL DEFAULT '0' COMMENT '是否置顶  0：否；1：物业置顶；2：管理员置顶',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of announce
-- ----------------------------
INSERT INTO `announce` VALUES ('vmZCPfKa', '大家有困难的联系我', 'MDOCGRFk', '2024-06-15 09:19:27', '1');

-- ----------------------------
-- Table structure for `building`
-- ----------------------------
DROP TABLE IF EXISTS `building`;
CREATE TABLE `building` (
  `id` char(8) NOT NULL DEFAULT '' COMMENT '楼栋id',
  `name` varchar(32) NOT NULL DEFAULT '' COMMENT '楼栋名称',
  `unit_name` varchar(32) NOT NULL DEFAULT '' COMMENT '单元名称',
  `district_id` char(8) NOT NULL DEFAULT '' COMMENT '所属小区id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of building
-- ----------------------------
INSERT INTO `building` VALUES ('bnJAXFbL', 'A栋', '2单元', '4hkuFJ3X');
INSERT INTO `building` VALUES ('hiJ5NkGn', 'A栋', '1单元', 'aLiV6CDn');
INSERT INTO `building` VALUES ('oiNb5ist', 'A栋', '1单元', '4hkuFJ3X');
INSERT INTO `building` VALUES ('OQUW465b', 'B栋', '1单元', '4hkuFJ3X');
INSERT INTO `building` VALUES ('rhOYOVNg', 'C栋', '1单元', 'aLiV6CDn');

-- ----------------------------
-- Table structure for `complaint`
-- ----------------------------
DROP TABLE IF EXISTS `complaint`;
CREATE TABLE `complaint` (
  `id` char(8) NOT NULL DEFAULT '' COMMENT '投诉id',
  `user_id` char(8) NOT NULL DEFAULT '' COMMENT '投诉所属用户id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `reply` varchar(512) NOT NULL DEFAULT '' COMMENT '受理回复',
  `state` int(2) NOT NULL DEFAULT '1' COMMENT '投诉状态 1：待受理；2：已受理',
  `content` varchar(512) NOT NULL DEFAULT '' COMMENT '投诉内容描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of complaint
-- ----------------------------
INSERT INTO `complaint` VALUES ('IJEVqN7e', 'kleIA3ir', '2024-06-10 11:38:43', '马上调监控严查！', '2', 'A栋有王八蛋乱扔东西啊');

-- ----------------------------
-- Table structure for `district`
-- ----------------------------
DROP TABLE IF EXISTS `district`;
CREATE TABLE `district` (
  `id` char(8) NOT NULL DEFAULT '' COMMENT '小区id',
  `name` varchar(32) NOT NULL DEFAULT '' COMMENT '小区名称',
  `location` varchar(128) NOT NULL DEFAULT '' COMMENT '小区地址',
  `area` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '占地面积(㎡)',
  `dev_name` varchar(128) NOT NULL DEFAULT '' COMMENT '开发商名称',
  `property_name` varchar(128) NOT NULL DEFAULT '' COMMENT '物业公司名称',
  `photo` varchar(256) NOT NULL DEFAULT 'common/no_image.jpg' COMMENT '小区图片',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of district
-- ----------------------------
INSERT INTO `district` VALUES ('4hkuFJ3X', '杨杨小区', '福建省福州市鼓楼区杨杨小区', '50000.00', '杨杨开发商', '杨杨物业公司', '20240615/1718415105189.png');
INSERT INTO `district` VALUES ('aLiV6CDn', '天元小区', '福建省福州市鼓楼区天元小区', '40000.00', '天元开发商', '天元物业公司', 'common/no_image.jpg');

-- ----------------------------
-- Table structure for `fee`
-- ----------------------------
DROP TABLE IF EXISTS `fee`;
CREATE TABLE `fee` (
  `id` char(8) NOT NULL DEFAULT '' COMMENT '缴费id',
  `user_id` char(8) NOT NULL DEFAULT '' COMMENT '缴费所属业主id',
  `content` varchar(512) NOT NULL DEFAULT '' COMMENT '缴费内容',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `state` int(11) NOT NULL DEFAULT '1' COMMENT '缴费状态：1：待支付；2：已支付；3：已逾期',
  `dead_time` datetime DEFAULT NULL COMMENT '逾期日期',
  `pay_time` datetime DEFAULT NULL COMMENT '缴费时间',
  `price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '缴费金额(元)',
  `dead_price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '逾期罚金(元/天)',
  `task_id` char(8) NOT NULL DEFAULT '' COMMENT 'xxl-job的任务id',
  `add_price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '附加金额(元)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of fee
-- ----------------------------
INSERT INTO `fee` VALUES ('3crVwdtN', 'kleIA3ir', '6月电费缴费', '2024-06-15 09:18:38', '2', '2024-06-30 00:00:00', '2024-06-15 10:30:04', '70.00', '1.00', '13', '0.00');

-- ----------------------------
-- Table structure for `house`
-- ----------------------------
DROP TABLE IF EXISTS `house`;
CREATE TABLE `house` (
  `id` char(8) NOT NULL DEFAULT '' COMMENT '房屋id',
  `building_id` char(8) NOT NULL DEFAULT '' COMMENT '房屋所属楼栋id',
  `card` varchar(8) NOT NULL DEFAULT '' COMMENT '门牌号',
  `area` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '房屋面积(㎡)',
  `user_id` char(8) NOT NULL DEFAULT '' COMMENT '房屋所属业主id',
  `content` varchar(128) NOT NULL DEFAULT '' COMMENT '房屋详细信息',
  `floor` int(2) NOT NULL DEFAULT '1' COMMENT '楼层',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of house
-- ----------------------------
INSERT INTO `house` VALUES ('sQpxze6v', 'oiNb5ist', '310', '99.00', 'kleIA3ir', '房屋三室两厅，朝南', '5');

-- ----------------------------
-- Table structure for `parking`
-- ----------------------------
DROP TABLE IF EXISTS `parking`;
CREATE TABLE `parking` (
  `id` char(8) NOT NULL DEFAULT '' COMMENT '车位id',
  `district_id` char(8) NOT NULL DEFAULT '' COMMENT '车位所属小区id',
  `name` varchar(16) NOT NULL DEFAULT '' COMMENT '车位名称',
  `user_id` char(8) NOT NULL DEFAULT '' COMMENT '车位所属业主id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of parking
-- ----------------------------
INSERT INTO `parking` VALUES ('E2xr2Zac', '4hkuFJ3X', '车位-101', 'kleIA3ir');
INSERT INTO `parking` VALUES ('qpZ1vfmB', '4hkuFJ3X', '车位-102', '');

-- ----------------------------
-- Table structure for `repair`
-- ----------------------------
DROP TABLE IF EXISTS `repair`;
CREATE TABLE `repair` (
  `id` char(8) NOT NULL DEFAULT '' COMMENT '维修id',
  `user_id` char(8) NOT NULL DEFAULT '' COMMENT '维修所属用户id',
  `content` varchar(512) NOT NULL DEFAULT '' COMMENT '维修内容描述',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `state` int(2) NOT NULL DEFAULT '1' COMMENT '维修状态 1：待受理；2：已受理',
  `reply` varchar(512) NOT NULL DEFAULT '' COMMENT '受理回复',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of repair
-- ----------------------------
INSERT INTO `repair` VALUES ('1PjCyZIM', 'kleIA3ir', '家里马桶堵了，救命！', '2024-06-10 09:15:55', '2', '已经为你安排工作人员：李师傅，电话：12000212278，注意接听！');

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` char(8) NOT NULL DEFAULT '' COMMENT '用户id',
  `username` varchar(16) NOT NULL DEFAULT '' COMMENT '用户姓名',
  `password` varchar(16) NOT NULL DEFAULT '' COMMENT '用户密码',
  `phone` char(11) NOT NULL DEFAULT '' COMMENT '用户手机号码',
  `role_id` int(2) NOT NULL DEFAULT '1' COMMENT '用户角色 1：业主；2：物业员工；3：管理员',
  `sex` int(2) NOT NULL DEFAULT '3' COMMENT '用户性别 1：男；2：女；3：未知',
  `identify_card` varchar(32) NOT NULL DEFAULT '' COMMENT '身份证号',
  `birth_date` date NOT NULL COMMENT '出生日期',
  `position` varchar(128) NOT NULL DEFAULT '' COMMENT '职位',
  `study` varchar(16) NOT NULL DEFAULT '' COMMENT '学历',
  `head_pic` varchar(256) NOT NULL DEFAULT 'common/no_image.jpg' COMMENT '用户头像',
  `district_id` char(8) NOT NULL DEFAULT '' COMMENT '所属小区id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('kleIA3ir', '业主小杨', '123456', '13774559481', '1', '1', '350993322212344', '2020-05-05', '程序员', '本科', '20240615/1718412748440.jpg', '4hkuFJ3X');
INSERT INTO `user` VALUES ('MDOCGRFk', '物业员工小王', '123456', '13774559843', '2', '1', '350993322212334', '1991-06-08', '暂无职位', '暂无学历', 'common/no_image.jpg', '4hkuFJ3X');
INSERT INTO `user` VALUES ('Npxh4H3D', '杨杨吖', '123456', '13774559485', '3', '1', '3501213322212334', '1990-04-30', '暂无职位', '暂无学历', '20240615/1718415308064.jpg', '暂无数据');
INSERT INTO `user` VALUES ('R1g7nv3L', '业主小张', '123456', '13774559483', '1', '1', '350999322212344', '1996-08-30', '工程师', '本科', 'common/no_image.jpg', 'aLiV6CDn');
INSERT INTO `user` VALUES ('XUfOr4sl', '物业员工小美', '123456', '13774558983', '2', '2', '350991232212334', '2000-06-14', '暂无职位', '暂无学历', 'common/no_image.jpg', 'aLiV6CDn');
