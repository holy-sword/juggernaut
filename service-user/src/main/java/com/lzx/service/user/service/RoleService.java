package com.lzx.service.user.service;


import com.lzx.service.user.entity.Role;
import com.lzx.web.service.CommonService;

import java.util.Collection;

/**
 * @author lzx
 * @since 2018/12/21
 */
public interface RoleService extends CommonService<Role, Long> {

    /**
     * 更新角色的权限信息，并删除原有权限信息（先删除原有再更新）
     *
     * @param roleId           角色ID
     * @param permissionIdList 权限ID列表
     */
    void saveRolePermissionAndDeleteOld(long roleId, Collection<Long> permissionIdList);

    /**
     * 更新角色的数据权限信息，并删除原有数据权限信息（先删除原有再更新）
     *
     * @param roleId               角色ID
     * @param dataPermissionIdList 数据权限ID列表
     */
    void saveRoleDataPermissionAndDeleteOld(long roleId, Collection<Long> dataPermissionIdList);

    /**
     * 更新角色的菜单信息，并删除原有菜单信息（先删除原有再更新）
     *
     * @param roleId     角色ID
     * @param menuIdList 菜单ID列表
     */
    void saveRoleMenuAndDeleteOld(long roleId, Collection<Long> menuIdList);

    /**
     * 根据角色的菜单一系列关联信息，更新角色的权限信息
     *
     * @param roleId 更新的角色ID
     */
    void updateRolePermissionByMenu(long roleId);

}
