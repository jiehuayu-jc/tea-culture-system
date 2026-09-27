<template>
	<div class="login-page">
		<div class="login-left">
			<div class="ll-grain"></div>
			<i class="ll-leaf ll-leaf1"></i>
			<i class="ll-leaf ll-leaf2"></i>
			<div class="ll-char">茶</div>
			<div class="ll-vertical">夜茶一盏 · 半席山河</div>
			<div class="ll-bottom">
				<span class="ll-seal">陆羽茶经</span>
				<div class="ll-slogan">六大茶类 · 源头直采 · 以盏见心</div>
			</div>
		</div>
		<div class="login-right">
			<el-form class="login_form animate__animated animate__fadeInUp">
				<div class="lr-head">
					<div class="lr-title">茶文化管理系统</div>
					<div class="lr-sub">MANAGE CONSOLE</div>
					<div class="lr-line"></div>
				</div>
				<div v-if="loginType==1" class="lr-field">
					<label>账&nbsp;号</label>
					<input placeholder="请输入账号：" name="username" type="text" v-model="rulesForm.username">
				</div>
				<div v-if="loginType==1" class="lr-field">
					<label>密&nbsp;码</label>
					<div class="lr-password">
						<input placeholder="请输入密码：" name="password" :type="showPassword?'text':'password'" v-model="rulesForm.password">
						<span class="icon iconfont" :class="showPassword?'icon-liulan13':'icon-liulan17'" @click="showPassword=!showPassword"></span>
					</div>
				</div>
				<div class="lr-field" v-if="roles.length>1">
					<label>角&nbsp;色</label>
					<div prop="loginInRole" class="list-type">
						<el-radio v-if="loginType==1||(loginType==2&&item.roleName!='管理员')" v-for="item in roles" v-bind:key="item.roleName" v-model="rulesForm.role" :label="item.roleName">{{item.roleName}}</el-radio>
					</div>
				</div>
				<el-button v-if="loginType==1" type="primary" @click="login()" class="loginInBt">登&nbsp;录</el-button>
				<div class="lr-foot">
					<span class="lr-tip">还没有商户账号？</span>
					<span class="register" @click="register('shangjia')">注册茶商 →</span>
				</div>
			</el-form>
		</div>
	</div>
</template>
<script>
	import 'animate.css'
	import menu from "@/utils/menu";
	export default {
		data() {
			return {
				verifyCheck2: false,
				flag: false,
				baseUrl:this.$base.url,
				loginType: 1,
				rulesForm: {
					username: "",
					password: "",
					role: "",
				},
				menus: [],
				roles: [],
				tableName: "",
				showPassword: false,
			};
		},
		mounted() {
			let menus = menu.list();
			this.menus = menus;

			for (let i = 0; i < this.menus.length; i++) {
				if (this.menus[i].hasBackLogin=='是') {
					this.roles.push(this.menus[i])
				}
			}

		},
		created() {

		},
		destroyed() {
		},
		components: {
		},
		methods: {

			//注册
			register(tableName){
				this.$storage.set("loginTable", tableName);
				this.$router.push({path:'/register',query:{pageFlag:'register'}})
			},
			// 登陆
			login() {

				if (!this.rulesForm.username) {
					this.$message.error("请输入用户名");
					return;
				}
				if (!this.rulesForm.password) {
					this.$message.error("请输入密码");
					return;
				}
				if(this.roles.length>1) {
					if (!this.rulesForm.role) {
						this.$message.error("请选择角色");
						return;
					}

					let menus = this.menus;
					for (let i = 0; i < menus.length; i++) {
						if (menus[i].roleName == this.rulesForm.role) {
							this.tableName = menus[i].tableName;
						}
					}
				} else {
					this.tableName = this.roles[0].tableName;
					this.rulesForm.role = this.roles[0].roleName;
				}
		
				this.loginPost()
			},
			loginPost() {
				this.$http({
					url: `${this.tableName}/login?username=${this.rulesForm.username}&password=${this.rulesForm.password}`,
					method: "post"
				}).then(({ data }) => {
					if (data && data.code === 0) {
						this.$storage.set("Token", data.token);
						this.$storage.set("role", this.rulesForm.role);
						this.$storage.set("sessionTable", this.tableName);
						this.$storage.set("adminName", this.rulesForm.username);
						this.$router.replace({ path: "/" });
					} else {
						this.$message.error(data.msg);
					}
				});
			},
		}
	}
</script>

