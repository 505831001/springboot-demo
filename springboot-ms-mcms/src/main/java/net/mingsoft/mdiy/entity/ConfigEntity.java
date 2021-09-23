package net.mingsoft.mdiy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import net.mingsoft.base.entity.BaseEntity;

/**
 * 进度表实体
 *
 * @author 铭飞科技
 * 创建日期：2021-3-18 11:50:14<br/>
 * 历史修订：<br/>
 */
@TableName("MDIY_CONFIG")
public class ConfigEntity extends BaseEntity {

    private static final long serialVersionUID = 1616039414961L;

    /**
     * 模型名称
     */
    private String configName;
    /**
     * 模型数据
     */
    private String configData;


    /**
     * 设置模型名称
     */

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    /**
     * 获取模型名称
     */
    public String getConfigName() {
        return this.configName;
    }

    /**
     * 设置模型数据
     */
    public void setConfigData(String configData) {
        this.configData = configData;
    }

    /**
     * 获取模型数据
     */
    public String getConfigData() {
        return this.configData;
    }

}
