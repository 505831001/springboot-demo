package com.example.service;

import com.example.model.TbUser;

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
    TbUser findByUserId(Long userId);

    /**
     * 查询用户列表
     *
     * @return
     */
    List<TbUser> findAll();
}
