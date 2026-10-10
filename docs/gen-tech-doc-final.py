# -*- coding: utf-8 -*-
"""传智杯项目技术文档终稿生成（内容全部预填，仅团队信息留占位）"""
import os
from docx import Document
from docx.shared import Pt, RGBColor, Cm
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.oxml.ns import qn
from docx.oxml import OxmlElement

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
FIGS = os.path.join(ROOT, "docs", "figs")
SHOTS = os.path.join(ROOT, "docs", "screenshots")
OUT = r"C:\Users\14483\Desktop\传智杯项目技术文档-终稿.docx"

GOLD = RGBColor(0x8A, 0x6D, 0x1F)
DARK = RGBColor(0x1A, 0x2E, 0x22)
GRAY = RGBColor(0x66, 0x66, 0x66)
RED = RGBColor(0xC0, 0x39, 0x2B)
GREEN = RGBColor(0x2E, 0x5E, 0x3E)
WHITE = RGBColor(0xFF, 0xFF, 0xFF)


def set_cn(run, name="微软雅黑", size=10.5, color=None, bold=False, italic=False):
    run.font.name = "Calibri"
    run._element.rPr.rFonts.set(qn("w:eastAsia"), name)
    run.font.size = Pt(size)
    run.font.bold = bold
    run.font.italic = italic
    if color is not None:
        run.font.color.rgb = color


def para(doc, text, size=10.5, color=DARK, bold=False, align=None, space_after=6,
         indent=True, space_before=0):
    p = doc.add_paragraph()
    if align:
        p.alignment = align
    pf = p.paragraph_format
    pf.space_after = Pt(space_after)
    pf.space_before = Pt(space_before)
    pf.line_spacing = 1.3
    if indent and align is None:
        pf.first_line_indent = Pt(size * 2)
    r = p.add_run(text)
    set_cn(r, size=size, color=color, bold=bold)
    return p


def guide(doc, text):
    """红色待办/说明，交稿前删除或替换"""
    para(doc, "⟪" + text + "⟫", size=9, color=RED, indent=False, space_after=8)


def h1(doc, text):
    h = doc.add_heading(level=1)
    r = h.add_run(text)
    set_cn(r, size=16, color=DARK, bold=True)
    h.paragraph_format.space_before = Pt(16)
    h.paragraph_format.space_after = Pt(8)


def h2(doc, text):
    h = doc.add_heading(level=2)
    r = h.add_run(text)
    set_cn(r, size=13, color=GREEN, bold=True)
    h.paragraph_format.space_before = Pt(10)
    h.paragraph_format.space_after = Pt(6)


def shade(cell, hexcolor):
    tcPr = cell._tc.get_or_add_tcPr()
    shd = OxmlElement("w:shd")
    shd.set(qn("w:val"), "clear")
    shd.set(qn("w:fill"), hexcolor)
    tcPr.append(shd)


def table(doc, headers, rows, widths=None, fs=9.5):
    t = doc.add_table(rows=1, cols=len(headers))
    t.style = "Table Grid"
    t.alignment = 1
    for i, h in enumerate(headers):
        c = t.rows[0].cells[i]
        c.text = ""
        r = c.paragraphs[0].add_run(h)
        set_cn(r, size=fs, color=WHITE, bold=True)
        shade(c, "1A2E22")
        if widths:
            c.width = Cm(widths[i])
    for row in rows:
        cells = t.add_row().cells
        for i, v in enumerate(row):
            cells[i].text = ""
            r = cells[i].paragraphs[0].add_run(str(v))
            set_cn(r, size=fs, color=RED if str(v).startswith("【占位") else DARK,
                   bold=str(v).startswith("【占位"))
            if widths:
                cells[i].width = Cm(widths[i])
    doc.add_paragraph().paragraph_format.space_after = Pt(2)
    return t


_num = [0]


def pic(doc, path, width_cm, caption):
    _num[0] += 1
    p = doc.add_paragraph()
    p.alignment = WD_ALIGN_PARAGRAPH.CENTER
    p.paragraph_format.space_before = Pt(6)
    run = p.add_run()
    run.add_picture(path, width=Cm(width_cm))
    cap = doc.add_paragraph()
    cap.alignment = WD_ALIGN_PARAGRAPH.CENTER
    cap.paragraph_format.space_after = Pt(10)
    r = cap.add_run(caption)
    set_cn(r, size=9, color=GRAY, italic=True)


