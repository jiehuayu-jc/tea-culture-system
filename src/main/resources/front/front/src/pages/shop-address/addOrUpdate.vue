<template>
	<div :style='{"padding":"0 0 20px","margin":"0px auto","color":"#EDE6D6","background":"none","maxWidth":"1400px","width":"100%","fontSize":"16px","position":"relative"}'>
		<el-form class="add-update-preview" ref="form" :model="form" :rules="rules" label-width="180px">
			<el-form-item :style='{"padding":"6px 0 0","margin":"0 0 20px 0","borderColor":"#475a8310","borderRadius":"0px","background":"none","borderWidth":" 0 0 0px","borderStyle":"solid"}' label="联系人" prop="name">
				<el-input v-model="form.name" placeholder="联系人"></el-input>
			</el-form-item>
			<el-form-item :style='{"padding":"6px 0 0","margin":"0 0 20px 0","borderColor":"#475a8310","borderRadius":"0px","background":"none","borderWidth":" 0 0 0px","borderStyle":"solid"}' label="手机号码" prop="phone">
				<el-input v-model="form.phone" placeholder="手机号码"></el-input>
			</el-form-item>
			<el-form-item :style='{"padding":"6px 0 0","margin":"0 0 20px 0","borderColor":"#475a8310","borderRadius":"0px","background":"none","borderWidth":" 0 0 0px","borderStyle":"solid"}' label="默认地址">
				<el-radio-group v-model="form.isdefault">
					<el-radio label="是"></el-radio>
					<el-radio label="否"></el-radio>
				</el-radio-group>
			</el-form-item>
			<el-form-item :style='{"padding":"6px 0 0","margin":"0 0 20px 0","borderColor":"#475a8310","borderRadius":"0px","background":"none","borderWidth":" 0 0 0px","borderStyle":"solid"}' label="地址" prop="address">
				<el-input v-model="form.address" placeholder="请输入地址"></el-input>
			</el-form-item>
			<el-form-item :style='{"padding":"0","margin":"20px 0"}'>
				<el-button class="submitBtn" type="primary" @click="onSubmit('form')">
					<span class="icon iconfont "></span>
					<span class="text">添加</span>
				</el-button>
				<el-button v-if="!isBuy" class="closeBtn" @click="goBack">
					<span class="icon iconfont "></span>
					<span class="text">取消</span>
				</el-button>
			</el-form-item>
		</el-form>
	</div>
</template>

