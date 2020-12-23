package com.liuweiwei.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liuweiwei
 * @since 2020-05-20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TbToken {
    private boolean authenticated;
    private String[] authorities;
    private String credentials;
    private TbDetails details;
    private String name;
    private String principal;
    private String token;
}
