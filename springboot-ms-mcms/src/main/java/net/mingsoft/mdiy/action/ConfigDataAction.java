package net.mingsoft.mdiy.action;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.mingsoft.base.entity.ResultData;
import net.mingsoft.basic.annotation.LogAnn;
import net.mingsoft.basic.constant.e.BusinessTypeEnum;
import net.mingsoft.basic.util.BasicUtil;
import net.mingsoft.mdiy.biz.IConfigBiz;
import net.mingsoft.mdiy.biz.IModelBiz;
import net.mingsoft.mdiy.entity.ConfigEntity;
import net.mingsoft.mdiy.entity.ModelEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 通用模型配置数据
 */
@Api(value = "通用模型配置接口")
@Controller
@RequestMapping("/${ms.manager.path}/mdiy/configData")
public class ConfigDataAction extends BaseAction {

    private final static String TYPE="config";
    /**
     * 注入自定义配置业务层
     */
    @Autowired
    private IConfigBiz configBiz;

    /**
     * 注入自定义模型业务层
     */
    @Autowired
    private IModelBiz modelBiz;


    /**
     * 自定义配置
     */
    @GetMapping("/form")
    public String form(HttpServletResponse response, HttpServletRequest request, @ApiIgnore ModelMap model) {
        return "/mdiy/config/data/form";
    }

    /**
     * 配置数据获取
     *
     * @param response
     * @param request
     * @return
     */
    @GetMapping("/get")
    @ResponseBody
    public ResultData get(HttpServletResponse response, HttpServletRequest request) {
        String modelId = BasicUtil.getString("modelId");
        if (StringUtils.isEmpty(modelId)) {
            return ResultData.build().error(getResString("err.empty",getResString("model.id")));
        }
        ModelEntity modelEntity = modelBiz.getById(modelId);
        if (modelEntity == null) {
            return ResultData.build().error(getResString("err.error", this.getResString("model.id")));
        }
        ConfigEntity configEntity = new ConfigEntity();
        configEntity.setConfigName(modelEntity.getModelName());
        configEntity =  configBiz.getOne(new QueryWrapper<>(configEntity));
        if (configEntity == null) {
            return ResultData.build().error(getResString("err.error", getResString("config.name")));
        }
        return ResultData.build().success(JSONObject.parseObject(configEntity.getConfigData()));
    }

    @ApiOperation(value = "更新自定义配置")
    @LogAnn(title = "更新自定义配置", businessType = BusinessTypeEnum.UPDATE)
    @PostMapping("/update")
    @ResponseBody
    @RequiresPermissions("mdiy:configData:update")
    public ResultData update(HttpServletResponse response, HttpServletRequest request) {
        Map<String, Object> map = BasicUtil.assemblyRequestMap();
        String modelId = map.get("modelId").toString();
        if (StringUtils.isEmpty(modelId)) {
            return ResultData.build().error(getResString("err.empty",getResString("model.id")));
        }
        ModelEntity modelEntity = modelBiz.getById(modelId);
        if (modelEntity == null) {
            return ResultData.build().error(getResString("err.error", this.getResString("model.id")));
        }
        ConfigEntity configEntity = new ConfigEntity();
        configEntity.setConfigName(modelEntity.getModelName());
        configEntity =  configBiz.getOne(new QueryWrapper<>(configEntity));
        if (configEntity == null) {
            return ResultData.build().error(getResString("err.empty", this.getResString("config.name")));
        }
        configEntity.setConfigData(JSONObject.toJSONString(map));
        configBiz.updateById(configEntity);
        return ResultData.build().success(configEntity);
    }

}
