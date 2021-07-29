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
    @TableField(value = "用户名称")
    private String username;
    @TableField(value = "用户密码")
    private String password;
    @TableField(value = "用户角色(admin-管理员，guest-宾客)")
    private String role;
    @TableField(value = "用户权限")
    private String permission;
    @TableField(value = "用户状态(1-可用，0停用)")
    private String ban;
    @TableField(value = "电话号码")
    private String phone;
    @TableField(value = "邮箱地址")
    private String email;
    @TableField(value = "创建时间(年-月-日 时:分:秒)")
    private Date created;
    @TableField(value = "修改时间(年-月-日 时:分:秒)")
    private Date updated;
}