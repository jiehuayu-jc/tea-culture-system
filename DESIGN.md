# 设计准则（夜茶 · 墨绿金）

本文档固化本项目的视觉规范与工程约束。**改任何前端样式前先读这里。**

规范来源：本机 `~/.zcode/skills/` 下的 19 个高分设计 skill（`frontend-design`、`high-end-visual-design`、`ui-ux-pro-max`、`design-taste-frontend`、`minimalist-ui`、`web-design-guidelines`、`emil-design-eng`、`design-system`、`ui-styling`、`theme-factory`、`composition-patterns`、`canvas-design`、`animation-vocabulary`、`improve-animations`、`review-animations`、`find-animation-opportunities`、`redesign-existing-projects`、`react-best-practices`、`web-artifacts-builder`）。

---

## 1. 主题令牌

定义位置：`src/main/resources/front/front/src/App.vue` 的 `:root`。**只用这 5 个，不要新增同义令牌，也不要留零引用的死令牌。**

| 令牌 | 值 | 用途 |
|---|---|---|
| `--tea-ink-deep` | `#0c1b14` | 页面底色 |
| `--tea-green` | `#3E6B4F` | 主色（按钮、强调） |
| `--tea-green-deep` | `#2E523C` | 主色 hover |
| `--tea-gold` | `#D4AF37` | 金：价格、active 态、边框点缀 |
| `--tea-content` | `1400px` | 内容区唯一宽度基准 |

派生色（面板、边框、次要文字）用 rgba 表达，不另立令牌：

```
面板底    rgba(21, 42, 32, .85)
卡片底    linear-gradient(175deg, #16291f, #122219)
金边框    rgba(212, 175, 55, .16 ~ .22)
主文字    #EDE6D6
次要文字  #93A396
米金标题  #E6CE9A
```

## 2. 硬性约束

**对比度**：深底上禁止出现 `#000` / `#333` / `#666` 等深色文字。正文对比度不低于 4.5:1，大字不低于 3:1。`minimalist-ui` 明确要求：正文不得使用纯黑。

**单一宽度基准**：所有区块用 `max-width: var(--tea-content)` + `margin: 0 auto`。禁止 `width: 1400px` 这类固定宽度，禁止 `calc((100% - Npx)/2)` 居中（视口小于 N 时表达式变负，直接横向溢出）。

**圆角克制**：卡片/面板 8–10px，按钮与输入框 6px，标签 4px。**禁止对大容器或主按钮使用胶囊形**（`border-radius: 30px+` / `430px`）——`minimalist-ui` 与 `high-end-visual-design` 均列为禁止项。

**色彩克制**：只允许墨绿 + 米白 + 金三个色系。禁止朱砂红 `#A63D2F`、明黄 `#f7db61`、浅蓝 `#d8e9ff` 这类模板残留色。颜色只用于语义（价格、状态、active），不做装饰。

**字体**：`TeaSerif` 为自托管思源宋体子集（woff2，0.91MB），仅用于标题与价格。正文字体栈必须显式声明，**不要让它继承 `TeaSerif`**——子集只含 3826 字，缺字形会渲染成豆腐块（面包屑分隔符 `≡`/`Ξ` 就是这么坏的）。

**动效**：`frontend-design` 指出，每个区块都做 fade-and-slide-up 入场、每张卡片都做 hover 过渡，是最典型的"AI 生成感"。整个页面**只保留一个**编排过的动效时刻，其余保持安静。禁止 `linear` / `ease-in-out`，用有质量的缓动。

## 3. 布局禁令

- 不用绝对定位 + 像素偏移对齐（如 `left: 296px`）。用 flex/grid。
- 不用 `setTimeout` 等异步结果。数据到位后初始化（参考 `index.vue` 的 `initSwiper()`）。
- 不用负 margin 补间距；除有意制造层叠效果（如 `.list` 的 `-22px` 压背景条）外，清楚注明意图。
- 桌面端的非对称布局，`768px` 以下必须回退为单列堆叠。

## 4. `!important` 的使用边界

首要原则：**先用正确的选择器，再考虑 `!important`。**

只允许两种情况使用：

1. **覆盖模板内联样式**。本项目的页面由模板生成，大量样式是内联 `style`，优先级高于外部样式表，只能靠 `!important` 覆盖。全部集中在 App.vue 的「深色主题校正层」。
2. **覆盖 Element UI 默认样式**。

**禁止**用它解决"选择器写错导致覆盖不上"的问题。项目里出现过一次典型事故：换肤规则写成 `.top-container .menu-item .title .text`，而 `.menu-item` 实际是 `.top-container` 的兄弟节点 `.menu-preview` 的子元素，规则从未生效，导航文字在深绿底上保持黑色，长期不可读。**写覆盖规则前先确认 DOM 层级。**

## 5. 已知坑

- `src/main/resources/front/front/dist/` 与 `src/main/resources/static/front/` 是构建产物，已 gitignore。改源码后需 `npm run build` 再复制到 `static/front` 才生效。
- 构建用系统 Node（`npm.cmd`，PowerShell 执行策略会挡 `npm.ps1`）。工具链已从 `node-sass` 迁到 Dart Sass，源码中禁止再出现 `/deep/`，用 `::v-deep`。
- 页面文案与数据来自数据库，字体子集必须覆盖常用字（当前含 GB2312 一级字库）。新增大量生僻字内容时需重新子集化。
