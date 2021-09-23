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
package net.mingsoft.mdiy.action;

import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.mingsoft.base.entity.BaseEntity;
import net.mingsoft.base.entity.ResultData;
import net.mingsoft.basic.annotation.LogAnn;
import net.mingsoft.basic.bean.EUListBean;
import net.mingsoft.basic.constant.e.BusinessTypeEnum;
import net.mingsoft.basic.util.BasicUtil;
import net.mingsoft.basic.util.StringUtil;
import net.mingsoft.mdiy.biz.IPageBiz;
import net.mingsoft.mdiy.entity.PageEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 自定义页面表管理控制层
 * @author 铭飞开发团队
 * @version
 * 版本号：1<br/>
 * 创建日期：2017-8-11 14:01:54<br/>
 * 历史修订：<br/>
 */
@Api(value = "自定义页面接口")
@Controller
@RequestMapping("/${ms.manager.path}/mdiy/page")
public class PageAction extends net.mingsoft.mdiy.action.BaseAction{

    /**
     * 注入自定义页面表业务层
     */
    @Autowired
    private IPageBiz pageBiz;

    /**
     * 返回主界面index
     */
    @GetMapping("/index")
    public String index(HttpServletResponse response,HttpServletRequest request){
        return "/mdiy/page/index";
    }

