package com.liuweiwei;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Liuweiwei
 * @since 2020-12-21
 */
@SpringBootApplication
@EnableSwagger2
@EnableWebMvc
@EnableWebSecurity
public class DemoSecurityApplication {

    /**
     * 日志-实现层：log4j
     */
    private static final Logger logger = LogManager.getLogger(DemoSecurityApplication.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DemoSecurityApplication.class, args);
        ConfigurableEnvironment environment = context.getEnvironment();
        logger.info(environment.getProperty("java.vendor.url"));
        logger.info(environment.getProperty("java.vendor.url.bug"));
        logger.info(environment.getProperty("sun.java.command") + "started successfully");
    }
}
