package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author LiuWeiWei
 * @since 2023-03-23
 */
@Controller
public class HtmlPageController {

    @GetMapping(value = "/index")
    public String index() {
        return "index";
    }
}
