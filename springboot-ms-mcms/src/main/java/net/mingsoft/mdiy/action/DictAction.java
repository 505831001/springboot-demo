/**
 * The MIT License (MIT) * Copyright (c) 2020 铭软科技(mingsoft.net)

 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package net.mingsoft.mdiy.action;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.mingsoft.base.entity.ResultData;
import net.mingsoft.basic.annotation.LogAnn;
import net.mingsoft.basic.bean.EUListBean;
import net.mingsoft.basic.constant.e.BusinessTypeEnum;
import net.mingsoft.basic.util.BasicUtil;
import net.mingsoft.basic.util.StringUtil;
import net.mingsoft.mdiy.biz.IDictBiz;
import net.mingsoft.mdiy.entity.DictEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 字典表管理控制层
 * @author 铭飞开发团队
 * @version
 * 版本号：1<br/>
 * 创建日期：2017-8-12 14:22:36<br/>
 * 历史修订：<br/>
 */
@Api(value = "字典表管理接口")
@Controller
@RequestMapping("/${ms.manager.path}/mdiy/dict")
public class DictAction extends net.mingsoft.mdiy.action.BaseAction{

    /**
     * 注入字典表业务层
     */
    @Autowired
    private IDictBiz dictBiz;

    /**
     * 返回主界面index
     */
    @GetMapping("/index")
    public String index(HttpServletResponse response,HttpServletRequest request){
        return "/mdiy/dict/index";
    }

