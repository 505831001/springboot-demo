package org.liuweiwei.config;

import lombok.extern.log4j.Log4j2;
import org.liuweiwei.component.Web01MvcInterceptor;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

import javax.annotation.Resource;

/**
 * @author liuweiwei
 * @since 2020-05-20
 */
@Configuration
@Log4j2
public class WebMvcAutoConfig extends WebMvcAutoConfiguration implements WebMvcConfigurer {

    @Resource
    private Web01MvcInterceptor mvcInterceptor;

    /**
     * 1. 允许跨域访问的路径。
     * 2. 允许跨域访问的源。
     * 3. 允许请求方法。
     * 4. 预检间隔时间。
     * 5. 允许头部设置。
     * 6. 是否发送cookie。
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("POST", "DELETE", "PUT", "GET", "OPTIONS")
                .maxAge(60 * 60 * 24 * 7)
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    /**
     * 同步请求拦截器：
     * org.springframework.web.servlet.config.annotation.InterceptorRegistry
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(mvcInterceptor);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

    }

    /**
     * 异步请求拦截器：
     * org.springframework.web.context.request.async.CallableProcessingInterceptor
     * org.springframework.web.context.request.async.DeferredResultProcessingInterceptor
     * @param configurer
     */
    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {

    }
}