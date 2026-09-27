# 「茶道 AI」升级计划——对齐传智杯 AI Web 应用赛项

## 对标定位
主打赛项推荐方向 1（AI Agent 智能助手）+ 2（RAG 知识问答），兼顾 6（健康生活·体质荐茶）、8（AI 数据分析）。核心叙事：茶文化内容多、选茶难 → AI 从"问答玩具"升级为"会查库、会调工具、会给结论"的茶顾问，且 AI 结果全部闭环到业务（加购/预约/跳转）。

## 技术选型
- **LLM：DeepSeek**（`deepseek-chat`，OpenAI 兼容协议，支持流式 SSE + Function Calling）。自研轻量客户端（Java 8 HttpURLConnection + SSE 行解析，约 250 行，零新依赖风险）；不引 langchain4j（需 Java 17）/deepseek4j（依赖 Reactor）只作协议参考
- **密钥**：`application.yml` 新增 `ai.deepseek.api-key` 配置位（你提供 key 填入即可，绝不碰健身房项目文件）。**无 key 或断网时自动降级"离线演示模式"**（内置规则回复 + 固定 RAG 答案），答辩现场永不冷场
- **RAG 检索**：内置 BM25（中文二字分词，纯 Java）为主路，可选千帆 Embedding 双路召回；知识库 = 站内茶文化文章 + 资讯 + 商品介绍 + 讲座，新表 `ai_knowledge` 存储
- **UI**：遵循已装高星设计 skill（frontend-design 等）标准，夜茶主题"茶道 AI"全屏页 + 全站右下角悬浮入口

## 实施阶段

### P1 RAG 知识问答（后端）
- 新表 `ai_knowledge`；建库脚本把站内 4 类内容自动入库分块
- `KnowledgeRetriever`（BM25 检索 TOP3）+ `DeepSeekClient`（chat/chatStream/tools）
- API：`POST /ai/rag`（问答 + 引用来源数组）

### P2 前端"茶道 AI"页 + 流式（前端）
- 新页面：对话流、**打字机流式渲染**（SSE）、引用来源卡片（点击跳原文）、右下角悬浮球入口
- 夜茶暗色主题 + 思源宋体，高星 skill 标准执行

### P3 Agent 工具调用（后端 + 前端，闭环核心）
- 5 个工具：`recommend_teas`（查商品库）、`search_knowledge`、`get_brewing_guide`、`get_my_orders`、`recommend_lectures`；ReAct 循环最多 4 轮
- 前端 **Agent 思考时间线**：思考→调用工具→结果→回答 全程可视化
- **闭环**：推荐商品卡片带"加入购物车/立即购买"直连订单接口；引用卡片跳详情；讲座卡片跳预约

### P4 管理端两件套（演示加分）
- 自然语言查数据："上个月哪种茶卖最好"→ 意图解析 → 统计接口 → ECharts 图表（方向 8）
- AI 写手：生成商品介绍/茶文化文章草稿 → 一键入库

### P5 演示与交付
- 一键连通性自测脚本；演示话术脚本（"送长辈什么茶"完整链路 demo）
- README 增"赛项能力对照表"：每条评审要求 ↔ 对应功能与代码位置

## 依赖与红线
- 需要你提供：DeepSeek API key（填入 application.yml，不碰健身房项目）
- 红线延续：只动本项目，内页暗色体系与既有 git 基线延续
- 新增文件预估：后端 6 个（DeepSeekClient/RagService/AgentService/AiController/知识库脚本/配置），前端 4 个（茶道AI页/Agent时间线组件/悬浮球/管理端AI面板），SQL 1 份迁移