/**
 * The MIT License (MIT)
 * Copyright (c) 2021 铭软科技(mingsoft.net)
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

package net.mingsoft.basic.action;

import cn.hutool.crypto.SecureUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.mingsoft.base.entity.ResultData;
import net.mingsoft.basic.annotation.LogAnn;
import net.mingsoft.basic.bean.EUListBean;
import net.mingsoft.basic.biz.IManagerBiz;
import net.mingsoft.basic.constant.e.BusinessTypeEnum;
import net.mingsoft.basic.constant.e.SessionConstEnum;
import net.mingsoft.basic.entity.ManagerEntity;
import net.mingsoft.basic.entity.ManagerSessionEntity;
import net.mingsoft.basic.util.BasicUtil;
import net.mingsoft.basic.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * 管理员管理控制层
 * @author 铭飞开发团队
 * @version
 * 版本号：1.0<br/>
 * 创建日期：2017-8-24 23:40:55<br/>
 * 历史修订：<br/>
 */
@Api(value = "管理员管理接口")
@Controller
@RequestMapping("/${ms.manager.path}/basic/manager")
public class ManagerAction extends net.mingsoft.basic.action.BaseAction{

    /**
     * 注入管理员业务层
     */
    @Autowired
    private IManagerBiz managerBiz;

    /**
     * 返回主界面index
     */
    @GetMapping("/index")
    public String index(HttpServletResponse response,HttpServletRequest request){
        return "/basic/manager/index";
    }


    @ApiOperation(value = "查询管理员列表")
    @GetMapping("/list")
    @ResponseBody
    public ResultData list(@ModelAttribute @ApiIgnore ManagerEntity manager,HttpServletResponse response, HttpServletRequest request,@ApiIgnore ModelMap model) {
        ManagerSessionEntity managerSession = getManagerBySession();
        BasicUtil.startPage();
        List managerList = managerBiz.queryAllChildManager(Integer.parseInt(managerSession.getId()));
        return ResultData.build().success(new EUListBean(managerList,(int)BasicUtil.endPage(managerList).getTotal()));
    }

    @ApiOperation(value = "查询管理员列表,去掉当前管理员id，确保不能删除和修改自己")
    @GetMapping("/query")
    @ResponseBody
    public ResultData query(HttpServletResponse response, HttpServletRequest request, @ApiIgnore ModelMap model) {
        ManagerSessionEntity managerSession = getManagerBySession();
        BasicUtil.startPage();
        List<ManagerEntity> managerList = managerBiz.queryAllChildManager(Integer.parseInt(managerSession.getId()));
        for(ManagerEntity _manager : managerList){
            if(_manager.getId().equals(managerSession.getId())){
                _manager.setId("0");
            }
        }
        return ResultData.build().success(new EUListBean(managerList,(int)BasicUtil.endPage(managerList).getTotal()));
    }

    @ApiOperation(value="获取管理员接口")
    @GetMapping("/get")
    @ResponseBody
    public ResultData get(@ModelAttribute @ApiIgnore ManagerEntity manager,HttpServletResponse response, HttpServletRequest request,@ApiIgnore ModelMap model){
        ManagerEntity managerEntity = new ManagerEntity();
        //判断是否传managerId
        if(StringUtils.isNotEmpty(manager.getId())){
            managerEntity = (ManagerEntity)managerBiz.getEntity(Integer.parseInt(manager.getId()));
        }else{
            ManagerEntity managerSession = (ManagerEntity) BasicUtil.getSession(SessionConstEnum.MANAGER_SESSION);
            managerEntity = (ManagerEntity)managerBiz.getEntity(Integer.parseInt(managerSession.getId()));
        }
        managerEntity.setManagerPassword("");
        return ResultData.build().success(managerEntity);
    }


