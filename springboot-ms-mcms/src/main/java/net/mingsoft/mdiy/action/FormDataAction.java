package net.mingsoft.mdiy.action;

import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.mingsoft.base.entity.ResultData;
import net.mingsoft.basic.annotation.LogAnn;
import net.mingsoft.basic.bean.EUListBean;
import net.mingsoft.basic.constant.e.BusinessTypeEnum;
import net.mingsoft.basic.util.BasicUtil;
import net.mingsoft.mdiy.biz.IModelBiz;
import net.mingsoft.mdiy.biz.IModelDataBiz;
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
import java.util.Map;

/**
 * 通用业务数据
 */
@Api(value = "通用模型业务接口")
@Controller
@RequestMapping("/${ms.manager.path}/mdiy/formData")
public class FormDataAction extends BaseAction {


    /**
     * 注入自定义配置业务层
     */
    @Autowired
    private IModelDataBiz modelDataBiz;

    /**
     * 注入自定义配置业务层
     */
    @Autowired
    private IModelBiz modelBiz;

    /**
     * 扩展模型表单
     */
    @GetMapping("/index")
    public String list(HttpServletResponse response, HttpServletRequest request, @ApiIgnore ModelMap model){
        return "/mdiy/form/data/index";
    }

    /**
     * 扩展模型表单
     */
    @GetMapping("/form")
    public String form(HttpServletResponse response, HttpServletRequest request, @ApiIgnore ModelMap model){
        return "/mdiy/form/data/form";
    }

    /**
     * 提供前端查询自定义表单提交数据
     *
     * @param request
     * @param response
     */
    @ApiOperation(value = "提供前端查询自定义表单提交数据")
    @ApiImplicitParam(name = "modelId", value = "模型编号", required = true, paramType = "query")
    @RequestMapping("/queryData")
    @ResponseBody
    public ResultData queryData( HttpServletRequest request, HttpServletResponse response) {
        //获取表单id
        Map<String, Object> map = BasicUtil.assemblyRequestMap();
        int modelId = Integer.parseInt(map.get("modelId").toString());
        if(modelId <= 0){
            return ResultData.build().error(getResString("err.empty",getResString("model.id")));
        }
        List list = modelDataBiz.queryDiyFormData(modelId,BasicUtil.assemblyRequestMap());
        if (ObjectUtil.isNotNull(list) ) {
            return ResultData.build().success(new EUListBean(list,(int)BasicUtil.endPage(list).getTotal()));
        }
        return ResultData.build().error();
    }

    /**
     * 提供前端查询自定义表单提交数据
     *
     * @param request
     * @param response
     */
    @ApiOperation(value = "提供前端查询自定义表单提交数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "modelId", value = "模型编号", required = true, paramType = "query"),
            @ApiImplicitParam(name = "id", value = "主键编号", required = true, paramType = "query")
    })
    @GetMapping("/getData")
    @ResponseBody
    public ResultData getData( HttpServletRequest request, HttpServletResponse response) {
        //获取表单id
        String modelId = BasicUtil.getString("modelId");
        String id = BasicUtil.getString("id");
        if(StringUtils.isEmpty(modelId)){
            return ResultData.build().error(getResString("err.empty",getResString("model.id")));
        }
        if(StringUtils.isEmpty(id)){
            return ResultData.build().error(getResString("err.empty",getResString("id")));
        }
        Object object = modelDataBiz.getFormData(modelId,id);
        if (ObjectUtil.isNotNull(object) ) {
            return ResultData.build().success(object);
        }
        return ResultData.build().error();
    }


    @ApiOperation("保存")
    @ApiImplicitParam(name = "modelId", value = "模型编号", required = true, paramType = "query")
    @LogAnn(title = "更新自定义业务数据",businessType= BusinessTypeEnum.INSERT)
    @PostMapping("save")
    @ResponseBody
    @RequiresPermissions("mdiy:formData:save")
    public ResultData save(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = BasicUtil.assemblyRequestMap();
        int modelId = Integer.parseInt(map.get("modelId").toString());
        if(modelId <= 0){
            return ResultData.build().error(getResString("err.empty",getResString("model.id")));
        }
        if (modelDataBiz.saveDiyFormData(modelId,map)) {
            return ResultData.build().success();
        }else {
            return ResultData.build().error(getResString("err.error",getResString("model.id")));
        }
    }

    @ApiOperation("更新自定义业务数据")
    @ApiImplicitParam(name = "modelId", value = "模型编号", required = true, paramType = "query")
    @LogAnn(title = "更新自定义业务数据",businessType= BusinessTypeEnum.UPDATE)
    @PostMapping("update")
    @ResponseBody
    @RequiresPermissions("mdiy:formData:update")
    public ResultData update(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = BasicUtil.assemblyRequestMap();
        int modelId = Integer.parseInt(map.get("modelId").toString());
        if(modelId <= 0){
            return ResultData.build().error(getResString("err.empty",getResString("model.id")));
        }
        if (modelDataBiz.updateDiyFormData(modelId,map)) {
            return ResultData.build().success();
        }else {
            return ResultData.build().error(getResString("err.error",getResString("model.id")));
        }
    }

    @ApiOperation(value = "批量删除自定义业务数据接口")
    @LogAnn(title = "批量删除自定义业务数据接口",businessType= BusinessTypeEnum.DELETE)
    @PostMapping("delete")
    @ResponseBody
    @RequiresPermissions("mdiy:formData:del")
    public ResultData delete(@RequestParam("modelId") String modelId, HttpServletResponse response, HttpServletRequest request) {
        int[] ids = BasicUtil.getInts("ids",",");
        if (StringUtils.isEmpty(modelId)) {
            return ResultData.build().error(getResString("err.empty",getResString("model.id")));
        }
        for (int id : ids) {
            modelDataBiz.deleteQueryDiyFormData(id,modelId);
        }
        return ResultData.build().success();
    }

}
