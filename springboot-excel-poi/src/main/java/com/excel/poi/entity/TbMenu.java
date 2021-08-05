package com.excel.poi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author Liuweiwei
 * @since 2021-08-05
 */
@Data
@NoArgsConstructor
@ToString
public class TbMenu implements Serializable {
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
    /**次级菜单*/
    @TableField(value = "children")
    private List<TbMenu> children;

    public TbMenu(String id, String parentId, String menuName, String role, String permission) {
        this.id = id;
        this.parentId = parentId;
        this.menuName = menuName;
        this.role = role;
        this.permission = permission;
    }
}
