package com.example.component;

import lombok.extern.log4j.Log4j2;
import org.quartz.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 触发定时任务有两种方式：第二种是定时执行。
 * @author Liuweiwei
 * @since 2021-09-02
 */
//@EnableScheduling
//@Component
@Log4j2
public class SchedulerTaskOneC {

    @Resource
    private SchedulerFactoryBean schedulerFactoryBean;

    /**
     * 同时启动两个定时任务
     *
     * @throws SchedulerException
     */
    @Scheduled(cron = "*/10 * * * * ?")
    public void process() throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();

        // ---------- 华丽的分割线 ----------

        scheduler.scheduleJob(
                JobBuilder
                        .newJob(new Job() {
                            @Override
                            public void execute(JobExecutionContext context) throws JobExecutionException {
                                String message = context.getJobDetail().getJobDataMap().getString("liu");
                                log.info("第二种有状态流弊了：{}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "->" + message);
                            }
                        }.getClass())
                        //.withIdentity("abcDetails", "ADMIN")
                        .usingJobData("liu", "Jack")
                        .build()
                ,
                TriggerBuilder
                        .newTrigger()
                        //.withIdentity("abcTrigger", "ADMIN")
                        .withSchedule(CronScheduleBuilder.cronSchedule("0/10 * * * * ?"))
                        .build());

        // ---------- 华丽的分割线 ----------

        scheduler.scheduleJob(
                JobBuilder
                        .newJob(new Job() {
                            @Override
                            public void execute(JobExecutionContext context) throws JobExecutionException {
                                String message = context.getJobDetail().getJobDataMap().getString("lin");
                                log.info("第二种有状态逗比了：{}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "->" + message);
                            }
                        }.getClass())
                        //.withIdentity("xyzDetails", "GUEST")
                        .usingJobData("lin", "Lucy")
                        .build()
                ,
                TriggerBuilder
                        .newTrigger()
                        //.withIdentity("xyzTrigger", "GUEST")
                        .withSchedule(CronScheduleBuilder.cronSchedule("0/10 * * * * ?"))
                        .build());
    }
}
