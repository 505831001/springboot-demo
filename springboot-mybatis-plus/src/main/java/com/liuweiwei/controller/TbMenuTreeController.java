package com.liuweiwei.controller;

import com.liuweiwei.model.TbMenuTree;
import com.liuweiwei.service.TbMenuTreeService;
import com.liuweiwei.utils.ResultData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author Liuweiwei
 * @since 2021-01-06
 */
@Controller
@Api(value = "", tags = "菜单树控制器")
@Slf4j
public class TbMenuTreeController {

    @Autowired
    private TbMenuTreeService menuTreeService;

    @PostMapping(value = "/insert")
    @ApiOperation(value = "新增菜单树接口", notes = "新增菜单树接口", tags = "")
    @ResponseBody
    public ResultData insert(@RequestBody TbMenuTree menuTree) {
        return menuTreeService.insertOther(menuTree);
    }

    @GetMapping(value = "/delete")
    @ApiOperation(value = "删除菜单树接口", notes = "删除菜单树接口", tags = "")
    @ResponseBody
    public ResultData delete(@RequestParam(value = "id", required = true, defaultValue = "") String id) {
        return menuTreeService.deleteOther(id);
    }

    @PostMapping(value = "/update")
    @ApiOperation(value = "编辑菜单树接口", notes = "编辑菜单树接口", tags = "")
    @ResponseBody
    public ResultData update(@RequestBody TbMenuTree menuTree) {
        return menuTreeService.updateOther(menuTree);
    }

    @GetMapping(value = "/select/id")
    @ApiOperation(value = "根据ID批量查询接口", notes = "根据ID批量查询接口", tags = "")
    @ResponseBody
    public ResultData select(@RequestParam(value = "id", required = true, defaultValue = "") String id) {
        return menuTreeService.selectOther(id);
    }

    @GetMapping(value = "/select/{id}")
    @ApiOperation(value = "根据ID批量查询接口", notes = "根据ID批量查询接口", tags = "")
    @ResponseBody
    public ResultData selectPath(@PathVariable(value = "id", required = true) String id) {
        return menuTreeService.selectPathOther(id);
    }

    @GetMapping(value = "/select/page")
    @ApiOperation(value = "分页批量查询接口", notes = "分页批量查询接口", tags = "")
    @ResponseBody
    public ResultData selectPage(@RequestParam String current, @RequestParam String size) {
        return menuTreeService.selectPageOther(current, size);
    }
}
