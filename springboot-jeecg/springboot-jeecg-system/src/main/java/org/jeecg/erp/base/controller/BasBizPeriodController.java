package org.jeecg.erp.base.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.erp.base.entity.BasBizPeriod;
import org.jeecg.erp.base.service.IBasBizPeriodService;

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
 * @Description: bas_biz_period
 * @Author: jeecg-boot
 * @Date: 2020-05-25
 * @Version: V1.0
 */
@RestController
@RequestMapping(value = "/bas/basBizPeriod")
@Api(value = "", tags = "基本商业时期处理控制器")
@Slf4j
public class BasBizPeriodController extends JeecgController<BasBizPeriod, IBasBizPeriodService> {

    @Autowired
    private IBasBizPeriodService basBizPeriodService;

    /**
     * 分页列表查询
     *
     * @param basBizPeriod
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "bas_biz_period-分页列表查询")
    @ApiOperation(value = "bas_biz_period-分页列表查询", notes = "bas_biz_period-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(
            BasBizPeriod basBizPeriod,
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            HttpServletRequest req) {
        QueryWrapper<BasBizPeriod> queryWrapper = QueryGenerator.initQueryWrapper(basBizPeriod, req.getParameterMap());
        Page<BasBizPeriod> page = new Page<BasBizPeriod>(pageNo, pageSize);
        IPage<BasBizPeriod> pageList = basBizPeriodService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param basBizPeriod
     * @return
     */
    @AutoLog(value = "bas_biz_period-添加")
    @ApiOperation(value = "bas_biz_period-添加", notes = "bas_biz_period-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody BasBizPeriod basBizPeriod) {
        basBizPeriodService.save(basBizPeriod);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param basBizPeriod
     * @return
     */
    @AutoLog(value = "bas_biz_period-编辑")
    @ApiOperation(value = "bas_biz_period-编辑", notes = "bas_biz_period-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody BasBizPeriod basBizPeriod) {
        basBizPeriodService.updateById(basBizPeriod);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "bas_biz_period-通过id删除")
    @ApiOperation(value = "bas_biz_period-通过id删除", notes = "bas_biz_period-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        basBizPeriodService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "bas_biz_period-批量删除")
    @ApiOperation(value = "bas_biz_period-批量删除", notes = "bas_biz_period-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.basBizPeriodService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "bas_biz_period-通过id查询")
    @ApiOperation(value = "bas_biz_period-通过id查询", notes = "bas_biz_period-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        BasBizPeriod basBizPeriod = basBizPeriodService.getById(id);
        if (basBizPeriod == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(basBizPeriod);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param basBizPeriod
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BasBizPeriod basBizPeriod) {
        return super.exportXls(request, basBizPeriod, BasBizPeriod.class, "bas_biz_period");
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
        return super.importExcel(request, response, BasBizPeriod.class);
    }

    @AutoLog(value = "月末结转")
    @ApiOperation(value = "月末结转", notes = "月末结转")
    @PostMapping(value = "/monthCarryForward")
    public Result<?> monthCarryForward(@RequestBody BasBizPeriod basBizPeriod) {
        basBizPeriodService.monthCarryForward(basBizPeriod.getYear(), basBizPeriod.getMonth());
        return Result.ok("月末结转成功！");
    }

}
