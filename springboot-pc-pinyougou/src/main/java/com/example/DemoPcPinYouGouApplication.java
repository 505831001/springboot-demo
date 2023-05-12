package com.example;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 1.SpringBoot静态资源存放于Spring-Boot-AutoConfigure-2.2.4.Release.jar
 * 2.ResourceProperties类中有个全量字符数组定义String[] CLASSPATH_RESOURCE_LOCATIONS=
 * 3.资源字符数组={"classpath:/META-INF/resources/", "classpath:/resources/", "classpath:/static/", "classpath:/public/"};
 * 4.1.静态资源核心配置方式一：spring.resources.static-locations=classpath:/resources/, classpath:/static/, classpath:/public/
 * 4.2.静态资源核心配置方式二：spring.mvc.static-path-pattern=/static/**
 * 4.1.此配置适用于WebApp目录下
 * 4.2.此配置适用于Resources目录下
 *
 * @author Liuweiwei
 * @since 2021-01-08
 */
@SpringBootApplication
@MapperScan(basePackages = "com.example.dao")
@Slf4j
public class DemoPcPinYouGouApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(DemoPcPinYouGouApplication.class);
        application.setBannerMode(Banner.Mode.CONSOLE);
        ConfigurableApplicationContext applicationContext = application.run(args);
        ConfigurableEnvironment applicationContextEnvironment = applicationContext.getEnvironment();
        log.info(applicationContextEnvironment.getProperty("java.vendor.url"));
        log.info(applicationContextEnvironment.getProperty("java.vendor.url.bug"));
        log.info(applicationContextEnvironment.getProperty("sun.java.command") + " started successfully.");
    }
}
