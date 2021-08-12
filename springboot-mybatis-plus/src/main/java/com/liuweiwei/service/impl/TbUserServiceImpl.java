package com.liuweiwei.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.liuweiwei.dao.TbUserMapper;
import com.liuweiwei.dto.TbUserDTO;
import com.liuweiwei.model.TbUser;
import com.liuweiwei.service.TbUserService;
import com.liuweiwei.vo.TbUserVO;
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
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Liuweiwei
 * @since 2021-01-06
 */
@Service
public class TbUserServiceImpl extends ServiceImpl<TbUserMapper, TbUser> implements TbUserService {

    private final Logger LOGGER = LogManager.getLogger(this.getClass());

    @Resource
    private TbUserMapper userMapper;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private RedisTemplate redisTemplate;

    protected QueryWrapper<TbUser>             wrapper01 = new QueryWrapper<>();
    protected QueryWrapper<TbUser>             wrapper02 = Wrappers.query();
    protected QueryWrapper<TbUser>             wrapper03 = Wrappers.<TbUser>query();
    protected LambdaQueryWrapper<TbUser>       wrapper04 = new LambdaQueryWrapper<>();
    protected LambdaQueryWrapper<TbUser>       wrapper05 = Wrappers.lambdaQuery();
    protected LambdaQueryWrapper<TbUser>       wrapper06 = Wrappers.<TbUser>lambdaQuery();
    protected LambdaQueryWrapper<TbUser>       wrapper07 = Wrappers.lambdaQuery(TbUser.class);
    protected QueryChainWrapper<TbUser>        wrapper08 = this.query();
    protected UpdateChainWrapper<TbUser>       wrapper09 = this.update();
    protected LambdaQueryChainWrapper<TbUser>  wrapper10 = this.lambdaQuery();
    protected LambdaUpdateChainWrapper<TbUser> wrapper11 = this.lambdaUpdate();
    protected TbUserMapper mapper = this.getBaseMapper();

    /**
     * TODO -> 顶级：IService - com.baomidou.mybatisplus.extension.service;
     * 1. 新增按钮
     * save(T entity);
     * saveBatch(Collection<T> entityList);
     * saveBatch(Collection<T> entityList, int batchSize);
     * saveOrUpdateBatch(Collection<T> entityList);
     * saveOrUpdateBatch(Collection<T> entityList, int batchSize);
     * saveOrUpdate(T entity);
     * saveOrUpdate(T entity, Wrapper<T> updateWrapper);
     * 2. 删除按钮
     * removeById(Serializable id);
     * removeByMap(Map<String, Object> columnMap);
     * remove(Wrapper<T> queryWrapper);
     * removeByIds(Collection<? extends Serializable> idList);
     * 3. 编辑按钮
     * updateById(T entity);
     * update(Wrapper<T> updateWrapper);
     * update(T entity, Wrapper<T> updateWrapper);
     * updateBatchById(Collection<T> entityList);
     * updateBatchById(Collection<T> entityList, int batchSize);
     * 4. 查询按钮
     * getById(Serializable id);
     * getOne(Wrapper<T> queryWrapper);
     * getOne(Wrapper<T> queryWrapper, boolean throwEx);
     * getMap(Wrapper<T> queryWrapper);
     * getObj(Wrapper<T> queryWrapper, Function<? super Object, V> mapper);
     * list();
     * list(Wrapper<T> queryWrapper);
     * listByIds(Collection<? extends Serializable> idList);
     * listByMap(Map<String, Object> columnMap);
     * listMaps(Wrapper<T> queryWrapper);
     * listMaps();
     * listObjs();
     * listObjs(Function<? super Object, V> mapper);
     * listObjs(Wrapper<T> queryWrapper);
     * listObjs(Wrapper<T> queryWrapper, Function<? super Object, V> mapper);
     * page(E page, Wrapper<T> queryWrapper);
     * page(E page);
     * pageMaps(E page, Wrapper<T> queryWrapper);
     * pageMaps(E page);
     * count();
     * count(Wrapper<T> queryWrapper);
     * 0. 包装器
     * query();
     * update();
     * lambdaQuery();
     * lambdaUpdate();
     * getBaseMapper();
     * TODO -> 基础：BaseMapper - com.baomidou.mybatisplus.core.mapper;
     * 1. 列表查询
     * selectList(wrapper09);
     * selectBatchIds(ids);
     * selectByMap(map);
     * selectMaps(wrapper10);
     * selectObjs(wrapper11);
     * 2. 分页查询
     * selectPage(new Page<>(current, size), wrapper7);
     * selectMapsPage(new Page<>(current, size), wrapper11);
     * 3. 统计查询
     * selectCount(wrapper08);
     * 4. 条件查询
     * selectById(id);
     * selectOne(wrapper6);
     */

