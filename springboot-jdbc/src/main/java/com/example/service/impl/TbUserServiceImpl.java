package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dao.TbUserMapper;
import com.example.model.TbUser;
import com.example.service.TbUserService;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;

/**
 * @author Liuweiwei
 * @since 2021-01-06
 */
@Service
public class TbUserServiceImpl extends ServiceImpl<TbUserMapper, TbUser> implements TbUserService {

    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private TbUserMapper tbUserMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public TbUser otherById(Serializable id) {
        String sql = "SELECT * FROM tb_user WHERE id=?";
        TbUser user = new TbUser();

        user = this.getById(id);
        return user;
    }

    @Override
    public TbUser otherOne(TbUser tbUser) {
        return null;
    }

    @Override
    public List<TbUser> otherList() {
        /**jdbc查询数据库*/
        String sql = "SELECT * FROM tb_user WHERE 1=1";
        List list = jdbcTemplate.query(sql, new BeanPropertyRowMapper(TbUser.class));
        List<Map<String, Object>> maps = new ArrayList<>();

        QueryWrapper<TbUser> wrapper = new QueryWrapper<>();

        list = this.list();
        list = this.list(wrapper);
        maps = this.listMaps();
        maps = this.listMaps(wrapper);

        return list;
    }
}
