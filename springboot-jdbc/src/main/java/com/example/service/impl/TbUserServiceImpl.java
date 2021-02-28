package com.example.service.impl;

import com.example.model.TbUser;
import com.example.service.TbUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Liuweiwei
 * @since 2021-01-06
 */
@Service
public class TbUserServiceImpl implements TbUserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public TbUser findByUserId(Long userId) {
        String sql = "SELECT * FROM tb_user WHERE id=?";
        return null;
    }

    @Override
    public List<TbUser> findAll() {
        String sql = "SELECT * FROM tb_user WHERE 1=1";
        List list = jdbcTemplate.query(sql, new BeanPropertyRowMapper(TbUser.class));
        return list;
    }
}