def add_toc(doc):
    p = doc.add_paragraph()
    r = p.add_run()
    fld = OxmlElement("w:fldChar"); fld.set(qn("w:fldCharType"), "begin")
    instr = OxmlElement("w:instrText"); instr.set(qn("xml:space"), "preserve")
    instr.text = r'TOC \o "1-2" \h \z \u'
    sep = OxmlElement("w:fldChar"); sep.set(qn("w:fldCharType"), "separate")
    t = OxmlElement("w:t"); t.text = "（在 Word 中右键此处 → 更新域，即可生成目录）"
    end = OxmlElement("w:fldChar"); end.set(qn("w:fldCharType"), "end")
    for e in (fld, instr, sep, t, end):
        r._r.append(e)


def add_footer(doc):
    for section in doc.sections:
        footer = section.footer
        p = footer.paragraphs[0]
        p.alignment = WD_ALIGN_PARAGRAPH.CENTER
        r = p.add_run()
        f1 = OxmlElement("w:fldChar"); f1.set(qn("w:fldCharType"), "begin")
        it = OxmlElement("w:instrText"); it.text = "PAGE"
        f2 = OxmlElement("w:fldChar"); f2.set(qn("w:fldCharType"), "end")
        r._r.append(f1); r._r.append(it); r._r.append(f2)
        set_cn(r, size=9, color=GRAY)


doc = Document()
for s in doc.sections:
    s.left_margin = Cm(2.2); s.right_margin = Cm(2.2)
    s.top_margin = Cm(2.2); s.bottom_margin = Cm(2.2)

# ============ 封面 ============
for _ in range(4):
    doc.add_paragraph()
para(doc, "茶文化管理系统", size=26, color=DARK, bold=True, align=WD_ALIGN_PARAGRAPH.CENTER)
para(doc, "基于混合检索 RAG 与 ReAct Agent 的茶文化智能服务平台", size=13, color=GOLD,
     align=WD_ALIGN_PARAGRAPH.CENTER, space_after=24)
for line in [
    "参赛赛道：第九届传智杯全国大学生数智创新与AI应用大赛 · AI WEB网页开发挑战赛（团队赛）",
    "参赛队伍：【占位：队名】　　学校：【占位】　　指导教师：【占位】",
    "团队成员：【占位：姓名 × 3】",
    "代码仓库：https://github.com/jiehuayu-jc/tea-culture-system",
    "在线演示：http://124.220.4.136:8090/springbootj8kskvkr/front/index.html",
]:
    para(doc, line, size=11, align=WD_ALIGN_PARAGRAPH.CENTER, space_after=8, indent=False)
doc.add_page_break()

# ============ 目录 ============
para(doc, "目　录", size=16, color=DARK, bold=True, align=WD_ALIGN_PARAGRAPH.CENTER, space_after=12, indent=False)
add_toc(doc)
para(doc, "（打开文档后：全选 → 右键 → 更新域，即可生成带页码的目录）", size=9, color=GRAY,
     align=WD_ALIGN_PARAGRAPH.CENTER, indent=False)
doc.add_page_break()

# ============ 摘要 ============
h1(doc, "摘要")
para(doc, "茶文化管理系统是一个“内容 + 电商 + AI”三位一体的 Web 平台：用户浏览茶文化栏目、购买好茶、参与茶友社区，并通过“茶道AI”获得基于站内知识库的智能问答、个性化荐茶与订单查询。系统将大模型能力深度融入检索、决策与数据分析三个层面：自研两阶段混合检索 RAG（BM25 稀疏召回 + 稠密向量 RRF 融合 + LLM 语义重排），回答附带可跳转的站内引用；ReAct 工具调用 Agent 打通“问答—查单—推荐—加购”业务闭环；自然语言查数让运营以中文提问直接获得统计图表。多模态覆盖文本、图像、语音、视频四类输入输出：识茶拍照、语音提问（ASR）、回答朗读（TTS）、茶艺视频抽帧理解，全部并入同一条 Agent 决策链路。全部 AI 能力在无网络、无密钥环境下自动降级为本地链路，保证演示与评审的可靠性。检索质量经 70 题评测集三策略消融验证：混合检索 + 语义重排的 Recall@3 达 0.914，较纯 BM25 基线提升 27.1 个百分点。", space_after=12)

