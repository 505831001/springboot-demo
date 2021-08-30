package org.liuweiwei.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;

/**
 * @author LiuWeiWei
 * @since 2021-08-30
 */
@Configuration
public class HttpSessionConfig {

    @Bean
    public HeaderHttpSessionIdResolver httpSessionStrategy() {
        return new HeaderHttpSessionIdResolver("x-auth-token");
    }
}
