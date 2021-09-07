package com.example.controller;

import lombok.extern.log4j.Log4j2;
import org.quartz.*;
import org.quartz.impl.DirectSchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
     * Quartz 数据持久化，5张表必须有数据：
     * 1. qrtz_triggers
     * 2. qrtz_job_details
     * 3. qrtz_cron_triggers
     * 4. qrtz_locks
     * 5. qrtz_fired_triggers
     * 接收Quartz调度器任务
     * @return
     */
    @PostMapping(value = "/receive")
    public String receive(String jobName, String jobGroupName, String jobClassName, String triggerName, String triggerGroupName, String cronExpression) {
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

    @Resource
    private SchedulerFactoryBean schedulerFactoryBean;
    @Resource
    private SchedulerFactory schedulerFactory = new StdSchedulerFactory();

    /**
     * 添加Quartz调度器任务，启动Quartz调度器任务
     * Quartz 4种Scheduler调度器：
     * 1.schedule = CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withIntervalInSeconds(10);
     * 2.schedule = CronScheduleBuilder.cronSchedule("0/10 * * * * ?");
     * 3.schedule = DailyTimeIntervalScheduleBuilder.dailyTimeIntervalSchedule().withIntervalInSeconds(10);
     * 4.schedule = SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10).repeatForever();
     * @param jobName
     * @param jobGroup
     * @param jobDescription
     * @param jobDataKey
     * @param jobDataValue
     * @param triggerName
     * @param triggerGroup
     * @param triggerDescription
     * @param triggerPriority
     * @param cronExpression
     * @param jobClass
     * @return
     */
    @GetMapping(value = "/create")
    public String create(
            @RequestParam(value = "jobName", required = false, defaultValue = "JobName") String jobName,
            @RequestParam(value = "jobGroup", required = false, defaultValue = "JobGroup") String jobGroup,
            @RequestParam(value = "jobDescription", required = false, defaultValue = "JobDescription") String jobDescription,
            @RequestParam(value = "jobDataKey", required = false, defaultValue = "email") String jobDataKey,
            @RequestParam(value = "jobDataValue", required = false, defaultValue = "liuweiwei@163.com") String jobDataValue,
            @RequestParam(value = "triggerName", required = false, defaultValue = "TriggerName") String triggerName,
            @RequestParam(value = "triggerGroup", required = false, defaultValue = "TriggerGroup") String triggerGroup,
            @RequestParam(value = "triggerDescription", required = false, defaultValue = "TriggerDescription") String triggerDescription,
            @RequestParam(value = "triggerPriority", required = false, defaultValue = "5") int triggerPriority,
            @RequestParam(value = "cronExpression", required = false, defaultValue = "0/10 * * * * ?") String cronExpression,
            Class<? extends Job> jobClass) {
        //创建任务
        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(jobName, jobGroup)
                .withDescription(jobDescription)
                .usingJobData(jobDataKey, jobDataValue)
                .build();
        //创建触发器，每time秒钟执行一次
        CronTrigger jobTrigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerName, triggerGroup)
                .withDescription(triggerDescription)
                .withPriority(triggerPriority)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .build();
        try {
            //获取实例化的Scheduler
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            scheduler = schedulerFactory.getScheduler();
            //将任务及其触发器放入调度器
            scheduler.scheduleJob(jobDetail, jobTrigger);
            //调度器开始调度任务
            if (!scheduler.isShutdown()) {
                scheduler.start();
                log.info("启动任务, 任务名称:{}, 分组:{}, 触发器名称:{}, 分组:{}, 间隔时间:{}s", jobName, jobGroup, triggerName, triggerGroup, cronExpression);
            }
        } catch (SchedulerException ex) {
            ex.printStackTrace();
        }
        return "Okay";
    }

    /**
     * 暂停Quartz调度器任务
     * @return
     */
    @GetMapping(value = "/pause")
    public String pause() {
        Scheduler scheduler = null;
        try {
            scheduler = schedulerFactory.getScheduler();
            scheduler.standby();
            log.info("暂停任务");
        } catch (SchedulerException ex) {
            ex.printStackTrace();
        }
        return "Okay";
    }

    /**
     * 停止Quartz调度器任务
     * @return
     */
    @GetMapping(value = "/stop")
    public String stop() {
        Scheduler scheduler = null;
        try {
            /**
             *  shutdown(true)  表示等待所有正在执行的任务执行完毕后关闭Scheduler
             *  shutdown(false) 表示直接关闭Scheduler
             */
            scheduler = schedulerFactory.getScheduler();
            scheduler.shutdown();
            log.info("停止任务");
        } catch (SchedulerException ex) {
            ex.printStackTrace();
        }
        return "Okay";
    }

    /**
     * 删除Quartz调度器任务
     * @param jobName
     * @param jobGroupName
     * @return
     */
    @GetMapping(value = "/delete")
    public String delete(@RequestParam String jobName, @RequestParam String jobGroupName) {
        Scheduler scheduler = null;
        try {
            scheduler = schedulerFactory.getScheduler();
            scheduler.pauseTrigger(TriggerKey.triggerKey(jobName, jobGroupName));
            scheduler.unscheduleJob(TriggerKey.triggerKey(jobName, jobGroupName));
            scheduler.deleteJob(JobKey.jobKey(jobName, jobGroupName));
            log.info("删除任务，任务名称：{}，任务分组：{}", jobName, jobGroupName);
        } catch (SchedulerException ex) {
            ex.printStackTrace();
        }
        return "Okay";
    }
}
