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

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import net.mingsoft.base.entity.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 网站基本信息实体类
 * @author 史爱华
 * @version
 * 版本号：100-000-000<br/>
 * 创建日期：2012-03-15<br/>
 * 历史修订：<br/>
 */
@TableName("app")
public class AppEntity extends BaseEntity {

    /**
     * 网站运行状态
     */
    private String appState;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 应用描述
     */
    private String appDescription;

    /**
     * 应用logo
     */
    private String appLogo;


    /**
     * 网站采用的模板风格
     */

    private String appStyle;

    /**
     * 网站关键字
     */
    private String appKeyword;

    /**
     * 网站版权信息
     */
    private String appCopyright;

    /**
     * 网站生成的文件夹
     */
    private String appDir;

    /**
     * 网站域名
     */
    private String appUrl;

    /**
     * 站点日期
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date appDatetime;

    /**
     * 应用续费时间
     */

    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    private Date appPayDate;

    /**
     * 应用费用清单
     */
    private String appPay;


    public Date getAppPayDate() {
        return appPayDate;
    }

    public void setAppPayDate(Date appPayDate) {
        this.appPayDate = appPayDate;
    }

    public String getAppPay() {
        return appPay;
    }

    public void setAppPay(String appPay) {
        this.appPay = appPay;
    }

    public Date getAppDatetime() {
        return appDatetime;
    }

    public void setAppDatetime(Date appDatetime) {
        this.appDatetime = appDatetime;
    }

    /**
     * 获取网站版权信息
     *
     * @return 返回网站版权信息
     */
    public String getAppCopyright() {
        return appCopyright;
    }

    public String getAppDescription() {
        return appDescription;
    }

    /**
     * 获取网站的关键字
     *
     * @return 返回网站关键字
     */
    public String getAppKeyword() {
        return appKeyword;
    }


    public String getAppName() {
        return appName;
    }

    /**
     * 获取网站使用的模板风格
     *
     * @return 返回网站使用的模板风格
     */
    public String getAppStyle() {
        return appStyle;
    }

    /**
     * 获取网站域名
     */
    public String getAppUrl() {
        //判断域名是否为空
        if(ObjectUtil.isNotNull(appUrl)){
            return appUrl.replaceAll(" ","");
        }
        return appUrl;
    }

    /**
     * 获取网站域名
     */
    public String getAppHostUrl() {
        //判断域名是否为空
        if(ObjectUtil.isNotNull(appUrl)){
            //存在多个域名绑定
            if (appUrl.indexOf("\n") > 0) {
                return appUrl.split("\n")[0].trim();
            }
        }
        return appUrl;
    }

    public String getAppLogo() {
        return appLogo;
    }

    /**
     * 设置网站版权信息
     *
     * @param appCopyright
     */
    public void setAppCopyright(String appCopyright) {
        this.appCopyright = appCopyright;
    }

    public void setAppDescription(String appDescription) {
        this.appDescription = appDescription;
    }

    public int getAppId() {
        return id==null? 0:Integer.parseInt(id);
    }

    public void setAppId(int id) {
        this.id = id+"";
    }

    /**
     * 设置网站关键字
     *
     * @param appKeyword
     */
    public void setAppKeyword(String appKeyword) {
        this.appKeyword = appKeyword;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    /**
     * 设置网站使用的模板风格
     *
     * @param appStyle
     */
    public void setAppStyle(String appStyle) {
        this.appStyle = appStyle;
    }

    /**
     * 设置网站域名
     *
     * @param appUrl
     */
    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }


    public void setAppLogo(String appLogo) {
        this.appLogo = appLogo;
    }

    public String getAppDir() {
        return appDir;
    }

    public void setAppDir(String appDir) {
        this.appDir = appDir;
    }

    public String getAppState() {
        return appState;
    }

    public void setAppState(String appState) {
        this.appState = appState;
    }
}
