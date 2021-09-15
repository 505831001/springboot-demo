package com.ruoyi.web.controller.system;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 首页
 *
 * @author ruoyi
 */
@RestController
@Api(value = "", tags = "首页请求处理控制器")
@Slf4j
public class SysIndexController {
    /**
     * 系统基础配置
     */
    @Resource
    private RuoYiConfig ruoyiConfig;

    /**
     * 访问首页，提示语
     */
    @GetMapping(value = "/")
    @ApiOperation(value = "首页请求处理", notes = "首页请求处理", tags = "")
    public String index() {
        return StringUtils.format("欢迎使用{}后台管理框架，当前版本：v{}，请通过前端地址访问。", ruoyiConfig.getName(), ruoyiConfig.getVersion());
    }
}