# ============ 一、作品概述 ============
h1(doc, "一、作品概述")
h2(doc, "1.1 作品定位")
para(doc, "茶文化管理系统是面向茶文化领域的“内容 + 电商 + AI”综合平台：前台用户逛好茶集市买茶、看茶文化栏目学茶、逛茶友圈社区交流、预约线上茶艺讲座，并随时向“茶道AI”提问；茶商在商家端维护自有商品与订单；管理员通过三端管理后台（25 个管理模块）统筹内容、商品、用户与运营数据。与“普通商城 + 人工客服”的传统方案相比，本作品把大模型能力做成了三类角色的日常生产力工具，而非一个孤立的聊天窗口。")
h2(doc, "1.2 与传统方案的对比")
table(doc, ["维度", "传统茶类商城", "本作品"], [
    ["咨询", "人工客服，响应慢", "茶道AI：知识库问答，秒级响应、带引用溯源"],
    ["导购", "分类筛选，用户自己找", "Agent 理解偏好、查历史订单后个性化推荐，可直接加购"],
    ["运营分析", "需要开发写 SQL 出报表", "自然语言查数：中文提问直接出统计图"],
    ["知识沉淀", "散落在文章里，无法复用", "站内内容自动建库 + 向量化，成为 AI 的领域知识库"],
    ["可靠性", "强依赖外部服务", "无外网/无密钥自动降级，功能链路不中断"],
], widths=[2.2, 6.0, 8.6])
h2(doc, "1.3 功能全景")
para(doc, "系统功能覆盖内容、电商、社区与 AI 四条线，各业务模块与数据表一一对应，管理端可对全部实体进行增删改查：")
table(doc, ["业务模块", "对应数据表", "说明"], [
    ["好茶集市", "shangpinxinxi / cart / orders / address / coupon", "茶叶商品浏览、购物车、下单、收货地址、优惠券"],
    ["茶文化", "jiaoxueshipin", "茶文化内容栏目（图文/视频教学）"],
    ["茶友圈", "forum / forumtype / forumreport", "社区发帖、分类、举报管理"],
    ["购物资讯", "news / newstype", "资讯栏目与文章"],
    ["线上讲座", "xinlizixun / yuyuezixun", "茶艺讲座发布与预约"],
    ["茶道AI", "ai_knowledge", "RAG 问答 / Agent 导购 / 多模态识茶"],
    ["智能私聊", "chat / chatmessage / friend", "用户间私聊与好友"],
    ["茶商端", "shangjia", "商家入驻、自有商品与订单维护"],
    ["管理后台", "users + 全部业务表", "25 个管理模块，含 AI 控制台与 RAG 评测"],
], widths=[2.6, 6.2, 8.0])
pic(doc, os.path.join(SHOTS, "front-home.png"), 14.5, "图 1-1　前台首页（夜茶·墨绿金设计主题，实拍截图）")

# ============ 二、需求与功能设计 ============
h1(doc, "二、需求与功能设计")
h2(doc, "2.1 角色与核心用例")
table(doc, ["角色", "核心用例", "AI 参与点"], [
    ["前台用户", "浏览商品/内容、加购下单、领券、发帖、预约讲座", "茶道AI 问答、Agent 荐茶、看图识茶、私聊"],
    ["茶商", "入驻、商品上架与维护、订单处理", "AI 写手一键生成商品介绍草稿"],
    ["管理员", "内容/商品/用户/订单管理、数据看板", "自然语言查数、RAG 评测、AI 写手"],
], widths=[2.6, 8.0, 6.2])
guide(doc, "如需图形化用例图，可用 draw.io 绘制三类角色 × 核心用例后导出 PNG 插入此处（非必需）。")
h2(doc, "2.2 核心流程展开（其余功能从简）")
para(doc, "流程一：茶道AI 一次 RAG 问答的全链路", bold=True, indent=False)
table(doc, ["步骤", "环节", "说明"], [
    ["1", "用户提问", "前台茶道AI 页输入自然语言问题（可附图片）"],
    ["2", "意图理解", "后端组装对话上下文，判断是否需要检索"],
    ["3", "混合召回", "BM25 稀疏召回 ∥ DashScope 稠密向量召回，双路并行取 Top-K"],
    ["4", "RRF 融合", "倒数排序融合（k=60），合并双路候选"],
    ["5", "语义重排", "大模型对候选按相关性重排并剔除不相关片段"],
    ["6", "拼装 Prompt", "检索结果附来源编号注入提示词"],
    ["7", "流式生成", "DeepSeek SSE 流式输出，前端打字机渲染"],
    ["8", "引用溯源", "回答下方渲染引用卡，点击可跳转站内原文"],
], widths=[1.4, 3.0, 12.4])
para(doc, "流程二：Agent 推荐下单闭环", bold=True, indent=False)
table(doc, ["步骤", "环节", "说明"], [
    ["1", "用户提问", "“我上次买的龙井怎么样？再推荐一款类似的”"],
    ["2", "Agent 规划", "模型决定先调用「查订单」工具获取历史订单"],
    ["3", "工具调用", "返回订单明细，模型改写检索词调用「推荐商品」"],
    ["4", "结果回填", "推荐结果以商品卡片返回，前端时间线展示思考→调用→结果"],
    ["5", "业务动作", "用户点击商品卡直接加购，购物车/订单落库"],
], widths=[1.4, 3.0, 12.4])
h2(doc, "2.3 其余功能")
para(doc, "优惠券领取与核销、茶友圈发帖与举报、讲座预约、用户间私聊等功能均为常规 CRUD + 校验流程，此处不再展开；全部模块见 1.3 节功能全景表。")

