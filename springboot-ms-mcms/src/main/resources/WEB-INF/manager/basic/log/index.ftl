<!DOCTYPE html>
<html>
<head>
	<title>系统日志</title>
		<#include "../../include/head-file.ftl">
</head>
<body>
	<div id="index" class="ms-index" v-cloak>
		<ms-search ref="search" @search="search" :condition-data="conditionList" :conditions="conditions"></ms-search>
		<div class="ms-search">
			<el-row>
				<el-form :model="form"  ref="searchForm"  label-width="120px" size="mini">
							<el-row>
											<el-col :span="8">

        <el-form-item  label="标题" prop="logTitle">
                <el-input
                        v-model="form.logTitle"
                        :disabled="false"
                          :style="{width:  '100%'}"
                          :clearable="true"
                        placeholder="请输入标题">
                </el-input>
         </el-form-item>
											</el-col>
											<el-col :span="8">

        <el-form-item  label="请求地址" prop="logUrl">
                <el-input
                        v-model="form.logUrl"
                        :disabled="false"
                          :style="{width:  '100%'}"
                          :clearable="true"
                        placeholder="请输入请求地址">
                </el-input>
         </el-form-item>
											</el-col>
											<el-col :span="8">

        <el-form-item  label="请求状态" prop="logStatus">
                    <el-select  v-model="form.logStatus"
                               :style="{width: '100%'}"
                               :filterable="false"
                               :disabled="false"
                               :multiple="false" :clearable="true"
                            placeholder="请选择请求状态">
                        <el-option v-for='item in logStatusOptions' :key="item.value" :value="item.value"
                                   :label="item.label"></el-option>
                    </el-select>
         </el-form-item>
											</el-col>
							</el-row>
							<el-row>
											<el-col :span="8">

        <el-form-item  label="业务类型" prop="logBusinessType">
                    <el-select  v-model="form.logBusinessType"
                               :style="{width: '100%'}"
                               :filterable="false"
                               :disabled="false"
                               :multiple="false" :clearable="true"
                            placeholder="请选择业务类型">
                        <el-option v-for='item in logBusinessTypeOptions' :key="item.value" :value="item.value"
                                   :label="item.label"></el-option>
                    </el-select>
         </el-form-item>
											</el-col>
											<el-col :span="8">

        <el-form-item  label="操作人员" prop="logUser">
                <el-input
                        v-model="form.logUser"
                        :disabled="false"
                          :style="{width:  '100%'}"
                          :clearable="true"
                        placeholder="请输入操作人员">
                </el-input>
         </el-form-item>
											</el-col>
								<el-col :span="8">
									<el-form-item label="创建时间" prop="createDate">
										<el-date-picker
												v-model="form.createDateScope"
												value-format="yyyy-MM-dd HH:mm:ss"
												type="datetimerange"
												:style="{width:  '100%'}"
												start-placeholder="开始时间"
												end-placeholder="结束时间">
										</el-date-picker>
									</el-form-item>
								</el-col>
							</el-row>
							<el-row :style="{padding: '10px'}">
										<el-col push="16" :span="8" style="text-align: right;">
											<el-button type="primary" icon="el-icon-search" size="mini" @click="currentPage=1;list()">搜索</el-button>
											<el-button type="primary" icon="iconfont icon-shaixuan1" size="mini" @click="currentPage=1;$refs.search.open()">筛选</el-button>
											<el-button @click="rest"  icon="el-icon-refresh" size="mini">重置</el-button>
										</el-col>
							</el-row>
				</el-form>
			</el-row>
		</div>
		<el-main class="ms-container">
			<el-table height="calc(100vh - 68px)" v-loading="loading" ref="multipleTable" border :data="dataList" tooltip-effect="dark" @selection-change="handleSelectionChange">
				<template slot="empty">
					{{emptyText}}
				</template>
				<el-table-column type="selection" width="40"></el-table-column>
                <el-table-column label="标题" width="120"  align="left" prop="logTitle">
                </el-table-column>
                <el-table-column label="请求地址"   align="left" prop="logUrl">
                </el-table-column>
                <el-table-column label="请求状态"  width="80"   align="left" prop="logStatus" :formatter="logStatusFormat">
                </el-table-column>
                <el-table-column label="操作人员"  width="100"  align="left" prop="logUser">
                </el-table-column>
				<el-table-column label="创建时间"  width="180"  align="left" prop="createDate">
				</el-table-column>
				<el-table-column label="操作"  width="60" align="center">
					<template slot-scope="scope">
						<@shiro.hasPermission name="basic:log:view">
						<el-link type="primary" :underline="false" @click="save(scope.row.id)">详情</el-link>
						</@shiro.hasPermission>
					</template>
				</el-table-column>
			</el-table>
            <el-pagination
					background
					:page-sizes="[10,20,30,40,50,100]"
					layout="total, sizes, prev, pager, next, jumper"
					:current-page="currentPage"
					:page-size="pageSize"
					:total="total"
					class="ms-pagination"
					@current-change='currentChange'
					@size-change="sizeChange">
            </el-pagination>
         </el-main>
	</div>
