package com.excel.poi.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
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
    /**主键ID(MyBatis-Plus默认主键策略:ASSIGN_ID使用了雪花算法(19位),ASSIGN_UUID(32位))*/
    @TableId(value = "id", type = IdType.ASSIGN_ID)
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
    /**创建用户*/
    @TableField(value = "create_name")
    private String createName;
    /**更新用户*/
    @TableField(value = "update_name")
    private String updateName;
    /**创建时间(年-月-日 时:分:秒)*/
    @TableField(value = "create_time", fill = FieldFill.INSERT_UPDATE)
    private Date createTime;
    /**更新时间(年-月-日 时:分:秒)*/
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    /**乐观锁(版本号)*/
    //@Version
    //private Integer version;
}
