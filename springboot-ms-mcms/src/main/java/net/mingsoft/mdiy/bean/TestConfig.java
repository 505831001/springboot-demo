package net.mingsoft.mdiy.bean;

/**
 * @Author: xierz
 * @Description:
 * @Date: Create in 2021/05/18 18:25
 */
public class TestConfig {
    String mailType;
    String mailName ;
    String mailForm;
    String mailFormName;
    String mailPassword;

    public TestConfig() {
    }

    public TestConfig(String mailType, String mailName, String mailForm, String mailFormName, String mailPassword) {
        this.mailType = mailType;
        this.mailName = mailName;
        this.mailForm = mailForm;
        this.mailFormName = mailFormName;
        this.mailPassword = mailPassword;
    }

    public String getMailType() {
        return mailType;
    }

    public void setMailType(String mailType) {
        this.mailType = mailType;
    }

    public String getMailName() {
        return mailName;
    }

    public void setMailName(String mailName) {
        this.mailName = mailName;
    }

    public String getMailForm() {
        return mailForm;
    }

    public void setMailForm(String mailForm) {
        this.mailForm = mailForm;
    }

    public String getMailFormName() {
        return mailFormName;
    }

    public void setMailFormName(String mailFormName) {
        this.mailFormName = mailFormName;
    }

    public String getMailPassword() {
        return mailPassword;
    }

    public void setMailPassword(String mailPassword) {
        this.mailPassword = mailPassword;
    }
}
