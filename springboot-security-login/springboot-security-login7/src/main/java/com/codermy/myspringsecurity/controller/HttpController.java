package com.codermy.myspringsecurity.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author LiuWeiWei
 * @since 2022-01-06
 */
@Controller
@Api(tags = "SpringSecurity相关接口")
@Slf4j
public class HttpController {

    @GetMapping(value = "/login.html")
    @ApiOperation(value = "登录界面")
    public String login() {
        System.out.println("[step 00] -> 请求控制器：login()方法...");
        return "login";
    }

    @GetMapping(value = "/login")
    @ApiOperation(value = "登录界面")
    public String loginDefault() {
        System.out.println("[step 00] -> 请求控制器：loginDefault()方法...");
        return "login";
    }

    @GetMapping(value = "/index")
    @ApiOperation(value = "主页")
    public String index() {
        System.out.println("[step 00] -> 请求控制器：index()方法...");
        return "index";
    }

    @GetMapping(value = "/403.html")
    @ApiOperation(value = "403")
    public String noPermission() {
        System.out.println("[step 00] -> 请求控制器：noPermission()方法...");
        return "403";
    }
}
