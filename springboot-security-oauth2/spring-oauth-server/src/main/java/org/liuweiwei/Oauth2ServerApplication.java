package org.liuweiwei;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Administrator
 */
@SpringBootApplication
@EnableSwagger2
@EnableResourceServer
public class Oauth2ServerApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(Oauth2ServerApplication.class);
    }
}
