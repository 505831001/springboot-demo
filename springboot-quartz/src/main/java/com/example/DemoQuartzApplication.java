package com.example;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * Quartz 有四个核心概念：
 * 1. Job TODO->任务
 *     1.是一个接口，只定义一个方法 execute(JobExecutionContext context) 指定需要定时执行的任务（Job）。
 *     2.在实现接口的 execute 方法中编写所需要定时执行的任务（Job），JobExecutionContext 类提供了调度应用的一些信息；
 *     3.Job 运行时的信息保存在 JobDataMap 实例中。
 *     class MyTask implements Job {@Override public void execute(JobExecutionContext context) throws JobExecutionException {...}}
 * 2. JobDetail TODO->任务Detail
 *     1.Quartz 每次调度 Job 时，都重新创建一个 Job 实例，因此它不接受一个 Job 的实例，
 *     2.相反它接收一个 Job 实现类 JobDetail，描述 Job 的实现类及其他相关的静态信息，
 *     3.如 Job 名字、描述、关联监听器等信息，以便运行时通过 newInstance() 的反射机制实例化 Job。
 *     jobDetail = JobBuilder.newJob(MyTask.class).build();
 * 3. Rigger TODO->触发器
 *     1.是一个类，描述触发 Job 执行的时间触发规则，主要有 SimpleTrigger 和 CronTrigger 这两个子类。
 *     2.当且仅当需调度一次或者以固定时间间隔周期执行调度，SimpleTrigger 是最适合的选择；
 *     3.而 CronTrigger 则可以通过 Cron 表达式定义出各种复杂时间规则的调度方案：如工作日周一到周五的 15：00 ~ 16：00 执行调度等。
 *     jobTrigger = TriggerBuilder.newTrigger().forJob(jobDetail).withSchedule(schedule).build();
 * 4. Scheduler TODO->调度器
 *     1.调度器就相当于一个容器，装载着任务和触发器，该类是一个接口，代表一个 Quartz 的独立运行容器，
 *     2.Trigger 和 JobDetail 可以注册到 Scheduler 中，
 *     3.两者在 Scheduler 中拥有各自的组及名称，组及名称是 Scheduler 查找定位容器中某一对象的依据，
 *     4.Trigger 的组及名称必须唯一，JobDetail 的组和名称也必须唯一（但可以和 Trigger 的组和名称相同，因为它们是不同类型的）。
 *     5.Scheduler 定义了多个接口方法，允许外部通过组及名称访问和控制容器中 Trigger 和 JobDetail。
 *     Quartz 4种Scheduler调度器：
 *         1.schedule = CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withIntervalInSeconds(10);
 *         2.schedule = CronScheduleBuilder.cronSchedule("0/10 * * * * ?");
 *         3.schedule = DailyTimeIntervalScheduleBuilder.dailyTimeIntervalSchedule().withIntervalInSeconds(10);
 *         4.schedule = SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10).repeatForever();
 *
 * Quartz 参数设置模板：
 * 1. Scheduled 参数。可以接受两种定时的设置：
 *     1.一种是 cron = * / 6 * * * * ?，两种都可表示固定周期执行定时任务。6秒执行一次。
 *     2.一种是 fixedRate = 6000，两种都可表示固定周期执行定时任务。6秒执行一次。
 * 2. cron 参数。指定有七位数，最后一位是年，定时方案只需要设置六位即可：
 *     1.表示秒数，取值是0 ~ 59
 *     2.表示分数，取值是0 ~ 59
 *     3.表示小时，取值是0 ~ 23
 *     4.表示天数，取值是0 ~ 31
 *     5.表示月份，取值是1 ~ 12
 *     6.表示星期，取值是1 ~ 7，星期一，星期二，...，还有 1 表示星期日
 *     7.表示年份，可以留空，取值是1970 ~ 2099
 * 3. 举几个例子熟悉一下：
 *     0 0    3 * * ?   每天 3 点执行；
 *     0 5    3 * * ?   每天 3 点 5 分执行；
 *     0 5    3 ? * *   每天 3 点 5 分执行，与上面作用相同；
 *     0 5/10 3 * * ?   每天 3 点的 5 分、15 分、25 分、35 分、45 分、55分这几个时间点执行；
 *     0 10   3 ? * 1   每周星期天，3 点 10 分执行，注，1 表示星期天；
 *     0 10   3 ? * 1#3 每个月的第三个星期，星期天执行，# 号只能出现在星期的位置。
 *
 * Quartz 调度框架启动打印日志：
 *     Scheduler class: 'org.quartz.core.QuartzScheduler' - running locally.
 *     NOT STARTED.
 *     Currently in standby mode.
 *     Number of jobs executed: 0
 *     Using thread pool 'org.quartz.simpl.SimpleThreadPool' - with 10 threads.
 *     Using job-store 'org.quartz.simpl.RAMJobStore' - which does not support persistence. and is not clustered.
 *
 * Quartz 数据持久化：
 *     spring.quartz.job-store-type=memory
 *     spring.quartz.job-store-type=jdbc
 *
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
 * 2.作业生成器用于实例化{JobDetail}：org.quartz.JobBuilder
 * public class JobBuilder {
 *     public static JobBuilder newJob(){}
 *     public static JobBuilder newJob(Class<?extends Job> jobClass){}
 *     public JobBuilder withIdentity(String name){}
 *     public JobBuilder withIdentity(String name,String group){}
 *     public JobBuilder withIdentity(JobKey jobKey){}
 *     public JobBuilder withDescription(String jobDescription){}
 *     public JobBuilder ofType(Class<?extends Job> jobClazz){}
 *     public JobBuilder requestRecovery(){}
 *     public JobBuilder requestRecovery(boolean jobShouldRecover){}
 *     public JobBuilder storeDurably(){}
 *     public JobBuilder storeDurably(boolean jobDurability){}
 *     public JobBuilder usingJobData(String dataKey,String value){}
 *     public JobBuilder usingJobData(String dataKey,Integer value){}
 *     public JobBuilder usingJobData(String dataKey,Long value){}
 *     public JobBuilder usingJobData(String dataKey,Float value){}
 *     public JobBuilder usingJobData(String dataKey,Double value){}
 *     public JobBuilder usingJobData(String dataKey,Boolean value){}
 *     public JobBuilder usingJobData(JobDataMap newJobDataMap){}
 *     public JobBuilder setJobData(JobDataMap newJobDataMap){}
 *     public JobDetail build(){}
 * }
 * 3.传达给定作业实例的详细属性。作业详细信息将使用{JobBuilder}创建/定义：org.quartz.JobDetail
 * public interface JobDetail extends Serializable, Cloneable {
 *     public Object clone();
 *     public String getDescription();
 *     public boolean isDurable();
 *     public boolean isPersistJobDataAfterExecution();
 *     public boolean isConcurrentExactionDisallowed();
 *     public boolean requestsRecovery();
 *     public JobBuilder getJobBuilder();
 *     public JobDataMap getJobDataMap();
 *     public Class<?extends Job> getJobClass();
 * }
 * 4.触发器生成器用于实例化{Trigger}：org.quartz.TriggerBuilder
 * public class TriggerBuilder<T extends Trigger> {
 *     private TriggerKey key;
 *     private String description;
 *     private Date startTime = new Date();
 *     private Date endTime;
 *     private int priority = Trigger.DEFAULT_PRIORITY;
 *     private String calendarName;
 *     private JobKey jobKey;
 *     private JobDataMap jobDataMap = new JobDataMap();
 *     private ScheduleBuilder<?> scheduleBuilder = null;
 *     private TriggerBuilder() {}
 *     public static TriggerBuilder<Trigger> newTrigger(){}
 *     public T build(){}
 *     public TriggerBuilder<T> withIdentity(String name){}
 *     public TriggerBuilder<T> withIdentity(String name,String group){}
 *     public TriggerBuilder<T> withIdentity(TriggerKey triggerKey){}
 *     public TriggerBuilder<T> withDescription(String triggerDescription){}
 *     public TriggerBuilder<T> withPriority(int triggerPriority){}
 *     public TriggerBuilder<T> modifiedByCalendar(String calName){}
 *     public TriggerBuilder<T> startAt(Date triggerStartTime){}
 *     public TriggerBuilder<T> startNow(){}
 *     public TriggerBuilder<T> endAt(Date triggerEndTime){}
 *     public<SBT extends T> TriggerBuilder<SBT> withSchedule(ScheduleBuilder<SBT> schedBuilder){}
 *     public TriggerBuilder<T> forJob(JobKey keyOfJobToFire){}
 *     public TriggerBuilder<T> forJob(String jobName){}
 *     public TriggerBuilder<T> forJob(String jobName,String jobGroup){}
 *     public TriggerBuilder<T> forJob(JobDetail jobDetail){}
 *     public TriggerBuilder<T> usingJobData(String dataKey,String value){}
 *     public TriggerBuilder<T> usingJobData(String dataKey,Integer value){}
 *     public TriggerBuilder<T> usingJobData(String dataKey,Long value){}
 *     public TriggerBuilder<T> usingJobData(String dataKey,Float value){}
 *     public TriggerBuilder<T> usingJobData(String dataKey,Double value){}
 *     public TriggerBuilder<T> usingJobData(String dataKey,Boolean value){}
 *     public TriggerBuilder<T> usingJobData(JobDataMap newJobDataMap){}
 * }
 * 5.调度器生成器：org.quartz.ScheduleBuilder
 * 5.1.public class CalendarIntervalScheduleBuilder extends ScheduleBuilder<CalendarIntervalTrigger> {}
 * 5.2.public class CronScheduleBuilder extends ScheduleBuilder<CronTrigger> {}
 * 5.3.public class DailyTimeIntervalScheduleBuilder extends ScheduleBuilder<DailyTimeIntervalTrigger> {}
 * 5.4.public class SimpleScheduleBuilder extends ScheduleBuilder<SimpleTrigger> {}
 * 6.作业监听器作业日志必备神器：org.quartz.JobListener
 * public interface JobListener {
 *    String getName();
 *    void jobToBeExecuted(JobExecutionContext context);
 *    void jobExecutionVetoed(JobExecutionContext context);
 *    void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException);
 * }
 * @author Liuweiwei
 * @since 2021-01-10
 */
@SpringBootApplication
@Log4j2
public class DemoQuartzApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DemoQuartzApplication.class, args);
        ConfigurableEnvironment environment = context.getEnvironment();
        log.info(environment.getProperty("java.vendor.url"));
        log.info(environment.getProperty("java.vendor.url.bug"));
        log.info(environment.getProperty("sun.java.command") + " started successfully.");
    }
}
