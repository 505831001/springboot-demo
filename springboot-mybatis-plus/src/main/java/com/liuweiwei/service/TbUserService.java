package com.liuweiwei.service;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.liuweiwei.dto.TbUserDTO;
import com.liuweiwei.model.TbUser;
import com.liuweiwei.vo.TbUserVO;

import java.io.Serializable;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author Liuweiwei
 * @since 2021-01-06
 */
public interface TbUserService extends IService<TbUser> {
    /**
     * 根据 ID 查询
     *
     * @param id
     * @return
     */
    TbUser otherGetById(Serializable id);

    /**
     * 根据 Wrapper，查询一条记录
     *
     * @param queryWrapper
     * @return
     */
    TbUser otherGetOne(Wrapper<TbUser> queryWrapper);

    /**
     * 根据 Wrapper 条件，查询全部记录
     *
     * @param queryWrapper
     * @return
     */
    Map<String, Object> otherGetMap(Wrapper<TbUser> queryWrapper);

    /**
     * 查询所有
     *
     * @return
     * @throws Exception
     */
    List<TbUser> otherList() throws Exception;

    /**
     * 根据 entity 条件，查询全部记录
     *
     * @param queryWrapper
     * @return
     */
    List<TbUser> otherList(Wrapper<TbUser> queryWrapper);

    /**
     * 查询（根据ID 批量查询）
     *
     * @param ids
     * @return
     */
    List<TbUser> otherListByIds(List<Integer> ids);

    /**
     * 查询（根据 columnMap 条件）
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
     * 根据 Wrapper 条件，查询全部记录
     *
     * @param queryWrapper
     * @return
     */
    List<Map<String, Object>> otherListMaps(Wrapper<TbUser> queryWrapper);

    /**
     * 根据 Wrapper 条件，查询全部记录
     *
     * @param queryWrapper
     * @return
     */
    List<Object> otherListObjs(Wrapper<TbUser> queryWrapper);

    /**
     * 无条件翻页查询
     *
     * @param page
     * @return
     */
    Page<TbUserVO> otherPage(Page<TbUser> page);

    /**
     * 根据 entity 条件，查询全部记录（并翻页）
     *
     * @param page
     * @param queryWrapper
     * @return
     */
    Page<TbUser> otherPage(Page<TbUser> page, Wrapper<TbUser> queryWrapper);

    /**
     * 无条件翻页查询
     *
     * @param page
     * @return
     */
    Page<Map<String, Object>> otherPageMaps(Page<TbUser> page);

    /**
     * 根据 Wrapper 条件，查询全部记录（并翻页）
     *
     * @param page
     * @param queryWrapper
     * @return
     */
    Page<Map<String, Object>> otherPageMaps(Page<TbUser> page, Wrapper<TbUser> queryWrapper);

    /**
     * 查询总记录数
     *
     * @return
     */
    Integer otherCount();

    /**
     * 根据 Wrapper 条件，查询总记录数
     *
     * @param queryWrapper
     * @return
     */
    Integer otherCount(Wrapper<TbUser> queryWrapper);

    /**
     * 插入一条记录（选择字段，策略插入）
     *
     * @param dto
     * @return
     */
    Boolean otherSave(TbUserDTO dto) throws ParseException;

    Integer jdbcUpdate(TbUser user);

    PageInfo<TbUser> githubPage(int currentNum, int pageSize);
}
