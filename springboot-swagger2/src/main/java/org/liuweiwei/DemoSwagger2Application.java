package org.liuweiwei;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Liuweiwei
 * @since 2021-01-05
 */
@SpringBootApplication
@EnableSwagger2
@EnableWebMvc
public class DemoSwagger2Application extends SpringBootServletInitializer {

    /**
     * 日志-实现层：logback<org.slf4j>
     */
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(DemoSwagger2Application.class);
    /**
     * 日志-实现层：log4j<org.apache.log4j>
     */
    private static final Logger logger = LogManager.getLogger(DemoSwagger2Application.class);

    /**
     * 类，可以用来从Java主方法引导和启动Spring应用程序。
     * 默认情况下，class将执行以下步骤来引导应用程序：
     */
    private static SpringApplication application;
    /**
     * 大部分(如果不是全部)应用程序上下文都要实现的SPI接口。
     * 除了{ApplicationContext}接口中的应用程序上下文客户端方法之外，还提供了配置应用程序上下文的工具。
     */
    private static ConfigurableApplicationContext applicationContext;
    /**
     * 大多数(如果不是全部){Environment}类型都要实现的配置接口。
     * 提供用于设置活动和默认概要文件以及操作底层属性源的工具。
     * 允许客户端通过{ConfigurablePropertyResolver}超接口设置和验证所需的属性，自定义转换服务和更多。
     */
    private static ConfigurableEnvironment applicationContextEnvironment;

    public static void main(String[] args) {
        application = new SpringApplication(DemoSwagger2Application.class);
        application.setBannerMode(Banner.Mode.CONSOLE);
        applicationContext = application.run(args);
        applicationContextEnvironment = applicationContext.getEnvironment();

        logger.info(applicationContextEnvironment.getProperty("java.vendor.url"));
        logger.info(applicationContextEnvironment.getProperty("java.vendor.url.bug"));
        logger.info(applicationContextEnvironment.getProperty("sun.java.command") + " started successfully.");
    }
}
