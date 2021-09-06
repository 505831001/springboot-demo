package com.example.component;

import lombok.extern.log4j.Log4j2;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 触发定时任务有两种方式：第二种是定时执行。
 * @author Liuweiwei
 * @since 2021-09-02
 */
@Configuration
@Log4j2
public class SchedulerTaskOneE {

    private static final String JOB_KEY = "message";
    private static final String JOB_VAL = "Hello World";

    /**
     * 1.作业执行上下文：org.quartz.JobExecutionContext
     * public interface JobExecutionContext {
     *     public Scheduler getScheduler();
     *     public Trigger getTrigger();
     *     public Calendar getCalendar();
     *     public boolean isRecovering();
     *     public TriggerKey getRecoveringTriggerKey()throws IllegalStateException;
     *     public int getRefireCount();
     *     public JobDataMap getMergedJobDataMap();
     *     public JobDetail getJobDetail();
     *     public Job getJobInstance();
     *     public Date getFireTime();
     *     public Date getScheduledFireTime();
     *     public Date getPreviousFireTime();
     *     public Date getNextFireTime();
     *     public String getFireInstanceId();
     *     public Object getResult();
     *     public void setResult(Object result);
     *     public long getJobRunTime();
     *     public void put(Object key,Object value);
     *     public Object get(Object key);
     * }
     */
    class MyJob extends QuartzJobBean {
        @Override
        protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
            String  name = context.getJobDetail().getKey().getName();
            String group = context.getJobDetail().getKey().getGroup();
            log.info("context get with identity name:{}, and group:{}", name, group);
            String  desc = context.getJobDetail().getDescription();
            log.info("context get with description:{}", desc);
            String value = context.getJobDetail().getJobDataMap().getString(JOB_KEY);
            log.info("context get using job data value:{}", value);
            log.info("第四种有状态流弊了：{}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "->" + value);
        }
    }

    /**
     * public class JobBuilder {
     *     //创建用于定义作业详细信息的作业生成器，并设置要执行的作业的类名。
     *     public static JobBuilder newJob(){}
     *     public static JobBuilder newJob(Class<?extends Job> jobClass){}
     *     //使用带有给定名称和默认组的{JobKey}来标识JobDetail。
     *     public JobBuilder withIdentity(String name){}
     *     public JobBuilder withIdentity(String name,String group){}
     *     //设置作业的给定描述。
     *     public JobBuilder withDescription(String jobDescription){}
     *     //将给定的键值对添加到JobDetail的{JobDataMap}集
     *     public JobBuilder usingJobData(String dataKey, String value){}
     *     public JobBuilder usingJobData(String dataKey, Integer value){}
     *     public JobBuilder usingJobData(String dataKey, Long value){}
     *     public JobBuilder usingJobData(String dataKey, Float value){}
     *     public JobBuilder usingJobData(String dataKey, Double value){}
     *     public JobBuilder usingJobData(String dataKey, Boolean value){}
     *     //作业在孤立后是否应保持存储无{Trigger}指向它
     *     public JobBuilder storeDurably(){}
     *     public JobBuilder storeDurably(boolean jobDurability){}
     *     //生成此作业生成器定义的jobDetail实例
     *     public JobDetail build(){}
     * }
     * @return
     */
    @Bean
    public JobDetail exampleJobDetail() {
        return JobBuilder
                .newJob(MyJob.class)
                .withIdentity("exampleDetail", "GUEST")
                .withDescription("exampleDescription")
                .usingJobData(JOB_KEY, JOB_VAL)
                .storeDurably(true)
                .build();
    }

    /**
     * Quartz 4种Scheduler调度器：
     * 1.schedule = CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withIntervalInSeconds(10);
     * 2.schedule = CronScheduleBuilder.cronSchedule("0/10 * * * * ?");
     * 3.schedule = DailyTimeIntervalScheduleBuilder.dailyTimeIntervalSchedule().withIntervalInSeconds(10);
     * 4.schedule = SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10).repeatForever();
     * @return
     */
    @Bean
    public Trigger exampleJobTrigger() {
        ScheduleBuilder schedule = null;
        schedule = CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withIntervalInSeconds(10);
        schedule = CronScheduleBuilder.cronSchedule("0/10 * * * * ?");
        schedule = DailyTimeIntervalScheduleBuilder.dailyTimeIntervalSchedule().withIntervalInSeconds(10);
        schedule = SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10).repeatForever();
        CronScheduleBuilder cronSchedule = CronScheduleBuilder.cronSchedule("0/10 * * * * ?");
        return TriggerBuilder
                .newTrigger()
                .forJob(exampleJobDetail())
                .withIdentity("exampleTrigger", "GUEST")
                .withSchedule(cronSchedule)
                .build();
    }
}
