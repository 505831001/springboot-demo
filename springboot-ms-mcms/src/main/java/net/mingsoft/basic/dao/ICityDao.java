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

package net.mingsoft.basic.dao;

import net.mingsoft.base.dao.IBaseDao;
import net.mingsoft.basic.entity.CityEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 省市县镇村数据持久层
 * @author 伍晶晶
 * @version
 * 版本号：100<br/>
 * 创建日期：2017-7-27 14:47:29<br/>
 * 历史修订：<br/>
 */
public interface ICityDao extends IBaseDao {

    /**
     * 查询省／直辖市／自治区
     * @return 省／直辖市／自治区列表
     */
    public List<CityEntity> queryProvince();
    /**
     * 查询市
     * @param cityEntity 城市实体
     * city_id
     * city_name
     * @return 市级列表
     */
    public List<CityEntity> queryCity(CityEntity cityEntity);
    /**
     * 查询区／县
     * @param cityEntity
     * county_id
     * county_name
     * @return 区／县列表

     */
    public List<CityEntity> queryCounty(CityEntity cityEntity);
    /**
     * 查询街道／镇
     * @param cityEntity
     * town_id
     * town_name
     * @return 街道／镇列表
     */
    public List<CityEntity> queryTown(CityEntity cityEntity);
    /**
     * 查询村委会
     * @param cityEntity
     * village_id
     * village_name
     * @return 村委会列表
     */
    public List<CityEntity> queryVillage(CityEntity cityEntity);

    /**
     * 通过层级过滤城市数据，提高性能
     * @param level 默认3级获取到区数据
     * @return 列表
     */
    public List<CityEntity> queryByLevel(@Param("level")int level);
}

