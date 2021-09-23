<!-- option 参考百度图表官方的配置，可任意更换不同的图表
option 示例
option = {
    tooltip: {
        trigger: 'item',
        formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
        orient: 'vertical',
        left: 10,
        data: [ '邮件营销',  '搜索引擎']
    },
    series: [
        {
            name: '访问来源',
            type: 'pie',
            radius: ['50%', '70%'],
            avoidLabelOverlap: false,
            label: {
                show: false,
                position: 'center'
            },
            data: [
                {value: 310, name: '邮件营销'},
                {value: 1548, name: '搜索引擎'}
            ]
        }
    ]
};
-->
<script id="ms-echart" type="x/template">
    <div v-loading="loading" id="chart" ref="chart" :style="'width:'+width+';height:'+height"></div>
</script>
<script>
    Vue.component("ms-echart", {
        template: "#ms-echart",
        props: {
            option: {
                type: Object,
                default: function () {
                    return {};
                }
            }
        },
        data: function () {
            return {
                chart: {},
                width: '600px',
                height: '400px',
                loading:true,
                noDataOption: {
                    title: {
                        text: '暂无数据',
                        x: 'center',
                        y: 'center',
                        textStyle: {
                            color: '#323437',
                            fontSize: 12,
                            fontWeight: 'normal',
                            fontFamily: '"Microsoft Yahei", Helvetica, STHeitiSC-Light, Arial, sans-serif',
                        }
                    }
                }
            };
        },
        watch: {
            option: function (n) {
                if (n) {
                    this.init(this.option);
                } else {
                    this.init(this.noDataOption);
                }
            }
        },
        methods: {
            //初始化数据
            init: function (option) {
                this.chart.setOption(option,true);
                this.$nextTick(function () {
                    this.loading = false;
                })
            },
            //自适应容器大小
            initSize: function () {
                var that = this;
                if (this.$refs.chart) {
                    this.width = this.$refs.chart.parentNode.offsetWidth + "px";
                    this.height = this.$refs.chart.parentNode.offsetHeight + "px";
                    this.$nextTick(function () {
                        that.chart.resize();
                    });
                }
            },
            //创建
            create: function () {
                this.chart = echarts.init(this.$refs.chart);
                this.initSize();
            }
        },
        mounted: function () {
            var _this = this;
            this.create();
            //监听窗口变化 - 多个图表同时刷新
            window.addEventListener('resize', function() {
                _this.initSize();
            })
        },
        destroyed: function () {
            //销毁监听窗口变化
            window.removeEventListener('resize', this.initSize(), false)
        }
    });
</script>
