<template id="ms-search">
    <el-dialog id="search" title="筛选" :visible.sync="searchVisible" width="70%" v-cloak>
        <el-row>
            <el-col :span="24">
                <el-scrollbar class="ms-scrollbar" style="height: 100%;">
                <el-form ref="form" label-width="120px" size="mini">
                    <el-form-item label="添加筛选条件:">
                        <el-select
                                @change="select">
                            <el-option v-for='(item,index) in condition' :key="item.index" :value="index"
                                       :label="item.name"></el-option>
                        </el-select>
                    </el-form-item>
                </el-form>
                </el-scrollbar>
            </el-col>
        </el-row>
        <el-table size="mini" :show-header="false"
                  :data="list"
                  style="width: 100%">
            <el-table-column
                    prop="name"
                    width="110">
            </el-table-column>
            <el-table-column width="130">
                <template slot-scope="scope">
                    <el-select v-model="scope.row.el" size="mini" v-if="scope.row.type=='input'||scope.row.type=='textarea'">
                        <el-option label="等于" value="eq"></el-option>
                        <el-option label="包含" value="like"></el-option>
                        <el-option label="左等于" value="likeLeft"></el-option>
                        <el-option label="右等于" value="likeRight"></el-option>
                    </el-select>
                    <el-select v-model="scope.row.el" size="mini" v-if="scope.row.type=='number'">
                        <el-option label="大于" value="gt"></el-option>
                        <el-option label="小于" value="lt"></el-option>
                        <el-option label="等于" value="eq"></el-option>
                        <el-option label="大于等于" value="gte"></el-option>
                        <el-option label="小于等于" value="lte"></el-option>
                    </el-select>
                    <el-select v-model="scope.row.el" size="mini" v-if="scope.row.type=='date'||scope.row.type=='time'" @change="dateChange(scope.row)">
                        <el-option label="大于" value="gt"></el-option>
                        <el-option label="等于" value="eq"></el-option>
                        <el-option label="小于" value="lt"></el-option>
                        <el-option label="范围" value="range"></el-option>
                    </el-select>
                    <span v-if="scope.row.hasOwnProperty('multiple')||scope.row.type=='switch' || scope.row.type=='role'">是</span>
                </template>
            </el-table-column>
            <el-table-column >
                <template slot-scope="scope">
                    <el-input  style="width: 200px" v-model="scope.row.value" size="mini"
                               v-if="scope.row.type=='input'||scope.row.type=='number'||scope.row.type=='textarea'">
                    </el-input>
                    <el-select  style="width: 200px" v-model="scope.row.value" size="mini" v-if="scope.row.hasOwnProperty('multiple')">
                        <el-option v-for='item in $root[scope.row.model+"Options"]' :key="item[scope.row.key]" :value="item[scope.row.key]"
                                   :label="item[scope.row.title]"></el-option>
                    </el-select>
                    <el-switch v-if="scope.row.type=='switch'" v-model="scope.row.value">
                    </el-switch>
                    <ms-employee v-if="scope.row.type=='role'"
                                      size="mini"
                                      v-model="scope.row.value">
                    </ms-employee>
                    <template v-if="scope.row.type=='time'">
                        <el-time-picker
                                size="mini"
                                v-if="scope.row.el=='range'"
                                clearable
                                is-range
                                value-format="HH:mm:ss"
                                range-separator="至"
                                start-placeholder="请选择开始时间"
                                end-placeholder="请选择结束时间"
                                v-model="scope.row.value">
                        </el-time-picker>
                        <el-time-picker
                                size="mini"
                                v-else
                                clearable
                                value-format="HH:mm:ss"
                                v-model="scope.row.value">
                        </el-time-picker>
                    </template>
                    <template v-if="scope.row.type=='date'">
                        <el-date-picker
                                size="mini"
                                v-if="scope.row.el=='range'"
                                v-model="scope.row.value"
                                type="datetimerange"
                                value-format="yyyy-MM-dd HH:mm:ss"
                                start-placeholder="请选择开始日期"
                                end-placeholder="请选择结束日期">
                        </el-date-picker>
                        <el-date-picker
                                size="mini"
                                v-else
                                clearable
                                value-format="yyyy-MM-dd HH:mm:ss"
                                v-model="scope.row.value">
                        </el-date-picker>
                    </template>
                </template>
            </el-table-column>
            <el-table-column align="center" width="130">
                <template slot-scope="scope">
                    <el-select v-model="scope.row.action" size="mini" v-if="scope.$index != list.length-1">
                        <el-option label="与" value="and"></el-option>
                        <el-option label="或" value="or"></el-option>
                    </el-select>
                </template>
            </el-table-column>
            <el-table-column align="center" width="60">
                <template slot-scope="scope">
                    <el-link type="primary" :underline="false"  @click="delField(scope.$index)">删除</el-link>
                </template>
            </el-table-column>
        </el-table>
        <span slot="footer" class="dialog-footer">
    <el-button type="primary" size="mini" @click="search">查询</el-button>
  </span>
    </el-dialog>
</template>
<script>
    Vue.component('ms-search', {
        template: '#ms-search',
        props: {
            conditionData:{
                type:Array,
                default: []
            },
            conditions:{
                type:Array,
                default: []
            },
            close: {
                type: Boolean,
                default: true
            },
        },
        watch:{
            list:{
                handler: function(nv) {
                    this.$emit("update:conditions",nv)
                },
                deep: true
            }
        },
        data: function () {
            return{
                searchVisible:false,
                condition:this.conditionData,
                list:this.conditions,
            }
        },
        methods: {
            delField: function (index) {
                this.list.splice(index, 1);
            },
            //选择条件
            select: function (val) {
                this.list.push(Object.assign({},
                    this.condition[val],{
                        value: this.condition[val].type=='switch'?false:'',
                    }
                ));
            },
            open: function(){
                this.searchVisible = true;
            },
            close: function(){
                this.searchVisible = false;
            },
            dateChange: function(data){
                //时间数据转换，不然会报错
                if(data.el==='range'){
                    data.value=[]
                }else {
                    data.value=''
                }
            },
            search: function(){
                if(this.close){
                    this.close();
                }
                var data = []
                this.list.forEach(function(x){
                    if(x.el==='range'){
                        if(x.value.length){
                            data.push(x)
                        }
                    }else {
                        if(x.value){
                            data.push(x)
                        }
                    }
                })
                this.$emit("search",data)
            },
        }
    });
</script>
<style>
    #search .el-date-editor,
    #search .el-select,
    #search .el-input{
        width: 100% !important;
    }
</style>
