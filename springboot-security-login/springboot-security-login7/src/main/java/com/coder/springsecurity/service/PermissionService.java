package com.coder.springsecurity.service;

import com.coder.springsecurity.dto.PermissionDto;
import com.coder.springsecurity.eneity.TbPermission;
import com.coder.springsecurity.utils.Result;

import java.util.List;

/**
 * @author LiuWeiWei
 * @since 2022-01-06
 */
public interface PermissionService {

    Result<TbPermission> getMenuAll();

    List<PermissionDto> buildMenuAll();

    Result<TbPermission> save(TbPermission permission);

    TbPermission getTbPermissionById(Integer id);

    Result updateTbPermission(TbPermission permission);

    Result delete(Integer id);

    List<PermissionDto> buildMenuAllByRoleId(Integer roleId);

    Result<TbPermission> getMenu(Integer userId);
}
