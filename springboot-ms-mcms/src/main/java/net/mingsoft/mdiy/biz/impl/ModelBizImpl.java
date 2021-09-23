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

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.mingsoft.base.biz.impl.BaseBizImpl;
import net.mingsoft.base.dao.IBaseDao;
import net.mingsoft.mdiy.bean.ModelJsonBean;
import net.mingsoft.mdiy.biz.IModelBiz;
import net.mingsoft.mdiy.dao.IModelDao;
import net.mingsoft.mdiy.entity.ModelEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 自定义表单接口实现类
 * @author 王天培QQ:78750478
 * @version
 * 版本号：100-000-000<br/>
 * 创建日期：2012-03-15<br/>
 * 历史修订：<br/>
 */
@Service("mdiyModelBizImpl")
@Transactional(rollbackFor = Exception.class)
public class ModelBizImpl extends BaseBizImpl<IModelDao,ModelEntity> implements IModelBiz {

    /**
     * 注入自定义表单持久化层
     */
    @Autowired
    private IModelDao modelDao;


    @Override
    protected IBaseDao getDao() {
        // TODO Auto-generated method stub
        return modelDao;
    }

    @Override
    public boolean importConfig(String customType,ModelJsonBean modelJsonBean) {
        if (StringUtils.isEmpty(customType) || modelJsonBean==null) {
            return  false;
        }
        return this.importModel(customType, modelJsonBean, "");
    }


    @Override
    public boolean importModel(String customType, ModelJsonBean modelJsonBean, String modelType) {
        if (StringUtils.isEmpty(customType) || modelJsonBean==null) {
            return  false;
        }
        ModelEntity model=new ModelEntity();
        model.setModelName(modelJsonBean.getTitle());
        model.setModelCustomType(customType);
        ModelEntity modelEntity = super.getOne(new QueryWrapper<>(model));
        //判断表名是否存在
        if(ObjectUtil.isNotNull(modelEntity)){
            return false;
        }
        switch (customType) {
            case "config":
                break;
            case "model":
                //在表名前面拼接前缀
                model.setModelTableName("mdiy_model_" + modelJsonBean.getTableName());
                //创建表
                modelDao.excuteSql(modelJsonBean.getSql().replace("{model}","mdiy_model_"));
                break;
            case "post":
                //在表名前面拼接前缀
                model.setModelTableName("mdiy_form_" + modelJsonBean.getTableName());
                //创建表
                modelDao.excuteSql(modelJsonBean.getSql().replace("{model}","mdiy_form_"));
                break;
            default:
                break;
        }
        Map json= new HashMap();
        json.put("html",modelJsonBean.getHtml());
        json.put("searchJson",modelJsonBean.getSearchJson());
        json.put("script",modelJsonBean.getScript());
        json.put("isWebSubmit",modelJsonBean.isWebSubmit());
        model.setModelField(modelJsonBean.getField());
        model.setModelType(modelType);
        model.setModelJson(JSONObject.toJSONString(json));
        //保存自定义模型实体
        super.save(model);
        return true;
    }

    @Override
    public boolean updateConfig(String modelId,ModelJsonBean modelJsonBean) {
        if (StringUtils.isEmpty(modelId) || modelJsonBean==null) {
            return  false;
        }
        return this.updateConfig(modelId, modelJsonBean, "");
    }

