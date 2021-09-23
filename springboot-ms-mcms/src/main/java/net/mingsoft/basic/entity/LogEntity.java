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

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import net.mingsoft.base.entity.BaseEntity;
/**
 * 系统日志实体
 * @author 铭飞开发团队
 * 创建日期：2020-11-21 9:41:34<br/>
 * 历史修订：<br/>
 */

@TableName("logger")
public class LogEntity extends BaseEntity {

    private static final long serialVersionUID = 1605922894330L;

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 标题
     */
    private String logTitle;
    /**
     * IP
     */
    private String logIp;
    /**
     * 请求方法
     */
    private String logMethod;
    /**
     * 请求方式
     */
    private String logRequestMethod;
    /**
     * 请求地址
     */
    private String logUrl;
    /**
     * 请求状态
     */
    private String logStatus;
    /**
     * 业务类型
     */
    private String logBusinessType;
    /**
     * 用户类型
     */
    private String logUserType;
    /**
     * 操作人员
     */
    private String logUser;
    /**
     * 所在地区
     */
    private String logLocation;
    /**
     * 请求参数
     */
    private String logParam;
    /**
     * 返回参数
     */
    private String logResult;
    /**
     * 错误消息
     */
    private String logErrorMsg;


    /**
     * 设置标题
     */
    public void setLogTitle(String logTitle) {
        this.logTitle = logTitle;
    }

    /**
     * 获取标题
     */
    public String getLogTitle() {
        return this.logTitle;
    }
    /**
     * 设置IP
     */
    public void setLogIp(String logIp) {
        this.logIp = logIp;
    }

    /**
     * 获取IP
     */
    public String getLogIp() {
        return this.logIp;
    }
    /**
     * 设置请求方法
     */
    public void setLogMethod(String logMethod) {
        this.logMethod = logMethod;
    }

    /**
     * 获取请求方法
     */
    public String getLogMethod() {
        return this.logMethod;
    }
    /**
     * 设置请求方式
     */
    public void setLogRequestMethod(String logRequestMethod) {
        this.logRequestMethod = logRequestMethod;
    }

    /**
     * 获取请求方式
     */
    public String getLogRequestMethod() {
        return this.logRequestMethod;
    }
    /**
     * 设置请求地址
     */
    public void setLogUrl(String logUrl) {
        this.logUrl = logUrl;
    }

    /**
     * 获取请求地址
     */
    public String getLogUrl() {
        return this.logUrl;
    }
    /**
     * 设置请求状态
     */
    public void setLogStatus(String logStatus) {
        this.logStatus = logStatus;
    }

    /**
     * 获取请求状态
     */
    public String getLogStatus() {
        return this.logStatus;
    }
    /**
     * 设置业务类型
     */
    public void setLogBusinessType(String logBusinessType) {
        this.logBusinessType = logBusinessType;
    }

    /**
     * 获取业务类型
     */
    public String getLogBusinessType() {
        return this.logBusinessType;
    }
    /**
     * 设置用户类型
     */
    public void setLogUserType(String logUserType) {
        this.logUserType = logUserType;
    }

    /**
     * 获取用户类型
     */
    public String getLogUserType() {
        return this.logUserType;
    }
    /**
     * 设置操作人员
     */
    public void setLogUser(String logUser) {
        this.logUser = logUser;
    }

    /**
     * 获取操作人员
     */
    public String getLogUser() {
        return this.logUser;
    }
    /**
     * 设置所在地区
     */
    public void setLogLocation(String logLocation) {
        this.logLocation = logLocation;
    }

    /**
     * 获取所在地区
     */
    public String getLogLocation() {
        return this.logLocation;
    }
    /**
     * 设置请求参数
     */
    public void setLogParam(String logParam) {
        this.logParam = logParam;
    }

    /**
     * 获取请求参数
     */
    public String getLogParam() {
        return this.logParam;
    }
    /**
     * 设置返回参数
     */
    public void setLogResult(String logResult) {
        this.logResult = logResult;
    }

    /**
     * 获取返回参数
     */
    public String getLogResult() {
        return this.logResult;
    }
    /**
     * 设置错误消息
     */
    public void setLogErrorMsg(String logErrorMsg) {
        this.logErrorMsg = logErrorMsg;
    }

    /**
     * 获取错误消息
     */
    public String getLogErrorMsg() {
        return this.logErrorMsg;
    }
}
