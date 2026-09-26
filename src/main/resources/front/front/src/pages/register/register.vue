<template>
	<div>

		<div class="container">
			<el-form class='rgs-form animate__animated animate__' v-if="pageFlag=='register'" ref="registerForm" :model="registerForm" :rules="rules">
				<div class="rgs-form2">
					<div class="title">茶文化管理系统</div>
					<p v-if="pageFlag=='register'" class="rgs-tip">请填写真实格式的手机号、身份证号和邮箱，否则无法通过校验。若注册失败，请查看页面提示或确认后端与数据库已就绪。</p>
					<el-form-item class="list-item" v-if="tableName=='yonghu'" prop="yonghuzhanghao">
						<div class="label" :class="changeRules('yonghuzhanghao')?'required':''">用户账号：</div>
						<el-input v-model="registerForm.yonghuzhanghao"  placeholder="请输入用户账号" />
					</el-form-item>
					<el-form-item class="list-item" v-if="tableName=='yonghu'" prop="mima">
						<div class="label" :class="changeRules('mima')?'required':''">密码：</div>
						<el-input v-model="registerForm.mima" type="password" placeholder="请输入密码" />
					</el-form-item>
					<el-form-item class="list-item" v-if="tableName=='yonghu'" prop="mima2">
						<div class="label" :class="changeRules('mima')?'required':''">确认密码：</div>
						<el-input v-model="registerForm.mima2" type="password" placeholder="请再次输入密码" />
					</el-form-item>
					<el-form-item class="list-item" v-if="tableName=='yonghu'" prop="yonghuxingming">
						<div class="label" :class="changeRules('yonghuxingming')?'required':''">用户姓名：</div>
						<el-input v-model="registerForm.yonghuxingming"  placeholder="请输入用户姓名" />
					</el-form-item>
					<el-form-item class="list-item" v-if="tableName=='yonghu'" prop="xingbie">
						<div class="label" :class="changeRules('xingbie')?'required':''">性别：</div>
						<el-select v-model="registerForm.xingbie" placeholder="请选择性别" >
							<el-option
								v-for="(item,index) in yonghuxingbieOptions"
								:key="index"
								:label="item"
								:value="item">
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item class="list-item" v-if="tableName=='yonghu'" prop="nianling">
						<div class="label" :class="changeRules('nianling')?'required':''">年龄：</div>
						<el-input v-model.number="registerForm.nianling"  placeholder="请输入年龄" />
					</el-form-item>
					<el-form-item class="list-item" v-if="tableName=='yonghu'" prop="lianxifangshi">
						<div class="label" :class="changeRules('lianxifangshi')?'required':''">联系方式：</div>
						<el-input v-model="registerForm.lianxifangshi"  placeholder="请输入联系方式" />
					</el-form-item>
					<el-form-item class="list-item" v-if="tableName=='yonghu'" prop="touxiang">
						<div class="label" :class="changeRules('touxiang')?'required':''">头像：</div>
						<file-upload
							tip="点击上传头像"
							action="file/upload"
							:limit="1"
							:multiple="true"
							:fileUrls="registerForm.touxiang?registerForm.touxiang:''"
							@change="yonghutouxiangUploadChange"
						></file-upload>
					</el-form-item>
					<el-form-item class="list-item" v-if="tableName=='yonghu'" prop="shenfenzhenghao">
						<div class="label" :class="changeRules('shenfenzhenghao')?'required':''">身份证号：</div>
						<el-input v-model="registerForm.shenfenzhenghao"  placeholder="请输入身份证号" />
					</el-form-item>
					<el-form-item class="list-item" v-if="tableName=='yonghu'" prop="youxiang">
						<div class="label" :class="changeRules('youxiang')?'required':''">邮箱：</div>
						<el-input v-model="registerForm.youxiang"  placeholder="请输入邮箱" />
					</el-form-item>
					<el-form-item class="list-item" v-if="tableName=='shangjia'" prop="shangjiazhanghao">
						<div class="label" :class="changeRules('shangjiazhanghao')?'required':''">茶商账号：</div>
						<el-input v-model="registerForm.shangjiazhanghao"  placeholder="请输入茶商账号" />
					</el-form-item>
					<el-form-item class="list-item" v-if="tableName=='shangjia'" prop="mima">
						<div class="label" :class="changeRules('mima')?'required':''">密码：</div>
						<el-input v-model="registerForm.mima" type="password" placeholder="请输入密码" />
					</el-form-item>
					<el-form-item class="list-item" v-if="tableName=='shangjia'" prop="mima2">
						<div class="label" :class="changeRules('mima')?'required':''">确认密码：</div>
						<el-input v-model="registerForm.mima2" type="password" placeholder="请再次输入密码" />
					</el-form-item>
					<el-form-item class="list-item" v-if="tableName=='shangjia'" prop="shangjiamingcheng">
						<div class="label" :class="changeRules('shangjiamingcheng')?'required':''">茶商名称：</div>
						<el-input v-model="registerForm.shangjiamingcheng"  placeholder="请输入茶商名称" />
					</el-form-item>
					<el-form-item class="list-item" v-if="tableName=='shangjia'" prop="touxiang">
						<div class="label" :class="changeRules('touxiang')?'required':''">头像：</div>
						<file-upload
							tip="点击上传头像"
							action="file/upload"
							:limit="1"
							:multiple="true"
							:fileUrls="registerForm.touxiang?registerForm.touxiang:''"
							@change="shangjiatouxiangUploadChange"
						></file-upload>
					</el-form-item>
					<el-form-item class="list-item" v-if="tableName=='shangjia'" prop="fuzeren">
						<div class="label" :class="changeRules('fuzeren')?'required':''">负责人：</div>
						<el-input v-model="registerForm.fuzeren"  placeholder="请输入负责人" />
					</el-form-item>
					<el-form-item class="list-item" v-if="tableName=='shangjia'" prop="lianxidianhua">
						<div class="label" :class="changeRules('lianxidianhua')?'required':''">联系电话：</div>
						<el-input v-model="registerForm.lianxidianhua"  placeholder="请输入联系电话" />
					</el-form-item>
					<el-form-item class="list-item" v-if="tableName=='shangjia'" prop="yingyezhizhao">
						<div class="label" :class="changeRules('yingyezhizhao')?'required':''">营业执照：</div>
						<file-upload
							tip="点击上传营业执照"
							action="file/upload"
							:limit="1"
							:type="3"
							:multiple="true"
							:fileUrls="registerForm.yingyezhizhao?registerForm.yingyezhizhao:''"
							@change="shangjiayingyezhizhaoUploadChange"
						></file-upload>
					</el-form-item>
					<div class="register-btn">
						<div class="register-btn1">
							<el-button class="register_btn" type="primary" @click="submitForm('registerForm')">注册</el-button>
						</div>
						<div class="register-btn2">
							<router-link class="has_btn" to="/login">已有账号，直接登录</router-link>
						</div>
					</div>
				</div>
				<div class="idea1"></div>
				<div class="idea2"></div>
			</el-form>
		</div>
	</div>
