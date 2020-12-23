package com.liuweiwei.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author liuweiwei
 * @since 2020-05-20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TbUser {
    @NotNull(message = "用在基本数据类型上面")
    private Long id;
    @NotBlank(message = "用在字符串类型上面")
    private String username;
    @NotEmpty(message = "用在集合类上面")
    private String password;
}