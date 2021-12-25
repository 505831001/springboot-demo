package org.liuweiwei.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;

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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TbUser extends Model<TbUser> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**主键ID(MyBatis-Plus默认主键策略:ASSIGN_ID使用了雪花算法(19位),ASSIGN_UUID(32位))*/
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    /**用户名*/
    @TableField(value = "username")
    private String username;
    /**密码，加密存储*/
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
    @TableField(value = "created", fill = FieldFill.INSERT_UPDATE)
    private Date created;
    /**更新时间*/
    @TableField(value = "updated", fill = FieldFill.INSERT_UPDATE)
    private Date updated;
    /**乐观锁(版本号)*/
    //@Version
    //private Integer version;
}
