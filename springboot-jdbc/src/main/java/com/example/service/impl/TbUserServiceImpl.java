package com.example.service.impl;

import com.example.model.TbUser;
import com.example.service.TbUserService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * @author Liuweiwei
 * @since 2021-01-06
 */
@Service
public class TbUserServiceImpl implements TbUserService {

    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public TbUser otherById(Serializable id) {
        return null;
    }

    @Override
    public TbUser otherOne(TbUser tbUser) {
        return null;
    }

    @Override
    public List<TbUser> otherList() {
        String sql = "SELECT * FROM tb_user WHERE 1=1";
        List<TbUser> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>());
        return list;
    }
}
