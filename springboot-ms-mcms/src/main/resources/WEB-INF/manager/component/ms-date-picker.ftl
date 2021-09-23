<script type="text/x-template" id="ms-date-picker">
    <el-date-picker v-bind="$props" v-model="time">
    </el-date-picker>
</script>
<script>
    (function () {
        var props = Object.assign({
                startDate: {},//开始时间
                endDate: {},//结束时间
            },
            Vue.options.components.ElDatePicker.options.props
        )
        Vue.component('ms-date-picker', {
            template: '#ms-date-picker',
            props: props,
            data: function () {
                return {
                    time: [],
                }
            },
            watch: {
                time: function (n, o) {
                    if(this.type =='datetimerange'||this.type =='daterange'){
                        if(n){
                            this.$emit('update:startDate', n[0]);
                            this.$emit('update:endDate', n[1]);
                        }else {
                            this.$emit('update:startDate','');
                            this.$emit('update:endDate', '');
                        }
                    }else {
                        this.$emit('input', n);
                    }


                },
                startDate: function (n, o) {
                    this.initTime()
                },
                endDate: function (n, o) {
                  this.initTime()
                },
            },
            methods: {
                initTime: function(){
                    if(this.type =='datetimerange'||this.type =='daterange'){
                        if (typeof this.startDate == 'number') {
                            this.startDate = ms.util.date.fmt(this.startDate, this.valueFormat || 'yyyy-MM-dd')
                        }
                        if (typeof this.endDate == 'number') {
                            this.endDate = ms.util.date.fmt(this.endDate, this.valueFormat || 'yyyy-MM-dd')
                        }
                        this.time = [this.startDate||'',this.endDate||'']
                    }else if(this.type =='date') {
                        this.time = ms.util.date.fmt(this.value, this.valueFormat || 'yyyy-MM-dd')
                    }else {
                        if (typeof this.value == 'number') {
                            this.time = ms.util.date.fmt(this.value, this.valueFormat || 'yyyy-MM-dd')
                        }
                    }

                }
            },
            created: function () {
                this.initTime()

            }
        });

    })()
</script>



