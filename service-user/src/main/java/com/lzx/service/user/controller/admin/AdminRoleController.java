package com.lzx.service.user.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lzx.common.Result;
import com.lzx.service.user.entity.*;
import com.lzx.service.user.entity.dto.MenuTree;
import com.lzx.service.user.service.*;
import com.lzx.web.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author lzx
 * @since 2019/3/15
 */
@RestController
@RequestMapping("api/admin/user/role")
public class AdminRoleController extends AbstractController<RoleService, Role, Long> {

    @Autowired
    private PermissionService permissionService;
    @Autowired
    private DataPermissionService dataPermissionService;
    @Autowired
    private RolePermissionService rolePermissionService;
    @Autowired
    private RoleDataPermissionService roleDataPermissionService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleMenuService roleMenuService;

    /**
     * 获取角色的权限信息
     *
     * @param roleId 角色ID
     * @param all    是否获取所有权限（true：获取所有，包括非角色拥有的权限，其中权限拥有字段have为false   false：只获取角色拥有的权限）
     */
    @RequestMapping(value = "{roleId}/permission", method = RequestMethod.GET)
    public Result<?> getPermissionByRole(@PathVariable String roleId, @RequestParam(defaultValue = "false") Boolean all) {
        List<Permission> permissions;
        //获取用户拥有的权限ID集合
        Set<Long> permissionIds = rolePermissionService.list(new LambdaQueryWrapper<RolePermission>().eq(RolePermission::getRoleId, roleId))
                .stream()
                .map(RolePermission::getPermissionId).collect(Collectors.toSet());
        //查询出所有权限
        List<Permission> permissionList = permissionService.list();
        if (all) {
            //设置have字段值
            permissions = permissionList.stream().peek(permission -> permission.setHave(permissionIds.contains(permission.getId()))).collect(Collectors.toList());
        } else {
            //过滤掉角色没有的权限
            permissions = permissionList.stream().filter(permission -> permissionIds.contains(permission.getId())).collect(Collectors.toList());
        }
        return Result.ok(permissions);
    }


    /**
     * 保存角色的权限信息
     *
     * @param roleId 角色ID
     * @param ids    权限ID集合
     */
    @RequestMapping(value = "{roleId}/permission", method = RequestMethod.POST)
    public Result<?> savePermission(@PathVariable Long roleId, @RequestBody Long[] ids) {
        service.saveRolePermissionAndDeleteOld(roleId, Arrays.asList(ids));
        return Result.ok();
    }


    /**
     * 获取角色的数据权限信息
     *
     * @param roleId 角色ID
     */
    @GetMapping("{roleId}/data/permission")
    public Result<?> getPermissionByRole(@PathVariable String roleId) {
        //获取用户拥有的权限ID集合
        Set<Long> permissionIds = roleDataPermissionService.list(new LambdaQueryWrapper<RoleDataPermission>().eq(RoleDataPermission::getRoleId, roleId))
                .stream()
                .map(RoleDataPermission::getPermissionId).collect(Collectors.toSet());
        //查询出所有权限
        List<DataPermission> permissionList = dataPermissionService.list();
        //过滤掉角色没有的权限
        List<DataPermission> permissions = permissionList.stream().filter(permission -> permissionIds.contains(permission.getId())).collect(Collectors.toList());
        return Result.ok(permissions);
    }

    /**
     * 保存角色的数据权限信息
     *
     * @param roleId 角色ID
     * @param ids    数据权限ID集合
     */
    @PostMapping("{roleId}/data/permission")
    public Result<?> saveDataPermission(@PathVariable Long roleId, @RequestBody Long[] ids) {
        service.saveRoleDataPermissionAndDeleteOld(roleId, Arrays.asList(ids));
        return Result.ok();
    }

    /**
     * 获取角色的菜单信息
     *
     * @param roleId 角色ID
     */
    @GetMapping("{roleId}/menu")
    public Result<List<Menu>> getMenuByRole(@PathVariable String roleId) {
        //获取角色拥有的菜单ID集合
        Set<Long> menuIds = roleMenuService.list(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId, roleId))
                .stream()
                .map(RoleMenu::getMenuId).collect(Collectors.toSet());
        //查询出所有菜单
        List<Menu> menuList = menuService.list();
        //过滤掉菜单没有的权限
        List<Menu> menus = menuList.stream().filter(menu -> menuIds.contains(menu.getId())).collect(Collectors.toList());
        return Result.ok(menus);
    }

    /**
     * 获取角色的菜单信息的树形结构
     *
     * @param roleId 角色ID
     */
    @GetMapping("{roleId}/menu/tree")
    public Result<?> getMenuTreeByRole(@PathVariable String roleId, @RequestParam(defaultValue = "0") Long rootId) {
        List<MenuTree> menuTreeList = this.getMenuByRole(roleId).getData().stream().map(MenuTree::new).collect(Collectors.toList());
        return Result.ok(MenuTree.buildTree(menuTreeList, rootId));
    }


    /**
     * 保存角色的菜单信息
     *
     * @param roleId 角色ID
     * @param ids    菜单ID集合
     */
    @PostMapping("{roleId}/menu")
    public Result<?> saveMenu(@PathVariable Long roleId, @RequestBody Long[] ids) {
        // 更新角色的菜单信息
        service.saveRoleMenuAndDeleteOld(roleId, Arrays.asList(ids));
        // 通过菜单关联更新角色所关联的权限信息
        service.updateRolePermissionByMenu(roleId);
        return Result.ok();
    }
}
