package org.liuweiwei.controller;

import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Liuweiwei
 * @since 2021-08-04
 */
@Controller
@Api(value = "", tags = "Html页面跳转控制器")
@Log4j2
public class HtmlController {

    @GetMapping(value = "/echo")
    @ResponseBody
    public String echo() {
        log.info("请求Url.echo()");
        return "Hello World";
    }

    @GetMapping(value = "/admin/echo")
    @ResponseBody
    public String echo2() {
        log.info("请求Url.echo2012()");
        return "Hello World 2212";
    }

    @GetMapping(value = "/guest/echo")
    @ResponseBody
    public String echo4() {
        log.info("请求Url.echo2014()");
        return "Hello World 2214";
    }
}
