<template>
	<div class="home-preview">

		<!-- ============ Hero：夜茶·墨绿金 ============ -->
		<section class="hero">
			<div class="hero-grain"></div>
			<i class="leaf leaf1"></i><i class="leaf leaf2"></i><i class="leaf leaf3"></i>
			<div class="hero-inner">
				<div class="hero-left">
					<div class="hero-eyebrow">
						<span class="seal">陆羽茶经</span>
						<span class="eyebrow-line"></span>
						<span class="eyebrow-text">CHINESE TEA CULTURE</span>
					</div>
					<h1 class="hero-title">一盏春色<br /><em>半&nbsp;席&nbsp;山&nbsp;河</em></h1>
					<p class="hero-sub">六大茶类 · 源头直采 · 从茶山到你的杯盏</p>
					<div class="hero-actions">
						<button class="btn-gold" @click="goMenu('/index/shangpinxinxi')">进入好茶集市</button>
						<button class="btn-ghost" @click="goMenu('/index/jiaoxueshipin')">品读茶文化</button>
					</div>
					<div class="hero-stats">
						<div class="stat"><b>{{ countUp(products.count) }}</b><span>在售好茶</span></div>
						<div class="stat-line"></div>
						<div class="stat"><b>{{ countUp(news.count) }}</b><span>茶事文章</span></div>
						<div class="stat-line"></div>
						<div class="stat"><b>{{ countUp(4820) }}+</b><span>茶友共盏</span></div>
					</div>
				</div>
				<div class="hero-right">
					<div class="moon-ring">
						<img :src="baseUrl + 'upload/picture1.jpg'" alt="茶" />
					</div>
					<div class="hero-vertical">茶之为饮&nbsp;发乎神农</div>
				</div>
			</div>
			<div class="hero-quote">
				<span class="q-seal">签</span>
				<span class="q-label">今日茶签</span>
				<transition name="qfade" mode="out-in">
					<span class="q-text" :key="quoteIndex">{{ quotes[quoteIndex] }}</span>
				</transition>
			</div>
		</section>

		<!-- ============ 鎏金滚动字幕 ============ -->
		<div class="marquee">
			<div class="marquee-track">
				<span v-for="n in 2" :key="n" class="marquee-group">
					<template v-for="m in marqueeWords">
						<i class="dot">◆</i>{{ m }}
					</template>
				</span>
			</div>
		</div>

		<!-- ============ 栏目入口 ============ -->
		<section class="entries">
			<div class="entry" v-for="(e, i) in entries" :key="i" @click="goMenu(e.url)">
				<div class="entry-num">{{ e.num }}</div>
				<div class="entry-name">{{ e.name }}</div>
				<div class="entry-desc">{{ e.desc }}</div>
				<div class="entry-go">进入<i>→</i></div>
			</div>
		</section>

		<!-- ============ 好茶集市推荐 ============ -->
		<section class="sec">
			<div class="sec-head">
				<div class="sec-title-wrap">
					<span class="sec-title">好茶集市</span>
					<span class="sec-en">TEA&nbsp;MARKET</span>
				</div>
				<div class="sec-more" @click="moreBtn('shangpinxinxi')">更多好茶 →</div>
			</div>
			<div class="goods" v-if="shangpinxinxiRecommend.length">
				<div class="goods-feature" v-if="shangpinxinxiRecommend[0]" @click="toDetail('shangpinxinxiDetail', shangpinxinxiRecommend[0])">
					<div class="gf-img">
						<img v-if="preHttp(shangpinxinxiRecommend[0].shangpintupian)" :src="shangpinxinxiRecommend[0].shangpintupian.split(',')[0]" alt="" />
						<img v-else :src="baseUrl + (shangpinxinxiRecommend[0].shangpintupian?shangpinxinxiRecommend[0].shangpintupian.split(',')[0]:'')" alt="" />
					</div>
					<div class="gf-info">
						<span class="gf-tag">掌柜推荐</span>
						<div class="gf-name">{{ shangpinxinxiRecommend[0].shangpinmingcheng }}</div>
						<div class="gf-cat">{{ shangpinxinxiRecommend[0].shangpinfenlei }}｜{{ shangpinxinxiRecommend[0].guige }}</div>
						<div class="gf-desc">{{ shangpinxinxiRecommend[0].shangpinjieshao }}</div>
						<div class="gf-bottom">
							<span class="gf-price"><i>￥</i>{{ shangpinxinxiRecommend[0].price }}</span>
							<span class="gf-buy">立即品鉴 →</span>
						</div>
					</div>
				</div>
				<div class="goods-grid">
					<div class="g-card" v-for="(item, index) in shangpinxinxiRecommend" :key="index"
						v-if="index > 0 && index < 7" @click="toDetail('shangpinxinxiDetail', item)">
						<div class="g-img">
							<img v-if="preHttp(item.shangpintupian)" :src="item.shangpintupian.split(',')[0]" alt="" />
							<img v-else :src="baseUrl + (item.shangpintupian?item.shangpintupian.split(',')[0]:'')" alt="" />
							<span class="g-cat">{{ item.shangpinfenlei }}</span>
						</div>
						<div class="g-name">{{ item.shangpinmingcheng }}</div>
						<div class="g-row">
							<span class="g-price"><i>￥</i>{{ item.price }}</span>
							<span class="g-spec">{{ item.guige }}</span>
						</div>
					</div>
				</div>
			</div>
		</section>

		<!-- ============ 购物资讯 ============ -->
		<section class="sec sec-news">
			<div class="sec-head">
				<div class="sec-title-wrap">
					<span class="sec-title">购物资讯</span>
					<span class="sec-en">TEA&nbsp;JOURNAL</span>
				</div>
				<div class="sec-more" @click="moreBtn('news')">更多文章 →</div>
			</div>
			<div class="news-list" v-if="newsList.length">
				<div class="n-card" v-for="(item, index) in newsList" :key="index" @click="toDetail('newsDetail', item)">
					<div class="n-img">
						<img :src="baseUrl + item.picture" class="image" />
					</div>
					<div class="n-body">
						<div class="n-date">{{ item.addtime ? item.addtime.split(' ')[0] : '' }}</div>
						<div class="n-title">{{ item.title }}</div>
						<div class="n-desc">{{ item.introduction }}</div>
						<div class="n-meta">
							<span>✎ {{ item.name }}</span>
							<span>♡ {{ item.thumbsupnum }}</span>
							<span>◎ {{ item.clicknum }}</span>
						</div>
					</div>
				</div>
			</div>
		</section>

		<!-- ============ Footer ============ -->
		<footer class="tea-footer">
			<div class="f-line"></div>
			<div class="f-slogan">以&nbsp;茶&nbsp;会&nbsp;友&nbsp;&nbsp;·&nbsp;&nbsp;以&nbsp;盏&nbsp;见&nbsp;心</div>
			<div class="f-copy">茶文化管理系统 © 2026 · 好茶集市 / 茶文化 / 茶友圈 / 线上讲座</div>
		</footer>
	</div>
