<!-- 扩展elementUI 数据源，支持普通数据列表 -->
<script type="text/x-template" id="ms-cascader">
    <el-cascader v-bind="$props"
                 :options="dataList"
                 v-model="select">
    </el-cascader>
</script>
<script>
    (function () {
        var props = Object.assign({
                url: String,//请求地址
            },
            Vue.options.components.ElCascader.options.props
        )
        Vue.component('ms-cascader', {
            template: '#ms-cascader',
            props: props,
            data: function () {
                return {
                    dataList: [],
                    select: this.value
                }
            },
            watch: {
                select: function (n, o) {
                    this.$emit("input", n)
                },
                value: function (n, o) {
                    this.select = n
                },
            },
            methods: {
                list: function () {
                    var that = this;
                    ms.http.get(this.url, {
                        pageSize: 99999
                    }).then(function (res) {
                        if(res.result){
                            that.dataList =  ms.util.treeData(res.data.rows,that.props.value,that.props.children,'children');
                        }else {
                            that.dataList = []
                        }
                    });
                },
            },
            created: function () {
                this.list()
            }
        });

    })()
</script>



