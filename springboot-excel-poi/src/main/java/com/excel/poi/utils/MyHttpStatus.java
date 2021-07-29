package com.excel.poi.utils;

/**
 * org.apache.http.HttpStatus
 * org.apache.http.HttpStatus
 *
 * org.springframework.http.HttpStatus
 * org.springframework.http.HttpStatus
 *
 * javax.servlet.http.HttpServletResponse
 * javax.servlet.http.HttpServletResponse
 *
 * @author Liuweiwei
 * @since 2021-07-29
 */
public enum MyHttpStatus {

    OK(200, "OK"),

    CREATED(201, "Created"),

    ACCEPTED(202, "Accepted"),

    NON_AUTHORITATIVE_INFORMATION(203, "Non-Authoritative Information"),

    NO_CONTENT(204, "No Content"),

    RESET_CONTENT(205, "Reset Content"),

    PARTIAL_CONTENT(206, "Partial Content"),

    MULTI_STATUS(207, "Multi-Status"),

    ALREADY_REPORTED(208, "Already Reported"),

    IM_USED(226, "IM Used");

    private int key;
    private String value;

    MyHttpStatus(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
