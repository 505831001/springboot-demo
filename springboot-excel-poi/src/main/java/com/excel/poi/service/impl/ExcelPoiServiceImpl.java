package com.excel.poi.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.excel.poi.dao.TbUserMapper;
import com.excel.poi.entity.TbUser;
import com.excel.poi.service.ExcelPoiService;
import com.excel.poi.utils.ExcelUtils;
import com.excel.poi.utils.FileUtils;
import com.excel.poi.utils.ResultData;
import com.excel.poi.vo.TbUserVO;
import com.google.common.collect.Lists;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Liuweiwei
 * @since 2021-07-04
 */
@Service
public class ExcelPoiServiceImpl extends ServiceImpl<TbUserMapper, TbUser> implements ExcelPoiService {

    private final Logger LOGGER = LogManager.getLogger(this.getClass());

    @Resource
    private TbUserMapper tbUserMapper;

    /**阿里巴巴w3c提示:魔法值.不允许任何魔法值(即未经定义的常量)直接出现在代码中.*/
    private static final String  XLS = "xls";
    private static final String XLSX = "xlsx";
    private static final String  BAN = "ban";
    private static final String ROLE = "role";

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
        Map<String, String> convertMap = new HashMap<>(10);
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
        Map<String, String> convertMap = new HashMap<>(10);
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
        /**Sheet表单获取单元格方式之一row.getLastCellNum()方法*/
        List<Map<String, Object>> list0 = new ArrayList<>(10);
        /**Sheet表单获取单元格方式之二row.getPhysicalNumberOfCells()方法*/
        List<Map<String, Object>> list1 = new ArrayList<>(10);
        /**Sheet表单获取单元格方式之二row.getPhysicalNumberOfCells()方法*/
        List<Map<String, Object>> list2 = new ArrayList<>(10);
        int index = 0;
        if (!FileUtils.checkExtension(file)) {
            return new ResponseEntity("请求文件类型错误:后缀名错误(xls,xlsx,XLS,XLSX).", HttpStatus.BAD_REQUEST);
        }
        try {
            if (FileUtils.isOfficeFile(file)) {
                // 1.High level representation of a Excel workbook. - Excel[工作簿]的高级表示。
                Workbook workbook = FileUtils.getWorkbookAuto(file);
                // 2.Sheets are the central structures within a workbook. - [工作表]是工作簿中的中心结构。
                Sheet sheet = workbook.getSheetAt(0);
                // 3.High level representation of a row of a spreadsheet. - 电子[表格行]的高级表示。
                for (Row row : sheet) {
                    /**第一行表头跳过*/
                    if (row.getRowNum() == 0) {
                        continue;
                    }
                    /**获得sheet每行有多少单元格*/
                    short lastCellNum = row.getLastCellNum();
                    Map<String, Object> map0 = new HashMap<>(10);
                    for (int i = 0; i < lastCellNum; i++) {
                        // 4.High level representation of a cell in a row of a spreadsheet. - 电子表格中一行[单元格]的高级表示。
                        Cell cell = row.getCell(i);
                        /**TODO -> sheet表单row行cell单元格非空判断，否则报错: Cell C2 is not part of an array formula.或者空指针异常*/
                        if (cell == null) {
                            map0.put(cnTitles[i], null);
                        } else {
                            String value = cell.toString().trim();
                            /**cell单元格值一些字符的替换*/
                            //value.replaceAll("\\(" + "s" + "\\)", "").replaceAll("\\（" + "s" + "\\）", "").replaceAll(" ", "").replaceAll("　", "");
                            switch (cell.getCellType()) {
                                case STRING:
                                    value = cell.getStringCellValue();
                                    break;
                                case NUMERIC:
                                    value = String.valueOf(cell.getNumericCellValue());
                                    break;
                                case BOOLEAN:
                                    value = String.valueOf(cell.getBooleanCellValue());
                                    break;
                                case BLANK:
                                    value = null;
                                    break;
                                default:
                                    value = cell.toString().trim();
                                    break;
                            }
                            map0.put(cnTitles[i], cell.toString().trim());
                        }
                    }
                    list0.add(map0);

                    /**获得sheet每行有多少单元格*/
                    int cells = row.getPhysicalNumberOfCells();
                    Map<String, Object> map1 = new HashMap<>(10);
                    for (int i = 0; i < cells; i++) {
                        // 4.High level representation of a cell in a row of a spreadsheet. - 电子表格中一行[单元格]的高级表示。
                        Cell cell = row.getCell(i);
                        /**当Sheet表中单元格有空值，会报空指针异常*/
                        if (cell == null) {
                            map1.put(cnTitles[i], null);
                        } else {
                            map1.put(cnTitles[i], cell.toString().trim());
                        }
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
                    Map<String, Object> map2 = new HashMap<>(10);
                    for (int j = 0; j < cells; j++) {
                        // 4.High level representation of a cell in a row of a spreadsheet. - 电子表格中一行[单元格]的高级表示。
                        Cell cell = row.getCell(j);
                        /**TODO -> sheet表单row行cell单元格非空判断，否则报错: Cell C2 is not part of an array formula.或者空指针异常*/
                        if (cell == null) {
                            map2.put(cnTitles[j], null);
                        } else {
                            String value = cell.toString().trim();
                            /**cell单元格值一些字符的替换*/
                            //value.replaceAll("\\(" + "s" + "\\)", "").replaceAll("\\（" + "s" + "\\）", "").replaceAll(" ", "").replaceAll("　", "");
                            switch (cell.getCellType()) {
                                case STRING:
                                    value = cell.getStringCellValue();
                                    break;
                                case NUMERIC:
                                    value = String.valueOf(cell.getNumericCellValue());
                                    break;
                                case BOOLEAN:
                                    value = String.valueOf(cell.getBooleanCellValue());
                                    break;
                                case BLANK:
                                    value = null;
                                    break;
                                default:
                                    value = cell.toString().trim();
                                    break;
                            }
                            map2.put(cnTitles[j], cell.toString().trim());
                        }
                    }
                    list2.add(map2);
                }
            } else {
                return new ResponseEntity("请求文件类型错误:文件类型错误(office文件)", HttpStatus.BAD_REQUEST);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Map<String, Object> map0 : list0) {
            //System.out.println("Sheet表单获取单元格方式00：" + map0.toString());
        }
        for (Map<String, Object> map1 : list1) {
            //System.out.println("Sheet表单获取单元格方式01：" + map1.toString());
        }
        for (Map<String, Object> map2 : list2) {
            //System.out.println("Sheet表单获取单元格方式02：" + map2.toString());
        }

        /**Sheet表单获取完单元格后中文[cnTitles]标题切换英文[enFields]字段来转换成[map]存储*/
        List<Map<String, Object>> list3 = new ArrayList<>(10);
        Map<String, String> cNtoENConvert = excelCNtoENConvert();
        for (Map<String, Object> cnTitles : list0/**或者list1或者list2都行*/) {
            Map<String, Object> enFields = new HashMap<>(10);
            for (Map.Entry<String, Object> entry : cnTitles.entrySet()) {
                String cnField = entry.getKey();
                String enField = cNtoENConvert.get(cnField);
                enFields.put(enField, entry.getValue());
            }
            list3.add(enFields);
        }
        for (Map<String, Object> map : list3) {
            //System.out.println("Sheet表单获取完单元格后中文[cnTitles]标题切换英文[enFields]字段来转换成[map]存储03：" + map.toString());
        }

        /**Sheet表单获取完单元格后中文[cnTitles]标题切换英文[enFields]字段来转换成[对象]存储*/
        List<TbUser> list5 = new ArrayList<>(10);
        List<TbUser> list4 = new ArrayList<>(10);
        for (Map<String, Object> map : list3) {
            JSONObject            object = new JSONObject();
            Map<String, Object> enFields = new HashMap<>(10);
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                enFields.put(entry.getKey(), entry.getValue());
                object.put(entry.getKey(), entry.getValue());
            }
            /**集合转json再转对象JSON或者JSONObject*/
            String json = JSONObject.toJSONString(enFields);
            TbUser user = JSONObject.parseObject(json, TbUser.class);
            list4.add(user);
            /**直接利用fast进行集合封装转对象*/
            list5.add(object.toJavaObject(TbUser.class));
        }
        for (TbUser user : list4) {
            //System.out.println("Sheet表单获取完单元格后中文[cnTitles]标题切换英文[enFields]字段来转换成[对象]存储04：" + user.toString());
        }
        for (TbUser user : list5) {
            //System.out.println("Sheet表单获取完单元格后中文[cnTitles]标题切换英文[enFields]字段来转换成[对象]存储05：" + user.toString());
        }

        /**Sheet表单获取完单元格后中文[cnTitles]标题切换英文[enFields]字段来转换成[对象]存储后再转换中文描述值到数据表数值*/
        buildTitlesToFields(list5);
        for (TbUser user : list5) {
            System.out.println("Sheet表单获取完单元格后中文[cnTitles]标题切换英文[enFields]字段来转换成[对象]存储后再转换中文描述值到数据表数值06：" + user.toString());
        }

        List<Map<String, Object>> checks = new ArrayList<>(10);
        /**检查导入数据，是否空集合，据类型是否正确，数据类型有错误。*/
        checks = checkData(list5);
        /**检查导入数据，是否有重复，分别添加错误列表，或者保存列表。*/
        checks = repeatData(list5, checks);
        //自测可以打印结果看看
        for (int i = 0; i < checks.size(); i++) {
            Map<String, Object> error = checks.get(i);
            if (Objects.nonNull(error.get("错误信息"))) {
                System.out.println("第" + (i + 2) + "行:" + error.get("错误信息").toString());
            }
        }
        /**如果有错误数据则返回400+errors，否则才返回成功200*/
        if (CollectionUtils.isNotEmpty(checks)) {
            return new ResponseEntity(checks.toString(), HttpStatus.BAD_REQUEST);
        }
        /**如果有错误数据则返回400，否则才返回成功200+data*/
        return new ResponseEntity(list5.toString(), HttpStatus.OK);
    }

