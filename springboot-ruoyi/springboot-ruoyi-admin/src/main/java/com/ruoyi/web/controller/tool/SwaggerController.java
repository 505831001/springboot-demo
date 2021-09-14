package com.ruoyi.web.controller.tool;

import com.ruoyi.common.core.controller.BaseController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * swagger 丝袜哥接口
 *
 * @author ruoyi
 */
@Controller
@RequestMapping("/tool/swagger")
@Api(value = "", tags = "丝袜哥接口页面控制器")
@Slf4j
public class SwaggerController extends BaseController {

    @RequiresPermissions("tool:swagger:view")
    @GetMapping()
    public String index() {
        return redirect("/swagger-ui/index.html");
    }
}
