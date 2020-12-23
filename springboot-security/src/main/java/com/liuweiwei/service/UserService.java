package com.liuweiwei.service;


import com.liuweiwei.model.TbUser;

import java.util.Set;

/**
 * 用户管理
 * @author liuweiwei
 * @since 2020-05-20
 */
public interface UserService {

	/**
	 * 根据用户名查找用户
	 * @param username
	 * @return
	 */
	TbUser findByUsername(String username);

	/**
	 * 查找用户的菜单权限标识集合
	 * @param userName
	 * @return
	 */
	Set<String> findPermissions(String username);

}