# ============ 三、系统架构 ============
h1(doc, "三、系统架构")
h2(doc, "3.1 总体架构")
pic(doc, os.path.join(FIGS, "fig-arch.png"), 15.2, "图 3-1　系统总体分层架构")
para(doc, "系统为单体 Spring Boot 应用，分客户端、控制、服务、持久、存储五层；AI 能力以独立服务组件（TeaRagService / TeaAgentService / RagEvalService）嵌入服务层，与业务 Service 平级共存，通过统一的外部服务适配层访问 DeepSeek 与 DashScope。管理端为手写静态页，构建时随 classpath 自动映射，不依赖 Node 工具链。")
pic(doc, os.path.join(SHOTS, "admin-console.png"), 14.5, "图 3-2　管理端控制台（与前台共用夜茶·墨绿金设计系统）")
h2(doc, "3.2 技术选型及理由")
table(doc, ["层", "技术", "为什么选它"], [
    ["后端框架", "Spring Boot 2.2.2 + MyBatis-Plus", "教学基座成熟稳定，团队可把精力聚焦在 AI 层创新"],
    ["大模型", "DeepSeek（OpenAI 兼容协议）", "自研轻量客户端（多轮对话 / Function Calling / SSE 流式），约几百行代码，不引入重框架，可控可测"],
    ["向量/多模态", "DashScope 百炼（text-embedding-v3 / qwen-vl / qwen3-asr / qwen3-omni）", "同一把 Key 同一兼容协议接入向量、视觉、语音四模态，代码结构统一，按量计费成本可控"],
    ["前端", "Vue 2.6 + ElementUI（前台）", "与基座一致，组件生态完整"],
    ["管理端", "手写静态页 + classpath 自动映射", "无 Node 工具链依赖，部署即生效，评委回现可跑"],
    ["存储", "MySQL 8.0", "业务数据与 ai_knowledge 知识库统一存储，向量化在应用层完成，免引入独立向量库的运维成本"],
    ["实时通信", "WebSocket", "私聊消息实时推送"],
], widths=[2.4, 4.6, 9.8])
h2(doc, "3.3 工程基座说明（诚实声明）")
para(doc, "本系统基于通用教学脚手架二次开发，原始基线见仓库首个提交 da04f21。本团队的原创工作集中在三块：①AI 服务层——RAG 混合检索引擎、ReAct Agent、自然语言查数与检索评测体系（TeaRagService / TeaAgentService / RagEvalService 等，约数千行新增代码）；②管理端界面重构——手写静态页替换原 Vue 管理端，统一夜茶·墨绿金设计系统；③全站安全整改——12 项整改逐条对应 Git 提交，可查证（见 6.2 节）。主动交代基座来源，是为了把评审注意力引向我们真正的增量工作。")
h2(doc, "3.4 代码结构")
para(doc, "com/controller：业务控制器 + AiController（AI 统一入口）；com/service：业务 Service 群 + TeaRagService（混合检索 RAG）+ TeaAgentService（ReAct Agent）+ RagEvalService（检索评测）；com/utils：DeepSeekClient（自研轻量大模型客户端）、EmbeddingProvider（向量适配）、AiRateLimiter（per-IP 限流）、AiMetrics（调用指标）；resources/front：Vue 前台源码；resources/admin-ui：管理端静态页源码。")

