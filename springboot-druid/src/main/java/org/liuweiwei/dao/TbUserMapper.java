package org.liuweiwei.dao;

import org.apache.ibatis.annotations.Param;
import org.liuweiwei.model.TbUser;
import org.springframework.stereotype.Repository;

import java.util.List;

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
public interface TbUserMapper {

    /**
     * 根据用户ID查询用户信息
     *
     * @param userId
     * @return
     */
    TbUser selectByUserId(@Param("userId") Long userId);

    /**
     * 查询用户列表
     *
     * @return
     */
    List<TbUser> selectAll();
}
