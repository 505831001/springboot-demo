package com.liuweiwei.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liuweiwei.dao.TbUserMapper;
import com.liuweiwei.model.TbUser;
import com.liuweiwei.service.TbUserService;
import com.liuweiwei.utils.PageRequest;
import com.liuweiwei.utils.PageResult;
import com.liuweiwei.utils.PageUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author Liuweiwei
 * @since 2021-01-06
 */
@Service
public class TbUserServiceImpl extends ServiceImpl<TbUserMapper, TbUser> implements TbUserService {

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public TbUser findByUserId(Long userId) {
        TbUser user = (TbUser) redisTemplate.opsForValue().get("user");
        if (Objects.isNull(user)) {
            // 1. XML 脚本文档方式
            //user = userMapper.queryById(userId);
            // 2. MyBatis Plus 内嵌脚本方式
            user = this.getBaseMapper().selectById(userId);
            // 3. MyBatis Plus 工具类<Wrappers>内嵌脚本方式
            user = this.getBaseMapper().selectOne(Wrappers.lambdaQuery(TbUser.class).eq(TbUser::getId, userId).last("limit 1"));
            logger.info("查询MySQL数据库");
            redisTemplate.opsForValue().set("user", user);
            redisTemplate.expire("user", 60L, TimeUnit.SECONDS);
            logger.info("写入NoSQL数据库");
        } else {
            logger.info("查询NoSQL数据库");
        }
        return user;
    }

    /**
     * Wrappers.query();
     * Wrappers.query(T entity);
     * Wrappers.lambdaQuery();
     * Wrappers.lambdaQuery(T entity);
     * Wrappers.lambdaQuery(Class<T> entityClass);
     * Wrappers.update();
     * Wrappers.update(T entity);
     * Wrappers.lambdaUpdate();
     * Wrappers.lambdaUpdate(T entity);
     * Wrappers.lambdaUpdate(Class<T> entityClass);
     *
     * @return
     */
    @Override
    public TbUser findOne() {
        TbUser tbUser = new TbUser();
        Long userId = tbUser.getId();

        // XML 脚本格式
        // tbUser = userMapper.queryById(userId);

        // MyBatis Plus 内嵌脚本方式
        tbUser = this.getBaseMapper().selectById(userId);

        // MyBatis Plus 工具类<Wrappers>内嵌脚本方式
        QueryWrapper<TbUser> wrapper01 = new QueryWrapper<>();
        wrapper01.eq("name", "李湘").last("LIMIT 1");
        tbUser = this.getBaseMapper().selectOne(wrapper01);

        QueryWrapper<TbUser> wrapper02 = Wrappers.query();
        wrapper02.eq("name", "李湘").last("LIMIT 1");
        tbUser = this.getBaseMapper().selectOne(wrapper02);

        LambdaQueryWrapper<TbUser> wrapper03 = wrapper01.lambda();
        wrapper03.eq(TbUser::getUsername, "李湘").last("LIMIT 1");
        tbUser = this.getBaseMapper().selectOne(wrapper03);

        LambdaQueryWrapper<TbUser> wrapper04 = Wrappers.lambdaQuery(TbUser.class);
        wrapper04.eq(TbUser::getUsername, "李湘").last("LIMIT 1");
        tbUser = this.getBaseMapper().selectOne(wrapper04);

        LambdaQueryWrapper<TbUser> wrapper05 = Wrappers.<TbUser>lambdaQuery().eq(TbUser::getUsername, "李湘").last("LIMIT 1");
        tbUser = this.getBaseMapper().selectOne(wrapper05);

        LambdaQueryWrapper<TbUser> wrapper06 = Wrappers.lambdaQuery(TbUser.class).eq(TbUser::getUsername, "李湘").last("LIMIT 1");
        tbUser = this.getBaseMapper().selectOne(wrapper06);

        tbUser = this.getBaseMapper().selectOne(Wrappers.<TbUser>query().eq("name", "李湘").last("LIMIT 1"));
        tbUser = this.getBaseMapper().selectOne(Wrappers.<TbUser>lambdaQuery().eq(TbUser::getUsername, "李湘").last("LIMIT 1"));
        tbUser = this.getBaseMapper().selectOne(Wrappers.lambdaQuery(TbUser.class).eq(TbUser::getUsername, "李湘").last("LIMIT 1"));

        return tbUser;
    }

    @Override
    public Integer findCount() {
        QueryWrapper<TbUser> wrapper = new QueryWrapper<>();

        Integer count = this.getBaseMapper().selectCount(wrapper);
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

        List<TbUser> list = this.getBaseMapper().selectList(null);
        return list;
    }

    @Override
    public PageResult findPage(PageRequest pageRequest) {
        PageResult pageResult = PageUtils.getPageResult(pageRequest, getPageInfo(pageRequest));
        return pageResult;
    }

    /**
     * 调用分页插件完成分页
     *
     * @param pageRequest
     * @return
     */
    private PageInfo<TbUser> getPageInfo(PageRequest pageRequest) {
        QueryWrapper<TbUser> wrapper1 = new QueryWrapper<>();
        QueryWrapper<TbUser> wrapper2 = Wrappers.query();
        LambdaQueryWrapper<TbUser> wrapper3 = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<TbUser> wrapper4 = Wrappers.lambdaQuery();
        LambdaQueryWrapper<TbUser> wrapper = Wrappers.lambdaQuery(TbUser.class);

        PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());
        List<TbUser> list = new LinkedList<>();
        list = this.list();
        list = this.list(wrapper);
        list = this.list(null);
        list = this.getBaseMapper().selectList(wrapper);
        list = this.getBaseMapper().selectList(null);
        PageInfo<TbUser> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Autowired
    private JdbcTemplate primaryJdbcTemplate;
    @Autowired
    private JdbcTemplate secondaryJdbcTemplate;

    @Override
    public Integer update(TbUser tbUser) {
        int index = 0;
        index += primaryJdbcTemplate.update("update tb_user set username = ? where id = ?", "李四A", 14);
        index += secondaryJdbcTemplate.update("update tb_user set username = ? where id = ?", "李四B", 14);
        return index;
    }
}
