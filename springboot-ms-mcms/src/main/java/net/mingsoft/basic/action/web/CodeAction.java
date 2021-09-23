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

package net.mingsoft.basic.action.web;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import io.swagger.annotations.ApiOperation;
import net.mingsoft.basic.constant.e.SessionConstEnum;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 图片验证码
 * @author by 铭软开发团队
 * @Description TODO
 * @date 2020/1/10 8:39
 */
@Controller("codeAction")
@RequestMapping("code")
public class CodeAction {

    /**
     * 图片默认宽度
     */
    private int imgWidth = 100;

    /**
     * 图片默认高度
     */
    private int imgHeight = 50;

    /**
     * 返回验证码图片
     *
     * @param req
     *            HttpServletRequest对象
     * @param res
     *            HttpServletResponse 对象
     * @throws ServletException
     *             异常处理
     * @throws IOException
     *             异常处理
     */
    @ApiOperation(value = "返回验证码图片")
    @GetMapping(produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] index(HttpServletRequest req, HttpServletResponse res, HttpSession session) throws IOException {
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(imgWidth, imgHeight, 4, 10);
        // 将认证码存入SESSION
        session.setAttribute(SessionConstEnum.CODE_SESSION.toString(),  captcha.getCode());
        return captcha.getImageBytes();
    }
}
