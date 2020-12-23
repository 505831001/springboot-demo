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
public class TbUser {
    private String id;
    private String userName;
    private String password;
    /**
     * 用户对应的角色集合
     */
    private Set<TbRole> roles;
}
