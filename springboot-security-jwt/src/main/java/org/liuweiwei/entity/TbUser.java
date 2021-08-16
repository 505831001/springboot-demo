package org.liuweiwei.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 常用：PO，VO，DTO；不常用：BO，DAO；必备：POJO。
 * 1.PO(Persistent Object) 持久层对象。用于表示数据库中的一条记录映射成的 java 对象。
 * 2.DTO(Data Transfer Object) 数据传输对象。前端请求时传输；也可理解成"上层"请求调用时传输。主要用于远程调用等需要大量传输对象的地方，
 * 3.VO(View Object) 表现层对象。主要对应前端界面显示的数据对象。
 * 常用：PO，VO，DTO；不常用：BO，DAO；必备：POJO。
 * 0.BO(Business Object) 业务层对象，是简单的真实世界的软件抽象，通常位于中间层。BO 的主要作用是把业务逻辑封装为一个对象，这个对象可以包括一个或多个其它的对象。
 * 0.DAO(Data Access Object) 数据访问对象，它是一个面向对象的数据库接口，负责持久层的操作，为业务层提供接口，主要用来封装对数据库的访问，
 * 0.POJO(Plain Ordinary Java Object) 简单的 Java 对象，实际就是普通的 JavaBeans，是为了避免和 EJB（Enterprise JavaBean）混淆所创造的简称。POJO 实质上可以理解为简单的实体类，
 *
 * @author Liuweiwei
 * @since 2021-07-04
 */
@Data
public class TbUser implements Serializable {
    private static final long serialVersionUID = 4445608153487429917L;
    /**主键ID*/
    @TableId(value = "id", type = IdType.UUID)
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
    @TableField(value = "created")
    private Date created;
    /**更新时间*/
    @TableField(value = "updated")
    private Date updated;
}