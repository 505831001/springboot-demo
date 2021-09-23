package net.mingsoft.mdiy.util;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.mingsoft.basic.util.SpringUtil;
import net.mingsoft.mdiy.biz.IConfigBiz;
import net.mingsoft.mdiy.entity.ConfigEntity;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义配置参数获取
 */
public class ConfigUtil {

    /**
     * 返回字符串类型的数据
     * @param configName 配置名称  对应自定义配置列表上的 配置名称 字段
     * @param key 对应代码生成器中的字段名称 注意：名称是驼峰式
     * @return 无匹配返回空
     */
    public static String getString(String configName,String key) {
        Object object = getObject(configName, key);
        if(object==null){
            return "";
        }
        return object.toString();
    }

    /**
     * 返回字符串类型的数据
     * @param configName 配置名称  对应自定义配置列表上的 配置名称 字段
     * @param key 对应代码生成器中的字段名称 注意：名称是驼峰式
     * @param defaultValue 默认值,如果配置中没有值，会返回默认值
     * @return 无匹配返回默认值
     */
    public static String getString(String configName,String key, String defaultValue) {
        Object object = getObject(configName, key);
        if(object==null){
            return defaultValue;
        }
        return object.toString();
    }



    /**
     * 返回整型类型的数据
     * @param configName 配置名称  对应自定义配置列表上的 配置名称 字段
     * @param key 对应代码生成器中的字段名称 注意：名称是驼峰式
     * @return 无匹配返回0
     */
    public static int getInt(String configName,String key) {
        Object object = getObject(configName, key);
        if(object==null){
            return 0;
        }
        return Integer.parseInt(object.toString());
    }

    /**
     * 返回整型类型的数据
     * @param configName 配置名称  对应自定义配置列表上的 配置名称 字段
     * @param key 对应代码生成器中的字段名称 注意：名称是驼峰式
     * @param defaultValue 默认值,如果配置中没有值，会返回默认值
     * @return 无匹配返回默认值
     */
    public static int getInt(String configName,String key,int defaultValue) {
        Object object = getObject(configName, key);
        if(object==null){
            return defaultValue;
        }
        return Integer.parseInt(object.toString());
    }

    /**
     * 如果不确定返回类型，可以使用 getObject
     * @param configName 配置名称  对应自定义配置列表上的 配置名称 字段
     * @param key 对应代码生成器中的字段名称 注意：名称是驼峰式
     * @return 无匹配返回null
     */
    public static Object getObject(String configName,String key) {
        if (StringUtils.isEmpty(configName) || StringUtils.isEmpty(configName)) {
            return null;
        }
        IConfigBiz configBiz = SpringUtil.getBean(IConfigBiz.class);
        //根据配置名称获取data
        ConfigEntity configEntity = new ConfigEntity();
        configEntity.setConfigName(configName);
        configEntity = configBiz.getOne(new QueryWrapper<>(configEntity));
        if (configEntity == null) {
            return null;
        }
        //将data转换成map
        HashMap map = JSON.parseObject(configEntity.getConfigData(), HashMap.class);
        if(map!=null){
            return map.get(key);
        }
        return null;
    }

    /**
     * 获取configName完整配置数据，通过一次性获取所有配置，避免重复传递 configName
     * @param configName 配置名称  对应自定义配置列表上的 配置名称 字段
     * @return map
     */
    public static Map getMap(String configName) {
        if (StringUtils.isEmpty(configName) || StringUtils.isEmpty(configName)) {
            return null;
        }
        IConfigBiz configBiz = SpringUtil.getBean(IConfigBiz.class);
        //根据配置名称获取data
        ConfigEntity configEntity = new ConfigEntity();
        configEntity.setConfigName(configName);
        configEntity = configBiz.getOne(new QueryWrapper<>(configEntity));
        if (configEntity == null || StringUtils.isEmpty(configEntity.getConfigData())) {
            return null;
        }
        return  JSON.parseObject(configEntity.getConfigData(), HashMap.class);
    }
}

