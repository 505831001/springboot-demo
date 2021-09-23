package net.mingsoft.mdiy.action.web;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.mingsoft.base.entity.ResultData;
import net.mingsoft.basic.bean.EUListBean;
import net.mingsoft.basic.util.BasicUtil;
import net.mingsoft.mdiy.action.BaseAction;
import net.mingsoft.mdiy.bean.ModelJsonBean;
import net.mingsoft.mdiy.biz.IModelBiz;
import net.mingsoft.mdiy.biz.IModelDataBiz;
import net.mingsoft.mdiy.entity.ModelEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
@Controller("webFormDataAction")
@RequestMapping("/mdiy/formData")
public class FormDataAction extends BaseAction {

    /**
     * 注入自定义配置业务层
     */
    @Autowired
    private IModelDataBiz modelDataBiz;

    /**
     * 注入自定义模型
     */
    @Autowired
    private IModelBiz modelBiz;


    @ApiOperation("保存")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "modelName", value = "业务名称", required = true),
            @ApiImplicitParam(name = "rand_code", value = "验证码", required = true, paramType = "query"),
    })
    @PostMapping("save")
    @ResponseBody
    public ResultData save(@RequestParam("modelName")String modelName, HttpServletRequest request, HttpServletResponse response) {

        LOG.debug("保存表单");
        //验证码
        if (!checkRandCode()) {
            LOG.debug("验证码错误");
            return ResultData.build().error(getResString("err.error", this.getResString("rand.code")));
        }
        //获取表单id
        if (StringUtils.isBlank(modelName)) {
            return ResultData.build().error("moelName 空值");
        }
        QueryWrapper<ModelEntity> queryWrapper = new QueryWrapper<ModelEntity>();
        queryWrapper.like("model_name", modelName);
        queryWrapper.like("model_custom_type", "post");
        ModelEntity model = modelBiz.getOne(queryWrapper);
        if (model != null) {
            //判断是否允许外部提交
            if (!Boolean.parseBoolean(JSONObject.parseObject(model.getModelJson(), Map.class).get("isWebSubmit").toString())) {
                ResultData.build().error();
            }

            Map<String, Object> map = BasicUtil.assemblyRequestMap();
            if (modelDataBiz.saveDiyFormData(Integer.parseInt(model.getId()), map)) {
                return ResultData.build().success();
            } else {
                return ResultData.build().error();
            }
        }

        return ResultData.build().error();

    }

    /**
     * 提供前端查询自定义表单提交数据
     *
     * @param modelName 业务名称
     * @param request
     * @param response
     */
    @ApiOperation(value = "提供前端查询自定义表单提交数据")
    @ApiImplicitParam(name = "modelName", value = "业务名称", required = true, paramType = "path")
    @GetMapping("list/")
    @ResponseBody
    public ResultData queryData(@RequestParam("modelName")String modelName, HttpServletRequest request, HttpServletResponse response) {
        //判断传入的加密数字是否能转换成整形
        QueryWrapper<ModelEntity> queryWrapper = new QueryWrapper<ModelEntity>();
        queryWrapper.like("model_name", modelName);
        ModelEntity model = modelBiz.getOne(queryWrapper);
        if (model != null) {
            //判断是否允许外部提交
            if (!Boolean.parseBoolean(JSONObject.parseObject(model.getModelJson(), Map.class).get("isWebSubmit").toString())) {
                ResultData.build().error();
            }
            List list = modelDataBiz.queryDiyFormData(Integer.parseInt(model.getId()), BasicUtil.assemblyRequestMap());
            if (ObjectUtil.isNotNull(list)) {
                return ResultData.build().success(new EUListBean(list, (int) BasicUtil.endPage(list).getTotal()));
            }
            return ResultData.build().error();
        }
        return ResultData.build().error();
    }


}
