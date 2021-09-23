/**
 * The MIT License (MIT)
 * Copyright (c) 2021 铭软科技(mingsoft.net)
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
package net.mingsoft.basic.action;

import java.util.List;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import cn.hutool.core.util.ObjectUtil;
import java.util.*;

import com.alibaba.fastjson.JSON;
import net.mingsoft.base.entity.ResultData;
import net.mingsoft.basic.bean.LogBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.ui.ModelMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.*;
import net.mingsoft.basic.biz.ILogBiz;
import net.mingsoft.basic.entity.LogEntity;
import net.mingsoft.base.util.JSONObject;
import net.mingsoft.base.entity.BaseEntity;
import net.mingsoft.basic.util.BasicUtil;
import net.mingsoft.basic.util.StringUtil;
import net.mingsoft.base.filter.DateValueFilter;
import net.mingsoft.base.filter.DoubleValueFilter;
import net.mingsoft.basic.bean.EUListBean;
import net.mingsoft.basic.annotation.LogAnn;
import net.mingsoft.basic.constant.e.BusinessTypeEnum;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;
/**
 * 系统日志管理控制层
 * @author 铭飞开发团队
 * 创建日期：2020-11-21 9:41:34<br/>
 * 历史修订：<br/>
 */
@Api(value = "系统日志接口")
@Controller("basicLogAction")
@RequestMapping("/${ms.manager.path}/basic/log")
public class LogAction extends BaseAction{


    /**
     * 注入系统日志业务层
     */
    @Autowired
    private ILogBiz logBiz;

    /**
     * 返回主界面index
     */
    @GetMapping("/index")
    public String index(HttpServletResponse response,HttpServletRequest request){
        return "/basic/log/index";
    }

    /**
     * 查询系统日志列表
     * @param log 系统日志实体
     */
    @ApiOperation(value = "查询系统日志列表接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "logTitle", value = "标题", required =false,paramType="query"),
            @ApiImplicitParam(name = "logIp", value = "IP", required =false,paramType="query"),
            @ApiImplicitParam(name = "logMethod", value = "请求方法", required =false,paramType="query"),
            @ApiImplicitParam(name = "logRequestMethod", value = "请求方式", required =false,paramType="query"),
            @ApiImplicitParam(name = "logUrl", value = "请求地址", required =false,paramType="query"),
            @ApiImplicitParam(name = "logStatus", value = "请求状态", required =false,paramType="query"),
            @ApiImplicitParam(name = "logBusinessType", value = "业务类型", required =false,paramType="query"),
            @ApiImplicitParam(name = "logUserType", value = "用户类型", required =false,paramType="query"),
            @ApiImplicitParam(name = "logUser", value = "操作人员", required =false,paramType="query"),
            @ApiImplicitParam(name = "logLocation", value = "所在地区", required =false,paramType="query"),
            @ApiImplicitParam(name = "logParam", value = "请求参数", required =false,paramType="query"),
            @ApiImplicitParam(name = "logResult", value = "返回参数", required =false,paramType="query"),
            @ApiImplicitParam(name = "logErrorMsg", value = "错误消息", required =false,paramType="query"),
            @ApiImplicitParam(name = "createBy", value = "创建人", required =false,paramType="query"),
            @ApiImplicitParam(name = "createDate", value = "创建时间", required =false,paramType="query"),
            @ApiImplicitParam(name = "updateBy", value = "修改人", required =false,paramType="query"),
            @ApiImplicitParam(name = "updateDate", value = "修改时间", required =false,paramType="query"),
            @ApiImplicitParam(name = "del", value = "删除标记", required =false,paramType="query"),
            @ApiImplicitParam(name = "id", value = "编号", required =false,paramType="query"),
    })
    @RequestMapping(value ="/list",method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public ResultData list(@ModelAttribute @ApiIgnore LogBean log, HttpServletResponse response, HttpServletRequest request, @ApiIgnore ModelMap model, BindingResult result) {
        BasicUtil.startPage();
        List logList = logBiz.query(log);
        return ResultData.build().success(new EUListBean(logList,(int)BasicUtil.endPage(logList).getTotal()));
    }

    /**
     * 返回编辑界面log_form
     */
    @GetMapping("/form")
    public String form(@ModelAttribute LogEntity log,HttpServletResponse response,HttpServletRequest request,ModelMap model){
        return "/basic/log/form";
    }
    /**
     * 获取系统日志
     * @param log 系统日志实体
     */
    @ApiOperation(value = "获取系统日志列表接口")
    @ApiImplicitParam(name = "id", value = "编号", required =true,paramType="query")
    @GetMapping("/get")
    @ResponseBody
    public ResultData get(@ModelAttribute @ApiIgnore LogEntity log,HttpServletResponse response, HttpServletRequest request,@ApiIgnore ModelMap model){
        if(log.getId()==null) {
            return ResultData.build().error();
        }
        LogEntity _log = (LogEntity)logBiz.getEntity(Integer.parseInt(log.getId()));
        return ResultData.build().success(_log);
    }
}
