package com.example.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.QrtzJobDetails;

/**
 * 继承自-顶级 Service
 * @author Administrator
 * @since 2021-09-07
 */
public interface QrtzJobDetailsService extends IService<QrtzJobDetails> {
    /**
     * 查询Quartz调度器任务
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<QrtzJobDetails> getQrtzJobDetails(Long pageNum, Long pageSize);
}
