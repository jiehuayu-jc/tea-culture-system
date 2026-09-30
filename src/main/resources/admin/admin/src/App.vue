<template>
	<div id="app" class="">
		<router-view></router-view>
	</div>
</template>

<script>
	export default {
		name: 'app',
		created() {
			
		}
	}
</script>

<style>
	/* P1-2：自托管思源宋体子集（woff2，957KB），登录页与 AI 控制台标题使用 */
	@font-face {
		font-family: 'TeaSerif';
		src: url('~@/assets/fonts/SourceHanSerifCN-Heavy-subset.woff2') format('woff2');
		font-display: swap;
	}

	* {
		padding: 0;
		margin: 0;
		box-sizing: border-box;
	}

	html,
	body {
		width: 100%;
		height: 100%;
	}

	#app {
		height: 100%;
	}

	body {
		padding: 0;
		margin: 0;

	}
</style>