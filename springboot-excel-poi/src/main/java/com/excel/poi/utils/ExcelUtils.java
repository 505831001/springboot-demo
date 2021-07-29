package com.excel.poi.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Liuweiwei
 * @since 2021-07-28
 */
public class ExcelUtils {
    /**
     * 导入导出状态和值切换
     *
     * @return
     */
    public static Map<String, String> statusMaps() {
        return new HashMap<String, String>() {
            {
                put("0", "停用");
                put("1", "可用");
            }
        };
    }

    /**
     * 导入导出来源和值切换
     *
     * @return
     */
    public static Map<String, String> roleMaps() {
        return new HashMap<String, String>() {
            {
                put("admin", "管理员");
                put("guest", "宾客");
            }
        };
    }
}
