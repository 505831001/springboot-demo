package com.liuweiwei.controller;

import com.liuweiwei.model.TbUser;
import com.liuweiwei.service.TbUserService;
import com.liuweiwei.utils.PageRequest;
import com.liuweiwei.utils.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
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
    @ApiOperation(value = "", notes = "根据用户ID查询信息")
    @ResponseBody
    public String otherById(@RequestParam(name = "userId", required = true, defaultValue = "1") Serializable id) {
        TbUser user = userService.otherById(id);
        return user.toString();
    }

    @GetMapping(value="/otherList")
    @ApiOperation(value = "", notes = "显示所有用户信息")
    @ResponseBody
    public String otherList() {
        List<TbUser> list = null;
        try {
            list = userService.otherList();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return list.toString();
    }

    @PostMapping(value = "/otherPage")
    @ApiOperation(value = "", notes = "分页查询用户信息")
    @ResponseBody
    public PageResult otherPage(PageRequest pageRequest) {
        return userService.otherPage(pageRequest);
    }

    @PostMapping(value = "/otherUpdate")
    @ApiOperation(value = "", notes = "根据用户ID更新用户信息")
    @ResponseBody
    public Integer otherUpdate(@RequestBody TbUser tbUser) {
        return userService.otherUpdate(tbUser);
    }
}