    /**
     * Excel文档导入方式之二
     *
     * @param file
     * @return
     * @throws IOException
     */
    @Override
    public ResultData importExcel(MultipartFile file) throws IOException {
        /**Sheet表单获取单元格方式之一row.getLastCellNum()方法*/
        List<Map<String, Object>> list0 = new ArrayList<>(10);
        /**Sheet表单获取单元格方式之二row.getPhysicalNumberOfCells()方法*/
        List<Map<String, Object>> list1 = new ArrayList<>(10);
        /**Sheet表单获取单元格方式之二row.getPhysicalNumberOfCells()方法*/
        List<Map<String, Object>> list2 = new ArrayList<>(10);
        int index = 0;

        /**获取文件路径*/
        String filename = file.getOriginalFilename();
        /**获取文件后缀类型*/
        String fileSuffix = filename.substring(filename.lastIndexOf(".") + 1);
        /**读取文件流*/
        BufferedInputStream is = new BufferedInputStream(file.getInputStream());
        // 1.High level representation of a Excel workbook. - Excel[工作簿]的高级表示。
        Workbook workbook = null;
        if (XLS.equalsIgnoreCase(fileSuffix)) {
            workbook = new HSSFWorkbook(is);
        } else if (XLSX.equalsIgnoreCase(fileSuffix)) {
            workbook = new XSSFWorkbook(is);
        }
        // 2.Sheets are the central structures within a workbook. - [工作表]是工作簿中的中心结构。
        Sheet sheet = workbook.getSheetAt(0);
        // 3.High level representation of a row of a spreadsheet. - 电子[表格行]的高级表示。
        for (Row row : sheet) {
            /**第一行表头跳过*/
            if (row.getRowNum() == 0) {
                continue;
            }
            /**获得sheet每行有多少单元格*/
            short lastCellNum = row.getLastCellNum();
            Map<String, Object> map0 = new HashMap<>(10);
            for (int i = 0; i < lastCellNum; i++) {
                // 4.High level representation of a cell in a row of a spreadsheet. - 电子表格中一行[单元格]的高级表示。
                Cell cell = row.getCell(i);
                /**TODO -> sheet表单row行cell单元格非空判断，否则报错: Cell C2 is not part of an array formula.或者空指针异常*/
                if (cell == null) {
                    map0.put(cnTitles[i], null);
                } else {
                    String value = cell.toString().trim();
                    /**cell单元格值一些字符的替换*/
                    //value.replaceAll("\\(" + "s" + "\\)", "").replaceAll("\\（" + "s" + "\\）", "").replaceAll(" ", "").replaceAll("　", "");
                    switch (cell.getCellType()) {
                        case STRING:
                            value = cell.getStringCellValue();
                            break;
                        case NUMERIC:
                            value = String.valueOf(cell.getNumericCellValue());
                            break;
                        case BOOLEAN:
                            value = String.valueOf(cell.getBooleanCellValue());
                            break;
                        case BLANK:
                            value = null;
                            break;
                        default:
                            value = cell.toString().trim();
                            break;
                    }
                    map0.put(cnTitles[i], cell.toString().trim());
                }
            }
            list0.add(map0);

            /**获得sheet每行有多少单元格*/
            int cells = row.getPhysicalNumberOfCells();
            Map<String, Object> map1 = new HashMap<>(10);
            for (int i = 0; i < cells; i++) {
                // 4.High level representation of a cell in a row of a spreadsheet. - 电子表格中一行[单元格]的高级表示。
                Cell cell = row.getCell(i);
                /**当Sheet表中单元格有空值，会报空指针异常*/
                if (cell == null) {
                    map1.put(cnTitles[i], null);
                } else {
                    map1.put(cnTitles[i], cell.toString().trim());
                }
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
            Map<String, Object> map2 = new HashMap<>(10);
            for (int j = 0; j < cells; j++) {
                // 4.High level representation of a cell in a row of a spreadsheet. - 电子表格中一行[单元格]的高级表示。
                Cell cell = row.getCell(j);
                /**TODO -> sheet表单row行cell单元格非空判断，否则报错: Cell C2 is not part of an array formula.或者空指针异常*/
                if (cell == null) {
                    map2.put(cnTitles[j], null);
                } else {
                    String value = cell.toString().trim();
                    /**cell单元格值一些字符的替换*/
                    //value.replaceAll("\\(" + "s" + "\\)", "").replaceAll("\\（" + "s" + "\\）", "").replaceAll(" ", "").replaceAll("　", "");
                    switch (cell.getCellType()) {
                        case STRING:
                            value = cell.getStringCellValue();
                            break;
                        case NUMERIC:
                            value = String.valueOf(cell.getNumericCellValue());
                            break;
                        case BOOLEAN:
                            value = String.valueOf(cell.getBooleanCellValue());
                            break;
                        case BLANK:
                            value = null;
                            break;
                        default:
                            value = cell.toString().trim();
                            break;
                    }
                    map2.put(cnTitles[j], cell.toString().trim());
                }
            }
            list2.add(map2);
        }

        for (Map<String, Object> map0 : list0) {
            //System.out.println("Sheet表单获取单元格方式00：" + map0.toString());
        }
        for (Map<String, Object> map1 : list1) {
            //System.out.println("Sheet表单获取单元格方式01：" + map1.toString());
        }
        for (Map<String, Object> map2 : list2) {
            //System.out.println("Sheet表单获取单元格方式02：" + map2.toString());
        }

        /**中文[cnTitles]标题切换英文[enFields]字段来转换成[map]或[对象]存储*/
        List<TbUser> list5 = new ArrayList<>(10);
        List<TbUser> list4 = new ArrayList<>(10);
        List<Map<String, Object>> list3 = new ArrayList<>(10);
        for (Map<String, Object> map : list0/**或者list1或者list2都行*/) {
            JSONObject object = new JSONObject();
            Map<String, Object> temp = new HashMap<>(10);
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String cnKey = entry.getKey();
                String enKey = excelCNtoENConvert().get(cnKey);
                temp.put(enKey, entry.getValue());
                object.put(enKey, entry.getValue());
            }
            /**封装到集合*/
            list3.add(temp);
            /**转换集合json再到对象*/
            String json = JSONObject.toJSONString(temp);
            TbUser user = JSONObject.parseObject(json, TbUser.class);
            list4.add(user);
            /**直接利用fast进行集合封装转对象。将指定值与此映射中的指定键关联（可选操作）。*/
            list5.add(JSONObject.toJavaObject(object, TbUser.class));
        }
        for (Map<String, Object> map3 : list3) {
            //System.out.println("中文[cnTitles]标题切换英文[enFields]字段来转换成[map]存储03：" + map3.toString());
        }
        for (TbUser user : list4) {
            //System.out.println("中文[cnTitles]标题切换英文[enFields]字段来转换成[对象]存储04：" + user.toString());
        }
        for (TbUser user : list5) {
            //System.out.println("中文[cnTitles]标题切换英文[enFields]字段来转换成[对象]存储05：" + user.toString());
        }

        /**Sheet表单获取完单元格后中文[cnTitles]标题切换英文[enFields]字段来转换成[对象]存储后再转换中文描述值到数据表数值*/
        buildTitlesToFields(list5);
        for (TbUser user : list5) {
            //System.out.println("Sheet表单获取完单元格后中文[cnTitles]标题切换英文[enFields]字段来转换成[对象]存储后再转换中文描述值到数据表数值06：" + user.toString());
        }

        List<Map<String, Object>> checks = new ArrayList<>(10);
        /**检查导入数据，是否空集合，据类型是否正确，数据类型有错误。*/
        checks = checkData(list5);
        /**检查导入数据，是否有重复，分别添加错误列表，或者保存列表。*/
        checks = repeatData(list5, checks);
        //自测可以打印结果看看
        for (int i = 0; i < checks.size(); i++) {
            Map<String, Object> error = checks.get(i);
            if (Objects.nonNull(error.get("错误信息"))) {
                System.out.println("第" + (i + 2) + "行:" + error.get("错误信息").toString());
            }
        }
        /**如果有错误数据则返回400+errors，否则才返回成功200*/
        if (CollectionUtils.isNotEmpty(checks)) {
            return ResultData.failure(checks.toString());
        }
        /**如果有错误数据则返回400，否则才返回成功200+data*/
        return ResultData.success(list5.toArray());
    }

