package org.jeecg.erp.base.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.annotation.Dict;

/**
 * @Description: 仓库
 * @Author: jeecg-boot
 * @Date: 2020-04-01
 * @Version: V1.0
 */
@Data
@TableName("bas_warehouse")
public class BasWarehouse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(type = IdType.ID_WORKER_STR)
    private String id;
    /**
     * 父级
     */
    @Excel(name = "父级", width = 15)
    private String pid;
    /**
     * 是否有子节点
     */
    @Excel(name = "是否有子节点", width = 15)
    private String hasChild;
    /**
     * 编码
     */
    @Excel(name = "编码", width = 15)
    private String code;
    /**
     * 名称
     */
    @Excel(name = "名称", width = 15)
    private String name;
    /**
     * 电话
     */
    @Excel(name = "电话", width = 15)
    private String phone;
    /**
     * 是否启用
     */
    @Excel(name = "是否启用", width = 15, dicCode = "yn")
    @Dict(dicCode = "yn")
    private Integer isEnabled;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    private String remark;
    /**
     * 创建人
     */
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    @Excel(name = "创建人", width = 15)
    private String createBy;
    /**
     * 创建时间
     */
    @Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime;
    /**
     * 修改人
     */
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    @Excel(name = "修改人", width = 15)
    private String updateBy;
    /**
     * 修改时间
     */
    @Excel(name = "修改时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date updateTime;
    /**
     * 版本
     */
    @ApiModelProperty(value = "版本")
    private Integer version;
}
