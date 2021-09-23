<!DOCTYPE html>
<html>
<head>
    <title>自定义配置</title>
    <#include "../../include/head-file.ftl">
    <script src="${base}/static/mdiy/index.js"></script>
</head>
<body>
<div id="form" v-loading="loading" v-cloak>
    <el-header class="ms-header ms-tr" height="50px">
        <el-button type="primary" icon="iconfont icon-baocun" size="mini" @click="save()" :loading="saveDisabled">保存
        </el-button>
        </el-header>
    <el-main class="ms-container">
        <div id="configModel"></div>
    </el-main>
</div>
</body>
</html>
<script>
    var form = new Vue({
        el: '#form',
        data: function () {
            return {
                loading: false,
                saveDisabled: false,
                //自定义模型实例
                configModel: undefined,
                //表单数据
                form: {
                    id:''
                },
            }
        },
        methods: {
            save: function () {
                var that = this;
                that.saveDisabled = true;
                that.configModel.save(function (res) {
                    if(res.result){
                        that.$notify({
                            title: '成功',
                            type: 'success',
                            message: '保存成功!'
                        });
                    }else {
                        that.$notify({
                            title: '失败',
                            message: res.msg,
                            type: 'warning'
                        });
                    }
                    that.saveDisabled = false;
                });
            },
        },
        created: function () {
            this.form.id = ms.util.getParameter("id");
            this.loading = true;
            var that = this;
            ms.mdiy.model.config("configModel", {"modelName":ms.util.getParameter("modelName")},{},true).then(function(obj) {
                that.configModel = obj;
            });;
            this.loading = false;
        }
    });
</script>
