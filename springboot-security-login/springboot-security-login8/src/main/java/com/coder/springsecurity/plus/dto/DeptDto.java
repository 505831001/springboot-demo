package com.coder.springsecurity.plus.dto;

import com.coder.springsecurity.plus.entity.BaseEntity;
import lombok.Data;

/**
 * @author LiuWeiWei
 * @since 2022-01-06
 */
@Data
public class DeptDto extends BaseEntity {

    private Integer id;

    private Integer parentId;

    private String checkArr = "0";

    private String title;
}
