package com.liuweiwei.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author liuweiwei
 * @since 2020-05-20
 */
@Controller
public class TbLoginController {

    @GetMapping("/loginPage")
    public String login() {
        return "login";
    }

    @GetMapping("/indexPage")
    public String index() {
        return "index_page";
    }

    @GetMapping("/loginError")
    public String error() {
        return "error_page";
    }

    @GetMapping("/what")
    @ResponseBody
    public Object whoIm() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}