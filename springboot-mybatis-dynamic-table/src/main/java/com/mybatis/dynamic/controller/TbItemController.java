package com.mybatis.dynamic.controller;

import com.mybatis.dynamic.model.TbItem;
import com.mybatis.dynamic.service.TbItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

    @Autowired
    private TbItemService itemService;


    @GetMapping(value="/findAll")
    @ApiOperation(value = "", notes = "查询All用户")
    @ResponseBody
    public String findAll() {
        List<TbItem> list = itemService.findAll();
        return list.toString();
    }
}
