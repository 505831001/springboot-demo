package com.coder.springsecurity.eneity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author LiuWeiWei
 * @since 2022-01-06
 */
@Data
@ApiModel(value = "RolePermission对象", description = "角色菜单关联表")
public class RolePermission {
    private Integer roleId;
    private Integer permissionId;
}
