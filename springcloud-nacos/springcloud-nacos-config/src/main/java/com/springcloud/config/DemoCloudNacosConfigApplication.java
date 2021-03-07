package com.springcloud.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
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
            logger.info("Application Context Environment System Properties -> key:{}, value:{}", entry.getKey(), entry.getValue());
        }
    }

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
