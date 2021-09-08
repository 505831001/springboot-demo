package com.example.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dao.QrtzJobDetailsMapper;
import com.example.model.QrtzJobDetails;
import com.example.service.QrtzJobDetailsService;
import org.springframework.stereotype.Service;

/**
 * 继承自-IService 实现类(泛型<M-是映射接口, T-是实体类>)
 * @author Administrator
 * @since 2021-09-07
 */
@Service
public class QrtzJobDetailsServiceImpl extends ServiceImpl<QrtzJobDetailsMapper, QrtzJobDetails> implements QrtzJobDetailsService {

    @Override
    public Page<QrtzJobDetails> getQrtzJobDetails(Long pageNum, Long pageSize) {
        Page<QrtzJobDetails> page = this.getBaseMapper().selectPage(new Page<>(pageNum, pageSize), null);
        page = this.page(new Page<>(pageNum, pageSize), null);
        return page;
    }
}
