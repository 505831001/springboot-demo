package com.excel.poi;

import jdk.nashorn.internal.runtime.logging.Logger;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
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
 * Action: Consider defining a bean of type 'com.excel.poi.dao.TbUserMapper' in your configuration.
 *
 * @author Liuweiwei
 * @since 2021-07-04
 */
@SpringBootApplication
@EnableSwagger2
@EnableWebMvc
@MapperScan(basePackages = {"com.excel.poi.dao"})
@EnableWebSecurity
@Log4j2
public class DemoExcelPoiApplication {
    /**
     * 同步日志-实现层：logback<org.slf4j>
     * private static final org.slf4j.Logger SLF4J = LoggerFactory.getLogger(DemoExcelPoiApplication.class);
     *
     * 异步日志-实现层：log4j<org.apache.log4j>
     * private static final Logger LOG4J2 = LogManager.getLogger(DemoExcelPoiApplication.class);
     */
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DemoExcelPoiApplication.class, args);
        ConfigurableEnvironment environment = context.getEnvironment();
        log.info(environment.getProperty("java.vendor.url"));
        log.info(environment.getProperty("java.vendor.url.bug"));
        log.info(environment.getProperty("sun.java.command") + " started successfully.");
    }
}
