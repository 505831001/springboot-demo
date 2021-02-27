package com.springboot.nacos.producer;

import com.alibaba.nacos.spring.context.annotation.discovery.EnableNacosDiscovery;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Liuweiwei
 * @since 2021-01-30
 */
@SpringBootApplication
@EnableNacosDiscovery
public class DemoBootNacosProducerApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoBootNacosProducerApplication.class, args);
    }
}
