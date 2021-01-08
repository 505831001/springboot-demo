package com.liuweiwei.model;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author liuweiwei
 * @since 2021-01-06
 */
@Data
@ToString
public class TbUser extends Model<TbUser> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码，加密存储
     */
    private String password;
    /**
     * 角色
     */
    private String role;
    /**
     * 权限
     */
    private String permission;
    /**
     * 账号状态
     */
    private String ban;
    /**
     * 注册手机号
     */
    private String phone;
    /**
     * 注册邮箱
     */
    private String email;
    /**
     * 创建时间
     */
    private Date created;
    /**
     * 更新时间
     */
    private Date updated;
}
