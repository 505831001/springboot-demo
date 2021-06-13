package com.cloud.x20.hystrix.dashboard;

import com.cloud.x20.hystrix.dashboard.service.OpenFeignService;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import feign.Feign;
import feign.Target;
import feign.hystrix.HystrixFeign;
import feign.hystrix.SetterFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.EnableTurbine;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;

import java.lang.reflect.Method;

/**
 * @author Liuweiwei
 * @since 2021-05-26
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = {"com.cloud.x20.hystrix.dashboard.service"})
@EnableCircuitBreaker
@EnableHystrix
@EnableTurbine
@EnableHystrixDashboard
public class CloudX20HystrixDashboardApplication {

    /**
     * 日志-实现层：log4j<org.apache.log4j>
     */
    private static final Logger LOGGER = LogManager.getLogger(CloudX20HystrixDashboardApplication.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(CloudX20HystrixDashboardApplication.class, args);
        ConfigurableEnvironment environment = context.getEnvironment();
        LOGGER.info(environment.getProperty("java.vendor.url"));
        LOGGER.info(environment.getProperty("java.vendor.url.bug"));
        LOGGER.info(environment.getProperty("sun.java.command") + " started successfully.");
    }

    /*
    @Bean
    public Feign.Builder feignHystrixBuilder() {
        SetterFactory setterFactory = new SetterFactory() {
            @Override
            public HystrixCommand.Setter create(Target<?> target, Method method) {
                return HystrixCommand.Setter
                        .withGroupKey(HystrixCommandGroupKey.Factory.asKey(OpenFeignService.class.getSimpleName()))
                        .andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(10000));
            }
        };
        return HystrixFeign.builder().setterFactory(setterFactory);
    }
    */

    @Bean
    public HystrixMetricsStreamServlet initialServlet() {
        return new HystrixMetricsStreamServlet();
    }

    @Bean
    public ServletRegistrationBean getServlet(HystrixMetricsStreamServlet servlet) {
        ServletRegistrationBean register = new ServletRegistrationBean();
        register.setServlet(servlet);
        register.setLoadOnStartup(1);
        register.addUrlMappings("/actuator/hystrix.stream");
        register.setName("HystrixMetricsStreamServlet");
        return register;
    }
}
