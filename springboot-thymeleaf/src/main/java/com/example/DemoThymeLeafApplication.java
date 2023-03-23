package com.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author Liuweiwei
 * @since 2021-01-08
 */
@SpringBootApplication
@Slf4j
public class DemoThymeLeafApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(DemoThymeLeafApplication.class);
        application.setBannerMode(Banner.Mode.CONSOLE);
        ConfigurableApplicationContext applicationContext = application.run(args);
        ConfigurableEnvironment applicationContextEnvironment = applicationContext.getEnvironment();
        log.info(applicationContextEnvironment.getProperty("java.vendor.url"));
        log.info(applicationContextEnvironment.getProperty("java.vendor.url.bug"));
        log.info(applicationContextEnvironment.getProperty("sun.java.command") + " started successfully.");
    }
}
