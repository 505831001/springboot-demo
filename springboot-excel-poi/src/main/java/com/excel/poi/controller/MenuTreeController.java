package com.excel.poi.controller;

import com.excel.poi.entity.TbMenu;
import com.excel.poi.service.ExcelPoiService;
import com.excel.poi.utils.MenuTreeUtils;
import com.excel.poi.utils.ResultData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Liuweiwei
 * @since 2021-08-05
 */
@RestController
@Api(value = "", tags = "菜单树控制器")
@Log4j2
public class MenuTreeController {

    @Autowired
    ExcelPoiService excelPoiService;

    @RequestMapping(value = "/uploadExcel", method = RequestMethod.GET)
    @ApiOperation(value = "菜单树构建", notes = "菜单树构建", tags = "")
    public ResultData uploadExcel() {
        /**获取数据表中数据*/
        List<TbMenu> menuList = new ArrayList<>();
        menuList.add(new TbMenu("GN001", "0", "一级-系统管理", "/admin", "Y"));
        menuList.add(new TbMenu("GN001D100", "GN001", "二级-权限管理", "/admin", "Y"));
        menuList.add(new TbMenu("GN001D110", "GN001D100", "三级-密码修改", "/admin", "Y"));
        menuList.add(new TbMenu("GN001D120", "GN001D100", "三级-新加用户", "/admin", "Y"));
        menuList.add(new TbMenu("GN001D200", "GN001", "二级-系统监控", "/admin", "Y"));
        menuList.add(new TbMenu("GN001D210", "GN001D200", "三级-在线用户", "/admin", "Y"));
        menuList.add(new TbMenu("GN002", "0", "一级-订阅域名", "/admin", "Y"));
        menuList.add(new TbMenu("GN003", "0", "一级-未知领域", "/admin", "Y"));
        /**获取数据表中数据添加到树*/
        MenuTreeUtils menuTree = new MenuTreeUtils(menuList);
        menuList = menuTree.buildMenuTree();
        return ResultData.success(menuList.toArray());
    }
}
