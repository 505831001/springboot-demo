package com.liuweiwei.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author liuweiwei
 * @since 2020-05-20
 */
@Controller
public class TbLoginController {

    @GetMapping("/loginPage")
    public String login() {
        return "login_page";
    }

    @GetMapping("/indexPage")
    public String index() {
        return "index_page";
    }

    @GetMapping("/loginError")
    public String error() {
        return "error_page";
    }
}