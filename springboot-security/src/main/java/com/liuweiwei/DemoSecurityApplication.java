package com.liuweiwei;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Liuweiwei
 * @since 2020-12-21
 */
@SpringBootApplication
@EnableSwagger2
@EnableWebSecurity
public class DemoSecurityApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoSecurityApplication.class, args);
    }
}
