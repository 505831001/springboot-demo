package com.example.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.model.QrtzJobDetails;
import com.example.service.QrtzJobDetailsService;
import com.example.utils.ResultData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Liuweiwei
 * @since 2021-08-04
 */
@RestController
@Api(value = "", tags = "Quartz定时框架任务控制器")
@RequestMapping(value = "/quartz")
public class QuartzController {

    @Resource
    private QrtzJobDetailsService qrtzJobDetailsService;

    @PostMapping(value = "/create")
    @ApiOperation(value = "创建任务", notes = "创建任务", tags = "")
    public ResultData create(@RequestBody QrtzJobDetails dto) {
        return qrtzJobDetailsService.scheduleJob(dto);
    }

    @PostMapping(value = "/add")
    @ApiOperation(value = "新增任务", notes = "新增任务", tags = "")
    public ResultData add(@RequestBody QrtzJobDetails dto) {
        return qrtzJobDetailsService.addJob(dto);
    }

    @PostMapping(value = "/update")
    @ApiOperation(value = "编辑任务", notes = "编辑任务", tags = "")
    public ResultData update(@RequestBody QrtzJobDetails dto) {
        return qrtzJobDetailsService.rescheduleJob(dto);
    }

    @GetMapping(value = "/pause")
    @ApiOperation(value = "暂停任务", notes = "暂停任务", tags = "")
    public ResultData pause(@RequestParam String jobName, @RequestParam String jobGroup) {
        return qrtzJobDetailsService.pauseJob(jobName, jobGroup);
    }

    @GetMapping(value = "/resume")
    @ApiOperation(value = "恢复任务", notes = "恢复任务", tags = "")
    public ResultData resume(@RequestParam String jobName, @RequestParam String jobGroup) {
        return qrtzJobDetailsService.resumeJob(jobName, jobGroup);
    }

    @GetMapping(value = "/delete")
    @ApiOperation(value = "删除任务", notes = "删除任务", tags = "")
    public ResultData delete(@RequestParam String jobName, @RequestParam String jobGroup) {
        return qrtzJobDetailsService.deleteJob(jobName, jobGroup);
    }

    @GetMapping(value = "/interrupt")
    @ApiOperation(value = "中断任务", notes = "中断任务", tags = "")
    public ResultData interrupt(@RequestParam String jobName, @RequestParam String jobGroup) {
        return qrtzJobDetailsService.interrupt(jobName, jobGroup);
    }

    @GetMapping(value = "/checkExists")
    @ApiOperation(value = "校验任务", notes = "校验任务", tags = "")
    public ResultData checkExists(@RequestParam String jobName, @RequestParam String jobGroup) {
        return qrtzJobDetailsService.checkExists(jobName, jobGroup);
    }

    @GetMapping(value = "/stop")
    @ApiOperation(value = "停止任务", notes = "停止任务", tags = "")
    public ResultData stop() {
        return qrtzJobDetailsService.shutdown();
    }

    // ---------- 华丽的分割线 ----------

    @GetMapping(value = "/query")
    @ApiOperation(value = "查询任务", notes = "查询任务", tags = "")
    public Page<QrtzJobDetails> query(@RequestParam(value = "pageNum") Long pageNum, @RequestParam(value = "pageSize") Long pageSize) {
        return qrtzJobDetailsService.getQrtzJobDetails(pageNum, pageSize);
    }
}