# -*- coding: utf-8 -*-
"""生成传智杯项目技术文档 Word 模板（预填 + 占位标记分色）"""
from docx import Document
from docx.shared import Pt, RGBColor, Cm
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.oxml.ns import qn
from docx.oxml import OxmlElement

GOLD = RGBColor(0x8A, 0x6D, 0x1F)
DARK = RGBColor(0x1A, 0x2E, 0x22)
GRAY = RGBColor(0x66, 0x66, 0x66)
RED = RGBColor(0xC0, 0x39, 0x2B)
GREEN = RGBColor(0x2E, 0x5E, 0x3E)

def set_cn(run, name="微软雅黑", size=10.5, color=None, bold=False):
    run.font.name = "Calibri"
    run._element.rPr.rFonts.set(qn("w:eastAsia"), name)
    run.font.size = Pt(size)
    run.font.bold = bold
    if color is not None:
        run.font.color.rgb = color

def para(doc, text, size=10.5, color=DARK, bold=False, align=None, space_after=6):
    p = doc.add_paragraph()
    if align:
        p.alignment = align
    p.paragraph_format.space_after = Pt(space_after)
    r = p.add_run(text)
    set_cn(r, size=size, color=color, bold=bold)
    return p

def guide(doc, text):
    """红色写作指引，交稿前删除"""
    para(doc, "⟪" + text + "⟫", size=9, color=RED, space_after=8)

def ph(doc, text):
    """占位符"""
    para(doc, "【占位】" + text, size=10.5, color=RED, space_after=8)

def h1(doc, text):
    h = doc.add_heading(level=1)
    r = h.add_run(text)
    set_cn(r, name="微软雅黑", size=16, color=DARK, bold=True)
    h.paragraph_format.space_before = Pt(18)

def h2(doc, text):
    h = doc.add_heading(level=2)
    r = h.add_run(text)
    set_cn(r, name="微软雅黑", size=13, color=GREEN, bold=True)

def shade(cell, hexcolor):
    tcPr = cell._tc.get_or_add_tcPr()
    shd = OxmlElement("w:shd")
    shd.set(qn("w:val"), "clear")
    shd.set(qn("w:fill"), hexcolor)
    tcPr.append(shd)

def table(doc, headers, rows, widths=None):
    t = doc.add_table(rows=1, cols=len(headers))
    t.style = "Table Grid"
    for i, h in enumerate(headers):
        c = t.rows[0].cells[i]
        c.text = ""
        r = c.paragraphs[0].add_run(h)
        set_cn(r, size=10, color=RGBColor(0xFF, 0xFF, 0xFF), bold=True)
        shade(c, "2E5E3E")
    for row in rows:
        cells = t.add_row().cells
        for i, v in enumerate(row):
            cells[i].text = ""
            r = cells[i].paragraphs[0].add_run(str(v))
            set_cn(r, size=10)
    doc.add_paragraph().paragraph_format.space_after = Pt(4)
    return t

doc = Document()
# 页边距
for s in doc.sections:
    s.left_margin = Cm(2.2); s.right_margin = Cm(2.2)
    s.top_margin = Cm(2.2); s.bottom_margin = Cm(2.2)

# ============ 使用说明 ============
para(doc, "项目技术文档 · 填写模板（第九届传智杯 · AI WEB网页开发挑战赛）", size=15, color=DARK, bold=True, align=WD_ALIGN_PARAGRAPH.CENTER)
para(doc, "红色文字 = 写作指引或占位符，按提示补齐后整段删除；黑色文字 = 已按项目实际预填，可直接保留或润色。全文导出 PDF 后不超过 30 页；架构图/流程图用 draw.io 绘制导出 PNG 插入，禁止代码截图凑图。所有数字与 docs/eval-200.json 公网实测一致。", size=9, color=GRAY, space_after=12)

# ============ 封面 ============
for _ in range(4):
    doc.add_paragraph()
para(doc, "茶文化管理系统", size=26, color=DARK, bold=True, align=WD_ALIGN_PARAGRAPH.CENTER)
para(doc, "基于混合检索 RAG 与 ReAct Agent 的茶文化智能服务平台", size=13, color=GOLD, align=WD_ALIGN_PARAGRAPH.CENTER, space_after=24)
for line in [
    "参赛赛道：第九届传智杯全国大学生数智创新与AI应用大赛 · AI WEB网页开发挑战赛（团队赛）",
    "参赛队伍：【占位：队名】　　学校：【占位】　　指导教师：【占位】",
    "团队成员：【占位：姓名 × 3】",
    "代码仓库：https://github.com/jiehuayu-jc/tea-culture-system",
    "在线演示：http://124.220.4.136:8090/springbootj8kskvkr/front/index.html",
]:
    para(doc, line, size=11, align=WD_ALIGN_PARAGRAPH.CENTER, space_after=8)
