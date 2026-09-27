<template>
	<div class="ai-console">
		<div class="head">
			<span class="seal">道</span>
			<div>
				<div class="t1">茶道AI 控制台</div>
				<div class="t2">自然语言查数据 · AI 写手 · 知识库管理</div>
			</div>
			<div class="status" :class="llmOk ? 'on' : 'off'">{{ llmOk ? '大模型在线' : '离线演示模式' }}</div>
		</div>

		<div class="grid">
			<!-- 自然语言查数据 -->
			<div class="card">
				<div class="c-title">自然语言查数据</div>
				<div class="c-sub">用一句话提问，AI 生成对应统计图表（对应赛项方向 8）</div>
				<div class="q-row">
					<input v-model="question" placeholder="例如：哪种茶卖得最好？/ 订单状态分布" @keydown.enter="runNlq" />
					<el-button type="primary" :loading="nlqLoading" @click="runNlq">生成图表</el-button>
				</div>
				<div class="chips">
					<span class="chip" v-for="(q, i) in nlqSamples" :key="i" @click="question = q; runNlq()">{{ q }}</span>
				</div>
				<div ref="chartBox" class="chart-box" v-show="chartData"></div>
			</div>

			<!-- AI 写手 -->
			<div class="card">
				<div class="c-title">AI 写手</div>
				<div class="c-sub">一键生成商品介绍 / 茶文化文章草稿（对应多模态内容生成）</div>
				<div class="w-row">
					<el-radio-group v-model="writerKind" size="small">
						<el-radio-button label="tea">商品介绍</el-radio-button>
						<el-radio-button label="article">茶文化文章</el-radio-button>
					</el-radio-group>
				</div>
				<div class="w-row">
					<el-input v-model="writerName" placeholder="名称，如：明前特级西湖龙井"></el-input>
				</div>
				<div class="w-row">
					<el-input v-model="writerKeywords" placeholder="要点，如：明前头采 豆香 回甘（可空）"></el-input>
				</div>
				<el-button type="primary" :loading="writerLoading" @click="runWriter">生成草稿</el-button>
				<el-input v-if="writerOut" type="textarea" :rows="9" v-model="writerOut" class="w-out"></el-input>
			</div>
		</div>

		<!-- 知识库 -->
		<div class="card kb">
			<div class="c-title">RAG 知识库</div>
			<div class="c-sub">茶道AI 的回答依据来自站内茶文化文章、资讯、商品介绍与讲座（已收录 {{ kbCount }} 条）</div>
			<el-button type="primary" plain :loading="kbLoading" @click="rebuild">重建知识库</el-button>
			<span class="kb-tip" v-if="kbTip">{{ kbTip }}</span>
		</div>
	</div>
</template>

