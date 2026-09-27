<template>
	<div>
	<!-- shangjiazhanghao -->
		<div class="breadcrumb-preview">
			<el-breadcrumb :separator="'≡'">
				<el-breadcrumb-item class="item1" to="/"><a>首页</a></el-breadcrumb-item>
				<el-breadcrumb-item class="item2" v-for="(item, index) in breadcrumbItem" :key="index" :to="'/index/shangpinxinxi?centerType=' + (centerType?'1':'0')"><a>{{item.name}}</a></el-breadcrumb-item>
				<el-breadcrumb-item class="item3"><a href="javascript:void(0);">详情</a></el-breadcrumb-item>
			</el-breadcrumb>
		</div>
		<div class="back_box">
			<el-button class="backBtn" size="mini" @click="backClick">
				<span class="icon iconfont icon-jiantou33"></span>
				<span class="text">返回</span>
			</el-button>
		</div>
		<div class="detail-preview">
			<div class="attr">
				<div class="info">
					<div class="title-item">
						<div class="detail-title">
							{{detail.shangpinmingcheng}}
						</div>
						<div class="colectBtn" @click="storeup(1)" v-show="!isStoreup">
							<i class="icon iconfont icon-shoucang12"></i>
							<span class="text">收藏({{detail.storeupnum}})</span>
						</div>
						<div class="colectBtnActive" @click="storeup(-1)" v-show="isStoreup">
							<i class="icon iconfont icon-shoucang12"></i>
							<span class="text">已收藏({{detail.storeupnum}})</span>
						</div>
					</div>
					<div class="item">
						<div class="coupon-kong"></div>
						<el-button class="couponBtn" type="warning" size="small" @click="couponClick">优惠券</el-button>
					</div>
					<div class="item">
						<div class="lable">限时秒杀</div>
						<count-down v-if="endTime&&startTime"
							class="count-down"
							:startTime="startTime"
							:endTime="endTime"
							:tipText="'距离开始 '"
							tipTextEnd="距离结束 "
							endText="活动已结束"
							dayTxt="天 "
							hourTxt="小时 "
							minutesTxt="分钟 "
							secondsTxt="秒">
						</count-down>
					</div>
					<div class="item" v-if="detail.price">
						<div class="lable">价格</div>
						<div class="text price bold"><span style="font-size: 12px">￥</span>{{detail.price}}</div>
					</div>
					<div class="item" v-if="detail.jf">
						<div class="lable">积分</div>
						<div class="text price bold">{{detail.jf}}</div>
					</div>
					<div class="item">
						<div class="lable">单限</div>
						<div class="text">{{detail.onelimittimes}}</div>
					</div>
					<div class="item">
						<div class="lable">库存</div>
						<div class="text">{{detail.alllimittimes}}</div>
					</div>
					<div class="item">
						<div class="lable">商品编号</div>
						<div class="text "  >{{detail.shangpinbianhao}}</div>
					</div>
					<div class="item">
						<div class="lable">商品分类</div>
						<div class="text "  >{{detail.shangpinfenlei}}</div>
					</div>
					<div class="item">
						<div class="lable">规格</div>
						<div class="text "  >{{detail.guige}}</div>
					</div>
					<div class="item">
						<div class="lable">生产厂商</div>
						<div class="text "  >{{detail.shengchanchangshang}}</div>
					</div>
					<div class="item">
						<div class="lable">茶商账号</div>
						<div class="text  link" @click="merchantClick('shangjiazhanghao')" >{{detail.shangjiazhanghao}}</div>
					</div>
					<div class="item">
						<div class="lable">茶商名称</div>
						<div class="text "  >{{detail.shangjiamingcheng}}</div>
					</div>
					<div class="item">
						<div class="lable">点击次数</div>
						<div class="text "  >{{detail.clicknum}}</div>
					</div>
					<div class="item">
						<div class="lable">评分</div>
						<div class="text "  >{{detail.totalscore}}</div>
					</div>
					<div class="btn_box">
						<el-input-number v-if="detail.alllimittimes" :min=1 v-model="buynumber"></el-input-number>
						<el-button class="addCartBtn" v-if="detail.alllimittimes" type="warning" size="small" @click="addCart">添加到购物车</el-button>
						<el-button class="buyBtn" v-if="detail.alllimittimes" type="warning" size="small" @click="buyNow">立即购买</el-button>

					</div>
					<div class="btn_box">
						<el-button class="editBtn" v-if="btnAuth('shangpinxinxi','修改')" @click="editClick">修改</el-button>
						<el-button class="delBtn" v-if="btnAuth('shangpinxinxi','删除')" @click="delClick">删除</el-button>
					</div>
				</div>
			</div>
		
			<div class="detail-swpier2" v-if="detailBanner.length">
				<div class="swiper21">
					<div class="swiper-container mySwiper21">
						<div class="swiper-wrapper">
							<div class="swiper-slide" v-for="item in detailBanner" :key="item.id">
								<div class="swiper-item">
									<img v-if="item.substr(0,4)=='http'" :src="item" class="image">
									<img v-else :src="baseUrl + item" class="image">
								</div>
							</div>
						</div>
						<div class="swiper-button-prev">
							<span class="icon iconfont icon-jiantou39"></span>
						</div>
						<div class="swiper-button-next">
							<span class="icon iconfont icon-jiantou18"></span>
						</div>
					</div>
				</div>
				<div class="swiper22">
					<div class="swiper-container mySwiper22">
						<div class="swiper-wrapper">
							<div class="swiper-slide" v-for="item in detailBanner" :key="item.id">
								<div class="swiper-item">
									<img v-if="item.substr(0,4)=='http'" :src="item" class="image">
									<img v-else :src="baseUrl + item" class="image">
								</div>
							</div>
						</div>
						<div class="swiper-button-prev">
							<span class="icon iconfont icon-jiantou39"></span>
						</div>
						<div class="swiper-button-next">
							<span class="icon iconfont icon-jiantou18"></span>
						</div>
					</div>
				</div>
			</div>


		

		
			<el-tabs class="detail-tabs" v-model="activeName" type="border-card">
				<el-tab-pane label="商品介绍" name="first">
					<div class="ql-snow ql-editor" v-html="detail.shangpinjieshao"></div>
				</el-tab-pane>
				<el-tab-pane label="评论" name="second">
					<el-form class="add commentForm" :model="form" :rules="rules" ref="form">
						<el-form-item class="item" label="评论" prop="content">
							<editor
								v-model="form.content" 
								class="editor" 
								action="file/upload">
							</editor>
						</el-form-item>
						<el-form-item class="item" label="评分" prop="score">
							<el-rate
								v-model="form.score"
								:max='Number(5)'
								:allow-half='false'
								:low-threshold='Number(2)'
								:high-threshold='Number(4)'
								:show-text='false'
								:texts='["极差", "失望", "一般", "满意", "惊喜"]'
								text-color='#1F2D3D'
								:colors='["#F7BA2A", "#F7BA2A", "#F7BA2A"]'
								void-color='#C6D1DE'
								disabled-void-color='#EFF2F7'
								:icon-classes='["el-icon-star-on", "el-icon-star-on","el-icon-star-on"]'
								void-icon-class='el-icon-star-off'
								disabled-void-icon-class='el-icon-star-on'
								:show-score='false'
							>
							</el-rate>
						</el-form-item>
						<el-form-item class="commentBtn">
							<el-button class="submitBtn" type="primary" @click="submitForm('form')">立即提交</el-button>
							<el-button class="resetBtn" @click="resetForm('form')">重置</el-button>
						</el-form-item>
					</el-form>
				
					<div v-if="infoList.length" class="comment-list">
						<div class="comment-item" v-for="item in infoList" :key="item.id" @mouseenter="discussEnter(item.id)"
							@mouseleave="discussLeave">
							<div class="istop" v-if="item.istop">
								<span class="icon iconfont icon-jiantou24"></span>
							</div>
							<div class="user">
								<el-image v-if="item.avatarurl" :size="50" :src="baseUrl + item.avatarurl"></el-image>
								<el-image v-if="!item.avatarurl" :size="50" :src="require('@/assets/touxiang.png')"></el-image>
								<div class="name">{{item.nickname}}</div>
							</div>
							<div class="comment-content-box">
								<div class="ql-snow ql-editor" v-html="item.content"></div>
								<el-rate
									v-model="item.score"
									disabled
									:max='Number(5)'
									:allow-half='false'
									:low-threshold='Number(2)'
									:high-threshold='Number(4)'
									:show-text='false'
									:texts='["极差", "失望", "一般", "满意", "惊喜"]'
									text-color='#1F2D3D'
									:colors='["#F7BA2A", "#F7BA2A", "#F7BA2A"]'
									void-color='#C6D1DE'
									disabled-void-color='#EFF2F7'
									:icon-classes='["el-icon-star-on", "el-icon-star-on","el-icon-star-on"]'
									void-icon-class='el-icon-star-off'
									disabled-void-icon-class='el-icon-star-on'
									:show-score='false'
								>
								</el-rate>
								<div class="zancai-box">
									<div v-if="!comcaiChange(item)" class="zan-item" :class="comzanChange(item)?'active':''" @click="comzanClick(item)">
										<span class="icon iconfont" :class="comzanChange(item)?'icon-zan11':'icon-zan07'"></span>
										<span class="label">{{comzanChange(item)?'已赞':'赞'}}</span>
										<span class="num">({{item.thumbsupnum}})</span>
									</div>
									<div v-if="!comzanChange(item)" class="cai-item" :class="comcaiChange(item)?'active':''" @click="comcaiClick(item)">
										<span class="icon iconfont" :class="comcaiChange(item)?'icon-cai16':'icon-cai01'"></span>
										<span class="label">{{comcaiChange(item)?'已踩':'踩'}}</span>
										<span class="num">({{item.crazilynum}})</span>
									</div>
								</div>
								<div class="comment-btn">
									<!-- <el-button :style='{"border":"0","cursor":"pointer","padding":"0 20px","margin":"0 10px","color":"#fff","borderRadius":"0px","background":"#3E6B4F","width":"auto","lineHeight":"32px","fontSize":"14px","minWidth":"90px","height":"32px"}'>回复</el-button> -->
									<el-button class="delBtn" v-if="showIndex==item.id&&userid==item.userid" @click="discussDel(item.id)">删除</el-button>
								</div>
							</div>
							<div class="comment-content-box" v-if="item.reply">
								回复：<span class="ql-snow ql-editor" v-html="item.reply"></span>
							</div>
						</div>
					</div>
				
					<el-pagination
						background
						id="pagination" class="pagination"
						:pager-count="7"
						:page-size="pageSize"
						prev-text="上一页"
						next-text="下一页"
						:hide-on-single-page="false"
						:layout='["total","prev","pager","next","sizes","jumper"].join()'
						:total="total"
						@current-change="curChange"
						@prev-click="prevClick"
						@next-click="nextClick"
						></el-pagination>
				</el-tab-pane>
			</el-tabs>

			<div class="idea1"></div>
			<div class="idea2"></div>
		</div>
		<div class="share_view">
		</div>
		<el-dialog title="优惠券" :visible.sync="couponVisible" width="85%">
			<div class="coupon_list">
				<div class="coupon" v-for="(item,index) in couponList" :key="index">
					<div class="coupon_left">
						<div class="name">{{item.name}}</div>
						<div class="name">满{{item.fullamount}}减{{item.discountamount}}</div>
						<div class="date">{{item.startime}}-{{item.endtime}}</div>
						<div class="date">{{item.remark}}</div>
						<div class="date1" v-if="item.shangjiazhanghao">仅限 {{item.shangjiazhanghao}} 商品可使用</div>
						<div class="date1" v-else>全品类商品可使用</div>
					</div>
					<div class="coupon_right">
						<div class="price">￥<span class="num">{{item.discountamount}}</span></div>
						<div class="btn" @click="getCoupon(item)">立即领取</div>
					</div>
				</div>
				<div class="noList" v-if="!couponList.length">
					暂无优惠券
				</div>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	import CountDown from '@/components/CountDown';
	import axios from 'axios'
	import Swiper from "swiper";
	export default {
		//数据集合
		data() {
			return {
				tablename: 'shangpinxinxi',
				baseUrl: '',
				breadcrumbItem: [
					{
						name: '好茶集市'
					}
				],
				title: '',
				detailBanner: [],
				userid: localStorage.getItem('frontUserid'),
				startTime:0,
				endTime: 0,
				id: 0,
				detail: {},
				activeName: 'second',
				form: {
					content: '',
					userid: localStorage.getItem('frontUserid'),
					nickname: localStorage.getItem('username'),
					avatarurl: '',
					score: 0,
				},
				showIndex: -1,
				infoList: [],
				colors: ['#99A9BF', '#F7BA2A', '#FF9900'],
				rules: {
					content: [
						{ required: true, message: '请输入内容', trigger: 'blur' }
					],
					score: [
						{ required: true, message: '请选择评分', trigger: 'blur' }
					],
				},
				total: 1,
				pageSize: 5,
				totalPage: 1,
				storeupParams: {
					name: '',
					picture: '',
					refid: 0,
					tablename: 'shangpinxinxi',
					userid: localStorage.getItem('frontUserid')
				},
				isStoreup: false,
				storeupInfo: {},
				buynumber: 1,
				cart: {
					buynumber: 0,
					discountprice: 0,
					goodid: 0,
					goodname: '',
					picture: '',
					price: 0,
					userid: localStorage.getItem('frontUserid')
				},
				isInCart: false,
				centerType: false,
				shareUrl: location.href,
				couponVisible: false,
				couponList: [],
			}
		},
		created() {
			if(this.$route.query.centerType&&this.$route.query.centerType!=0) {
				this.centerType = true
			}
			
			this.init();
		},
		mounted() {
			setTimeout(()=>{
				let mySwiper22 = new Swiper(".mySwiper22", {"navigation":{"nextEl":".swiper-button-next","prevEl":".swiper-button-prev"},"freeMode":true,"watchSlidesVisibility":true,"watchSlidesProgress":true,"loopedSlides":5,"slidesPerView":3,"spaceBetween":30})
				let option21 = {...{"navigation":{"nextEl":".swiper-button-next","prevEl":".swiper-button-prev"},"loopedSlides":5,"spaceBetween":10}}
				option21.thumbs = {
					swiper: mySwiper22
				}
				new Swiper(".mySwiper21", option21)
			},100)
		},
		//方法集合
		methods: {
			init() {
				this.id = this.$route.query.id
				this.baseUrl = this.$config.baseUrl;
				this.$http.get(this.tablename + '/detail/'  + this.id, {}).then(res => {
					if (res.data.code == 0) {
						this.detail = res.data.data;
						this.title = this.detail.shangpinmingcheng;
						this.detailBanner = this.detail.shangpintupian ? this.detail.shangpintupian.split(",") : [];
						this.startTime = new Date().getTime();
						this.endTime = new Date(this.detail.reversetime).getTime();
						this.$forceUpdate();
						this.getDiscussList(1);
						if(localStorage.getItem('frontToken')){
							this.getStoreupStatus();
							this.getCartList();
						}

					}
				});
			},
			merchantClick(name){
				this.$http.get('shangjia/query',{params: {
					shangjiazhanghao: this.detail.shangjiazhanghao
				}}).then(rs=>{
					let params = {
						id: rs.data.data.id
					}
					if(this.centerType){
						params.centerType = 1
					}
					this.$router.push({path: '/index/shangjiaDetail', query: params});
				})
			},
			storeup(type) {
				if (type == 1 && !this.isStoreup) {
					this.storeupParams.name = this.title;
					this.storeupParams.picture = this.detailBanner[0];
					this.storeupParams.refid = this.detail.id;
					this.storeupParams.type = type;
					this.$http.post('storeup/add', this.storeupParams).then(res => {
						if (res.data.code == 0) {
							this.isStoreup = true;
							this.detail.storeupnum++
							this.$http.post('shangpinxinxi/update', this.detail).then(res => {});
							this.$message({
								type: 'success',
								message: '收藏成功!',
								duration: 1500,
							});
						}
					});
				}
				if (type == -1 && this.isStoreup) {
					this.$http.get('storeup/list', {params: {page: 1, limit: 1, type: 1, refid: this.detail.id, tablename: 'shangpinxinxi', userid: localStorage.getItem('frontUserid')}}).then(res => {
						if (res.data.code == 0 && res.data.data.list.length > 0) {
							this.isStoreup = true;
							this.storeupInfo = res.data.data.list[0];
							let delIds = new Array();
							delIds.push(this.storeupInfo.id);
							this.$http.post('storeup/delete', delIds).then(res => {
								if (res.data.code == 0) {
									this.isStoreup = false;
									this.detail.storeupnum--
									this.$http.post('shangpinxinxi/update', this.detail).then(res => {});
									this.$message({
										type: 'success',
										message: '取消成功!',
										duration: 1500,
									});
								}
							});
						}
					});
				}
			},
			getStoreupStatus(){
				if(localStorage.getItem("frontToken")) {
					this.$http.get('storeup/list', {params: {page: 1, limit: 1, type: 1, refid: this.detail.id, tablename: 'shangpinxinxi', userid: localStorage.getItem('frontUserid')}}).then(res => {
						if (res.data.code == 0 && res.data.data.list.length > 0) {
							this.isStoreup = true;
							this.storeupInfo = res.data.data.list[0];
						}
					});
				}
			},
			curChange(page) {
				this.getDiscussList(page);
			},
			prevClick(page) {
				this.getDiscussList(page);
			},
			nextClick(page) {
				this.getDiscussList(page);
			},
			sizeChange(size){
				this.pageSize = size
				this.getDiscussList(1);
			},
			// 返回按钮
			backClick(){
				let params = {}
				if(this.centerType){
					params.centerType = 1
				}
				this.$router.push({path: '/index/shangpinxinxi', query: params});
			},
			// 下载
			download(file ){
				if(!file) {
					this.$message({
						type: 'error',
						message: '文件不存在',
						duration: 1500,
					});
					return;
				}
				let arr = file.replace(new RegExp('upload/', "g"), "")
				axios.get(this.baseUrl + '/file/download?fileName=' + arr, {
					headers: {
						token: localStorage.getItem("frontToken")
					},
					responseType: "blob"
				}).then(({
					data
				}) => {
					const binaryData = [];
					binaryData.push(data);
					const objectUrl = window.URL.createObjectURL(new Blob(binaryData, {
						type: 'application/pdf;chartset=UTF-8'
					}))
					const a = document.createElement('a')
					a.href = objectUrl
					a.download = arr
					// a.click()
					// 下面这个写法兼容火狐
					a.dispatchEvent(new MouseEvent('click', {
						bubbles: true,
						cancelable: true,
						view: window
					}))
					window.URL.revokeObjectURL(data)
				},err=>{
					axios.get((location.href.split(this.$config.name).length>1 ? location.href.split(this.$config.name)[0] :'') + this.$config.name + '/file/download?fileName=' + arr, {
						headers: {
							token: localStorage.getItem("frontToken")
						},
						responseType: "blob"
					}).then(({
						data
					}) => {
						const binaryData = [];
						binaryData.push(data);
						const objectUrl = window.URL.createObjectURL(new Blob(binaryData, {
							type: 'application/pdf;chartset=UTF-8'
						}))
						const a = document.createElement('a')
						a.href = objectUrl
						a.download = arr
						// a.click()
						// 下面这个写法兼容火狐
						a.dispatchEvent(new MouseEvent('click', {
							bubbles: true,
							cancelable: true,
							view: window
						}))
						window.URL.revokeObjectURL(data)
					})
				})
			},
			getDiscussList(page) {
				this.$http.get('discussshangpinxinxi/list', {params: {page, limit: this.pageSize, refid: this.detail.id,sort: 'istop',order: 'desc'}}).then(res => {
					if (res.data.code == 0) {
						this.infoList = res.data.data.list;
						this.total = res.data.data.total;
						this.pageSize = Number(res.data.data.pageSize);
						this.totalPage = res.data.data.totalPage;
					}
				});
			},
			comzanChange(row){
				if(row.tuserids){
					let arr = row.tuserids.split(',')
					for(let x in arr){
						if(arr[x] == this.userid){
							return true
						}
					}
				}
				return false
			},
			comzanClick(row){
				if(!this.userid){
					return false
				}
				if(!this.comzanChange(row)){
					row.thumbsupnum++
					if(row.tuserids){
						row.tuserids = row.tuserids + ',' + this.userid
					}else {
						row.tuserids = this.userid
					}
					this.$http.post('discussshangpinxinxi/update',row).then(rs=>{
						this.$message.success('点赞成功')
					})
				}else {
					row.thumbsupnum--
					let arr = row.tuserids.split(',')
					for(let x in arr){
						if(arr[x] == this.userid){
							arr.splice(x,1)
						}
					}
					row.tuserids = arr.join(',')
					this.$http.post('discussshangpinxinxi/update',row).then(rs=>{
						this.$message.success('取消成功')
					})
				}
			},
			comcaiChange(row){
				if(row.cuserids){
					let arr = row.cuserids.split(',')
					for(let x in arr){
						if(arr[x] == this.userid){
							return true
						}
					}
				}
				return false
			},
			comcaiClick(row){
				if(!this.userid){
					return false
				}
				if(!this.comcaiChange(row)){
					row.crazilynum++
					if(row.cuserids){
						row.cuserids = row.cuserids + ',' + this.userid
					}else {
						row.cuserids = this.userid
					}
					this.$http.post('discussshangpinxinxi/update',row).then(rs=>{
						this.$message.success('点踩成功')
					})
				}else {
					row.crazilynum--
					let arr = row.cuserids.split(',')
					for(let x in arr){
						if(arr[x] == this.userid){
							arr.splice(x,1)
						}
					}
					row.cuserids = arr.join(',')
					this.$http.post('discussshangpinxinxi/update',row).then(rs=>{
						this.$message.success('取消成功')
					})
				}
			},
			discussEnter(index){
				this.showIndex = index
			},
			discussLeave(){
				this.showIndex = -1
			},
			discussDel(id){
				this.$confirm('是否删除此评论？').then(_ => {
					this.$http.post('discussshangpinxinxi/delete',[id]).then(res=>{
						if(res.data&&res.data.code==0){
							this.addDiscussNum(1)
							this.$message({
								type: 'success',
								message: '删除成功!',
								duration: 1500,
								onClose: () => {
									this.getDiscussList(1);
								}
							});
						}
					})
				}).catch(_ => {});
			},
			submitForm(formName) {
				this.$refs[formName].validate((valid) => {
					if (valid) {
						this.$http.get('orders/list', {params: {page: 1, limit: 1, status: '已完成', goodid: this.detail.id, userid: localStorage.getItem('frontUserid')}}).then(res => {
							if (res.data.code == 0 && res.data.data.list.length == 0) {
								this.$message({
									type: 'error',
									message: '请完成订单后再评论!',
									duration: 1500
								});
								return false
							} else {
								this.$http.get('discussshangpinxinxi/page',{params: {
									page: 1,
									limit: 1,
									refid: this.detail.id,
									userid: localStorage.getItem('frontUserid')
								}}).then(rs=>{
									if(rs.data.data.list.length){
										this.$message({
											type: 'error',
											message: '每个用户只能评论一次!',
											duration: 1500
										});
										return false
									}
									this.form.refid = this.detail.id;
									this.form.avatarurl = localStorage.getItem('frontHeadportrait')?localStorage.getItem('frontHeadportrait'):'';
									this.$http.post('discussshangpinxinxi/add', this.form).then(rs2 => {
										if (rs2.data.code == 0) {
											this.addDiscussNum(2)
											this.form.content = '';
											this.form.score = 0
											this.getDiscussList(1);
											this.$message({
												type: 'success',
												message: '评论成功!',
												duration: 1500,
											});
										}
									});
								})
							}
						});
					} else {
						return false;
					}
				});
			},
			resetForm(formName) {
				this.$refs[formName].resetFields();
			},
			addDiscussNum(type){
				if(type==2){
					this.detail.discussnum++
				}else if(type==1){
					if(this.detail.discussnum!=0){
						this.detail.discussnum--
					}else{
						this.detail.discussnum = 0
					}
				}
				this.$http.get('discussshangpinxinxi/list',{params: {
					page: 1,
					limit: 10000,
					refid: this.detail.id
				}}).then(rs=>{
					let score = 0
					for(let x in rs.data.data.list){
						score += Number(rs.data.data.list[x].score)
					}
					this.detail.totalscore = Number((score / Number(rs.data.data.list.length)).toFixed(2))
					this.$http.post('shangpinxinxi/update',this.detail).then(res=>{})
				})
			},
			getCartList() {
				this.$http.get('cart/list', {params: {userid: localStorage.getItem('frontUserid'), tablename: 'shangpinxinxi', goodid: this.detail.id}}).then(res => {
					if (res.data.code == 0) {
						if (res.data.data.list.length > 0) {
							this.isInCart = true;
						} else {
							this.isInCart = false;
						}
					}
				});
			},
			addCart() {
				if(new Date().getTime() > new Date(this.detail.reversetime).getTime()) {
					this.$message.error('活动已结束');
					return;
				}
				// 单次购买限制
				if (this.detail.onelimittimes > 0 && this.detail.onelimittimes < this.buynumber) {
					this.$message.error(`每人单次只能购买${this.detail.onelimittimes}件`);
					return
				}
				// 库存不够
				if (this.detail.alllimittimes <= 0 ) {
					this.$message.error(`商品已售罄`);
					return
				}
				// 库存限制
				if (this.detail.alllimittimes > 0 && this.detail.alllimittimes < this.buynumber) {
					this.$message.error(`库存不足`);
					return
				}
				if (this.isInCart) {
					this.$message.error('该商品已经添加到购物车');
					return;
				}
		
				this.cart.buynumber = this.buynumber;
				this.cart.goodid = this.detail.id;
				this.cart.goodname = this.title;
				this.cart.tablename = this.tablename;
				this.cart.shangjiazhanghao = this.detail.shangjiazhanghao;
				this.cart.picture = this.detailBanner[0];
				this.cart.price = this.detail.price;
				this.cart.discountprice = this.detail.vipprice;
				this.$http.post('cart/save', this.cart).then(res => {
					if (res.data.code === 0) {
						this.getCartList();
						this.$message({
							message: '添加成功',
							type: 'success',
							duration: 1500,
						});
					} else {
						this.$message.error(res.data.msg);
					}
				});
			},
			//立即购买
			buyNow() {
				if(new Date().getTime() > new Date(this.detail.reversetime).getTime()) {
					this.$message.error('活动已结束');
					return;
				}
				// 单次购买限制
				if (this.detail.onelimittimes > 0 && this.detail.onelimittimes < this.buynumber) {
					this.$message.error(`每人单次只能购买${this.detail.onelimittimes}件`);
					return
				}
				// 库存不够
				if (this.detail.alllimittimes <= 0 ) {
					this.$message.error(`商品已售罄`);
					return
				}
				// 库存限制
				if (this.detail.alllimittimes > 0 && this.detail.alllimittimes < this.buynumber) {
					this.$message.error(`库存不足`);
					return
				}
				// 保存到storage中，在确认下单页面中获取要购买的商品
				localStorage.setItem('orderGoods', JSON.stringify([{
					tablename: this.tablename,
					goodid: this.detail.id,
					goodname: this.title,
					shangjiazhanghao: this.detail.shangjiazhanghao,

					picture:this.detailBanner[0],
					buynumber: this.buynumber,
					userid: localStorage.getItem('frontUserid'),
					price: this.detail.price,
					discountprice: this.detail.vipprice ? this.detail.vipprice : 0
				}]));
				// 跳转到确认下单页面
				let query = {
					type: 1,
				}
				this.$router.push({path: '/index/shop-order/orderConfirm', query: query});
			},


			// 权限判断
			btnAuth(tableName,key){
				if(this.centerType){
					return this.isBackAuth(tableName,key)
				}else{
					return this.isAuth(tableName,key)
				}
			},
			// 修改
			editClick(){
				this.$router.push(`/index/shangpinxinxiAdd?type=edit&&id=${this.detail.id}`);
			},
			// 删除
			async delClick(){
				await this.$confirm('是否删除此好茶集市？') .then(_ => {
					this.$http.post('shangpinxinxi/delete', [this.detail.id]).then(async res => {
						if (res.data.code == 0) {
							this.$http.get('storeup/list',{params:{
								page: 1,
								limit: 100,
								refid: this.detail.id,
								tablename: 'shangpinxinxi',
							}}).then(async obj=>{
								if(obj.data&&obj.data.code==0){
									let arr = []
									for(let x in obj.data.data.list){
										arr.push(obj.data.data.list[x].id)
									}
									if(arr.length){
										await this.$http.post('storeup/delete',arr).then(()=>{})
									}
									this.$message({
										type: 'success',
										message: '删除成功!',
										duration: 1500,
										onClose: () => {
											history.back()
										}
									});
								}
							})
						}
					});
				}).catch(_ => {});
			},
			// 获取uuid
			getUUID() {
				return new Date().getTime();
			},
			couponClick() {
				this.couponList = []
				this.couponVisible = true
				this.getCouponList()
			},
			getCouponList() {
				this.$http.get('coupon/lists').then(res => {
					if (res.data && res.data.code == 0) {
						for (let x in res.data.data) {
							if (this.changeCoupon(res.data.data[x])) {
								this.couponList.push(res.data.data[x])
							}
						}
					}
				})
			},
			changeCoupon(row) {
				if (!this.isBetweenTime(this.getCurDateTime(), [row.startime, row.endtime])) {
					return false
				}
				if(row.shangjiazhanghao == ''){
					return true
				}
				if(this.detail.shangjiazhanghao != row.shangjiazhanghao){
					return false
				}
				return true
			},
			isBetweenTime(currentDate, timeQuantum) {
				let isBetween = true;
				let currentTime = new Date(currentDate);
				let startTime = new Date(timeQuantum[0]);
				let endTime = new Date(timeQuantum[1]);
				let t1 = currentTime.getTime() - startTime.getTime();
				let t2 = currentTime.getTime() - endTime.getTime();
				if (t1 < 0 || t2 > 0) {
					isBetween = false;
				}
				return isBetween;
			},
			getCoupon(item) {
				this.$http.get('mycoupon/page', {
					params: {
						page: 1,
						couponid: item.id
					}
				}).then(res => {
					if (res.data && res.data.code == 0) {
						if (res.data.data.list.length) {
							this.$message.error('已领取该优惠券，请前往使用')
							return false
						}
						let data = {
							couponid: item.id,
							shangjiazhanghao: item.shangjiazhanghao
						}
						data = Object.assign(data, JSON.parse(JSON.stringify(item)))
						data.userid = localStorage.getItem('frontUserid')
						data.couponnumber = this.getUUID()
						delete data.id
						delete data.addtime
						this.$http.post('mycoupon/add', data).then(res => {
							if (res.data && res.data.code == 0) {
								this.$message.success('领取成功！')
							}
						})
					}
				})
			},
		},
		components: {
			CountDown
		}
	}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
	.detail-preview {
		padding: 20px 0; /* P1-1 内容令牌收敛 */ max-width: var(--tea-content); margin-left: auto; margin-right: auto;
		margin: 0px auto;
		color: #666;
		align-content: flex-start;
		background: #fff;
		display: flex;
		width: 100%;
		font-size: 16px;
		justify-content: space-between;
		align-items: flex-start;
		position: relative;
		flex-wrap: wrap;
		.attr {
			padding: 0;
			background: none;
			flex: 1;
			display: block;
			width: calc(100% - 500px);
			position: relative;
			order: 2;
			.info {
				padding: 0;
				margin: 0;
				background: none;
				width: 100%;
				min-height: 560px;
				.title-item {
					border-radius: 0px;
					padding: 10px 0px;
					margin: 0 auto 3px;
					background: #ffffff;
					display: flex;
					border-color: #ddd;
					border-width: 0 0 0px;
					line-height: 1.5;
					justify-content: space-between;
					align-items: center;
					border-style: dashed;
					.detail-title {
						color: #000;
						font-weight: 600;
						font-size: 18px;
					}
					.colectBtn {
						cursor: pointer;
						border: 0px solid #ddd;
						border-radius: 8px;
						padding: 4px 10px;
						background: none;
						.icon {
							color: #666;
							font-size: inherit;
						}
						.text {
							color: #666;
							font-size: inherit;
						}
					}
					.colectBtnActive {
						border: 0px solid #fcbc78;
						color: #fc8108;
						background: none;
						.icon {
							color: #fc8108;
						}
						.text {
							color: #fc8108;
						}
					}
					.colectBtn:hover {
						border: 0px solid #fcbc78;
						color: #fc8108;
						background: none;
						.icon {
							color: #fc8108;
						}
						.text {
							color: #fc8108;
						}
					}
					.colectBtnActive:hover {
						border: 0px solid #fcbc78;
						color: #fc8108;
						background: none;
						.icon {
							color: #fc8108;
						}
						.text {
							color: #fc8108;
						}
					}
				}
				.coupon-item {
					border-radius: 0px;
					padding: 10px 0px;
					margin: 0 auto 3px;
					background: #ffffff;
					display: flex;
					border-color: #ddd;
					border-width: 0 0 0px;
					line-height: 1.5;
					justify-content: space-between;
					align-items: center;
					border-style: dashed;
					.coupon-kong {
						color: #000;
						font-weight: 600;
						font-size: 18px;
					}
					.couponBtn {
						border: 0;
						cursor: pointer;
						border-radius: 4px;
						padding: 0 10px;
						margin: 0 5px 0 0;
						outline: none;
						color: #fff;
						background: rgba(64, 158, 255, 1);
						width: auto;
						font-size: 14px;
						line-height: 40px;
						height: 40px;
					}
					.couponBtn:hover {
						background: rgba(64, 158, 255, 0.5);
					}
				}
				.item {
					border-radius: 0px;
					padding: 8px 0 !important;
					margin: 0 0 3px 0;
					background: #fff;
					display: flex;
					border-color: #ccc;
					border-width: 0 0 1px;
					justify-content: spaceBetween;
					border-style: solid;
					.lable {
						padding: 0 10px 0 0;
						color: #333;
						white-space: nowrap;
						font-weight: 500;
						width: auto;
						font-size: inherit;
						line-height: 24px;
						text-align: right;
						height: auto;
					}
					.count-down {
						padding: 0px 10px 0;
						color: inherit;
						word-break: break-all;
						flex: 1;
						font-size: inherit;
						line-height: 24px;
						height: auto;
					}
					.text {
						padding: 0px 10px 0;
						color: inherit;
						word-break: break-all;
						flex: 1;
						font-size: inherit;
						line-height: 24px;
						height: auto;
					}
					.price {
						color: var(--tea-gold);
					}
					.bold {
						font-weight: bold;
					}
					.link {
						cursor: pointer;
						text-decoration: underline;
					}
				}
				.btn_box {
					padding: 10px 0;
					display: flex;
					font-size: 14px;
					flex-wrap: wrap;
				}
				.el-input-number {
					margin: 0 5px 0 0;
					display: inline-block;
					width: 180px;
					line-height: 38px;
					position: relative;
					.el-input-number__decrease:hover:not(.is-disabled)~.el-input .el-input__inner:not(.is-disabled), .el-input-number__increase:hover:not(.is-disabled)~.el-input .el-input__inner:not(.is-disabled) {
					  border-color: none;
					}
					::v-deep .el-input-number__decrease {
						cursor: pointer;
						z-index: 1;
						display: flex;
						border-color: #DCDFE6;
						border-radius: 4px 0 0 4px;
						top: 1px;
						left: 1px;
						background: #f5f5f5;
						width: 40px;
						justify-content: center;
						border-width: 0 1px 0 0;
						align-items: center;
						position: absolute;
						border-style: solid;
						text-align: center;
						height: 38px;
					}
					
					::v-deep .el-input-number__decrease i {
						color: #666;
						font-size: 14px;
					}
				
					::v-deep .el-input-number__increase {
						cursor: pointer;
						z-index: 1;
						display: flex;
						border-color: #DCDFE6;
						right: 1px;
						border-radius: 0 4px 4px 0;
						top: 1px;
						background: #f5f5f5;
						width: 40px;
						justify-content: center;
						border-width: 0 0 0 1px;
						align-items: center;
						position: absolute;
						border-style: solid;
						text-align: center;
						height: 38px;
					}
					
					::v-deep .el-input-number__increase i {
						color: #666;
						font-size: 14px;
					}
					
					::v-deep .el-input .el-input__inner {
						border: 1px solid #DCDFE6;
						border-radius: 4px;
						padding: 0 40px;
						outline: none;
						color: #666;
						background: #FFF;
						display: inline-block;
						width: 100%;
						font-size: 14px;
						line-height: 40px;
						text-align: center;
						height: 40px;
					}
				}
				.addCartBtn {
					border: 0px solid rgba(33, 190, 50, .3);
					cursor: pointer;
					border-radius: 0px;
					padding: 0 10px;
					margin: 0 5px 0 0;
					outline: none;
					color: #fff;
					background: #3E6B4F;
					width: auto;
					font-size: inherit;
					line-height: 40px;
					height: 40px;
				}
				.addCartBtn:hover {
				}
				.buyBtn {
					border: 0px solid rgba(64, 158, 255, .3);
					cursor: pointer;
					border-radius: 0px;
					padding: 0 10px;
					margin: 0 5px 0 0;
					color: #fff;
					background: #A63D2F;
					width: auto;
					font-size: inherit;
					line-height: 40px;
					height: 40px;
				}
				.buyBtn:hover {
				}
				.editBtn {
					border: 0px solid rgba(64, 158, 255, .3);
					cursor: pointer;
					border-radius: 0px;
					padding: 0 10px;
					margin: 0 5px 0 0;
					color: #fff;
					background: #2E523C;
					width: auto;
					font-size: inherit;
					line-height: 40px;
					height: 40px;
				}
				.editBtn:hover {
				}
				.delBtn {
					border: 0px solid rgba(209, 6, 2, .3);
					cursor: pointer;
					border-radius: 0px;
					padding: 0 10px;
					margin: 0 5px 0 0;
					outline: none;
					color: #fff;
					background: #DFE8DC;
					width: auto;
					font-size: inherit;
					line-height: 40px;
					height: 40px;
				}
				.delBtn:hover {
				}
			}
		}
		.detail-swpier2 {
			margin: 0 40px 20px 0;
			display: block;
			width: 48%;
			text-align: center;
			height: auto;
			order: 1;
			.swiper21 {
				width: 100%;
				height: auto;
				.swiper-button-prev:after {
					display:none;
				}
				.swiper-button-next:after {
					display:none;
				}
				.swiper-item {
					width: 100%;
					height: auto;
					img {
						object-fit: cover;
						width: 100%;
						height: 400px;
					}
				}
				.swiper-button-prev {
					margin: -12px 0 0;
					top: 50%;
					width: 24px;
					height: 24px;
					.icon {
						color: #fff;
						width: 24px;
						font-size: 24px;
						height: 24px;
					}
				}
				.swiper-button-next {
					margin: -12px 0 0;
					top: 50%;
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
			.swiper22 {
				margin: 20px 0 0;
				width: 100%;
				height: auto;
				
				.swiper-button-prev:after {
					display:none;
				}
				.swiper-button-next:after {
					display:none;
				}
				
				.swiper-item {
					border-radius: 10px;
					width: 100%;
					opacity: 0.4;
					height: auto;
					img {
						border-radius: 0px;
						object-fit: cover;
						width: 100%;
						height: 140px;
					}
				}
				.swiper-slide.swiper-slide-thumb-active div {
					opacity: 1;
				}
				.swiper-button-prev {
					margin: -7px 0 0;
					top: 50%;
					width: 14px;
					height: 14px;
					.icon {
						color: #fff;
						width: 14px;
						font-size: 14px;
						height: 14px;
					}
				}
				.swiper-button-next {
					margin: -7px 0 0;
					top: 50%;
					width: 14px;
					height: 14px;
					.icon {
						color: #fff;
						width: 14px;
						font-size: 14px;
						height: 14px;
					}
				}
			}
		}
		.detail-tabs {
			border: 0px solid #E9D9B8;
			border-radius: 10px;
			box-shadow: none;
			padding: 0;
			margin: 20px auto 40px;
			background: #fff;
			flex: 1;
			display: flex;
			width: 100%;
			order: 21;
			&::v-deep .el-tabs__header .el-tabs__nav-wrap {
				margin-bottom: 0;
			}
			::v-deep .el-tabs__header {
				padding: 30px 0;
				margin: 0 20px 0 0;
				flex-direction: column;
				background: linear-gradient(90deg, #12362A 0%, #3E6B4F 100%) repeat-y right top / 100% auto,#EAF0E7;
				display: flex;
				width: 200px;
				border-color: #d1060230;
				border-width: 0 0 0px 0;
				border-style: solid;
				flex-wrap: wrap;
			}
			
			::v-deep .el-tabs__header .el-tabs__item {
				border: 0;
				padding: 0 20px;
				margin: 0 2px;
				color: #000;
				font-weight: 500;
				display: block;
				font-size: 16px;
				line-height: 40px;
				transition: all 0s;
				background: none;
				width: 200px;
				position: relative;
				list-style: none;
				text-align: center;
				height: 50px;
			}
			
			::v-deep .el-tabs__header .el-tabs__item:hover {
				border: 0;
				color: #fff;
				background: linear-gradient(90deg, #12362A 0%, #3E6B4F 100%) no-repeat right top / 98% 100%;
				width: 200px;
				font-size: 16px;
				line-height: 40px;
				text-align: center;
				height: 50px;
			}
			
			::v-deep .el-tabs__header .el-tabs__item.is-active {
				border: 0;
				color: #fff;
				background: linear-gradient(90deg, #12362A 0%, #3E6B4F 100%) no-repeat right top / 98% 100%;
				width: 200px;
				font-size: 16px;
				line-height: 40px;
				text-align: center;
				height: 50px;
			}
			
			::v-deep .el-tabs__content {
				padding: 15px;
				width: 100%;
			}
			.commentForm {
				box-shadow: none;
				padding: 0;
				margin: 20px 0;
				width: 100%;
				.item {
					padding: 0;
					display: flex;
					width: 100%;
					height: auto;
					::v-deep .el-form-item__label {
						padding: 0 10px 0 0;
						color: #666;
						white-space: nowrap;
						font-weight: 500;
						width: auto;
						font-size: inherit;
						line-height: 40px;
						text-align: right;
					}
					.editor {
						border: 1px solid #ccc;
						border-radius: 4px;
						box-shadow: 0 0 0px rgba(80, 80, 80, .2);
						color: #333;
						background: #fff;
						width: 100%;
						font-size: 14px;
						min-height: 350px;
						line-height: 32px;
						::v-deep .avatar-uploader {
							height: 0;
							line-height: 0;
						}
					}
				}
				.commentBtn {
					padding: 0;
					margin: 10px 0 0;
					width: 100%;
					text-align: left;
					height: auto;
					.submitBtn {
						border: 0;
						cursor: pointer;
						border-radius: 0px;
						padding: 0 20px 0 25px;
						margin: 0 20px 0 0;
						color: #fff;
						background: #3E6B4F;
						width: auto;
						font-size: 15px;
						line-height: 40px;
						min-width: 110px;
						height: 40px;
					}
					.submitBtn:hover {
						opacity: 0.8;
					}
					.resetBtn {
						border: 0;
						cursor: pointer;
						border-radius: 0px;
						padding: 0 20px 0 25px;
						margin: 0 20px 0 0;
						color: #333;
						background: #c5c5c5;
						width: auto;
						font-size: 15px;
						line-height: 40px;
						min-width: 110px;
						height: 40px;
					}
					.resetBtn:hover {
						opacity: 0.8;
					}
				}
			}
			.comment-list {
				box-shadow: none;
				padding: 0;
				margin: 40px 0 0;
				display: flex;
				width: 100%;
				justify-content: space-between;
				flex-wrap: wrap;
				.comment-item {
					padding: 0;
					margin: 0 0 20px;
					width: 100%;
					border-color: #d1060220;
					border-width: 0px;
					align-items: center;
					border-style: solid;
					height: auto;
					.istop {
						box-shadow: 0 4px 8px rgba(0,0,0,.1);
						top: 0;
						background: #fff;
						position: absolute;
						right: 10px;
						.icon {
							color: #000;
						}
					}
					.user {
						padding: 5px;
						background: #fff;
						display: flex;
						width: 100%;
						align-items: center;
						height: auto;
						.el-image {
							border-radius: 10px;
							margin: 0 10px 0 0;
							object-fit: cover;
							width: 60px;
							height: 60px;
						}
						.name {
							color: #333;
							font-weight: 600;
							font-size: 16px;
						}
					}
					.comment-content-box {
						border-radius: 4px;
						padding: 8px;
						margin: 10px 0px 0px;
						word-wrap: break-word;
						color: #888;
						background: none;
						font-size: 14px;
						line-height: 1.5;
						.zancai-box {
							margin: 8px 0 0 0;
							display: flex;
							width: 100%;
							justify-content: flex-end;
							align-items: center;
							height: 30px;
							.zan-item {
								display: flex;
								align-items: center;
								.icon {
								font-size: 14px;
								}
								.label {
								display: none;
								font-size: 14px;
								}
								.num {
								font-size: 14px;
								}
							}
							.zan-item.active {
								background: none;
								.icon {
								color: #ff0000;
								font-size: 14px;
								}
								.label {
								color: #ff0000;
								display: none;
								font-size: 14px;
								}
								.num {
								color: #ff0000;
								font-size: 14px;
								}
							}
							.zan-item:hover {
								opacity: 0.8;
								.icon {
								color: #ff000030;
								}
								.label {
								color: #ff000030;
								}
								.num {
								color: #ff000030;
								}
							}
							.cai-item {
								display: flex;
								align-items: center;
								.icon {
								font-size: 14px;
								}
								.label {
								display: none;
								font-size: 14px;
								}
								.num {
								font-size: 14px;
								}
							}
							.cai-item.active {
								background: none;
								.icon {
								color: #ff0000;
								font-size: 14px;
								}
								.label {
								color: #ff0000;
								display: none;
								font-size: 14px;
								}
								.num {
								color: #ff0000;
								font-size: 14px;
								}
							}
							.cai-item:hover {
								opacity: 0.8;
								.icon {
								color: #ff000030;
								}
								.label {
								color: #ff000030;
								}
								.num {
								color: #ff000030;
								}
							}
						}
						.comment-btn {
							margin: 8px 0 0 0;
							display: flex;
							width: 100%;
							justify-content: flex-start;
							align-items: center;
							height: 40px;
							.delBtn {
								border: 0;
								cursor: pointer;
								padding: 0 20px;
								margin: 0 10px;
								color: #333;
								font-size: 14px;
								line-height: 32px;
								border-radius: 0px;
								outline: none;
								background: #c5c5c5;
								width: auto;
								min-width: 90px;
								height: 32px;
							}
						}
					}
				}
			}
			.el-rate {
				border-radius: 4px;
				padding: 0 10px;
				background: none;
				width: 100%;
				line-height: 40px;
				height: 40px;
				::v-deep .el-rate__item {
					cursor: pointer;
					display: inline-block;
					vertical-align: middle;
					font-size: 0;
					position: relative;
				}
				
				::v-deep .el-rate__item .el-rate__icon {
					margin: 0 3px;
					display: block;
					font-size: 18px;
					position: relative;
					transition: .3s;
				}
			}
		}
	}
	.share_view{
		box-shadow: 0 1px 6px rgba(0,0,0,.3);
		z-index: 11;
		bottom: 20%;
		background: #fff;
		position: fixed;
		right: 0;
		.share:last-of-type{
			border:none;
		}
	}
	.coupon_list {
		width: 100%;
		display: flex;
		flex-wrap: wrap;
	
		.coupon {
			width: 31%;
			background: url('../../assets/coupon.png');
			background-repeat: no-repeat;
			background-position: center center;
			background-size: cover;
			padding: 60px 30px;
			display: flex;
			align-items: center;
			margin: 0 1% 20px;
	
			position: relative;
	
			.no-coupon {
				position: absolute;
				width: 80px;
				right: 20px;
				top: 20px;
			}
	
			.coupon_left {
				flex: 1;
				color: #000;
	
				.name {
					font-size: 22px;
					line-height: 1.5;
				}
	
				.date {
					font-size: 14px;
					line-height: 1.5;
				}
				.date1 {
					font-size: 12px;
					line-height: 1.5;
					color: #FF0800;
				}
			}
	
			.coupon_right {
				display: flex;
				flex-direction: column;
				align-items: center;
	
				.price {
					color: #FF0800;
					font-size: 20px;
					padding: 0 0 10px;
	
					.num {
						font-size: 28px;
					}
				}
	
				.btn {
					width: 100px;
					height: 30px;
					line-height: 30px;
					border-radius: 30px;
					background: #FF0800;
					text-align: center;
					color: #fff;
					font-size: 18px;
					cursor: pointer;
				}
			}
		}
	
		.noList {
			color: #9e9e9e;
			width: 100%;
			text-align: center;
			padding: 60px 0;
		}
	}
	.idea1 {
		background: #fff;
		width: 100%;
		height: 1px;
		order: 4;
	}
	.idea2 {
		background: #fff;
		width: 100%;
		height: 1px;
		order: 6;
	}
</style>
