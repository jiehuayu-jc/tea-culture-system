<template>
	<div>
		<div class="breadcrumb-preview">
			<el-breadcrumb :separator="'/'">
				<el-breadcrumb-item class="item1" to="/"><a>首页</a></el-breadcrumb-item>
				<el-breadcrumb-item class="item2" v-for="(item, index) in breadcrumbItem" :key="index"><a>{{item.name}}</a></el-breadcrumb-item>
			</el-breadcrumb>
		</div>
	
		<div class="news-preview-pv">
			<el-form :inline="true" :model="formSearch" class="list-form-pv">
				<el-form-item class="search-item">
					<el-input v-model="title" placeholder="标题"></el-input>
				</el-form-item>
				<el-button class="search-btn" type="primary" @click="getNewsList(1)">
					<span class="icon iconfont icon-chakan14"></span>
					搜索
				</el-button>
			</el-form>
			
			<!-- category -->
			<div class="category-list">
				<div class="item" @click="categoryClick(0)" :class="categoryIndex == 0 ? 'active' : ''">全部</div>
				<div v-for="(item,index) in categoryList" @click="categoryClick(index+1)" :key="index" class="item" :class="categoryIndex == index+1 ? 'active' : ''">{{item.typename}}</div>
			</div>
			<div class="list11">
				<div v-for="(item,index) in newsList" :key="index" :class="'list-item' + ((index%2) + 1)" @click="toNewsDetail(item)">
					<div class="img">
						<img class="image" :src="baseUrl + item.picture" >
					</div>
					<div class="infoBox">
						<div class="name">{{item.title}}</div>
						<div class="desc">{{item.introduction}}</div>
						<div class="infoCenter">
							<div class="publisher_item">
								<span class="icon iconfont" :class="index%2==0?'icon-geren16':'icon-geren16'"></span>
								<span class="label">{{index%2==0?'发布人：':'发布人：'}}</span>
								<span class="text">{{item.name}}</span>
							</div>
							<div class="like_item">
								<span class="icon iconfont" :class="index%2==0?'icon-zan10':'icon-zan10'"></span>
								<span class="label">{{index%2==0?'点赞：':'点赞：'}}</span>
								<span class="text">{{item.thumbsupnum}}</span>
							</div>
							<div class="collect_item">
								<span class="icon iconfont" :class="index%2==0?'icon-shoucang10':'icon-shoucang10'"></span>
								<span class="label">{{index%2==0?'收藏：':'收藏：'}}</span>
								<span class="text">{{item.storeupnum}}</span>
							</div>
							<div class="view_item">
								<span class="icon iconfont" :class="index%2==0?'icon-chakan2':'icon-chakan2'"></span>
								<span class="label">{{index%2==0?'浏览次数：':'浏览次数：'}}</span>
								<span class="text">{{item.clicknum}}</span>
							</div>
						</div>
						<div class="infoBottom">
							<div class="time_item">
								<span class="icon iconfont" :class="index%2==0?'icon-shijian21':'icon-shijian21'"></span>
								<span class="label">{{index%2==0?'发布时间：':'发布时间：'}}</span>
								<span class="text">{{index%2==0? item.addtime.split(' ')[0] : item.addtime.split(' ')[0] }}</span>
							</div>
							<div class="more_btn">
								<span class="text">{{index%2==0?'查看更多':'查看更多'}}</span>
								<span class="icon iconfont" :class="index%2==0?'icon-jiantou25':'icon-jiantou25'"></span>
							</div>
						</div>
					</div>
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

			<!-- 热门信息 -->
			<div class="hot">
				<div class="hot-title">热门信息</div>
				<div class="hot-list">
					<div class="hot-item" v-for="item in hotList" :key="item.id" @click="toNewsDetail(item)">
						<img :src="baseUrl + item.picture" alt="">
						<div class="hot-name">{{ item.title }}</div>
						<div class="hot-time">{{item.addtime}}</div>
					</div>
				</div>
			</div>
			<div class="idea1"></div>
		</div>
	</div>
</template>

