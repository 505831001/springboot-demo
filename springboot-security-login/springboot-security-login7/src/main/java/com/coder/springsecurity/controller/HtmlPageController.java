package com.coder.springsecurity.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author LiuWeiWei
 * @since 2022-01-06
 */
@Controller
@Api(tags = "Html页面跳转相关接口")
public class HtmlPageController {

    @GetMapping(value = "/login.html")
    @ApiOperation(value = "登录界面")
    public String login(){
        return "login";
    }

    @GetMapping(value = "/index")
    @ApiOperation(value = "主页")
    public String index() {
        return "index";
    }

    @GetMapping(value = "/403.html")
    @ApiOperation(value = "403")
    public String noPermission() {
        return "403";
    }
}
