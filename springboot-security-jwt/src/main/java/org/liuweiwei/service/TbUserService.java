package org.liuweiwei.service;

import org.liuweiwei.model.TbUser;

import java.util.Set;

/**
 * 用户管理
 *
 * @author Liuweiwei
 * @since 2020-12-31
 */
public interface TbUserService {

    /**
     * 根据用户名查找用户
     *
     * @param username
     * @return
     */
    TbUser findByUsername(String username);

    /**
     * 查找用户的菜单权限标识集合
     *
     * @param username
     * @return
     */
    Set<String> findPermissions(String username);
}
