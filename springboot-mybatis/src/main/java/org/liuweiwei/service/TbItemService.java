package org.liuweiwei.service;

import com.github.pagehelper.PageInfo;
import org.liuweiwei.model.TbItem;

import java.util.List;

/**
 * @author Liuweiwei
 * @since 2021-01-06
 */
public interface TbItemService {
    /**
     * 查询用户列表
     *
     * @return
     */
    List<TbItem> findAll();

    /**
     * GitHub分页插件
     * @param currentNum
     * @param pageSize
     * @return
     */
    PageInfo<TbItem> githubPage(int currentNum, int pageSize);

    /**
     * Spring数据源+JDBC模板
     * @param item
     * @return
     */
    Integer jdbcUpdate(TbItem item);
}