    /**
     * 查询自定义页面表列表
     * @param page 自定义页面表实体
     * <i>page参数包含字段信息参考：</i><br/>
     * pageAppId 应用id<br/>
     * pagePath 自定义页面绑定模板的路径<br/>
     * pageTitle 自定义页面标题<br/>
     * pageKey 自定义页面访问路径<br/>
     * <dt><span class="strong">返回</span></dt><br/>
     * <dd>[<br/>
     * { <br/>
     * pageAppId: 应用id<br/>
     * pagePath: 自定义页面绑定模板的路径<br/>
     * pageTitle: 自定义页面标题<br/>
     * pageKey: 自定义页面访问路径<br/>
     * }<br/>
     * ]</dd><br/>
     */
    @ApiOperation(value = "查询自定义页面列表接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pagePath", value = "自定义页面绑定模板的路径", required = false,paramType="query"),
            @ApiImplicitParam(name = "pageTitle", value = "自定义页面标题", required = false,paramType="query"),
            @ApiImplicitParam(name = "pageKey", value = "自定义页面访问路径", required = false,paramType="query")
    })
    @GetMapping("/list")
    @ResponseBody
    public ResultData list(@ModelAttribute @ApiIgnore PageEntity page, HttpServletResponse response, HttpServletRequest request, @ApiIgnore ModelMap model) {
        BasicUtil.startPage();
        List pageList = pageBiz.query(page);
        return ResultData.build().success(new EUListBean(pageList,(int)BasicUtil.endPage(pageList).getTotal()));
    }

    /**
     * 返回编辑界面page_form
     */
    @GetMapping("/form")
    public String form(@ModelAttribute PageEntity page,HttpServletResponse response,HttpServletRequest request,@ApiIgnore ModelMap model){
        if(ObjectUtil.isNotEmpty(page) && StringUtils.isNotEmpty(page.getId())){
            BaseEntity pageEntity = pageBiz.getEntity(Integer.parseInt(page.getId()));
            model.addAttribute("pageEntity",pageEntity);
        }

        return "/mdiy/page/form";
    }

    /**
     * 获取自定义页面表
     * @param page 自定义页面表实体
     * <i>page参数包含字段信息参考：</i><br/>
     * pageAppId 应用id<br/>
     * pagePath 自定义页面绑定模板的路径<br/>
     * pageTitle 自定义页面标题<br/>
     * pageKey 自定义页面访问路径<br/>
     * <dt><span class="strong">返回</span></dt><br/>
     * <dd>{ <br/>
     * pageAppId: 应用id<br/>
     * pagePath: 自定义页面绑定模板的路径<br/>
     * pageTitle: 自定义页面标题<br/>
     * pageKey: 自定义页面访问路径<br/>
     * }</dd><br/>
     */
    @ApiOperation(value = "获取自定义页面接口")
    @ApiImplicitParam(name = "id", value = "自定义页面编号", required = true,paramType="query")
    @GetMapping("/get")
    @ResponseBody
    public ResultData get(@ModelAttribute @ApiIgnore PageEntity page,HttpServletResponse response, HttpServletRequest request,@ApiIgnore ModelMap model){
        if(StringUtils.isEmpty(page.getId())){
            return ResultData.build().error( getResString("err.error", this.getResString("page.id")));
        }
        PageEntity _page = (PageEntity)pageBiz.getEntity(Integer.parseInt(page.getId()));
        return ResultData.build().success(_page);
    }

    /**
     * 保存自定义页面表实体
     * @param page 自定义页面表实体
     * <i>page参数包含字段信息参考：</i><br/>
     * pageAppId 应用id<br/>
     * pagePath 自定义页面绑定模板的路径<br/>
     * pageTitle 自定义页面标题<br/>
     * pageKey 自定义页面访问路径<br/>
     * <dt><span class="strong">返回</span></dt><br/>
     * <dd>{ <br/>
     * pageAppId: 应用id<br/>
     * pagePath: 自定义页面绑定模板的路径<br/>
     * pageTitle: 自定义页面标题<br/>
     * pageKey: 自定义页面访问路径<br/>
     * }</dd><br/>
     */
    @ApiOperation(value = "保存自定义页面接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pagePath", value = "自定义页面绑定模板的路径", required = true,paramType="query"),
            @ApiImplicitParam(name = "pageTitle", value = "自定义页面标题", required = true,paramType="query"),
            @ApiImplicitParam(name = "pageKey", value = "自定义页面访问路径", required = true,paramType="query"),

    })
    @LogAnn(title = "保存自定义页面接口",businessType= BusinessTypeEnum.INSERT)
    @PostMapping("/save")
    @ResponseBody
    @RequiresPermissions("mdiy:page:save")
    public ResultData save(@ModelAttribute @ApiIgnore PageEntity page, HttpServletResponse response, HttpServletRequest request) {

        //验证自定义页面绑定模板的路径的值是否合法
        if(StringUtils.isBlank(page.getPagePath())){
            return ResultData.build().error(getResString("err.empty", this.getResString("page.path")));
        }
        if(!StringUtil.checkLength(page.getPagePath()+"", 1, 255)){
            return ResultData.build().error( getResString("err.length", this.getResString("page.path"), "1", "255"));
        }
        //验证自定义页面标题的值是否合法
        if(StringUtils.isBlank(page.getPageTitle())){
            return ResultData.build().error(getResString("err.empty", this.getResString("page.title")));
        }
        if(!StringUtil.checkLength(page.getPageTitle()+"", 1, 255)){
            return ResultData.build().error( getResString("err.length", this.getResString("page.title"), "1", "255"));
        }
        //验证自定义页面访问路径的值是否合法
        if(StringUtils.isBlank(page.getPageKey())){
            return ResultData.build().error(getResString("err.empty", this.getResString("page.key")));
        }
        if(!StringUtil.checkLength(page.getPageKey()+"", 1, 255)){
            return ResultData.build().error( getResString("err.length", this.getResString("page.key"), "1", "255"));
        }
        pageBiz.saveEntity(page);
        return ResultData.build().success(page);
    }

    /**
     * @param pages 自定义页面表实体
     * <i>page参数包含字段信息参考：</i><br/>
     * id:多个id直接用逗号隔开,例如id=1,2,3,4
     * 批量删除自定义页面表
     *            <dt><span class="strong">返回</span></dt><br/>
     *            <dd>{code:"错误编码",<br/>
     *            result:"true｜false",<br/>
     *            resultMsg:"错误信息"<br/>
     *            }</dd>
     */
    @ApiOperation(value = "批量删除自定义页面接口")
    @LogAnn(title = "批量删除自定义页面接口",businessType= BusinessTypeEnum.DELETE)
    @PostMapping("/delete")
    @ResponseBody
    @RequiresPermissions("mdiy:page:del")
    public ResultData delete(@RequestBody List<PageEntity> pages,HttpServletResponse response, HttpServletRequest request) {
        int[] ids = new int[pages.size()];
        for(int i = 0;i<pages.size();i++){
            ids[i] = Integer.parseInt(pages.get(i).getId());
        }
        pageBiz.delete(ids);
        return ResultData.build().success();
    }

    /**
     * 更新自定义页面表信息自定义页面表
     * @param page 自定义页面表实体
     * <i>page参数包含字段信息参考：</i><br/>
     * pageAppId 应用id<br/>
     * pagePath 自定义页面绑定模板的路径<br/>
     * pageTitle 自定义页面标题<br/>
     * pageKey 自定义页面访问路径<br/>
     * <dt><span class="strong">返回</span></dt><br/>
     * <dd>{ <br/>
     * pageAppId: 应用id<br/>
     * pagePath: 自定义页面绑定模板的路径<br/>
     * pageTitle: 自定义页面标题<br/>
     * pageKey: 自定义页面访问路径<br/>
     * }</dd><br/>
     */
    @ApiOperation(value = "更新自定义页面接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "自定义页面编号", required =true,paramType="query"),
            @ApiImplicitParam(name = "pagePath", value = "自定义页面绑定模板的路径", required = true,paramType="query"),
            @ApiImplicitParam(name = "pageTitle", value = "自定义页面标题", required = true,paramType="query"),
            @ApiImplicitParam(name = "pageKey", value = "自定义页面访问路径", required = true,paramType="query"),
    })
    @LogAnn(title = "更新自定义页面接口",businessType= BusinessTypeEnum.UPDATE)
    @PostMapping("/update")
    @ResponseBody
    @RequiresPermissions("mdiy:page:update")
    public ResultData update(@ModelAttribute @ApiIgnore PageEntity page, HttpServletResponse response,
                             HttpServletRequest request) {
        //验证自定义页面绑定模板的路径的值是否合法
        if(StringUtils.isBlank(page.getPagePath())){
            return ResultData.build().error(getResString("err.empty", this.getResString("page.path")));
        }
        if(!StringUtil.checkLength(page.getPagePath()+"", 1, 255)){
            return ResultData.build().error( getResString("err.length", this.getResString("page.path"), "1", "255"));
        }
        //验证自定义页面标题的值是否合法
        if(StringUtils.isBlank(page.getPageTitle())){
            return ResultData.build().error(getResString("err.empty", this.getResString("page.title")));
        }
        if(!StringUtil.checkLength(page.getPageTitle()+"", 1, 255)){
            return ResultData.build().error( getResString("err.length", this.getResString("page.title"), "1", "255"));
        }
        //验证自定义页面访问路径的值是否合法
        if(StringUtils.isBlank(page.getPageKey())){
            return ResultData.build().error(getResString("err.empty", this.getResString("page.key")));
        }
        if(!StringUtil.checkLength(page.getPageKey()+"", 1, 255)){
            return ResultData.build().error( getResString("err.length", this.getResString("page.key"), "1", "255"));
        }
        pageBiz.updateEntity(page);
        return ResultData.build().success(page);
    }



    /**
     * 校验参数*/
    @GetMapping("/verify")
    @ResponseBody
    public ResultData verify(String fieldName, String fieldValue, String id, String idName){
        boolean verify = false;
        if(StringUtils.isBlank(id)){
            verify = super.validated("mdiy_page",fieldName,fieldValue);
        }else{
            verify = super.validated("mdiy_page",fieldName,fieldValue,id,idName);
        }
        if(verify){
            return ResultData.build().success(false);
        }else {
            return ResultData.build().success(true);
        }
    }

}
