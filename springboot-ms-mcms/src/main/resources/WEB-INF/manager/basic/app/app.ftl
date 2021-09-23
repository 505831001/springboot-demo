<!DOCTYPE html>
<html>
<head>
    <title>应用设置</title>
    <#include "../../include/head-file.ftl">
</head>
<body>
<div id="form" v-cloak>
    <el-header class="ms-header ms-tr" height="50px">
        <el-button type="primary" icon="iconfont icon-baocun" size="mini" @click="save()" :loading="saveDisabled">保存</el-button>
    </el-header>
    <el-main class="ms-container">
        <el-scrollbar class="ms-scrollbar" style="height: 100%;">
        <el-form ref="form" :model="form" :rules="rules" label-width="140px" size="mini">
            <el-row>
                <el-col span="12">
                    <el-form-item label="网站标题" prop="appName">
                        <el-input v-model="form.appName"
                                  :disabled="false"
                                  :style="{width:  '100%'}"
                                  :clearable="true"
                                  placeholder="请输入网站标题">
                        </el-input>
                        <div class="ms-form-tip">
                         标签<a href="http://doc.mingsoft.net/mcms/biao-qian/tong-yongquan-ju-ms-global.html" target="_blank">{ms:global.name/}</a>
                        </div>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col span="12">
                    <el-form-item  label="站点风格" prop="appStyle">
                        <el-select v-model="form.appStyle"
                                   :style="{width: '100%'}"
                                   :filterable="false"
                                   :disabled="false"
                                   :multiple="false" :clearable="true"
                                   placeholder="请选择站点风格">
                            <el-option v-for='item in templateFolderNameList' :key="item" :value="item"
                                       :label="item"></el-option>
                        </el-select>
                        <div class="ms-form-tip">
                         标签<a href="http://doc.mingsoft.net/mcms/biao-qian/tong-yongquan-ju-ms-global.html" target="_blank">{ms:global.style/}</a>，
                            如果站点下有多套模版，切换模版保存后，需要重新静态化前台才会生效
                        </div>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row
                    :gutter="0"
                    justify="start" align="top">
                <el-col :span="12">

                    <el-form-item  label="网站生成目录" prop="appDir">
                        <el-input
                                v-model="form.appDir"
                                :disabled="false"
                                :readonly="false"
                                :style="{width:  '100%'}"
                                :clearable="false"
                                placeholder="请输入网站生成目录">
                        </el-input>
                        <div class="ms-form-tip">
                            application.yml中配置ms.html-dir了父目录，这里配置的是站点再父目录中生成的文件夹
                        </div>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                </el-col>
            </el-row>
            <el-form-item  label="网站Logo" prop="appLogo">
                <el-upload
                        :file-list="form.appLogo"
                        :action="ms.base+'/file/upload.do'"
                        :on-remove="appLogohandleRemove"
                        :style="{width:''}"
                        :limit="1"
                        :on-exceed="appLogohandleExceed"
                        :disabled="false"
                        :data="{uploadPath:'/appLogo','isRename':true,'appId':true}"
                        :on-success="appLogoSuccess"
                        accept="image/*"
                        list-type="picture-card">
                    <i class="el-icon-plus"></i>
                    <div slot="tip" class="el-upload__tip">支持jpg,png格式，最多上传1张图片</div>
                </el-upload>
                <div class="ms-form-tip">
                <a href="http://doc.mingsoft.net/mcms/biao-qian/tong-yongquan-ju-ms-global.html" target="_blank">{@ms:file global.logo/}</a>，注意这里的获取图片标签会采用ms.file方式获取
                </div>
            </el-form-item>
            <el-form-item label="关键字" prop="appKeyword">

                <el-input
                        type="textarea" :rows="5"
                        :disabled="false"
                        v-model="form.appKeyword"
                        :style="{width: '100%'}"
                        placeholder="请输入关键字">
                </el-input>
                <div class="ms-form-tip">
                 标签<a href="http://doc.mingsoft.net/mcms/biao-qian/tong-yongquan-ju-ms-global.html" target="_blank">{ms:global.keyword/}</a>,
                    用于SEO优化
                </div>
            </el-form-item>


            <el-form-item label="描述" prop="appDescription">
                <el-input
                        type="textarea" :rows="5"
                        :disabled="false"
                        v-model="form.appDescription"
                        :style="{width: '100%'}"
                        placeholder="请输入描述">
                </el-input>
                <div class="ms-form-tip">
                 标签<a href="http://doc.mingsoft.net/mcms/biao-qian/tong-yongquan-ju-ms-global.html" target="_blank">{ms:global.descrip/}</a>，
                    用于SEO优化
                </div>
            </el-form-item>
            <el-form-item label="版权信息" prop="appCopyright">
                <el-input
                        type="textarea" :rows="5"
                        :disabled="false"
                        v-model="form.appCopyright"
                        :style="{width: '100%'}"
                        placeholder="请输入版权信息">
                </el-input>
                <div class="ms-form-tip">
                 标签<a href="http://doc.mingsoft.net/mcms/biao-qian/tong-yongquan-ju-ms-global.html" target="_blank">{ms:global.copyright/}</a>，
                    设置网站底部的版权信息
                </div>
            </el-form-item>
        </el-form>
        </el-scrollbar>
    </el-main>
