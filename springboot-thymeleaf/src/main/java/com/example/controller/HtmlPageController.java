package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
     * @return
     */
    @GetMapping(value = "/index")
    public String index() {
        return "index";
    }
}
