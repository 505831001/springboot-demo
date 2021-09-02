package org.liuweiwei.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Liuweiwei
 * @since 2021-01-05
 */
@Controller
@Api(value = "", tags = "Html页面跳转控制器")
public class HtmlController {

    @GetMapping(value = "/loginPage")
    @ApiOperation(value = "登录页面", notes = "登录页面", tags = "")
    public String loginPage() {
        return "login_page";
    }
}
