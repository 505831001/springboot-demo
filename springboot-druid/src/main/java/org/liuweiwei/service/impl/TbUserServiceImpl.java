package org.liuweiwei.service.impl;

import org.liuweiwei.dao.TbUserMapper;
import org.liuweiwei.model.TbUser;
import org.liuweiwei.service.TbUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Liuweiwei
 * @since 2021-01-06
 */
@Service
public class TbUserServiceImpl implements TbUserService {

    @Autowired
    private TbUserMapper userMapper;

    @Override
    public TbUser findByUserId(Long userId) {
        TbUser user = userMapper.selectByUserId(userId);
        return user;
    }

    @Override
    public List<TbUser> findAll() {
        List<TbUser> list = userMapper.selectAll();
        return list;
    }
}
