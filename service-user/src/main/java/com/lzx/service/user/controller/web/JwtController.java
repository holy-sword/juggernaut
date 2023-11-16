package com.lzx.service.user.controller.web;

import com.lzx.common.Result;
import com.lzx.service.user.service.JwtService;
import com.lzx.service.user.util.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;


/**
 * JWT管理
 *
 * @author lzx
 * @since 2018/12/20
 */
@RestController
@RequestMapping("api/token")
public class JwtController {

    @Autowired
    private JwtService jwtService;

    /**
     * 根据用户登录用户名以及登录密码获取token
     *
     * @param loginParam 登录参数 {@link LoginParam}
     */
    @PostMapping("login")
    public Result<?> createAuthenticationTokenByNameAndPassword(@RequestBody LoginParam loginParam) {
        //返回jwt
        return Result.ok(JwtTokenUtil.TOKEN_PREFIX + jwtService.loginByNameAndPassword(loginParam.getLoginName(), loginParam.getLoginPassword()));
    }

    /**
     * 登录参数
     */
    @Data
    static class LoginParam {
        private String loginName;
        private String loginPassword;
    }


    /**
     * 刷新token，获取新的时效性token
     */
    @GetMapping("refresh")
    public Result<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION).substring(JwtTokenUtil.TOKEN_PREFIX.length());
        String refreshedToken = jwtService.refresh(token);
        return Result.ok(JwtTokenUtil.TOKEN_PREFIX + refreshedToken);
    }

    /**
     * 验证token有效性
     *
     * @param token 要校验的token
     */
    @GetMapping("verify")
    public Result<?> verify(String token) {
        jwtService.validate(token.substring(JwtTokenUtil.TOKEN_PREFIX.length()));
        return Result.ok();
    }


}
