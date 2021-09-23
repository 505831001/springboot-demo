/**
 * The MIT License (MIT)
 * Copyright (c) 2021 铭软科技(mingsoft.net)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.mingsoft.basic.util;

import org.springframework.data.redis.core.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 *
 * ms-basic 通用 redis操作类
 *
 * @author 铭飞开发团队
 * @version 版本号：100-000-000<br/>
 *          创建日期：2017年9月6日<br/>
 *          历史修订：<br/>
 */
public class RedisUtil {

    private static RedisTemplate redis;


    // ---------------------------------------------------------------------
    // redisTemplate
    // ---------------------------------------------------------------------
    private static RedisTemplate getRedisTemplate() {
        if(redis==null) {
            redis = SpringUtil.getBean(StringRedisTemplate.class);
        }
        return redis;
    }

    /**
     * 判断key是否存在
     *
     * @param key
     */
    public static boolean hasKey(String key) {
        return RedisUtil.getRedisTemplate().hasKey(key);
    }

    /**
     * 删除key
     *
     * @param key
     */
    public static void delete(String key) {
        RedisUtil.getRedisTemplate().delete(key);
    }

    /**
     * 判断指定key的hashKey是否存在
     *
     * @param key
     * @param hashKey
     * @return
     */
    public static boolean hasKey(String key, String hashKey) {
        return RedisUtil.getRedisTemplate().opsForHash().hasKey(key, hashKey);
    }

    /**
     * 设置超时时间
     *
     * @param key
     * @param timeout
     * @param unit
     */
    public static void expire(String key, final long timeout, final TimeUnit unit) {
        RedisUtil.getRedisTemplate().expire(key, timeout, unit);
    }

    /**
     * 获取过期时间
     *
     * @param key
     * @return
     */
    public static long ttl(String key) {
        return RedisUtil.getRedisTemplate().getExpire(key);
    }

    /**
     * 获取指定pattern的key
     *
     * @param pattern
     * @return
     */
    public static Set<String> keys(String pattern) {
        return RedisUtil.getRedisTemplate().keys(pattern);
    }

    /**
     * 删除多个key
     *
     * @param keys
     */
    public static void delete(Set<String> keys) {
        RedisUtil.getRedisTemplate().delete(keys);
    }

    /**
     * 设置过期时间
     *
     * @param key
     * @param expire
     */
    private static void setExpire(String key, long expire) {
        if (expire != -1) {
            RedisUtil.getRedisTemplate().expire(key, expire, TimeUnit.SECONDS);
        }
    }

    // ---------------------------------------------------------------------
    // ValueOperations -> Redis String/Value 操作
    // ---------------------------------------------------------------------

    private static ValueOperations getValueOperations() {
        return RedisUtil.getRedisTemplate().opsForValue();
    }

    /**
     * 设置key-value值
     */
    public static void addValue(String key, Object value, long expire) {
        RedisUtil.getValueOperations().set(key, value);
        setExpire(key, expire);
    }

    /**
     * 设置key-value值,传入时间单位
     */
    public static void addValue(String key, Object value, long expire, TimeUnit timeUnit) {
        RedisUtil.getValueOperations().set(key, value, expire, timeUnit);

    }

    /**
     * 设置key-value值, 无过期时间
     */
    public static void addValue(String key, Object value) {
        RedisUtil.getValueOperations().set(key, value);
    }

    /**
     * 获取key的值
     *
     */
    public static Object getValue(String key) {
        return RedisUtil.getValueOperations().get(key);
    }

    /**
     * 获取key值
     *
     * @param key
     *            值
     * @return 字符串
     */
    public static String get(String key) {
        Object obj = RedisUtil.getValueOperations().get(key);
        return obj==null?null:obj.toString();
    }

    private static HashOperations getHashOperations() {
        return RedisUtil.getRedisTemplate().opsForHash();
    }
    // ---------------------------------------------------------------------
    // HashOperations -> Redis Redis Hash 操作
    // ---------------------------------------------------------------------

