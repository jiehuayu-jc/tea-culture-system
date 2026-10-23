<template>
	<div class="reg-page">
		<div class="reg-wrap">
			<div class="reg-brand">
				<div class="reg-seal">茶</div>
				<h1 class="reg-name">茶文化管理系统</h1>
				<p class="reg-slogan">一盏清茗酬知音</p>
				<div class="reg-roles">
					<div class="role-card" :class="{ on: tableName=='yonghu' }">
						<div class="rc-t">茶友注册</div>
						<div class="rc-d">填写账号密码与基本资料，即刻加入茶友圈</div>
					</div>
					<div class="role-card" :class="{ on: tableName=='shangjia' }">
						<div class="rc-t">茶商入驻</div>
						<div class="rc-d">完善商家信息并上传营业执照，审核通过后可上架商品</div>
					</div>
				</div>
			</div>
			<el-form class="reg-card" v-if="pageFlag=='register'" ref="registerForm" :model="registerForm" :rules="rules">
				<div class="card-head">
					<div class="card-title" v-if="tableName=='yonghu'">注册茶友账号</div>
					<div class="card-title" v-else>茶商入驻申请</div>
					<div class="card-sub" v-if="tableName=='yonghu'">注册通过后即可选购好茶、参与茶友圈互动</div>
					<div class="card-sub" v-else>提交后需管理员审核，审核通过即可上架商品</div>
				</div>
				<el-form-item class="list-item" v-if="tableName=='yonghu'" prop="yonghuzhanghao">
					<div class="label" :class="changeRules('yonghuzhanghao')?'required':''">用户账号</div>
					<el-input v-model="registerForm.yonghuzhanghao" placeholder="请输入用户账号" />
				</el-form-item>
				<el-form-item class="list-item" v-if="tableName=='yonghu'" prop="mima">
					<div class="label" :class="changeRules('mima')?'required':''">密码</div>
					<el-input v-model="registerForm.mima" type="password" placeholder="请输入密码" />
				</el-form-item>
				<el-form-item class="list-item" v-if="tableName=='yonghu'" prop="mima2">
					<div class="label" :class="changeRules('mima')?'required':''">确认密码</div>
					<el-input v-model="registerForm.mima2" type="password" placeholder="请再次输入密码" />
				</el-form-item>
				<el-form-item class="list-item" v-if="tableName=='yonghu'" prop="yonghuxingming">
					<div class="label" :class="changeRules('yonghuxingming')?'required':''">用户姓名</div>
					<el-input v-model="registerForm.yonghuxingming" placeholder="请输入用户姓名" />
				</el-form-item>
				<el-form-item class="list-item half" v-if="tableName=='yonghu'" prop="xingbie">
					<div class="label" :class="changeRules('xingbie')?'required':''">性别</div>
					<el-select v-model="registerForm.xingbie" placeholder="请选择性别">
						<el-option v-for="(item,index) in yonghuxingbieOptions" :key="index" :label="item" :value="item"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item class="list-item half" v-if="tableName=='yonghu'" prop="nianling">
					<div class="label" :class="changeRules('nianling')?'required':''">年龄</div>
					<el-input v-model.number="registerForm.nianling" placeholder="请输入年龄" />
				</el-form-item>
				<el-form-item class="list-item" v-if="tableName=='yonghu'" prop="lianxifangshi">
					<div class="label" :class="changeRules('lianxifangshi')?'required':''">联系方式</div>
					<el-input v-model="registerForm.lianxifangshi" placeholder="请输入联系方式" />
				</el-form-item>
				<el-form-item class="list-item" v-if="tableName=='yonghu'" prop="touxiang">
					<div class="label" :class="changeRules('touxiang')?'required':''">头像</div>
					<file-upload
						tip="点击上传头像"
						action="file/upload"
						:limit="1"
						:multiple="true"
						:fileUrls="registerForm.touxiang?registerForm.touxiang:''"
						@change="yonghutouxiangUploadChange"
					></file-upload>
				</el-form-item>
				<el-form-item class="list-item" v-if="tableName=='shangjia'" prop="shangjiazhanghao">
					<div class="label" :class="changeRules('shangjiazhanghao')?'required':''">茶商账号</div>
					<el-input v-model="registerForm.shangjiazhanghao" placeholder="请输入茶商账号" />
				</el-form-item>
				<el-form-item class="list-item" v-if="tableName=='shangjia'" prop="mima">
					<div class="label" :class="changeRules('mima')?'required':''">密码</div>
					<el-input v-model="registerForm.mima" type="password" placeholder="请输入密码" />
				</el-form-item>
				<el-form-item class="list-item" v-if="tableName=='shangjia'" prop="mima2">
					<div class="label" :class="changeRules('mima')?'required':''">确认密码</div>
					<el-input v-model="registerForm.mima2" type="password" placeholder="请再次输入密码" />
				</el-form-item>
				<el-form-item class="list-item" v-if="tableName=='shangjia'" prop="shangjiamingcheng">
					<div class="label" :class="changeRules('shangjiamingcheng')?'required':''">茶商名称</div>
					<el-input v-model="registerForm.shangjiamingcheng" placeholder="请输入茶商名称" />
				</el-form-item>
				<el-form-item class="list-item" v-if="tableName=='shangjia'" prop="fuzeren">
					<div class="label" :class="changeRules('fuzeren')?'required':''">负责人</div>
					<el-input v-model="registerForm.fuzeren" placeholder="请输入负责人" />
				</el-form-item>
				<el-form-item class="list-item" v-if="tableName=='shangjia'" prop="lianxidianhua">
					<div class="label" :class="changeRules('lianxidianhua')?'required':''">联系电话</div>
					<el-input v-model="registerForm.lianxidianhua" placeholder="请输入联系电话" />
				</el-form-item>
				<el-form-item class="list-item" v-if="tableName=='shangjia'" prop="touxiang">
					<div class="label" :class="changeRules('touxiang')?'required':''">头像</div>
					<file-upload
						tip="点击上传头像"
						action="file/upload"
						:limit="1"
						:multiple="true"
						:fileUrls="registerForm.touxiang?registerForm.touxiang:''"
						@change="shangjiatouxiangUploadChange"
					></file-upload>
				</el-form-item>
				<el-form-item class="list-item" v-if="tableName=='shangjia'" prop="yingyezhizhao">
					<div class="label" :class="changeRules('yingyezhizhao')?'required':''">营业执照</div>
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
					<el-button class="register_btn" type="primary" @click="submitForm('registerForm')">注 册</el-button>
					<router-link class="has_btn" to="/login">已有账号，直接登录 →</router-link>
				</div>
			</el-form>
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
	/* 注册页 · 夜茶主题重设计：左品牌区 + 右毛玻璃表单卡 */
	.reg-page {
		min-height: 100vh;
		display: flex;
		align-items: center;
		justify-content: center;
		padding: 40px 24px;
		background: url(https://pic1.imgdb.cn/item/6787458cd0e0a243d4f46756.png) no-repeat center center / cover;
		position: relative;
		/* 深色遮罩让表单更聚焦 */
		&::before {
			content: '';
			position: absolute;
			inset: 0;
			background: linear-gradient(105deg, rgba(8, 20, 14, .82) 0%, rgba(8, 20, 14, .55) 45%, rgba(8, 20, 14, .35) 100%);
		}
	}
	.reg-wrap {
		position: relative;
		z-index: 1;
		display: flex;
		align-items: center;
		gap: 72px;
		width: 100%;
		max-width: 1060px;
	}
	/* 左：品牌区 */
	.reg-brand {
		flex: 0 0 330px;
		color: #EDE6D6;
		.reg-seal {
			width: 64px;
			height: 64px;
			border-radius: 50%;
			display: flex;
			align-items: center;
			justify-content: center;
			font-size: 34px;
			font-weight: 700;
			font-family: 'TeaSerif', 'STSong', serif;
			color: #14251a;
			background: linear-gradient(160deg, #e6ce9a, #d4af37);
			box-shadow: 0 8px 24px rgba(212, 175, 55, .4);
			margin-bottom: 22px;
		}
		.reg-name {
			margin: 0 0 10px;
			font-size: 34px;
			letter-spacing: 6px;
			font-weight: 700;
			font-family: 'TeaSerif', 'STSong', serif;
			color: #fff;
			text-shadow: 0 2px 12px rgba(0, 0, 0, .45);
		}
		.reg-slogan {
			margin: 0 0 30px;
			font-size: 15px;
			letter-spacing: 4px;
			color: rgba(237, 230, 214, .75);
		}
		.reg-roles {
			display: flex;
			flex-direction: column;
			gap: 12px;
		}
		.role-card {
			border: 1px solid rgba(237, 230, 214, .22);
			border-radius: 12px;
			padding: 14px 16px;
			background: rgba(16, 34, 25, .45);
			transition: all .2s ease;
			.rc-t {
				font-size: 15px;
				font-weight: 600;
				letter-spacing: 2px;
				color: #EDE6D6;
				margin-bottom: 4px;
			}
			.rc-d {
				font-size: 12px;
				line-height: 18px;
				color: rgba(237, 230, 214, .6);
			}
			&.on {
				border-color: rgba(212, 175, 55, .65);
				background: rgba(212, 175, 55, .12);
				.rc-t { color: #e6ce9a; }
				.rc-d { color: rgba(230, 206, 154, .75); }
			}
		}
	}
	/* 右：毛玻璃表单卡 */
	.reg-card {
		flex: 1;
		min-width: 0;
		background: rgba(14, 30, 22, .82);
		backdrop-filter: blur(14px);
		-webkit-backdrop-filter: blur(14px);
		border: 1px solid rgba(212, 175, 55, .28);
		border-radius: 18px;
		padding: 34px 36px 28px;
		box-shadow: 0 24px 60px rgba(0, 0, 0, .45);
		max-height: 82vh;
		overflow-y: auto;
		scrollbar-width: thin;
		scrollbar-color: rgba(212, 175, 55, .5) transparent;
		&::-webkit-scrollbar { width: 6px; }
		&::-webkit-scrollbar-thumb { background: rgba(212, 175, 55, .5); border-radius: 3px; }
		&::-webkit-scrollbar-track { background: transparent; }
		.card-head {
			margin-bottom: 22px;
			.card-title {
				font-size: 22px;
				font-weight: 700;
				letter-spacing: 3px;
				color: #fff;
				font-family: 'TeaSerif', 'STSong', serif;
			}
			.card-sub {
				margin-top: 6px;
				font-size: 13px;
				color: rgba(237, 230, 214, .6);
			}
		}
		.list-item {
			margin-bottom: 16px;
			display: block;
			.label {
				display: block;
				margin-bottom: 6px;
				font-size: 13px;
				letter-spacing: 1px;
				color: rgba(237, 230, 214, .85);
				&.required::after {
					content: '*';
					color: #d4af37;
					margin-left: 4px;
				}
			}
			&.half {
				display: inline-block;
				width: 48%;
				margin-right: 4%;
				vertical-align: top;
				&:nth-of-type(odd) { margin-right: 0; }
			}
		}
		::v-deep .el-input__inner {
			background: rgba(237, 230, 214, .06);
			border: 1px solid rgba(237, 230, 214, .2);
			border-radius: 8px;
			color: #EDE6D6;
			height: 44px;
			line-height: 44px;
			width: 100%;
			&:focus {
				border-color: rgba(212, 175, 55, .7);
				background: rgba(237, 230, 214, .09);
			}
		}
		::v-deep .el-select { width: 100%; }
		::v-deep .el-input__icon { color: rgba(237, 230, 214, .6); }
		/* 上传组件融入暗色 */
		::v-deep .upload-demo, ::v-deep .el-upload {
			background: rgba(237, 230, 214, .06);
			border: 1px dashed rgba(237, 230, 214, .3);
			border-radius: 8px;
			color: rgba(237, 230, 214, .7);
		}
		.register-btn {
			margin-top: 24px;
			.register_btn {
				width: 100%;
				height: 46px;
				border: 0;
				border-radius: 10px;
				font-size: 16px;
				letter-spacing: 6px;
				font-weight: 600;
				color: #14251a;
				background: linear-gradient(135deg, #e6ce9a, #d4af37);
				box-shadow: 0 8px 20px rgba(212, 175, 55, .35);
				&:hover { opacity: .9; }
			}
			.has_btn {
				display: block;
				margin-top: 16px;
				text-align: center;
				font-size: 13px;
				color: rgba(230, 206, 154, .8);
				&:hover { color: #d4af37; }
			}
		}
	}
	/* 窄屏：单栏堆叠 */
	@media (max-width: 900px) {
		.reg-wrap { flex-direction: column; gap: 28px; }
		.reg-brand { flex: none; text-align: center; .reg-seal { margin: 0 auto 16px; } .reg-roles { display: none; } }
		.reg-card { max-height: none; padding: 26px 22px; }
		.list-item.half { width: 100% !important; margin-right: 0 !important; }
	}
</style>
