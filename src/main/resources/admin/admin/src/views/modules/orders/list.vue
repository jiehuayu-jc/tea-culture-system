<template>
	<div class="main-content" :style='{"padding":"20px 30px","fontSize":"15px","flexWrap":"wrap","display":"flex"}'>
		<!-- 列表页 -->
		<template v-if="showFlag">
			<el-form class="center-form-pv" :style='{"width":"100%","margin":"0 10px 20px"}' :inline="true" :model="searchForm">
				<el-row :style='{"width":"100%","padding":"10px","alignItems":"center","flexWrap":"wrap","background":"none","display":"flex"}' >
					<div :style='{"alignItems":"center","margin":"0 10px 0 0","display":"flex"}'>
						<label :style='{"margin":"0 5px 0 0","whiteSpace":"nowrap","color":"#fff","display":"inline-block","width":"auto","lineHeight":"40px","fontSize":"15px","fontWeight":"500","height":"40px"}' class="item-label">订单编号</label>
						<el-input v-model="searchForm.orderid" placeholder="订单编号" @keydown.enter.native="search()" clearable></el-input>
					</div>
					<div :style='{"alignItems":"center","margin":"0 10px 0 0","display":"flex"}'>
						<label :style='{"margin":"0 5px 0 0","whiteSpace":"nowrap","color":"#fff","display":"inline-block","width":"auto","lineHeight":"40px","fontSize":"15px","fontWeight":"500","height":"40px"}' class="item-label">商品名称</label>
						<el-input v-model="searchForm.goodname" placeholder="商品名称" @keydown.enter.native="search()" clearable></el-input>
					</div>
					<el-button class="search" type="success" @click="search()">
						<span class="icon iconfont icon-fangdajing07" :style='{"margin":"0 2px","fontSize":"16px","color":"#fff","height":"40px"}'></span>
						查询
					</el-button>
				</el-row>

				<el-row class="actions" :style='{"padding":"10px","margin":"20px 0","flexWrap":"wrap","background":"none","flexDirection":"row-reverse","display":"flex","width":"100%","justifyContent":"flex-start"}'>
					<el-button class="add" v-if="isAuth('orders'+'/'+orderStatus,'新增')" type="success" @click="addOrUpdateHandler()">
						<span class="icon iconfont icon-tianjia14" :style='{"margin":"0 2px","fontSize":"inherit","color":"inherit","height":"38px"}'></span>
						创建
					</el-button>
					<el-button class="del" v-if="isAuth('orders'+'/'+orderStatus,'删除')" :disabled="dataListSelections.length?false:true" type="danger" @click="deleteHandler()">
						<span class="icon iconfont icon-shanchu9" :style='{"margin":"0 2px","fontSize":"inherit","color":"inherit","height":"38px"}'></span>
						删除
					</el-button>


					<download-excel v-if="isAuth('orders'+'/'+orderStatus,'导出')" class="export-excel-wrapper" :data = "dataList" :fields = "json_fields" name = "订单.xls">
						<!-- 导出excel -->
						<el-button class="btn18" type="success">
							<span class="icon iconfont icon-daochu1" :style='{"margin":"0 2px","fontSize":"inherit","color":"inherit","height":"38px"}'></span>
							导出
						</el-button>
					</download-excel>
					<el-button class="btn18" v-if="isAuth('orders'+'/'+orderStatus,'日销量')" type="success" @click="dayNumberChartDialog()">
						<span class="icon iconfont icon-tongjitu01" :style='{"margin":"0 2px","fontSize":"inherit","color":"inherit","height":"38px"}'></span>
						日销量
					</el-button>
					<el-button class="btn18" v-if="isAuth('orders'+'/'+orderStatus,'月销量')" type="success" @click="monthNumberChartDialog()">
						<span class="icon iconfont icon-tongjitu01" :style='{"margin":"0 2px","fontSize":"inherit","color":"inherit","height":"38px"}'></span>
						月销量
					</el-button>
					<el-button class="btn18" v-if="isAuth('orders'+'/'+orderStatus,'年销量')" type="success" @click="yearNumberChartDialog()">
						<span class="icon iconfont icon-tongjitu01" :style='{"margin":"0 2px","fontSize":"inherit","color":"inherit","height":"38px"}'></span>
						年销量
					</el-button>
					<el-button class="btn18" v-if="isAuth('orders'+'/'+orderStatus,'品销量')" type="success" @click="goodnameNumberChartDialog()">
						<span class="icon iconfont icon-tongji18" :style='{"margin":"0 2px","fontSize":"inherit","color":"inherit","height":"38px"}'></span>
						商品销量
					</el-button>
					<el-button class="btn18" v-if="isAuth('orders'+'/'+orderStatus,'类销量')" type="success" @click="goodtypeNumberChartDialog()">
						<span class="icon iconfont icon-tongji18" :style='{"margin":"0 2px","fontSize":"inherit","color":"inherit","height":"38px"}'></span>
						类型销量
					</el-button>
					<el-button class="btn18" v-if="isAuth('orders'+'/'+orderStatus,'日销额')" type="success" @click="dayAmountChartDialog()">
						<span class="icon iconfont icon-tongjitu13" :style='{"margin":"0 2px","fontSize":"inherit","color":"inherit","height":"38px"}'></span>
						日销额
					</el-button>
					<el-button class="btn18" v-if="isAuth('orders'+'/'+orderStatus,'月销额')" type="success" @click="monthAmountChartDialog()">
						<span class="icon iconfont icon-tongjitu13" :style='{"margin":"0 2px","fontSize":"inherit","color":"inherit","height":"38px"}'></span>
						月销额
					</el-button>
					<el-button class="btn18" v-if="isAuth('orders'+'/'+orderStatus,'年销额')" type="success" @click="yearAmountChartDialog()">
						<span class="icon iconfont icon-tongjitu13" :style='{"margin":"0 2px","fontSize":"inherit","color":"inherit","height":"38px"}'></span>
						年销额
					</el-button>
					<el-button class="btn18" v-if="isAuth('orders'+'/'+orderStatus,'品销额')" type="success" @click="goodnameAmountChartDialog()">
						<span class="icon iconfont icon-tongji5" :style='{"margin":"0 2px","fontSize":"inherit","color":"inherit","height":"38px"}'></span>
						商品销额
					</el-button>
					<el-button class="btn18" v-if="isAuth('orders'+'/'+orderStatus,'类销额')" type="success" @click="goodtypeAmountChartDialog()">
						<span class="icon iconfont icon-tongji5" :style='{"margin":"0 2px","fontSize":"inherit","color":"inherit","height":"38px"}'></span>
						类型销额
					</el-button>
					
				</el-row>
			</el-form>
			<div :style='{"width":"100%","padding":"0 10px","fontSize":"14px","color":"#000"}'>
				<el-table class="tables"
					:stripe='false'
					:style='{"padding":"0","borderColor":"#9E9E9E","borderRadius":"20px 20px 0 0","borderWidth":"1px 0 0 1px","background":"#fff","width":"100%","fontSize":"inherit","borderStyle":"solid"}' 
					:border='true'
					v-if="isAuth('orders'+'/'+orderStatus,'查看')"
					:data="dataList"
					v-loading="dataListLoading"
				@selection-change="selectionChangeHandler">
					<el-table-column :resizable='false' type="selection" align="center" width="50"></el-table-column>
					<el-table-column :resizable='false' :sortable='false' label="序号" type="index" width="50" />
					<el-table-column :resizable='false' :sortable='false'  
						prop="orderid"
						label="订单编号">
						<template slot-scope="scope">
							{{scope.row.orderid}}
						</template>
					</el-table-column>
					<el-table-column :resizable='false' :sortable='false'  
						prop="goodname"
						label="商品名称">
						<template slot-scope="scope">
							{{scope.row.goodname}}
						</template>
					</el-table-column>
					<el-table-column  :resizable='false' prop="picture" width="200" label="商品图片">
						<template slot-scope="scope">
							<div v-if="scope.row.picture">
								<img v-if="scope.row.picture.substring(0,4)=='http'" :src="scope.row.picture.split(',')[0]" width="100" height="100" style="object-fit: cover" @click="imgPreView(scope.row.picture.split(',')[0])">
								<img v-else :src="$base.url+scope.row.picture.split(',')[0]" width="100" height="100" style="object-fit: cover" @click="imgPreView($base.url+scope.row.picture.split(',')[0])">
							</div>
							<div v-else>无图片</div>
						</template>
					</el-table-column>
					<el-table-column :resizable='false' :sortable='false'  
						prop="buynumber"
						label="购买数量">
						<template slot-scope="scope">
							{{scope.row.buynumber}}
						</template>
					</el-table-column>
					<el-table-column :resizable='false' :sortable='false'  
						prop="price"
						label="价格">
						<template slot-scope="scope">
							{{scope.row.price}}
						</template>
					</el-table-column>
					<el-table-column :resizable='false' :sortable='false'  
						prop="total"
						label="总价格">
						<template slot-scope="scope">
							{{scope.row.total}}
						</template>
					</el-table-column>
					<el-table-column :resizable='false' :sortable='false' prop="status" :formatter="orderStatusFormatter"
						label="状态">
					</el-table-column>
					<el-table-column :resizable='false' :sortable='false'  
						prop="address"
						label="地址">
						<template slot-scope="scope">
							{{scope.row.address}}
						</template>
					</el-table-column>
					<el-table-column :resizable='false' :sortable='false'  
						prop="tel"
						label="电话">
						<template slot-scope="scope">
							{{scope.row.tel}}
						</template>
					</el-table-column>
					<el-table-column :resizable='false' :sortable='false'  
						prop="consignee"
						label="收货人">
						<template slot-scope="scope">
							{{scope.row.consignee}}
						</template>
					</el-table-column>
					<el-table-column :resizable='false' :sortable='false'  
						prop="remark"
						label="备注">
						<template slot-scope="scope">
							{{scope.row.remark}}
						</template>
					</el-table-column>
					<el-table-column :resizable='false' :sortable='false'  
						prop="shangjiazhanghao"
						label="商户名称">
						<template slot-scope="scope">
							{{scope.row.shangjiazhanghao}}
						</template>
					</el-table-column>
					<el-table-column :resizable='false' :sortable='false' prop="sfsh" label="退货审核" v-if="changeReturnGoods()">
						<template slot-scope="scope">
							<el-tag type="success" v-if="scope.row.sfsh=='是'">已通过</el-tag>
							<el-tag type="warning" v-if="scope.row.sfsh=='待审核'">待审核</el-tag>
							<el-tag type="danger" v-if="scope.row.sfsh=='否'">未通过</el-tag>
						</template>
					</el-table-column>
					<el-table-column :resizable='false' :sortable='false' prop="shhf" label="审核回复" show-overflow-tooltip v-if="changeReturnGoods()">
						<template slot-scope="scope">
							<div style="white-space: nowrap;">{{scope.row.shhf}}</div>
						</template>
					</el-table-column>
					<el-table-column :resizable='false' :sortable='false' prop="returnreason" label="退货原因" show-overflow-tooltip v-if="changeReturnGoods()">
						<template slot-scope="scope">
							<div style="white-space: nowrap;">{{scope.row.returnreason}}</div>
						</template>
					</el-table-column>
					<el-table-column :resizable='false' :sortable='false' prop="addtime" label="下单时间">
						<template slot-scope="scope">
							{{scope.row.addtime}}
						</template>
					</el-table-column>
					<el-table-column width="300" label="操作">
						<template slot-scope="scope">
							<el-button class="view" v-if=" isAuth('orders'+'/'+orderStatus,'查看')" type="success" @click="addOrUpdateHandler(scope.row.id,'info')">
								<span class="icon iconfont icon-xihuan" :style='{"margin":"0 2px","fontSize":"14px","color":"#fff","display":"none","height":"40px"}'></span>
								查看
							</el-button>
							<el-button class="edit" v-if=" isAuth('orders'+'/'+orderStatus,'修改') " type="success" @click="addOrUpdateHandler(scope.row.id)">
								<span class="icon iconfont icon-xihuan" :style='{"margin":"0 2px","fontSize":"14px","color":"#fff","display":"none","height":"40px"}'></span>
								编辑
							</el-button>

							<el-button class="btn8" v-if="isAuth('orders'+'/'+orderStatus,'物流')&&scope.row.id" type="success" @click="logisticsUpdate(scope.row.id)">
								<span class="icon iconfont icon-xihuan" :style='{"margin":"0 2px","fontSize":"14px","color":"#fff","display":"none","height":"40px"}'></span>
								物流
							</el-button>
							<el-button class="btn8" v-if="isAuth('orders'+'/'+orderStatus,'发货')&&scope.row.id" type="success" @click="updateHandler(scope.row)">
								<span class="icon iconfont icon-xihuan" :style='{"margin":"0 2px","fontSize":"14px","color":"#fff","display":"none","height":"40px"}'></span>
								发货
							</el-button>
							<el-button class="btn8" v-if="isAuth('orders'+'/'+orderStatus,'确认收货')&&scope.row.id" type="success" @click="updateHandler2(scope.row)">
								<span class="icon iconfont icon-xihuan" :style='{"margin":"0 2px","fontSize":"14px","color":"#fff","display":"none","height":"40px"}'></span>
								确认收货
							</el-button>
							<el-button class="btn8" v-if="isAuth('orders'+'/'+orderStatus,'退货审核')&&scope.row.sfsh=='待审核'" type="success" @click="returnSh(scope.row)">
								<span class="icon iconfont icon-xihuan" :style='{"margin":"0 2px","fontSize":"14px","color":"#fff","display":"none","height":"40px"}'></span>
								退货审核
							</el-button>



							<el-button class="del" v-if="isAuth('orders'+'/'+orderStatus,'删除') " type="primary" @click="deleteHandler(scope.row.id )">
								<span class="icon iconfont icon-xihuan" :style='{"margin":"0 2px","fontSize":"14px","color":"#fff","display":"none","height":"40px"}'></span>
								删除
							</el-button>
						</template>
					</el-table-column>
				</el-table>
			</div>
			<el-pagination
				@size-change="sizeChangeHandle"
				@current-change="currentChangeHandle"
				:current-page="pageIndex"
				background
				:page-sizes="[10, 50, 100, 200]"
				:page-size="pageSize"
				:layout="layouts.join()"
				:total="totalPage"
				prev-text="上一页 "
				next-text="下一页 "
				:hide-on-single-page="false"
				:style='{"padding":"0","boxShadow":"0px 2px 5px 0px rgba(0,0,0,0.3)","margin":"20px auto 0","whiteSpace":"nowrap","color":"#333","alignItems":"center","textAlign":"center","display":"flex","justifyContent":"center","overflow":"hidden","borderRadius":"30px","background":"#fff","width":"auto","fontSize":"inherit","fontWeight":"500"}'
			></el-pagination>
		</template>
		
		<!-- 添加/修改页面  将父组件的search方法传递给子组件-->
		<add-or-update v-if="addOrUpdateFlag" :parent="this" ref="addOrUpdate"></add-or-update>


		<el-dialog title="退货审核" :visible.sync="returnShVisible" width="50%">
			<el-form ref="returnShForm" :model="returnShForm" label-width="80px" :rules="returnShRules">
				<el-form-item label="退货理由" prop="returnreason">
					{{returnShForm.returnreason}}
				</el-form-item>
				<el-form-item label="审核状态" prop="sfsh">
					<el-select v-model="returnShForm.sfsh" placeholder="审核状态">
						<el-option label="通过" value="是"></el-option>
						<el-option label="不通过" value="否"></el-option>
						<el-option label="待审核" value="待审核"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="内容" prop="shhf">
					<el-input type="textarea" :rows="8" v-model="returnShForm.shhf"></el-input>
				</el-form-item>
			</el-form>
			<span slot="footer" class="dialog-footer">
				<el-button @click="returnShVisible=false">取 消</el-button>
				<el-button type="primary" @click="returnShSave">确 定</el-button>
			</span>
		</el-dialog>


		<el-dialog
			title="日销量"
			:visible.sync="dayNumberChartVisiable"
			width="800">
			<div id="dayNumberChart" style="width:100%;height:600px;"></div>
			<span slot="footer" class="dialog-footer">
				<el-button @click="dayNumberChartDialog">返回</el-button>
			</span>
		</el-dialog>

		<el-dialog
			title="月销量"
			:visible.sync="monthNumberChartVisiable"
			width="800">
			<div id="monthNumberChart" style="width:100%;height:600px;"></div>
			<span slot="footer" class="dialog-footer">
				<el-button @click="monthNumberChartDialog">返回</el-button>
			</span>
		</el-dialog>
		
		<el-dialog
			title="年销量"
			:visible.sync="yearNumberChartVisiable"
			width="800">
			<div id="yearNumberChart" style="width:100%;height:600px;"></div>
			<span slot="footer" class="dialog-footer">
				<el-button @click="yearNumberChartDialog">返回</el-button>
			</span>
		</el-dialog>
	
		<el-dialog
			title="商品销量"
			:visible.sync="goodnameNumberChartVisiable"
			width="800">
			<div id="goodnameNumberChart" style="width:100%;height:600px;"></div>
			<span slot="footer" class="dialog-footer">
				<el-button @click="goodnameNumberChartDialog">返回</el-button>
			</span>
		</el-dialog>
		
		<el-dialog
			title="类型销量"
			:visible.sync="goodtypeNumberChartVisiable"
			width="800">
			<div id="goodtypeNumberChart" style="width:100%;height:600px;"></div>
			<span slot="footer" class="dialog-footer">
				<el-button @click="goodtypeNumberChartDialog">返回</el-button>
			</span>
		</el-dialog>
	
		<el-dialog
			title="日销额"
			:visible.sync="dayAmountChartVisiable"
			width="800">
			<div id="dayAmountChart" style="width:100%;height:600px;"></div>
			<span slot="footer" class="dialog-footer">
				<el-button @click="dayAmountChartDialog">返回</el-button>
			</span>
		</el-dialog>
		
		<el-dialog
			title="月销额"
			:visible.sync="monthAmountChartVisiable"
			width="800">
			<div id="monthAmountChart" style="width:100%;height:600px;"></div>
			<span slot="footer" class="dialog-footer">
				<el-button @click="monthAmountChartDialog">返回</el-button>
			</span>
		</el-dialog>
	
		<el-dialog
			title="年销额"
			:visible.sync="yearAmountChartVisiable"
			width="800">
			<div id="yearAmountChart" style="width:100%;height:600px;"></div>
			<span slot="footer" class="dialog-footer">
				<el-button @click="yearAmountChartDialog">返回</el-button>
			</span>
		</el-dialog>
		
		<el-dialog
			title="商品销额"
			:visible.sync="goodnameAmountChartVisiable"
			width="800">
			<div id="goodnameAmountChart" style="width:100%;height:600px;"></div>
			<span slot="footer" class="dialog-footer">
				<el-button @click="goodnameAmountChartDialog">返回</el-button>
			</span>
		</el-dialog>
		
		<el-dialog
			title="类型销额"
			:visible.sync="goodtypeAmountChartVisiable"
			width="800">
			<div id="goodtypeAmountChart" style="width:100%;height:600px;"></div>
			<span slot="footer" class="dialog-footer">
				<el-button @click="goodtypeAmountChartDialog">返回</el-button>
			</span>
		</el-dialog>

		<el-dialog title="预览图" :visible.sync="previewVisible" width="50%">
			<img :src="previewImg" alt="" style="width: 100%;">
		</el-dialog>
	</div>