    /**
     * 向redis 中添加内容
     *
     * @param key
     *            保存key
     * @param hashKey
     *            hashKey
     * @param data
     *            保存对象 data
     * @param expire
     *            过期时间 -1：表示不过期
     */
    public static void addHashValue(String key, String hashKey, Object data, long expire) {
        RedisUtil.getHashOperations().put(key, hashKey, data);
        setExpire(key, expire);
    }

    /**
     * Hash 添加数据
     *
     * @param key
     *            key
     * @param map
     *            data
     */
    public static void addAllHashValue(String key, Map<String, Object> map, long expire) {
        RedisUtil.getHashOperations().putAll(key, map);

        setExpire(key, expire);
    }

    /**
     * 删除hash key
     *
     * @param key
     *            key
     * @param hashKey
     *            hashKey
     */
    public static long deleteHashValue(String key, String hashKey) {
        return RedisUtil.getHashOperations().delete(key, hashKey);
    }

    /**
     * 获取数据
     */
    public static Object getHashValue(String key, String hashKey) {
        return RedisUtil.getHashOperations().get(key, hashKey);
    }

    /**
     * 批量获取数据
     */
    public static List<Object> getHashAllValue(String key) {
        return RedisUtil.getHashOperations().values(key);
    }

    /**
     * 批量获取指定hashKey的数据
     */
    public static List<Object> getHashMultiValue(String key, List<String> hashKeys) {
        return RedisUtil.getHashOperations().multiGet(key, hashKeys);
    }

    /**
     * 获取hash数量
     */
    public static Long getHashCount(String key) {
        return RedisUtil.getHashOperations().size(key);
    }

    // ---------------------------------------------------------------------
    // ZSetOperations -> Redis Sort Set 操作
    // ---------------------------------------------------------------------
    private static ZSetOperations getZSetOperations() {
        return RedisUtil.getRedisTemplate().opsForZSet();
    }

    /**
     * 设置zset值
     */
    public static boolean addZSetValue(String key, Object member, long score) {
        return RedisUtil.getZSetOperations().add(key, member, score);
    }

    /**
     * 设置zset值
     */
    public static boolean addZSetValue(String key, Object member, double score) {
        return RedisUtil.getZSetOperations().add(key, member, score);
    }

    /**
     * 批量设置zset值
     */
    public static long addBatchZSetValue(String key, Set<ZSetOperations.TypedTuple<Object>> tuples) {
        return RedisUtil.getZSetOperations().add(key, tuples);
    }

    /**
     * 自增zset值
     */
    public static void incZSetValue(String key, String member, long delta) {
        RedisUtil.getZSetOperations().incrementScore(key, member, delta);
    }

    /**
     * 获取zset数量
     */
    public static long getZSetScore(String key, String member) {
        Double score = RedisUtil.getZSetOperations().score(key, member);
        if (score == null) {
            return 0;
        } else {
            return score.longValue();
        }
    }

    /**
     * 获取有序集 key 中成员 member 的排名 。其中有序集成员按 score 值递减 (从小到大) 排序。
     */
    public static Set<ZSetOperations.TypedTuple<Object>> getZSetRank(String key, long start, long end) {
        return RedisUtil.getZSetOperations().rangeWithScores(key, start, end);
    }

    // ---------------------------------------------------------------------
    // listOperations -> Redis List() 操作
    // ---------------------------------------------------------------------
    private static ListOperations getListOperations() {
        return RedisUtil.getRedisTemplate().opsForList();
    }

    /**
     * 添加list列表
     */
    public static void addListValue(String key, Object list) {
        RedisUtil.getListOperations().leftPush(key, list);
    }

    /**
     * 获取指定Key对应的list
     */
    public static Object getListValue(String key) {
        return RedisUtil.getListOperations().leftPop(key);
    }

    private static SetOperations getSetOperations() {
        return RedisUtil.getRedisTemplate().opsForSet();
    }

    /**
     * 添加Set集合集合
     */
    public static void addSetValue(String key, Object list) {
        RedisUtil.getSetOperations().add(key, list);
    }

    /**
     * 获取指定Key对应的set
     */
    public static Object getSetValue(String key) {
        return RedisUtil.getSetOperations().members(key);
    }

    /**
     * 获取并移除指定key的值
     */
    public static Object popSetValue(String key) {
        return RedisUtil.getSetOperations().pop(key);
    }

}
