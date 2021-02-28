package org.liuweiwei.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.liuweiwei.dao.TbUserMapper;
import org.liuweiwei.service.TbUserService;
import org.liuweiwei.model.TbUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author Liuweiwei
 * @since 2021-01-06
 */
@Service
public class TbUserServiceImpl implements TbUserService {

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    private TbUserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public TbUser queryOne(TbUser tbUser) {
        Long userId = tbUser.getId();

        tbUser = (TbUser) redisTemplate.opsForValue().get(userId);
        if (Objects.isNull(tbUser)) {
            // XML 配置写法
            tbUser = userMapper.selectByUserId(userId);
            // MyBatis 内嵌脚本
            // tbUser = userMapper.selectById(userId);
            logger.info("查询MySQL数据库");
            redisTemplate.opsForValue().set(userId, tbUser);
            redisTemplate.expire(userId, 60L, TimeUnit.SECONDS);
            logger.info("写入NoSQL数据库");
        } else {
            logger.info("查询NoSQL数据库");
        }
        return tbUser;
    }

    @Override
    public List<TbUser> findAll() {
        List<TbUser> list = userMapper.selectAll();
        return list;
    }
}
