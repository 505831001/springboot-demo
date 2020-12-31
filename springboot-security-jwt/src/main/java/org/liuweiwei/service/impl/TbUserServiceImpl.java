package org.liuweiwei.service.impl;

import org.liuweiwei.model.TbUser;
import org.liuweiwei.service.TbUserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Liuweiwei
 * @since 2020-12-31
 */
@Service
public class TbUserServiceImpl implements TbUserService {

    @Override
    public TbUser findByUsername(String username) {
        TbUser user = new TbUser();
        user.setId(1L);
        user.setUsername(username);
        String password = new BCryptPasswordEncoder().encode("123");
        user.setPassword(password);
        return user;
    }

    @Override
    public Set<String> findPermissions(String username) {
        Set<String> permissions = new HashSet<>();
        permissions.add("sys:user:view");
        permissions.add("sys:user:add");
        permissions.add("sys:user:edit");
        permissions.add("sys:user:delete");
        return permissions;
    }
}
