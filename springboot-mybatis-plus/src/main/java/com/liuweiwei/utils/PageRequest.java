package com.liuweiwei.utils;

import lombok.Data;

/**
 * 分页请求
 *
 * @author Administrator
 */
@Data
public class PageRequest {
    /**
     * 当前页码
     */
    private int pageNum;

    /**
     * 每页数量
     */
    private int pageSize;
}