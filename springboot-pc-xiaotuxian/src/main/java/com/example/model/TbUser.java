package com.example.model;

import lombok.*;

import java.io.Serializable;

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
public class TbUser implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String username;
    private String password;
    private String gender;
    private String phone;
    private String email;
    private String address;
    private String deptCode;
    private String deptName;
}
