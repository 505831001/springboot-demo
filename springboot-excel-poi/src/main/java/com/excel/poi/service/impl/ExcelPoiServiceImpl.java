package com.excel.poi.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.BeanUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.excel.poi.constants.NumericConstants;
import com.excel.poi.dao.TbUserMapper;
import com.excel.poi.entity.TbUser;
import com.excel.poi.service.ExcelPoiService;
import com.excel.poi.utils.Utils;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Liuweiwei
 * @since 2021-07-04
 */
@Service
public class ExcelPoiServiceImpl implements ExcelPoiService {

    @Autowired
    private TbUserMapper tbUserMapper;

    /**
     * Excel文档导入方式之一
     *
     * @param file
     * @return
     */
    @Override
    public ResponseEntity uploadExcel(MultipartFile file) {
        List<TbUser> list = new ArrayList<>(10);
        int index = 0;
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
                    /**获得sheet行有多少单元格*/
                    short lastCellNum = row.getLastCellNum();
                    for (int i = 0; i < lastCellNum; i++) {
                        // 4.High level representation of a cell in a row of a spreadsheet. - 电子表格中一行[单元格]的高级表示。
                        Cell cell = row.getCell(i);
                        System.out.println("单元格[Cell]数据如何添加到对象保存数据表：" + cell.toString());
                    }
                }

