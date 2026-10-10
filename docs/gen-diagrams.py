# -*- coding: utf-8 -*-
"""技术文档配图：架构图 / RAG 流水线 / ReAct Agent / NLQ / ER 图（夜茶·墨绿金配色）"""
import matplotlib
matplotlib.use("Agg")
import matplotlib.pyplot as plt
from matplotlib.patches import FancyBboxPatch
import os

plt.rcParams["font.family"] = "Microsoft YaHei"
plt.rcParams["axes.unicode_minus"] = False

INK = "#08130d"      # 墨绿
GOLD = "#D4AF37"     # 金
CREAM = "#EDE6D6"    # 米白
DEEP = "#1A2E22"     # 深绿
SAGE = "#3E5C49"     # 灰绿
PAPER = "#FBF8F1"    # 纸色

OUT = os.path.join(os.path.dirname(__file__), "figs")
os.makedirs(OUT, exist_ok=True)


def box(ax, x, y, w, h, text, fc=DEEP, ec=INK, tc=CREAM, fs=10, bold=False, dashed=False, lw=1.2):
    b = FancyBboxPatch((x, y), w, h, boxstyle="round,pad=0.02,rounding_size=0.06",
                       fc=fc, ec=ec, lw=lw, linestyle="--" if dashed else "-")
    ax.add_patch(b)
    ax.text(x + w / 2, y + h / 2, text, ha="center", va="center", fontsize=fs,
            color=tc, fontweight="bold" if bold else "normal", linespacing=1.5)


def arrow(ax, x1, y1, x2, y2, color=INK, lw=1.4, style="-|>", ls="-"):
    ax.annotate("", xy=(x2, y2), xytext=(x1, y1),
                arrowprops=dict(arrowstyle=style, color=color, lw=lw, linestyle=ls))


def canvas(w, h):
    fig, ax = plt.subplots(figsize=(w, h))
    ax.set_xlim(0, 10)
    ax.set_ylim(0, 10 * h / w)
    ax.axis("off")
    fig.patch.set_facecolor(PAPER)
    ax.set_facecolor(PAPER)
    return fig, ax


def save(fig, name):
    fig.savefig(os.path.join(OUT, name), dpi=200, bbox_inches="tight", facecolor=PAPER)
    plt.close(fig)
    print("saved", name)


# ============ 图 3-1 总体分层架构 ============
fig, ax = canvas(10.5, 7.4)
H = 10 * 7.4 / 10.5

# 左侧主链路（x: 0.4~6.6）
box(ax, 0.4, H - 1.3, 6.2, 1.05, "客户端层", fc=INK, fs=11, bold=True)
box(ax, 0.7, H - 2.55, 1.85, 0.95, "前台用户端\nVue 2.6 + ElementUI", fc=DEEP, fs=8.5)
box(ax, 2.75, H - 2.55, 1.75, 0.95, "茶商端\n商品 / 订单维护", fc=DEEP, fs=8.5)
box(ax, 4.7, H - 2.55, 1.85, 0.95, "管理端（手写静态页）\nclasspath 自动映射", fc=DEEP, fs=8.5)

box(ax, 0.4, H - 3.85, 6.2, 0.85,
    "控制层　Spring Boot 2.2 Controller（业务 Controller + AiController）\nToken 拦截器统一鉴权 · 全局异常处理",
    fc=SAGE, fs=8.5)
box(ax, 0.4, H - 5.35, 6.2, 1.15,
    "服务层", fc=INK, fs=11, bold=True)
box(ax, 0.7, H - 6.5, 1.7, 0.85, "业务 Service 群\n商城 / 内容 / 社区", fc=DEEP, fs=8)
box(ax, 2.55, H - 6.5, 1.25, 0.85, "TeaRagService\n混合检索 RAG", fc="#6B5316", fs=8)
box(ax, 3.95, H - 6.5, 1.25, 0.85, "TeaAgentService\nReAct Agent", fc="#6B5316", fs=8)
box(ax, 5.35, H - 6.5, 1.25, 0.85, "RagEvalService\n检索评测", fc="#6B5316", fs=8)

