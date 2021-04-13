package com.springcloud.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author Liuweiwei
 * @since 2021-01-31
 */
@SpringBootApplication
@EnableDiscoveryClient
public class DemoCloudNacosConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoCloudNacosConsumerApplication.class, args);
    }

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
