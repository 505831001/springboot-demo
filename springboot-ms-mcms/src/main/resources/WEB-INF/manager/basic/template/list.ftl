<!DOCTYPE html>
<html>
<head>
	<title>模板管理</title>
		<#include "../../include/head-file.ftl">
	<script src="${base}/static/plugins/codemirror/5.48.4/codemirror.js"></script>
	<link href="${base}/static/plugins/codemirror/5.48.4/codemirror.css" rel="stylesheet">
	<script src="${base}/static/plugins/codemirror/5.48.4/mode/css/css.js"></script>
	<script src="${base}/static/plugins/vue-codemirror/vue-codemirror.js"></script>
	<script src="${base}/static/plugins/codemirror/5.48.4/addon/scroll/annotatescrollbar.js"></script>
	<script src="${base}/static/plugins/codemirror/5.48.4/mode/xml/xml.js"></script>
	<script src="${base}/static/plugins/codemirror/5.48.4/mode/javascript/javascript.js"></script>
</head>
<body>
	<div id="index" class="ms-index" v-cloak class="ms-index">
			<el-header class="ms-header" height="50px">
				<el-col span="2">
					<el-upload
							size="mini"
							:file-list="fileList"
							:show-file-list="false"
							:action="ms.manager+'/file/uploadTemplate.do'"
							:style="{width:''}"
							accept=".htm,.ftl,.html,.jpg,.gif,.png,.css,.js,.ico,.swf,.less"
							:disabled="false"
							:on-success="fileUploadSuccess"
							:data="{uploadPath:templateData.uploadPath, uploadFloderPath:true, 'rename':false}">
						<el-tooltip effect="dark" content="建议上传5M以下htm/html/css/js/jpg/gif/png/swf文件" placement="left-end">
						<el-button size="small" icon="el-icon-upload2" type="primary">上传模版文件</el-button>
						</el-tooltip>
					</el-upload>
				</el-col>
				<el-col span=22>
					<el-button size="mini" plain onclick="" style="float: right" @click="back"><i class="iconfont icon-fanhui"></i>返回</el-button>
					<@shiro.hasPermission name="template:update">
						<el-button :disabled="form.name == ''" style="float: right;margin-right: 10px" type="primary" icon="iconfont icon-baocun" size="mini" @click="saveFile">保存</el-button>
					</@shiro.hasPermission>
				</el-col>
			</el-header>
		<el-main class="ms-container" style="flex-direction: row" v-loading="loading">
			<#--表格-->
			<div style="overflow: hidden; background-color: rgb(242, 246, 252);  display: flex;position: relative;margin-right: 10px;width: 300px">
						<el-table
								highlight-current-row
								height="calc(100vh - 80px)"
								ref="multipleTable"
								border
								:data="templateData.fileNameList"
								tooltip-effect="dark">
							<template slot="empty">
								{{emptyText}}
							</template>
							<el-table-column label="模板名称" align="left" prop="name" width="200">
								<template slot-scope="scope">
									<div style="cursor: pointer; margin-left: 5px;display: flex;align-items: center;justify-content: flex-start;" @click="view(scope.row)">
										<svg class="icon" aria-hidden="true">
											<use :xlink:href="scope.row.icon"></use>
										</svg>
										<span style="margin-left: 5px">{{scope.row.name}}</span>
									</div>
								</template>
							</el-table-column>
							<el-table-column label="操作" align="center" width="100">
								<template slot-scope="scope">
									<el-link type="primary" :underline="false" v-if="scope.row.folderType == '返回'" @click="view(scope.row)">返回</el-link>
									<el-link type="primary" :underline="false" v-if="scope.row.folderType == '文件夹'" @click="view(scope.row)">查看</el-link>
									<el-link type="primary" :underline="false" v-if="scope.row.folderType == '文件'" @click="view(scope.row)">编辑</el-link>
									<@shiro.hasPermission name="template:del">
										<el-link type="primary" :underline="false" v-if="scope.row.folderType != '文件夹' && scope.row.folderType != '返回'" @click="del(scope.row.folderName)">删除</el-link>
									</@shiro.hasPermission>
								</template>
							</el-table-column>
						</el-table>
			</div>
			<#--编辑框-->
			<div :class="imgFlag?'file-img':'file-edit'">
				<el-main style="padding: 0px;">
					<codemirror ref="code" v-show="!imgFlag" v-model="form.fileContent" :options="cmOption" :on-success="codemirrorSuccess">
					</codemirror>
					<img :src="imgPath" v-if="imgFlag" style="max-width: 100%;">
				</el-main>
			</div>

         </el-main>
	</div>
