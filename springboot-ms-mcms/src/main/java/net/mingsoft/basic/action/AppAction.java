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

import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.mingsoft.base.entity.ResultData;
import net.mingsoft.basic.annotation.LogAnn;
import net.mingsoft.basic.biz.IAppBiz;
import net.mingsoft.basic.constant.e.BusinessTypeEnum;
import net.mingsoft.basic.constant.e.CookieConstEnum;
import net.mingsoft.basic.entity.AppEntity;
import net.mingsoft.basic.entity.ManagerEntity;
import net.mingsoft.basic.util.BasicUtil;
import net.mingsoft.basic.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 网站基本信息控制层
 *
 * @author 铭飞开发团队
 * @version 版本号：100-000-000<br/>
 *          创建日期：2014-07-14<br/>
 *          历史修订：<br/>
 */
@Api("网站基本信息接口")
@Controller
@RequestMapping("/${ms.manager.path}/app/")
public class AppAction extends BaseAction {

    /**
     * appBiz业务层的注入
     */
    @Autowired
    private IAppBiz appBiz;

    /**
     * 跳转到修改页面
     *
     * @param mode
     *            ModelMap实体对象
     * @param appId
     *            站点id
     * @param request
     *            请求对象
     * @return 站点修改页面
     */
    @ApiOperation(value = "跳转到修改页面")
    @ApiImplicitParam(name = "appId", value = "站点ID", required = true,paramType="path")
    @GetMapping(value = "/{appId}/edit")
    public String edit(ModelMap mode, @PathVariable @ApiIgnore int appId, HttpServletRequest request) {
        AppEntity app = null;
        //若有appid直接根据appId查询
        if (appId < 0) {
            app = BasicUtil.getApp();
            if(app!=null) {
                //防止session再次压入appId
                if(BasicUtil.getSession("appId")==null){
                    BasicUtil.setSession("appId",app.getAppId());
                }
            } else {
                appId = (int) BasicUtil.getSession("appId");
                app =  appBiz.getById(appId);
            }
        } else {
            app =  appBiz.getById(appId);
        }

        // 判断否是超级管理员,是的话不显示站点风格
        if (this.isSystemManager()) {
            mode.addAttribute("SystemManager", true);
        } else {
            mode.addAttribute("SystemManager", false);
        }
        mode.addAttribute("app", app);
        mode.addAttribute("appId", appId);
        return "/basic/app/app";

    }

    /**
     * 获取站点信息
     * @param appId
     * @return
     */
    @ApiOperation(value = "获取站点信息")
    @ApiImplicitParam(name = "appId", value = "站点ID", required = true,paramType="path")
    @GetMapping(value = "/{appId}/get")
    @ResponseBody
    public ResultData get(@PathVariable @ApiIgnore int appId) {
        AppEntity app = null;
        //若有appid直接根据appId查询
        if (appId < 0) {
            app = BasicUtil.getApp();
            if(app!=null) {
                //防止session再次压入appId
                if(BasicUtil.getSession("appId")==null){
                    BasicUtil.setSession("appId",app.getAppId());
                }
            } else {
                appId = (int) BasicUtil.getSession("appId");
                app =  appBiz.getById(appId);
            }
        } else {
            app =  appBiz.getById(appId);
        }
        return ResultData.build().success(app);

    }

