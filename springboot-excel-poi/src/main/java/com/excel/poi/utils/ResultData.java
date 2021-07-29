package com.excel.poi.utils;

import lombok.Data;
import org.apache.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

/**
 * @author Liuweiwei
 * @since 2021-07-29
 */
@Data
public class ResultData implements Serializable {
    /**
     * HTTP Status Code - HTTP状态码表示网页服务器超文本传输协议响应状态的3位数字代码。
     */
    private Integer code;
    /**
     * 响应状态码，响应数据集
     */
    private Object data;
    /**
     * 响应状态码，响应数据集，响应描述
     */
    private String message;
    /**
     * 响应状态码，响应数据集合，响应描述，响应状态
     */
    private Boolean status;

    /**
     * 响应成功方法，无参
     *
     * @return
     */
    public static ResultData success() {
        ResultData result = new ResultData();
        result.setStatus(true);
        result.setCode(HttpStatus.SC_OK);
        result.setCode(HttpServletResponse.SC_OK);
        result.setMessage(org.springframework.http.HttpStatus.OK.getReasonPhrase());
        return result;
    }

    /**
     * 返回成功方法，带参
     *
     * @param data
     * @return
     */
    public static ResultData success(Object data) {
        ResultData result = new ResultData();
        result.setData(data);
        result.setStatus(true);
        result.setCode(HttpStatus.SC_OK);
        result.setCode(HttpServletResponse.SC_OK);
        result.setMessage(org.springframework.http.HttpStatus.OK.getReasonPhrase());
        return result;
    }

    /**
     * 返回失败方法，无参
     *
     * @return
     */
    public static ResultData failure() {
        ResultData result = new ResultData();
        result.setStatus(false);
        result.setCode(HttpStatus.SC_NO_CONTENT);
        result.setCode(HttpServletResponse.SC_NO_CONTENT);
        result.setMessage(org.springframework.http.HttpStatus.NO_CONTENT.getReasonPhrase());
        return result;
    }

    /**
     * 返回失败方法，带参
     *
     * @return
     */
    public static ResultData failure(Object data) {
        ResultData result = new ResultData();
        result.setData(data);
        result.setStatus(false);
        result.setCode(HttpStatus.SC_NO_CONTENT);
        result.setCode(HttpServletResponse.SC_NO_CONTENT);
        result.setMessage(org.springframework.http.HttpStatus.NO_CONTENT.getReasonPhrase());
        return result;
    }
}
