package com.coder.springsecurity.controller;

import com.coder.springsecurity.dto.PermissionDto;
import com.coder.springsecurity.eneity.TbPermission;
import com.coder.springsecurity.service.PermissionService;
import com.coder.springsecurity.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping(value = "/system/permission")
@Api(tags = "菜单信息相关接口")
@Slf4j
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    @GetMapping(value = "/index")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('menu:list')")
    @ApiOperation(value = "菜单列表")
    public Result getMenuAll() {
        return permissionService.getMenuAll();
    }

    @GetMapping(value = "/build")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('menu:list')")
    @ApiOperation(value = "绘制菜单树")
    public Result buildMenuAll() {
        List<PermissionDto> menuAll = permissionService.buildMenuAll();
        return Result.ok().data(menuAll);
    }

    @GetMapping(value = "/ebuild/{roleId}")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('menu:list')")
    @ApiOperation(value = "通过id绘制菜单树")
    public Result buildMenuAllByRoleId(@PathVariable Integer roleId) {
        List<PermissionDto> menuAll = permissionService.buildMenuAllByRoleId(roleId);
        return Result.ok().data(menuAll);
    }

    @GetMapping(value = "/add")
    @PreAuthorize("hasAnyAuthority('menu:add')")
    @ApiOperation(value = "添加菜单页面")
    public String addPermission(Model model) {
        model.addAttribute("tbPermission", new TbPermission());
        return "/system/permission/permission-add";
    }

    @PostMapping(value = "/add")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('menu:add')")
    @ApiOperation(value = "添加菜单")
    public Result<TbPermission> savePermission(@RequestBody TbPermission permission) {
        return permissionService.save(permission);
    }

    @GetMapping(value = "/edit")
    @PreAuthorize("hasAnyAuthority('menu:edit')")
    @ApiOperation(value = "修改菜单页面")
    public String editPermission(Model model, TbPermission permission) {
        model.addAttribute("tbPermission", permissionService.getTbPermissionById(permission.getId()));
        return "/system/permission/permission-add";
    }

    @PostMapping(value = "/edit")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('menu:edit')")
    @ApiOperation(value = "通修改菜单")
    public Result updatePermission(@RequestBody TbPermission permission) {
        return permissionService.updateTbPermission(permission);
    }

    @GetMapping(value = "/delete")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('menu:del')")
    @ApiOperation(value = "删除菜单")
    public Result deletePermission(TbPermission tbPermission) {
        return permissionService.delete(tbPermission.getId());
    }

    @GetMapping(value = "/menu")
    @ResponseBody
    @ApiOperation(value = "通过id获取菜单")
    public Result<TbPermission> getMenu(Integer userId) {
        return permissionService.getMenu(userId);
    }
}
