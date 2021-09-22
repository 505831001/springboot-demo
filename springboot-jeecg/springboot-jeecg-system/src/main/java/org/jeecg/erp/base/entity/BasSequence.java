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
 * @Description: bas_sequence
 * @Author: jeecg-boot
 * @Date: 2020-03-20
 * @Version: V1.0
 */
@Data
@TableName("bas_sequence")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "bas_sequence对象", description = "bas_sequence")
public class BasSequence implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * k
     */
    @Excel(name = "k", width = 15)
    @ApiModelProperty(value = "k")
    @TableId(value = "k", type = IdType.INPUT)
    private String k;
    /**
     * v
     */
    @Excel(name = "v", width = 15)
    @ApiModelProperty(value = "v")
    private Integer v;
}