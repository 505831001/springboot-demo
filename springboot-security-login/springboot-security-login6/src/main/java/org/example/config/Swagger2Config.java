package org.example.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Liuweiwei
 * @since 2020-12-31
 */
@EnableSwagger2
@Configuration
@Slf4j
public class Swagger2Config {
    @Bean
    public Docket createRestApi() {
        //添加请求参数，我们这里把token作为请求头部参数传入后端。
        List<Parameter> parameters = new ArrayList<Parameter>();
        parameters.add(new ParameterBuilder().name("Authorization").description("Token").modelRef(new ModelRef("string")).parameterType("header").required(false).build());

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(parameters);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("SpringBoot API Doc")
                .description("This is a restful api document of Spring Boot.")
                .version("2.9.2")
                .build();
    }
}