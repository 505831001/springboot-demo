package org.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author Liuweiwei
 * @since 2021-09-11
 */
@Configuration
public class Swagger3Config {

    private Boolean swaggerEnabled = true;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                .enable(swaggerEnabled)
                //.pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.example.controller"))
                //.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                //.apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("SpringBoot 整合 Swagger3 API Doc")
                .description("This is a restful api document of Swagger.")
                .version("1.0")
                .contact(new Contact("abc", "xyz", "123"))
                .license("license")
                .licenseUrl("licenseURL")
                .build();
    }
}
