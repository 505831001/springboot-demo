<el-dialog v-cloak id="form" title="自定义页面" :visible.sync="dialogVisible" width="50%">
    <el-form ref="form" :model="form" :rules="rules" label-width="130px" size="mini">
        <el-row>
            <el-col :span="24">
                <el-form-item  label="标题" prop="pageTitle">
                    <el-input v-model="form.pageTitle"
                              :disabled="false"
                              :style="{width:  '100%'}"
                              :clearable="true"
                              placeholder="请输入自定义页面标题">
                    </el-input>
                </el-form-item>
            </el-col></el-row>
        <el-row>
            <el-row>
                <el-col span="24">
                    <el-form-item  label="分类" prop="pageType">
<#--                        <template slot='label'>分类-->
<#--                            <el-popover placement="top-start" title="提示" trigger="hover" content="类型不满足可以在自定义字典菜单中新增,字段类型为“自定义页面类型”">-->
<#--                                <i class="el-icon-question" slot="reference"></i>-->
<#--                            </el-popover>-->
<#--                        </template>-->
                        <el-select v-model="form.pageType"
                                   :style="{width: '100%'}"
                                   :filterable="false"
                                   :disabled="false"
                                   :multiple="false" :clearable="true"
                                   placeholder="请选择分类">
                            <el-option v-for='item in pageTypeOptions' :key="item.dictValue" :value="item.dictValue"
                                       :label="item.dictLabel"></el-option>
                        </el-select>
                        <div class="ms-form-tip">
                            可以通过 <b>自定义字典</b> 配置，根据不同的业务模块定义，方便业务开发管理。
                        </div>

                    </el-form-item>
                </el-col>
            </el-row>
            <el-col :span="24">
                <el-form-item  label="绑定模板" prop="pagePath">
                    <el-select v-model="form.pagePath"
                               :style="{width:  '100%'}"
                               :disabled="false"
                               filterable
                               :multiple="false" :clearable="true"
                               placeholder="请选择绑定模板">
                        <el-option v-for='item in pagePathOptions' :key="item" :value="item"
                                   :label="item"></el-option>
                    </el-select>
                </el-form-item>
            </el-col>
        </el-row>

        <el-form-item  label="" prop="pageKey">
            <template slot='label'>路径关键字
<#--                <el-popover placement="top-start" title="提示" trigger="hover">-->
<#--                    路径关键字决定了访问地址的路径<br/>-->
<#--                    例如：输入"login"对应访问的地址为 "域名/mdiyPage/login.do"，<br/>-->
<#--                    特別注意：会员登录后访问的路径，路径关键字必须带有"people/"前缀<br/>-->
<#--                    如：个人中心，路径关键字"people/center",对应访问地址："域名/people/center.do"-->
<#--                    <i class="el-icon-question" slot="reference"></i>-->
<#--                </el-popover>-->
            </template>
            <el-input v-model="form.pageKey"
                      :disabled="false"
                      :style="{width:  '100%'}"
                      :clearable="true"
                      placeholder="请输入自定义页面访问路径">
            </el-input>
            <div class="ms-form-tip">
                路径关键字决定了访问地址的路径<br/>
                例如：输入"login"对应访问的地址为 "域名/mdiyPage/login.do"，<br/>
                注意：会员登录后访问的路径，必须带有"people/"前缀<br/>
                如：个人中心"people/center",对应访问地址："域名/people/center.do"
            </div>

        </el-form-item>
    </el-form>  <div slot="footer">
        <el-button size="mini" @click="dialogVisible = false">取 消</el-button>
        <el-button size="mini" type="primary" @click="save()" :loading="saveDisabled">保存</el-button>
    </div>
</el-dialog>
<script>
    var form = new Vue({
        el: '#form',
        data: function () {
            return {
                saveDisabled: false,
                dialogVisible: false,
                //表单数据
                form: {
                    pageType: '',
                    // 自定义页面标题
                    pageTitle: '',
                    // 绑定模板
                    pagePath: '',
                    // 自定义页面访问路径
                    pageKey: ''
                },
                pagePathOptions: [],
                pageTypeOptions: [],
                rules: {
                    pageTitle: [{
                        "required": true,
                        "message": "标题必须填写"
                    }, {
                        "min": 1,
                        "max": 30,
                        "message": "标题长度必须为1-30"
                    }],
                    // 绑定模板
                    pagePath: [{
                        "required": true,
                        "message": "绑定模板必须填写"
                    }],
                    // 访问路径
                    pageKey: [{
                        "required": true,
                        "message": "访问路径必须填写"
                    }, {
                        "min": 1,
                        "max": 300,
                        "message": "访问路径长度必须为1-300"
                    }],
                    // 分类
                    pageType: [{
                        "required": true,
                        "message": "请选择分类"
                    }]
                }
            };
        },
        watch: {
            dialogVisible: function (v) {
                if (!v) {
                    this.$refs.form.resetFields();
                    this.form.id = 0;
                }
            }
        },
        computed: {},
        methods: {
            open: function (id) {
                if (id) {
                    this.get(id);
                }

                this.pagePathOptionsGet();
                this.pageTypeOptionsGet();
                this.$nextTick(function () {
                    this.dialogVisible = true;
                });
            },
            save: function () {
                var that = this;
                var url = ms.manager + "/mdiy/page/save.do";

                if (that.form.id > 0) {
                    url = ms.manager + "/mdiy/page/update.do";
                }

                this.$refs.form.validate(function (valid) {
                    if (valid) {
                        that.saveDisabled = true;
                        var data = JSON.parse(JSON.stringify(that.form));
                        ms.http.post(url, data).then(function (data) {
                            if (data.result) {
                                that.$notify({
                                    title: '成功',
                                    message: '保存成功',
                                    type: 'success'
                                });
                                that.saveDisabled = false;
                                indexVue.list();
                            }else {
                                that.$notify({
                                    title: '失败',
                                    message: data.msg,
                                    type: 'warning'
                                });
                                that.saveDisabled = false;
                            }
                            that.dialogVisible = false;
                        });
                    } else {
                        return false;
                    }
                });
            },
            //获取当前自定义页面
            get: function (id) {
                var that = this;
                ms.http.get(ms.manager + "/mdiy/page/get.do", {
                    "id": id
                }).then(function (data) {
                    if (data.result) {
                        that.form = data.data;
                    }
                });
            },
            //获取pagePath数据源
            pagePathOptionsGet: function () {
                var that = this;
                ms.http.get(ms.manager + "/template/queryTemplateFileForColumn.do", {}).then(function (data) {
                    that.pagePathOptions = data.data;
                });
            },
            //获取pageType数据源
            pageTypeOptionsGet: function () {
                var that = this;
                ms.http.get(ms.base + '/mdiy/dict/list.do', {
                    dictType: '自定义页面类型',
                    pageSize: 99999
                }).then(function (data) {
                    if(data.result){
                        data = data.data;
                        that.pageTypeOptions = data.rows;
                    }
                });
            }
        },
        created: function () {}
    });
</script>
