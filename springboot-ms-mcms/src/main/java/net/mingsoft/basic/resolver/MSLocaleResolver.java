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
package net.mingsoft.basic.resolver;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;

/**
 * 自定义国际化语言解析器
 *
 * @author wjj
 * @version 创建日期：2020/12/2 14:27<br/>
 * 历史修订：<br/>
 */
public class MSLocaleResolver implements LocaleResolver {

    private static final String I18N_LANGUAGE = "lang";
    private static final String I18N_LANGUAGE_SESSION = "i18n_language_session";

    @Override
    public Locale resolveLocale(HttpServletRequest req) {
        String lang = req.getParameter(I18N_LANGUAGE);
        Locale locale = Locale.getDefault();
        if(!StringUtils.isEmpty(lang)) {
            String[] language = lang.split("_");
            locale = new Locale(language[0], language[1]);
            //将国际化语言保存到session
            HttpSession session = req.getSession();
            session.setAttribute(I18N_LANGUAGE_SESSION, locale);
        }else {
            //如果没有带国际化参数，则判断session有没有保存，有保存，则使用保存的，也就是之前设置的，避免之后的请求不带国际化参数造成语言显示不对
            HttpSession session = req.getSession();
            Locale localeInSession = (Locale) session.getAttribute(I18N_LANGUAGE_SESSION);
            if(localeInSession != null) {
                locale = localeInSession;
            }else {
                // session没有保存，默认中文
                locale =  Locale.SIMPLIFIED_CHINESE;
            }
        }
        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale) {

    }
}
