package com.liuweiwei.service.impl;

import com.liuweiwei.model.TbPermission;
import com.liuweiwei.model.TbRole;
import com.liuweiwei.model.TbUser;
import com.liuweiwei.service.TbLoginService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Liuweiwei
 * @since 2020-12-23
 */
@Service
public class TbLoginServiceImpl implements TbLoginService {

    @Override
    public TbUser getUserByName(String getMapByName) {
        return getMapByName(getMapByName);
    }

    /**
     * 模拟数据库查询
     *
     * @param userName 用户名
     * @return User
     */
    private TbUser getMapByName(String userName) {
        TbPermission permission01 = new TbPermission("1", "insert");
        TbPermission permission02 = new TbPermission("2", "delete");
        TbPermission permission03 = new TbPermission("3", "update");
        TbPermission permission04 = new TbPermission("4", "select");

        Set<TbPermission> permissionsSet01 = new HashSet<>();
        permissionsSet01.add(permission01);
        permissionsSet01.add(permission02);
        permissionsSet01.add(permission03);
        permissionsSet01.add(permission04);

        Set<TbPermission> permissionsSet02 = new HashSet<>();
        permissionsSet02.add(permission04);

        TbRole role01 = new TbRole("1", "admin", permissionsSet01);
        Set<TbRole> admin = new HashSet<>();
        admin.add(role01);
        TbRole role02 = new TbRole("2", "guest", permissionsSet02);
        Set<TbRole> guest = new HashSet<>();
        guest.add(role02);

        Map<String, TbUser> map = new HashMap<>();

        TbUser user01 = new TbUser("1", "liuweiwei", "123456", admin);
        map.put(user01.getUserName(), user01);
        TbUser user02 = new TbUser("2", "linyiming", "123456", guest);
        map.put(user02.getUserName(), user02);

        return map.get(userName);
    }
}
