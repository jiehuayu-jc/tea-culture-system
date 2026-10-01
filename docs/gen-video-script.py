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
para(doc, "作品：茶文化管理系统　|　视频时长 6 分 30 秒　|　出镜要求：普通话讲解、画面 ≥1920×1080、30fps", size=10, color=GRAY, align=WD_ALIGN_PARAGRAPH.CENTER, space_after=12)

h1(doc, "一、官方要求与提交方式")
para(doc, "① 时长 5-8 分钟；② 分辨率 ≥1080p；③ 普通话讲解，展示核心功能与创新点。")
para(doc, "提交方式：视频统一上传到 B 站 / 抖音等可公开访问的平台，提交时只交视频链接、不交视频文件。建议 B 站，设为公开或「仅链接可见」，把链接填进申报表。")

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

h1(doc, "三、分镜脚本（总长 6 分 30 秒）")
table(doc, ["时间", "画面", "操作", "口播要点"], [
    ["0:00-0:30", "登录页", "缓慢滑过左右两栏", "一句话定位：茶文化管理系统，一个内容+电商+AI 的完整平台。我们不做 AI 套壳，把大模型能力融进了检索、决策、数据分析三个层面"],
    ["0:30-1:10", "前台首页", "滚动展示 hero 与栏目入口，点「问问茶道AI」", "强调整站设计语言统一，顺势进入主题"],
    ["1:10-2:20", "茶道AI 对话", "问「新手喝什么茶不容易失眠？」，等流式回答后点击引用来源卡跳转原文", "重点讲三件事：流式输出、引用可溯源、回答基于站内知识库而非大模型裸答"],
    ["2:20-3:10", "茶道AI Agent", "问「我上次买的龙井怎么样？再推荐一款类似的」，展示时间线", "ReAct 工具调用循环，模型自己决定调几次工具；推荐结果可直接加购，AI 与业务是闭环"],
    ["3:10-3:40", "多模态", "上传一张干茶照片，问「这是什么茶」", "图片理解接入了 Agent 决策链路，不只是看图说话"],
    ["3:40-4:40", "管理端 RAG 评测", "登录管理台 → RAG 评测 → 点「快速验证（前 8 题）」等表格出来", "我们不靠感觉调 prompt——70 题评测集、三种检索策略消融对比，Recall@3 和 MRR 摆在表里，检索质量可度量"],
    ["4:40-5:20", "自然语言查数", "输入「哪种茶卖得最好？」出条形图，再点一个示例", "运营不用写 SQL，中文提问直接出统计图，意图解析失败有关键词兜底"],
    ["5:20-5:50", "管理端数据管理", "搜索模块、编辑一条商品保存（toast 出现即可）", "快速带过 25 个模块的通用管理能力，证明系统完整度"],
    ["5:50-6:20", "离线兜底", "断网或换未配 Key 的配置，刷新茶道AI 发一句话", "评审环境没有外网也能跑：自动降级为 BM25 检索+本地推荐，功能链路不中断"],
    ["6:20-6:50", "收尾", "回到首页定格", "技术栈与分工见技术文档，代码仓库与文档同步提交，谢谢观看"],
])
para(doc, "时长微调原则：AI 问答与 Agent 两段是重头戏，宁可压缩「数据管理」，不要压这两段。", size=9.5, color=GRAY)

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

h1(doc, "五、剪辑与封装")
for item in [
    "剪掉等待片段时用「4 倍速 + 字幕：实时生成中」，不要硬剪出黑帧",
    "关键结论配字幕条（如 Recall@3 = 0.914、MRR = 0.819），评委静音看也能懂",
    "导出参数：H.264 / 1080p / 30fps / AAC 192kbps，命名「队伍名-作品名-demo.mp4」",
    "上传 B 站后自查：标题含作品名、简介贴仓库地址、清晰度选 1080P 高码率",
]:
    para(doc, "· " + item, size=10, space_after=4)

out = r"C:\Users\14483\Desktop\传智杯演示视频拍摄脚本.docx"
doc.save(out)
print("saved:", out)
