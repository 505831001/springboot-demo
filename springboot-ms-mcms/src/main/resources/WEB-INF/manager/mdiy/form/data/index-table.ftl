<div id="list-temp">
	<template   id="custom-model">
		<div  class="ms-index">
			<ms-search ref="search" @search="search" :condition-data="conditionList" :conditions="conditions"></ms-search>
			<el-header class="ms-header" height="50px">
				<el-col :span="12">
					<@shiro.hasPermission name="mdiy:formData:save">
						<el-button type="primary" icon="el-icon-plus" size="mini" @click="save()">新增</el-button>
					</@shiro.hasPermission>
					<@shiro.hasPermission name="mdiy:formData:del">
						<el-button type="danger" icon="el-icon-delete" size="mini" @click="del(selectionList)"  :disabled="!selectionList.length">删除</el-button>
					</@shiro.hasPermission>
				</el-col>
				<!-- 菜单点击到当前页面就没有返回按钮-->
				<el-col span="12" class="ms-tr" >
					<el-button type="primary" icon="iconfont icon-shaixuan1" size="mini" @click="currentPage=1;$refs.search.open()">筛选</el-button>
					<el-button size="mini"   plain onclick="javascript:history.go(-1)"><i class="iconfont icon-fanhui"></i>返回</el-button>
				</el-col>
			</el-header>
			<el-main class="ms-container">
				<el-table v-loading="loading" ref="multipleTable" height="calc(100vh-68px)" class="ms-table-pagination" border :data="dataList" tooltip-effect="dark" @selection-change="handleSelectionChange">
					<template slot="empty">
						{{emptyText}}
					</template>
					<el-table-column type="selection" width="40"></el-table-column>
					<div v-if="field.isShow == 'true'" v-for="(field,index) in tableField">
						<el-table-column min-width="120" :label="field.name" :prop="field.key" v-if="field.type == 'input' ">
						</el-table-column>
						<el-table-column min-width="100" :label="field.name" :prop="field.key" v-else-if=" field.type == 'number' ">
						</el-table-column>

						<el-table-column min-width="120" :label="field.name" :prop="field.key" v-else-if="field.type == 'select' || field.type == 'radio' || field.type == 'checkbox'"  :formatter="fmt">
						</el-table-column>
						<el-table-column min-width="190" align="center" :label="field.name" :prop="field.key" v-else-if="field.type == 'date' ">
							<template slot-scope="scope">
								<span v-text="ms.util.date.fmt(scope.row[field.key],'yyyy-MM-dd hh:mm:ss')"></span>
							</template>
						</el-table-column>
						<el-table-column min-width="120" align="center" :label="field.name" :prop="field.key" v-else-if="field.type == 'time' ">
							<template slot-scope="scope">
								<span v-text="ms.util.date.fmt(scope.row[field.key],'hh:mm:ss')"></span>
							</template>
						</el-table-column>
						<el-table-column min-width="120" :label="field.name" align="center" :prop="field.key" v-else-if="field.type == 'imgupload' ">
							<template slot-scope="scope">
								<div class="block" v-for="src in scope.row[field.key]">
									<el-image
											style="width: 50px; height: 50px;float: left;margin-right: 5px;"
											:src="ms.base+src.path"
											:preview-src-list="[ms.base+src.path]">
									</el-image>
								</div>
							</template>
						</el-table-column>
					</div>
					<el-table-column label="操作" align="center" width="180">
						<template slot-scope="scope">
							<@shiro.hasPermission name="mdiy:formData:del">
								<el-link type="primary" :underline="false"  @click="del(scope.row)">删除</el-link>
							</@shiro.hasPermission>
							<@shiro.hasPermission name="mdiy:formData:update">
								<el-link type="primary" :underline="false" @click="save(scope.row.id)">编辑</el-link>
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
	</template>



</div>

