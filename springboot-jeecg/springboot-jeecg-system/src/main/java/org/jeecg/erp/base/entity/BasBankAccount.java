package org.jeecg.erp.base.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 银行账户
 * @Author: jeecg-boot
 * @Date: 2020-04-14
 * @Version: V1.0
 */
@Data
@TableName("bas_bank_account")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "bas_bank_account对象", description = "银行账户")
public class BasBankAccount implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "ID")
    private String id;
    /**
     * 账号
     */
    @Excel(name = "账号", width = 15)
    @ApiModelProperty(value = "账号")
    private String accountNo;
    /**
     * 账户名称
     */
    @Excel(name = "账户名称", width = 15)
    @ApiModelProperty(value = "账户名称")
    private String name;
    /**
     * 币种
     */
    @Excel(name = "币种", width = 15, dictTable = "bas_currency", dicText = "name", dicCode = "code")
    @Dict(dictTable = "bas_currency", dicText = "name", dicCode = "code")
    @ApiModelProperty(value = "币种")
    private String currency;
    /**
     * 初始余额
     */
    @Excel(name = "初始余额", width = 15)
    @ApiModelProperty(value = "初始余额")
    private java.math.BigDecimal initBal;
    /**
     * 行号
     */
    @Excel(name = "行号", width = 15)
    @ApiModelProperty(value = "行号")
    private String bankNo;
    /**
     * 银行地址
     */
    @Excel(name = "银行地址", width = 15)
    @ApiModelProperty(value = "银行地址")
    private String bankAddress;
    /**
     * 账户管理员
     */
    @Excel(name = "账户管理员", width = 15, dictTable = "sys_user", dicText = "realname", dicCode = "username")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    @ApiModelProperty(value = "账户管理员")
    private String manager;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String note;
    /**
     * 附件
     */
    @Excel(name = "附件", width = 15)
    @ApiModelProperty(value = "附件")
    private String attachment;
    /**
     * 是否启用
     */
    @Excel(name = "是否启用", width = 15, dicCode = "yn")
    @Dict(dicCode = "yn")
    @ApiModelProperty(value = "是否启用")
    private Integer isEnabled;
    /**
     * 创建人
     */
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    @ApiModelProperty(value = "创建人")
    private String createBy;
    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
    /**
     * 修改人
     */
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    @ApiModelProperty(value = "修改人")
    private String updateBy;
    /**
     * 修改时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;
    /**
     * 版本
     */
    @ApiModelProperty(value = "版本")
    private Integer version;
}