</div>
</template>

<script>
	import 'animate.css';

export default {
    //数据集合
    data() {
		return {
            pageFlag : '',
			tableName: '',
			registerForm: {},
			forgetForm: {},
			rules: {},
			requiredRules: {},
            yonghuxingbieOptions: [],
		}
    },
	mounted() {
		if (this.$route.path === '/register' && (this.$route.query.pageFlag !== 'register' || !this.$route.query.role)) {
			this.$router.replace({
				path: '/register',
				query: { role: this.$route.query.role || 'yonghu', pageFlag: 'register' }
			});
			return;
		}
		if(this.$route.query.pageFlag=='register'){
			this.tableName = this.$route.query.role;
			if(this.tableName=='yonghu'){
				this.registerForm = {
					yonghuzhanghao: '',
					mima: '',
					mima2: '',
					yonghuxingming: '',
					xingbie: '',
					nianling: '',
					lianxifangshi: '',
					touxiang: '',
					shenfenzhenghao: '',
					youxiang: '',
					money: '',
					status: '',
				}
			}
			if(this.tableName=='shangjia'){
				this.registerForm = {
					shangjiazhanghao: '',
					mima: '',
					mima2: '',
					shangjiamingcheng: '',
					touxiang: '',
					fuzeren: '',
					lianxidianhua: '',
					yingyezhizhao: '',
					sfsh: '',
					shhf: '',
					money: '',
				}
			}
			if ('yonghu' == this.tableName) {
				this.rules.yonghuzhanghao = [{ required: true, message: '请输入用户账号', trigger: 'blur' }];
				this.requiredRules.yonghuzhanghao = [{ required: true, message: '请输入用户账号', trigger: 'blur' }]
			}
			if ('yonghu' == this.tableName) {
				this.rules.mima = [{ required: true, message: '请输入密码', trigger: 'blur' }];
				this.requiredRules.mima = [{ required: true, message: '请输入密码', trigger: 'blur' }]
			}
			if ('yonghu' == this.tableName) {
				this.rules.yonghuxingming = [{ required: true, message: '请输入用户姓名', trigger: 'blur' }];
				this.requiredRules.yonghuxingming = [{ required: true, message: '请输入用户姓名', trigger: 'blur' }]
			}
			this.yonghuxingbieOptions = "男,女".split(',');
			if ('yonghu' == this.tableName) {
				this.rules.xingbie = [{ required: true, message: '请输入性别', trigger: 'blur' }];
				this.requiredRules.xingbie = [{ required: true, message: '请输入性别', trigger: 'blur' }]
			}
			if ('yonghu' == this.tableName) {
				this.rules.nianling = [{ required: true, validator: this.$validate.isIntNumerNotNull, trigger: 'blur' }];
				this.requiredRules.nianling = [{ required: true, message: '请输入年龄', trigger: 'blur' }]
			}
			if ('yonghu' == this.tableName) {
				this.rules.lianxifangshi = [{ required: true, validator: this.$validate.isMobile, trigger: 'blur' }];
			}
			if ('yonghu' == this.tableName) {
				this.rules.touxiang = [{ required: false }];
			}
			if ('yonghu' == this.tableName) {
				this.rules.mima2 = [{ required: true, message: '请再次输入密码', trigger: 'blur' }];
			}
			if ('yonghu' == this.tableName) {
				this.rules.shenfenzhenghao = [{ required: true, validator: this.$validate.isIdCard, trigger: 'blur' }];
			}
			if ('yonghu' == this.tableName) {
				this.rules.youxiang = [{ required: true, validator: this.$validate.isEmail, trigger: 'blur' }];
			}
			if ('shangjia' == this.tableName) {
				this.rules.shangjiazhanghao = [{ required: true, message: '请输入茶商账号', trigger: 'blur' }];
				this.requiredRules.shangjiazhanghao = [{ required: true, message: '请输入茶商账号', trigger: 'blur' }]
			}
			if ('shangjia' == this.tableName) {
				this.rules.mima = [{ required: true, message: '请输入密码', trigger: 'blur' }];
				this.requiredRules.mima = [{ required: true, message: '请输入密码', trigger: 'blur' }]
			}
			if ('shangjia' == this.tableName) {
				this.rules.shangjiamingcheng = [{ required: true, message: '请输入茶商名称', trigger: 'blur' }];
				this.requiredRules.shangjiamingcheng = [{ required: true, message: '请输入茶商名称', trigger: 'blur' }]
			}
			if ('shangjia' == this.tableName) {
				this.rules.lianxidianhua = [{ required: true, validator: this.$validate.isMobile, trigger: 'blur' }];
			}
			if ('shangjia' == this.tableName) {
				this.rules.mima2 = [{ required: true, message: '请再次输入密码', trigger: 'blur' }];
			}
			if ('shangjia' == this.tableName) {
				this.rules.touxiang = [{ required: false }];
				this.rules.fuzeren = [{ required: false }];
				this.rules.yingyezhizhao = [{ required: false }];
			}
		}
	},
    created() {
		this.pageFlag = this.$route.query.pageFlag;
    },
    //方法集合
    methods: {
		changeRules(name){
			if(this.requiredRules[name]){
				return true
			}
			return false
		},
		// 获取uuid
		getUUID () {
			return new Date().getTime();
		},
        // 下二随
		yonghutouxiangUploadChange(fileUrls) {
			this.registerForm.touxiang = fileUrls.replace(new RegExp(this.$config.baseUrl,"g"),"");
		},
		shangjiatouxiangUploadChange(fileUrls) {
			this.registerForm.touxiang = fileUrls.replace(new RegExp(this.$config.baseUrl,"g"),"");
		},
		shangjiayingyezhizhaoUploadChange(fileUrls) {
			this.registerForm.yingyezhizhao = fileUrls.replace(new RegExp(this.$config.baseUrl,"g"),"");
		},

		// 多级联动参数


		submitForm(formName) {
			this.$refs[formName].validate((valid) => {
				if (valid) {
					var url=this.tableName+"/register";
					if(`yonghu` == this.tableName && this.registerForm.mima!=this.registerForm.mima2) {
						this.$message.error(`两次密码输入不一致`);
						return
					}
					if(this.tableName=='shangjia'){
						this.registerForm.sfsh = '待审核'
					}
				if(`shangjia` == this.tableName && this.registerForm.mima!=this.registerForm.mima2) {
					this.$message.error(`两次密码输入不一致`);
					return
				}
					if (this.tableName === 'yonghu') {
						if (this.registerForm.money === '' || this.registerForm.money == null) {
							this.registerForm.money = 0;
						}
						if (this.registerForm.status === '' || this.registerForm.status == null) {
							this.registerForm.status = 0;
						}
					}
					if (this.tableName === 'shangjia') {
						if (this.registerForm.money === '' || this.registerForm.money == null) {
							this.registerForm.money = 0;
						}
					}
					if (!this.tableName) {
						this.$message.error('缺少注册类型，请从登录页「注册用户」链接进入');
						return;
					}
					var payload = Object.assign({}, this.registerForm);
					delete payload.mima2;
					this.$http.post(url, payload).then(res => {
						const data = res.body != null ? res.body : res.data;
						if (!data) {
							this.$message.error('服务器无有效返回，请确认后端已启动');
							return;
						}
						if (data.code === 0) {
							this.$message({
								message: data.msg || '注册成功',
								type: 'success',
								duration: 2600,
								onClose: () => {
									this.$router.push('/login');
								}
							});
						} else {
							let msg = data.msg || '注册失败';
							if (data.code === 401 || msg === '请先登录') {
								msg = '注册接口未放行或请求未到达后端，请确认 Spring Boot 已启动，且前台代理指向 http://127.0.0.1:8080';
							}
							this.$message.error(msg);
						}
					}).catch((err) => {
						let data = err && (err.body != null ? err.body : err.data);
						if (typeof data === 'string') {
							try {
								data = JSON.parse(data);
							} catch (e) {
								data = null;
							}
						}
						if (data && typeof data === 'object') {
							let msg = data.msg || '注册失败';
							if (data.code === 401 || msg === '请先登录') {
								msg = '注册接口未放行或请求未到达后端，请确认 Spring Boot 已启动，且前台代理指向 http://127.0.0.1:8080';
							}
							this.$message.error(msg);
							return;
						}
						const st = err && err.status;
						if (st === 502 || st === 503) {
							this.$message.error('连不上后端（HTTP ' + st + '）：请先在 IDEA 启动 Spring Boot（端口 8080），再在项目里执行 npm run serve 打开用户端。');
							return;
						}
						if (st === 404) {
							this.$message.error('接口 404：地址需带前缀 /springbootj8kskvkr，且不要用「直接打开 dist网页」的方式访问（请用 npm run serve）。');
							return;
						}
						if (st) {
							this.$message.error('请求失败 HTTP ' + st + '，请确认后端在 8080 运行、MySQL 已启动。');
							return;
						}
						this.$message.error('网络异常或浏览器拦截：请确认后端已启动（端口 8080）、用户端用 npm run serve 打开。');
					});
				} else {
					this.$message.warning('请检查标红项，按提示修改后再注册');
					return false;
				}
			});
		},
		resetForm(formName) {
			this.$refs[formName].resetFields();
		}
    }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
	.container {
		background-repeat: no-repeat;
		background-size: 100% 100% !important;
		background-position: center center;
		background: url(https://pic1.imgdb.cn/item/6787458cd0e0a243d4f46756.png);
		display: flex;
		width: 100%;
		min-height: 100vh;
		justify-content: center;
		align-items: center;
		position: relative;
		.rgs-form {
			border: 0px solid #b0b0b0;
			border-radius: 0px;
			padding: 20px 0 0 0;
			margin: 100px 38% 100px 0;
			background: none;
			width: 33vw;
			.rgs-form2 {
				width: 100%;
				.title {
					margin: 0 0 20px 0;
					color: #fff;
					font-weight: 600;
					width: 90%;
					font-size: 22px;
					line-height: 44px;
					text-align: center;
				}
				.rgs-tip {
					margin: -8px auto 16px;
					padding: 8px 12px;
					width: 86%;
					font-size: 12px;
					line-height: 1.5;
					color: rgba(255, 255, 255, 0.95);
					background: rgba(0, 0, 0, 0.35);
					border-radius: 8px;
				}
				.subtitle {
					margin: 0 0 10px 0;
					text-shadow: 4px 4px 2px rgba(64, 158, 255, .5);
					color: rgba(64, 158, 255, 1);
					width: 100%;
					font-size: 20px;
					line-height: 44px;
					text-align: center;
				}
				.list-item {
					border-radius: 8px;
					margin: 0 auto 20px;
					background: #fff;
					width: 80%;
					/deep/.el-form-item__content {
						padding: 0 0 0 120px;
						display: block;
						width: calc(100% - 0px);
						.label {
							padding: 0 5px 0 0;
							z-index: 9;
							color: #333;
							left: 0;
							width: 120px;
							font-size: 16px;
							line-height: 40px;
							position: absolute !important;
							text-align: right;
						}
						
						.required {
							position: relative;
						}
						.required::after{
							margin: 0 10px 0 0;
							color: red;
							left: 110px;
							position: inherit;
							content: "*";
						}
						.el-input {
							flex: 1;
							width: 100%;
						}
						.el-input .el-input__inner {
							border: 0px solid #b0b0b0;
							border-radius: 8px;
							padding: 0 10px;
							color: #666;
							flex: 1;
							width: calc(100% - 0px);
							font-size: 15px;
							height: 40px;
						}
						.el-input .el-input__inner:focus {
							border: 0px solid #b0b0b0;
							border-radius: 8px;
							padding: 0 10px;
							outline: none;
							color: #666;
							width: calc(100% - 0px);
							font-size: 15px;
							height: 40px;
						}
						.el-input-number {
							flex: 1;
							width: 100%;
						}
						.el-input-number /deep/ .el-input__inner {
							text-align: left;
							border: 0px solid #b0b0b0;
							border-radius: 8px;
							padding: 0 10px;
							color: #666;
							flex: 1;
							width: calc(100% - 0px);
							font-size: 15px;
							height: 40px;
						}
						.el-input-number /deep/ .el-input-number__decrease {
							display: none;
						}
						.el-input-number /deep/ .el-input-number__increase {
							display: none;
						}
						.el-select {
							flex: 1;
							width: calc(100% - 0px);
						}
						.el-select .el-input__inner {
							border: 0px solid #b0b0b0;
							border-radius: 8px;
							padding: 0 10px;
							color: #666;
							width: 100%;
							font-size: 15px;
							height: 40px;
						}
						.el-select .el-input__inner:focus {
							border: 0px solid #b0b0b0;
							border-radius: 8px;
							padding: 0 10px;
							outline: none;
							color: #666;
							width: 100%;
							font-size: 15px;
							height: 40px;
						}
						.el-date-editor {
							flex: 1;
							width: calc(100% - 0px);
						}
						.el-date-editor .el-input__inner {
							border: 0px solid #b0b0b0;
							border-radius: 8px;
							padding: 0 10px 0 30px;
							color: #666;
							width: 100%;
							font-size: 15px;
							height: 40px;
						}
						.el-date-editor .el-input__inner:focus {
							border: 0px solid #b0b0b0;
							border-radius: 8px;
							padding: 0 10px 0 30px;
							outline: none;
							color: #666;
							width: 100%;
							font-size: 15px;
							height: 40px;
						}
						.el-upload--picture-card {
							background: transparent;
							border: 0;
							border-radius: 0;
							width: auto;
							height: auto;
							line-height: initial;
							vertical-align: middle;
						}
						.upload .upload-img {
							border: 1px solid #eee;
							cursor: pointer;
							border-radius: 0px;
							margin: 5px 0 0;
							color: #b0b0b0;
							background: #fff;
							width: 80px;
							font-size: 24px;
							line-height: 50px;
							text-align: center;
							height: 50px;
						}
						.el-upload-list .el-upload-list__item {
							border: 1px solid #eee;
							cursor: pointer;
							border-radius: 0px;
							margin: 5px 0 0;
							color: #b0b0b0;
							background: #fff;
							width: 80px;
							font-size: 24px;
							line-height: 50px;
							text-align: center;
							height: 50px;
							font-size: 14px;
							line-height: 1.8;
						}
						.el-upload .el-icon-plus {
							border: 1px solid #eee;
							cursor: pointer;
							border-radius: 0px;
							margin: 5px 0 0;
							color: #b0b0b0;
							background: #fff;
							width: 80px;
							font-size: 24px;
							line-height: 50px;
							text-align: center;
							height: 50px;
						}
						.el-upload__tip {
							color: #fff;
							font-size: 15px;
						}
						.emailInput {
							border: 0px solid #b0b0b0;
							border-radius: 0px 0 0 0px;
							padding: 0 10px;
							margin: 0;
							color: #606266;
							background: #fff;
							flex: 1;
							width: calc(100% - 0px);
							font-size: 15px;
							height: 40px;
						}
						.emailInput:focus {
							border: 0px solid #b0b0b0;
							border-radius: 0px 0 0 0px;
							padding: 0 10px;
							outline: none;
							color: #606266;
							width: calc(100% - 0px);
							font-size: 15px;
							height: 40px;
						}
						.el-btn {
							border: 0px solid #b0b0b0;
							cursor: pointer;
							border-radius: 0 8px 8px 0;
							padding: 0 10px;
							margin: 0;
							color: #fff;
							background: #3E6B4F;
							width: 110px;
							font-size: 15px;
							border-width: 0px 0px 0px 0;
							float: right;
							height: 40px;
						}
						.el-btn:hover {
						}
						
						.el-input__inner::placeholder {
							color: #999;
							font-size: 15px;
						}
						input::placeholder {
							color: #999;
							font-size: 15px;
						}
						.editor {
							border-radius: 8px;
							margin: 0 0 0 0px;
							background: #fff;
							width: calc(100% - 0px);
							height: auto;
						}
					}
				}
				.register-btn {
					margin: 20px auto;
					display: flex;
					width: 80%;
					flex-wrap: wrap;
				}
				.register-btn1 {
					padding: 0 0 0 0px;
					width: 100%;
				}
				.register-btn2 {
					padding: 0 0 0 0px;
					margin: 0 auto 10px;
					width: 100%;
					text-align: center;
					order: -1;
				}
				.register_btn {
					border: 0;
					cursor: pointer;
					border-radius: 8px;
					padding: 0 30px;
					margin: 0 0 20px;
					color: #fff;
					background: #3E6B4F;
					letter-spacing: 4px;
					width: 100%;
					font-size: 20px;
					height: 48px;
				}
				.register_btn:hover {
				}
				.has_btn {
					cursor: pointer;
					padding: 0;
					color: #fff;
					display: inline-block;
					text-decoration: none;
					font-size: 15px;
					line-height: 40px;
				}
				.has_btn:hover {
					opacity: 0.8;
				}
			}
			.idea1 {
				background: red;
				display: none;
				width: 100%;
				height: 40px;
			}
			.idea2 {
				background: blue;
				display: none;
				width: 100%;
				height: 40px;
			}
		}
	}
	
	::-webkit-scrollbar {
		display: none;
	}
</style>
