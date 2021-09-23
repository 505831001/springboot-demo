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
import net.mingsoft.base.constant.Const;
import net.mingsoft.base.entity.ResultData;
import net.mingsoft.basic.util.BasicUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author by Administrator
 * @Description TODO
 * @date 2019/9/29 13:46
 */
public abstract class BaseFileAction extends net.mingsoft.basic.action.BaseAction{
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

    @Value("${ms.upload.denied}")
    private String uploadFileDenied;

    @Value("${ms.upload.template}")
    private String uploadTemplatePath;

    /**
     * 统一上传文件方法
     * @param config
     * @return
     * @throws IOException
     */
    public ResultData upload(Config config) throws IOException {
        // 过滤掉的文件类型
        String[] errorType = uploadFileDenied.split(",");
        //文件上传类型限制
        String fileName=config.getFile().getOriginalFilename();
        if(fileName.lastIndexOf(".")<0){
            LOG.info("文件格式错误:{}",fileName);
            return ResultData.build().error(getResString("err.error", new String[]{getResString("file.name")}));
        }
        String fileType=fileName.substring(fileName.lastIndexOf("."));
        boolean isReal = new File(uploadFloderPath).isAbsolute();
        //绝对路径
        String realPath = isReal? uploadFloderPath:config.uploadFloderPath?BasicUtil.getRealPath(""):BasicUtil.getRealPath(uploadFloderPath) ;
        //修改上传物理路径
        if(StringUtils.isNotBlank(config.getRootPath())){
            realPath=config.getRootPath();
        }
        //修改文件名
        if(!config.isRename()){
            fileName=config.getFile().getOriginalFilename();
            //Windows 系统下文件名最后会去掉. 这种文件默认拒绝  xxx.jsp. => xxx.jsp
            if(fileName.endsWith(".")&&System.getProperty("os.name").startsWith("Windows")){
                LOG.info("文件类型被拒绝:{}",fileName);
                return ResultData.build().error(getResString("err.error", new String[]{getResString("file.type")}));
            }
            fileType=fileName.substring(fileName.lastIndexOf("."));
        }else {
            //取随机名
            fileName=System.currentTimeMillis()+fileType;
        }
        for (String type : errorType) {
            if((fileType).equalsIgnoreCase(type)){
                LOG.info("文件类型被拒绝:{}",fileType);
                return ResultData.build().error(getResString("err.error", new String[]{getResString("file.type")}));
            }
        }
        // 上传的文件路径,判断是否填的绝对路径
        String uploadFolder = realPath +  File.separator;
        //修改upload下的上传路径
        if(StringUtils.isNotBlank(config.getUploadPath())){
            uploadFolder+=config.getUploadPath()+ File.separator;
        }
        //保存文件
        File saveFolder = new File(uploadFolder);
        File saveFile=new File(uploadFolder,fileName);
        if(!saveFolder.exists()){
            FileUtil.mkdir(saveFolder);
        }
        config.getFile().transferTo(saveFile);
        //绝对映射路径处理
        //如果UploadFloderPath = true则返回路径中不拼upload的路径
        String path = (config.isUploadFloderPath() ? "" : uploadMapping.replace("**",""))
                //转为相对路径
                + uploadFolder.replace(realPath,"")
                //添加文件名
                +  Const.SEPARATOR + fileName;
        //替换多余
        return ResultData.build().success(new File(Const.SEPARATOR + path).getPath().replace("\\","/").replace("//","/"));
    }

