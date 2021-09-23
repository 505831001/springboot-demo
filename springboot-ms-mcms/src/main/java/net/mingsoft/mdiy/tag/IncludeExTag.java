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
package net.mingsoft.mdiy.tag;

import cn.hutool.core.io.FileUtil;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.core.Environment;
import freemarker.core._MiscTemplateException;
import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.*;
import net.mingsoft.basic.util.SpringUtil;
import net.mingsoft.mdiy.util.ParserUtil;

import java.io.IOException;
import java.util.Map;

/**
 * @Author: huise
 * @Description: 自定义引入模板标签
 * @Date: Create in 2020/06/24 15:37
 */
public class IncludeExTag implements TemplateDirectiveModel {

    /**
     * 外部key值
     */
    private static final String TEMPLATE_KEY = "template";

    protected static BeansWrapper build = new BeansWrapperBuilder(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS).build();

    /**
     * 模板根目录
     */
    private String basePath;
    /**
     * 字符串加载器
     */
    private StringTemplateLoader stringLoader;

    public IncludeExTag(String basePath, StringTemplateLoader stringLoader) {
        this.basePath = basePath;
        this.stringLoader = stringLoader;
    }

    @Override
    public void execute(Environment environment, Map params, TemplateModel[] templateModel,
                        TemplateDirectiveBody directiveBody) throws TemplateException, IOException {

        TemplateLoader templateLoader = environment.getConfiguration().getTemplateLoader();
        org.springframework.core.env.Environment SpringEnv = SpringUtil.getBean(org.springframework.core.env.Environment.class);
        String uploadTemplatePath = SpringEnv.getProperty("ms.upload.template");
        String path = build.unwrap((TemplateModel) params.get(uploadTemplatePath)).toString();

        if (!params.containsKey(uploadTemplatePath)) {
            throw new MalformedTemplateNameException("missing required parameter '" + uploadTemplatePath, "'");
        }
        if (templateLoader.findTemplateSource(path) == null) {
            throw new _MiscTemplateException(environment, "Missing template file path:" + path);
        }
        //替换模板
        String temp = FileUtil.readString(FileUtil.file(basePath, path).getPath(), "utf-8");
        //正则替换标签
        temp = ParserUtil.replaceTag(temp);
        stringLoader.putTemplate("ms:custom:" + path, temp);
        Template template = environment.getTemplateForInclusion("ms:custom:" + path, null, true);
        //引入模板
        environment.include(template);


    }
}
