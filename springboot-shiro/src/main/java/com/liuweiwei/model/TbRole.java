package com.liuweiwei.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

/**
 * @author Liuweiwei
 * @since 2020-12-23
 */
@Data
@AllArgsConstructor
public class TbRole {
    private String id;
    private String roleName;
    /**
     * 角色对应权限集合
     */
    private Set<TbPermission> permissions;
}
