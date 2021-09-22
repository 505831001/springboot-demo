package org.jeecg.erp.finance.controller;

import org.jeecg.erp.finance.service.IFinCarryForwardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.vo.Result;
import org.jeecg.common.annotation.AutoLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/finance/carryForward")
@Api(value = "", tags = "财务结转处理控制器")
@Slf4j
public class FinCarryForwardController {

    @Autowired
    IFinCarryForwardService finCarryForwardService;

    @AutoLog(value = "月末结转")
    @ApiOperation(value = "月末结转", notes = "月末结转")
    @PostMapping(value = "/month")
    public Result<?> monthCarryForward(@RequestParam(name = "year", required = true) Integer year,
                                       @RequestParam(name = "month", required = true) Integer month) {
        finCarryForwardService.monthCarryForward(year, month);
        return Result.ok("月末结转成功！");
    }

}
