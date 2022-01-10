package com.coder.springsecurity.dto;

import com.coder.springsecurity.eneity.TbRole;

import java.util.List;

/**
 * @author LiuWeiWei
 * @since 2022-01-06
 */
public class RoleDto extends TbRole {
    private static final long serialVersionUID = -5784234789156935003L;

    private List<Integer> permissionIds;

    public List<Integer> getPermissionIds() {
        return permissionIds;
    }

    public void setPermissionIds(List<Integer> permissionIds) {
        this.permissionIds = permissionIds;
    }
}