    /**
     * TODO -> 包装器：Wrappers
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
     * @param id
     * @return
     */
    @Override
    public TbUser otherGetById(Serializable id) {
        List<Integer>       ids = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        TbUser  user = null;
        long current = 1L;
        long    size = 10L;

        /**列表查询 - 顶级IService*/
        List<TbUser>               list1 = this.list();
        List<TbUser>               list2 = this.list(wrapper01);
        List<TbUser>               list3 = this.listByIds(ids);
        List<TbUser>               list4 = this.listByMap(map);
        List<Map<String, Object>>  maps1 = this.listMaps();
        List<Object>               objs1 = this.listObjs();
        List<Map<String, Object>>  maps2 = this.listMaps(wrapper02);
        List<Object>               objs2 = this.listObjs(wrapper03);
        /**分页查询 - 顶级IService*/
        Page<TbUser>               page1 = this.page(new Page<>(current, size));
        Page<TbUser>               page2 = this.page(new Page<>(current, size), wrapper04);
        Page<Map<String, Object>> pages1 = this.pageMaps(new Page<>(current, size));
        Page<Map<String, Object>> pages2 = this.pageMaps(new Page<>(current, size), wrapper05);
        /**统计查询 - 顶级IService*/
        int                       count1 = this.count();
        int                       count2 = this.count(wrapper06);
        /**条件查询 - 顶级IService*/
        TbUser                     user1 = this.getById(id);
        TbUser                     user2 = this.getOne(wrapper07);
        Map<String, Object>        mapp3 = this.getMap(wrapper08);

        /**列表查询 - 基础BaseMapper*/
        List<TbUser>               list5 = this.getBaseMapper().selectList(wrapper09);
        List<TbUser>               list6 = this.getBaseMapper().selectBatchIds(ids);
        List<TbUser>               list7 = this.getBaseMapper().selectByMap(map);
        List<Map<String, Object>>  list8 = this.getBaseMapper().selectMaps(wrapper10);
        List<Object>               list9 = this.getBaseMapper().selectObjs(wrapper11);
        /**分页查询 - 基础BaseMapper*/
        Page<TbUser>               page3 = this.getBaseMapper().selectPage(new Page<>(current, size), wrapper11);
        Page<Map<String, Object>> pages3 = this.getBaseMapper().selectMapsPage(new Page<>(current, size), wrapper11);
        /**统计查询 - 基础BaseMapper*/
        Integer                   count3 = this.getBaseMapper().selectCount(wrapper11);
        /**条件查询 - 基础BaseMapper*/
        TbUser                     user3 = this.getBaseMapper().selectById(id);
        TbUser                     user4 = this.getBaseMapper().selectOne(wrapper11);

        if (org.springframework.util.StringUtils.isEmpty(id)) {
            LOGGER.error("查询ID不存在:{}", StringUtils.join(id));
        } else {
            user = this.getById(id);
        }
        if (Objects.nonNull(user)) {
            // TODO -> 用于简单(或Redis术语"string")值的Redis操作。
            Object object = redisTemplate.opsForValue().get(id);
            String data = object.toString();
            user = JSONObject.parseObject(data, TbUser.class);
        } else {
            LOGGER.error("查询缓存数据不存在:{}", JSONObject.toJSONString(user));
        }
        if (Objects.isNull(user)) {
            // TODO -> 1. XML 脚本文档格式
            user = userMapper.selectById(id);

            // TODO -> 2. MyBatis Plus [顶级 IService]内嵌脚本方式
            user = this.getById(id);
            user = this.getOne(wrapper11);

            // TODO -> 3. MyBatis [基础 BaseMapper]内嵌脚本方式
            user = this.getBaseMapper().selectById(id);
            user = this.getBaseMapper().selectOne(wrapper11);

            LOGGER.info("查询MySQL数据库:{}", StringUtils.join(id));
            String data = JSONObject.toJSONString(user);
            redisTemplate.opsForValue().set(id, data);
            redisTemplate.expire(id, 60L, TimeUnit.SECONDS);
            LOGGER.info("写入NoSQL数据库:{}", StringUtils.join(id));
        } else {
            LOGGER.error("查询磁盘数据不存在:{}", JSONObject.toJSONString(user));
        }
        return user;
    }

