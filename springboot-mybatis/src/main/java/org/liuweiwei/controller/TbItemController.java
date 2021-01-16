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
@Api(value = "Value 用户控制器", tags = "Tags 用户控制器", description = "Description 用户控制器")
public class TbItemController {

    @Autowired
    private TbItemService itemService;


    @GetMapping(value="/findAll")
    @ApiOperation(value = "Value 查询All用户", notes = "Notes 查询All用户", tags = "Tags 查询All用户")
    @ResponseBody
    public String findAll() {
        List<TbItem> list = itemService.findAll();
        return list.toString();
    }
}