                /**获得sheet有多少行*/
                int rows = sheet.getPhysicalNumberOfRows();
                for (int i = 0; i < rows; i++) {
                    // 3.High level representation of a row of a spreadsheet. - 电子[表格行]的高级表示。
                    Row row = sheet.getRow(i);
                    /**获得sheet每行有多少单元格*/
                    int cells = row.getPhysicalNumberOfCells();
                    for (int j = 0; j < cells; j++) {
                        // 4.High level representation of a cell in a row of a spreadsheet. - 电子表格中一行[单元格]的高级表示。
                        Cell cell = row.getCell(j);
                        System.out.println("单元格[Cell]数据如何添加到对象保存数据表：" + cell.toString());
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

    /**
     * Excel文档导入方式之二
     *
     * @param file
     * @return
     * @throws IOException
     */
    @Override
    public ResponseEntity importExcel(MultipartFile file) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>(10);
        /**sheet工作簿title行cells单元格*/
        String[] headers = {"id", "username", "password", "role", "permission", "ban", "phone", "email", "created", "updated"};
        /**流读取文件*/
        BufferedInputStream is = new BufferedInputStream(file.getInputStream());
        // 1.High level representation of a Excel workbook. - Excel[工作簿]的高级表示。
        Workbook workbook = new HSSFWorkbook(is);
        // 2.Sheets are the central structures within a workbook. - [工作表]是工作簿中的中心结构。
        Sheet sheet = workbook.getSheetAt(0);

        // 3.High level representation of a row of a spreadsheet. - 电子[表格行]的高级表示。
        for (Row row : sheet) {
            /**获得sheet每行有多少单元格*/
            short lastCellNum = row.getLastCellNum();
            for (int i = 0; i < lastCellNum; i++) {
                // 4.High level representation of a cell in a row of a spreadsheet. - 电子表格中一行[单元格]的高级表示。
                Cell cell = row.getCell(i);
            }
            /**获得sheet每行有多少单元格*/
            int cells = row.getPhysicalNumberOfCells();
            for (int i = 0; i < cells; i++) {
                // 4.High level representation of a cell in a row of a spreadsheet. - 电子表格中一行[单元格]的高级表示。
                Cell cell = row.getCell(i);
            }
        }

        /**获得sheet有多少行*/
        int rows = sheet.getPhysicalNumberOfRows();
        /**遍历所有行*/
        for (int i = 0; i < rows; i++) {
            /**第一行表头跳过*/
            if (i == 0) {
                continue;
            }
            // 3.High level representation of a row of a spreadsheet. - 电子[表格行]的高级表示。
            Row row = sheet.getRow(i);
            Map<String, Object> map = new HashMap<>();
            for (int j = 0; j < headers.length; j++) {
                // 4.High level representation of a cell in a row of a spreadsheet. - 电子表格中一行[单元格]的高级表示。
                Cell cell = row.getCell(j);
                map.put(headers[j], cell.toString() == null ? null : cell.toString());
            }
            list.add(map);
            /**转换成对象写入数据库*/
            TbUser tbUser = cellsToTbUser(row);
        }
        /**转换成集合或转换成json*/
        System.out.println("总计行：" + list.size());
        for (Map<String, Object> map : list) {
            String json = JSONObject.toJSONString(map);
            System.out.println("行内容：" + json);
            TbUser tbUser = JSONObject.parseObject(json, TbUser.class);
            System.out.println("行对象：" + tbUser.toString());
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
                /**第一行表头跳过*/
                if (row.getRowNum() == 0) {
                    continue;
                }
                /**跳过空值的行，要求此行作废*/
                if (row.getCell(0) == null || StringUtils.isBlank(row.getCell(0).getStringCellValue())) {
                    continue;
                }
                TbUser tbUser = cellsToTbUser(row);
                list.add(tbUser);
            }
            /**调用业务层*/
            for (TbUser tbUser : list) {
                tbUserMapper.insertSelective(tbUser);
            }
            /**删除文件*/
            myFile.delete();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            /**服务器错误*/
            return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private TbUser cellsToTbUser(Row row) {
        TbUser tbUser = new TbUser();
        int id = UUID.randomUUID().toString().hashCode();
        id = id < 0 ? -id : id;
        tbUser.setId(Long.parseLong(String.valueOf(id)));
        tbUser.setUsername(row.getCell(NumericConstants.ONE)    == null ? null : row.getCell(1).getStringCellValue());
        tbUser.setPassword(row.getCell(NumericConstants.TWO)    == null ? null : row.getCell(2).getStringCellValue());
        tbUser.setRole(row.getCell(NumericConstants.THREE)      == null ? null : row.getCell(3).getStringCellValue());
        tbUser.setPermission(row.getCell(NumericConstants.FOUR) == null ? null : row.getCell(4).getStringCellValue());
        tbUser.setBan(row.getCell(NumericConstants.FIVE)        == null ? null : row.getCell(5).getStringCellValue());
        tbUser.setPhone(row.getCell(NumericConstants.SIX)       == null ? null : row.getCell(6).getStringCellValue());
        tbUser.setEmail(row.getCell(NumericConstants.SEVEN)     == null ? null : row.getCell(7).getStringCellValue());
        tbUser.setCreated(row.getCell(NumericConstants.EIGHT)   == null ? null : new Date());
        tbUser.setUpdated(row.getCell(NumericConstants.NINE)    == null ? null : new Date());
        return tbUser;
    }

    /**
     * Excel导出方式之一
     *
     * @param response
     */
    @Override
    public void downloadExcel(HttpServletResponse response) {
        //设置要导出的文件的名字
        String fileName  = "userinfo" + ".xls";
        String sheetName = "userinfo";
        //新增数据行，并且设置单元格数据
        String[] headers = {"id", "username", "password", "role", "permission", "ban", "phone", "email", "created", "updated"};

        List<TbUser> list = tbUserMapper.selectList(null);
        System.out.println("org.apache.commons.collections4.CollectionUtils - 如果指定的集合不为空，则执行空安全检查。");
        if (CollectionUtils.isNotEmpty(list)) {
            List<List<TbUser>> partition = Lists.partition(list, HttpStatus.OK.value());
            for (List<TbUser> users : partition) {
                for (TbUser tbUser : users) {
                    System.out.println("com.google.common.collect.Lists - 返回一个列表的连续{List.subList(int,int) subList}，每个列表的大小相同(最后的列表可能更小)。");
                    String json = JSONObject.toJSONString(tbUser);
                    System.out.println("com.alibaba.fastjson.JSONObject - 此方法将指定的对象序列化为其等效的json表示形式。");
                    TbUser object = JSONObject.parseObject(json, TbUser.class);
                    System.out.println("com.alibaba.fastjson.JSONObject - 此方法将指定的json反序列化为指定类的对象。");
                    List<Map<String, Object>> maps = BeanUtils.beansToMaps(list);
                    System.out.println("");
                }
            }
        }
        System.out.println("org.springframework.util.CollectionUtils - 如果提供的集合为{null}或空，则返回{true}。");
        if (org.springframework.util.CollectionUtils.isEmpty(list)) {
            List<List<TbUser>> partition = ListUtils.partition(list, HttpServletResponse.SC_OK);
            for (List<TbUser> users : partition) {
                for (TbUser tbUser : users) {
                    System.out.println("org.apache.commons.collections4.ListUtils - 返回一个列表的连续{List.subList(int,int) subList}，每个列表的大小相同(最后的列表可能更小)。");
                }
            }
        }

        // 1.High level representation of a Excel workbook. - Excel工作簿的高级表示。
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 2.Sheets are the central structures within a workbook. - 工作表是工作簿中的中心结构。
        HSSFSheet sheet = workbook.createSheet(sheetName);
        // 3.High level representation of a row of a spreadsheet. - 电子表格行的高级表示。
        HSSFRow row = sheet.createRow(0);
        /**在excel表中添加表头*/
        for (int i = 0; i < headers.length; i++) {
            // 4.High level representation of a cell in a row of a spreadsheet. - 电子表格中一行[单元格]的高级表示。
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }
        /**在表中存放查询到的数据放入对应的列*/
        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(1 + 1);
            row.createCell(0).setCellValue(list.get(i).getId()         == null ? null : list.get(i).getId());
            row.createCell(1).setCellValue(list.get(i).getUsername()   == null ? null : list.get(i).getUsername());
            row.createCell(2).setCellValue(list.get(i).getPassword()   == null ? null : list.get(i).getPassword());
            row.createCell(3).setCellValue(list.get(i).getRole()       == null ? null : list.get(i).getRole());
            row.createCell(4).setCellValue(list.get(i).getPermission() == null ? null : list.get(i).getPermission());
            row.createCell(5).setCellValue(list.get(i).getBan()        == null ? null : list.get(i).getBan());
            row.createCell(6).setCellValue(list.get(i).getPhone()      == null ? null : list.get(i).getPhone());
            row.createCell(7).setCellValue(list.get(i).getEmail()      == null ? null : list.get(i).getEmail());
            row.createCell(8).setCellValue(list.get(i).getCreated()    == null ? null : new SimpleDateFormat().format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(list.get(i).getCreated())));
            row.createCell(9).setCellValue(list.get(i).getUpdated()    == null ? null : new SimpleDateFormat().format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(list.get(i).getUpdated())));
        }
        response.reset();
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        try {
            response.flushBuffer();
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Excel导出方式之二
     *
     * @param response
     * @throws IOException
     */
    @Override
    public void exportExcel(HttpServletResponse response) throws IOException {
        /**新增数据行，并且设置单元格数据*/
        String[] headers = {"id", "username", "password", "role", "permission", "ban", "phone", "email", "created", "updated"};
        /**设置要导出的文件的名字*/
        String fileName  = "userinfo" + ".xls";

        // 1.High level representation of a Excel workbook. - Excel工作簿的高级表示。
        Workbook workbook = new HSSFWorkbook();
        // 2.Sheets are the central structures within a workbook. - 工作表是工作簿中的中心结构。
        Sheet sheet = workbook.createSheet();
        // 3.High level representation of a row of a spreadsheet. - 电子表格行的高级表示。
        Row row = sheet.createRow(0);
        for (int j = 0; j < headers.length; j++) {
            // 4.High level representation of a cell in a row of a spreadsheet. - 电子表格中一行[单元格]的高级表示。
            Cell cell = row.createCell(j);
            cell.setCellValue(headers[j]);
        }
        List<TbUser> list = tbUserMapper.selectList(null);
        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i + 1);
            row.createCell(NumericConstants.ZERO).setCellValue(list.get(i).getId()         == null ? null : list.get(i).getId());
            row.createCell(NumericConstants.ONE).setCellValue(list.get(i).getUsername()    == null ? null : list.get(i).getUsername());
            row.createCell(NumericConstants.TWO).setCellValue(list.get(i).getPassword()    == null ? null : list.get(i).getPassword());
            row.createCell(NumericConstants.THREE).setCellValue(list.get(i).getRole()      == null ? null : list.get(i).getRole());
            row.createCell(NumericConstants.FOUR).setCellValue(list.get(i).getPermission() == null ? null : list.get(i).getPermission());
            row.createCell(NumericConstants.FIVE).setCellValue(list.get(i).getBan()        == null ? null : list.get(i).getBan());
            row.createCell(NumericConstants.SIX).setCellValue(list.get(i).getPhone()       == null ? null : list.get(i).getPhone());
            row.createCell(NumericConstants.SEVEN).setCellValue(list.get(i).getEmail()     == null ? null : list.get(i).getEmail());
            row.createCell(NumericConstants.EIGHT).setCellValue(list.get(i).getCreated()   == null ? null : new SimpleDateFormat().format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(list.get(i).getCreated())));
            row.createCell(NumericConstants.NINE).setCellValue(list.get(i).getUpdated()    == null ? null : new SimpleDateFormat().format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(list.get(i).getUpdated())));
        }
        response.reset();
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        try {
            response.flushBuffer();
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
