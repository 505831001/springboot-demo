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
@Log4j
@Slf4j
public class SwaggerConfig {

    private static final org.slf4j.Logger slf4j = org.slf4j.LoggerFactory.getLogger(SwaggerConfig.class);

    private final org.apache.log4j.Logger log4j = org.apache.log4j.LogManager.getLogger(this.getClass());

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