<script>
	import { Loading } from 'element-ui';
	export default {
		//数据集合
		data() {
			return {
				form: {
					userid: localStorage.getItem('frontUserid'),
					name: localStorage.getItem('username'),
					phone: '',
					isdefault: '是',
					address: ''
				},
				rules: {
					name: [{ required: true, message: '请输入联系人', trigger: 'blur' }],
					phone: [
						{ required: true, message: '请输入手机号码', trigger: 'blur' },
						{ required: true, validator: this.$validate.isMobile, trigger: 'blur' }
					],
					address: [{ required: true, message: '请选择收货地址', trigger: 'blur' }]
				},
				isEdit: false,
				isBuy: false
			}
		},
		created() {
			if (this.$route.query.id != undefined) {
				this.isEdit = true;
				this.form = Object.assign({}, JSON.parse(this.$route.query.editObj));
			}
		},
		//方法集合
		methods: {
			buyAdd(){
				this.isBuy = true
			},
			onSubmit(formName) {
				this.$refs[formName].validate((valid) => {
					if (valid) {
						this.$http.post(`address/${this.isEdit ? 'update' : 'add'}`, this.form).then(res => {
							if (res.data.code == 0) {
								this.$message({
									message: `${this.isEdit ? '更新' : '添加'}成功`,
									type: 'success',
									duration: 1500,
									onClose: () => {
										if(this.isBuy){
											this.$emit('change')
										}else{
											this.$router.go(-1);
									  }
									}
								});
							}
						});
					} else {
						return false;
					}
				});
			},
			getAddr() {
				if (this.address == '') {
					this.$message({
						message: '地址不能为空',
						type: 'error',
						duration: 1500
					});
					return;
				}
				this.form.address = this.address;
				this.dialogVisible = false;
			},
			goBack() {
				this.$router.go(-1);
			},
		}
	}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
	.add-update-preview .el-form-item ::v-deep .el-form-item__label {
		padding: 0 10px 0 0;
		color: #93A396;
		font-weight: 500;
		width: 180px;
		font-size: inherit;
		line-height: 40px;
		text-align: right;
	}
	
	.add-update-preview .el-form-item ::v-deep .el-form-item__content {
		margin-left: 180px;
	}
	
	.add-update-preview .el-input ::v-deep .el-input__inner {
		border: 1px solid #ddd;
		border-radius: 4px;
		padding: 0 12px;
		box-shadow: none;
		color: inherit;
		width: auto;
		font-size: 16px;
		height: 40px;
	}
	
	.add-update-preview .el-select ::v-deep .el-input__inner {
		border: 1px solid #ddd;
		border-radius: 4px;
		padding: 0 10px;
		color: inherit;
		width: 100%;
		font-size: 16px;
		min-width: inherit;
		height: 40px;
	}
	
	.add-update-preview .el-date-editor ::v-deep .el-input__inner {
		border: 1px solid #ddd;
		border-radius: 4px;
		padding: 0 10px 0 30px;
		box-shadow: none;
		color: inherit;
		width: auto;
		font-size: 16px;
		height: 40px;
	}
	
	.add-update-preview ::v-deep .el-upload--picture-card {
		background: transparent;
		border: 0;
		border-radius: 0;
		width: auto;
		height: auto;
		line-height: initial;
		vertical-align: middle;
	}
	
	.add-update-preview ::v-deep .upload .upload-img {
		border: 1px solid #ddd;
		cursor: pointer;
		border-radius: 4px;
		color: #93A396;
		background: transparent;
		width: 80px;
		font-size: 26px;
		line-height: 60px;
		text-align: center;
		height: 60px;
	}
	
	.add-update-preview ::v-deep .el-upload-list .el-upload-list__item {
		border: 1px solid #ddd;
		cursor: pointer;
		border-radius: 4px;
		color: #93A396;
		background: transparent;
		width: 80px;
		font-size: 26px;
		line-height: 60px;
		text-align: center;
		height: 60px;
	}
	
	.add-update-preview ::v-deep .el-upload .el-icon-plus {
		border: 1px solid #ddd;
		cursor: pointer;
		border-radius: 4px;
		color: #93A396;
		background: transparent;
		width: 80px;
		font-size: 26px;
		line-height: 60px;
		text-align: center;
		height: 60px;
	}
	
	.add-update-preview .el-textarea ::v-deep .el-textarea__inner {
		border: 1px solid #ddd;
		border-radius: 4px;
		padding: 12px;
		box-shadow: none;
		color: inherit;
		width: auto;
		font-size: 16px;
		min-height: 150px;
		min-width: 48%;
		height: auto;
	}
    .map{
        height: 50vh;
    }
	.add-update-preview .submitBtn {
		border: 0;
		cursor: pointer;
		padding: 0 24px;
		margin: 0 20px 0 0;
		display: inline-block;
		font-size: 16px;
		line-height: 44px;
		border-radius: 4px;
		background: #3E6B4F;
		width: auto;
		text-align: center;
		min-width: 120px;
		height: 44px;
		.icon {
			color: #fff;
		}
		.text {
			color: #fff;
		}
	}
	.add-update-preview .submitBtn:hover {
		.icon {
		}
		.text {
		}
	}
	.add-update-preview .closeBtn {
		border: 0px solid #ddd;
		cursor: pointer;
		padding: 0 24px;
		margin: 0 20px 0 0;
		color: #fff;
		display: inline-block;
		font-size: 16px;
		line-height: 44px;
		border-radius: 4px;
		background: #858585;
		width: auto;
		text-align: center;
		min-width: 120px;
		height: 44px;
		.icon {
			color: #fff;
		}
		.text {
			color: #fff;
		}
	}
	.add-update-preview .closeBtn:hover {
		.icon {
		}
		.text {
		}
	}
</style>
