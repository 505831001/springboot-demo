<script type="text/x-template" id="ms-select">
    <el-select v-bind="$props" @change="$emit('change',$event)" v-model="select">
        <el-option v-for='(item,index) in dataList' :key="index" :value="item[dictType?'dictValue':prop.value]"
                   :label="item[dictType?'dictLabel':prop.label]">
           <slot v-bind="item"></slot>
        </el-option>
    </el-select>
</script>
<script>
    (function () {
        var props = Object.assign({
                dictType: String,//字典类型
                url: String,//请求地址
                prop: {
                    type: Object,
                    default:function() {
                        return {
                            value: 'id', //数据值
                            label: 'label'//显示值
                        };
                    }
                }

            },
            Vue.options.components.ElSelect.options.props
        )
        Vue.component('ms-select', {
            template: '#ms-select',
            props: props,
            data: function () {
                return {
                    dataList: [],
                    select: this.value
                }
            },
            watch: {
                select: function (n, o) {
                    if(this.multiple == true){
                        this.$emit("input", this.select.join(','))
                        return
                    }
                    this.$emit("input", n)
                },
                value: function (n, o) {
                   this.init()
                },
            },
            methods: {
                init: function () {
                    //配合 multiple 使用
                    if(this.multiple == true){
                        if(this.value!=null && this.value.length>0){
                            this.select = this.value.split(",")
                        }else {
                            this.select = []
                        }
                    }else {
                        this.select = this.value
                    }
                },
                dictList: function () {
                    var that = this;
                    ms.http.get(ms.base + '/mdiy/dict/list.do', {
                        dictType: this.dictType,
                        pageSize: 99999
                    }).then(function (res) {
                        if(res.result){
                            that.dataList = res.data.rows;
                        }
                    });
                },
                list: function () {
                    var that = this;
                    ms.http.get(this.url, {
                        pageSize: 99999
                    }).then(function (res) {
                        if(res.result){
                            that.dataList = res.data.rows;
                        }
                    });
                },
            },
            created: function () {
                if(this.dictType){//字典类型拉取字典数据
                    this.dictList()
                }else if(this.url){//拉取列表数据
                    this.list()
                }
                //存在逗号则说明需要是多选框需要进行分割
               this.init()
            }
        });

    })()
</script>



