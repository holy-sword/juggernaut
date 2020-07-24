package com.lzx.service.user.entity.dto;

import com.lzx.service.user.entity.Menu;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 菜单树
 *
 * @author lzx
 * @since 2020/6/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MenuTree extends TreeNode {
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 菜单名称
     */
    private String name;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 图标
     */
    private String icon;
    /**
     * 路径
     */
    private String path;
    /**
     * 缓存
     */
    private String keepAlive;
    /**
     * 菜单类型
     */
    private String type;
    /**
     * 权限字段
     */
    private String permission;

    public MenuTree() {
    }

    public MenuTree(Menu menu) {
        this.id = menu.getId();
        this.parentId = menu.getParentId();
        this.icon = menu.getIcon();
        this.name = menu.getName();
        this.path = menu.getPath();
        this.type = menu.getType();
        this.projectName = menu.getProjectName();
        this.sort = menu.getSort();
        this.keepAlive = menu.getKeepAlive();
        this.permission = menu.getPermission();
    }


    /**
     * 构造树形结构列表
     *
     * @param menuTreeList 菜单数据列表
     * @param rootId       构建的根节点ID
     */
    public static List<MenuTree> buildTree(List<MenuTree> menuTreeList, long rootId) {
        List<MenuTree> trees = new ArrayList<>();
        for (MenuTree menuTree : menuTreeList) {

            if (Objects.equals(menuTree.getParentId(), rootId)) {
                trees.add(menuTree);
            }
            // 拼接当前节点下的所有子节点
            for (MenuTree menu : menuTreeList) {
                if (Objects.equals(menu.getParentId(), menuTree.getId())) {
                    menuTree.add(menu);
                }
            }
        }
        return trees;
    }
}