box(ax, 0.4, H - 7.45, 6.2, 0.7, "数据访问层　MyBatis-Plus ORM", fc=SAGE, fs=9)
box(ax, 0.4, H - 8.75, 6.2, 0.95,
    "MySQL 8.0\n业务表（用户/茶商/商品/订单…） + ai_knowledge 知识库", fc=INK, fs=9)

for y1, y2 in [(H - 2.55, H - 3.0), (H - 3.85, H - 4.2), (H - 6.5, H - 6.75), (H - 7.45, H - 7.8)]:
    arrow(ax, 3.5, y1, 3.5, y2, lw=1.6)

# 右侧外部服务
box(ax, 7.3, H - 3.85, 2.4, 2.95, "", fc="#F1E9D6", ec=GOLD, dashed=True, lw=1.6)
ax.text(8.5, H - 1.15, "外部 AI 服务", ha="center", fontsize=10, color="#6B5316", fontweight="bold")
box(ax, 7.5, H - 2.6, 2.0, 1.1, "DeepSeek API\n多轮对话 · Function\nCalling · SSE 流式", fc="#FBF3DC", ec=GOLD, tc="#4A3A0E", fs=7.5)
box(ax, 7.5, H - 3.75, 2.0, 0.85, "DashScope 百炼\ntext-embedding-v3 向量", fc="#FBF3DC", ec=GOLD, tc="#4A3A0E", fs=7.5)
arrow(ax, 7.3, H - 2.9, 6.6, H - 2.9, color=GOLD, style="<|-|>")
arrow(ax, 7.3, H - 3.4, 6.6, H - 3.4, color=GOLD, style="<|-|>")
box(ax, 7.3, H - 5.6, 2.4, 1.2,
    "降级开关\n无网络 / 无密钥时：\nBM25 单路检索 +\n本地规则推荐", fc="#EFE3C8", ec=GOLD, tc="#4A3A0E", fs=8, dashed=True)
arrow(ax, 8.5, H - 3.85, 8.5, H - 4.4, color=GOLD, ls="--")
ax.text(5.0, 0.35, "图 3-1　系统总体分层架构（虚线 = 外部依赖，可降级）", ha="center", fontsize=9, color="#666666", style="italic")
save(fig, "fig-arch.png")

# ============ 图 4-1 RAG 混合检索流水线 ============
fig, ax = canvas(11, 3.6)
H = 10 * 3.6 / 11
y = H / 2 + 0.55
w, h = 1.42, 1.0
xs = [0.25, 2.0, 3.75, 5.5, 7.25, 9.0]
labels = ["用户提问\n（自然语言）", "混合召回", "RRF 融合\n（k = 60）", "LLM 语义重排\n（剔除不相关）", "拼装 Prompt\n（附引用来源）", "DeepSeek\n流式生成回答"]
for x, t in zip(xs, labels):
    hot = t in ("混合召回", "LLM 语义重排")
    box(ax, x, y, w, h, t, fc="#6B5316" if hot else DEEP, ec=GOLD if hot else INK, fs=8.5)
for i in range(len(xs) - 1):
    arrow(ax, xs[i] + w, y + h / 2, xs[i + 1], y + h / 2, lw=1.5)

# 双路召回展开
box(ax, 2.0, y - 1.35, w, 0.75, "BM25 稀疏召回\n（关键词命中）", fc="#8A6D1F", ec=GOLD, fs=7.5)
box(ax, 3.75, y - 1.35, w, 0.75, "DashScope 稠密向量\n（语义近义）", fc="#8A6D1F", ec=GOLD, fs=7.5)
arrow(ax, 2.0 + w / 2, y, 2.0 + w / 2, y - 0.6, color=GOLD)
arrow(ax, 3.75 + w / 2, y - 0.975, 3.75 + w / 2, y, color=GOLD)
ax.text(2.85, y - 1.85, "ai_knowledge（200 条：领域种子 160 + 站内内容 40）", ha="center", fontsize=7.5, color="#666666")
ax.text(0.25, 0.18, "离线降级：无密钥/无网络时仅保留 BM25 单路，链路不中断", fontsize=8, color="#8A6D1F")
ax.text(5.0, H - 0.18, "", ha="center")
save(fig, "fig-rag.png")

