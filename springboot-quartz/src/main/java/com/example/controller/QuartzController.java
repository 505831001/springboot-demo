package com.example.controller;

import lombok.extern.log4j.Log4j2;
import org.quartz.*;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.util.StringUtils;
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

    //@Resource
    //private Scheduler scheduler;
    //@Resource
    //private SchedulerFactory schedulerFactory = new StdSchedulerFactory();
    //@Resource
    //private QuartzScheduler quartzScheduler;
    @Resource
    private SchedulerFactoryBean schedulerFactoryBean;

    /**
     * 添加Quartz调度器任务，启动Quartz调度器任务
     * http://localhost:8518/quartz/create
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
            @RequestParam(value = "intervalInSeconds", required = false, defaultValue = "10") int intervalInSeconds,
            Class<? extends Job> jobClass) {
        ScheduleBuilder schedule = null;
        if (StringUtils.containsWhitespace("calendarIntervalSchedule")) {
            schedule = CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withIntervalInSeconds(intervalInSeconds);
        }
        if (StringUtils.containsWhitespace("cronSchedule")) {
            schedule = CronScheduleBuilder.cronSchedule(cronExpression);
        }
        if (StringUtils.containsWhitespace("dailyTimeIntervalSchedule")) {
            schedule = DailyTimeIntervalScheduleBuilder.dailyTimeIntervalSchedule().withIntervalInSeconds(intervalInSeconds);
        }
        if (StringUtils.containsWhitespace("simpleSchedule")) {
            schedule = SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(intervalInSeconds).repeatForever();
        }
        //创建任务
        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(jobName, jobGroup)
                .withDescription(jobDescription)
                .usingJobData(jobDataKey, jobDataValue)
                .build();
        //创建触发器
        Trigger jobTrigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerName, triggerGroup)
                .withDescription(triggerDescription)
                .withPriority(triggerPriority)
                .withSchedule(CronScheduleBuilder.cronSchedule("0/10 * * * * ?"))
                .build();
        try {
            //获取实例化的调度器，将任务及其触发器放入调度器中
            //scheduler.scheduleJob(jobDetail, jobTrigger);
            //scheduler = schedulerFactory.getScheduler();
            //scheduler = schedulerFactoryBean.getScheduler();
            //quartzScheduler.scheduleJob(jobDetail, jobTrigger);
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            scheduler.scheduleJob(jobDetail, jobTrigger);
            //调度器开始调度任务
            if (!scheduler.isShutdown()) {
                scheduler.start();
                log.info("启动任务, 任务名称:{}, 分组:{}, 触发器名称:{}, 分组:{}, 间隔时间:{}s", jobName, jobGroup, triggerName, triggerGroup, cronExpression);
            }
        } catch (SchedulerException ex) {
            log.error("Quartz add task failed: {}", ex.getMessage());
            return "Failed";
        }
        return "Success";
    }

    /**
     * 更新Quartz调度器任务，指定任务
     * @param jobName
     * @param jobGroup
     * @param cronExpression
     * @return
     */
    @GetMapping(value = "/update")
    public String update(String jobName, String jobGroup, String cronExpression) {
        try {
            Scheduler   scheduler = schedulerFactoryBean.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
            Trigger    jobTrigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build();
            //用给定的键移除{Trigger}，并存储新的给定触发器-必须与同一个作业关联（新触发器必须指定作业名称和组）-但是，新触发器不必与旧触发器同名。
            scheduler.rescheduleJob(triggerKey, jobTrigger);
        } catch (SchedulerException ex) {
            ex.printStackTrace();
            return "Failed";
        }
        return "Success";
    }

    /**
     * 暂停Quartz调度器任务，不推荐，不指定任务
     * @return
     */
    @GetMapping(value = "/stand")
    public String stand() {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            //暂时停止计划程序对{Trigger}的触发。
            scheduler.standby();
            log.info("暂停任务");
        } catch (SchedulerException ex) {
            ex.printStackTrace();
            return "Failed";
        }
        return "Okay";
    }

    /**
     * 暂停Quartz调度器任务，推荐，指定任务
     * @return
     */
    @GetMapping(value = "/pause")
    public String pause(String jobName, String jobGroup) {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            //用给定的键暂停{JobDetail}——暂停所有当前的触发器。
            scheduler.pauseJob(JobKey.jobKey(jobName, jobGroup));
            log.info("暂停任务");
        } catch (SchedulerException ex) {
            ex.printStackTrace();
            return "Failed";
        }
        return "Okay";
    }

    /**
     * 恢复Quartz调度器任务，指定任务
     * @return
     */
    @GetMapping(value = "/resume")
    public String resume(String jobName, String jobGroup) {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            //使用给定的键恢复，取消暂停{JobDetail}。
            scheduler.resumeJob(JobKey.jobKey(jobName, jobGroup));
            log.info("恢复任务");
        } catch (SchedulerException ex) {
            ex.printStackTrace();
            return "Failed";
        }
        return "Okay";
    }

    /**
     * 停止Quartz调度器任务
     * @return
     */
    @GetMapping(value = "/stop")
    public String stop() {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            //shutdown(true)表示等待所有正在执行的任务执行完毕后关闭Scheduler。shutdown(false)表示直接关闭Scheduler
            scheduler.shutdown();
            log.info("停止任务");
        } catch (SchedulerException ex) {
            ex.printStackTrace();
            return "Failed";
        }
        return "Okay";
    }

    /**
     * 删除Quartz调度器任务
     * 两步，麻烦。
     * 1.如果先执行TriggerName+TriggerGroup：http://localhost:8517/quartz/delete?jobName=TriggerName&jobGroup=TriggerGroup
     * 2.那么还执行JobName+JobGroup：http://localhost:8517/quartz/delete?jobName=JobName&jobGroup=JobGroup
     * 一步，搞定。
     * 1.如果先执行JobName+JobGroup：http://localhost:8517/quartz/delete?jobName=GuestJobName&jobGroup=JobGroup
     * @param jobName
     * @param jobGroup
     * @return
     */
    @GetMapping(value = "/delete")
    public String delete(@RequestParam String jobName, @RequestParam String jobGroup) {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            //org.quartz.SchedulerException: The Scheduler has been shutdown.
            if (scheduler.isShutdown() == true) {
                scheduler.start();
            }
            scheduler.pauseTrigger(TriggerKey.triggerKey(jobName, jobGroup));
            scheduler.unscheduleJob(TriggerKey.triggerKey(jobName, jobGroup));
            //从计划程序中删除已识别的作业，以及任何相关的触发器。
            scheduler.deleteJob(JobKey.jobKey(jobName, jobGroup));
            log.info("删除任务，任务名称：{}，任务分组：{}", jobName, jobGroup);
        } catch (SchedulerException ex) {
            ex.printStackTrace();
            return "Failed";
        }
        return "Okay";
    }
}
