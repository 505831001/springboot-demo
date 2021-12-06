package com.liuweiwei.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author Liuweiwei
 * @since 2021-07-21
 */
@Data
@ApiModel(description = "用户表入参对象Dto，出参对象Vo（所有字段字符串类型响应）")
public class TbUserVo implements Serializable {
    /**主键ID*/
    @ApiModelProperty(value = "主键ID", required = false)
    private String id;
    /**用户名*/
    @NotBlank(message = "用户名称不能为空")
    @ApiModelProperty(value = "用户名", required = false)
    private String username;
    /**密码，加密存储*/
    @NotBlank(message = "用户密码不能为空")
    @ApiModelProperty(value = "密码，加密存储", required = false)
    private String password;
    /**角色*/
    @ApiModelProperty(value = "角色", required = false)
    private String role;
    /**权限*/
    @ApiModelProperty(value = "权限", required = false)
    private String permission;
    /**账号状态*/
    @ApiModelProperty(value = "账号状态", required = false)
    private String ban;
    /**注册手机号*/
    @ApiModelProperty(value = "注册手机号", required = false)
    private String phone;
    /**注册邮箱*/
    @ApiModelProperty(value = "注册邮箱", required = false)
    private String email;
    /**创建时间*/
    @ApiModelProperty(value = "创建时间（年-月-日 时:分:秒）", required = false)
    private String created;
    /**更新时间*/
    @ApiModelProperty(value = "更新时间（年-月-日 时:分:秒）", required = false)
    private String updated;
}
