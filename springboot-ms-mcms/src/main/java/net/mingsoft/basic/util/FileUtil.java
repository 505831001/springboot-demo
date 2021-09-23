/**
 * The MIT License (MIT)
 * Copyright (c) 2021 铭软科技(mingsoft.net)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.mingsoft.basic.util;

import com.alibaba.fastjson.JSONObject;
import net.mingsoft.base.entity.BaseEntity;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: FileUtil
 * @Description: TODO(清理文件)
 * @author 铭软开发团队
 * @date 2018年7月29日
 *
 */
public class FileUtil {

    /**
     * 文件后缀
     */
    private static String fileSuffix = "[/|\\\\]upload.*?\\.(rmvb|mpga|mpg4|mpeg|docx|xlsx|pptx|jpeg|[a-z]{3})";

    /**
     * @Title: del
     * @Description: TODO(查找出json数据里面的附件路径，并执行删除)
     * @param json
     *            通常是业务实体转换之后的json字符串
     */
    public static void del(String json) {
        Pattern pattern = Pattern.compile(fileSuffix);
        Matcher matcher = pattern.matcher(json);
        while (matcher.find()) {
            try {
                FileUtils.forceDelete(new File(BasicUtil.getRealPath(matcher.group())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @Title: del
     * @Description: TODO(查找出list数据里面的附件路径，并执行删除)
     * @param list
     *            对象集合
     */
    public static void del(List<?> list) {
        String json = "";
        for (Object entity : list) {
            json = JSONObject.toJSONString(entity);
            FileUtil.del(json);
        }
    }

    /**
     * @Title: del
     * @Description: TODO(查找出实体数据里面的附件路径，并执行删除)
     * @param entity
     *            实体对象
     */
    public static void del(BaseEntity entity) {
        String json = JSONObject.toJSONString(entity);
        FileUtil.del(json);
    }

}
