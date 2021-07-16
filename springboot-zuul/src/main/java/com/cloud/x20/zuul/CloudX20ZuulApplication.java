package com.cloud.x20.zuul;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @EnableZuulProxy
 *     简单理解为@EnableZuulServer的增强版，当Zuul与Eureka、Ribbon等组件配合使用时，我们使用@EnableZuulProxy。
 *     开启zuul的网关功能，他是一个组合注解，集成了eureka客户端注解。
 * @EnableEurekaClient
 *     开启eureka客户端的消费者。
 *
 * @author Liuweiwei
 * @since 2021-05-26
 */
@SpringBootApplication
@EnableZuulProxy
public class CloudX20ZuulApplication {
    /**
     * 日志-实现层：logback<org.slf4j>
     */
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(CloudX20ZuulApplication.class);

    /**
     * 日志-实现层：log4j<org.apache.log4j>
     */
    private static final Logger LOGGER = LogManager.getLogger(CloudX20ZuulApplication.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(CloudX20ZuulApplication.class, args);
        ConfigurableEnvironment environment = context.getEnvironment();
        LOGGER.info(environment.getProperty("java.vendor.url"));
        LOGGER.info(environment.getProperty("java.vendor.url.bug"));
        LOGGER.info(environment.getProperty("sun.java.command") + " started successfully.");
    }
}
