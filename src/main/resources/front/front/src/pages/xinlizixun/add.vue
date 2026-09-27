<template>
	<div class="add-update-preview">
		<el-form
			class="add-update-form"
			ref="ruleForm"
			:model="ruleForm"
			:rules="rules"
			label-width="180px"
			>
			<el-form-item class="add-item" label="讲座名称" prop="zixunmingcheng">
				<el-input v-model="ruleForm.zixunmingcheng" 
					placeholder="讲座名称" clearable :disabled=" false  ||ro.zixunmingcheng"></el-input>
			</el-form-item>
			
			<el-form-item class="add-item" label="封面图片" v-if="type!='cross' || (type=='cross' && !ro.fengmiantupian)" prop="fengmiantupian">
				<file-upload
					tip="点击上传封面图片"
					action="file/upload"
					:limit="3"
					:multiple="true"
					:fileUrls="ruleForm.fengmiantupian?ruleForm.fengmiantupian:''"
					@change="fengmiantupianUploadChange"
					></file-upload>
			</el-form-item>
			<el-form-item class="add-item" v-else label="封面图片" prop="fengmiantupian">
				<img v-if="ruleForm.fengmiantupian.substring(0,4)=='http'" class="upload-img" v-bind:key="index" :src="ruleForm.fengmiantupian.split(',')[0]">
				<img v-else class="upload-img" v-bind:key="index" v-for="(item,index) in ruleForm.fengmiantupian.split(',')" :src="baseUrl+item">
			</el-form-item>
			<el-form-item class="add-item" label="讲座费用" prop="zixunfeiyong">
				<el-input-number v-model="ruleForm.zixunfeiyong" placeholder="讲座费用" :disabled=" false ||ro.zixunfeiyong"></el-input-number>
			</el-form-item>
			<el-form-item class="add-item" label="开放时间" prop="kaifangshijian">
				<el-input v-model="ruleForm.kaifangshijian" 
					placeholder="开放时间" clearable :disabled=" false  ||ro.kaifangshijian"></el-input>
			</el-form-item>
			<el-form-item class="add-item" label="讲座介绍" prop="zixunjieshao">
				<el-input v-model="ruleForm.zixunjieshao" 
					placeholder="讲座介绍" clearable :disabled=" false  ||ro.zixunjieshao"></el-input>
			</el-form-item>
			
			<el-form-item class="add-item" label="讲座详情" prop="zixunxiangqing">
				<editor 
					v-model="ruleForm.zixunxiangqing" 
					class="editor" 
					action="file/upload">
				</editor>
			</el-form-item>

			<el-form-item class="add-btn-item">
				<el-button class="submitBtn"  type="primary" @click="onSubmit">
					<span class="icon iconfont icon-kaitongfuwu"></span>
					<span class="text">提交</span>
				</el-button>
				<el-button class="closeBtn" @click="back()">
					<span class="icon iconfont icon-shanchu1"></span>
					<span class="text">取消</span>
				</el-button>
			</el-form-item>
		</el-form>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				id: '',
				baseUrl: '',
				ro:{
					zixunmingcheng : false,
					zixunfenlei : false,
					fengmiantupian : false,
					zixunfeiyong : false,
					kaifangshijian : false,
					zixunjieshao : false,
					zixunxiangqing : false,
					zixunshizhanghao : false,
					zixunshixingming : false,
				},
				type: '',
				userTableName: localStorage.getItem('UserTableName'),
				ruleForm: {
					zixunmingcheng: '',
					zixunfenlei: '',
					fengmiantupian: '',
					zixunfeiyong: '',
					kaifangshijian: '',
					zixunjieshao: '',
					zixunxiangqing: '',
					zixunshizhanghao: '',
					zixunshixingming: '',
				},
				zixunfenleiOptions: [],


				rules: {
					zixunmingcheng: [
					],
					zixunfenlei: [
					],
					fengmiantupian: [
					],
					zixunfeiyong: [
						{ validator: this.$validate.isNumber, trigger: 'blur' },
					],
					kaifangshijian: [
					],
					zixunjieshao: [
					],
					zixunxiangqing: [
					],
					zixunshizhanghao: [
					],
					zixunshixingming: [
					],
				},
				centerType: false,
			};
		},
		computed: {



		},
		components: {
		},
		created() {
			if(this.$route.query.centerType){
				this.centerType = true
			}
			//this.bg();
			let type = this.$route.query.type ? this.$route.query.type : '';
			this.init(type);
			this.baseUrl = this.$config.baseUrl;
		},
		methods: {
			getMakeZero(s) {
				return s < 10 ? '0' + s : s;
			},
			// 下载
			download(file){
				window.open(`${file}`)
			},
			// 初始化
			init(type) {
				this.type = type;
				if(type=='cross'){
					var obj = JSON.parse(localStorage.getItem('crossObj'));
					for (var o in obj){
						if(o=='zixunmingcheng'){
							this.ruleForm.zixunmingcheng = obj[o];
							this.ro.zixunmingcheng = true;
							continue;
						}
						if(o=='zixunfenlei'){
							this.ruleForm.zixunfenlei = obj[o];
							this.ro.zixunfenlei = true;
							continue;
						}
						if(o=='fengmiantupian'){
							this.ruleForm.fengmiantupian = obj[o].split(",")[0];
							this.ro.fengmiantupian = true;
							continue;
						}
						if(o=='zixunfeiyong'){
							this.ruleForm.zixunfeiyong = obj[o];
							this.ro.zixunfeiyong = true;
							continue;
						}
						if(o=='kaifangshijian'){
							this.ruleForm.kaifangshijian = obj[o];
							this.ro.kaifangshijian = true;
							continue;
						}
						if(o=='zixunjieshao'){
							this.ruleForm.zixunjieshao = obj[o];
							this.ro.zixunjieshao = true;
							continue;
						}
						if(o=='zixunxiangqing'){
							this.ruleForm.zixunxiangqing = obj[o];
							this.ro.zixunxiangqing = true;
							continue;
						}
						if(o=='zixunshizhanghao'){
							this.ruleForm.zixunshizhanghao = obj[o];
							this.ro.zixunshizhanghao = true;
							continue;
						}
						if(o=='zixunshixingming'){
							this.ruleForm.zixunshixingming = obj[o];
							this.ro.zixunshixingming = true;
							continue;
						}
					}
				}else if(type=='edit'){
					this.info()
				}
				// 获取用户信息
				this.$http.get(this.userTableName + '/session', {emulateJSON: true}).then(res => {
					if (res.data.code == 0) {
						var json = res.data.data;
						if((json.zixunshizhanghao!=''&&json.zixunshizhanghao) || json.zixunshizhanghao==0){
							this.ruleForm.zixunshizhanghao = json.zixunshizhanghao;
							this.ro.zixunshizhanghao = true;
						}
						if((json.zixunshixingming!=''&&json.zixunshixingming) || json.zixunshixingming==0){
							this.ruleForm.zixunshixingming = json.zixunshixingming;
							this.ro.zixunshixingming = true;
						}
					}
				});
				this.$http.get('option/zixunfenlei/zixunfenlei', {emulateJSON: true}).then(res => {
					if (res.data.code == 0) {
						this.zixunfenleiOptions = res.data.data;
					}
				});

				if (localStorage.getItem('raffleType') && localStorage.getItem('raffleType') != null) {
					localStorage.removeItem('raffleType')
					setTimeout(() => {
						this.onSubmit()
					}, 300)
				}
			},

			// 多级联动参数
			// 多级联动参数
			info() {
				this.$http.get(`xinlizixun/detail/${this.$route.query.id}`, {emulateJSON: true}).then(res => {
					if (res.data.code == 0) {
						this.ruleForm = res.data.data;
					}
				});
			},
			// 提交
			async onSubmit() {
				await this.$refs["ruleForm"].validate(async valid => {
					if(valid) {
						if(this.type=='cross'){
							var statusColumnName = localStorage.getItem('statusColumnName');
							var statusColumnValue = localStorage.getItem('statusColumnValue');
							if(statusColumnName && statusColumnName!='') {
								var obj = JSON.parse(localStorage.getItem('crossObj'));
								if(!statusColumnName.startsWith("[")) {
									for (var o in obj){
										if(o==statusColumnName){
											obj[o] = statusColumnValue;
										}
									}
									var table = localStorage.getItem('crossTable');
									await this.$http.post(table+'/update', obj).then(res => {});
								}
							}
						}


						await this.$http.post(`xinlizixun/${this.ruleForm.id?'update':this.centerType?'save':'add'}`, this.ruleForm).then(async res => {
							if (res.data.code == 0) {
								this.$message({
									message: '操作成功',
									type: 'success',
									duration: 1500,
									onClose: () => {
										this.$router.go(-1);
										
									}
								});
							} else {
								this.$message({
									message: res.data.msg,
									type: 'error',
									duration: 1500
								});
							}
						});
					}
				});
			},
			// 获取uuid
			getUUID () {
				return new Date().getTime();
			},
			// 返回
			back() {
				this.$router.go(-1);
			},
			fengmiantupianUploadChange(fileUrls) {
				this.ruleForm.fengmiantupian = fileUrls.replace(new RegExp(this.$config.baseUrl,"g"),"");
			},
		}
	};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
	.add-update-preview {
		padding: 0 0 20px;
		margin: 0px auto;
		color: #666;
		background: #fff;
		max-width: var(--tea-content);
		font-size: 16px;
		position: relative;
		.add-update-form {
			margin: 20px 0 0;
			width: 100%;
			position: relative;
			.add-item.el-form-item {
				border: 2px inset #f7db6150;
				padding: 10px;
				margin: 0 0 10px;
				background: #f7db6110;
				::v-deep .el-form-item__label {
					padding: 0 10px 0 0;
					color: #666;
					font-weight: 500;
					width: 180px;
					font-size: inherit;
					line-height: 40px;
					text-align: right;
				}
				::v-deep .el-form-item__content {
					margin-left: 180px;
				}
				.el-input {
					width: auto;
				}
				.el-input ::v-deep .el-input__inner {
					border: 1px solid #ddd;
					border-radius: 0px;
					padding: 0 12px;
					box-shadow: none;
					color: inherit;
					width: auto;
					font-size: 16px;
					height: 40px;
				}
				.el-input ::v-deep .el-input__inner[readonly="readonly"] {
					border: 0;
					cursor: not-allowed;
					border-radius: 0px;
					padding: 0 12px;
					box-shadow: none;
					color: rgba(85, 85, 127, 1.0);
					background: none;
					width: auto;
					font-size: 16px;
					height: 40px;
				}
				.el-input-number ::v-deep .el-input__inner {
					text-align: left;
					border: 1px solid #ddd;
					border-radius: 0px;
					padding: 0 12px;
					box-shadow: none;
					color: inherit;
					width: auto;
					font-size: 16px;
					height: 40px;
				}
				.el-input-number ::v-deep .is-disabled .el-input__inner {
					text-align: left;
					border: 0;
					cursor: not-allowed;
					border-radius: 0px;
					padding: 0 12px;
					box-shadow: none;
					color: rgba(85, 85, 127, 1.0);
					background: none;
					width: auto;
					font-size: 16px;
					height: 40px;
				}
				.el-input-number ::v-deep .el-input-number__decrease {
					display: none;
				}
				.el-input-number ::v-deep .el-input-number__increase {
					display: none;
				}
				.el-select {
					width: auto;
				}
				.el-select ::v-deep .el-input__inner {
					border: 1px solid #ddd;
					border-radius: 0px;
					padding: 0 10px;
					color: inherit;
					width: 100%;
					font-size: 16px;
					min-width: inherit !important;
					height: 40px;
				}
				.el-select ::v-deep .is-disabled .el-input__inner {
					border: 0;
					cursor: not-allowed;
					border-radius: 0px;
					padding: 0 10px;
					box-shadow: none;
					color: inherit;
					background: none;
					width: 100%;
					font-size: 16px;
					height: 40px;
				}
				.el-date-editor {
					width: auto;
				}
				.el-date-editor ::v-deep .el-input__inner {
					border: 1px solid #ddd;
					border-radius: 0px;
					padding: 0 10px 0 30px;
					box-shadow: none;
					color: inherit;
					width: auto;
					font-size: 16px;
					height: 40px;
				}
				.el-date-editor ::v-deep .el-input__inner[readonly="readonly"] {
					border: 0;
					cursor: not-allowed;
					border-radius: 0px;
					padding: 0 10px 0 30px;
					box-shadow: none;
					color: inherit;
					background: none;
					width: auto;
					font-size: 16px;
					height: 40px;
				}
				::v-deep .el-upload--picture-card {
					background: transparent;
					border: 0;
					border-radius: 0;
					width: auto;
					height: auto;
					line-height: initial;
					vertical-align: middle;
				}
				::v-deep .upload .upload-img {
					border: 1px solid #ddd;
					cursor: pointer;
					border-radius: 0px;
					color: #999;
					background: #fff;
					width: 80px;
					font-size: 26px;
					line-height: 60px;
					text-align: center;
					height: 60px;
				}
				::v-deep .el-upload-list .el-upload-list__item {
					border: 1px solid #ddd;
					cursor: pointer;
					border-radius: 0px;
					color: #999;
					background: #fff;
					width: 80px;
					font-size: 26px;
					line-height: 60px;
					text-align: center;
					height: 60px;
					font-size: 14px;
					line-height: 1.8;
				}
				::v-deep .el-upload .el-icon-plus {
					border: 1px solid #ddd;
					cursor: pointer;
					border-radius: 0px;
					color: #999;
					background: #fff;
					width: 80px;
					font-size: 26px;
					line-height: 60px;
					text-align: center;
					height: 60px;
				}
				::v-deep .el-upload__tip {
					color: #888;
					font-size: 16px;
				}
				.el-textarea ::v-deep .el-textarea__inner {
					border: 1px solid #ddd;
					border-radius: 0px;
					padding: 12px;
					box-shadow: none;
					color: inherit;
					width: auto;
					font-size: 16px;
					min-height: 150px;
					min-width: 48%;
					height: auto;
				}
				.el-textarea ::v-deep .el-textarea__inner[readonly="readonly"] {
					border: 0px solid #ddd;
					cursor: not-allowed;
					border-radius: 0px;
					padding: 12px;
					box-shadow: none;
					color: inherit;
					background: none;
					width: auto;
					font-size: 16px;
					min-height: 150px;
					min-width: 50%;
					height: auto;
				}
				::v-deep .el-input__inner::placeholder {
					color: inherit;
					font-size: inherit;
				}
				::v-deep textarea::placeholder {
					color: inherit;
					font-size: inherit;
				}
				.editor {
					background-color: #fff;
					border-radius: 0;
					padding: 0;
					box-shadow: none;
					margin: 0;
					width: 100%;
					min-height: 350px;
					border-color: #ccc;
					border-width: 1px;
					border-style: solid;
					height: auto;
				}
				.upload-img {
					object-fit: cover;
					width: 100px;
					height: 100px;
				}
				.viewBtn {
					border: 0;
					cursor: pointer;
					border-radius: 0px;
					padding: 0 20px;
					margin: 0;
					color: #333;
					background: #f7db61;
					display: inline-block;
					width: auto;
					font-size: 14px;
					line-height: 34px;
					height: 34px;
				}
				.viewBtn:hover {
					background: #f7db6199;
				}
				.unviewBtn {
					border: 0;
					cursor: pointer;
					padding: 0 20px;
					margin: 0;
					color: #333;
					display: inline-block;
					font-size: 14px;
					line-height: 34px;
					border-radius: 0px;
					outline: none;
					background: #ddd;
					width: auto;
					height: 34px;
				}
				.unviewBtn:hover {
					background: #eee;
				}
			}
			.add-btn-item {
				padding: 0;
				margin: 20px 0;
				.submitBtn {
					border: 0;
					cursor: pointer;
					padding: 0 15px;
					margin: 0 20px 0 0;
					display: inline-block;
					font-size: 16px;
					line-height: 40px;
					border-radius: 2px;
					background: #f7db61;
					width: auto;
					text-align: center;
					min-width: 110px;
					height: 40px;
					.icon {
						color: #333;
					}
					.text {
						color: #333;
					}
				}
				.submitBtn:hover {
					opacity: 0.8;
					.icon {
						color: #000;
					}
					.text {
						color: #000;
					}
				}
				.closeBtn {
					border: 1px solid #ddd;
					cursor: pointer;
					padding: 0 15px;
					margin: 0 20px 0 0;
					display: inline-block;
					font-size: 16px;
					line-height: 40px;
					border-radius: 2px;
					background: #fff;
					width: auto;
					text-align: center;
					min-width: 110px;
					height: 40px;
					.icon {
						color: #666;
					}
					.text {
						color: #666;
					}
				}
				.closeBtn:hover {
					background: #f7db61;
					border-color: #f7db61;
					.icon {
						color: #fff;
					}
					.text {
						color: #fff;
					}
				}
			}
		}
	}
	.el-date-editor.el-input {
		width: auto;
	}
</style>
