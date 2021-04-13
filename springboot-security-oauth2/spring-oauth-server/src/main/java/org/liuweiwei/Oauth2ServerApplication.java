package org.liuweiwei;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Liuweiwei
 * @since 2020-12-23
 */
@SpringBootApplication
@EnableSwagger2
@EnableWebMvc
@EnableWebSecurity
@EnableAuthorizationServer
@EnableResourceServer
public class Oauth2ServerApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Oauth2ServerApplication.class);
    }
}
