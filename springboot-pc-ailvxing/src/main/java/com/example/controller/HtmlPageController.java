package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author LiuWeiWei
 * @since 2023-05-12
 */
@Controller
public class HtmlPageController {

    @GetMapping(value = "/index")
    public String index() {
        return "index";
    }

    @GetMapping(value = "/register")
    public String register() {
        return "register";
    }

    @GetMapping(value = "/login")
    public String login() {
        return "login";
    }

    @GetMapping(value = "/signin")
    public String signIn() {
        return "signin";
    }
}
