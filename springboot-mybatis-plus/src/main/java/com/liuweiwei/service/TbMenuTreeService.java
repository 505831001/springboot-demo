package com.liuweiwei.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liuweiwei.model.TbMenuTree;
import com.liuweiwei.utils.ResultData;

/**
 * @author Liuweiwei
 * @since 2021-01-06
 */
public interface TbMenuTreeService extends IService<TbMenuTree> {

    /**
     * 新增菜单树接口
     * @param menuTree
     * @return
     */
    ResultData insertOther(TbMenuTree menuTree);

    /**
     * 删除菜单树接口
     * @param id
     * @return
     */
    ResultData deleteOther(String id);

    /**
     * 编辑菜单树接口
     * @param menuTree
     * @return
     */
    ResultData updateOther(TbMenuTree menuTree);

    /**
     * 根据ID批量查询接口
     * @param id
     * @return
     */
    ResultData selectOther(String id);

    /**
     * 根据ID批量查询接口
     * @param id
     * @return
     */
    ResultData selectPathOther(String id);

    /**
     * 分页批量查询接口
     * @param current
     * @param size
     * @return
     */
    ResultData selectPageOther(String current, String size);

    /**
     * 树型结构查询接口
     * @return
     */
    ResultData selectMenuTree();
}
