/**
 * The MIT License (MIT) * Copyright (c) 2020 铭软科技(mingsoft.net)

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
package net.mingsoft.mdiy.action.web;

import io.swagger.annotations.Api;
import net.mingsoft.base.entity.ResultData;
import net.mingsoft.mdiy.action.BaseAction;
import net.mingsoft.mdiy.util.ConfigUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义配置表管理控制层
 * @author 铭飞开发团队
 * @version
 * 版本号：1<br/>
 * 创建日期：2017-8-12 15:58:29<br/>
 * 历史修订：<br/>
 */
@Api(value = "自定义配置接口")
@Controller("webMdiyConfig")
@RequestMapping("/mdiy/config")
public class ConfigAction extends BaseAction {


    /**
     *  获取配置中的key指定value值
     * @param configName 配置名称
     * @param key 配置的key值
     * @param response
     * @param request
     * @return
     */
    @GetMapping("/get")
    @ResponseBody
    public ResultData get(String configName,String key, HttpServletResponse response, HttpServletRequest request){
        if (StringUtils.isEmpty(configName) || StringUtils.isEmpty(key)) {
            return ResultData.build().error(getResString("err.empty",getResString("config.name")));
        }
        return ResultData.build().success(ConfigUtil.getString(configName,key));
    }

}
