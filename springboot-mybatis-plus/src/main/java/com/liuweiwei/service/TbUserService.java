package com.liuweiwei.service;


import com.liuweiwei.model.TbUser;
import com.liuweiwei.utils.PageRequest;
import com.liuweiwei.utils.PageResult;

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

    /**
     * 分页查询用户
     *
     * @return
     */
    PageResult findPage(PageRequest pageRequest);

    /**
     * 根据用户ID更新用户信息
     *
     * @param tbUser
     * @return
     */
    Integer update(TbUser tbUser);
}
