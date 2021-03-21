package com.liuweiwei.controller;

import com.liuweiwei.model.TbUser;
import com.liuweiwei.service.TbUserService;
import com.liuweiwei.utils.PageRequest;
import com.liuweiwei.utils.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping(value = "/update")
    @ApiOperation(value = "", notes = "根据用户ID更新用户信息")
    @ResponseBody
    public Integer update(TbUser tbUser) {
        return userService.update(tbUser);
    }

    @PostMapping(value = "/page")
    @ApiOperation(value = "", notes = "分页查询用户信息")
    @ResponseBody
    public PageResult findPage(PageRequest pageRequest) {
        return userService.findPage(pageRequest);
    }

    @GetMapping(value="/findByUserId")
    @ApiOperation(value = "", notes = "根据用户ID查询信息")
    @ResponseBody
    public String findByUserId(@RequestParam(name = "userId", required = true, defaultValue = "1") Long userId) {
        TbUser user = userService.findByUserId(userId);
        return user.toString();
    }

    @GetMapping(value="/list")
    @ApiOperation(value = "", notes = "显示所有用户信息")
    @ResponseBody
    public String findAll() {
        List<TbUser> list = userService.findAll();
        return list.toString();
    }
}
