package com.logback.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Liuweiwei
 * @since 2021-05-14
 */
@RestController
public class GatewayController {

    @GetMapping(value = "/echo")
    public String echo(String message) {
        return "Logback 日志框架应用输出：" + message;
    }
}
