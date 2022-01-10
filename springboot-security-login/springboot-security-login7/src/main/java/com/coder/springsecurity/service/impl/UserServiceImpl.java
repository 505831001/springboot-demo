package com.coder.springsecurity.service.impl;

import com.coder.springsecurity.utils.Result;
import com.coder.springsecurity.dao.RoleUserDao;
import com.coder.springsecurity.dao.UserDao;
import com.coder.springsecurity.eneity.TbRoleUser;
import com.coder.springsecurity.eneity.TbUser;
import com.coder.springsecurity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author LiuWeiWei
 * @since 2022-01-06
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleUserDao roleUserDao;

    @Override
    public TbUser getUser(String userName) {
        return userDao.getUser(userName);
    }

    @Override
    public Result<TbUser> getUserByFuzzyUsername(String username, Integer offset, Integer limit) {
        return Result.ok().count(userDao.getUserByFuzzyUsername(username).intValue()).data(userDao.getUserByFuzzyUsernameByPage(username,offset,limit));
    }

    @Override
    public Result<TbUser> getAllUsersByPage(Integer startPosition, Integer limit) {

        return Result.ok().count(userDao.countAllUser().intValue()).data(userDao.getAllUserByPage(startPosition,limit));
    }

    @Override
    public Result<TbUser> save(TbUser user, Integer roleId) {
        if(roleId!= null){
            userDao.save(user);
            TbRoleUser tbRoleUser = new TbRoleUser();
            tbRoleUser.setRoleId(roleId);
            tbRoleUser.setUserId(user.getId().intValue());
            roleUserDao.save(tbRoleUser);
            return Result.ok();
        }
        return Result.error();
    }

    @Override
    public Result<TbUser> updateUser(TbUser tbUser, Integer roleId) {
        if (roleId!=null){
            userDao.updateUser(tbUser);
            TbRoleUser tbRoleUser = new TbRoleUser();
            tbRoleUser.setUserId(tbUser.getId().intValue());
            tbRoleUser.setRoleId(roleId);
            if(roleUserDao.getRoleUserByUserId(tbUser.getId().intValue())!=null){
                roleUserDao.updateTbRoleUser(tbRoleUser);
            }else {
                roleUserDao.save(tbRoleUser);
            }
            return Result.ok().message("成功");
        }else {
            return Result.error().message("失败");
        }
    }

    @Override
    public TbUser getUserById(Long id) {
        return userDao.getUserById(id);
    }

    @Override
    public TbUser getUserByPhone(String phone) {
        return userDao.getUserByPhone(phone);
    }

    @Override
    public int deleteUser(Long id) {
        roleUserDao.deleteRoleUserByUserId(id.intValue());
        return userDao.deleteUser(id.intValue());
    }
}
