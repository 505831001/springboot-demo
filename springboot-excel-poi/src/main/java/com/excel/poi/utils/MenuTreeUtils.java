package com.excel.poi.utils;

import com.excel.poi.vo.TbMenuTreeVO;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Liuweiwei
 * @since 2021-08-05
 */
@Log4j2
public class MenuTreeUtils {
    private List<TbMenuTreeVO> dbList = new ArrayList<>();

    public MenuTreeUtils(List<TbMenuTreeVO> dbList) {
        this.dbList = dbList;
    }

    /**
     * 建立树形结构
     * @return
     */
    public List<TbMenuTreeVO> buildMenuTree() {
        List<TbMenuTreeVO> treeList = new ArrayList<>();
        for (TbMenuTreeVO rootNode : getMenuTreeRootNode()) {
            rootNode = getMenuTreeChildNode(rootNode);
            treeList.add(rootNode);
        }
        return treeList;
    }

    /**
     * 获取根节点
     * @return
     */
    private List<TbMenuTreeVO> getMenuTreeRootNode() {
        List<TbMenuTreeVO> rootNodeList = new ArrayList<>();
        for (int i = 0; i < dbList.size(); i++) {
            TbMenuTreeVO rootNode = dbList.get(i);
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
    private TbMenuTreeVO getMenuTreeChildNode(TbMenuTreeVO nextNode) {
        List<TbMenuTreeVO> childNodeList = new ArrayList<>(10);
        for (int i = 0; i < dbList.size(); i++) {
            TbMenuTreeVO childNode = dbList.get(i);
            if (childNode.getParentId().equals(nextNode.getId())) {
                childNodeList.add(getMenuTreeChildNode(childNode));
                log.info("下一个节点[0-" + i + "]" + childNode.toString());
            }
        }
        nextNode.setChildren(childNodeList);
        return nextNode;
    }

    public List<TbMenuTreeVO> buildTree() {
        List<TbMenuTreeVO> treeList = new ArrayList<>();
        for (TbMenuTreeVO rootNode : getRootNode()) {
            rootNode = getChildNode(rootNode);
            treeList.add(rootNode);
        }
        return treeList;
    }

    private List<TbMenuTreeVO> getRootNode() {
        List<TbMenuTreeVO> rootNodeList = new ArrayList<>();
        for (TbMenuTreeVO rootNode : dbList) {
            if (rootNode.getParentId().equalsIgnoreCase("0")) {
                rootNodeList.add(rootNode);
            }
        }
        return rootNodeList;
    }

    private TbMenuTreeVO getChildNode(TbMenuTreeVO nextNode) {
        List<TbMenuTreeVO> childNodeList = new ArrayList<>();
        for (TbMenuTreeVO childNode : dbList) {
            if (childNode.getParentId().equals(nextNode.getId())) {
                childNodeList.add(getMenuTreeChildNode(childNode));
            }
        }
        nextNode.setChildren(childNodeList);
        return nextNode;
    }
}
