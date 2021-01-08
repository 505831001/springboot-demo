package com.liuweiwei.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liuweiwei.dao.TbUserMapper;
import com.liuweiwei.model.TbUser;
import com.liuweiwei.service.TbUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Liuweiwei
 * @since 2021-01-06
 */
@Service
public class TbUserServiceImpl implements TbUserService {

    @Autowired
    private TbUserMapper userMapper;

    @Override
    public TbUser findByUserId(Long userId) {
        TbUser user = userMapper.selectById(userId);
        return user;
    }

    @Override
    public TbUser findOne() {
        QueryWrapper<TbUser> wrapper01 = new QueryWrapper<>();
        wrapper01.eq("name", "李湘").last("LIMIT 1");
        userMapper.selectOne(wrapper01);

        LambdaQueryWrapper<TbUser> wrapper02 = wrapper01.lambda();
        wrapper02.eq(TbUser::getUsername, "李湘").last("LIMIT 1");
        userMapper.selectOne(wrapper02);

        LambdaQueryWrapper<TbUser> wrapper03 = Wrappers.lambdaQuery(TbUser.class);
        wrapper03.eq(TbUser::getUsername, "李湘").last("LIMIT 1");
        userMapper.selectOne(wrapper03);

        LambdaQueryWrapper<TbUser> wrapper04 = Wrappers.lambdaQuery(TbUser.class).eq(TbUser::getUsername, "李湘").last("LIMIT 1");
        userMapper.selectOne(wrapper04);

        LambdaQueryWrapper<TbUser> wrapper05 = Wrappers.<TbUser>lambdaQuery().eq(TbUser::getUsername, "李湘").last("LIMIT 1");
        userMapper.selectOne(wrapper05);

        TbUser user = userMapper.selectOne(wrapper05);
        return user;
    }

    @Override
    public Integer findCount() {
        QueryWrapper<TbUser> wrapper = new QueryWrapper<>();

        Integer count = userMapper.selectCount(wrapper);
        return count;
    }

    @Override
    public List<TbUser> findAll() {
        QueryWrapper<TbUser> wrapper = new QueryWrapper<>();
        wrapper.like("name", "雨").lt("age", 40);

        wrapper = Wrappers.query();
        wrapper.like("name", "雨").between("age", 20, 40).isNotNull("email");

        wrapper = Wrappers.query();
        wrapper.likeRight("name", "林").or().ge("age", 25).orderByDesc("age").orderByAsc("id");

        wrapper = Wrappers.query();
        wrapper.apply("date_format(create_time,'%Y-%m-%d') = {0}", "2019-02-14").inSql("manager_id", "SELECT id FROM tb_user WHERE `name` LIKE '林%'");

        wrapper = Wrappers.query();
        wrapper.likeRight("name", "林").and(qw -> qw.lt("age", 40).or().isNotNull("email"));

        List<TbUser> list = userMapper.selectList(null);
        return list;
    }
}
