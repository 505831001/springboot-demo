package com.excel.poi.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Liuweiwei
 * @since 2021-08-04
 */
@Controller
@Api(value = "", tags = "Html页面跳转控制器")
@Log4j2
public class HtmlController {

    @GetMapping(value = "/index")
    @ApiOperation(value = "公司主页", notes = "公司主页", tags = "")
    public String index() {
        log.info("请求Url.index()");
        return "index_page";
    }

    @GetMapping(value = "/loginPage")
    @ApiOperation(value = "登录页面", notes = "登录页面", tags = "")
    public String login() {
        log.info("请求Url.loginPage(String loginPage)");
        return "login_page";
    }

    @GetMapping(value = "/successPage")
    @ApiOperation(value = "成功页面", notes = "成功页面", tags = "")
    public String success() {
        log.info("请求Url.successHandler(String successPage)");
        return "success_page";
    }

    @GetMapping(value = "/failurePage")
    @ApiOperation(value = "失败页面", notes = "失败页面", tags = "")
    public String failure() {
        log.info("请求Url.failureHandler(String failurePage)");
        return "failure_page";
    }

    @GetMapping(value = "/error")
    @ApiOperation(value = "异常页面", notes = "异常页面", tags = "")
    public String error() {
        log.info("请求Url.errorHandler(String errorPage)");
        return "error_page";
    }
}
