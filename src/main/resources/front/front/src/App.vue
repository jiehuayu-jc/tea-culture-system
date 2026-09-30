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
		color: #93A396;
		display: inline-block;
		font-size: 14px;
		letter-spacing: 2px;
	}
	.breadcrumb-preview .el-breadcrumb .item2 .el-breadcrumb__inner a {
		color: #E6CE9A;
		display: inline-block;
		font-family: 'TeaSerif', 'STSong', 'SimSun', serif;
		font-size: 30px;
		letter-spacing: 5px;
	}
	.breadcrumb-preview .el-breadcrumb .item3 .el-breadcrumb__inner a {
		color: #E6CE9A;
		display: inline-block;
		font-family: 'TeaSerif', 'STSong', 'SimSun', serif;
		font-size: 30px;
		letter-spacing: 5px;
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
	/* 自托管思源宋体（Heavy）子集 woff2：夜茶主题大标题专用（P1-2：11.31MB -> 0.91MB） */
	@font-face {
		font-family: 'TeaSerif';
		src: url('~@/assets/fonts/SourceHanSerifCN-Heavy-subset.woff2') format('woff2');
		font-display: swap;
	}

	/* ===== 夜茶·墨绿金：内页全局换肤（P1-3 合并两层、去除非内联对抗的） =====
	   特异性约定：结构规则统一加 .main-containers .body-containers 前缀压过页面 scoped；
	   element 组件规则靠「main.js 中 element 先于 App.vue 注入」的加载顺序取胜。
	   仅剩的 用于对抗模板生成的内联 style（外部样式表无法覆盖内联），
	   清零前提是把模板内联样式抽离到组件样式——规模约 70 文件，单独立项执行。 */
	.main-containers .body-containers {
		background: linear-gradient(180deg, #0e2318 0%, #0c1b14 26%, #0c1b14 100%);
		min-height: 100vh;
	}

	/* 面包屑条（模板对该类有内联背景，保留） */
	.main-containers .body-containers .breadcrumb-preview {
		background: rgba(21, 42, 32, .55);
		border: 1px solid rgba(212, 175, 55, .18);
		border-radius: 8px;
		box-shadow: none;
	}
	.main-containers .body-containers .breadcrumb-preview .el-breadcrumb__inner,
	.main-containers .body-containers .breadcrumb-preview .el-breadcrumb__inner a,
	.main-containers .body-containers .breadcrumb-preview .el-breadcrumb__separator {
		color: #E6CE9A;
	}

	/* 分类标签条（模板按钮带内联色，保留） */
	.main-containers .body-containers .item-body .item {
		color: #E6CE9A;
		background: rgba(21, 42, 32, .8);
		border: 1px solid rgba(212, 175, 55, .22);
		border-radius: 4px;
	}
	.main-containers .body-containers .item-body .item.active {
		color: #F6F3EC;
		background: #2E523C;
		border-color: #D4AF37;
	}

	/* 排序条（模板原为 codegen 蓝图背景，改渐变后同位置覆盖，保留） */
	.main-containers .body-containers .sort_view {
		background: linear-gradient(90deg, #12362A 0%, #3E6B4F 100%);
		border-radius: 6px;
	}
	.main-containers .body-containers .sort_view * {
		color: #F6F3EC;
	}

	/* 列表卡片（模板卡片带内联浅底与黑字，保留 收口文字可读性） */
	.main-containers .body-containers .list-preview {
		background: transparent;
	}
	.main-containers .body-containers .list-preview .list-item,
	.main-containers .body-containers .list-preview .list-item1,
	.main-containers .body-containers .list-preview .list-item2 {
		background: linear-gradient(175deg, #16291f, #122219);
		border: 1px solid rgba(212, 175, 55, .16);
		border-radius: 8px;
		box-shadow: 0 4px 16px rgba(0, 0, 0, .25);
		color: #EDE6D6;
		transition: transform .25s ease, border-color .25s ease, box-shadow .25s ease;
	}
	.main-containers .body-containers .list-preview .list-item:hover,
	.main-containers .body-containers .list-preview .list-item1:hover,
	.main-containers .body-containers .list-preview .list-item2:hover {
		transform: translateY(-6px);
		border-color: #D4AF37;
		box-shadow: 0 16px 36px rgba(0, 0, 0, .45);
	}
	.main-containers .body-containers .list-preview .name,
	.main-containers .body-containers .list-preview .title {
		color: #EDE6D6;
		font-family: 'TeaSerif', 'STSong', serif;
		letter-spacing: 1px;
	}
	.main-containers .body-containers .list-preview .price {
		color: #D4AF37;
		font-family: 'TeaSerif', serif;
	}
	.main-containers .body-containers .list-preview .label,
	.main-containers .body-containers .list-preview .text,
	.main-containers .body-containers .list-preview .desc,
	.main-containers .body-containers .list-preview .time_item,
	.main-containers .body-containers .list-preview .publisher_item,
	.main-containers .body-containers .list-preview .collect_item,
	.main-containers .body-containers .list-preview .view_item,
	.main-containers .body-containers .list-preview .like_item {
		color: #93A396;
	}

	/* 详情页 / 表单页 / 社区容器（模板白底内联，保留） */
	.main-containers .body-containers .add-update-preview,
	.main-containers .body-containers .detail-preview,
	.main-containers .body-containers .forum-preview,
	.main-containers .body-containers .center-preview,
	.main-containers .body-containers .messages-preview,
	.main-containers .body-containers .list-form-pv,
	.main-containers .body-containers .select2 {
		background: rgba(21, 42, 32, .85);
		border: 1px solid rgba(212, 175, 55, .16);
		border-radius: 10px;
		box-shadow: 0 6px 24px rgba(0, 0, 0, .35);
	}
	.main-containers .body-containers .detail-preview .item .lable,
	.main-containers .body-containers .list-form-pv .lable,
	.main-containers .body-containers .list-form-pv .label,
	.main-containers .body-containers .select2 .label {
		color: #93A396;
	}
	.main-containers .body-containers .detail-preview .item .text,
	.main-containers .body-containers .detail-preview .name,
	.main-containers .body-containers .detail-preview .text {
		color: #EDE6D6;
	}
	.main-containers .body-containers .detail-preview .text.price,
	.main-containers .body-containers .detail-preview .price {
		color: #D4AF37;
		font-family: 'TeaSerif', serif;
	}
	.main-containers .body-containers .detail-preview .ql-editor,
	.main-containers .body-containers .detail-preview .ql-snow {
		color: #C9C4B4;
	}

	/* 讲座页卡片（容器非 .list-preview，且 .name 自带白底） */
	.main-containers .body-containers .list-item1 .infoBox,
	.main-containers .body-containers .list-item2 .infoBox,
	.main-containers .body-containers .list-item1 .name,
	.main-containers .body-containers .list-item2 .name,
	.main-containers .body-containers .list-item1 .time_item,
	.main-containers .body-containers .list-item2 .time_item,
	.main-containers .body-containers .list-item1 .more_btn,
	.main-containers .body-containers .list-item2 .more_btn {
		color: #EDE6D6;
	}
	.main-containers .body-containers .list-item1 .name,
	.main-containers .body-containers .list-item2 .name {
		background: transparent;
	}
	.main-containers .body-containers .list-item1 .label,
	.main-containers .body-containers .list-item2 .label {
		color: #93A396;
	}

	/* 模板内联的朱砂红按钮统一为主题绿（内联样式只能属性选择器对抗） */
	.main-containers .body-containers [style*="#A63D2F"] {
		background: #3E6B4F;
	}

	/* ---- element 组件：无内联对抗，靠加载顺序 + 特异性，已去 ---- */
	.main-containers .body-containers .el-button--primary {
		background-color: var(--tea-green);
		border-color: var(--tea-green);
	}
	.main-containers .body-containers .el-button--primary:hover,
	.main-containers .body-containers .el-button--primary:focus {
		background-color: var(--tea-green-deep);
		border-color: var(--tea-green-deep);
	}
	.main-containers .body-containers .el-button--text {
		color: var(--tea-green);
	}
	.main-containers .body-containers .el-link.el-link--primary {
		color: var(--tea-green);
	}
	.main-containers .body-containers .el-radio__input.is-checked .el-radio__inner,
	.main-containers .body-containers .el-checkbox__input.is-checked .el-checkbox__inner {
		border-color: var(--tea-green);
		background: var(--tea-green);
	}
	.main-containers .body-containers .el-radio__input.is-checked + .el-radio__label,
	.main-containers .body-containers .el-checkbox__input.is-checked + .el-checkbox__label {
		color: var(--tea-green);
	}
	.main-containers .body-containers .el-pagination.is-background .el-pager li:not(.disabled).active {
		background-color: var(--tea-green);
	}
	.main-containers .body-containers .el-loading-spinner .el-loading-text {
		color: var(--tea-green);
	}
	.main-containers .body-containers .el-loading-spinner .path {
		stroke: var(--tea-green);
	}
	.main-containers .body-containers .el-input__inner,
	.main-containers .body-containers .el-textarea__inner {
		background-color: #152A20;
		border: 1px solid rgba(212, 175, 55, .25);
		color: #EDE6D6;
	}
	.main-containers .body-containers .el-input__inner::placeholder {
		color: rgba(147, 163, 150, .55);
	}
	.main-containers .body-containers .el-input__inner:focus,
	.main-containers .body-containers .el-textarea__inner:focus {
		border-color: var(--tea-green);
	}
	.main-containers .body-containers #pagination.el-pagination .el-pager li,
	.main-containers .body-containers #pagination.el-pagination button {
		background: #152A20;
		color: #C9C4B4;
	}
	.main-containers .body-containers #pagination.el-pagination .el-pager li.active {
		color: #D4AF37;
		border-color: #D4AF37;
	}
	.main-containers .body-containers .el-table {
		background: transparent;
		color: #C9C4B4;
	}
	.main-containers .body-containers .el-table th,
	.main-containers .body-containers .el-table tr,
	.main-containers .body-containers .el-table td {
		background-color: transparent;
		color: #C9C4B4;
		border-bottom: 1px solid rgba(212, 175, 55, .14);
	}
	.main-containers .body-containers .el-table th {
		color: #E6CE9A;
	}
	.main-containers .body-containers .el-table--enable-row-hover .el-table__body tr:hover > td {
		background-color: rgba(212, 175, 55, .07);
	}
	.main-containers .body-containers .el-tabs__item {
		color: #93A396;
	}
	.main-containers .body-containers .el-tabs__item.is-active {
		color: #D4AF37;
	}
	.main-containers .body-containers .el-radio__label,
	.main-containers .body-containers .el-checkbox__label {
		color: #C9C4B4;
	}
	.main-containers .body-containers .el-message {
		background: #152A20;
		border: 1px solid rgba(212, 175, 55, .3);
		color: #EDE6D6;
	}
	.main-containers .body-containers .el-select-dropdown__item.selected {
		color: var(--tea-green);
		font-weight: 600;
	}

</style>