    /**
     * 更新站点信息
     *
     * @param mode
     *            ModelMap实体对象
     * @param app
     *            站点对象
     * @param request
     *            请求对象
     * @param response
     *            相应对象
     */
    @ApiOperation(value ="更新站点信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appName", value = "应用名称", required = false,paramType="query"),
            @ApiImplicitParam(name = "appDescription", value = "应用描述", required = false,paramType="query"),
            @ApiImplicitParam(name = "appLogo", value = "应用logo", required = false,paramType="query"),
            @ApiImplicitParam(name = "appDatetime", value = "站点日期", required = false,paramType="query"),
            @ApiImplicitParam(name = "appKeyword", value = "网站关键字", required = false,paramType="query"),
            @ApiImplicitParam(name = "appCopyright", value = "网站版权信息", required = false,paramType="query"),
            @ApiImplicitParam(name = "appStyle", value = "网站采用的模板风格", required = false,paramType="query"),
            @ApiImplicitParam(name = "appUrl", value = "网站域名", required = false,paramType="query"),
            @ApiImplicitParam(name = "appManagerId", value = "管理站点的管理员id", required = false,paramType="query"),
            @ApiImplicitParam(name = "appPayDate", value = "应用续费时间", required = false,paramType="query"),
            @ApiImplicitParam(name = "appPay", value = "应用费用清单", required = false,paramType="query"),
    })
    @PostMapping("/update")
    @LogAnn(title = "更新站点信息",businessType = BusinessTypeEnum.UPDATE)
    @RequiresPermissions("app:update")
    @ResponseBody
    public ResultData update(@ModelAttribute @ApiIgnore AppEntity app,ModelMap mode, HttpServletRequest request,
                             HttpServletResponse response) {
        mode.clear();
        // 获取Session值
        ManagerEntity managerSession = (ManagerEntity) getManagerBySession();
        if (managerSession == null) {
            return ResultData.build().error();
        }
        mode.addAttribute("managerSession", managerSession);

        //验证重复
        if(super.validated("app", "app_dir", app.getAppDir(), app.getId(), "id")){
            return ResultData.build().error(getResString("err.exist", this.getResString("app.dir")));
        }

        //验证网站生成目录的值是否合法
        if(StringUtil.isBlank(app.getAppDir())){
            return ResultData.build().error(getResString("err.empty", this.getResString("app.dir")));
        }
        if(!StringUtil.checkLength(app.getAppDir()+"", 0, 10)){
            return ResultData.build().error(getResString("err.length", this.getResString("app.dir"), "0", "10"));
        }

        // 判断否是超级管理员,不是则不修改应用续费时间和清单
        if (!this.isSystemManager()) {
            app.setAppPayDate(null);
            app.setAppPay(null);
        }
        int roleId = managerSession.getRoleId();
        // 判断站点数据的合法性
        // 获取cookie
        String cookie = BasicUtil.getCookie(CookieConstEnum.PAGENO_COOKIE);
        int pageNo = 1;
        // 判断cookies是否为空
        if (StringUtils.isNotBlank(cookie) && Integer.valueOf(cookie) > 0) {
            pageNo = Integer.valueOf(cookie);
        }
        mode.addAttribute("pageNo", pageNo);
        if (!checkForm(app, response)) {
            return ResultData.build().error();
        }
        if (StringUtils.isNotBlank(app.getAppLogo())) {
            app.setAppLogo(app.getAppLogo().replace("|", ""));
        }
        app.setAppUrl(BasicUtil.getUrl());
        appBiz.updateById(app);
        appBiz.updateCache();
        return ResultData.build().success();
    }

    /**
     * 判断站点域名的合法性
     *
     * @param app
     *            要验证的站点信息
     * @param response
     *            response对象
     */
    public boolean checkForm(AppEntity app, HttpServletResponse response) {

        /*
         * 判断数据的合法性
         */
        if (!StringUtil.checkLength(app.getAppKeyword(), 0, 1000)) {
            ResultData.build().error(getResString("err.length", this.getResString("appKeyword"), "0", "1000"));
            return false;
        }
        if (!StringUtil.checkLength(app.getAppCopyright(), 0, 1000)) {
            ResultData.build().error(getResString("err.length", this.getResString("appCopyright"), "0", "1000"));
            return false;
        }
        if (!StringUtil.checkLength(app.getAppDescription(), 0, 1000)) {
            ResultData.build().error(getResString("err.length", this.getResString("appDescrip"), "0", "1000"));
            return false;
        }
        if (!StringUtil.checkLength(app.getAppName(), 1, 50)) {
            ResultData.build().error(getResString("err.length", this.getResString("appTitle"), "1", "50"));
            return false;
        }
        if (StringUtils.isNotBlank(app.getAppStyle()) && !StringUtil.checkLength(app.getAppStyle(), 1, 30)) {
            ResultData.build().error(getResString("err.length", this.getResString("appStyle"), "1", "30"));
            return false;
        }
        if(ObjectUtil.isNotNull(app.getAppHostUrl())){
            if (!StringUtil.checkLength(app.getAppHostUrl(), 10, 150)) {
                ResultData.build().error(getResString("err.length", this.getResString("appUrl"), "10", "150"));
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否有重复的域名
     *
     * @param request
     *            请求对象
     * @return true:重复,false:不重复
     */
    @ApiOperation(value = "判断是否有重复的域名")
    @GetMapping("/checkUrl")
    @ResponseBody
    public ResultData checkUrl(HttpServletRequest request) {
        if (request.getParameter("appUrl") != null) {
            if (appBiz.countByUrl(request.getParameter("appUrl")) > 0) {
                return ResultData.build().success();
            } else {
                return ResultData.build().error();
            }
        } else {
            return ResultData.build().error();
        }

    }
}
