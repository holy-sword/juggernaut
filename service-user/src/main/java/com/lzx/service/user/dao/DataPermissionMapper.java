package com.lzx.service.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzx.service.user.entity.DataPermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lzx
 * @since 2019/3/14
 */
public interface DataPermissionMapper extends BaseMapper<DataPermission> {


    /**
     * 根据用户ID获取他拥有的权限（不包括角色信息）
     *
     * @param userId 用户ID
     */
    List<DataPermission> listByUserId(@Param("userId") long userId);

}
