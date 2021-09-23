<!DOCTYPE html>
<html>
<head>
	<title>管理员管理</title>
		<#include "../../include/head-file.ftl">
</head>
<body>
	<div id="index" v-cloak class="ms-index">
			<el-header class="ms-header" height="50px">
				<el-col :span="12">
					<@shiro.hasPermission name="manager:save">
					<el-button type="primary" icon="el-icon-plus" size="mini" @click="save()">新增</el-button>
				</@shiro.hasPermission>
					<@shiro.hasPermission name="manager:del">
					<el-button type="danger" icon="el-icon-delete" size="mini" @click="del(selectionList)"  :disabled="!selectionList.length">删除</el-button>
					</@shiro.hasPermission>
				</el-col>
			</el-header>
		<el-main class="ms-container">
			<el-table v-loading="loading"  height="calc(100vh - 68px)" ref="multipleTable" class="ms-table-pagination" border :data="dataList" tooltip-effect="dark" @selection-change="handleSelectionChange">
				<template slot="empty">
					{{emptyText}}
				</template>
			<el-table-column type="selection" :selectable="isChecked" width="40" ></el-table-column>
            <el-table-column label="账号" min-width="120" align="left" prop="managerName" show-overflow-tooltip>
            </el-table-column>
            <el-table-column label="昵称" min-width="120" align="left" prop="managerNickName" show-overflow-tooltip>
            </el-table-column>
            <el-table-column label="角色名称" min-width="120"  align="left" prop="roleName" show-overflow-tooltip>
            </el-table-column>
			<el-table-column label="创建时间" width="180" align="center" prop="createTime">
            </el-table-column>
				<el-table-column label="操作" fixed="right" align="center" width="180">
					<template slot-scope="scope" v-if="'${Session.manager_session.managerName}' != scope.row.managerName">
						<@shiro.hasPermission name="manager:update">
						<el-link type="primary" :underline="false" @click="save(scope.row.id)">编辑</el-link>
						</@shiro.hasPermission>
						<@shiro.hasPermission name="manager:del">
						<el-link type="primary" :underline="false" @click="del([scope.row])">删除</el-link>
						</@shiro.hasPermission>
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
		<#include "/basic/manager/form.ftl">
</body>

</html>
<script>
	var indexVue = new Vue({
		el: '#index',
		data: {
			dataList: [],
			//管理员管理列表
			selectionList: [],
			//管理员管理列表选中
			total: 0,
			//总记录数量
			pageSize: 10,
			//页面数量
			currentPage: 1,
			//初始页
			mananger: ms.manager,
			loading: true,
			//加载状态
			emptyText: '',
			//提示文字
			//搜索表单
			form: {}
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
				that.loading = true;
				setTimeout(function () {
					ms.http.get(ms.manager + "/basic/manager/list.do", Object.assign({},that.form, page)).then(function (data) {
						that.loading = false;
						if (data.data.total <= 0) {
							that.emptyText = '暂无数据';
							that.dataList = [];
						} else {
							that.emptyText = '';
							that.total = data.data.total;
							that.dataList = data.data.rows;
						}
					});
				}, 500);
			},
			//管理员管理列表选中
			handleSelectionChange: function (val) {
				this.selectionList = val;
			},
			//不能删除自己
			isChecked: function (row, index) {
				if ("${Session.manager_session.managerName}" == row.managerName) {
					return false;
				} else {
					return true;
				}
			},
			//删除
			del: function (row) {
				var that = this;
				that.$confirm('此操作将永久删除所选内容, 是否继续?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(function () {
					ms.http.post(ms.manager + "/basic/manager/delete.do", row.length ? row : [row], {
						headers: {
							'Content-Type': 'application/json'
						}
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
			//新增
			save: function (id) {
				form.open(id);
			},
			//表格数据转换
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
			//重置表单
			rest: function () {
				this.currentPage = 1;
				this.loading = true;
				this.$refs.searchForm.resetFields();
				if (history.hasOwnProperty("state")&&history.state) {
					this.form = history.state.form;
					this.total = history.state.total;
					this.currentPage = history.state.page.pageNo;
					this.pageSize = history.state.page.pageSize;
				}
				this.list();
			}
		},
		created: function () {
			this.list();
		}
	});
</script>
<style>
</style>
