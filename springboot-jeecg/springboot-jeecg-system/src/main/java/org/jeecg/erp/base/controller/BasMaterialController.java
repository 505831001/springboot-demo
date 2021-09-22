package org.jeecg.erp.base.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.erp.base.entity.BasMaterial;
import org.jeecg.erp.base.service.IBasMaterialService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.annotation.AutoLog;

/**
 * @Description: 物料
 * @Author: jeecg-boot
 * @Date: 2020-05-29
 * @Version: V1.0
 */
@RestController
@RequestMapping(value = "/bas/basMaterial")
@Api(value = "", tags = "物料处理控制器")
@Slf4j
public class BasMaterialController extends JeecgController<BasMaterial, IBasMaterialService> {

    @Autowired
    private IBasMaterialService basMaterialService;

    /**
     * 分页列表查询
     *
     * @param basMaterial
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "物料-分页列表查询")
    @ApiOperation(value = "物料-分页列表查询", notes = "物料-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(
            BasMaterial basMaterial,
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            HttpServletRequest req) {
        QueryWrapper<BasMaterial> queryWrapper = QueryGenerator.initQueryWrapper(basMaterial, req.getParameterMap());
        Page<BasMaterial> page = new Page<BasMaterial>(pageNo, pageSize);
        IPage<BasMaterial> pageList = basMaterialService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param basMaterial
     * @return
     */
    @AutoLog(value = "物料-添加")
    @ApiOperation(value = "物料-添加", notes = "物料-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody BasMaterial basMaterial) {
        basMaterialService.save(basMaterial);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param basMaterial
     * @return
     */
    @AutoLog(value = "物料-编辑")
    @ApiOperation(value = "物料-编辑", notes = "物料-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody BasMaterial basMaterial) {
        basMaterialService.updateById(basMaterial);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "物料-通过id删除")
    @ApiOperation(value = "物料-通过id删除", notes = "物料-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        basMaterialService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "物料-批量删除")
    @ApiOperation(value = "物料-批量删除", notes = "物料-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.basMaterialService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "物料-通过id查询")
    @ApiOperation(value = "物料-通过id查询", notes = "物料-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        BasMaterial basMaterial = basMaterialService.getById(id);
        if (basMaterial == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(basMaterial);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param basMaterial
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BasMaterial basMaterial) {
        return super.exportXls(request, basMaterial, BasMaterial.class, "物料");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, BasMaterial.class);
    }

}
