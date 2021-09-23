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

package net.mingsoft.basic.aop;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import net.mingsoft.base.entity.BaseEntity;
import net.mingsoft.basic.annotation.LogAnn;
import net.mingsoft.basic.biz.ILogBiz;
import net.mingsoft.basic.entity.LogEntity;
import net.mingsoft.basic.util.BasicUtil;
import net.mingsoft.basic.util.SpringUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.Date;
import java.util.Map;

/**
 *
 * @author by 铭软开发团队
 * @Description TODO
 * @date 2019/11/20 12:04
 */
@Aspect
public abstract class BaseLogAop extends BaseAop{

    /**
     * 获取用户名
     * @return
     */
    public abstract String getUserName();

    /**
     * 是否切面
     * @return
     */
    public abstract boolean isCut(LogAnn log);

    /**
     * 切入点
     */
    @Pointcut("@annotation(net.mingsoft.basic.annotation.LogAnn)")
    public void logPointCut()
    { }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "logPointCut()", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, Object result)
    {
        handleLog(joinPoint, null, result);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e 异常
     */
    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e)
    {
        handleLog(joinPoint, e, null);
    }
    /**
     * 成功状态
     */
    private static final String SUCCESS="success";
    /**
     * 失败状态
     */
    private static final String ERROR="error";

    /**
     * 日志业务层
     */
    @Autowired
    private ILogBiz logBiz;

    private static final Logger LOG = LoggerFactory.getLogger(SystemLogAop.class);

    protected void handleLog(final JoinPoint joinPoint, final Exception e, Object result) {
        try{
            // 获得注解
            LogAnn controllerLog = getAnnotation(joinPoint, LogAnn.class);
            if (controllerLog == null){
                return;
            }
            if(!isCut(controllerLog)){
                return;
            }
            LogEntity logEntity = new LogEntity();
            logEntity.setLogUser(getUserName());
            logEntity.setLogStatus(SUCCESS);
            // 请求的地址
            String ip = BasicUtil.getIp();
            //设置IP
            logEntity.setLogIp(ip);
            // 过滤id
            SimplePropertyPreFilter classAFilter = new SimplePropertyPreFilter(BaseEntity.class, "id");
            SerializeFilter[] filters =new SerializeFilter[]{classAFilter};
            //设置返回参数
            logEntity.setLogResult(JSONObject.toJSONString(result,filters, SerializerFeature.PrettyFormat,
                    SerializerFeature.WriteDateUseDateFormat));
            //设置请求地址
            logEntity.setLogUrl(SpringUtil.getRequest().getRequestURI());

            if (e != null){
                logEntity.setLogStatus(ERROR);
                logEntity.setLogErrorMsg(StringUtils.substring(e.getMessage(), 0, 4000));
            }
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            logEntity.setLogMethod(className + "." + methodName + "()");
            // 设置请求方式
            logEntity.setLogRequestMethod(SpringUtil.getRequest().getMethod());

            // 设置action动作
            logEntity.setLogBusinessType(controllerLog.businessType().name().toLowerCase());
            // 设置标题
            logEntity.setLogTitle(controllerLog.title());
            // 设置操作人类别
            logEntity.setLogUserType(controllerLog.operatorType().name().toLowerCase());
            // 是否需要保存request，参数和值
            if (controllerLog.isSaveRequestData()){
                // 获取参数的信息，传入到数据库中。
                boolean isJson =StringUtils.isNotBlank(SpringUtil.getRequest().getContentType())&&MediaType.valueOf(SpringUtil.getRequest().getContentType()).includes(MediaType.APPLICATION_JSON);
                //如果是json请求参数需要获取方法体上的参数
                if(isJson){
                    Object jsonParam = getJsonParam(joinPoint);
                    if(ObjectUtil.isNotNull(jsonParam)){
                        String jsonString = JSONObject.toJSONString(jsonParam, SerializerFeature.PrettyFormat,
                                SerializerFeature.WriteDateUseDateFormat);
                        logEntity.setLogParam(StringUtils.substring(jsonString, 0, 4000));
                    }
                }else {
                    Map<String, String[]> map = SpringUtil.getRequest().getParameterMap();
                    String params =JSONObject.toJSONString(map, SerializerFeature.PrettyFormat,
                            SerializerFeature.WriteDateUseDateFormat);
                    logEntity.setLogParam(StringUtils.substring(params, 0, 4000));
                }
            }
            logEntity.setCreateDate(new Date());
            logBiz.saveData(logEntity);
        }
        catch (Exception exp){
            LOG.error("日志记录错误:{}", exp.getMessage());
            exp.printStackTrace();
        }
    }

}