</div>
</body>
</html>
<script>
    var form = new Vue({
        el: '#form',
        data: function () {
            return {
                saveDisabled: false,
                //表单数据
                form: {
                    // 站点名称
                    appName: '',
                    // 站点风格
                    appStyle: [],
                    // 网站Logo
                    appLogo: [],
                    // 关键字
                    appKeyword: '',
                    // 描述
                    appDescription: '',
                    // 版权信息
                    appCopyright: '',
                    // 网站生成目录
                    appDir:'',
                },
                templateFolderNameList: [],//当前站点模版文件夹下所有的模版
                rules: {
                    // 网站标题
                    appName: [{
                        "required": true,
                        "message": "网站标题必须填写"
                    }, {
                        "min": 1,
                        "max": 50,
                        "message": "站点名称长度必须为10-150"
                    }],
                    appDescription: [{
                        "min": 0,
                        "max": 1000,
                        "message": "描述长度必须小于1000"
                    }],
                    appKeyword: [{
                        "min": 0,
                        "max": 1000,
                        "message": "关键字长度必须小于1000"
                    }],
                    appCopyright: [{
                        "min": 0,
                        "max": 1000,
                        "message": "版权信息长度必须小于1000"
                    }],
                    // 网站生成目录
                    appDir: [
                        {"required":true,"message":"网站生成目录不能为空"},
                        {"min":0,"max":10,"message":"网站生成目录长度必须为0-10"},
                        {
                            "pattern": /^[^[!@#$"'%^&*()_+-/~?！@#￥%…&*（）——+—？》《：“‘’]+$/,
                            "message": "网站生成目录格式不匹配"
                        }
                    ],
                }
            };
        },
        watch: {},
        computed: {},
        methods: {
            save: function () {
                var that = this;
                url = ms.manager + "/app/update.do";
                this.$refs.form.validate(function(valid) {
                    if (valid) {
                        that.saveDisabled = true;
                        var data = JSON.parse(JSON.stringify(that.form));
                        if(data.appLogo){
                            data.appLogo = JSON.stringify(data.appLogo);
                        }
                        ms.http.post(url, data).then(function (data) {
                            if (data.result) {
                                that.$notify({
                                    title: '成功',
                                    message: '保存成功',
                                    type: 'success'
                                });
                            } else {
                                that.$notify({
                                    title: '失败',
                                    message: data.msg,
                                    type: 'warning'
                                });
                            }
                            that.saveDisabled = false;
                        });
                    } else {
                        return false;
                    }
                });
            },
            //获取当前应用表
            get: function (id) {
                var that = this;
                this.loading = true
                ms.http.get(ms.manager + "/app/"+id+"/get.do", {"id":id}).then(function (res) {
                    that.loading = false
                    if(res.result && res.data){
                        if(res.data.appLogo){
                            res.data.appLogo = JSON.parse(res.data.appLogo);
                            res.data.appLogo.forEach(function(value){
                                value.url= ms.base + value.path
                            })
                        }else{
                            res.data.appLogo=[]
                        }
                        that.form = res.data;
                    }
                });
            },
            //上传超过限制
            appLogohandleExceed:function(files, fileList) {
                this.$notify({ title: '当前最多上传1张图片', type: 'warning' });},
            appLogohandleRemove:function(file, files) {
                var index = -1;
                index = this.form.appLogo.findIndex(function(e){return e == file} );
                if (index != -1) {
                    this.form.appLogo.splice(index, 1);
                }
            },
            disabledDate: function () {},
            //获取appStyle数据源
            queryAppTemplateSkin: function () {
                var that = this;
                ms.http.get(ms.manager + '/template/queryAppTemplateSkin.do', {
                    pageSize: 99999
                }).then(function (data) {
                    that.templateFolderNameList = data.data.appTemplates;
                });
            },
            //appLogo文件上传完成回调
            appLogoSuccess: function (response, file, fileList) {
                if(response.result){
                    this.form.appLogo.push({url:file.url,name:file.name,path:response.data,uid:file.uid});
                }else {
                    this.$notify({
                        title: '失败',
                        message: response.msg,
                        type: 'warning'
                    });
                }

            },
            //上传超过限制
            appLogohandleExceed: function (files, fileList) {
                this.$notify({
                    title: '失败',
                    message: '当前最多上传1个文件',
                    type: 'warning'
                });
            },
            appLogohandleRemove: function (file, files) {
                var index = -1;
                index = this.form.appLogo.findIndex(function (text) {
                    return text == file;
                });

                if (index != -1) {
                    this.form.appLogo.splice(index, 1);
                }
            }
        },
        created: function () {
            this.queryAppTemplateSkin();
            this.form.id = -1;

            if (this.form.id) {
                this.get(this.form.id);
            } else {
                delete this.form.id;
            }
        }
    });
</script>