<style lang="scss" scoped>
.login-container {
	min-height: 100vh;
	position: relative;
	background-repeat: no-repeat;
	background-position: center center;
	background-size: cover;
	background-repeat: no-repeat;
	background-size: cover;
	display: flex;
	width: 100%;
	min-height: 100vh;
	justify-content: center;
	align-items: center;
	background-image: url(~@/assets/img/login-bg.png);
	background-position: center center;

	.login_form {
		padding: 40px 200px 40px 33%;
		margin: 0;
		z-index: 1000;
		display: flex;
		min-height: 68vh;
		flex-wrap: wrap;
		border-radius: 0;
		box-shadow: inset 0px 0px 0px 0px #000;
		flex-direction: column;
		background: url(~@/assets/img/login-side.webp) left center/35% 100% no-repeat #fff;
		width: 70%;
		align-items: flex-start;
		position: statics;
		height: auto;
		.login_form2 {
			width: 100%;
		}
		.title-container {
			padding: 0 ;
			margin: 0 0 80px -120px;
			color: #2E523C;
			background: none;
			font-weight: 600;
			width: calc(100% + 240px);
			font-size: 22px;
			line-height: 40px;
			text-align: center;
		}
		.list-item {
			padding: 0;
			margin: 0 0 10px;
			display: flex;
			width: calc(100% - 0px);
			align-items: center;
			flex-wrap: wrap;
			.lable {
				color: #000;
				width: 100%;
				font-size: 14px;
				line-height: 40px;
				text-align: left;
			}
			input {
				border: 1px solid #efeff7;
				border-radius: 5px;
				padding: 0 10px;
				color: #666;
				width: 100%;
				font-size: 16px;
				height: 50px;
			}
			input:focus {
				border: 1px solid #d8d8d8;
				border-radius: 5px;
				padding: 0 10px;
				color: #000;
				width: 100%;
				font-size: 16px;
				height: 50px;
			}
			.password-box {
				display: flex;
				width: 100%;
				position: relative;
				align-items: center;
				input {
					border: 1px solid #efeff7;
					border-radius: 5px;
					padding: 0 10px;
					color: #666;
					width: 100%;
					font-size: 14px;
					height: 50px;
				}
				input:focus {
					border: 1px solid #d8d8d8;
					border-radius: 5px;
					padding: 0 10px;
					color: #000;
					width: 100%;
					font-size: 14px;
					height: 50px;
				}
				.iconfont {
					cursor: pointer;
					z-index: 1;
					color: #666;
					top: 0;
					font-size: 16px;
					line-height: 50px;
					position: absolute;
					right: 12px;
				}
			}
			input::placeholder {
				color: #999;
				font-size: 16px;
			}
		}
		.list-type {
			padding: 0;
			margin: 0;
			top: 120px;
			background: none;
			width: 67%;
			line-height: 40px;
			position: absolute;
			height: auto;
			/deep/ .el-radio__input .el-radio__inner {
				background: rgba(53, 53, 53, 0);
				display: none;
				border-color: #666;
			}
			/deep/ .el-radio__input.is-checked .el-radio__inner {
				background: #3E6B4F;
				display: none;
				border-color: #3E6B4F;
			}
			/deep/ .el-radio__label {
				border: 1px solid #D8D8D8;
				padding: 0 20px;
				color: #c8c8c8;
				display: inline-block;
				font-size: 18px;
				border-width: 0 0 2px;
				line-height: 40px;
				height: 40px;
			}
			/deep/ .el-radio__input.is-checked+.el-radio__label {
				border: 1px solid #3E6B4F;
				padding: 0 20px;
				color: #3E6B4F;
				display: inline-block;
				font-size: 18px;
				border-width: 0 0 2px;
				line-height: 40px;
				height: 40px;
			}
		}
		.login-btn {
			margin: 20px auto;
			display: flex;
			width: 100%;
			justify-content: center;
			align-items: center;
			flex-wrap: wrap;
			.login-btn1 {
				width: 100%;
			}
			.login-btn2 {
				padding: 0 60px;
				top: 66%;
				left: 0;
				display: flex;
				width: 35%;
				justify-content: center;
				align-items: center;
				position: absolute;
				flex-wrap: wrap;
			}
			.login-btn3 {
				width: 100%;
			}
			.loginInBt {
				border: 0px solid rgba(0, 0, 0, 1);
				cursor: pointer;
				border-radius: 60px;
				padding: 0 10px;
				margin: 0 0 10px;
				color: #fff;
				background: #3E6B4F;
				font-weight: 600;
				width: 100%;
				font-size: 26px;
				min-width: 68px;
				height: 60px;
			}
			.loginInBt:hover {
				color: #aaa;
				opacity: 0.8;
			}
			.register {
				border: 0px solid rgba(0, 0, 0, 1);
				cursor: pointer;
				border-radius: 4px;
				padding: 0 10px;
				margin: 0 10px 15px;
				color: rgba(0, 0, 0, 1);
				background: #fff;
				width: calc(50% - 20px);
				font-size: 16px;
				height: 34px;
			}
			.register:hover {
				opacity: 0.8;
			}
			.forget {
				border: 0;
				cursor: pointer;
				padding: 0;
				margin: 20px auto 0;
				color: #D8D8D8;
				font-weight: bold;
				display: block;
				font-size: 18px;
				border-radius: 0;
				background: none;
				width: auto;
				text-align: center;
				height: 34px;
			}
			.forget:hover {
				opacity: 0.5;
			}
		}
	}
	.idea-box1 {
		border: 4px solid #fff;
		border-radius: 50%;
		top: 25%;
		left: calc(17% - 55px);
		background: url(http://codegen.caihongy.cn/20241008/fbafeed7f8ba433a89d57f0ca0d7eaf5.jpg) center center/100% 100% no-repeat;
		width: 120px;
		position: absolute;
		height: 120px;
		order: -2;
	}
	.idea-box2 {
		margin: 5px 0 40px;
		color: #fff;
		top: 48%;
		left: 0;
		background: none;
		font-weight: bold;
		width: 35%;
		font-size: 44px;
		position: absolute;
		text-align: center;
		height: 30px;
		order: -1;
	}
}

</style>
