package org.liuweiwei.service.impl;

import org.liuweiwei.dao.TbUserDao;
import org.liuweiwei.model.TbUser;
import org.liuweiwei.service.TbUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Liuweiwei
 * @since 2021-01-06
 */
@Service
public class TbUserServiceImpl implements TbUserService {

    @Resource
    private TbUserDao userDao;

    @Override
    public TbUser findByUserId(Long userId) {
        TbUser user = userDao.getOne(userId);
        return user;
    }

    @Override
    public List<TbUser> findAll() {
        List<TbUser> list = userDao.findAll();
        return list;
    }
}
