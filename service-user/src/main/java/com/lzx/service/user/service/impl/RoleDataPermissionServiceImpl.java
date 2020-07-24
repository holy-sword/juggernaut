package com.lzx.service.user.service.impl;

import com.lzx.service.user.dao.RoleDataPermissionMapper;
import com.lzx.service.user.entity.RoleDataPermission;
import com.lzx.service.user.service.RoleDataPermissionService;
import com.lzx.web.service.CommonServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lzx
 * @since 2019/3/14
 */
@Service
public class RoleDataPermissionServiceImpl extends CommonServiceImpl<RoleDataPermissionMapper, RoleDataPermission, Long> implements RoleDataPermissionService {


    @Override
    public List<RoleDataPermission> getByUserId(long userId) {
        return baseMapper.listByUserId(userId);
    }

}
