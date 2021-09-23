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

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapWrapper;
import freemarker.core.Environment;
import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.*;
import net.mingsoft.basic.util.SpringUtil;
import net.mingsoft.basic.util.StringUtil;
import net.mingsoft.mdiy.biz.ITagBiz;
import net.mingsoft.mdiy.entity.TagEntity;
import net.mingsoft.mdiy.util.ParserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.NClob;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: 铭飞团队
 * @Description: 自定义标签
 * @Date: Create in 2020/06/23 9:16
 */

public class CustomTag implements TemplateDirectiveModel {
    protected static BeansWrapper build = new BeansWrapperBuilder(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS).build();
    /*
     * log4j日志记录
     */
    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    /**
     * 解析标签用的map
     */
    private Map data;
    /**
     * 标签sql模板
     */
    private TagEntity tag;
    /**
     * 传出的变量名
     */
    private String variableName;



    private Map tagParams;


    public CustomTag() {

    }
    public CustomTag(Map data, TagEntity tag) {
        this.data = data;
        this.tag = tag;
        this.variableName = "field";
    }

    public Map getTagParams() {
        return this.tagParams;
    }

    @Override
    public void execute(Environment environment, Map params, TemplateModel[] templateModels,
                        TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {

        //如果只是获取标签属性
        if(data==null && tag == null) {
            this.tagParams = params;
            return;
        }

        HashMap<Object, Object> _params = CollUtil.newHashMap();
        //历史变量
        //将外部传入的压入
        _params.putAll(data);

        TemplateModel oldVar = environment.getVariable(variableName);
        //压入了栏目则传入sql模板
        TemplateModel column = environment.getVariable(ParserUtil.COLUMN);
        if (column != null) {
            _params.put(ParserUtil.COLUMN, build.unwrap(column));
        }

        //将标签传入参数逐一的压入
        params.forEach((k, v) -> {
            if (v instanceof TemplateModel) {
                try {
                    _params.put(k, build.unwrap((TemplateModel) v));
                } catch (TemplateModelException e) {
                    e.printStackTrace();
                    LOG.error("转换参数错误 key:{} -{}", k, e);
                }
            }
        });



        LOG.debug("标签sql处理");
        ITagBiz tagSqlBiz = SpringUtil.getBean(ITagBiz.class);
        String sql = ParserUtil.rendering(_params, tag.getTagSql());

        List<Map> _list = (List) tagSqlBiz.excuteSql(sql);//执行一条查询

        AtomicInteger index = new AtomicInteger(1);
        //渲染标签
        _list.forEach(x -> {
            //实现索引
            x.put("index", index.getAndIncrement());
            try {
                //把NClob类型转化成string
                MapWrapper<String, Object> mw = new MapWrapper<>((HashMap<String, Object>) x);
                mw.forEach(y-> {
                    if (y.getValue() instanceof NClob) {
                        y.setValue(StringUtil.nclobStr((NClob) y.getValue()));
                    }
                });

                TemplateModel columnModel = build.wrap(x);
                //如果自定义了变量则赋值自定义变量
                if (templateModels.length > 0) {
                    templateModels[0] = columnModel;
                } else {
                    environment.setVariable(variableName, columnModel);
                }

                //压入 以便嵌套使用
                environment.setVariable(ParserUtil.COLUMN, columnModel);
                //渲染
                templateDirectiveBody.render(environment.getOut());
                //渲染完成还原变量
                environment.setVariable(ParserUtil.COLUMN, column);
                environment.setVariable(variableName, oldVar);

            } catch (TemplateModelException e) {
                e.printStackTrace();
            } catch (TemplateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


}
