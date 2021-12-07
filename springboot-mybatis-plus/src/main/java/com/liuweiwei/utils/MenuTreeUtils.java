package com.liuweiwei.utils;

import com.liuweiwei.vo.TbMenuTreeVo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @since 2021-12-07
 */
public class MenuTreeUtils {
    private List<TbMenuTreeVo> menuList = new ArrayList<TbMenuTreeVo>();

    public MenuTreeUtils(List<TbMenuTreeVo> menuList) {
        this.menuList = menuList;
    }

    //建立树形结构
    public List<TbMenuTreeVo> builTree() {
        List<TbMenuTreeVo> treeMenus = new ArrayList<TbMenuTreeVo>();
        for (TbMenuTreeVo menuNode : getRootNode()) {
            menuNode = buildChilTree(menuNode);
            treeMenus.add(menuNode);
        }
        return treeMenus;
    }

    //递归，建立子树形结构
    private TbMenuTreeVo buildChilTree(TbMenuTreeVo pNode) {
        List<TbMenuTreeVo> chilMenus = new ArrayList<TbMenuTreeVo>();
        for (TbMenuTreeVo menuNode : menuList) {
            if (menuNode.getParentId().equals(pNode.getId())) {
                chilMenus.add(buildChilTree(menuNode));
            }
        }
        pNode.setChildren(chilMenus);
        return pNode;
    }

    //获取根节点
    private List<TbMenuTreeVo> getRootNode() {
        List<TbMenuTreeVo> rootMenuLists = new ArrayList<TbMenuTreeVo>();
        for (TbMenuTreeVo menuNode : menuList) {
            if ("-1".equals(menuNode.getParentId())) {
                rootMenuLists.add(menuNode);
            }
        }
        return rootMenuLists;
    }
}
