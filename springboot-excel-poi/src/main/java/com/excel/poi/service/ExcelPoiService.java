package com.excel.poi.service;

import com.excel.poi.utils.ResultData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author Liuweiwei
 * @since 2021-07-04
 */
public interface ExcelPoiService {
    /**
     * Excel文档导入
     *
     * @param file
     * @return
     */
    ResponseEntity uploadExcel(MultipartFile file);

    /**
     * Excel文档导入
     *
     * @param file
     * @return
     * @throws IOException
     */
    ResultData importExcel(MultipartFile file) throws IOException;

    /**
     * Excel导出
     *
     * @param response
     */
    void downloadExcel(HttpServletResponse response);

    /**
     * Excel导出
     *
     * @param response
     */
    void exportExcel(HttpServletResponse response) throws IOException;
}
