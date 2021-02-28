package com.example.controller;

import com.example.model.TbUser;
import com.example.service.TbUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @GetMapping(value="/findByUserId")
    @ApiOperation(value = "", notes = "Notes ID查询用户")
    @ResponseBody
    public String findByUserId(@RequestParam(name = "userId", required = true, defaultValue = "1") Long userId) {
        TbUser user = userService.findByUserId(userId);
        return user.toString();
    }

    @GetMapping(value="/findAll")
    @ApiOperation(value = "", notes = "Notes 查询All用户")
    @ResponseBody
    public String findAll() {
        List<TbUser> list = userService.findAll();
        return list.toString();
    }
}
