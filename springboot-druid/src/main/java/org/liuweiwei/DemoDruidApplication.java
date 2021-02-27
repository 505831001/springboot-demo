package org.liuweiwei;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Map;

/**
 * Druid Monitor 德鲁伊监控器地址：http://localhost:8080/druid/login.html
 *
 * Action:
 * org.apache.ibatis.binding.BindingException: Invalid bound statement (not found):
 * 配置：resources/mapper非<配置在pom.xml中>：java/mapper则YML中添加扫描配置。
 * mybatis:
 *   mapper-locations: classpath*:/resources/mapper/*.xml 或者相对路径方式：classpath*:mapper/*.xml
 *   type-aliases-package: org.example.dao
 *
 * Action:
 * Consider defining a bean of type 'org.liuweiwei.dao.TbUserMapper' in your configuration.
 * 1. 通过配置扫描注解：@MapperScan(basePackages = "org.example.dao")
 * 2. 通过注释：@Mapper
 *
 * @author Liuweiwei
 * @since 2021-01-05
 */
@SpringBootApplication
@EnableSwagger2
@EnableWebMvc
@MapperScan(basePackages = "org.liuweiwei.dao")
public class DemoDruidApplication {
    /**
     * 日志-实现层：logback<org.slf4j>
     */
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(DemoDruidApplication.class);
    /**
     * 日志-实现层：log4j<org.apache.log4j>
     */
    private static final Logger logger = LogManager.getLogger(DemoDruidApplication.class);

    /**
     * Class that can be used to bootstrap and launch a Spring application from a Java main method.
     * By default class will perform the following steps to bootstrap your application:
     */
    private static SpringApplication application;
    /**
     * SPI interface to be implemented by most if not all application contexts.
     * Provides facilities to configure an application context in addition to the application context client methods in the {ApplicationContext} interface.
     */
    private static ConfigurableApplicationContext applicationContext;
    /**
     * Configuration interface to be implemented by most if not all {Environment} types.
     * Provides facilities for setting active and default profiles and manipulating underlying property sources.
     * Allows clients to set and validate required properties, customize the conversion service and more through the {ConfigurablePropertyResolver} superinterface.
     */
    private static ConfigurableEnvironment applicationContextEnvironment;

    public static void main(String[] args) {
        application = new SpringApplication(DemoDruidApplication.class);
        application.setBannerMode(Banner.Mode.CONSOLE);
        applicationContext = application.run(args);
        applicationContextEnvironment = applicationContext.getEnvironment();

        logger.info(applicationContextEnvironment.getProperty("java.vendor.url"));
        logger.info(applicationContextEnvironment.getProperty("java.vendor.url.bug"));
        logger.info(applicationContextEnvironment.getProperty("sun.java.command") + " started successfully.");
    }
}
