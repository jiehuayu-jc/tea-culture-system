<template>
	<div class="forumdetail-preview">
		<div class="forumdetail-title">
			<div>茶友圈</div>
		</div>
		<div class="back_box">
			<el-button class="backBtn" size="mini" @click="backClick">
				<span class="icon iconfont icon-jiantou33"></span>
				<span class="text">返回</span>
			</el-button>
		</div>

		<div class="forumdetail-swpier2" v-if="detailBanner.length">
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
		<div class="section-content">
			<div class="content-title">{{detail.title}}</div>
			<div class="subhead-box">
				<div class="time_item" v-if="detail.addtime">
					<span class="icon iconfont" :class=""></span>
					<span class="label">发布时间：</span>
					<span class="text">{{detail.addtime.split(' ')[0]}}</span>
				</div>
				<div class="publisher_item">
					<span class="icon iconfont" :class=""></span>
					<span class="label">发布人：</span>
					<span class="text">{{detail.isanon==1&&detail.userid!=userid?'匿名':detail.username}}</span>
				</div>
			</div>
			<div class="operate">
				<div class="likeBtn" @click="zanClick">
					<span class="icon iconfont" :class="zanType?'icon-guanzhu-zhihui':'icon-guanzhu-zhihui'"></span>
					<span class="text">{{zanType?'取消点赞':'点赞'}}({{allZan}})</span>
				</div>
				<div class="collectBtn" @click="collectClick">
					<span class="icon iconfont" :class="collectType?'icon-shoucang12':'icon-shoucang10'"></span>
					<span class="text">{{collectType?'取消收藏':'收藏'}}({{allCollect}})</span>
				</div>
			</div>
			<div class="content-detail ql-snow ql-editor" v-html="detail.content"></div>
			<div class="report-box" title="举报" @click="reportClick">
				<span class="icon iconfont icon-zhangjie18"></span>
			</div>
			<div class="comment-box">
				<div class="comment-top-box">
					<div class="comment-title">评论列表</div>
					<el-button class="pubAdd" type="primary" @click="addComment">
						<span class="icon iconfont "></span>
						<span class="text">点击评论</span>
					</el-button>
				</div>
				<div class="comment-list">
					<template v-if="commentList && commentList.length">
						<div class="comment-item" v-for="item in commentList" :key="item.id" @mouseenter="commentEnter(item.id)" @mouseleave="commentLeave">
							<div class="comment-user">
								<img v-if="item.avatarurl" :src="baseUrl + item.avatarurl">
								<img v-if="!item.avatarurl" :src="require('@/assets/touxiang.png')">
								<div class="name">用户：{{item.username}}</div>
							</div>
							<div class="comment-content ql-snow ql-editor" v-html="item.content"></div>
							<div class="comment-btn">
								<el-button class="replyBtn" v-if="showIndex==item.id&&showIndex1 == -1" @click="replyClick(item.id)">回复</el-button>
								<el-button class="delBtn" v-if="showIndex==item.id&&userid==item.userid&&showIndex1 == -1" @click="commentDel(item.id)">删除</el-button>
							</div>
							
							<template v-if="item.childs && item.childs.length">
								<div class="comment">
									<div class="item" v-for="items in item.childs" :key="items.id" @mouseenter="commentEnter1(items.id)" @mouseleave="commentLeave1">
										<div class="user">
											<img v-if="items.avatarurl" :src="baseUrl + items.avatarurl">
											<img v-if="!items.avatarurl" :src="require('@/assets/touxiang.png')">
											<div class="name">用户：{{items.username}}</div>
										</div>
										<div class="reply ql-snow ql-editor" v-html="items.content"></div>
										<div class="reply-btn">
											<el-button class="delBtn" v-if="showIndex==item.id&&userid==items.userid&&showIndex1==items.id" @click="commentDel(items.id)">删除</el-button>
										</div>
									</div>
								</div>
							</template>
						</div>
					</template>
				</div>
			</div>
		</div>
		<el-dialog title="添加评论" :visible.sync="dialogFormVisible">
			<el-form :model="form" :rules="rules" ref="form">
				<el-form-item label="评论" label-width="60px" prop="content">
					<editor
						style="width: 100%"
						v-model="form.content" 
						class="editor" 
						action="file/upload">
					</editor>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="addForum('form')">确 定</el-button>
			</div>
		</el-dialog>
		<el-dialog title="茶友圈举报" :visible.sync="reportVisible">
			<el-form :model="reportForm" :rules="reportRules" ref="reportForm">
				<el-form-item label="举报原因" label-width="120px" prop="reason">
					<el-input type="textarea" v-model="reportForm.reason"></el-input>
				</el-form-item>
				<el-form-item label="图片补充" label-width="120px">
					<file-upload
						tip="点击上传图片补充"
						action="file/upload"
						:limit="3"
						:multiple="true"
						:fileUrls="reportForm.picture?reportForm.picture:''"
						@change="pictureUploadChange"
						></file-upload>
					</el-form-item>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="reportVisible = false">取 消</el-button>
				<el-button type="primary" @click="reportSave('reportForm')">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	import Swiper from "swiper";
	export default {
		//数据集合
		data() {
			return {
				baseUrl: '',
				id: '',
				detail: {},
				commentList: [],
				dialogFormVisible: false,
				detailBanner: [],
				form: {
					content: '',
					parentid: '',
					userid: localStorage.getItem('frontUserid'),
					username: localStorage.getItem('username'),
					avatarurl: '',
				},
				userid: localStorage.getItem('frontUserid'),
				editorOption: {
					modules: {
						toolbar: [
							["bold", "italic", "underline", "strike"],
							["blockquote", "code-block"],
							[{ header: 1 }, { header: 2 }],
							[{ list: "ordered" }, { list: "bullet" }],
							[{ script: "sub" }, { script: "super" }],
							[{ indent: "-1" }, { indent: "+1" }],
							[{ direction: "rtl" }],
							[{ size: ["small", false, "large", "huge"] }],
							[{ header: [1, 2, 3, 4, 5, 6, false] }],
							[{ color: [] }, { background: [] }],
							[{ font: [] }],
							[{ align: [] }],
							["clean"],
							["image", "video"]
						]
					}
				},
				rules: {
					content: [
						{ required: true, message: '请输入评论', trigger: 'blur' }
					]
				},
				zanType:false,
				zanForm:{},
				collectType: false,
				collectForm:{},
				allCollect: 0,
				showIndex: -1,
				showIndex1: -1,
				allZan: 0,
				reportVisible: false,
				reportForm: {
					forumid: '',
					title: '',
					userid: localStorage.getItem('frontUserid'),
					username: localStorage.getItem('username'),
					reporteduserid: '',
					reportedusername: '',
					reason: '',
					picture: '',
					handleadvise: '',
					status: '处理中',
					reporttype: '主题帖举报'
				},
				reportRules: {
					reason: [
						{ required: true, message: '请输入举报理由', trigger: 'blur' }
					]
				}
			}
		},
		created() {
			this.baseUrl = this.$config.baseUrl;
			this.id = this.$route.query.id
			this.getDetail()
		},
		mounted() {
			setTimeout(()=>{
				let mySwiper22 = new Swiper(".mySwiper22", {"navigation":{"nextEl":".swiper-button-next","prevEl":".swiper-button-prev"},"freeMode":true,"watchSlidesVisibility":true,"watchSlidesProgress":true,"loopedSlides":5,"slidesPerView":4,"spaceBetween":10})
				let option21 = {...{"navigation":{"nextEl":".swiper-button-next","prevEl":".swiper-button-prev"},"loopedSlides":5,"spaceBetween":10}}
				option21.thumbs = {
					swiper: mySwiper22
				}
				new Swiper(".mySwiper21", option21)
			},100)
		},
		//方法集合
		methods: {
			reportClick(){
				this.reportForm.forumid = this.detail.id
				this.reportForm.title = this.detail.title
				this.reportForm.reporteduserid = this.detail.userid
				this.reportForm.reportedusername = this.detail.username
				this.reportForm.reason = ''
				this.reportForm.picture = ''
				this.reportVisible = true
			},
			pictureUploadChange(fileUrls) {
				this.reportForm.picture = fileUrls.replace(new RegExp(this.$config.baseUrl,"g"),"");
			},
			reportSave(formName){
				this.$refs[formName].validate((valid) => {
					if (valid) {
						this.$http.post('forumreport/add', this.reportForm).then(res => {
							if (res.data.code == 0) {
								this.$message({
									type: 'success',
									message: '提交成功!',
									duration: 1500,
									onClose: () => {
										this.reportVisible = false;
									}
								});
							}
						});
					} else {
						return false;
					}
				});
			},
			// 返回按钮
			backClick(){
				history.back()
			},
			onEditorReady(editor) {
				editor.root.setAttribute('data-placeholder', "请输入内容...");
			},
			getDetail() {
				this.$http.get(`forum/list/${this.id}`).then(res => {
					if (res.data.code == 0) {
						this.detailBanner = res.data.data.cover ? res.data.data.cover.split(",") : [];
						res.data.data.content = res.data.data.content.replace(/img src/gi,"img style=\"width:100%;\" src");
						this.detail = res.data.data
						this.commentList = res.data.data.childs;
						this.getZan()
						this.getAllZan()
						this.getCollect()
						this.getAllCollect()
					}
				});
			},
			getZan() {
				this.$http.get('storeup/list', {
					params: {
						page: 1,
						limit: 1,
						type: '21',
						userid: localStorage.getItem('frontUserid'),
						tablename: 'forum',
						refid: this.id
					}
				}).then(res=>{
					if(res.data&&res.data.code==0){
						if(res.data.data.list.length){
							this.zanType = true
							this.zanForm = res.data.data.list[0]
						}else{
							this.zanType = false
						}
					}
				})
			},
			getCollect() {
				this.$http.get('storeup/list', {
					params: {
						page: 1,
						limit: 1,
						type: '1',
						userid: localStorage.getItem('frontUserid'),
						tablename: 'forum',
						refid: this.id
					}
				}).then(res=>{
					if(res.data&&res.data.code==0){
						if(res.data.data.list.length){
							this.collectType = true
							this.collectForm = res.data.data.list[0]
						}else{
							this.collectType = false
						}
					}
				})
			},
			getAllZan() {
				this.$http.get('storeup/list', {
					params: {
						page: 1,
						type: '21',
						tablename: 'forum',
						refid: this.id
					}
				}).then(res => {
					if (res.data && res.data.code == 0) {
						this.allZan = Number(res.data.data.total)
					}
				})
			},
			getAllCollect() {
				this.$http.get('storeup/list', {
					params: {
						page: 1,
						type: '1',
						tablename: 'forum',
						refid: this.id
					}
				}).then(res => {
					if (res.data && res.data.code == 0) {
						this.allCollect = Number(res.data.data.total)
					}
				})
			},
			zanClick() {
				if(this.zanType){
					this.$http.post('storeup/delete', [this.zanForm.id]).then(res => {
						if (res.data && res.data.code == 0) {
							this.$message.success('取消成功')
							this.getZan()
							this.getAllZan()
						}
					})
				}else{
					let data = {
						name: this.detail.title,
						picture: this.detailBanner[0],
						refid: this.detail.id,
						type: '21',
						tablename: 'forum',
						userid: localStorage.getItem('frontUserid')
					}
					this.$http.post('storeup/add', data).then(res => {
						if (res.data && res.data.code == 0) {
							this.$message.success('点赞成功')
							this.getZan()
							this.getAllZan()
						}
					})
				}
			},
			collectClick() {
				if(this.collectType){
					this.$http.post('storeup/delete', [this.collectForm.id]).then(res => {
						if (res.data && res.data.code == 0) {
							this.$message.success('取消成功')
							this.getCollect()
							this.getAllCollect()
						}
					})
				}else{
					let data = {
						name: this.detail.title,
						picture: this.detailBanner[0],
						refid: this.detail.id,
						type: '1',
						tablename: 'forum',
						userid: localStorage.getItem('frontUserid')
					}
					this.$http.post('storeup/add', data).then(res => {
						if (res.data && res.data.code == 0) {
							this.$message.success('收藏成功')
							this.getCollect()
							this.getAllCollect()
						}
					})
				}
			},
			// 新增评论
			addComment(){
				this.form.parentid = this.detail.id
				this.dialogFormVisible = true
			},
			// 鼠标移入
			commentEnter(index){
				this.showIndex = index
			},
			// 鼠标移出
			commentLeave(){
				this.showIndex = -1
			},
			// 二级评论鼠标移入
			commentEnter1(index){
				this.showIndex1 = index
			},
			// 二级评论鼠标移出
			commentLeave1(){
				this.showIndex1 = -1
			},
			// 删除评论
			commentDel(id){
				this.$confirm('是否删除此评论？')
				  .then(_ => {
					this.$http.post('forum/delete',[id]).then(res=>{
					  if(res.data&&res.data.code==0){
						  this.$message({
							type: 'success',
							message: '删除成功!',
							duration: 1500,
							onClose: () => {
								this.getDetail();
							}
						  });
					  }
				  })
			  }).catch(_ => {});
			},
			// 回复评论
			replyClick(id){
				this.form.parentid = id
				this.dialogFormVisible = true
			},
			addForum(formName) {
				this.$refs[formName].validate((valid) => {
					if (valid) {
						this.form.avatarurl = localStorage.getItem('frontHeadportrait')?localStorage.getItem('frontHeadportrait'):'';
						this.$http.post('forum/add', this.form).then(res => {
							if (res.data.code == 0) {
								this.$message({
									type: 'success',
									message: '评论成功!',
									duration: 1500,
									onClose: () => {
										this.form.content = '';
										this.getDetail();
										this.dialogFormVisible = false;
									}
								});
							}
						});
					} else {
						return false;
					}
				});
			}
		}
	}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
	.forumdetail-preview {
				padding: 0 0; /* P1-1 内容令牌收敛 */ max-width: 1400px; margin-left: auto; margin-right: auto;
				margin: 10px auto;
				background: rgb(255, 255, 255);
				width: 100%;
				position: relative;
				.forumdetail-title {
						padding: 0;
						margin: 0 0 20px;
						background: linear-gradient(90deg, #12362A 0%, #3E6B4F 100%) no-repeat center 12px;
						font-weight: 600;
						display: none;
						width: 100%;
						font-size: 26px;
						line-height: 40px;
						position: relative;
						text-align: center;
						div {
								color: #333;
								font-size: 24px;
							}
		}
		.forumdetail-swpier2 {
			background: #fff;
			width: 100%;
			height: auto;
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
					opacity: 0.4;
					height: auto;
					img {
						object-fit: cover;
						width: 100%;
						height: 100px;
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
		.section-content {
						border: 0px solid #E9D9B8;
						padding: 0;
						margin: 20px auto;
						background: rgb(255, 255, 255);
						width: 100%;
						position: relative;
						.content-title {
								padding: 0 10px;
								margin: 0 0 20px;
								color: #fff;
								background: #0674f9;
								font-weight: 5800;
								width: 100%;
								font-size: 22px;
								line-height: 60px;
								text-align: center;
								height: 60px;
							}
			.subhead-box {
								display: flex;
								width: 100%;
								justify-content: center;
								align-items: center;
								.time_item {
										padding: 0 10px;
										.icon {
												margin: 0 2px 0 0;
												color: #999;
												font-size: 16px;
												line-height: 1.5;
											}
					.label {
												color: #999;
												font-size: 16px;
												line-height: 1.5;
											}
					.text {
												color: #999;
												font-size: 16px;
												line-height: 1.5;
											}
				}
				.publisher_item {
										padding: 0 10px;
										.icon {
												margin: 0 2px 0 0;
												color: #999;
												font-size: 16px;
												line-height: 1.5;
											}
					.label {
												color: #999;
												font-size: 16px;
												line-height: 1.5;
											}
					.text {
												color: #999;
												font-size: 16px;
												line-height: 1.5;
											}
				}
			}
			.operate {
								padding: 10px;
								display: flex;
								width: 100%;
								justify-content: flex-end;
								align-items: center;
								.likeBtn {
										cursor: pointer;
										padding: 0 15px;
										background: none;
										display: flex;
										justify-content: center;
										align-items: center;
										.icon {
												margin: 0 5px 0 0;
												color: #333;
												font-size: 16px;
												line-height: 40px;
											}
					.text {
												color: #333;
												font-size: 16px;
												line-height: 40px;
											}
				}
				.likeBtn:hover {
										.icon {
											}
					.text {
											}
				}
				.collectBtn {
										cursor: pointer;
										padding: 0 15px;
										display: flex;
										justify-content: center;
										align-items: center;
										.icon {
												margin: 0 2px 0 0;
												color: #333;
												font-size: 14px;
												line-height: 40px;
											}
					.text {
												color: #333;
												font-size: 14px;
												line-height: 40px;
											}
				}
				.collectBtn:hover {
										.icon {
											}
					.text {
											}
				}
			}
			.content-detail {
								padding: 0 20px;
								font-size: 16px;
								line-height: 1.5;
							}
			.report-box {
								cursor: pointer;
								border-radius: 50%;
								box-shadow: 0 0px 0px rgba(0,0,0,.3);
								bottom: 20%;
								background: rgb(6, 116, 252);
								width: 48px;
								line-height: 48px;
								position: fixed;
								right: 10%;
								text-align: center;
								height: 48px;
								.iconfont {
										color: #fff;
										font-size: 24px;
									}
			}
			.comment-box {
								padding: 0;
								box-shadow: none;
								width: 100%;
								.comment-top-box {
										padding: 5px 5px 5px 15px;
										display: flex;
										width: 100%;
										border-color: #E9D9B8;
										border-width: 0px;
										justify-content: space-between;
										align-items: center;
										border-style: solid;
										.comment-title {
												color: #333;
												font-weight: 600;
												font-size: 20px;
											}
					.pubAdd {
												cursor: pointer;
												border: 0;
												border-radius: 4px;
												padding: 0 20px 0 26px;
												margin: 0 10px 0 0;
												color: #fff;
												background: #3E6B4F;
												width: auto;
												font-size: 14px;
												line-height: 44px;
												height: 44px;
												.icon {
														margin: 0 2px 0 0;
														color: #fff;
														font-weight: 600;
														font-size: 16px;
													}
						.text {
														color: #fff;
														font-size: 16px;
													}
					}
					.pubAdd:hover {
												.icon {
													}
						.text {
													}
					}
				}
				.comment-list {
										width: 100%;
										.comment-item {
												border: 0px solid #E9D9B8;
												border-radius: 10px;
												padding: 20px;
												margin: 20px 0;
												color: #666;
												background: none;
												width: 100%;
												align-items: center;
												height: auto;
												.comment-user {
														padding: 0;
														background: none;
														display: flex;
														width: 100%;
														align-items: center;
														height: auto;
														img {
																border-radius: 100%;
																margin: 0 10px 0 0;
																object-fit: cover;
																width: 40px;
																height: 40px;
															}
							.name {
																color: #333;
																font-size: 16px;
															}
						}
						.comment-content {
														border-radius: 4px;
														padding: 0px;
														box-shadow: none;
														margin: 10px 0px 0px;
														color: #666;
														background: none;
														font-size: 16px;
														line-height: 30px;
													}
						.comment-btn {
														padding: 0;
														margin: 10px 0 0;
														display: flex;
														width: 100%;
														justify-content: flex-start;
														align-items: center;
														height: 40px;
														.replyBtn {
																border: 0;
																cursor: pointer;
																padding: 0 20px;
																margin: 0 20px 0 0;
																color: #fff;
																font-size: 14px;
																line-height: 32px;
																border-radius: 0px;
																outline: none;
																background: #3E6B4F;
																width: auto;
																min-width: 80px;
																height: 32px;
															}
							.delBtn {
																border: 0;
																cursor: pointer;
																padding: 0 20px;
																margin: 0;
																color: #333;
																font-size: 14px;
																line-height: 32px;
																border-radius: 0px;
																outline: none;
																background: #c5c5c5;
																width: auto;
																min-width: 80px;
																height: 32px;
															}
						}
						.comment {
														padding: 0 20px;
														width: 100%;
														.item {
																padding: 0px;
																margin: 20px 0 0 0;
																color: #666;
																background: #fff;
																width: 100%;
																border-color: #eee;
																border-width: 0px;
																align-items: center;
																border-style: solid;
																height: auto;
																.user {
																		border-radius: 4px;
																		padding: 0;
																		background: none;
																		display: flex;
																		width: 100%;
																		align-items: center;
																		height: auto;
																		img {
																				border-radius: 8px;
																				margin: 0 10px 0 0;
																				object-fit: cover;
																				width: 40px;
																				height: 40px;
																			}
									.name {
																				color: #666;
																				font-size: 16px;
																			}
								}
								.reply {
																		border-radius: 4px;
																		padding: 8px;
																		box-shadow: none;
																		margin: 10px 0px 0px;
																		color: #888;
																		background: none;
																		font-size: 16px;
																		line-height: 30px;
																	}
								.reply-btn {
																		margin: 8px 0 0 0;
																		display: flex;
																		width: 100%;
																		justify-content: flex-end;
																		align-items: center;
																		height: 40px;
																		.delBtn {
																				border: 0;
																				cursor: pointer;
																				padding: 0 20px;
																				margin: 0 0px;
																				color: #333;
																				font-size: 14px;
																				line-height: 32px;
																				border-radius: 0px;
																				outline: none;
																				background: #c5c5c5;
																				width: auto;
																				min-width: 80px;
																				height: 32px;
																			}
								}
							}
						}
					}
				}
			}
		}
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
			}
</style>
