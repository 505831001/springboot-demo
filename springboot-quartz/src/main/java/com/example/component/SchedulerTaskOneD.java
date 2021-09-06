package com.example.component;

import lombok.extern.log4j.Log4j2;
import org.quartz.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 触发定时任务有两种方式：第一种是项目启动时执行。
 * @author Liuweiwei
 * @since 2021-09-02
 */
//@Component
@Log4j2
public class SchedulerTaskOneD implements CommandLineRunner {

    class HeTask implements Job {
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            String message = context.getJobDetail().getJobDataMap().getString("liu");
            log.info("第三种有状态嘻嘻了：{}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "->" + message);
        }
    }

    private void scheduleJobAbc(Scheduler scheduler) throws SchedulerException {
        ScheduleBuilder schedule = null;
        schedule = CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withIntervalInSeconds(10);
        schedule = CronScheduleBuilder.cronSchedule("0/10 * * * * ?");
        schedule = DailyTimeIntervalScheduleBuilder.dailyTimeIntervalSchedule().withIntervalInSeconds(10);
        schedule = SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10).repeatForever();
        CronScheduleBuilder cronSchedule = CronScheduleBuilder.cronSchedule("0/10 * * * * ?");
        JobDetail jobDetail = JobBuilder
                .newJob(HeTask.class)
                //.withIdentity("opqDetails", "ADMIN")
                .usingJobData("liu", "LiuWeiWei")
                .build();
        CronTrigger jobTrigger = TriggerBuilder
                .newTrigger()
                //.withIdentity("opqTrigger", "ADMIN")
                .withSchedule(cronSchedule)
                .build();
        scheduler.scheduleJob(jobDetail, jobTrigger);
    }

    // ---------- 华丽的分割线 ----------

    class MyTask implements Job {
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            String message = context.getJobDetail().getJobDataMap().getString("lin");
            log.info("第三种有状态哈哈了：{}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "->" + message);
        }
    }

    private void scheduleJobXyz(Scheduler scheduler) throws SchedulerException {
        ScheduleBuilder schedule = null;
        schedule = CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withIntervalInSeconds(10);
        schedule = CronScheduleBuilder.cronSchedule("0/10 * * * * ?");
        schedule = DailyTimeIntervalScheduleBuilder.dailyTimeIntervalSchedule().withIntervalInSeconds(10);
        schedule = SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10).repeatForever();
        CronScheduleBuilder cronSchedule = CronScheduleBuilder.cronSchedule("0/10 * * * * ?");
        JobDetail jobDetail = JobBuilder
                .newJob(MyTask.class)
                //.withIdentity("rstDetails", "GUEST")
                .usingJobData("lin", "LinYiMing")
                .build();
        CronTrigger jobTrigger = TriggerBuilder
                .newTrigger()
                //.withIdentity("rstTrigger", "GUEST")
                .withSchedule(cronSchedule)
                .build();
        scheduler.scheduleJob(jobDetail, jobTrigger);
    }

    // ---------- 华丽的分割线 ----------

    @Resource
    private SchedulerFactoryBean schedulerFactoryBean;

    /**
     * 同时启动两个定时任务
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        scheduleJobAbc(scheduler);
        scheduleJobXyz(scheduler);
    }
}
