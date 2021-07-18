package com.example.controller;

import com.example.model.TbUser;
import com.example.service.TbUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author Liuweiwei
 * @since 2021-01-06
 */
@Controller
@Api(tags = "用户控制器")
public class TbUserController {

    @Autowired
    private TbUserService userService;

    @GetMapping(value="/otherById")
    @ApiOperation(value = "", notes = "Notes ID查询用户")
    @ResponseBody
    public String otherById(@RequestParam(name = "id", required = true, defaultValue = "1") Serializable id) {
        TbUser user = userService.otherById(id);
        return user.toString();
    }

    @PostMapping(value="/otherOne")
    @ApiOperation(value = "", notes = "根据用户字段查询用户信息")
    @ResponseBody
    public String otherOne(@RequestBody TbUser tbUser) {
        TbUser user = userService.otherOne(tbUser);
        return user.toString();
    }

    @GetMapping(value="/otherList")
    @ApiOperation(value = "", notes = "显示所有用户信息")
    @ResponseBody
    public String otherList() {
        List<TbUser> list = null;
        try {
            list = userService.otherList();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list.toString();
    }
}
