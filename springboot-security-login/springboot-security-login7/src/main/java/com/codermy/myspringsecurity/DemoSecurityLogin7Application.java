package com.codermy.myspringsecurity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author LiuWeiWei
 * @since 2022-01-06
 */
@SpringBootApplication
@Slf4j
public class DemoSecurityLogin7Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DemoSecurityLogin7Application.class, args);
        ConfigurableEnvironment environment = context.getEnvironment();
        log.info(environment.getProperty("java.vendor.url"));
        log.info(environment.getProperty("java.vendor.url.bug"));
        log.info(environment.getProperty("sun.java.command") + " started successfully.");
    }
}
