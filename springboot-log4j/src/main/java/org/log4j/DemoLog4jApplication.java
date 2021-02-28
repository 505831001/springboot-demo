package org.log4j;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * SpringBoot 日志框架
 * 1. 在项目的开发中，日志是必不可少的一个记录事件的组件，所以也会相应的在项目中实现和构建我们所需要的日志框架。
 * 2. 而市面上常见的日志框架有很多，比如：JCL、SLF4J、Jboss-logging、jUL、log4j、log4j2、logback等等，我们该如何选择呢？
 * 3. 通常情况下，日志是由一个"抽象层"+"实现层"的组合来搭建的。
 * 4. 菜鸡们，菜鸟们，看清楚抽象和实现两个概念。那些说有SLF4J和Logback的菜鸡们，菜鸟们。
 * 日志<抽象层>：
 *     JCL
 *     SLF4J
 *     jboss-logging
 * 日志<实现层>：
 *     JUL
 *     log4j
 *     log4j2
 *     logback
 *
 * @author Liuweiwei
 * @since 2021-01-17
 */
@SpringBootApplication
public class DemoLog4jApplication extends SpringBootServletInitializer {

    /**
     * 日志-实现层：logback<org.slf4j>
     */
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(DemoLog4jApplication.class);
    /**
     * 日志-实现层：log4j<org.apache.log4j>
     */
    private static final Logger logger = LogManager.getLogger(DemoLog4jApplication.class);

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
        application = new SpringApplication(DemoLog4jApplication.class);
        application.setBannerMode(Banner.Mode.CONSOLE);
        applicationContext = application.run(args);
        applicationContextEnvironment = applicationContext.getEnvironment();

        logger.info(applicationContextEnvironment.getProperty("java.vendor.url"));
        logger.info(applicationContextEnvironment.getProperty("java.vendor.url.bug"));
        logger.info(applicationContextEnvironment.getProperty("sun.java.command") + " started successfully.");
    }
}
