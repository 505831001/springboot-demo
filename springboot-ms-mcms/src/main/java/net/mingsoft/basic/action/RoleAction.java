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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.mingsoft.base.entity.BaseEntity;
import net.mingsoft.base.entity.ResultData;
import net.mingsoft.basic.annotation.LogAnn;
import net.mingsoft.basic.bean.EUListBean;
import net.mingsoft.basic.bean.RoleBean;
import net.mingsoft.basic.biz.IModelBiz;
import net.mingsoft.basic.biz.IRoleBiz;
import net.mingsoft.basic.biz.IRoleModelBiz;
import net.mingsoft.basic.constant.e.BusinessTypeEnum;
import net.mingsoft.basic.entity.ManagerSessionEntity;
import net.mingsoft.basic.entity.RoleEntity;
import net.mingsoft.basic.entity.RoleModelEntity;
import net.mingsoft.basic.util.BasicUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色管理控制层
 * @author 铭飞开发团队
 * @version
 * 版本号：1.0<br/>
 * 创建日期：2017-8-24 23:40:55<br/>
 * 历史修订：<br/>
 */
@Api("角色管理控制层")
@Controller
@RequestMapping("/${ms.manager.path}/basic/role")
public class RoleAction extends net.mingsoft.basic.action.BaseAction{

    /**
     * 注入角色业务层
     */
    @Autowired
    private IRoleBiz roleBiz;
    /**
     * 模块业务层
     */
    @Autowired
    private IModelBiz modelBiz;
    /**
     * 角色模块关联业务层
     */
    @Autowired
    private IRoleModelBiz roleModelBiz;

    /**
     * 返回主界面index
     */
    @ApiOperation(value = "返回主界面index")
    @GetMapping("/index")
    public String index(HttpServletResponse response,HttpServletRequest request){
        return "/basic/role/index";
    }


