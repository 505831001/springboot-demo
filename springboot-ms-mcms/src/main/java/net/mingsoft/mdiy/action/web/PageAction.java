/**
 * The MIT License (MIT) * Copyright (c) 2020 铭软科技(mingsoft.net)

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
package net.mingsoft.mdiy.action.web;

import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.mingsoft.basic.util.BasicUtil;
import net.mingsoft.mdiy.action.BaseAction;
import net.mingsoft.mdiy.biz.IPageBiz;
import net.mingsoft.mdiy.entity.PageEntity;
import net.mingsoft.mdiy.util.ParserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @ClassName:  PageAction
 * @Description:TODO(自定义页面)
 * @author: 铭飞开发团队
 * @date:   2018年12月17日 下午6:10:12
 *
 * @Copyright: 2018 www.mingsoft.net Inc. All rights reserved.
 */
@Api("自定义页码接口")
@Controller("webDiyPath")
@RequestMapping(value={"/mdiyPage"})
public class PageAction extends BaseAction {

    /**
     * 自定义页面业务层
     */
    @Autowired
    private IPageBiz pageBiz;

    @Value("${ms.html-dir:html}")
    private String htmlDir;

    @ApiOperation(value = "自定义页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "diy", value = "请求地址", required = true,paramType="path")
    })
    @GetMapping(value = "/{diy}")
    public void diy(@PathVariable(value = "diy") String diy, HttpServletRequest req, HttpServletResponse resp) {
        Map<String,Object> map = BasicUtil.assemblyRequestMap();
        map.forEach((k,v)->{
            map.put(k,v.toString().replaceAll("('|\"|\\\\)","\\\\$1"));
        });

        //站点编号
        if (BasicUtil.getWebsiteApp() != null) {
            map.put(ParserUtil.APP_DIR, BasicUtil.getWebsiteApp().getAppDir());
            map.put(ParserUtil.URL, BasicUtil.getWebsiteApp().getAppHostUrl());
            map.put(ParserUtil.APP_ID, BasicUtil.getWebsiteApp().getAppId());
        } else {
            map.put(ParserUtil.URL, BasicUtil.getUrl());
            map.put(ParserUtil.APP_DIR, BasicUtil.getApp().getAppDir());
        }

        map.put(ParserUtil.IS_DO,false);
        map.put(ParserUtil.HTML,htmlDir);

        PageEntity page = new PageEntity();
        page.setPageKey(diy);
        PageEntity _page = (PageEntity) pageBiz.getEntity(page);
        try {
            String content = ParserUtil.rendering(_page.getPagePath().replace(" ", ""), map);

            BasicUtil.outString(resp, content);
        } catch (TemplateNotFoundException e1) {
            e1.printStackTrace();
        } catch (MalformedTemplateNameException e1) {
            e1.printStackTrace();
        } catch (ParseException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
