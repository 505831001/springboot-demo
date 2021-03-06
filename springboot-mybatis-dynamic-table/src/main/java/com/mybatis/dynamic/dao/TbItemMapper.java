package com.mybatis.dynamic.dao;

import com.mybatis.dynamic.model.TbItem;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 商品表 服务映射类
 * </p>
 *
 * @author liuweiwei
 * @since 2020-05-19
 */
@Repository
public interface TbItemMapper {

    /**
     *
     * @return
     */
    List<TbItem> selectAll();
}