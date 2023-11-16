package com.lzx.service.user.controller.web;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lzx.common.Result;
import com.lzx.service.user.entity.Menu;
import com.lzx.service.user.entity.RoleMenu;
import com.lzx.service.user.entity.User;
import com.lzx.service.user.entity.UserRole;
import com.lzx.service.user.entity.dto.MenuTree;
import com.lzx.service.user.service.*;
import com.lzx.service.user.util.UserUtil;
import com.lzx.web.controller.BaseController;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户信息管理
 *
 * @author lzx
 * @since 2018/12/14
 */
@RestController
@RequestMapping("api/user")
public class UserController extends BaseController<User, Long> {

    @Autowired
    private UserService service;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleMenuService roleMenuService;
    @Autowired
    private MenuService menuService;


    /**
     * 获取当前登录用户信息
     */
    @GetMapping("info")
    public Result<?> getCurrentUserInfo() {
        long userId = UserUtil.getCurrentUserId();
        User user = service.getById(userId);
        return Result.ok(user);
    }

    /**
     * 获取当前登录用户的权限信息
     */
    @GetMapping("info/permission")
    public Result<?> getCurrentUserPermissionInfo() {
        long userId = UserUtil.getCurrentUserId();
        return Result.ok(permissionService.getByUserId(userId));
    }


    /**
     * 获取当前登录用户的菜单信息
     */
    @GetMapping("info/menu/tree")
    public Result<?> getCurrentUserMenuTreeInfo(HttpServletRequest request, @RequestParam(defaultValue = "0") Long rootId) {
        long userId = UserUtil.getCurrentUserId();
        //先获取用户拥有的角色ID集合
        Set<Long> roleIds = userRoleService
                .list(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, userId))
                .stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toSet());
        //根据角色ID集合查询菜单ID集合
        Set<Long> menuIds = roleMenuService
                .list(new LambdaQueryWrapper<RoleMenu>().in(RoleMenu::getRoleId, roleIds))
                .stream()
                .map(RoleMenu::getMenuId)
                .collect(Collectors.toSet());
        //查询出对应菜单并构造权限树信息
        LambdaQueryWrapper<Menu> menuWrapper = buildQueryWrapperFromRequest(request, Menu.class).lambda();
        List<MenuTree> menuTreeList = menuService
                .list(menuWrapper.in(Menu::getId, menuIds).orderByAsc(Menu::getSort))
                .stream()
                .map(MenuTree::new)
                .collect(Collectors.toList());
        return Result.ok(MenuTree.buildTree(menuTreeList, rootId));
    }


    /**
     * 修改登录密码
     *
     * @param oldPassword 原密码
     * @param newPassword 新密码
     */
    @RequestMapping(value = "password/change", method = {RequestMethod.POST, RequestMethod.PUT})
    public Result<?> changeLoginPassword(@RequestParam String oldPassword, @RequestParam String newPassword) {
        service.changePassword(UserUtil.getCurrentUserId(), oldPassword, newPassword);
        return Result.ok();
    }


}