# ============ 四、AI 能力设计 ============
h1(doc, "四、AI 能力设计（全文重点）")
h2(doc, "4.1 混合检索 RAG：让回答建立在可溯源的领域知识库上")
pic(doc, os.path.join(FIGS, "fig-rag.png"), 15.5, "图 4-1　混合检索 RAG 流水线")
para(doc, "为什么单一检索不够：BM25 擅长关键词精确命中，但对“新手喝什么茶不容易失眠”这类字面不含“绿茶/助眠”的口语化问题召回困难；纯向量检索擅长语义近义，却容易漏掉商品名、茶名等必须精确匹配的实体。两路召回经 RRF 融合后再由大模型做语义重排、剔除不相关片段，兼顾查全与查准——这正是第五章消融实验验证的组合。")
para(doc, "知识库构成：ai_knowledge 表存 200 条茶文化语料（领域种子库 160 条，覆盖六大茶类、名茶、冲泡、茶史、茶器、储存、饮忌、茶俗、品鉴、术语、山场；站内商品/资讯/讲座内容自动收割 40 条），向量化在应用层完成并缓存，服务启动时预热，避免首次请求卡顿。")
para(doc, "引用溯源：每条回答标注所引知识条目，前端渲染可点击的引用卡，跳转站内原文——这是与“直接问大模型”的本质区别，也是答案可信度的直观证据。核心代码：TeaRagService.java、EmbeddingProvider.java。")
h2(doc, "4.2 ReAct Agent：AI 决策直接驱动业务动作")
pic(doc, os.path.join(FIGS, "fig-agent.png"), 14.5, "图 4-2　ReAct Agent 工具调用循环")
table(doc, ["工具", "能力", "服务端校验"], [
    ["查订单", "获取当前用户历史订单，支撑个性化推荐", "仅限本人数据，Token 强制鉴权"],
    ["推荐商品", "按偏好/历史行为检索站内商品", "仅返回在售商品"],
    ["检索知识", "复用 RAG 检索链路回答领域问题", "白名单数据源"],
    ["泡茶指南", "按茶类返回冲泡参数建议", "静态知识源"],
    ["推荐讲座", "推荐可预约的线上茶艺讲座", "仅返回未结束讲座"],
], widths=[3.0, 8.0, 5.8])
para(doc, "模型自主规划调用次数与检索词改写（最多 4 轮），前端以时间线可视化“思考 → 调用 → 结果”全过程；推荐结果渲染为商品卡，可直接加购——AI 不止于“聊”，而是驱动真实业务动作。防失控设计：工具白名单 + 参数服务端校验 + 轮数上限，不存在自由 SQL 或自由下单。工具接口以统一 schema 定义，当前基于 Function Calling 标准实现，具备向 MCP 协议生态平滑迁移的路径。核心代码：TeaAgentService.java。")
h2(doc, "4.3 多模态：图像 · 语音 · 视频共用一条决策链路")
para(doc, "系统多模态覆盖文本、图像、语音、视频四类模态，设计原则是“多模态只是新的输入输出通道，智能内核只有一套”：所有模态的识别结果都并入同一条 Agent 决策链路（RAG 检索 + 工具调用 + 业务动作），而非各搞一套孤立功能。")
para(doc, "图像识茶（已有）：上传干茶/茶汤照片，qwen-vl 视觉模型转成结构化描述后注入 Agent 上下文——“看图提问”而非独立的看图工具。")
para(doc, "语音提问（ASR）：茶道AI 页内置录音按钮，浏览器采集 16kHz PCM 并编码 WAV，qwen3-asr-flash 转写为文字后回填输入框，确认即走原有对话链路——语音提问与文字提问在后端完全同构。")
para(doc, "回答朗读（TTS）：开启“音”开关后，每轮回答完成即由 qwen3-omni-flash 流式合成语音（模型输出裸 PCM 分片，服务端拼接并补 WAV 头，24kHz/16bit/单声道），前端 audio 直接播放，长回答先出字后出声、互不阻塞。")
para(doc, "视频理解：上传茶艺/泡茶视频后，浏览器按时间轴均匀抽取 5 帧 JPEG（无需服务端转码），qwen-vl 将多帧视为同一事件的连续画面作综合判断，识别描述与用户问题一并进入 Agent 链路——支持“我这个手法对不对”这类过程性问题。")
para(doc, "降级一致性：语音与视觉按钮仅在校验到百炼 Key 可用时渲染（/ai/status 暴露 speech_configured / vision_configured），未配置 Key 时按钮自动隐藏，文本主流程不受任何影响——与整体降级设计同一套哲学。")
h2(doc, "4.4 自然语言查数（NLQ）：让运营不写 SQL")
pic(doc, os.path.join(FIGS, "fig-nlq.png"), 15.0, "图 4-3　NLQ 意图白名单流程")
para(doc, "管理端以中文提问直接获得统计图表。安全设计与“让模型生成 SQL”的方案有本质区别：大模型只负责把问题解析为四类白名单枚举意图（商品销量 / 商品浏览 / 订单状态 / 分类销量），SQL 由服务端受限模板拼接、参数化传值，模型无权触碰数据库；解析失败自动落关键词兜底规则，页面不会白屏。核心代码：AiController#nlq。")
h2(doc, "4.5 AI 文本生成")
para(doc, "管理端 AI 写手支持商品介绍、茶文化文章草稿一键生成，覆盖茶商与运营的内容生产场景（AiController#writer）。")
h2(doc, "4.6 降级设计：为评审环境兜底")
para(doc, "无 DeepSeek Key 或无外网时，系统自动切换离线演示模式：RAG 降为单路 BM25、Agent 降为本地规则推荐，状态灯明确提示“离线演示模式”。该设计保证功能链路在评审现场完整可演示——即使评委拔掉外网，系统依然可用。")
h2(doc, "4.7 性能与成本优化策略")
para(doc, "延迟优化：文档向量在应用层缓存并在服务启动时预热，避免首次请求冷启动卡顿；回答采用 SSE 流式输出，用户首字等待从“整段生成”降至首 token 时间，主观等待显著缩短；检索链路中 BM25 为纯内存计算，向量召回仅对候选集做重排，整体问答链路在公网实测稳定可用。")
para(doc, "成本与可控性：per-IP 限流（10 次/分钟、200 次/天）+ max_tokens 预算双重约束单用户消耗，调用指标实时记录（AiMetrics）；选用 DeepSeek 的核心原因之一是按量计费下单次问答成本在分级别，商业化后毛利结构清晰（见 10.4 节）。")

