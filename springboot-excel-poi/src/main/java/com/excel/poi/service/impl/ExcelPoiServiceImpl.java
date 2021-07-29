package com.excel.poi.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.BeanUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.excel.poi.dao.TbUserMapper;
import com.excel.poi.entity.TbUser;
import com.excel.poi.service.ExcelPoiService;
import com.excel.poi.utils.ExcelUtils;
import com.excel.poi.utils.Utils;
import com.excel.poi.vo.TbUserVO;
import com.google.common.collect.Lists;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Liuweiwei
 * @since 2021-07-04
 */
@Service
public class ExcelPoiServiceImpl implements ExcelPoiService {

    private final Logger LOGGER = LogManager.getLogger(this.getClass());

    @Resource
    private TbUserMapper tbUserMapper;

    /**字符串格式cn字符串和en字符串*/
    public static String cnTitleStrings = "主键,用户名称,用户密码,用户角色,用户权限,用户状态,电话号码,邮箱地址,创建时间,修改时间";
    public static String enFieldStrings = "id,username,password,role,permission,ban,phone,email,created,updated";

    /**字符串数组格式cn字符串和en字符串*/
    public static String[] cnTitles = {"主键", "用户名称", "用户密码", "用户角色", "用户权限", "用户状态", "电话号码", "邮箱地址", "创建时间", "修改时间"};
    public static String[] enFields = {"id", "username", "password", "role", "permission", "ban", "phone", "email", "created", "updated"};

    /**字符串数组方法格式cn字符串和en字符串*/
    public static String[] cnTitlesMethod() {
        return new String[]{"主键", "用户名称", "用户密码", "用户角色", "用户权限", "用户状态", "电话号码", "邮箱地址", "创建时间", "修改时间"};
    }
    public static String[] enTitlesMethod() {
        return new String[]{"id", "username", "password", "role", "permission", "ban", "phone", "email", "created", "updated"};
    }

    /**
     * Excel中文Titles和英文Fields切换
     *
     * @return
     */
    public static Map<String, String> excelCNtoENSwitch() {
        Map<String, String> convertMap = new HashMap<>();
        String[] titles = cnTitleStrings.split(",");
        String[] fields = enFieldStrings.split(",");
        for (int i = 0; i < titles.length; i++) {
            convertMap.put(titles[i], fields[i]);
        }
        return convertMap;
    }

    /**
     * Excel中文Titles和英文Fields转换
     *
     * @return
     */
    public static Map<String, String> excelCNtoENConvert() {
        Map<String, String> convertMap = new HashMap<>();
        String[] titles = cnTitles;
        String[] fields = enFields;
        for (int i = 0; i < titles.length; i++) {
            convertMap.put(titles[i], fields[i]);
        }
        return convertMap;
    }

    /**
     * Excel文档导入方式之一
     *
     * @param file
     * @return
     */
    @Override
    public ResponseEntity uploadExcel(MultipartFile file) {
        List<Map<String, Object>> list1 = new ArrayList<>(10);
        List<Map<String, Object>> list2 = new ArrayList<>(10);
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
                    /**第一行表头跳过*/
                    if (row.getRowNum() == 0) {
                        continue;
                    }
                    /**获得sheet行有多少单元格*/
                    short lastCellNum = row.getLastCellNum();
                    Map<String, Object> map1 = new HashMap<>();
                    for (int i = 0; i < lastCellNum; i++) {
                        // 4.High level representation of a cell in a row of a spreadsheet. - 电子表格中一行[单元格]的高级表示。
                        Cell cell = row.getCell(i);
                        map1.put(cnTitles[i], cell.toString() == null ? null : cell.toString());
                    }
                    list1.add(map1);
                }

