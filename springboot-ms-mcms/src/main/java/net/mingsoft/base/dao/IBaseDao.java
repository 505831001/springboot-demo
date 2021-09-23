/**
 * The MIT License (MIT) * Copyright (c) 2020 铭软科技(mingsoft.net)

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

package net.mingsoft.base.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import net.mingsoft.base.entity.BaseEntity;

/**
 *
 * @ClassName:  IBaseDao
 * @Description:TODO(基础dao)
 * @author: 铭飞开发团队
 * @date:   2018年3月19日 下午3:34:58
 *
 * @param <E>
 * @Copyright: 2018 www.mingsoft.net Inc. All rights reserved.
 */
public interface IBaseDao<E> extends BaseMapper<E> {

    /**
     * SQL修改表
     *
     * @param table
     *            表名称
     * @param fileds
     *            key:字段名称 list[0] 类型 list[1]长度 list[2]默认值 list[3]是否不填
     */
    void alterTable(@Param("table") String table, @Param("fileds") Map fileds, @Param("type") String type);

    /**
     * SQL总数
     *
     * @param table
     *            表名称
     * @param wheres
     *            条件 都是key-value对应
     * @return 总数
     */
    int countBySQL(@Param("table") String table, @Param("wheres") Map wheres);

    /**
     * SQL创建表
     *
     * @param table
     *            表名称
     * @param fileds
     *            key:字段名称 list[0] 类型 list[1]长度 list[2]默认值 list[3]是否不填
     */
    void createTable(@Param("table") String table, @Param("fileds") Map<Object, List> fileds);

    /**
     * SQL动态SQL删除
     *
     * @param table
     *            表名称
     * @param wheres
     *            條件 都是key-value对应
     */

    void deleteBySQL(@Param("table") String table, @Param("wheres") Map wheres);

    /**
     * SQL删除表
     *
     * @param table
     *            表名称
     */
    void dropTable(@Param("table") String table);

    /**
     * SQL导入执行数据
     *
     * @param sql
     *            sql语句
     */
    @SqlParser(filter = true)

    List excuteSql(@Param("sql") String sql);

    /**
     * SQL添加记录
     *
     * @param table
     *            表名称
     * @param fields
     *            编号
     */
    void insertBySQL(@Param("table") String table, @Param("fields") Map fields);

    /**
     * SQL动态sql查询
     *
     * @param table
     *            表名称
     * @param fields
     *            list集合
     * @param wheres
     *            条件 都是key-value对应
     * @param begin
     *            开始位置
     * @param end
     *            结束位置
     * @param order
     *            排序方式,asc;desc
     * @return 返回查询结果
     */
    @SuppressWarnings("rawtypes")
    List queryBySQL(@Param("table") String table, @Param("fields") List<String> fields, @Param("wheres") Map wheres, @Param("sqlWhereList") List<Map> sqlWhereList,
                    @Param("begin") Integer begin, @Param("end") Integer end, @Param("orderBy") String orderBy, @Param("order") String order);

    /**
     * SQL动态SQL更新
     *
     * @param table
     *            表名称
     * @param fields
     *            list集合每个map都是key-value对应
     * @param wheres
     *            条件 都是key-value对应
     */
    void updateBySQL(@Param("table") String table, @Param("fields") Map fields, @Param("wheres") Map wheres);

    /**
     * 根据id集合实现批量的删除
     *
     * @param ids
     *            id集合
     */
    void delete(@Param("ids") int[] ids);

    /**
     * 根据id集合实现批量的删除
     *
     * @param ids
     *            id集合
     */
    void delete(@Param("ids") String[] ids);

    /**
     * 更新缓存
     * 使用场景：当前这个类存在数据缓存，使用了mybitsPlus的更新、保存等方法没有刷新数据缓存，
     * 调用该方法需要dao xml中实现一个更新方法
     * xml示例：
     * 	<update id="updateCache"  flushCache="true">
     * 		UPDATE table set del=0 where del=-1
     * 	</update>
     */
    void updateCache();

    /**
     * 根据id删除实体 推荐使用delete(int[] ids)
     *
     * @param id
     *            要删除的主键id
     */
    @Deprecated
    void deleteEntity(int id);

    /**
     * 通过entity条件删除对应entity
     * @param entity
     */
    @Deprecated
    void deleteByEntity(BaseEntity entity);

    /**
     * 根据ID查询实体信息
     *
     * @param id
     *            实体ID
     * @return 返回base实体
     */
    @Deprecated
    BaseEntity getEntity(Integer id);
    /**
     * 根据entity查询实体信息
     *
     * @param entity
     *            实体
     * @return 返回base实体
     */
    @Deprecated
    <E>E getByEntity(BaseEntity entity);

    /**
     * 查询
     */
    List<E> query(BaseEntity entity);

    /**
     * 查询所有
     *
     * @return 返回list数组
     */
    @Deprecated
    List<E> queryAll();

    /**
     * 分页查询,4.5.8版本之后推荐使用query方法查询
     *
     * @param pageNo
     *            页码
     * @param pageSize
     *            显示条数
     * @param orderBy
     *            排序字段
     * @param order
     *            order 排序方式,true:asc;fales:desc
     * @return 返回list数组
     */
    @Deprecated
    List<E> queryByPage(@Param("pageNo") int pageNo, @Param("pageSize") int pageSize,
                        @Param("orderBy") String orderBy, @Param("order") boolean order);

    /**
     * 查询数据表中记录集合总数
     *
     * @return 返回查询总条数
     */
    @Deprecated
    int queryCount();

    /**
     * 批量新增
     *
     * @param list
     *            新增数据
     *过期理由不适配oracle,请使用mybatis-plus的biz.saveBatch批量保存，注：mybatis-plus在dao层没有批量保存方法
     */
    @Deprecated
    void saveBatch(@Param("list") List list);

    /**
     * 保存
     *
     * @param entity
     *            实体
     * @return 返回保存后的id
     */
    @Deprecated
    int saveEntity(BaseEntity entity);

    /**
     * 更新实体
     *
     * @param entity
     *            实体
     */
    @Deprecated
    void updateEntity(BaseEntity entity);


}