# ============ 五、AI 质量评测 ============
h1(doc, "五、AI 质量评测")
h2(doc, "5.1 评测集")
para(doc, "自建 70 题茶文化评测集并标注标准答案，覆盖知识库全部 200 条语料来源；问题刻意口语化改写、不与知识标题字面重合，专门考验语义召回而非关键词匹配。评测通过管理台「RAG 评测」页一键运行，也可经接口现场复跑。")
h2(doc, "5.2 三策略消融实验（公网服务器实测，知识库 200 条）")
table(doc, ["检索策略", "Recall@3", "MRR", "命中"], [
    ["A　单路 BM25", "0.643", "0.548", "45/70"],
    ["B　双路 RRF（BM25 + 向量）", "0.814", "0.683", "57/70"],
    ["C　双路 + LLM 语义重排", "0.914", "0.819", "64/70"],
], widths=[7.0, 3.2, 3.2, 3.4])
para(doc, "结论：混合检索相对纯稀疏基线提升 17.1 个百分点，叠加语义重排后累计提升 27.1 个百分点；MRR 0.819 表明正确答案绝大多数排在第一位。原始数据见仓库 docs/eval-200.json，管理台「RAG 评测」页可现场复跑验证。")
guide(doc, "建议：演示时在管理台 RAG 评测页补拍一张三策略对比截图插入此处，证据更直观。")
h2(doc, "5.3 常规功能与安全测试")
para(doc, "功能测试：围绕三角色核心链路（游客浏览 → 注册登录 → 加购下单 → AI 问答/导购 → 管理端运营）进行手工回归，配套 8 个演示用户 + 8 个演示商家账号矩阵覆盖各角色权限边界；管理端 25 个模块共用同一套列表/表单组件，抽测通过即全量覆盖。")
para(doc, "安全测试：6.2 节 12 项整改逐条验证（匿名接口全量梳理、SQL 注入、路径穿越、CORS、依赖漏洞），并以公网环境长期运行作为持续验证手段；AI 侧增加 prompt 注入与滥用场景抽测，限流与预算约束生效。")
para(doc, "诚实说明：检索质量以 70 题评测集量化覆盖（5.1-5.2 节），常规功能的自动化单元测试尚在建设中，计划优先覆盖 RRF 融合排序与 NLQ 意图白名单两个关键算法路径（见 10.3）。")
h2(doc, "5.4 已知限制")
para(doc, "评测集与语料同源（覆盖式测试），独立外部题集在计划中；语料 200 条属演示规模，但架构按增量设计，扩容语料无需修改代码。")

# ============ 六、数据与安全设计 ============
h1(doc, "六、数据与安全设计")
h2(doc, "6.1 数据设计")
pic(doc, os.path.join(FIGS, "fig-er.png"), 13.5, "图 6-1　核心数据表关系")
para(doc, "业务数据（用户/茶商/商品/订单/购物车/地址/优惠券）与 AI 知识库（ai_knowledge）同库存储：知识库条目含向量、原文与来源引用，由站内内容自动收割与领域种子库两部分构成，支撑 RAG 检索与引用溯源。")
h2(doc, "6.2 安全整改清单（逐条对应 Git 提交，可查证）")
table(doc, ["整改项", "说明", "提交"], [
    ["收紧 @IgnoreAuth", "移除 100 处匿名放行（含购物车/订单/地址等私有数据匿名读写与匿名删除）", "fd1b16c"],
    ["密码 BCrypt 化", "注册/改密走 BCrypt 带盐哈希，历史 MD5 登录后透明升级；密码字段不再随 JSON 返回", "fd1b16c"],
    ["匿名重置封堵", "/resetPass 移出匿名白名单，防止任意管理员密码被重置", "b279c1c"],
    ["AI 接口防护", "chat/stream 收紧鉴权，per-IP 限流（10 次/分钟、200 次/天）+ max_tokens 预算 + 调用指标", "23bebe4"],
    ["SQL 注入防护", "NLQ 意图白名单 + 参数化查询；修复 orders 表列名错误", "fd1b16c"],
    ["路径穿越与上传", "文件下载修复 ../ 穿越；上传增加后缀白名单；mysqldump 加管理员门禁", "fd1b16c"],
    ["CORS 收紧", "不再回显任意 Origin，改为端口白名单", "fd1b16c"],
    ["依赖升级", "fastjson 1.2.8 → 1.2.83，消除历史反序列化漏洞", "fd1b16c"],
    ["密钥治理", "API Key 改环境变量/外部配置注入不入库；移除源码硬编码凭据；历史提交经 git-filter-repo 清洗", "a17a1a9 / 32f4d56"],
], widths=[3.4, 10.4, 3.0], fs=9)
h2(doc, "6.3 公网部署安全")
para(doc, "系统已在公网服务器长期运行验证（124.220.4.136:8090）：应用以非 root 的 systemd 服务运行；数据库使用独立账号并仅授权本项目库（最小授权原则）；防火墙仅放行 8090 端口。")

