package com.mybatis.dynamic.service;


import com.mybatis.dynamic.model.TbItem;

import java.util.List;

/**
 * @author Liuweiwei
 * @since 2021-01-06
 */
public interface TbItemService {

    /**
     * 查询用户列表
     *
     * @return
     */
    List<TbItem> findAll();
}
