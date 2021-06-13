package com.cloud.x20.hystrix.dashboard.controller;

import com.cloud.x20.hystrix.dashboard.service.OpenFeignService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.conf.HystrixPropertiesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Liuweiwei
 * @since 2021-05-22
 */
@RestController
public class OpenFeignController {

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

    @HystrixCommand(
            fallbackMethod = "fallback_method",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
            }
    )
    @GetMapping(value = "/desc")
    public String desc(String message) {
        String desc = openFeignService.desc(message);
        throw new RuntimeException(desc);
    }

    public String fallback_method(String message) {
        return "服务熔断＋服务降级＋服务限流＋限流算法：令牌桶＋漏桶：" + message;
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
