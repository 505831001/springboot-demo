package com.liuweiwei.service.impl;

import com.liuweiwei.model.TbUser;
import com.liuweiwei.service.TbUserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * @author liuweiwei
 * @since 2020-05-20
 */
@Service
public class TbUserServiceImpl implements TbUserService {

	@Override
	public TbUser findUserByName(String username) {
		TbUser tbUser = new TbUser();
		tbUser.setId(1L);
		tbUser.setUsername(username);
		String password = new BCryptPasswordEncoder().encode("123456");
		tbUser.setPassword(password);
		return tbUser;
	}

	@Override
	public Set<String> getPermissions(String username) {
		Set<String> permissions = new HashSet<>();
		permissions.add("sys:user:insert");
		permissions.add("sys:user:delete");
		permissions.add("sys:user:update");
		permissions.add("sys:user:select");
		return permissions;
	}
}
