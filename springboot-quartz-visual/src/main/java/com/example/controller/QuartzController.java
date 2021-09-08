package com.example.controller;

import lombok.extern.log4j.Log4j2;
import org.quartz.*;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Liuweiwei
 * @since 2021-08-04
 */
@RestController
@RequestMapping(value = "/quartz")
@Log4j2
public class QuartzController {

    @Resource
    private SchedulerFactoryBean schedulerFactoryBean;

    /**
     * 添加Quartz调度器任务，启动Quartz调度器任务
     * http://localhost:8518/quartz/create
     * @return
     */
    @GetMapping(value = "/create")
    public String create() {
        //创建任务
        JobDetail jobDetail = JobBuilder.newJob(new Job() {
            @Override
            public void execute(JobExecutionContext context) throws JobExecutionException {
                String message = context.getJobDetail().getJobDataMap().getString("liu");
                log.info("第二种有状态流弊了：{}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "->" + message);
            }
        }.getClass()).build();
        //创建触发器
        Trigger jobTrigger = TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.cronSchedule("0/10 * * * * ?")).build();
        try {
            //获取实例化的调度器，将任务及其触发器放入调度器中
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            scheduler.scheduleJob(jobDetail, jobTrigger);
            //org.quartz.SchedulerException: The Scheduler has been shutdown.
            if (scheduler.isShutdown() == true) {
                scheduler.start();
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
            scheduler.rescheduleJob(triggerKey, jobTrigger);
            log.info("Scheduler reschedule job -> jobName:{}, jobGroup:{}", jobName, jobGroup);
        } catch (SchedulerException ex) {
            ex.printStackTrace();
            return "Failed";
        }
        return "Success";
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
            log.info("Scheduler pause job -> jobName:{}, jobGroup:{}", jobName, jobGroup);
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
            log.info("Scheduler resume job -> jobName:{}, jobGroup:{}", jobName, jobGroup);
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
            log.info("Scheduler shutdown job");
        } catch (SchedulerException ex) {
            ex.printStackTrace();
            return "Failed";
        }
        return "Okay";
    }

    /**
     * 删除Quartz调度器任务
     * http://localhost:8518/quartz/delete?jobName=6da64b5bd2ee-23685c67-68f6-4763-a268-bd6f36921f27&jobGroup=DEFAULT
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
            scheduler.deleteJob(JobKey.jobKey(jobName, jobGroup));
            log.info("Scheduler delete job -> jobName:{}, jobGroup:{}", jobName, jobGroup);
        } catch (SchedulerException ex) {
            ex.printStackTrace();
            return "Failed";
        }
        return "Okay";
    }
}
