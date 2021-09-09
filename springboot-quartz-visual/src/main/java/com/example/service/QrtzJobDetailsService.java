package com.example.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.QrtzJobDetails;
import com.example.utils.ResultData;

/**
 * 继承自-顶级 Service
 * @author Liuweiwei
 * @since 2021-09-07
 */
public interface QrtzJobDetailsService extends IService<QrtzJobDetails> {

    /**
     * 创建Quartz调度器任务，启动Quartz调度器任务
     * http://localhost:8518/quartz/create
     * @param dto
     * @return com.example.utils.ResultData
     */
    ResultData scheduleJob(QrtzJobDetails dto);

    /**
     * 添加Quartz调度器任务，作业将处于"dormant"状态，直到使用触发器调度该作业，或者为它调用
     * http://localhost:8518/quartz/add
     * @param dto
     * @return com.example.utils.ResultData
     */
    ResultData addJob(QrtzJobDetails dto);

    /**
     * 更新Quartz调度器任务，指定任务
     * http://localhost:8518/quartz/update?jobName=6da64b5bd2ee-5cb7afbe-b9c5-4709-9c5f-3b83d9dee3a7&jobGroup=DEFAULT
     * @param dto
     * @return com.example.utils.ResultData
     */
    ResultData rescheduleJob(QrtzJobDetails dto);

    /**
     * 暂停Quartz调度器任务，指定任务
     * http://localhost:8518/quartz/resume?jobName=6da64b5bd2ee-23685c67-68f6-4763-a268-bd6f36921f27&jobGroup=DEFAULT
     * @param jobName
     * @param jobGroup
     * @return com.example.utils.ResultData
     */
    ResultData pauseJob(String jobName, String jobGroup);

    /**
     * 恢复Quartz调度器任务，指定任务
     * http://localhost:8518/quartz/resume?jobName=6da64b5bd2ee-23685c67-68f6-4763-a268-bd6f36921f27&jobGroup=DEFAULT
     * @param jobName
     * @param jobGroup
     * @return com.example.utils.ResultData
     */
    ResultData resumeJob(String jobName, String jobGroup);

    /**
     * 删除Quartz调度器任务，指定任务
     * http://localhost:8518/quartz/delete?jobName=6da64b5bd2ee-23685c67-68f6-4763-a268-bd6f36921f27&jobGroup=DEFAULT
     * @param jobName
     * @param jobGroup
     * @return com.example.utils.ResultData
     */
    ResultData deleteJob(String jobName, String jobGroup);

    /**
     * 中断Quartz调度器任务，指定任务
     * http://localhost:8518/quartz/interrupt?jobName=6da64b5bd2ee-23685c67-68f6-4763-a268-bd6f36921f27&jobGroup=DEFAULT
     * @param jobName
     * @param jobGroup
     * @return com.example.utils.ResultData
     */
    ResultData interrupt(String jobName, String jobGroup);

    /**
     * 校验Quartz调度器任务是否已存在，指定任务
     * http://localhost:8518/quartz/checkExists?jobName=6da64b5bd2ee-23685c67-68f6-4763-a268-bd6f36921f27&jobGroup=DEFAULT
     * @param jobName
     * @param jobGroup
     * @return com.example.utils.ResultData
     */
    ResultData checkExists(String jobName, String jobGroup);

    /**
     * 停止Quartz调度器任务，未指定任务
     * http://localhost:8518/quartz/stop
     * 1.shutdown(true)  表示等待所有正在执行的任务执行完毕后关闭Scheduler
     * 2.shutdown(false) 表示直接关闭Scheduler
     * @return com.example.utils.ResultData
     */
    ResultData shutdown();

    // ---------- 华丽的分割线 ----------

    /**
     * 查询Quartz调度器任务，分页列表
     * @param pageNum
     * @param pageSize
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page
     */
    Page<QrtzJobDetails> getQrtzJobDetails(Long pageNum, Long pageSize);
}
