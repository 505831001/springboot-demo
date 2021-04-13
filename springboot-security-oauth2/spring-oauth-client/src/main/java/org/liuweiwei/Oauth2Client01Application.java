package org.liuweiwei;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author Liuweiwei
 * @since 2020-12-23
 */
@SpringBootApplication
@EnableWebMvc
@EnableOAuth2Sso
public class Oauth2Client01Application {

    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

    public static void main(String[] args) {
        SpringApplication.run(Oauth2Client01Application.class, args);
    }
}
