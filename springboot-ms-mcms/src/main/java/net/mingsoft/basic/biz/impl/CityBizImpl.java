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


package net.mingsoft.basic.biz.impl;

import net.mingsoft.base.biz.impl.BaseBizImpl;
import net.mingsoft.base.dao.IBaseDao;
import net.mingsoft.basic.bean.CityBean;
import net.mingsoft.basic.biz.ICityBiz;
import net.mingsoft.basic.dao.ICityDao;
import net.mingsoft.basic.entity.CityEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 省市县镇村数据管理持久化层
 * @author 伍晶晶
 * @version
 * 版本号：100<br/>
 * 创建日期：2017-7-27 14:47:29<br/>
 * 历史修订：<br/>
 */
@Service("cityBizImpl")
@Transactional
public class CityBizImpl extends BaseBizImpl implements ICityBiz {


    @Autowired
    private ICityDao cityDao;


    @Override
    protected IBaseDao getDao() {
        // TODO Auto-generated method stub
        return cityDao;
    }
    @Override
    public List<CityEntity> queryProvince() {
        return cityDao.queryProvince();
    }
    @Override
    public List<CityEntity> queryCity(CityEntity cityEntity) {
        return cityDao.queryCity(cityEntity);
    }
    @Override
    public List<CityEntity> queryCounty(CityEntity cityEntity) {
        return cityDao.queryCounty(cityEntity);
    }
    @Override
    public List<CityEntity> queryTown(CityEntity cityEntity) {
        return cityDao.queryTown(cityEntity);
    }
    @Override
    public List<CityEntity> queryVillage(CityEntity cityEntity) {
        return cityDao.queryVillage(cityEntity);
    }
    @Override
    public List<CityBean> queryForTree(int level,String type) {
        List<CityEntity> cityList = cityDao.queryByLevel(level);
        Map<Long,String> province = new HashMap<>();
        Map<Long,Map<Long,String>> city = new HashMap<>();
        Map<Long,Map<Long,String>> county = new HashMap<>();
        Map<Long,Map<Long,String>> town = new HashMap<>();
        Map<Long,Map<Long,String>> village = new HashMap<>();
        for(CityEntity cityEntity : cityList){
            //组织省级／市级／县级／镇级／村级数据，并保存。
            if(level>=1){
                //组织省、自治区、直辖市的数据
                if(province.get(cityEntity.getProvinceId()) == null){
                    province.put(cityEntity.getProvinceId(), cityEntity.getProvinceName());
                }
                if(level>=2){
                    //组织市的数据
                    if(city.get(cityEntity.getProvinceId()) != null){		//如果当前map中已包含当前省级，那么最近向value中填充市级数据。
                        city.get(cityEntity.getProvinceId()).put(cityEntity.getCityId(), cityEntity.getCityName());
                    }else{		//否则，直接添加省级，并将当前市数据填充进value
                        Map<Long,String> tempCity = new HashMap<>();
                        tempCity.put(cityEntity.getCityId(), cityEntity.getCityName());
                        city.put(cityEntity.getProvinceId(), tempCity);
                    }
                    if(level>=3){
                        //组织县、区的数据
                        if(county.get(cityEntity.getCityId()) != null){
                            county.get(cityEntity.getCityId()).put(cityEntity.getCountyId(), cityEntity.getCountyName());
                        }else{
                            Map<Long,String> tempCounty = new HashMap<>();
                            tempCounty.put(cityEntity.getCountyId(), cityEntity.getCountyName());
                            county.put(cityEntity.getCityId(), tempCounty);
                        }
                        if(level>=4){
                            //组织镇、街道的数据
                            if(town.get(cityEntity.getCountyId()) != null){
                                town.get(cityEntity.getCountyId()).put(cityEntity.getTownId(), cityEntity.getTownName());
                            }else{
                                Map<Long,String> tempTown = new HashMap<>();
                                tempTown.put(cityEntity.getTownId(), cityEntity.getTownName());
                                town.put(cityEntity.getCountyId(), tempTown);
                            }
                            if(level>=5){
                                //组织村的数据
                                if(village.get(cityEntity.getTownId()) != null){
                                    village.get(cityEntity.getTownId()).put(cityEntity.getVillageId(), cityEntity.getVillageName());
                                }else{
                                    Map<Long,String> tempVillage = new HashMap<>();
                                    tempVillage.put(cityEntity.getVillageId(), cityEntity.getVillageName());
                                    village.put(cityEntity.getTownId(), tempVillage);
                                }
                            }
                        }
                    }

                }
            }

        }
        List<CityBean> cityBeanList = new ArrayList<>();
        if("tree".equals(type)){
            //数据组织返回格式
            if(level >= 1){//树类型
                //遍历省级数据，组织第一级
                for (Long provinceKey : province.keySet()) {
                    CityBean provinceBean = new CityBean();
                    String provinceName = province.get(provinceKey);
                    provinceBean.setId(provinceKey);
                    provinceBean.setName(provinceName);
                    provinceBean.setChildrensList(new ArrayList<CityBean>());
                    if(level >= 2){
                        //遍历市级数据，组织第二级
                        Map<Long,String> cityMap = city.get(provinceKey);
                        for(Long cityKey : cityMap.keySet()){
                            String cityName = cityMap.get(cityKey) ;
                            CityBean cityBean = new CityBean();
                            cityBean.setId(cityKey);
                            cityBean.setName(cityName);
                            cityBean.setChildrensList(new ArrayList<CityBean>());
                            if(level >= 3){
                                //遍历县级数据，组织第三极
                                Map<Long,String> countyMap = county.get(cityKey);
                                for(Long countyKey : countyMap.keySet()){
                                    String countyName = countyMap.get(countyKey);
                                    CityBean countyBean = new CityBean();
                                    countyBean.setId(countyKey);
                                    countyBean.setName(countyName);
                                    countyBean.setChildrensList(new ArrayList<CityBean>());
                                    if(level >= 4){
                                        //遍历镇数据，组织第四级
                                        Map<Long,String> townMap = town.get(countyKey);
                                        for(Long townKey : townMap.keySet()){
                                            String townName = townMap.get(townKey);
                                            CityBean townBean = new CityBean();
                                            townBean.setId(townKey);
                                            townBean.setName(townName);
                                            townBean.setChildrensList(new ArrayList<CityBean>());
                                            if(level >= 5){
                                                //遍历村数据，组织第五级
                                                Map<Long,String> villageMap = village.get(townKey);
                                                for(Long villageKey : villageMap.keySet()){
                                                    String villageName = villageMap.get(villageKey);
                                                    CityBean villageBean = new CityBean();
                                                    villageBean.setId(villageKey);
                                                    villageBean.setName(villageName);
                                                    townBean.getChildrensList().add(villageBean);
                                                }
                                            }
                                            countyBean.getChildrensList().add(townBean);
                                        }
                                    }
                                    cityBean.getChildrensList().add(countyBean);
                                }
                            }
                            provinceBean.getChildrensList().add(cityBean);
                        }
                    }
                    cityBeanList.add(provinceBean);
                }
            }
        }else{//行数据类型
            CityBean cityBean = new CityBean();
            //组织省的数据
            if(level>=1){
                for(Long provinceId : province.keySet()){
                    cityBean = new CityBean();
                    cityBean.setId(provinceId);
                    cityBean.setName(province.get(provinceId));
                    cityBeanList.add(cityBean);
                }
            }
            //组织市的数据，父级id为省id
            if(level>=2){
                for(Long provinceId : city.keySet()){
                    Map<Long,String> _city = city.get(provinceId) ;
                    for(Long cityId : _city.keySet()){
                        cityBean = new CityBean();
                        cityBean.setId(cityId);
                        cityBean.setName(_city.get(cityId));
                        cityBean.setParentId(provinceId);
                        cityBeanList.add(cityBean);
                    }
                }
            }
            //组织县的数据，父id为市id
            if(level>=3){
                for(Long cityId : county.keySet()){
                    Map<Long,String> _county = county.get(cityId) ;
                    for(Long countyId : _county.keySet()){
                        cityBean = new CityBean();
                        cityBean.setId(countyId);
                        cityBean.setName(_county.get(countyId));
                        cityBean.setParentId(cityId);
                        cityBeanList.add(cityBean);
                    }
                }
            }
            //组织镇的数据，父id为县id
            if(level>=4){
                for(Long countyId : town.keySet()){
                    Map<Long,String> _town = county.get(countyId) ;
                    for(Long townId : _town.keySet()){
                        cityBean = new CityBean();
                        cityBean.setId(townId);
                        cityBean.setName(_town.get(townId));
                        cityBean.setParentId(countyId);
                        cityBeanList.add(cityBean);
                    }
                }
            }
            //组织村的数据，父id为镇id
            if(level>=5){
                for(Long countyId : village.keySet()){
                    Map<Long,String> _village = county.get(countyId) ;
                    for(Long villageId : _village.keySet()){
                        cityBean = new CityBean();
                        cityBean.setId(villageId);
                        cityBean.setName(_village.get(villageId));
                        cityBean.setParentId(countyId);
                        cityBeanList.add(cityBean);
                    }
                }
            }
        }

        return cityBeanList;
    }
    @Override
    public List<CityEntity> queryByLevel(int level) {
        // TODO Auto-generated method stub
        return cityDao.queryByLevel(level);
    }

}
