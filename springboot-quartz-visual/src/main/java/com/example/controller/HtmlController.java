package com.example.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Liuweiwei
 * @since 2021-08-04
 */
@Controller
@Log4j2
public class HtmlController {

    @GetMapping(value = "/index")
    public String index() {
        log.info("请求Url.index()");
        return "index_page";
    }
}
