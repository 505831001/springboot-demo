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
package net.mingsoft.basic.util;

import cn.hutool.crypto.SecureUtil;
import net.mingsoft.base.util.BundleUtil;
import net.mingsoft.basic.constant.Const;
import net.mingsoft.basic.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;

/**
 * SQL注入工具类
 *
 * @author Administrator
 * @version 创建日期：2021/4/7 8:43<br/>
 * 历史修订：<br/>
 */
public class SqlInjectionUtil {

    private static Logger LOG = LoggerFactory.getLogger(SqlInjectionUtil.class);


    private static final String xssStr = "'|and |exec |insert |select |delete |update |drop |count |chr |mid |master |truncate |char |declare |;|or |+|,";

    private static final String reg = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|"

            + "(\\b(select|update|and|or|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)";


    /**
     * 表示忽略大小写
     */
    private static final Pattern sqlPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);



    /**
     * sql注入过滤处理，遇到注入关键字抛异常
     *
     * @param value
     * @return
     */
    public static void filterContent(String value) {
        if (value == null || "".equals(value)) {
            return;
        }
        // 统一转为小写
        value = value.toLowerCase();
        String[] xssArr = xssStr.split("\\|");
        for (int i = 0; i < xssArr.length; i++) {
            if (value.indexOf(xssArr[i]) > -1) {
                LOG.info("请注意，存在SQL注入关键词---> {}", xssArr[i]);
                LOG.info("请注意，值可能存在SQL注入风险!---> {}", value);
                throw new BusinessException(NOT_ACCEPTABLE, BundleUtil.getLocaleString("sql.param.illegal", Const.RESOURCES));
            }
        }
        return;
    }

    /**
     * sql注入过滤处理，遇到注入关键字抛异常
     *
     * @param values
     * @return
     */
    public static void filterContent(String[] values) {
        String[] xssArr = xssStr.split("\\|");
        for (String value : values) {
            if (value == null || "".equals(value)) {
                continue;
            }
            // 统一转为小写
            value = value.toLowerCase();
            for (int i = 0; i < xssArr.length; i++) {
                if (value.indexOf(xssArr[i]) > -1) {
                    LOG.info("请注意，存在SQL注入关键词---> {}", xssArr[i]);
                    LOG.info("请注意，值可能存在SQL注入风险!---> {}", value);
                    throw new BusinessException(NOT_ACCEPTABLE, BundleUtil.getLocaleString("sql.param.illegal", Const.RESOURCES));
                }
            }
        }
        return;
    }




    /**
     * 参数校验
     * @param str ep: "or 1=1"
     */
    public static boolean isSqlValid(String str) {
        Matcher matcher = sqlPattern.matcher(str);
        if (matcher.find()) {
            //获取非法字符：or
            LOG.info("参数存在非法字符，请确认："+matcher.group());
            return false;
        }
        return true;
    }
}
