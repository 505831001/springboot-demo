package com.liuweiwei.config;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author Liuweiwei
 * @since 2020-12-23
 */
@Configuration
// @Log4j - java: 程序包org.apache.log4j不存在
@Slf4j
public class SwaggerConfig {

    /**
     * SpringBoot 默认的日志框架是：Slf4j + Logback。
     *
     * 日志框架配置文档如下：
     *      Logback - logback-spring.xml, logback.xml, logback-spring.groovy, logback.groovy
     *      Log4j   - log4j-spring.xml, log4j.xml, log4j-spring.properties, log4j.properties
     *      Log4j2  - log4j2-spring.xml, log4j2.xml
     *      JDK (Java Util Logging) - logging.properties
     *
     *   # file: D:/logs/log4j.log
     * SpringBoot 整合常用两大日志框架：
     *      org.slf4j日志框架；
     *      org.apache.log4j日志框架。
     *
     * SpringBoot 整合常用两大日志框架实现方式：
     *  注解方式：
     *      @Log4j；
     *      @Slf4j。
     *  代码方式：
     *      org.slf4j.Logger；
     *      org.apache.log4j.Logger；
     *      org.apache.logging.log4j.Logger。
     * 日志级别总共有：TRACE < DEBUG < INFO(默认级别) < WARN < ERROR < FATAL。
     */

    /**
     * @Slf4j - java: 程序包org.slf4j不存在
     */
    private static final org.slf4j.Logger slf4j = org.slf4j.LoggerFactory.getLogger(SwaggerConfig.class);

    /**
     * @Log4j - java: 程序包org.apache.log4j不存在
     *
     * <dependency>
     *     <groupId>log4j</groupId>
     *     <artifactId>log4j</artifactId>
     *     <version>1.2.17</version>
     * </dependency>
     */
    // private final org.apache.log4j.Logger log4j = org.apache.log4j.LogManager.getLogger(this.getClass());

    /**
     * @Slf4j - java: 程序包org.apache.logging.log4j不存在
     *
     * 排除自带默认日志框架：Logback。如果找不到spring-boot-starter则查找spring-boot-starter-web
     * <exclusions>
     *     <exclusion>
     *         <groupId>org.springframework.boot</groupId>
     *         <artifactId>spring-boot-starter-logging</artifactId>
     *     </exclusion>
     * </exclusions>
     */
    private final org.apache.logging.log4j.Logger log4j2 = org.apache.logging.log4j.LogManager.getLogger(this.getClass());

    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("SpringBoot API Doc")
                .description("This is a restful api document of Spring Boot.")
                .version("1.0")
                .build();
    }
}