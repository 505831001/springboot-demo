package com.cloud.x20.openfeign;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author Liuweiwei
 * @since 2021-05-22
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class CloudX20OpenFeignApplication {

    /**
     * 日志-实现层：log4j<org.apache.log4j>
     */
    private static final Logger LOGGER = LogManager.getLogger(CloudX20OpenFeignApplication.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(CloudX20OpenFeignApplication.class, args);
        ConfigurableEnvironment environment = context.getEnvironment();
        LOGGER.info(environment.getProperty("java.vendor.url"));
        LOGGER.info(environment.getProperty("java.vendor.url.bug"));
        LOGGER.info(environment.getProperty("sun.java.command") + " started successfully.");
    }
}
