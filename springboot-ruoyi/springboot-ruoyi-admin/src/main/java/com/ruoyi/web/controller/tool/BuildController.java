package com.ruoyi.web.controller.tool;

import com.ruoyi.common.core.controller.BaseController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * build 表单构建
 *
 * @author ruoyi
 */
@Controller
@RequestMapping("/tool/build")
@Api(value = "", tags = "表单构建页面控制器")
@Slf4j
public class BuildController extends BaseController {

    private String prefix = "tool/build";

    @RequiresPermissions("tool:build:view")
    @GetMapping()
    public String build() {
        return prefix + "/build";
    }
}
