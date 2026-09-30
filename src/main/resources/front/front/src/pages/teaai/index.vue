<template>
	<div class="teaai-page">
		<div class="login-guide" v-if="needLogin">
			<div class="lg-card">
				<div class="lg-seal">茶</div>
				<div class="lg-t1">茶道AI 需要登录后使用</div>
				<div class="lg-t2">登录后可享：个性化荐茶 · 订单查询 · 一键加购</div>
				<button class="lg-btn" @click="goMenu('/login')">去登录</button>
			</div>
		</div>
		<div class="ai-shell" v-else>
			<aside class="ai-side">
				<div class="side-brand">
					<span class="seal">道</span>
					<h2>茶道AI</h2>
					<p class="en">TEA DAO AGENT</p>
				</div>
				<div class="side-block">
					<div class="side-title">怎么问我</div>
					<div class="chip" v-for="(q, i) in samples" :key="i" @click="ask(q)">{{ q }}</div>
				</div>
				<div class="side-foot">
					<div class="kb-meta" v-if="knowledgeCount > 0">
						茶识 {{ knowledgeCount }} 卷
						<span class="sep">·</span>
						<span class="st" :class="llmOk ? 'on' : 'off'">
							<span class="dot"></span>{{ llmOk ? '智识在线' : '离线茶识' }}
						</span>
					</div>
					AI 生成内容仅供参考<br />茶事有据 · 以盏见心
				</div>
			</aside>

			<main class="ai-main" ref="chatBox">
				<div class="msg" v-for="(m, i) in messages" :key="i" :class="m.role">
					<div class="avatar">{{ m.role === 'user' ? '我' : 'AI' }}</div>
					<div class="bubble">
						<!-- Agent 时间线 -->
						<template v-if="m.steps && m.steps.length">
							<div class="timeline">
								<div class="tl-item" v-for="(s, j) in m.steps" :key="j" :class="s.type">
									<span class="tl-dot"></span>
									<template v-if="s.type === 'tool_call'">
										<span class="tl-text">调用工具 <b>{{ s.name }}</b> {{ s.args }}</span>
									</template>
									<template v-else-if="s.type === 'tool_result'">
										<span class="tl-text">{{ s.summary }}</span>
									</template>
								</div>
							</div>
						</template>
						<div class="text" v-html="m.text"></div>
						<!-- 引用来源 -->
						<div class="sources" v-if="m.sources && m.sources.length">
							<div class="src-title">引用来源</div>
							<div class="src-chip" v-for="(s, j) in m.sources" :key="j" @click="goSource(s)">
								《{{ s.title }}》
							</div>
						</div>
						<!-- 商品卡片（闭环：可加购/购买） -->
						<div class="cards" v-if="m.cards && m.cards.length && m.cards[0].name">
							<div class="p-card" v-for="(c, j) in m.cards" :key="'c' + j">
								<div class="p-info">
									<div class="p-name">{{ c.name }}</div>
									<div class="p-meta">{{ c.category }} · {{ c.guige }}</div>
									<div class="p-price">￥{{ c.price }}</div>
								</div>
								<div class="p-acts">
									<button class="p-btn ghost" @click="goDetail(c)">看详情</button>
									<button class="p-btn solid" @click="addToCart(c)">加入购物车</button>
								</div>
							</div>
						</div>
						<!-- 讲座卡片 -->
						<div class="cards" v-if="m.lectures && m.lectures.length">
							<div class="p-card" v-for="(c, j) in m.lectures" :key="'l' + j">
								<div class="p-info">
									<div class="p-name">{{ c.name }}</div>
									<div class="p-meta">{{ c.time }}</div>
									<div class="p-price" v-if="c.fee != null && c.fee !== ''">￥{{ c.fee }}</div>
								</div>
								<div class="p-acts">
									<button class="p-btn solid" @click="goMenu('/index/xinlizixun')">去预约</button>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="typing" v-if="streaming">AI 正在思考<span class="dots">…</span></div>
			</main>
		</div>

		<footer class="ai-input">
			<input v-model="draft" :disabled="streaming" placeholder="例如：送长辈什么茶好？预算两百以内"
				@keydown.enter="send" />
			<button :disabled="streaming || !draft.trim()" @click="send">发送</button>
		</footer>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				baseUrl: '',
				draft: '',
				needLogin: false,
				typeTimer: null,
				streaming: false,
				knowledgeCount: 0,
				llmOk: false,
				samples: [
					'送长辈什么茶好？预算两百以内',
					'如何泡好一杯龙井？',
					'帮我推荐一款口粮乌龙茶',
					'查一下我最近的订单',
					'最近有什么茶艺讲座？'
				],
				messages: [],
			}
		},
		created() {
			this.baseUrl = this.$config.baseUrl;
			if (!localStorage.getItem('frontToken')) {
				this.needLogin = true;
				return;
			}
			this.loadStatus();
		},
		mounted() {
			this.typeTimer = setInterval(() => this.flushTyping(), 16);
		},
		beforeDestroy() {
			clearInterval(this.typeTimer);
		},
		methods: {
			goMenu(url) {
				this.$router.push(url);
			},
			loadStatus() {
				this.$http.get('ai/status').then(res => {
					if (res.data.code == 0) {
						this.knowledgeCount = res.data.data.knowledge_count || 0;
						this.llmOk = !!res.data.data.llm_configured;
					}
				});
			},
			ask(q) {
				this.draft = q;
				this.send();
			},
			goSource(s) {
				if (s.source_type === 'article') this.$router.push({ path: '/index/jiaoxueshipinDetail', query: { id: s.source_id } });
				else if (s.source_type === 'news') this.$router.push({ path: '/index/newsDetail', query: { id: s.source_id } });
				else if (s.source_type === 'tea') this.$router.push({ path: '/index/shangpinxinxiDetail', query: { id: s.source_id } });
				else if (s.source_type === 'lecture') this.$router.push({ path: '/index/xinlizixunDetail', query: { id: s.source_id } });
			},
			goDetail(c) {
				this.$router.push({ path: '/index/shangpinxinxiDetail', query: { id: c.id } });
			},
			addToCart(c) {
				const token = localStorage.getItem('frontToken');
				if (!token) { this.$message ? this.$message.error('请先登录') : alert('请先登录'); return; }
				this.$http.post('cart/save', {
					userid: Number(localStorage.getItem('frontUserid')),
					tablename: 'shangpinxinxi',
					goodid: c.id,
					goodname: c.name,
					picture: 'upload/art_p' + (((c.id - 1) % 8) + 1) + '.jpg',
					buynumber: 1,
					price: c.price
				}).then(res => {
					if (res.data.code == 0) alert('已加入购物车');
					else alert(res.data.msg || '加购失败');
				});
			},
			esc(s) {
				return String(s).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
			},
			send() {
				const q = this.draft.trim();
				if (!q || this.streaming) return;
				this.draft = '';
				this.streaming = true;
				this.messages.push({ role: 'user', text: this.esc(q) });
				const aiMsg = { role: 'ai', raw: '', shown: 0, text: '', steps: [], sources: [], cards: [], lectures: [], done: false };
				this.messages.push(aiMsg);
				this.scrollToBottom();

				fetch('/springbootj8kskvkr/ai/chat/stream', {
					method: 'POST',
					headers: {
						'Content-Type': 'application/json; charset=utf-8',
						'Token': localStorage.getItem('frontToken') || ''
					},
					body: JSON.stringify({ query: q })
				}).then(resp => {
					if (!resp.ok) {
						// 429 限流 / 400 参数错误：响应体是 JSON 而非 SSE，读出 msg 展示到气泡
						return resp.text().then(t => {
							let msg = '请求失败（HTTP ' + resp.status + '）';
							try { const j = JSON.parse(t); if (j && j.msg) msg = j.msg; } catch (e) { }
							aiMsg.text += '<br>[系统] ' + this.esc(msg);
							this.streaming = false;
							this.scrollToBottom();
						});
					}
					const reader = resp.body.getReader();
					const decoder = new TextDecoder('utf-8');
					let buf = '';
					const parseLine = (line) => {
						if (!line.startsWith('data:')) return;
						let payload;
						try { payload = JSON.parse(line.substring(5).trim()); } catch (e) { return; }
						this.handleEvent(aiMsg, payload);
					};
					const pump = () => {
						const that = this;
						return reader.read().then(({ done, value }) => {
							if (done) { that.streaming = false; that.scrollToBottom(); return; }
							buf += decoder.decode(value, { stream: true });
							const lines = buf.split('\n');
							buf = lines.pop();
							lines.forEach(l => { if (l.trim()) parseLine(l.trim()); });
							that.scrollToBottom();
							return pump();
						});
					};
					return pump();
				}).catch(() => { this.streaming = false; });
			},
			handleEvent(m, payload) {
				const t = payload.type, d = payload.data;
				if (t === 'delta') {
					m.text += this.esc(d).replace(/\n/g, '<br>');
				} else if (t === 'mode') {
					m.steps.push({ type: 'tool_result', summary: '离线演示模式（未配置大模型网络）' });
				} else if (t === 'tool_call') {
					const sp = String(d).split(' ');
					m.steps.push({ type: 'tool_call', name: sp[0], args: sp.slice(1).join(' ') });
				} else if (t === 'tool_result') {
					let summary = '';
					try {
						const j = JSON.parse(d);
						if (j.recommendations) summary = '查询到 ' + j.count + ' 款在售好茶';
						else if (j.hits) summary = '检索到 ' + j.hits.length + ' 条知识片段';
						else if (j.orders) summary = '查到 ' + j.orders.length + ' 条订单';
						else if (j.lectures) summary = '推荐 ' + j.lectures.length + ' 场讲座';
						else if (j.guide) summary = '已取得冲泡建议';
						else summary = d.slice(0, 60);
						if (j.recommendations) m.cards = j.recommendations;
						if (j.lectures) m.lectures = j.lectures;
					} catch (e) { summary = String(d).slice(0, 60); }
					m.steps.push({ type: 'tool_result', summary });
				} else if (t === 'sources') {
					try { m.sources = JSON.parse(d) || []; } catch (e) { }
				} else if (t === 'cards') {
					try {
						const arr = JSON.parse(d) || [];
						if (arr.length && !m.cards.length) m.cards = arr;
					} catch (e) { }
				} else if (t === 'error') {
					m.raw += '\n[系统] ' + d;
				} else if (t === 'done') {
					m.done = true;
				}
			},
			// 打字机渲染：每 16ms 从 raw 推进若干字符到 text，纯前端动画，不占后端线程
			flushTyping() {
				const m = this.messages[this.messages.length - 1];
				if (!m || m.role !== 'ai') return;
				if (m.shown < m.raw.length) {
					m.shown = Math.min(m.raw.length, m.shown + 3);
					m.text = this.esc(m.raw.slice(0, m.shown)).replace(/\n/g, '<br>');
					this.scrollToBottom();
				} else if (m.done && this.streaming) {
					this.streaming = false;
					this.loadStatus();
				}
			},
			scrollToBottom() {
				this.$nextTick(() => {
					const box = this.$refs.chatBox;
					if (box) box.scrollTop = box.scrollHeight;
				});
			}
		}
	}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
	.teaai-page {
		min-height: calc(100vh - 120px);
		background: linear-gradient(180deg, #0e2318 0%, #0c1b14 100%);
		padding: 26px 0 40px;
		color: #ede6d6;
	}
	.ai-shell {
		width: 94%;
		max-width: var(--tea-content);
		margin: 0 auto;
		display: flex;
		gap: 22px;
		align-items: stretch;
	}
	.ai-side {
		width: 250px;
		flex-shrink: 0;
		background: rgba(21, 42, 32, .6);
		border: 1px solid rgba(212, 175, 55, .18);
		border-radius: 8px;
		padding: 26px 20px;
		display: flex;
		flex-direction: column;
	}
	.side-brand {
		.seal {
			display: inline-block;
			width: 42px;
			height: 42px;
			line-height: 42px;
			text-align: center;
			background: #3E6B4F;
			border-radius: 8px;
			color: #f6f3ec;
			font-family: 'TeaSerif', serif;
			font-size: 24px;
		}
		h2 {
			font-family: 'TeaSerif', 'STSong', serif;
			font-size: 26px;
			letter-spacing: 4px;
			margin: 14px 0 4px;
			color: #ede6d6;
		}
		.en { color: #93a396; font-size: 11px; letter-spacing: 3px; }
	}
	.side-block { margin-top: 30px; }
	.side-title {
		color: #e6ce9a;
		letter-spacing: 3px;
		font-size: 13px;
		margin-bottom: 12px;
		border-left: 3px solid #d4af37;
		padding-left: 8px;
	}
	.chip {
		background: rgba(212, 175, 55, .07);
		border: 1px solid rgba(212, 175, 55, .18);
		color: #c9c4b4;
		font-size: 12px;
		border-radius: 4px;
		padding: 8px 10px;
		margin-bottom: 8px;
		cursor: pointer;
		transition: all .2s;
		&:hover { color: #d4af37; border-color: #d4af37; }
	}
	.side-foot { margin-top: auto; color: rgba(147, 163, 150, .5); font-size: 11px; line-height: 1.9; }
	.kb-meta {
		color: rgba(147, 163, 150, .55);
		font-size: 11px;
		letter-spacing: 1px;
		margin-bottom: 10px;
		.sep { margin: 0 4px; opacity: .6; }
		.st {
			display: inline-flex;
			align-items: center;
			gap: 5px;
			&.on { color: rgba(124, 155, 132, .9); }
			&.off { color: rgba(147, 163, 150, .6); }
			.dot {
				width: 5px;
				height: 5px;
				border-radius: 50%;
				background: currentColor;
			}
		}
	}

	.ai-main {
		flex: 1;
		background: rgba(16, 34, 25, .75);
		border: 1px solid rgba(212, 175, 55, .14);
		border-radius: 8px;
		padding: 26px;
		height: 620px;
		overflow-y: auto;
	}
	.msg { display: flex; gap: 14px; margin-bottom: 22px; }
	.avatar {
		width: 38px;
		height: 38px;
		flex-shrink: 0;
		border-radius: 8px;
		text-align: center;
		line-height: 38px;
		font-family: 'TeaSerif', serif;
		background: rgba(212, 175, 55, .14);
		color: #e6ce9a;
	}
	.msg.user .avatar { background: rgba(166, 61, 52, .2); color: #d98a7c; }
	.bubble {
		flex: 1;
		background: rgba(21, 42, 32, .72);
		border: 1px solid rgba(212, 175, 55, .14);
		border-radius: 8px;
		padding: 14px 18px;
		font-size: 14px;
		line-height: 1.9;
		.text { word-break: break-word; }
	}
	.msg.user .bubble { background: rgba(212, 175, 55, .08); border-color: rgba(212, 175, 55, .3); }

	.timeline { margin-bottom: 12px; }
	.tl-item {
		display: flex;
		align-items: center;
		gap: 10px;
		padding: 5px 0;
		color: #93a396;
		font-size: 12.5px;
		.tl-dot {
			width: 8px;
			height: 8px;
			border-radius: 50%;
			background: #d4af37;
			box-shadow: 0 0 8px rgba(212, 175, 55, .6);
			flex-shrink: 0;
		}
		b { color: #e6ce9a; }
	}
	.tl-item.tool_result .tl-dot { background: #7c9b84; box-shadow: none; }

	.sources { margin-top: 14px; }
	.src-title {
		color: #93a396;
		font-size: 12px;
		letter-spacing: 2px;
		margin-bottom: 8px;
	}
	.src-chip {
		display: inline-block;
		margin: 0 8px 6px 0;
		padding: 4px 12px;
		font-size: 12px;
		color: #e6ce9a;
		background: rgba(212, 175, 55, .08);
		border: 1px solid rgba(212, 175, 55, .25);
		border-radius: 4px;
		cursor: pointer;
		&:hover { color: #d4af37; border-color: #d4af37; }
	}

	.cards { margin-top: 14px; display: grid; grid-template-columns: 1fr 1fr; gap: 12px; }
	.p-card {
		display: flex;
		flex-direction: column;
		justify-content: space-between;
		background: rgba(12, 27, 20, .7);
		border: 1px solid rgba(212, 175, 55, .18);
		border-radius: 6px;
		padding: 12px 14px;
	}
	.p-name { color: #ede6d6; font-family: 'TeaSerif', serif; font-size: 16px; letter-spacing: 1px; }
	.p-meta { color: #93a396; font-size: 12px; margin: 4px 0; }
	.p-price { color: #d4af37; font-family: 'TeaSerif', serif; font-size: 20px; }
	.p-acts { display: flex; gap: 8px; margin-top: 10px; }
	.p-btn {
		flex: 1;
		cursor: pointer;
		font-size: 12px;
		padding: 7px 0;
		border-radius: 4px;
		border: 1px solid rgba(212, 175, 55, .4);
		background: transparent;
		color: #e6ce9a;
		&.solid {
			background: linear-gradient(160deg, #e6ce9a, #d4af37);
			color: #14251a;
			border: 0;
			font-weight: 600;
		}
		&:hover { filter: brightness(1.08); }
	}

	.typing { color: #93a396; font-size: 13px; letter-spacing: 2px; padding: 6px 0; }

	.ai-input {
		width: 94%;
		max-width: var(--tea-content);
		margin: 18px auto 0;
		display: flex;
		gap: 12px;
		input {
			flex: 1;
			height: 52px;
			background: rgba(21, 42, 32, .8);
			border: 1px solid rgba(212, 175, 55, .25);
			border-radius: 6px;
			color: #ede6d6;
			padding: 0 18px;
			font-size: 15px;
			outline: none;
			&:focus { border-color: #d4af37; box-shadow: 0 0 0 3px rgba(212, 175, 55, .12); }
		}
		button {
			width: 120px;
			border: 0;
			border-radius: 6px;
			background: linear-gradient(160deg, #e6ce9a, #d4af37);
			color: #14251a;
			font-family: 'TeaSerif', serif;
			font-size: 17px;
			letter-spacing: 6px;
			cursor: pointer;
			&:disabled { opacity: .45; cursor: not-allowed; }
		}
	}

	@media (max-width: 900px) {
		.ai-side { display: none; }
		.cards { grid-template-columns: 1fr; }
	}

	.login-guide {
		min-height: calc(100vh - 120px);
		display: flex;
		align-items: center;
		justify-content: center;
	}
	.lg-card {
		width: 420px;
		background: rgba(21, 42, 32, .75);
		border: 1px solid rgba(212, 175, 55, .2);
		border-radius: 10px;
		padding: 44px 40px;
		text-align: center;
	}
	.lg-seal {
		display: inline-block;
		width: 52px;
		height: 52px;
		line-height: 52px;
		background: #3E6B4F;
		border-radius: 10px;
		color: #f6f3ec;
		font-family: 'TeaSerif', serif;
		font-size: 28px;
		margin-bottom: 18px;
	}
	.lg-t1 { color: #ede6d6; font-family: 'TeaSerif', serif; font-size: 22px; letter-spacing: 2px; margin-bottom: 10px; }
	.lg-t2 { color: #93a396; font-size: 13px; margin-bottom: 26px; }
	.lg-btn {
		width: 100%;
		height: 46px;
		border: 0;
		border-radius: 4px;
		background: linear-gradient(160deg, #e6ce9a, #d4af37);
		color: #14251a;
		font-family: 'TeaSerif', serif;
		font-size: 16px;
		letter-spacing: 6px;
		cursor: pointer;
	}
</style>
