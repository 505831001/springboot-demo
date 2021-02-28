package com.liuweiwei.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liuweiwei.model.TbUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

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
}