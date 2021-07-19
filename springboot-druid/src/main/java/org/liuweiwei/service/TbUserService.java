package org.liuweiwei.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.liuweiwei.model.TbUser;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * @author Liuweiwei
 * @since 2021-01-06
 */
public interface TbUserService extends IService<TbUser> {
    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    TbUser otherGetById(Serializable id);

    /**
     * 根据Wrapper条件，查询一条记录
     *
     * @param queryWrapper
     * @return
     */
    TbUser otherGetOne(Wrapper<TbUser> queryWrapper);

    /**
     * 根据Wrapper条件，查询全部记录
     *
     * @param queryWrapper
     * @return
     */
    Map<String, Object> otherGetMap(Wrapper<TbUser> queryWrapper);

    /**
     * 查询所有
     *
     * @return
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    List<TbUser> otherList() throws Exception;

    /**
     * 查询(根据ID批量查询)
     *
     * @param idList
     * @return
     */
    List<TbUser> otherListByIds(List<Integer> idList);

    /**
     * 查询(根据columnMap条件)
     *
     * @param map
     * @return
     */
    List<TbUser> otherListByMap(Map<String, Object> map);

    /**
     * 查询所有列表
     *
     * @return
     */
    List<Map<String, Object>> otherListMaps();

    /**
     * 查询全部记录
     *
     * @return
     */
    List<Object> otherListObjs();

    /**
     * 无条件翻页查询
     *
     * @param current 当前页
     * @param size    每页显示条数
     * @return
     */
    Page<TbUser> otherPage(long current, long size);

    /**
     * 无条件翻页查询
     *
     * @param current 当前页
     * @param size    每页显示条数
     * @return
     */
    Page<Map<String, Object>> otherPageMaps(long current, long size);

    /**
     * 查询总记录数
     *
     * @return
     */
    int otherCount();
}
