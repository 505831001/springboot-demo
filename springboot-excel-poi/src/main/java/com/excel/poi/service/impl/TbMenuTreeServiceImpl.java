package com.excel.poi.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.excel.poi.dao.TbMenuTreeMapper;
import com.excel.poi.entity.TbMenuTree;
import com.excel.poi.service.TbMenuTreeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * @author Liuweiwei
 * @since 2021-08-06
 */
@Service
public class TbMenuTreeServiceImpl extends ServiceImpl<TbMenuTreeMapper, TbMenuTree> implements TbMenuTreeService {

    @Resource
    private TbMenuTreeMapper tbMenuTreeMapper;

    @Override
    public TbMenuTree otherGetById(Serializable id) {
        return null;
    }

    @Override
    public TbMenuTree otherGetOne(Wrapper<TbMenuTree> queryWrapper) {
        return null;
    }

    @Override
    public List<TbMenuTree> otherList() throws Exception {
        List<TbMenuTree> list = this.list();
        list = this.list(null);
        list = this.getBaseMapper().selectList(null);
        list = tbMenuTreeMapper.selectList(null);
        return list;
    }

    @Override
    public List<TbMenuTree> otherListByIds(List<Integer> ids) {
        return null;
    }

    @Override
    public Page<TbMenuTree> otherPage(Page<TbMenuTree> page) {
        return null;
    }

    @Override
    public Integer otherCount() {
        return null;
    }
}
