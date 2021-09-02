package com.example;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * No qualifying bean of type 'com.example.dao.TbUserMapper' available: expected at least 1 bean which qualifies as autowire candidate.
 * 没有"com.example.dao.TbUserMapper"类型的合格bean可用：至少需要1个符合autowire候选条件的bean。
 *
 * @author Liuweiwei
 * @since 2021-01-10
 */
@SpringBootApplication
@EnableSwagger2
@Log4j2
public class DemoDataJdbcApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DemoDataJdbcApplication.class, args);
        ConfigurableEnvironment environment = context.getEnvironment();
        log.info(environment.getProperty("java.vendor.url"));
        log.info(environment.getProperty("java.vendor.url.bug"));
        log.info(environment.getProperty("sun.java.command") + " started successfully.");
    }
}
