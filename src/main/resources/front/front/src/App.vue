<template>
	<router-view></router-view>
</template>

<script>
	export default {
		name: 'App',
		created() {
			
		}
	}
</script>

<style>
	* {
		box-sizing: border-box;
	}

	html,
	body {
		width: 100%;
		height: 100%;
		margin: 0;
		padding: 0;
	}

	[v-cloak] {
		display: none
	}

	.el-tabs__item {
		font-size: 18px;
	}

	.el-loading-spinner .el-loading-text {
		color: #4169E1;
	}

	.el-loading-spinner .path {
		stroke: #4169E1;
	}

	.el-breadcrumb {
		line-height: 60px;
		font-size: 18px;
	}

	.ql-editor {
		/*height: 500px;*/
	}

	.ql-snow .ql-tooltip[data-mode="link"]::before {
		content: "请输入链接地址:";
	}

	.ql-snow .ql-tooltip.ql-editing a.ql-action::after {
		border-right: 0px;
		content: "保存";
		padding-right: 0px;
	}

	.ql-snow .ql-tooltip[data-mode="video"]::before {
		content: "请输入视频地址:";
	}

	.ql-snow .ql-picker.ql-size .ql-picker-label::before,
	.ql-snow .ql-picker.ql-size .ql-picker-item::before {
		content: "14px";
	}

	.ql-snow .ql-picker.ql-size .ql-picker-label[data-value="small"]::before,
	.ql-snow .ql-picker.ql-size .ql-picker-item[data-value="small"]::before {
		content: "10px";
	}

	.ql-snow .ql-picker.ql-size .ql-picker-label[data-value="large"]::before,
	.ql-snow .ql-picker.ql-size .ql-picker-item[data-value="large"]::before {
		content: "18px";
	}

	.ql-snow .ql-picker.ql-size .ql-picker-label[data-value="huge"]::before,
	.ql-snow .ql-picker.ql-size .ql-picker-item[data-value="huge"]::before {
		content: "32px";
	}

	.ql-snow .ql-picker.ql-header .ql-picker-label::before,
	.ql-snow .ql-picker.ql-header .ql-picker-item::before {
		content: "文本";
	}

	.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="1"]::before,
	.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="1"]::before {
		content: "标题1";
	}

	.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="2"]::before,
	.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="2"]::before {
		content: "标题2";
	}

	.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="3"]::before,
	.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="3"]::before {
		content: "标题3";
	}

	.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="4"]::before,
	.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="4"]::before {
		content: "标题4";
	}

	.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="5"]::before,
	.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="5"]::before {
		content: "标题5";
	}

	.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="6"]::before,
	.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="6"]::before {
		content: "标题6";
	}

	.ql-snow .ql-picker.ql-font .ql-picker-label::before,
	.ql-snow .ql-picker.ql-font .ql-picker-item::before {
		content: "标准字体";
	}

	.ql-snow .ql-picker.ql-font .ql-picker-label[data-value="serif"]::before,
	.ql-snow .ql-picker.ql-font .ql-picker-item[data-value="serif"]::before {
		content: "衬线字体";
	}

	.ql-snow .ql-picker.ql-font .ql-picker-label[data-value="monospace"]::before,
	.ql-snow .ql-picker.ql-font .ql-picker-item[data-value="monospace"]::before {
		content: "等宽字体";
	}
	.breadcrumb-preview {
		border-radius: 0;
		padding: 0px 20px;
		margin: 0px auto;
		background: #fff;
		width: 100%;
		border-color: #b2b2b2;
		border-width: 0 0 1px;
		border-style: solid;
	}
	.breadcrumb-preview .el-breadcrumb {
		padding: 0 20px;
		margin: 0 auto;
		background: none;
		max-width: var(--tea-content);
		font-size: 16px;
		line-height: 50px;
		
	}
	.breadcrumb-preview .el-breadcrumb .el-breadcrumb__separator {
		margin: 0 30px;
		color: #666;
		font-weight: 500;
		/* P1-6：显式指定字体栈，避免继承 TeaSerif 导致 '≡' 缺字形渲染成豆腐块 */
		font-family: "SimSun", "STSong", "Songti SC", serif;
	}
	.breadcrumb-preview .el-breadcrumb .item1 .el-breadcrumb__inner a {
		color: #000;
		display: inline-block;
	}
	.breadcrumb-preview .el-breadcrumb .item2 .el-breadcrumb__inner a {
		color: #333;
		display: inline-block;
	}
	.breadcrumb-preview .el-breadcrumb .item3 .el-breadcrumb__inner a {
		color: #3E6B4F;
		display: inline-block;
	}
	.back_box {
		border-radius: 0px;
		padding: 10px 0; /* P1-1 内容令牌收敛 */ max-width: var(--tea-content); margin-left: auto; margin-right: auto;
		margin: 0 auto;
		background: #fff;
		display: flex;
		width: 100%;
		justify-content: flex-end;
	}
	.back_box .backBtn {
		border: 0;
		cursor: pointer;
		border-radius: 4px;
		padding: 0 20px;
		margin: 0;
		color: #fff;
		background: #3E6B4F;
		width: auto;
		font-size: 14px;
		line-height: 36px;
		height: 36px;
	}
	.back_box .backBtn .icon {
		margin: 0 4px 0 0;
		color: #fff;
		font-size: 14px;
	}
	.back_box .backBtn .text {
		color: #fff;
	}
	.back_box .backBtn:hover {
		opacity: 1;
	}
	.back_box .backBtn:hover .icon {
		color: #fff;
	}
	.back_box .backBtn:hover .text {
		color: #fff;
	}
	/* 分页样式 */
	#pagination {
		padding: 0 0; /* P1-1 内容令牌收敛 */ max-width: var(--tea-content); margin-left: auto; margin-right: auto;
		margin: 20px auto;
		overflow: hidden;
		color: #333;
		white-space: nowrap;
		font-weight: 500;
		width: 100%;
		clear: both;
		font-size: 16px;
		text-align: center;
		order: 50;
	}
	#pagination.el-pagination .el-pagination__total {
		margin: 0 10px 0 0;
		color: #666;
		font-weight: 400;
		display: inline-block;
		vertical-align: top;
		font-size: inherit;
		line-height: 28px;
		height: 28px;
	}
	
	#pagination.el-pagination .btn-prev {
		border: none;
		border-radius: 2px;
		padding: 0 10px;
		margin: 0 5px;
		color: #666;
		background: #f4f4f5;
		display: inline-block;
		vertical-align: top;
		font-size: 16px;
		line-height: 28px;
		min-width: 35px;
		height: 28px;
	}
	
	#pagination.el-pagination .btn-next {
		border: none;
		border-radius: 2px;
		padding: 0 10px;
		margin: 0 5px;
		color: #666;
		background: #f4f4f5;
		display: inline-block;
		vertical-align: top;
		font-size: 16px;
		line-height: 28px;
		min-width: 35px;
		height: 28px;
	}
	
	#pagination.el-pagination .btn-prev:disabled {
		border: none;
		cursor: not-allowed;
		border-radius: 2px;
		padding: 0 10px;
		margin: 0 5px;
		color: #C0C4CC;
		background: #f4f4f5;
		display: inline-block;
		vertical-align: top;
		font-size: 16px;
		line-height: 28px;
		height: 28px;
	}
	
	#pagination.el-pagination .btn-next:disabled {
		border: none;
		cursor: not-allowed;
		border-radius: 2px;
		padding: 0 10px;
		margin: 0 5px;
		color: #C0C4CC;
		background: #f4f4f5;
		display: inline-block;
		vertical-align: top;
		font-size: 16px;
		line-height: 28px;
		height: 28px;
	}
	
	#pagination.el-pagination .el-pager {
		padding: 0;
		margin: 0;
		display: inline-block;
		vertical-align: top;
	}
	
	#pagination.el-pagination .el-pager .number {
		cursor: pointer;
		padding: 0 4px;
		margin: 0 5px;
		color: #666;
		display: inline-block;
		vertical-align: top;
		font-size: 16px;
		line-height: 28px;
		border-radius: 2px;
		background: #f4f4f5;
		text-align: center;
		min-width: 30px;
		height: 28px;
	}
	
	#pagination.el-pagination .el-pager .number:hover {
		cursor: pointer;
		padding: 0 4px;
		margin: 0 5px;
		color: #fff;
		display: inline-block;
		vertical-align: top;
		font-size: 16px;
		line-height: 28px;
		border-radius: 2px;
		background: #3E6B4F;
		text-align: center;
		min-width: 30px;
		height: 28px;
	}
	
	#pagination.el-pagination .el-pager .number.active {
		cursor: default;
		padding: 0 4px;
		margin: 0 5px;
		color: #fff;
		display: inline-block;
		vertical-align: top;
		font-size: 16px;
		line-height: 28px;
		border-radius: 2px;
		background: #3E6B4F;
		text-align: center;
		min-width: 30px;
		height: 28px;
	}
	
	#pagination.el-pagination .el-pagination__sizes {
		display: inline-block;
		vertical-align: top;
		font-size: 13px;
		line-height: 28px;
		height: 28px;
	}
	
	#pagination.el-pagination .el-pagination__sizes .el-input {
		margin: 0 5px;
		width: 100px;
		position: relative;
	}
	
	#pagination.el-pagination .el-pagination__sizes .el-input .el-input__inner {
		border: 1px solid #DCDFE6;
		cursor: pointer;
		padding: 0 25px 0 8px;
		color: #606266;
		display: inline-block;
		font-size: 16px;
		line-height: 28px;
		border-radius: 3px;
		outline: 0;
		background: #FFF;
		width: 100%;
		text-align: center;
		height: 28px;
	}
	
	#pagination.el-pagination .el-pagination__sizes .el-input span.el-input__suffix {
		top: 0;
		position: absolute;
		right: 0;
		height: 100%;
	}
	
	#pagination.el-pagination .el-pagination__sizes .el-input .el-input__suffix .el-select__caret {
		cursor: pointer;
		color: #C0C4CC;
		width: 25px;
		font-size: 14px;
		line-height: 28px;
		text-align: center;
	}
	
	#pagination.el-pagination .el-pagination__jump {
		margin: 0 0 0 24px;
		color: #606266;
		display: inline-block;
		vertical-align: top;
		font-size: inherit;
		line-height: 28px;
		height: 28px;
	}
	
	#pagination.el-pagination .el-pagination__jump .el-input {
		border-radius: 3px;
		padding: 0 2px;
		margin: 0 2px;
		display: inline-block;
		width: 50px;
		font-size: inherit;
		line-height: 18px;
		position: relative;
		text-align: center;
		height: 28px;
	}
	
	#pagination.el-pagination .el-pagination__jump .el-input .el-input__inner {
		border: 1px solid #DCDFE6;
		cursor: pointer;
		padding: 0 3px;
		color: #606266;
		display: inline-block;
		font-size: 16px;
		line-height: 26px;
		border-radius: 3px;
		outline: 0;
		background: #FFF;
		width: 100%;
		text-align: center;
		height: 26px;
	}

	/* ===== 夜茶·墨绿金主题令牌（P1-3：移除废弃的宣纸米白体系与 5 个零引用死令牌） ===== */
	:root {
		--tea-ink-deep: #0c1b14;   /* 主题底色（页面背景） */
		--tea-green: #3E6B4F;      /* 茶绿主色 */
		--tea-green-deep: #2E523C; /* 深茶绿 hover */
		--tea-gold: #D4AF37;       /* 主题金：价格、强调、边框 */
		--tea-content: 1400px;     /* P1-1 内容区统一宽度：所有区块唯一的宽度基准 */
	}

	body {
		background: var(--tea-ink-deep);
	}

	/* ElementUI 主色统一为茶绿 */
	.el-button--primary {
		background-color: var(--tea-green);
		border-color: var(--tea-green);
	}
	.el-button--primary:hover,
	.el-button--primary:focus {
		background-color: var(--tea-green-deep);
		border-color: var(--tea-green-deep);
	}
	.el-button--text {
		color: var(--tea-green);
	}
	.el-link.el-link--primary {
		color: var(--tea-green);
	}
	.el-link.el-link--primary:hover {
		color: var(--tea-green-deep);
	}
	.el-radio__input.is-checked .el-radio__inner,
	.el-checkbox__input.is-checked .el-checkbox__inner {
		border-color: var(--tea-green);
		background: var(--tea-green);
	}
	.el-radio__input.is-checked + .el-radio__label,
	.el-checkbox__input.is-checked + .el-checkbox__label {
		color: var(--tea-green);
	}
	.el-pagination.is-background .el-pager li:not(.disabled).active {
		background-color: var(--tea-green);
	}
	.el-loading-spinner .el-loading-text,
	.el-loading-spinner .path {
		color: var(--tea-green) !important;
		stroke: var(--tea-green) !important;
	}
	.el-input__inner:focus,
	.el-textarea__inner:focus {
		border-color: var(--tea-green) !important;
	}

	/* 自托管思源宋体（Heavy）子集 woff2：夜茶主题大标题专用（P1-2：11.31MB -> 0.91MB） */
	@font-face {
		font-family: 'TeaSerif';
		src: url('~@/assets/fonts/SourceHanSerifCN-Heavy-subset.woff2') format('woff2');
		font-display: swap;
	}

	/* 组件级补色：下拉选中 / 轮播箭头 */
	.el-select-dropdown__item.selected {
		color: #3E6B4F !important;
		font-weight: 600;
	}
	.swiper-button-next,
	.swiper-button-prev {
		color: #d4af37 !important;
	}

	/* 分类条/标签条文字对比度 */
	.item-body .item {
		color: #E6CE9A !important;
	}
	.item-body .item.active {
		color: #F6F3EC !important;
		background: #2E523C !important;
	}

	/* ===== 夜茶·墨绿金：内页全局换肤（压过模板内联样式） ===== */
	.body-containers {
		background: linear-gradient(180deg, #0e2318 0%, #0c1b14 26%, #0c1b14 100%) !important;
		min-height: 100vh;
	}

	/* 面包屑条 */
	.breadcrumb-preview {
		background: transparent !important;
		border-bottom: 1px solid rgba(212, 175, 55, .18) !important;
		box-shadow: none !important;
	}
	.breadcrumb-preview a,
	.breadcrumb-preview .item1,
	.breadcrumb-preview .item2 {
		color: #E6CE9A !important;
	}
	.breadcrumb-preview .el-breadcrumb { line-height: 56px !important; }

	/* 分类标签条 */
	.item-body .item {
		color: #E6CE9A !important;
		background: rgba(21, 42, 32, .8) !important;
		border: 1px solid rgba(212, 175, 55, .22) !important;
		border-radius: 4px;
	}
	.item-body .item.active {
		color: #F6F3EC !important;
		background: #2E523C !important;
		border-color: #D4AF37 !important;
	}

	/* 排序条 */
	.sort_view {
		background: linear-gradient(90deg, #12362A 0%, #3E6B4F 100%) !important;
		border-radius: 6px !important;
	}
	.sort_view * {
		color: #F6F3EC !important;
	}

	/* 列表卡片 */
	.list-preview {
		background: transparent !important;
	}
	.list-preview .list-item,
	.list-preview .list-item1,
	.list-preview .list-item2 {
		background: linear-gradient(175deg, #16291f, #122219) !important;
		border: 1px solid rgba(212, 175, 55, .16) !important;
		border-radius: 8px !important;
		box-shadow: 0 4px 16px rgba(0, 0, 0, .25) !important;
		transition: transform .25s ease, border-color .25s ease, box-shadow .25s ease !important;
	}
	.list-preview .list-item:hover,
	.list-preview .list-item1:hover,
	.list-preview .list-item2:hover {
		transform: translateY(-6px) !important;
		border-color: #D4AF37 !important;
		box-shadow: 0 16px 36px rgba(0, 0, 0, .45) !important;
	}
	.list-preview .name,
	.list-preview .title {
		color: #EDE6D6 !important;
		font-family: 'TeaSerif', 'STSong', serif;
		letter-spacing: 1px;
	}
	.list-preview .price {
		color: #D4AF37 !important;
		font-family: 'TeaSerif', serif;
	}
	.list-preview .label,
	.list-preview .text,
	.list-preview .desc,
	.list-preview .time_item,
	.list-preview .publisher_item,
	.list-preview .collect_item,
	.list-preview .view_item,
	.list-preview .like_item {
		color: #93A396 !important;
	}

	/* 详情页 */
	.detail-preview {
		background: linear-gradient(175deg, #16291f, #10201900) !important;
		background-color: #14271d !important;
		border: 1px solid rgba(212, 175, 55, .16) !important;
		border-radius: 10px !important;
	}
	.detail-preview .item .lable,
	.detail-preview .label {
		color: #93A396 !important;
	}
	.detail-preview .item .text,
	.detail-preview .name,
	.detail-preview .text {
		color: #EDE6D6 !important;
	}
	.detail-preview .text.price,
	.detail-preview .price {
		color: #D4AF37 !important;
		font-family: 'TeaSerif', serif;
	}
	.detail-preview .ql-editor,
	.detail-preview .ql-snow {
		color: #C9C4B4 !important;
	}

	/* 表单页 */
	.el-form-item__label {
		color: #93A396 !important;
	}
	.el-input__inner,
	.el-textarea__inner {
		background-color: #152A20 !important;
		border: 1px solid rgba(212, 175, 55, .25) !important;
		color: #EDE6D6 !important;
	}
	.el-input__inner::placeholder {
		color: rgba(147, 163, 150, .55) !important;
	}

	/* 分页 */
	#pagination.el-pagination .el-pager li,
	#pagination.el-pagination button {
		background: #152A20 !important;
		color: #C9C4B4 !important;
	}
	#pagination.el-pagination .el-pager li.active {
		color: #D4AF37 !important;
		border-color: #D4AF37 !important;
	}

	/* 表格类列表页 */
	.el-table {
		background: transparent !important;
		color: #C9C4B4 !important;
	}
	.el-table th,
	.el-table tr,
	.el-table td {
		background-color: transparent !important;
		color: #C9C4B4 !important;
		border-bottom: 1px solid rgba(212, 175, 55, .14) !important;
	}
	.el-table th {
		color: #E6CE9A !important;
	}
	.el-table--enable-row-hover .el-table__body tr:hover > td {
		background-color: rgba(212, 175, 55, .07) !important;
	}
	.el-table::before,
	.el-table::after {
		background-color: rgba(212, 175, 55, .2) !important;
	}

	/* 选项卡 */
	.el-tabs__item {
		color: #93A396 !important;
	}
	.el-tabs__item.is-active {
		color: #D4AF37 !important;
	}

	/* 单选/复选文字 */
	.el-radio__label,
	.el-checkbox__label {
		color: #C9C4B4 !important;
	}

	/* 消息弹窗 */
	.el-message {
		background: #152A20 !important;
		border: 1px solid rgba(212, 175, 55, .3) !important;
		color: #EDE6D6 !important;
	}

	/* ===== 深色主题校正层 =====
	   模板生成的页面使用内联样式 + 浅底深字，主题改为「夜茶·墨绿金」后统一校正。
	   此处必须用 !important：内联 style 优先级高于外部样式表，部分组件还带 scoped 属性。
	   色板：底 #0c1b14 ／ 面板 rgba(21,42,32,·) ／ 主文字 #EDE6D6 ／ 次要 #93A396 ／ 金 #D4AF37 */
	.breadcrumb-preview {
		background: rgba(21, 42, 32, .55) !important;
		border: 1px solid rgba(212, 175, 55, .18) !important;
		border-radius: 8px !important;
		box-shadow: none !important;
	}
	.breadcrumb-preview .el-breadcrumb__inner,
	.breadcrumb-preview .el-breadcrumb__inner a,
	.breadcrumb-preview .el-breadcrumb__inner a:hover,
	.breadcrumb-preview .el-breadcrumb__separator {
		color: #E6CE9A !important;
	}

	/* 搜索面板、筛选面板与表单/详情/社区容器：模板白底与浅灰底 → 深色面板 */
	.add-update-preview,
	.detail-preview,
	.forum-preview,
	.center-preview,
	.messages-preview,
	.list-form-pv,
	.select2 {
		background: rgba(21, 42, 32, .85) !important;
		border: 1px solid rgba(212, 175, 55, .16) !important;
		border-radius: 10px !important;
		box-shadow: 0 6px 24px rgba(0, 0, 0, .35) !important;
	}
	.list-form-pv .lable,
	.list-form-pv .label,
	.select2 .label {
		color: #93A396 !important;
	}

	/* 模板内联的朱砂红按钮统一为主题绿（内联样式只能用属性选择器覆盖） */
	[style*="#A63D2F"] {
		background: #3E6B4F !important;
	}
</style>
