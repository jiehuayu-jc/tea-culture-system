# -*- coding: utf-8 -*-
"""生成传智杯演示视频拍摄脚本 Word 文档"""
from docx import Document
from docx.shared import Pt, RGBColor, Cm
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.oxml.ns import qn
from docx.oxml import OxmlElement

DARK = RGBColor(0x1A, 0x2E, 0x22)
GRAY = RGBColor(0x66, 0x66, 0x66)
RED = RGBColor(0xC0, 0x39, 0x2B)
GREEN = RGBColor(0x2E, 0x5E, 0x3E)

def set_cn(run, size=10.5, color=None, bold=False):
    run.font.name = "Calibri"
    run._element.rPr.rFonts.set(qn("w:eastAsia"), "微软雅黑")
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

def h1(doc, text):
    h = doc.add_heading(level=1)
    r = h.add_run(text)
    set_cn(r, size=15, color=DARK, bold=True)
    h.paragraph_format.space_before = Pt(16)

def shade(cell, hexcolor):
    tcPr = cell._tc.get_or_add_tcPr()
    shd = OxmlElement("w:shd")
    shd.set(qn("w:val"), "clear")
    shd.set(qn("w:fill"), hexcolor)
    tcPr.append(shd)

def table(doc, headers, rows, col_widths=None):
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
            set_cn(r, size=9.5)
    doc.add_paragraph().paragraph_format.space_after = Pt(4)
    return t

doc = Document()
for s in doc.sections:
    s.left_margin = Cm(1.8); s.right_margin = Cm(1.8)
    s.top_margin = Cm(2.0); s.bottom_margin = Cm(2.0)

para(doc, "演示视频拍摄脚本 · 第九届传智杯 AI WEB网页开发挑战赛", size=15, color=DARK, bold=True, align=WD_ALIGN_PARAGRAPH.CENTER)
para(doc, "作品：茶文化管理系统　|　视频时长 7 分 20 秒　|　出镜要求：普通话讲解、MP4、画面 ≥1920×1080、30fps", size=10, color=GRAY, align=WD_ALIGN_PARAGRAPH.CENTER, space_after=12)

h1(doc, "一、官方要求与提交方式（2026-10 依官网赛道细则核对更新）")
para(doc, "官方硬性要求：① 时长 5-8 分钟；② MP4 格式，分辨率不低于 1080p；③ 普通话讲解，可配字幕；④ 内容须包含项目介绍、核心功能演示、AI 技术亮点展示三部分。")
para(doc, "提交方式：所有材料通过大赛官网统一提交——缴费后登录赛事个人中心所报赛项查看提交入口（不设外部链接）。按官方口径交 MP4 视频文件；B 站公开链接作为补充（简介贴仓库地址，链接可填入申报表/技术文档），两手准备。")
para(doc, "关键时间：报名与作品提交截止 2026 年 11 月 18 日；材料提交后不得修改，交前务必自查；评审意见预计 2027 年 1 月底发布，期间公网演示地址须保持可用。")
para(doc, "评分权重提示（官网细则）：AI 技术深度 35%、创新性 25%、前端工程质量 10%、实用性 10%、用户体验 10%、社会价值与商业潜力 10%——分镜表最后一列标注了每个片段主打的评分点，AI 相关片段是 60% 权重的主战场，宁可压缩其他段落也不压它们。")

h1(doc, "二、录制前准备清单（每条都要做）")
for item in [
    "后端以 run.bat 或 mvnw spring-boot:run 冷启动一次，确认一键可跑（这本身就是评分点）",
    "浏览器开全新 Profile，隐藏书签栏 / 下载记录 / 其他标签页，缩放 100%",
    "预登录管理员与用户两个角色（各开一个浏览器 Profile），避免录到输密码",
    "预置演示数据：至少 3 条已支付订单、几条商品评论、茶道AI 有一轮历史对话",
    "确认管理台右上角状态灯为「大模型在线」；录离线兜底段落时再故意断网",
    "录制前把每个要演示的 AI 问题先跑一遍（向量缓存预热），避免录到长时间空等",
    "OBS / captura 设 1080p / 30fps，先试录 30 秒检查音量与画面",
    "把下表「口播要点」抄成台词卡，练到能脱稿，不要照读",
]:
    para(doc, "□ " + item, size=10, space_after=4)

