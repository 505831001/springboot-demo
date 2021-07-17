package com.liuweiwei.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.liuweiwei.model.TbUser;
import com.liuweiwei.utils.PageRequest;
import com.liuweiwei.utils.PageResult;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author Liuweiwei
 * @since 2021-01-06
 */
public interface TbUserService extends IService<TbUser> {

    /**
     * 根据用户ID查询用户信息
     *
     * @param id
     * @return
     */
    public TbUser otherById(Serializable id);

    /**
     * 查询用户
     *
     * @param tbUser
     * @return
     */
    public TbUser otherOne(TbUser tbUser);

    /**
     * 查询用户统计
     *
     * @return
     */
    public Integer otherCount();

    /**
     * 查询用户列表
     *
     * @return
     */
    List<TbUser> otherList() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException;

    /**
     * 根据用户主键查询用户详情
     *
     * @param id
     * @return
     */
    TbUser otherDetails(Serializable id);

    /**
     * 分页查询用户
     *
     * @return
     */
    PageResult otherPage(PageRequest pageRequest);

    /**
     * 根据用户ID更新用户信息
     *
     * @param tbUser
     * @return
     */
    Integer otherUpdate(TbUser tbUser);
}
