package org.liuweiwei.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.log4j.Log4j2;
import org.liuweiwei.dao.TbUserMapper;
import org.liuweiwei.entity.TbUser;
import org.liuweiwei.service.TbUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Liuweiwei
 * @since 2020-12-31
 */
@Service
@Log4j2
public class TbUserServiceImpl implements TbUserService {

    @Resource
    private TbUserMapper userMapper;

    @Override
    public TbUser findByUsername(String username) {
        LambdaQueryWrapper<TbUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TbUser::getUsername, username);
        TbUser user = userMapper.selectOne(wrapper);
        log.info("Query database data -> {}", user.toString());
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
