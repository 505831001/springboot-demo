package com.excel.poi.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Liuweiwei
 * @since 2021-07-04
 */
@Data
public class TbUser implements Serializable {
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