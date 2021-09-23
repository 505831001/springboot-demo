package net.mingsoft.mdiy.action.web;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import net.mingsoft.base.entity.ResultData;
import net.mingsoft.mdiy.action.BaseAction;
import net.mingsoft.mdiy.biz.IModelBiz;
import net.mingsoft.mdiy.entity.ModelEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 通用模型
 */
@Api(value = "通用模型配置接口")
@Controller("webFormAction")
@RequestMapping("/mdiy/form")
public class FormAction extends BaseAction {

    private final static String TYPE = "post";

    /**
     * 注入自定义模型业务层
     */
    @Autowired
    private IModelBiz modelBiz;

    /**
     * 通用渲染表单
     *
     * @param response
     * @param request
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "modelName", required = true),
    })
    @GetMapping("/get")
    @ResponseBody
    public ResultData get(String modelName, HttpServletResponse response, HttpServletRequest request) {
        //获取表单id
        if (StringUtils.isBlank(modelName)) {
            return ResultData.build().error();
        }
        QueryWrapper<ModelEntity> queryWrapper = new QueryWrapper<ModelEntity>();
        queryWrapper.like("model_name", modelName);
        ModelEntity model = modelBiz.getOne(queryWrapper);
        if(model!=null) {
            //判断是否允许外部提交
            if(Boolean.parseBoolean(JSONObject.parseObject(model.getModelJson(), Map.class).get("isWebSubmit").toString())) {
                return ResultData.build().success(model);
            }
        }
        return ResultData.build().error();
    }

}
