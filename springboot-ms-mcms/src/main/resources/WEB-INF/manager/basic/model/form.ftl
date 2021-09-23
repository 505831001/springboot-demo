<el-dialog id="form" :title="(addEditForm.modelIsMenu == 1) ? '菜单编辑' : '按钮编辑'" :visible.sync="dialogVisible" width="50%" v-cloak>
    <template slot="title">
        <i class="el-icon-menu">菜单编辑
    </template>
    <el-form ref="addEditForm" :model="addEditForm" :rules="rules" label-width="110px" size="mini">
        <el-form-item prop="modelIsMenu">
            <template slot='label'>菜单类型</template>
            <el-radio-group v-model="addEditForm.modelIsMenu">
                <el-radio :label="1">导航链接</el-radio>
                <el-radio :label="0">功能权限</el-radio>
            </el-radio-group>
            <div class="ms-form-tip">
                <b>导航连接</b>会显示在菜单中，<b>功能权限</b>用来控制页面中按钮功能权限
            </div>
        </el-form-item>
        <el-form-item  label="父级菜单" prop="modelId">
            <ms-tree-select ref="treeselect"
                            :props="props"
                            :options="modelList"
                            :value="addEditForm.modelId"
                            :clearable="isClearable"
                            :accordion="isAccordion"
                            @get-value="getValue($event)"></ms-tree-select>
            <div class="ms-form-tip"  >
                不能选择功能菜单类型,不能选择自身为父栏目
            </div>
        </el-form-item>
        <el-form-item  label="标题" prop="modelTitle">
            <el-input v-model="addEditForm.modelTitle" placeholder="请输入标题"></el-input>
            <div class="ms-form-tip">标题不能为空</div>
        </el-form-item>


        <el-form-item prop="modelUrl">
            <template slot='label'>{{(addEditForm.modelIsMenu==1) ? '链接地址' : '权限标识'}}</template>
            <el-input v-model="addEditForm.modelUrl" :placeholder="(addEditForm.modelIsMenu==1) ? '请输入链接地址' : '请输入权限标识'"></el-input>
            <div class="ms-form-tip"  v-if="addEditForm.modelIsMenu == 1">
                导航链接如“model/index.do”
            </div>
            <div v-if="addEditForm.modelIsMenu == 0" class="ms-form-tip">
                推荐格式：业务名:update,业务名:del,业务名:view<br>
                控制层:	@RequiresPermissions("业务名:update") <br/>
                视图层：<#noparse><@shiro.hasPermission name="业务名:update"></#noparse> <#noparse> </@shiro.hasPermission></#noparse>
            </div>
        </el-form-item>
        <el-form-item  label="图标" prop="modelIcon" v-if="addEditForm.modelIsMenu == 1">
            <el-col :span="6">
                <ms-icon v-model="addEditForm.modelIcon"></ms-icon>
            </el-col>
        </el-form-item>
        <el-form-item prop="modelSort" label="排序" v-if="addEditForm.modelIsMenu == 1">
            <el-col :span="6">
                <el-input v-model.number="addEditForm.modelSort" maxlength="11" placeholder="请输入排序"></el-input>
                <div class="ms-form-tip" v-if="addEditForm.modelIsMenu == 1">
                    导航菜单倒序排序
                </div>
            </el-col>
        </el-form-item>
        <el-form-item label="扩展业务"  prop="isChild" v-if="addEditForm.modelIsMenu == 1">
            <el-input v-model="addEditForm.isChild"
                      :disabled="false"
                      :style="{width: '100%'}"
                      :clearable="true"
                      placeholder="请输入系统扩展">
            </el-input>
            <div class="ms-form-tip">
              例如：业务A定义菜单，业务B定义菜单，通过接口增加参数的方式可以区分开，也减少了重复开发菜单的工作<br/>
            </div>
        </el-form-item>
    </el-form>
    <div slot="footer">
        <el-button size="mini" @click="dialogVisible = false">取 消</el-button>
        <el-button size="mini" type="primary" @click="save()" :loading="saveDisabled">保 存</el-button>
    </div>
