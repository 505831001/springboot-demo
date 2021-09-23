<!DOCTYPE html>
<html>
<head>
	<title>菜单</title>
	<meta charset="utf-8">
	<#include "../../include/head-file.ftl">
</head>
<body>
<div id="index" v-cloak class="ms-index">
	<el-header class="ms-header" height="50px">
		<el-col :span="24">
			<@shiro.hasPermission name="model:save">
			<el-button type="primary" icon="el-icon-plus" size="mini" @click="editModal(0)">新增</el-button>
			</@shiro.hasPermission>
			<@shiro.hasPermission name="model:del">
			<el-button type="danger" icon="el-icon-delete" size="mini" @click="del(selectionList)"  :disabled="!selectionList.length">删除</el-button>
			</@shiro.hasPermission>
			<@shiro.hasPermission name="model:update">
			<el-button icon="iconfont icon-daoru" size="mini" @click="dialogImportVisible=true" style="float: right">导入</el-button>
			</@shiro.hasPermission>
		</el-col>
	</el-header>

	<el-dialog  :visible.sync="dialogImportVisible" width="600px" append-to-body v-cloak>
		<template slot="title">
            <i class="iconfont icon-daoru"> 导入菜单JSON
        </template>
		<el-form label-width="110px" >
			<el-form-item label="菜单JSON">
				<el-input :rows="10" type="textarea" v-model="form.menuStr"></el-input>
				<div class="ms-form-tip">
					粘贴来自<b>自定义配置、自定义业务、代码生成器</b> 的<b>复制菜单JSON</b>功能数据
				</div>
			</el-form-item>
		</el-form>

		<div slot="footer">
			<el-button size="mini" @click="dialogImportVisible = false">取 消</el-button>
			<el-button size="mini" :loading="saveDisabled" :disabled="form.menuStr==''" type="primary" @click="imputJson()">确 定</el-button>
		</div>
	</el-dialog>

	<el-main class="ms-container">
		<el-table ref="multipleTable" v-loading="loading"
				  height="calc(100vh - 68px)"
				  class="ms-table-pagination"
				  border :data="dataList"
				  tooltip-effect="dark"
				  @selection-change="handleSelectionChange"
				  row-key="id"
				  :tree-props="{children: 'modelChildList'}">
			<template style="width:100%" slot="empty">
				{{emptyText}}
			</template>
			<el-table-column type="selection" width="40"></el-table-column>
			<el-table-column label="菜单标题" width="200" align="left" prop="modelTitle" show-overflow-tooltip>
			</el-table-column>
			<el-table-column label="菜单图标" width="100" align="center" prop="modelIcon">
				<template slot-scope="scope">
					<i style="font-size: 24px !important;" class="iconfont" :class="scope.row.modelIcon"></i>
				</template>
			</el-table-column>
			<el-table-column label="菜单链接地址" align="left" prop="modelUrl" show-overflow-tooltip>
			</el-table-column>

			<el-table-column label="菜单排序" width="90" align="right" prop="modelSort">
			</el-table-column>
			<el-table-column label="菜单类型"  width="130" align="center" prop="modelIsMenu">
				<template slot-scope="scope">
					<span v-if="scope.row.modelIsMenu == 1">导航链接</span>
					<span v-else>功能权限</span>
				</template>
			</el-table-column>
			<el-table-column label="系统扩展" width="100" align="left" prop="isChild" show-overflow-tooltip>
			</el-table-column>
			<@shiro.hasAnyPermissions name="model:update,model:del">
				<el-table-column label="操作"  align="center" width="250">
					<template slot-scope="scope">
						<@shiro.hasPermission name="model:update"><el-button size="medium" type="text" @click="editModal(scope.row.id)">编辑</el-button></@shiro.hasPermission>
						<@shiro.hasPermission name="model:del"><el-button size="medium" type="text" @click="del([scope.row])">删除</el-button></@shiro.hasPermission>
						<@shiro.hasPermission name="model:save"><el-button size="medium" type="text" v-if="scope.row.modelIsMenu == 1"  @click="dialogImportVisible=true;form.modelId = scope.row.id">导入子菜单</el-button></@shiro.hasPermission>
						<el-link type="primary" icon="iconfont icon-fuzhidaima" :underline="false" class="copyBtn"
								 :data-clipboard-text="menuStr" @click="copyMenu(scope.row)">复制菜单
						</el-link>
					</template>
				</el-table-column>
			</@shiro.hasAnyPermissions>
		</el-table>
	</el-main>
