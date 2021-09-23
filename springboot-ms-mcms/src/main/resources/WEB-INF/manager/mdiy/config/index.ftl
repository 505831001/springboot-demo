<!DOCTYPE html>
<html>
<head>
	<title>自定义配置</title>
	<#include "../../include/head-file.ftl">
	<script src="${base}/static/plugins/clipboard/clipboard.js"></script>
</head>
<body>
<div id="index" v-cloak class="ms-index">
	<el-header class="ms-header" height="50px">
		<el-col :span="24">
			<@shiro.hasPermission name="mdiy:config:del">
				<el-button type="danger" icon="el-icon-delete" size="mini" @click="del(selectionList)"  :disabled="!selectionList.length">删除</el-button>
			</@shiro.hasPermission>
			<@shiro.hasPermission name="mdiy:config:importJson">
				<el-button icon="iconfont icon-daoru" size="mini" @click="dialogImportVisible=true;impForm.id=''" style="float: right">导入</el-button>
			</@shiro.hasPermission>
		</el-col>
	</el-header>

	<el-dialog title="导入自定义模型配置json" :visible.sync="dialogImportVisible" width="600px"  append-to-body v-cloak>
		<el-scrollbar class="ms-scrollbar" style="height: 100%;">
			<el-form ref="form" :model="impForm" :rules="rules" size="mini" label-width="130px">
				<el-form-item prop="modelJson">
                    <span slot='label'>自定义模型json</span>
					<el-input :rows="10" type="textarea" v-model="impForm.modelJson"  placeholder="请粘贴来自代码生器中自定义模型的配置json"></el-input>
					<div class="ms-form-tip">
						通过 <a href="https://code.mingsoft.net/people/index.do?selMenu=generate" target="_blank">代码生成器</a> 在线设计表单，
						打开 <b>代码预览</b> 菜单，选择 <b>自定义模型</b>并<b>复制代码</b>粘贴到当前文本框 <br/>
						<b>注意:</b> 更新模型时会根据新的 <b>自定义模型</b>配置 来修改对应的表结构，包括删除字段、修改字段。所以更新模型时谨慎操作
					</div>
				</el-form-item>

			</el-form>

		</el-scrollbar>
		<div slot="footer">
			<el-button size="mini" @click="dialogImportVisible = false">取 消</el-button>
			<el-button size="mini" type="primary" @click="imputJson()">确 定</el-button>
		</div>
	</el-dialog>
	<el-dialog title="预览" :visible.sync="dialogViewVisible" width="70%" append-to-body v-cloak>
		<div id="model"></div>
	</el-dialog>
	<div class="ms-search" style="padding: 20px 10px 0 10px;">
		<el-row>
			<el-form :model="form"  ref="searchForm"  label-width="85px" size="mini">
				<el-row>
					<el-col :span="8">
						<el-form-item  label="配置名称" prop="modelName">
							<el-input v-model="form.modelName"
									  :disabled="false"
									  :style="{width:  '100%'}"
									  :clearable="true"
									  placeholder="请输入配置名称">
							</el-input>
						</el-form-item>
					</el-col>
					<el-col :span="16" style="text-align: right">
						<el-button type="primary" icon="el-icon-search" size="mini" @click="loading=true;currentPage=1;list()">查询</el-button>
						<el-button @click="rest"  icon="el-icon-refresh" size="mini">重置</el-button>
					</el-col>
				</el-row>
			</el-form>
		</el-row>
	</div>

	<el-main class="ms-container">
		<el-alert
				class="ms-alert-tip"
				title="功能介绍"
				type="info"
				description="满足配置化数据的业务管理，例如：上传设置、会员设置、支付宝设置。 ">
			<template slot="title">
				功能介绍 <a href='http://doc.mingsoft.net/plugs/zi-ding-yi-cha-jian/ye-wu-kai-fa/zi-ding-yi-pei-zhi.html' target="_blank">开发手册</a>
			</template>
		</el-alert>
		<el-table v-loading="loading" ref="multipleTable" height="calc(100vh-68px)" class="ms-table-pagination" border :data="dataList" tooltip-effect="dark" @selection-change="handleSelectionChange">
			<template slot="empty">
				{{emptyText}}
			</template>
			<el-table-column type="selection" width="40"></el-table-column>
			<el-table-column label="编号" width="120" align="left" prop="id">
			</el-table-column>

			<el-table-column align="left" prop="modelName">
				<template slot='header'>
					配置名称
					<el-popover placement="top-start" title="提示" trigger="hover" >
						提供给业务中调用 如：ConfigUtil.getString("配置名称","字段名称")
						<i class="el-icon-question" slot="reference"></i>
					</el-popover>
				</template>
			</el-table-column>

			<el-table-column align="center" width="400">
				<template slot='header'>操作
					<el-popover placement="top-start" title="提示" trigger="hover" >
						复制菜单JSON：用在 权限管理>菜单管理 导入，方便快速创建菜单使用; 数据预览 可以填写配置信息<br/>
						注意：只能是导入到已有的父菜单中
						<i class="el-icon-question" slot="reference"></i>
					</el-popover>
				</template>
				<template slot-scope="scope" >
                    <@shiro.hasPermission name="mdiy:config:del">
                        <el-link :underline="false" type="primary" @click="del([scope.row])">删除</el-link>
                    </@shiro.hasPermission>

					<@shiro.hasPermission name="mdiy:config:updateJson">
						<el-link type="primary" :underline="false"  @click="dialogImportVisible=true;impForm.id=scope.row.id">更新模型</el-link>
					</@shiro.hasPermission>
					<el-link type="primary" :underline="false" class="copyBtn" :data-clipboard-text="JSON.stringify(menuJson)" @click="copyJson(scope.row.modelName)">复制菜单JSON</el-link>
					<el-link type="primary" :underline="false"  @click="view(scope.row)">表单预览</el-link>
					<el-link type="primary" :underline="false" :href="ms.manager+'/mdiy/configData/form.do?modelName='+encodeURI(scope.row.modelName)">数据预览</el-link>
                </template>
			</el-table-column>
		</el-table>
		<el-pagination
				background
				:page-sizes="[5, 10, 20]"
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
		data: {
			//自定义模型列表
			dataList: [],
			//自定义模型列表选中
			selectionList: [],
			//总记录数量
			total: 0,
			//页面数量
			pageSize: 10,
			//初始页
			currentPage: 1,
			modelTypeOptions: [],
			dialogViewVisible: false,
			dialogImportVisible: false,
			loading: true,
			emptyText: '',
			//搜索配置
			form: {
				// 模型名称
				modelName: '',
			},
			impForm: {
				//模型编号
				id:'',
				// 模型名称
				modelJson: '',
			},
			//配置验证
			rules: {
				modelJson: [{
					required: true,
					message: 'json数据不能为空',
					trigger: 'blur'
				}]
			},
			menuJson:[
				{
					modelIsMenu:1,
					modelTitle:"",
					modelUrl:"",
					modelChildList:[
						{
							modelIsMenu:0,
							modelTitle:"更新",
							modelUrl:"mdiy:configData:update"
						},
						{
							modelIsMenu:0,
							modelTitle:"查看",
							modelUrl:"mdiy:configData:view"
						}
					]
				}
			]
		},
		watch: {
			'dialogImportVisible': function (n, o) {
				if (!n) {
					this.$refs.form.resetFields();
					this.form.id = 0;
				}
			},
		},
		methods: {
			//查询列表
			list: function () {
				var that = this;
				var page = {
					pageNo: that.currentPage,
					pageSize: that.pageSize
				};
				var form = JSON.parse(JSON.stringify(that.form));

				for (key in form) {
					if (!form[key]) {
						delete form[key];
					}
				}

				history.replaceState({
					form: form,
					page: page,
					total: that.total
				}, "");
				setTimeout(function () {
					ms.http.get(ms.manager + "/mdiy/config/list.do", Object.assign({}, that.form, page)).then(function (data) {
						if(data.result){
							if (data.data.total <= 0) {
								that.loading = false;
								that.emptyText = '暂无数据';
								that.dataList = [];
							} else {
								that.emptyText = '';
								that.loading = false;
								that.total = data.data.total;
								that.dataList = data.data.rows;
							}
						}
					});
				}, 500);
			},
			//自定义模型列表选中
			handleSelectionChange: function (val) {
				this.selectionList = val;
			},
			//删除
			del: function(row) {
				var that = this;
				that.$confirm('此操作将永久删除所选内容, 是否继续?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(function () {
					ms.http.post(ms.manager + "/mdiy/config/delete.do", row.length ? row : [row], {
						headers: {
							'Content-Type': 'application/json'
						}
					}).then(function (data) {
						if (data.result) {
							that.$notify({
								title: '成功',
								type: 'success',
								message: '删除成功!'
							}); //删除成功，刷新列表

							that.list();
						} else {
							that.$notify({
								title: '失败',
								message: data.msg,
								type: 'warning'
							});
						}
					});
				});
			},
			//预览
			view: function(row) {

				var data = JSON.parse(row.modelJson);

				var that = this;
				this.dialogViewVisible = true;
				this.$nextTick(function () {
					var model = document.getElementById('model');
					var custom = document.getElementById('c_model');

					if (custom) {
						model.removeChild(custom);
					}

					var div = document.createElement('div');
					div.id = 'c_model';
					model.appendChild(div);
					var s = document.createElement('script');
					s.innerHTML = data.script;
					var con = document.createElement('div');
					con.id = 'custom-model';
					con.innerHTML = data.html;
					div.appendChild(s);
					div.appendChild(con); //初始化自定义模型并传入关联参数

					that.model = new custom_model();
				});
			},
			imputJson: function () {
				var that = this;
				var url = "/mdiy/config/importJson.do";
				if(that.impForm.id>0){
					url = "/mdiy/config/updateJson.do";
				}

				that.$confirm('此操作会进行更新表操作，请谨慎操作', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(function () {
					// 控制前端接口isWebSubmit，保存至json
					that.$refs.form.validate(function (valid) {
						try {
							JSON.parse(that.impForm.modelJson);
						}catch(e) {
							that.$notify({
								title: '失败',
								message: "json格式不匹配，请复制 代码生成器 中的 自定义模型代码",
								type: 'warning'
							});
						};

						if (valid) {
							ms.http.post(ms.manager + url, that.impForm).then(function (data) {
								if (data.result) {
									window.location.href = ms.manager + "/mdiy/config/index.do";
								} else {
									that.$notify({
										title: '失败',
										message: data.msg,
										type: 'warning'
									});
								}
							})
						}
					});
				});
			},
			//pageSize改变时会触发
			sizeChange: function (pagesize) {
				this.loading = true;
				this.pageSize = pagesize;
				this.list();
			},
			//currentPage改变时会触发
			currentChange: function (currentPage) {
				this.loading = true;
				this.currentPage = currentPage;
				this.list();
			},
			//重置配置
			rest: function () {
				this.currentPage = 1;
				this.loading = true;
				this.$refs.searchForm.resetFields();
				this.list();
			},
			//复制json
			copyJson: function (menuTitle) {
				this.menuJson[0].modelTitle=menuTitle;
				this.menuJson[0].modelUrl="/mdiy/configData/form.do?modelName="+menuTitle;
				var clipboard = new ClipboardJS('.copyBtn');
				var self = this;
				clipboard.on('success', function (e) {
					self.$notify({
						title: '提示',
						message: '菜单json已保存到剪切板，可在菜单管理中导入',
						type: 'success'
					});
					clipboard.destroy();
				});
			},
		},
		created: function () {
			if (history.hasOwnProperty("state")&&history.state) {
				this.form = history.state.form;
				this.total = history.state.total;
				this.currentPage = history.state.page.pageNo;
				this.pageSize = history.state.page.pageSize;
			}
			this.list();
		}
	});
</script>
<style>
	#index .iconfont{
		font-size: 12px;
		margin-right: 5px;
	}
</style>
