/**
 * The MIT License (MIT) * Copyright (c) 2020 铭软科技(mingsoft.net)
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.mingsoft.base.entity;


import cn.hutool.json.JSONString;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpStatus;

import java.util.HashMap;

/**
 * @ClassName: ResultJson
 * @Description:TODO(json数据返回数据格式)
 * @author: 铭飞开发团队
 * @date: 2018年3月19日 下午3:41:53
 * @Copyright: 2018 www.mingsoft.net Inc. All rights reserved.
 */
public class ResultData extends HashMap<String, Object> {

    /**
     * 状态码
     */
    public static final String CODE_KEY = "code";
    /**
     * 数据
     */
    public static final String DATA_KEY = "data";
    /**
     * 信息
     */
    public static final String MSG_KEY = "msg";
    /**
     * 请求状态
     */
    public static final String RESULT_KEY = "result";


    public static ResultData build() {
        return new ResultData();
    }

    public ResultData code(HttpStatus code) {
        return add(RESULT_KEY, code == HttpStatus.OK).add(CODE_KEY, code.value());
    }

    public ResultData code(String code) {
        return add(RESULT_KEY, code.equalsIgnoreCase(HttpStatus.OK.toString())).add(CODE_KEY, code);
    }


    /**
     * 返回信息
     *
     * @param msg
     * @return
     */
    public ResultData msg(String msg) {
        return add(MSG_KEY, msg);
    }

    /**
     * 返回数据
     *
     * @param data
     * @return
     */
    public ResultData data(Object data) {
        return add(DATA_KEY, data);
    }

    /**
     * 成功返回
     *
     * @return
     */
    public ResultData success() {
        return code(HttpStatus.OK);
    }

    /**
     * 成功返回
     *
     * @return
     */
    public ResultData success(Object data) {
        return success().data(data);
    }

    /**
     * 成功返回
     *
     * @return
     */
    public ResultData success(String msg, Object data) {
        return success().msg(msg).data(data);
    }

    /**
     * 错误返回
     *
     * @return
     */
    public ResultData error() {
        return code(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 错误返回
     *
     * @return
     */
    public ResultData error(String msg) {
        return error().msg(msg);
    }

    /**
     * 添加返回参数
     *
     * @param key
     * @param value
     * @return
     */
    public ResultData add(String key, Object value) {
        this.put(key, value);
        return this;
    }

    /**
     * 获取data对象
     * @param c 对象类型
     * @param <T>
     * @return
     */
    public <T> T getData(Class<T> c) {
        return (T)this.get(DATA_KEY);
    }

    /**
     * 获取文本提示信息
     * @return
     */
    public String getMsg() {
        return this.get(MSG_KEY)+"";
    }

    /**
     * 获取编码
     * @return
     */
    public int getCode() {
        return Integer.parseInt(this.get(CODE_KEY)+"");
    }

    /**
     * 判断接口成功状态
     * @return true:成功
     */
    public boolean isSuccess() {
        return Boolean.parseBoolean(this.get(RESULT_KEY)+"");
    }
}
