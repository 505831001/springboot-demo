package com.liuweiwei.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import com.liuweiwei.components.ValidationGroup;
import com.liuweiwei.model.TbUser;
import com.liuweiwei.service.TbUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "用户控制器")
public class TbUserController {

    @Resource
    private TbUserService userService;

    @GetMapping(value = "/otherGetById")
    @ApiOperation(value = "", notes = "根据ID查询")
    @ResponseBody
    public String otherGetById(@RequestParam("id") Serializable id) {
        TbUser user = userService.otherGetById(id);
        return user.toString();
    }

    @PostMapping(value = "/otherGetOne")
    @ApiOperation(value = "", notes = "根据Wrapper条件，查询一条记录")
    @ResponseBody
    public String otherGetOne(@RequestBody @Valid TbUser tbUser) {
        QueryWrapper<TbUser> wrapper = new QueryWrapper<>();
        wrapper.setEntity(tbUser);
        TbUser user = userService.otherGetOne(wrapper);
        return user.toString();
    }

    @PostMapping(value = "/otherGetMap")
    @ApiOperation(value = "", notes = "根据Wrapper条件，查询全部记录")
    @ResponseBody
    public String otherGetMap(@RequestBody @Validated(value = ValidationGroup.class) TbUser tbUser) {
        QueryWrapper<TbUser> wrapper = new QueryWrapper<>();
        wrapper.setEntity(tbUser);
        Map<String, Object> map = userService.otherGetMap(wrapper);
        return map.toString();
    }

    @PostMapping(value = "/otherListByIds")
    @ApiOperation(value = "", notes = "查询（根据ID 批量查询）")
    @ResponseBody
    public List<TbUser> otherListByIds(@RequestBody List<Integer> ids) {
        List<TbUser> list = userService.otherListByIds(ids);
        return list;
    }

    @PostMapping(value = "/otherListByMap")
    @ApiOperation(value = "", notes = "查询（根据 columnMap 条件）")
    @ResponseBody
    public List<TbUser> otherListByMap(@RequestBody Map<String, Object> map) {
        List<TbUser> list = userService.otherListByMap(map);
        return list;
    }

    @GetMapping(value = "/otherPage")
    @ApiOperation(value = "", notes = "无条件翻页查询")
    @ResponseBody
    public Page<TbUser> otherPage(@RequestParam long current, @RequestParam long size) {
        Page<TbUser> pages = userService.otherPage(new Page<>(current, size));
        return pages;
    }

    @GetMapping(value = "/otherPageMaps")
    @ApiOperation(value = "", notes = "无条件翻页查询")
    @ResponseBody
    public Page<Map<String, Object>> otherPageMaps(@RequestParam long current, @RequestParam long size) {
        Page<Map<String, Object>> maps = userService.otherPageMaps(new Page<>(current, size));
        return maps;
    }

    @PostMapping(value = "/jdbcUpdate")
    @ResponseBody
    public Integer jdbcUpdate(@Valid @RequestBody TbUser user) {
        return userService.jdbcUpdate(user);
    }

    @GetMapping(value = "/githubPage")
    @ResponseBody
    public PageInfo<TbUser> githubPage(@RequestParam int currentNum, @RequestParam int pageSize) {
        return userService.githubPage(currentNum, pageSize);
    }
}
