package com.codermy.myspringsecurity.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author LiuWeiWei
 * @since 2022-01-06
 */
@Data
public class PermissionDto implements Serializable {
    private Integer id;
    private Integer parentId;
    private String checkArr = "0";
    private String title;
}
