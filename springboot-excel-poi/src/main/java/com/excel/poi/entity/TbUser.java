package com.excel.poi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Liuweiwei
 * @since 2021-07-04
 */
@Data
public class TbUser implements Serializable {
    @TableId(value = "id", type = IdType.UUID)
    private Long id;
    @TableField(value = "username")
    private String username;
    @TableField(value = "password")
    private String password;
    @TableField(value = "role")
    private String role;
    @TableField(value = "permission")
    private String permission;
    @TableField(value = "ban")
    private String ban;
    @TableField(value = "phone")
    private String phone;
    @TableField(value = "email")
    private String email;
    @TableField(value = "created")
    private Date created;
    @TableField(value = "updated")
    private Date updated;
}