package com.ruoyi.web.controller.demo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.utils.DateUtils;

import java.util.Date;

public class UserTableModel {
    /**
     * 用户ID
     */
    private int userId;

    /**
     * 用户编号
     */
    @Excel(name = "用户编号", cellType = Excel.ColumnType.NUMERIC)
    private String userCode;

    /**
     * 用户姓名
     */
    @Excel(name = "用户姓名")
    private String userName;

    /**
     * 用户性别
     */
    private String userSex;

    /**
     * 用户手机
     */
    @Excel(name = "用户手机")
    private String userPhone;

    /**
     * 用户邮箱
     */
    @Excel(name = "用户邮箱")
    private String userEmail;

    /**
     * 用户余额
     */
    @Excel(name = "用户余额", cellType = Excel.ColumnType.NUMERIC)
    private double userBalance;

    /**
     * 用户状态（0正常 1停用）
     */
    private String status;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public UserTableModel() {

    }

    public UserTableModel(int userId, String userCode, String userName, String userSex, String userPhone,
                          String userEmail, double userBalance, String status) {
        this.userId = userId;
        this.userCode = userCode;
        this.userName = userName;
        this.userSex = userSex;
        this.userPhone = userPhone;
        this.userEmail = userEmail;
        this.userBalance = userBalance;
        this.status = status;
        this.createTime = DateUtils.getNowDate();
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public double getUserBalance() {
        return userBalance;
    }

    public void setUserBalance(double userBalance) {
        this.userBalance = userBalance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
