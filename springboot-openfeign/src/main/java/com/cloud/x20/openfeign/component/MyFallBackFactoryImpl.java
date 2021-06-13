package com.cloud.x20.openfeign.component;

import com.cloud.x20.openfeign.service.OpenFeignService;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 熔断降级托底实现类
 *
 * @author Liuweiwei
 * @since 2021-05-27
 */
@Component
public class MyFallBackFactoryImpl implements FallbackFactory<OpenFeignService> {
    @Override
    public OpenFeignService create(Throwable cause) {
        return new OpenFeignService() {
            @Override
            public String echo(String message) {
                return "spring cloud hystrix - circuit breaker exception information: " + message;
            }

            @Override
            public String info(String message) {
                return "spring cloud hystrix - circuit breaker exception information: " + message;
            }

            @Override
            public String desc(String message) {
                return "spring cloud hystrix - circuit breaker exception information: " + message;
            }
        };
    }
}
