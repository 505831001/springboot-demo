package com.cloud.x20.openfeign.service;

import com.cloud.x20.openfeign.component.MyFallBackFactoryImpl;
import com.cloud.x20.openfeign.config.OpenFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Ribbon
 *     01. 实现方式: @RibbonClient + RestTemplate(@LoadBalanced)
 *     02. 通过微服务名称来获得调用地址
 *     03. 客户端
 * Feign
 *     01. 实现方式: @EnableFeignClients + @FeignClient
 *     02. 通过接口＋注解来获得调用服务
 *     03. 客户端
 * OpenFeign 属性说明：
 *     name:            指定要调用的微服务的名字，用于服务发现，必填
 *     value:           同NAME属性
 *     url:             URL一般用于调试，可以手动指定调用的绝对地址
 *     configuration:   FEIGN配置类，可以自定义Feign的Encoder、Decoder、LogLevel、Contract
 *     fallback:        定义容错的处理类，当调用远程接口失败或超时时，会调用对应接口的容错逻辑，fallback指定的类必须实现@FeignClient标记的接口
 *     fallbackFactory: 工厂类，用于生成fallback类示例，通过这个属性我们可以实现每个接口通用的容错逻辑，减少重复的代码
 *     path:            定义当前FeignClient的统一前缀，设置context-path的服务，这个值如果不注意配置就404了
 *     decode404:       当发生http 404错误时，如果该字段位true，会调用decoder进行解码，否则抛出FeignException，默认为false
 *
 * @author Liuweiwei
 * @since 2021-05-22
 */
@FeignClient(
        name = "CLIENT-LOG4J-SERVICE",
        value = "CLIENT-LOG4J-SERVICE",
        url = "",
        configuration = OpenFeignConfig.class,
        fallbackFactory = MyFallBackFactoryImpl.class
)
public interface OpenFeignService {
    /**
     * @RequestMapping
     * @RequestParam
     *
     * @param message
     * @return
     */
    @RequestMapping(value = "/echo", method = RequestMethod.GET)
    public String echo(@RequestParam String message);
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public String info(@RequestParam String message);
    @RequestMapping(value = "/desc", method = RequestMethod.GET)
    public String desc(@RequestParam String message);
}
