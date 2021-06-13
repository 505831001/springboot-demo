package com.cloud.x20.config.client.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Liuweiwei
 * @since 2021-06-03
 */
@RestController
public class ConfigClientController {

    @Value(value = "${example.support}")
    String support;

    @GetMapping(value = "/echo")
    public String echo() {
        return "Spring cloud config 1.x 配置中心加载：" + support;
    }
}
