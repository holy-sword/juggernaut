package com.lzx.service.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzx.service.user.entity.RolePermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lzx
 * @since 2019/3/14
 */
public interface RolePermissionMapper extends BaseMapper<RolePermission> {


    /**
     * 根据用户ID获取他全部权限
     *
     * @param userId
     */
    List<RolePermission> listByUserId(@Param("userId") long userId);


}