    @ApiOperation(value = "查询角色列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleName", value = "角色名称", required = false,paramType="query"),
            @ApiImplicitParam(name = "managerId", value = "该角色的创建者ID", required = false,paramType="query"),
    })
    @GetMapping("/list")
    @ResponseBody
    public ResultData list(@ModelAttribute @ApiIgnore RoleEntity role,HttpServletResponse response, HttpServletRequest request,@ApiIgnore ModelMap model) {
        ManagerSessionEntity managerSession = getManagerBySession();
        role.setManagerId(Integer.parseInt(managerSession.getId()));
        BasicUtil.startPage();
        List roleList = roleBiz.query(role);
        return ResultData.build().success(new EUListBean(roleList,(int)BasicUtil.endPage(roleList).getTotal()));
    }

    @ApiOperation(value = "根据角色ID查询模块集合")
    @ApiImplicitParam(name = "roleId", value = "角色ID", required = true,paramType="path")
    @GetMapping("/{roleId}/queryByRole")
    @ResponseBody
    public ResultData queryByRole(@PathVariable @ApiIgnore int roleId, HttpServletResponse response){
        List models = modelBiz.queryModelByRoleId(roleId);
        return ResultData.build().success(models);
    }
    /**
     * 返回编辑界面role_form
     */
    @ApiOperation(value = "返回编辑界面role_form")
    @ApiImplicitParam(name = "id", value = "角色ID", required = true,paramType="query")
    @GetMapping("/form")
    public String form(@ModelAttribute @ApiIgnore RoleEntity role,HttpServletResponse response,HttpServletRequest request,@ApiIgnore ModelMap model){
        if(StringUtils.isNotEmpty(role.getId())){
            BaseEntity roleEntity = roleBiz.getEntity(Integer.parseInt(role.getId()));
            model.addAttribute("roleEntity",roleEntity);
        }
        return "/basic/role/form";
    }

    @ApiOperation(value = "查询所有角色列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleName", value = "角色名称", required = false,paramType="query"),
            @ApiImplicitParam(name = "managerId", value = "该角色的创建者ID", required = false,paramType="query"),
    })
    @GetMapping("/all")
    @ResponseBody
    public ResultData all(@ModelAttribute @ApiIgnore RoleEntity role,HttpServletResponse response, HttpServletRequest request,@ApiIgnore ModelMap model) {
        BasicUtil.startPage();
        List roleList = roleBiz.query(role);
        return ResultData.build().success(new EUListBean(roleList,(int)BasicUtil.endPage(roleList).getTotal()));
    }

    @ApiOperation(value = "获取角色")
    @ApiImplicitParam(name = "id", value = "角色ID", required = true,paramType="query")
    @GetMapping("/get")
    @ResponseBody
    public ResultData get(@ModelAttribute @ApiIgnore RoleEntity role,HttpServletResponse response, HttpServletRequest request,@ApiIgnore ModelMap model){
        if(StringUtils.isEmpty(role.getId())) {
            return ResultData.build().error(getResString("err.error", this.getResString("role.id")));
        }
        RoleEntity _role = (RoleEntity)roleBiz.getEntity(Integer.parseInt(role.getId()));
        return ResultData.build().success(_role);
    }


    @ApiOperation(value = "保存角色实体")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleName", value = "角色名称", required = false,paramType="query"),
            @ApiImplicitParam(name = "managerId", value = "该角色的创建者ID", required = false,paramType="query"),
    })
    @LogAnn(title = "保存角色实体",businessType= BusinessTypeEnum.UPDATE)
    @PostMapping("/saveOrUpdateRole")
    @ResponseBody
    @RequiresPermissions("role:save")
    public ResultData saveOrUpdateRole(@ModelAttribute @ApiIgnore RoleBean role, HttpServletResponse response, HttpServletRequest request) {
        //组织角色属性，并对角色进行保存
        RoleBean _role = new RoleBean();
        _role.setRoleName(role.getRoleName());
        //获取管理员id
        ManagerSessionEntity managerSession = getManagerBySession();
        role.setManagerId(Integer.parseInt(managerSession.getId()));
        if(StringUtils.isEmpty(role.getRoleName())){
            return ResultData.build().error(getResString("err.empty", this.getResString("rolrName")));
        }
        RoleBean roleBean = (RoleBean) roleBiz.getEntity(_role);
        //通过角色id判断是保存还是修改
        if(StringUtils.isNotEmpty(role.getId())){
            //若为更新角色，数据库中存在该角色名称且当前名称不为更改前的名称，则属于重名
            if(roleBean != null && !roleBean.getId().equals(role.getId())){
                return ResultData.build().error(getResString("roleName.exist"));
            }
            roleBiz.updateEntity(role);
        }else{
            //判断角色名是否重复
            if(roleBean != null){
                return ResultData.build().error(getResString("roleName.exist"));
            }
            //获取管理员id
            roleBiz.save(role);
            roleBiz.updateCache();
        }
        //开始保存相应的关联数据。组织角色模块的列表。
        List<RoleModelEntity> roleModelList = new ArrayList<>();
        if(!StringUtils.isEmpty(role.getIds())){
            for(String id : role.getIds().split(",")){
                RoleModelEntity roleModel = new RoleModelEntity();
                roleModel.setRoleId(Integer.parseInt(role.getId()));
                roleModel.setModelId(Integer.parseInt(id));
                roleModelList.add(roleModel);
            }
            //先删除当前的角色关联菜单，然后重新添加。
            roleModelBiz.deleteEntity(Integer.parseInt(role.getId()));
            modelBiz.updateCache();

            //加上数量参数用于区分IBaseBiz的重名方法
            roleModelBiz.saveBatch(roleModelList, roleModelList.size());
        }else{
            roleModelBiz.deleteEntity(Integer.parseInt(role.getId()));
        }

        return ResultData.build().success(role);
    }


    @ApiOperation(value = "批量删除角色")
    @PostMapping("/delete")
    @ResponseBody
    @RequiresPermissions("role:del")
    @LogAnn(title = "删除角色", businessType = BusinessTypeEnum.DELETE)
    public ResultData delete(@RequestBody List<RoleEntity> roles,HttpServletResponse response, HttpServletRequest request) {
        int[] ids = new int[roles.size()];
        ManagerSessionEntity managerSession = this.getManagerBySession();
        int currentRoleId = managerSession.getRoleId();
        for(int i = 0;i<roles.size();i++){
            if(currentRoleId == Integer.parseInt(roles.get(i).getId())){
                //当前角色不能删除
                continue ;
            }
            ids[i] = Integer.parseInt(roles.get(i).getId());
        }
        roleBiz.delete(ids);
        return ResultData.build().success();
    }
}
