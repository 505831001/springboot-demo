package com.excel.poi.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Liuweiwei
 * @since 2021-08-05
 */
@Data
@ApiModel(description = "用户表入参对象DTO（所有字段类型保持和PO一样），出参对象VO（所有字段转字符串类型响应）")
public class TbMenuTreeVO implements Serializable {
    private static final long serialVersionUID = 1342245960543905436L;
    /**主键ID(子节点ID)*/
    @ApiModelProperty(value = "主键ID", required = false)
    private String id;

    /**父节点ID*/
    @ApiModelProperty(value = "父节点ID", required = false)
    private String parentId;

    /**菜单名称*/
    @ApiModelProperty(value = "菜单名称", required = false)
    private String menuName;

    /**菜单角色*/
    @ApiModelProperty(value = "菜单角色", required = false)
    private String role;

    /**菜单权限*/
    @ApiModelProperty(value = "菜单权限", required = false)
    private String permission;

    /**创建时间*/
    @ApiModelProperty(value = "创建时间（年-月-日 时:分:秒）", required = false)
    private String createTime;

    /**创建人员*/
    @ApiModelProperty(value = "创建人员", required = false)
    private String createName;

    /**更新时间*/
    @ApiModelProperty(value = "更新时间（年-月-日 时:分:秒）", required = false)
    private String updateTime;

    /**更新人员*/
    @ApiModelProperty(value = "更新人员", required = false)
    private String updateName;

    /**下一级菜单*/
    @ApiModelProperty(value = "下一级菜单", required = false)
    private List<TbMenuTreeVO> children;
}
