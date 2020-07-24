package com.lzx.service.user.service.impl;

import com.lzx.service.user.dao.PermissionMapper;
import com.lzx.service.user.entity.Permission;
import com.lzx.service.user.service.PermissionService;
import com.lzx.web.service.CommonServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lzx
 * @since 2019/3/14
 */
@Service
public class PermissionServiceImpl extends CommonServiceImpl<PermissionMapper, Permission, Long> implements PermissionService {

    @Override
    public List<Permission> getByUserId(long userId) {
        return baseMapper.listByUserId(userId);
    }
}
