package com.liuweiwei.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

/**
 * @author Liuweiwei
 * @since 2021-03-23
 */
@RestController
@Api(value = "", tags = "文件控制器")
@Log4j2
public class FileController {

    private String folder = "C:\\Users\\Administrator\\Documents";

    @PostMapping(value = "/upload")
    @ApiOperation(value = "文件上传", notes = "文件上传", tags = "")
    public String upload(MultipartFile file) throws IOException {
        log.info(file.getName());
        log.info(file.getOriginalFilename());
        log.info(String.valueOf(file.getSize()));

        File localFile = new File(folder, new Date().toLocaleString() + ".txt");
        file.transferTo(localFile);
        String path = localFile.getAbsolutePath();
        return path;
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "文件下载", notes = "文件下载", tags = "")
    public Integer download(@PathVariable(value = "id") String id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        File file = new File(folder, id + ".txt");
        FileInputStream inputStream = new FileInputStream(file);
        ServletOutputStream outputStream = response.getOutputStream();
        response.setContentType("application/x-download");
        response.addHeader("Content-Disposition", "attachment;filename=test.txt");
        int copy = IOUtils.copy(inputStream, outputStream);
        outputStream.flush();
        return copy;
    }
}
