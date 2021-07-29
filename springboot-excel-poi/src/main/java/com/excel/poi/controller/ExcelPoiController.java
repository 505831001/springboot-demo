package com.excel.poi.controller;

import com.excel.poi.service.ExcelPoiService;
import com.excel.poi.utils.ResultData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Liuweiwei
 * @since 2021-07-04
 */
@RestController
@Api(value = "", tags = "Excel-Poi导入导出控制器")
public class ExcelPoiController {

    @Autowired
    ExcelPoiService excelPoiService;

    @RequestMapping(value = "/uploadExcel", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "上传按钮", notes = "上传按钮", tags = "")
    public ResponseEntity<Object> uploadExcel(@RequestParam("file") MultipartFile file) {
        return excelPoiService.uploadExcel(file);
    }

    @RequestMapping(value = "downloadExcel", method = RequestMethod.GET)
    @ApiOperation(value = "下载按钮", notes = "下载按钮", tags = "")
    public void downloadExcel(HttpServletResponse response) {
        excelPoiService.downloadExcel(response);
    }

    @RequestMapping(value = "/importExcel", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "导入按钮", notes = "导入按钮", tags = "")
    public ResultData importExcel(@RequestParam("file") MultipartFile file) {
        try {
            return excelPoiService.importExcel(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultData.success();
    }

    @RequestMapping(value = "exportExcel", method = RequestMethod.GET)
    @ApiOperation(value = "导出按钮", notes = "导出按钮", tags = "")
    public void exportExcel(HttpServletResponse response) {
        try {
            excelPoiService.exportExcel(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
