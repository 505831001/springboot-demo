package com.liuweiwei.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

    /**主键ID*/
    @NotNull(message = "主键id不能为空")
    @Min(value = 1, message = "主键id必须为正整数")
    @TableId(value = "id", type = IdType.UUID)
    private Long id;

    /**用户名*/
    @NotBlank(message = "用户名称不能为空")
    @TableField(value = "username")
    private String username;

    /**密码，加密存储*/
    @NotBlank(message = "用户密码不能为空")
    @TableField(value = "password")
    private String password;

    /**角色*/
    @TableField(value = "role")
    private String role;

    /**权限*/
    @TableField(value = "permission")
    private String permission;

    /**账号状态*/
    @TableField(value = "ban")
    private String ban;

    /**注册手机号*/
    @TableField(value = "phone")
    private String phone;

    /**注册邮箱*/
    @TableField(value = "email")
    private String email;

    /**创建时间*/
    @TableField(value = "created")
    private Date created;

    /**更新时间*/
    @TableField(value = "updated")
    private Date updated;
}
