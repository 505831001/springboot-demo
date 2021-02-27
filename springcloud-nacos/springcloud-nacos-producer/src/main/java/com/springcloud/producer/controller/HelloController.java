package com.springcloud.producer.controller;


import com.alibaba.nacos.api.config.annotation.NacosValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Liuweiwei
 * @since 2021-01-31
 */
@RestController
@RefreshScope
public class HelloController {

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

    @GetMapping(value = "/echo/{message}")
    public String echo(@PathVariable(value = "message") String message) {
        return "Spring cloud 整合 nacos 之 producer 输出：" + message;
    }
}
