<template>
	<div class="addEdit-block">
		<el-form
			class="add-update-preview"
			ref="ruleForm"
			:model="ruleForm"
			:rules="rules"
			label-width="180px"
		>
			<template v-show="logistics">
				<el-form-item class="input" v-if="type!='info'" label="订单编号" prop="orderid" >
					<el-input v-model="ruleForm.orderid" placeholder="订单编号" readonly></el-input>
				</el-form-item>
				<el-form-item class="input" v-else-if="ruleForm.orderid" label="订单编号" prop="orderid" >
					<el-input v-model="ruleForm.orderid" placeholder="订单编号" readonly></el-input>
				</el-form-item>
				<el-form-item class="input" v-if="type!='info'"  label="商品名称" prop="goodname" >
					<el-input v-model="ruleForm.goodname" placeholder="商品名称" clearable  :readonly="ro.goodname"></el-input>
				</el-form-item>
				<el-form-item v-else class="input" label="商品名称" prop="goodname" >
					<el-input v-model="ruleForm.goodname" placeholder="商品名称" readonly></el-input>
				</el-form-item>
				<el-form-item class="upload" v-if="type!='info' && !ro.picture" label="商品图片" prop="picture" >
					<file-upload
						tip="点击上传商品图片"
						action="file/upload"
						:limit="3"
						:multiple="true"
						:fileUrls="ruleForm.picture?ruleForm.picture:''"
						@change="pictureUploadChange"
					></file-upload>
				</el-form-item>
				<el-form-item class="upload" v-else-if="ruleForm.picture" label="商品图片" prop="picture" >
					<img v-if="ruleForm.picture.substring(0,4)=='http'" class="upload-img" style="margin-right:20px;" v-bind:key="index" :src="ruleForm.picture.split(',')[0]" width="100" height="100">
					<img v-else class="upload-img" style="margin-right:20px;" v-bind:key="index" v-for="(item,index) in ruleForm.picture.split(',')" :src="$base.url+item" width="100" height="100">
				</el-form-item>
				<el-form-item class="input" v-if="type!='info'"  label="购买数量" prop="buynumber" >
					<el-input v-model.number="ruleForm.buynumber" placeholder="购买数量" clearable  :readonly="ro.buynumber"></el-input>
				</el-form-item>
				<el-form-item v-else class="input" label="购买数量" prop="buynumber" >
					<el-input v-model="ruleForm.buynumber" placeholder="购买数量" readonly></el-input>
				</el-form-item>
				<el-form-item class="input" v-if="type!='info'"  label="价格" prop="price" >
					<el-input-number v-model="ruleForm.price" placeholder="价格" :disabled="ro.price"></el-input-number>
				</el-form-item>
				<el-form-item v-else class="input" label="价格" prop="price" >
					<el-input v-model="ruleForm.price" placeholder="价格" readonly></el-input>
				</el-form-item>
				<el-form-item class="input" v-if="type!='info'"  label="总价格" prop="total" >
					<el-input-number v-model="ruleForm.total" placeholder="总价格" :disabled="ro.total"></el-input-number>
				</el-form-item>
				<el-form-item v-else class="input" label="总价格" prop="total" >
					<el-input v-model="ruleForm.total" placeholder="总价格" readonly></el-input>
				</el-form-item>
				<el-form-item class="select" v-if="type!='info'"  label="支付类型" prop="type" >
					<el-select :disabled="ro.type" v-model.number="ruleForm.type" placeholder="请选择支付类型" >
						<el-option
							v-for="(item,index) in typeOptions"
							v-bind:key="index"
							:label="item"
							:value="index+1">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item v-else class="input" label="支付类型" prop="type" >
					<el-input v-model="ruleForm.type-0==1?'现金':'积分'"
						placeholder="支付类型" readonly></el-input>
				</el-form-item>
				<el-form-item class="select" v-if="type!='info'"  label="状态" prop="status" >
					<el-select :disabled="ro.status" v-model="ruleForm.status" placeholder="请选择状态" >
						<el-option
							v-for="(item,index) in statusOptions"
							v-bind:key="index"
							:label="item"
							:value="item">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item v-else class="input" label="状态" prop="status" >
					<el-input v-model="ruleForm.status"
						placeholder="状态" readonly></el-input>
				</el-form-item>
				<el-form-item class="input" v-if="type!='info'"  label="地址" prop="address" >
					<el-input v-model="ruleForm.address" placeholder="地址" clearable  :readonly="ro.address"></el-input>
				</el-form-item>
				<el-form-item v-else class="input" label="地址" prop="address" >
					<el-input v-model="ruleForm.address" placeholder="地址" readonly></el-input>
				</el-form-item>
				<el-form-item class="input" v-if="type!='info'"  label="电话" prop="tel" >
					<el-input v-model="ruleForm.tel" placeholder="电话" clearable  :readonly="ro.tel"></el-input>
				</el-form-item>
				<el-form-item v-else class="input" label="电话" prop="tel" >
					<el-input v-model="ruleForm.tel" placeholder="电话" readonly></el-input>
				</el-form-item>
				<el-form-item class="input" v-if="type!='info'"  label="收货人" prop="consignee" >
					<el-input v-model="ruleForm.consignee" placeholder="收货人" clearable  :readonly="ro.consignee"></el-input>
				</el-form-item>
				<el-form-item v-else class="input" label="收货人" prop="consignee" >
					<el-input v-model="ruleForm.consignee" placeholder="收货人" readonly></el-input>
				</el-form-item>
				<el-form-item class="input" v-if="type!='info'"  label="备注" prop="remark" >
					<el-input v-model="ruleForm.remark" placeholder="备注" clearable  :readonly="ro.remark"></el-input>
				</el-form-item>
				<el-form-item v-else class="input" label="备注" prop="remark" >
					<el-input v-model="ruleForm.remark" placeholder="备注" readonly></el-input>
				</el-form-item>
				<el-form-item class="input" v-if="type!='info'"  label="商户名称" prop="shangjiazhanghao" >
					<el-input v-model="ruleForm.shangjiazhanghao" placeholder="商户名称" clearable  :readonly="ro.shangjiazhanghao"></el-input>
				</el-form-item>
				<el-form-item v-else class="input" label="商户名称" prop="shangjiazhanghao" >
					<el-input v-model="ruleForm.shangjiazhanghao" placeholder="商户名称" readonly></el-input>
				</el-form-item>
				<el-form-item class="input" v-if="type!='info'"  label="用户角色" prop="role" >
					<el-input v-model="ruleForm.role" placeholder="用户角色" clearable  :readonly="ro.role"></el-input>
				</el-form-item>
				<el-form-item v-else class="input" label="用户角色" prop="role" >
					<el-input v-model="ruleForm.role" placeholder="用户角色" readonly></el-input>
				</el-form-item>
				<el-form-item class="input" v-if="type!='info'"  label="券编号" prop="couponnumber" >
					<el-input v-model="ruleForm.couponnumber" placeholder="券编号" clearable  :readonly="ro.couponnumber"></el-input>
				</el-form-item>
				<el-form-item v-else class="input" label="券编号" prop="couponnumber" >
					<el-input v-model="ruleForm.couponnumber" placeholder="券编号" readonly></el-input>
				</el-form-item>
				<el-form-item class="input" v-if="type!='info'"  label="优惠额" prop="discountamount" >
					<el-input-number v-model="ruleForm.discountamount" placeholder="优惠额" :disabled="ro.discountamount"></el-input-number>
				</el-form-item>
				<el-form-item v-else class="input" label="优惠额" prop="discountamount" >
					<el-input v-model="ruleForm.discountamount" placeholder="优惠额" readonly></el-input>
				</el-form-item>
				<el-form-item class="input" v-if="type!='info'"  label="统一订单编号" prop="orderno" >
					<el-input v-model="ruleForm.orderno" placeholder="统一订单编号" clearable  :readonly="ro.orderno"></el-input>
				</el-form-item>
				<el-form-item v-else class="input" label="统一订单编号" prop="orderno" >
					<el-input v-model="ruleForm.orderno" placeholder="统一订单编号" readonly></el-input>
				</el-form-item>
				<el-form-item class="input" v-if="type!='info'"  label="退货原因" prop="returnreason" >
					<el-input v-model="ruleForm.returnreason" placeholder="退货原因" clearable  :readonly="ro.returnreason"></el-input>
				</el-form-item>
				<el-form-item v-else class="input" label="退货原因" prop="returnreason" >
					<el-input v-model="ruleForm.returnreason" placeholder="退货原因" readonly></el-input>
				</el-form-item>
			</template>
			<el-form-item v-if="type=='info'" class="input" label="下单时间" prop="addtime">
				<el-input v-model="ruleForm.addtime" placeholder="下单时间" readonly></el-input>
			</el-form-item>
			<el-form-item v-if="type!='info'"  label="物流" prop="logistics" >
				<editor 
					style="min-width: 200px; max-width: 600px;"
					v-model="ruleForm.logistics" 
					class="editor" 
					action="file/upload">
				</editor>
			</el-form-item>
			<el-form-item v-else-if="ruleForm.logistics" label="物流" prop="logistics" >
				<span class="text ql-snow ql-editor" v-html="ruleForm.logistics"></span>
			</el-form-item>
			<el-form-item class="btn">
				<el-button class="btn3"  v-if="type!='info'" type="success" @click="onSubmit">
					<span class="icon iconfont icon-xihuan"></span>
					提交
				</el-button>
				<el-button class="btn4" v-if="type!='info'" type="success" @click="back()">
					<span class="icon iconfont icon-xihuan"></span>
					取消
				</el-button>
				<el-button class="btn5" v-if="type=='info'" type="success" @click="back()">
					<span class="icon iconfont icon-xihuan"></span>
					返回
				</el-button>
			</el-form-item>
		</el-form>
    

	</div>
