package com.springcloud.producer;

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
public class DemoCloudNacosProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoCloudNacosProducerApplication.class, args);
    }

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