</el-dialog>
<script>
    var form = new Vue({
        el: '#form',
        data: function () {
            return {
                isClearable: false,
                // 可清空（可选）
                isAccordion: true,
                // 可收起（可选）
                modelTitle: '',
                props: {
                    // 配置项（必选）
                    value: 'id',
                    label: 'modelTitle',
                    children: 'children' // disabled:true

                },
                modelList: [],
                //菜单数据
                modeldata: [],
                saveDisabled: false,
                dialogVisible: false,
                //表单数据
                addEditForm: {
                    id: 0,
                    modelId: 0,
                    modelTitle: '',
                    modelIcon: '',
                    modelUrl: '',
                    isChild: '',
                    modelCode:'',
                    modelSort: 0,
                    modelIsMenu: 1
                },
                rules: {
                    modelTitle: [{
                        required: true,
                        message: '请输入标题',
                        trigger: 'blur'
                    }, {
                        min: 1,
                        max: 20,
                        message: '长度不能超过20个字符',
                        trigger: 'change'
                    }],
                    modelUrl: [{
                        min: 0,
                        max: 100,
                        message: '长度不能超过100个字符',
                        trigger: 'change'
                    }],
                    // modelCode:[{min: 0, max: 20, message: '长度不能超过20个字符', trigger: 'change'}],
                    modelSort: [{
                        type: 'number',
                        message: '排序必须为数字值'
                    }],
                    isChild: [{
                        min: 0,
                        max: 100,
                        message: '长度不能超过100个字符',
                        trigger: 'change'
                    }]
                }
            };
        },
        watch: {
            'dialogVisible': function (n, o) {
                if (!n) {
                    this.$refs.addEditForm.resetFields();
                }
            },
            'modeldata': function (n, o) {
                if (n) {
                    this.modelList.push({
                        modelTitle: '顶级菜单',
                        id: 0,
                        children: this.modeldata
                    });
                }
            }
        },
        methods: {
            open: function (id) {
                this.addEditForm.id = 0;
                this.addEditForm.modelId = '';
                this.addEditForm.modelCode = '';

                if (id > 0) {
                    this.get(id);
                }

                this.$nextTick(function () {
                    this.dialogVisible = true;
                });
            },
            save: function () {
                var that = this;
                var url = ms.manager + "/model/save.do";

                if (that.addEditForm.id > 0) {
                    url = ms.manager + "/model/update.do";
                } //按钮没有图标


                if (that.addEditForm.modelIsMenu == 0) {
                    that.addEditForm.modelIcon = '';
                }

                that.$refs.addEditForm.validate(function (valid) {
                    if (valid) {
                        that.saveDisabled = true;

                        if (!that.addEditForm.modelId) {
                            delete that.addEditForm.modelId;
                        }

                        var data = JSON.parse(JSON.stringify(that.addEditForm));
                        delete data.modelChildList;
                        ms.http.post(url, data).then(function (data) {
                            if (data.result) {
                                that.$notify({
                                    title: '成功',
                                    message: '保存成功',
                                    type: 'success'
                                });
                                that.saveDisabled = false;
                                that.dialogVisible = false;
                                window.location.href = ms.manager + "/model/index.do";
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
            getValue: function (data) {
                if (data.node.modelParentIds != null) {
                    var parentIndex = data.node.modelParentIds.split(",").indexOf(this.addEditForm.id);
                    if (parentIndex > -1) {
                        this.$notify({
                            title: '提示',
                            message: '不能把自身的子菜单作为父级菜单',
                            type: 'info'
                        });
                        return false;
                    }
                }
                if (data.node.id == this.addEditForm.id && data.node.id!=0) {
                    this.$notify({
                        title: '提示',
                        message: '不能把自身作为父级菜单',
                        type: 'info'
                    });
                    return false;
                }
                if (data.node.modelIsMenu == 0) {
                    this.$notify({
                        title: '提示',
                        message: '不能将功能按钮添加为菜单',
                        type: 'info'
                    });
                } else {
                    this.addEditForm.modelId = data.node.id;
                    this.$refs.treeselect.valueId = data.node[this.props.value];
                    this.$refs.treeselect.valueTitle = data.node[this.props.label];
                    data.dom.blur();
                }
            },
            //获取当前任务
            get: function (id) {
                var that = this;
                ms.http.get(ms.manager + "/model/get.do", {
                    id: id
                }).then(function (data) {
                    if (data.result) {
                        that.addEditForm = data.data.model;
                        delete that.addEditForm.modelDatetime;
                    }
                });
            }
        },
        created: function () {}
    });
</script>
<style>
    #form .el-select{
        width: 100%;
    }
</style>
