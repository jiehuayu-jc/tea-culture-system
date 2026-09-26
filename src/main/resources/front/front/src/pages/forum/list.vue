<template>
	<div class="forum-preview">
		<div class="forum-title">
			<div>茶友圈</div>
		</div>
		<el-form :inline="true" :model="formSearch" class="list-form-pv">
			<el-form-item class="search-item">
				<el-input v-model="title" placeholder="标题"></el-input>
			</el-form-item>
			<div class="search-btn-item">
				<el-button class="searchBtn" type="primary" @click="getForumList(1)">
					<span class="icon iconfont "></span>
					查询
				</el-button>
				<el-button class="pubBtn" type="primary" @click="toForumAdd">
					<span class="icon iconfont "></span>
					发布帖子
				</el-button>
			</div>
		</el-form>
		<div class="category-list">
			<div class="item" @click="categoryClick(0)" :class="categoryIndex == 0 ? 'active' : ''">全部</div>
			<div v-for="(item,index) in categoryList" @click="categoryClick(index+1)" :key="index" class="item" :class="categoryIndex == index+1 ? 'active' : ''">{{item.typename}}</div>
		</div>
		<div class="z-box">
			<div class="section-content" v-for="item in forumList" :key="item.id" @click.stop="toForumDetail(item)">
				<img class="item-img" :src="$config.baseUrl + item.cover.split(',')[0]" @click.stop="imgPreView($config.baseUrl + item.cover.split(',')[0])" />
				<div class="item-title">{{item.title}}</div>
				<div class="item-user">发布人：{{item.isanon==1&&item.userid!=userid?'匿名':item.username}}</div>
				<div class="item-time">{{item.addtime}}</div>
			</div>
		</div>
		<el-pagination
			background
			id="pagination" class="pagination"
			:pager-count="7"
			:page-size="pageSize"
			:page-sizes="pageSizes"
			prev-text="上一页"
			next-text="下一页"
			:hide-on-single-page="false"
			:layout='["total","prev","pager","next","sizes","jumper"].join()'
			:total="total"
			@current-change="curChange"
			@prev-click="prevClick"
			@next-click="nextClick"
			></el-pagination>
		<el-dialog title="预览图" :visible.sync="previewVisible" width="50%">
			<img :src="previewImg" alt="" style="width: 100%;">
		</el-dialog>
	</div>
</template>

