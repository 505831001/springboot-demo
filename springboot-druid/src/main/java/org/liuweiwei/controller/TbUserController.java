package org.liuweiwei.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.liuweiwei.service.TbUserService;
import org.liuweiwei.model.TbUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(value="/findOne")
    @ApiOperation(value = "", notes = "根据用户查询用户信息")
    @ResponseBody
    public String getOne(@RequestBody TbUser tbUser) {
        TbUser user = userService.getOne(tbUser);
        return user.toString();
    }

    @GetMapping(value="/findAll")
    @ApiOperation(value = "", notes = "查询所有用户信息")
    @ResponseBody
    public String getAll() {
        List<TbUser> list = null;
        try {
            list = userService.getAll();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return list.toString();
    }
}
