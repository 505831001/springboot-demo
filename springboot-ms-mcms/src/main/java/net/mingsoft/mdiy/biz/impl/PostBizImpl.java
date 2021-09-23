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
package net.mingsoft.mdiy.biz.impl;

import cn.hutool.core.util.ObjectUtil;
import net.mingsoft.base.biz.impl.BaseBizImpl;
import net.mingsoft.base.dao.IBaseDao;
import net.mingsoft.basic.util.BasicUtil;
import net.mingsoft.mdiy.biz.IModelBiz;
import net.mingsoft.mdiy.biz.IPostBiz;
import net.mingsoft.mdiy.dao.IModelDao;
import net.mingsoft.mdiy.entity.ModelEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义表单接口实现类
 * @author 王天培QQ:78750478
 * @version
 * 版本号：100-000-000<br/>
 * 创建日期：2012-03-15<br/>
 * 历史修订：<br/>
 */
@Service
public class PostBizImpl extends BaseBizImpl implements IPostBiz {

    @Autowired
    private IModelBiz modelBiz;
    /**
     * 注入自定义表单持久化层
     */
    @Autowired
    private IModelDao modelDao;



    /**
     * 获取类别持久化层
     * @return diyFormDao 返回类别持久话层
     */
    @Override
    protected IBaseDao getDao() {
        // TODO Auto-generated method stub
        return modelDao;
    }

    @Override
    public void saveDiyFormData(int formId, Map<String,Object> params) {
        ModelEntity model = (ModelEntity) modelBiz.getEntity(formId);
        if (ObjectUtil.isNotNull(model) ) {
            Map fieldMap = model.getFieldMap();
            HashMap<String, Object> fields = new HashMap<>();
            //拼接字段
            for (String s : params.keySet()) {
                //判断是否存在此字段
                if (fieldMap.containsKey(s)) {
                    fields.put(fieldMap.get(s).toString(), params.get(s));
                }
            }
            modelBiz.insertBySQL(model.getModelTableName(), fields);
        }
    }

    @Override
    public List queryDiyFormData(int formId,Map<String,Object> params) {
        ModelEntity model = (ModelEntity) modelBiz.getEntity(formId);
        if (ObjectUtil.isNotNull(model) ) {
            Map fieldMap = model.getFieldMap();
            HashMap<String, Object> fields = new HashMap<>();
            //拼接字段
            for (String s : params.keySet()) {
                //判断是否存在此字段
                if (fieldMap.containsKey(s)) {
                    fields.put(fieldMap.get(s).toString(), "'"+params.get(s)+"'");
                }
            }
            //TODO 分页插件在这使用会报错
            BasicUtil.startPage();
            List list = modelBiz.queryBySQL(model.getModelTableName(), null, fields);
            return list;
        }
        return null;
    }

    @Override
    public void deleteQueryDiyFormData(int id,String diyFormId) {
        ModelEntity model = (ModelEntity) modelBiz.getEntity(Integer.parseInt(diyFormId));
        if (ObjectUtil.isNotNull(model) ) {
            HashMap hashMap = new HashMap();
            hashMap.put("id",id);
            modelBiz.deleteBySQL(model.getModelTableName(), hashMap);
        }
    }

    @Override
    public int countDiyFormData(int diyFormId,Map<String,Object> params) {
        ModelEntity model = (ModelEntity) modelBiz.getEntity(diyFormId);
        if (ObjectUtil.isNotNull(model) ) {
            HashMap fields = new HashMap();
            Map fieldMap = model.getFieldMap();
            for (String s : params.keySet()) {
                //判断是否存在此字段
                if (fieldMap.containsKey(s)) {
                    fields.put(fieldMap.get(s).toString(), "'"+params.get(s)+"'");
                }
            }
            return  modelBiz.countBySQL(model.getModelTableName(), fields);
        }
        return 0;
    }

}
