package org.liuweiwei.controller;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.liuweiwei.model.TbItem;
import org.liuweiwei.service.TbItemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Liuweiwei
 * @since 2021-01-06
 */
@RestController
@Api(value = "", tags = "用户控制器")
public class TbItemController {

    @GetMapping(value = "/echo")
    @ApiOperation(value = "应用程序输出", notes = "应用程序输出", tags = "")
    public String echo(String message) {
        return "MyBatis 应用程序输出：" + message;
    }

    @Resource
    private TbItemService itemService;

    @GetMapping(value="/findAll")
    @ApiOperation(value = "查询所有用户信息", notes = "查询所有用户信息", tags = "")
    public String findAll() {
        List<TbItem> list = itemService.findAll();
        return list.toString();
    }

    @GetMapping(value = "/githubPage")
    @ApiOperation(value = "GitHub分页插件查询", notes = "GitHub分页插件查询", tags = "")
    public PageInfo<TbItem> githubPage(@RequestParam int currentNum, @RequestParam int pageSize) {
        return itemService.githubPage(currentNum, pageSize);
    }
}
