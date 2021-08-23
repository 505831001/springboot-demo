package com.example.service;

import com.example.model.TbUser;

import java.io.Serializable;
import java.util.List;

/**
 * @author Liuweiwei
 * @since 2021-01-06
 */
public interface TbUserService {
    /**
     * 根据用户ID查询用户信息
     *
     * @param id
     * @return
     */
    TbUser otherById(Serializable id);

    /**
     * 根据用户字段查询用户信息
     *
     * @param tbUser
     * @return
     */
    TbUser otherOne(TbUser tbUser);

    /**
     * 查询用户列表
     *
     * @return
     */
    List<TbUser> otherList();
}
