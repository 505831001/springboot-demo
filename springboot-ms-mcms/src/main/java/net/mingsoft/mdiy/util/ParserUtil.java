/**
 * The MIT License (MIT) * Copyright (c) 2020 铭软科技(mingsoft.net)
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package net.mingsoft.mdiy.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapWrapper;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.core.ParseException;
import freemarker.template.*;
import net.mingsoft.base.constant.Const;
import net.mingsoft.basic.util.BasicUtil;
import net.mingsoft.basic.util.SpringUtil;
import net.mingsoft.basic.util.StringUtil;
import net.mingsoft.mdiy.biz.ITagBiz;
import net.mingsoft.mdiy.constant.e.TagTypeEnum;
import net.mingsoft.mdiy.entity.TagEntity;
import net.mingsoft.mdiy.tag.CustomTag;
import net.mingsoft.mdiy.tag.IncludeExTag;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.NClob;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.shiro.util.ThreadContext.get;

public class ParserUtil {
    /*
     * log4j日志记录
     */
    protected final static Logger LOG = LoggerFactory.getLogger(ParserUtil.class);

    /**
     * 静态文件生成路径;例如：mcms/html/1
     */
    public static final String HTML = "html";

    /**
     * index
     */
    public static final String INDEX = "index";

    /**
     * 文件夹路径名后缀;例如：1/58/71.html
     */
    public static final String HTML_SUFFIX = ".html";

    /**
     * 标签指令前缀
     */
    public static final String TAG_PREFIX = "ms_";

    /**
     * 生成的静态列表页面名;例如：list1.html
     */
    public static final String PAGE_LIST = "list-";
    /**
     * 模版文件后缀名;例如：index.htm
     */
    public static final String HTM_SUFFIX = ".htm";

    /**
     * 是否是动态解析;true:动态、false：静态
     */
    public static final String IS_DO = "isDo";

    /**
     * 当前系统访问路径
     */
    public static final String URL = "url";

    /**
     * 栏目实体;
     */
    public static final String COLUMN = "column";

    /**
     * 文章编号
     */
    public static final String ID = "id";

    /**
     * 字段名称
     */
    public static final String FIELD = "field";


    /**
     * 自定义模型表名;
     */
    public static final String TABLE_NAME = "tableName";

    /**
     * 模块路径;
     */
    public static final String MODEL_NAME = "modelName";


    /**
     * 分页，提供給解析传递给sql解析使用
     */
    public static final String PAGE = "pageTag";


    /**
     * 栏目编号;原标签没有使用驼峰命名
     */
    public static final String TYPE_ID = "typeid";


    /**
     * 站点编号
     */
    public static final String APP_ID = "appId";

    /**
     * 站点目录
     */
    public static final String APP_DIR = "appDir";


    /**
     * 初始化Configuration
     */
    public static Configuration cfg = new Configuration(Configuration.VERSION_2_3_0);

    /**
     * 文件模板渲染
     */
    public static FileTemplateLoader ftl = null;

    /**
     * 字符串模板渲染
     */
    public static StringTemplateLoader stringLoader;




    /**
     * 系统预设需要特殊条件的标签
     */
    public static List<String> systemTag1 = CollUtil.toList("field", "pre", "page", "next");


    /**
     * 获取模版文件夹
     * @return ms.upload.template + 应用编号
     */
    public static String buildTemplatePath() {
        return ParserUtil.buildTemplatePath(null);
    }

    /**
     * 拼接模板文件路径
     *
     * @param path 主题下对应的htm模版文件
     * @return 完整的模板文件路径
     */
    public static String buildTemplatePath(String path) {
        return ParserUtil.buildTemplatePath(null,path);
    }



    /**
     * 更具指定皮肤生成模版
     * @param style 指定主题获取模版
     * @param path 主题下对应的htm模版文件
     * @return
     */
    public static String buildTemplatePath(String style,String path) {
        Environment environment = SpringUtil.getBean(Environment.class);
        String uploadTemplatePath = environment.getProperty("ms.upload.template");
        if (BasicUtil.getWebsiteApp() != null) {
            return BasicUtil.getRealPath(uploadTemplatePath + File.separator + BasicUtil.getWebsiteApp().getAppId() + File.separator
                    + (style != null ? (File.separator + style) : BasicUtil.getWebsiteApp().getAppStyle() ) + (path != null ? (File.separator + path) : ""));
        } else {
            return BasicUtil.getRealPath(uploadTemplatePath + File.separator + BasicUtil.getApp().getAppId() + File.separator
                    + (style != null ? (File.separator + style) : BasicUtil.getApp().getAppStyle()) + (path != null ? (File.separator + path) : ""));
        }
    }


    /**
     * 拼接生成后的路径地址
     * @param path 当前业务路径
     * @param appDir 站点路径，根据应用设置配置
     * @param htmlDir 静态文件根路径，根据yml配置，默认html
     * @return
     */
    public static String buildHtmlPath(String path,String htmlDir,String appDir) {
        return BasicUtil.getRealPath(htmlDir) + File.separator + appDir + File.separator + path
                + HTML_SUFFIX;
    }


    /**
     * 根据文本内容渲染模板
     *
     * @param root    参数值
     * @param content 模板内容
     * @return 渲染后的内容
     */
    public static String rendering(Map root, String content) throws IOException, TemplateException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_0);
        StringTemplateLoader stringLoader = new StringTemplateLoader();
        stringLoader.putTemplate("template", content);
        cfg.setNumberFormat("#");
        cfg.setTemplateLoader(stringLoader);

        Template template = cfg.getTemplate("template", "utf-8");
        StringWriter writer = new StringWriter();
        template.process(root, writer);
        return writer.toString();

    }


    /**
     * 根据模板文件渲染
     *
     * @param templatePath 模板路径
     * @return
     * @throws TemplateNotFoundException
     * @throws MalformedTemplateNameException
     * @throws ParseException
     * @throws IOException
     */
    public static int getPageSize(String templatePath, int defaultSize) {
        //组织模板路径
        String buildTempletPath = ParserUtil.buildTemplatePath();
        // 读取模板文件
        String content = FileUtil.readString(FileUtil.file(buildTempletPath, templatePath), "utf-8");

        // 创建 Pattern 对象
        Pattern pattern = Pattern.compile("\\{(.*?)ispaging=true(.*?)\\}");
        // 现在创建 matcher 对象
        Matcher m = pattern.matcher(content);

        String size = null;
        if (m.find()) {
            size = ReUtil.extractMulti("size=(\\d*)", m.group(1), "$1");
            //没有找到继续找
            if (size == null) {
                size = ReUtil.extractMulti("size=(\\d*)", m.group(2), "$1");
            }

            if (size != null) {
                defaultSize = Integer.parseInt(size);
            }
            LOG.debug("获取分页的size:{}", size);
        }

        return defaultSize;
    }


    /**
     * 渲染模板
     *
     * @param templatePath 模板路径
     * @param map          传入参数
     * @return
     * @throws TemplateNotFoundException
     * @throws MalformedTemplateNameException
     * @throws ParseException
     * @throws IOException
     */
    public static String rendering(String templatePath, Map map)
            throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException {

        //组织模板路径
        String buildTempletPath = ParserUtil.buildTemplatePath();

        //初始化
        if (ftl == null || !buildTempletPath.equals(ftl.baseDir.getPath())) {

            stringLoader = new StringTemplateLoader();
            ftl = new FileTemplateLoader(new File(buildTempletPath));
            MultiTemplateLoader multiTemplateLoader = new MultiTemplateLoader(new TemplateLoader[]{stringLoader, ftl});

            cfg.setNumberFormat("#");
            cfg.setTemplateLoader(multiTemplateLoader);

            //自动导入宏
            ClassPathResource classPathResource = new ClassPathResource("WEB-INF/macro.ftl");

            String template = IOUtils.toString(classPathResource.getInputStream(), "UTF-8");
            stringLoader.putTemplate("macro.ms", template);

            cfg.setClassicCompatible(true);
            cfg.addAutoInclude("macro.ms");
        }



        // 读取模板文件
        String temp = FileUtil.readString(FileUtil.file(buildTempletPath, templatePath), "utf-8");

        //替换标签
        temp = replaceTag(temp);

        //添加自定义模板
        stringLoader.putTemplate("ms:custom:" + templatePath, temp);

        //获取自定义模板
        Template template = null;
        try {
            template = cfg.getTemplate("ms:custom:" + templatePath, Const.UTF8);
        } catch (Exception e) {
            LOG.debug("模版错误");
            e.printStackTrace();
            LOG.debug(temp);
        }

        //设置兼容模式
        cfg.setClassicCompatible(true);
        //设置扩展include
        cfg.setSharedVariable(TAG_PREFIX + "includeEx", new IncludeExTag(buildTempletPath, stringLoader));

        //读取标签
        ITagBiz tagBiz = SpringUtil.getBean(ITagBiz.class);
//        List<TagSqlEntity> list = tagSqlBiz.list();

        List<TagEntity> list = tagBiz.list();

        list.forEach(tagSql -> {
            //添加自定义标签
            if (StrUtil.isNotBlank(tagSql.getTagName())) {

                TagTypeEnum typeEnum = TagTypeEnum.get(tagSql.getTagType());

                if (typeEnum == TagTypeEnum.LIST) {//列表标签
                    cfg.setSharedVariable(TAG_PREFIX + tagSql.getTagName(), new CustomTag(map, tagSql));
                }

                if (typeEnum == TagTypeEnum.SINGLE && (!systemTag1.contains(tagSql.getTagName())
                        //文字内容需要id参数
                        || (map.containsKey("id") && tagSql.getTagName().equals("field"))
                        //分页需要pageTag参数
                        || (map.containsKey("pageTag") && (tagSql.getTagName().equals("pre")
                        || tagSql.getTagName().equals("next") || tagSql.getTagName().equals("page")))
                )) {

                    String sql = null;
                    try {
                        sql = rendering(map, tagSql.getTagSql());
                        List _list = (List) tagBiz.excuteSql(sql);
                        if (_list.size() > 0) {
                            if (_list.get(0) != null) {
                                MapWrapper<String, Object> mw = new MapWrapper<>((HashMap<String, Object>) _list.get(0));
                                //把NClob类型转化成string
                                mw.forEach(x-> {
                                    if (x.getValue() instanceof NClob) {
                                        x.setValue(StringUtil.nclobStr((NClob) x.getValue()));
                                    }
                                });
                            }
                            map.put(tagSql.getTagName(), _list.get(0));
                        }
                    } catch (IOException e) {
                        LOG.error("", e);
                    } catch (TemplateException e) {
                        LOG.error("", e);
                    }
                }
            }
        });

        StringWriter writer = new StringWriter();
        try {
            template.process(map, writer);
            return writer.toString();
        } catch (Exception e) {
            LOG.error("渲染错误", e);
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 标签替换
     *
     * @param content 模板内容
     * @return 替换后的内容
     */
    public static String replaceTag(String content) {
        // 创建 Pattern 对象
        //替include标签 <#include "header.htm" /> 或者 <#include "header.htm">  转换为 <@ms_includeEx template=header.htm/>
        content = content.replaceAll("<#include(.*)/>", StrUtil.format("<@{}includeEx template=$1/>", TAG_PREFIX));
        content = content.replaceAll("<#include(.*)>", StrUtil.format("<@{}includeEx template=$1/>", TAG_PREFIX));

        //替换全局标签{ms:global.name/} 转换为{global.name/}
        content = content.replaceAll("\\{ms:([^\\}]+)/\\}", "\\${$1}");
        //替换全局标签 {@ms:file */} 转换为<@ms_file */>
        content = content.replaceAll("\\{@ms:([^\\}]+)/\\}", StrUtil.format("<@{}$1/>", TAG_PREFIX));

        //替换列表开头标签 {ms:arclist *} 转换为{@ms_arclist */}
        content = content.replaceAll("\\{ms:([^\\}]+)\\}", StrUtil.format("<@{}$1>", TAG_PREFIX));
        //替换列表结束标签 {/ms:arclist *} 转换为{/@ms_arclist}
        content = content.replaceAll("\\{/ms:([^\\}]+)\\}", StrUtil.format("</@{}$1>", TAG_PREFIX));
        //替换内容老的标签 [field.*/] 转换为${filed.*}
        content = content.replaceAll("\\[([^\\]]+)/\\]", "\\${$1}");
        return content;
    }

}
