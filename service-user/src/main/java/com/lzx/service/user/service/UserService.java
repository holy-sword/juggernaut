package com.lzx.service.user.service;


import com.lzx.service.user.entity.User;
import com.lzx.web.service.CommonService;

import java.util.Optional;

/**
 * @author lzx
 * @since 2018/12/14
 */
public interface UserService extends CommonService<User, Long> {

    /**
     * 新增用户（注册）
     */
    User insert(User user);

    /**
     * 根据ID获取带有权限的用户信息
     *
     * @param id 用户id
     * @return user
     */
    User getUserAndRoleById(long id);

    /**
     * 根据登录账号获取用户信息
     */
    Optional<User> getUserByLoginName(String loginName);

    /**
     * 根据用户名获取用户信息
     */
    Optional<User> getUserByName(String name);

    /**
     * 根据手机号获取用户信息
     */
    Optional<User> getUserByTel(String tel);

    /**
     * 用户登录账号密码校验
     */
    Optional<User> validate(String loginName, String loginPassword);

    /**
     * 修改密码
     *
     * @param userId      修改用户
     * @param oldPassword 原密码
     * @param newPassword 新密码
     */
    void changePassword(long userId, String oldPassword, String newPassword);

}
