package com.lzx.service.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lzx.service.user.dao.RoleMapper;
import com.lzx.service.user.entity.*;
import com.lzx.service.user.service.*;
import com.lzx.web.exception.ServiceException;
import com.lzx.web.service.CommonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author lzx
 * @since 2018/12/21
 */
@Service
public class RoleServiceImpl extends CommonServiceImpl<RoleMapper, Role, Long> implements RoleService {

    /**
     * 重写保存，校验名称是否重复
     */
    @Override
    public boolean save(Role entity) {
        String name = entity.getName();
        Objects.requireNonNull(name, "角色名称不能为空");
        Role one = this.getOne(new LambdaQueryWrapper<Role>().eq(Role::getName, name));
        if (one != null) throw new ServiceException("角色名称不能重复，已存在名称：" + name);
        return super.save(entity);
    }

    @Autowired
    private RolePermissionService rolePermissionService;
    @Autowired
    private RoleDataPermissionService roleDataPermissionService;
    @Autowired
    private RoleMenuService roleMenuService;
    @Autowired
    private MenuPermissionService menuPermissionService;

    @Override
    public void saveRolePermissionAndDeleteOld(long roleId, Collection<Long> permissionIdList) {
        //删除原先角色权限数据
        rolePermissionService.remove(new LambdaQueryWrapper<RolePermission>().eq(RolePermission::getRoleId, roleId));
        //保存新的角色权限数据
        permissionIdList.forEach(permissionId ->
                rolePermissionService.save(new RolePermission() {{
                    setRoleId(roleId);
                    setPermissionId(permissionId);
                }})
        );
    }

    @Override
    public void saveRoleDataPermissionAndDeleteOld(long roleId, Collection<Long> dataPermissionIdList) {
        // 删除原先角色数据权限数据
        roleDataPermissionService.remove(new LambdaQueryWrapper<RoleDataPermission>().eq(RoleDataPermission::getRoleId, roleId));
        // 保存新的角色数据权限数据
        dataPermissionIdList.forEach(dataPermissionId ->
                roleDataPermissionService.save(new RoleDataPermission() {{
                    setRoleId(roleId);
                    setPermissionId(dataPermissionId);
                }})
        );
    }

    @Override
    public void saveRoleMenuAndDeleteOld(long roleId, Collection<Long> menuIdList) {
        //删除原先角色权限数据
        roleMenuService.remove(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId, roleId));
        //保存新的角色权限数据
        menuIdList.forEach(menuId ->
                roleMenuService.save(new RoleMenu() {{
                    setRoleId(roleId);
                    setMenuId(menuId);
                }})
        );
    }

    @Override
    public void updateRolePermissionByMenu(long roleId) {
        // 首先查询出角色关联的菜单ID集合
        Set<Long> menuIds = roleMenuService.list(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId, roleId))
                .stream()
                .map(RoleMenu::getMenuId)
                .collect(Collectors.toSet());
        if (menuIds.isEmpty()) return;
        // 通过菜单ID集合去找到菜单关联的权限ID集合
        Set<Long> permissionIds = menuPermissionService.list(new LambdaQueryWrapper<MenuPermission>().in(MenuPermission::getMenuId, menuIds))
                .stream()
                .map(MenuPermission::getPermissionId)
                .collect(Collectors.toSet());
        if (permissionIds.isEmpty()) return;

        //调用角色更新权限方法
        this.saveRolePermissionAndDeleteOld(roleId, permissionIds);
    }

    @Override
    public List<Role> list(Wrapper<Role> queryWrapper) {
        List<Role> list = super.list(queryWrapper);

        list.forEach(e -> {
            //获取用户拥有的权限ID集合
            List<Long> permissionIds = rolePermissionService.list(new LambdaQueryWrapper<RolePermission>()
                    .eq(RolePermission::getRoleId, e.getId()))
                    .stream()
                    .map(RolePermission::getPermissionId)
                    .collect(Collectors.toList());
            e.setHasPermissionIdList(permissionIds);
        });

        return list;
    }
}
