package org.liuweiwei.service;

import org.liuweiwei.model.TbUser;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author Liuweiwei
 * @since 2021-01-06
 */
public interface TbUserService {

    /**
     * 根据用户ID查询用户信息
     *
     * @param tbUser
     * @return
     */
    TbUser queryOne(TbUser tbUser);

    /**
     * 查询用户列表
     *
     * @return
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    List<TbUser> findAll() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException;
}
