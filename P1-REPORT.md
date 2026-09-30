# P1 深色主题整改 · 接手报告

接手对象：ZCode 会话 `sess_9f2ad09d`（工作区 `springbootj8kskvkr`，模型 GLM-5.3-Flash，模式 build）
接手时间：2026-09-30 20:35 起

## 一、接手时的现场

**ZCode 卡死的直接原因**：GLM 体验套餐额度耗尽，输入框上方提示「体验套餐可用额度已用完，请升级套餐或等待额度恢复」。

**「后端启动失败 exit code 1」的真实原因**：`MySQL96` 服务处于 Stopped，3306 端口无监听，Spring Boot 连不上库启动即失败。ZCode 全程没有检查数据库状态，只是一遍遍重启后端。

**一个反直觉事实**：20:11:32 启动的后端实例（PID 1400）一直是健康的。ZCode 在 20:3x 又启动一次，撞上自己已经占用的 8080 才报 `Port 8080 was already in use`，然后它开始 `curl` 等一个其实早就就绪的服务。

## 二、ZCode 已完成并提交的工作

| commit | 内容 |
|---|---|
| `dc10073` | P1-1~6：calc 居中收敛为内容令牌、菜单回文档流、字体子集 woff2、`!important 126→6`、色值清扫、卡片 flex 化、轮播数据驱动、面包屑分隔符、SSE complete 修复 |
| `e6296a7` | P1 深色主题落地修正：面包屑升页面英雄带、顶栏夜茶渐变、菜单条可读、特异性体系修正（去 `#app` 误前缀）、轮播指示点金色、聊天滚动改 `nextTick` |
| `34e5166` | 登录页夜茶整页重做 |

todo 清单第 4 项 `P1-3 主题令牌：!important 126→6` 的量化目标确实达成了：App.vue 的 `!important` 从 82 处降到 0，全站从 141 处降到 6 处。

## 三、定位到的错误

### 3.1 核心错误：把「减少 !important」当目标，连必需的一起删了

`dc10073` 之前的换肤层是「全局选择器 + `!important`」，注释明确写着「压过模板内联样式」。改动后换成 `.main-containers .body-containers` 前缀 + 零 `!important`。

用前缀提高特异性来压 **scoped 样式**是对的；但压 **模板内联样式**，任何选择器都赢不了内联（内联优先级 1,0,0,0），只有 `!important` 能赢。

ZCode 在 [App.vue:488](src/main/resources/front/front/src/App.vue) 的注释里自己写下了清零的前提，然后没满足前提就清零了：

> 仅剩的 用于对抗模板生成的内联 style（**外部样式表无法覆盖内联**），清零前提是把模板内联样式抽离到组件样式——规模约 70 文件，单独立项执行

同一文件里注释与实现还自相矛盾：第 498 行标「面包屑条（模板对该类有**内联背景**，保留）」、第 511 行标「分类标签条（模板按钮带**内联色**，保留）」，标注完却把 `!important` 删了。

### 3.2 实际后果（headless Edge 截图复现）

讲座/茶文化列表页：卡片白底黑字，筛选区一条纯白长条 —— 在夜茶深色主题上最破相的一种。

成因：卡片 `class="list-item animation-box"` 的容器带内联 `"background":"#fff"` + `"color":"#000"`；`.select2` / `.detail-preview` 同样带内联 `"background":"#fff"`。这些内联在基线版本被 `!important` 压住，清零后全部露出来。

### 3.3 顺带查到的、ZCode 完全没碰的问题

- **9 个「我的」类页面根容器整页浅底**：`shop-cart`、`shop-order`（list+confirm）、`storeup`、`shop-address`（list+addOrUpdate）、`messages`、`forum/add`、`forum/myForumList` 的第 2 行根容器内联 `"background":"#f6f6f6"` + `"color":"#666"` + `"width":"1400px"`，同时违反主题一致性和 DESIGN.md 的单一宽度基准
- **el-tabs 内容区浅灰** `#F7F5F7`
- **`deploy-front.ps1` 无法在 PowerShell 5.1 运行**：文件是 UTF-8 无 BOM 且含中文，5.1 按 ANSI 解码导致引号配对错乱报 `Unexpected token ')'`，只有 pwsh 7 能跑

## 四、采取的处理

按 DESIGN.md 第 4 条「先用正确的选择器、优先改源头」，**不恢复 `!important`**，直接修正模板内联值（这与 ZCode 已完成的深色化方向一致，是根治）：

- `jiaoxueshipin` / `discussjiaoxueshipin` 的 `list`+`detail`+`add`：卡片改墨绿渐变 `linear-gradient(175deg,#16291f,#122219)`、文字改 `#EDE6D6`、面板改 `rgba(21,42,32,.85)`、`#FFC174` 明黄按钮改朱砂语义色 `#A63D2F`、蓝调阴影改金色
- 9 个「我的」页根容器：`#f6f6f6` 整页浅底改 `none`（交回 `.body-containers` 的深色渐变接管）、`#666` 深灰字改 `#EDE6D6`
- `el-tabs` 的 `#F7F5F7` 改 `rgba(21,42,32,.6)`

提交：`b82c3f1`（16 files changed, 50 insertions, 56 deletions）

## 五、验证

用 headless Edge 对运行中的服务截图做改前/改后对比：

- 改前：讲座列表页卡片白底黑字 + 白色长条
- 改后：卡片恢复墨绿渐变面板 + 米白字 + 金边，筛选区、分页条、面包屑统一深色
- 详情页：`.detail-preview` 面板恢复深绿玻璃质感，标题/字段米白可读

构建产物：`css/app.6ee9ba64.css`，已双写部署到 `src/main/resources/static/front` 与 `target/classes/static/front`。

## 六、环境说明与遗留

**本次为验证临时拉起的环境**（原 MySQL96 服务无法启动，当前 shell 无管理员权限）：

- 用 MySQL 8.0 在 `C:\Users\14483\.mysql-local\` 初始化了独立实例，端口 3306，root 密码 `123456`，已导入 30 张表。未改动 `D:\mysql\Data`（属主 NetworkService，当前用户无权访问）
- 后端：PID 1400，端口 8080，`/ai/status` 返回 200

**遗留问题**（建议单独立项，不建议零散修补）：

1. **RAG 知识库是空的**：`/ai/status` 返回 `knowledge_count: 0`，`ai_knowledge` 表无数据，AI 问答的检索增强无内容可检索。ZCode 最后正是在验收 AI 却没注意到这个信号
2. 3 处 quill 富文本编辑器保留白底，深色化需组件级改造
3. 子元素内联 `#333`（17 处）、`#3B2626`（6 处）需逐个判断所在背景再改，不可盲目批量替换
4. 9 个「我的」页根容器的 `"width":"1400px"` 固定宽度未动（属布局改动，风险高于配色）
5. `deploy-front.ps1` 建议改写为 UTF-8 带 BOM，或显式声明 `#requires -Version 7`
