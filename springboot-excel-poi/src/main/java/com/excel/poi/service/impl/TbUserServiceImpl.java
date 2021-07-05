package com.excel.poi.service.impl;

import com.excel.poi.dao.TbUserMapper;
import com.excel.poi.service.TbUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Liuweiwei
 * @since 2021-07-04
 */
@Service
public class TbUserServiceImpl implements TbUserService {

    @Autowired
    private TbUserMapper tbUserMapper;


}
