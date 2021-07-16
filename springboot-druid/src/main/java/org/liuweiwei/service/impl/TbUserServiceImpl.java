package org.liuweiwei.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
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

    @Autowired
    private TbUserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public TbUser getOne(TbUser tbUser) {
        Long userId = tbUser.getId();

        tbUser = (TbUser) redisTemplate.opsForValue().get(userId);
        if (Objects.isNull(tbUser)) {
            // XML 配置写法
            // tbUser = userMapper.selectByUserId(userId);
            // MyBatis 内嵌脚本
            tbUser = userMapper.selectById(userId);
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
    public List<TbUser> getAll() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        QueryWrapper<TbUser> wrapper1 = new QueryWrapper<>();
        QueryWrapper<TbUser> wrapper2 = Wrappers.query();
        LambdaQueryWrapper<TbUser> wrapper3 = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<TbUser> wrapper4 = Wrappers.lambdaQuery();
        LambdaQueryWrapper<TbUser> wrapper5 = Wrappers.lambdaQuery(TbUser.class);

        List<TbUser> list = new LinkedList<>();
        list = this.list();
        list = this.list(wrapper5);
        list = this.list(null);
        list = this.getBaseMapper().selectList(wrapper5);
        list = this.getBaseMapper().selectList(null);

        Set<String> unameSet = list.stream().map(TbUser::getUsername).collect(Collectors.toSet());
        Set<TbUser> adminSet = list.stream().filter(user -> user.getPermission().equalsIgnoreCase("admin")).collect(Collectors.toSet());
        Set<TbUser> guestSet = list.stream().filter(user -> user.getPermission().equalsIgnoreCase("guest")).collect(Collectors.toSet());
        wrapper5.in(TbUser::getUsername, unameSet)
                .and(user -> user
                        .in(TbUser::getUsername, adminSet)
                        .or()
                        .in(TbUser::getUsername, guestSet));

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
        return list;
    }
}
