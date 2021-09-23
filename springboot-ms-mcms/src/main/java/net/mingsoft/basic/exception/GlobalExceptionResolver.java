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

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import net.mingsoft.base.entity.ResultData;
import net.mingsoft.base.util.BundleUtil;
import net.mingsoft.basic.util.BasicUtil;
import net.mingsoft.basic.util.CalculationUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.CredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.session.ExpiredSessionException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

/**
 * 全局异常处理类
 *
 * @author 铭软开发团队-Administrator
 * @date 2018年4月6日
 */
@ControllerAdvice
public class GlobalExceptionResolver extends DefaultHandlerExceptionResolver {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Value("${ms.upload.multipart.max-file-size}")
    private long maxFileSize;

    /**
     * 全局异常
     *
     * @param request
     * @param response
     * @param e
     * @return
     */
    @ExceptionHandler(value = BusinessException.class)
    public ModelAndView handleBusinessException(HttpServletRequest request, HttpServletResponse response, BusinessException e) {
        LOG.debug("handleBusinessException");
        response.setStatus(e.getCode().value());
        return render(request, response, ResultData.build().code(e.getCode()).data(e.getData()).msg(e.getMsg()), e);

    }

    /**
     * 全局异常
     *
     * @param request
     * @param response
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public ModelAndView handleException(HttpServletRequest request, HttpServletResponse response, Exception e) {
        LOG.debug("handleException");
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return render(request, response, ResultData.build().code(HttpStatus.INTERNAL_SERVER_ERROR)
                        .msg(e.getStackTrace()[0].toString().concat(":").concat(e.toString()))
                , e);
    }

    /**
     * 上传文件异常捕获
     *
     * @param request
     * @param response
     * @param e
     * @return
     * @throws IOException
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ModelAndView uploadException(HttpServletRequest request, HttpServletResponse response, MaxUploadSizeExceededException e) throws IOException {
        LOG.debug("MaxUploadSizeExceededException");
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return render(request, response, ResultData.build().code(HttpStatus.INTERNAL_SERVER_ERROR)
                .msg(BundleUtil.getString("net.mingsoft.basic.resources.resources","upload.max.size", CalculationUtil.convertSpaceUnit(this.maxFileSize))), e);
    }


    /**
     * 全局异常 未找到类404
     *
     * @param request
     * @param response
     * @param e
     * @return
     */
    @ExceptionHandler(value = NoHandlerFoundException.class)
    public ModelAndView handleNoHandlerFoundException(HttpServletRequest request, HttpServletResponse response, NoHandlerFoundException e) {
        LOG.debug("handleNoHandlerFoundException");
        return render(request, response, ResultData.build().code(HttpStatus.NOT_FOUND).msg("page 404"), e);
    }

    /**
     * 请求参数异常
     *
     * @param request
     * @param response
     * @param e
     * @return
     */
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ModelAndView handleMissingServletRequestParameterException(HttpServletRequest request, HttpServletResponse response, MissingServletRequestParameterException e) {
        LOG.debug("handleMissingServletRequestParameterException");
        return render(request, response, ResultData.build().code(HttpStatus.BAD_REQUEST).msg("request parameter err"), e);

    }

    /**
     * 请求方法类型错误
     *
     * @param request
     * @param response
     * @param e
     * @return
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ModelAndView handleHttpRequestMethodNotSupportedException(HttpServletRequest request, HttpServletResponse response, HttpRequestMethodNotSupportedException e) {
        LOG.debug("handleHttpRequestMethodNotSupportedException");

        return render(request, response, ResultData.build().code(HttpStatus.METHOD_NOT_ALLOWED).msg("request method not support"), e);
    }

    /**
     * 统一处理请求参数校验(实体对象传参)
     *
     * @param e BindException
     * @return ResultResponse
     */
    @ExceptionHandler(BindException.class)
    public ModelAndView handleValidExceptionHandler(HttpServletRequest request, HttpServletResponse response, BindException e) {
        LOG.debug("handleValidExceptionHandler");
        StringBuilder message = new StringBuilder();
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        for (FieldError error : fieldErrors) {
            message.append(error.getField()).append(error.getDefaultMessage()).append(",");
        }
        message = new StringBuilder(message.substring(0, message.length() - 1));

        return render(request, response, ResultData.build().code(HttpStatus.NOT_ACCEPTABLE).msg(message.toString()), e);
    }

