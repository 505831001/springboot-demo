package com.coder.springsecurity.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.coder.springsecurity.dao.PermissionDao;
import com.coder.springsecurity.dao.RolePermissionDao;
import com.coder.springsecurity.dto.PermissionDto;
import com.coder.springsecurity.eneity.TbPermission;
import com.coder.springsecurity.service.PermissionService;
import com.coder.springsecurity.utils.Result;
import com.coder.springsecurity.utils.TreeUtil;
import com.coder.springsecurity.utils.TreeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author LiuWeiWei
 * @since 2022-01-06
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private RolePermissionDao rolePermissionDao;

    @Override
    public Result<TbPermission> getMenuAll() {
        return Result.ok().data(permissionDao.findAll());
    }

    @Override
    public List<PermissionDto> buildMenuAll() {
        return permissionDao.buildAll();
    }

    @Override
    public Result<TbPermission> save(TbPermission permission) {
        return (permissionDao.save(permission) > 0) ? Result.ok().message("请求成功") : Result.error().message("请求失败");
    }

    @Override
    public TbPermission getTbPermissionById(Integer id) {
            return permissionDao.getTbPermissionById(id);
    }

    @Override
    public Result updateTbPermission(TbPermission permission) {
        return (permissionDao.update(permission) > 0) ? Result.ok().message("请求成功") : Result.error().message("请求失败");
    }

    @Override
    public Result delete(Integer id) {
        permissionDao.deleteById(id);
        permissionDao.deleteByParentId(id);
        return Result.ok();
    }

    @Override
    public List<PermissionDto> buildMenuAllByRoleId(Integer roleId) {
        List<PermissionDto> listByRoleId = permissionDao.listByRoleId(roleId);
        List<PermissionDto> permissionDtos = permissionDao.buildAll();
        List<PermissionDto> tree = TreeUtil.tree(listByRoleId, permissionDtos);
        return tree;
    }

    @Override
    public Result<TbPermission> getMenu(Integer userId) {
        List<TbPermission> datas = permissionDao.listByUserId(userId);
        datas = datas.stream().filter(p -> p.getType().equals(1)).collect(Collectors.toList());
        JSONArray array = new JSONArray();
        TreeUtils.setPermissionsTree(0, datas, array);
        return Result.ok().data(array);
    }


}
