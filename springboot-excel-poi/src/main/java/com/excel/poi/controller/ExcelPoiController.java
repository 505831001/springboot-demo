package com.excel.poi.controller;

import com.excel.poi.service.ExcelPoiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Liuweiwei
 * @since 2021-07-04
 */
@RestController
public class ExcelPoiController {

    @Autowired
    ExcelPoiService excelPoiService;

    @RequestMapping(value = "/uploadExcel", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> uploadExcel(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        return excelPoiService.uploadExcel(file);
    }

    @RequestMapping(value = "downloadExcel", method = RequestMethod.GET)
    public void downloadExcel(HttpServletResponse response) {
        excelPoiService.downloadExcel(response);
    }
}