    /**
     * 统一处理请求参数校验(普通传参)
     *
     * @param e ConstraintViolationException
     * @return ResultResponse
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ModelAndView handleConstraintViolationException(HttpServletRequest request, HttpServletResponse response, ConstraintViolationException e) {
        LOG.debug("handleConstraintViolationException");
        StringBuilder message = new StringBuilder();
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            Path path = violation.getPropertyPath();
            String[] pathArr = StringUtils.splitByWholeSeparatorPreserveAllTokens(path.toString(), ".");
            message.append(pathArr[1]).append(violation.getMessage()).append(",");
        }
        message = new StringBuilder(message.substring(0, message.length() - 1));

        return render(request, response, ResultData.build().code(HttpStatus.INTERNAL_SERVER_ERROR).msg(message.toString()), e);
    }


    /**
     * shiro权限未授权异常
     *
     * @param request
     * @param response
     * @param e
     * @return
     */
    @ExceptionHandler(value = UnauthorizedException.class)
    public ModelAndView handleUnauthorizedException(HttpServletRequest request, HttpServletResponse response, UnauthorizedException e) {
        LOG.debug("handleUnauthorizedException");
        return render(request, response, ResultData.build().code(HttpStatus.UNAUTHORIZED).msg("nauthorizedException"), e);
    }

    /**
     * shiro权限未授权异常
     *
     * @param request
     * @param response
     * @param e
     * @return
     */
    @ExceptionHandler(value = LockedAccountException.class)
    public ModelAndView handleLockedAccountException(HttpServletRequest request, HttpServletResponse response, LockedAccountException e) {
        LOG.debug("handleLockedAccountException");
        response.setStatus(HttpStatus.LOCKED.value());
        return render(request, response, ResultData.build().code(HttpStatus.LOCKED).msg("LockedAccountException"), e);
    }


    /**
     * 登录异常
     *
     * @param request
     * @param response
     * @param e
     * @return
     */
    @ExceptionHandler(value = AuthenticationException.class)
    public ModelAndView handleAuthenticationException(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) {
        LOG.debug("AuthenticationException");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        return render(request, response, ResultData.build().code(HttpStatus.UNAUTHORIZED).msg("AuthenticationException"), e);
    }

    /**
     * shiro权限错误
     *
     * @param request
     * @param response
     * @param e
     * @return
     */
    @ExceptionHandler(value = AuthorizationException.class)
    public ModelAndView handleAuthorizationException(HttpServletRequest request, HttpServletResponse response, AuthorizationException e) {
        LOG.debug("AuthorizationException");

        return render(request, response, ResultData.build().code(HttpStatus.UNAUTHORIZED).msg("AuthorizationException"), e);
    }


    /**
     * shiro权限错误
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = CredentialsException.class)
    public ModelAndView handleCredentialsException(HttpServletRequest request, HttpServletResponse response, CredentialsException e) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        return render(request, response, ResultData.build().code(HttpStatus.UNAUTHORIZED).msg("CredentialsException"), e);

    }


    /**
     * session失效异常
     *
     * @param request
     * @param response
     * @param e
     * @return
     */
    @ExceptionHandler(value = ExpiredSessionException.class)
    public ModelAndView handleExpiredSessionException(HttpServletRequest request, HttpServletResponse response, ExpiredSessionException e) {
        LOG.debug("ExpiredSessionException", e);
        response.setStatus(HttpStatus.GATEWAY_TIMEOUT.value());
        return render(request, response, ResultData.build().code(HttpStatus.GATEWAY_TIMEOUT), e);
    }

    /**
     * 返回异常信息处理
     *
     * @param request
     * @param response
     * @param e
     * @return
     */
    private ModelAndView render(HttpServletRequest request, HttpServletResponse response, ResultData resultData, Exception e) {
//        Map map = new HashMap();
//        map.put("cls", e.getStackTrace()[0] + ""); //出错的类
//        map.put("url", request.getServletPath()); //请求地址
//        map.put("code", ErrorCodeEnum.CLIENT_REQUEST);
//        map.put("result", false);
//        map.put("msg", message.toString());
//        map.put("exc", e.getClass()); //详细异常信息
        LOG.debug("url: {}",request.getRequestURI());
        e.printStackTrace();
        if (BasicUtil.isAjaxRequest(request)) {
            try {
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter writer = response.getWriter();
                writer.write(JSONObject.toJSONString(resultData));
                writer.flush();
                writer.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        } else {
            return new ModelAndView("/error/index", resultData);
        }

        return null;
    }

}
