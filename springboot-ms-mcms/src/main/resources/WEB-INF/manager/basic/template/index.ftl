<!DOCTYPE html>
<html>
<head>
    <title>模板管理</title>
    <#include "../../include/head-file.ftl">
</head>
<body>
<div id="index" class="ms-index" v-cloak class="ms-index">
    <el-header class="ms-header" height="50px">
        <el-col span="2">
            <el-upload
                    ref="uploadTemplate"
                    size="mini"
                    :file-list="fileList"
                    :show-file-list="false"
                    :action="ms.manager+'/file/uploadTemplate.do'"
                    :style="{width:''}"
                    accept=".zip"
                    :limit="1"
                    :disabled="false"
                    :on-error="onError"
                    :on-success="onSuccess"
                    :before-upload="beforeUpload"
                    :data="{uploadFloderPath:true}">
                <el-tooltip effect="dark" content="只允许上传zip文件！" placement="left-end">
                <el-button size="small" icon="el-icon-upload2" type="primary" :loading="uploadLoading">上传模版ZIP压缩包</el-button>
                </el-tooltip>
            </el-upload>
        </el-col>
    </el-header>

    <el-main class="ms-container">
        <el-alert
                class="ms-alert-tip"
                title="功能介绍"
                type="info"
                description="将制作好的模版使用压缩工具进行ZIP格式压缩，注意：压缩方式为 存储,模版必须放在一个文件夹下，例如：web/index.htm,web/list.htm，也可以直接将模版上传到 template/目录下">
            <template slot="title">
                功能介绍 <a href='http://doc.mingsoft.net/mcms/mo-ban-zhi-zuo.html#%E4%B8%8A%E4%BC%A0%E6%A8%A1%E7%89%88' target="_blank">开发手册</a>
            </template>
        </el-alert>
        <el-table height="calc(100vh - 68px)" v-loading="loading" ref="multipleTable" border :data="dataList"
                  tooltip-effect="dark" @selection-change="handleSelectionChange">
            <template slot="empty">
                {{emptyText}}
            </template>
            <el-table-column label="模板名称" align="left" prop="folderName">
                <template slot-scope="scope">
                    <div style="margin-left: 5px;display: flex;align-items: center;justify-content: flex-start;">
                    <svg class="icon" aria-hidden="true">
                        <use xlink:href="#icon-wenjianjia2"></use>
                    </svg>
                    <span style="margin-left: 5px">{{scope.row.folderName}}</span>
                    </div>
                </template>
            </el-table-column>
            <el-table-column label="类型" width="100" align="center" prop="folderType">
            </el-table-column>
            <el-table-column label="操作" width="180" align="center">
                <template slot-scope="scope">
                    <el-link type="primary" :underline="false" @click="view(scope.row.folderName)">查看</el-link>
                    <@shiro.hasPermission name="template:del">
                        <el-link type="primary" :underline="false" @click="del(scope.row.folderName)">删除</el-link>
                    </@shiro.hasPermission>
                </template>
            </el-table-column>
        </el-table>
    </el-main>
</div>
</body>

</html>
<script>
    var indexVue = new Vue({
        el: '#index',
        data: {
            uploadLoading:false,
            dataList: [],
            //模板管理列表
            selectionList: [],
            //模板管理列表选中
            manager: ms.manager,
            loading: true,
            //加载状态
            emptyText: '',
            fileList: [],
            websiteId: null
        },
        methods: {
            //查询列表
            list: function () {
                var that = this;
                that.loading = true;
                setTimeout(function () {
                    ms.http.get(ms.manager + "/template/queryTemplateSkin.do").then(function (res) {
                        that.dataList = [];
                        if(res.data.folderNameList!=null){
                            res.data.folderNameList.forEach(function (item) {
                                var type = "文件夹";
                                that.dataList.push({
                                    folderName: item,
                                    folderType: type
                                });
                            });
                        }
                        that.websiteId = res.data.websiteId;
                    }).finally(function () {
                        that.loading = false;
                    });
                }, 500);
            },
            //模板管理列表选中
            handleSelectionChange: function (val) {
                this.selectionList = val;
            },
            //删除
            del: function (name) {
                var that = this;
                that.$confirm('确定删除该模板吗？', '删除模板', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    ms.http.post(ms.manager + "/template/delete.do", {
                        fileName: name
                    }).then(function (res) {
                        if (res.result) {
                            that.$notify({
                                type: 'success',
                                title: '删除!',
                                message: '删除成功!'
                            }); //删除成功，刷新列表

                            that.list();
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
            //新增
            view: function (name) {
                window.location.href = ms.manager + "/template/list.do?template="  + this.websiteId + "/" + name;
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
                this.list();
            },

            beforeUpload:function(file){
                this.uploadLoading = true;
                if(file.type!='application/zip' && file.type!='application/x-zip-compressed') {
                    this.$notify({
                        title: '模版类型错误',
                        message: "只能上传zip压缩包",
                        type: 'warning'
                    });
                    this.uploadLoading = false;
                    return false;
                }
            },
            //fileUpload文件上传完成回调
            onSuccess: function (response, file, fileList) {
                var that = this;
                if(response.result){
                    that.uploadLoading = false;
                    ms.http.get(ms.manager + "/template/unZip.do", {
                        fileUrl: response.data
                    }).then(function () {
                        that.list();
                        that.uploadLoading = false;
                        that.$notify({
                            type: 'success',
                            title: '模版上传',
                            message: '上传成功!'
                        }); //删除成功，刷新列表

                    });
                    this.fileList.push({
                        url: file.url,
                        name: file.name,
                        path: response.data,
                        uid: file.uid
                    });
                }
                this.fileList=[];
                this.$refs.uploadTemplate.clearFiles();
            },
            onError:function (response, file, fileList) {
                this.$notify({
                    type: 'warning',
                    title: '模版上传失败',
                    message: JSON.parse(response.message).msg
                }); //删除成功，刷新列表
                this.$refs.uploadTemplate.clearFiles();
                this.fileList=[];
            }
        },
        mounted: function () {
            this.list();
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
</style>
