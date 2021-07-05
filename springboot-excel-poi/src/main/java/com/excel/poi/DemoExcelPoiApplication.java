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
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Action: Consider defining a bean of type 'com.excel.poi.dao.TbUserMapper' in your configuration.
 *
 * @author Liuweiwei
 * @since 2021-07-04
 */
@SpringBootApplication
@Log4j2
@EnableSwagger2
@MapperScan(basePackages = {"com.excel.poi.dao"})
public class DemoExcelPoiApplication {
    /**
     * 日志-实现层：logback<org.slf4j>
     */
    //private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DemoExcelPoiApplication.class);
    /**
     * 日志-实现层：log4j<org.apache.log4j>
     */
    //private static final Logger LOGGER = LogManager.getLogger(DemoExcelPoiApplication.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DemoExcelPoiApplication.class, args);
        ConfigurableEnvironment environment = context.getEnvironment();
        log.info(environment.getProperty("java.vendor.url"));
        log.info(environment.getProperty("java.vendor.url.bug"));
        log.info(environment.getProperty("sun.java.command") + " started successfully.");
    }
}
