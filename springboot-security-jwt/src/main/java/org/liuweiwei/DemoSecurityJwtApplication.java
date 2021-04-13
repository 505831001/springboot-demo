package org.liuweiwei;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Liuweiwei
 * @since 2020-12-31
 */
@SpringBootApplication
@EnableSwagger2
@EnableWebMvc
@EnableWebSecurity
@EnableAuthorizationServer
@Log4j2
public class DemoSecurityJwtApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoSecurityJwtApplication.class, args);
    }
}
