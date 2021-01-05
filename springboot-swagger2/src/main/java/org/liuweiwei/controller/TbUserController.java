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
@Api(tags = "Tags 用户控制器", value = "Value 用户控制器", description = "Description 用户控制器")
public class TbUserController {

    @GetMapping(value = "/loginPage")
    @ApiOperation(value = "对应于操作的摘要字段", notes = "对应于操作的备注字段", tags = "用于API文档控制的标签列表")
    public String lgoinPage() {
        return "login_page";
    }
}
