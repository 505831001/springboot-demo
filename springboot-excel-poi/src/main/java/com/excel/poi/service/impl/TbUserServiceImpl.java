package com.excel.poi.service.impl;

import com.excel.poi.dao.TbUserMapper;
import com.excel.poi.service.TbUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Liuweiwei
 * @since 2021-07-04
 */
@Service
public class TbUserServiceImpl implements TbUserService {

    @Resource
    private TbUserMapper tbUserMapper;
}