    /**
     * 1.Sheet表单获取单元格方式之0-1-2
     * 2.Sheet表单获取完单元格后，中文[cnTitles]切换英文[enFields]来转换成[map]存储
     * 2.Sheet表单获取完单元格后，中文[cnTitles]切换英文[enFields]来转换成[对象]存储
     * 3.Sheet表单获取完单元格后，中文[cnTitles]切换英文[enFields]来转换成[对象]存储后，再转换字段中文描述值到数据表数值
     *
     * @param list5
     */
    private void buildTitlesToFields(List<TbUser> list5) {
        for (TbUser user : list5) {
            if (StringUtils.isNotEmpty(user.getRole())) {
                for (Map.Entry<String, String> entry : ExcelUtils.roleMaps().entrySet()) {
                    if (entry.getValue().equalsIgnoreCase(user.getRole())) {
                        user.setRole(entry.getKey());
                    }
                }
            }
            if (StringUtils.isNotEmpty(user.getBan())) {
                for (Map.Entry<String, String> entry : ExcelUtils.statusMaps().entrySet()) {
                    if (user.getBan().equalsIgnoreCase(entry.getValue())) {
                        user.setBan(entry.getKey());
                    }
                }
            }
        }
    }

    /**
     * 检查导入数据
     *
     * @param list
     * @return
     */
    private List<Map<String, Object>> checkData(List<TbUser> list) {
        //TODO -> 查询数据库来校验比如物料编码是否存在。map.containsKey(Object key)方法。
        //TODO -> 查询数据库来校验比如物料编码是否存在。map.containsValue(Object value)方法。
        Map<String, String> database = new HashMap<String, String>(10) {
            {
                put("admin", "管理员");
                put("guest", "访宾客");
            }
        };
        //TODO -> 远程调用字典库查询用户是否存在。list.contains(Object obj)方法。
        List<String> rest = new ArrayList<>();
        rest.add("admin");
        rest.add("guest");
        rest.add("Jessica");

        List<Map<String, Object>> errors = new ArrayList<>();
        /**
         * java中list集合删除其中的某一个元素
         * 1.Iterator 去除
         *     缺点：虽然也能去除，但是列表改造成 Iterator 类型的了，还要转换。
         * 2.stream 去除
         *     优点：没有改变list格式并且更简洁。
         *     缺点：remove的时候会再次遍历整个list来找出 这个元素，性能会有一定的损耗。
         *     实现：list.stream().filter(user -> StringUtils.isEmpty(user.getUsername())).collect(Collectors.toList());
         *     实现：list.stream().findFirst().map(user -> {if (StringUtils.isEmpty(user.getUsername())) {list.remove(user);}return user;});
         * 3.stream+索引 去除
         *     优点：更简洁，高效。
         *     实现：IntStream.range(0,list.size()).filter(i-> StringUtils.isEmpty(list.get(i).getUsername())).boxed().findFirst().map(i->list.remove((int)i));
         * 4.fori()和forEach()去除都会有问题。
         */
        Iterator<TbUser> iterator = list.iterator();
        while (iterator.hasNext()) {
            TbUser user = iterator.next();
            Map<String, Object> map = new HashMap<>(10);
            /**多个字段非空判断*/
            //Checks if any value in the given array is {@code null}.判断条件有一个为空则满足。
            if (ObjectUtils.anyNull(user.getUsername(), user.getPassword(), user.getRole())) {
                System.out.println("ObjectsUtils.anyNull() -> " + user.toString());
            }
            //Checks if all values in the given array are {@code null}.判断条件全部为空则满足。
            if (ObjectUtils.allNull(user.getUsername(), user.getPassword(), user.getRole())) {
                System.out.println("ObjectsUtils.allNull() -> " + user.toString());
            }
            /**字段非空判断*/
            //1.Break语句使用场景Switch语句循环结构。
            //2.Continue语句使用场景For语句循环结构和While语句循环结构及doWhile语句循环结构。
            //3.Return语句使用场景Method方法体。
            if (org.springframework.util.StringUtils.isEmpty(user.getUsername())) {
                map.put("错误信息", "用户名称不能为空");
                errors.add(map);
                iterator.remove();
                continue;
            }
            if (org.springframework.util.StringUtils.isEmpty(user.getPassword())) {
                map.put("错误信息", "用户密码不能为空");
                errors.add(map);
                iterator.remove();
                continue;
            }
            if (org.springframework.util.StringUtils.isEmpty(user.getRole())) {
                map.put("错误信息", "用户角色不能为空");
                errors.add(map);
                iterator.remove();
                continue;
            }
            if (org.springframework.util.StringUtils.isEmpty(user.getPermission())) {
                map.put("错误信息", "用户权限不能为空");
                errors.add(map);
                iterator.remove();
                continue;
            }
            /**字段合法性判断*/
            if (StringUtils.isNotEmpty(user.getUsername()) && !database.containsKey(user.getUsername())) {
                map.put("错误信息", "集合用户字典表不存在此用户");
                errors.add(map);
                iterator.remove();
                continue;
            }
            if (StringUtils.isNotEmpty(user.getUsername()) && list.contains(user.getUsername())) {
                map.put("错误信息", "列表用户字典表不存在此用户");
                errors.add(map);
                iterator.remove();
                continue;
            }
        }
        return errors;
    }

