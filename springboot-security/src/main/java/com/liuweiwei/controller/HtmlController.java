package com.liuweiwei.controller;

import com.liuweiwei.model.TbUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liuweiwei
 * @since 2020-05-20
 */
@Controller
@Api(value = "", tags = "Html页面跳转控制器")
@Log4j2
public class HtmlController {

    @GetMapping(value = "/string")
    @ApiOperation(value = "上下文对象", notes = "上下文对象", tags = "")
    @ResponseBody
    public String string() {
        log.info("来自于：SecurityContextHolder.getContext().setAuthentication(token)");
        return SecurityContextHolder.getContext().getAuthentication().toString();
    }

    @GetMapping(value = "/principal")
    @ApiOperation(value = "上下文信息", notes = "上下文信息", tags = "")
    @ResponseBody
    public Object principal() {
        log.info("来自于：SecurityContextHolder.getContext().setAuthentication(token)");
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

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

    @GetMapping(value = "/errorPage")
    @ApiOperation(value = "异常页面", notes = "异常页面", tags = "")
    public String error() {
        log.info("请求Url.errorHandler(String errorPage)");
        return "error_page";
    }

    @GetMapping("/user")
    @ApiOperation(value = "用户对象", notes = "用户对象", tags = "")
    @ResponseBody
    public Object user(@Valid @RequestBody(required = false) TbUser user, BindingResult errors) {
        if (errors.hasErrors()) {
            List<ObjectError> errorList = errors.getAllErrors();
            for (ObjectError error : errorList) {
                log.info(error.getDefaultMessage());
            }
            errorList.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).forEach(System.out::println);
            errorList.stream().forEach(error -> log.info(error.getDefaultMessage()));
            errorList.stream().forEach(error -> {
                log.info(error.getDefaultMessage());
            });
        }
        List<TbUser> userList = new ArrayList<>();
        userList.add(new TbUser());
        userList.stream().forEach(tbUser -> {
            tbUser.setId(10086L);
            tbUser.setUsername("中国移动");
            tbUser.setPassword("123456");
        });
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}