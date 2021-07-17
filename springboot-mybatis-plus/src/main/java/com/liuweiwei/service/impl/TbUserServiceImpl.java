package com.liuweiwei.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.liuweiwei.dao.TbUserMapper;
import com.liuweiwei.model.TbUser;
import com.liuweiwei.service.TbUserService;
import com.liuweiwei.utils.PageRequest;
import com.liuweiwei.utils.PageResult;
import com.liuweiwei.utils.PageUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Liuweiwei
 * @since 2021-01-06
 */
@Service
public class TbUserServiceImpl extends ServiceImpl<TbUserMapper, TbUser> implements TbUserService {

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Resource
    private TbUserMapper tbUserMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public TbUser otherById(Serializable id) {
        QueryWrapper<TbUser> wrapper1 = new QueryWrapper<>();
        QueryWrapper<TbUser> wrapper2 = Wrappers.query();
        LambdaQueryWrapper<TbUser> wrapper3 = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<TbUser> wrapper4 = Wrappers.lambdaQuery();
        LambdaQueryWrapper<TbUser> wrapper5 = Wrappers.lambdaQuery(TbUser.class);

        TbUser user = (TbUser) redisTemplate.opsForValue().get("user");
        if (Objects.isNull(user)) {
            // 1. XML 脚本文档方式
            user = tbUserMapper.selectById(id);

            // 2. MyBatis Plus [顶级 IService]内嵌脚本方式
            user = this.getById(id);
            user = this.getOne(wrapper1);

            // 3. MyBatis Plus [基础 Mapper]内嵌脚本方式
            user = this.getBaseMapper().selectById(id);

            // 4. MyBatis Plus <Wrappers>工具类内嵌脚本方式
            user = this.getBaseMapper().selectOne(Wrappers.lambdaQuery(TbUser.class).eq(TbUser::getId, id).last("limit 1"));

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
    public TbUser otherOne(TbUser tbUser) {
        QueryWrapper<TbUser> wrapper1 = new QueryWrapper<>();
        QueryWrapper<TbUser> wrapper2 = Wrappers.query();
        LambdaQueryWrapper<TbUser> wrapper3 = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<TbUser> wrapper4 = Wrappers.lambdaQuery();
        LambdaQueryWrapper<TbUser> wrapper5 = Wrappers.lambdaQuery(TbUser.class);
        Long id = tbUser.getId();

        if (Objects.nonNull(tbUser)) {
            Object object = redisTemplate.opsForValue().get(id);
            String data = object.toString();
            tbUser = JSONObject.parseObject(data, TbUser.class);
        }
        if (Objects.isNull(tbUser)) {
            // 1. XML 脚本文档方式
            tbUser = tbUserMapper.selectById(id);

            // 2. MyBatis Plus [顶级 IService]内嵌脚本方式
            tbUser = this.getById(id);
            tbUser = this.getOne(wrapper5);

            // 3. MyBatis [基础 Mapper]内嵌脚本方式
            tbUser = this.getBaseMapper().selectById(id);
            tbUser = this.getBaseMapper().selectOne(wrapper5);

            logger.info("查询MySQL数据库:{}", id);
            String data = JSONObject.toJSONString(tbUser);
            redisTemplate.opsForValue().set(String.valueOf(id), data);
            redisTemplate.expire(String.valueOf(id), 60L, TimeUnit.SECONDS);
            logger.info("写入NoSQL数据库:{}", id);
        }

        // MyBatis Plus <Wrapper>内嵌脚本方式
        QueryWrapper<TbUser> wrapper01 = new QueryWrapper<>();
        wrapper01.eq("name", "李湘").last("LIMIT 1");
        tbUser = this.getBaseMapper().selectOne(wrapper01);

        QueryWrapper<TbUser> wrapper02 = Wrappers.query();
        wrapper02.eq("name", "李湘").last("LIMIT 1");
        tbUser = this.getBaseMapper().selectOne(wrapper02);

        // MyBatis Plus <Wrappers>工具类内嵌脚本方式
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
    public Integer otherCount() {
        QueryWrapper<TbUser> wrapper1 = new QueryWrapper<>();
        QueryWrapper<TbUser> wrapper2 = Wrappers.query();
        LambdaQueryWrapper<TbUser> wrapper3 = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<TbUser> wrapper4 = Wrappers.lambdaQuery();
        LambdaQueryWrapper<TbUser> wrapper5 = Wrappers.lambdaQuery(TbUser.class);

        Integer count = 0;
        count = this.count();
        count = this.count(wrapper1);
        count = this.getBaseMapper().selectCount(wrapper1);
        count = this.getBaseMapper().selectCount(null);
        return count;
    }

    @Override
    public List<TbUser> otherList() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        List<TbUser> list = new LinkedList<>();
        if (org.springframework.util.CollectionUtils.isEmpty(list)) {
            // 集合数组2次遍历
            List<Object> abc = redisTemplate.opsForList().range("abc", 0, -1);
            for (Object object : abc) {
                String json = object.toString();
                List<String> strings = JSONObject.parseArray(json, String.class);
                for (String string : strings) {
                    TbUser user = JSONObject.parseObject(string, TbUser.class);
                    list.add(user);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(list)) {
            // 序列化集合对象1次遍历
            List<Object> xyz = redisTemplate.opsForList().range("xyz", 0, -1);
            for (Object object : xyz) {
                String string = object.toString();
                TbUser user = JSONObject.parseObject(string, TbUser.class);
                list.add(user);
            }
        }

        QueryWrapper<TbUser> wrapper1 = new QueryWrapper<>();
        QueryWrapper<TbUser> wrapper2 = Wrappers.query();
        LambdaQueryWrapper<TbUser> wrapper3 = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<TbUser> wrapper4 = Wrappers.lambdaQuery();
        LambdaQueryWrapper<TbUser> wrapper5 = Wrappers.lambdaQuery(TbUser.class);

        list = this.list();
        list = this.list(wrapper5);
        list = this.list(null);
        list = this.getBaseMapper().selectList(wrapper5);
        list = this.getBaseMapper().selectList(null);

        // 过滤姓名为刘伟伟[并且]邮箱为email@163.com[或者]电话为13812345678的渣男
        Set<String> unameSet = list.stream().map(TbUser::getUsername).collect(Collectors.toSet());
        Set<TbUser> emailSet = list.stream().filter(user -> user.getPermission().equalsIgnoreCase("email")).collect(Collectors.toSet());
        Set<TbUser> guestSet = list.stream().filter(user -> user.getPermission().equalsIgnoreCase("45678")).collect(Collectors.toSet());
        wrapper5.in(TbUser::getUsername, unameSet)
                .and(user -> user
                        .in(TbUser::getEmail, emailSet)
                        .or()
                        .in(TbUser::getPhone, guestSet));

        // org.apache.commons.collections4.CollectionUtils - 如果指定的集合不为空，则执行空安全检查。
        if (CollectionUtils.isNotEmpty(list)) {
            // com.google.common.collect.Lists - 返回一个列表的连续{List.subList(int, int) subList}，每个列表的大小相同(最后的列表可能更小)。
            List<List<TbUser>> partition = Lists.partition(list, HttpServletResponse.SC_OK);
            for (List<TbUser> users : partition) {
                for (TbUser tbUser : users) {
                    // com.alibaba.fastjson.JSONObject - 此方法将指定的对象序列化为其等效的Json表示形式。
                    String json = JSONObject.toJSONString(tbUser);
                    System.out.println("[object转json] - " + json);
                    // com.alibaba.fastjson.JSONObject - 此方法将指定的Json反序列化为指定类的对象。
                    TbUser object = JSONObject.parseObject(json, TbUser.class);
                    System.out.println("[json转object] - " + object.toString());
                    if (StringUtils.isNotEmpty(tbUser.getUsername())) {
                        System.out.println("");
                    }
                }
            }
        }
        // org.apache.commons.collections.CollectionUtils  - 不建议使用。ListUtils.partition(ist, size)不可用。
        // org.apache.commons.collections4.CollectionUtils - 如果指定的集合不为空，则执行空安全检查。
        if (CollectionUtils.isNotEmpty(list)) {
            // org.apache.commons.collections4.ListUtils - 返回一个列表的连续{List.subList(int, int) subList}，每个列表的大小相同(最后的列表可能更小)。
            List<List<TbUser>> partition = ListUtils.partition(list, HttpStatus.OK.value());
            for (List<TbUser> users : partition) {
                for (TbUser tbUser : users) {
                    System.out.println("collections和collections4区别" + tbUser.toString());
                }
            }
            for (TbUser tbUser : list) {
                // org.apache.commons.beanutils.BeanUtils - 返回指定bean为其提供读取方法的整个属性集。
                Map<String, String> map1 = BeanUtils.describe(tbUser);
                Map<String, Object> map2 = PropertyUtils.describe(tbUser);
                System.out.println("BeanUtils和PropertyUtils区别" + map2.get(0).toString());
            }
            List<Map<String, Object>> collect = list.stream().map(tbUser -> {
                try {
                    // org.apache.commons.beanutils.PropertyUtils - 返回指定bean为其提供读取方法的整个属性集。
                    Map<String, String> map1 = BeanUtils.describe(tbUser);
                    Map<String, Object> map2 = PropertyUtils.describe(tbUser);
                    return map2;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return null;
                }
            }).collect(Collectors.toList());
        }

        // 写入缓存 - 列表类型(list)用于存储一个有序的字符串列表List<String>。
        List<String> data = new ArrayList<>();
        for (TbUser user : list) {
            String json = JSONObject.toJSONString(user);
            data.add(json);
        }
        // 集合数组
        redisTemplate.opsForList().rightPush("abc", data);
        // 序列化集合对象
        redisTemplate.opsForList().rightPushAll("xyz", data.stream().collect(Collectors.toList()));
        // 设置过期时间60s
        redisTemplate.expire("abc", 60, TimeUnit.SECONDS);
        redisTemplate.expire("xyz", 60, TimeUnit.SECONDS);

        return list;
    }

    @Override
    public TbUser otherDetails(Serializable id) {
        return this.getById(id);
    }

    @Override
    public PageResult otherPage(PageRequest pageRequest) {
        PageResult pageResult = PageUtils.getPageResult(pageRequest, getPageInfo(pageRequest));
        return pageResult;
    }

    /**调用分页插件完成分页*/
    private PageInfo<TbUser> getPageInfo(PageRequest pageRequest) {
        QueryWrapper<TbUser> wrapper1 = new QueryWrapper<>();
        QueryWrapper<TbUser> wrapper2 = Wrappers.query();
        LambdaQueryWrapper<TbUser> wrapper3 = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<TbUser> wrapper4 = Wrappers.lambdaQuery();
        LambdaQueryWrapper<TbUser> wrapper5 = Wrappers.lambdaQuery(TbUser.class);

        PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());
        List<TbUser> list = new LinkedList<>();
        list = this.list();
        list = this.list(wrapper5);
        list = this.list(null);
        list = this.getBaseMapper().selectList(wrapper5);
        list = this.getBaseMapper().selectList(null);
        PageInfo<TbUser> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Resource
    private JdbcTemplate primaryJdbcTemplate;
    @Resource
    private JdbcTemplate secondaryJdbcTemplate;
    @Override
    public Integer otherUpdate(TbUser tbUser) {
        int index = 0;
        index += primaryJdbcTemplate.update("update tb_user set username = ? where id = ?", "李四A", 14);
        index += secondaryJdbcTemplate.update("update tb_user set username = ? where id = ?", "李四B", 14);
        return index;
    }
}
