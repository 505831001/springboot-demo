package com.liuweiwei.service.impl;

import com.liuweiwei.model.TbUser;
import com.liuweiwei.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * @author liuweiwei
 * @since 2020-05-20
 */
@Service
public class SysUserServiceImpl implements UserService {

	@Override
	public TbUser findByUsername(String username) {
		TbUser tbUser = new TbUser();
		tbUser.setId(1L);
		tbUser.setUsername(username);
		String password = new BCryptPasswordEncoder().encode("123");
		tbUser.setPassword(password);
		return tbUser;
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
