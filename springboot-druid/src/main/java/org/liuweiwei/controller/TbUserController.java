package org.liuweiwei.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.liuweiwei.service.TbUserService;
import org.liuweiwei.model.TbUser;
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
@Api(tags = "用户管理控制器")
public class TbUserController {

    @Autowired
    private TbUserService userService;

    @PostMapping(value="/query")
    @ApiOperation(value = "", notes = "根据用户字段查询用户信息")
    @ResponseBody
    public String otherOne(@RequestBody TbUser tbUser) {
        TbUser user = userService.otherOne(tbUser);
        return user.toString();
    }

    @GetMapping(value="/list")
    @ApiOperation(value = "", notes = "查询所有用户信息")
    @ResponseBody
    public String otherAll() {
        List<TbUser> list = null;
        try {
            list = userService.otherAll();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return list.toString();
    }

    @GetMapping(value="/details")
    @ApiOperation(value = "", notes = "根据用户主键查询用户详情")
    @ResponseBody
    public String otherDetails(@RequestParam("id")Serializable id) {
        TbUser user = userService.otherDetails(id);
        return user.toString();
    }
}
