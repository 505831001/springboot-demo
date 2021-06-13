package com.cloud.x20.hystrix.controller;

import com.cloud.x20.hystrix.service.OpenFeignService;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.conf.HystrixPropertiesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.netflix.hystrix.HystrixCommandProperties.ExecutionIsolationStrategy;
import static com.netflix.hystrix.HystrixCommandProperties.Setter;

/**
 * @author Liuweiwei
 * @since 2021-05-22
 */
@RestController
public class OpenFeignController {

    private static final int value = 10;

    {
        // to use thread isolation
        Setter().withExecutionIsolationStrategy(ExecutionIsolationStrategy.THREAD);

        // to use semaphore isolation
        Setter().withExecutionIsolationStrategy(ExecutionIsolationStrategy.SEMAPHORE);

        // 设置使用semaphore隔离策略的时候允许访问的最大并发量，超过这个最大并发量，请求直接被reject
        Setter().withExecutionIsolationSemaphoreMaxConcurrentRequests(value);

        // set command thread pool - default int 10
        HystrixThreadPoolProperties.Setter().withCoreSize(value);

        // set queue size
        HystrixThreadPoolProperties.Setter().withQueueSizeRejectionThreshold(value);
    }

    @Autowired
    private OpenFeignService openFeignService;

    /**
     * 1. to use thread isolation - 线程池隔离
     *     HystrixCommandProperties.Setter().withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD);
     *     @HystrixCommand(commandProperties = @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_STRATEGY, value = "THREAD"))
     *     @HystrixCommand(commandProperties = @HystrixProperty(name = "execution.isolation.strategy", value ="THREAD"))
     * 2. to use semaphore isolation - 信号量隔离
     *     HystrixCommandProperties.Setter().withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE);
     *     @HystrixCommand(commandProperties = @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_STRATEGY, value = "SEMAPHORE"))
     *     @HystrixCommand(commandProperties = @HystrixProperty(name = "execution.isolation.strategy", value ="SEMAPHORE"))
     *
     * @param message
     * @return
     */
    @HystrixCommand(
            fallbackMethod = "fallback_method",
            commandProperties = {
                    @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_STRATEGY, value = "THREAD")
            }
    )
    @GetMapping(value = "/echo")
    public String echo(String message) {
        String echo = openFeignService.echo(message);
        throw new RuntimeException(echo);
    }

    @HystrixCommand(
            fallbackMethod = "fallback_method",
            commandProperties = {
                    @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_STRATEGY, value = "SEMAPHORE")
            }
    )
    @GetMapping(value = "/info")
    public String info(String message) {
        String info = openFeignService.info(message);
        throw new RuntimeException(info);
    }

    @GetMapping(value = "/desc")
    public String desc(String message) {
        String desc = openFeignService.desc(message);
        return desc;
    }

    public String fallback_method(String message) {
        return "服务熔断＋服务降级＋服务限流＋限流算法(令牌桶＋漏桶)+应用级限流(Tomcat限流/MySQL限流/Redis限流)+分布式限流(Redis限流/Nginx限流)+Java实现:" + message;
    }

    /**
     * 配置线程池隔离策略 + 自定义线程池
     *
     * @param message
     * @return
     */
    @HystrixCommand(
            fallbackMethod = "fallback_method",
            commandProperties = {
                    @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_STRATEGY, value = "THREAD")
            },
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "10"),
                    @HystrixProperty(name = "maxQueueSize", value = "20"),
                    @HystrixProperty(name = "keepAliveTimeMinutes", value = "0"),
                    @HystrixProperty(name = "queueSizeRejectionThreshold", value = "15"),
                    @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "12"),
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "1440")
            }
    )
    @GetMapping(value = "/abc")
    public String abc(String message) {
        return openFeignService.echo(message);
    }

    /**
     * 配置信号量隔离策略 + 自定义信号量
     *
     * @param message
     * @return
     */
    @HystrixCommand(
            fallbackMethod = "fallback_method",
            commandProperties = {
                    @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_STRATEGY, value = "SEMAPHORE"),
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "500"),
                    @HystrixProperty(name = "execution.isolation.semaphore.maxConcurrentRequests", value = "9"),
                    @HystrixProperty(name = "fallback.isolation.semaphore.maxConcurrentRequests", value = "20"),
                    @HystrixProperty(name = "execution.isolation.strategy", value ="SEMAPHORE")
            }
    )
    @GetMapping(value = "/xyz")
    public String xyz(String message) {
        return openFeignService.echo(message);
    }
}
