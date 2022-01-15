package com.coder.springsecurity.plus.controller;

import com.coder.springsecurity.plus.dto.JwtUserDto;
import com.coder.springsecurity.plus.dto.MenuIndexDto;
import com.coder.springsecurity.plus.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author LiuWeiWei
 * @since 2022-01-06
 */
@Controller
@RequestMapping(value = "/api")
@Api(tags = "系统：菜单路由")
public class ApiController {

    @Autowired
    private MenuService menuService;

    @GetMapping(value = "/index")
    @ResponseBody
    @ApiOperation(value = "通过用户id获取菜单")
    public List<MenuIndexDto> getMenu() {
        JwtUserDto jwtUserDto = (JwtUserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = jwtUserDto.getMyUser().getUserId();
        return menuService.getMenu(userId);
    }

    @GetMapping(value = "/console")
    @ApiOperation(value = "后台首页")
    public String console() {
        return "console/console1";
    }

    @GetMapping(value = "/form/build")
    @ApiOperation(value = "后台首页")
    public String formBuild() {
        return "system/form/index";
    }

    @GetMapping(value = "/403")
    @ApiOperation(value = "403页面")
    public String error403() {
        return "error/403";
    }

    @GetMapping(value = "/404")
    @ApiOperation(value = "404页面")
    public String error404() {
        return "error/404";
    }

    @GetMapping(value = "/500")
    @ApiOperation(value = "500页面")
    public String error500() {
        return "error/500";
    }
}
