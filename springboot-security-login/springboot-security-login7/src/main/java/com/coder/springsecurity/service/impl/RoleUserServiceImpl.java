package com.coder.springsecurity.service.impl;

import com.coder.springsecurity.service.RoleUserService;
import com.coder.springsecurity.utils.Result;
import com.coder.springsecurity.dao.RoleUserDao;
import com.coder.springsecurity.eneity.TbRoleUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LiuWeiWei
 * @since 2022-01-06
 */
@Service
public class RoleUserServiceImpl implements RoleUserService {
    @Autowired
    private RoleUserDao roleUserDao;
    @Override
    public Result getTbRoleUserByUserId(Integer userId) {
        List <TbRoleUser> tbRoleUser = roleUserDao.getTbRoleUserByUserId(userId);
        if(tbRoleUser != null){
            return Result.ok().data(tbRoleUser);
        }else{
            return Result.error();
        }
    }
}