</template>
<script>
	import { 
		isNumber,
		isIntNumer,
	} from "@/utils/validate";
	export default {
		data() {
			var validateNumber = (rule, value, callback) => {
				if(!value){
					callback();
				} else if (!isNumber(value)) {
					callback(new Error("请输入数字"));
				} else {
					callback();
				}
			};
			var validateIntNumber = (rule, value, callback) => {
				if(!value){
					callback();
				} else if (!isIntNumer(value)) {
					callback(new Error("请输入整数"));
				} else {
					callback();
				}
			};
			return {
				logistics: true,
				id: '',
				type: '',
			
			
				ro:{
					orderid : false,
					tablename : false,
					userid : false,
					goodid : false,
					goodname : false,
					picture : false,
					buynumber : false,
					price : false,
					total : false,
					type : false,
					status : false,
					address : false,
					tel : false,
					consignee : false,
					logistics : false,
					remark : false,
					shangjiazhanghao : false,
					sfsh : false,
					shhf : false,
					role : false,
					couponnumber : false,
					discountamount : false,
					orderno : false,
					returnreason : false,
				},
			
				ruleForm: {
					orderid: this.getUUID(),
					userid: '',
					goodid: '',
					goodname: '',
					picture: '',
					buynumber: '',
					status: '',
					address: '',
					tel: '',
					consignee: '',
					logistics: '',
					remark: '',
					shangjiazhanghao: '',
					sfsh: '',
					shhf: '',
					role: '',
					couponnumber: '',
					orderno: '',
					returnreason: '',
				},
				typeOptions: [],
				statusOptions: [],

				rules: {
					orderid: [
						{ required: true, message: '订单编号不能为空', trigger: 'blur' },
					],
					tablename: [
					],
					userid: [
						{ required: true, message: '用户id不能为空', trigger: 'blur' },
					],
					goodid: [
						{ required: true, message: '商品id不能为空', trigger: 'blur' },
					],
					goodname: [
					],
					picture: [
					],
					buynumber: [
						{ required: true, message: '购买数量不能为空', trigger: 'blur' },
						{ validator: validateIntNumber, trigger: 'blur' },
					],
					price: [
						{ required: true, message: '价格不能为空', trigger: 'blur' },
						{ validator: validateNumber, trigger: 'blur' },
					],
					total: [
						{ required: true, message: '总价格不能为空', trigger: 'blur' },
						{ validator: validateNumber, trigger: 'blur' },
					],
					type: [
						{ validator: validateIntNumber, trigger: 'blur' },
					],
					status: [
					],
					address: [
					],
					tel: [
					],
					consignee: [
					],
					logistics: [
					],
					remark: [
					],
					shangjiazhanghao: [
					],
					sfsh: [
					],
					shhf: [
					],
					role: [
					],
					couponnumber: [
					],
					discountamount: [
						{ validator: validateNumber, trigger: 'blur' },
					],
					orderno: [
					],
					returnreason: [
					],
				},
			};
		},
		props: ["parent"],
		computed: {



		},
		components: {
		},
		created() {
		},
		methods: {
			// 下载
			download(file){
				window.open(`${file}`)
			},
			// 初始化
			init(id,type) {
				if (id) {
					this.id = id;
					this.type = type;
				}
				if(this.type=='info'||this.type=='else'||this.type=='msg'){
					this.info(id);
				}else if(this.type=='logistics'){
					this.logistics=false;
					this.info(id);
				}else if(this.type=='cross'){
					var obj = this.$storage.getObj('crossObj');
					for (var o in obj){
						if(o=='orderid'){
							this.ruleForm.orderid = obj[o];
							this.ro.orderid = true;
							continue;
						}
						if(o=='tablename'){
							this.ruleForm.tablename = obj[o];
							this.ro.tablename = true;
							continue;
						}
						if(o=='userid'){
							this.ruleForm.userid = obj[o];
							this.ro.userid = true;
							continue;
						}
						if(o=='goodid'){
							this.ruleForm.goodid = obj[o];
							this.ro.goodid = true;
							continue;
						}
						if(o=='goodname'){
							this.ruleForm.goodname = obj[o];
							this.ro.goodname = true;
							continue;
						}
						if(o=='picture'){
							this.ruleForm.picture = obj[o];
							this.ro.picture = true;
							continue;
						}
						if(o=='buynumber'){
							this.ruleForm.buynumber = obj[o];
							this.ro.buynumber = true;
							continue;
						}
						if(o=='price'){
							this.ruleForm.price = obj[o];
							this.ro.price = true;
							continue;
						}
						if(o=='total'){
							this.ruleForm.total = obj[o];
							this.ro.total = true;
							continue;
						}
						if(o=='type'){
							this.ruleForm.type = obj[o];
							this.ro.type = true;
							continue;
						}
						if(o=='status'){
							this.ruleForm.status = obj[o];
							this.ro.status = true;
							continue;
						}
						if(o=='address'){
							this.ruleForm.address = obj[o];
							this.ro.address = true;
							continue;
						}
						if(o=='tel'){
							this.ruleForm.tel = obj[o];
							this.ro.tel = true;
							continue;
						}
						if(o=='consignee'){
							this.ruleForm.consignee = obj[o];
							this.ro.consignee = true;
							continue;
						}
						if(o=='logistics'){
							this.ruleForm.logistics = obj[o];
							this.ro.logistics = true;
							continue;
						}
						if(o=='remark'){
							this.ruleForm.remark = obj[o];
							this.ro.remark = true;
							continue;
						}
						if(o=='shangjiazhanghao'){
							this.ruleForm.shangjiazhanghao = obj[o];
							this.ro.shangjiazhanghao = true;
							continue;
						}
						if(o=='role'){
							this.ruleForm.role = obj[o];
							this.ro.role = true;
							continue;
						}
						if(o=='couponnumber'){
							this.ruleForm.couponnumber = obj[o];
							this.ro.couponnumber = true;
							continue;
						}
						if(o=='discountamount'){
							this.ruleForm.discountamount = obj[o];
							this.ro.discountamount = true;
							continue;
						}
						if(o=='orderno'){
							this.ruleForm.orderno = obj[o];
							this.ro.orderno = true;
							continue;
						}
						if(o=='returnreason'){
							this.ruleForm.returnreason = obj[o];
							this.ro.returnreason = true;
							continue;
						}
					}
				}
				this.typeOptions = "现金,积分".split(',')
				this.statusOptions = "未支付,已支付,已完成,已取消,已退款,已发货".split(',')
			
			},
			// 多级联动参数

			info(id) {
				this.$http({
					url: `orders/info/${id}`,
					method: "get"
				}).then(({ data }) => {
					if (data && data.code === 0) {
						this.ruleForm = data.data;
						//解决前台上传图片后台不显示的问题
						let reg=new RegExp('../../../upload','g')//g代表全部
						this.ruleForm.logistics = this.ruleForm.logistics.replace(reg,'../../../springbootj8kskvkr/upload');
					} else {
						this.$message.error(data.msg);
					}
				});
			},

			// 提交
			async onSubmit() {
					if(this.ruleForm.orderid) {
						this.ruleForm.orderid = String(this.ruleForm.orderid)
					}
					if(this.ruleForm.picture!=null) {
						this.ruleForm.picture = this.ruleForm.picture.replace(new RegExp(this.$base.url,"g"),"");
					}
					var objcross = this.$storage.getObj('crossObj');
					await this.$refs["ruleForm"].validate(async valid => {
						if (valid) {
							if(this.type=='cross'){
								var statusColumnName = this.$storage.get('statusColumnName');
								var statusColumnValue = this.$storage.get('statusColumnValue');
								if(statusColumnName!='') {
									var obj = this.$storage.getObj('crossObj');
									if(statusColumnName && !statusColumnName.startsWith("[")) {
										for (var o in obj){
											if(o==statusColumnName){
												obj[o] = statusColumnValue;
											}
										}
										var table = this.$storage.get('crossTable');
										await this.$http({
											url: `${table}/update`,
											method: "post",
											data: obj
										}).then(({ data }) => {});
									}
								}
							}
							
							await this.$http({
								url: `orders/${!this.ruleForm.id ? "save" : "update"}`,
								method: "post",
								data: this.ruleForm
							}).then(async ({ data }) => {
								if (data && data.code === 0) {
									this.$message({
										message: "操作成功",
										type: "success",
										duration: 1500,
										onClose: () => {
											this.parent.showFlag = true;
											this.parent.addOrUpdateFlag = false;
											this.parent.ordersCrossAddOrUpdateFlag = false;
											this.parent.search();
											this.parent.contentStyleChange();
										}
									});
								} else {
									this.$message.error(data.msg);
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
				this.parent.showFlag = true;
				this.parent.addOrUpdateFlag = false;
				this.parent.ordersCrossAddOrUpdateFlag = false;
				this.parent.contentStyleChange();
			},
			pictureUploadChange(fileUrls) {
				this.ruleForm.picture = fileUrls;
			},
		}
	};
</script>
<style lang="scss" scoped>
	.addEdit-block {
		padding: 30px;
	}
	.add-update-preview {
		border-radius: 10px;
		padding: 40px 20px 20px;
		background: #fff;
		width: 100%;
		border-color: #eee;
		border-width: 0px 0 0;
		border-style: solid;
	}
	.amap-wrapper {
		width: 100%;
		height: 500px;
	}
	
	.search-box {
		position: absolute;
	}
	
	.el-date-editor.el-input {
		width: auto;
	}
	.add-update-preview /deep/ .el-form-item {
		border: 0px solid #eee;
		padding: 0;
		margin: 0 0 26px 0;
		display: inline-block;
		width: 49%;
	}
	.add-update-preview .el-form-item /deep/ .el-form-item__label {
		padding: 0 10px 0 0;
		color: #9E9E9E;
		font-weight: 600;
		width: 180px;
		font-size: 16px;
		line-height: 40px;
		text-align: right;
	}
	
	.add-update-preview .el-form-item /deep/ .el-form-item__content {
		margin-left: 180px;
	}
	.add-update-preview .el-form-item span.text {
		padding: 0 10px;
		color: #333;
		background: none;
		font-weight: 500;
		display: inline-block;
		font-size: 16px;
		line-height: 40px;
		min-width: 50%;
	}
	
	.add-update-preview .el-input {
		width: 100%;
	}
	.add-update-preview .el-input /deep/ .el-input__inner {
		border: 1px solid #E8E8E8;
		border-radius: 5px;
		padding: 0 12px;
		color: #000;
		width: 100%;
		font-size: 16px;
		min-width: 50%;
		height: 40px;
	}
	.add-update-preview .el-input /deep/ .el-input__inner[readonly="readonly"] {
		border: 1px solid #E8E8E8;
		cursor: not-allowed;
		border-radius: 5px;
		padding: 0 12px;
		color: #666;
		background: #ddd;
		width: 100%;
		font-size: 16px;
		height: 40px;
	}
	.add-update-preview .el-input-number {
		text-align: left;
		width: 100%;
	}
	.add-update-preview .el-input-number /deep/ .el-input__inner {
		text-align: left;
		border: 1px solid #E8E8E8;
		border-radius: 5px;
		padding: 0 12px;
		color: #000;
		width: 100%;
		font-size: 16px;
		min-width: 50%;
		height: 40px;
	}
	.add-update-preview .el-input-number /deep/ .is-disabled .el-input__inner {
		text-align: left;
		border: 1px solid #E8E8E8;
		cursor: not-allowed;
		border-radius: 5px;
		padding: 0 12px;
		color: #666;
		background: #ddd;
		width: 100%;
		font-size: 16px;
		height: 40px;
	}
	.add-update-preview .el-input-number /deep/ .el-input-number__decrease {
		display: none;
	}
	.add-update-preview .el-input-number /deep/ .el-input-number__increase {
		display: none;
	}
	.add-update-preview .el-select {
		width: 100%;
	}
	.add-update-preview .el-select /deep/ .el-input__inner {
		border: 1px solid #E8E8E8;
		border-radius: 5px;
		padding: 0 10px;
		color: #000;
		width: 100%;
		font-size: 16px;
		height: 40px;
	}
	.add-update-preview .el-select /deep/ .is-disabled .el-input__inner {
		border: 1px solid #E8E8E8;
		cursor: not-allowed;
		border-radius: 5px;
		padding: 0 10px;
		color: #666;
		background: #ddd;
		width: 100%;
		font-size: 16px;
		height: 40px;
	}
	.add-update-preview .el-date-editor {
		width: 100%;
	}
	.add-update-preview .el-date-editor /deep/ .el-input__inner {
		border: 1px solid #E8E8E8;
		border-radius: 5px;
		padding: 0 10px 0 30px;
		color: #000;
		background: #fff;
		width: 100%;
		font-size: 16px;
		height: 40px;
	}
	.add-update-preview .el-date-editor /deep/ .el-input__inner[readonly="readonly"] {
		border: 1px solid #E8E8E8;
		cursor: not-allowed;
		border-radius: 5px;
		padding: 0 10px 0 30px;
		color: #666;
		background: #ddd;
		width: 100%;
		font-size: 16px;
		height: 40px;
	}
	.add-update-preview .viewBtn {
		border: 1px solid #507AFC;
		cursor: pointer;
		border-radius: 5px;
		padding: 0 15px;
		box-shadow: 4px 4px 0 #507AFC;
		margin: 0 10px 10px 0;
		transform: skew(-15deg, 0deg);
		color: #507AFC;
		background: #fff;
		width: auto;
		font-size: 14px;
		height: 36px;
		.iconfont {
			margin: 0 2px;
			color: #666;
			display: none;
			font-size: 16px;
			height: 34px;
		}
	}
	.add-update-preview .viewBtn:hover {
		box-shadow: 4px 4px 0 #aaa;
		color: #aaa;
		border-color: #aaa;
		opacity: 1;
	}
	.add-update-preview .downBtn {
		border: 1px solid #FAC858;
		cursor: pointer;
		border-radius: 5px;
		padding: 0 15px;
		box-shadow: 4px 4px 0 #FAC858;
		margin: 0 10px 10px 0;
		transform: skew(-15deg, 0deg);
		color: #FAC858;
		background: #fff;
		width: auto;
		font-size: 14px;
		height: 36px;
		.iconfont {
			margin: 0 2px;
			color: inherit;
			display: none;
			font-size: inherit;
			height: 34px;
		}
	}
	.add-update-preview .downBtn:hover {
		box-shadow: 4px 4px 0 #aaa;
		color: #aaa;
		border-color: #aaa;
		opacity: 1;
	}
	.add-update-preview .unBtn {
		border: 1px solid #5AFFEF;
		cursor: not-allowed;
		border-radius: 5px;
		padding: 0 15px;
		box-shadow: 4px 4px 0 #5AFFEF;
		margin: 0 10px 10px 0;
		transform: skew(-15deg, 0deg);
		color: #5AFFEF;
		background: #fff;
		width: auto;
		font-size: 14px;
		height: 36px;
		.iconfont {
			margin: 0 2px;
			color: #fff;
			display: none;
			font-size: 14px;
			height: 34px;
		}
	}
	.add-update-preview .unBtn:hover {
		opacity: 1;
	}
	.add-update-preview /deep/ .el-upload--picture-card {
		background: transparent;
		border: 0;
		border-radius: 0;
		width: auto;
		height: auto;
		line-height: initial;
		vertical-align: middle;
	}
	
	.add-update-preview /deep/ .upload .upload-img {
		border: 1px solid #E8E8E8;
		cursor: pointer;
		border-radius: 5px;
		color: #666;
		background: #fff;
		width: 90px;
		font-size: 24px;
		line-height: 60px;
		text-align: center;
		height: 60px;
	}
	
	.add-update-preview /deep/ .el-upload-list .el-upload-list__item {
		border: 1px solid #E8E8E8;
		cursor: pointer;
		border-radius: 5px;
		color: #666;
		background: #fff;
		width: 90px;
		font-size: 24px;
		line-height: 60px;
		text-align: center;
		height: 60px;
	}
	
	.add-update-preview /deep/ .el-upload .el-icon-plus {
		border: 1px solid #E8E8E8;
		cursor: pointer;
		border-radius: 5px;
		color: #666;
		background: #fff;
		width: 90px;
		font-size: 24px;
		line-height: 60px;
		text-align: center;
		height: 60px;
	}
	.add-update-preview /deep/ .el-upload__tip {
		color: #666;
		font-size: 15px;
	}
	
	.add-update-preview .el-textarea /deep/ .el-textarea__inner {
		border: 1px solid #E8E8E8;
		border-radius: 5px;
		padding: 12px;
		color: #000;
		background: #fff;
		width: 100%;
		font-size: 14px;
		height: 120px;
	}
	.add-update-preview .el-textarea /deep/ .el-textarea__inner[readonly="readonly"] {
				border: 1px solid #E8E8E8;
				cursor: not-allowed;
				border-radius: 5px;
				padding: 12px;
				color: #666;
				background: #ddd;
				width: 100%;
				font-size: 14px;
				height: 120px;
			}
	.add-update-preview .el-form-item.btn {
		padding: 0;
		margin: 20px 0 0;
		.btn1 {
			border: 0px solid #ccc;
			cursor: pointer;
			border-radius: 5px;
			padding: 0 10px;
			margin: 0 10px 0 0;
			color: #fff;
			background: #179BD7;
			width: auto;
			font-size: 16px;
			min-width: 110px;
			height: 40px;
			.iconfont {
				margin: 0 2px;
				color: #fff;
				display: none;
				font-size: 14px;
				height: 40px;
			}
		}
		.btn1:hover {
			transform: skew(-15deg, 0deg);
			opacity: 1;
		}
		.btn2 {
			border: 0px solid #ccc;
			cursor: pointer;
			border-radius: 5px;
			padding: 0 10px;
			margin: 0 10px 0 0;
			color: #fff;
			background: #FFBA59;
			width: auto;
			font-size: 16px;
			min-width: 110px;
			height: 40px;
			.iconfont {
				margin: 0 2px;
				color: #fff;
				display: none;
				font-size: 14px;
				height: 34px;
			}
		}
		.btn2:hover {
			transform: skew(-15deg, 0deg);
			opacity: 1;
		}
		.btn3 {
			border: 0px solid #ccc;
			cursor: pointer;
			border-radius: 4px;
			padding: 0 10px;
			margin: 0 10px 0 0;
			color: #fff;
			background: #507AFC;
			width: auto;
			font-size: 16px;
			min-width: 110px;
			height: 40px;
			.iconfont {
				margin: 0 2px;
				color: #fff;
				display: none;
				font-size: 14px;
				height: 40px;
			}
		}
		.btn3:hover {
			transform: skew(-15deg, 0deg);
			opacity: 1;
		}
		.btn4 {
			border: 0px solid #ccc;
			cursor: pointer;
			border-radius: 4px;
			padding: 0 10px;
			margin: 0 10px 0 0;
			color: #fff;
			background: #8D8D8D;
			width: auto;
			font-size: 16px;
			min-width: 110px;
			height: 40px;
			.iconfont {
				margin: 0 2px;
				color: #fff;
				display: none;
				font-size: 14px;
				height: 40px;
			}
		}
		.btn4:hover {
			transform: skew(-15deg, 0deg);
			opacity: 1;
		}
		.btn5 {
			border: 0px solid #ccc;
			cursor: pointer;
			border-radius: 4px;
			padding: 0 10px;
			margin: 0 10px 0 0;
			color: #fff;
			background: #43B27F;
			width: auto;
			font-size: 16px;
			min-width: 110px;
			height: 40px;
			.iconfont {
				margin: 0 2px;
				color: #fff;
				display: none;
				font-size: 14px;
				height: 40px;
			}
		}
		.btn5:hover {
			transform: skew(-15deg, 0deg);
			opacity: 1;
		}
	}
</style>
