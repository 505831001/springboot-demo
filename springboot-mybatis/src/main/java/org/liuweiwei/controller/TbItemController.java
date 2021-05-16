package org.liuweiwei.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.liuweiwei.model.TbItem;
import org.liuweiwei.service.TbItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author Liuweiwei
 * @since 2021-01-06
 */
@Controller
@Api(tags = "用户控制器")
public class TbItemController {

    @GetMapping(value = "/echo")
    @ResponseBody
    public String echo(String message) {
        return "MyBatis 应用程序输出：" + message;
    }

    @Autowired
    private TbItemService itemService;

    @GetMapping(value="/findAll")
    @ApiOperation(value = "", notes = "查询所有用户信息")
    @ResponseBody
    public String findAll() {
        List<TbItem> list = itemService.findAll();
        return list.toString();
    }
}
