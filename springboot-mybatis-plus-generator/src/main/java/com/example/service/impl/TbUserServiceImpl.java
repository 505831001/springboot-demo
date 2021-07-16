package com.example.service.impl;

import com.example.entity.TbUser;
import com.example.mapper.TbUserMapper;
import com.example.service.TbUserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author liuweiwei
 * @since 2021-07-16
 */
@Service
public class TbUserServiceImpl extends ServiceImpl<TbUserMapper, TbUser> implements TbUserService {

}
