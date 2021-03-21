package com.springcloud.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author Liuweiwei
 * @since 2021-01-31
 */
@SpringBootApplication
public class DemoCloudNacosConfigApplication {
    private static final Logger logger = LogManager.getLogger(DemoCloudNacosConfigApplication.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DemoCloudNacosConfigApplication.class, args);
        ConfigurableEnvironment environment = context.getEnvironment();
        Map<String, Object> map = environment.getSystemProperties();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            logger.info("应用程序上下文环境系统属性 -> key:{}, value:{}", entry.getKey(), entry.getValue());
        }
    }

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        /**
         * Redis 默认序列化格式：JdkSerializationRedisSerializer();
         * Redis 指定JSON序列化格式：new GenericJackson2JsonRedisSerializer();, 或者：new Jackson2JsonRedisSerializer<>(Object.class);
         */
        template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }
}
