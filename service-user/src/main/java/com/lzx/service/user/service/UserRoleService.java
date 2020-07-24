package com.lzx.service.user.service;


import com.lzx.service.user.entity.UserRole;
import com.lzx.web.service.CommonService;

import java.util.List;

/**
 * @author lzx
 * @since 2019/1/9
 */
public interface UserRoleService extends CommonService<UserRole, Long> {

    /**
     * 添加用户角色
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     */
    UserRole addUserRole(long userId, long roleId);

    /**
     * 删除用户角色
     *
     * @param userId 用户ID
     * @param roleId 为 0 时则删除用户所有角色
     */
    int removeUserRole(long userId, long roleId);

    /**
     * 根据用户ID获取用户角色
     *
     * @param userId 用户ID
     */
    List<UserRole> listByUserId(long userId);
}
