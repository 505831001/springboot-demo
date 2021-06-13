package com.cloud.x20.hystrix.config;

import com.netflix.hystrix.HystrixCommandProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;

import java.util.stream.Collectors;

/**
 * feign - HTTP客户端配置
 *
 * @author Liuweiwei
 * @since 2021-05-22
 */
@Configuration
public class OpenFeignConfig {
    /**
     * No qualifying bean of type 'org.springframework.boot.autoconfigure.http.HttpMessage'
     */
    @Bean
    @ConditionalOnMissingBean
    public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
        return new HttpMessageConverters(converters.orderedStream().collect(Collectors.toList()));
    }
}