h1(doc, "三、分镜脚本（总长 7 分 20 秒，含评分点对应）")
table(doc, ["时间", "画面", "操作", "口播要点", "主打评分点"], [
    ["0:00-0:30", "登录页", "缓慢滑过左右两栏", "一句话定位：茶文化管理系统，一个内容+电商+AI 的完整平台。我们不做 AI 套壳，把大模型能力融进了检索、决策、数据分析三个层面", "实用性·场景真实"],
    ["0:30-1:10", "前台首页", "滚动展示 hero 与栏目入口，点「问问茶道AI」", "强调整站设计语言统一（夜茶·墨绿金），顺势进入主题", "用户体验·UI"],
    ["1:10-2:20", "茶道AI 对话", "问「新手喝什么茶不容易失眠？」，等流式回答后点击引用来源卡跳转原文", "重点讲三件事：流式输出、引用可溯源、回答基于站内知识库而非大模型裸答", "AI深度·RAG+LLM；AI交互体验"],
    ["2:20-3:10", "茶道AI Agent", "问「我上次买的龙井怎么样？再推荐一款类似的」，展示时间线，推荐卡点击加购", "ReAct 工具调用循环，模型自己决定调几次工具；推荐结果直接加购，AI 与业务闭环", "AI深度·Agent/工具集成"],
    ["3:10-4:00", "多模态三连", "① 点「语」按钮说「新手喝什么茶」→ 识别文本回填后发送；② 开「音」开关重问一轮，回答自动朗读；③ 点「影」上传一段泡茶视频，AI 抽帧识别后作答", "四模态一套内核：语音转写走同一条 RAG/Agent 链路；回答流式合成朗读（服务端拼 PCM 补 WAV 头）；视频浏览器抽帧、视觉模型综合判断——多模态不是演示玩具，是同一智能体的不同感官", "AI深度·多模态；创新性·交互"],
    ["4:00-4:50", "管理端 RAG 评测", "登录管理台 → RAG 评测 → 点「快速验证（前 8 题）」等表格出来", "我们不靠感觉调 prompt——70 题评测集、三种检索策略消融对比，Recall@3 和 MRR 摆在表里，检索质量可度量", "AI深度·RAG；创新性·技术方案"],
    ["4:50-5:25", "自然语言查数", "输入「哪种茶卖得最好？」出条形图，再点一个示例", "运营不用写 SQL，中文提问直接出统计图，意图解析失败有关键词兜底", "创新性·交互模式"],
    ["5:25-5:50", "管理端数据管理", "搜索模块、编辑一条商品保存（toast 出现即可）", "快速带过 25 个模块的通用管理能力，证明系统完整度", "实用性·方案有效"],
    ["5:50-6:20", "离线兜底", "断网或换未配 Key 的配置，刷新茶道AI 发一句话", "评审环境没有外网也能跑：自动降级为 BM25 检索+本地推荐，功能链路不中断", "AI深度·安全可控"],
    ["6:20-6:50", "价值与商业", "回前台首页定格，快闪内容栏目与讲座页", "我们用 AI 降低普通人识茶、泡茶、买茶的门槛，让传统文化被年轻人重新看见；商业模式上服务茶商入驻与 AI 增值服务，单次问答成本分级别，可持续运营", "社会价值与商业潜力"],
    ["6:50-7:10", "收尾", "首页定格 + 字幕条（仓库地址 / 在线演示地址）", "代码仓库、技术文档与在线演示同步提交，评审期间服务持续可用，谢谢观看", "—"],
])
para(doc, "时长微调原则：AI 问答、Agent、评测三段合计占 60% 权重（AI技术深度+创新性），宁可压缩「数据管理」，不要压这三段。", size=9.5, color=GRAY)

h1(doc, "四、翻车点提醒（每条都是真实事故高发区）")
for i, item in enumerate([
    "AI 回答等待时间：录制前先跑一遍同样的问题让向量缓存热起来，避免录屏里干等 30 秒",
    "评测功能全量跑要 5-10 分钟：只录「前 8 题」快速档；全量结果可提前跑好后展示历史表格",
    "不现场输入密码，不露出任何 API Key（包括 IDEA 配置界面、环境变量窗口）",
    "修改数据类演示只演示一次且用测试数据，避免把种子数据改乱",
    "断网演示段录完立刻恢复网络，防止后面素材同步出问题",
    "画面里所有 URL 必须是演示地址（http://124.220.4.136:8090/...），不能带个人路径或 localhost 之外的开发地址",
], 1):
    para(doc, f"{i}. {item}", size=10, space_after=4)

h1(doc, "五、剪辑、封装与提交自查")
for item in [
    "剪掉等待片段时用「4 倍速 + 字幕：实时生成中」，不要硬剪出黑帧",
    "关键结论配字幕条（如 Recall@3 = 0.914、MRR = 0.819），评委静音看也能懂",
    "导出参数：H.264 / MP4 / 1080p / 30fps / AAC 192kbps，命名「队伍名-作品名-demo.mp4」",
    "提交前自查（官方要求材料提交后不得修改，务必一次交对）：①MP4 文件可在官网个人中心入口正常上传；②时长在 5-8 分钟内；③音画同步、无底噪；④画面无 API Key / 评审账号密码 / 个人路径",
    "B 站补充动作：上传并设公开，标题含作品名，简介贴仓库与在线演示地址，清晰度选 1080P 高码率；链接同步填入技术文档与申报表",
    "提醒评审：在线演示地址与评审专用账号已写入技术文档 9.4 节，评审期间服务器保持可用",
]:
    para(doc, "· " + item, size=10, space_after=4)

out = r"C:\Users\14483\Desktop\传智杯演示视频拍摄脚本.docx"
doc.save(out)
print("saved:", out)
