package com.lzx.service.user.entity.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 树形节点
 *
 * @author lzx
 * @since 2020/6/3
 */
@Data
public class TreeNode {
    /**
     * 当前节点ID
     */
    protected long id;
    /**
     * 上级节点ID
     */
    protected long parentId;
    /**
     * 子节点列表
     */
    protected List<TreeNode> children = new ArrayList<>();

    public void add(TreeNode node) {
        children.add(node);
    }
}
