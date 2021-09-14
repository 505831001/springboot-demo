package com.ruoyi.web.controller.demo.domain;

public class UserTableColumn {
    /**
     * 表头
     */
    private String title;
    /**
     * 字段
     */
    private String field;

    public UserTableColumn() {

    }

    public UserTableColumn(String title, String field) {
        this.title = title;
        this.field = field;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
