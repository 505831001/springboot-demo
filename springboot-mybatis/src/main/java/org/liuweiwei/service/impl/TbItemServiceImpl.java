package org.liuweiwei.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.liuweiwei.dao.TbItemMapper;
import org.liuweiwei.model.TbItem;
import org.liuweiwei.service.TbItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Liuweiwei
 * @since 2021-01-06
 */
@Service
public class TbItemServiceImpl implements TbItemService {

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<TbItem> findAll() {
        List<TbItem> list = new ArrayList<>();
        list = (List<TbItem>) redisTemplate.opsForValue().get("list");
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
}
