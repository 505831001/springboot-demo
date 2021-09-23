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

package net.mingsoft.base.entity;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlTransient;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.google.inject.internal.cglib.core.$FieldTypeCustomizer;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import net.mingsoft.base.constant.e.DeleteEnum;

/**
 *
 * @ClassName:  BaseEntity
 * @Description:TODO(基础实体类，其他所有实体都需要继承)
 * @author: 铭飞开发团队
 * @date:   2018年3月19日 下午3:36:17
 *
 * @Copyright: 2018 www.mingsoft.net Inc. All rights reserved.
 */
public abstract class  BaseEntity implements Serializable{

    /**
     * 创建用户编号
     */
    protected String createBy;
    /**
     * 创建日期
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    protected Date createDate;

    /**
     * 标记
     */
    protected Integer del=0;

    /**
     * 实体编号（唯一标识）
     */
    protected String id;

    /**
     * 备注
     */
    @TableField(exist = false)
    protected String remarks;

    /**
     * 最后更新用户编号
     */
    protected String updateBy;

    /**
     * 最后更新日期
     */
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    protected Date updateDate;

    /**
     * 自定义SQL where条件，需要配合对应dao.xml使用
     */
    @JsonIgnore
    @XmlTransient
    @TableField(exist = false)
    protected String sqlWhere;

    /**
     * 自定义SQL where条件，需要配合对应dao.xml使用
     */
    @JsonIgnore
    @XmlTransient
    @TableField(exist = false)
    protected String sqlDataScope;

    /**
     * 排序字段
     */
    @JsonIgnore
    @XmlTransient
    @TableField(exist = false)
    protected String orderBy;

    /**
     * 排序方式
     */
    @TableField(exist = false)
    protected String order;


    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getDel() {
        return del;
    }

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public void setDel(DeleteEnum del) {
        this.del = del.toInt();
    }

    public void setDel(Integer del) {
        this.del = del;
    }

    public String getId() {
        return id;
    }

    /**
     * 方便业务层获取id,为空返回null
     * @return
     */
    @JSONField(serialize = false)
    @XmlTransient
    public Integer getIntegerId(){
        if(StringUtils.isEmpty(id)){
            return null;
        }else {
            return Integer.parseInt(id);
        }
    }

    /**
     * 方便业务层获取id,为空返回0
     * @return id
     */
    @JSONField(serialize = false)
    @XmlTransient
    public int getIntId(){
        if(StringUtils.isEmpty(id)){
            return 0;
        }else {
            return Integer.parseInt(id);
        }
    }


    /**
     *
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 方便业务层设置id
     * @return
     */
    @JsonIgnore
    @XmlTransient
    public void setIntegerId(Integer id) {
        this.id = String.valueOf(id);
    }

    /**
     * 方便业务层设置id
     * @return
     */
    @JsonIgnore
    @XmlTransient
    public void setIntId(int id) {
        this.id = String.valueOf(id);
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @JsonIgnore
    @XmlTransient
    public String getSqlWhere() {
        return sqlWhere;
    }

    @JsonIgnore
    @XmlTransient
    public List getSqlWhereList() {
        if(StringUtils.isNotBlank(sqlWhere)){
            try {
                return JSONObject.parseArray(sqlWhere,Map.class);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return Collections.EMPTY_LIST;
    }

    public void setSqlWhere(String sqlWhere) {
        this.sqlWhere = sqlWhere;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }


    public String getSqlDataScope() {
        return sqlDataScope;
    }

    public void setSqlDataScope(String sqlDataScope) {
        this.sqlDataScope = sqlDataScope;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }



}
