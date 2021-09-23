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

package net.mingsoft.basic.action;

import cn.hutool.core.io.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.mingsoft.base.entity.ResultData;
import net.mingsoft.basic.annotation.LogAnn;
import net.mingsoft.basic.constant.e.BusinessTypeEnum;
import net.mingsoft.basic.util.BasicUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 *
 * 上传文件
 */
@Api("上传文件接口")
@Controller("ManageFileAction")
@RequestMapping("${ms.manager.path}/file")
public class ManageFileAction extends BaseFileAction {


    @Value("${ms.upload.template}")
    private String uploadTemplatePath;


    /**
     * 处理post请求上传文件
     * 可以自定义项目路径下任意文件夹
     * @param req
     *            HttpServletRequest对象
     * @param res
     *            HttpServletResponse 对象
     * @throws ServletException
     *             异常处理
     * @throws IOException
     *             异常处理
     */
    @ApiOperation(value = "处理post请求上传文件")
    @LogAnn(title = "处理post请求上传文件",businessType= BusinessTypeEnum.OTHER)
    @PostMapping("/upload")
    @ResponseBody
    public ResultData upload(Bean bean, boolean uploadFloderPath, HttpServletRequest req, HttpServletResponse res) throws IOException {
        //非法路径过滤
        if(checkUploadPath(bean)){
            return ResultData.build().error();
        }

        Config config = new Config(bean.getUploadPath(),bean.getFile(),null,uploadFloderPath,bean.isRename());
        return this.upload(config);
    }

    @ApiOperation(value = "处理post请求上传模板文件")
    @PostMapping("/uploadTemplate")
    @ResponseBody
    public ResultData uploadTemplate(Bean bean, boolean uploadFloderPath, HttpServletResponse res) throws IOException {
        //非法路径过滤
        if(checkUploadPath(bean)){
            return ResultData.build().error(getResString("err.error", new String[]{getResString("file.type")}));
        }
        if (StringUtils.isEmpty(bean.getUploadPath())) {
            bean.setUploadPath(uploadTemplatePath + File.separator +  BasicUtil.getApp().getAppId());
        }

        Config config = new Config(bean.getUploadPath(), bean.getFile(),null, uploadFloderPath, bean.isRename());
        return this.uploadTemplate(config);
    }

    protected boolean checkUploadPath(Bean bean){
        return (bean.getUploadPath()!=null&&(bean.getUploadPath().contains("../")||bean.getUploadPath().contains("..\\")));
    }

    @ApiOperation(value = "处理WEB-INF文件下载接口")
    @PostMapping(value = "/download")
    public void download(String filePath, HttpServletResponse response) {
        //检测空
        if (StringUtils.isEmpty(filePath)) {
            return;
        }
        //检测非法路径
        if (filePath.contains("../") || filePath.contains("..\\")) {
            return;
        }

        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
        try {
            fileName = new String(fileName.getBytes("UTF-8"),"ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        try {
            FileUtil.writeToStream(BasicUtil.getRealPath("WEB-INF/" + filePath), response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
