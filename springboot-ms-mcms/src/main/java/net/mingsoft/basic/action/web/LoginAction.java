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


package net.mingsoft.basic.action.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.mingsoft.base.entity.ResultData;
import net.mingsoft.basic.action.BaseAction;
import net.mingsoft.basic.biz.IAppBiz;
import net.mingsoft.basic.constant.e.SessionConstEnum;
import net.mingsoft.basic.entity.ManagerEntity;
import net.mingsoft.basic.strategy.ILoginStrategy;
import net.mingsoft.basic.util.BasicUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @ClassName: LoginAction
 * @Description:TODO(登录的基础应用层)
 * @author: 铭飞开发团队
 * @date: 2015年1月27日 下午3:21:47
 *
 * @Copyright: 2018 www.mingsoft.net Inc. All rights reserved.
 */
@Api("登录的基础应用层接口")
@Controller
@RequestMapping("/${ms.manager.path}")
public class LoginAction extends BaseAction {

    @Value("${ms.manager.path}")
    private String managerPath;

    /**
     * 站点业务层
     */
    @Autowired
    private IAppBiz appBiz;

    @Autowired
    private ILoginStrategy loginStrategy;

    /**
     * 加载管理员登录界面
     *
     * @param request
     *            请求对象
     * @return 管理员登录界面地址
     */
    @ApiOperation(value = "加载管理员登录界面")
    @SuppressWarnings("resource")
    @GetMapping("/login")
    public String login(HttpServletRequest request) {
        if (BasicUtil.getSession(SessionConstEnum.MANAGER_SESSION) != null) {
            return "redirect:" + managerPath + "/index.do";
        }

        request.setAttribute("app", BasicUtil.getApp());
        return "/login";
    }

    /**
     * 验证登录
     *
     * @param manager
     *            管理员实体
     * @param request
     *            请求
     * @param response
     *            响应
     */
    @ApiOperation(value = "验证登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "managerName", value = "帐号", required = true, paramType = "query"),
            @ApiImplicitParam(name = "managerPassword", value = "密码", required = true, paramType = "query"),
    })
    @PostMapping(value = "/checkLogin")
    @ResponseBody
    public ResultData checkLogin(@ModelAttribute @ApiIgnore ManagerEntity manager, HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("basic checkLogin");

        //验证码
        if (!(checkRandCode())) {
            return ResultData.build().error(getResString("err.error", this.getResString("rand.code")));
        }
        if(loginStrategy.login(manager)){
            return ResultData.build().success();
        }else {
            return ResultData.build().error(getResString("err.error", this.getResString("manager.name.or.password")));
        }

    }
}
