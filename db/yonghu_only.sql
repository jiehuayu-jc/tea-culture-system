-- 仅补「用户」表与示例数据，用于大脚本导入中断时先恢复前台登录（用户账号1 / 123456）
-- 在 Navicat 中：选中库 springbootj8kskvkr → 新建查询 → 粘贴本文件全部内容 → 运行

SET NAMES utf8mb4;

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

INSERT INTO `yonghu` VALUES (11, '2025-01-14 09:08:13', '用户账号1', 'e10adc3949ba59abbe56e057f20f883e', '用户姓名1', '男', 1, '13823888881', 'upload/yonghu_touxiang1.jpg', '440300199101010001', '773890001@qq.com', 200, 0);
INSERT INTO `yonghu` VALUES (12, '2025-01-14 09:08:13', '用户账号2', 'e10adc3949ba59abbe56e057f20f883e', '用户姓名2', '男', 2, '13823888882', 'upload/yonghu_touxiang2.jpg', '440300199202020002', '773890002@qq.com', 200, 0);
INSERT INTO `yonghu` VALUES (13, '2025-01-14 09:08:13', '用户账号3', 'e10adc3949ba59abbe56e057f20f883e', '用户姓名3', '男', 3, '13823888883', 'upload/yonghu_touxiang3.jpg', '440300199303030003', '773890003@qq.com', 200, 0);
INSERT INTO `yonghu` VALUES (14, '2025-01-14 09:08:13', '用户账号4', 'e10adc3949ba59abbe56e057f20f883e', '用户姓名4', '男', 4, '13823888884', 'upload/yonghu_touxiang4.jpg', '440300199404040004', '773890004@qq.com', 200, 0);
INSERT INTO `yonghu` VALUES (15, '2025-01-14 09:08:13', '用户账号5', 'e10adc3949ba59abbe56e057f20f883e', '用户姓名5', '男', 5, '13823888885', 'upload/yonghu_touxiang5.jpg', '440300199505050005', '773890005@qq.com', 200, 0);
INSERT INTO `yonghu` VALUES (16, '2025-01-14 09:08:13', '用户账号6', 'e10adc3949ba59abbe56e057f20f883e', '用户姓名6', '男', 6, '13823888886', 'upload/yonghu_touxiang6.jpg', '440300199606060006', '773890006@qq.com', 200, 0);
INSERT INTO `yonghu` VALUES (17, '2025-01-14 09:08:13', '用户账号7', 'e10adc3949ba59abbe56e057f20f883e', '用户姓名7', '男', 7, '13823888887', 'upload/yonghu_touxiang7.jpg', '440300199707070007', '773890007@qq.com', 200, 0);
INSERT INTO `yonghu` VALUES (18, '2025-01-14 09:08:13', '用户账号8', 'e10adc3949ba59abbe56e057f20f883e', '用户姓名8', '男', 8, '13823888888', 'upload/yonghu_touxiang8.jpg', '440300199808080008', '773890008@qq.com', 200, 0);
