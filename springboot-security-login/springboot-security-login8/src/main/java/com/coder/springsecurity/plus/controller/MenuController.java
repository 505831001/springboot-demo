package com.coder.springsecurity.plus.controller;

import com.coder.springsecurity.plus.dto.MenuDto;
import com.coder.springsecurity.plus.entity.MyMenu;
import com.coder.springsecurity.plus.service.MenuService;
import com.coder.springsecurity.plus.utils.Result;
import com.coder.springsecurity.plus.utils.ResultCode;
import com.coder.springsecurity.plus.log.aop.MyLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author LiuWeiWei
 * @since 2022-01-06
 */
@Controller
@RequestMapping("/api/menu")
@Api(tags = "系统：菜单管理")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("index")
    @PreAuthorize("hasAnyAuthority('menu:list')")
    @ApiOperation(value = "返回菜单页面")
    public String index(){
        return "system/menu/menu";
    }


    @GetMapping
    @ResponseBody
    @ApiOperation(value = "菜单列表")
    @PreAuthorize("hasAnyAuthority('menu:list')")
    @MyLog("查询菜单")
    public Result getMenuAll(String queryName,Integer queryType){
        return Result.ok().data(menuService.getMenuAll(queryName,queryType)).code(ResultCode.TABLE_SUCCESS);
    }

    @GetMapping("/build")
    @ResponseBody
    @ApiOperation(value = "绘制菜单树")
    @PreAuthorize("hasAnyAuthority('menu:add','menu:edit')")
    @MyLog("绘制菜单树")
    public Result buildMenuAll(){
        List<MenuDto> menuAll =menuService.buildMenuAll();
        return Result.ok().data(menuAll);
    }

    @GetMapping("/ebuild/{roleId}")
    @ResponseBody
    @ApiOperation(value = "通过id绘制菜单树")
    @PreAuthorize("hasAnyAuthority('role:add','role:edit')")
    @MyLog("通过id绘制菜单树")
    public Result buildMenuAllByRoleId(@PathVariable Integer roleId){
        List<MenuDto> menuAll =menuService.buildMenuAllByRoleId(roleId);
        return Result.ok().data(menuAll);
    }

    @GetMapping(value = "/edit")
    @ApiOperation(value = "修改菜单页面")
    @PreAuthorize("hasAnyAuthority('menu:edit')")
    public String editPermission(Model model, MyMenu myMenu) {
        model.addAttribute("myMenu",menuService.getMenuById(myMenu.getMenuId()));
        return "system/menu/menu-edit";
    }

    @PutMapping
    @ResponseBody
    @ApiOperation(value = "修改菜单")
    @PreAuthorize("hasAnyAuthority('menu:edit')")
    @MyLog("修改菜单")
    public Result updateMenu(@RequestBody MyMenu menu) {
        return menuService.updateMenu(menu);
    }


    @GetMapping(value = "/add")
    @ApiOperation(value = "添加菜单页面")
    @PreAuthorize("hasAnyAuthority('menu:add')")
    public String addMenu(Model model) {
        model.addAttribute("myMenu",new MyMenu());
        return "system/menu/menu-add";
    }

    @PostMapping
    @ResponseBody
    @ApiOperation(value = "添加菜单")
    @PreAuthorize("hasAnyAuthority('menu:add')")
    @MyLog("添加菜单")
    public Result<MyMenu> savePermission(@RequestBody MyMenu myMenu) {
        return menuService.save(myMenu);
    }


    /**
     * @param menuId
     * @return
     */
    @DeleteMapping
    @ResponseBody
    @ApiOperation(value = "删除菜单")
    @PreAuthorize("hasAnyAuthority('menu:del')")
    @MyLog("删除菜单")
    public Result deleteMenu(Integer menuId) {
        return menuService.delete(menuId);
    }

}
