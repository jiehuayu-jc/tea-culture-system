/*
 Navicat Premium Dump SQL

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50726 (5.7.26)
 Source Host           : localhost:3306
 Source Schema         : springbootj8kskvkr

 Target Server Type    : MySQL
 Target Server Version : 50726 (5.7.26)
 File Encoding         : 65001

 Date: 15/01/2025 15:46:48
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for address
-- ----------------------------
DROP TABLE IF EXISTS `address`;
CREATE TABLE `address`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `userid` bigint(20) NOT NULL COMMENT '用户id',
  `address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '地址',
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '收货人',
  `phone` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '电话',
  `isdefault` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '是否默认地址[是/否]',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '地址' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of address
-- ----------------------------
INSERT INTO `address` VALUES (1, '2025-01-14 09:08:13', 11, '宇宙银河系金星1号', '金某', '13823888881', '是');
INSERT INTO `address` VALUES (2, '2025-01-14 09:08:13', 12, '宇宙银河系木星1号', '木某', '13823888882', '是');
INSERT INTO `address` VALUES (3, '2025-01-14 09:08:13', 13, '宇宙银河系水星1号', '水某', '13823888883', '是');
INSERT INTO `address` VALUES (4, '2025-01-14 09:08:13', 14, '宇宙银河系火星1号', '火某', '13823888884', '是');
INSERT INTO `address` VALUES (5, '2025-01-14 09:08:13', 15, '宇宙银河系土星1号', '土某', '13823888885', '是');
INSERT INTO `address` VALUES (6, '2025-01-14 09:08:13', 16, '宇宙银河系月球1号', '月某', '13823888886', '是');
INSERT INTO `address` VALUES (7, '2025-01-14 09:08:13', 17, '宇宙银河系黑洞1号', '黑某', '13823888887', '是');
INSERT INTO `address` VALUES (8, '2025-01-14 09:08:13', 18, '宇宙银河系地球1号', '地某', '13823888888', '是');

-- ----------------------------
-- Table structure for cart
-- ----------------------------
DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `tablename` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'shangpinxinxi' COMMENT '商品表名',
  `userid` bigint(20) NOT NULL COMMENT '用户id',
  `goodid` bigint(20) NOT NULL COMMENT '商品id',
  `goodname` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商品名称',
  `picture` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '图片',
  `buynumber` int(11) NOT NULL COMMENT '购买数量',
  `price` double NULL DEFAULT NULL COMMENT '单价',
  `shangjiazhanghao` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商户名称',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `price`(`price`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '购物车表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cart
-- ----------------------------

-- ----------------------------
-- Table structure for chargerecord
-- ----------------------------
DROP TABLE IF EXISTS `chargerecord`;
CREATE TABLE `chargerecord`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `userid` bigint(20) NOT NULL COMMENT '用户id',
  `username` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `role` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色',
  `amount` double NOT NULL COMMENT '金额',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '充值记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of chargerecord
-- ----------------------------
INSERT INTO `chargerecord` VALUES (1, '2025-01-14 09:08:13', 1, '用户名1', '角色1', 1);
INSERT INTO `chargerecord` VALUES (2, '2025-01-14 09:08:13', 2, '用户名2', '角色2', 2);
INSERT INTO `chargerecord` VALUES (3, '2025-01-14 09:08:13', 3, '用户名3', '角色3', 3);
INSERT INTO `chargerecord` VALUES (4, '2025-01-14 09:08:13', 4, '用户名4', '角色4', 4);
INSERT INTO `chargerecord` VALUES (5, '2025-01-14 09:08:13', 5, '用户名5', '角色5', 5);
INSERT INTO `chargerecord` VALUES (6, '2025-01-14 09:08:13', 6, '用户名6', '角色6', 6);
INSERT INTO `chargerecord` VALUES (7, '2025-01-14 09:08:13', 7, '用户名7', '角色7', 7);
INSERT INTO `chargerecord` VALUES (8, '2025-01-14 09:08:13', 8, '用户名8', '角色8', 8);

-- ----------------------------
-- Table structure for chat
-- ----------------------------
DROP TABLE IF EXISTS `chat`;
CREATE TABLE `chat`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `userid` bigint(20) NOT NULL COMMENT '用户id',
  `adminid` bigint(20) NULL DEFAULT NULL COMMENT '管理员id',
  `ask` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '提问',
  `reply` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '回复',
  `isreply` int(11) NULL DEFAULT NULL COMMENT '是否回复',
  `isread` int(11) NULL DEFAULT 0 COMMENT '已读/未读(1:已读,0:未读)',
  `uname` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户头像',
  `uimage` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '用户名',
  `type` int(11) NULL DEFAULT 1 COMMENT '内容类型(1:文本,2:图片,3:视频,4:文件,5:表情)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '智能AI' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of chat
-- ----------------------------
INSERT INTO `chat` VALUES (1, '2025-01-14 09:08:13', 1, 1, '提问1', '回复1', 1, 1, '用户头像1', 'upload/chat_uimage1.jpg,upload/chat_uimage2.jpg,upload/chat_uimage3.jpg', 1);
INSERT INTO `chat` VALUES (2, '2025-01-14 09:08:13', 2, 2, '提问2', '回复2', 2, 2, '用户头像2', 'upload/chat_uimage2.jpg,upload/chat_uimage3.jpg,upload/chat_uimage4.jpg', 2);
INSERT INTO `chat` VALUES (3, '2025-01-14 09:08:13', 3, 3, '提问3', '回复3', 3, 3, '用户头像3', 'upload/chat_uimage3.jpg,upload/chat_uimage4.jpg,upload/chat_uimage5.jpg', 3);
INSERT INTO `chat` VALUES (4, '2025-01-14 09:08:13', 4, 4, '提问4', '回复4', 4, 4, '用户头像4', 'upload/chat_uimage4.jpg,upload/chat_uimage5.jpg,upload/chat_uimage6.jpg', 4);
INSERT INTO `chat` VALUES (5, '2025-01-14 09:08:13', 5, 5, '提问5', '回复5', 5, 5, '用户头像5', 'upload/chat_uimage5.jpg,upload/chat_uimage6.jpg,upload/chat_uimage7.jpg', 5);
INSERT INTO `chat` VALUES (6, '2025-01-14 09:08:13', 6, 6, '提问6', '回复6', 6, 6, '用户头像6', 'upload/chat_uimage6.jpg,upload/chat_uimage7.jpg,upload/chat_uimage8.jpg', 6);
INSERT INTO `chat` VALUES (7, '2025-01-14 09:08:13', 7, 7, '提问7', '回复7', 7, 7, '用户头像7', 'upload/chat_uimage7.jpg,upload/chat_uimage8.jpg,upload/chat_uimage1.jpg', 7);
INSERT INTO `chat` VALUES (8, '2025-01-14 09:08:13', 8, 8, '提问8', '回复8', 8, 8, '用户头像8', 'upload/chat_uimage8.jpg,upload/chat_uimage1.jpg,upload/chat_uimage2.jpg', 8);

-- ----------------------------
-- Table structure for chatmessage
-- ----------------------------
DROP TABLE IF EXISTS `chatmessage`;
CREATE TABLE `chatmessage`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `uid` bigint(20) NOT NULL COMMENT '用户ID',
  `fid` bigint(20) NOT NULL COMMENT '好友用户ID',
  `content` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '内容',
  `format` int(11) NULL DEFAULT NULL COMMENT '格式(1:文字，2:图片)',
  `isread` int(11) NULL DEFAULT 0 COMMENT '消息已读(0:未读，1:已读)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '消息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of chatmessage
-- ----------------------------

-- ----------------------------
-- Table structure for config
-- ----------------------------
DROP TABLE IF EXISTS `config`;
CREATE TABLE `config`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '配置参数名称',
  `value` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '配置参数值',
  `url` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'url',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '配置文件' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config
-- ----------------------------
INSERT INTO `config` VALUES (1, 'picture1', 'upload/art_b1.jpg', NULL);
INSERT INTO `config` VALUES (2, 'picture2', 'upload/art_b2.jpg', NULL);
INSERT INTO `config` VALUES (3, 'picture3', 'upload/art_b3.jpg', NULL);

-- ----------------------------
-- Table structure for coupon
-- ----------------------------
DROP TABLE IF EXISTS `coupon`;
CREATE TABLE `coupon`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `userid` bigint(20) NOT NULL COMMENT '用户id',
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '名称',
  `type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '券类型',
  `fullamount` double NOT NULL DEFAULT 0 COMMENT '满额',
  `discountamount` double NOT NULL DEFAULT 0 COMMENT '优惠额',
  `startime` datetime NOT NULL COMMENT '生效时间',
  `endtime` datetime NOT NULL COMMENT '过期时间',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `shangjiazhanghao` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商户名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '优惠券' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of coupon
INSERT INTO `coupon` VALUES (1, '2026-09-01 09:00:00', 11, '新人见面礼：满99减10', '满减券', 99, 10, '2026-09-01 00:00:00', '2026-12-31 23:59:59', '全场茶叶通用', '商家账号1');
INSERT INTO `coupon` VALUES (2, '2026-09-01 09:00:00', 12, '白茶季：满228减30', '满减券', 228, 30, '2026-09-10 00:00:00', '2026-11-30 23:59:59', '白茶类专用', '商家账号5');
INSERT INTO `coupon` VALUES (3, '2026-09-01 09:00:00', 13, '茶器焕新：满399减50', '满减券', 399, 50, '2026-09-15 00:00:00', '2026-12-31 23:59:59', '茶具类专用', '商家账号8');
-- ----------------------------

-- ----------------------------
-- Table structure for discussjiaoxueshipin
-- ----------------------------
DROP TABLE IF EXISTS `discussjiaoxueshipin`;
CREATE TABLE `discussjiaoxueshipin`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `refid` bigint(20) NOT NULL COMMENT '关联表id',
  `userid` bigint(20) NOT NULL COMMENT '用户id',
  `avatarurl` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '头像',
  `nickname` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '评论内容',
  `reply` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '回复内容',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '学习视频评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of discussjiaoxueshipin
-- ----------------------------

-- ----------------------------
-- Table structure for discussshangpinxinxi
-- ----------------------------
DROP TABLE IF EXISTS `discussshangpinxinxi`;
CREATE TABLE `discussshangpinxinxi`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `refid` bigint(20) NOT NULL COMMENT '关联表id',
  `userid` bigint(20) NOT NULL COMMENT '用户id',
  `avatarurl` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '头像',
  `nickname` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户名',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '评论内容',
  `score` double NULL DEFAULT NULL COMMENT '评分',
  `reply` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '回复内容',
  `thumbsupnum` int(11) NULL DEFAULT 0 COMMENT '赞',
  `crazilynum` int(11) NULL DEFAULT 0 COMMENT '踩',
  `istop` int(11) NULL DEFAULT 0 COMMENT '置顶(1:置顶,0:非置顶)',
  `tuserids` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '赞用户ids',
  `cuserids` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '踩用户ids',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '商品信息评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of discussshangpinxinxi
-- ----------------------------

-- ----------------------------
-- Table structure for forum
-- ----------------------------
DROP TABLE IF EXISTS `forum`;
CREATE TABLE `forum`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '帖子标题',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '帖子内容',
  `parentid` bigint(20) NULL DEFAULT NULL COMMENT '父节点id',
  `userid` bigint(20) NOT NULL COMMENT '用户id',
  `username` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户名',
  `avatarurl` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '头像',
  `isdone` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '状态',
  `istop` int(11) NULL DEFAULT 0 COMMENT '是否置顶',
  `toptime` datetime NULL DEFAULT NULL COMMENT '置顶时间',
  `typename` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '分类名称',
  `cover` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '封面',
  `isanon` int(11) NULL DEFAULT 0 COMMENT '是否匿名(1:是,0:否)',
  `delflag` int(11) NULL DEFAULT 0 COMMENT '是否删除(1:是,0:否)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `typename`(`typename`) USING BTREE,
  CONSTRAINT `forum_ibfk_1` FOREIGN KEY (`typename`) REFERENCES `forumtype` (`typename`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '论坛交流' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of forum
INSERT INTO `forum` VALUES (1, '2026-08-15 09:00:00', '用盖碗还是紫砂泡凤凰单丛？求劝架', '同一泡鸭屎香，盖碗闻香更锐，紫砂入口更柔，至今无法说服自己只用其一。各位茶友站哪边？', 0, 12, '用户账号12', 'upload/yonghu_touxiang2.jpg', '开放', 1, '2026-08-15 09:00:00', '品茶心得', 'upload/art_c1.jpg', 0, 0);
INSERT INTO `forum` VALUES (2, '2026-08-15 09:00:00', '第一次买白毫银针，这样正常吗', '芽头带小绒毛，泡起来毫香很明显，就是总觉得味道"淡"。后来才知道银针本就清淡淡雅，是我误会它了。', 0, 13, '用户账号13', 'upload/yonghu_touxiang3.jpg', '开放', 0, '2026-08-15 09:00:00', '品茶心得', 'upload/art_c2.jpg', 0, 0);
INSERT INTO `forum` VALUES (3, '2026-08-15 09:00:00', '请教：办公室泡茶有什么好方案', '工位只有一个马克杯，又不想喝到茶渣。求推荐便携茶具或者冷泡方案！', 0, 14, '用户账号14', 'upload/yonghu_touxiang4.jpg', '开放', 0, '2026-08-15 09:00:00', '问答互助', 'upload/art_c3.jpg', 0, 0);
INSERT INTO `forum` VALUES (4, '2026-08-15 09:00:00', '西湖边茶馆打卡，一盏龙井配桂花', '秋天就该这样过：湖边、桂香、一盏温热的龙井。老板娘说再过两周桂花开满就更好看了。', 0, 15, '用户账号15', 'upload/yonghu_touxiang5.jpg', '开放', 0, '2026-08-15 09:00:00', '茶友闲聊', 'upload/art_c4.jpg', 0, 0);
INSERT INTO `forum` VALUES (5, '2026-08-15 09:00:00', '存了三年的白牡丹开饼记', '当年随手囤的饼，现在药香出来了，汤也稠了。事实证明：白茶的时间魔法是真的。附撬饼教程在楼里。', 0, 16, '用户账号16', 'upload/yonghu_touxiang6.jpg', '开放', 0, '2026-08-15 09:00:00', '晒茶晒器', 'upload/art_c5.jpg', 0, 0);
INSERT INTO `forum` VALUES (6, '2026-08-15 09:00:00', '新手求推荐入门级红茶', '预算 100 以内，想找不苦涩、甜口一点的。正山小种和祁红之间怎么选？', 0, 11, '用户账号11', 'upload/yonghu_touxiang1.jpg', '开放', 0, '2026-08-15 09:00:00', '问答互助', 'upload/art_c6.jpg', 0, 0);
INSERT INTO `forum` VALUES (7, '2026-08-15 09:00:00', '秋天真的太适合煮老白茶了', '红枣+老白茶，小火慢煮十分钟，满屋枣香。降温的日子被这一壶治愈了。', 0, 12, '用户账号12', 'upload/yonghu_touxiang2.jpg', '开放', 0, '2026-08-15 09:00:00', '冲泡技巧', 'upload/art_c7.jpg', 0, 0);
INSERT INTO `forum` VALUES (8, '2026-08-15 09:00:00', '晒晒我的茶桌一角', '换了原木小茶盘，添了一只手作公道杯，每天下班最期待的就是这半小时。', 0, 13, '用户账号13', 'upload/yonghu_touxiang3.jpg', '开放', 0, '2026-08-15 09:00:00', '以茶会友', 'upload/art_c8.jpg', 0, 0);
-- ----------------------------

-- ----------------------------
-- Table structure for forumreport
-- ----------------------------
DROP TABLE IF EXISTS `forumreport`;
CREATE TABLE `forumreport`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `forumid` bigint(20) NULL DEFAULT NULL COMMENT '论坛id',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '帖子标题',
  `userid` bigint(20) NOT NULL COMMENT '举报用户id',
  `username` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '举报用户名',
  `reporteduserid` bigint(20) NOT NULL COMMENT '被举报用户id',
  `reportedusername` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '被举报用户名',
  `reason` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '举报原因',
  `picture` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '图片补充',
  `handleadvise` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '处理建议',
  `status` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '处理中' COMMENT '状态',
  `reporttype` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '主题帖举报' COMMENT '举报类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '论坛交流举报' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of forumreport
-- ----------------------------

-- ----------------------------
-- Table structure for forumtype
-- ----------------------------
DROP TABLE IF EXISTS `forumtype`;
CREATE TABLE `forumtype`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `typename` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '分类名称',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `forumtype_m838`(`typename`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '论坛交流类型' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of forumtype
INSERT INTO `forumtype` VALUES (1, '2026-08-15 09:00:00', '茶友闲聊');
INSERT INTO `forumtype` VALUES (2, '2026-08-15 09:00:00', '品茶心得');
INSERT INTO `forumtype` VALUES (3, '2026-08-15 09:00:00', '晒茶晒器');
INSERT INTO `forumtype` VALUES (4, '2026-08-15 09:00:00', '问答互助');
INSERT INTO `forumtype` VALUES (5, '2026-08-15 09:00:00', '冲泡技巧');
INSERT INTO `forumtype` VALUES (6, '2026-08-15 09:00:00', '茶山游记');
INSERT INTO `forumtype` VALUES (7, '2026-08-15 09:00:00', '茶博会');
INSERT INTO `forumtype` VALUES (8, '2026-08-15 09:00:00', '以茶会友');
-- ----------------------------

-- ----------------------------
-- Table structure for friend
-- ----------------------------
DROP TABLE IF EXISTS `friend`;
CREATE TABLE `friend`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `uid` bigint(20) NOT NULL COMMENT '用户ID',
  `fid` bigint(20) NOT NULL COMMENT '好友用户ID',
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '名称',
  `picture` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图片',
  `role` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色',
  `tablename` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '表名',
  `alias` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '别名',
  `type` int(11) NULL DEFAULT 0 COMMENT '类型(0:好友申请，1:好友，2:消息)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '好友表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of friend
-- ----------------------------

-- ----------------------------
-- Table structure for jiaoxueshipin
-- ----------------------------
DROP TABLE IF EXISTS `jiaoxueshipin`;
CREATE TABLE `jiaoxueshipin`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `biaoti` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标题',
  `kechengleibie` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '课程类别',
  `fengmian` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '封面',
  `jiaoshigonghao` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '讲师工号',
  `jiaoshixingming` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '讲师姓名',
  `jiaoxueshipin` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '学习视频',
  `fabushijian` date NULL DEFAULT NULL COMMENT '发布时间',
  `jibenjieshao` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '基本介绍',
  `clicktime` datetime NULL DEFAULT NULL COMMENT '最近点击时间',
  `clicknum` int(11) NULL DEFAULT 0 COMMENT '点击次数',
  `discussnum` int(11) NULL DEFAULT 0 COMMENT '评论数',
  `storeupnum` int(11) NULL DEFAULT 0 COMMENT '收藏数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 52 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '学习视频' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of jiaoxueshipin
INSERT INTO `jiaoxueshipin` VALUES (1, '2026-08-15 09:00:00', '中国茶文化简史：从神农尝百草到茶马古道', '茶史', 'upload/art_c1.jpg', 'JS001', '林清和', '', '2026-08-06', '<p>中国是茶的故乡。自神农氏"日遇七十二毒，得荼而解之"的传说起，茶便与中华文明交织：唐代陆羽著《茶经》首开茶学先河，宋代点茶斗茶蔚然成风，明清散茶冲泡奠定今日品饮格局，茶马古道则把这片东方树叶送往世界。</p>', '2026-08-15 09:00:00', 57, 0, 1);
INSERT INTO `jiaoxueshipin` VALUES (2, '2026-08-15 09:00:00', '六大茶类图鉴：一张表分清绿红青白黑黄', '六大茶类', 'upload/art_c2.jpg', 'JS002', '周茗月', '', '2026-08-07', '<p>以工艺定类别：绿茶不发酵讲究鲜爽，白茶微发酵自然萎凋，黄茶闷黄出甜醇，乌龙茶（青茶）半发酵求花香岩韵，红茶全发酵温润甜醇，黑茶后发酵越陈越香。看工艺，就能看懂一片叶子的千变万化。</p>', '2026-08-15 09:00:00', 74, 0, 2);
INSERT INTO `jiaoxueshipin` VALUES (3, '2026-08-15 09:00:00', '绿茶冲泡的三个关键：水温、投茶量、出汤', '冲泡技艺', 'upload/art_c3.jpg', 'JS003', '陈云鹤', '', '2026-08-08', '<p>名优绿茶宜 80-85℃ 水温，茶水比约 1:50，玻璃杯下投法先水后茶亦可上投观赏。第一泡 15 秒内出汤，久闷必苦。记住"水温低、时间短、茶叶嫩"，绿茶的鲜就不难得到。</p>', '2026-08-15 09:00:00', 91, 0, 3);
INSERT INTO `jiaoxueshipin` VALUES (4, '2026-08-15 09:00:00', '功夫茶入门：盖碗八式分解教学', '冲泡技艺', 'upload/art_c4.jpg', 'JS004', '苏若水', '', '2026-08-09', '<p>备器、候汤、温杯、投茶、润茶、冲泡、出汤、分茶，八式一气呵成。"关公巡城"求匀，"韩信点兵"取浓，一杯一盏皆见工夫。</p>', '2026-08-15 09:00:00', 108, 0, 4);
INSERT INTO `jiaoxueshipin` VALUES (5, '2026-08-15 09:00:00', '紫砂壶入门：泥料、器型与养壶', '茶器', 'upload/art_c5.jpg', 'JS005', '陶紫砂', '', '2026-08-10', '<p>紫泥沉稳、朱泥高香、段泥清雅。新人建议从西施、石瓢等经典器型入手，一壶侍一茶。养壶之道在于勤淋勤拭、内壁勿存宿茶，日久自然温润如玉。</p>', '2026-08-15 09:00:00', 125, 0, 5);
INSERT INTO `jiaoxueshipin` VALUES (6, '2026-08-15 09:00:00', '潮汕工夫茶俗：关公巡城与韩信点兵', '茶俗', 'upload/art_c6.jpg', 'JS006', '郑潮生', '', '2026-08-11', '<p>在潮汕，茶是待客的最高礼遇。三家四户必备茶器，客人礼让三让方落座。"茶三酒四踢桃二"，小小盖碗在指尖流转之间，传承着最重的家常与礼数。</p>', '2026-08-15 09:00:00', 142, 0, 6);
INSERT INTO `jiaoxueshipin` VALUES (7, '2026-08-15 09:00:00', '茶席布置基础：从一巾一则开始', '茶器', 'upload/art_c7.jpg', 'JS007', '白鼎生', '', '2026-08-12', '<p>茶席之美在于留白与呼应：席布定色调，主泡器定重心，花器插一只应季草木即可。一巾一则之间，让器物各归其位，让茶成为主角。</p>', '2026-08-15 09:00:00', 159, 0, 7);
INSERT INTO `jiaoxueshipin` VALUES (8, '2026-08-15 09:00:00', '茶联与茶诗：杯中有文章', '茶史', 'upload/art_c8.jpg', 'JS008', '古滇南', '', '2026-08-13', '<p>"寒夜客来茶当酒，竹炉汤沸火初红。"从卢仝七碗茶到郑板桥的茶联，文人以茶入诗、以诗题联。读茶诗，品的其实是中国人把日子过成艺术的底气。</p>', '2026-08-15 09:00:00', 176, 0, 8);
-- ----------------------------

-- ----------------------------
-- Table structure for messages
-- ----------------------------
DROP TABLE IF EXISTS `messages`;
CREATE TABLE `messages`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `userid` bigint(20) NOT NULL COMMENT '留言人id',
  `username` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户名',
  `avatarurl` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '头像',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '留言内容',
  `cpicture` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '留言图片',
  `reply` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '回复内容',
  `rpicture` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '回复图片',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '留言板' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of messages
-- ----------------------------
INSERT INTO `messages` VALUES (1, '2025-01-14 09:08:13', 1, '用户名1', 'upload/messages_avatarurl1.jpg', '留言内容1', 'upload/messages_cpicture1.jpg', '回复内容1', 'upload/messages_rpicture1.jpg');
INSERT INTO `messages` VALUES (2, '2025-01-14 09:08:13', 2, '用户名2', 'upload/messages_avatarurl2.jpg', '留言内容2', 'upload/messages_cpicture2.jpg', '回复内容2', 'upload/messages_rpicture2.jpg');
INSERT INTO `messages` VALUES (3, '2025-01-14 09:08:13', 3, '用户名3', 'upload/messages_avatarurl3.jpg', '留言内容3', 'upload/messages_cpicture3.jpg', '回复内容3', 'upload/messages_rpicture3.jpg');
INSERT INTO `messages` VALUES (4, '2025-01-14 09:08:13', 4, '用户名4', 'upload/messages_avatarurl4.jpg', '留言内容4', 'upload/messages_cpicture4.jpg', '回复内容4', 'upload/messages_rpicture4.jpg');
INSERT INTO `messages` VALUES (5, '2025-01-14 09:08:13', 5, '用户名5', 'upload/messages_avatarurl5.jpg', '留言内容5', 'upload/messages_cpicture5.jpg', '回复内容5', 'upload/messages_rpicture5.jpg');
INSERT INTO `messages` VALUES (6, '2025-01-14 09:08:13', 6, '用户名6', 'upload/messages_avatarurl6.jpg', '留言内容6', 'upload/messages_cpicture6.jpg', '回复内容6', 'upload/messages_rpicture6.jpg');
INSERT INTO `messages` VALUES (7, '2025-01-14 09:08:13', 7, '用户名7', 'upload/messages_avatarurl7.jpg', '留言内容7', 'upload/messages_cpicture7.jpg', '回复内容7', 'upload/messages_rpicture7.jpg');
INSERT INTO `messages` VALUES (8, '2025-01-14 09:08:13', 8, '用户名8', 'upload/messages_avatarurl8.jpg', '留言内容8', 'upload/messages_cpicture8.jpg', '回复内容8', 'upload/messages_rpicture8.jpg');

-- ----------------------------
-- Table structure for mycoupon
-- ----------------------------
DROP TABLE IF EXISTS `mycoupon`;
CREATE TABLE `mycoupon`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `userid` bigint(20) NOT NULL COMMENT '用户id',
  `couponid` bigint(20) NOT NULL COMMENT '优惠券id',
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '名称',
  `couponnumber` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '券编号',
  `fullamount` double NOT NULL DEFAULT 0 COMMENT '满额',
  `discountamount` double NOT NULL DEFAULT 0 COMMENT '优惠额',
  `startime` datetime NULL DEFAULT NULL COMMENT '生效时间',
  `endtime` datetime NULL DEFAULT NULL COMMENT '过期时间',
  `type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '优惠券类型',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `status` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '状态',
  `shangjiazhanghao` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商户名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '我的优惠券' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mycoupon
-- ----------------------------

-- ----------------------------
-- Table structure for news
-- ----------------------------
DROP TABLE IF EXISTS `news`;
CREATE TABLE `news`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标题',
  `introduction` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '简介',
  `typename` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '分类名称',
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '发布人',
  `headportrait` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '头像',
  `clicknum` int(11) NULL DEFAULT 0 COMMENT '点击次数',
  `clicktime` datetime NULL DEFAULT NULL COMMENT '最近点击时间',
  `thumbsupnum` int(11) NULL DEFAULT 0 COMMENT '赞',
  `crazilynum` int(11) NULL DEFAULT 0 COMMENT '踩',
  `storeupnum` int(11) NULL DEFAULT 0 COMMENT '收藏数',
  `picture` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图片',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '内容',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '购物资讯' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of news
INSERT INTO `news` VALUES (1, '2026-08-15 09:00:00', '2026 秋茶上市：乌龙茶为什么讲究"秋香"', '秋茶以香见长。寒露前后采摘的凤凰单丛与铁观音，香气清锐高扬，被茶客称为"秋香"。今年产区雨水适中，秋茶整体品质优于去年，……', '茶界动态', '茶小辑', 'upload/yonghu_touxiang1.jpg', 151, '2026-08-15 09:00:00', 32, 4, 1, 'upload/art_c1.jpg', '<p>秋茶以香见长。寒露前后采摘的凤凰单丛与铁观音，香气清锐高扬，被茶客称为"秋香"。今年产区雨水适中，秋茶整体品质优于去年，感兴趣的茶友可以关注本周上新的单丛新焙火批次。</p>');
INSERT INTO `news` VALUES (2, '2026-08-15 09:00:00', '上新公告 | 头采明前龙井限量开售', '狮峰产区头采龙井已到仓，共 200 罐，每人限购 2 罐。今年头采发芽整齐，豆香明显、鲜爽度高。老客户优先，售罄即止，点……', '上新公告', '茶小辑', 'upload/yonghu_touxiang2.jpg', 182, '2026-08-15 09:00:00', 39, 5, 2, 'upload/art_c2.jpg', '<p>狮峰产区头采龙井已到仓，共 200 罐，每人限购 2 罐。今年头采发芽整齐，豆香明显、鲜爽度高。老客户优先，售罄即止，点击"到货通知"可订阅补货提醒。</p>');
INSERT INTO `news` VALUES (3, '2026-08-15 09:00:00', '一文看懂茶叶包装上的等级与标准号', 'GB/T 22111 指地理标志普洱茶，GB/T 22291 指白茶。"特级""一级"是感官等级而非品质保证，产区、工艺……', '茶知识', '茶小辑', 'upload/yonghu_touxiang3.jpg', 213, '2026-08-15 09:00:00', 46, 6, 3, 'upload/art_c3.jpg', '<p>GB/T 22111 指地理标志普洱茶，GB/T 22291 指白茶。"特级""一级"是感官等级而非品质保证，产区、工艺、仓储同样重要。学会看标准号与产地，能避开九成营销话术。</p>');
INSERT INTO `news` VALUES (4, '2026-08-15 09:00:00', '绿茶到底要不要放冰箱？家庭存茶指南', '绿茶、黄茶密封冷藏（0-5℃），取茶后回温再开袋以防吸潮；乌龙、红茶常温避光密封即可；白茶、黑茶常温通风、离墙离地，忌塑……', '茶知识', '茶小辑', 'upload/yonghu_touxiang4.jpg', 244, '2026-08-15 09:00:00', 53, 7, 4, 'upload/art_c4.jpg', '<p>绿茶、黄茶密封冷藏（0-5℃），取茶后回温再开袋以防吸潮；乌龙、红茶常温避光密封即可；白茶、黑茶常温通风、离墙离地，忌塑料密封。记住：怕氧怕光怕潮怕异味。</p>');
INSERT INTO `news` VALUES (5, '2026-08-15 09:00:00', '玻璃杯泡绿茶的三投法，新手也能会', '上投法：先水后茶，适合白毫银针等重芽茶；中投法：水至三分之一投茶再续水，通用；下投法：先茶后水，适合大宗绿茶。水位七分满……', '冲泡指南', '茶小辑', 'upload/yonghu_touxiang5.jpg', 275, '2026-08-15 09:00:00', 60, 8, 5, 'upload/art_c5.jpg', '<p>上投法：先水后茶，适合白毫银针等重芽茶；中投法：水至三分之一投茶再续水，通用；下投法：先茶后水，适合大宗绿茶。水位七分满，留三分是人情。</p>');
INSERT INTO `news` VALUES (6, '2026-08-15 09:00:00', '普洱饼怎么撬不碎手不伤：工具与手法', '茶锥沿饼窝边缘分层插入，轻抬手腕"撬"而非"剁"；紧压老茶可先以茶针沿层面松针式探入。散落碎茶收入茶漏再利用，一片饼能多……', '冲泡指南', '茶小辑', 'upload/yonghu_touxiang6.jpg', 306, '2026-08-15 09:00:00', 67, 9, 6, 'upload/art_c6.jpg', '<p>茶锥沿饼窝边缘分层插入，轻抬手腕"撬"而非"剁"；紧压老茶可先以茶针沿层面松针式探入。散落碎茶收入茶漏再利用，一片饼能多喝两泡。</p>');
INSERT INTO `news` VALUES (7, '2026-08-15 09:00:00', '茶多酚、咖啡碱到底对身体意味着什么', '茶多酚抗氧化，咖啡碱提神并促进代谢，茶氨酸则带来"平静的专注"。肠胃敏感者避免空腹浓茶，睡眠弱者下午四点后可选低咖啡碱的……', '茶与健康', '茶小辑', 'upload/yonghu_touxiang7.jpg', 337, '2026-08-15 09:00:00', 74, 10, 7, 'upload/art_c7.jpg', '<p>茶多酚抗氧化，咖啡碱提神并促进代谢，茶氨酸则带来"平静的专注"。肠胃敏感者避免空腹浓茶，睡眠弱者下午四点后可选低咖啡碱的白茶或熟普。</p>');
INSERT INTO `news` VALUES (8, '2026-08-15 09:00:00', '回顾 | 秋季茶博会上的器物之美', '本届茶博会 300 余家展商中，手作盖碗与粗陶公道杯最受年轻茶友欢迎。展会首次设立"新中式茶空间"体验区，把席、器、花、……', '活动回顾', '茶小辑', 'upload/yonghu_touxiang8.jpg', 368, '2026-08-15 09:00:00', 81, 11, 8, 'upload/art_c8.jpg', '<p>本届茶博会 300 余家展商中，手作盖碗与粗陶公道杯最受年轻茶友欢迎。展会首次设立"新中式茶空间"体验区，把席、器、花、香融进一方小桌，人气爆棚。</p>');
-- ----------------------------

-- ----------------------------
-- Table structure for newstype
-- ----------------------------
DROP TABLE IF EXISTS `newstype`;
CREATE TABLE `newstype`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `typename` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分类名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '购物资讯分类' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of newstype
INSERT INTO `newstype` VALUES (1, '2026-08-15 09:00:00', '茶界动态');
INSERT INTO `newstype` VALUES (2, '2026-08-15 09:00:00', '上新公告');
INSERT INTO `newstype` VALUES (3, '2026-08-15 09:00:00', '茶知识');
INSERT INTO `newstype` VALUES (4, '2026-08-15 09:00:00', '冲泡指南');
INSERT INTO `newstype` VALUES (5, '2026-08-15 09:00:00', '茶与健康');
INSERT INTO `newstype` VALUES (6, '2026-08-15 09:00:00', '活动回顾');
INSERT INTO `newstype` VALUES (7, '2026-08-15 09:00:00', '茶器鉴赏');
INSERT INTO `newstype` VALUES (8, '2026-08-15 09:00:00', '茶山游记');
-- ----------------------------

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `orderid` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '订单编号',
  `tablename` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'shangpinxinxi' COMMENT '商品表名',
  `userid` bigint(20) NOT NULL COMMENT '用户id',
  `goodid` bigint(20) NOT NULL COMMENT '商品id',
  `goodname` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商品名称',
  `picture` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '商品图片',
  `buynumber` int(11) NOT NULL COMMENT '购买数量',
  `price` double NOT NULL DEFAULT 0 COMMENT '价格',
  `total` double NOT NULL DEFAULT 0 COMMENT '总价格',
  `type` int(11) NULL DEFAULT 1 COMMENT '支付类型',
  `status` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '状态',
  `address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '地址',
  `tel` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '电话',
  `consignee` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '收货人',
  `logistics` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '物流',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `shangjiazhanghao` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商户名称',
  `sfsh` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '是否审核',
  `shhf` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '审核回复',
  `role` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户角色',
  `couponnumber` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '券编号',
  `discountamount` double NULL DEFAULT 0 COMMENT '优惠额',
  `orderno` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '统一订单编号',
  `returnreason` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '退货原因',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `orderid`(`orderid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '订单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orders
INSERT INTO `orders` VALUES (1, '2026-09-20 10:30:00', '2026092010300001', 'shangpinxinxi', 11, 1, '明前特级西湖龙井', 'upload/shangpinxinxi_shangpintupian1.jpg', 1, 128, 128, 1, '已完成', '宇宙银河系金星1号', '13823888881', '金某', 'SF1060001112233', '', '商家账号1', '是', '感谢惠顾，欢迎再次品鉴', '用户', '', 0, '2026092010300001', '');
INSERT INTO `orders` VALUES (2, '2026-09-21 14:05:00', '2026092114050002', 'shangpinxinxi', 12, 3, '安溪铁观音清香型', 'upload/shangpinxinxi_shangpintupian3.jpg', 2, 158, 316, 1, '已发货', '宇宙银河系木星1号', '13823888882', '木某', 'SF1060001112244', '', '商家账号3', '是', '已发货，注意查收', '用户', '', 0, '2026092114050002', '');
INSERT INTO `orders` VALUES (3, '2026-09-24 19:22:00', '2026092419220003', 'shangpinxinxi', 13, 12, '宜兴紫砂西施壶', 'upload/shangpinxinxi_shangpintupian4.jpg', 1, 399, 399, 1, '已支付', '宇宙银河系水星1号', '13823888883', '水某', '', '', '商家账号8', '否', '', '用户', '', 0, '2026092419220003', '');
INSERT INTO `orders` VALUES (4, '2026-09-25 09:10:00', '2026092509100004', 'shangpinxinxi', 11, 11, '茉莉花茶雪毫', 'upload/shangpinxinxi_shangpintupian3.jpg', 2, 45, 90, 1, '未支付', '宇宙银河系金星1号', '13823888881', '金某', '', '', '商家账号1', '否', '', '用户', '', 0, '2026092509100004', '');
INSERT INTO `orders` VALUES (5, '2026-09-18 16:48:00', '2026091816480005', 'shangpinxinxi', 14, 16, '茶香凤梨酥', 'upload/shangpinxinxi_shangpintupian8.jpg', 2, 35, 70, 1, '已退款', '宇宙银河系火星1号', '13823888884', '火某', '', '', '商家账号2', '是', '已退款原路退回', '用户', '', 30, '2026091816480005', '破损');
-- ----------------------------

-- ----------------------------
-- Table structure for shangjia
-- ----------------------------
DROP TABLE IF EXISTS `shangjia`;
CREATE TABLE `shangjia`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `shangjiazhanghao` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商家账号',
  `mima` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `shangjiamingcheng` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商家名称',
  `touxiang` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '头像',
  `fuzeren` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '负责人',
  `lianxidianhua` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系电话',
  `yingyezhizhao` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '营业执照',
  `sfsh` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '待审核' COMMENT '是否审核',
  `shhf` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '审核回复',
  `money` double NULL DEFAULT 0 COMMENT '余额',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `shangjiazhanghao`(`shangjiazhanghao`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '商家' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of shangjia
INSERT INTO `shangjia` VALUES (21, '2026-08-15 09:00:00', '商家账号1', 'e10adc3949ba59abbe56e057f20f883e', '云雾茶庄', 'upload/shangjia_touxiang1.jpg', '陈云鹤', '13823888861', '', '是', '', 200);
INSERT INTO `shangjia` VALUES (22, '2026-08-15 09:00:00', '商家账号2', 'e10adc3949ba59abbe56e057f20f883e', '清心茶舍', 'upload/shangjia_touxiang2.jpg', '林清和', '13823888862', '', '是', '', 200);
INSERT INTO `shangjia` VALUES (23, '2026-08-15 09:00:00', '商家账号3', 'e10adc3949ba59abbe56e057f20f883e', '武夷星茶业', 'upload/shangjia_touxiang3.jpg', '王武夷', '13823888863', '', '是', '', 200);
INSERT INTO `shangjia` VALUES (24, '2026-08-15 09:00:00', '商家账号4', 'e10adc3949ba59abbe56e057f20f883e', '澜沧古茶坊', 'upload/shangjia_touxiang4.jpg', '古滇南', '13823888864', '', '是', '', 200);
INSERT INTO `shangjia` VALUES (25, '2026-08-15 09:00:00', '商家账号5', 'e10adc3949ba59abbe56e057f20f883e', '福鼎白茶行', 'upload/shangjia_touxiang5.jpg', '白鼎生', '13823888865', '', '是', '', 200);
INSERT INTO `shangjia` VALUES (26, '2026-08-15 09:00:00', '商家账号6', 'e10adc3949ba59abbe56e057f20f883e', '祁门红茶庄', 'upload/shangjia_touxiang6.jpg', '祁门红', '13823888866', '', '是', '', 200);
INSERT INTO `shangjia` VALUES (27, '2026-08-15 09:00:00', '商家账号7', 'e10adc3949ba59abbe56e057f20f883e', '凤凰单丛铺', 'upload/shangjia_touxiang7.jpg', '凤凰木', '13823888867', '', '是', '', 200);
INSERT INTO `shangjia` VALUES (28, '2026-08-15 09:00:00', '商家账号8', 'e10adc3949ba59abbe56e057f20f883e', '紫砂茶器阁', 'upload/shangjia_touxiang8.jpg', '陶紫砂', '13823888868', '', '是', '', 200);
-- ----------------------------

-- ----------------------------
-- Table structure for shangpinfenlei
-- ----------------------------
DROP TABLE IF EXISTS `shangpinfenlei`;
CREATE TABLE `shangpinfenlei`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `shangpinfenlei` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商品分类',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '商品分类' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of shangpinfenlei
INSERT INTO `shangpinfenlei` VALUES (1, '2026-08-15 09:00:00', '绿茶');
INSERT INTO `shangpinfenlei` VALUES (2, '2026-08-15 09:00:00', '红茶');
INSERT INTO `shangpinfenlei` VALUES (3, '2026-08-15 09:00:00', '乌龙茶');
INSERT INTO `shangpinfenlei` VALUES (4, '2026-08-15 09:00:00', '白茶');
INSERT INTO `shangpinfenlei` VALUES (5, '2026-08-15 09:00:00', '黄茶');
INSERT INTO `shangpinfenlei` VALUES (6, '2026-08-15 09:00:00', '黑茶');
INSERT INTO `shangpinfenlei` VALUES (7, '2026-08-15 09:00:00', '茶具');
INSERT INTO `shangpinfenlei` VALUES (8, '2026-08-15 09:00:00', '茶点');
-- ----------------------------

-- ----------------------------
-- Table structure for shangpinxinxi
-- ----------------------------
DROP TABLE IF EXISTS `shangpinxinxi`;
CREATE TABLE `shangpinxinxi`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `shangpinbianhao` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品编号',
  `shangpinmingcheng` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品名称',
  `shangpinfenlei` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品分类',
  `shangpintupian` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品图片',
  `guige` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '规格',
  `shengchanchangshang` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '生产厂商',
  `shangpinjieshao` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '商品介绍',
  `onelimittimes` int(11) NULL DEFAULT NULL COMMENT '单限',
  `alllimittimes` int(11) NULL DEFAULT NULL COMMENT '库存',
  `shangjiazhanghao` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商家账号',
  `shangjiamingcheng` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '商家名称',
  `clicktime` datetime NULL DEFAULT NULL COMMENT '最近点击时间',
  `reversetime` datetime NULL DEFAULT NULL COMMENT '倒计结束时间',
  `clicknum` int(11) NULL DEFAULT 0 COMMENT '点击次数',
  `discussnum` int(11) NULL DEFAULT 0 COMMENT '评论数',
  `totalscore` double NULL DEFAULT 0 COMMENT '评分',
  `price` double NOT NULL COMMENT '价格',
  `onshelves` int(11) NULL DEFAULT 1 COMMENT '是否上架(1:上架，0:下架)',
  `storeupnum` int(11) NULL DEFAULT 0 COMMENT '收藏数',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `shangpinbianhao`(`shangpinbianhao`) USING BTREE,
  INDEX `shangpinxinxi_price`(`price`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '商品信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of shangpinxinxi
INSERT INTO `shangpinxinxi` VALUES (1, '2026-08-15 09:00:00', 'SP001', '明前特级西湖龙井', '绿茶', 'upload/art_p1.jpg,upload/art_p2.jpg,upload/art_p3.jpg', '50g/罐', '杭州西湖龙井茶厂', '豆香清鲜，回甘持久。狮峰产区明前头采，一芽一叶初展，手工辉锅。', 2, 88, '商家账号1', '云雾茶庄', '2026-08-15 09:00:00', '2026-08-15 09:00:00', 83, 0, 4.699999999999999, 128, 1, 1);
INSERT INTO `shangpinxinxi` VALUES (2, '2026-08-15 09:00:00', 'SP002', '洞庭山碧螺春', '绿茶', 'upload/art_p2.jpg,upload/art_p3.jpg,upload/art_p4.jpg', '100g/罐', '苏州洞庭茶业', '花果香馥郁，茶果间作生态茶园，卷曲如螺，白毫显露。', 2, 66, '商家账号2', '清心茶舍', '2026-08-15 09:00:00', '2026-08-15 09:00:00', 106, 0, 4.8, 98, 1, 2);
INSERT INTO `shangpinxinxi` VALUES (3, '2026-08-15 09:00:00', 'SP003', '安溪铁观音清香型', '乌龙茶', 'upload/art_p3.jpg,upload/art_p4.jpg,upload/art_p5.jpg', '250g/盒', '安溪茶业公司', '兰花香明显，观音韵足，传统正味工艺，七泡有余香。', 2, 80, '商家账号3', '武夷星茶业', '2026-08-15 09:00:00', '2026-08-15 09:00:00', 129, 0, 4.8999999999999995, 158, 1, 3);
INSERT INTO `shangpinxinxi` VALUES (4, '2026-08-15 09:00:00', 'SP004', '武夷山大红袍', '乌龙茶', 'upload/art_p4.jpg,upload/art_p5.jpg,upload/art_p6.jpg', '100g/礼盒', '武夷星茶业', '岩骨花香，正岩产区，炭焙足火，汤感醇厚有劲。', 1, 55, '商家账号3', '武夷星茶业', '2026-08-15 09:00:00', '2026-08-15 09:00:00', 152, 0, 4.6, 268, 1, 4);
INSERT INTO `shangpinxinxi` VALUES (5, '2026-08-15 09:00:00', 'SP005', '云南普洱熟茶饼', '黑茶', 'upload/art_p5.jpg,upload/art_p6.jpg,upload/art_p7.jpg', '357g/饼', '勐海茶业', '陈香糯滑，勐海发酵，仓储干净，适合日常口粮。', 3, 120, '商家账号4', '澜沧古茶坊', '2026-08-15 09:00:00', '2026-08-15 09:00:00', 175, 0, 4.699999999999999, 199, 1, 5);
INSERT INTO `shangpinxinxi` VALUES (6, '2026-08-15 09:00:00', 'SP006', '福鼎白毫银针', '白茶', 'upload/art_p6.jpg,upload/art_p7.jpg,upload/art_p8.jpg', '100g/罐', '福鼎白茶行', '头春肥壮单芽，毫香蜜韵，日光萎凋，越陈越香。', 2, 60, '商家账号5', '福鼎白茶行', '2026-08-15 09:00:00', '2026-08-15 09:00:00', 198, 0, 4.8, 228, 1, 6);
INSERT INTO `shangpinxinxi` VALUES (7, '2026-08-15 09:00:00', 'SP007', '桐木关正山小种', '红茶', 'upload/art_p7.jpg,upload/art_p8.jpg,upload/art_p1.jpg', '100g/罐', '桐木关茶业', '松烟香桂圆汤，传统烟熏工艺，原产地核心山场。', 2, 75, '商家账号6', '祁门红茶庄', '2026-08-15 09:00:00', '2026-08-15 09:00:00', 221, 0, 4.8999999999999995, 88, 1, 7);
INSERT INTO `shangpinxinxi` VALUES (8, '2026-08-15 09:00:00', 'SP008', '祁门红茶香螺', '红茶', 'upload/art_p8.jpg,upload/art_p1.jpg,upload/art_p2.jpg', '100g/罐', '祁门红茶集团', '祁门香高扬似花似果，条索紧细如螺，甜润顺口。', 2, 90, '商家账号7', '凤凰单丛铺', '2026-08-15 09:00:00', '2026-08-15 09:00:00', 244, 0, 4.6, 78, 1, 8);
INSERT INTO `shangpinxinxi` VALUES (9, '2026-08-15 09:00:00', 'SP009', '君山银针', '黄茶', 'upload/art_p1.jpg,upload/art_p2.jpg,upload/art_p3.jpg', '50g/盒', '君山茶场', '闷黄工艺，甜醇不涩，芽头肥壮金毫披身，三起三落可观。', 1, 40, '商家账号8', '紫砂茶器阁', '2026-08-15 09:00:00', '2026-08-15 09:00:00', 267, 0, 4.699999999999999, 168, 1, 9);
INSERT INTO `shangpinxinxi` VALUES (10, '2026-08-15 09:00:00', 'SP010', '凤凰单丛鸭屎香', '乌龙茶', 'upload/art_p2.jpg,upload/art_p3.jpg,upload/art_p4.jpg', '125g/罐', '凤凰单丛铺', '高香型单丛，金银花香锐长，回甘力强，耐泡度佳。', 2, 70, '商家账号8', '紫砂茶器阁', '2026-08-15 09:00:00', '2026-08-15 09:00:00', 290, 0, 4.8, 138, 1, 10);
INSERT INTO `shangpinxinxi` VALUES (11, '2026-08-15 09:00:00', 'SP011', '茉莉花茶雪毫', '绿茶', 'upload/art_p3.jpg,upload/art_p4.jpg,upload/art_p5.jpg', '200g/袋', '福州茉莉花茶厂', '六窨一提，鲜灵持久，茶引花香，花增茶味。', 3, 150, '商家账号1', '云雾茶庄', '2026-08-15 09:00:00', '2026-08-15 09:00:00', 313, 0, 4.8999999999999995, 45, 1, 11);
INSERT INTO `shangpinxinxi` VALUES (12, '2026-08-15 09:00:00', 'SP012', '宜兴紫砂西施壶', '茶具', 'upload/art_p4.jpg,upload/art_p5.jpg,upload/art_p6.jpg', '180ml/把', '宜兴紫砂工艺厂', '原矿底槽清，球孔出水顺畅，壶身圆润称手，适合乌龙与普洱。', 1, 30, '商家账号8', '紫砂茶器阁', '2026-08-15 09:00:00', '2026-08-15 09:00:00', 336, 0, 4.6, 399, 1, 12);
INSERT INTO `shangpinxinxi` VALUES (13, '2026-08-15 09:00:00', 'SP013', '德化白瓷盖碗', '茶具', 'upload/art_p5.jpg,upload/art_p6.jpg,upload/art_p7.jpg', '120ml/只', '德化白瓷', '高白瓷胎，聚香扬甜，碗沿外翻不烫手，百元内入门首选。', 2, 100, '商家账号8', '紫砂茶器阁', '2026-08-15 09:00:00', '2026-08-15 09:00:00', 359, 0, 4.699999999999999, 89, 1, 13);
INSERT INTO `shangpinxinxi` VALUES (14, '2026-08-15 09:00:00', 'SP014', '竹制茶道六君子', '茶具', 'upload/art_p6.jpg,upload/art_p7.jpg,upload/art_p8.jpg', '六件/套', '安吉竹木工坊', '茶筒、茶夹、茶针、茶匙、茶则、茶漏齐备，碳化竹色温润。', 2, 80, '商家账号7', '凤凰单丛铺', '2026-08-15 09:00:00', '2026-08-15 09:00:00', 382, 0, 4.8, 59, 1, 14);
INSERT INTO `shangpinxinxi` VALUES (15, '2026-08-15 09:00:00', 'SP015', '桂花绿豆糕', '茶点', 'upload/art_p7.jpg,upload/art_p8.jpg,upload/art_p1.jpg', '6枚/盒', '苏州采芝斋', '桂花点缀，入口即化，配绿茶红茶皆宜。', 3, 200, '商家账号2', '清心茶舍', '2026-08-15 09:00:00', '2026-08-15 09:00:00', 405, 0, 4.8999999999999995, 29, 1, 15);
INSERT INTO `shangpinxinxi` VALUES (16, '2026-08-15 09:00:00', 'SP016', '茶香凤梨酥', '茶点', 'upload/art_p8.jpg,upload/art_p1.jpg,upload/art_p2.jpg', '8枚/盒', '广州莲香楼', '乌龙茶粉入皮，酥松不腻，佐茶佳品。', 3, 180, '商家账号2', '清心茶舍', '2026-08-15 09:00:00', '2026-08-15 09:00:00', 428, 0, 4.6, 35, 1, 16);
-- ----------------------------

-- ----------------------------
-- Table structure for storeup
-- ----------------------------
DROP TABLE IF EXISTS `storeup`;
CREATE TABLE `storeup`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `userid` bigint(20) NOT NULL COMMENT '用户id',
  `refid` bigint(20) NULL DEFAULT NULL COMMENT '商品id',
  `tablename` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '表名',
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '名称',
  `picture` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '图片',
  `type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '1' COMMENT '类型',
  `inteltype` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '推荐类型',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '收藏表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of storeup
INSERT INTO `storeup` VALUES (1, '2026-09-25 15:00:00', 11, 1, 'shangpinxinxi', '明前特级西湖龙井', 'upload/shangpinxinxi_shangpintupian1.jpg', '1', '', NULL);
INSERT INTO `storeup` VALUES (2, '2026-09-25 15:02:00', 12, 1, 'jiaoxueshipin', '中国茶文化简史：从神农尝百草到茶马古道', 'upload/forum_cover1.jpg', '1', '', NULL);
INSERT INTO `storeup` VALUES (3, '2026-09-26 09:20:00', 13, 6, 'shangpinxinxi', '福鼎白毫银针', 'upload/shangpinxinxi_shangpintupian6.jpg', '1', '', NULL);
-- ----------------------------

-- ----------------------------
-- Table structure for syslog
-- ----------------------------
DROP TABLE IF EXISTS `syslog`;
CREATE TABLE `syslog`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `username` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `operation` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户操作',
  `method` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '请求方法',
  `params` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '请求参数',
  `time` bigint(20) NULL DEFAULT NULL COMMENT '请求时长(毫秒)',
  `ip` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'IP地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of syslog
-- ----------------------------
INSERT INTO `syslog` VALUES (1, '2025-01-15 14:05:13', '用户账号8', '新增收藏表', 'com.controller.StoreupController.add()', '{\"id\":1,\"userid\":18,\"refid\":51,\"tablename\":\"jiaoxueshipin\",\"name\":\"11\",\"picture\":\"upload/1736920792786.jpg\",\"type\":\"1\",\"inteltype\":\"\"}', 5, '127.0.0.1');

-- ----------------------------
-- Table structure for token
-- ----------------------------
DROP TABLE IF EXISTS `token`;
CREATE TABLE `token`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userid` bigint(20) NOT NULL COMMENT '用户id',
  `username` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `tablename` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表名',
  `role` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色',
  `token` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  `expiratedtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '过期时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'token表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of token
-- ----------------------------
INSERT INTO `token` VALUES (1, 1, 'admin', 'users', '管理员', 'm38y21qavxdjxz59btyoj3n7jeobprr9', '2025-01-15 13:12:51', '2025-01-15 15:52:18');
INSERT INTO `token` VALUES (2, 18, '用户账号8', 'yonghu', '用户', 'pm2szzr0tg4mj9h1hhlp1beqqnnf0vqj', '2025-01-15 13:55:12', '2025-01-15 16:18:46');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `image` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像',
  `role` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '管理员' COMMENT '角色',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '管理员表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, 'admin', 'admin', 'upload/image1.jpg', '管理员', '2025-01-14 09:08:13');

-- ----------------------------
-- Table structure for xinlizixun
-- ----------------------------
DROP TABLE IF EXISTS `xinlizixun`;
CREATE TABLE `xinlizixun`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `zixunmingcheng` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '咨询名称',
  `zixunfenlei` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '咨询分类',
  `fengmiantupian` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '封面图片',
  `zixunfeiyong` double NULL DEFAULT NULL COMMENT '咨询费用',
  `kaifangshijian` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '开放时间',
  `zixunjieshao` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '咨询介绍',
  `zixunxiangqing` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '咨询详情',
  `zixunshizhanghao` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '咨询师账号',
  `zixunshixingming` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '咨询师姓名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '心理咨询' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of xinlizixun
INSERT INTO `xinlizixun` VALUES (1, '2026-08-15 09:00:00', '绿茶冲泡实战：水温、投茶量与出汤', '茶艺入门', 'upload/art_c1.jpg', 39, '周三 19:30-21:00', '从三投法到出汤计时，两小时带走一套不苦涩的玻璃杯方案。｜主讲：茶艺师 林清和', '<p>从三投法到出汤计时，两小时带走一套不苦涩的玻璃杯方案。</p>', '商家账号1', '云雾茶庄');
INSERT INTO `xinlizixun` VALUES (2, '2026-08-15 09:00:00', '一泡看懂乌龙茶：从铁观音到单丛', '进阶品鉴', 'upload/art_c2.jpg', 59, '周六 14:00-16:00', '盲品四款乌龙，读懂"观音韵"与"山韵"的区别。｜主讲：评茶员 周茗月', '<p>盲品四款乌龙，读懂"观音韵"与"山韵"的区别。</p>', '商家账号2', '清心茶舍');
INSERT INTO `xinlizixun` VALUES (3, '2026-08-15 09:00:00', '普洱生熟之辨与仓储入门', '进阶品鉴', 'upload/art_c3.jpg', 49, '周日 10:00-12:00', '同一山头生熟对冲，现场拆解仓储年份茶的判别要点。｜主讲：茶艺师 古滇南', '<p>同一山头生熟对冲，现场拆解仓储年份茶的判别要点。</p>', '商家账号4', '澜沧古茶坊');
INSERT INTO `xinlizixun` VALUES (4, '2026-08-15 09:00:00', '白茶工艺与年份茶鉴别', '茶类通识', 'upload/art_c4.jpg', 45, '周五 19:30-21:00', '日光萎凋现场解析，三年陈与新茶对比开汤。｜主讲：评茶员 白鼎生', '<p>日光萎凋现场解析，三年陈与新茶对比开汤。</p>', '商家账号5', '福鼎白茶行');
INSERT INTO `xinlizixun` VALUES (5, '2026-08-15 09:00:00', '红茶调饮创意工作坊', '茶艺入门', 'upload/art_c5.jpg', 29, '周日 15:00-16:30', '正山小种做奶茶、祁红调果饮，附家庭调饮配方手册。｜主讲：茶艺师 祁门红', '<p>正山小种做奶茶、祁红调果饮，附家庭调饮配方手册。</p>', '商家账号7', '凤凰单丛铺');
INSERT INTO `xinlizixun` VALUES (6, '2026-08-15 09:00:00', '宋代点茶体验课', '茶史美学', 'upload/art_c6.jpg', 69, '周六 10:00-12:00', '碾茶、候汤、击拂，复刻一盏宋人手中的雪沫乳花。｜主讲：茶艺师 沈宋雅', '<p>碾茶、候汤、击拂，复刻一盏宋人手中的雪沫乳花。</p>', '商家账号3', '武夷星茶业');
INSERT INTO `xinlizixun` VALUES (7, '2026-08-15 09:00:00', '亲子茶艺：给孩子的一堂茶课', '亲子茶课', 'upload/art_c7.jpg', 25, '周日 09:30-11:00', '奉茶礼仪+趣味识茶，适合 6-12 岁小朋友与家长共同参与。｜主讲：茶艺师 程雨前', '<p>奉茶礼仪+趣味识茶，适合 6-12 岁小朋友与家长共同参与。</p>', '商家账号6', '祁门红茶庄');
INSERT INTO `xinlizixun` VALUES (8, '2026-08-15 09:00:00', '盖碗与紫砂：器具选择公开课', '茶器入门', 'upload/art_c8.jpg', 0, '周三 20:00-21:00', '免费公开课：什么阶段该买什么壶，一件不浪费的购器清单。｜主讲：茶器主播 陶紫砂', '<p>免费公开课：什么阶段该买什么壶，一件不浪费的购器清单。</p>', '商家账号8', '紫砂茶器阁');
-- ----------------------------

-- ----------------------------
-- Table structure for yonghu
-- ----------------------------
DROP TABLE IF EXISTS `yonghu`;
CREATE TABLE `yonghu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `yonghuzhanghao` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户账号',
  `mima` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `yonghuxingming` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户姓名',
  `xingbie` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '性别',
  `nianling` int(11) NOT NULL COMMENT '年龄',
  `lianxifangshi` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系方式',
  `touxiang` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '头像',
  `shenfenzhenghao` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '身份证号',
  `youxiang` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `money` double NULL DEFAULT 0 COMMENT '余额',
  `status` int(11) NULL DEFAULT 0 COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `yonghuzhanghao`(`yonghuzhanghao`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of yonghu
INSERT INTO `yonghu` VALUES (11, '2026-08-15 09:00:00', '用户账号1', 'e10adc3949ba59abbe56e057f20f883e', '林清欢', '女', 20, '13823888881', 'upload/yonghu_touxiang1.jpg', '440300199101010001', '773890001@qq.com', 200, 0);
INSERT INTO `yonghu` VALUES (12, '2026-08-15 09:00:00', '用户账号2', 'e10adc3949ba59abbe56e057f20f883e', '苏小满', '男', 23, '13823888882', 'upload/yonghu_touxiang2.jpg', '440300199102010002', '773890002@qq.com', 200, 0);
INSERT INTO `yonghu` VALUES (13, '2026-08-15 09:00:00', '用户账号3', 'e10adc3949ba59abbe56e057f20f883e', '陆知茶', '女', 26, '13823888883', 'upload/yonghu_touxiang3.jpg', '440300199103010003', '773890003@qq.com', 200, 0);
INSERT INTO `yonghu` VALUES (14, '2026-08-15 09:00:00', '用户账号4', 'e10adc3949ba59abbe56e057f20f883e', '沈一盏', '男', 29, '13823888884', 'upload/yonghu_touxiang4.jpg', '440300199104010004', '773890004@qq.com', 200, 0);
INSERT INTO `yonghu` VALUES (15, '2026-08-15 09:00:00', '用户账号5', 'e10adc3949ba59abbe56e057f20f883e', '顾山青', '女', 32, '13823888885', 'upload/yonghu_touxiang5.jpg', '440300199105010005', '773890005@qq.com', 200, 0);
INSERT INTO `yonghu` VALUES (16, '2026-08-15 09:00:00', '用户账号6', 'e10adc3949ba59abbe56e057f20f883e', '程雨前', '男', 35, '13823888886', 'upload/yonghu_touxiang6.jpg', '440300199106010006', '773890006@qq.com', 200, 0);
INSERT INTO `yonghu` VALUES (17, '2026-08-15 09:00:00', '用户账号7', 'e10adc3949ba59abbe56e057f20f883e', '叶知秋', '女', 38, '13823888887', 'upload/yonghu_touxiang7.jpg', '440300199107010007', '773890007@qq.com', 200, 0);
INSERT INTO `yonghu` VALUES (18, '2026-08-15 09:00:00', '用户账号8', 'e10adc3949ba59abbe56e057f20f883e', '何小茶', '男', 41, '13823888888', 'upload/yonghu_touxiang8.jpg', '440300199108010008', '773890008@qq.com', 200, 0);
-- ----------------------------

-- ----------------------------
-- Table structure for yuyuezixun
-- ----------------------------
DROP TABLE IF EXISTS `yuyuezixun`;
CREATE TABLE `yuyuezixun`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `zixunmingcheng` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '咨询名称',
  `zixunfenlei` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '咨询分类',
  `fengmiantupian` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '封面图片',
  `yuyueshijian` datetime NULL DEFAULT NULL COMMENT '预约时间',
  `zixunfeiyong` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '咨询费用',
  `xuqiumiaoshu` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '需求描述',
  `yuyuexiangqing` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '预约详情',
  `yonghuzhanghao` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户账号',
  `yonghuxingming` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户姓名',
  `zixunshizhanghao` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '咨询师账号',
  `zixunshixingming` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '咨询师姓名',
  `ispay` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '未支付' COMMENT '是否支付',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '预约咨询' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of yuyuezixun
INSERT INTO `yuyuezixun` VALUES (1, '2026-09-22 10:00:00', '绿茶冲泡实战：水温、投茶量与出汤', '茶艺入门', 'upload/forum_cover1.jpg', '2026-10-08 19:30:00', '39', '想系统学盖碗冲泡', '<p>想系统学习绿茶冲泡，带问题来。</p>', '用户账号1', '林清欢', '商家账号1', '云雾茶庄', '已支付');
INSERT INTO `yuyuezixun` VALUES (2, '2026-09-24 20:12:00', '宋代点茶体验课', '茶史美学', 'upload/forum_cover6.jpg', '2026-10-12 10:00:00', '69', '和朋友一起约的点茶课', '<p>第一次体验点茶，期待！</p>', '用户账号2', '苏小满', '商家账号3', '武夷星茶业', '未支付');
INSERT INTO `yuyuezixun` VALUES (3, '2026-09-25 09:40:00', '亲子茶艺：给孩子的一堂茶课', '亲子茶课', 'upload/forum_cover7.jpg', '2026-10-11 09:30:00', '25', '孩子六岁，想培养兴趣', '<p>希望孩子学学奉茶礼仪。</p>', '用户账号3', '陆知茶', '商家账号6', '祁门红茶庄', '已支付');
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------
-- Table structure for ai_knowledge（茶道AI知识库）
-- ----------------------------
DROP TABLE IF EXISTS `ai_knowledge`;
CREATE TABLE `ai_knowledge` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `source_type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '来源类型: tea/news/article/lecture',
  `source_id` bigint NOT NULL COMMENT '来源表主键',
  `title` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `content` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_type` (`source_type`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='茶道AI知识库';

-- ----------------------------
-- Records of ai_knowledge（启动时由 TeaRagService.autoSeed 自动重建，无需手工数据）
-- ----------------------------

-- ----------------------------
-- 内容配图改用真实茶摄影（6 张轮换）
-- 实体位置：src/main/resources/static/upload/photo_1..6.jpg
-- 说明：原 art_*.jpg 为金线线稿风插画，此处统一替换为实拍照片
-- ----------------------------
UPDATE `jiaoxueshipin` SET `fengmian`       = CONCAT('upload/photo_', ((id-1)%6)+1, '.jpg');
UPDATE `shangpinxinxi` SET `shangpintupian` = CONCAT('upload/photo_', ((id-1)%6)+1, '.jpg');
UPDATE `xinlizixun`    SET `fengmiantupian` = CONCAT('upload/photo_', ((id-1)%6)+1, '.jpg');
UPDATE `news`          SET `picture`        = CONCAT('upload/photo_', ((id-1)%6)+1, '.jpg');
