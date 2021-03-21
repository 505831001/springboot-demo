package com.liuweiwei.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liuweiwei.model.TbUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Liuweiwei
 * @since 2021-01-06
 */
@Repository
public interface TbUserMapper extends BaseMapper<TbUser> {

    /**
     * 根据用户ID查询用户信息
     *
     * @param userId
     * @return
     */
    TbUser queryById(@Param("userId") Long userId);

    /**
     * 查询全部用户
     *
     * @return
     */
    List<TbUser> queryAll();

    /**
     * 分页查询用户
     *
     * @return
     */
    List<TbUser> queryPage();
}