package com.coder.springsecurity.test;

import com.coder.springsecurity.dao.PermissionDao;
import com.coder.springsecurity.dto.PermissionDto;
import com.coder.springsecurity.utils.TreeUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class TestSecurityLogin7Application {
    @Autowired
    private PermissionDao permissionDao;
    @Test
    void contextLoads() {
        List<PermissionDto> listByRoleId = permissionDao.listByRoleId(2);
        List<PermissionDto> permissionDtos = permissionDao.buildAll();
        List<PermissionDto> tree = TreeUtil.tree(listByRoleId, permissionDtos);
        System.out.println(tree);
    }

}
