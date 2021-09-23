package net.mingsoft.mdiy.action;

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
import net.mingsoft.mdiy.biz.IModelBiz;
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
 * 通用模型
 */
@Api(value = "通用模型配置接口")
@Controller
@RequestMapping("/${ms.manager.path}/mdiy/form")
public class FormAction extends BaseAction {

    private final static String TYPE="post";

    /**
     * 注入自定义配置业务层
     */
    @Autowired
    private IModelBiz modelBiz;

    /**
     * 主页
     * @param response
     * @param request
     * @return
     */
    @GetMapping("/index")
    public String index(HttpServletResponse response, HttpServletRequest request){
        return "/mdiy/form/index";
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


    @ApiOperation(value = "导入自定义模型")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "modelJson", value = "json", required =true,paramType="query"),
    })
    @LogAnn(title = "导入",businessType= BusinessTypeEnum.INSERT)
    @PostMapping("/importJson")
    @ResponseBody
    @RequiresPermissions("mdiy:form:importJson")
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
            return ResultData.build().success();
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
    @RequiresPermissions("mdiy:form:updateJson")
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


    @ApiOperation(value = "批量删除自定义模型列表接口")
    @LogAnn(title = "批量删除自定义模型列表接口",businessType= BusinessTypeEnum.DELETE)
    @PostMapping("/delete")
    @ResponseBody
    @RequiresPermissions("mdiy:form:del")
    public ResultData delete(@RequestBody List<ModelEntity> models, HttpServletResponse response, HttpServletRequest request) {
        List<String> ids = models.stream().map(p -> p.getId()).collect(Collectors.toList());
        if (modelBiz.delete(ids)) {
            return ResultData.build().success();
        }else {
            return ResultData.build().error(getResString("err.error",getResString("id")));
        }

    }

}