    /**
     * 检查重复数据
     *
     * @param list5
     * @param checks
     * @return
     */
    private List<Map<String, Object>> repeatData(List<TbUser> list5, List<Map<String, Object>> checks) {
        String[] uniqueIndexFields = new String[]{"username", "phone"};
        Map<String, String> map = new HashMap<>(10);
        for (int i = 0; i < list5.size(); i++) {
            String dtoJson = JSONObject.toJSONString(list5.get(i));
            Map<String, String> dtoMap = (Map<String, String>) JSONObject.parse(dtoJson);
            if (dtoMap.size() > 0) {
                String supplierMaterial = new String();
                for (String fields : uniqueIndexFields) {
                    supplierMaterial += dtoMap.get(fields);
                }
                Map<String, Object> errorMap = new HashMap<>(10);
                if (null != map.get(supplierMaterial)) {
                    errorMap.put("错误信息", "第" + (i + 1) + "行数据重复(可以添加到错误列表)");
                    checks.add(errorMap);
                    continue;
                } else {
                    map.put(supplierMaterial, "新数据添加进来，后面如果再有拿来判断其重复鸟");
                    errorMap.put("正确信息", "第" + (i + 1) + "初始行数据(可以添加到保存列表)");
                    checks.add(errorMap);
                }
            }
        }
        return checks;
    }