doc.add_page_break()

# ============ 摘要 ============
h1(doc, "摘要")
para(doc, "茶文化管理系统是一个“内容 + 电商 + AI”三位一体的 Web 平台：用户浏览茶文化栏目、购买好茶、参与茶友社区，并通过“茶道AI”获得基于站内知识库的智能问答、个性化荐茶与订单查询。系统将大模型能力深度融入检索、决策与数据分析三个层面：自研两阶段混合检索 RAG（BM25 稀疏召回 + 稠密向量 RRF 融合 + LLM 语义重排），回答附带可跳转的站内引用；ReAct 工具调用 Agent 打通“问答—查单—推荐—加购”业务闭环；自然语言查数让运营以中文提问直接获得统计图表。全部 AI 能力在无网络、无密钥环境下自动降级为本地链路，保证演示与评审的可靠性。")

# ============ 一、作品概述 ============
h1(doc, "一、作品概述")
h2(doc, "1.1 作品定位")
guide(doc, "一句话定位：面向茶文化领域的内容+电商+AI 综合平台，三类角色（用户/茶商/管理员）各取所需。配一张三角色-场景示意图。")
h2(doc, "1.2 与传统方案的对比")
table(doc, ["维度", "传统茶类商城", "本作品"], [
    ["咨询", "人工客服，响应慢", "茶道AI：知识库问答，秒级响应、带引用溯源"],
    ["导购", "分类筛选，用户自己找", "Agent 理解偏好、查历史订单后个性化推荐，可直接加购"],
    ["运营分析", "需要开发写 SQL 出报表", "自然语言查数：中文提问直接出统计图"],
    ["知识沉淀", "散落在文章里，无法复用", "自动建库 + 向量化，成为 AI 的领域知识库"],
    ["可靠性", "依赖外部服务", "无外网/无密钥自动降级，功能链路不中断"],
])
h2(doc, "1.3 功能全景")
guide(doc, "功能清单表（模块/角色/一句话说明），直接引用 README 的“模块对照表”：好茶集市、茶文化、茶友圈、购物资讯、线上讲座、留言板、茶道AI、购物车/订单/优惠券、三端管理后台（25 个管理模块）。")

# ============ 二、需求与功能设计 ============
h1(doc, "二、需求与功能设计")
h2(doc, "2.1 用例图")
ph(doc, "draw.io 绘制：三类角色 × 核心用例（浏览/下单/提问/管理），导出 PNG 插入")
h2(doc, "2.2 核心流程时序图（只展开两个，其余从简）")
para(doc, "流程一：茶道AI 一次 RAG 问答的全链路", bold=True)
ph(doc, "时序图：用户提问 → 意图理解 → BM25 召回 ∥ 稠密向量召回 → RRF 融合 → LLM 语义重排 → 拼装带引用的 Prompt → DeepSeek 流式生成 → 前端打字机渲染 + 引用卡跳转")
para(doc, "流程二：Agent 推荐下单闭环", bold=True)
ph(doc, "时序图：用户提问 → Agent 规划 → 调用“查订单”工具 → 调用“推荐商品”工具 → 结果卡片 → 用户点击加购 → 订单落库")
h2(doc, "2.3 功能清单")
guide(doc, "表格：模块 × 角色 × 说明，直接引用 README 模块对照表（16 行），不再展开细节。")

# ============ 三、系统架构 ============
h1(doc, "三、系统架构")
h2(doc, "3.1 总体架构图")
ph(doc, "分层图：客户端（Vue2 + ElementUI）／管理端（手写静态页 + classpath 自动映射）／Spring Boot 2.2（Controller-Service-DAO）／MySQL 8.0 ＋ 外部服务（DeepSeek 对话、DashScope 向量）")
h2(doc, "3.2 技术选型及理由")
table(doc, ["层", "技术", "为什么选它"], [
    ["后端框架", "Spring Boot 2.2.2 + MyBatis-Plus", "教学基座成熟稳定，团队可聚焦 AI 层创新"],
    ["大模型", "DeepSeek（OpenAI 兼容协议）", "自研轻量客户端（多轮对话/Function Calling/SSE 流式），不引入重框架"],
    ["向量", "DashScope text-embedding-v3", "通用文本向量，中文语义检索效果好"],
    ["前端", "Vue2 + ElementUI", "基座一致，组件生态完整"],
])
h2(doc, "3.3 工程基座说明（诚实声明）")
para(doc, "本系统基于通用教学脚手架二次开发。本团队的原创工作集中在：AI 服务层（RAG 检索引擎 / ReAct Agent / NLQ 查数 / 评测体系）、管理端界面重构（手写静态页 + classpath 自动映射）与安全整改（12 项，见 6.2 节，均有 Git 提交可查证）。")

