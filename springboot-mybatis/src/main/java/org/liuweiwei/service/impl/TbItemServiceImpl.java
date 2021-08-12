package org.liuweiwei.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.liuweiwei.dao.TbItemMapper;
import org.liuweiwei.model.TbItem;
import org.liuweiwei.service.TbItemService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Liuweiwei
 * @since 2021-01-06
 */
@Service
public class TbItemServiceImpl implements TbItemService {

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Resource
    private TbItemMapper itemMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<TbItem> findAll() {
        List<TbItem> list = (List<TbItem>) redisTemplate.opsForValue().get("list");
        if (CollectionUtils.isEmpty(list)) {
            list = itemMapper.selectAll();
            redisTemplate.opsForValue().set("list", list);
            logger.info("查询MySQL数据库信息");
            redisTemplate.expire(list.toString(), 60L, TimeUnit.SECONDS);
            logger.info("写入NoSQL数据库信息");
        } else {
            logger.info("查询NoSQL数据库信息");
        }
        return list;
    }

    @Override
    public PageInfo<TbItem> githubPage(int currentNum, int pageSize) {
        /**开始分页*/
        PageHelper.startPage(currentNum, pageSize);
        /**包装Page对象*/
        PageInfo<TbItem> pageInfo = new PageInfo<>(itemMapper.selectAll());
        return pageInfo;
    }

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public Integer jdbcUpdate(TbItem item) {
        int index = jdbcTemplate.update("update tb_item set username = ? where id = ?", "李四B", 14);
        return index;
    }
}