    @Override
    public boolean updateConfig(String modelId,ModelJsonBean modelJsonBean,String modelType) {
        if (StringUtils.isEmpty(modelId) || modelJsonBean==null) {
            return  false;
        }
        ModelEntity modelEntity = super.getById(modelId);
        if(ObjectUtil.isNull(modelEntity)){
            return false;
        }
        ModelEntity model=new ModelEntity();
        model.setModelName(modelJsonBean.getTitle());
        model.setModelCustomType(modelEntity.getModelCustomType());
        ModelEntity oldModel = super.getOne(new QueryWrapper<>(model));
        //判断表名是否存在
        if(ObjectUtil.isNotNull(oldModel) && !modelEntity.getId().equals(oldModel.getId())){
            return false;
        }
        // 原始表名
        String oldTableName = modelEntity.getModelTableName();
        // 表名重命名
        String rename = "ALTER  TABLE {} RENAME TO {};";
        switch (modelEntity.getModelCustomType()) {
            case "config":
                break;
            case "model":
                //在表名前面拼接前缀
                modelEntity.setModelTableName("mdiy_model_" + modelJsonBean.getTableName());
                updateTable(modelEntity.getModelField(),modelJsonBean.getField(),modelEntity.getModelTableName());

                if(!oldTableName.equals(modelEntity.getModelTableName())){
                    rename = StrUtil.format(rename,oldTableName,modelEntity.getModelTableName());
                    modelDao.excuteSql(rename);
                }
                break;
            case "post":
                //在表名前面拼接前缀
                modelEntity.setModelTableName("mdiy_form_" + modelJsonBean.getTableName());
                updateTable(modelEntity.getModelField(),modelJsonBean.getField(),modelEntity.getModelTableName());
                if(!oldTableName.equals(modelEntity.getModelTableName())){
                    rename = StrUtil.format(rename,oldTableName,modelEntity.getModelTableName());
                    modelDao.excuteSql(rename);
                }
                break;
            default:
                break;
        }
        // 更新业务信息
        Map json= new HashMap();
        json.put("html",modelJsonBean.getHtml());
        json.put("script",modelJsonBean.getScript());
        json.put("searchJson",modelJsonBean.getSearchJson());
        json.put("isWebSubmit",modelJsonBean.isWebSubmit());
        modelEntity.setModelField(modelJsonBean.getField());
        modelEntity.setModelName(modelJsonBean.getTitle());
        modelEntity.setModelType(modelType);
        modelEntity.setModelJson(JSONObject.toJSONString(json));
        //保存自定义模型实体
        super.updateById(modelEntity);
        return true;
    }

    @Override
    public boolean delete(List<String> ids) {
        for(String id : ids){
            ModelEntity modelEntity = super.getById(id);
            boolean flag = super.removeById(id);
            if(!flag){
                LOG.debug("{}删除失败",modelEntity.getModelTableName());
                break;
            }else {
                modelDao.dropTable(modelEntity.getModelTableName());
            }
        }
        return true;
    }


    private void updateTable (String oldStr,String newStr,String tableName){
        List<Dict> oldList = JSONObject.parseArray(oldStr, Dict.class);
        List<Dict> newList = JSONObject.parseArray(newStr, Dict.class);
        StringBuffer stringBuffer = new StringBuffer();
        // 获取两个集合的差集
        Collection<Dict> disMap = CollUtil.disjunction(oldList, newList);
        if (CollUtil.isNotEmpty(disMap)) {
            // 旧字符串中删除和修改的集合
            Collection<Dict> oldIntersection = CollUtil.intersection(oldList, disMap);
            String alertTable = "ALTER TABLE `{}` ";
            alertTable = StrUtil.format(alertTable,tableName);
            stringBuffer.append(alertTable);
            if (CollUtil.isNotEmpty(oldIntersection)) {
                String dropSql =  "DROP COLUMN `{}`";
                List<String> dropList = oldIntersection.stream().map(dict -> StrUtil.format(dropSql, dict.getStr("key"))).collect(Collectors.toList());
                stringBuffer.append(CollUtil.join(dropList,","));
            }
            // 新字符串中新增和修改的集合
            Collection<Dict> newIntersection = CollUtil.intersection(newList, disMap);
            if (CollUtil.isNotEmpty(newIntersection)) {
                // 拼接drop和add 中间的分隔符
                if (CollUtil.isNotEmpty(oldIntersection)) {
                    stringBuffer.append(",");
                }
                String addSql = "ADD COLUMN `{}` {}(255) NULL COMMENT '{}'";
                List<String> addList = newIntersection.stream().map(dict -> StrUtil.format(addSql, dict.getStr("key"),dict.getStr("jdbcType"),dict.getStr("name"))).collect(Collectors.toList());
                stringBuffer.append(CollUtil.join(addList,","));
            }
            stringBuffer.append(";");
            LOG.debug("执行的SQL{}",stringBuffer.toString());
            modelDao.excuteSql(stringBuffer.toString());
        }
    }
}
