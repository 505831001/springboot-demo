package com.springcloud.consumer.controller;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Liuweiwei
 * @since 2021-01-31
 */
@RestController
@RefreshScope
public class WorldController {

    @Value("${example.no}")
    private String no;
    @Value("${example.name}")
    private String name;
    @Value("${example.age}")
    private String age;

    @NacosValue(value = "${server.port}", autoRefreshed = true)
    protected String serverPort;

    @GetMapping(value = "/user")
    public String user() {
        return "【端口】:" + serverPort + " - 编号：" + no + "，姓名：" + name + "，年龄：" + age;
    }
}
