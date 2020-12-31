package org.liuweiwei.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 用户模型
 *
 * @author Liuweiwei
 * @since 2020-12-31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TbUser {
    private Long id;
    private String username;
    private String password;
}