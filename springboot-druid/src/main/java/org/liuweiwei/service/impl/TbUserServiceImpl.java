package org.liuweiwei.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.liuweiwei.dao.TbUserMapper;
import org.liuweiwei.model.TbUser;
import org.liuweiwei.service.TbUserService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
    private TbUserMapper userMapper;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private RedisTemplate redisTemplate;

    protected QueryWrapper<TbUser> wrapper1 = new QueryWrapper<>();
    protected QueryWrapper<TbUser> wrapper2 = Wrappers.query();
    protected QueryWrapper<TbUser> wrapper3 = Wrappers.<TbUser>query();
    protected LambdaQueryWrapper<TbUser> wrapper4 = new LambdaQueryWrapper<>();
    protected LambdaQueryWrapper<TbUser> wrapper5 = Wrappers.lambdaQuery();
    protected LambdaQueryWrapper<TbUser> wrapper6 = Wrappers.<TbUser>lambdaQuery();
    protected LambdaQueryWrapper<TbUser> wrapper7 = Wrappers.lambdaQuery(TbUser.class);
    protected QueryChainWrapper<TbUser>        wrapper08 = this.query();
    protected UpdateChainWrapper<TbUser>       wrapper09 = this.update();
    protected LambdaQueryChainWrapper<TbUser>  wrapper10 = this.lambdaQuery();
    protected LambdaUpdateChainWrapper<TbUser> wrapper11 = this.lambdaUpdate();
    protected TbUserMapper mapper = this.getBaseMapper();

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
        Long id = tbUser.getId();

        if (Objects.nonNull(tbUser)) {
            // TODO -> 用于简单(或Redis术语"string")值的Redis操作。
            Object object = redisTemplate.opsForValue().get(id);
            String data = object.toString();
            tbUser = JSONObject.parseObject(data, TbUser.class);
        }
        if (Objects.isNull(tbUser)) {
            // TODO -> 1. XML 脚本文档格式
            tbUser = userMapper.selectById(id);

            // TODO -> 2. MyBatis Plus [顶级 IService]内嵌脚本方式
            tbUser = this.getById(id);
            tbUser = this.getOne(wrapper1);

            // TODO -> 3. MyBatis [基础 BaseMapper]内嵌脚本方式
            tbUser = this.getBaseMapper().selectById(id);
            tbUser = this.getBaseMapper().selectOne(wrapper2);

            logger.info("查询MySQL数据库:{}", id);
            String data = JSONObject.toJSONString(tbUser);
            redisTemplate.opsForValue().set(id, data);
            redisTemplate.expire(id, 60L, TimeUnit.SECONDS);
            logger.info("写入NoSQL数据库:{}", id);
        }
        return tbUser;
    }

    @Override
    public List<TbUser> otherAll() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        List<TbUser> list = new LinkedList<>();
        if (org.springframework.util.CollectionUtils.isEmpty(list)) {
            // TODO -> Redis列出特定的操作。集合数组2次遍历.opsForList()
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
            // TODO -> Redis列出特定的操作。序列化集合对象1次遍历.opsForList()
            List<Object> xyz = redisTemplate.opsForList().range("xyz", 0, -1);
            for (Object object : xyz) {
                String string = object.toString();
                TbUser user = JSONObject.parseObject(string, TbUser.class);
                list.add(user);
            }
        }

        list = this.list();
        list = this.list(wrapper3);
        list = this.list(null);
        list = this.getBaseMapper().selectList(wrapper4);
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
            // TODO -> com.google.common.collect.Lists - 返回一个列表的连续{List.subList(int, int) subList}，每个列表的大小相同(最后的列表可能更小)。
            List<List<TbUser>> partition = Lists.partition(list, HttpServletResponse.SC_OK);
            for (List<TbUser> users : partition) {
                for (TbUser tbUser : users) {
                    // com.alibaba.fastjson.JSONObject - 此方法将指定的对象序列化为其等效的Json表示形式。
                    String json = JSONObject.toJSONString(tbUser);
                    System.out.println("[object转json] - " + json);
                    // TODO -> com.alibaba.fastjson.JSONObject - 此方法将指定的Json反序列化为指定类的对象。
                    TbUser object = JSONObject.parseObject(json, TbUser.class);
                    System.out.println("[json转object] - " + object.toString());
                    if (StringUtils.isNotEmpty(tbUser.getUsername())) {
                        System.out.println("");
                    }
                }
            }
        }
        // org.apache.commons.collections.CollectionUtils  - 不建议使用。ListUtils.partition(ist, size)不可用。
        // TODO -> org.apache.commons.collections4.CollectionUtils - 如果指定的集合不为空，则执行空安全检查。
        if (CollectionUtils.isNotEmpty(list)) {
            // TODO -> org.apache.commons.collections4.ListUtils - 返回一个列表的连续{List.subList(int, int) subList}，每个列表的大小相同(最后的列表可能更小)。
            List<List<TbUser>> partition = ListUtils.partition(list, HttpStatus.OK.value());
            for (List<TbUser> users : partition) {
                for (TbUser tbUser : users) {
                    System.out.println("collections和collections4区别" + tbUser.toString());
                }
            }
            for (TbUser tbUser : list) {
                // TODO -> org.apache.commons.beanutils.BeanUtils - 返回指定bean为其提供读取方法的整个属性集。
                Map<String, String> map1 = BeanUtils.describe(tbUser);
                Map<String, Object> map2 = PropertyUtils.describe(tbUser);
                System.out.println("BeanUtils和PropertyUtils区别" + map2.get(0).toString());
            }
            List<Map<String, Object>> collect = list.stream().map(tbUser -> {
                try {
                    // TODO -> org.apache.commons.beanutils.PropertyUtils - 返回指定bean为其提供读取方法的整个属性集。
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
        // TODO -> 集合数组
        redisTemplate.opsForList().rightPush("abc", data);
        // TODO -> 序列化集合对象
        redisTemplate.opsForList().rightPushAll("xyz", data.stream().collect(Collectors.toList()));
        // 设置过期时间60s
        redisTemplate.expire("abc", 60, TimeUnit.SECONDS);
        redisTemplate.expire("xyz", 60, TimeUnit.SECONDS);

        return list;
    }

    @Override
    public TbUser otherDetails(Serializable id) {
        wrapper08.eq("id", id);
        wrapper09.eq("id", id);
        wrapper10.eq(TbUser::getId, id);
        wrapper11.eq(TbUser::getId, id);
        mapper.selectList(null);
        return this.getById(id);
    }
}
