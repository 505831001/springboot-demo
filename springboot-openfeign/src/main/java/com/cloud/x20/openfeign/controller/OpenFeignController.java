package com.cloud.x20.openfeign.controller;

import com.cloud.x20.openfeign.service.OpenFeignService;
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

    @GetMapping(value = "/echo")
    public String echo(String message) {
        return openFeignService.echo(message);
    }
}