<script>
	export default {
		//数据集合
		data() {
			return {
				baseUrl: this.$config.baseUrl,
				breadcrumbItem: [
				  {
					name: '购物资讯'
				  }
				],
				newsList: [],
				total: 1,
				pageSize: 10,
				pageSizes: [],
				totalPage: 1,
				layouts: '',
				title: '',
				categoryIndex: 0,
				categoryList: [],
				hotList: [],
			}
		},
		created() {
			this.getCategoryList()
			this.getNewsList(1);
			this.getHotList()
		},
		//方法集合
		methods: {
			getCategoryList(){
				this.$http.get('newstype/list', {}).then(res => {
					if (res.data.code == 0) {
						this.categoryList = res.data.data.list
					}
				});
			},
			categoryClick(index) {
				this.categoryIndex = index
				this.getNewsList()
			},
			getNewsList(page) {
				let params = {page, limit: this.pageSize,sort:'addtime',order:'desc'};
				let searchWhere = {};
				if(this.title != '') searchWhere.title = '%' + this.title + '%';
				if(this.categoryIndex!=0){
					searchWhere.typename = this.categoryList[this.categoryIndex - 1].typename
				}
				this.$http.get('news/list', {params: Object.assign(params, searchWhere)}).then(res => {
					if (res.data.code == 0) {
						this.newsList = res.data.data.list;
						this.total = res.data.data.total;
						this.pageSize = Number(res.data.data.pageSize);
						this.totalPage = res.data.data.totalPage;
						if(this.pageSizes.length==0){
							this.pageSizes = [this.pageSize, this.pageSize*2, this.pageSize*3, this.pageSize*5];
						}
					}
				});
			},
			getHotList(){
				let params = {page:1, limit: 4,sort:'addtime',order:'desc'};
				this.$http.get('news/autoSort', {params: params}).then(res => {
					if (res.data.code == 0) {
						this.hotList = res.data.data.list;
					}
				});
			},
			curChange(page) {
				this.getNewsList(page);
			},
			prevClick(page) {
				this.getNewsList(page);
			},
			nextClick(page) {
				this.getNewsList(page);
			},
			toNewsDetail(item) {
				this.$router.push({path: '/index/newsDetail', query: {id: item.id}});
			}
		}
	}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
	.news-preview-pv {
				margin: 0px auto;
				color: #EDE6D6;
				background: transparent;
				display: flex;
				max-width: var(--tea-content);
				font-size: 16px;
				justify-content: flex-start;
				align-items: flex-start;
				position: relative;
				flex-wrap: wrap;
				.list-form-pv {
						padding: 10px;
						margin: 0;
						background: none;
						display: flex;
						width: 101%;
						align-items: center;
						flex-wrap: wrap;
						height: auto;
						order: 2;
						.search-item {
								margin: 0 0px;
								.el-input {
										width: 100%;
									}
				.el-input ::v-deep .el-input__inner {
										border: 1px solid #ccc;
										border-radius: 4px;
										padding: 0 10px;
										margin: 0 10px 0 0;
										color: #EDE6D6;
										background: transparent;
										width: auto;
										font-size: 16px;
										line-height: 42px;
										min-width: 350px;
										height: 42px;
									}
			}
			.search-btn {
								cursor: pointer;
								border: 0;
								border-radius: 8px;
								padding: 0px 20px;
								margin: 0 10px 0 0;
								color: #fff;
								background: #3E6B4F;
								width: auto;
								font-size: inherit;
								line-height: 42px;
								height: 42px;
								.icon {
										margin: 0 3px 0 0;
										color: #fff;
										font-size: inherit;
									}
			}
		}
		.category-list {
						padding: 0;
						margin: 20px 0 0;
						background: none;
						display: flex;
						width: 101%;
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
								background: linear-gradient(90deg, #12362A 0%, #3E6B4F 100%) no-repeat left center / auto 100%,linear-gradient(90deg, #12362A 0%, #3E6B4F 100%) no-repeat right center / auto 100%,linear-gradient(90deg, #12362A 0%, #3E6B4F 100%) repeat-x center center / auto 100%;
								justify-content: center;
								align-items: center;
								min-width: 120px;
								height: 94px;
							}
			
			.item:hover {
								color: #fff;
								background: linear-gradient(90deg, #12362A 0%, #3E6B4F 100%) no-repeat left center / auto 100%,linear-gradient(90deg, #12362A 0%, #3E6B4F 100%) no-repeat right center / auto 100%,linear-gradient(90deg, #12362A 0%, #3E6B4F 100%) repeat-x center center / auto 100%;
							}
			
			.item.active {
								color: #fff;
								background: linear-gradient(90deg, #12362A 0%, #3E6B4F 100%) no-repeat left center / auto 100%,linear-gradient(90deg, #12362A 0%, #3E6B4F 100%) no-repeat right center / auto 100%,linear-gradient(90deg, #12362A 0%, #3E6B4F 100%) repeat-x center center / auto 100%;
							}
		}
		.list11 {
						margin: 0px auto ;
						overflow: hidden;
						color: #93A396;
						flex: 1;
						width: calc(100% - 0px);
						clear: both;
						font-size: 15px;
						order: 8;
						.list-item1 {
								padding: 10px 10px 20px;
								margin: 60px 0 0;
								background: transparent;
								width: 100%;
								min-height: 260px;
								border-color: #2c2c2c;
								border-width: 1px;
								position: relative;
								border-style: solid;
								height: auto;
								.img {
										overflow: hidden;
										display: none;
										width: 275px;
										float: left;
										height: 275px;
										img {
												object-fit: cover;
												width: 100%;
												transition: all 0.6s;
												height: 100%;
											}
				}
				.infoBox {
										padding: 20px;
										width: calc(100% - 0px);
										position: relative;
										float: right;
										height: 100%;
										.name {
												border: 0px solid #eee;
												padding: 10px 0;
												color: #EDE6D6;
												background: none;
												font-weight: 600;
												width: calc(100% - 0px);
												font-size: 18px;
												line-height: 30px;
												position: relative;
											}
					.name::after {
												left: 0;
												background: #fcb76f;
												bottom: 0;
												width: 0px;
												position: absolute;
												content: "";
												transition: all 0.5s ease;
												height: 2px;
											}
					.desc {
												padding: 0 10px 0 0;
												margin: 5px 0 0;
												overflow: hidden;
												color: #93A396;
												text-indent: 2em;
												font-size: 15px;
												line-height: 30px;
												height: 120px;
											}
					.infoCenter {
												padding: 5px 0;
												margin: 0px 0 0;
												display: flex;
												width: 100%;
												justify-content: flex-start;
												flex-wrap: wrap;
												.publisher_item {
														padding: 0 10px 0 0;
														.icon {
																margin: 0 2px 0 0;
																line-height: 1.5;
															}
							.label {
																line-height: 1.5;
															}
							.text {
																line-height: 1.5;
															}
						}
						.like_item {
														padding: 0 10px 0 0;
														.icon {
																margin: 0 2px 0 0;
																line-height: 1.5;
															}
							.label {
																line-height: 1.5;
															}
							.text {
																line-height: 1.5;
															}
						}
						.collect_item {
														padding: 0 10px 0 0;
														.icon {
																margin: 0 2px 0 0;
																line-height: 1.5;
															}
							.label {
																line-height: 1.5;
															}
							.text {
																line-height: 1.5;
															}
						}
						.view_item {
														padding: 0 10px 0 0;
														.icon {
																margin: 0 2px 0 0;
																line-height: 1.5;
															}
							.label {
																line-height: 1.5;
															}
							.text {
																line-height: 1.5;
															}
						}
					}
					.infoBottom {
												margin: 0px 0 0;
												display: flex;
												justify-content: space-between;
												align-items: center;
												flex-wrap: wrap;
												.time_item {
														padding: 0 20px;
														z-index: 999;
														top: -42px;
														color: #fff;
														left: -12px;
														background: #2c2c2c;
														font-size: 16px;
														line-height: 60px;
														position: absolute;
														height: 60px;
														.icon {
																margin: 0 2px 0 0;
																line-height: 1.5;
															}
							.label {
																line-height: 1.5;
															}
							.text {
																line-height: 1.5;
															}
						}
						.more_btn {
														border: 1px solid #eee;
														border-radius: 20px;
														padding: 10px;
														background: transparent;
														display: none;
														width: 150px;
														text-align: center;
														.text {
																color: inherit;
															}
							.icon {
																color: inherit;
															}
						}
					}
				}
			}
			.list-item1:hover {
								cursor: pointer;
								box-shadow: 1px 2px 9px #3E6B4F;
								background: transparent;
								border-color: #3E6B4F;
								.img {
					img {
												transform: scale(1.05);
											}
				}
				.infoBox {
										.name {
												border: 0px solid #ffffff50;
												color: #3E6B4F;
												background: none;
											}
					.name::after {
												width: 0px;
											}
					.desc {
												color: #3E6B4F;
											}
					.infoCenter {
						.publisher_item {
							.icon {
																color: #3E6B4F;
															}
							.label {
																color: #3E6B4F;
															}
							.text {
																color: #3E6B4F;
															}
						}
						.like_item {
							.icon {
																color: #3E6B4F;
															}
							.label {
																color: #3E6B4F;
															}
							.text {
																color: #3E6B4F;
															}
						}
						.collect_item {
							.icon {
																color: #3E6B4F;
															}
							.label {
																color: #3E6B4F;
															}
							.text {
																color: #3E6B4F;
															}
						}
						.view_item {
							.icon {
																color: #3E6B4F;
															}
							.label {
																color: #3E6B4F;
															}
							.text {
																color: #3E6B4F;
															}
						}
					}
					.infoBottom {
						.time_item {
							.icon {
																color: #fff;
															}
							.label {
																color: #fff;
															}
							.text {
																color: #fff;
															}
						}
						.more_btn {
														background: #ffffff30;
														.text {
																color: #93A396;
															}
							.icon {
																color: #93A396;
															}
						}
					}
				}
			}
			.list-item2 {
								padding: 10px 10px 20px;
								margin: 60px 0 0;
								background: transparent;
								width: 100%;
								min-height: 260px;
								border-color: #2c2c2c;
								border-width: 1px;
								position: relative;
								border-style: solid;
								height: auto;
								.img {
										margin: 0 0 0 20px;
										overflow: hidden;
										display: none;
										width: 275px;
										float: right;
										height: 275px;
										order: 2;
										img {
												object-fit: cover;
												width: 100%;
												transition: all 0.6s;
												height: 100%;
											}
				}
				.infoBox {
										padding: 20px;
										width: calc(100% - 0px);
										position: relative;
										float: left;
										height: 100%;
										order: 1;
										.name {
												border: 0px solid #eee;
												padding: 10px 0;
												color: #EDE6D6;
												background: none;
												font-weight: 600;
												width: calc(100% - 0px);
												font-size: 18px;
												line-height: 30px;
												position: relative;
											}
					.name::after {
												left: 0;
												background: #fcb76f;
												bottom: 0;
												width: 0px;
												position: absolute;
												content: "";
												transition: all 0.5s ease;
												height: 2px;
											}
					.desc {
												padding: 0 10px 0 0;
												margin: 5px 0 0;
												overflow: hidden;
												color: #93A396;
												text-indent: 2em;
												font-size: 15px;
												line-height: 30px;
												height: 120px;
											}
					.infoCenter {
												padding: 5px 0;
												margin: 0px 0 0;
												display: flex;
												width: 100%;
												justify-content: flex-start;
												flex-wrap: wrap;
												.publisher_item {
														padding: 0 10px 0 0;
														.icon {
																margin: 0 2px 0 0;
																line-height: 1.5;
															}
							.label {
																line-height: 1.5;
															}
							.text {
																line-height: 1.5;
															}
						}
						.like_item {
														padding: 0 10px 0 0;
														.icon {
																margin: 0 2px 0 0;
																line-height: 1.5;
															}
							.label {
																line-height: 1.5;
															}
							.text {
																line-height: 1.5;
															}
						}
						.collect_item {
														padding: 0 10px 0 0;
														.icon {
																margin: 0 2px 0 0;
																line-height: 1.5;
															}
							.label {
																line-height: 1.5;
															}
							.text {
																line-height: 1.5;
															}
						}
						.view_item {
														padding: 0 10px 0 0;
														.icon {
																margin: 0 2px 0 0;
																line-height: 1.5;
															}
							.label {
																line-height: 1.5;
															}
							.text {
																line-height: 1.5;
															}
						}
					}
					.infoBottom {
												margin: 0px 0 0;
												display: flex;
												justify-content: space-between;
												align-items: center;
												flex-wrap: wrap;
												.time_item {
														padding: 0 20px;
														z-index: 999;
														top: -42px;
														color: #fff;
														left: -12px;
														background: #2c2c2c;
														font-size: 16px;
														line-height: 60px;
														position: absolute;
														height: 60px;
														.icon {
																margin: 0 2px 0 0;
																line-height: 1.5;
															}
							.label {
																line-height: 1.5;
															}
							.text {
																line-height: 1.5;
															}
						}
						.more_btn {
														border: 1px solid #eee;
														border-radius: 20px;
														padding: 10px;
														display: none;
														width: 150px;
														text-align: center;
														.text {
																color: inherit;
															}
							.icon {
																color: inherit;
															}
						}
					}
				}
			}
			.list-item2:hover {
								cursor: pointer;
								box-shadow: 1px 2px 9px #3E6B4F;
								background: transparent;
								border-color: #3E6B4F;
								.img {
					img {
												transform: scale(1.05);
											}
				}
				.infoBox {
										.name {
												border: 0px solid #ffffff50;
												color: #3E6B4F;
												background: none;
											}
					.name::after {
												width: 0px;
											}
					.desc {
												color: #3E6B4F;
											}
					.infoCenter {
						.publisher_item {
							.icon {
																color: #3E6B4F;
															}
							.label {
																color: #3E6B4F;
															}
							.text {
																color: #3E6B4F;
															}
						}
						.like_item {
							.icon {
																color: #3E6B4F;
															}
							.label {
																color: #3E6B4F;
															}
							.text {
																color: #3E6B4F;
															}
						}
						.collect_item {
							.icon {
																color: #3E6B4F;
															}
							.label {
																color: #3E6B4F;
															}
							.text {
																color: #3E6B4F;
															}
						}
						.view_item {
							.icon {
																color: #3E6B4F;
															}
							.label {
																color: #3E6B4F;
															}
							.text {
																color: #3E6B4F;
															}
						}
					}
					.infoBottom {
						.time_item {
							.icon {
																color: #fff;
															}
							.label {
																color: #fff;
															}
							.text {
																color: #fff;
															}
						}
						.more_btn {
														background: #ffffff30;
														.text {
																color: #93A396;
															}
							.icon {
																color: #93A396;
															}
						}
					}
				}
			}
		}
		.hot {
						margin: 30px 40px 0 0;
						background: none;
						width: 350px;
						height: auto;
						order: 7;
						.hot-title {
								padding: 0;
								margin: 0;
								color: #fff;
								background: linear-gradient(90deg, #12362A 0%, #3E6B4F 100%) no-repeat center top / 100% 100%;
								font-weight: 600;
								width: 100%;
								font-size: 26px;
								line-height: 100px;
								position: relative;
								text-align: center;
							}
			.hot-list {
								padding: 20px;
								margin: 0;
								background: transparent;
								display: flex;
								width: 100%;
								justify-content: space-between;
								flex-wrap: wrap;
								height: auto;
								.hot-item {
										cursor: pointer;
										border: 2px solid #e2e1e1;
										border-radius: 10px;
										padding: 10px;
										margin: 0 0 20px;
										background: transparent;
										width: calc(100% - 0px);
										height: auto;
										img {
												border-radius: 100px;
												object-fit: cover;
												display: block;
												width: 130px;
												float: left;
												height: 200px;
											}
					.hot-name {
												padding: 0px 0px;
												overflow: hidden;
												color: #EDE6D6;
												white-space: nowrap;
												width: calc(100% - 150px);
												font-size: 15px;
												line-height: 40px;
												text-overflow: ellipsis;
												float: right;
											}
					.hot-time {
												padding: 0 0px;
												color: #93A396;
												display: inline-block;
												width: calc(100% - 150px);
												font-size: 14px;
												line-height: 24px;
												float: right;
												text-align: left;
											}
				}
			}
		}
		.idea1 {
						background: transparent;
						width: 101%;
						height: 1px;
						order: 10;
					}
	}
	
	.index-pv1 .animation-box {
		transform: rotate(0deg) scale(1) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
		z-index: initial;
	}
	
	.index-pv1 .animation-box:hover {
				transform: rotate(0deg) scale(1) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
				-webkit-perspective: 1000px;
				perspective: 1000px;
				transition: 0.3s;
				z-index: 1;
	}
	
	.index-pv1 .animation-box img {
		transform: rotate(0deg) scale(1) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
	}
	
	.index-pv1 .animation-box img:hover {
				transform: rotate(0deg) scale(0.98) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
				-webkit-perspective: 1000px;
				perspective: 1000px;
				transition: 0.3s;
			}
</style>
