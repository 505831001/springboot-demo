package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author LiuWeiWei
 * @since 2023-03-23
 */
@Controller
public class HtmlPageController {

    /**
     * 取决于配置：spring.mvc.static-path-pattern: /static/**
     * D:\workspace-idea\springboot-demo\springboot-thymeleaf\src\main\resources\templates\index.html
     * 取决于配置：spring.resources.static-locations: classpath:/resources/, classpath:/static/, classpath:/public/
     * D:\workspace-idea\springboot-demo\springboot-thymeleaf\src\main\webapp\templates\index.html
     *
     * @return
     */
    @RequestMapping(value = "/index")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/login")
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/register")
    public String register() {
        return "register";
    }

    @RequestMapping(value = "/center-order")
    public String centerOrder() {
        return "center-order";
    }

    @RequestMapping(value = "/center")
    public String center() {
        return "center";
    }

    @RequestMapping(value = "/product")
    public String product() {
        return "product";
    }
}
