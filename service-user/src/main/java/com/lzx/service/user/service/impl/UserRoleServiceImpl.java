package com.lzx.service.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lzx.service.user.dao.UserRoleMapper;
import com.lzx.service.user.entity.UserRole;
import com.lzx.service.user.service.UserRoleService;
import com.lzx.web.service.CommonServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author lzx
 * @since 2019/1/9
 */
@Service
public class UserRoleServiceImpl extends CommonServiceImpl<UserRoleMapper, UserRole, Long> implements UserRoleService {


    @Override
    public UserRole addUserRole(long userId, long roleId) {
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(roleId);
        baseMapper.insert(userRole);
        return userRole;
    }

    @Override
    public int removeUserRole(long userId, long roleId) {
        LambdaQueryWrapper<UserRole> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserRole::getUserId, userId);
        if (roleId > 0) {
            wrapper.eq(UserRole::getRoleId, roleId);
        }
        return baseMapper.delete(wrapper);
    }

    @Override
    public List<UserRole> listByUserId(long userId) {
        if (userId > 0) {
            return baseMapper.findByUserId(userId);
        }
        return Collections.emptyList();
    }
}