# ============ 图 4-2 ReAct Agent 工具循环 ============
fig, ax = canvas(9.5, 5.6)
H = 10 * 5.6 / 9.5
box(ax, 0.3, H / 2 - 0.5, 1.5, 1.0, "用户消息\n（文本 / 图片）", fc=INK, fs=9)
box(ax, 2.4, H / 2 - 0.5, 1.7, 1.0, "Agent 思考规划\n（ReAct 推理）", fc="#6B5316", ec=GOLD, fs=9)
box(ax, 4.8, H / 2 + 0.7, 1.9, 0.9, "调用工具\n（白名单内选择）", fc=DEEP, fs=9)
box(ax, 7.3, H / 2 + 0.7, 2.2, 0.9, "观察结果回填\n上下文", fc=DEEP, fs=9)
box(ax, 4.8, H / 2 - 2.0, 4.7, 1.4, "", fc="#F1E9D6", ec=GOLD, dashed=True)
ax.text(7.15, H / 2 - 0.75, "工具白名单（参数由服务端校验）", ha="center", fontsize=8, color="#6B5316", fontweight="bold")
tools = ["查订单", "推荐商品", "检索知识", "泡茶指南", "推荐讲座"]
for i, t in enumerate(tools):
    box(ax, 4.95 + i * 0.92, H / 2 - 1.8, 0.86, 0.7, t, fc="#FBF3DC", ec=GOLD, tc="#4A3A0E", fs=7)
box(ax, 4.8, H / 2 - 0.5, 1.9, 1.0, "生成最终回答\n（时间线可视化）", fc=INK, fs=9)
box(ax, 7.3, H / 2 - 0.5, 2.2, 1.0, "前端动作\n推荐卡一键加购", fc="#8A6D1F", ec=GOLD, fs=9)

arrow(ax, 1.8, H / 2, 2.4, H / 2, lw=1.5)
arrow(ax, 4.1, H / 2 + 0.25, 4.8, H / 2 + 1.0, lw=1.5)
arrow(ax, 6.7, H / 2 + 1.15, 7.3, H / 2 + 1.15, lw=1.5)
arrow(ax, 8.4, H / 2 + 0.7, 4.55, H / 2 + 0.25, color=GOLD, ls="--")
ax.text(6.6, H / 2 + 1.55, "观察（Observation）", fontsize=7.5, color="#8A6D1F")
arrow(ax, 8.4, H / 2 + 0.7, 8.4, H / 2 + 0.45, color=GOLD)
arrow(ax, 8.4, H / 2 + 0.45, 4.55, H / 2 + 0.2, color=GOLD, ls="--")
arrow(ax, 4.1, H / 2 - 0.25, 4.8, H / 2 - 0.05, lw=1.5, color=INK)
ax.text(4.45, H / 2 - 0.55, "无需更多工具", fontsize=7, color="#666666")
arrow(ax, 6.7, H / 2, 7.3, H / 2, lw=1.5)
ax.text(4.75, H / 2 - 2.45, "最多 4 轮工具调用；模型自主规划调用次数与检索词改写", fontsize=8, color="#666666")
save(fig, "fig-agent.png")

# ============ 图 4-3 NLQ 自然语言查数 ============
fig, ax = canvas(10.5, 2.9)
H = 10 * 2.9 / 10.5
y = H / 2 - 0.5
w, h = 1.75, 1.0
xs = [0.25, 2.45, 4.65, 6.85, 8.75]
labels = ["运营中文提问\n「各类商品卖多少」", "LLM 意图解析\n输出四类枚举之一", "受限 SQL 模板\n参数化执行", "统计结果集", "ECharts\n条形图渲染"]
for x, t in zip(xs, labels):
    hot = "意图解析" in t
    box(ax, x, y, w if x < 8 else 1.4, h, t, fc="#6B5316" if hot else DEEP, ec=GOLD if hot else INK, fs=8)
