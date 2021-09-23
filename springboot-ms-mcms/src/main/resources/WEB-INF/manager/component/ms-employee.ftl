<script type="text/x-template" id="ms-employee">
    <el-cascader v-bind="$props"
                 @change="$emit('change',$event)"
                 :options="dataList"
                 :props="dataProps"
                 v-model="select">
    </el-cascader>
</script>
<script>
    (function () {
        Vue.component('ms-employee', {
            template: '#ms-employee',
            props: Object.assign({
                    emitPath: {
                        type: Boolean,
                        default: function() {
                            return false;
                        }
                    },url:String,
                    multiple:{
                        type:Boolean,
                        default: function() {
                            return false;
                        }
                    },
                },
                Vue.options.components.ElCascader.options.props
            ),
            data: function () {
                return {
                    dataList: [],
                    select: this.value,
                    dataProps:{
                        'multiple':this.multiple,
                        "emitPath":this.emitPath,
                        "checkStrictly":false,
                        "expandTrigger":"hover",
                    },
                }
            },
            watch: {
                select: function (n, o) {
                    this.$emit("input", n)
                },
                value: function (n, o) {
                    this.select = n
                },
                url: function (n, o) {
                    this.list(n)
                },
            },
            methods: {
                list: function (url) {
                    var that = this;
                    ms.http.get(
                        url,
                        {employeeStatus:"in"}
                    ).then(function (res) {
                        if(res.result){
                            that.dataList =  res.data;
                        }else {
                            that.dataList = []
                        }
                    });
                },
            },
            created: function () {
                this.list(this.url?this.url:(ms.manager + "/organization/employee/getPostEmployeeBeans.do"))
            }
        });

    })()
</script>
