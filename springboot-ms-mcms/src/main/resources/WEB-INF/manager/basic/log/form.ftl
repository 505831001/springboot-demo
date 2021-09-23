<!DOCTYPE html>
<html>
<head>
	 <title>系统日志</title>
     <#include "../../include/head-file.ftl">
</head>
<body>
	<div id="form" v-loading="loading" v-cloak>
		<el-header class="ms-header ms-tr" height="50px">
			<el-button size="mini" icon="iconfont icon-fanhui" plain onclick="javascript:history.go(-1)">返回</el-button>
		</el-header>
		<el-main class="ms-container">
            <el-form ref="form" :model="form" :rules="rules" label-width="120px" label-position="right" size="small">
                        <el-row
                                :gutter="0"
                                justify="start" align="top">
                                <el-col :span="12">

        <el-form-item  label="标题" prop="logTitle">
                <el-input
                        v-model="form.logTitle"
                        :disabled="true"
                          :style="{width:  '100%'}"
                          :clearable="true"
                        placeholder="请输入标题">
                </el-input>
        </el-form-item>
                                </el-col>
                                <el-col :span="12">

        <el-form-item  label="IP" prop="logIp">
                <el-input
                        v-model="form.logIp"
                        :disabled="true"
                          :style="{width:  '100%'}"
                          :clearable="true"
                        placeholder="请输入IP">
                </el-input>
        </el-form-item>
                                </el-col>
                        </el-row>
                        <el-row
                                :gutter="0"
                                justify="start" align="top">
                                <el-col :span="12">

        <el-form-item  label="请求方法" prop="logMethod">
                <el-input
                        v-model="form.logMethod"
                        :disabled="true"
                          :style="{width:  '100%'}"
                          :clearable="true"
                        placeholder="请输入请求方法">
                </el-input>
        </el-form-item>
                                </el-col>
                                <el-col :span="12">

        <el-form-item  label="请求方式" prop="logRequestMethod">
                    <el-select  v-model="form.logRequestMethod"
                               :style="{width: '100%'}"
                               :filterable="false"
                               :disabled="true"
                               :multiple="false" :clearable="true"
                            placeholder="请选择请求方式">
                        <el-option v-for='item in logRequestMethodOptions' :key="item.value" :value="item.value"
                                   :label="item.value"></el-option>
                    </el-select>
        </el-form-item>
                                </el-col>
                        </el-row>
                        <el-row
                                :gutter="0"
                                justify="start" align="top">
                                <el-col :span="12">

        <el-form-item  label="请求地址" prop="logUrl">
                <el-input
                        v-model="form.logUrl"
                        :disabled="true"
                          :style="{width:  '100%'}"
                          :clearable="true"
                        placeholder="请输入请求地址">
                </el-input>
        </el-form-item>
                                </el-col>
                                <el-col :span="12">

        <el-form-item  label="请求状态" prop="logStatus">
                    <el-select  v-model="form.logStatus"
                               :style="{width: '100%'}"
                               :filterable="false"
                               :disabled="true"
                               :multiple="false" :clearable="true"
                            placeholder="请选择请求状态">
                        <el-option v-for='item in logStatusOptions' :key="item.value" :value="item.value"
                                   :label="item.label"></el-option>
                    </el-select>
        </el-form-item>
                                </el-col>
                        </el-row>
                        <el-row
                                :gutter="0"
                                justify="start" align="top">
                                <el-col :span="12">

        <el-form-item  label="业务类型" prop="logBusinessType">
                    <el-select  v-model="form.logBusinessType"
                               :style="{width: '100%'}"
                               :filterable="false"
                               :disabled="true"
                               :multiple="false" :clearable="true"
                            placeholder="请选择业务类型">
                        <el-option v-for='item in logBusinessTypeOptions' :key="item.value" :value="item.value"
                                   :label="item.label"></el-option>
                    </el-select>
        </el-form-item>
                                </el-col>
                                <el-col :span="12">

        <el-form-item  label="用户类型" prop="logUserType">
                    <el-select  v-model="form.logUserType"
                               :style="{width: '100%'}"
                               :filterable="false"
                               :disabled="true"
                               :multiple="false" :clearable="true"
                            placeholder="请选择用户类型">
                        <el-option v-for='item in logUserTypeOptions' :key="item.value" :value="item.value"
                                   :label="item.label"></el-option>
                    </el-select>
        </el-form-item>
                                </el-col>
                        </el-row>
                        <el-row
                                :gutter="0"
                                justify="start" align="top">
                                <el-col :span="12">

        <el-form-item  label="操作人员" prop="logUser">
                <el-input
                        v-model="form.logUser"
                        :disabled="true"
                          :style="{width:  '100%'}"
                          :clearable="true"
                        placeholder="请输入操作人员">
                </el-input>
        </el-form-item>
                                </el-col>
                                <el-col :span="12">

        <el-form-item  label="所在地区" prop="logLocation">
                <el-input
                        v-model="form.logLocation"
                        :disabled="true"
                          :style="{width:  '100%'}"
                          :clearable="true"
                        placeholder="请输入所在地区">
                </el-input>
        </el-form-item>
                                </el-col>
                        </el-row>
                        <el-row
                                :gutter="0"
                                justify="start" align="top">
                                <el-col :span="12">

        <el-form-item  label="创建时间" prop="logUser">
                <el-input
                        v-model="form.createDate"
                        :disabled="true"
                          :style="{width:  '100%'}"
                          :clearable="true">
                </el-input>
        </el-form-item>
                                </el-col>
                        </el-row>
                        <el-row
                                :gutter="0"
                                justify="start" align="top">
                                <el-col :span="12">

        <el-form-item  label="请求参数" prop="logParam">
                <el-input
                        type="textarea" :rows="5"
                        :disabled="true"
                        v-model="form.logParam"
                        :style="{width: '100%'}"
                        placeholder="请输入请求参数">
                </el-input>
        </el-form-item>
                                </el-col>
                                <el-col :span="12">

        <el-form-item  label="返回参数" prop="logResult">
                <el-input
                        type="textarea" :rows="5"
                        :disabled="true"
                        v-model="form.logResult"
                        :style="{width: '100%'}"
                        placeholder="请输入返回参数">
                </el-input>
        </el-form-item>
                                </el-col>
                        </el-row>

        <el-form-item  label="错误消息" prop="logErrorMsg">
                <el-input
                        type="textarea" :rows="5"
                        :disabled="true"
                        v-model="form.logErrorMsg"
                        :style="{width: '100%'}"
                        placeholder="请输入错误消息">
                </el-input>
        </el-form-item>
            </el-form>
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
                //表单数据
                form: {
                    // 标题
                    logTitle:'',
                    // IP
                    logIp:'',
                    // 请求方法
                    logMethod:'',
                    // 请求方式
                    logRequestMethod:'',
                    // 请求地址
                    logUrl:'',
                    // 请求状态
                    logStatus:'',
                    // 业务类型
                    logBusinessType:'insert',
                    // 用户类型
                    logUserType:'',
                    // 操作人员
                    logUser:'',
                    // 所在地区
                    logLocation:'',
                    // 请求参数
                    logParam:'',
                    // 返回参数
                    logResult:'',
                    // 错误消息
                    logErrorMsg:'',
                },
                logRequestMethodOptions:[{"value":"get"},{"value":"post"},{"value":"put"}],
                logStatusOptions:[{"value":"success","label":"成功"},{"value":"error","label":"失败"}],
                logBusinessTypeOptions:[{"value":"insert","label":"新增"},{"value":"delete","label":"删除"},{"value":"update","label":"修改"},{"value":"other","label":"其他"}],
                logUserTypeOptions:[{"value":"other","label":"其他"},{"value":"manage","label":"管理员"},{"value":"people","label":"会员"}],
                rules:{
                },

            }
        },
        watch:{
        },
        computed:{
        },
        methods: {
            save:function() {
                var that = this;
                var url = ms.manager + "/basic/log/save.do"
                if (that.form.id > 0) {
                    url = ms.manager + "/basic/log/update.do";
                }
                this.$refs.form.validate(function(valid) {
                    if (valid) {
                        that.saveDisabled = true;
                        var data = JSON.parse(JSON.stringify(that.form));
                        ms.http.post(url, data).then(function (data) {
                            if (data.result) {
                                that.$notify({
                                    title: "成功",
                                    message: "保存成功",
                                    type: 'success'
                                });
                                location.href = ms.manager + "/basic/log/index.do";
                            } else {
                                that.$notify({
                                    title: "错误",
                                    message: data.msg,
                                    type: 'warning'
                                });
                            }
                            that.saveDisabled = false;
                        });
                    } else {
                        return false;
                    }
                })
            },

            //获取当前系统日志
            get:function(id) {
                var that = this;
                this.loading = true
                ms.http.get(ms.manager + "/basic/log/get.do", {"id":id}).then(function (res) {
                    that.loading = false
                    if(res.result&&res.data){
                        that.form = res.data;
                    }
                });
            },
        },
        created:function() {
            var that = this;
            this.form.id = ms.util.getParameter("id");
            if (this.form.id) {
                this.get(this.form.id);
            }
        }
    });
</script>
