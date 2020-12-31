package org.liuweiwei.config;

import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * @author Liuweiwei
 * @since 2020-12-31
 */
@Configuration
public class WebMvcAutoConfig extends WebMvcAutoConfiguration implements WebMvcConfigurer {

    /**
     * 1. 允许跨域访问的路径。
     * 2. 允许跨域访问的源。
     * 3. 允许请求方法。
     * 4. 预检间隔时间。
     * 5. 允许头部设置。
     * 6. 是否发送cookie。
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                .maxAge(60 * 60 * 24 * 7)
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}