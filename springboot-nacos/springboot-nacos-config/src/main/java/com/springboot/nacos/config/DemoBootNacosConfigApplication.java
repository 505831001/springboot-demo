package com.springboot.nacos.config;

import com.alibaba.nacos.spring.context.annotation.config.EnableNacosConfig;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Nacos 有三大主要功能：
 *     1. 服务发现和服务健康监测。Nacos 支持基于 DNS 和基于 RPC 的服务发现。
 *     2. 动态配置服务。
 *     3. 动态 DNS 服务。
 *     4. 服务及其元数据管理。
 *
 * @author Liuweiwei
 * @since 2021-01-30
 */
@SpringBootApplication
@EnableNacosConfig
@NacosPropertySource(dataId = "itsc-nacos-config-dev.properties", groupId = "dev", autoRefreshed = true)
public class DemoBootNacosConfigApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoBootNacosConfigApplication.class, args);
    }
}
