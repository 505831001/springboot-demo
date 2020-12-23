package com.liuweiwei.component;

import com.liuweiwei.model.TbPermission;
import com.liuweiwei.model.TbRole;
import com.liuweiwei.model.TbUser;
import com.liuweiwei.service.TbLoginService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * @author Liuweiwei
 * @since 2020-12-23
 */
public class CustomRealm extends AuthorizingRealm {

    @Autowired
    private TbLoginService loginService;

    /**
     * 权限配置类：Authority TODO - 是否调用取决于：SecurityUtils.getSubject().login(token)或者SecurityUtils.getSubject().logout()
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //获取登录用户名
        String name = (String) principals.getPrimaryPrincipal();
        //查询用户名称
        TbUser user = loginService.getUserByName(name);
        //添加角色和权限
        SimpleAuthorizationInfo authority = new SimpleAuthorizationInfo();
        for (TbRole role : user.getRoles()) {
            //添加角色
            authority.addRole(role.getRoleName());
            //添加权限
            for (TbPermission permissions : role.getPermissions()) {
                authority.addStringPermission(permissions.getPermissionsName());
            }
        }
        return authority;
    }

    /**
     * 认证配置类：Authentic TODO - 是否调用取决于：SecurityUtils.getSubject().checkRole<s>("")或者SecurityUtils.getSubject().checkPermission<s>("")
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        if (StringUtils.isEmpty(authenticationToken.getPrincipal())) {
            return null;
        }
        //获取用户信息
        String name = authenticationToken.getPrincipal().toString();
        TbUser user = loginService.getUserByName(name);
        if (user == null) {
            //这里返回后会报出对应异常
            return null;
        } else {
            //这里验证authenticationToken和simpleAuthenticationInfo的信息
            SimpleAuthenticationInfo authentic = new SimpleAuthenticationInfo(name, user.getPassword().toString(), getName());
            return authentic;
        }
    }
}
