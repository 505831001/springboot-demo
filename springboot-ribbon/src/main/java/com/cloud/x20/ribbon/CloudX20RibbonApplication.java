package com.cloud.x20.ribbon;

import com.netflix.loadbalancer.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.client.RestTemplate;

/**
 * @author Liuweiwei
 * @since 2021-05-18
 */
@SpringBootApplication
@EnableEurekaClient
public class CloudX20RibbonApplication {

    /**
     * 日志-实现层：log4j<org.apache.log4j>
     */
    private static final Logger LOGGER = LogManager.getLogger(CloudX20RibbonApplication.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(CloudX20RibbonApplication.class, args);
        ConfigurableEnvironment environment = context.getEnvironment();
        LOGGER.info(environment.getProperty("java.vendor.url"));
        LOGGER.info(environment.getProperty("java.vendor.url.bug"));
        LOGGER.info(environment.getProperty("sun.java.command") + " started successfully.");
    }

    /**
     * Ribbon
     *     01. 实现方式: @RibbonClient + RestTemplate(@LoadBalanced)
     *     02. 通过微服务名称来获得调用地址
     *     03. 客户端
     * Feign
     *     01. 实现方式: @EnableFeignClients + @FeignClient
     *     02. 通过接口＋注解来获得调用服务
     *     03. 客户端
     *
     * @return
     */
    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public IRule iRule() {
        IRule rule = new AvailabilityFilteringRule();
        rule = new WeightedResponseTimeRule();
        rule = new BestAvailableRule();
        rule = new ZoneAvoidanceRule();
        rule = new RoundRobinRule();
        rule = new RandomRule();
        rule = new RetryRule();
        return rule;
    }
}
