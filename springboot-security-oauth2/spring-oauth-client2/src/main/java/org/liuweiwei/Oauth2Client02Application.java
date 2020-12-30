package org.liuweiwei;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author Liuweiwei
 * @since 2020-12-23
 */
@SpringBootApplication
@EnableWebMvc
public class Oauth2Client02Application extends SpringBootServletInitializer {
    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

    public static void main(String[] args) {
        SpringApplication.run(Oauth2Client02Application.class, args);
    }
}