# ============ 七、创新点 ============
h1(doc, "七、创新点")
h2(doc, "7.1 技术创新点")
para(doc, "1. 混合检索 RAG，回答可溯源", bold=True, indent=False, space_after=2)
para(doc, "绝大多数参赛作品把大模型当“问答机器”，本作品让它建立在可溯源的领域知识库上：稀疏 + 稠密双路召回、RRF 融合、LLM 语义重排，回答附带可跳转的站内引用。检索融合与重排逻辑与站内业务数据深度耦合，自研流水线换来对每个环节的可控与可测；相比微调模型，向量库增量更新成本更低且能提供引用。")
para(doc, "2. 检索质量可评测，不靠感觉调 prompt", bold=True, indent=False, space_after=2)
para(doc, "内置 70 题评测集与标注答案，管理端一键运行三种检索策略消融对比，Recall@3 与 MRR 摆在表里、可现场复跑——检索质量是“测出来的”，不是“讲出来的”。")
para(doc, "3. NLQ 意图白名单：模型“听懂”不“碰库”", bold=True, indent=False, space_after=2)
para(doc, "中文提问直接出统计图，但接口只开放四类白名单统计：模型输出被约束为枚举意图而非 SQL 文本，SQL 由服务端模板参数化执行——这是与“直接让模型生成 SQL”方案的本质区别。")
h2(doc, "7.2 应用创新点")
para(doc, "1. ReAct Agent 业务闭环，AI 驱动真实业务动作", bold=True, indent=False, space_after=2)
para(doc, "别的作品 AI 只能“聊”，本作品的 AI 能查订单、挑商品、直接把推荐结果加进购物车；工具白名单 + 服务端参数校验 + 轮数上限保证可控；前端时间线让 AI 决策过程透明可见。图像、语音、视频多模态同样接入这条决策链路——“看图提问、张口即问、看视频求证”而非孤立的多模态演示。")
para(doc, "2. 全链路离线演示模式", bold=True, indent=False, space_after=2)
para(doc, "无网无 Key 功能不中断：BM25 检索 + 本地推荐自动接管，可现场“拔网线”演示，直接回应“评审现场没网怎么办”的经典问题。")
para(doc, "3. 全站统一“夜茶·墨绿金”设计系统", bold=True, indent=False, space_after=2)
para(doc, "前台与管理端共用一套色板（墨绿 #08130d + 金 #D4AF37 + 米白 #EDE6D6）与同一份衬线题字字体，管理端零 Node 工具链部署——从 URL 到视觉都是“一个作品”而非“两个项目拼接”。")

# ============ 八、团队分工 ============
h1(doc, "八、团队分工")
table(doc, ["成员", "职责", "核心贡献"], [
    ["【占位：姓名】", "【占位：如 AI 服务层】", "RAG 混合检索引擎、ReAct Agent、检索评测体系"],
    ["【占位：姓名】", "【占位：如 前后端业务】", "商城/订单模块、管理端界面重构、安全整改"],
    ["【占位：姓名】", "【占位：如 文档与测试】", "技术文档、评测执行、演示视频、部署运维"],
], widths=[3.2, 5.0, 8.6])
para(doc, "成员姓名与 Git 提交作者一一对应，GitHub 提交历史（67 次提交、含安全整改与功能迭代全过程）即过程真实性的直接证据。", size=9.5, color=GRAY)

