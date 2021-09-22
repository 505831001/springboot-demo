package org.jeecg.erp.base.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: bas_biz_period
 * @Author: jeecg-boot
 * @Date: 2020-05-25
 * @Version: V1.0
 */
@Data
@TableName("bas_biz_period")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "bas_biz_period对象", description = "bas_biz_period")
public class BasBizPeriod implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty(value = "ID")
    private String id;
    /**
     * 启用年度
     */
    @Excel(name = "启用年度", width = 15)
    @ApiModelProperty(value = "启用年度")
    private Integer beginYear;
    /**
     * 启用月度
     */
    @Excel(name = "启用月度", width = 15)
    @ApiModelProperty(value = "启用月度")
    private Integer beginMonth;
    /**
     * 当前年度
     */
    @Excel(name = "当前年度", width = 15)
    @ApiModelProperty(value = "当前年度")
    private Integer year;
    /**
     * 当前月度
     */
    @Excel(name = "当前月度", width = 15)
    @ApiModelProperty(value = "当前月度")
    private Integer month;
    /**
     * 版本
     */
    @ApiModelProperty(value = "版本")
    private Integer version;
}
