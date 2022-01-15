package com.coder.springsecurity.plus.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author LiuWeiWei
 * @since 2022-01-06
 */
@Data
public class MenuDto implements Serializable {

    private Integer id;

    private Integer parentId;

    private String checkArr = "0";

    private String title;
}
