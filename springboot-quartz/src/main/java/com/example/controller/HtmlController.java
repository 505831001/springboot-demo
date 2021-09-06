package com.example.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Liuweiwei
 * @since 2021-08-04
 */
@Controller
@Log4j2
public class HtmlController {

    @GetMapping(value = "/index")
    public String index() {
        log.info("请求Url.index()");
        return "index_page";
    }

    @GetMapping(value = "/loginPage")
    public String login() {
        log.info("请求Url.loginPage(String loginPage)");
        return "login_page";
    }

    @GetMapping(value = "/successPage")
    public String success() {
        log.info("请求Url.successHandler(String successPage)");
        return "success_page";
    }

    @GetMapping(value = "/failurePage")
    public String failure() {
        log.info("请求Url.failureHandler(String failurePage)");
        return "failure_page";
    }

    @GetMapping(value = "/error")
    public String error() {
        log.info("请求Url.errorHandler(String errorPage)");
        return "error_page";
    }
}
