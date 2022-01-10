package com.coder.springsecurity.service;

import com.coder.springsecurity.dto.RoleDto;
import com.coder.springsecurity.eneity.TbRole;
import com.coder.springsecurity.utils.Result;

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
