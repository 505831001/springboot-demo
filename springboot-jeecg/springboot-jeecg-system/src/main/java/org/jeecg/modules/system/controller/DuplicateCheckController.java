package org.jeecg.modules.system.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.jeecg.common.vo.Result;
import org.jeecg.modules.system.mapper.SysDictMapper;
import org.jeecg.modules.system.model.DuplicateCheckVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @Title: DuplicateCheckAction
 * @Description: 重复校验工具
 * @Author 张代浩
 * @Date 2019-03-25
 * @Version V1.0
 */
@RestController
@RequestMapping(value = "/sys/duplicate")
@Api(value = "", tags = "重复校验处理控制器")
@Slf4j
public class DuplicateCheckController {

    @Autowired
    SysDictMapper sysDictMapper;

    /**
     * 校验数据是否在系统中是否存在
     *
     * @return
     */
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    @ApiOperation("重复校验接口")
    public Result<Object> doDuplicateCheck(DuplicateCheckVo duplicateCheckVo, HttpServletRequest request) {
        Long num = null;

        log.info("----duplicate check------：" + duplicateCheckVo.toString());
        if (StringUtils.isNotBlank(duplicateCheckVo.getDataId())) {
            // [2].编辑页面校验
            num = sysDictMapper.duplicateCheckCountSql(duplicateCheckVo);
        } else {
            // [1].添加页面校验
            num = sysDictMapper.duplicateCheckCountSqlNoDataId(duplicateCheckVo);
        }

        if (num == null || num == 0) {
            // 该值可用
            return Result.ok("该值可用！");
        } else {
            // 该值不可用
            log.info("该值不可用，系统中已存在！");
            return Result.error("该值不可用，系统中已存在！");
        }
    }
}