# ============ 四、AI 能力设计 ============
h1(doc, "四、AI 能力设计（全文重点）")
h2(doc, "4.1 混合检索 RAG")
ph(doc, "流水线图：问题 → BM25 稀疏召回 ∥ DashScope 稠密向量召回 → RRF 融合（k=60）→ LLM 语义重排（剔除不相关）→ 带引用 Prompt → 流式回答")
para(doc, "正文要点：①为什么单一检索不够——关键词命中与语义近义互补，例如“新手喝什么茶不容易失眠”字面不含“绿茶/助眠”，纯关键词检索召回困难；②知识库构成 = 站内内容自动收割（40 条）+ 领域种子库（160 条，覆盖六大茶类、名茶、冲泡、茶史、茶器、储存、饮忌、茶俗、品鉴、术语、山场）；③文档向量缓存与启动预热，避免首次请求卡顿；④引用溯源：回答标注来源条目，前端卡片可跳原文。核心代码：TeaRagService.java、EmbeddingProvider.java。")
h2(doc, "4.2 ReAct Agent 业务闭环")
guide(doc, "工具清单表（查订单/推荐商品/检索知识/泡茶指南/推荐讲座）；真实多轮调用日志一段（脱敏）；强调模型自主规划调用次数与检索词改写、前端“思考→调用→结果”时间线可视化、推荐结果直接加购；防失控：工具白名单 + 服务端参数校验 + 轮数上限。代码：TeaAgentService.java")
h2(doc, "4.3 多模态识茶")
guide(doc, "图片上传后进入视觉理解，与文本同处一条 Agent 决策链路（“看图提问”而非独立看图工具）；配演示截图：上传干茶照片 → 识别茶类 → 推荐冲泡参数。")
h2(doc, "4.4 自然语言查数 NLQ")
para(doc, "流程：中文提问 → LLM 意图解析（四类白名单枚举：商品销量/商品浏览/订单状态/分类销量）→ 服务端受限 SQL 模板 → 条形图渲染。安全设计：模型输出被约束为枚举意图而非 SQL 文本，参数化传值，杜绝注入；解析失败有关键词兜底，不会白屏。代码：AiController#nlq。")
h2(doc, "4.5 降级设计")
para(doc, "无 DeepSeek Key / 无外网时：RAG 降为单路 BM25、Agent 降为本地规则推荐、状态灯提示“离线演示模式”——为评审环境专门设计，保证功能链路完整可演示。")

# ============ 五、AI 质量评测 ============
h1(doc, "五、AI 质量评测")
h2(doc, "5.1 评测集")
para(doc, "70 题茶文化评测集，覆盖全部知识库来源（领域种子库 160 条 + 站内内容 40 条）；问题刻意口语化改写、不与知识标题字面重合，专测语义召回能力。")
h2(doc, "5.2 消融实验结果（公网服务器实测，知识库 200 条）")
table(doc, ["检索策略", "Recall@3", "MRR", "命中"], [
    ["A 单路 BM25", "0.643", "0.548", "45/70"],
    ["B 双路 RRF（BM25 + 向量）", "0.814", "0.683", "57/70"],
    ["C 双路 + LLM 语义重排", "0.914", "0.819", "64/70"],
])
para(doc, "结论：混合检索相对纯稀疏召回提升 17.1 个百分点，语义重排再提升 10 个百分点；MRR 0.819 表明正确答案绝大多数排在第一位。原始数据见仓库 docs/eval-200.json，管理台“RAG 评测”页可现场复跑。", space_after=4)
ph(doc, "插入管理端评测页截图一张（RAG 评测页三策略对比表）")
h2(doc, "5.3 已知限制")
para(doc, "评测集与语料同源（覆盖式测试），独立外部题集在计划中；语料 200 条属演示规模，扩容无需修改代码。")

