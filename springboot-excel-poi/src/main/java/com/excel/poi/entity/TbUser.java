package com.excel.poi.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
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

    private String username;

    private String password;

    private String role;

    private String permission;

    private String ban;

    private String phone;

    private String email;

    private Date created;

    private Date updated;
}