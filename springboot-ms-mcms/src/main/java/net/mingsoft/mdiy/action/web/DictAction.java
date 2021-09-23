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
package net.mingsoft.mdiy.action.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.mingsoft.base.entity.ResultData;
import net.mingsoft.basic.action.BaseAction;
import net.mingsoft.basic.bean.EUListBean;
import net.mingsoft.basic.util.BasicUtil;
import net.mingsoft.mdiy.biz.IDictBiz;
import net.mingsoft.mdiy.entity.DictEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 通用自定义字典
 * @author 铭飞开发团队 <br/>
 * 创建日期：2017年11月8日<br/>
 * 历史修订：<br/>
 */
@Api("通用自定义字典")
@Controller("webDictAction")
@RequestMapping("/mdiy/dict")
public class DictAction extends BaseAction{

    /**
     * 注入字典表业务层
     */
    @Autowired
    private IDictBiz dictBiz;

    /**
     * 查询字典表列表
     * @param dict 字典表实体
     * <i>dict参数包含字段信息参考：</i><br/>
     * dictId 编号<br/>
     * dictAppId 应用编号<br/>
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
     * dictId: 编号<br/>
     * dictAppId: 应用编号<br/>
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
    @ApiOperation(value = "查询字典表列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "dictType", value = "类型", required = false,paramType="query"),
            @ApiImplicitParam(name = "dictValue", value = "数据值", required =false,paramType="query"),
            @ApiImplicitParam(name = "dictLabel", value = "标签名", required = false,paramType="query"),

            @ApiImplicitParam(name = "dictSort", value = "排序（升序）", required = false,paramType="query"),
            @ApiImplicitParam(name = "isChild", value = "子业务关联", required = false,paramType="query"),
    })
    @GetMapping("/list")
    @ResponseBody
    public ResultData list(@ModelAttribute @ApiIgnore DictEntity dict, HttpServletResponse response, HttpServletRequest request) {
        if(dict == null || StringUtils.isEmpty(dict.getDictType())){
            return ResultData.build().error(this.getResString("dict.type"));
        }
        BasicUtil.startPage(1,100,true);
        //增加条件字典启用状态，子业务查询启用的字典
        dict.setDictEnable(true);
        List dictList = dictBiz.query(dict);
        return ResultData.build().success(new EUListBean(dictList,(int)BasicUtil.endPage(dictList).getTotal()));
    }

    @ApiOperation(value = "查询字典表列表,排除站点编号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dictValue", value = "数据值", required =false,paramType="query"),
            @ApiImplicitParam(name = "dictLabel", value = "标签名", required = false,paramType="query"),
            @ApiImplicitParam(name = "dictType", value = "类型", required = false,paramType="query"),
            @ApiImplicitParam(name = "dictDescription", value = "描述", required = false,paramType="query"),
            @ApiImplicitParam(name = "dictSort", value = "排序（升序）", required = false,paramType="query"),
            @ApiImplicitParam(name = "isChild", value = "子业务关联", required = false,paramType="query"),
    })
    @GetMapping("/listExcludeApp")
    @ResponseBody
    public ResultData listExcludeApp(@ModelAttribute @ApiIgnore DictEntity dict, HttpServletResponse response, HttpServletRequest request) {
        //增加条件字典启用状态，子业务查询启用的字典
        dict.setDictEnable(true);
        List dictList = dictBiz.queryExcludeApp(dict);
        return ResultData.build().success(dictList);
    }

}