<script>
	import * as echarts from 'echarts'

	export default {
		data() {
			return {
				llmOk: false,
				kbCount: 0,
				kbLoading: false,
				kbTip: '',
				question: '',
				nlqLoading: false,
				chartData: null,
				nlqSamples: ['哪种茶卖得最好？', '商品浏览量排行', '订单状态分布', '各分类销量对比'],
				writerKind: 'tea',
				writerName: '',
				writerKeywords: '',
				writerLoading: false,
				writerOut: '',
				chart: null,
			}
		},
		created() {
			this.loadStatus();
		},
		methods: {
			loadStatus() {
				this.$http({
					url: '/ai/status',
					method: 'get'
				}).then(({ data }) => {
					if (data.code === 0) {
						this.llmOk = !!data.data.llm_configured;
						this.kbCount = data.data.knowledge_count || 0;
					}
				});
			},
			rebuild() {
				this.kbLoading = true;
				this.$http({
					url: '/ai/knowledge/rebuild',
					method: 'post'
				}).then(({ data }) => {
					this.kbLoading = false;
					if (data.code === 0) {
						this.kbCount = data.data.knowledge_count;
						this.kbTip = '已重建 ' + this.kbCount + ' 条知识';
					} else {
						this.kbTip = data.msg;
					}
				}).catch(() => { this.kbLoading = false; });
			},
			runNlq() {
				if (!this.question.trim()) return;
				this.nlqLoading = true;
				this.$http({
					url: '/ai/admin/nlq',
					method: 'post',
					data: { question: this.question }
				}).then(({ data }) => {
					this.nlqLoading = false;
					if (data.code === 0) {
						this.chartData = data.data;
						this.$nextTick(() => this.renderChart());
					} else {
						this.$message.error(data.msg);
					}
				}).catch(() => { this.nlqLoading = false; });
			},
			renderChart() {
				if (!echarts) return;
				if (!this.chart) {
					this.chart = echarts.init(this.$refs.chartBox);
				}
				this.chart.setOption({
					title: { text: this.chartData.title, left: 'center', textStyle: { color: '#EDE6D6', fontSize: 15 } },
					tooltip: { trigger: 'axis' },
					grid: { left: 60, right: 30, bottom: 60, top: 50 },
					xAxis: { type: 'category', data: this.chartData.categories, axisLabel: { color: '#C9C4B4', rotate: 20, interval: 0 }, axisLine: { lineStyle: { color: 'rgba(212,175,55,.4)' } } },
					yAxis: { type: 'value', axisLabel: { color: '#C9C4B4' }, splitLine: { lineStyle: { color: 'rgba(212,175,55,.12)' } } },
					series: [{
						type: 'bar',
						data: this.chartData.values,
						barMaxWidth: 42,
						itemStyle: { color: '#D4AF37', borderRadius: [4, 4, 0, 0] }
					}]
				});
			},
			runWriter() {
				if (!this.writerName.trim()) { this.$message.error('请填写名称'); return; }
				this.writerLoading = true;
				this.$http({
					url: '/ai/admin/writer',
					method: 'post',
					data: { kind: this.writerKind, name: this.writerName, keywords: this.writerKeywords }
				}).then(({ data }) => {
					this.writerLoading = false;
					if (data.code === 0) {
						this.writerOut = data.data;
					} else {
						this.$message.error(data.msg);
					}
				}).catch(() => { this.writerLoading = false; });
			}
		}
	}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
	.ai-console {
		padding: 20px;
		min-height: calc(100vh - 120px);
		background: linear-gradient(180deg, #0e2318 0%, #0c1b14 100%);
	}
	.head {
		display: flex;
		align-items: center;
		gap: 16px;
		margin-bottom: 22px;
		.seal {
			width: 46px;
			height: 46px;
			line-height: 46px;
			text-align: center;
			background: #a63d2f;
			border-radius: 10px;
			color: #f6f3ec;
			font-family: 'TeaSerif', serif;
			font-size: 26px;
		}
		.t1 { color: #ede6d6; font-family: 'TeaSerif', serif; font-size: 24px; letter-spacing: 3px; }
		.t2 { color: #93a396; font-size: 12px; letter-spacing: 2px; }
		.status {
			margin-left: auto;
			padding: 6px 14px;
			border-radius: 4px;
			font-size: 12px;
			&.on { color: #7c9b84; border: 1px solid rgba(124, 155, 132, .5); }
			&.off { color: #d98a7c; border: 1px solid rgba(166, 61, 52, .5); }
		}
	}
	.grid {
		display: grid;
		grid-template-columns: 1.25fr 1fr;
		gap: 20px;
	}
	.card {
		background: linear-gradient(175deg, #16291f, #122219);
		border: 1px solid rgba(212, 175, 55, .16);
		border-radius: 8px;
		padding: 22px;
		margin-bottom: 20px;
	}
	.c-title { color: #ede6d6; font-family: 'TeaSerif', serif; font-size: 20px; letter-spacing: 2px; margin-bottom: 6px; }
	.c-sub { color: #93a396; font-size: 12px; margin-bottom: 18px; }
	.q-row { display: flex; gap: 12px; margin-bottom: 12px;
		input {
			flex: 1;
			height: 40px;
			background: #0c1b14;
			border: 1px solid rgba(212, 175, 55, .25);
			border-radius: 4px;
			color: #ede6d6;
			padding: 0 14px;
			outline: none;
			&:focus { border-color: #d4af37; }
		}
	}
	.chips { margin: 6px 0 16px;
		.chip {
			display: inline-block;
			margin: 0 8px 8px 0;
			padding: 5px 12px;
			font-size: 12px;
			color: #e6ce9a;
			background: rgba(212, 175, 55, .07);
			border: 1px solid rgba(212, 175, 55, .2);
			border-radius: 4px;
			cursor: pointer;
			&:hover { border-color: #d4af37; color: #d4af37; }
		}
	}
	.chart-box { width: 100%; height: 320px; }
	.w-row { margin-bottom: 14px; }
	.w-out { margin-top: 14px;
		/deep/ textarea { color: #ede6d6; }
	}
	.kb { display: flex; align-items: center; gap: 18px; }
	.kb .c-sub { margin-bottom: 0; margin-right: auto; }
	.kb-tip { color: #7c9b84; font-size: 13px; }
</style>
