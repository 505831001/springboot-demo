package com.excel.poi.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.excel.poi.dao.TbUserMapper;
import com.excel.poi.entity.TbUser;
import com.excel.poi.service.ExcelPoiService;
import com.excel.poi.utils.Utils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Liuweiwei
 * @since 2021-07-04
 */
@Service
public class ExcelPoiServiceImpl implements ExcelPoiService {

    @Autowired
    private TbUserMapper tbUserMapper;

    @Override
    public ResponseEntity uploadExcel(MultipartFile file) {
        if (!Utils.checkExtension(file)) {
            return new ResponseEntity("请求文件类型错误:后缀名错误(xls,xlsx,XLS,XLSX).", HttpStatus.BAD_REQUEST);
        }
        try {
            if (Utils.isOfficeFile(file)) {
                // 1.High level representation of a Excel workbook. - Excel[工作簿]的高级表示。
                Workbook workbook = Utils.getWorkbookAuto(file);
                // 2.Sheets are the central structures within a workbook. - [工作表]是工作簿中的中心结构。
                Sheet sheet = workbook.getSheetAt(0);
                // 3.High level representation of a row of a spreadsheet. - 电子[表格行]的高级表示。
                for (Row row : sheet) {
                    short lastCellNum = row.getLastCellNum();
                    for (int i = 0; i < lastCellNum; i++) {
                        // 4.High level representation of a cell in a row of a spreadsheet. - 电子表格中一行[单元格]的高级表示。
                        Cell cell = row.getCell(i);
                        System.out.print("[Cell01] -> " + cell.toString() + ",");
                    }
                }
                System.out.println("");
                //获得sheet有多少行
                int rows = sheet.getPhysicalNumberOfRows();
                //遍历sheet表格行数rows
                for (int i = 0; i < rows; i++) {
                    // 3.High level representation of a row of a spreadsheet. - 电子[表格行]的高级表示。
                    Row row = sheet.getRow(i);
                    short lastCellNum = row.getLastCellNum();
                    for (int j = 0; j < lastCellNum; j++) {
                        // 4.High level representation of a cell in a row of a spreadsheet. - 电子表格中一行[单元格]的高级表示。
                        Cell cell = row.getCell(j);
                        if (cell != null) {
                            System.out.print("[Cell02] -> " + cell.toString() + ",");
                        }
                    }
                }
            } else {
                return new ResponseEntity("请求文件类型错误:文件类型错误(office文件)", HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @Override
    public ResponseEntity importExcel(MultipartFile file) throws IOException {
        //流读取文件
        FileInputStream is = new FileInputStream(new File("C:/Users/Administrator/Downloads/userinfo.xls"));
        // 1.High level representation of a Excel workbook. - Excel[工作簿]的高级表示。
        Workbook workbook = new HSSFWorkbook(is);
        // 2.Sheets are the central structures within a workbook. - [工作表]是工作簿中的中心结构。
        Sheet sheet = workbook.getSheetAt(0);
        // 3.High level representation of a row of a spreadsheet. - 电子[表格行]的高级表示。
        for (Row row : sheet) {
            short lastCellNum = row.getLastCellNum();
            for (int i = 0; i < lastCellNum; i++) {
                // 4.High level representation of a cell in a row of a spreadsheet. - 电子表格中一行[单元格]的高级表示。
                Cell cell = row.getCell(i);
                System.out.println("[Cell01] -> " + cell.toString());
            }
        }

        //获得sheet有多少行
        int rows = sheet.getPhysicalNumberOfRows();
        //遍历所有行
        for (int i = 0; i < rows; i++) {
            // 3.High level representation of a row of a spreadsheet. - 电子[表格行]的高级表示。
            Row row = sheet.getRow(i);
            short lastCellNum = row.getLastCellNum();
            for (int j = 0; j < lastCellNum; j++) {
                // 4.High level representation of a cell in a row of a spreadsheet. - 电子表格中一行[单元格]的高级表示。
                Cell cell = row.getCell(j);
                System.out.println("[Cell02] -> " + cell.toString());
            }
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    public ResponseEntity<Void> provinceAndCitySheet(MultipartFile file, HttpServletRequest request) {
        try {
            File myFile = new File("d:\\"+ UUID.randomUUID()+file.getOriginalFilename());
            file.transferTo(myFile);
            FileInputStream is = new FileInputStream(myFile);
            List<TbUser> list = new ArrayList<>();

            // 1.High level representation of a Excel workbook. - Excel[工作簿]的高级表示。
            HSSFWorkbook workbook = new HSSFWorkbook(is);
            // 2.Sheets are the central structures within a workbook. - [工作表]是工作簿中的中心结构。
            HSSFSheet sheet = workbook.getSheetAt(0);
            // 3.High level representation of a row of a spreadsheet. - 电子[表格行]的高级表示。
            for (Row row : sheet) {
                //第一行表头跳过
                if (row.getRowNum() == 0) {
                    continue;
                }
                //跳过空值的行，要求此行作废
                if (row.getCell(0) == null || StringUtils.isBlank(row.getCell(0).getStringCellValue())) {
                    continue;
                }
                TbUser tbUser = new TbUser();
                tbUser.setId(Long.getLong(row.getCell(0).getStringCellValue()));
                tbUser.setUsername(row.getCell(1).getStringCellValue());
                tbUser.setPassword(row.getCell(2).getStringCellValue());
                tbUser.setRole(row.getCell(3).getStringCellValue());
                tbUser.setPermission(row.getCell(4).getStringCellValue());
                tbUser.setBan(row.getCell(5).getStringCellValue());
                tbUser.setPhone(row.getCell(6).getStringCellValue());
                tbUser.setEmail(row.getCell(7).getStringCellValue());
                tbUser.setCreated(row.getCell(8).getDateCellValue());
                tbUser.setUpdated(row.getCell(9).getDateCellValue());
                list.add(tbUser);
            }
            // 调用业务层
            for (TbUser tbUser : list) {
                tbUserMapper.insert(tbUser);
            }
            //删除文件
            myFile.delete();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            //服务器错误
            return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void downloadExcel(HttpServletResponse response) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("用户信息表");
        List<TbUser> tbUserList = tbUserMapper.selectList(null);
        //设置要导出的文件的名字
        String fileName = "userinfo" + ".xls";
        //新增数据行，并且设置单元格数据
        int rowNum = 1;
        String[] headers = {"id", "username", "password", "role", "permission", "ban", "phone", "email", "created", "updated"};
        //headers表示excel表中第一行的表头
        HSSFRow row = sheet.createRow(0);
        //在excel表中添加表头
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }
        //在表中存放查询到的数据放入对应的列
        for (TbUser tbUser : tbUserList) {
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(tbUser.getId() == null ? null : tbUser.getId());
            row1.createCell(1).setCellValue(tbUser.getUsername() == null ? null : tbUser.getUsername());
            row1.createCell(2).setCellValue(tbUser.getPassword() == null ? null : tbUser.getPassword());
            row1.createCell(3).setCellValue(tbUser.getRole() == null ? null : tbUser.getRole());
            row1.createCell(4).setCellValue(tbUser.getPermission() == null ? null : tbUser.getPermission());
            row1.createCell(5).setCellValue(tbUser.getBan() == null ? null : tbUser.getBan());
            row1.createCell(6).setCellValue(tbUser.getPhone() == null ? null : tbUser.getPhone());
            row1.createCell(7).setCellValue(tbUser.getEmail() == null ? null : tbUser.getEmail());
            row1.createCell(8).setCellValue(tbUser.getCreated() == null ? null : tbUser.getCreated());
            row1.createCell(9).setCellValue(tbUser.getUpdated() == null ? null : tbUser.getUpdated());
            rowNum++;
        }
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        try {
            response.flushBuffer();
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exportExcel(HttpServletResponse response) throws IOException {
        // 1.High level representation of a Excel workbook. - Excel工作簿的高级表示。
        Workbook workbook = new HSSFWorkbook();
        // 2.Sheets are the central structures within a workbook. - 工作表是工作簿中的中心结构。
        Sheet sheet = workbook.createSheet();
        // 3.High level representation of a row of a spreadsheet. - 电子表格行的高级表示。
        Row row = sheet.createRow(3);
        // 4.High level representation of a cell in a row of a spreadsheet. - 电子表格中一行[单元格]的高级表示。
        Cell cell = row.createCell(3);

        //设置内容
        cell.setCellValue("一统江湖");
        //设置内容格式
        Font font = workbook.createFont();
        //以像素点的方式设置字体大小
        font.setFontHeightInPoints((short) 24);
        //设置字体
        font.setFontName("华文彩云");

        //System.out.println(Short.MIN_VALUE+"-"+Short.MAX_VALUE);
        //创建格式
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);

        //将cellStyle给cell
        cell.setCellStyle(cellStyle);
        //保存
        FileOutputStream stream = new FileOutputStream(new File("d://a.xls"));
        workbook.write(stream);
        stream.flush();
        stream.close();
        System.out.println("运行结束");
    }
}
