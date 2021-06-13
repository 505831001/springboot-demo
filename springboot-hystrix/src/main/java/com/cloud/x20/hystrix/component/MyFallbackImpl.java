package com.cloud.x20.hystrix.component;

import com.cloud.x20.hystrix.service.OpenFeignService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

/**
 * fallback
 *     1. 回退类指定的装客户端接口。
 *     2. 回退类必须实现接口注释的注释和是一个有效的spring bean。
 *     3. @return后备类指定的装客户端接口。
 * fallbackFactory
 *     1. 定义一个后备工厂指定的装客户端接口。
 *     2. 回退工厂必须产生回退实现接口的类的实例的注释{@link FeignClient}。必须是一个有效的spring bean的后备工厂。
 *     3. @return后备工厂指定的装客户端接口。
 *
 * @author Liuweiwei
 * @since 2021-05-26
 */
@Component
public class MyFallbackImpl implements OpenFeignService {
    @Override
    public String echo(String message) {
        return "fallback回退类指定Feign调用的装客户端接口。" + message;
    }

    @Override
    public String info(String message) {
        return "fallback回退类指定Feign调用的装客户端接口。" + message;
    }

    @Override
    public String desc(String message) {
        return "fallback回退类指定Feign调用的装客户端接口。" + message;
    }
}
