<template>
	<div class="login-page">
		<div class="lp-overlay"></div>
		<div class="lp-left">
			<div class="lp-seal">陆羽茶经</div>
			<div class="lp-eyebrow">CHINESE TEA CULTURE</div>
			<div class="lp-vertical">茶之为饮&nbsp;发乎神农</div>
			<div class="lp-slogan">六大茶类 · 源头直采 · 以盏见心</div>
		</div>
		<div class="lp-form-zone">
			<el-form ref="loginForm" :model="loginForm" :rules="rules" class="login_form animate__animated animate__fadeInUp">
				<div class="lp-head">
					<div class="lp-title">茶文化管理系统</div>
					<div class="lp-sub">TEA CULTURE · 用户登录</div>
					<div class="lp-line"></div>
				</div>
				<div class="lp-field">
					<label>账&nbsp;号</label>
					<input v-model="loginForm.username" placeholder="请输入账号" name="username" type="text">
				</div>
				<div class="lp-field">
					<label>密&nbsp;码</label>
					<div class="lp-password">
						<input v-model="loginForm.password" placeholder="请输入密码" :type="showPassword?'text':'password'" name="password">
						<span class="icon iconfont" :class="showPassword?'icon-liulan13':'icon-liulan17'" @click="showPassword=!showPassword"></span>
					</div>
				</div>

				<div class="lp-field" v-if="roles.length>1">
					<label>角&nbsp;色</label>
					<div class="list-type" prop="role">
						<el-radio v-model="loginForm.tableName" :label="item.tableName" v-for="(item, index) in roles" :key="index" @change.native="getCurrentRow(item)">{{item.roleName}}</el-radio>
					</div>
				</div>

				<button class="lp-submit" v-if="loginType==1" @click.prevent="submitForm('loginForm')">登&nbsp;录</button>
				<div class="lp-links" v-if="loginType==1">
					<router-link class="lp-register" :to="{path: '/register', query: {role: item.tableName,pageFlag:'register'}}" v-if="item.hasFrontRegister=='是'" v-for="(item, index) in roles" :key="index">注册{{item.roleName.replace('注册','')}} →</router-link>
				</div>

				<div class="demo-fill" v-if="loginType==1">
					<span class="demo-fill-label">演示账号一键填充</span>
					<span class="demo-fill-btn" @click="fillDemo('用户账号1','123456')">用户</span>
					<span class="demo-fill-btn" @click="fillDemo('商家账号1','123456')">茶商</span>
					<span class="demo-fill-btn" @click="fillDemo('admin','admin')">管理员</span>
				</div>
			</el-form>
		</div>
	</div>
</template>

<script>
	import 'animate.css';
