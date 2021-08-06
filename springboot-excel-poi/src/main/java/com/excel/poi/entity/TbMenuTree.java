package com.excel.poi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Liuweiwei
 * @since 2021-08-05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TbMenuTree implements Serializable {
    private static final long serialVersionUID = 1342245960543905436L;
    /**主键ID(子节点ID)*/
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**父节点ID*/
    @TableField(value = "parent_id")
    private String parentId;
    /**菜单名称*/
    @TableField(value = "menu_name")
    private String menuName;
    /**角色*/
    @TableField(value = "role")
    private String role;
    /**权限*/
    @TableField(value = "permission")
    private String permission;
    /**创建时间*/
    @TableField(value = "create_time")
    private Date createTime;
    /**创建人员*/
    @TableField(value = "create_name")
    private String createName;
    /**更新时间*/
    @TableField(value = "update_time")
    private Date updateTime;
    /**更新人员*/
    @TableField(value = "update_name")
    private String updateName;
}