# ============ 六、数据与安全设计 ============
h1(doc, "六、数据与安全设计")
h2(doc, "6.1 数据设计")
ph(doc, "核心 ER 图：users / yonghu / shangjia / shangpinxinxi / orders / ai_knowledge 六表关系")
h2(doc, "6.2 安全整改清单（每条均有 Git 提交可查证）")
table(doc, ["整改项", "说明"], [
    ["收紧 @IgnoreAuth", "移除 100 处匿名放行（含私有数据匿名读写与匿名删除）"],
    ["密码 BCrypt 化", "注册/改密走 BCrypt 带盐哈希，历史 MD5 登录后透明升级；密码字段不再随 JSON 返回"],
    ["匿名重置封堵", "/resetPass 移出匿名白名单（公网部署前整改）"],
    ["SQL 注入防护", "NLQ 意图白名单 + 参数化查询；修复 orders 表列名错误"],
    ["路径穿越修复", "文件下载修复 ../ 穿越；上传增加后缀白名单"],
    ["CORS 收紧", "不再回显任意 Origin，改为端口白名单"],
    ["依赖升级", "fastjson 1.2.8 → 1.2.83（消除历史反序列化漏洞）"],
    ["密钥治理", "移除源码硬编码凭据；API Key 走环境变量/外部配置不入库；历史提交经 git-filter-repo 清洗"],
])
h2(doc, "6.3 公网部署安全")
guide(doc, "说明：独立数据库账号最小授权（仅授权本项目库）、应用以非 root systemd 服务运行、防火墙仅放行 8090 端口。")

# ============ 七、创新点 ============
h1(doc, "七、创新点")
for t in [
    "1. 混合检索 RAG：稀疏 + 稠密双路召回、LLM 语义重排、回答引用可溯源",
    "2. 检索质量可评测：70 题三策略消融，Recall@3 / MRR 可现场复跑",
    "3. ReAct Agent 业务闭环：查订单 → 推荐 → 一键加购，AI 驱动真实业务动作",
    "4. 自然语言查数：意图白名单 + 受限 SQL，运营不写 SQL 出图表",
]:
    para(doc, t, bold=True, space_after=4)
guide(doc, "每条配一段正文 + 一张配图（截图或示意图）。详细口径参考 docs/innovation-points.md：含每条的“一句话卖点、演示画面、评委追问标准答法”。")

# ============ 八、团队分工 ============
h1(doc, "八、团队分工")
table(doc, ["成员", "职责", "核心贡献"], [
    ["【占位：姓名】", "【占位：如 AI 服务层】", "RAG 检索引擎、评测体系"],
    ["【占位：姓名】", "【占位：如 前后端业务】", "订单/商城模块、管理端重构"],
    ["【占位：姓名】", "【占位：如 文档与测试】", "技术文档、评测执行、演示视频"],
])
guide(doc, "成员姓名与 Git 提交作者名对应得上；GitHub 提交历史即过程真实性证据。")

# ============ 九、部署与复现手册 ============
h1(doc, "九、部署与复现手册")
h2(doc, "9.1 三步启动")
para(doc, "① 数据库：执行 db/init-database.bat（或 Docker：docker compose up -d）；② 后端：双击 run.bat（或 IDE 运行 com.TeaCultureApplication），端口 8080；③ 访问：前台 /springbootj8kskvkr/front/index.html，管理端 /springbootj8kskvkr/admin/index.html（账号 admin / admin）。")
h2(doc, "9.2 AI 能力开启")
para(doc, "环境变量 DEEPSEEK_API_KEY（对话）与 DashScope Key（向量，可选）注入；不配置自动进入离线演示模式，功能链路不中断。")
h2(doc, "9.3 常见问题排查")
table(doc, ["现象", "原因", "处理"], [
    ["管理端 404", "使用了旧版本仓库", "拉取最新代码，管理端已随构建自动映射，无需任何手工部署"],
    ["AI 回复提示离线模式", "未配置 API Key", "配置环境变量后重启后端"],
    ["端口占用", "8080 被其他进程占用", "修改 application.yml 中 server.port"],
])

# ============ 十、总结与展望 ============
h1(doc, "十、总结与展望")
h2(doc, "10.1 已完成")
para(doc, "混合检索 RAG 全链路与可复跑的检索评测体系；ReAct Agent 业务闭环与多模态输入；完整的电商、内容与三端管理模块。")
h2(doc, "10.2 诚实的限制")
para(doc, "知识库 200 条属演示规模（扩容无需修改代码）；支付为模拟流程、无真实支付网关；系统基于教学脚手架基座 + 自研 AI 层构成（见 3.3 节诚实声明）。")
h2(doc, "10.3 下一步计划")
para(doc, "引入独立外部评测题集；语料扩容至千条级并接入商品评价数据；在线演示绑定域名并启用 HTTPS。")

out = r"C:\Users\14483\Desktop\传智杯项目技术文档模板.docx"
doc.save(out)
print("saved:", out)
