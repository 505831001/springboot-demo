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
 * @author liuweiwei
 * @since 2020-05-20
 */
@Configuration
@Log4j
@Slf4j
public class SwaggerConfig {

    /**
     * SpringBoot 默认的日志框架是：Slf4j + Logback。
     * <p>
     * 日志框架配置文档如下：
     * Logback - logback-spring.xml, logback.xml, logback-spring.groovy, logback.groovy
     * Log4j   - log4j-spring.xml, log4j.xml, log4j-spring.properties, log4j.properties
     * Log4j2  - log4j2-spring.xml, log4j2.xml
     * JDK (Java Util Logging) - logging.properties
     * <p>
     * SpringBoot 整合常用两大日志框架：
     * org.slf4j日志框架；
     * org.apache.log4j日志框架。
     * <p>
     * SpringBoot 整合常用两大日志框架实现方式：
     * 注解方式：
     *
     * @Log4j；
     * @Slf4j。 代码方式：
     * org.slf4j.Logger；
     * org.apache.log4j.Logger；
     * org.apache.logging.log4j.Logger。
     * 日志级别总共有：TRACE < DEBUG < INFO(默认级别) < WARN < ERROR < FATAL。
     */
    private static final org.slf4j.Logger slf4j = org.slf4j.LoggerFactory.getLogger(SwaggerConfig.class);

    private final org.apache.log4j.Logger log4j = org.apache.log4j.LogManager.getLogger(this.getClass());

    /**
     *  <exclusions>
     *      SpringBoot需要注意的是只有1.3.x和1.3.x以下版本才支持log4j的日志配置。1.3.x以上版本只支持log4j2
     *      <exclusion>
     *          <groupId>org.springframework.boot</groupId>
     *          <artifactId>spring-boot-starter-logging</artifactId>
     *      </exclusion>
     *  </exclusions>
     */
    // private final org.apache.logging.log4j.Logger log4j2 = org.apache.logging.log4j.LogManager.getLogger(this.getClass());

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("SpringBoot API Doc")
                .description("This is a restful api document of Spring Boot.")
                .version("1.0")
                .build();
    }
}