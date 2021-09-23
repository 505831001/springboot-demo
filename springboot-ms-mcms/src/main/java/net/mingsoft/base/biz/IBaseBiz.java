/**
 * The MIT License (MIT) * Copyright (c) 2020 铭软科技(mingsoft.net)
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.mingsoft.base.biz;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.mingsoft.base.entity.BaseEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: BaseAction
 * @Description:TODO(基础接口，应用层可继承该类)
 * @author: 铭飞开发团队
 * @date: 2018年3月19日 下午3:28:27
 *
 * @Copyright: 2018 www.mingsoft.net Inc. All rights reserved.
 */

public interface IBaseBiz<T> extends IService<T> {

    /**
     * 添加记录
     *
     * @param table
     *            表名称
     * @param fields
     *            编号
     */
    void insertBySQL(String table, Map fields);

    /**
     * 动态sql查询
     *
     * @param table
     *            表名称
     * @param fields
     *            list集合
     * @param wheres
     *            条件 都是key-value对应
     * @return 返回list实体数组
     */
    List queryBySQL(String table, List<String> fields, Map wheres);

    /**
     * 动态sql查询
     *
     * @param table
     *            表名称
     * @param fields
     *            list集合
     * @param wheres
     *            条件 都是key-value对应
     * @param begin
     *            开始
     * @param end
     *            结束
     * @return 返回list实体数组
     */
    List queryBySQL(String table, List<String> fields, Map wheres, Integer begin, Integer end);

    /**
     * 动态sql查询
     *
     * @param table
     *            表名称
     * @param fields
     *            list集合
     * @param wheres
     *            条件 都是key-value对应
     * @param sqlWhereList
     *      前端高级搜索条件 示例：[{"action":"and","field":"qj_group","el":"like","model":"qjGroup","name":"任务组","type":"input","value":"1"}]
     * @return 返回list实体数组
     */
    List queryBySQL(String table, List<String> fields, Map wheres,List<Map> sqlWhereList);

    /**
     * 动态sql查询
     *
     * @param table
     *            表名称
     * @param fields
     *            list集合
     * @param wheres
     *            条件 都是key-value对应
     * @param sqlWhereList
     *      前端高级搜索条件 示例：[{"action":"and","field":"qj_group","el":"like","model":"qjGroup","name":"任务组","type":"input","value":"1"}]
     * @param begin
     *            开始
     * @param end
     *            结束
     * @return 返回list实体数组
     */
    List queryBySQL(String table, List<String> fields, Map wheres,List<Map> sqlWhereList, Integer begin, Integer end);

    /**
     * 动态sql查询
     *
     * @param table
     *            表名称
     * @param fields
     *            list集合
     * @param wheres
     *            条件 都是key-value对应
     * @param sqlWhereList
     *      前端高级搜索条件 示例：[{"action":"and","field":"qj_group","el":"like","model":"qjGroup","name":"任务组","type":"input","value":"1"}]
     * @param orderBy
     *            排序字段
     * @param order
     *            排序方式,asc;desc
     * @return 返回list实体数组
     */
    List queryBySQL(String table, List<String> fields, Map wheres,List<Map> sqlWhereList, String orderBy,String order);

    /**
     * 动态sql查询
     *
     * @param table
     *            表名称
     * @param fields
     *            list集合
     * @param wheres
     *            条件 都是key-value对应
     * @param sqlWhereList
     *      前端高级搜索条件 示例：[{"action":"and","field":"qj_group","el":"like","model":"qjGroup","name":"任务组","type":"input","value":"1"}]
     * @param begin
     *            开始
     * @param end
     *            结束
     * @param orderBy
     *            排序字段
     * @param order
     *            排序方式,asc;desc
     * @return 返回list实体数组
     */
    List queryBySQL(String table, List<String> fields, Map wheres,List<Map> sqlWhereList, Integer begin, Integer end,String orderBy,String order);

    /**
     * 查询表中记录总数
     *
     * @param table
     *            表名称
     * @param wheres
     *            条件 都是key-value对应
     * @return 返回查询总数
     */
    int countBySQL(String table, Map wheres);


    /**
     * 动态SQL删除
     *
     * @param table
     *            表名称
     * @param wheres
     *            條件 都是key-value对应
     */
    void deleteBySQL(String table, Map wheres);



    /**
     * 根据id集合实现批量的删除
     * 扩展雪花编号的id
     * @param ids
     *            id集合
     */
    void delete(String[] ids);


    /**
     * 根据id集合实现批量的删除
     *
     * @param ids
     *            id集合
     */
    void delete(int[] ids);


    /**
     * 动态SQL更新
     *
     * @param table
     *            表名称
     * @param fields
     *            list集合每个map都是key-value对应
     * @param wheres
     *            条件 都是key-value对应
     */
    void updateBySQL(String table, Map fields, Map wheres);




    /**
     * 创建表
     *
     * @param table
     *            表名称
     * @param fileds
     *            key:字段名称 list[0] 类型 list[1]长度 list[2]默认值 list[3]是否不填
     */
    void createTable(String table, Map<Object, List> fileds);


    /**
     * SQL修改表
     *
     * @param table
     *            表名称
     * @param fileds
     *            key:字段名称 list[0] 类型 list[1]长度 list[2]默认值 list[3]是否不填
     */
    void alterTable(String table,Map fileds,String type);


    /**
     * 删除表
     *
     * @param table
     *            表名称
     */
    void dropTable(String table);


    /**
     * 导入执行数据
     *
     * @param sql
     *            sql语句
     */
    Object excuteSql(String sql);






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
     * 根据id删除实体
     *
     * @param ene
     *            要删除的主键id
     */
    @Deprecated
    void deleteEntity(BaseEntity entity);

    /**
     * 根据id删除实体
     *
     * @param id
     *            要删除的主键id
     */
    @Deprecated
    void deleteEntity(int id);

    /**
     * 更具ID查询实体信息
     *
     * @param id
     *            实体ID
     * @return 返回实体
     */
    @Deprecated
    <E> E getEntity(BaseEntity entity);

    /**
     * 更具ID查询实体信息
     *
     * @param id
     *            实体ID
     * @return 返回实体
     */
    @Deprecated
    <E> BaseEntity getEntity(int id);



    /**
     * 查询，如果使用数据权限控制，推荐使用当前方法
     */
    List<T> query(BaseEntity entity);

    /**
     * 查询所有
     *
     * @return 返回list实体数组
     */
    @Deprecated
    List<T> queryAll();

    /**
     * 查询数据表中记录集合总数</br>
     * 可配合分页使用</br>
     *
     * @return 返回集合总数
     */
    @Deprecated
    int queryCount();

    /**
     * 批量新增
     *
     * @param list
     *            新增数据
     */
    @Deprecated
    void saveBatch(List list);

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
     */
    @Deprecated
    void updateEntity(BaseEntity entity);


}
