package com.codermy.myspringsecurity.service;

import com.codermy.myspringsecurity.dto.RoleDto;
import com.codermy.myspringsecurity.eneity.TbRole;
import com.codermy.myspringsecurity.utils.Result;

/**
 * @author LiuWeiWei
 * @since 2022-01-06
 */
public interface RoleService {

    Result<TbRole> getAllRolesByPage(Integer startPosition, Integer limit);

    Result<TbRole> getAllRoles();

    Result save(RoleDto roleDto);

    TbRole getRoleById(Integer id);

    Result update(RoleDto roleDto);

    Result<TbRole> delete(Integer id);

}
