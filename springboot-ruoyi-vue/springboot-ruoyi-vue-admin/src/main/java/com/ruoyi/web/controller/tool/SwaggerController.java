package com.ruoyi.web.controller.tool;

import com.ruoyi.common.core.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
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
@Api(value = "", tags = "丝袜哥接口处理控制器")
@Slf4j
public class SwaggerController extends BaseController {

    @PreAuthorize("@ss.hasPermi('tool:swagger:view')")
    @GetMapping(value = "")
    @ApiOperation(value = "丝袜哥接口列表", notes = "丝袜哥接口列表", tags = "")
    public String index() {
        return redirect("/swagger-ui.html");
    }
}
