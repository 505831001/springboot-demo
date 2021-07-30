package com.excel.poi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.excel.poi.dao.TbUserMapper;
import com.excel.poi.entity.TbUser;
import com.excel.poi.service.TbUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Liuweiwei
 * @since 2021-07-04
 */
@Service
public class TbUserServiceImpl extends ServiceImpl<TbUserMapper, TbUser> implements TbUserService {

    @Resource
    private TbUserMapper tbUserMapper;

}
