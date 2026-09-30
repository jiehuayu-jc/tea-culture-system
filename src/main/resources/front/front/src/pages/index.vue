<template>
	<div class="main-containers">
		<div class="ai-float" v-if="$route.path != '/index/teaai'" @click="$router.push('/index/teaai')" title="茶道AI">
			<span>道</span>
		</div>
		<div class="body-containers">
			<div class="top-container">
				<!-- info -->
				<div class="top_title">
					<span class="brand-serif" @click="goMenu('/index/home')">茶文化管理系统</span>
				</div>
				<div class="top_tel"></div>
			

				<el-dropdown class="dropdown-box" @command="handleCommand" trigger="click">
					<div class="el-dropdown-link" v-if="Token">
						<img class="top_avatar2" v-if="headportrait&&Token" :src="headportrait?baseUrl + headportrait:require('@/assets/avator.png')">
						<span class="top_label2"></span>
						<span class="top_nickname2">{{username}}</span>
						<span class="icon iconfont icon-xiala"></span>
					</div>
					<div class="el-dropdown-link" v-if="!Token">
						<div class="login-item" @click="toLogin">
							<span class="icon iconfont icon-tijiao16"></span>
							登录
						</div>
					</div>
					<el-dropdown-menu class="top-el-dropdown-menu" slot="dropdown" v-if="Token">
						<el-dropdown-item class="shop-item" :command="'shop'">
							<span class="icon iconfont icon-wuliu8"></span>
							购物车
						</el-dropdown-item>
						<el-dropdown-item class="service-item" :command="'service'">
							<span class="icon iconfont icon-touxiang04"></span>
							智能AI
						</el-dropdown-item>
						<el-dropdown-item v-if="notAdmin" class="user-item" :command="'user'">
							<span class="icon iconfont icon-geren11"></span>
							个人中心
						</el-dropdown-item>
						<el-dropdown-item class="register-item" :command="'register'">
							<span class="icon iconfont icon-fanhui14"></span>
							退出
						</el-dropdown-item>
					</el-dropdown-menu>
				</el-dropdown>
			</div>


			<div class="menu-preview">
				<div class="menu-list">
					<div class="menu-home" :class="activeMenu=='/index/home'?'menu-active':''" @click="goMenu('/index/home')">
						<div class="title">
							<span class="icon iconfont icon-home19"></span>
							<div class="text">首页</div>
						</div>
					</div>
					<div class="menu-item" v-for="(item,index) in menuList" :key="index" @mouseenter="menuShowClick4(index)" @mouseleave="menuShowClick4(-1)" :class="activeMenu==item.url?'menu-active':''" @click.stop="goMenu(item.url)">
						<div class="title">
							<span :class="iconArr[index]"></span>
							<div class="text">{{item.name}}</div>
						</div>
						<transition name="el-zoom-in-top">
							<div v-if="showType4==index&&item.hasCate" class="menu-child-list">
								<div class="child-item" v-for="(items,indexs) in item.cateList" :key="indexs" @click.stop="cateClick(item.url,items)">{{items}}</div>
							</div>
						</transition>
					</div>
					<div class="menu-shop" :class="activeMenu=='/index/cart'?'menu-active':''" @click="goMenu('/index/cart')" v-if="Token">
						<div class="title">
							<span class="icon iconfont icon-shouye-zhihui"></span>
							<div class="text">购物车</div>
						</div>
					</div>
					<div class="menu-service" @click="goChat" v-if="Token">
						<div class="title">
							<span class="icon iconfont icon-shouye-zhihui"></span>
							<div class="text">客服</div>
						</div>
					</div>
					<div class="menu-user" :class="activeMenu=='/index/center'?'menu-active':''" @click="goMenu('/index/center')" v-if="Token && notAdmin">
						<div class="title">
							<span class="icon iconfont icon-shouye-zhihui"></span>
							<div class="text">个人中心</div>
						</div>
					</div>
				</div>
			</div>