                /**获得sheet有多少行*/
                int rows = sheet.getPhysicalNumberOfRows();
                for (int i = 0; i < rows; i++) {
                    /**第一行表头跳过*/
                    if (i == 0) {
                        continue;
                    }
                    // 3.High level representation of a row of a spreadsheet. - 电子[表格行]的高级表示。
                    Row row = sheet.getRow(i);
                    /**获得sheet每行有多少单元格*/
                    int cells = row.getPhysicalNumberOfCells();
                    Map<String, Object> map2 = new HashMap<>();
                    for (int j = 0; j < cells; j++) {
                        // 4.High level representation of a cell in a row of a spreadsheet. - 电子表格中一行[单元格]的高级表示。
                        Cell cell = row.getCell(j);
                        map2.put(cnTitles[j], cell.toString() == null ? null : cell.toString());
                    }
                    list2.add(map2);
                }
            } else {
                return new ResponseEntity("请求文件类型错误:文件类型错误(office文件)", HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Map<String, Object> map : list1) {
            System.out.println("导入Excel集合1：" + map.toString());
        }
        for (Map<String, Object> map : list2) {
            System.out.println("导入Excel集合2：" + map.toString());
        }
        List<Map<String, Object>> list3 = new ArrayList<>(10);
        for (Map<String, Object> map : list2) {
            Map<String, Object> temp = new HashMap<>();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String cnField = entry.getKey();
                String enField = excelCNtoENConvert().get(cnField);
                temp.put(enField, entry.getValue());
            }
            list3.add(temp);
        }
        for (Map<String, Object> map : list3) {
            System.out.println("转换Excel集合3：" + map.toString());
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
            for (int j = 0; j < cnTitles.length; j++) {
                // 4.High level representation of a cell in a row of a spreadsheet. - 电子表格中一行[单元格]的高级表示。
                Cell cell = row.getCell(j);
                map.put(cnTitles[j], cell.toString() == null ? null : cell.toString());
            }
            list.add(map);
        }
        List<TbUser>              what = new ArrayList<>(10);
        List<TbUser>              objs = new ArrayList<>(10);
        List<Map<String, Object>> maps = new ArrayList<>(10);
        /**中文[cnTitles]切换英文[enFields]来转换成[map]或[对象]存储*/
        for (Map<String, Object> map : list) {
            JSONObject          fast = new JSONObject();
            Map<String, Object> temp = new HashMap<>();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String cnKey = entry.getKey();
                String enKey = excelCNtoENConvert().get(cnKey);
                temp.put(enKey, entry.getValue());
                fast.put(enKey, entry.getValue());
            }
            /**封装到集合*/
            maps.add(temp);
            /**转换集合json再到对象*/
            String json = JSONObject.toJSONString(temp);
            what.add(JSONObject.parseObject(json, TbUser.class));
            /**直接利用fast进行集合封装转对象*/
            objs.add(fast.toJavaObject(TbUser.class));
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * Excel导出方式之一
     *
     * @param response
     */
    @Override
    public void downloadExcel(HttpServletResponse response) {
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

        /**设置要导出的文件的名字*/
        String fileName  = "userinfo" + ".xls";
        String sheetName = "userinfo";
        /**新增数据行，并且设置单元格数据*/
        String[] headers = {"id", "username", "password", "role", "permission", "ban", "phone", "email", "created", "updated"};
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
            row.createCell(8).setCellValue(list.get(i).getCreated()    == null ? null : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(list.get(i).getCreated()));
            row.createCell(9).setCellValue(list.get(i).getUpdated()    == null ? null : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(list.get(i).getUpdated()));
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
        List<TbUserVO> voList = new ArrayList<>(10);
        List<TbUser>   poList = tbUserMapper.selectList(null);
        //持久层PO对象转视图层VO对象，DTO对象入参给接口传递使用。
        poList.stream().forEach(source -> {
            TbUserVO target = new TbUserVO();
            org.springframework.beans.BeanUtils.copyProperties(source, target);
            target.setId(String.valueOf(source.getId()));
            target.setCreated(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(source.getCreated()));
            target.setUpdated(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(source.getUpdated()));
            voList.add(target);
        });

        /**
         * 对象列表转集合列表。TODO -> stream().map(单例集合list)方法。
         * Returns a stream consisting of the results of applying the given function to the elements of this stream.
         * 返回一个流，该流包含将给定函数应用于该流元素的结果。
         */
        List<Map<String, Object>> mapList = voList.stream().map(tbUserVO -> {
            try {
                Map<String, Object> map = PropertyUtils.describe(tbUserVO);
                return map;
            } catch (Exception ex) {
                LOGGER.error("导出异常-> msg:{}, data:{}", ex.getMessage(), JSONObject.toJSONString(tbUserVO));
                ex.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
        if (voList.stream().filter(vo -> Objects.isNull(vo)).collect(Collectors.toList()).size() > 0) {
            throw new RuntimeException("导出列表无数据");
        }
        voList.stream().forEach(vo -> {
            Map<String, String> statusMap = ExcelUtils.statusMaps();
            for (Map.Entry<String, String> entry : statusMap.entrySet()) {
                if (vo.getBan().equalsIgnoreCase(entry.getKey())) {
                    vo.setBan(entry.getValue());
                }
            }
            Map<String, String> roleMap = ExcelUtils.roleMaps();
            for (Map.Entry<String, String> entry : roleMap.entrySet()) {
                if (vo.getRole().equalsIgnoreCase(entry.getKey())) {
                    vo.setRole(entry.getValue());
                }
            }
        });

        /**
         * 遍历集合列表下集合键值对。TODO -> stream().flatMap(双列集合map)方法。
         * Returns a stream consisting of the results of replacing each element of this stream with the contents of a mapped stream produced by applying the provided mapping function to each element.
         * 返回一个流，该流由将提供的映射函数应用于每个元素而生成的映射流的内容替换该流的每个元素的结果组成。
         */
        mapList.stream().flatMap(map -> map.entrySet().stream()).forEach(entry -> {
            if (entry.getKey().equalsIgnoreCase("ban")) {
                System.out.println("用户状态数值描述切换");
            }
            if (entry.getKey().equalsIgnoreCase("role")) {
                System.out.println("用户角色数值描述替换");
            }
        });

        /**字符串数组格式cn标题Title*/
        String[] cnHeaders = {"主键", "用户名称", "用户密码", "用户角色", "用户权限", "用户状态", "电话号码", "邮箱地址", "创建时间", "修改时间"};
        /**字符串数组格式en标题Title*/
        String[] enHeaders = {"id", "username", "password", "role", "permission", "ban", "phone", "email", "created", "updated"};
        /**设置要导出的文件的名字*/
        String fileName  = "userinfo" + ".xls";
        // TODO -> 1.High level representation of a Excel workbook. - Excel工作簿的高级表示。
        Workbook workbook = new HSSFWorkbook();
        // TODO -> 2.Sheets are the central structures within a workbook. - 工作表是工作簿中的中心结构。
        Sheet sheet = workbook.createSheet();
        // TODO -> 3.High level representation of a row of a spreadsheet. - 电子表格行的高级表示。
        Row row = sheet.createRow(0);
        for (int j = 0; j < cnHeaders.length; j++) {
            // TODO -> 4.High level representation of a cell in a row of a spreadsheet. - 电子表格中一行[单元格]的高级表示。
            Cell cell = row.createCell(j);
            cell.setCellValue(cnHeaders[j]);
        }
        /*
        for (int j = 0; j < enHeaders.length; j++) {
            // TODO -> 4.High level representation of a cell in a row of a spreadsheet. - 电子表格中一行[单元格]的高级表示。
            Cell cell = row.createCell(j);
            cell.setCellValue(enHeaders[j]);
        }
        */
        /**sheet中第0+1行添加每个单元格cell数值*/
        for (int i = 0; i < voList.size(); i++) {
            row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(voList.get(i).getId()         == null ? null : voList.get(i).getId());
            row.createCell(1).setCellValue(voList.get(i).getUsername()   == null ? null : voList.get(i).getUsername());
            row.createCell(2).setCellValue(voList.get(i).getPassword()   == null ? null : voList.get(i).getPassword());
            row.createCell(3).setCellValue(voList.get(i).getRole()       == null ? null : voList.get(i).getRole());
            row.createCell(4).setCellValue(voList.get(i).getPermission() == null ? null : voList.get(i).getPermission());
            row.createCell(5).setCellValue(voList.get(i).getBan()        == null ? null : voList.get(i).getBan());
            row.createCell(6).setCellValue(voList.get(i).getPhone()      == null ? null : voList.get(i).getPhone());
            row.createCell(7).setCellValue(voList.get(i).getEmail()      == null ? null : voList.get(i).getEmail());
            row.createCell(8).setCellValue(voList.get(i).getCreated()    == null ? null : voList.get(i).getCreated());
            row.createCell(9).setCellValue(voList.get(i).getUpdated()    == null ? null : voList.get(i).getUpdated());
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

    public ResponseEntity<Void> provinceAndCitySheet(MultipartFile file) {
        List<Map<String, Object>> list1 = new ArrayList<>(10);
        List<Map<String, Object>> list2 = new ArrayList<>(10);
        try {
            /**流读取文件*/
            BufferedInputStream input = new BufferedInputStream(file.getInputStream());
            // 1.High level representation of a Excel workbook. - Excel[工作簿]的高级表示。
            HSSFWorkbook workbook = new HSSFWorkbook(input);
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
                /**获得sheet行有多少单元格*/
                short lastCellNum = row.getLastCellNum();
                Map<String, Object> map1 = new HashMap<>();
                for (int i = 0; i < lastCellNum; i++) {
                    // 4.High level representation of a cell in a row of a spreadsheet. - 电子表格中一行[单元格]的高级表示。
                    Cell cell = row.getCell(i);
                    map1.put(cnTitles[i], cell.toString() == null ? null : cell.toString());
                }
                list1.add(map1);
            }

            /**获得sheet有多少行*/
            int rows = sheet.getPhysicalNumberOfRows();
            for (int i = 0; i < rows; i++) {
                /**第一行表头跳过*/
                if (i == 0) {
                    continue;
                }
                // 3.High level representation of a row of a spreadsheet. - 电子[表格行]的高级表示。
                Row row = sheet.getRow(i);
                /**获得sheet每行有多少单元格*/
                int cells = row.getPhysicalNumberOfCells();
                Map<String, Object> map2 = new HashMap<>();
                for (int j = 0; j < cells; j++) {
                    // 4.High level representation of a cell in a row of a spreadsheet. - 电子表格中一行[单元格]的高级表示。
                    Cell cell = row.getCell(j);
                    map2.put(cnTitles[j], cell.toString() == null ? null : cell.toString());
                }
                list2.add(map2);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            /**服务器错误*/
            return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