    @Override
    public TbUser otherGetOne(Wrapper<TbUser> queryWrapper) {
        TbUser user = queryWrapper.getEntity();
        Long id = null;
        if (Objects.nonNull(user)) {
            Object object = redisTemplate.opsForValue().get(id);
            String data = object.toString();
            user = JSONObject.parseObject(data, TbUser.class);
        }
        if (Objects.isNull(user)) {
            // 1. XML 脚本文档方式
            user = userMapper.selectById(id);

            // 2. MyBatis Plus [顶级 IService]内嵌脚本方式
            user = this.getById(id);
            user = this.getOne(wrapper11);

            // 3. MyBatis [基础 Mapper]内嵌脚本方式
            user = this.getBaseMapper().selectById(id);
            user = this.getBaseMapper().selectOne(wrapper11);

            LOGGER.info("查询MySQL数据库:{}", StringUtils.join(id));
            String data = JSONObject.toJSONString(user);
            redisTemplate.opsForValue().set(String.valueOf(id), data);
            redisTemplate.expire(String.valueOf(id), 60L, TimeUnit.SECONDS);
            LOGGER.info("写入NoSQL数据库:{}", StringUtils.join(id));
        }

        // MyBatis Plus <Wrapper>内嵌脚本方式
        QueryWrapper<TbUser> wrapper01 = new QueryWrapper<>();
        wrapper01.eq("name", "李湘").last("LIMIT 1");
        user = this.getBaseMapper().selectOne(wrapper01);

        QueryWrapper<TbUser> wrapper02 = Wrappers.query();
        wrapper02.eq("name", "李湘").last("LIMIT 1");
        user = this.getBaseMapper().selectOne(wrapper02);

        // MyBatis Plus <Wrappers>工具类内嵌脚本方式
        LambdaQueryWrapper<TbUser> wrapper03 = wrapper01.lambda();
        wrapper03.eq(TbUser::getUsername, "李湘").last("LIMIT 1");
        user = this.getBaseMapper().selectOne(wrapper03);

        LambdaQueryWrapper<TbUser> wrapper04 = Wrappers.lambdaQuery(TbUser.class);
        wrapper04.eq(TbUser::getUsername, "李湘").last("LIMIT 1");
        user = this.getBaseMapper().selectOne(wrapper04);

        LambdaQueryWrapper<TbUser> wrapper05 = Wrappers.<TbUser>lambdaQuery().eq(TbUser::getUsername, "李湘").last("LIMIT 1");
        user = this.getBaseMapper().selectOne(wrapper05);

        LambdaQueryWrapper<TbUser> wrapper06 = Wrappers.lambdaQuery(TbUser.class).eq(TbUser::getUsername, "李湘").last("LIMIT 1");
        user = this.getBaseMapper().selectOne(wrapper06);

        user = this.getBaseMapper().selectOne(Wrappers.<TbUser>query().eq("name", "李湘").last("LIMIT 1"));
        user = this.getBaseMapper().selectOne(Wrappers.<TbUser>lambdaQuery().eq(TbUser::getUsername, "李湘").last("LIMIT 1"));
        user = this.getBaseMapper().selectOne(Wrappers.lambdaQuery(TbUser.class).eq(TbUser::getUsername, "李湘").last("LIMIT 1"));

        return user;
    }

    @Override
    public Map<String, Object> otherGetMap(Wrapper<TbUser> queryWrapper) {
        Map<String, Object> map = this.getMap(queryWrapper);
        return map;
    }

