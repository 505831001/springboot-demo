package com.liuweiwei.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liuweiwei.common.constants.MenuTreeConstant;
import com.liuweiwei.dao.TbMenuTreeMapper;
import com.liuweiwei.model.TbMenuTree;
import com.liuweiwei.service.TbMenuTreeService;
import com.liuweiwei.utils.MenuTreeUtils;
import com.liuweiwei.utils.ResultData;
import com.liuweiwei.vo.TbMenuTreeVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author Liuweiwei
 * @since 2021-01-06
 */
@Service
@Slf4j
public class TbMenuTreeServiceImpl extends ServiceImpl<TbMenuTreeMapper, TbMenuTree> implements TbMenuTreeService {

    @Autowired
    private TbMenuTreeMapper menuTreeMapper;

    /**
     * 1.主键ID(MyBatis-Plus默认主键策略:ASSIGN_ID使用了雪花算法(19位),ASSIGN_UUID(32位))
     * 2.String globalId = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
     * 3.entity.setId(globalId);
     */
    @Override
    public ResultData insertOther(TbMenuTree menuTree) {
        TbMenuTree entity = new TbMenuTree();
        switch (menuTree.getPermission()) {
            case MenuTreeConstant.FIRST_LEVEL:
                entity.setParentId("-1");
                entity.setPermission(MenuTreeConstant.FIRST_LEVEL);
                break;
            case MenuTreeConstant.SECOND_LEVEL:
                entity.setParentId(menuTree.getId());
                entity.setPermission(MenuTreeConstant.SECOND_LEVEL);
                break;
            case MenuTreeConstant.THIRD_LEVEL:
                entity.setParentId(menuTree.getId());
                entity.setPermission(MenuTreeConstant.THIRD_LEVEL);
                break;
            case MenuTreeConstant.FOURTH_LEVEL:
                entity.setParentId(menuTree.getId());
                entity.setPermission(MenuTreeConstant.FOURTH_LEVEL);
                break;
            default:
                break;
        }
        entity.setMenuName(menuTree.getMenuName());
        entity.setRole("ADMIN");
        entity.setCreateTime(new Date());
        entity.setCreateName(menuTree.getCreateName());
        entity.setUpdateTime(new Date());
        entity.setUpdateName(menuTree.getUpdateName());
        int index = menuTreeMapper.insert(entity);
        if (index > 0) {
            ResultData.success(index);
        }
        return ResultData.success();
    }

    @Override
    public ResultData deleteOther(String id) {
        int index = menuTreeMapper.deleteById(id);
        if (index > 0) {
            return ResultData.success(index);
        }
        return ResultData.success();
    }

    @Override
    public ResultData updateOther(TbMenuTree menuTree) {
        int index = menuTreeMapper.updateById(menuTree);
        if (index > 0) {
            return ResultData.success(index);
        }
        return ResultData.success();
    }

    @Override
    public ResultData selectOther(String id) {
        TbMenuTree object = menuTreeMapper.selectById(id);
        if (Objects.nonNull(object)) {
            return ResultData.success(object);
        }
        return ResultData.success();
    }

    @Override
    public ResultData selectPathOther(String id) {
        TbMenuTree object = menuTreeMapper.selectById(id);
        if (Objects.nonNull(object)) {
            return ResultData.success(object);
        }
        return ResultData.success();
    }

    @Override
    public ResultData selectPageOther(String current, String size) {
        Page<TbMenuTree> page = menuTreeMapper.selectPage(new Page<>(Long.parseLong(current), Long.parseLong(size)), null);
        if (CollectionUtils.isNotEmpty(page.getRecords())) {
            return ResultData.success(page);
        }
        return ResultData.success();
    }

    @Override
    public ResultData selectMenuTree() {
        List<TbMenuTreeVo> voList = new ArrayList<>();
        List<TbMenuTree>   poList = menuTreeMapper.selectList(new QueryWrapper<TbMenuTree>().lambda().eq(TbMenuTree::getRole, "ADMIN"));
        if (CollectionUtils.isNotEmpty(poList)) {
            poList.forEach(source -> {
                TbMenuTreeVo target = new TbMenuTreeVo();
                BeanUtils.copyProperties(source, target);
                voList.add(target);
            });
        }
        if (CollectionUtils.isNotEmpty(voList)) {
            List<TbMenuTreeVo> tree = new MenuTreeUtils(voList).builTree();
            if (CollectionUtils.isNotEmpty(tree)) {
                return ResultData.success(tree);
            }
        }
        return ResultData.success();
    }
}
