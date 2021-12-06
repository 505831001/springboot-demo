package com.liuweiwei.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liuweiwei.common.constants.MenuTreeConstant;
import com.liuweiwei.dao.TbMenuTreeMapper;
import com.liuweiwei.model.TbMenuTree;
import com.liuweiwei.service.TbMenuTreeService;
import com.liuweiwei.utils.ResultData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Liuweiwei
 * @since 2021-01-06
 */
@Service
@Slf4j
public class TbMenuTreeServiceImpl extends ServiceImpl<TbMenuTreeMapper, TbMenuTree> implements TbMenuTreeService {

    @Autowired
    private TbMenuTreeMapper menuTreeMapper;

    @Override
    public ResultData insertOther(TbMenuTree menuTree) {
        TbMenuTree entity = new TbMenuTree();
        String globalId = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        entity.setId(globalId);
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
        return null;
    }

    @Override
    public ResultData updateOther(TbMenuTree menuTree) {
        return null;
    }

    @Override
    public ResultData selectOther(String id) {
        return null;
    }

    @Override
    public ResultData selectPathOther(String id) {
        return null;
    }

    @Override
    public ResultData selectPageOther(String current, String size) {
        return null;
    }
}
