package com.lzx.service.user.service;


import com.lzx.service.user.entity.Permission;
import com.lzx.web.service.CommonService;

import java.util.List;

/**
 * @author lzx
 * @since 2019/3/14
 */
public interface PermissionService extends CommonService<Permission, Long> {


    /**
     * 根据用户ID获取他所有的资源权限
     *
     * @param userId 用户ID
     */
    List<Permission> getByUserId(long userId);


}
