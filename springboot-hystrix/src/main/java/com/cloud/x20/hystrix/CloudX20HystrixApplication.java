package com.cloud.x20.hystrix;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 断路器(Hystrix) - 隔离策略
 *     1. 线程池隔离(thread)(默认)
 *         1. 第三方应用或者接口
 *         2. 并发量大
 *         --
 *         1. 与调用线程不是同一个线程，因此支持异步
 *         2. 线程排队，调度，上下文需要切换，调度的消耗
 *         3. 并发支持，最大并发量取决于线程池大小
 *         4. 适合有计算，会有耗时操作的服务
 *         5. 保护Tomcat线程，不至于Tomcat一直阻塞，一旦不对，立马失败或者超时返回
 *     2. 信号量隔离(semaphore)
 *         1. 内部应用或者中间件(redis)
 *         2. 并发需求不大
 *         --
 *         1. 与调用线程是同一个线程(jetty线程)，因此只能同步
 *         2. 无线程切换，开销低，因此可快速进行反应
 *         3. 并发支持，最大并发量取决于信号量大小
 *         4. 适合低计算，快速通信的服务
 *
 * 1. to use thread isolation    - 线程池隔离(默认)
 *     HystrixCommandProperties.Setter().withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD);
 *     @HystrixCommand(commandProperties = @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_STRATEGY, value = "THREAD"))
 * 2. to use semaphore isolation - 信号量隔离
 *     HystrixCommandProperties.Setter().withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE);
 *     @HystrixCommand(commandProperties = @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_STRATEGY, value = "SEMAPHORE"))
 *
 * @author Liuweiwei
 * @since 2021-05-22
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableCircuitBreaker
public class CloudX20HystrixApplication {

    /**
     * 日志-实现层：log4j<org.apache.log4j>
     */
    private static final Logger LOGGER = LogManager.getLogger(CloudX20HystrixApplication.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(CloudX20HystrixApplication.class, args);
        ConfigurableEnvironment environment = context.getEnvironment();
        LOGGER.info(environment.getProperty("java.vendor.url"));
        LOGGER.info(environment.getProperty("java.vendor.url.bug"));
        LOGGER.info(environment.getProperty("sun.java.command") + " started successfully.");
    }
}