    @Override
    public List<TbUser> otherList() throws Exception {
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
        list = this.list(wrapper11);
        list = this.list(null);
        list = this.getBaseMapper().selectList(wrapper11);
        list = this.getBaseMapper().selectList(null);

        // 过滤姓名为刘伟伟[并且]邮箱为email@163.com[或者]电话为13812345678的渣男
        Set<String> unameSet = list.stream().map(TbUser::getUsername).collect(Collectors.toSet());
        Set<TbUser> emailSet = list.stream().filter(user -> user.getPermission().equalsIgnoreCase("email")).collect(Collectors.toSet());
        Set<TbUser> guestSet = list.stream().filter(user -> user.getPermission().equalsIgnoreCase("45678")).collect(Collectors.toSet());
        wrapper11.in(TbUser::getUsername, unameSet)
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
    public List<TbUser> otherList(Wrapper<TbUser> queryWrapper) {
        List<TbUser> list = this.list(queryWrapper);
        return list;
    }

    @Override
    public List<TbUser> otherListByIds(List<Integer> ids) {
        List<TbUser> list = this.listByIds(ids);
        return list;
    }

    @Override
    public List<TbUser> otherListByMap(Map<String, Object> map) {
        List<TbUser> list = this.listByMap(map);
        return list;
    }

    @Override
    public List<Map<String, Object>> otherListMaps() {
        List<Map<String, Object>> maps = this.listMaps();
        return maps;
    }

    @Override
    public List<Object> otherListObjs() {
        List<Object> objs = this.listObjs();
        return objs;
    }

    @Override
    public List<Map<String, Object>> otherListMaps(Wrapper<TbUser> queryWrapper) {
        List<Map<String, Object>> maps = this.listMaps(queryWrapper);
        return maps;
    }

    @Override
    public List<Object> otherListObjs(Wrapper<TbUser> queryWrapper) {
        List<Object> objs = this.listObjs(queryWrapper);
        return objs;
    }

    @Override
    public Page<TbUserVO> otherPage(Page<TbUser> page) {
        /**字符串转日期格式*/
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Page<TbUser> pages = this.page(page);
        List<TbUser> list = pages.getRecords();
        List<TbUserVO> records = new ArrayList<>();
        list.stream().forEach(source -> {
            TbUserVO target = new TbUserVO();
            org.springframework.beans.BeanUtils.copyProperties(source, target);
            target.setId(String.valueOf(source.getId()));
            target.setCreated(format.format(source.getCreated()));
            target.setUpdated(format.format(source.getUpdated()));
            records.add(target);
        });

        Page<TbUserVO> data = new Page<>();
        data.setRecords(records);
        data.setTotal(pages.getTotal());
        data.setSize(pages.getSize());
        data.setCurrent(pages.getCurrent());
        data.setOrders(pages.getOrders());
        data.setOptimizeCountSql(pages.optimizeCountSql());
        data.setHitCount(pages.isHitCount());
        data.setCountId(pages.countId());
        data.setMaxLimit(pages.maxLimit());
        data.setSearchCount(pages.isSearchCount());
        data.setPages(pages.getPages());
        return data;
    }

    @Override
    public Page<TbUser> otherPage(Page<TbUser> page, Wrapper<TbUser> queryWrapper) {
        Page<TbUser> pages = this.page(page, queryWrapper);
        return pages;
    }

    @Override
    public Page<Map<String, Object>> otherPageMaps(Page<TbUser> page) {
        Page<Map<String, Object>> pageMaps = this.pageMaps(new Page<>(page.getCurrent(), page.getSize()));
        return pageMaps;
    }

    @Override
    public Page<Map<String, Object>> otherPageMaps(Page<TbUser> page, Wrapper<TbUser> queryWrapper) {
        Page<Map<String, Object>> pageMaps = this.pageMaps(new Page<>(page.getCurrent(), page.getSize()), queryWrapper);
        return pageMaps;
    }

    @Override
    public Integer otherCount() {
        int count = this.count();
        return count;
    }

    @Override
    public Integer otherCount(Wrapper<TbUser> queryWrapper) {
        int count = this.count(queryWrapper);
        return count;
    }

    @Override
    public Boolean otherSave(TbUserDTO source) throws ParseException {
        /**UUID生成纯数字*/
        Integer uuid = UUID.randomUUID().toString().hashCode();
        uuid = uuid < 0 ? -uuid : uuid;
        /**字符串转日期格式*/
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        TbUser target = new TbUser();
        org.springframework.beans.BeanUtils.copyProperties(source, target);
        target.setId(Long.parseLong(String.valueOf(uuid)));
        target.setCreated(format.parse(source.getCreated()));
        target.setUpdated(format.parse(source.getUpdated()));

        boolean flag = this.save(target);
        return flag;
    }

    @Resource
    private JdbcTemplate atomikosJdbcTemplate;

    /**
     * Spring-jta-Atomikos数据源+JDBC模板
     * @param user
     * @return
     */
    @Override
    public Integer jdbcUpdate(TbUser user) {
        int index = atomikosJdbcTemplate.update("update tb_user set username = ? where id = ?", "李四B", 14);
        return index;
    }
}
