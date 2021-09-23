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

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.mingsoft.base.entity.ResultData;
import net.mingsoft.base.util.JSONObject;
import net.mingsoft.basic.annotation.LogAnn;
import net.mingsoft.basic.bean.EUListBean;
import net.mingsoft.basic.constant.e.BusinessTypeEnum;
import net.mingsoft.basic.util.BasicUtil;
import net.mingsoft.mdiy.bean.ModelJsonBean;
import net.mingsoft.mdiy.biz.IConfigBiz;
import net.mingsoft.mdiy.biz.IModelBiz;
import net.mingsoft.mdiy.entity.ConfigEntity;
import net.mingsoft.mdiy.entity.ModelEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 自定义配置表管理控制层
 * @author 铭飞开发团队
 * @version
 * 版本号：1<br/>
 * 创建日期：2017-8-12 15:58:29<br/>
 * 历史修订：<br/>
 */
@Api(value = "自定义配置接口")
@Controller("mdiyConfig")
@RequestMapping("/${ms.manager.path}/mdiy/config")
public class ConfigAction extends BaseAction {

    private final static String TYPE="config";
    /**
     * 注入自定义配置业务层
     */
    @Autowired
    private IModelBiz modelBiz;

    /**
     * 注入自定义配置业务层
     */
    @Autowired
    private IConfigBiz configBiz;


    /**
     * 返回主界面index
     */
    @GetMapping("/index")
    public String index(HttpServletResponse response,HttpServletRequest request){

        return "/mdiy/config/index";
    }

    @ApiOperation(value = "查询自定义模型列表接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "modelName", value = "模型名称", required =false,paramType="query"),
            @ApiImplicitParam(name = "modelTableName", value = "模型表名", required =false,paramType="query"),
            @ApiImplicitParam(name = "appId", value = "应用编号", required =false,paramType="query"),
            @ApiImplicitParam(name = "modelJson", value = "json", required =false,paramType="query"),
            @ApiImplicitParam(name = "createBy", value = "创建人", required =false,paramType="query"),
            @ApiImplicitParam(name = "createDate", value = "创建时间", required =false,paramType="query"),
            @ApiImplicitParam(name = "updateBy", value = "修改人", required =false,paramType="query"),
            @ApiImplicitParam(name = "updateDate", value = "修改时间", required =false,paramType="query"),
            @ApiImplicitParam(name = "del", value = "删除标记", required =false,paramType="query"),
            @ApiImplicitParam(name = "id", value = "编号", required =false,paramType="query"),
    })
    @GetMapping("/list")
    @ResponseBody
    public ResultData list(@ModelAttribute @ApiIgnore ModelEntity modelEntity, HttpServletResponse response, HttpServletRequest request) {
        modelEntity.setModelCustomType(TYPE);
        BasicUtil.startPage();
        List modelList = modelBiz.list(new QueryWrapper<>(modelEntity));



        return ResultData.build().success(new EUListBean(modelList,(int)BasicUtil.endPage(modelList).getTotal()));
    }

    /**
     * 通用渲染表单
     * @param response
     * @param request
     * @return
     */
    @GetMapping("/get")
    @ResponseBody
    public ResultData get(ModelEntity modelEntity, HttpServletResponse response, HttpServletRequest request){
        if(modelEntity==null || StringUtils.isEmpty(modelEntity.getModelName())){
            return ResultData.build().error(this.getResString("err.error",this.getResString("model.name")));
        }
        modelEntity.setModelCustomType(TYPE);
        ModelEntity model = modelBiz.getOne(new QueryWrapper<>(modelEntity));
        return ResultData.build().success(model);
    }




    @ApiOperation(value = "批量删除自定义模型列表接口")
    @LogAnn(title = "批量删除自定义模型列表接口",businessType= BusinessTypeEnum.DELETE)
    @PostMapping("/delete")
    @ResponseBody
    @RequiresPermissions("mdiy:config:del")
    public ResultData delete(@RequestBody List<ModelEntity> models, HttpServletResponse response, HttpServletRequest request) {
        List<String> ids = models.stream().map(p -> p.getId()).collect(Collectors.toList());
        if(CollUtil.isNotEmpty(ids)){
            for(String id : ids){
                ModelEntity modelEntity = modelBiz.getById(id);
                if (modelEntity != null) {
                    ConfigEntity configEntity = new ConfigEntity();
                    configEntity.setConfigName(modelEntity.getModelName());
                    configBiz.remove(new QueryWrapper<>(configEntity));
                }
            }
        }
        if (modelBiz.removeByIds(ids)) {
            return ResultData.build().success();
        }else {
            return ResultData.build().error(getResString("err.error",getResString("id")));
        }
    }

    @ApiOperation(value = "导入自定义模型")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "modelJson", value = "json", required =true,paramType="query"),
    })
    @LogAnn(title = "导入",businessType= BusinessTypeEnum.INSERT)
    @PostMapping("/importJson")
    @ResponseBody
    @RequiresPermissions("mdiy:config:importJson")
    public ResultData importJson(@ModelAttribute @ApiIgnore ModelEntity modelEntity, HttpServletResponse response, HttpServletRequest request, BindingResult result) {
        //验证json的值是否合法
        if(StringUtils.isBlank(modelEntity.getModelJson())){
            return ResultData.build().error(getResString("err.empty", this.getResString("model.json")));
        }
        ModelJsonBean modelJsonBean = new ModelJsonBean();
        try{
            modelJsonBean = JSONObject.parseObject(modelEntity.getModelJson(), ModelJsonBean.class);
        }catch (Exception e){
            return ResultData.build().error(getResString("err.error", this.getResString("model.json")));
        }
        // 保存导入的json模型
        if(modelBiz.importConfig(TYPE, modelJsonBean)){
            ConfigEntity configEntity = new ConfigEntity();
            configEntity.setConfigName(modelJsonBean.getTitle());
            configBiz.save(configEntity);
            return ResultData.build().success(configEntity);
        }else {
            return ResultData.build().error(getResString("err.exist", this.getResString("table.name")));
        }
    }

    @ApiOperation(value = "更新导入自定义模型")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "modelJson", value = "json", required =true,paramType="query"),
    })
    @LogAnn(title = "导入",businessType= BusinessTypeEnum.INSERT)
    @PostMapping("/updateJson")
    @ResponseBody
    @RequiresPermissions("mdiy:config:updateJson")
    public ResultData updateJson(@ModelAttribute @ApiIgnore ModelEntity modelEntity, HttpServletResponse response, HttpServletRequest request, BindingResult result) {
        //验证json的值是否合法
        if(StringUtils.isBlank(modelEntity.getModelJson())){
            return ResultData.build().error(getResString("err.empty", this.getResString("model.json")));
        }
        if(StringUtils.isBlank(modelEntity.getId())){
            return ResultData.build().error(getResString("err.empty", this.getResString("id")));
        }
        ModelJsonBean modelJsonBean = new ModelJsonBean();
        try{
            modelJsonBean = JSONObject.parseObject(modelEntity.getModelJson(), ModelJsonBean.class);
        }catch (Exception e){
            return ResultData.build().error(getResString("err.error", this.getResString("model.json")));
        }
        // 保存导入的json模型
        if(modelBiz.updateConfig(modelEntity.getId(), modelJsonBean)){
            return ResultData.build().success();
        }else {
            return ResultData.build().error(getResString("err.exist", this.getResString("table.name")));
        }
    }

}
