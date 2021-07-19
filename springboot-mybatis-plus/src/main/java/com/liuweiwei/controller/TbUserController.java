package com.liuweiwei.controller;

import com.github.pagehelper.PageInfo;
import com.liuweiwei.model.TbUser;
import com.liuweiwei.service.TbUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

/**
 * @author Liuweiwei
 * @since 2021-01-06
 */
@Controller
@Api(tags = "用户控制器")
public class TbUserController {

    @Resource
    private TbUserService userService;

    @GetMapping(value="/otherGetById")
    @ApiOperation(value = "", notes = "根据用户ID查询信息")
    @ResponseBody
    public String otherGetById(@RequestParam(name = "userId", required = true, defaultValue = "1") Serializable id) {
        TbUser user = userService.otherGetById(id);
        return user.toString();
    }

    @GetMapping(value="/otherList")
    @ApiOperation(value = "", notes = "显示所有用户信息")
    @ResponseBody
    public String otherList() {
        try {
            List<TbUser> list = userService.otherList();
            return list.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @PostMapping(value = "/jdbcUpdate")
    @ResponseBody
    public Integer jdbcUpdate(@Valid @RequestBody TbUser user) {
        return userService.jdbcUpdate(user);
    }

    @GetMapping(value = "/githubPage")
    @ResponseBody
    public PageInfo<TbUser> githubPage(@RequestParam int currentNum,@RequestParam int pageSize) {
        return userService.githubPage(currentNum, pageSize);
    }
}
