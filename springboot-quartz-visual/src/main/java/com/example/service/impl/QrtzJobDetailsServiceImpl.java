package com.example.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dao.QrtzJobDetailsMapper;
import com.example.model.QrtzJobDetails;
import com.example.service.QrtzJobDetailsService;
import com.example.utils.ResultData;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.time.DateUtils;
import org.quartz.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;

/**
 * 继承自-IService 实现类(泛型<M-是映射接口, T-是实体类>)
 * @author Liuweiwei
 * @since 2021-09-07
 */
@Service
@Log4j2
public class QrtzJobDetailsServiceImpl extends ServiceImpl<QrtzJobDetailsMapper, QrtzJobDetails> implements QrtzJobDetailsService {

    @Resource
    private Scheduler scheduler;

    /**
     * scheduleJob(JobDetail jobDetail, Trigger trigger) 将给定的任务详情{JobDetail}添加到调度程序，并将给定的触发器{Trigger}与其关联。
     * @param dto
     * @return com.example.utils.ResultData
     */
    @Override
    public ResultData scheduleJob(QrtzJobDetails dto) {
        //TODO->qrtz_job_details表写入数据(作业有了)
        JobDetail jobDetail = JobBuilder
                .newJob(new Job() {@Override public void execute(JobExecutionContext context) throws JobExecutionException { }}.getClass())
                .withIdentity(dto.getJobName(), dto.getJobGroup())
                .withDescription(Scheduler.DEFAULT_GROUP)
                .storeDurably()
                .build();
        //TODO->qrtz_triggers表写入数据(触发器有了)
        Trigger jobTrigger = TriggerBuilder
                .newTrigger()
                .withIdentity("08613713731139")
                .withDescription(Scheduler.DEFAULT_GROUP)
                .startAt(new Date())
                .endAt(DateUtils.addDays(new Date(), 1))
                .withSchedule(CronScheduleBuilder.cronSchedule("0/10 * * * * ?"))
                .build();
        try {
            scheduler.scheduleJob(jobDetail, jobTrigger);
            log.info("Scheduler schedule job success: {}", HttpStatus.OK);
            //org.quartz.SchedulerException: The Scheduler has been shutdown.
            if (scheduler.isShutdown() == true || scheduler.isStarted() == false || scheduler.isInStandbyMode() == true) {
                scheduler.start();
            }
        } catch (SchedulerException ex) {
            log.info("Scheduler schedule job failed: {}", ex.getMessage());
            return ResultData.failure("Failed");
        }
        return ResultData.success("Success");
    }

    /**
     * addJob(JobDetail jobDetail, boolean replace)
     * 1.将给定的作业添加到计划程序-没有关联的触发器。作业将处于"dormant"状态，直到使用触发器调度该作业，或者为它调用Scheduler.triggerJob()
     * 2.根据定义，作业必须是"durable"，否则将引发ScheduleException：Jobs added with no trigger must be durable
     * 3.如果存在内部计划程序错误，或者如果工作不持久，或者已经存在同名的作业，那么replace=false
     * @param dto
     * @return
     */
    @Override
    public ResultData addJob(QrtzJobDetails dto) {
        JobDetail jobDetail = JobBuilder
                .newJob(new Job() {@Override public void execute(JobExecutionContext context) throws JobExecutionException { }}.getClass())
                .withIdentity(dto.getJobName(), dto.getJobGroup())
                .withDescription(Scheduler.DEFAULT_GROUP)
                //org.quartz.core.QuartzScheduler.SchedulerException: Jobs added with no trigger must be durable.
                .storeDurably()
                .build();
        try {
            //TODO->qrtz_job_details表写入数据(作业有了)
            scheduler.addJob(jobDetail, true);
            //TODO->qrtz_triggers表未写入数据(未触发)
            scheduler.triggerJob(JobKey.jobKey(dto.getJobName(), dto.getJobGroup()));
            log.info("Scheduler add job success. jobName:{}, jobGroup:{}", dto.getJobName(), dto.getJobGroup());
        } catch (SchedulerException ex) {
            log.info("Scheduler add job failed: {}", ex.getMessage());
            return ResultData.failure("Failed");
        }
        return ResultData.success("Success");
    }

