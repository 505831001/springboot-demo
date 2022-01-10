package com.coder.springsecurity.service;

import com.coder.springsecurity.utils.Result;

/**
 * @author LiuWeiWei
 * @since 2022-01-06
 */
public interface RoleUserService {
    Result getTbRoleUserByUserId(Integer userId);
}
