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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.mingsoft.base.entity.ResultData;
import net.mingsoft.basic.action.BaseFileAction;
import net.mingsoft.basic.util.BasicUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * 上传文件
 */
@Api("上传文件接口")
@Controller
@RequestMapping("/file")
public class FileAction extends BaseFileAction {


    @Value("${ms.upload.denied}")
    private String uploadFileDenied;

    @Value("${ms.upload.enable-web:true}")
    private Boolean uploadEnable;


    @ApiOperation(value = "处理post请求上传文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uploadPath", value = "上传文件夹地址", required =false,paramType="query"),
            @ApiImplicitParam(name = "file", value = "文件流", required =false,paramType="query"),
            @ApiImplicitParam(name = "rename", value = "是否重命名", required =false,paramType="query",defaultValue="true"),
            @ApiImplicitParam(name = "appId", value = "上传路径是否需要拼接appId", required =false,paramType="query",defaultValue="false"),
    })
    @PostMapping("/upload")
    @ResponseBody
    public ResultData upload(Bean bean, HttpServletRequest req, HttpServletResponse res) throws IOException {
        if(uploadEnable){
            //非法路径过滤
            if(bean.getUploadPath()!=null&&(bean.getUploadPath().contains("../")||bean.getUploadPath().contains("..\\"))){
                return ResultData.build().error(getResString("err.error", new String[]{getResString("file.path")}));
            }
            // 是否需要拼接appId
            if(bean.isAppId()){
                bean.setUploadPath(BasicUtil.getApp().getAppId()+ File.separator+ bean.getUploadPath()) ;
            }
            Config config = new Config(bean.getUploadPath(),bean.getFile(),null,false,bean.isRename());
            return this.upload(config);
        }else {
            return ResultData.build().error(getResString("insufficient.permissions"));
        }

    }
}