# ============ 九、部署与复现手册 ============
h1(doc, "九、部署与复现手册")
h2(doc, "9.1 三步启动")
para(doc, "① 数据库：执行 db/init-database.bat（或项目根目录 docker compose up -d，首次启动自动导入 SQL）；② 后端：双击 run.bat（或 IDE 运行 com.TeaCultureApplication），端口 8080，context-path /springbootj8kskvkr；③ 访问：前台 /springbootj8kskvkr/front/index.html，管理端 /springbootj8kskvkr/admin/index.html（管理员账号 admin / admin，演示用户与茶商密码 123456）。前端无需构建，后端启动即全部可用。")
h2(doc, "9.2 开启完整 AI 能力")
para(doc, "通过环境变量 DEEPSEEK_API_KEY 注入对话模型 Key（DashScope 向量 Key 可选，未配置时检索自动退化为单路 BM25）；或复制 config/application.yml.example 为 config/application.yml 填入（该目录已被 .gitignore 排除，密钥不入库）。不配置任何 Key 时系统进入离线演示模式，功能链路不中断。")
h2(doc, "9.3 常见问题排查")
table(doc, ["现象", "原因", "处理"], [
    ["管理端 404", "使用了旧版本仓库", "拉取最新代码：管理端已随构建自动映射，无需手工部署"],
    ["AI 回复提示离线模式", "未配置 API Key", "配置环境变量后重启后端"],
    ["向量检索未生效", "未配置 DashScope Key", "配置后重启，/ai/status 可查看当前检索架构"],
    ["端口占用", "8080 被其他进程占用", "修改 application.yml 中 server.port"],
    ["构建前端失败", "Node 新版本 OpenSSL 不兼容", "设置 NODE_OPTIONS=--openssl-legacy-provider 后重新构建（仅改前台源码时需要）"],
], widths=[3.6, 5.4, 7.8])

h2(doc, "9.4 评审专用体验账号")
table(doc, ["角色", "地址", "账号", "密码"], [
    ["管理员", "…/admin/index.html", "【占位：评审账号】", "【占位】"],
    ["前台用户", "…/front/index.html", "【占位：评审账号】", "【占位】"],
    ["茶商", "…/front/index.html", "【占位：评审账号】", "【占位】"],
], widths=[2.6, 6.6, 4.2, 3.4])
para(doc, "在线演示地址：http://124.220.4.136:8090/springbootj8kskvkr/front/index.html。评审期间（至评审意见发布）保证服务持续可用；上述专用账号仅授予评审所需的最小权限，用后可停用。", size=9.5, color=GRAY)

# ============ 十、总结与展望 ============
h1(doc, "十、总结与展望")
h2(doc, "10.1 已完成")
para(doc, "①混合检索 RAG 全链路（双路召回 + RRF + 语义重排 + 引用溯源）与可复跑的检索评测体系（Recall@3 0.914）；②ReAct Agent 业务闭环（查订单 → 推荐 → 一键加购）与多模态输入；③自然语言查数、AI 写手、离线演示降级；④完整的电商、内容、社区与三端管理模块，以及 12 项可查证的安全整改与公网长期运行验证。")
h2(doc, "10.2 诚实的限制")
para(doc, "知识库 200 条属演示规模；支付为模拟流程、未接真实支付网关；系统基于教学脚手架基座 + 自研 AI 层构成（见 3.3 节）；评测集与语料同源，独立外部题集待建。")
h2(doc, "10.3 下一步计划")
para(doc, "引入独立外部评测题集并定期回归；语料扩容至千条级并接入商品评价数据丰富推荐信号；管理端“忘记密码”补充验证机制；在线演示绑定域名并启用 HTTPS；前端引入图片懒加载与路由级代码分割、评估向 Vue3/TypeScript 渐进迁移；接入语音多模态；工具层向 MCP 协议生态迁移。")
h2(doc, "10.4 社会价值与商业潜力")
para(doc, "社会价值：茶是中国最具代表性的传统文化符号之一，但年轻一代与优质茶文化内容、可靠购茶渠道之间仍有断层。本作品用 AI 把分散的茶知识组织成可问答、可溯源的知识库，降低普通人“识茶、泡茶、买茶”的门槛，属于“AI + 传统文化数字化”的落地实践；内容栏目与线上讲座亦可持续沉淀为公开的茶文化科普资源。")
para(doc, "商业潜力：目标用户为对茶感兴趣但缺乏专业知识的年轻消费者与中小茶商。可行模式为“B 端 + C 端”双轨——B 端向入驻茶商收取摊位/佣金并销售 AI 导购带来的转化；C 端后期可对深度 AI 服务（个性化体质荐茶、专属茶艺助手）提供订阅增值。成本端，DeepSeek/DashScope 均为按量计费，单次问答成本在分级水平，配合限流与 token 预算，毛利结构清晰；系统基于成熟开源基座 + 自研 AI 层，边际扩张成本低，具备可持续运营条件。")

add_footer(doc)
doc.save(OUT)
print("saved:", OUT)
