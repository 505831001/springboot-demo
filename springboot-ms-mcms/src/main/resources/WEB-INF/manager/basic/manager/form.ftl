<el-dialog id="form" :visible.sync="dialogVisible" width="50%" v-cloak>
        <template slot="title">
            <i class="el-icon-user-solid">管理员
        </template>
            <el-form ref="form" :model="form" :rules="rules" label-width="120px" size="mini">
            <el-form-item  label="管理员名" prop="managerName">
                    <el-input v-model="form.managerName"
                          :disabled="nameDisabled"
                          :style="{width:  '100%'}"
                          :clearable="true"
                          placeholder="请输入管理员名">

                </el-input>
                <div class="ms-form-tip">管理员名必须填写，长度为6-12个字符！</div>
            </el-form-item>

            <el-form-item  label="管理员昵称" prop="managerNickName">
                    <el-input v-model="form.managerNickName"
                          :disabled="false"
                          :style="{width:  '100%'}"
                          :clearable="true"
                          placeholder="请输入管理员昵称">
                </el-input>
                <div class="ms-form-tip">管理员昵称必须填写，长度为1-12个字符！</div>
            </el-form-item>
            <el-form-item  label="管理员密码" prop="managerPassword">
                    <el-input v-model="form.managerPassword"
                          type="password"
                          :disabled="false"
                          :style="{width:  '100%'}"
                          :clearable="true"
                          placeholder="请输入管理员密码">
                </el-input>
                <div class="ms-form-tip">管理员密码必须填写，长度为6-30个字符！</div>
            </el-form-item>
            <el-form-item  label="角色名称" prop="roleId">
                        <el-select v-model="form.roleId"
                               :style="{width: ''}"
                               :filterable="false"
                               :disabled="false"
                               :multiple="false" :clearable="true"
                               placeholder="请选择角色名称">
                        <el-option v-for='item in managerRoleidOptions' :key="item.id" :value="item.id"
                                   :label="item.roleName"></el-option>
                    </el-select>
                    <div class="ms-form-tip">角色名称必须填写</div>
            </el-form-item>
            </el-form>   <div slot="footer">
        <el-button size="mini" @click="dialogVisible = false">取 消</el-button>
        <el-button size="mini" type="primary" @click="save()" :loading="saveDisabled">保 存</el-button>
    </div>
</el-dialog>
<script>
    var form = new Vue({
        el: '#form',
        data: function () {
            return {
                saveDisabled: false,
                dialogVisible: false,
                nameDisabled: false,
                //表单数据
                form: {
                    // 管理员名
                    managerName: '',
                    // 管理员昵称
                    managerNickName: '',
                    // 管理员密码
                    managerPassword: '',
                    // 角色名称
                    roleId: ''
                },
                managerRoleidOptions: [],
                rules: {
                    // 管理员名
                    managerName: [{
                        "required": true,
                        "message": "管理员名必须填写"
                    }, {
                        min: 6,
                        max: 12,
                        message: '管理员名长度为6-12个字符!',
                        trigger: 'change'
                    }],
                    // 管理员昵称
                    managerNickName: [{
                        "required": true,
                        "message": "管理员昵称必须填写"
                    }, {
                        min: 1,
                        max: 12,
                        message: '管理员昵称长度为1-12个字符！',
                        trigger: 'change'
                    }],
                    // 管理员密码
                    managerPassword: [{
                        "required": true,
                        "message": "管理员密码必须填写"
                    }, {
                        min: 6,
                        max: 30,
                        message: '管理员密码长度为6-30个字符!',
                        trigger: 'change'
                    }],
                    // 角色名称
                    roleId: [{
                        "required": true,
                        "message": "角色名称必须填写"
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
                this.nameDisabled = false;
                this.managerRoleidOptionsGet();

                if (id) {
                    this.get(id);
                }

                this.$nextTick(function () {
                    this.dialogVisible = true;
                });
            },
            save: function () {
                var that = this;
                var url = ms.manager + "/basic/manager/save.do";

                if (that.form.id > 0) {
                    url = ms.manager + "/basic/manager/update.do"; //更新时密码不必填

                    this.rules.managerPassword[0].required = false;
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
                                that.dialogVisible = false;
                                that.rules.managerPassword[0].required = true;
                                that.form.id = 0;
                                indexVue.list();
                            } else {
                                that.$notify({
                                    title: '失败',
                                    message: data.msg,
                                    type: 'warning'
                                });
                                that.saveDisabled = false;
                            }
                        });
                    } else {
                        return false;
                    }
                });
            },
            //获取当前管理员管理
            get: function (id) {
                var that = this;
                ms.http.get(ms.manager + "/basic/manager/get.do", {
                    "id": id
                }).then(function (data) {
                    if (data.data.id) {
                        that.form = data.data;
                        that.form.roleId += "";
                        if (that.form.roleId == "0") {
                            that.form.roleId = null;
                        }
                        if (that.form.id) {
                            that.nameDisabled = true;
                        }
                    }
                });
            },
            //获取managerRoleid数据源
            managerRoleidOptionsGet: function () {
                var that = this;
                ms.http.get(ms.manager + "/basic/role/list.do?pageSize=9999", {}).then(function (data) {
                    that.managerRoleidOptions = data.data.rows;
                });
            }
        },
        created: function () {}
    });
</script>
