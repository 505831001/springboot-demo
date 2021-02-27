package com.mybatis.dynamic.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Liuweiwei
 * @since 2021-01-17
 */
@Data
@ToString
public class TbRootDML implements Serializable {

    private Long id;

    private String userName;

    private String operation;

    private String method;

    private String params;

    private Long time;

    private String ip;
    
    private String tableName;
}