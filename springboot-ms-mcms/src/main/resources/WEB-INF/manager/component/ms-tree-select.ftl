<script type="text/x-template" id="ms-tree-select">
    <div id="tree-select">
    <el-select :value="valueTitle" :clearable="clearable" @clear="clearHandle" ref="tsSselect">
        <el-option :value="valueTitle" :label="valueTitle" class="options">
            <el-tree  id="tree-option"
                      ref="selectTree"
                      default-expand-all
                      :expand-on-click-node="false"
                      :accordion="accordion"
                        :data="options"
                        :props="props"
                        :node-key="props.value"
                        :default-expanded-keys="defaultExpandedKey"
                        @node-click="handleNodeClick">
            </el-tree>
        </el-option>
    </el-select>
    </div>
</script>

<script>
    Vue.component('ms-tree-select', {
        template: "#ms-tree-select",
        name: "ms-el-tree-select",
        props: {
            /* 配置项 */
            props: {
                type: Object,
                default: function () {
                    return {
                        value: 'id',
                        // ID字段名
                        label: 'title',
                        // 显示名称
                        children: 'children' // 子级字段名

                    };
                }
            },

            /* 选项列表数据(树形结构的对象数组) */
            options: {
                type: Array,
                default: function () {
                    return [];
                }
            },

            /* 初始值 */
            value: {
                type: Number,
                default: function () {
                    return null;
                }
            },

            /* 可清空选项 */
            clearable: {
                type: Boolean,
                default: function () {
                    return true;
                }
            },

            /* 自动收起 */
            accordion: {
                type: Boolean,
                default: function () {
                    return false;
                }
            }
        },
        data: function () {
            return {
                valueId: this.value,
                // 初始值
                valueTitle: '',
                defaultExpandedKey: []
            };
        },
        mounted: function () {
            this.initHandle();
        },
        methods: {
            // 初始化值
            initHandle: function () {
                if (this.valueId) {
                    this.valueTitle = this.$refs.selectTree.getNode(this.valueId).data[this.props.label]; // 初始化显示

                    this.$refs.selectTree.setCurrentKey(this.valueId); // 设置默认选中

                    this.defaultExpandedKey = [this.valueId]; // 设置默认展开
                }

                this.initScroll();
            },
            // 初始化滚动条
            initScroll: function () {
                this.$nextTick(function () {
                    var scrollWrap = document.querySelectorAll('.el-scrollbar .el-select-dropdown__wrap')[0];
                    var scrollBar = document.querySelectorAll('.el-scrollbar .el-scrollbar__bar');
                    scrollWrap.style.cssText = 'margin: 0px; max-height: none; overflow: hidden;';
                    scrollBar.forEach(function (ele) {
                        return ele.style.width = 0;
                    });
                });
            },
            // 切换选项
            handleNodeClick: function (node) {
                /*this.valueTitle = node[this.props.label]
                this.valueId = node[this.props.value]*/
                this.$emit('get-value', {
                    'node': node,
                    'dom': this.$refs.tsSselect
                });
                this.defaultExpandedKey = [];
            },
            // 清除选中
            clearHandle: function () {
                this.valueTitle = '';
                this.valueId = null;
                this.defaultExpandedKey = [];
                this.clearSelected();
                this.$emit('get-value', null);
            },

            /* 清空选中样式 */
            clearSelected: function () {
                var allNode = document.querySelectorAll('#tree-option .el-tree-node');
                allNode.forEach(function (element) {
                    return element.classList.remove('is-current');
                });
            }
        },
        watch: {
            value: function () {
                this.valueId = this.value;

                if (this.value == 0) {
                    this.valueTitle = '顶级菜单';
                }

                this.initHandle();
            }
        }
    });
</script>
<style scoped>
.el-scrollbar .el-scrollbar__view .el-select-dropdown__item{
height: auto;
max-height: 274px;
padding: 0;
overflow: hidden;
overflow-y: auto;
}
.el-select-dropdown__item.selected{
font-weight: normal;
}
ul li >>>.el-tree .el-tree-node__content{
height:auto;
padding: 0 20px;
}
.el-tree-node__label{
font-weight: normal;
}
.el-tree >>>.is-current .el-tree-node__label{
color: #409EFF;
font-weight: 700;
}
.el-tree >>>.is-current .el-tree-node__children .el-tree-node__label{
color:#606266;
font-weight: normal;
}
</style>
