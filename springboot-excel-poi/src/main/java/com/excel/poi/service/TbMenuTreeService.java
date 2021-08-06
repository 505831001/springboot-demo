package com.excel.poi.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.excel.poi.entity.TbMenuTree;

import java.io.Serializable;
import java.util.List;

/**
 * @author Liuweiwei
 * @since 2021-08-06
 */
public interface TbMenuTreeService extends IService<TbMenuTree> {
    /**
     * 根据 ID 查询
     *
     * @param id
     * @return
     */
    TbMenuTree otherGetById(Serializable id);

    /**
     * 根据 Wrapper，查询一条记录
     *
     * @param queryWrapper
     * @return
     */
    TbMenuTree otherGetOne(Wrapper<TbMenuTree> queryWrapper);

    /**
     * 查询所有
     *
     * @return
     * @throws Exception
     */
    List<TbMenuTree> otherList() throws Exception;

    /**
     * 查询（根据ID 批量查询）
     *
     * @param ids
     * @return
     */
    List<TbMenuTree> otherListByIds(List<Integer> ids);

    /**
     * 无条件翻页查询
     *
     * @param page
     * @return
     */
    Page<TbMenuTree> otherPage(Page<TbMenuTree> page);

    /**
     * 查询总记录数
     *
     * @return
     */
    Integer otherCount();
}
