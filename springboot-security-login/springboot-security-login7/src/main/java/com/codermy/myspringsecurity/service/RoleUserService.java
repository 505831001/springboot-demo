package com.codermy.myspringsecurity.service;

import com.codermy.myspringsecurity.utils.Result;

/**
 * @author LiuWeiWei
 * @since 2022-01-06
 */
public interface RoleUserService {
    Result getTbRoleUserByUserId(Integer userId);
}
