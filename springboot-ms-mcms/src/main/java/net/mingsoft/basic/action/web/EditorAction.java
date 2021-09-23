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

import com.alibaba.fastjson.JSON;
import com.mingsoft.ueditor.MsUeditorActionEnter;
import net.mingsoft.basic.util.BasicUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Map;

/**
 * 百度编辑器上传
 * @author 铭软开发团队
 * @date 2019年7月16日
 */
@Controller("ueAction")
@RequestMapping("/static/plugins/ueditor/{version}/jsp")
public class EditorAction {

    /**
     * 上传路径
     */
    @Value("${ms.upload.path}")
    private String uploadFloderPath;

    /**
     * 上传路径映射
     */
    @Value("${ms.upload.mapping}")
    private String uploadMapping;
    @ResponseBody
    @RequestMapping("editor")
    public String editor(HttpServletRequest request, HttpServletResponse response, String jsonConfig){
        String rootPath = BasicUtil.getRealPath("");
        //如果是绝对路径就直接使用配置的绝对路径
        File saveFloder=new File(uploadFloderPath);
        if (saveFloder.isAbsolute()) {
            rootPath = saveFloder.getPath();
            //因为绝对路径已经映射了路径所以隐藏
            jsonConfig = jsonConfig.replace("{ms.upload}", "");
        } else {
            //如果是相对路径替换成配置的路径
            jsonConfig = jsonConfig.replace("{ms.upload}","/"+uploadFloderPath);
        }
        //执行exec()方法才保存文件
        String json = new MsUeditorActionEnter(request, rootPath, jsonConfig, BasicUtil.getRealPath("")).exec();
        if (saveFloder.isAbsolute()) {
            //如果是配置的绝对路径需要在前缀加上映射路径
            Map data = (Map) JSON.parse(json);
            data.put("url", uploadMapping.replace("/**", "") + data.get("url"));
            return JSON.toJSONString(data);
        }
        return json;
    }
}
