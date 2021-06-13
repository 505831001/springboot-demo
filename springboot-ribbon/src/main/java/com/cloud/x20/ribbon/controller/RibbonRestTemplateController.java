package com.cloud.x20.ribbon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author Liuweiwei
 * @since 2021-05-22
 */
@RestController
public class RibbonRestTemplateController {

    private static final String EUREKA_APP_NAME = "CLIENT-LOG4J-SERVICE";

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping(value = "/echo")
    public String echo(String message) {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://" + EUREKA_APP_NAME + "/echo?message=" + message, String.class);
        String body = responseEntity.getBody();
        return body;
    }
}
