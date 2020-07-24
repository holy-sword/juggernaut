package com.lzx.service.user.service.impl;

import com.lzx.service.user.dao.RolePermissionMapper;
import com.lzx.service.user.entity.RolePermission;
import com.lzx.service.user.service.RolePermissionService;
import com.lzx.web.service.CommonServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lzx
 * @since 2019/3/14
 */
@Service
public class RolePermissionServiceImpl extends CommonServiceImpl<RolePermissionMapper, RolePermission, Long> implements RolePermissionService {


    @Override
    public List<RolePermission> getByUserId(long userId) {
        return baseMapper.listByUserId(userId);
    }

}
