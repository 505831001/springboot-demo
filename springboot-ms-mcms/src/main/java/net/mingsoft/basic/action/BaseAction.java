/**
 * The MIT License (MIT)
 * Copyright (c) 2021 铭软科技(mingsoft.net)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */


package net.mingsoft.basic.action;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.SecureUtil;
import io.swagger.annotations.Api;
import net.mingsoft.basic.biz.IAppBiz;
import net.mingsoft.basic.constant.Const;
import net.mingsoft.basic.constant.e.CookieConstEnum;
import net.mingsoft.basic.constant.e.SessionConstEnum;
import net.mingsoft.basic.entity.ManagerEntity;
import net.mingsoft.basic.entity.ManagerSessionEntity;
import net.mingsoft.basic.util.BasicUtil;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;

/**
 * 基础应用层的父类base
 *
 * @author 铭飞开发团队
 * @version 版本号：100-000-000<br/>
 *          创建日期：2015-7-19<br/>
 *          历史修订：<br/>
 */
@Api("基础应用层的父类base")
public abstract class BaseAction extends net.mingsoft.base.action.BaseAction {
    /**
     * appBiz业务层的注入
     */
    @Autowired
    private IAppBiz appBiz;

    @Value("${ms.manager.check-code:true}")
    private Boolean checkCode;

    /**
     * 读取管理员session
     *
     * @return 获取不到就返回null
     */
    protected ManagerSessionEntity getManagerBySession() {
        Subject subject = SecurityUtils.getSubject();
        ManagerEntity _manager = (ManagerEntity) subject.getPrincipal();
        ManagerSessionEntity managerSession = new ManagerSessionEntity();
//                 压入管理员seesion
        BasicUtil.setSession(SessionConstEnum.MANAGER_SESSION, managerSession);
        BeanUtils.copyProperties(_manager, managerSession);
        // 传入用管理员请求，读取管理员的session || super,调用父类的protected属性的getSession方法
//		ManagerSessionEntity managerSession = (ManagerSessionEntity) BasicUtil
//				.getSession(SessionConstEnum.MANAGER_SESSION);
        if (managerSession != null) {
            // 返回管理员的所有信息
            return managerSession;
        }
        return null;
    }


    /**
     * 判断当前管理员是否是系统平台管理员
     *
     * @return true:是系统平台管理员，false:不是系统平台管理员
     */
    protected boolean isSystemManager() {
        ManagerSessionEntity manager = getManagerBySession();
        if (manager.getRoleId() == Const.DEFAULT_SYSTEM_MANGER_ROLE_ID) {
            return true;
        } else {
            return false;
        }
    }



    @Override
    protected String getResString(String key) {
        // TODO Auto-generated method stub
        String str = "";
        try {
            str = super.getResString(key);
        } catch (MissingResourceException e) {
            str = getLocaleString(key, Const.RESOURCES);
        }
        return str;
    }


    /**
     * 验证验证码
     *
     * @return 如果相同，返回true，否则返回false
     */
    protected boolean checkRandCode() {
        return checkRandCode( SessionConstEnum.CODE_SESSION.toString());
    }

    /**
     * AES解密字符串,key值为当前应用编号
     *
     * @param request HttpServletRequest对象
     * @param str     需要解密的字符串
     * @return 返回解密后的字符串
     */
    protected String decryptByAES(HttpServletRequest request, String str) {
        // 这里存在一个糊涂工具的bug必须先用变量保存变量再返回
        String _str = SecureUtil.aes(SecureUtil.md5(BasicUtil.getApp().getAppId() + "").substring(16).getBytes())
                .decryptStr(str);
        return _str;
    }

    /**
     * AES加密字符串,key值为当前应用编号
     *
     * @param request HttpServletRequest对象
     * @param str     需要加密的字符串
     * @return 返回加密后的字符串
     */
    protected String encryptByAES(HttpServletRequest request, String str) {
        // 这里存在一个糊涂工具的bug必须先用变量保存变量再返回
        String _str = SecureUtil.aes(SecureUtil.md5(BasicUtil.getApp().getAppId() + "").substring(16).getBytes())
                .encryptHex(str);
        return _str;
    }

    /**
     * 获取验证码
     *
     * @return 返回验证码，获取不到返回null
     */
    protected String getRandCode() {
        return BasicUtil.getSession(SessionConstEnum.CODE_SESSION) + "";
    }

    /**
     * 返回重定向
     *
     * @param flag    true:提供给springMVC返回，false:只是获取地址
     * @return 返回重定向后的地址
     */
    protected String redirectBack( boolean flag) {
        if (flag) {
            return "redirect:" + BasicUtil.getCookie(CookieConstEnum.BACK_COOKIE);
        } else {
            return BasicUtil.getCookie(CookieConstEnum.BACK_COOKIE);
        }

    }

    /**
     * 验证验证码
     *
     * @param param   表单验证码参数名称
     * @return 如果相同，返回true，否则返回false
     */
    protected boolean checkRandCode( String param) {
        if(!checkCode){
            return true;
        }
        String sessionCode = this.getRandCode();
        String requestCode = BasicUtil.getString(param);
        LOG.debug("session_code:" + sessionCode + " requestCode:" + requestCode);
        if (sessionCode.equalsIgnoreCase(requestCode)) {
            return true;
        }
        return false;
    }


    /**
     * 移除url参数
     *
     * @param request
     * @param fitlers 需要移除的字段名称
     */
    @Deprecated
    protected void removeUrlParams(HttpServletRequest request, String[] fitlers) {
        request.setAttribute(Const.PARAMS, BasicUtil.assemblyRequestUrlParams(fitlers));
    }

    /**
     * 适用于insert save数据时进行唯一性判断
     * 判断指定字段在数据库是否已经存在
     * @param tableName 表名
     * @param fieldName 字段名
     * @param fieldValue 字段值
     * @return
     */
    protected boolean validated(String tableName,String fieldName, String fieldValue) {
        Map where = new HashMap<>(1);
        where.put(fieldName, fieldValue);
        List list = appBiz.queryBySQL(tableName, null, where);
        if (ObjectUtil.isNotNull(list) && !list.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * 适用于update 更新 数据时进行唯一性判断
     * 判断指定字段在数据库是否已经存在
     * 主键id用来防止跟自身字段验证重复
     * @param tableName 表名
     * @param fieldName 字段名
     * @param fieldValue 字段值
     * @param id 要更新的主键id
     * @param idName 要更新的主键名称
     * @return
     */
    protected boolean validated(String tableName, String fieldName, String fieldValue, String id,String idName) {
        Map where = new HashMap<>(1);
        where.put(fieldName, fieldValue);
        List<CaseInsensitiveMap> list = appBiz.queryBySQL(tableName, null, where);
        if (ObjectUtil.isNotNull(list) && !list.isEmpty()) {
            //更新时判断是否是本身
            if(list.size() == 1){
                CaseInsensitiveMap _map = new CaseInsensitiveMap(list.get(0));
                if(id.equals(_map.get(idName).toString())){
                    return false;
                }
            }
            return true;
        }
        return false;
    }


}
