package org.log4j.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Liuweiwei
 * @since 2021-05-14
 */
@RestController
public class RibbonController {

    @GetMapping(value = "/desc")
    public String desc(String message) {
        return "Log4j 日志框架应用输出：" + message;
    }
}
