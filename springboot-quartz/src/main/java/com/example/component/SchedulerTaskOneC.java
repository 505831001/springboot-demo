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
@EnableScheduling
@Component
@Log4j2
public class SchedulerTaskOneC {

    @Resource
    private SchedulerFactoryBean schedulerFactoryBean;

    /**
     * 同时启动两个定时任务
     * Quartz 4种Scheduler调度器：
     * 1.schedule = CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withIntervalInSeconds(10);
     * 2.schedule = CronScheduleBuilder.cronSchedule("0/10 * * * * ?");
     * 3.schedule = DailyTimeIntervalScheduleBuilder.dailyTimeIntervalSchedule().withIntervalInSeconds(10);
     * 4.schedule = SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10).repeatForever();
     * @throws SchedulerException
     */
    @Scheduled(cron = "*/30 * * * * ?")
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
                        .withDescription("JobDescription")
                        .withIdentity("LiuJobName", "JobGroup")
                        .usingJobData("liu", "LiuWeiWei")
                        .storeDurably(true)
                        .build()
                ,
                TriggerBuilder
                        .newTrigger()
                        .withPriority(5)
                        .withDescription("TriggerDescription")
                        .withIdentity("LiuTriggerName", "TriggerGroup")
                        .withSchedule(CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withIntervalInSeconds(10))
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
                        .withDescription("JobDescription")
                        .withIdentity("WeiJobName", "JobGroup")
                        .usingJobData("lin", "LinYiMing")
                        .storeDurably(true)
                        .build()
                ,
                TriggerBuilder
                        .newTrigger()
                        .withPriority(5)
                        .withDescription("TriggerDescription")
                        .withIdentity("WeiTriggerName", "TriggerGroup")
                        .withSchedule(CronScheduleBuilder.cronSchedule("0/10 * * * * ?"))
                        .build());
    }
}
