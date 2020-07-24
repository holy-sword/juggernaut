package com.lzx.service.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzx.service.user.entity.UserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lzx
 * @since 2018/12/20
 */
public interface UserRoleMapper extends BaseMapper<UserRole> {

    List<UserRole> findByUserId(@Param("userId") long userId);

}
