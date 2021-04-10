package com.liuweiwei.config;

import com.liuweiwei.api.Web01MvcInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.CallableProcessingInterceptor;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.DeferredResultProcessingInterceptor;
import org.springframework.web.servlet.config.annotation.*;

import java.util.concurrent.Callable;

/**
 * @author liuweiwei
 * @since 2020-05-20
 */
@Configuration
public class WebMvcAutoConfig extends WebMvcAutoConfiguration implements WebMvcConfigurer {

    @Autowired
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
     * 同步请求拦截器：InterceptorRegistry
     *
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
     * 异步请求拦截器：CallableProcessingInterceptor
     *
     * @param configurer
     */
    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.registerCallableInterceptors(new CallableProcessingInterceptor() {
            @Override
            public <T> void beforeConcurrentHandling(NativeWebRequest request, Callable<T> task) throws Exception {

            }
        });
        configurer.registerDeferredResultInterceptors(new DeferredResultProcessingInterceptor() {
            @Override
            public <T> void beforeConcurrentHandling(NativeWebRequest request, DeferredResult<T> deferredResult) throws Exception {

            }
        });
    }
}