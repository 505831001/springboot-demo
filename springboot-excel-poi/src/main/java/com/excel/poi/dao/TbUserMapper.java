package com.excel.poi.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.excel.poi.entity.TbUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Liuweiwei
 * @since 2021-07-04
 */
@Repository
public interface TbUserMapper extends BaseMapper<TbUser> {

    int deleteByPrimaryKey(@Param("id") Long id);

    int insert(@Param("record") TbUser record);

    int insertSelective(@Param("record") TbUser record);

    TbUser selectByPrimaryKey(@Param("id") Long id);

    int updateByPrimaryKeySelective(@Param("record") TbUser record);

    int updateByPrimaryKey(@Param("record") TbUser record);
}