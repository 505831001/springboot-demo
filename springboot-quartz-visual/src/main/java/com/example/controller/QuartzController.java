package com.example.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.model.QrtzJobDetails;
import com.example.service.QrtzJobDetailsService;
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
     */
    @GetMapping(value = "/create")
    public String create() {
        JobDetail jobDetail = JobBuilder.newJob(new Job() {
            @Override
            public void execute(JobExecutionContext context) throws JobExecutionException {
                String message = context.getJobDetail().getJobDataMap().getString("liu");
                log.info("第二种有状态流弊了：{}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "->" + message);
            }
        }.getClass()).build();
        Trigger jobTrigger = TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.cronSchedule("0/10 * * * * ?")).build();
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            //scheduleJob(JobDetail jobDetail, Trigger trigger)
            //将给定的任务详情{JobDetail}添加到调度程序，并将给定的触发器{Trigger}与其关联。
            Date date = scheduler.scheduleJob(jobDetail, jobTrigger);
            //org.quartz.SchedulerException: The Scheduler has been shutdown.
            if (scheduler.isShutdown() == true || scheduler.isStarted() == false || scheduler.isInStandbyMode() == true) {
                scheduler.start();
            }
        } catch (SchedulerException ex) {
            log.error("Quartz add task failed: {}", ex.getMessage());
            return "Failed";
        }
        return "Success";
    }

    @GetMapping(value = "/add")
    public String add() {
        JobDetail jobDetail = JobBuilder.newJob(new Job() {
            @Override
            public void execute(JobExecutionContext context) throws JobExecutionException {
                String[] keys = context.getJobDetail().getJobDataMap().getKeys();
                for (String key : keys) {
                    log.info("" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), "->" + key);
                }
            }
        }.getClass()).build();
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            //addJob(JobDetail jobDetail, boolean replace)
            //将给定的作业添加到计划程序-没有关联的触发器。作业将处于"休眠"状态，直到使用触发器或调度程序对其进行调度。或者为它调用Scheduler.triggerJob()。
            scheduler.addJob(jobDetail, true);
        } catch (SchedulerException ex) {
            log.error("Quartz add task failed: {}", ex.getMessage());
            return "Failed";
        }
        return "Success";
    }

    /**
     * 更新Quartz调度器任务，指定任务
     * http://localhost:8518/quartz/update?jobName=6da64b5bd2ee-5cb7afbe-b9c5-4709-9c5f-3b83d9dee3a7&jobGroup=DEFAULT
     */
    @GetMapping(value = "/update")
    public String update(String jobName, String jobGroup) {
        try {
            Scheduler   scheduler = schedulerFactoryBean.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
            Trigger    jobTrigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(20).repeatForever()).build();
            //rescheduleJob(TriggerKey triggerKey, Trigger newTrigger)
            //用给定的键移除{Trigger}，并存储新的给定触发器-必须与同一个作业关联(新触发器必须指定作业名称和组)-但是，新触发器不必与旧触发器同名。
            Date date = scheduler.rescheduleJob(triggerKey, jobTrigger);
            log.info("Scheduler reschedule job -> jobName:{}, jobGroup:{}", jobName, jobGroup);
        } catch (SchedulerException ex) {
            ex.printStackTrace();
            return "Failed";
        }
        return "Success";
    }

    /**
     * 暂停Quartz调度器任务，指定任务
     * http://localhost:8518/quartz/resume?jobName=6da64b5bd2ee-23685c67-68f6-4763-a268-bd6f36921f27&jobGroup=DEFAULT
     */
    @GetMapping(value = "/pause")
    public String pause(String jobName, String jobGroup) {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            //pauseJob(JobKey jobKey)
            //用给定的键暂停{JobDetail}-暂停所有当前的触发器。
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
     * http://localhost:8518/quartz/resume?jobName=6da64b5bd2ee-23685c67-68f6-4763-a268-bd6f36921f27&jobGroup=DEFAULT
     */
    @GetMapping(value = "/resume")
    public String resume(String jobName, String jobGroup) {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            //resumeJob(JobKey jobKey)
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
     * 删除Quartz调度器任务，指定任务
     * http://localhost:8518/quartz/delete?jobName=6da64b5bd2ee-23685c67-68f6-4763-a268-bd6f36921f27&jobGroup=DEFAULT
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
            //unscheduledJob(TriggerKey triggerKey)
            scheduler.unscheduleJob(TriggerKey.triggerKey(jobName, jobGroup));
            //deleteJob(JobKey jobKey)
            boolean flag = scheduler.deleteJob(JobKey.jobKey(jobName, jobGroup));
            log.info("Scheduler delete job -> jobName:{}, jobGroup:{}", jobName, jobGroup);
        } catch (SchedulerException ex) {
            ex.printStackTrace();
            return "Failed";
        }
        return "Okay";
    }

    @GetMapping(value = "/interrupt")
    public String interrupt(@RequestParam String jobName, @RequestParam String jobGroup) {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            //interrupt(JobKey jobKey)
            //在此调度程序实例中请求中断，在已识别的作业的所有当前执行实例中，它必须是InterruptableJob接口的实现者。
            boolean flag = scheduler.interrupt(JobKey.jobKey(jobName, jobGroup));
        } catch (UnableToInterruptJobException ex) {
            ex.printStackTrace();
            return "Failed";
        }
        return "Okay";
    }

    @GetMapping(value = "/checkExists")
    public String checkExists(@RequestParam String jobName, @RequestParam String jobGroup) {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            //checkExists(JobKey jobKey)
            //确定计划程序中是否已存在具有给定标识符的{Job}。
            boolean flag = scheduler.checkExists(JobKey.jobKey(jobName, jobGroup));
        } catch (SchedulerException ex) {
            ex.printStackTrace();
            return "Failed";
        }
        return "Okay";
    }

    /**
     * 停止Quartz调度器任务，未指定任务
     * http://localhost:8518/quartz/stop
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

    @Resource
    private QrtzJobDetailsService qrtzJobDetailsService;

    /**
     * 查询Quartz调度器任务，分页列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/query")
    public Page<QrtzJobDetails> query(@RequestParam(value = "pageNum") Long pageNum, @RequestParam(value = "pageSize") Long pageSize) {
        return qrtzJobDetailsService.getQrtzJobDetails(pageNum, pageSize);
    }
}
