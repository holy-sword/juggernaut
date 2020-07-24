package com.lzx.service.user.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lzx.common.Result;
import com.lzx.service.user.entity.Menu;
import com.lzx.service.user.entity.MenuPermission;
import com.lzx.service.user.entity.Permission;
import com.lzx.service.user.entity.dto.MenuTree;
import com.lzx.service.user.service.MenuPermissionService;
import com.lzx.service.user.service.MenuService;
import com.lzx.service.user.service.PermissionService;
import com.lzx.web.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author lzx
 * @since 2020/6/3
 */
@RestController
@RequestMapping("api/admin/user/menu")
public class AdminMenuController extends AbstractController<MenuService, Menu, Long> {

    @Autowired
    private PermissionService permissionService;
    @Autowired
    private MenuPermissionService menuPermissionService;

    /**
     * 获取菜单的权限信息
     *
     * @param menuId 菜单ID
     */
    @GetMapping("{menuId}/permission")
    public Result<?> getPermissionByMenu(@PathVariable String menuId) {
        //获取用户拥有的权限ID集合
        Set<Long> permissionIds = menuPermissionService.list(new LambdaQueryWrapper<MenuPermission>().eq(MenuPermission::getMenuId, menuId))
                .stream()
                .map(MenuPermission::getPermissionId).collect(Collectors.toSet());
        //查询出所有权限
        List<Permission> permissionList = permissionService.list();
        //过滤掉菜单没有的权限
        List<Permission> permissions = permissionList.stream().filter(permission -> permissionIds.contains(permission.getId())).collect(Collectors.toList());
        return Result.ok(permissions);
    }


    /**
     * 保存菜单的权限信息
     *
     * @param menuId 菜单ID
     * @param ids    权限ID集合
     */
    @PostMapping("{menuId}/permission")
    public Result<?> savePermission(@PathVariable Long menuId, @RequestBody Long[] ids) {
        service.saveMenuPermissionAndDeleteOld(menuId, Arrays.asList(ids));
        return Result.ok();
    }

    /**
     * 获取菜单树形结构
     *
     * @param rootId 根节点ID
     */
    @GetMapping("tree")
    public Result<?> tree(@RequestParam(defaultValue = "0") Long rootId) {
        List<MenuTree> menuTreeList = service.list(new LambdaQueryWrapper<Menu>().orderByAsc(Menu::getSort))
                .stream()
                .map(MenuTree::new)
                .collect(Collectors.toList());
        return Result.ok(MenuTree.buildTree(menuTreeList, rootId));
    }


}
