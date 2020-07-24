package com.lzx.service.user.service.impl;

import com.lzx.service.user.dao.DataPermissionMapper;
import com.lzx.service.user.entity.DataPermission;
import com.lzx.service.user.service.DataPermissionService;
import com.lzx.web.service.CommonServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lzx
 * @since 2019/3/14
 */
@Service
public class DataPermissionServiceImpl extends CommonServiceImpl<DataPermissionMapper, DataPermission, Long> implements DataPermissionService {

    @Override
    public List<DataPermission> getByUserId(long userId) {
        return baseMapper.listByUserId(userId);
    }
}
