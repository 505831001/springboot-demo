<script type="text/x-template" id="ms-upload">
    <el-upload v-bind="$props"
               :on-success="success"
               :on-remove="remove"
               :on-preview="preview"
               :on-exceed="exceed"
               :file-list="list">
        <slot name="trigger" slot="trigger"></slot>
        <slot name="tip" slot="tip"></slot>
        <slot></slot>
    </el-upload>
</script>
<script>
    (function () {
        var props = Object.assign({
                value:Array,
            },
            Vue.options.components.ElUpload.options.props
        )
        Vue.component('ms-upload', {
            template: '#ms-upload',
            props: props,
            data: function () {
                return {
                    list: [],
                    ms: ms,
                }
            },
            watch: {
                list: function (n, o) {
                    var data;
                    if(n.length){
                        data = JSON.stringify(n)
                    }else {
                        data = null
                    }
                    this.$emit("input",data)
                },
                value: function (n, o) {
                   this.initList()

                },
            },
            methods: {
                success:function(response,file, fileList) {
                    if(response.result){
                        file.url = ms.base+response.data;
                        file.path = file.url;
                        this.list.push(file);
                    }else {
                        this.$notify({
                            title: '失败',
                            message: response.msg,
                            type: 'warning'
                        });
                    }

                },
                preview:function(file) {
                    //调用父组件的方法
                    this.$emit("on-preview", file);
                    window.open(file.url)
                },
                remove:function(file, fileList) {
                    var index = -1;
                    index = this.list.findIndex(function (text) {
                        return text == file;
                    });
                    if (index != -1) {
                        this.list.splice(index, 1);
                    }
                },
                exceed:function(file, fileList) {
                    this.$notify({ title: '当前最多上传'+this.limit+'个附件', type: 'warning' });
                },
                initList:function () {
                    if(this.value){
                        try {
                            this.list = JSON.parse(this.value)
                            this.list.forEach(function(value){
                                if(value.response && value.response.data){
                                    value.url= ms.base + value.response.data
                                }else {
                                    value.url= ms.base + value.path
                                }
                            })
                        }catch (e) {
                            this.list = []
                        }
                    }else {
                        this.list = []
                    }
                }
            },
            created: function () {
               this.initList()
            }
        });

    })()
</script>



