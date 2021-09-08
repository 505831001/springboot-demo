package com.example.service;

import com.example.entity.QrtzJobDetails;

import java.util.List;

/**
 * @author Administrator
 * @since 2021-09-07
 */
public interface QrtzJobDetailsService {
    /**
     * 查询Quartz调度器任务
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<QrtzJobDetails> getQrtzJobDetails(String pageNum, String pageSize);
}
