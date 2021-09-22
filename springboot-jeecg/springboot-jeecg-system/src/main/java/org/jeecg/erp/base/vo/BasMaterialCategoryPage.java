package org.jeecg.erp.base.vo;

import java.util.List;

import org.jeecg.erp.base.entity.BasMaterial;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 物料分类
 * @Author: jeecg-boot
 * @Date: 2020-03-29
 * @Version: V1.0
 */
@Data
@ApiModel(value = "bas_material_categoryPage对象", description = "物料分类")
public class BasMaterialCategoryPage {
    @ApiModelProperty(value = "ID")
    private String id;

    @Excel(name = "父节点", width = 15)
    @ApiModelProperty(value = "父节点")
    private String pid;

    @Excel(name = "是否有子节点", width = 15)
    @ApiModelProperty(value = "是否有子节点")
    private String hasChild;

    @Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
    private String name;

    @Excel(name = "编码", width = 15)
    @ApiModelProperty(value = "编码")
    private String code;

    @Excel(name = "全名", width = 15)
    @ApiModelProperty(value = "全名")
    private String fullname;

    @Excel(name = "是否启用", width = 15)
    @ApiModelProperty(value = "是否启用")
    private Integer isEnabled;

    @Excel(name = "创建人", width = 15)
    @ApiModelProperty(value = "创建人")
    private String createBy;

    @Excel(name = "创建时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;

    @Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
    private String updateBy;

    @Excel(name = "修改时间", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "修改时间")
    private java.util.Date updateTime;

    @Excel(name = "版本", width = 15)
    @ApiModelProperty(value = "版本")
    private Integer version;

    @ExcelCollection(name = "物料")
    @ApiModelProperty(value = "物料")
    private List<BasMaterial> basMaterialList;
}