    /**
     * 查询字典表列表
     * @param dict 字典表实体
     * <i>dict参数包含字段信息参考：</i><br/>
     * dictValue 数据值<br/>
     * dictLabel 标签名<br/>
     * dictType 类型<br/>
     * dictDescription 描述<br/>
     * dictSort 排序（升序）<br/>
     * createBy 创建者<br/>
     * createDate 创建时间<br/>
     * updateBy 更新者<br/>
     * updateDate 更新时间<br/>
     * dictRemarks 备注信息<br/>
     * del 删除标记<br/>
     * <dt><span class="strong">返回</span></dt><br/>
     * <dd>[<br/>
     * { <br/>
     * dictValue: 数据值<br/>
     * dictLabel: 标签名<br/>
     * dictType: 类型<br/>
     * dictDescription: 描述<br/>
     * dictSort: 排序（升序）<br/>
     * createBy: 创建者<br/>
     * createDate: 创建时间<br/>
     * updateBy: 更新者<br/>
     * updateDate: 更新时间<br/>
     * dictRemarks: 备注信息<br/>
     * del: 删除标记<br/>
     * }<br/>
     * ]</dd><br/>
     */
    @ApiOperation(value = "查询字典表列表接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dictType", value = "类型", required = false,paramType="query"),
            @ApiImplicitParam(name = "dictLabel", value = "标签名", required = false,paramType="query"),
            @ApiImplicitParam(name = "dictValue", value = "数据值", required =false,paramType="query"),
            @ApiImplicitParam(name = "isChild", value = "子业务关联", required = false,paramType="query"),
    })
    @RequestMapping(value ="/list",method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public ResultData list(@ModelAttribute @ApiIgnore DictEntity dict, HttpServletResponse response, HttpServletRequest request, @ApiIgnore ModelMap model) {
        BasicUtil.startPage();
        //新增dictEnable字典状态字段，子业务默认只查询启用状态的字典数据
        //自定义字典管理页面查询全部
        //为了减少代码管理页面传入dictEnable参数来判断是否查询全部
        if (dict.getDictEnable() == null) {
            dict.setDictEnable(true);
        }else {
            dict.setDictEnable(null);
        }
        List dictList = dictBiz.query(dict);
        return ResultData.build().success(new EUListBean(dictList,(int)BasicUtil.endPage(dictList).getTotal()));
    }

    @ApiOperation(value = "根据子业务类型获取所有字典类型")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "isChild", value = "子业务关联", required = false,paramType="query"),
    })
    @GetMapping("/dictType")
    @ResponseBody
    public ResultData dictType  (@ModelAttribute @ApiIgnore DictEntity dict,HttpServletResponse response, HttpServletRequest request,@ApiIgnore ModelMap model) {
        BasicUtil.startPage();
        List dictList = dictBiz.dictType(dict);
        return ResultData.build().success(new EUListBean(dictList,(int)BasicUtil.endPage(dictList).getTotal()));
    }

    @ApiOperation(value = "根据字典类型获取字典，可支持多个类型用英文逗号隔开")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dictType", value = "字典类型", required = true,paramType="query"),
            @ApiImplicitParam(name = "isChild", value = "子业务关联", required = false,paramType="query"),
    })
    @GetMapping("/dictList")
    @ResponseBody
    public ResultData dictList  (@ModelAttribute @ApiIgnore DictEntity dict,HttpServletResponse response, HttpServletRequest request,@ApiIgnore ModelMap model) {
        if(StringUtils.isEmpty(dict.getDictType())) {
            return ResultData.build().error(getResString("err.error", this.getResString("dict.type")));
        }
        String[] types = dict.getDictType().split(",");
        DictEntity _dict = new DictEntity();
        _dict.setIsChild(dict.getIsChild());
        List list = new ArrayList();
        for(String type : types){
            _dict.setDictType(type);
            list.add(dictBiz.query(_dict));
        }
        return ResultData.build().success(list);
    }

    /**
     * 返回编辑界面dict_form
     */
    @GetMapping("/form")
    public String form(@ModelAttribute DictEntity dict,HttpServletResponse response,HttpServletRequest request,@ApiIgnore ModelMap model){

        return "/mdiy/dict/form";
    }

    /**
     * 获取字典表
     * @param dict 字典表实体
     * <i>dict参数包含字段信息参考：</i><br/>
     * dictValue 数据值<br/>
     * dictLabel 标签名<br/>
     * dictType 类型<br/>
     * dictDescription 描述<br/>
     * dictSort 排序（升序）<br/>
     * createBy 创建者<br/>
     * createDate 创建时间<br/>
     * updateBy 更新者<br/>
     * updateDate 更新时间<br/>
     * dictRemarks 备注信息<br/>
     * del 删除标记<br/>
     * <dt><span class="strong">返回</span></dt><br/>
     * <dd>{ <br/>
     * dictValue: 数据值<br/>
     * dictLabel: 标签名<br/>
     * dictType: 类型<br/>
     * dictDescription: 描述<br/>
     * dictSort: 排序（升序）<br/>
     * createBy: 创建者<br/>
     * createDate: 创建时间<br/>
     * updateBy: 更新者<br/>
     * updateDate: 更新时间<br/>
     * dictRemarks: 备注信息<br/>
     * del: 删除标记<br/>
     * }</dd><br/>
     */
    @ApiOperation(value = "获取字典详情接口")
    @ApiImplicitParam(name = "id", value = "字典编号", required = true,paramType="query")
    @GetMapping("/get")
    @ResponseBody
    public ResultData get(@ModelAttribute @ApiIgnore DictEntity dict,HttpServletResponse response, HttpServletRequest request,@ApiIgnore ModelMap model){
        if(StringUtils.isBlank(dict.getId())) {
            return ResultData.build().error(getResString("err.error", this.getResString("dict.id")));
        }
        DictEntity _dict = (DictEntity)dictBiz.getEntity(Integer.parseInt(dict.getId()));
        return ResultData.build().success(_dict);
    }

    /**
     * 保存字典表实体
     * @param dict 字典表实体
     * <i>dict参数包含字段信息参考：</i><br/>
     * dictValue 数据值<br/>
     * dictLabel 标签名<br/>
     * dictType 类型<br/>
     * dictDescription 描述<br/>
     * dictSort 排序（升序）<br/>
     * createBy 创建者<br/>
     * createDate 创建时间<br/>
     * updateBy 更新者<br/>
     * updateDate 更新时间<br/>
     * dictRemarks 备注信息<br/>
     * del 删除标记<br/>
     * <dt><span class="strong">返回</span></dt><br/>
     * <dd>{ <br/>
     * dictValue: 数据值<br/>
     * dictLabel: 标签名<br/>
     * dictType: 类型<br/>
     * dictDescription: 描述<br/>
     * dictSort: 排序（升序）<br/>
     * createBy: 创建者<br/>
     * createDate: 创建时间<br/>
     * updateBy: 更新者<br/>
     * updateDate: 更新时间<br/>
     * dictRemarks: 备注信息<br/>
     * del: 删除标记<br/>
     * }</dd><br/>
     */
    @ApiOperation(value = "保存字典接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dictLabel", value = "标签名", required = true,paramType="query"),
            @ApiImplicitParam(name = "dictType", value = "类型", required = true,paramType="query"),
            @ApiImplicitParam(name = "dictValue", value = "数据值", required =false,paramType="query"),
            @ApiImplicitParam(name = "dictDescription", value = "描述", required = false,paramType="query"),
            @ApiImplicitParam(name = "dictSort", value = "排序（升序）", required = false,paramType="query"),
            @ApiImplicitParam(name = "isChild", value = "子业务关联", required = false,paramType="query"),
            @ApiImplicitParam(name = "dictRemarks", value = "备注信息", required = false,paramType="query")
    })
    @LogAnn(title = "保存字典接口",businessType= BusinessTypeEnum.INSERT)
    @PostMapping("/save")
    @ResponseBody
    @RequiresPermissions("mdiy:dict:save")
    public ResultData save(@ModelAttribute @ApiIgnore DictEntity dict, HttpServletResponse response, HttpServletRequest request) {
        // type和lable不能为重复
        if(dictBiz.getByTypeAndLabelAndValue(dict.getDictType(),dict.getDictLabel(),null)!=null){
            return ResultData.build().error(getResString("diy.dict.type.and.label.repeat"));
        }
        // type和value不能为重复
        if(dictBiz.getByTypeAndLabelAndValue(dict.getDictType(),null,dict.getDictValue())!=null){
            return ResultData.build().error(getResString("diy.dict.type.and.value.repeat"));
        }
        dictBiz.saveEntity(dict);
        if (StringUtil.isBlank(dict.getDictValue())){
            dict.setDictValue(dict.getId());
            dictBiz.updateEntity(dict);
        }
        return ResultData.build().success();
    }

    /**
     * @param dicts 字典表实体
     * <i>dict参数包含字段信息参考：</i><br/>
     * id:id,id=1,2,3,4
     * 批量删除字典表
     *            <dt><span class="strong">返回</span></dt><br/>
     *            <dd>{code:"错误编码",<br/>
     *            result:"true｜false",<br/>
     *            resultMsg:"错误信息"<br/>
     *            }</dd>
     */
    @ApiOperation(value = "批量删除字典")
    @LogAnn(title = "批量删除字典",businessType= BusinessTypeEnum.DELETE)
    @PostMapping("/delete")
    @ResponseBody
    @RequiresPermissions("mdiy:dict:del")
    public ResultData delete(@RequestBody List<DictEntity> dicts,HttpServletResponse response, HttpServletRequest request) {
        int[] ids = new int[dicts.size()];
        for(int i = 0;i<dicts.size();i++){
            ids[i] = Integer.parseInt(dicts.get(i).getId());
        }
        dictBiz.delete(ids);
        return ResultData.build().success();
    }

    /**
     * 更新字典表信息字典表
     * @param dict 字典表实体
     * <i>dict参数包含字段信息参考：</i><br/>
     * dictValue 数据值<br/>
     * dictLabel 标签名<br/>
     * dictType 类型<br/>
     * dictDescription 描述<br/>
     * dictSort 排序（升序）<br/>
     * createBy 创建者<br/>
     * createDate 创建时间<br/>
     * updateBy 更新者<br/>
     * updateDate 更新时间<br/>
     * dictRemarks 备注信息<br/>
     * del 删除标记<br/>
     * <dt><span class="strong">返回</span></dt><br/>
     * <dd>{ <br/>
     * dictValue: 数据值<br/>
     * dictLabel: 标签名<br/>
     * dictType: 类型<br/>
     * dictDescription: 描述<br/>
     * dictSort: 排序（升序）<br/>
     * createBy: 创建者<br/>
     * createDate: 创建时间<br/>
     * updateBy: 更新者<br/>
     * updateDate: 更新时间<br/>
     * dictRemarks: 备注信息<br/>
     * del: 删除标记<br/>
     * }</dd><br/>
     */
    @ApiOperation(value = "更新字典信息接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "字典编号", required =true,paramType="query"),
            @ApiImplicitParam(name = "dictLabel", value = "标签名", required = true,paramType="query"),
            @ApiImplicitParam(name = "dictType", value = "类型", required = true,paramType="query"),
            @ApiImplicitParam(name = "dictValue", value = "数据值", required =false,paramType="query"),
            @ApiImplicitParam(name = "dictDescription", value = "描述", required = false,paramType="query"),
            @ApiImplicitParam(name = "dictSort", value = "排序（升序）", required = false,paramType="query"),
            @ApiImplicitParam(name = "isChild", value = "子业务关联", required = false,paramType="query"),
            @ApiImplicitParam(name = "dictRemarks", value = "备注信息", required = false,paramType="query")
    })
    @LogAnn(title = "更新字典信息接口",businessType= BusinessTypeEnum.UPDATE)
    @PostMapping("/update")
    @ResponseBody
    @RequiresPermissions("mdiy:dict:update")
    public ResultData update(@ModelAttribute @ApiIgnore DictEntity dict, HttpServletResponse response,
                             HttpServletRequest request) {
        DictEntity _dict = dictBiz.getByTypeAndLabelAndValue(dict.getDictType(),dict.getDictLabel(),null);
        // type和lable不能为重复
        if(_dict!=null){
            if(!_dict.getId().equals(dict.getId())){
                return ResultData.build().error(getResString("diy.dict.type.and.label.repeat"));
            }
        }
        // type和value不能为重复
        DictEntity _dict2 = dictBiz.getByTypeAndLabelAndValue(dict.getDictType(),null,dict.getDictValue());
        if(_dict2!=null){
            if(!_dict2.getId().equals(dict.getId())){
                return ResultData.build().error(getResString("diy.dict.type.and.value.repeat"));
            }
        }

        if (StringUtils.isBlank(dict.getDictValue())){
            dict.setDictValue(null);
        }
        dictBiz.updateEntity(dict);
        return ResultData.build().success();
    }

}
