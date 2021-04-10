package com.liuweiwei.controller;

import com.liuweiwei.model.TbUser;
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
@Slf4j
public class LoginController {

    @GetMapping(value = "/echo")
    @ResponseBody
    public String echo() {
        return SecurityContextHolder.getContext().getAuthentication().toString();
    }

    @GetMapping(value = "/what")
    @ResponseBody
    public Object what() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @GetMapping(value = "/index")
    public String index() {
        log.info("请求Url.index()");
        return "index_page";
    }

    @GetMapping("/loginPage")
    public String login() {
        log.info("请求Url.loginPage(String loginPage)");
        return "login_page";
    }

    @GetMapping("/successPage")
    public String success() {
        log.info("请求Url.successHandler(String successPage)");
        return "success_page";
    }

    @GetMapping("/failurePage")
    public String failure() {
        log.info("请求Url.failureHandler(String failurePage)");
        return "failure_page";
    }

    @GetMapping("/user")
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