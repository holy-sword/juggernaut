package com.lzx.service.user.service.impl;

import com.lzx.service.user.dao.UserRoleMapper;
import com.lzx.service.user.entity.User;
import com.lzx.service.user.entity.UserRole;
import com.lzx.service.user.service.JwtService;
import com.lzx.service.user.service.UserService;
import com.lzx.service.user.util.JwtTokenUtil;
import com.lzx.web.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @author lzx
 * @since 2018/12/20
 */
@Service
public class JwtServiceImpl implements JwtService {


    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public String loginByNameAndPassword(String loginName, String loginPassword) {
        //获取用户
        User user = userService.validate(loginName, loginPassword).orElseThrow(() -> new ServiceException("用户登录名错误或密码错误"));
        //用户冻结判断
        if (user.getFrozen()) {
            throw new ServiceException("该用户已经被冻结");
        }
        return this.generateTokenByUser(user);
    }

    @Override
    public String refresh(String oldToken) {
        return JwtTokenUtil.generateToken(JwtTokenUtil.getUserFromToken(oldToken));
    }

    @Override
    public void validate(String token) {
        JwtTokenUtil.getUserFromToken(token);
    }


    /**
     * 根据用户信息创建token
     */
    private String generateTokenByUser(User user) {
        //获取用户权限
        List<UserRole> userRoles = userRoleMapper.findByUserId(user.getId());
        //权限集合转化为字符串以英文逗号 `,` 分隔
        String role = userRoles.stream().map(UserRole::getRoleName).collect(Collectors.joining(","));
        return JwtTokenUtil.generateToken(
                new JwtTokenUtil.UserInfo()
                        .setId(user.getId())
                        .setLoginName(user.getLoginName())
                        .setName(user.getName())
                        .setRole(role)
        );
    }
}
