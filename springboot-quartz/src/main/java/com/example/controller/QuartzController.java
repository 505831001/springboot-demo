package com.example.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Liuweiwei
 * @since 2021-08-04
 */
@RestController
@RequestMapping(value = "/quartz")
@Log4j2
public class QuartzController {

    /**
     * Form 表单提交数据格式：
     * 1. enctype="multipart/form-data"               类型主要是上传文件时用到
     * 2. enctype="application/x-www-form-urlencoded" 类型主要是提交k-v时用到，当然这种方法也可以将json设置在v中提交json数据
     * 3. enctype="text/plain"                        类型主要是传递json数据用到，层次比较深的数据
     *
     * Quartz 数据持久化，5张表必须有数据：
     * 1. qrtz_triggers
     * 2. qrtz_job_details
     * 3. qrtz_cron_triggers
     * 4. qrtz_locks
     * 5. qrtz_fired_triggers
     * @return
     */
    @PostMapping(value = "/insert")
    public String insert(String jobName, String jobGroupName, String jobClassName, String triggerName, String triggerGroupName, String cronExpression) {
        Map<String, Object> map = new HashMap<>();
        map.put("a", jobName);
        map.put("b", jobGroupName);
        map.put("c", jobClassName);
        map.put("d", triggerName);
        map.put("e", triggerGroupName);
        map.put("f", cronExpression);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            log.info("jobKey:{}, jobValue:{}", entry.getKey(), entry.getValue());
        }
        log.info("请求Url.insert()");
        return "index_page";
    }
}
