package com.codermy.myspringsecurity.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author codermy
 * @createTime 2020/7/2
 */
@Controller
@Api(tags = "SpringSecurity相关接口")
@Slf4j
public class HttpController {

    @GetMapping(value = "/login.html")
    @ApiOperation(value = "登录界面")
    public String login() {
        System.out.println("[step 00] -> 登录自定义页面");
        return "login";
    }

    @GetMapping(value = "/login")
    @ApiOperation(value = "登录界面")
    public String loginDefault() {
        System.out.println("[step 00] -> 登录默认页面");
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