    public ResultData uploadTemplate(Config config) throws IOException {
        String[] errorType = uploadFileDenied.split(",");
        //文件上传类型限制
        //获取文件名字
        String fileName=config.getFile().getOriginalFilename();
        if(fileName.lastIndexOf(".")<0){
            LOG.info("文件格式错误:{}",fileName);
            return ResultData.build().error(getResString("err.error", new String[]{getResString("file.name")}));
        }
        //获取文件类型
        String fileType=fileName.substring(fileName.lastIndexOf("."));
        //判断上传路径是否为绝对路径
        boolean isReal = new File(uploadTemplatePath).isAbsolute();
        String realPath=null;
        if(!isReal){
            //如果不是就获取当前项目路径
            realPath=BasicUtil.getRealPath("");
        }
        else {
            //如果是就直接取改绝对路径
            realPath=uploadTemplatePath;
        }
        //修改文件名
        if(!config.isRename()){
            fileName=config.getFile().getOriginalFilename();
            //Windows 系统下文件名最后会去掉. 这种文件默认拒绝  xxx.jsp. => xxx.jsp
            if(fileName.endsWith(".")&&System.getProperty("os.name").startsWith("Windows")){
                LOG.info("文件类型被拒绝:{}",fileName);
                return ResultData.build().error(getResString("err.error", new String[]{getResString("file.type")}));
            }
            fileType=fileName.substring(fileName.lastIndexOf("."));
        }else {
            //取随机名
            fileName=System.currentTimeMillis()+fileType;
        }
        for (String type : errorType) {
            //校验禁止上传的文件后缀名（忽略大小写）
            if((fileType).equalsIgnoreCase(type)){
                LOG.info("文件类型被拒绝:{}",fileType);
                return ResultData.build().error(getResString("err.error", new String[]{getResString("file.type")}));
            }
        }
        // 上传的文件路径,判断是否填的绝对路径
        String uploadFolder = realPath +  File.separator;
        //修改upload下的上传路径
        if(StringUtils.isNotBlank(config.getUploadPath())){
            uploadFolder+=config.getUploadPath()+ File.separator;
        }
        //保存文件
        File saveFolder = new File(uploadFolder);
        File saveFile=new File(uploadFolder,fileName);
        if(!saveFolder.exists()){
            FileUtil.mkdir(saveFolder);
        }
        config.getFile().transferTo(saveFile);
        //绝对映射路径处理
        String path= uploadFolder.replace(realPath,"")
                //添加文件名
                +  Const.SEPARATOR + fileName;
        //替换多余
        return ResultData.build().success(new File(Const.SEPARATOR + path).getPath().replace("\\","/").replace("//","/"));
    }
    public class Bean{
        /**
         * 上传文件夹
         */
        private String uploadPath;
        private MultipartFile file;

        /**
         * 文件重命名
         */
        private boolean rename = true;
        /**
         * 上传地址拼接appId
         */
        private boolean appId = false;

        public boolean isAppId() {
            return appId;
        }

        public void setAppId(boolean appId) {
            this.appId = appId;
        }

        public boolean isRename() {
            return rename;
        }

        public void setRename(boolean rename) {
            this.rename = rename;
        }

        public String getUploadPath() {
            return uploadPath;
        }

        public void setUploadPath(String uploadPath) {
            this.uploadPath = uploadPath;
        }

        public MultipartFile getFile() {
            return file;
        }

        public void setFile(MultipartFile file) {
            this.file = file;
        }
    }

    /**
     *上传配置
     */
    public class Config extends Bean{

        /**
         * 上传根目录，由业务决定
         */
        private String rootPath;
        /**
         * 是否重定向到项目目录,针对老版本兼容的临时处理
         */
        private boolean uploadFloderPath;
        public Config() {
        }
        public Config(String fileName, String rootPath) {
            this.rootPath = rootPath;
        }

        public Config(String uploadPath, MultipartFile file, String rootPath,boolean uploadFloderPath) {
            this.rootPath = rootPath;
            this.uploadFloderPath = uploadFloderPath;
            this.setUploadPath(uploadPath);
            this.setFile(file);
        }
        public Config(String uploadPath, MultipartFile file, String rootPath) {
            this.rootPath = rootPath;
            this.setUploadPath(uploadPath);
            this.setFile(file);
        }

        public Config(String uploadPath, MultipartFile file, String rootPath, boolean uploadFloderPath, boolean rename) {
            this.rootPath = rootPath;
            this.uploadFloderPath = uploadFloderPath;
            this.setUploadPath(uploadPath);
            this.setFile(file);
            this.setRename(rename);
        }

        public String getRootPath() {
            return rootPath;
        }

        public void setRootPath(String rootPath) {
            this.rootPath = rootPath;
        }

        public boolean isUploadFloderPath() {
            return uploadFloderPath;
        }

        public void setUploadFloderPath(boolean uploadFloderPath) {
            this.uploadFloderPath = uploadFloderPath;
        }
    }

}
