package com.liuweiwei.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
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
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**用户名*/
    @NotBlank(message = "用户名不能为空")
    @TableField
    private String username;
    /**密码，加密存储*/
    @NotBlank(message = "密码不能为空")
    @TableField
    private String password;
    /**角色*/
    @TableField
    private String role;
    /**权限*/
    @TableField
    private String permission;
    /**账号状态*/
    @TableField
    private String ban;
    /**注册手机号*/
    @TableField
    private String phone;
    /**注册邮箱*/
    @TableField
    private String email;
    /**创建时间*/
    @TableField
    private Date created;
    /**更新时间*/
    @TableField
    private Date updated;
}
