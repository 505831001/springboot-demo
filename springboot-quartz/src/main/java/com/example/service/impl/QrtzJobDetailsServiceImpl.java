package com.example.service.impl;

import com.example.entity.QrtzJobDetails;
import com.example.service.QrtzJobDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Administrator
 * @since 2021-09-07
 */
@Service
public class QrtzJobDetailsServiceImpl implements QrtzJobDetailsService {

    @Override
    public List<QrtzJobDetails> getQrtzJobDetails(String pageNum, String pageSize) {
        return null;
    }
}
