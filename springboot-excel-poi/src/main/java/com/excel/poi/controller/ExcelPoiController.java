package com.excel.poi.controller;

import com.excel.poi.service.ExcelPoiService;
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
public class ExcelPoiController {

    @Autowired
    ExcelPoiService excelPoiService;

    @RequestMapping(value = "/uploadExcel", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> uploadExcel(@RequestParam("file") MultipartFile file) {
        return excelPoiService.uploadExcel(file);
    }

    @RequestMapping(value = "downloadExcel", method = RequestMethod.GET)
    public void downloadExcel(HttpServletResponse response) {
        excelPoiService.downloadExcel(response);
    }

    @RequestMapping(value = "/importExcel", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> importExcel(@RequestParam("file") MultipartFile file) {
        try {
            return excelPoiService.importExcel(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(Object.class);
    }

    @RequestMapping(value = "exportExcel", method = RequestMethod.GET)
    public void exportExcel(HttpServletResponse response) {
        try {
            excelPoiService.exportExcel(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
