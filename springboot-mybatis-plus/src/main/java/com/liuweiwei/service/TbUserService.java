package com.liuweiwei.service;


import com.liuweiwei.model.TbUser;

import java.util.List;

/**
 * @author Liuweiwei
 * @since 2021-01-06
 */
public interface TbUserService {

    /**
     * 根据用户ID查询用户信息
     *
     * @param userId
     * @return
     */
    public TbUser findByUserId(Long userId);

    /**
     * 查询用户
     *
     * @return
     */
    public TbUser findOne();

    /**
     * 查询用户统计
     *
     * @return
     */
    public Integer findCount();

    /**
     * 查询用户列表
     *
     * @return
     */
    public List<TbUser> findAll();
}
