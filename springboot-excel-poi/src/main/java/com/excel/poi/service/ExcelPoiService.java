package com.excel.poi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.excel.poi.entity.TbUser;
import com.excel.poi.utils.ResultData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Liuweiwei
 * @since 2021-07-04
 */
public interface ExcelPoiService extends IService<TbUser> {
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
    void downloadExcel(HttpServletResponse response) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException;

    /**
     * Excel导出
     *
     * @param response
     */
    void exportExcel(HttpServletResponse response);
}
