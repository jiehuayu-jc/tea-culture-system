<template>
	<div>
		<div class="breadcrumb-preview">
			<el-breadcrumb :separator="'≡'">
				<el-breadcrumb-item class="item1" to="/"><a>首页</a></el-breadcrumb-item>
				<el-breadcrumb-item class="item2" v-for="(item, index) in breadcrumbItem" :key="index"><a>{{item.name}}</a></el-breadcrumb-item>
			</el-breadcrumb>
		</div>
		<div v-if="centerType" class="back_box">
			<el-button class="backBtn" size="mini" @click="backClick">
				<span class="icon iconfont icon-jiantou33"></span>
				<span class="text">返回</span>
			</el-button>
		</div>
		<div class="list-preview">
			<el-form :inline="true" :model="formSearch" class="list-form-pv">
				<el-form-item class="list-item">
					<div class="lable">茶商账号：</div>
					<el-input v-model="formSearch.shangjiazhanghao" placeholder="茶商账号" @keydown.enter.native="getList(1, curFenlei)" clearable></el-input>
				</el-form-item>
				<el-form-item class="list-item">
					<div class="lable">茶商名称：</div>
					<el-input v-model="formSearch.shangjiamingcheng" placeholder="茶商名称" @keydown.enter.native="getList(1, curFenlei)" clearable></el-input>
				</el-form-item>
				<el-form-item class="list-item">
					<div class="lable">负责人：</div>
					<el-input v-model="formSearch.fuzeren" placeholder="负责人" @keydown.enter.native="getList(1, curFenlei)" clearable></el-input>
				</el-form-item>
				<el-button class="list-search-btn" v-if=" true " type="primary" @click="getList(1, curFenlei)">
					<i class="el-icon-search"></i>
					查询
				</el-button>
				<el-button class="list-add-btn" v-if="btnAuth('shangjia','新增')" type="primary" @click="add('/index/shangjiaAdd')">
					<i class="el-icon-circle-plus-outline"></i>
					添加
				</el-button>
			</el-form>
			<div class="select2">
				<div class="select2-list" v-for="(item,index) in selectOptionsList" :key="item">
					<div class="label">{{item.name}}：</div>
					<div class="item-body">
						<div class="item" @click="selectClick2(item,-1)" :class="item.check ==-1 ? 'active' : ''">全部</div>
						<div class="item" @click="selectClick2(item,index1)" :class="item.check == index1 ? 'active' : ''" v-for="item1,index1 in item.list" :key="item1">{{item1}}</div>
					</div>
				</div>
			</div>
			<div class="list">
				<!-- 样式二 -->
				<div class="list2 index-pv1">
					<div v-for="(item, index) in dataList" :key="index" class="list-item animation-box">
						<div class="img">
							<img @click.stop="imgPreView(item.touxiang.split(',')[0])" v-if="item.touxiang && item.touxiang.substr(0,4)=='http'" :src="item.touxiang.split(',')[0]" class="image" />
							<img @click.stop="imgPreView(baseUrl + (item.touxiang?item.touxiang.split(',')[0]:''))" v-else :src="baseUrl + (item.touxiang?item.touxiang.split(',')[0]:'')" class="image" />
						</div>
						<div class="item-info">
							<div class="name">茶商名称:{{item.shangjiamingcheng}}</div>
							<div class="name">{{item.lianxidianhua}}</div>
							<div class="time_item">
								<span class="icon iconfont icon-shijian21"></span>
								<span class="label">发布时间：</span>
								<span class="text">{{item.addtime.split(' ')[0]}}</span>
							</div>
							<div class="more_btn" @click.stop="toDetail(item)">
							  查看详情
							</div>
						</div>
					</div>
				</div>
			</div>

	
			<el-pagination
				background
				id="pagination"
				class="pagination"
				:pager-count="7"
				:page-size="pageSize"
				prev-text="上一页"
				next-text="下一页"
				:hide-on-single-page="false"
				:layout='["total","prev","pager","next","sizes","jumper"].join()'
				:total="total"
				:page-sizes="pageSizes"
				@current-change="curChange"
				@size-change="sizeChange"
				@prev-click="prevClick"
				@next-click="nextClick"
				></el-pagination>
		</div>
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
				selectIndex2: 0,
				selectOptionsList: [],
				layouts: '',
				swiperIndex: -1,
				baseUrl: '',
				breadcrumbItem: [
					{
						name: '茶商'
					}
				],
				formSearch: {
					shangjiazhanghao: '',
					shangjiamingcheng: '',
					fuzeren: '',
				},
				fenlei: [],
				feileiColumn: '',
				dataList: [],
				total: 1,
				pageSize: 12,
				pageSizes: [],
				totalPage: 1,
				curFenlei: '全部',
				isPlain: false,
				indexQueryCondition: '',
				timeRange: [],
				centerType:false,
				previewImg: '',
				previewVisible: false,
				sortType: 'id',
				sortOrder: 'desc',
			}
		},
		async created() {
			if(this.$route.query.centerType&&this.$route.query.centerType!=0){
				this.centerType = true
			}
			this.baseUrl = this.$config.baseUrl;
			await this.getFenlei();
			let fenlei = '全部'
			if(this.$route.query.homeFenlei){
				fenlei = this.$route.query.homeFenlei
			}
			this.getList(1, fenlei);
		},
		watch:{
			$route(newValue){
				this.getList(1, newValue.query.homeFenlei);
			}
		},
		//方法集合
		methods: {
			selectClick2(row,index) {
				row.check = index
				if(index == -1){
					this.formSearch[row.tableName] = ''
				}else {
					this.formSearch[row.tableName] = row.list[index]
				}
				this.getList()
			},
			add(path) {
				let query = {}
				if(this.centerType){
					query.centerType = 1
				}
				this.$router.push({path: path,query:query});
			},
			async getFenlei() {
			},
			getList(page, fenlei, ref = '') {
				let params = {
					page,
					limit: this.pageSize,
				};
				let searchWhere = {};
				if (this.formSearch.shangjiazhanghao != '') searchWhere.shangjiazhanghao = '%' + this.formSearch.shangjiazhanghao + '%';
				if (this.formSearch.shangjiamingcheng != '') searchWhere.shangjiamingcheng = '%' + this.formSearch.shangjiamingcheng + '%';
				if (this.formSearch.fuzeren != '') searchWhere.fuzeren = '%' + this.formSearch.fuzeren + '%';
				if(!this.centerType){
					params['sfsh'] = '是';
				}
				let user = JSON.parse(localStorage.getItem('sessionForm'))
				if (this.sortType) searchWhere.sort = this.sortType
				if (this.sortOrder) searchWhere.order = this.sortOrder
				this.$http.get(`shangjia/${this.centerType?'page':'list'}`, {params: Object.assign(params, searchWhere)}).then(res => {
					if (res.data.code == 0) {
						this.dataList = res.data.data.list;
						this.total = Number(res.data.data.total);
						this.pageSize = Number(res.data.data.pageSize);
						this.totalPage = res.data.data.totalPage;
						if(this.pageSizes.length==0){
							this.pageSizes = [this.pageSize, this.pageSize*2, this.pageSize*3, this.pageSize*5];
						}
					}
				});
			},
			curChange(page) {
				this.getList(page);
			},
			prevClick(page) {
				this.getList(page);
			},
			sizeChange(size){
				this.pageSize = size
				this.getList(1);
			},
			nextClick(page) {
				this.getList(page);
			},
			imgPreView(url){
				this.previewImg = url
				this.previewVisible = true
			},
			toDetail(item) {
				let params = {
					id: item.id
				}
				if(this.centerType){
					params.centerType = 1
				}
				this.$router.push({path: '/index/shangjiaDetail', query: params});
			},
			btnAuth(tableName,key){
				if(this.centerType){
					return this.isBackAuth(tableName,key)
				}else{
					return this.isAuth(tableName,key)
				}
			},
			backClick() {
				this.$router.push({path: '/index/center'});
			},
		}
	}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
	.list-preview {
		padding: 0 calc((100% - 1400px)/2);
		margin: 0px auto;
		color: #333;
		background: #f6f6f6;
		display: flex;
		width: 100%;
		font-size: 16px;
		justify-content: flex-start;
		align-items: flex-start;
		position: relative;
		flex-wrap: wrap;
		.list-form-pv {
			padding: 10px;
			margin: 20px 0;
			color: inherit;
			background: none;
			display: flex;
			width: 100%;
			font-size: inherit;
			flex-wrap: wrap;
			height: auto;
			order: 2;
			.list-item {
				padding: 0;
				margin: 0 0px 10px 0;
				display: flex;
				font-size: inherit;
				align-items: center;
				flex-wrap: wrap;
				/deep/.el-form-item__content {
					display: flex;
				}
				.lable {
					padding: 0 10px;
					color: #333;
					white-space: nowrap;
					display: inline-block;
					width: auto;
					font-size: 16px;
					line-height: 40px;
				}
				.el-input {
					width: auto;
				}
				.datetimerange {
					border: 1px solid #ccc;
					border-radius: 8px;
					padding: 3px 3px;
					background: #fff;
					width: auto;
					justify-content: center;
				}
				.el-input /deep/ .el-input__inner {
					border: 1px solid #ccc;
					border-radius: 4px;
					padding: 0 10px;
					margin: 0 5px 0 0;
					color: #333;
					width: auto;
					font-size: 16px;
					line-height: 40px;
					height: 40px;
				}
				.el-select {
					width: 100%;
				}
				.el-select /deep/ .el-input__inner {
				}
				.el-date-editor {
					width: auto;
				}
				.el-date-editor /deep/ .el-input__inner {
					border: 1px solid #ccc;
					border-radius: 4px;
					padding: 0 0px 0 30px;
					margin: 0;
					color: #333;
					width: auto;
					font-size: 16px;
					line-height: 40px;
					height: 40px;
				}
			}
			.list-search-btn {
				cursor: pointer;
				border: 0;
				border-radius: 4px;
				padding: 0px 15px;
				margin: 0 10px 0 10px;
				color: #fff;
				background: #1547ea;
				width: auto;
				font-size: inherit;
				line-height: 40px;
				height: 40px;
				i {
					margin: 0 10px 0 0;
					color: #fff;
					font-size: inherit;
				}
			}
			.list-add-btn {
				cursor: pointer;
				border: 0;
				border-radius: 4px;
				padding: 0px 15px;
				margin: 0 10px 0 0;
				color: #fff;
				background: #5a95db;
				width: auto;
				font-size: inherit;
				line-height: 40px;
				height: 40px;
				i {
					margin: 0 10px 0 0;
					color: #fff;
					font-size: inherit;
				}
			}
		}
		.select2 {
			border: 0px solid #999;
			padding: 0;
			margin: 0 auto;
			background: #f3f3f3;
			width: 100%;
			font-size: 15px;
			height: auto;
			order: 3;
			.select2-list {
				padding: 0 20px;
				margin: 0 0 10px;
				background: url(http://codegen.caihongy.cn/20241023/98a588f51ab348ef8235791bab1732b1.png) repeat-x center center / auto 100%;
				width: 100%;
				min-height: 80px;
				height: auto;
				.label {
					padding: 0 5px;
					color: #333;
					font-weight: 500;
					display: inline-block;
					font-size: inherit;
					line-height: 80px;
				}
				.item-body {
					display: inline-block;
					width: auto;
					flex-wrap: wrap;
					height: auto;
					.item {
						border-radius: 4px;
						padding: 0 5px;
						color: inherit;
						background: none;
						display: inline-block;
						font-size: inherit;
						line-height: 80px;
						text-align: center;
						min-width: 80px;
					}
					.item:hover {
						cursor: pointer;
						color: #fff;
						background: url(http://codegen.caihongy.cn/20241023/01e4dfdb2dfb41fe9b73dd5a59b60c41.png) no-repeat center center / 100% 100%;
					}
					.item.active {
						cursor: pointer;
						color: #fff;
						background: url(http://codegen.caihongy.cn/20241023/01e4dfdb2dfb41fe9b73dd5a59b60c41.png) no-repeat center center / 100% 100%;
						display: inline-block;
						min-width: 80px;
						text-align: center;
					}
				}
			}
		}
		.list {
			margin: -22px 0 0;
			overflow: hidden;
			background: none;
			width: calc(100% - 0px);
			clear: both;
			font-size: 15px;
			order: 8;
			.index-pv1 .animation-box {
				transform: rotate(0deg) scale(1) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
				z-index: initial;
			}
				
			.index-pv1 .animation-box:hover {
				transform: rotate(0) scale(1) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
				-webkit-perspective: 1000px;
				perspective: 1000px;
				transition: 0.3s;
				z-index: 1;
			}
				
			.index-pv1 .animation-box img {
				transform: rotate(0deg) scale(1) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
			}
			
			.index-pv1 .animation-box img:hover {
				transform: rotate(0) scale(1) skew(0deg, 0deg) translate3d(0px, 0px, 0px);
				-webkit-perspective: 1000px;
				perspective: 1000px;
				transition: 0.3s;
			}
			.list2 {
				border-radius: 0 0 20px 20px;
				padding: 20px;
				margin: 0 auto;
				background: #fff;
				display: flex;
				width: calc(100% - 20px);
				flex-wrap: wrap;
				height: auto;
				.list-item {
					cursor: pointer;
					padding: 10px;
					margin: 0 10px 20px;
					background: #f5f5f5;
					display: flex;
					width: calc(50% - 20px);
					position: relative;
					height: auto;
					.img {
						border: 1px solid #0a34bc;
						padding: 10px;
						overflow: hidden;
						width: 290px;
						height: 290px;
						.image {
							object-fit: cover;
							display: block;
							width: 100%;
							transition: all 0.4s;
							height: 100%;
						}
					}
					.item-info {
						padding: 10px;
						overflow: hidden;
						color: #666;
						flex: 1;
						display: inline-block;
						font-size: 15px;
						height: 290px;
						.name {
							padding: 0 10px;
							overflow: hidden;
							color: #3E6B4F;
							white-space: nowrap;
							width: 100%;
							font-size: 16px;
							line-height: 1.5;
							text-overflow: ellipsis;
						}
						.price {
							padding: 0 10px;
							color: #f00;
							font-size: 22px;
							line-height: 40px;
							order: 10;
						}
						.time_item {
							padding: 0 10px;
							color: #00ddff;
							.icon {
								margin: 0 2px 0 0;
								color: inherit;
								font-size: inherit;
								line-height: 1.5;
							}
							.label {
								color: inherit;
								font-size: inherit;
								line-height: 1.5;
							}
							.text {
								color: inherit;
								font-size: inherit;
								line-height: 1.5;
							}
						}
						.publisher_item {
							padding: 0 10px;
							color: #0058bb;
							display: inline-block;
							.icon {
								margin: 0 2px 0 0;
								color: inherit;
								font-size: inherit;
								line-height: 1.5;
							}
							.label {
								color: inherit;
								font-size: inherit;
								line-height: 1.5;
							}
							.text {
								color: inherit;
								font-size: inherit;
								line-height: 1.5;
							}
						}
						.like_item {
							padding: 0 10px;
							color: #0071fc;
							display: inline-block;
							.icon {
								margin: 0 2px 0 0;
								color: inherit;
								font-size: inherit;
								line-height: 1.5;
							}
							.label {
								color: inherit;
								font-size: inherit;
								line-height: 1.5;
							}
							.text {
								color: inherit;
								font-size: inherit;
								line-height: 1.5;
							}
						}
						.collect_item {
							padding: 0 10px;
							color: #ffb41d;
							display: inline-block;
							.icon {
								margin: 0 2px 0 0;
								color: inherit;
								font-size: inherit;
								line-height: 1.5;
							}
							.label {
								color: inherit;
								font-size: inherit;
								line-height: 1.5;
							}
							.text {
								color: inherit;
								font-size: inherit;
								line-height: 1.5;
							}
						}
						.view_item {
							padding: 0 10px;
							color: #25b8b3;
							display: inline-block;
							.icon {
								margin: 0 2px 0 0;
								color: inherit;
								font-size: inherit;
								line-height: 1.5;
							}
							.label {
								color: inherit;
								font-size: inherit;
								line-height: 1.5;
							}
							.text {
								color: inherit;
								font-size: inherit;
								line-height: 1.5;
							}
						}
						.more_btn {
							margin: 20px 0 0;
							z-index: 9;
							color: #fff;
							bottom: 10px;
							display: block;
							font-size: 15px;
							line-height: 34px;
							transition: all .4s ease;
							border-radius: 0;
							left: 296px;
							background: url(http://codegen.caihongy.cn/20241025/1ce93313a09c40e192d02f162aac65f6.png) no-repeat left center / 100% 100%;
							width: 150px;
							position: absolute;
							text-align: center;
							height: 44px;
						}
					}
				}
				.list-item::before {
					border: 5px solid #2e89ff;
					transform: scale3d(0, 1, 1);
					top: 0;
					left: 0;
					width: 100%;
					border-width: 5px 0;
					position: absolute;
					transform-origin: left;
					box-sizing: inherit;
					content: "";
					height: 100%;
				}
				.list-item::after {
					border: 5px solid #2e89ff;
					transform: scale3d(1, 0, 1);
					top: 0;
					left: 0;
					width: 100%;
					border-width: 0 5px;
					position: absolute;
					transform-origin: bottom;
					box-sizing: inherit;
					content: "";
					height: 100%;
				}
				.list-item:hover {
					background: #2e89ff10;
					.img {
						.image {
							transform: scale(1.05);
						}
					}
					.item-info {
						.name {
							color: #2e89ff;
						}
						.more_btn {
							background: url(http://codegen.caihongy.cn/20241025/1ce93313a09c40e192d02f162aac65f6.png) no-repeat left center / 100% 100%;
						}
					}
				}
				.list-item:hover::before {
					transform: scale3d(1, 1, 1);
					transition: transform 0.4s;
				}
				.list-item:hover::after {
					transform: scale3d(1, 1, 1);
					transition: transform 0.4s;
				}
			}
		}
	}
</style>
