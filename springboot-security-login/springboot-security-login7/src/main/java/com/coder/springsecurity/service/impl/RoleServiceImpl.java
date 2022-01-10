package com.coder.springsecurity.service.impl;

import com.coder.springsecurity.dao.RoleDao;
import com.coder.springsecurity.service.RoleService;
import com.coder.springsecurity.utils.Result;
import com.coder.springsecurity.dao.RolePermissionDao;
import com.coder.springsecurity.dao.RoleUserDao;
import com.coder.springsecurity.dto.RoleDto;
import com.coder.springsecurity.eneity.TbRole;
import com.coder.springsecurity.eneity.TbRoleUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author LiuWeiWei
 * @since 2022-01-06
 */
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDao roleDao;;

    @Autowired
    private RolePermissionDao rolePermissionDao;

    @Autowired
    private RoleUserDao roleUserDao;

    @Override
    public Result<TbRole> getAllRolesByPage(Integer startPosition, Integer limit) {
        return Result.ok().count(roleDao.countAllRoles().intValue()).data(roleDao.getAllRolesByPage(startPosition, limit));
    }

    @Override
    public Result<TbRole> getAllRoles() {

        return Result.ok().data(roleDao.getAllRoles());
    }

    @Override
    public Result save(RoleDto roleDto) {
        //1、先保存角色"
        roleDao.saveRole(roleDto);
        List<Integer> permissionIds = roleDto.getPermissionIds();
        //移除0,permission id是从1开始
        //2、保存角色对应的所有权限
        if (!CollectionUtils.isEmpty(permissionIds)) {
            rolePermissionDao.save(roleDto.getId(), permissionIds);
        }
        return Result.ok();
    }

    @Override
    public TbRole getRoleById(Integer id) {
        return roleDao.getById(id);
    }

    @Override
    public Result update(RoleDto roleDto) {
        List<Integer> permissionIds = roleDto.getPermissionIds();
        permissionIds.remove(0L);
        //1、更新角色权限之前要删除该角色之前的所有权限
        rolePermissionDao.deleteRolePermission(roleDto.getId());
        //2、判断该角色是否有赋予权限值，有就添加"
        if (!CollectionUtils.isEmpty(permissionIds)) {
            rolePermissionDao.save(roleDto.getId(), permissionIds);
        }
        //3、更新角色表
        int countData = roleDao.update(roleDto);
        if(countData > 0){
            return Result.ok();
        }else{
            return Result.error();
        }
    }

    @Override
    public Result<TbRole> delete(Integer id) {
        List<TbRoleUser> tbRoleUsers = roleUserDao.listAllRoleUserByRoleId(id);
        if(tbRoleUsers.size() <= 0){
            rolePermissionDao.deleteRolePermission(id);
            roleDao.delete(id);
            return Result.ok();
        }
        return Result.error().message("该角色已经关联,无法删除");
    }

}