for i in range(len(xs) - 1):
    arrow(ax, xs[i] + (w if xs[i] < 8 else 1.4), y + h / 2, xs[i + 1], y + h / 2, lw=1.5)
box(ax, 2.45, y - 1.15, 4.2, 0.65,
    "白名单：sales_by_product / click_by_product / order_status / sales_by_category",
    fc="#F1E9D6", ec=GOLD, tc="#4A3A0E", fs=7)
arrow(ax, 3.3, y, 3.3, y - 0.5, color=GOLD)
ax.text(5.15, 0.12, "模型只「听懂」不「碰库」：输出被约束为枚举意图而非 SQL 文本；解析失败有关键词兜底，不会白屏",
        ha="center", fontsize=8, color="#8A6D1F")
save(fig, "fig-nlq.png")

# ============ 图 6-1 核心数据表关系 ============
fig, ax = canvas(9.5, 6.2)
H = 10 * 6.2 / 9.5
box(ax, 0.4, H - 1.6, 1.8, 0.95, "users\n管理员", fc=INK, fs=9)
box(ax, 0.4, H - 3.3, 1.8, 0.95, "yonghu\n用户", fc=DEEP, fs=9)
box(ax, 0.4, H - 5.0, 1.8, 0.95, "shangjia\n茶商", fc=DEEP, fs=9)
box(ax, 3.4, H - 3.3, 2.0, 0.95, "shangpinxinxi\n茶叶商品", fc="#6B5316", ec=GOLD, fs=9)
box(ax, 6.6, H - 1.6, 1.9, 0.95, "orders\n订单", fc=DEEP, fs=9)
box(ax, 6.6, H - 3.3, 1.9, 0.95, "cart\n购物车", fc=DEEP, fs=9)
box(ax, 6.6, H - 5.0, 1.9, 0.95, "address\n收货地址", fc=DEEP, fs=9)
box(ax, 3.4, H - 6.5, 2.0, 0.95, "coupon / mycoupon\n优惠券", fc=DEEP, fs=9)
box(ax, 6.4, H - 6.9, 3.0, 1.35,
    "ai_knowledge 知识库\n（向量 + 原文 + 来源）\n站内内容自动收割 40 条\n+ 领域种子库 160 条",
    fc="#F1E9D6", ec=GOLD, tc="#4A3A0E", fs=8, dashed=True)

arrow(ax, 2.2, H - 3.0, 3.4, H - 3.0, color=GOLD, style="<|-|>")
ax.text(2.8, H - 2.85, "发布 1:N", fontsize=7.5, ha="center", color="#8A6D1F")
arrow(ax, 2.2, H - 2.0, 6.6, H - 1.15, color=INK, style="<|-")
ax.text(4.3, H - 1.35, "下单 1:N", fontsize=7.5, ha="center", color="#666666")
arrow(ax, 2.2, H - 3.15, 6.6, H - 2.9, color=INK, style="<|-")
ax.text(4.3, H - 3.25, "加购 1:N", fontsize=7.5, ha="center", color="#666666")
arrow(ax, 2.2, H - 3.6, 6.6, H - 4.55, color=INK, style="<|-")
ax.text(4.3, H - 4.35, "收货 1:N", fontsize=7.5, ha="center", color="#666666")
arrow(ax, 5.4, H - 2.5, 6.6, H - 1.15, color=GOLD, style="<|-|>")
ax.text(6.05, H - 1.85, "订购 N:M", fontsize=7.5, ha="center", color="#8A6D1F")
arrow(ax, 5.4, H - 3.1, 6.6, H - 2.9, color=GOLD, style="<|-|>")
ax.text(6.05, H - 3.15, "加入 N:1", fontsize=7.5, ha="center", color="#8A6D1F")
ax.text(5.0, 0.2, "图 6-1　核心数据表关系（省略内容/社区类表）", ha="center", fontsize=9, color="#666666", style="italic")
save(fig, "fig-er.png")

print("all done ->", OUT)
