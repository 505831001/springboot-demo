package com.ruoyi.web.controller.monitor;

import com.ruoyi.common.core.controller.BaseController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * druid 德鲁伊监控
 *
 * @author ruoyi
 */
@Controller
@RequestMapping("/monitor/data")
@Api(value = "", tags = "德鲁伊监控页面控制器")
@Slf4j
public class DruidController extends BaseController {

    private String prefix = "/druid";

    @RequiresPermissions("monitor:data:view")
    @GetMapping()
    public String index() {
        return redirect(prefix + "/index.html");
    }
}
