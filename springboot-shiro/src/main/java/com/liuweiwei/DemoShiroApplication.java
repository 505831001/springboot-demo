package com.liuweiwei;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Liuweiwei
 * @since 2020-12-23
 */
@SpringBootApplication
@EnableSwagger2
public class DemoShiroApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoShiroApplication.class, args);
    }
}
