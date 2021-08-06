package com.excel.poi.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.excel.poi.dao.TbMenuTreeMapper;
import com.excel.poi.entity.TbMenuTree;
import com.excel.poi.service.TbMenuTreeService;
import com.excel.poi.utils.MenuTreeUtils;
import com.excel.poi.vo.TbMenuTreeVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public List<TbMenuTreeVO> otherList() throws Exception {
        /**获取磁盘库数据*/
        List<TbMenuTree> list = this.list();
        list = this.list(null);
        list = this.getBaseMapper().selectList(null);
        list = tbMenuTreeMapper.selectList(null);
        /**PO列表转VO列表*/
        List<TbMenuTreeVO> voList = new ArrayList<>(10);
        list.stream().forEach(source -> {
            TbMenuTreeVO target = new TbMenuTreeVO();
            BeanUtils.copyProperties(source, target);
            target.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(source.getCreateTime()));
            target.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(source.getUpdateTime()));
            voList.add(target);
        });
        /**获取数据表中数据添加到树*/
        MenuTreeUtils menuTree = new MenuTreeUtils(voList);
        List<TbMenuTreeVO> data = menuTree.buildMenuTree();
        return data;
    }

    @Override
    public List<TbMenuTree> otherListByIds(List<Integer> ids) {
        return null;
    }

    @Override
    public Page<TbMenuTree> otherPage(long current, long size) {
        Page<TbMenuTree> page = this.page(new Page<>(current, size));
        return page;
    }

    @Override
    public Page<Map<String, Object>> otherPageMaps(long current, long size) {
        Page<Map<String, Object>> maps = this.pageMaps(new Page<>(current, size));
        return maps;
    }

    @Override
    public Integer otherCount() {
        return null;
    }
}
