package org.liuweiwei.dao;

import org.liuweiwei.model.TbItem;
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
     * 查询列表
     *
     * @return
     */
    List<TbItem> selectAll();
}