import menu from '@/config/menu'
export default {
	//数据集合
	data() {
		return {
            baseUrl: this.$config.baseUrl,
            loginType: 1,
			roleMenus: [],
			loginForm: {
				username: '',
				password: '',
				tableName: '',
				code: '',
			},
			role: '',
            roles: [],
			rules: {
				username: [
					{ required: true, message: '请输入账号', trigger: 'blur' }
				],
				password: [
					{ required: true, message: '请输入密码', trigger: 'blur' }
				]
			},
			codes: [{
				num: 1,
				color: '#000',
				rotate: '10deg',
				size: '16px'
			}, {
				num: 2,
				color: '#000',
				rotate: '10deg',
				size: '16px'
			}, {
				num: 3,
				color: '#000',
				rotate: '10deg',
				size: '16px'
			}, {
				num: 4,
				color: '#000',
				rotate: '10deg',
				size: '16px'
			}],
			flag: false,
			verifyCheck2: false,
			showPassword: false,
		}
	},
	components: {
	},
	created() {
		this.roleMenus = menu.list()
		for(let item in this.roleMenus) {
			if(this.roleMenus[item].hasFrontLogin=='是') {
				this.roles.push(this.roleMenus[item]);
			}
		}
		
	},
	mounted() {
		if (this.roles.length === 1) {
			this.loginForm.tableName = this.roles[0].tableName;
			this.role = this.roles[0].roleName;
		}
	},
	//方法集合
	methods: {
		/** 一键填充演示账号（仅填账号密码，角色选择不变） */
		fillDemo(username, password) {
			this.loginForm.username = username;
			this.loginForm.password = password;
		},
		randomString() {
			var len = 4;
			var chars = [
				'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
				'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
				'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G',
				'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
				'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2',
				'3', '4', '5', '6', '7', '8', '9'
			]
			var colors = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f']
			var sizes = ['14', '15', '16', '17', '18']
			
			var output = []
			for (var i = 0; i < len; i++) {
				// 随机验证码
				var key = Math.floor(Math.random() * chars.length)
				this.codes[i].num = chars[key]
				// 随机验证码颜色
				var code = '#'
				for (var j = 0; j < 6; j++) {
					var key = Math.floor(Math.random() * colors.length)
					code += colors[key]
				}
				this.codes[i].color = code
				// 随机验证码方向
				var rotate = Math.floor(Math.random() * 45)
				var plus = Math.floor(Math.random() * 2)
				if (plus == 1) rotate = '-' + rotate
				this.codes[i].rotate = 'rotate(' + rotate + 'deg)'
				// 随机验证码字体大小
				var size = Math.floor(Math.random() * sizes.length)
				this.codes[i].size = sizes[size] + 'px'
			}
		},
		getCurrentRow(row) {
			this.role = row.roleName;
			this.loginForm.tableName = row.tableName;
		},
		submitForm(formName) {
			if (this.roles.length!=1) {
				if (!this.role) {
					this.$message.error("请选择登录用户类型");
					return false;
				}
			} else {
				this.role = this.roles[0].roleName;
				this.loginForm.tableName = this.roles[0].tableName;
			}
			if (!this.loginForm.username) {
				this.$message.error("请输入用户名");
				return;
			}
			if (!this.loginForm.password) {
				this.$message.error("请输入密码");
				return;
			}

			this.loginPost(formName)
		},
			loginPost(formName) {
			this.$refs[formName].validate((valid) => {
				if (valid) {
					const username = (this.loginForm.username || '').trim()
					const password = (this.loginForm.password || '').trim()
					if (!this.loginForm.tableName) {
						this.$message.error('缺少登录类型，请刷新页面重试');
						return;
					}
					this.$http.post(
						`${this.loginForm.tableName}/login`,
						{ username, password, captcha: this.loginForm.code || '' },
						{ emulateJSON: true }
					).then(res => {
						const data = res.body != null ? res.body : res.data;
						if (!data) {
							this.$message.error('服务器无有效返回，请确认后端已在 IDEA 中启动（端口 8080）');
							return;
						}
						if (data.code === 0 || data.code === '0') {
							localStorage.setItem('frontToken', data.token);
							localStorage.setItem('UserTableName', this.loginForm.tableName);
							localStorage.setItem('username', username);
							localStorage.setItem('frontSessionTable', this.loginForm.tableName);
							localStorage.setItem('frontRole', this.role);
							localStorage.setItem('keyPath', 0);
							this.$router.push('/');
							this.$message({
								message: '登录成功',
								type: 'success',
								duration: 1500,
							});
						} else {
							let msg =
								data.msg ||
								data.message ||
								(typeof data.error === 'string' ? data.error : null) ||
								(data.status ? '服务异常 HTTP ' + data.status + '，请查看 IDEA 控制台与 MySQL 是否已导入 db脚本' : null) ||
								'登录失败';
							if (data.code === 401 || msg === '请先登录') {
								msg = '登录接口未放行或请求未到达后端，请确认 Spring Boot 已启动，且前台代理指向 http://127.0.0.1:8080';
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
							let msg =
								data.msg ||
								data.message ||
								(typeof data.error === 'string' ? data.error : null) ||
								(data.status ? '服务异常 HTTP ' + data.status : null) ||
								'登录失败';
							if (data.code === 401 || msg === '请先登录') {
								msg = '登录接口未放行或请求未到达后端，请确认 Spring Boot 已启动，且前台代理指向 http://127.0.0.1:8080';
							}
							this.$message.error(msg);
							return;
						}
						const st = err && err.status;
						if (st === 502 || st === 503) {
							this.$message.error('连不上后端（HTTP ' + st + '）：请先启动 Spring Boot（端口 8080），用户端请用 npm run serve。');
							return;
						}
						if (st === 404) {
							this.$message.error('接口 404：请确认前缀 /springbootj8kskvkr，并用 npm run serve 打开用户端。');
							return;
						}
						if (st) {
							this.$message.error('请求失败 HTTP ' + st + '，请确认后端在 8080 运行。');
							return;
						}
						this.$message.error('无法连接后端：请确认 Spring Boot 已运行（8080）且用户端为 npm run serve。');
					});
				} else {
					return false;
				}
			});
		},
    }
}
</script>



<style rel="stylesheet/scss" lang="scss" scoped>
	/* ===== 夜茶·墨绿金 登录页（与首页同一套令牌与组件语言） ===== */
	.login-page {
		position: relative;
		width: 100vw;
		height: 100vh;
		overflow: hidden;
		display: flex;
		background: url('~@/assets/img/login-bg.png') center / cover no-repeat;
		font-family: 'Noto Sans SC', sans-serif;
	}
	.lp-overlay {
		position: absolute;
		inset: 0;
		background: linear-gradient(90deg, rgba(10, 22, 16, .88) 0%, rgba(10, 22, 16, .5) 46%, rgba(12, 27, 20, .88) 100%);
	}
	.lp-left {
		position: relative;
		z-index: 2;
		flex: 1.1;
		padding: 16vh 0 0 8vw;
	}
	.lp-seal {
		display: inline-block;
		background: #a63d2f;
		color: #f6f3ec;
		font-family: 'TeaSerif', 'STSong', serif;
		font-size: 15px;
		letter-spacing: 3px;
		padding: 6px 10px;
		border-radius: 6px;
		margin-bottom: 26px;
		box-shadow: 0 4px 14px rgba(0, 0, 0, .35);
	}
	.lp-eyebrow {
		color: #93a396;
		font-size: 13px;
		letter-spacing: 6px;
		margin-bottom: 30px;
	}
	.lp-vertical {
		position: absolute;
		right: 10%;
		top: 50%;
		transform: translateY(-50%);
		writing-mode: vertical-rl;
		font-family: 'TeaSerif', 'STSong', serif;
		color: #e6ce9a;
		font-size: 20px;
		letter-spacing: 10px;
		opacity: .85;
	}
	.lp-slogan {
		margin-top: 42px;
		color: rgba(230, 206, 154, .82);
		font-family: 'TeaSerif', 'STSong', serif;
		font-size: 18px;
		letter-spacing: 4px;
	}
	.lp-form-zone {
		position: relative;
		z-index: 2;
		width: 500px;
		flex-shrink: 0;
		display: flex;
		align-items: center;
		justify-content: center;
		background: rgba(12, 27, 20, .58);
		border-left: 1px solid rgba(212, 175, 55, .25);
		backdrop-filter: blur(10px);
	}
	.login_form {
		width: min(390px, 86%);
		padding: 20px 0;
	}
	.lp-head { margin-bottom: 38px; }
	.lp-title {
		font-family: 'TeaSerif', 'STSong', serif;
		color: #e6ce9a;
		font-size: 30px;
		letter-spacing: 5px;
		margin-bottom: 10px;
	}
	.lp-sub { color: #93a396; font-size: 11px; letter-spacing: 4px; margin-bottom: 18px; }
	.lp-line { width: 52px; height: 2px; background: linear-gradient(90deg, #d4af37, rgba(212, 175, 55, .1)); }
	.lp-field { margin-bottom: 24px; }
	.lp-field label {
		display: block;
		color: #93a396;
		font-size: 12px;
		letter-spacing: 3px;
		margin-bottom: 9px;
	}
	.lp-field input {
		width: 100%;
		height: 46px;
		background: rgba(21, 42, 32, .7);
		border: 1px solid rgba(212, 175, 55, .22);
		border-radius: 4px;
		color: #ede6d6;
		padding: 0 14px;
		font-size: 15px;
		outline: none;
		box-sizing: border-box;
		transition: border-color .25s, box-shadow .25s;
	}
	.lp-field input::placeholder { color: rgba(147, 163, 150, .5); }
	.lp-field input:focus {
		border-color: #d4af37;
		box-shadow: 0 0 0 3px rgba(212, 175, 55, .12);
	}
	.lp-password { position: relative; }
	.lp-password .iconfont {
		position: absolute;
		right: 14px;
		top: 50%;
		transform: translateY(-50%);
		color: #93a396;
		cursor: pointer;
	}
	.list-type .el-radio { color: #c9c4b4; }
	.list-type .el-radio__input.is-checked + .el-radio__label { color: #d4af37; }
	.lp-submit {
		width: 100%;
		height: 48px;
		margin-top: 6px;
		border: 0;
		border-radius: 4px;
		background: linear-gradient(160deg, #e6ce9a, #d4af37);
		color: #14251a;
		font-family: 'TeaSerif', 'STSong', serif;
		font-size: 18px;
		font-weight: 600;
		letter-spacing: 8px;
		cursor: pointer;
		box-shadow: 0 10px 26px rgba(212, 175, 55, .28);
		transition: transform .25s ease, box-shadow .25s ease;
	}
	.lp-submit:hover { transform: translateY(-2px); box-shadow: 0 14px 32px rgba(212, 175, 55, .42); }
	.lp-links { margin-top: 20px; text-align: right; }
	.lp-register {
		color: #e6ce9a;
		font-size: 13px;
		text-decoration: none;
		border-bottom: 1px dashed rgba(212, 175, 55, .4);
		padding-bottom: 2px;
		&:hover { color: #d4af37; }
	}
	.demo-fill {
		margin-top: 26px;
		padding-top: 16px;
		border-top: 1px dashed rgba(212, 175, 55, .25);
		display: flex;
		align-items: center;
		gap: 10px;
		flex-wrap: wrap;
	}
	.demo-fill-label { color: #93a396; font-size: 12px; letter-spacing: 1px; }
	.demo-fill-btn {
		padding: 5px 14px;
		border: 1px solid rgba(212, 175, 55, .45);
		border-radius: 4px;
		background: rgba(212, 175, 55, .06);
		color: #E6CE9A;
		font-size: 12px;
		letter-spacing: 1px;
		cursor: pointer;
		transition: all .2s;
		&:hover { color: #14251a; background: linear-gradient(160deg, #e6ce9a, #d4af37); border-color: #d4af37; }
	}
	@media (max-width: 900px) {
		.lp-left { display: none; }
		.lp-form-zone { width: 100%; }
	}
</style>

