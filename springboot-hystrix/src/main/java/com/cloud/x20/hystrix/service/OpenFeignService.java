package com.cloud.x20.hystrix.service;

import com.cloud.x20.hystrix.component.MyFallBackFactoryImpl;
import com.cloud.x20.hystrix.component.MyFallbackImpl;
import com.cloud.x20.hystrix.config.OpenFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
 * @since 2021-05-22
 */
@FeignClient(
        name = "CLIENT-LOG4J-SERVICE", //Feign
        value = "CLIENT-LOG4J-SERVICE",
        path = "",
        url = "http://localhost:8858/", //Ribbon+RestTemplate(HTTP)
        configuration = OpenFeignConfig.class,
        fallback = MyFallbackImpl.class,
        fallbackFactory = MyFallBackFactoryImpl.class
)
public interface OpenFeignService {
    /**
     * @param message
     * @return
     * @RequestMapping
     * @RequestParam
     */
    @RequestMapping(value = "/echo", method = RequestMethod.GET)
    public String echo(@RequestParam String message);
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public String info(@RequestParam String message);
    @RequestMapping(value = "/desc", method = RequestMethod.GET)
    public String desc(@RequestParam String message);
}