</template>

<script>
	import * as echarts from 'echarts'
	import chinaJson from "@/components/echarts/china.json";
	import axios from 'axios';
	import AddOrUpdate from "./add-or-update";
	import {
		Loading
	} from 'element-ui';
	export default {
		data() {
			return {
				indexQueryCondition: '',
				searchForm: {
					key: ""
				},
				form:{},
				dataList: [],
				pageIndex: 1,
				pageSize: 10,
				totalPage: 0,
				dataListLoading: false,
				dataListSelections: [],
				showFlag: true,
				line: {"backgroundColor":"transparent","yAxis":{"axisLabel":{"borderType":"solid","rotate":0,"padding":0,"shadowOffsetX":0,"margin":15,"backgroundColor":"transparent","borderColor":"#000","shadowOffsetY":0,"color":"#333","shadowBlur":0,"show":true,"inside":false,"ellipsis":"...","overflow":"none","borderRadius":0,"borderWidth":0,"width":"","fontSize":12,"lineHeight":24,"shadowColor":"transparent","fontWeight":"normal","height":""},"axisTick":{"show":true,"length":5,"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"cap":"butt","color":"#333","shadowBlur":0,"width":1,"type":"solid","opacity":1,"shadowColor":"rgba(0,0,0,.5)"},"inside":false},"splitLine":{"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"cap":"butt","color":"#666","shadowBlur":0,"width":1,"type":"solid","opacity":1,"shadowColor":"rgba(0,0,0,.5)"},"show":true},"axisLine":{"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"cap":"butt","color":"#333","shadowBlur":0,"width":1,"type":"solid","opacity":1,"shadowColor":"rgba(0,0,0,.5)"},"show":true},"splitArea":{"show":false,"areaStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"color":"rgba(25,25,25,0.3)","opacity":1,"shadowBlur":10,"shadowColor":"rgba(0,0,0,.5)"}}},"xAxis":{"axisLabel":{"borderType":"solid","rotate":0,"padding":0,"shadowOffsetX":0,"margin":4,"backgroundColor":"transparent","borderColor":"#000","shadowOffsetY":0,"color":"#333","shadowBlur":0,"show":true,"inside":false,"ellipsis":"...","overflow":"none","borderRadius":0,"borderWidth":0,"width":"","fontSize":12,"lineHeight":24,"shadowColor":"transparent","fontWeight":"normal","height":""},"axisTick":{"show":true,"length":5,"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"cap":"butt","color":"#333","shadowBlur":0,"width":1,"type":"solid","opacity":1,"shadowColor":"rgba(0,0,0,.5)"},"inside":false},"splitLine":{"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"cap":"butt","color":"#333","shadowBlur":0,"width":1,"type":"solid","opacity":1,"shadowColor":"rgba(0,0,0,.5)"},"show":false},"axisLine":{"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"cap":"butt","color":"#333","shadowBlur":0,"width":1,"type":"solid","opacity":1,"shadowColor":"rgba(0,0,0,.5)"},"show":true},"splitArea":{"show":false,"areaStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"color":"rgba(25,25,25,.3)","opacity":1,"shadowBlur":10,"shadowColor":"rgba(0,0,0,.5)"}}},"color":["#179BD7","#91CC75","#507AFC","#FAC858","#93BEFF","#0E2732","#3E6DFF","#9a60b4","#ea7ccc"],"legend":{"padding":0,"itemGap":10,"shadowOffsetX":0,"backgroundColor":"transparent","borderColor":"#666","shadowOffsetY":0,"orient":"horizontal","shadowBlur":0,"bottom":"auto","itemHeight":14,"show":true,"icon":"roundRect","itemStyle":{"borderType":"solid","shadowOffsetX":0,"borderColor":"inherit","shadowOffsetY":0,"color":"#333","shadowBlur":0,"borderWidth":0,"opacity":1,"shadowColor":"transparent"},"right":"auto","top":"auto","borderRadius":0,"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"color":"inherit","shadowBlur":0,"width":"auto","type":"inherit","opacity":1,"shadowColor":"transparent"},"left":"right","borderWidth":0,"width":"80%","itemWidth":20,"textStyle":{"textBorderWidth":0,"color":"#333","textShadowColor":"transparent","ellipsis":"...","overflow":"none","fontSize":12,"lineHeight":24,"textShadowOffsetX":0,"textShadowOffsetY":0,"textBorderType":"solid","fontWeight":500,"textBorderColor":"transparent","textShadowBlur":0},"shadowColor":"rgba(0,0,0,.3)","height":"auto"},"series":{"showSymbol":true,"symbol":"emptyCircle","symbolSize":10},"tooltip":{"backgroundColor":"#123","textStyle":{"color":"#fff"}},"title":{"borderType":"solid","padding":0,"shadowOffsetX":0,"backgroundColor":"transparent","borderColor":"#666","shadowOffsetY":0,"shadowBlur":0,"bottom":"auto","show":true,"right":"auto","top":"auto","borderRadius":0,"left":"left","borderWidth":0,"textStyle":{"textBorderWidth":0,"color":"#333","textShadowColor":"transparent","fontSize":14,"lineHeight":24,"textShadowOffsetX":0,"textShadowOffsetY":0,"textBorderType":"solid","fontWeight":600,"textBorderColor":"#666","textShadowBlur":0},"shadowColor":"transparent"}},
				bar: {"backgroundColor":"transparent","yAxis":{"axisLabel":{"borderType":"solid","rotate":0,"padding":0,"shadowOffsetX":0,"margin":12,"backgroundColor":"transparent","borderColor":"#666","shadowOffsetY":0,"color":"#333","shadowBlur":0,"show":true,"inside":false,"ellipsis":"...","overflow":"none","borderRadius":0,"borderWidth":0,"width":"","fontSize":12,"lineHeight":24,"shadowColor":"transparent","fontWeight":"normal","height":""},"axisTick":{"show":false,"length":5,"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"cap":"butt","color":"#333","shadowBlur":0,"width":1,"type":"solid","opacity":1,"shadowColor":"rgba(0,0,0,.5)"},"inside":false},"splitLine":{"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"cap":"butt","color":"#666","shadowBlur":0,"width":1,"type":"solid","opacity":1,"shadowColor":"rgba(0,0,0,.5)"},"show":false},"axisLine":{"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"cap":"butt","color":"#333","shadowBlur":0,"width":1,"type":"solid","opacity":1,"shadowColor":"rgba(0,0,0,.5)"},"show":true},"splitArea":{"show":false,"areaStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"color":"rgba(25,25,25,0.3)","opacity":1,"shadowBlur":10,"shadowColor":"rgba(0,0,0,.5)"}}},"xAxis":{"axisLabel":{"borderType":"solid","rotate":0,"padding":0,"shadowOffsetX":0,"margin":4,"backgroundColor":"transparent","borderColor":"#000","shadowOffsetY":0,"color":"#333","shadowBlur":0,"show":true,"inside":false,"ellipsis":"...","overflow":"none","borderRadius":0,"borderWidth":0,"width":"","fontSize":12,"lineHeight":24,"shadowColor":"transparent","fontWeight":"normal","height":""},"axisTick":{"show":false,"length":5,"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"cap":"butt","color":"#333","shadowBlur":0,"width":1,"type":"solid","opacity":1,"shadowColor":"rgba(0,0,0,.5)"},"inside":false},"splitLine":{"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"cap":"butt","color":"#333","shadowBlur":0,"width":1,"type":"solid","opacity":1,"shadowColor":"rgba(0,0,0,.5)"},"show":false},"axisLine":{"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"cap":"butt","color":"#333","shadowBlur":0,"width":1,"type":"solid","opacity":1,"shadowColor":"rgba(0,0,0,.5)"},"show":true},"splitArea":{"show":false,"areaStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"color":"rgba(25,25,25,.3)","opacity":1,"shadowBlur":10,"shadowColor":"rgba(0,0,0,.5)"}}},"color":["#179BD7","#91CC75","#507AFC","#FAC858","#93BEFF","#0E2732","#3E6DFF","#9a60b4","#ea7ccc"],"legend":{"padding":0,"itemGap":10,"shadowOffsetX":0,"backgroundColor":"transparent","borderColor":"#666","shadowOffsetY":0,"orient":"horizontal","shadowBlur":0,"bottom":"auto","itemHeight":14,"show":true,"icon":"roundRect","itemStyle":{"borderType":"solid","shadowOffsetX":0,"borderColor":"inherit","shadowOffsetY":0,"color":"inherit","shadowBlur":0,"borderWidth":0,"opacity":1,"shadowColor":"transparent"},"right":"auto","top":"auto","borderRadius":0,"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"color":"inherit","shadowBlur":0,"width":"auto","type":"inherit","opacity":1,"shadowColor":"transparent"},"left":"right","borderWidth":0,"width":"80%","itemWidth":20,"textStyle":{"textBorderWidth":0,"color":"inherit","textShadowColor":"transparent","ellipsis":"...","overflow":"none","fontSize":12,"lineHeight":12,"textShadowOffsetX":0,"textShadowOffsetY":0,"textBorderType":"solid","fontWeight":500,"textBorderColor":"transparent","textShadowBlur":0},"shadowColor":"rgba(0,0,0,.3)","height":"auto"},"series":{"barWidth":"auto","itemStyle":{"borderType":"solid","shadowOffsetX":0,"borderColor":"#666","shadowOffsetY":0,"color":"","borderRadius":20,"shadowBlur":4,"borderWidth":0,"opacity":1,"shadowColor":"#000"},"colorBy":"data","barCategoryGap":"20%"},"tooltip":{"backgroundColor":"#123","textStyle":{"color":"#fff"}},"title":{"borderType":"solid","padding":0,"shadowOffsetX":0,"backgroundColor":"transparent","borderColor":"#666","shadowOffsetY":0,"shadowBlur":0,"bottom":"auto","show":true,"right":"auto","top":"auto","borderRadius":0,"left":"left","borderWidth":0,"textStyle":{"textBorderWidth":0,"color":"#333","textShadowColor":"transparent","fontSize":14,"lineHeight":24,"textShadowOffsetX":0,"textShadowOffsetY":0,"textBorderType":"solid","fontWeight":600,"textBorderColor":"#666","textShadowBlur":0},"shadowColor":"transparent"},"base":{"animate":false,"interval":2000}},
				pie: {"tooltip":{"backgroundColor":"#123","textStyle":{"color":"#fff"}},"backgroundColor":"transparent","color":["#179BD7","#91CC75","#507AFC","#FAC858","#93BEFF","#0E2732","#3E6DFF","#9a60b4","#ea7ccc"],"title":{"borderType":"solid","padding":0,"shadowOffsetX":0,"backgroundColor":"transparent","borderColor":"#666","shadowOffsetY":0,"shadowBlur":0,"bottom":"auto","show":true,"right":"auto","top":"auto","borderRadius":0,"left":"left","borderWidth":0,"textStyle":{"textBorderWidth":0,"color":"#333","textShadowColor":"transparent","fontSize":14,"lineHeight":14,"textShadowOffsetX":0,"textShadowOffsetY":0,"textBorderType":"solid","fontWeight":600,"textBorderColor":"#666","textShadowBlur":0},"shadowColor":"transparent"},"legend":{"padding":0,"itemGap":10,"shadowOffsetX":0,"backgroundColor":"transparent","borderColor":"#666","shadowOffsetY":0,"orient":"horizontal","shadowBlur":0,"bottom":"auto","itemHeight":10,"show":true,"icon":"roundRect","itemStyle":{"borderType":"solid","shadowOffsetX":0,"borderColor":"inherit","shadowOffsetY":0,"color":"inherit","shadowBlur":0,"borderWidth":0,"opacity":1,"shadowColor":"transparent"},"right":0,"top":"auto","borderRadius":0,"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"color":"inherit","shadowBlur":0,"width":"auto","type":"inherit","opacity":1,"shadowColor":"transparent"},"left":"right","borderWidth":0,"width":"80%","itemWidth":20,"textStyle":{"textBorderWidth":0,"color":"inherit","textShadowColor":"transparent","ellipsis":"...","overflow":"none","fontSize":12,"lineHeight":12,"textShadowOffsetX":0,"textShadowOffsetY":0,"textBorderType":"solid","fontWeight":500,"textBorderColor":"transparent","textShadowBlur":0},"shadowColor":"rgba(0,0,0,.3)","height":"auto"},"series":{"itemStyle":{"borderType":"solid","shadowOffsetX":0,"borderColor":"#666","shadowOffsetY":0,"color":"","shadowBlur":4,"borderWidth":0,"opacity":1,"shadowColor":"#000"},"label":{"borderType":"solid","rotate":0,"padding":0,"textBorderWidth":0,"backgroundColor":"transparent","borderColor":"#666","color":"inherit","show":true,"textShadowColor":"transparent","distanceToLabelLine":5,"ellipsis":"...","overflow":"none","borderRadius":0,"borderWidth":0,"fontSize":12,"lineHeight":18,"textShadowOffsetX":0,"position":"outside","textShadowOffsetY":0,"textBorderType":"solid","textBorderColor":"#666","textShadowBlur":0},"labelLine":{"show":false,"length":10,"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"color":"#666","shadowBlur":0,"width":1,"type":"solid","opacity":1,"shadowColor":"#000"},"length2":14,"smooth":false}}},
				funnel: {"tooltip":{"backgroundColor":"#123","textStyle":{"color":"#fff"}},"backgroundColor":"transparent","color":["#179BD7","#91CC75","#507AFC","#FAC858","#93BEFF","#0E2732","#3E6DFF","#9a60b4","#ea7ccc"],"title":{"borderType":"solid","padding":2,"shadowOffsetX":0,"backgroundColor":"transparent","borderColor":"#ccc","shadowOffsetY":0,"shadowBlur":0,"bottom":"auto","show":true,"right":"auto","top":"auto","borderRadius":0,"left":"center","borderWidth":0,"textStyle":{"textBorderWidth":0,"color":"#666","textShadowColor":"transparent","fontSize":14,"lineHeight":12,"textShadowOffsetX":0,"textShadowOffsetY":0,"textBorderType":"solid","fontWeight":500,"textBorderColor":"#ccc","textShadowBlur":0},"shadowColor":"transparent"},"legend":{"padding":5,"itemGap":10,"shadowOffsetX":0,"backgroundColor":"transparent","borderColor":"#ccc","shadowOffsetY":0,"orient":"vertical","shadowBlur":0,"bottom":"auto","itemHeight":10,"show":true,"icon":"roundRect","itemStyle":{"borderType":"solid","shadowOffsetX":0,"borderColor":"inherit","shadowOffsetY":0,"color":"inherit","shadowBlur":0,"borderWidth":0,"opacity":1,"shadowColor":"transparent"},"top":"auto","borderRadius":0,"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"color":"inherit","shadowBlur":0,"width":"auto","type":"inherit","opacity":1,"shadowColor":"transparent"},"left":"left","borderWidth":0,"width":"auto","itemWidth":20,"textStyle":{"textBorderWidth":0,"color":"inherit","textShadowColor":"transparent","ellipsis":"...","overflow":"none","fontSize":12,"lineHeight":20,"textShadowOffsetX":0,"textShadowOffsetY":0,"textBorderType":"solid","fontWeight":500,"textBorderColor":"transparent","textShadowBlur":0},"shadowColor":"rgba(0,0,0,.3)","height":"auto"},"series":{"itemStyle":{"borderType":"solid","shadowOffsetX":0,"borderColor":"#000","shadowOffsetY":0,"color":"","shadowBlur":4,"borderWidth":0,"opacity":1,"shadowColor":"#000"},"label":{"borderType":"solid","rotate":0,"padding":0,"textBorderWidth":0,"backgroundColor":"transparent","borderColor":"#fff","color":"","show":true,"textShadowColor":"transparent","distanceToLabelLine":5,"ellipsis":"...","overflow":"none","borderRadius":0,"borderWidth":0,"fontSize":12,"lineHeight":18,"textShadowOffsetX":0,"position":"outside","textShadowOffsetY":0,"textBorderType":"solid","textBorderColor":"#fff","textShadowBlur":0},"labelLine":{"show":true,"length":10,"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"shadowBlur":0,"width":1,"type":"solid","opacity":1,"shadowColor":"#000"},"length2":14,"smooth":false}}},
				boardBase: {"funnelNum":8,"lineNum":8,"gaugeNum":8,"barNum":8,"pieNum":8},
				gauge: {"tooltip":{"backgroundColor":"#123","textStyle":{"color":"#fff"}},"backgroundColor":"transparent","color":["#179BD7","#91CC75","#507AFC","#FAC858","#93BEFF","#0E2732","#3E6DFF","#9a60b4","#ea7ccc"],"title":{"top":"top","left":"left","textStyle":{"fontSize":14,"lineHeight":24,"color":"#333","fontWeight":600}},"series":{"pointer":{"offsetCenter":[0,"10%"],"icon":"path://M2.9,0.7L2.9,0.7c1.4,0,2.6,1.2,2.6,2.6v115c0,1.4-1.2,2.6-2.6,2.6l0,0c-1.4,0-2.6-1.2-2.6-2.6V3.3C0.3,1.9,1.4,0.7,2.9,0.7z","width":8,"length":"80%"},"axisLine":{"lineStyle":{"shadowOffsetX":0,"shadowOffsetY":0,"opacity":0.5,"shadowBlur":1,"shadowColor":"#000"},"roundCap":true},"anchor":{"show":true,"itemStyle":{"color":"inherit"},"size":18,"showAbove":true},"emphasis":{"disabled":false},"progress":{"show":true,"roundCap":true,"overlap":true},"splitNumber":25,"detail":{"formatter":"{value}","backgroundColor":"inherit","color":"#fff","borderRadius":3,"width":20,"fontSize":12,"height":10},"title":{"fontSize":14},"animation":true}},
				dayNumberChartVisiable: false,
				monthNumberChartVisiable: false,
				yearNumberChartVisiable: false,
				goodnameNumberChartVisiable: false,
				goodtypeNumberChartVisiable: false,
				dayAmountChartVisiable: false,
				monthAmountChartVisiable: false,
				yearAmountChartVisiable: false,
				goodnameAmountChartVisiable: false,
				goodtypeAmountChartVisiable: false,
				returnShVisible: false,
				returnShForm: {},
				returnShRules:{
					sfsh:[{ required: true, message: '审核状态不能为空', trigger: 'blur' },],
					shhf:[{ required: true, message: '审核回复不能为空', trigger: 'blur' },],
				},
				addOrUpdateFlag:false,
				layouts: ["prev","pager","next"],
				orderStatus: this.$route.params.status,
//导出excel
				json_fields: {
					"订单编号": "orderid",    //常规字段
					"商品表名": "tablename",    //常规字段
					"用户id": "userid",    //常规字段
					"商品id": "goodid",    //常规字段
					"商品名称": "goodname",    //常规字段
					"商品图片": "picture",    //常规字段
					"购买数量": "buynumber",    //常规字段
					"价格": "price",    //常规字段
					"总价格": "total",    //常规字段
					"支付类型": "type",    //常规字段
					"状态": "status",    //常规字段
					"地址": "address",    //常规字段
					"电话": "tel",    //常规字段
					"收货人": "consignee",    //常规字段
					"物流": "logistics",    //常规字段
					"备注": "remark",    //常规字段
					"商户名称": "shangjiazhanghao",    //常规字段
					"是否审核": "sfsh",    //常规字段
					"审核回复": "shhf",    //常规字段
					"用户角色": "role",    //常规字段
					"券编号": "couponnumber",    //常规字段
					"优惠额": "discountamount",    //常规字段
					"统一订单编号": "orderno",    //常规字段
					"退货原因": "returnreason",    //常规字段
				},
				json_meta: [
					[
						{
							" key ": " charset ",
							" value ": " utf- 8 "
						}
					]
				],
				previewImg: '',
				previewVisible: false,
			};
		},
		created() {
			this.init();
			this.getDataList();
			this.contentStyleChange();
		},
		mounted() {
		},
		//监听订单表参数是否变化，从而调取接口
		watch: {
			'$route' (to, from) { //监听路由是否变化
				if(this.$route.params.status){//判断状态是否有值
				//调数据
					this.orderStatus=this.$route.params.status;
					this.getDataList();
					this.contentStyleChange()
				}
			}
		},
		filters: {
			htmlfilter: function (val) {
				return val.replace(/<[^>]*>/g).replace(/undefined/g,'');
			}
		},
		computed: {
			tablename(){
				return this.$storage.get('sessionTable')
			},
		},
		components: {
			AddOrUpdate,
		},
		methods: {
			imgPreView(url){
				this.previewImg = url
				this.previewVisible = true
			},
			orderStatusFormatter: function(row, column) {
				var temp = row.status
				if(row.status=='已退款'&&row.sfsh!=''){
					temp = '已退货'
				}
				return temp
			},
			orderTypeFormatter: function(row, column) {
				var temp = ''
			  // 处理逻辑 保存接口 数据，进行匹配 name
				switch (row.type-0) {
					case 1:
						temp = '现金'
						break
					case 2:
						temp = '积分'
						break
					case 3:
						temp = '拼团'
						break
				}
				return temp
			},
			updateHandler(row) {
				this.$confirm(`确定进行发货操作?`, "提示", {
					confirmButtonText: "确定",
					cancelButtonText: "取消",
					type: "warning"
				}).then(() => {
					row.status = "已发货";
					this.$http({
						url: `orders/update`,
						method: "post",
						data: row
					}).then(({ data }) => {
						if (data && data.code === 0) {
							this.$message({
								message: "操作成功",
								type: "success",
								duration: 1500,
								onClose: () => {
									this.search();
								}
							});
						} else {
							this.$message.error(data.msg);
						}
					});
				});
			},
			updateHandler2(row) {
				this.$confirm(`确定已收货?`, "提示", {
					confirmButtonText: "确定",
					cancelButtonText: "取消",
					type: "warning"
				}).then(() => {
					row.status = "已完成";
					this.$http({
						url: `orders/update`,
						method: "post",
						data: row
					}).then(({ data }) => {
						if (data && data.code === 0) {
							this.$message({
								message: "操作成功",
								type: "success",
								duration: 1500,
								onClose: () => {
									this.search();
								}
							});
						} else {
							this.$message.error(data.msg);
						}
					});
				});
			},
			//物流
			logisticsUpdate(id,type) {
				this.showFlag = false;
				this.addOrUpdateFlag = true;
				this.crossAddOrUpdateFlag = false;
				if(type!='info'){
					type = 'logistics';
				}
				this.$nextTick(() => {
					this.$refs.addOrUpdate.init(id,type);
				});
			},
			contentStyleChange() {
				this.contentPageStyleChange()
			},
			// 分页
			contentPageStyleChange(){
				let arr = []

				// if(this.contents.pageTotal) arr.push('total')
				// if(this.contents.pageSizes) arr.push('sizes')
				// if(this.contents.pagePrevNext){
				//   arr.push('prev')
				//   if(this.contents.pagePager) arr.push('pager')
				//   arr.push('next')
				// }
				// if(this.contents.pageJumper) arr.push('jumper')
				// this.layouts = arr.join()
				// this.contents.pageEachNum = 10
			},
			// 退货审核
			returnSh(row){
				this.returnShForm = JSON.parse(JSON.stringify(row))
				this.returnShVisible = true
			},
			// 退货审核保存
			returnShSave(){
				this.$refs["returnShForm"].validate(valid => {
					if(valid){
						if(this.returnShForm.sfsh=="是"){
							this.returnShForm.status = '已退款'
							this.$http({
								url: `${this.returnShForm.tablename}/info/${this.returnShForm.goodid}`,
								method: 'get',
							}).then(res=>{
								if(res.data&&res.data.code==0){
									let good = res.data.data
									// 商品存在库存。加回库存
									if(good.alllimittimes){
										good.alllimittimes = parseInt(good.alllimittimes) + parseInt(this.returnShForm.buynumber)
										this.$http({
											url: `${this.returnShForm.tablename}/update`,
											method: 'post',
											data: good
										}).then(obj=>{})
									}
									this.$http({
										url: `${this.returnShForm.role}/info/${this.returnShForm.userid}`,
										method: 'get',
									}).then(res1=>{
										if(res1.data&&res1.data.code==0){
											let user = res1.data.data
											// 如果是积分兑换 加回积分 
											if(this.returnShForm.type==2){
												user.jf = (Number(user.jf) + Number(this.returnShForm.total)).toFixed(2)
												this.$http({
													url: `${this.returnShForm.role}/update`,
													method: 'post',
													data: user
												}).then(obj=>{})
											}
											// 如果是其他形式 有积分删除积分。余额加回余额
											else{
												// 商品存在积分。加回积分
												if(good.jf){
													user.jf = (Number(user.jf) - Number(this.returnShForm.total)).toFixed(2)
												}
												if(user.jf<0) {
													user.jf = 0
												}
												// 加回余额
												user.money = (Number(user.money) + Number(this.returnShForm.total)).toFixed(2)
												this.$http({
													url: `${this.returnShForm.role}/update`,
													method: 'post',
													data: user
												}).then(obj=>{})
											}
											this.$http({
												url: 'orders/update',
												method: 'post',
												data: this.returnShForm
											}).then(res=>{
												if(res.data&&res.data.code==0){
													this.$message({
														message: "审核成功",
														type: "success",
														duration: 1500,
														onClose: () => {
															this.search()
															this.returnShVisible = false
														}
													})
												}
											})
										}
									})
								}
							})
						}else if(this.returnShForm.sfsh=='否'){
							this.$http({
								url: 'orders/update',
								method: 'post',
								data: this.returnShForm
							}).then(res=>{
								if(res.data&&res.data.code==0){
									this.$message({
										message: "审核成功",
										type: "success",
										duration: 1500,
										onClose: () => {
											this.search()
											this.returnShVisible = false
										}
									})
								}
							})
						}
					}
				})
			},
			dayNumberChartDialog() {
				this.dayNumberChartVisiable = !this.dayNumberChartVisiable;
				this.$nextTick(()=>{
					var dayNumberChart = echarts.init(document.getElementById("dayNumberChart"),'macarons');
					this.$http({
						url: `orders/value/addtime/buynumber/日`,
						method: "get"
					}).then(({ data }) => {
						if (data && data.code === 0) {
							let res = data.data;
							let xAxis = [];
							let yAxis = [];
							let pArray = []
							for(let i=0;i<res.length;i++){
								xAxis.push(res[i].addtime);
								yAxis.push(parseFloat(res[i].total));
								pArray.push({
									value: parseFloat((res[i].total)),
									name: res[i].addtime
								})
							}
							var option = {};
							let titleObj = this.bar.title;
							titleObj.text = '日销量';
							
							const legendObj = this.bar.legend
							let tooltipObj = {trigger: 'item',formatter: '{b} : {c}'}
							tooltipObj = Object.assign(tooltipObj , this.bar.tooltip?this.bar.tooltip:{})
							
							let xAxisObj = this.bar.xAxis
							xAxisObj.type = 'category'
							xAxisObj.data = xAxis
							
							let yAxisObj = this.bar.yAxis
							yAxisObj.type = 'value'
							
							let seriesObj = {
								data: yAxis,
								type: 'bar'
							}
							seriesObj = Object.assign(seriesObj , this.bar.series)
							const gridObj = this.bar.grid
							
							option = {
								backgroundColor: this.bar.backgroundColor,
								color: this.bar.color,
								tooltip: tooltipObj,
								legend: legendObj,
								title: titleObj,
								xAxis: xAxisObj,
								yAxis: yAxisObj,
								series: [seriesObj],
								grid: gridObj
							};
							// 使用刚指定的配置项和数据显示图表。
							dayNumberChart.setOption(option);
							//根据窗口的大小变动图表
							window.onresize = function() {
								dayNumberChart.resize();
							};
						}else{
							this.$message({
								message: data.msg,
								type: "warning",
								duration: 1500,
							})
						}
					});
				})
			},
	
			monthNumberChartDialog() {
				this.monthNumberChartVisiable = !this.monthNumberChartVisiable;
				this.$nextTick(()=>{
					var monthNumberChart = echarts.init(document.getElementById("monthNumberChart"),'macarons');
					this.$http({
						url: `orders/value/addtime/buynumber/月`,
						method: "get"
					}).then(({ data }) => {
						if (data && data.code === 0) {
							let res = data.data;
							let xAxis = [];
							let yAxis = [];
							let pArray = []
							for(let i=0;i<res.length;i++){
								xAxis.push(res[i].addtime);
								yAxis.push(parseFloat(res[i].total));
								pArray.push({
									value: parseFloat((res[i].total)),
									name: res[i].addtime
								})
							}
							var option = {};
							
							let titleObj = this.bar.title
							titleObj.text = '月销量'
							
							const legendObj = this.bar.legend
							let tooltipObj = {trigger: 'item',formatter: '{b} : {c}'}
							tooltipObj = Object.assign(tooltipObj , this.bar.tooltip?this.bar.tooltip:{})
							
							let xAxisObj = this.bar.xAxis
							xAxisObj.type = 'value'
							
							let yAxisObj = this.bar.yAxis
							yAxisObj.type = 'category'
							yAxisObj.data = xAxis
							
							let seriesObj = {
								data: yAxis,
								type: 'bar'
							}
							seriesObj = Object.assign(seriesObj , this.bar.series)
							const gridObj = this.bar.grid
							
							option = {
								backgroundColor: this.bar.backgroundColor,
								color: this.bar.color,
								tooltip: tooltipObj,
								legend: legendObj,
								title: titleObj,
								xAxis: xAxisObj,
								yAxis: yAxisObj,
								series: [seriesObj],
								grid: gridObj,
							};
							// 使用刚指定的配置项和数据显示图表。
							monthNumberChart.setOption(option);
							//根据窗口的大小变动图表
							window.onresize = function() {
								monthNumberChart.resize();
							};
						}else{
							this.$message({
								message: data.msg,
								type: "warning",
								duration: 1500,
							})
						}
					});
				})
			},
	
			yearNumberChartDialog() {
				this.yearNumberChartVisiable = !this.yearNumberChartVisiable;
				this.$nextTick(()=>{
					var yearNumberChart = echarts.init(document.getElementById("yearNumberChart"),'macarons');
					this.$http({
						url: `orders/value/addtime/buynumber/年`,
						method: "get"
					}).then(({ data }) => {
						if (data && data.code === 0) {
							let res = data.data;
							let xAxis = [];
							let yAxis = [];
							let pArray = []
							for(let i=0;i<res.length;i++){
								xAxis.push(res[i].addtime);
								yAxis.push(parseFloat(res[i].total));
								pArray.push({
									value: parseFloat((res[i].total)),
									name: res[i].addtime
								})
							}
							var option = {};
							
							let titleObj = this.line.title
							titleObj.text = '年销量'
							
							const legendObj = this.line.legend
							let tooltipObj = { trigger: 'item',formatter: '{b} : {c}'}
							tooltipObj = Object.assign(tooltipObj , this.line.tooltip?this.line.tooltip:{})
							
							let xAxisObj = this.line.xAxis
							xAxisObj.type = 'category'
							xAxisObj.boundaryGap = false
							xAxisObj.data = xAxis
							
							let yAxisObj = this.line.yAxis
							yAxisObj.type = 'value'
							const gridObj = this.line.grid
							let seriesObj = {
								data: yAxis,
								type: 'line',
								smooth: true
							}
							seriesObj = Object.assign(seriesObj , this.line.series)
							option = {
								backgroundColor: this.line.backgroundColor,
								color: this.line.color,
								title: titleObj,
								legend: legendObj,
								grid: gridObj,
								tooltip: tooltipObj,
								xAxis: xAxisObj,
								yAxis: yAxisObj,
								series: [seriesObj]
							};
							
							// 使用刚指定的配置项和数据显示图表。
							yearNumberChart.setOption(option);
							//根据窗口的大小变动图表
							window.onresize = function() {
								yearNumberChart.resize();
							};
						}else{
							this.$message({
								message: data.msg,
								type: "warning",
								duration: 1500,
							})
						}
					});
				})
			},
	
			goodnameNumberChartDialog() {
				this.goodnameNumberChartVisiable = !this.goodnameNumberChartVisiable;
				this.$nextTick(()=>{
					var goodnameNumberChart = echarts.init(document.getElementById("goodnameNumberChart"),'macarons');
					this.$http({
						url: `orders/value/goodname/buynumber`,
						method: "get"
					}).then(({ data }) => {
						if (data && data.code === 0) {
							let res = data.data;
							let xAxis = [];
							let yAxis = [];
							let pArray = []
							for(let i=0;i<res.length;i++){
								xAxis.push(res[i].goodname);
								yAxis.push(parseFloat(res[i].total));
								pArray.push({
									value: parseFloat((res[i].total)),
									name: res[i].goodname
								})
							}
							var option = {};
							
							let titleObj = this.funnel.title
							titleObj.text = '商品销量'
							
							let legendObj = {
								data: xAxis,
							}
							legendObj = Object.assign(legendObj , this.funnel.legend)
							let tooltipObj = {trigger: 'item',formatter: '{b} : {c}'}
							tooltipObj = Object.assign(tooltipObj , this.funnel.tooltip?this.funnel.tooltip:{})
							let seriesObj = {
								name: '商品销量',
								data: pArray,
								type: 'funnel',
								left: '10%',
								top: 60,
								bottom: 60,
								width: '80%',
								minSize: '0%',
								maxSize: '100%',
							}
							seriesObj = Object.assign(seriesObj , this.funnel.series)
							const gridObj = this.funnel.grid
							option = {
								backgroundColor: this.funnel.backgroundColor,
								color: this.funnel.color,
								title: titleObj,
								legend: legendObj,
								tooltip: tooltipObj,
								series: seriesObj,
								grid: gridObj
							}
							
							// 使用刚指定的配置项和数据显示图表。
							goodnameNumberChart.setOption(option);
							//根据窗口的大小变动图表
							window.onresize = function() {
								goodnameNumberChart.resize();
							};
						}else{
							this.$message({
								message: data.msg,
								type: "warning",
								duration: 1500,
							})
						}
					});
				})
			},
	
			goodtypeNumberChartDialog() {
				this.goodtypeNumberChartVisiable = !this.goodtypeNumberChartVisiable;
				this.$nextTick(()=>{
					var goodtypeNumberChart = echarts.init(document.getElementById("goodtypeNumberChart"),'macarons');
					this.$http({
						url: `orders/value/goodtype/buynumber`,
						method: "get"
					}).then(({ data }) => {
						if (data && data.code === 0) {
							let res = data.data;
							let xAxis = [];
							let yAxis = [];
							let pArray = []
							for(let i=0;i<res.length;i++){
								xAxis.push(res[i].goodtype);
								yAxis.push(parseFloat(res[i].total));
								pArray.push({
									value: parseFloat((res[i].total)),
									name: res[i].goodtype
								})
							}
							var option = {};
							
							let titleObj = this.pie.title
							titleObj.text = '类型销量'
							
							const legendObj = this.pie.legend
							let tooltipObj = {trigger: 'item',formatter: '{b} : {c} ({d}%)'}
							tooltipObj = Object.assign(tooltipObj , this.pie.tooltip?this.pie.tooltip:{})
							
							let seriesObj = {
								type: 'pie',
								radius: ['25%', '55%'],
								center: ['50%', '60%'],
								data: pArray,
								emphasis: {
									itemStyle: {
										shadowBlur: 10,
										shadowOffsetX: 0,
										shadowColor: 'rgba(0, 0, 0, 0.5)'
									}
								}
							}
							seriesObj = Object.assign(seriesObj , this.pie.series)
							const gridObj = this.pie.grid
							option = {
								backgroundColor: this.pie.backgroundColor,
								color: this.pie.color,
								tooltip: tooltipObj,
								title: titleObj,
								legend: legendObj,
								series: [seriesObj],
								grid: gridObj
							};
							// 使用刚指定的配置项和数据显示图表。
							goodtypeNumberChart.setOption(option);
							//根据窗口的大小变动图表
							window.onresize = function() {
								goodtypeNumberChart.resize();
							};
						}else{
							this.$message({
								message: data.msg,
								type: "warning",
								duration: 1500,
							})
						}
					});
				})
			},
	
			dayAmountChartDialog() {
				this.dayAmountChartVisiable = !this.dayAmountChartVisiable;
				this.$nextTick(()=>{
					var dayAmountChart = echarts.init(document.getElementById("dayAmountChart"),'macarons');
					this.$http({
						url: `orders/value/addtime/total/日`,
						method: "get"
					}).then(({ data }) => {
						if (data && data.code === 0) {
							let res = data.data;
							let xAxis = [];
							let yAxis = [];
							let pArray = []
							for(let i=0;i<res.length;i++){
								xAxis.push(res[i].addtime);
								yAxis.push(parseFloat((res[i].total)).toFixed(2));
								pArray.push({
									value: parseFloat((res[i].total)),
									name: res[i].addtime
								})
							}
							var option = {};
							
							let titleObj = this.bar.title
							titleObj.text = '日销额'
							
							const legendObj = this.bar.legend
							let tooltipObj = {trigger: 'axis'}
							tooltipObj = Object.assign(tooltipObj , this.bar.tooltip?this.bar.tooltip:{})
							
							let xAxisObj = this.bar.xAxis
							xAxisObj.type = 'category'
							xAxisObj.data = xAxis
							
							let yAxisObj = this.bar.yAxis
							yAxisObj.type = 'value'
							
							let seriesObj = {
								data: yAxis,
								type: 'bar'
							}
							seriesObj = Object.assign(seriesObj , this.bar.series)
							const gridObj = this.bar.grid
							
							option = {
								title: titleObj,
								legend: legendObj,
								tooltip: tooltipObj,
								xAxis: xAxisObj,
								yAxis: yAxisObj,
								series: [seriesObj],
								grid: gridObj,
							};
							// 使用刚指定的配置项和数据显示图表。
							dayAmountChart.setOption(option);
							//根据窗口的大小变动图表
							window.onresize = function() {
								dayAmountChart.resize();
							};
						}else{
							this.$message({
								message: data.msg,
								type: "warning",
								duration: 1500,
							})
						}
					});
				})
			},
	
			monthAmountChartDialog() {
				this.monthAmountChartVisiable = !this.monthAmountChartVisiable;
				this.$nextTick(()=>{
					var monthAmountChart = echarts.init(document.getElementById("monthAmountChart"),'macarons');
					this.$http({
						url: `orders/value/addtime/total/月`,
						method: "get"
					}).then(({ data }) => {
						if (data && data.code === 0) {
							let res = data.data;
							let xAxis = [];
							let yAxis = [];
							let pArray = []
							for(let i=0;i<res.length;i++){
								xAxis.push(res[i].addtime);
								yAxis.push(parseFloat((res[i].total)).toFixed(2));
								pArray.push({
									value: parseFloat((res[i].total)),
									name: res[i].addtime
								})
							}
							var option = {};
							
							let titleObj = this.bar.title
							titleObj.text = '月销额'
							
							const legendObj = this.bar.legend
							let tooltipObj = {trigger: 'item',formatter: '{b} : {c}'}
							tooltipObj = Object.assign(tooltipObj , this.bar.tooltip?this.bar.tooltip:{})
							
							let xAxisObj = this.bar.xAxis
							xAxisObj.type = 'value'
							
							let yAxisObj = this.bar.yAxis
							yAxisObj.type = 'category'
							yAxisObj.data = xAxis
							
							let seriesObj = {
								data: yAxis,
								type: 'bar'
							}
							seriesObj = Object.assign(seriesObj , this.bar.series)
							const gridObj = this.bar.grid
							option = {
								legend: legendObj,
								tooltip: tooltipObj,
								title: titleObj,
								xAxis: xAxisObj,
								yAxis: yAxisObj,
								series: [seriesObj],
								grid: gridObj
							};
							// 使用刚指定的配置项和数据显示图表。
							monthAmountChart.setOption(option);
							//根据窗口的大小变动图表
							window.onresize = function() {
								monthAmountChart.resize();
							};
						}else{
							this.$message({
								message: data.msg,
								type: "warning",
								duration: 1500,
							})
						}
					});
				})
			},
	
			yearAmountChartDialog() {
				this.yearAmountChartVisiable = !this.yearAmountChartVisiable;
				this.$nextTick(()=>{
					var yearAmountChart = echarts.init(document.getElementById("yearAmountChart"),'macarons');
					this.$http({
						url: `orders/value/addtime/total/年`,
						method: "get"
					}).then(({ data }) => {
						if (data && data.code === 0) {
							let res = data.data;
							let xAxis = [];
							let yAxis = [];
							let pArray = []
							for(let i=0;i<res.length;i++){
								xAxis.push(res[i].addtime);
								yAxis.push(parseFloat((res[i].total)).toFixed(2));
								pArray.push({
									value: parseFloat((res[i].total)),
									name: res[i].addtime
								})
							}
							var option = {};
							
							let titleObj = this.line.title
							titleObj.text = '年销额'
							
							const legendObj = this.line.legend
							let tooltipObj = { trigger: 'item',formatter: '{b} : {c}'}
							tooltipObj = Object.assign(tooltipObj , this.line.tooltip?this.line.tooltip:{})
							
							let xAxisObj = this.line.xAxis
							xAxisObj.type = 'category'
							xAxisObj.boundaryGap = false
							xAxisObj.data = xAxis
							
							let yAxisObj = this.line.yAxis
							yAxisObj.type = 'value'
							const gridObj = this.line.grid
							
							let seriesObj = {
								data: yAxis,
								type: 'line',
								smooth: true
							}
							seriesObj = Object.assign(seriesObj , this.line.series)
							option = {
								backgroundColor: this.line.backgroundColor,
								color: this.line.color,
								title: titleObj,
								legend: legendObj,
								grid: gridObj,
								tooltip: tooltipObj,
								xAxis: xAxisObj,
								yAxis: yAxisObj,
								series: [seriesObj]
							};
							// 使用刚指定的配置项和数据显示图表。
							yearAmountChart.setOption(option);
							//根据窗口的大小变动图表
							window.onresize = function() {
								yearAmountChart.resize();
							};
						}else{
							this.$message({
								message: data.msg,
								type: "warning",
								duration: 1500,
							})
						}
					});
				})
			},
	
			goodnameAmountChartDialog() {
				this.goodnameAmountChartVisiable = !this.goodnameAmountChartVisiable;
				this.$nextTick(()=>{
					var goodnameAmountChart = echarts.init(document.getElementById("goodnameAmountChart"),'macarons');
					this.$http({
						url: `orders/value/goodname/total`,
						method: "get"
					}).then(({ data }) => {
						if (data && data.code === 0) {
							let res = data.data;
							let xAxis = [];
							let yAxis = [];
							let pArray = []
							for(let i=0;i<res.length;i++){
								xAxis.push(res[i].goodname);
								yAxis.push(parseFloat((res[i].total)).toFixed(2));
								pArray.push({
									value: parseFloat((res[i].total)),
									name: res[i].goodname
								})
							}
							var option = {};
							
							let titleObj = this.funnel.title
							titleObj.text = '商品销额'
							
							let legendObj = {
								data: xAxis,
							}
							legendObj = Object.assign(legendObj , this.funnel.legend)
							let tooltipObj = {trigger: 'item',formatter: '{b} : {c}'}
							tooltipObj = Object.assign(tooltipObj , this.funnel.tooltip?this.funnel.tooltip:{})
							let seriesObj = {
								name: '商品销额',
								data: pArray,
								type: 'funnel',
								left: '10%',
								top: 60,
								bottom: 60,
								width: '80%',
								minSize: '0%',
								maxSize: '100%',
							}
							seriesObj = Object.assign(seriesObj , this.funnel.series)
							const gridObj = this.funnel.grid
							option = {
								backgroundColor: this.funnel.backgroundColor,
								color: this.funnel.color,
								title: titleObj,
								legend: legendObj,
								tooltip: tooltipObj,
								series: seriesObj,
								grid: gridObj
							}
							// 使用刚指定的配置项和数据显示图表。
							goodnameAmountChart.setOption(option);
							//根据窗口的大小变动图表
							window.onresize = function() {
								goodnameAmountChart.resize();
							};
						}else{
							this.$message({
								message: data.msg,
								type: "warning",
								duration: 1500,
							})
						}
					});
				})
			},
	
			goodtypeAmountChartDialog() {
				this.goodtypeAmountChartVisiable = !this.goodtypeAmountChartVisiable;
				this.$nextTick(()=>{
					var goodtypeAmountChart = echarts.init(document.getElementById("goodtypeAmountChart"),'macarons');
					this.$http({
						url: `orders/value/goodtype/total`,
						method: "get"
					}).then(({ data }) => {
						if (data && data.code === 0) {
							let res = data.data;
							let xAxis = [];
							let yAxis = [];
							let pArray = []
							for(let i=0;i<res.length;i++){
								xAxis.push(res[i].goodtype);
								yAxis.push(parseFloat((res[i].total)).toFixed(2));
								pArray.push({
									value: parseFloat((res[i].total)),
									name: res[i].goodtype
								})
							}
							var option = {};
							
							let titleObj = this.pie.title
							titleObj.text = '类型销额'
							
							const legendObj = this.pie.legend
							let tooltipObj = {trigger: 'item',formatter: '{b} : {c} ({d}%)'}
							tooltipObj = Object.assign(tooltipObj , this.pie.tooltip?this.pie.tooltip:{})
							
							let seriesObj = {
								type: 'pie',
								radius: ['25%', '55%'],
								center: ['50%', '60%'],
								data: pArray,
								emphasis: {
									itemStyle: {
										shadowBlur: 10,
										shadowOffsetX: 0,
										shadowColor: 'rgba(0, 0, 0, 0.5)'
									}
								}
							}
							seriesObj = Object.assign(seriesObj , this.pie.series)
							const gridObj = this.pie.grid
							option = {
								backgroundColor: this.pie.backgroundColor,
								color: this.pie.color,
								tooltip: tooltipObj,
								title: titleObj,
								legend: legendObj,
								series: [seriesObj],
								grid: gridObj
							};
							// 使用刚指定的配置项和数据显示图表。
							goodtypeAmountChart.setOption(option);
							//根据窗口的大小变动图表
							window.onresize = function() {
								goodtypeAmountChart.resize();
							};
						}else{
							this.$message({
								message: data.msg,
								type: "warning",
								duration: 1500,
							})
						}
					});
				})
			},





			init () {
			},
			search() {
				this.pageIndex = 1;
				this.getDataList();
			},

			// 获取数据列表
			getDataList() {
				this.dataListLoading = true;
				let params = {
					page: this.pageIndex,
					limit: this.pageSize,
					sort: 'id',
					order: 'desc',
					status: this.$route.params.status,
				}
 				if(this.searchForm.orderid!='' && this.searchForm.orderid!=undefined){
					params['orderid'] = '%' + this.searchForm.orderid + '%'
				}
 				if(this.searchForm.goodname!='' && this.searchForm.goodname!=undefined){
					params['goodname'] = '%' + this.searchForm.goodname + '%'
				}
				if(this.searchForm.type!='' && this.searchForm.type!=undefined){
					params['type'] = this.searchForm.type
				}
				if(this.searchForm.status!='' && this.searchForm.status!=undefined){
					params['status'] = this.searchForm.status
				}
				this.$http({
					url: "orders/page",
					method: "get",
					params: params
				}).then(({ data }) => {
					if (data && data.code === 0) {
						this.dataList = data.data.list;
						this.totalPage = data.data.total;
					} else {
						this.dataList = [];
						this.totalPage = 0;
					}
					this.dataListLoading = false;
				});
			},
			// 每页数
			sizeChangeHandle(val) {
				this.pageSize = val;
				this.pageIndex = 1;
				this.getDataList();
			},
			// 当前页
			currentChangeHandle(val) {
				this.pageIndex = val;
				this.getDataList();
			},
			// 多选
			selectionChangeHandler(val) {
				this.dataListSelections = val;
			},
			// 添加/修改
			addOrUpdateHandler(id,type) {
				this.showFlag = false;
				this.addOrUpdateFlag = true;
				this.crossAddOrUpdateFlag = false;
				if(type!='info'&&type!='msg'){
					type = 'else';
				}
				this.$nextTick(() => {
					this.$refs.addOrUpdate.init(id,type);
				});
			},
			// 查看评论
			disscussListHandler(id,type) {
				this.$router.push({path:'/discussorders',query:{refid:id}});
			},
			// 删除
			async deleteHandler(id ) {
				var ids = id? [Number(id)]: this.dataListSelections.map(item => {
					return Number(item.id);
				});
				await this.$confirm(`确定进行[${id ? "删除" : "批量删除"}]操作?`, "提示", {
					confirmButtonText: "确定",
					cancelButtonText: "取消",
					type: "warning"
				}).then(async () => {
					await this.$http({
						url: "orders/delete",
						method: "post",
						data: ids
					}).then(async ({ data }) => {
						if (data && data.code === 0) {
							this.$message({
								message: "操作成功",
								type: "success",
								duration: 1500,
								onClose: () => {
									this.search();
								}
							});
			
						} else {
							this.$message.error(data.msg);
						}
					});
				});
			},


		}

	};
</script>
<style lang="scss" scoped>
	//导出excel
	.export-excel-wrapper{
		display: inline-block;
	}
	
	.center-form-pv {
		.el-date-editor.el-input {
			width: auto;
		}
	}
	
	.el-input {
		width: auto;
	}
	
	// form
	.center-form-pv .el-input {
		width: 100%;
	}
	.center-form-pv .el-input ::v-deep .el-input__inner {
		border: 1px solid #E8E8E8;
		border-radius: 0px;
		padding: 0 12px;
		color: #666;
		width: 170px;
		font-size: 16px;
		height: 40px;
	}
	.center-form-pv .el-select {
		width: 100%;
	}
	.center-form-pv .el-select ::v-deep .el-input__inner {
		border: 1px solid #E8E8E8;
		border-radius: 0px;
		padding: 0 10px;
		color: #666;
		width: 170px;
		font-size: 16px;
		height: 40px;
	}
	.center-form-pv .el-date-editor {
		width: 100%;
	}
	
	.center-form-pv .el-date-editor ::v-deep .el-input__inner {
		border: 1px solid #E8E8E8;
		border-radius: 0px;
		padding: 0 10px 0 30px;
		color: #666;
		width: 170px;
		font-size: 16px;
		height: 40px;
	}
	
	.center-form-pv .search {
		border: 0;
		cursor: pointer;
		border-radius: 5px;
		padding: 0 15px;
		color: #fff;
		background: #179BD7;
		width: auto;
		font-size: 16px;
		height: 40px;
	}
	
	.center-form-pv .search:hover {
		opacity: 0.8;
	}
	
	.center-form-pv .actions .add {
		border: 1px solid #179BD7;
		cursor: pointer;
		border-radius: 5px;
		padding: 0 15px;
		margin: 4px;
		color: #fff;
		background: #179BD7;
		width: auto;
		font-size: inherit;
		height: 38px;
	}
	
	.center-form-pv .actions .add:hover {
		color: #aaa;
		opacity: 1;
	}
	
	.center-form-pv .actions .del {
		border: 1px solid #179BD7;
		cursor: pointer;
		border-radius: 5px;
		padding: 0 15px;
		margin: 4px;
		color: #179BD7;
		background: #fff;
		width: auto;
		font-size: inherit;
		height: 38px;
	}
	
	.center-form-pv .actions .del:hover {
		color: #AAA;
		opacity: 1;
	}
	
	.center-form-pv .actions .statis {
		border: 1px solid #179BD7;
		cursor: pointer;
		border-radius: 5px;
		padding: 0 15px;
		margin: 4px;
		color: #179BD7;
		background: #fff;
		width: auto;
		font-size: inherit;
		height: 38px;
	}
	
	.center-form-pv .actions .statis:hover {
		color: #AAA;
		opacity: 1;
	}
	
	.center-form-pv .actions .btn18 {
		border: 1px solid #179BD7;
		cursor: pointer;
		border-radius: 5px;
		padding: 0 15px;
		margin: 4px;
		color: #179BD7;
		background: #fff;
		width: auto;
		font-size: inherit;
		height: 38px;
	}
	
	.center-form-pv .actions .btn18:hover {
		color: #AAA;
		opacity: 1;
	}
	
	// table
	.el-table ::v-deep .el-table__header-wrapper thead {
		color: #999;
		font-weight: 500;
		width: 100%;
	}
	
	.el-table ::v-deep .el-table__header-wrapper thead tr {
		background: #179BD7;
	}
	
	.el-table ::v-deep .el-table__header-wrapper thead tr th {
		padding: 10px 0;
		background: none;
		border-color: #eee;
		border-width: 0 1px 1px 0;
		border-style: solid;
		text-align: left;
	}

	.el-table ::v-deep .el-table__header-wrapper thead tr th .cell {
		padding: 0 0 0 5px;
		word-wrap: normal;
		color: #fff;
		white-space: normal;
		font-weight: bold;
		display: flex;
		vertical-align: middle;
		font-size: 14px;
		line-height: 24px;
		text-overflow: ellipsis;
		word-break: break-all;
		width: 100%;
		align-items: center;
		position: relative;
		min-width: 110px;
	}

	
	.el-table ::v-deep .el-table__body-wrapper tbody {
		width: 100%;
	}

	.el-table ::v-deep .el-table__body-wrapper tbody tr {
		background: #fff;
	}
	
	.el-table ::v-deep .el-table__body-wrapper tbody tr td {
		padding: 4px 0;
		color: #333;
		background: #fff;
		font-size: inherit;
		border-color: #9E9E9E;
		border-width: 0 0px 1px 0;
		border-style: solid;
		text-align: left;
	}
	
		
	.el-table ::v-deep .el-table__body-wrapper tbody tr:hover td {
		padding: 4px 0;
		color: #333;
		background: #f0f0f0;
		border-color: #9E9E9E;
		border-width: 0 0px 1px 0;
		border-style: solid;
		text-align: left;
	}
	
	.el-table ::v-deep .el-table__body-wrapper tbody tr td {
		padding: 4px 0;
		color: #333;
		background: #fff;
		font-size: inherit;
		border-color: #9E9E9E;
		border-width: 0 0px 1px 0;
		border-style: solid;
		text-align: left;
	}

	.el-table ::v-deep .el-table__body-wrapper tbody tr td .cell {
		padding: 0 0 0 5px;
		overflow: hidden;
		word-break: break-all;
		white-space: normal;
		font-size: inherit;
		line-height: 24px;
		text-overflow: ellipsis;
	}
	
	.el-table ::v-deep .el-table__body-wrapper tbody tr td .view {
		border: 1px solid #179BD7;
		cursor: pointer;
		border-radius: 5px;
		padding: 0 15px;
		box-shadow: 4px 4px 0 #179BD7;
		margin: 0 10px 10px 0;
		transform: skew(-15deg, 0deg);
		color: #179BD7;
		background: #fff;
		width: auto;
		font-size: 14px;
		height: 36px;
	}
	
	.el-table ::v-deep .el-table__body-wrapper tbody tr td .view:hover {
		color: #aaa;
		opacity: 1;
	}
	
	.el-table ::v-deep .el-table__body-wrapper tbody tr td .add {
	}
	
	.el-table ::v-deep .el-table__body-wrapper tbody tr td .add:hover {
	}
	
	.el-table ::v-deep .el-table__body-wrapper tbody tr td .edit {
		border: 1px solid #43B27F;
		cursor: pointer;
		border-radius: 5px;
		padding: 0 15px;
		box-shadow: 4px 4px 0 #43B27F;
		margin: 0 10px 10px 0;
		transform: skew(-15deg, 0deg);
		color: #43B27F;
		background: #fff;
		width: auto;
		font-size: 14px;
		height: 36px;
	}
	
	.el-table ::v-deep .el-table__body-wrapper tbody tr td .edit:hover {
		color: #aaa;
		opacity: 1;
	}
	
	.el-table ::v-deep .el-table__body-wrapper tbody tr td .del {
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
	}
	
	.el-table ::v-deep .el-table__body-wrapper tbody tr td .del:hover {
		color: #aaa;
		opacity: 1;
	}
	
	.el-table ::v-deep .el-table__body-wrapper tbody tr td .btn8 {
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
	}
	
	.el-table ::v-deep .el-table__body-wrapper tbody tr td .btn8:hover {
		opacity: 0.8;
	}
	
	// pagination
	.main-content .el-pagination ::v-deep .el-pagination__total {
		margin: 0 10px 0 0;
		color: #666;
		font-weight: 400;
		display: inline-block;
		vertical-align: top;
		font-size: inherit;
		line-height: 48px;
		height: 48px;
	}
	
	.main-content .el-pagination ::v-deep .btn-prev {
		border: 1px solid #c2c2c2;
		border-radius: 2px;
		padding: 0 20px;
		margin: 0;
		color: #fff;
		background: #179BD7;
		display: inline-block;
		vertical-align: top;
		font-size: 20px;
		line-height: auto;
		min-width: 35px;
		height: 60px;
	}
	
	.main-content .el-pagination ::v-deep .btn-next {
		border: 1px solid #c2c2c2;
		border-radius: 2px;
		padding: 0 20px;
		margin: 0;
		color: #fff;
		background: #179BD7;
		display: inline-block;
		vertical-align: top;
		font-size: 20px;
		line-height: auto;
		min-width: 35px;
		height: 60px;
	}
	
	.main-content .el-pagination ::v-deep .btn-prev:disabled {
		border: 1px solid #c2c2c2;
		cursor: not-allowed;
		border-radius: 0;
		padding: 0 20px;
		margin: 0;
		color: #fff;
		background: #179BD7;
		display: inline-block;
		vertical-align: top;
		font-size: 20px;
		line-height: auto;
		height: 60px;
	}
	
	.main-content .el-pagination ::v-deep .btn-next:disabled {
		border: 1px solid #c2c2c2;
		cursor: not-allowed;
		border-radius: 0;
		padding: 0 20px;
		margin: 0;
		color: #fff;
		background: #179BD7;
		display: inline-block;
		vertical-align: top;
		font-size: 20px;
		line-height: auto;
		height: 60px;
	}

	.main-content .el-pagination ::v-deep .el-pager {
		padding: 0;
		margin: 0 10px;
		display: flex;
		vertical-align: top;
		align-items: center;
	}

	.main-content .el-pagination ::v-deep .el-pager .number {
		cursor: pointer;
		border-radius: 2px;
		padding: 0 15px;
		margin: 0 10px;
		color: #666;
		background: none;
		display: inline-block;
		vertical-align: top;
		font-size: 16px;
		line-height: 40px;
		text-align: center;
		height: 40px;
	}
	
	.main-content .el-pagination ::v-deep .el-pager .number:hover {
		cursor: pointer;
		border-radius: 50%;
		padding: 0 15px;
		margin: 0 10px;
		color: #fff;
		background: #179BD7;
		display: inline-block;
		vertical-align: top;
		font-size: 16px;
		line-height: 40px;
		text-align: center;
		height: 40px;
	}
	
	.main-content .el-pagination ::v-deep .el-pager .number.active {
		cursor: pointer;
		border-radius: 50%;
		padding: 0 15px;
		margin: 0 10px;
		color: #fff;
		background: #179BD7;
		display: inline-block;
		vertical-align: top;
		font-size: 16px;
		line-height: 40px;
		text-align: center;
		height: 40px;
	}
	
	.main-content .el-pagination ::v-deep .el-pagination__sizes {
		display: inline-block;
		vertical-align: top;
		font-size: 16px;
		line-height: 48px;
		height: 48px;
	}
	
	.main-content .el-pagination ::v-deep .el-pagination__sizes .el-input {
		margin: 0 5px;
		width: 100px;
		position: relative;
	}
	
	.main-content .el-pagination ::v-deep .el-pagination__sizes .el-input .el-input__inner {
		border: 1px solid #DCDFE6;
		cursor: pointer;
		padding: 0 25px 0 8px;
		color: #606266;
		display: inline-block;
		font-size: 16px;
		line-height: 28px;
		border-radius: 3px;
		outline: 0;
		background: #FFF;
		width: 100%;
		text-align: center;
		height: 28px;
	}
	
	.main-content .el-pagination ::v-deep .el-pagination__sizes .el-input span.el-input__suffix {
		top: 0;
		position: absolute;
		right: 0;
		height: 100%;
	}
	
	.main-content .el-pagination ::v-deep .el-pagination__sizes .el-input .el-input__suffix .el-select__caret {
		cursor: pointer;
		color: #C0C4CC;
		width: 25px;
		font-size: 14px;
		line-height: 48px;
		text-align: center;
	}
	
	.main-content .el-pagination ::v-deep .el-pagination__jump {
		margin: 0 0 0 24px;
		color: #606266;
		display: inline-block;
		vertical-align: top;
		font-size: 16px;
		line-height: 48px;
		height: 48px;
	}
	
	.main-content .el-pagination ::v-deep .el-pagination__jump .el-input {
		border-radius: 3px;
		padding: 0 2px;
		margin: 0 2px;
		display: inline-block;
		width: 50px;
		font-size: 14px;
		line-height: 18px;
		position: relative;
		text-align: center;
		height: 28px;
	}
	
	.main-content .el-pagination ::v-deep .el-pagination__jump .el-input .el-input__inner {
		border: 1px solid #DCDFE6;
		cursor: pointer;
		padding: 0 3px;
		color: #606266;
		display: inline-block;
		font-size: 16px;
		line-height: 28px;
		border-radius: 3px;
		outline: 0;
		background: #FFF;
		width: 100%;
		text-align: center;
		height: 28px;
	}
	
	// list one
	.one .list1-view {
		border: 1px solid #179BD7;
		cursor: pointer;
		border-radius: 5px;
		padding: 0 15px;
		box-shadow: 4px 4px 0 #179BD7;
		margin: 0 10px 10px 0;
		transform: skew(-15deg, 0deg);
		color: #179BD7;
		background: #fff;
		width: auto;
		font-size: 14px;
		height: 36px;
	}
	
	.one .list1-view:hover {
		box-shadow: 4px 4px 0 #aaa;
		color: #aaa;
		border-color: #aaa;
		opacity: 1;
	}
	
	.one .list1-edit {
		border: 1px solid #43B27F;
		cursor: pointer;
		border-radius: 5px;
		padding: 0 15px;
		box-shadow: 4px 4px 0 #43B27F;
		margin: 0 10px 10px 0;
		transform: skew(-15deg, 0deg);
		color: #43B27F;
		background: #fff;
		width: auto;
		font-size: 14px;
		height: 36px;
	}
	
	.one .list1-edit:hover {
		box-shadow: 4px 4px 0 #aaa;
		color: #aaa;
		border-color: #aaa;
		opacity: 1;
	}
	
	.one .list1-del {
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
	}
	
	.one .list1-del:hover {
		box-shadow: 4px 4px 0 #aaa;
		color: #aaa;
		border-color: #aaa;
		opacity: 1;
	}
	
	.one .list1-btn8 {
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
	}
	
	.one .list1-btn8:hover {
		box-shadow: 4px 4px 0 #aaa;
		color: #aaa;
		border-color: #aaa;
		opacity: 1;
	}
	
	.main-content .el-table .el-switch {
		display: inline-flex;
		vertical-align: middle;
		line-height: 30px;
		position: relative;
		align-items: center;
		height: 30px;
	}
	.main-content .el-table .el-switch ::v-deep .el-switch__label--left {
		cursor: pointer;
		margin: 0 10px 0 0;
		color: #333;
		font-weight: 500;
		display: inline-block;
		vertical-align: middle;
		font-size: 16px;
		transition: .2s;
		height: 30px;
	}
	.main-content .el-table .el-switch ::v-deep .el-switch__label--right {
		cursor: pointer;
		margin: 0 0 0 10px;
		color: #333;
		font-weight: 500;
		display: inline-block;
		vertical-align: middle;
		font-size: 16px;
		transition: .2s;
		height: 30px;
	}
	.main-content .el-table .el-switch ::v-deep .el-switch__core {
		border: 1px solid #75c0d6;
		cursor: pointer;
		border-radius: 15px;
		margin: 0;
		background: #75c0d6;
		display: inline-block;
		width: 42px;
		box-sizing: border-box;
		transition: border-color .3s,background-color .3s;
		height: 20px;
	}
	.main-content .el-table .el-switch ::v-deep .el-switch__core::after {
		border-radius: 100%;
		top: 1px;
		left: 1px;
		background: #fff;
		width: 16px;
		position: absolute;
		transition: all .3s;
		height: 16px;
	}
	.main-content .el-table .el-switch.is-checked ::v-deep .el-switch__core::after {
		margin: 0 0 0 -18px;
		left: 100%;
	}
	
	.main-content .el-table .el-rate ::v-deep .el-rate__item {
		cursor: pointer;
		display: inline-block;
		vertical-align: middle;
		font-size: 0;
		position: relative;
	}
	.main-content .el-table .el-rate ::v-deep .el-rate__item .el-rate__icon {
		margin: 0 3px;
		display: inline-block;
		font-size: 18px;
		position: relative;
		transition: .3s;
	}

</style>
