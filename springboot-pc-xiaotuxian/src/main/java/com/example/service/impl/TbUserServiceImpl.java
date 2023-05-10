package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dao.TbUserMapper;
import com.example.model.TbUser;
import com.example.service.TbUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LiuWeiWei
 * @since 2023-05-10
 */
@Service
public class TbUserServiceImpl extends ServiceImpl<TbUserMapper, TbUser> implements TbUserService {

    @Autowired
    private TbUserMapper tbUserMapper;

    @Override
    public List<TbUser> selectByPage(String current, String size) {
        return tbUserMapper.selectByPage(Long.valueOf(current), Long.valueOf(size));
    }
}