</template>

<script>
	export default {
		//数据集合
		data() {
			return {
				baseUrl: '',
				newsList: [],
				shangpinxinxiRecommend: [],
				quoteIndex: 0,
				quoteTimer: null,
				quotes: [
					'寒夜客来茶当酒，竹炉汤沸火初红。',
					 '从来佳茗似佳人，欲把西湖比西子。',
					'七碗吃不得也，唯觉两腋习习清风生。',
					'茶禅一味，苦尽甘来。',
				],
				marqueeWords: [
					'明前头采 · 狮峰龙井', '武夷正岩 · 大红袍', '福鼎白毫 · 银针新制',
					'勐海古树 · 普洱陈饼', '桐木关内 · 正山小种', '安溪西坪 · 传统铁观音',
				],
				entries: [
					{ num: '壹', name: '好茶集市', desc: '源头好茶 · 直购价', url: '/index/shangpinxinxi' },
					{ num: '贰', name: '茶文化', desc: '茶史茶艺 · 视频课堂', url: '/index/jiaoxueshipin' },
					{ num: '叁', name: '线上讲座', desc: '名师开讲 · 周周上新', url: '/index/xinlizixun' },
					{ num: '肆', name: '茶友圈', desc: '晒茶问答 · 以茶会友', url: '/index/forum' },
				],
			}
		},
		computed: {
			products() {
				return { count: this.shangpinxinxiRecommend.length ? 16 : 0 };
			},
			news() {
				return { count: this.newsList.length ? 8 : 0 };
			},
		},
		created() {
			this.baseUrl = this.$config.baseUrl;
			this.getNewsList();
			this.getList();
		},
		mounted() {
			this.quoteTimer = setInterval(() => {
				this.quoteIndex = (this.quoteIndex + 1) % this.quotes.length;
			}, 5000);
		},
		beforeDestroy() {
			if (this.quoteTimer) clearInterval(this.quoteTimer);
		},
		//方法集合
		methods: {
			goMenu(url) {
				this.$router.push(url)
			},
			countUp(n) {
				return n;
			},
			preHttp(str) {
				return str && str.substr(0, 4) == 'http';
			},
			getNewsList() {
				let data = {
					page: 1,
					limit: 4,
					sort: 'addtime',
					order: 'desc'
				}
				this.$http.get('news/list', { params: data }).then(res => {
					if (res.data.code == 0) {
						this.newsList = res.data.data.list;
					}
				});
			},
			getList() {
				let autoSortUrl = "shangpinxinxi/autoSort";
				if (localStorage.getItem('frontToken')) {
					autoSortUrl = "shangpinxinxi/autoSort2";
				}
				let data = {
					page: 1,
					limit: 8,
					onshelves: 1,
				}
				this.$http.get(autoSortUrl, { params: data }).then(res => {
					if (res.data.code == 0) {
						this.shangpinxinxiRecommend = res.data.data.list;
					}
				});
			},
			toDetail(path, item) {
				this.$router.push({ path: '/index/' + path, query: { id: item.id } });
			},
			moreBtn(path) {
				this.$router.push({ path: '/index/' + path });
			}
		}
	}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
	$ink: #0c1b14;
	$panel: #10221a;
	$card: #152a20;
	$gold: #d4af37;
	$gold-soft: #e6ce9a;
	$gold-line: rgba(212, 175, 55, .32);
	$text: #ede6d6;
	$muted: #93a396;
	$cinnabar: #b54334;

	.home-preview {
		margin: 0 auto;
		flex-direction: column;
		background: $ink;
		display: flex;
		width: 100%;
		color: $text;
	}

	/* ---------- Hero ---------- */
	.hero {
		position: relative;
		width: 100%;
		padding: 90px 0 70px;
		background:
			radial-gradient(1200px 500px at 85% -10%, rgba(212, 175, 55, .12), transparent 60%),
			radial-gradient(900px 420px at -10% 110%, rgba(53, 94, 59, .5), transparent 60%),
			linear-gradient(160deg, #0e2318 0%, $ink 55%, #0a1610 100%);
		overflow: hidden;
	}
	.hero-grain {
		position: absolute;
		inset: 0;
		background-image: radial-gradient(rgba(230, 206, 154, .05) 1px, transparent 1px);
		background-size: 26px 26px;
		pointer-events: none;
	}
	.leaf {
		position: absolute;
		width: 220px;
		height: 220px;
		border-radius: 0 50% 0 50%;
		border: 1px solid rgba(212, 175, 55, .16);
		background: linear-gradient(135deg, rgba(230, 206, 154, .06), transparent 55%);
		animation: floatLeaf 9s ease-in-out infinite;
	}
	.leaf1 { top: 8%; right: 26%; animation-delay: 0s; }
	.leaf2 { bottom: 6%; left: 4%; width: 150px; height: 150px; animation-delay: -3s; }
	.leaf3 { top: 55%; right: 6%; width: 90px; height: 90px; animation-delay: -6s; }
	@keyframes floatLeaf {
		0%, 100% { transform: translateY(0) rotate(8deg); }
		50% { transform: translateY(-22px) rotate(-6deg); }
	}
	.hero-inner {
		position: relative;
		width: 92%;
		max-width: var(--tea-content);
		margin: 0 auto;
		display: flex;
		align-items: center;
		justify-content: space-between;
		gap: 40px;
		z-index: 2;
	}
	.hero-left { max-width: 760px; }
	.hero-eyebrow {
		display: flex;
		align-items: center;
		gap: 16px;
		margin-bottom: 34px;
		.seal {
			font-family: 'TeaSerif', 'STSong', 'SimSun', serif;
			background: $cinnabar;
			color: #f6f3ec;
			padding: 6px 10px;
			font-size: 15px;
			letter-spacing: 3px;
			border-radius: 4px;
			box-shadow: 0 2px 10px rgba(0, 0, 0, .4);
		}
		.eyebrow-line { width: 64px; height: 1px; background: $gold-line; }
		.eyebrow-text { color: $muted; font-size: 12px; letter-spacing: 5px; }
	}
	.hero-title {
		margin: 0 0 26px;
		font-family: 'TeaSerif', 'STSong', 'SimSun', serif;
		font-size: 88px;
		line-height: 1.14;
		color: $text;
		font-weight: 400;
		letter-spacing: 8px;
		em {
			font-style: normal;
			color: $gold-soft;
			text-shadow: 0 0 34px rgba(212, 175, 55, .35);
		}
	}
	.hero-sub {
		margin: 0 0 40px;
		color: $muted;
		letter-spacing: 4px;
		font-size: 16px;
	}
	.hero-actions {
		display: flex;
		gap: 20px;
		margin-bottom: 52px;
	}
	.btn-gold {
		cursor: pointer;
		background: linear-gradient(160deg, #e6ce9a, #d4af37);
		color: #14251a;
		border: 0;
		padding: 14px 44px;
		font-size: 16px;
		letter-spacing: 4px;
		border-radius: 2px;
		font-family: 'TeaSerif', 'STSong', serif;
		box-shadow: 0 8px 24px rgba(212, 175, 55, .25);
		transition: transform .25s ease, box-shadow .25s ease;
		&:hover { transform: translateY(-3px); box-shadow: 0 14px 30px rgba(212, 175, 55, .4); }
	}
	.btn-ghost {
		cursor: pointer;
		background: transparent;
		color: $gold-soft;
		border: 1px solid $gold-line;
		padding: 14px 44px;
		font-size: 16px;
		letter-spacing: 4px;
		border-radius: 2px;
		font-family: 'TeaSerif', 'STSong', serif;
		transition: all .25s ease;
		&:hover { border-color: $gold; color: $gold; background: rgba(212, 175, 55, .06); }
	}
	.hero-stats {
		display: flex;
		align-items: center;
		gap: 26px;
		.stat {
			b {
				display: block;
				font-family: 'TeaSerif', serif;
				font-size: 34px;
				color: $gold-soft;
				font-weight: 400;
			}
			span { color: $muted; font-size: 12px; letter-spacing: 2px; }
		}
		.stat-line { width: 1px; height: 34px; background: $gold-line; }
	}
	.hero-right {
		position: relative;
		flex-shrink: 0;
		.moon-ring {
			width: 360px;
			height: 360px;
			border-radius: 50%;
			padding: 10px;
			border: 1px solid $gold-line;
			background: radial-gradient(circle at 30% 30%, rgba(230, 206, 154, .1), transparent 60%);
			img {
				width: 100%;
				height: 100%;
				object-fit: cover;
				border-radius: 50%;
				border: 1px solid rgba(230, 206, 154, .25);
				filter: saturate(.9) brightness(.92);
			}
		}
		.hero-vertical {
			position: absolute;
			right: -52px;
			top: 50%;
			transform: translateY(-50%);
			writing-mode: vertical-rl;
			letter-spacing: 10px;
			color: $gold-soft;
			font-family: 'TeaSerif', 'STSong', serif;
			font-size: 18px;
			opacity: .85;
		}
	}
	.hero-quote {
		position: relative;
		z-index: 2;
		width: 92%;
		max-width: var(--tea-content);
		margin: 56px auto 0;
		display: flex;
		align-items: center;
		gap: 14px;
		border: 1px solid $gold-line;
		background: rgba(21, 42, 32, .6);
		padding: 14px 22px;
		border-radius: 3px;
		.q-seal {
			width: 30px;
			height: 30px;
			line-height: 30px;
			text-align: center;
			background: $cinnabar;
			color: #f6f3ec;
			font-family: 'TeaSerif', serif;
			border-radius: 3px;
			font-size: 15px;
		}
		.q-label { color: $gold-soft; letter-spacing: 3px; font-size: 14px; }
		.q-text { color: $text; letter-spacing: 2px; font-size: 15px; }
		.qfade-enter-active, .qfade-leave-active { transition: opacity .6s ease; }
		.qfade-enter, .qfade-leave-to { opacity: 0; }
	}

	/* ---------- 鎏金滚动字幕 ---------- */
	.marquee {
		width: 100%;
		border-top: 1px solid $gold-line;
		border-bottom: 1px solid $gold-line;
		background: linear-gradient(90deg, rgba(212, 175, 55, .05), rgba(212, 175, 55, .12), rgba(212, 175, 55, .05));
		overflow: hidden;
		padding: 13px 0;
	}
	.marquee-track {
		display: flex;
		width: max-content;
		animation: marquee 26s linear infinite;
	}
	.marquee-group {
		display: inline-block;
		white-space: nowrap;
		color: $gold-soft;
		font-family: 'TeaSerif', 'STSong', serif;
		font-size: 16px;
		letter-spacing: 3px;
		padding-right: 30px;
		.dot { color: $gold; font-style: normal; font-size: 10px; margin: 0 18px; vertical-align: 2px; }
	}
	@keyframes marquee {
		from { transform: translateX(0); }
		to { transform: translateX(-50%); }
	}

	/* ---------- 栏目入口 ---------- */
	.entries {
		width: 92%;
		max-width: var(--tea-content);
		margin: 70px auto 0;
		display: grid;
		grid-template-columns: repeat(4, 1fr);
		gap: 22px;
	}
	.entry {
		position: relative;
		background: linear-gradient(170deg, $panel, $card);
		border: 1px solid rgba(212, 175, 55, .14);
		border-radius: 4px;
		padding: 34px 28px 26px;
		cursor: pointer;
		transition: transform .28s ease, border-color .28s ease, box-shadow .28s ease;
		overflow: hidden;
		&::after {
			content: '';
			position: absolute;
			right: -34px;
			top: -34px;
			width: 90px;
			height: 90px;
			transform: rotate(45deg);
			background: rgba(212, 175, 55, .07);
		}
		&:hover {
			transform: translateY(-8px);
			border-color: $gold;
			box-shadow: 0 18px 40px rgba(0, 0, 0, .45);
		}
		.entry-num {
			font-family: 'TeaSerif', 'STSong', serif;
			font-size: 40px;
			color: transparent;
			-webkit-text-stroke: 1px $gold-soft;
			line-height: 1;
			margin-bottom: 18px;
		}
		.entry-name {
			font-family: 'TeaSerif', 'STSong', serif;
			font-size: 22px;
			letter-spacing: 4px;
			color: $text;
			margin-bottom: 8px;
		}
		.entry-desc { color: $muted; font-size: 13px; letter-spacing: 2px; }
		.entry-go {
			margin-top: 20px;
			color: $gold-soft;
			font-size: 13px;
			letter-spacing: 2px;
			opacity: 0;
			transform: translateX(-8px);
			transition: all .28s ease;
			i { font-style: normal; margin-left: 6px; }
		}
		&:hover .entry-go { opacity: 1; transform: translateX(0); }
	}

	/* ---------- 通用 Section ---------- */
	.sec {
		width: 92%;
		max-width: var(--tea-content);
		margin: 96px auto 0;
	}
	.sec-head {
		display: flex;
		align-items: flex-end;
		justify-content: space-between;
		margin-bottom: 40px;
		border-bottom: 1px solid $gold-line;
		padding-bottom: 22px;
	}
	.sec-title-wrap { display: flex; align-items: baseline; gap: 18px; }
	.sec-title {
		font-family: 'TeaSerif', 'STSong', serif;
		font-size: 42px;
		letter-spacing: 8px;
		color: $text;
		position: relative;
		padding-left: 22px;
		&::before {
			content: '';
			position: absolute;
			left: 0;
			top: 8px;
			bottom: 8px;
			width: 4px;
			background: linear-gradient($gold, rgba(212, 175, 55, .1));
		}
	}
	.sec-en { color: rgba(147, 163, 150, .7); letter-spacing: 6px; font-size: 13px; }
	.sec-more {
		color: $gold-soft;
		letter-spacing: 2px;
		cursor: pointer;
		font-size: 14px;
		transition: color .2s;
		&:hover { color: $gold; }
	}

	/* ---------- 商品 ---------- */
	.goods { display: grid; grid-template-columns: 460px 1fr; gap: 24px; }
	.goods-feature {
		background: linear-gradient(175deg, $panel, $card);
		border: 1px solid rgba(212, 175, 55, .16);
		border-radius: 4px;
		overflow: hidden;
		cursor: pointer;
		display: flex;
		flex-direction: column;
		transition: transform .3s ease, box-shadow .3s ease, border-color .3s;
		&:hover {
			transform: translateY(-8px);
			border-color: $gold;
			box-shadow: 0 22px 44px rgba(0, 0, 0, .5);
		}
		.gf-img {
			height: 330px;
			overflow: hidden;
			img { width: 100%; height: 100%; object-fit: cover; transition: transform .6s ease; }
			&:hover img { transform: scale(1.06); }
		}
		.gf-info { padding: 24px 26px 26px; display: flex; flex-direction: column; flex: 1; }
		.gf-tag {
			align-self: flex-start;
			background: $cinnabar;
			color: #f6f3ec;
			font-size: 12px;
			letter-spacing: 3px;
			padding: 4px 10px;
			border-radius: 2px;
			margin-bottom: 14px;
		}
		.gf-name {
			font-family: 'TeaSerif', 'STSong', serif;
			font-size: 26px;
			color: $text;
			letter-spacing: 2px;
			margin-bottom: 8px;
		}
		.gf-cat { color: $gold-soft; font-size: 13px; letter-spacing: 2px; margin-bottom: 14px; }
		.gf-desc {
			color: $muted;
			font-size: 13px;
			line-height: 1.9;
			margin-bottom: 18px;
			display: -webkit-box;
			-webkit-line-clamp: 2;
			-webkit-box-orient: vertical;
			overflow: hidden;
		}
		.gf-bottom {
			margin-top: auto;
			display: flex;
			align-items: baseline;
			justify-content: space-between;
		}
		.gf-price {
			color: $gold;
			font-family: 'TeaSerif', serif;
			font-size: 34px;
			i { font-style: normal; font-size: 16px; margin-right: 2px; }
		}
		.gf-buy { color: $gold-soft; letter-spacing: 2px; font-size: 14px; }
	}
	.goods-grid {
		display: grid;
		grid-template-columns: repeat(3, 1fr);
		gap: 24px;
	}
	.g-card {
		background: linear-gradient(175deg, $panel, $card);
		border: 1px solid rgba(212, 175, 55, .12);
		border-radius: 4px;
		overflow: hidden;
		cursor: pointer;
		transition: transform .28s ease, border-color .28s ease, box-shadow .28s ease;
		&:hover {
			transform: translateY(-8px);
			border-color: $gold;
			box-shadow: 0 16px 36px rgba(0, 0, 0, .45);
		}
		.g-img {
			position: relative;
			height: 170px;
			overflow: hidden;
			img { width: 100%; height: 100%; object-fit: cover; transition: transform .5s ease; }
			&:hover img { transform: scale(1.07); }
			.g-cat {
				position: absolute;
				left: 10px;
				top: 10px;
				background: rgba(12, 27, 20, .78);
				border: 1px solid $gold-line;
				color: $gold-soft;
				font-size: 12px;
				letter-spacing: 2px;
				padding: 3px 10px;
				border-radius: 2px;
			}
		}
		.g-name {
			padding: 14px 16px 4px;
			color: $text;
			font-size: 16px;
			letter-spacing: 1px;
			white-space: nowrap;
			overflow: hidden;
			text-overflow: ellipsis;
		}
		.g-row {
			display: flex;
			align-items: baseline;
			justify-content: space-between;
			padding: 4px 16px 16px;
			.g-price {
				color: $gold;
				font-family: 'TeaSerif', serif;
				font-size: 22px;
				i { font-style: normal; font-size: 13px; }
			}
			.g-spec { color: $muted; font-size: 12px; }
		}
	}

	/* ---------- 资讯 ---------- */
	.sec-news { margin-bottom: 90px; }
	.news-list {
		display: grid;
		grid-template-columns: repeat(2, 1fr);
		gap: 24px;
	}
	.n-card {
		display: flex;
		gap: 20px;
		background: linear-gradient(175deg, $panel, $card);
		border: 1px solid rgba(212, 175, 55, .12);
		border-radius: 4px;
		padding: 18px;
		cursor: pointer;
		transition: transform .28s ease, border-color .28s ease;
		&:hover { transform: translateY(-6px); border-color: $gold; }
		.n-img {
			width: 190px;
			height: 130px;
			flex-shrink: 0;
			border-radius: 3px;
			overflow: hidden;
			img { width: 100%; height: 100%; object-fit: cover; transition: transform .5s; }
			&:hover img { transform: scale(1.06); }
		}
		.n-body { flex: 1; min-width: 0; display: flex; flex-direction: column; }
		.n-date { color: $gold-soft; font-size: 12px; letter-spacing: 2px; margin-bottom: 8px; }
		.n-title {
			font-family: 'TeaSerif', 'STSong', serif;
			color: $text;
			font-size: 19px;
			letter-spacing: 1px;
			margin-bottom: 8px;
			white-space: nowrap;
			overflow: hidden;
			text-overflow: ellipsis;
		}
		.n-desc {
			color: $muted;
			font-size: 13px;
			line-height: 1.8;
			display: -webkit-box;
			-webkit-line-clamp: 2;
			-webkit-box-orient: vertical;
			overflow: hidden;
		}
		.n-meta {
			margin-top: auto;
			padding-top: 10px;
			display: flex;
			gap: 18px;
			color: rgba(147, 163, 150, .75);
			font-size: 12px;
		}
	}

	/* ---------- Footer ---------- */
	.tea-footer {
		background: #081209;
		border-top: 1px solid $gold-line;
		padding: 54px 0 46px;
		text-align: center;
		.f-line {
			width: 56px;
			height: 3px;
			background: linear-gradient(90deg, transparent, $gold, transparent);
			margin: 0 auto 26px;
		}
		.f-slogan {
			font-family: 'TeaSerif', 'STSong', serif;
			color: $gold-soft;
			font-size: 22px;
			letter-spacing: 6px;
			margin-bottom: 16px;
		}
		.f-copy { color: rgba(147, 163, 150, .55); font-size: 12px; letter-spacing: 2px; }
	}

	@media (max-width: 1100px) {
		.hero-title { font-size: 60px; }
		.hero-inner { flex-direction: column; align-items: flex-start; }
		.hero-right { display: none; }
		.goods { grid-template-columns: 1fr; }
		.goods-grid { grid-template-columns: repeat(2, 1fr); }
		.entries { grid-template-columns: repeat(2, 1fr); }
		.news-list { grid-template-columns: 1fr; }
	}
</style>