    /**
     * Excel导出方式之一
     *
     * @param response
     */
    @Override
    public void downloadExcel(HttpServletResponse response) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        List<TbUser> poList = this.list();
        System.out.println("org.apache.commons.collections4.CollectionUtils - 如果指定的集合不为空，则执行空安全检查。");
        if (CollectionUtils.isNotEmpty(poList)) {
            System.out.println("com.google.common.collect.Lists - 返回一个列表的连续{List.subList(int,int) subList}，每个列表的大小相同(最后的列表可能更小)。");
            List<List<TbUser>> partition = Lists.partition(poList, HttpStatus.OK.value());
            for (List<TbUser> users : partition) {
                for (TbUser tbUser : users) {
                    System.out.println("com.alibaba.fastjson.JSONObject - 此方法将指定的对象序列化为其等效的json表示形式。");
                    String json = JSONObject.toJSONString(tbUser);
                    System.out.println("com.alibaba.fastjson.JSONObject - 此方法将指定的json反序列化为指定类的对象。");
                    TbUser user = JSONObject.parseObject(json, TbUser.class);
                    System.out.println("org.apache.commons.beanutils.BeanUtils - 返回指定bean提供读取方法的整个属性集。");
                    Map<String, String> map1 = BeanUtils.describe(user);
                    System.out.println("org.apache.commons.beanutils.PropertyUtils - 返回指定bean提供读取方法的整个属性集。");
                    Map<String, Object> map2 = PropertyUtils.describe(user);
                }
            }
        }
        System.out.println("org.springframework.util.CollectionUtils - 如果提供的集合为{null}或空，则返回{true}。");
        if (org.springframework.util.CollectionUtils.isEmpty(poList)) {
            System.out.println("org.apache.commons.collections4.ListUtils - 返回一个列表的连续{List.subList(int,int) subList}，每个列表的大小相同(最后的列表可能更小)。");
            List<List<TbUser>> partition = ListUtils.partition(poList, HttpServletResponse.SC_OK);
            for (List<TbUser> users : partition) {
                for (TbUser tbUser : users) {
                    System.out.println("com.alibaba.fastjson.JSON - 此方法将指定的对象序列化为其等效的json表示形式。");
                    String json = JSON.toJSONString(tbUser);
                    System.out.println("com.alibaba.fastjson.JSON - 此方法将指定的json反序列化为指定类的对象。");
                    TbUser user = JSON.parseObject(json, TbUser.class);
                    System.out.println("org.apache.commons.beanutils.BeanUtils - 返回指定bean提供读取方法的整个属性集。");
                    Map<String, String> map1 = BeanUtils.describe(user);
                    System.out.println("org.apache.commons.beanutils.PropertyUtils - 返回指定bean提供读取方法的整个属性集。");
                    Map<String, Object> map2 = PropertyUtils.describe(user);
                }
            }
        }
        List<TbUserVO> voList = new ArrayList<>(10);
        //持久层PO对象转视图层VO对象，DTO对象入参给接口传递使用。
        poList.stream().forEach(po -> {
            TbUserVO target = new TbUserVO();
            org.springframework.beans.BeanUtils.copyProperties(po, target);
            target.setId(String.valueOf(po.getId()));
            target.setCreated(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(po.getCreated()));
            target.setUpdated(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(po.getUpdated()));
            voList.add(target);
        });
        //导入导出状态和来源之数值切换描述
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
        if (voList.stream().filter(vo -> Objects.isNull(vo)).collect(Collectors.toList()).size() > 0) {
            throw new RuntimeException("导出列表无数据");
        }

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
        /**
         * 遍历集合列表下集合键值对。TODO -> stream().flatMap(双列集合map)方法。
         * Returns a stream consisting of the results of replacing each element of this stream with the contents of a mapped stream produced by applying the provided mapping function to each element.
         * 返回一个流，该流由将提供的映射函数应用于每个元素而生成的映射流的内容替换该流的每个元素的结果组成。
         */
        mapList.stream().flatMap(map -> map.entrySet().stream()).forEach(entry -> {
            if (entry.getKey().equalsIgnoreCase(BAN)) {
                System.out.println("用户状态数值描述切换");
            }
            if (entry.getKey().equalsIgnoreCase(ROLE)) {
                System.out.println("用户角色数值描述替换");
            }
        });

