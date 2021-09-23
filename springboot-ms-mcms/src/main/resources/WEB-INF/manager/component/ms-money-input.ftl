<script id="ms-money-input" type="x/template">
    <el-input
            :maxlength="maxlength"
            ref="input"
            v-model="formatValue"
            @focus="selectAll"
            @blur="onBlur"
            @change="$emit('change',$event)"
            :disabled="disabled"
            :placeholder="placeholder"
            :size="size"
    ></el-input>
</script>
<script>
    Vue.component("ms-money-input", {
        template: "#ms-money-input",
        props: {
            value: {
                default: 0,
                desc:'数值'
            },
            decimal:{
                type:Number,
                default:2,
                desc:'小数位'
            },
            placeholder:{
                type:String,
                default:"请输入金额"
            },
            size:{
                type:String,
                default:""
            },
            disabled:{
                type:Boolean,
                default:false
            },
            maxlength:{
                type:Number,
                default: 12
            }
        },
        data: function () {
            return {
                focused:false,
                val:this.value,
            };
        },
        watch:{
            val: function (n) {
                this.$emit('input',n?accounting.unformat(n):n);
            },
            value: function (n) {
                this.val = n
            }
        },
        computed:{
            formatValue:{
                get: function(){
                    if(this.focused){
                        var x = this.val;
                        var y = String(x).indexOf(".") + 1;//获取小数点的位置
                        var count = String(x).length - y;//获取小数点后的个数
                        if(y > 0 && count >2) {
                            return this.val = (parseInt(this.val*100)/100).toFixed(this.decimal)
                        } else {
                            return this.val;
                        }
                    }else{
                        if(!isNaN(Number.parseFloat(this.val))){
                            return accounting.formatMoney(this.val,"￥",this.decimal);
                        }else {
                            return ''
                        }
                    }
                },
                set: function(value){
                    if(value && !isNaN(value)){
                        this.val = value;
                    } else {
                        this.val = "";
                    }

                }
            }
        },
        methods: {
            onBlur: function () {
                this.focused = false;
            },
            selectAll: function (event) {
                this.focused = true;
                setTimeout(function () {
                    event.target.select()
                }, 0)
            }
        },
        mounted: function () {
        }
    });
</script>
