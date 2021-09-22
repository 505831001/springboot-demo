package org.jeecg.erp.base.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.erp.base.entity.BasSupplier;
import org.jeecg.erp.base.service.IBasSupplierService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.vo.Result;
import org.jeecg.common.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 供应商
 * @Author: jeecg-boot
 * @Date: 2020-04-01
 * @Version: V1.0
 */
@RestController
@RequestMapping(value = "/bas/basSupplier")
@Api(value = "", tags = "供应商处理控制器")
@Slf4j
public class BasSupplierController extends JeecgController<BasSupplier, IBasSupplierService> {

    @Autowired
    private IBasSupplierService basSupplierService;

    /**
     * 分页列表查询
     *
     * @param basSupplier
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "供应商-分页列表查询")
    @ApiOperation(value = "供应商-分页列表查询", notes = "供应商-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(
            BasSupplier basSupplier,
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            HttpServletRequest req) {
        QueryWrapper<BasSupplier> queryWrapper = QueryGenerator.initQueryWrapper(basSupplier, req.getParameterMap());
        Page<BasSupplier> page = new Page<BasSupplier>(pageNo, pageSize);
        IPage<BasSupplier> pageList = basSupplierService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param basSupplier
     * @return
     */
    @AutoLog(value = "供应商-添加")
    @ApiOperation(value = "供应商-添加", notes = "供应商-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody BasSupplier basSupplier) {
        basSupplierService.save(basSupplier);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param basSupplier
     * @return
     */
    @AutoLog(value = "供应商-编辑")
    @ApiOperation(value = "供应商-编辑", notes = "供应商-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody BasSupplier basSupplier) {
        basSupplierService.updateById(basSupplier);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "供应商-通过id删除")
    @ApiOperation(value = "供应商-通过id删除", notes = "供应商-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        basSupplierService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "供应商-批量删除")
    @ApiOperation(value = "供应商-批量删除", notes = "供应商-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.basSupplierService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "供应商-通过id查询")
    @ApiOperation(value = "供应商-通过id查询", notes = "供应商-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        BasSupplier basSupplier = basSupplierService.getById(id);
        if (basSupplier == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(basSupplier);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param basSupplier
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BasSupplier basSupplier) {
        return super.exportXls(request, basSupplier, BasSupplier.class, "供应商");
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
        return super.importExcel(request, response, BasSupplier.class);
    }

}
