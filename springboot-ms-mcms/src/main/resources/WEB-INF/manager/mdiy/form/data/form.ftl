<!DOCTYPE html>
<html>
<head>
    <title>自定义表单</title>
    <#include "../../include/head-file.ftl">
    <script src="${base}/static/mdiy/index.js"></script>
</head>
<body>
<div id="form" v-loading="loading" v-cloak>
    <el-header class="ms-header ms-tr" height="50px">
        <el-button type="primary" icon="iconfont icon-baocun" size="mini" @click="save()" :loading="saveDisabled">保存</el-button>
        <el-button size="mini"  icon="iconfont icon-fanhui" plain onclick="javascript:history.go(-1)">返回</el-button>
    </el-header>
    <el-main class="ms-container" >
        <div id="formModel"> </div>
    </el-main>
</div>
</body>
</html>
<script>
    var form = new Vue({
        el: '#form',
        data:function() {
            return {
                loading:false,
                saveDisabled: false,
                //自定义模型实例
                formModel: undefined,
                form:{},
            }
        },
        methods: {
            save: function () {
                var that = this;
                that.saveDisabled = true;
                    that.formModel.save(function (res) {
                        if(res.result){
                            that.$notify({
                                title: '成功',
                                type: 'success',
                                message: '保存成功!'
                            });
                            window.history.go(-1);
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
        created:function() {
            var that = this;
            this.form.id = ms.util.getParameter("id");
            this.loading = true;
            ms.mdiy.model.form("formModel", {"modelName":ms.util.getParameter("modelName")},{id:that.form.id},true).then(function(obj) {
                that.formModel = obj;
            });;
            this.loading = false;
        }
    });
</script>
