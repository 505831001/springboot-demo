package com.mybatis.dynamic.service.impl;

import com.mybatis.dynamic.dao.TbItemMapper;
import com.mybatis.dynamic.model.TbItem;
import com.mybatis.dynamic.service.TbItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Liuweiwei
 * @since 2021-01-06
 */
@Service
public class TbItemServiceImpl implements TbItemService {

    @Autowired
    private TbItemMapper itemMapper;

    @Override
    public List<TbItem> findAll() {
        List<TbItem> list = itemMapper.selectAll();
        return list;
    }
}