        /**设置要导出的文件的名字*/
        String fileName  = "userinfo" + ".xls";
        String sheetName = "userinfo";
        /**字符串数组格式cn标题Title*/
        String[] cnHeaders = {"主键", "用户名称", "用户密码", "用户角色", "用户权限", "用户状态", "电话号码", "邮箱地址", "创建时间", "修改时间"};
        /**字符串数组格式en标题Title*/
        String[] enHeaders = {"id", "username", "password", "role", "permission", "ban", "phone", "email", "created", "updated"};
        // 1.High level representation of a Excel workbook. - Excel工作簿的高级表示。
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 2.Sheets are the central structures within a workbook. - 工作表是工作簿中的中心结构。
        HSSFSheet sheet = workbook.createSheet(sheetName);
        // 3.High level representation of a row of a spreadsheet. - 电子表格行的高级表示。
        HSSFRow row = sheet.createRow(0);
        /**在excel表中添加表头*/
        for (int j = 0; j < cnHeaders.length; j++) {
            // TODO -> 4.High level representation of a cell in a row of a spreadsheet. - 电子表格中一行[单元格]的高级表示。
            HSSFCell cell = row.createCell(j);
            HSSFRichTextString text = new HSSFRichTextString(cnHeaders[j]);
            cell.setCellValue(text);
        }
        /*
        for (int j = 0; j < enHeaders.length; j++) {
            // TODO -> 4.High level representation of a cell in a row of a spreadsheet. - 电子表格中一行[单元格]的高级表示。
            HSSFCell cell = row.createCell(j);
            HSSFRichTextString text = new HSSFRichTextString(enHeaders[j]);
            cell.setCellValue(text);
        }
        */
        /**在表中存放查询到的数据放入对应的列*/
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

