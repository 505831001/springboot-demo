package com.excel.poi.controller;

import com.alibaba.fastjson.JSONObject;
import com.excel.poi.service.TbMenuTreeService;
import com.excel.poi.utils.ResultData;
import com.excel.poi.vo.TbMenuTreeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

/**
 * @author Liuweiwei
 * @since 2021-08-05
 */
@RestController
@Api(value = "", tags = "菜单树控制器")
@Log4j2
public class MenuTreeController {

    @Resource
    TbMenuTreeService menuTreeService;

    @RequestMapping(value = "/getMenuTree", method = RequestMethod.GET)
    @ApiOperation(value = "菜单树构建", notes = "菜单树构建", tags = "")
    public ResultData getMenuTree() {
        try {
            List<TbMenuTreeVO> voList = menuTreeService.otherList();
            return ResultData.success(voList.toArray());
        } catch (Exception ex) {
            log.error("菜单树形结构查询异常, {}", ex.getMessage(), ex);
            log.error("菜单树形结构查询异常, list:{}", JSONObject.toJSONString(null));
            log.error("菜单树形结构查询异常, ids:{}", StringUtils.join(null));
            return ResultData.failure(ex.getMessage());
        }
    }

    @RequestMapping(value = "/getFolders", method = RequestMethod.GET)
    @ApiOperation(value = "获取文件夹文件", notes = "获取文件夹文件", tags = "")
    public ResultData getFolders() {
        /**获取此目录File对象*/
        File directories = new File("D:\\i4Tools");
        if (directories.exists()) {
            getFiles(directories);
        } else {
            log.error("此目录不存在: {}" + directories.getPath());
        }
        return ResultData.success();
    }

    private void getFiles(File directories) {
        File[] files = directories.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                getFiles(file);
                log.info("目录相关: {}", file.getPath());
            } else {
                log.info("文件相关: {}", file.getName());
            }
        }
    }
}
