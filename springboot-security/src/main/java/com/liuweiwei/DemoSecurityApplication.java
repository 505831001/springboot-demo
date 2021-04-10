package com.liuweiwei;

import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.LoggerFactory;
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
@MapperScan(basePackages = {"com.liuweiwei.mapper"})
@EnableSwagger2
@EnableWebMvc
@EnableWebSecurity
@Slf4j
public class DemoSecurityApplication {

    /**
     * 日志-实现层：logback
     */
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DemoSecurityApplication.class);

    /**
     * 日志-实现层：log4j
     */
    private static final Logger logger2 = LogManager.getLogger(DemoSecurityApplication.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DemoSecurityApplication.class, args);
        ConfigurableEnvironment environment = context.getEnvironment();
        log.info(environment.getProperty("java.vendor.url"));
        log.info(environment.getProperty("java.vendor.url.bug"));
        log.info(environment.getProperty("sun.java.command") + "started successfully");
    }
}
