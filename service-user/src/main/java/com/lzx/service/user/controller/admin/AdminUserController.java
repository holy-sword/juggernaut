package com.lzx.service.user.controller.admin;

import com.lzx.common.Result;
import com.lzx.service.user.constant.UserConstant;
import com.lzx.service.user.entity.User;
import com.lzx.service.user.entity.UserRole;
import com.lzx.service.user.service.PermissionService;
import com.lzx.service.user.service.UserRoleService;
import com.lzx.service.user.service.UserService;
import com.lzx.web.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户信息管理
 *
 * @author lzx
 * @since 2018/12/14
 */
@RestController
@RequestMapping("api/admin/user")
public class AdminUserController extends AbstractController<UserService, User, Long> {

    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private PermissionService permissionService;


    /**
     * 更新（重写以设置角色信息）
     *
     * @param entity 更新对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> update(@RequestBody User entity) {
        //处理角色（角色以英文逗号分隔）
        if (StringUtils.hasText(entity.getRole())) {
            //先删除原有角色信息
            userRoleService.removeUserRole(entity.getId(), 0);
            for (String s : entity.getRole().split(",")) {
                userRoleService.addUserRole(entity.getId(), Long.parseLong(s));
            }
        }
        //处理密码
        if (StringUtils.hasText(entity.getLoginPassword())) {
            entity.setLoginPassword(new BCryptPasswordEncoder(UserConstant.PASSWORD_ENCODER_SALT).encode(entity.getLoginPassword()));
        }

        return Result.ok(service.updateById(entity));
    }


    /**
     * 根据用户ID获取用户信息（带关联信息与权限）
     *
     * @param id 用户ID
     */
    @GetMapping("{id}/info")
    public Result<?> getUserById(@PathVariable Long id, @RequestParam(required = false, defaultValue = "false") Boolean hasRole) {
        User user = service.getById(id);
        if (hasRole) {
            List<UserRole> userRoles = userRoleService.listByUserId(id);
            //权限集合转化为字符串以英文逗号 `,` 分隔
            user.setRole(userRoles.stream().map(UserRole::getRoleName).collect(Collectors.joining(",")));
        }
        return Result.ok(user);
    }


    /**
     * 根据用户ID获取权限信息
     *
     * @param id 用户ID
     */
    @GetMapping(value = "{id}/permission")
    public Result<?> getByUserId(@PathVariable Long id) {
        //获取用户拥有的权限
        return Result.ok(permissionService.getByUserId(id));
    }

}
