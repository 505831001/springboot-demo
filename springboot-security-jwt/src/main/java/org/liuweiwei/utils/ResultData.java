package org.liuweiwei.utils;

import lombok.Data;
import org.springframework.http.HttpStatus;

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
     * 响应成功方法，无参（200-Success）
     *
     * @return
     */
    public static ResultData success() {
        ResultData result = new ResultData();
        result.setStatus(true);
        result.setCode(HttpStatus.OK.value());
        result.setCode(HttpServletResponse.SC_OK);
        result.setMessage(HttpStatus.OK.getReasonPhrase());
        return result;
    }

    /**
     * 返回成功方法，带参（200-Success）
     *
     * @param data
     * @return
     */
    public static ResultData success(Object data) {
        ResultData result = new ResultData();
        result.setData(data);
        result.setStatus(true);
        result.setCode(org.apache.http.HttpStatus.SC_OK);
        result.setCode(HttpServletResponse.SC_OK);
        result.setMessage(HttpStatus.OK.getReasonPhrase());
        return result;
    }

    /**
     * 返回失败方法，无参（400-Bad Request）
     *
     * @return
     */
    public static ResultData failure() {
        ResultData result = new ResultData();
        result.setStatus(false);
        result.setCode(HttpStatus.BAD_REQUEST.value());
        result.setCode(HttpServletResponse.SC_BAD_REQUEST);
        result.setMessage(HttpStatus.NO_CONTENT.getReasonPhrase());
        return result;
    }

    /**
     * 返回失败方法，带参（400-Bad Request）
     *
     * @return
     */
    public static ResultData failure(Object data) {
        ResultData result = new ResultData();
        result.setData(data);
        result.setStatus(false);
        result.setCode(org.apache.http.HttpStatus.SC_BAD_REQUEST);
        result.setCode(HttpServletResponse.SC_NO_CONTENT);
        result.setMessage(HttpStatus.NO_CONTENT.getReasonPhrase());
        return result;
    }
}
