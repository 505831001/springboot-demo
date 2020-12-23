package com.liuweiwei.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @author Liuweiwei
 * @since 2020-12-23
 */
@Data
@AllArgsConstructor
public class TbUser {
    @NotNull(message = "用在基本数据类型上面")
    private String id;
    @NotBlank(message = "用在字符串类型上面")
    private String userName;
    @NotEmpty(message = "用在集合类上面")
    private String password;
    /**
     * 用户对应的角色集合
     */
    private Set<TbRole> roles;
}