    /**
     * rescheduleJob(TriggerKey triggerKey, Trigger newTrigger) 用给定的键移除{Trigger}，并存储新的给定触发器-必须与同一个作业关联(新触发器必须指定作业名称和组)-但是，新触发器不必与旧触发器同名。
     * @param dto
     * @return com.example.utils.ResultData
     */
    @Override
    public ResultData rescheduleJob(QrtzJobDetails dto) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(dto.getJobName(), dto.getJobGroup());
            Trigger    jobTrigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(20).repeatForever()).build();
            scheduler.rescheduleJob(triggerKey, jobTrigger);
            log.info("Scheduler reschedule job success. jobName:{}, jobGroup:{}", dto.getJobName(), dto.getJobGroup());
        } catch (SchedulerException ex) {
            log.info("Scheduler reschedule job failed: {}", ex.getMessage());
            return ResultData.failure("Failed");
        }
        return ResultData.success("Success");
    }

    /**
     * pauseJob(JobKey jobKey) 用给定的键暂停{JobDetail}-暂停所有当前的触发器。
     * @param jobName
     * @param jobGroup
     * @return com.example.utils.ResultData
     */
    @Override
    public ResultData pauseJob(String jobName, String jobGroup) {
        try {
            scheduler.pauseJob(JobKey.jobKey(jobName, jobGroup));
            log.info("Scheduler pause job -> jobName:{}, jobGroup:{}", jobName, jobGroup);
        } catch (SchedulerException ex) {
            log.info("Scheduler pause job failed: {}", ex.getMessage());
            return ResultData.failure("Failed");
        }
        return ResultData.success("Success");
    }

    /**
     * resumeJob(JobKey jobKey) 使用给定的键恢复，取消暂停{JobDetail}。
     * @param jobName
     * @param jobGroup
     * @return com.example.utils.ResultData
     */
    @Override
    public ResultData resumeJob(String jobName, String jobGroup) {
        try {
            scheduler.resumeJob(JobKey.jobKey(jobName, jobGroup));
            log.info("Scheduler resume job success. jobName:{}, jobGroup:{}", jobName, jobGroup);
        } catch (SchedulerException ex) {
            log.info("Scheduler resume job failed: {}", ex.getMessage());
            return ResultData.failure("Failed");
        }
        return ResultData.success("Success");
    }

    /**
     * 1.pauseTrigger(TriggerKey triggerKey)   用给定的键暂停触发器。
     * 2.unscheduledJob(TriggerKey triggerKey) 从调度程序中删除指示的触发器。
     * 3.deleteJob(JobKey jobKey)              从计划程序中删除已识别的作业，以及任何相关的触发器。
     * @param jobName
     * @param jobGroup
     * @return com.example.utils.ResultData
     */
    @Override
    public ResultData deleteJob(String jobName, String jobGroup) {
        try {
            //org.quartz.SchedulerException: The Scheduler has been shutdown.
            if (scheduler.isShutdown() == true) {
                scheduler.start();
            }
            scheduler.pauseTrigger(TriggerKey.triggerKey(jobName, jobGroup));
            scheduler.unscheduleJob(TriggerKey.triggerKey(jobName, jobGroup));
            scheduler.deleteJob(JobKey.jobKey(jobName, jobGroup));
            log.info("Scheduler delete job success. jobName:{}, jobGroup:{}", jobName, jobGroup);
        } catch (SchedulerException ex) {
            log.info("Scheduler delete job failed: {}", ex.getMessage());
            return ResultData.failure("Failed");
        }
        return ResultData.success("Success");
    }

    /**
     * interrupt(JobKey jobKey) 在此调度程序实例中请求中断，在已识别的作业的所有当前执行实例中，它必须是InterruptableJob接口的实现者。
     * @param jobName
     * @param jobGroup
     * @return com.example.utils.ResultData
     */
    @Override
    public ResultData interrupt(String jobName, String jobGroup) {
        try {
            scheduler.interrupt(JobKey.jobKey(jobName, jobGroup));
            log.info("Scheduler interrupt job success. jobName:{}, jobGroup:{}", jobName, jobGroup);
        } catch (UnableToInterruptJobException ex) {
            log.info("Scheduler interrupt job failed: {}", ex.getMessage());
            return ResultData.failure("Failed");
        }
        return ResultData.success("Success");
    }

    /**
     * checkExists(JobKey jobKey) 确定计划程序中是否已存在具有给定标识符的工作。
     * @param jobName
     * @param jobGroup
     * @return com.example.utils.ResultData
     */
    @Override
    public ResultData checkExists(String jobName, String jobGroup) {
        try {
            scheduler.checkExists(JobKey.jobKey(jobName, jobGroup));
            log.info("Scheduler check exists job success. jobName:{}, jobGroup:{}", jobName, jobGroup);
        } catch (SchedulerException ex) {
            log.info("Scheduler check exists job failed: {}", ex.getMessage());
            return ResultData.failure("Failed");
        }
        return ResultData.success("Success");
    }

    /**
     * scheduler.shutdown() 停止计划程序对{Trigger}的触发，并清除与调度程序关联的所有资源。相当于关闭(错误)。
     * 1.shutdown(true)  表示等待所有正在执行的任务执行完毕后关闭Scheduler
     * 2.shutdown(false) 表示直接关闭Scheduler
     * @return com.example.utils.ResultData
     */
    @Override
    public ResultData shutdown() {
        try {
            scheduler.shutdown();
            log.info("Scheduler shutdown job success: {}", HttpStatus.OK);
        } catch (SchedulerException ex) {
            log.info("Scheduler shutdown job failed: {}", ex.getMessage());
            return ResultData.failure("Failed");
        }
        return ResultData.success("Success");
    }

    // ---------- 华丽的分割线 ----------

    /**
     * 查询Quartz调度器任务，分页列表
     * @param pageNum
     * @param pageSize
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page
     */
    @Override
    public Page<QrtzJobDetails> getQrtzJobDetails(Long pageNum, Long pageSize) {
        Page<QrtzJobDetails> page = this.getBaseMapper().selectPage(new Page<>(pageNum, pageSize), null);
        page = this.page(new Page<>(pageNum, pageSize), null);
        return page;
    }

    /**
     * 日期：加一天
     * @return java.util.Date
     */
    private Date buildDatePlus() {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 1);
        Date date = calendar.getTime();
        return date;
    }
}
