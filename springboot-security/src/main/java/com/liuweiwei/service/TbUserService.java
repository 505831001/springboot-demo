package com.liuweiwei.service;

import com.liuweiwei.model.TbUser;

import java.util.Set;

/**
 * 用户管理
 *
 * @author liuweiwei
 * @since 2020-05-20
 */
public interface TbUserService {
    /**
     * 根据用户名查找用户
     *
     * @param userName
     * @return
     */
    TbUser findUserByName(String userName);

    /**
     * 查找用户的菜单权限标识集合
     *
     * @param userName
     * @return
     */
    Set<String> getPermissions(String userName);

    /**
     * 根据用户名称查询数据库用户密码
     *
     * @param username
     * @return md5Password
     */
    String findPasswordByName(String username);
}