    /**
     * Excel导出方式之二
     *
     * @param response
     * @throws IOException
     */
    @Override
    public void exportExcel(HttpServletResponse response) {
        List<TbUserVO> voList = new ArrayList<>(10);
        List<TbUser>   poList = tbUserMapper.selectList(null);
        //持久层PO对象转视图层VO对象，DTO对象入参给接口传递使用。
        poList.stream().forEach(po -> {
            TbUserVO target = new TbUserVO();
            org.springframework.beans.BeanUtils.copyProperties(po, target);
            target.setId(String.valueOf(po.getId()));
            target.setCreated(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(po.getCreated()));
            target.setUpdated(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(po.getUpdated()));
            voList.add(target);
        });
        //导入导出状态和来源之数值切换描述
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
        if (voList.stream().filter(vo -> Objects.isNull(vo)).collect(Collectors.toList()).size() > 0) {
            throw new RuntimeException("导出列表无数据");
        }

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
        /**
         * 遍历集合列表下集合键值对。TODO -> stream().flatMap(双列集合map)方法。
         * Returns a stream consisting of the results of replacing each element of this stream with the contents of a mapped stream produced by applying the provided mapping function to each element.
         * 返回一个流，该流由将提供的映射函数应用于每个元素而生成的映射流的内容替换该流的每个元素的结果组成。
         */
        mapList.stream().flatMap(map -> map.entrySet().stream()).forEach(entry -> {
            if (entry.getKey().equalsIgnoreCase(BAN)) {
                System.out.println("用户状态数值描述切换");
            }
            if (entry.getKey().equalsIgnoreCase(ROLE)) {
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
            /**获取文件路径*/
            String filename = file.getOriginalFilename();
            /**获取文件后缀类型*/
            String fileSuffix = filename.substring(filename.lastIndexOf(".") + 1);
            /**流读取文件*/
            BufferedInputStream is = new BufferedInputStream(file.getInputStream());
            // 1.High level representation of a Excel workbook. - Excel[工作簿]的高级表示。
            Workbook workbook = null;
            if (XLS.equalsIgnoreCase(fileSuffix)) {
                workbook = new HSSFWorkbook(is);
            } else if (XLSX.equalsIgnoreCase(fileSuffix)) {
                workbook = new XSSFWorkbook(is);
            }
            // 2.Sheets are the central structures within a workbook. - [工作表]是工作簿中的中心结构。
            Sheet sheet = workbook.getSheetAt(0);
            // 3.High level representation of a row of a spreadsheet. - 电子[表格行]的高级表示。
            for (Row row : sheet) {
                /**第一行表头跳过*/
                if (row.getRowNum() == 0) {
                    continue;
                }
                /**跳过空值的行，要求此行作废*/
                if (row.getCell(0) == null || org.apache.commons.lang3.StringUtils.isBlank(row.getCell(0).getStringCellValue())) {
                    continue;
                }
                /**获得sheet行有多少单元格*/
                short lastCellNum = row.getLastCellNum();
                Map<String, Object> map1 = new HashMap<>(10);
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
                Map<String, Object> map2 = new HashMap<>(10);
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