</body>
</html>

<script>
	Vue.use(VueCodemirror);
	var indexVue = new Vue({
		el: '#index',
		data: {
			//数据stak
			dataStack: [],
			manager: ms.manager,
			loading: true,
			//加载状态
			emptyText: '',
			//提示文字
			fileList: [],
			templateData: {
				fileNameList: [],
				uploadPath: null,
				path: null,
				websiteId: null
			},
			saveDisabled: false,
			currentRow:'',
			imgPath:'',
			//是否显示图片
			imgFlag:false,
			//设置
			cmOption: {
				tabSize: 4,
				styleActiveLine: true,
				lineNumbers: true,
				line: true,
				styleSelectedText: true,
				lineWrapping: true,
				mode: 'css',
				matchBrackets: true,
				showCursorWhenSelecting: true,
				hintOptions: {
					completeSingle: false
				}
			},
			fileName: "",
			//表单数据
			form: {
				// 文件名称
				name: '',
				fileName: '',
				fileNamePrefix: '',
				fileContent: ''
			},
		},
		methods: {
			codemirrorSuccess() {
			},
			//查询列表
			list: function (skinFolderName) {
				var that = this;
				that.loading = true;
				setTimeout(function () {
					ms.http.get(ms.manager + "/template/showChildFileAndFolder.do", {
						skinFolderName: skinFolderName
					}).then(function (res) {
						var folderNum = res.data.folderNum;
						var folderIndex = 0;
						var data = {
							fileNameList: [],
							uploadFileUrl: null
						};
						//最上层目录没有返回
						if (that.dataStack.length > 0) {
							//返回一层目录（参考linux目录结构）
							data.fileNameList.push({
								name:"...",
								icon: "#icon-wenjianjia2",
								folderName: '',
								folderType: "返回"
							});
						}

						res.data.fileNameList.forEach(function (item) {
							var type = "文件夹";
							var icon = "#icon-wenjianjia2";

							// 路径排序中，文件夹在前面，根据文件夹数量判断是否文件夹
							if (folderIndex >= folderNum) {
								type = "文件";
								icon = "#icon-wenjian2";
							}
							folderIndex++;

							var suffix = item.substring(item.lastIndexOf(".") + 1);

							if (suffix == "jpg" || suffix == "gif" || suffix == "png") {
								type = "图片";
								icon = "#icon-tupian1";
							}
							var name = item.substring(item.lastIndexOf("\\") + 1);
							name = name.substring(name.lastIndexOf("/") + 1);
							data.fileNameList.push({
								name:name,
								icon: icon,
								folderName: item,
								folderType: type
							});
						}); //保存当前路径以及相关信息

						data.path = skinFolderName;
						data.uploadPath = res.data.uploadFileUrl;
						data.websiteId = res.data.websiteId;
						that.templateData = data;
						that.dataStack.push(data);

						for (var key in that.templateData.fileNameList) {
							if (that.templateData.fileNameList[key].name == that.currentRow) {
								that.$refs.multipleTable.setCurrentRow(that.templateData.fileNameList[key]);
								break;
							}
						}
					}).finally(function () {
						that.loading = false;
					});
				}, 500);
			},
			//删除
			del: function (name) {
				var that = this;
				that.$confirm('确定删除该文件吗?', '删除文件', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(function () {
					ms.http.post(ms.manager + "/template/deleteTemplateFile.do", {
						fileName: name
					}).then(function (res) {
						if (res.result) {
							that.$notify({
								type: 'success',
								message: '删除成功!'
							}); //删除成功，刷新列表,弹出并重新加载
							that.imgFlag = false;
							that.form = {
								name: '',
								fileName: '',
								fileNamePrefix: '',
								fileContent: ''
							};
							that.list(that.dataStack.pop().path);
						} else {
							that.$notify({
								title: '失败',
								message: res.msg,
								type: 'warning'
							});
						}
					});
				})
			},
			//查看文件夹
			view: function (row) {
				if (!row) {
					return false;
				}
				this.loading = true;
				this.form = {
					name: '',
					fileName: '',
					fileNamePrefix: '',
					fileContent: ''
				};
				var type = row.folderType;
				var name = row.folderName;
				var path =  this.templateData.websiteId + "/" + name;
				this.imgFlag = false;
				this.currentRow = null;
				switch (type) {
					case "返回":
						this.dataStack.pop();
						this.templateData = this.dataStack[this.dataStack.length - 1];
						this.loading = false;
						return  false;
					case "文件夹":
						this.list(path);
						break;
					case "文件":
						this.$refs.multipleTable.setCurrentRow(row);
						this.currentRow = row.name;
						var suffix = path.substring(path.lastIndexOf(".") + 1);
						var mode = "text/html";
						if (suffix == "css") {
							mode = "css";
						}
						if (suffix == "js") {
							mode = "text/javascript";
						}
						this.cmOption.mode = mode;
						this.getFile(path);
						break;
					case "图片":
						this.imgPath = "/" + this.templateData.uploadPath + "/" + row.name;
						this.imgFlag = true;
						this.loading = false;
						break;
					default:
						this.loading = false;
						break;
				}
			},
			back: function () {
				window.location.href = ms.manager + "/template/index.do";
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
			search: function (data) {
				this.form.sqlWhere = JSON.stringify(data);
				this.list();
			},
			//重置表单
			rest: function () {
				this.form.sqlWhere = null;
				this.$refs.searchForm.resetFields();
				this.list();
			},
			//fileUpload文件上传完成回调
			fileUploadSuccess: function (response, file, fileList) {
				this.list(this.dataStack.pop().path);
				this.fileList.push({
					url: file.url,
					name: file.name,
					path: response,
					uid: file.uid
				});
			},
			saveFile: function () {
				var that = this;
				var url = ms.manager + "/template/writeFileContent.do";
				that.saveDisabled = true;
				that.form.oldFileName = that.form.fileNamePrefix + that.form.name;
				delete that.form.oldFileContent;
				that.$confirm('确定修改该文件吗？', '修改文件', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(function () {
					ms.http.post(url, that.form).then(function (data) {
						if (data.result) {
							that.$notify({
								title: '成功',
								message: '修改成功',
								type: 'success'
							});
							that.dialogVisible = false;
							that.list(that.dataStack.pop().path);
						} else {
							that.$notify({
								title: '失败',
								message: data.msg,
								type: 'warning'
							});
						}
						that.saveDisabled = false;
					});
				})
			},
			//获取当前模板管理
			getFile: function (fileName) {
				var that = this;
				ms.http.get(ms.manager + "/template/readFileContent.do", {
					"fileName": fileName
				}).then(function (res) {
					that.form = res.data;
					// that.$refs.code.codemirror.refresh();
					that.loading = false;
				}).finally(function () {
					that.loading = false;
				});
			}
		},
		mounted: function () {
			this.template = ms.util.getParameter("template");

			if (this.template) {
				this.list(this.template);
			}
		}
	});

</script>
<style>
	#index .ms-container {
		height: calc(100vh - 78px);
	}

	.icon {
		width: 2em;
		height: 2em;
		vertical-align: -0.15em;
		fill: currentColor;
		overflow: hidden;
	}
	#index .file-img {
		display: flex;
		flex-direction: column;
		flex: 1;
		justify-content: center;
		align-items: center;
	}
	#index .file-edit {
		display: flex;
		flex-direction: column;
		flex: 1;
		width: calc(100vh - 300px);
	}
	.CodeMirror {
		border: 1px solid #eee;
		height: auto;
	}

	.CodeMirror-scroll {
		overflow-y: hidden;
		overflow-x: auto;
	}
	.el-table--scrollable-x .el-table__body-wrapper {
		overflow-x: hidden;
	}
</style>
