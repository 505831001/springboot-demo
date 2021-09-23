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
package net.mingsoft.basic.cache;

import cn.hutool.core.collection.CollUtil;
import net.mingsoft.basic.util.SpringUtil;
import net.sf.ehcache.Ehcache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;

import java.util.List;

/**
 * 基础缓存,
 *
 * @author 铭飞团队
 * @version 版本号：<br/>
 * 创建日期：2016年6月2日<br/>
 * 历史修订：<br/>
 */
public abstract class BaseCache<T> {

    @Autowired
    protected CacheManager cacheManager;


    /**
     * 保存时候需要指定key
     * @param t
     * @return
     */
    public abstract T saveOrUpdate(String key,T t);

    public abstract void delete(String key);

    public abstract void deleteAll();

    public abstract T get(String key);

    public abstract void flush();
    public void flush(String cacheName) {
        Ehcache cache = (Ehcache) SpringUtil.getBean(CacheManager.class).getCache(cacheName).getNativeCache();
        cache.flush();
    }

    public abstract int count();

    public int count(String cacheName) {
        Ehcache cache = (Ehcache) SpringUtil.getBean(CacheManager.class).getCache(cacheName).getNativeCache();
        return cache.getSize();
    }

    public abstract List list();

    /**
     *
     * @param cacheName
     * @return
     */
    public List list(String cacheName) {
        Ehcache cache = (Ehcache) SpringUtil.getBean(CacheManager.class).getCache(cacheName).getNativeCache();
        List list = CollUtil.newArrayList();
        cache.getKeys().forEach(key ->
                list.add(cacheManager.getCache(cacheName).get(key).get())
        );
        return list;
    }

}
