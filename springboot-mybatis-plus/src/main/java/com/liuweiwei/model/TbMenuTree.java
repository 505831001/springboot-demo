package com.liuweiwei.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 菜单树形结构表
 * </p>
 *
 * @author LiuWeiWei
 * @since 2021-12-06
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TbMenuTree extends Model<TbMenuTree> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**类别ID*/
    @TableId(value = "id", type = IdType.AUTO)
    private String id;
    /**父类别ID*/
    @TableField(value = "parent_id")
    private String parentId;
    /**菜单名称*/
    @TableField(value = "menu_name")
    private String menuName;
    /**菜单角色(admin-管理员，guest-宾客)*/
    @TableField(value = "role")
    private String role;
    /**菜单权限*/
    @TableField(value = "permission")
    private String permission;
    /**创建时间(年-月-日 时:分:秒)*/
    @TableField(value = "create_time")
    private Date createTime;
    /**创建用户*/
    @TableField(value = "create_name")
    private String createName;
    /**更新时间(年-月-日 时:分:秒)*/
    @TableField(value = "update_time")
    private Date updateTime;
    /**更新用户*/
    @TableField(value = "update_name")
    private String updateName;
}
