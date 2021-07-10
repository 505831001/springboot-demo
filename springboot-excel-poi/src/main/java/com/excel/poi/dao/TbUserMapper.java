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

    int insert2(TbUser record);

    int insertSelective(TbUser record);

    TbUser selectByPrimaryKey(@Param("id") Long id);

    int updateByPrimaryKeySelective(TbUser record);

    int updateByPrimaryKey(TbUser record);
}