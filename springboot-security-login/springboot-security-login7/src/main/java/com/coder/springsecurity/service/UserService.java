package com.coder.springsecurity.service;

import com.coder.springsecurity.eneity.TbUser;
import com.coder.springsecurity.utils.Result;

/**
 * @author LiuWeiWei
 * @since 2022-01-06
 */
public interface UserService {
    TbUser getUser(String userName);

    Result<TbUser> getUserByFuzzyUsername(String username, Integer offset, Integer limit);

    Result<TbUser> getAllUsersByPage(Integer startPosition, Integer limit);

    Result<TbUser> save(TbUser user, Integer roleId);

    Result<TbUser> updateUser(TbUser tbUser, Integer roleId);

    TbUser getUserById(Long id);

    TbUser getUserByPhone(String phone);

    int deleteUser(Long id);
}
