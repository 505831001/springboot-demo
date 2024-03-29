package org.liuweiwei.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.liuweiwei.components.ValidationGroup;
import org.liuweiwei.model.TbUser;
import org.liuweiwei.service.TbUserService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author Liuweiwei
 * @since 2021-01-06
 */
@Controller
@Api(tags = "用户管理控制器")
public class TbUserController {

    @Resource
    private TbUserService userService;

    @GetMapping(value="/getById")
    @ApiOperation(value = "", notes = "根据ID查询")
    @ResponseBody
    public String getById(@RequestParam("id")Serializable id) {
        TbUser user = userService.otherGetById(id);
        return user.toString();
    }

    @PostMapping(value="/getOne")
    @ApiOperation(value = "", notes = "根据Wrapper条件，查询一条记录")
    @ResponseBody
    public String getOne(@RequestBody @Valid TbUser tbUser) {
        QueryWrapper<TbUser> wrapper = new QueryWrapper<>();
        wrapper.setEntity(tbUser);
        TbUser user = userService.otherGetOne(wrapper);
        return user.toString();
    }

    @PostMapping(value="/getMap")
    @ApiOperation(value = "", notes = "根据Wrapper条件，查询全部记录")
    @ResponseBody
    public String getMap(@RequestBody @Validated(value = ValidationGroup.class) TbUser tbUser) {
        QueryWrapper<TbUser> wrapper = new QueryWrapper<>();
        wrapper.setEntity(tbUser);
        Map<String, Object> map = userService.otherGetMap(wrapper);
        return map.toString();
    }

    @GetMapping(value="/list")
    @ApiOperation(value = "", notes = "查询所有")
    @ResponseBody
    public String list() {
        try {
            List<TbUser> list = userService.otherList();
            return list.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
