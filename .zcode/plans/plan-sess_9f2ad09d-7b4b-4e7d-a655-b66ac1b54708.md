# 茶文化管理系统整改计划（修订版：高星参考 + 赛事级 UI）

## 全程红线（保护健身房小程序项目）
- 只改 `C:\Users\14483\Desktop\springbootj8kskvkr` 目录；只操作 MySQL 中 `springbootj8kskvkr` 一个库
- 绝不触碰：`Desktop/gym-system`、`Desktop/mini-program`、`gym_system` 库、8001 端口、MySQL root 密码与全局配置
- 保留端口 8080；每阶段结束项目保持可编译、可启动

## 阶段 0：回滚保障
- 项目内 `git init` + `.gitignore`（排除 node_modules/target/maven.zip/.idea/dist）+ 初始提交（仅本地）
- mysqldump 仅导出 springbootj8kskvkr 库 → `db/backup/initial.sql`

## 阶段 1：后端安全与 bug 修复
1. 修复 `ChatController.security` 空列名 bug
2. 收紧 `@IgnoreAuth`（移除 update/detail/query 上的匿名放行）
3. 拦截器去掉"URI 包含即放行"宽松兜底；CORS Origin 改本机白名单
4. `/common/mysqldump` 加管理员校验；fastjson 1.2.8 → 1.2.83（仅 3 处引用）
5. 密码：注册/改密用 BCrypt（spring-security-crypto），登录兼容旧 MD5

## 阶段 2：代码瘦身与文档
- 删零引用依赖（shiro-spring、commons-math3、unirest-java，删前逐一 grep 确认）；清理死代码；补 README

## 阶段 3：数据真实化（茶叶主题）
- 表结构不动，只重写种子数据：真实茶叶商品 12~16 个（龙井/碧螺春/铁观音/大红袍/普洱+茶具）、茶文化栏目、购物资讯、茶友圈帖子、茶艺讲座、优惠券示例
- 图片沿用现有占位图保证离线可演示；SQL 规范参照 litemall（20.4k★）的 db 组织方式

## 阶段 4：前端 UI 美化（对标设计赛事获奖作品，新中式茶美学）
**风格定位**（依据 NCDA 未来设计师大赛获奖作品《让年轻人着迷的新中式茶空间》、A' Design Award 铜奖 Chillwild 茶品牌、霸王茶姬"极简国际化东方美学"品牌体系）：
- 配色 tokens：宣纸米色底（#F5F1E8 系）+ 墨黑正文 + 茶绿/茶褐主色 + 朱砂红印章色点缀（替换现在突兀的荧光绿 #00c292）
- 字体：标题宋体衬线 + 正文无衬线的层级体系；留白加大、卡片化
- 传统元素意象：印章式标签、圆窗/满月形轮播（呼应 G-Mark 获奖茶壶的满月提梁意象）
**落点**：
- 管理端：element-variables.scss 一处改主题色 + 品牌文案统一
- 客户端：config.js 去掉写死的外网 IP 121.41.237.212；home.vue 首页重排（轮播 Banner + 栏目入口卡片化 + 新配色字体）；好茶集市/茶文化关键列表页统一视觉
- 不升级 Vue2/ElementUI，不做全站 40 页换肤

## 阶段 5：验证与交付
- 后端 mvnw 编译+启动冒烟；SQL 一键导入验证；两端 npm run build 通过
- 浏览器实测截图验收；交付整改报告 + 演示账号卡

## 参考名单（只取高星，只抄设计不搬全家桶）
- macrozheng/mall（84.8k★）：BCrypt 认证、优惠券模块设计
- linlinjava/litemall（20.4k★）：双前端工程组织、SQL 种子规范
- newbee-ltd/newbee-mall（11.6k★）：订单状态机、模拟支付流程
- 不再参考 638★ 及以下仓库（yshopmall 及各茶毕设仓已移出名单）

## 执行顺序
0→1→2→3→4→5；后端与数据先行，UI 最后承载新数据。预计后端约 8 个文件、SQL 1 个、前端约 10 个文件。