<div class="page-hero" v-if="$route.path != '/index/home' && $route.path != '/index/teaai'">
				<div class="ph-grain"></div>
				<div class="ph-inner">
					<div class="ph-eyebrow">CHINESE TEA CULTURE</div>
					<div class="ph-title">{{ pageTitle }}</div>
					<div class="ph-line"></div>
				</div>
				<div class="ph-vertical">茶之为饮&nbsp;发乎神农</div>
			</div>
			<router-view id="scrollView"></router-view>
			
			<div class="bottom-preview">
				<div class="footer"><div v-html="bottomContent"></div></div>
			</div>
		</div>
		
		<el-dialog title="智能AI" :visible.sync="chatFormVisible" width="60%" :before-close="chatClose">
			<div class="chat-content" id="chat-content">
				<div v-bind:key="item.id" v-for="item in chatList">
					<div v-if="item.addtime" style="width: 100%;text-align: center;font-size: 10px;color: #93A396;">{{timeFormat(item.addtime)}}</div>
					<div v-if="item.ask" class="right-content">
						<div style="display: flex;align-items: flex-start;">
							<el-alert v-if="item.type==1" class="text-content" :title="item.ask" :closable="false"
								type="warning"></el-alert>
							<el-image v-else-if="item.type==2" :src="baseUrl + item.ask" style="width: 150px;height: 150px;" fit="cover" :preview-src-list="[baseUrl + item.ask]"></el-image>
							<video v-else-if="item.type==3" :src="baseUrl + item.ask" style="width: 280px;" controls></video>
							<el-button v-else-if="item.type==4" type="primary" size="mini" @click="download(item.ask)">文件预览</el-button>
							<img :src="item.uimage?(baseUrl + item.uimage.split(',')[0]):require('@/assets/avator.png')" style="width: 30px;height: 30px;border-radius: 50%;margin: 0 0 0 5px;" alt="">
						</div>
					</div>
					<div v-else class="left-content">
						<div style="display: flex;align-items: flex-start;">
							<img :src="item.uimage?(baseUrl + item.uimage.split(',')[0]):require('@/assets/AI.png')" style="width: 30px;height: 30px;border-radius: 50%;margin: 0 5px 0 0;" alt="">
							<el-alert v-if="item.type==1" class="text-content" :title="item.reply" :closable="false"
								type="success"></el-alert>
							<el-image v-else-if="item.type==2" :src="baseUrl + item.reply" style="width: 150px;height: 150px;" fit="cover" :preview-src-list="[baseUrl + item.reply]"></el-image>
							<video v-else-if="item.type==3" :src="baseUrl + item.reply" style="width: 280px;" controls></video>
							<el-button v-else-if="item.type==4" type="primary" size="mini" @click="download(item.reply)">文件预览</el-button>
						</div>
					</div>
					<div class="clear-float"></div>
				</div>
			</div>
			<div v-if="aiLoading" v-loading="true" element-loading-background="rgba(255, 255, 255, 0.2)" style="text-align: center">
				AI正在解答您的问题，请稍后...
			</div>
			<div slot="footer" class="dialog-footer">
				<div v-if="askShow"
					style="padding-bottom: 10px;display: flex;align-items: center;justify-content: center;">
					<el-upload class="upload-demo" :action="uploadUrl" :on-success="uploadSuccess" accept=".jpg,.png"
						:show-file-list="false">
						<el-button size="mini" type="success">上传图片</el-button>
					</el-upload>
					<el-upload class="upload-demo" :action="uploadUrl" :on-success="uploadSuccess2" accept=".mp4"
						:show-file-list="false">
						<el-button size="mini" type="success" style="margin: 0 0 0 10px;">上传视频</el-button>
					</el-upload>
					<el-upload class="upload-demo" :action="uploadUrl" :on-success="uploadSuccess3"
						:show-file-list="false">
						<el-button size="mini" type="success" style="margin: 0 0 0 10px;">上传文件</el-button>
					</el-upload>
					<el-button size="mini" type="primary" style="margin: 0 0 0 10px;" @click="askChange">
						转{{askType==1?'人工服务':'智能回复'}}</el-button>
				</div>
				<div style="width: 100%;display: flex;align-items: center;justify-content: space-between;">
					<img style="width: 30px;cursor: pointer;" @click="askShow = !askShow" src="../assets/jiahao.png">
					<el-input @keydown.enter.native="addChat(null)" v-model="form.ask" placeholder="请输入内容" style="width: calc(100% - 110px);">
					</el-input>
					<el-button type="primary" @click="addChat(null)">发送</el-button>
					<div style="position: relative;" v-if="askType==2">
						<span @click="showEmoji=!showEmoji" class="icon iconfont icon-gerenzhongxin-zhihui" style="font-size: 30px;color: #93A396;cursor: pointer;"></span>
						<picker
							:include="['people', 'Smileys']"
							:showSearch="false"
							:showPreview="false"
							:showCategories="false"
							@select="addEmoji"
							v-if="showEmoji"
							:backgroundImageFn="((set,sheetSize)=>{
								return require('@/assets/32.png')
							})"
							style="position: absolute;bottom: 40px;left: -100px;"
						/>
					</div>
				</div>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	import Vue from 'vue'
	import Swiper from "swiper";
	import axios from 'axios'
	import { Picker } from "emoji-mart-vue";
	import timeMethod from '@/common/timeMethod'
	import {
		WebsocketMixin
	} from '@/mixins/WebsocketMixin'