    @ApiOperation(value = "保存管理员实体")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "managerName", value = "帐号", required = true,paramType="query"),
            @ApiImplicitParam(name = "managerNickName", value = "昵称", required = true,paramType="query"),
            @ApiImplicitParam(name = "managerPassword", value = "密码", required = true,paramType="query"),
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = false,paramType="query"),
            @ApiImplicitParam(name = "peopleId", value = "用户ID", required = false,paramType="query"),
    })
    @LogAnn(title = "保存管理员实体",businessType= BusinessTypeEnum.INSERT)
    @PostMapping("/save")
    @ResponseBody
    @RequiresPermissions("manager:save")
    public ResultData save(@ModelAttribute @ApiIgnore ManagerEntity manager, HttpServletResponse response, HttpServletRequest request) {
        //用户名是否存在
        if(managerBiz.getManagerByManagerName(manager.getManagerName())!= null){
            return ResultData.build().error(getResString("err.exist", this.getResString("manager.name")));
        }
        //验证管理员用户名的值是否合法
        if(StringUtil.isBlank(manager.getManagerName())){
            return ResultData.build().error(getResString("err.empty", this.getResString("manager.name")));
        }
        if(!StringUtil.checkLength(manager.getManagerName()+"", 1, 15)){
            return ResultData.build().error(getResString("err.length", this.getResString("manager.name"), "1", "15"));
        }
        //验证管理员昵称的值是否合法
        if(StringUtil.isBlank(manager.getManagerNickName())){
            return ResultData.build().error(getResString("err.empty", this.getResString("manager.nickname")));
        }
        if(!StringUtil.checkLength(manager.getManagerNickName()+"", 1, 15)){
            return ResultData.build().error(getResString("err.length", this.getResString("manager.nickname"), "1", "15"));
        }
        //验证管理员密码的值是否合法
        if(StringUtil.isBlank(manager.getManagerPassword())){
            return ResultData.build().error(getResString("err.empty", this.getResString("manager.password")));
        }
        if(!StringUtil.checkLength(manager.getManagerPassword()+"", 1, 45)){
            return ResultData.build().error(getResString("err.length", this.getResString("manager.password"), "1", "45"));
        }
        manager.setManagerPassword(SecureUtil.md5(manager.getManagerPassword()));
        managerBiz.saveEntity(manager);
        return ResultData.build().success(manager);
    }


    @ApiOperation(value = "批量删除管理员")
    @LogAnn(title = "批量删除管理员",businessType= BusinessTypeEnum.DELETE)
    @PostMapping("/delete")
    @ResponseBody
    @RequiresPermissions("manager:del")
    public ResultData delete(@RequestBody List<ManagerEntity> managers,HttpServletResponse response, HttpServletRequest request) {
        int[] ids = new int[managers.size()];
        for(int i = 0;i<managers.size();i++){
            ids[i] = Integer.parseInt(managers.get(i).getId());
        }
        managerBiz.delete(ids);
        return ResultData.build().success();
    }

    @ApiOperation(value = "更新管理员信息管理员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "managerName", value = "帐号", required = true,paramType="query"),
            @ApiImplicitParam(name = "managerNickName", value = "昵称", required = true,paramType="query"),
            @ApiImplicitParam(name = "managerPassword", value = "密码", required = true,paramType="query"),
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = false,paramType="query"),
            @ApiImplicitParam(name = "peopleId", value = "用户ID", required = false,paramType="query"),
    })
    @LogAnn(title = "更新管理员信息管理员",businessType= BusinessTypeEnum.UPDATE)
    @PostMapping("/update")
    @ResponseBody
    @RequiresPermissions("manager:update")
    public ResultData update(@ModelAttribute @ApiIgnore ManagerEntity manager) {

        ManagerEntity _manager = managerBiz.getManagerByManagerName(manager.getManagerName());
        //用户名是否存在
        if(_manager != null){
            if(!_manager.getId().equals(manager.getId())){
                return ResultData.build().error(getResString("err.exist", this.getResString("manager.name")));
            }
        }
        //验证管理员用户名的值是否合法
        if(StringUtil.isBlank(manager.getManagerName())){
            return ResultData.build().error(getResString("err.empty", this.getResString("manager.name")));
        }
        if(!StringUtil.checkLength(manager.getManagerName()+"", 1, 15)){
            return ResultData.build().error(getResString("err.length", this.getResString("manager.name"), "1", "15"));
        }
        //验证管理员昵称的值是否合法
        if(StringUtil.isBlank(manager.getManagerNickName())){
            return ResultData.build().error(getResString("err.empty", this.getResString("manager.nickname")));
        }
        if(!StringUtil.checkLength(manager.getManagerNickName()+"", 1, 15)){
            return ResultData.build().error(getResString("err.length", this.getResString("manager.nickname"), "1", "15"));
        }
        //验证管理员密码的值是否合法
        if(!StringUtil.isBlank(manager.getManagerPassword())){
            if(!StringUtil.checkLength(manager.getManagerPassword()+"", 1, 45)){
                return ResultData.build().error(getResString("err.length", this.getResString("manager.password"), "1", "45"));
            }
            manager.setManagerPassword(SecureUtil.md5(manager.getManagerPassword()));
        }
        managerBiz.updateEntity(manager);
        return ResultData.build().success(manager);
    }

}
