package com.example.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.model.TbUser;
import org.springframework.stereotype.Repository;

/**
 * Action:
 * Consider defining a bean of type 'org.liuweiwei.dao.TbUserMapper' in your configuration.
 * 1. 通过配置扫描注解：@MapperScan(basePackages = "org.example.dao")
 * 2. 通过注释：@Mapper
 *
 * @author Liuweiwei
 * @since 2021-01-06
 */
@Repository
public interface TbUserMapper extends BaseMapper<TbUser> {

}
