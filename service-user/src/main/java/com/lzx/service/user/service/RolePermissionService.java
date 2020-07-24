package com.lzx.service.user.service;


import com.lzx.service.user.entity.RolePermission;
import com.lzx.web.service.CommonService;

import java.util.List;

/**
 * @author lzx
 * @since 2019/3/14
 */
public interface RolePermissionService extends CommonService<RolePermission, Long> {


    /**
     * 根据用户ID获取他所有的资源权限
     *
     * @param userId 用户ID
     * @return 资源权限包装对象
     */
    List<RolePermission> getByUserId(long userId);

}