<script>
	export default {
		//数据集合
		data() {
			return {
				formSearch: {},
				title: '',
				layouts: '',
				forumList: [],
				total: 1,
				pageSize: 10,
				pageSizes: [],
				totalPage: 1,
				previewImg: '',
				previewVisible: false,
				userid: localStorage.getItem('frontUserid'),
				categoryIndex: 0,
				categoryList: [],
			}
		},
		created() {
			this.getCategoryList()
			this.getForumList(1);
		},
		//方法集合
		methods: {
			getCategoryList(){
				this.$http.get('forumtype/list', {}).then(res => {
					if (res.data.code == 0) {
						this.categoryList = res.data.data.list
					}
				});
			},
			categoryClick(index) {
				this.categoryIndex = index
				this.getForumList()
			},
			imgPreView(url){
				this.previewImg = url
				this.previewVisible = true
			},
			getForumList(page) {
				let params = {page, limit: this.pageSize, isdone: '开放', sort: 'istop,toptime', order: 'desc,desc',delflag: 0};
				let searchWhere = {};
				if(this.title != '') searchWhere.title = '%' + this.title + '%';
				if(this.categoryIndex!=0){
					searchWhere.typename = this.categoryList[this.categoryIndex - 1].typename
				}
				this.$http.get('forum/flist', {params: Object.assign(params, searchWhere)}).then(res => {
					if (res.data.code == 0) {
						this.forumList = res.data.data.list;
						this.total = res.data.data.total;
						this.pageSize = Number(res.data.data.pageSize);
						this.totalPage = res.data.data.totalPage;
						if(this.pageSizes.length==0){
							this.pageSizes = [this.pageSize, this.pageSize*2, this.pageSize*3, this.pageSize*5];
						}
					}
				});
			},
			curChange(page) {
				this.getForumList(page);
			},
			prevClick(page) {
				this.getForumList(page);
			},
			nextClick(page) {
				this.getForumList(page);
			},
			toForumAdd() {
				this.$router.push('/index/forumAdd');
			},
			toForumDetail(item) {
				this.$router.push({path: '/index/forumDetail', query: {id: item.id}});
			}
		}
	}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
	.forum-preview {
				padding: 0 calc((100% - 1400px)/2);
				margin: 0 auto;
				align-content: flex-start;
				background: #f6f6f6;
				display: flex;
				width: 100%;
				font-size: 16px;
				align-items: flex-start;
				position: relative;
				flex-wrap: wrap;
				.forum-title {
						padding: 0;
						margin: 0;
						background: url(http://codegen.caihongy.cn/20241026/0662ab9288ed4b258f2abbdfec5cc101.png) no-repeat center 12px;
						font-weight: 600;
						width: 100%;
						font-size: 26px;
						line-height: 110px;
						position: relative;
						text-align: center;
						height: 100px;
						div {
								color: #fff;
								letter-spacing: 4px;
								font-size: 28px;
							}
		}
		.list-form-pv {
						padding: 40px 0 20px;
						margin: 0 auto;
						background: #fff;
						display: flex;
						width: 1400px;
						justify-content: center;
						align-items: center;
						flex-wrap: wrap;
						height: auto;
						order: 2;
						.search-item {
								margin: 0;
								/deep/ .el-form-item__content {
										display: flex;
										align-items: center;
									}
				.el-input {
										width: 100%;
									}
				.el-input /deep/ .el-input__inner {
										border: 1px solid #ddd;
										border-radius: 4px;
										padding: 0 10px;
										margin: 0;
										color: #888;
										background: #fff;
										width: 280px;
										font-size: 15px;
										line-height: 42px;
										height: 42px;
									}
			}
			.search-btn-item {
								display: flex;
								.searchBtn {
										cursor: pointer;
										border: 0;
										border-radius: 4px;
										padding: 0px 20px;
										margin: 0 10px 0 10px;
										color: #fff;
										background: #3E6B4F;
										width: auto;
										font-size: inherit;
										line-height: 40px;
										height: 40px;
										.icon {
												margin: 0 2px 0 0;
												color: #fff;
											}
				}
				
				.searchBtn:hover {
									}
				
				.pubBtn {
										cursor: pointer;
										border: 0px solid #fcbb78;
										border-radius: 4px;
										padding: 0px 15px;
										margin: 0 10px 0 0;
										color: #fff;
										background: #3E6B4F;
										width: auto;
										font-size: inherit;
										line-height: 40px;
										height: 40px;
										.icon {
												margin: 0 2px 0 0;
												color: #fff;
												font-size: 16px;
											}
				}
				
				.pubBtn:hover {
									}
			}
		}
		.category-list {
						padding: 20px;
						margin: 0;
						background: none;
						display: flex;
						width: 100%;
						flex-wrap: wrap;
						height: auto;
						order: 1;
						.item {
								cursor: pointer;
								border: 0px solid #475a8350;
								padding: 0px 20px 20px;
								margin: 0 20px 20px 0;
								color: #fff;
								display: flex;
								font-size: 16px;
								flex-wrap: wrap;
								background: url(http://codegen.caihongy.cn/20241023/7db6e8651c02480da7ccfb89283730fc.png) no-repeat left center / auto 100%,url(http://codegen.caihongy.cn/20241023/08826ed48d7145f4ade6f18536695d1f.png) no-repeat right center / auto 100%,url(http://codegen.caihongy.cn/20241023/b103608cb25c49ef9a7c76e34d0e59b9.png) repeat-x center center / auto 100%;
								justify-content: center;
								align-items: center;
								min-width: 120px;
								height: 94px;
							}
			
			.item:hover {
								color: #fff;
								background: url(http://codegen.caihongy.cn/20241023/0a71fbf90ea34e2f97b9250a2940579e.png) no-repeat left center / auto 100%,url(http://codegen.caihongy.cn/20241023/00f7d5b077674c4ea859eebae391a686.png) no-repeat right center / auto 100%,url(http://codegen.caihongy.cn/20241023/9f6dc91dedbf49c0aebbb2b503633f89.png) repeat-x center center / auto 100%;
							}
			
			.item.active {
								color: #fff;
								background: url(http://codegen.caihongy.cn/20241023/0a71fbf90ea34e2f97b9250a2940579e.png) no-repeat left center / auto 100%,url(http://codegen.caihongy.cn/20241023/00f7d5b077674c4ea859eebae391a686.png) no-repeat right center / auto 100%,url(http://codegen.caihongy.cn/20241023/9f6dc91dedbf49c0aebbb2b503633f89.png) repeat-x center center / auto 100%;
							}
		}
		.z-box {
						padding: 20px 40px;
						margin: 0 auto;
						background: #fff;
						display: flex;
						width: 100%;
						justify-content: space-between;
						flex-wrap: wrap;
						order: 3;
						.section-content {
								cursor: pointer;
								padding: 10px;
								box-shadow: inset 0px 0px 6px 0px #3E6B4F50;
								margin: 0 0 30px;
								background: #d8e9ff;
								display: flex;
								width: 49%;
								border-color: #3E6B4F80;
								border-width: 1px;
								align-items: flex-start;
								position: relative;
								border-style: solid;
								.item-img {
										border-radius: 100%;
										margin: 0 10px 0 0;
										object-fit: cover;
										width: 60px;
										transition: all .4s;
										height: 60px;
									}
				.item-title {
										overflow: hidden;
										color: #333;
										white-space: nowrap;
										font-weight: 500;
										width: calc(100% - 80px);
										font-size: 16px;
										line-height: 40px;
										text-overflow: ellipsis;
									}
				.item-user {
										margin: 0 10px 0 0;
										color: #888;
										white-space: nowrap;
										display: none;
										font-size: 15px;
										line-height: 40px;
									}
				.item-time {
										color: #999;
										top: 45px;
										left: 80px;
										font-size: 15px;
										line-height: 1.5;
										position: absolute;
									}
			}
			.section-content:hover {
								.item-title {
										color: #000;
									}
				.item-user {
									}
				.item-time {
									}
			}
		}
	}
</style>
