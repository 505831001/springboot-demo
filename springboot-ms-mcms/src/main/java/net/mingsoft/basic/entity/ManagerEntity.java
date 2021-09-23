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


package net.mingsoft.basic.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import net.mingsoft.base.entity.BaseEntity;

import java.util.Date;

/**
 * 管理员实体类
 * @author ms dev group
 * @version
 * 版本号：100-000-000<br/>
 * 创建日期：2012-03-15<br/>
 * 历史修订：<br/>
 */
@TableName("manager")
public class ManagerEntity extends BaseEntity {


    /**
     * 帐号
     */
    private String managerName;

    /**
     * 昵称
     */
    @TableField("MANAGER_NICKNAME")
    private String managerNickName;

    /**
     * 角色名
     */
    private String roleName;

    /**
     * 密码
     */
    private String managerPassword;

    /**
     * 目前只在业务系统中使用
     */
    private String managerAdmin;

    /**
     * 角色ID
     */
    private int roleId;

    /**
     * 用户ID
     */
    private int peopleId;


    /**
     * 获取角色名
     * @return
     */
    public String getRoleName() {
        return roleName;
    }

    public String getManagerAdmin() {
        return managerAdmin;
    }

    public void setManagerAdmin(String managerAdmin) {
        this.managerAdmin = managerAdmin;
    }

    /**
     * 设置角色名
     * @param roleName
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }


    /**
     * 获取managerName
     * @return managerName
     */
    public String getManagerName() {
        return managerName;
    }

    /**
     * 设置managerName
     * @param managerName
     */
    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    /**
     * 获取managerPassword
     * @return managerPassword
     */
    public String getManagerPassword() {
        return managerPassword;
    }

    /**
     * 设置managerPassword
     * @param managerPassword
     */
    public void setManagerPassword(String managerPassword) {

        this.managerPassword = managerPassword;
    }

    public int getPeopleId() {
        return peopleId;
    }

    public void setPeopleId(int peopleId) {
        this.peopleId = peopleId;
    }

    /**
     * 获取managerNickName
     * @return managerNickName
     */
    public String getManagerNickName() {
        return managerNickName;
    }

    /**
     * 设置managerNickName
     * @param managerNickName
     */
    public void setManagerNickName(String managerNickName) {
        this.managerNickName = managerNickName;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

}
