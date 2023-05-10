package com.example.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.TbUser;
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
     * 分页查询列表
     *
     * @param current
     * @param size
     * @return
     */
    List<TbUser> selectByPage(@Param("current") long current, @Param("size") long size);
}