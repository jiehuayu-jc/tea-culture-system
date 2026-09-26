# 茶文化管理系统

一个"内容 + 电商"的茶文化主题系统：用户逛**好茶集市**买茶、看**茶文化**栏目、逛**茶友圈**社区、参加**茶艺讲座**、与**智能 AI** 问答；茶商管理商品；管理员统筹后台。

## 技术栈

| 层 | 技术 |
|---|---|
| 后端 | Spring Boot 2.2.2 · MyBatis-Plus 2.3 · MySQL · WebSocket · 百度千帆 AI |
| 管理端前端 | Vue 2.7 + ElementUI（`src/main/resources/admin/admin`，开发端口 8081） |
| 客户端前端 | Vue 2.6 + ElementUI（`src/main/resources/front/front`，开发端口 8082） |
| 认证 | Token 存库 + 拦截器校验；密码 BCrypt（兼容历史 MD5，登录时自动升级） |

## 启动步骤

1. **数据库**（三选一）：
   - 本机 MySQL：执行 `db/init-database.bat`（重置并导入 `db/springbootj8kskvkr.sql`，root 密码默认 123456）
   - Docker：项目根目录 `docker compose up -d`（首次启动自动导入 SQL）
2. **后端**：双击 `run.bat`（或 IDE 直接运行 `com.SpringBootTestSchemaApplication`），端口 8080，context-path `/springbootj8kskvkr`
3. **前端**（开发模式）：`src/main/resources/admin/admin` 下先 `1-install.bat` 再 `2-run.bat`；客户端在 `src/main/resources/front/front` 下执行 `run.bat`

## 演示账号（密码均为 `123456`）

| 角色 | 账号 | 说明 |
|---|---|---|
| 管理员 | admin | 后台管理 |
| 用户 | 用户账号1 ~ 用户账号8 | 前台演示 |
| 茶商 | 商家账号1 ~ 商家账号8 | 商品维护 |

> 老库中历史密码为 MD5 时依然可登录，登录成功后自动升级为 BCrypt 存储。

## 模块对照表

| 数据表 | 业务名称 |
|---|---|
| shangpinxinxi / cart / orders / address / coupon | 好茶集市（茶叶买卖） |
| jiaoxueshipin | 茶文化（内容栏目） |
| forum / forumtype / forumreport | 茶友圈 |
| news / newstype | 购物资讯 |
| xinlizixun / yuyuezixun | 线上讲座 / 讲座预约 |
| chat / chatmessage / friend | 智能 AI / 私聊 |
| shangjia | 茶商 |
| yonghu / users | 用户 / 管理员 |

## 2026-09 整改记录

安全类：
- 收紧 `@IgnoreAuth`：移除 100 处匿名放行（含购物车/订单/地址等私有数据的匿名读写与匿名 `/delete`），仅保留登录注册、游客内容浏览、统计与管理端接口
- 删除 19 个控制器中损坏且无调用的 `/security` 接口（空列名查询必报错）
- 拦截器移除"URI 包含即放行"宽松兜底；CORS 不再回显任意 Origin，改为本机端口白名单
- 文件下载修复 `../` 路径穿越；上传增加后缀白名单
- `/common/mysqldump` 增加管理员角色门禁
- fastjson 1.2.8 → 1.2.83（消除历史反序列化漏洞）
- 密码改用 BCrypt（带随机盐），注册/改密走新哈希，历史 MD5/明文登录后透明升级；用户与茶商实体的密码字段不再随 JSON 返回

工程类：
- 移除零引用依赖 shiro-spring、commons-math3
- 种子数据全部替换为真实茶叶主题内容（商品、茶文化、资讯、茶友圈、讲座）
- 管理端主题色与客户端首页按新中式茶美学风格重做

## 已知限制

- 管理员"忘记密码"（`users/resetPass`）为匿名接口，可被调用将任意管理员密码重置为 123456，正式部署前应加验证机制
- 支付为模拟流程，无真实支付网关
- AI 问答依赖百度千帆 API Key（`BaiduUtil`），未配置时该功能不可用