</body>

</html>
<script>
var indexVue = new Vue({
	el: '#index',
	data:{
		conditionList:[
        {action:'and', field: 'log_title', el: 'eq', model: 'logTitle', name: '标题', type: 'input'},
         {action:'and', field: 'log_ip', el: 'eq', model: 'logIp', name: 'IP', type: 'input'},
         {action:'and', field: 'log_method', el: 'eq', model: 'logMethod', name: '请求方法', type: 'input'},
             {action:'and', field: 'log_request_method', el: 'eq', model: 'logRequestMethod', name: '请求方式', key: 'value', title: 'value', type: 'select', multiple: false},
         {action:'and', field: 'log_url', el: 'eq', model: 'logUrl', name: '请求地址', type: 'input'},
             {action:'and', field: 'log_status', el: 'eq', model: 'logStatus', name: '请求状态', key: 'value', title: 'label', type: 'select', multiple: false},
             {action:'and', field: 'log_business_type', el: 'eq', model: 'logBusinessType', name: '业务类型', key: 'value', title: 'label', type: 'select', multiple: false},
             {action:'and', field: 'log_user_type', el: 'eq', model: 'logUserType', name: '用户类型', key: 'value', title: 'label', type: 'select', multiple: false},
         {action:'and', field: 'log_user', el: 'eq', model: 'logUser', name: '操作人员', type: 'input'},
         {action:'and', field: 'log_location', el: 'eq', model: 'logLocation', name: '所在地区', type: 'input'},
         {action:'and', field: 'log_param', el: 'eq', model: 'logParam', name: '请求参数', type: 'textarea'},
         {action:'and', field: 'log_result', el: 'eq', model: 'logResult', name: '返回参数', type: 'textarea'},
         {action:'and', field: 'log_error_msg', el: 'eq', model: 'logErrorMsg', name: '错误消息', type: 'textarea'},
          {action:'and', field: 'create_date', el: 'eq', model: 'createDate', name: '创建时间', type: 'date'},
          {action:'and', field: 'update_date', el: 'eq', model: 'updateDate', name: '修改时间', type: 'date'},
 		],
		conditions:[],
		dataList: [], //系统日志列表
		selectionList:[],//系统日志列表选中
		total: 0, //总记录数量
        pageSize: 10, //页面数量
        currentPage:1, //初始页
        manager: ms.manager,
		loadState:false,
		loading: true,//加载状态
		emptyText:'',//提示文字
                logRequestMethodOptions:[{"value":"get"},{"value":"post"},{"value":"put"}],
                logStatusOptions:[{"value":"success","label":"成功"},{"value":"error","label":"失败"}],
                logBusinessTypeOptions:[{"value":"insert","label":"新增"},{"value":"delete","label":"删除"},{"value":"update","label":"修改"},{"value":"other","label":"其他"}],
                logUserTypeOptions:[{"value":"other","label":"其他"},{"value":"manage","label":"管理员"},{"value":"people","label":"会员"}],
		//搜索表单
		form:{
			sqlWhere:null,
			// 标题
			logTitle:null,
			// 请求地址
			logUrl:null,
			// 请求状态
			logStatus:null,
			// 业务类型
			logBusinessType:null,
			// 操作人员
			logUser:null,
			createDateScope:null,
		},
	},
	watch:{
     	},
	methods:{
	    //查询列表
	    list: function() {
	    	var that = this;
			that.loading = true;
			that.loadState = false;
			var page={
				pageNo: that.currentPage,
				pageSize : that.pageSize
			}
			var form = JSON.parse(JSON.stringify(that.form))
			for (key in form){
				if(!form[key]){
					delete  form[key]
				}
			}

			//处理时间范围
			if (form.createDateScope) {
				form.startTime = form.createDateScope[0];
				form.endTime = form.createDateScope[1];
			}

			history.replaceState({form:form,page:page},"");
			ms.http.post(ms.manager+"/basic/log/list.do",form.sqlWhere?Object.assign({},{sqlWhere:form.sqlWhere}, page)
				:Object.assign({},form, page)).then(
					function(res) {
						if(that.loadState){
							that.loading = false;
						}else {
							that.loadState = true
						}
						if (!res.result||res.data.total <= 0) {
							that.emptyText ="暂无数据"
							that.dataList = [];
							that.total = 0;
						} else {
							that.emptyText = '';
							that.total = res.data.total;
							that.dataList = res.data.rows;
						}
					});
			setTimeout(function(){
				if(that.loadState){
					that.loading = false;
				}else {
					that.loadState = true
				}
			}, 500);
			},
		//系统日志列表选中
		handleSelectionChange:function(val){
			this.selectionList = val;
		},

		//新增
        save:function(id){
			if(id){
				location.href=this.manager+"/basic/log/form.do?id="+id;
			}else {
				location.href=this.manager+"/basic/log/form.do";
			}
        },
        //表格数据转换
		logStatusFormat:function(row, column, cellValue, index){
			var value="";
			if(cellValue){
				var data = this.logStatusOptions.find(function(value){
					return value.value==cellValue;
				})
				if(data&&data.label){
					value = data.label;
				}
			}
            return value;
		},
		logBusinessTypeFormat:function(row, column, cellValue, index){
			var value="";
			if(cellValue){
				var data = this.logBusinessTypeOptions.find(function(value){
					return value.value==cellValue;
				})
				if(data&&data.label){
					value = data.label;
				}
			}
            return value;
		},
		logUserTypeFormat:function(row, column, cellValue, index){
			var value="";
			if(cellValue){
				var data = this.logUserTypeOptions.find(function(value){
					return value.value==cellValue;
				})
				if(data&&data.label){
					value = data.label;
				}
			}
            return value;
		},
        //pageSize改变时会触发
        sizeChange:function(pagesize) {
			this.loading = true;
            this.pageSize = pagesize;
            this.list();
        },
        //currentPage改变时会触发
        currentChange:function(currentPage) {
			this.loading = true;
			this.currentPage = currentPage;
            this.list();
        },
		search:function(data){
        	this.form.sqlWhere = JSON.stringify(data);
        	this.list();
		},
		//重置表单
		rest:function(){
			this.currentPage = 1;
			this.form.sqlWhere = null;
			this.$refs.searchForm.resetFields();
			this.list();
		},
	},
created:function(){
   	if(history.hasOwnProperty("state")&& history.state){
		this.form = history.state.form;
		this.currentPage = history.state.page.pageNo;
		this.pageSize = history.state.page.pageSize;
	}
		this.list();
	},
})
</script>
<style>
	#index .ms-container {
		height: calc(100vh - 141px);
	}
</style>
