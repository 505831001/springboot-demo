package com.cloud.x20.hystrix.dashboard.service;

import com.cloud.x20.hystrix.dashboard.config.OpenFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Liuweiwei
 * @since 2021-05-22
 */
@FeignClient(
        name = "CLIENT-LOG4J-SERVICE",
        value = "CLIENT-LOG4J-SERVICE",
        path = "",
        configuration = OpenFeignConfig.class
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
