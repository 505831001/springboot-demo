package com.logback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

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
 * @since 2021-01-16
 */
@SpringBootApplication
@EnableSwagger2
public class DemoLogbackApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(DemoLogbackApplication.class, args);
    }
}
