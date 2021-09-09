package com.example.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Liuweiwei
 * @since 2021-08-04
 */
@Controller
@Api(value = "", tags = "Html页面跳转控制器")
@Log4j2
public class HtmlController {

    @GetMapping(value = "/index")
    @ApiOperation(value = "公司主页", notes = "公司主页", tags = "")
    public String index() {
        log.info("请求Url.index()");
        return "index_default";
    }
}
