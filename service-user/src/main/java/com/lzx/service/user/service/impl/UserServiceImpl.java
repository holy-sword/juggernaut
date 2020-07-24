package com.lzx.service.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lzx.service.user.constant.UserConstant;
import com.lzx.service.user.dao.UserMapper;
import com.lzx.service.user.entity.User;
import com.lzx.service.user.entity.UserRole;
import com.lzx.service.user.service.UserRoleService;
import com.lzx.service.user.service.UserService;
import com.lzx.web.exception.ServiceException;
import com.lzx.web.service.CommonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author lzx
 * @since 2018/12/14
 */
@Service
public class UserServiceImpl extends CommonServiceImpl<UserMapper, User, Long> implements UserService {

    @Autowired
    private UserRoleService userRoleService;

    /**
     * 重写新增逻辑（BaseController中默认调用添加）
     */
    @Override
    public boolean save(User entity) {
        this.insert(entity);
        return true;
    }

    /**
     * 重写列表查询，携带角色信息
     *
     * @param queryWrapper 查询参数
     */
    @Override
    public List<User> list(Wrapper<User> queryWrapper) {
        List<User> list = super.list(queryWrapper);
        list.forEach(e -> {
            List<UserRole> userRoles = userRoleService.listByUserId(e.getId());
            //权限集合转化为字符串以英文逗号 `,` 分隔
            e.setRole(userRoles.stream().map(UserRole::getRoleId).map(String::valueOf).collect(Collectors.joining(",")));
        });
        return list;

    }

    /**
     * 重写获取单个用户信息，携带角色信息
     *
     * @param id 用户id
     */
    @Override
    public User getById(Serializable id) {
        User user = super.getById(id);
        List<UserRole> userRoles = userRoleService.listByUserId((Long) id);
        user.setRole(userRoles.stream().map(UserRole::getRoleId).map(String::valueOf).collect(Collectors.joining(",")));
        return user;
    }

    @Transactional
    @Override
    public User insert(User user) {
        //检测用户名
        if (this.getUserByName(Objects.requireNonNull(user.getName(), "用户名不能为空")).isPresent()) {
            throw new ServiceException("该用户名已经存在：" + user.getName());
        }
        //检测登录账号
        String loginName = Objects.requireNonNull(user.getLoginName(), "登录账号不能为空");
        if (!loginName.matches(UserConstant.USER_LOGIN_NAME_VALIDATE_REGEX)) {
            throw new ServiceException(UserConstant.USER_LOGIN_NAME_VALIDATE_REGEX_MESSAGE);
        }
        if (this.getUserByLoginName(loginName).isPresent()) {
            throw new ServiceException("该登录账号已经存在：" + user.getLoginName());
        }
        //检测手机号（第三方关联账号创建时可为空）
        if (!StringUtils.isEmpty(user.getTel())) {
            if (this.getUserByTel(user.getTel()).isPresent()) {
                throw new ServiceException("该手机号码已经被使用：" + user.getTel());
            }
        }

        user.setFrozen(false);
        //密码加密存储
        String password = user.getLoginPassword();
        if (StringUtils.isEmpty(password)) {
            throw new ServiceException("登录密码不能为空");
        }
        user.setLoginPassword(new BCryptPasswordEncoder(UserConstant.PASSWORD_ENCODER_SALT).encode(password));
        baseMapper.insert(user);

        //添加角色
        String role = user.getRole();
        if (StringUtils.isEmpty(role)) {
            throw new ServiceException("用户角色不能为空");
        }
        for (String s : role.split(",")) {
            userRoleService.addUserRole(user.getId(), Long.parseLong(s));
        }

        return user;
    }

    @Override
    public User getUserAndRoleById(long id) {
        User user = baseMapper.selectById(id);
        List<UserRole> userRoles = userRoleService.listByUserId(id);
        //权限集合转化为字符串以英文逗号 `,` 分隔
        user.setRole(userRoles.stream().map(UserRole::getRoleId).map(String::valueOf).collect(Collectors.joining(",")));
        return user;
    }

    @Override
    public Optional<User> getUserByLoginName(String loginName) {
        return Optional.ofNullable(baseMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getLoginName, loginName)));
    }

    @Override
    public Optional<User> getUserByName(String name) {
        return Optional.ofNullable(baseMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getName, name)));
    }

    @Override
    public Optional<User> getUserByTel(String tel) {
        return Optional.ofNullable(baseMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getTel, tel)));
    }

    @Override
    public Optional<User> validate(String loginName, String loginPassword) {
        //提示信息加上密码错误，防止扫库
        User user = this.getUserByLoginName(loginName).orElseThrow(() -> new ServiceException("登录账号或登录密码错误"));
        if (new BCryptPasswordEncoder(UserConstant.PASSWORD_ENCODER_SALT).matches(loginPassword, user.getLoginPassword())) {
            return Optional.of(user);
        }
        return Optional.empty();
    }

    @Override
    public void changePassword(long userId, String oldPassword, String newPassword) {
        User user = Objects.requireNonNull(this.baseMapper.selectById(userId), "用户不存在，ID：" + userId);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(UserConstant.PASSWORD_ENCODER_SALT);
        //校验原密码
        if (bCryptPasswordEncoder.matches(oldPassword, user.getLoginPassword())) {
            //修改密码
            this.updateById(new User() {{
                setId(userId);
                setLoginPassword(bCryptPasswordEncoder.encode(newPassword));
            }});
        } else {
            throw new ServiceException("用户原密码不正确");
        }
    }

}
