package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LiuWeiWei
 * @since 2023-05-06
 */
@RestController
public class AjaxRequestController {

    @GetMapping(value = "/jsAjax")
    public String jsAjax() {
        return "LiuWeiWei";
    }

}
