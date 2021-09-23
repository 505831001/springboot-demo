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

package net.mingsoft.basic.exception;

import net.mingsoft.base.entity.ResultData;
import org.springframework.http.HttpStatus;

/**
 * 业务异常处理
 *
 * @author 铭软开发团队-Administrator
 * @date 2018年7月6日
 */

public class BusinessException extends RuntimeException {

    private HttpStatus code = HttpStatus.INTERNAL_SERVER_ERROR;
    private String msg;
    private Object data;

    public BusinessException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public BusinessException(HttpStatus code, String msg, Object data) {
        super(msg);
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public BusinessException(String msg, Object data) {
        super(msg);
        this.msg = msg;
        this.data = data;
    }

    /**
     * 自定义异常信息
     *
     * @param code 错误码 HttpStatus
     * @param msg  错误信息
     */
    public BusinessException(HttpStatus code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public HttpStatus getCode() {
        return code;
    }

    public void setCode(HttpStatus code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
