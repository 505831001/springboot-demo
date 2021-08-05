package com.excel.poi.utils;

import com.excel.poi.entity.TbMenu;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Liuweiwei
 * @since 2021-08-05
 */
@Log4j2
public class MenuTreeUtils {
    private List<TbMenu> dbList = new ArrayList<>();

    public MenuTreeUtils(List<TbMenu> dbList) {
        this.dbList = dbList;
    }

    /**
     * 建立树形结构
     * @return
     */
    public List<TbMenu> buildMenuTree() {
        List<TbMenu> treeList = new ArrayList<>();
        for (TbMenu rootNode : getMenuTreeRootNode()) {
            rootNode = getMenuTreeChildNode(rootNode);
            treeList.add(rootNode);
        }
        return treeList;
    }

    /**
     * 获取根节点
     * @return
     */
    private List<TbMenu> getMenuTreeRootNode() {
        List<TbMenu> rootNodeList = new ArrayList<>();
        for (int i = 0; i < dbList.size(); i++) {
            TbMenu rootNode = dbList.get(i);
            if (rootNode.getParentId().equalsIgnoreCase("0")) {
                rootNodeList.add(rootNode);
                log.info("根节点[0-" + i + "]" + rootNode.toString());
            }
        }
        return rootNodeList;
    }

    /**
     * 递归，建立子树形结构
     * @param nextNode
     * @return
     */
    private TbMenu getMenuTreeChildNode(TbMenu nextNode) {
        List<TbMenu> childNodeList = new ArrayList<>(10);
        for (int i = 0; i < dbList.size(); i++) {
            TbMenu childNode = dbList.get(i);
            if (childNode.getParentId().equals(nextNode.getId())) {
                childNodeList.add(getMenuTreeChildNode(childNode));
                log.info("下一个节点[0-" + i + "]" + childNode.toString());
            }
        }
        nextNode.setChildren(childNodeList);
        return nextNode;
    }

    public List<TbMenu> buildTree() {
        List<TbMenu> treeList = new ArrayList<>();
        for (TbMenu rootNode : getRootNode()) {
            rootNode = getChildNode(rootNode);
            treeList.add(rootNode);
        }
        return treeList;
    }

    private List<TbMenu> getRootNode() {
        List<TbMenu> rootNodeList = new ArrayList<>();
        for (TbMenu rootNode : dbList) {
            if (rootNode.getParentId().equalsIgnoreCase("0")) {
                rootNodeList.add(rootNode);
            }
        }
        return rootNodeList;
    }

    private TbMenu getChildNode(TbMenu nextNode) {
        List<TbMenu> childNodeList = new ArrayList<>();
        for (TbMenu childNode : dbList) {
            if (childNode.getParentId().equals(nextNode.getId())) {
                childNodeList.add(getMenuTreeChildNode(childNode));
            }
        }
        nextNode.setChildren(childNodeList);
        return nextNode;
    }
}
