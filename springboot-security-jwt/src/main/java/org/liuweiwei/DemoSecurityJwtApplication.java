package org.liuweiwei;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Liuweiwei
 * @since 2020-12-31
 */
@SpringBootApplication
@EnableSwagger2
@EnableWebSecurity
public class DemoSecurityJwtApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoSecurityJwtApplication.class, args);
    }
}
