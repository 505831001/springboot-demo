package org.example.config;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/**
 * @author LiuWeiWei
 * @since 2022-01-10
 */
@Configuration
public class GlobalExceptionConfig {
    @Bean
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryWebServerFactoryCustomizer() {
        return (factory -> {
            ErrorPage errorPage403 = new ErrorPage(HttpStatus.FORBIDDEN, "/403");
            ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, "/404");
            ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500");
            factory.addErrorPages(errorPage403);
            factory.addErrorPages(errorPage404);
            factory.addErrorPages(errorPage500);
        });
    }
}