</div>
<#include "/component/ms-icon.ftl">
<#include "/component/ms-tree-select.ftl">
<#include "/basic/model/form.ftl">
</body>

</html>
<script>
	var indexVue = new Vue({
		el: '#index',
		data: {
			dataList: [],
			//列表
			selectionList: [],
			//列表选中
			mananger: ms.manager,
			dialogImportVisible: false,
			form:{
				menuStr: '',
				modelId:0,
			},
			saveDisabled: false,
			loading: true,
			emptyText: '',
			menuStr:'',
		},
		watch: {
			'dialogImportVisible': function (n, o) {
				if (!n) {
					this.form.menuStr = '';
					this.form.mdoelId = 0;
				}
			}
		},
		methods: {
			//查询列表
			list: function () {
				var that = this;
				ms.http.get(ms.manager + "/model/list.do", {}).then(function (data) {
					if (data.data.total <= 0) {
						that.loading = false;
						that.emptyText = '暂无数据';
						that.dataList = [];
					} else {
						that.emptyText = '';
						that.loading = false;
						that.dataList = ms.util.treeData(data.data.rows, 'id', 'modelId', 'modelChildList');
						that.dataList.forEach(function(menu) {
							if(menu.modelIsMenu==1) {
								form.modeldata.push(menu);
							}
						});
					}
				});
			},
			//列表选中
			handleSelectionChange: function (val) {
				this.selectionList = val;
			},
			//删除
			del: function (row) {
				var that = this;
				that.$confirm('删除选中菜单，如果有子菜单也会一并删除', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(function () {
					var ids = "";
					var markList = JSON.parse(localStorage.getItem("markList"));
					for (var i = 0; i < row.length; i++) {
						if (ids == "") {
							ids = row[i].id;
							var index = markList.findIndex(function (x) {
								return x.title == row[i].modelTitle
							});
							markList.splice(index, 1);
						} else {
							ids = ids + "," + row[i].id;
						}
					}
					localStorage.setItem("markList", JSON.stringify(markList))
					ms.http.post(ms.manager + "/model/delete.do", {
						ids: ids
					}).then(function (data) {
						if (data.result) {
							that.$notify({
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
				})
			},
			//新增或编辑
			editModal: function (id) {
				form.open(id);
			},
			// 复制菜单
			copyMenu(menu) {
				var that = this;
				that.menuStr = JSON.stringify(new Array(menu))
				var clipboard = new ClipboardJS('.copyBtn');
				clipboard.on('success', function (e) {
					that.$message.success('菜单数据已保存到剪切板');
					clipboard.destroy();
				});
			},
			imputJson: function () {
				var that = this;
				this.saveDisabled = true
				ms.http.post(ms.manager + "/model/import.do", this.form).then(function (data) {
					if (data.result) {
						that.list();
						var model = JSON.parse(that.form.menuStr)[0];
						var markList = JSON.parse(localStorage.getItem("markList"));
						var menu = {
							title: model.modelTitle,
							icon: model.modelIcon,
						}
						markList.push(menu);
						localStorage.setItem("markList", JSON.stringify(markList))
						that.saveDisabled = false
						that.dialogImportVisible = false;
						that.$notify({
							title: '导入成功',
							message: "请刷新当前页面，查看菜单",
							type: 'success'
						});
					} else {
						that.$notify({
							title: '失败',
							message: data.msg,
							type: 'warning'
						});
						that.saveDisabled = false
					}
				}).finally(function () {
					that.saveDisabled = false
				});
			}
		},
		mounted: function () {
			this.list();
		}
	});
</script>
<style>
	#index .ms-search{
		background: #fff;
	}
	#index .iconfont{
		font-size: 12px !important;
		margin-right: 4px;
	}
	#index .ms-search .ms-search-footer{
		line-height: 60px;
		text-align: center;
	}
</style>
