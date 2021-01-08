package org.liuweiwei.dao;

import org.apache.ibatis.annotations.Param;
import org.liuweiwei.model.TbUser;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
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