export default {
	components:{
		Picker
	},
	mixins: [WebsocketMixin],
	data() {
		return {
			activeIndex: '0',
			baseUrl: '',
			swiperInstance: null,
			carouselList: [],
			carouselForm: {
				inHome: true,
				inOther: true,
			},
			menuList: [],
			askType: 1, //1为智能回复 2为人工服务
			chatFormVisible: false,
			chatList: [],
			headers: {
				Token: localStorage.getItem('frontToken')
			},
			uploadUrl: this.$config.baseUrl + 'file/upload',
			askShow: false,
			aiLoading: false,
			showEmoji: false,
			form: {
				ask: '',
			},
			headportrait: localStorage.getItem('frontHeadportrait')?localStorage.getItem('frontHeadportrait'):'',
			Token: localStorage.getItem('frontToken'),
			username: localStorage.getItem('username'),
			notAdmin: localStorage.getItem('frontSessionTable')!='"users"',
			iconArr: [
				'el-icon-star-off',
				'el-icon-goods',
				'el-icon-warning',
				'el-icon-question',
				'el-icon-info',
				'el-icon-help',
				'el-icon-picture-outline-round',
				'el-icon-camera-solid',
				'el-icon-video-camera-solid',
				'el-icon-video-camera',
				'el-icon-bell',
				'el-icon-s-cooperation',
				'el-icon-s-order',
				'el-icon-s-platform',
				'el-icon-s-operation',
				'el-icon-s-promotion',
				'el-icon-s-release',
				'el-icon-s-ticket',
				'el-icon-s-management',
				'el-icon-s-open',
				'el-icon-s-shop',
				'el-icon-s-marketing',
				'el-icon-s-flag',
				'el-icon-s-comment',
				'el-icon-s-finance',
				'el-icon-s-claim',
				'el-icon-s-opportunity',
				'el-icon-s-data',
				'el-icon-s-check'
			],
			bottomContent: '',
			showType4: -1,
		}
	},
	async created() {
		this.baseUrl = this.$config.baseUrl;
		this.menuList = this.$config.indexNav;
		this.getCarousel();
		if(localStorage.getItem('frontToken') && localStorage.getItem('frontToken')!=null) {
			this.getSession()
		}
		this.cateList = this.$config.cateList
		if(this.cateList.length){
			for(let x in this.menuList){
				for(let i in this.cateList){
					if(this.menuList[x].name==this.cateList[i].name){
						await this.$http.get(`option/${this.cateList[i].refTable}/${this.cateList[i].refColumn}`).then(rs=>{
							this.menuList[x].cateList = rs.data.data
							this.menuList[x].hasCate = true
						})
					}
				}
			}
		}
	},
	mounted() {
		this.activeIndex = localStorage.getItem('keyPath') || '0';
		// P1-5：banner 改由 getCarousel 数据到达后初始化，不再用 setTimeout 赌时序
	},
	beforeDestroy() {
		if (this.swiperInstance) {
			this.swiperInstance.destroy(true, true);
			this.swiperInstance = null;
		}
	},
	computed: {
		pageTitle() {
			const url = this.activeMenu;
			if (url === '/index/home') return '一盏春色';
			const hit = (this.menuList || []).find(m => m.url === url);
			if (hit) return hit.name;
			const map = { '/index/cart': '购物车', '/index/center': '个人中心' };
			return map[url] || '茶事美学';
		},
		activeMenu() {
			const route = this.$route
			const {
				meta,
				path
			} = route
			// if st path, the sidebar will highlight the path you sete
			if (meta.activeMenu) {
				return meta.activeMenu
			}
			return path
		},
	},
	watch: {
		$route(newValue) {
			let that = this
			let url = window.location.href
			let arr = url.split('#')
			for (let x in this.menuList) {
				if (newValue.path == this.menuList[x].url) {
					this.activeIndex = x
				}
			}
			this.Token = localStorage.getItem('frontToken')
			if(arr[1]!='/index/home'){
				var element = document.getElementById('scrollView');
				var distance = element.offsetTop;
				window.scrollTo( 0, distance )
			}else{
				window.scrollTo( 0, 0 )
			}
		},
		headportrait(){
			this.$forceUpdate()
		},
	},
	methods: {
		cateClick(url,fenlei){
			this.$router.push(url + '?homeFenlei=' + fenlei);
		},
		preHttp(str) {
			return str && str.substr(0,4)=='http';
		},

		async getSession() {
			await this.$http.get(`${localStorage.getItem('UserTableName')}/session`, {emulateJSON: true}).then(async res => {
				if (res.data.code == 0) {
					localStorage.setItem('sessionForm',JSON.stringify(res.data.data))
					localStorage.setItem('frontUserid', res.data.data.id);
					if(res.data.data.vip) {
						localStorage.setItem('vip', res.data.data.vip);
					}
					if(res.data.data.touxiang) {
						this.headportrait = res.data.data.touxiang
						localStorage.setItem('frontHeadportrait', res.data.data.touxiang);
					} else if(res.data.data.headportrait) {
						this.headportrait = res.data.data.headportrait
						localStorage.setItem('frontHeadportrait', res.data.data.headportrait);
					}
				}
			});
		},
		handleSelect(keyPath) {
			if (keyPath) {
				localStorage.setItem('keyPath', keyPath)
			}
		},
		toLogin() {
		  this.$router.push('/login');
		},
		logout() {
			localStorage.clear();
			Vue.http.headers.common['Token'] = "";
			this.$router.push('/index/home');
			this.activeIndex = '0'
			localStorage.setItem('keyPath', this.activeIndex)
			this.Token = ''
			this.$forceUpdate()
			this.$message({
				message: '登出成功',
				type: 'success',
				duration: 1000,
			});
		},
		getCarousel() {
			this.$http.get('config/list', {params: { page: 1, limit: 3 }}).then(res => {
				if (res.data.code == 0) {
					this.carouselList = res.data.data.list;
					// P1-5：数据到位后再初始化 Swiper，避免在空容器上初始化导致布局塌陷
					this.initSwiper();
				}
			});
		},
		initSwiper() {
			this.$nextTick(() => {
				if (!this.carouselList || !this.carouselList.length) return;
				if (!document.querySelector('.mySwiper3')) return;
				if (this.swiperInstance) {
					this.swiperInstance.destroy(true, true);
					this.swiperInstance = null;
				}
				this.swiperInstance = new Swiper('.mySwiper3', {
					navigation: { nextEl: '.swiper-button-next', prevEl: '.swiper-button-prev' },
					pagination: { el: '.swiper-pagination', clickable: true },
					autoplay: { delay: 2500, disableOnInteraction: false },
					effect: 'fade'
				});
			});
		},
		// 轮播图跳转
		carouselClick(url) {
			if (url) {
				if (url.indexOf('https') != -1) {
					window.open(url)
				} else {
					this.$router.push(url)
				}
			}
		},
		carouselChange(){
			let url = window.location.href
			let arr = url.split('#')
			return (this.carouselForm.inHome&&arr[1]=='/index/home')||(this.carouselForm.inOther&&arr[1]!='/index/home')
		},
		goBackend() {
			localStorage.setItem('Token', localStorage.getItem('frontToken'));
			localStorage.setItem('role', localStorage.getItem('frontRole'));
			localStorage.setItem('sessionTable', localStorage.getItem('frontSessionTable'));
			localStorage.setItem('headportrait', localStorage.getItem('frontHeadportrait'));
			localStorage.setItem('userid', localStorage.getItem('frontUserid'));
			window.location.href = `${this.$config.baseUrl}admin/dist/index.html`
			
		},
		formatMessages(messages) {
			let lastTime = null;
			messages.forEach((message, index) => {
				const currentTime = new Date(message.addtime).getTime();
				if (lastTime !== null) {
					const timeDiff = (currentTime - lastTime) / 1000 / 60; // 转换为分钟
					if (timeDiff < 3) {
						message.addtime = ''; // 如果小于3分钟，不显示时间
					}
				}
				lastTime = currentTime;
			});
			return messages;
		},
		timeFormat(time) {
			const Time = timeMethod.getTime(time).split("T");
			//当前消息日期属于周
			const week = timeMethod.getDateToWeek(time);
			//当前日期0时
			const nti = timeMethod.setTimeZero(timeMethod.getNowTime());
			//消息日期当天0时
			const mnti = timeMethod.setTimeZero(timeMethod.getTime(time));
			//计算日期差值
			const diffDate = timeMethod.calculateTime(nti, mnti);
			//本周一日期0时 （后面+1是去除当天时间）
			const fwnti = timeMethod.setTimeZero(timeMethod.countDateStr(-timeMethod.getDateToWeek(timeMethod
				.getNowTime()).weekID + 1));
			//计算周日期差值
			const diffWeek = timeMethod.calculateTime(mnti, fwnti);
		
			if (diffDate === 0) { //消息发送日期删除当天日期如果等于0则是当天时间
				return Time[1].slice(0, 5);
			} else if (diffDate < 172800000) { //当前日期删除消息发送日期小于2天（172800000ms）则是昨天-  一天最大差值前天凌晨00:00:00到今天晚上23:59:59
				return "昨天 " + Time[1].slice(0, 5);
			} else if (diffWeek >= 0) { //消息日期删除本周一日期大于0则是本周
				return week.weekName;
			} else { //其他时间则是日期
				return Time[0].slice(5, 10);
			}
		},
		addEmoji(e) {
			this.form.ask += e.native;
			this.showEmoji = false
		},
		getChatList() {
			this.$http.get('chat/list', {params: { userid: Number(localStorage.getItem('frontUserid')), sort: 'addtime', order: 'asc',limit: 1000 }}).then(res => {
				if (res.data.code == 0) {
					this.chatList = this.formatMessages(res.data.data.list);
					let div = document.getElementsByClassName('chat-content')[0]
					this.$nextTick(() => {
						if (div) div.scrollTop = div.scrollHeight
					})
				}
			});
		},
		addChat(ask=null,type=1) {
			let params = JSON.parse(JSON.stringify(this.form))
			if(params.ask==''&&ask==null){
				this.$message.error('内容不能为空')
				return false
			}
			if(ask){
				params.ask = ask
			}
			params.type = type
			params.uimage = localStorage.getItem('frontHeadportrait')
			params.uname = localStorage.getItem('username')
			params.userid = Number(localStorage.getItem('frontUserid'))
			this.$http.post('chat/add', params).then(res => {
				if (res.data.code == 0) {
					this.getChatList();
					if (this.askType == 1 && ask == null) {
						let ask2 = JSON.parse(JSON.stringify(this.form.ask))
						this.getChathelper(ask2)
					}
					if(this.askType==2){
						this.websocketSend(ask?ask:params.ask)
					}
					this.form.ask = '';
				}
			});
		},
		chatClose() {
			if(this.askType==2){
				this.websocketOnclose();
			}
			this.chatFormVisible = false;
		},
		websocketOnmessage:function(e) {
			this.getChatList()
		},
		goChat() {
			if(!localStorage.getItem('frontToken')) {
				this.toLogin();
				return;
			}
			this.askType = 1
			this.saveChathelper('主人，我是您的智能助手小搏，请问有什么可以帮您！');
			this.getChatList();
			this.chatFormVisible = true;
		},
		uploadSuccess(res) {
			if (res.code == 0) {
				this.askShow = !this.askShow;
				this.addChat('upload/' + res.file,2)
			}
		},
		uploadSuccess2(res) {
			if (res.code == 0) {
				this.askShow = !this.askShow;
				this.addChat('upload/' + res.file,3)
			}
		},
		uploadSuccess3(res) {
			if (res.code == 0) {
				this.askShow = !this.askShow;
				this.addChat('upload/' + res.file,4)
			}
		},
		download(url){
			if(!url){
				return false
			}
			window.open((location.href.split(this.$config.name).length>1 ? location.href.split(this.$config.name)[0] + this.$config.name + '/' + url :this.$config.baseUrl + url))
		},
		getChathelper(ask) {
			this.aiLoading = true
			let div = document.getElementsByClassName('chat-content')[0]
			this.$nextTick(() => {
				if (div) div.scrollTop = div.scrollHeight
			})
			this.$http.post('baidu/askai', {
				ask: `${ask}`,
			}).then(res => {
				if (res.data.code == 0) {
					this.aiLoading = false
					this.saveChathelper(res.data.data);
				}else {
					this.aiLoading = false
					this.saveChathelper('主人，AI还不够聪明，无法理解您的意思！')
				}
			});
		},
		saveChathelper(reply) {
			this.$http.post('chat/save', {
				reply: reply,
				userid: Number(localStorage.getItem('frontUserid')),
				type: 1
			}).then(res => {
				if (res.data.code == 0) {
					this.form.ask = '';
					this.getChatList();
				}
			});
		},
		askChange() {
			this.askShow = !this.askShow;
			this.askType = this.askType == 1 ? 2 : 1
			if(this.askType==1) {
				this.saveChathelper('主人，我是您的智能助手小搏，请问有什么可以帮您！');
				this.websocketOnclose();
			} else if(this.askType==2){
				this.saveChathelper('您好，在线客服很高兴为您服务！');
				this.initWebSocket(1)
			}
		},
		menuShowClick4(index){
			this.showType4 = index
		},
		goMenu(path) {
			this.$router.push(path);
		},
		handleCommand(name){
			if(name == 'register') {
				this.logout()
			}
			else if (name == 'shop') {
				this.goMenu('/index/cart')
			}
			else if (name == 'service') {
				this.goChat()
			}
			else if (name == 'user'){
				this.goMenu('/index/center')
			}
			else if (name == 'login'){
				this.toLogin()
			}
		},
	}
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
	.top-el-dropdown-menu {
		border: 1px solid #EBEEF5;
		border-radius: 4px;
		padding: 10px 0;
		box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
		margin: 18px 0;
		background: transparent;
		.shop-item {
			border: 0;
			padding: 0 8px;
			margin: 0 0px;
			color: inherit;
			background: transparent;
			display: none;
			width: auto;
			font-size: inherit;
			line-height: 32px;
			height: 32px;
			.icon {
				color: inherit;
				font-size: inherit;
			}
		}
		.shop-item:hover {
			color: #EDE6D6;
			background: #475a8330;
		}
		.service-item {
			border: 0;
			padding: 0 8px;
			margin: 0 0px;
			color: inherit;
			background: transparent;
			width: auto;
			font-size: inherit;
			line-height: 32px;
			height: 32px;
			.icon {
				color: inherit;
				font-size: inherit;
			}
		}
		.service-item:hover {
			color: #EDE6D6;
			background: #475a8330;
		}
		.user-item {
			border: 0;
			padding: 0 8px;
			margin: 0 0px;
			color: inherit;
			background: transparent;
			width: auto;
			font-size: inherit;
			line-height: 32px;
			height: 32px;
			.icon {
				color: inherit;
				font-size: inherit;
			}
		}
		.user-item:hover {
			color: #EDE6D6;
			background: #475a8330;
		}
		.register-item {
			border: 0;
			padding: 0 8px;
			margin: 0 0px;
			color: inherit;
			background: transparent;
			width: auto;
			font-size: inherit;
			line-height: 32px;
			height: 32px;
			.icon {
				color: inherit;
				font-size: inherit;
			}
		}
		.register-item:hover {
			color: #EDE6D6;
			background: #475a8330;
		}
	}
	.main-containers {
		.body-containers {
			padding: 0px 0 0;
			margin: 0;
			background: linear-gradient(180deg, #0e2318 0%, #0c1b14 26%, #0c1b14 100%);
			min-height: 100vh;
			position: relative;
			.top-container {
				padding: 0 20px 44px;
				z-index: 1002;
				color: #ede6d6;
				display: flex;
				font-size: 16px;
				box-shadow: 0 0px 0px rgba(64, 158, 255, .3);
				top: 0;
				left: 0;
				/* P1-3：夜茶渐变直接落在组件自身样式（原 #fff 白底是白带根因） */
				background: linear-gradient(180deg, rgba(15, 36, 25, .92) 0%, rgba(12, 27, 20, .88) 100%);
				width: 100%;
				justify-content: flex-start;
				align-items: center;
				position: sticky;
				height: 140px;
				.top_title {
					display: block;
					span {
						padding: 0;
						color: #e6ce9a;
						font-weight: 600;
						font-size: 20px;
						line-height: 44px;
						float: left;
					}
				}
				.top_tel {
					margin: 0 10px;
					color: #ede6d6;
					font-size: 16px;
				}
				.dropdown-box {
					color: inherit;
					display: flex;
					font-size: inherit;
					position: absolute;
					right: 20px;
					.el-dropdown-link {
						color: inherit;
						display: flex;
						font-size: inherit;
						align-items: center;
						.top_avatar2 {
							border-radius: 100%;
							margin: 0 10px;
							object-fit: cover;
							display: inline-block;
							width: 40px;
							height: 40px;
						}
						.top_label2 {
							color: inherit;
							font-size: inherit;
							line-height: 32px;
						}
						.top_nickname2 {
							color: inherit;
							font-size: inherit;
							line-height: 32px;
						}
						.icon {
							margin: 0 0 0 5px;
							color: #93A396;
							font-size: 14px;
						}
						.login-item {
							border: 1px solid rgba(212,175,55,.4);
							padding: 0 16px;
							margin: 0 0px;
							color: #e6ce9a;
							background: rgba(212,175,55,.08);
							width: auto;
							font-size: inherit;
							line-height: 32px;
							height: 32px;
							.icon {
								color: inherit;
								font-size: inherit;
							}
						}
						.login-item:hover {
							color: #d4af37;
							background: rgba(212,175,55,.16);
						}
					}
				}
			}
			.menu-preview {
				.el-scrollbar {
					height: 100%;
			  
					&::v-deep .scrollbar-wrapper-vertical {
						overflow-x: hidden;
					}
			  
					&::v-deep .scrollbar-wrapper-horizontal {
						overflow-y: hidden;
			  
						.el-scrollbar__view {
							white-space: nowrap;
						}
					}
				}
				/* P1-1：菜单回归文档流，用内容令牌居中，去掉绝对定位 + calc 偏移 */
				margin: 0 auto;
				z-index: 1003;
				background: rgba(12, 27, 20, .55);
				width: 100%;
				max-width: var(--tea-content);
				position: relative;
				height: 100px;
				.menu-list {
					padding: 0 10px;
					margin: 0 auto;
					background: none;
					display: flex;
					width: 100%;
					justify-content: center;
					position: relative;
					// 首页
					.menu-home {
						cursor: pointer;
						color: #fff;
						.title {
							cursor: pointer;
							padding: 0 20px;
							color: #ede6d6;
							display: flex;
							border-color: rgba(212,175,55,.18);
							border-width: 0 0 4px 0;
							border-style: solid;
							.icon {
								padding: 0 10px;
								margin: 0;
								color: inherit;
								display: none;
								width: 14px;
								font-size: 14px;
								line-height: 100px;
								height: 100px;
							}
							.text {
								padding: 0 10px;
								color: inherit;
								font-size: 18px;
								line-height: 96px;
								height: 96px;
							}
						}
					}
					.menu-home:hover {
						.title {
							color: #ede6d6;
							border-color: #3E6B4F;
						}
					}
					.menu-home.menu-active {
						.title {
							color: #ede6d6;
							border-color: #3E6B4F;
						}
					}
					// 其他盒子
					.menu-item {
						color: #ede6d6;
						background: none;
						.title {
							cursor: pointer;
							padding: 0 20px;
							color: #ede6d6;
							display: flex;
							border-color: rgba(212,175,55,.18);
							border-width: 0 0 4px 0;
							border-style: solid;
							span {
								padding: 0 10px;
								margin: 0;
								color: inherit;
								display: none;
								width: 14px;
								font-size: 14px;
								line-height: 100px;
								height: 100px;
							}
							.text {
								padding: 0 10px;
								color: inherit;
								font-size: 18px;
								line-height: 96px;
								height: 96px;
							}
						}
						.menu-child-list {
							z-index: 11;
							flex-direction: column;
							background: rgba(255,255,255,.9);
							display: flex;
							width: 200px;
							justify-content: flex-start;
							position: absolute;
							flex-wrap: wrap;
							.child-item {
								cursor: pointer;
								padding: 0 20px;
								color: #EDE6D6;
								width: 100%;
								font-size: 15px;
								line-height: 40px;
							}
							.child-item:hover {
								color: #3E6B4F;
								background: none;
							}
						}
					}
					.menu-item:hover {
						.title {
							color: #ede6d6;
							border-color: #3E6B4F;
						}
					}
					.menu-item.menu-active {
						.title {
							color: #d4af37;
							border-color: #3E6B4F;
						}
					}
					// 购物车
					.menu-shop {
						color: #ede6d6;
						background: none;
						.title {
							cursor: pointer;
							padding: 0 20px;
							color: #ede6d6;
							background: none;
							display: flex;
							border-color: rgba(212,175,55,.18);
							border-width: 0 0 4px 0;
							border-style: solid;
							.icon {
								padding: 0 10px;
								margin: 0;
								color: inherit;
								display: none;
								width: 14px;
								font-size: 14px;
								line-height: 50px;
								height: 50px;
							}
							.text {
								padding: 0 10px;
								color: inherit;
								font-size: 18px;
								line-height: 96px;
								height: 96px;
							}
						}
					}
					.menu-shop:hover {
						.title {
							color: #ede6d6;
							background: none;
							border-color: #3E6B4F;
						}
					}
					.menu-shop.menu-active {
						.title {
							color: #ede6d6;
							background: none;
							border-color: #3E6B4F;
						}
					}
					// 客服
					.menu-service {
						cursor: pointer;
						color: #fff;
						display: none;
						line-height: 50px;
						height: 50px;
						.title {
							padding: 0 20px;
							display: flex;
							height: 50px;
							.icon {
								padding: 0 10px;
								margin: 0;
								color: inherit;
								width: 14px;
								font-size: 14px;
								line-height: 50px;
								height: 50px;
							}
							.text {
								padding: 0 10px;
								color: inherit;
								font-size: 14px;
								line-height: 50px;
								height: 50px;
							}
						}
					}
					.menu-service:hover {
						.title {
							background: #000;
						}
					}
					.menu-service.menu-active {
						.title {
							background: #000;
						}
					}
					// 个人中心
					.menu-user {
						cursor: pointer;
						color: #fff;
						display: none;
						.title {
							padding: 0 20px;
							color: #ede6d6;
							display: flex;
							height: 42px;
							.icon {
								padding: 0 10px;
								margin: 0;
								color: inherit;
								width: 14px;
								font-size: 14px;
								line-height: 60px;
								height: 60px;
							}
							.text {
								padding: 0 10px;
								color: inherit;
								font-size: 14px;
								line-height: 42px;
								height: 42px;
							}
						}
					}
					.menu-user:hover {
						.title {
							color: #E9D9B8;
							background: none;
						}
					}
					.menu-user.menu-active {
						.title {
							color: #E9D9B8;
							background: none;
						}
					}
				}
			}
			.banner-preview {
				margin: 0 auto;
				width: 100%;
				position: relative;
				height: auto;
				.swiper-button-prev:after {
					display:none;
				}
				.swiper-button-next:after {
					display:none;
				}
				.swiper-slide {
					.swiper-item {
						width: 100%;
						height: auto;
						.el-image {
							width: 100%;
							height: 600px;
							/* object-fit 由 <el-image fit="cover"> 作用于内部 <img>，此处不重复声明 */
						}
					}
				}
				@keyframes wave1 {from { left: -236px } to { left: -1233px }}
				@keyframes wave2 {from { left: 0 } to { left: -1009px }}
				.swiper-pagination {
					left: 0;
					bottom: 10px;
					width: 100%;
					::v-deep span.swiper-pagination-bullet {
						border-radius: 100%;
						margin: 0 4px;
						background: #E6CE9A;
						display: inline-block;
						width: 8px;
						opacity: .2;
						height: 8px;
					}
					::v-deep span.swiper-pagination-bullet:hover {
						background: transparent;
						opacity: 1;
					}
					::v-deep span.swiper-pagination-bullet.swiper-pagination-bullet-active {
						background: transparent;
						opacity: 1;
					}
				}
				.swiper-button-next {
					margin: -12px auto 0 0;
					max-width: var(--tea-content);
					top: 50%;
					display: none;
					width: 24px;
					height: 24px;
					.icon {
						color: #fff;
						width: 24px;
						font-size: 24px;
						height: 24px;
					}
				}
				.swiper-button-prev {
					margin: -12px 0 0 auto;
					max-width: var(--tea-content);
					top: 50%;
					display: none;
					width: 24px;
					height: 24px;
					.icon {
						color: #fff;
						width: 24px;
						font-size: 24px;
						height: 24px;
					}
				}
			}
			.bottom-preview {
				width: 100%;
				height: auto;
				.footer {
					padding: 20px 0;
					margin: 0 auto;
					overflow: hidden;
					color: #fff;
					background: var(--tea-ink-deep);
					width: 100%;
					min-height: 120px;
					text-align: center;
					height: auto;
				}
			}
		}
	}
	.chat-content {
		padding-bottom: 20px;
		width: 100%;
		margin-bottom: 10px;
		max-height: 600px;
		height: 600px;
		overflow-y: scroll;
		border: 1px solid #eeeeee;
		background: transparent;

		.left-content {
			float: left;
			margin-bottom: 10px;
			padding: 10px;
			max-width: 80%;
		}

		.right-content {
			float: right;
			margin-bottom: 10px;
			padding: 10px;
			max-width: 80%;
		}
	}

	.clear-float {
		clear: both;
	}
	.emoji-mart[data-v-7bc71df8] {
		font-family: -apple-system, BlinkMacSystemFont, "Helvetica Neue", sans-serif;
		display: -ms-flexbox;
		display: flex;
		-ms-flex-direction: column;
		flex-direction: column;
		height: 420px;
		color: #ffffff;
		border: 1px solid #d9d9d9;
		border-radius: 5px;
		background: transparent;
	}

	/* ===== 夜茶·墨绿金顶栏（背景交给 .top-container 主规则：半透明 + sticky 毛玻璃） ===== */
	.top-container {
		color: #ede6d6;
		box-shadow: 0 2px 16px rgba(0, 0, 0, .4);
		padding-bottom: 0;
	}
	.top-container .top_title span {
		color: #e6ce9a;
		font-family: 'TeaSerif', 'STSong', 'SimSun', serif;
		font-size: 22px;
		letter-spacing: 4px;
	}
	/* 菜单文字色已直接落在 .menu-* 本体上；原 `.top-container .menu-item …` 选择器层级写错（.menu-item 在 .menu-preview 内，非 .top-container 后代）而从未生效，已移除 */
	.top-container .login-item {
		color: #e6ce9a;
	}
	.top-container .menu-item.menu-active .title .text {
		color: #d4af37;
	}
	.top-container .menu-item.menu-active {
		border-bottom: 2px solid #d4af37;
	}
	.top-container .top_nickname2 { color: #ede6d6; }
	.top-container ::v-deep .el-dropdown-menu { background: #12241b; }

	.ai-float {
		position: fixed;
		right: 26px;
		bottom: 30px;
		width: 56px;
		height: 56px;
		border-radius: 50%;
		background: linear-gradient(160deg, #e6ce9a, #d4af37);
		color: #14251a;
		display: flex;
		align-items: center;
		justify-content: center;
		font-family: 'TeaSerif', 'STSong', serif;
		font-size: 26px;
		cursor: pointer;
		z-index: 3000;
		box-shadow: 0 8px 24px rgba(212, 175, 55, .45);
		transition: transform .25s ease;
	}
	.ai-float:hover { transform: translateY(-4px) scale(1.05); }

	/* P1-1：窄屏头部堆叠 + 菜单横向滚动（模板原为固定 1200px 桌面布局） */
	@media (max-width: 992px) {
		.top-container {
			flex-wrap: wrap;
			height: auto;
			padding-bottom: 0;
		}
		.top-container .top_title {
			width: 100%;
		}
		.menu-preview {
			width: 100%;
			height: auto;
			overflow-x: auto;
		}
		.menu-list {
			flex-wrap: nowrap;
			width: max-content;
			min-width: 100%;
		}
		.menu-item .title,
		.menu-home .title {
			padding: 0 14px;
		}
		.menu-item .title .text,
		.menu-home .title .text {
			white-space: nowrap;
		}
	}
</style>
