package com.lzx.service.user.service;

/**
 * @author lzx
 * @since 2018/12/20
 */
public interface JwtService {

    /**
     * 使用账号密码登录发放token
     *
     * @param loginName     登录名
     * @param loginPassword 登录密码
     */
    String loginByNameAndPassword(String loginName, String loginPassword);

    /**
     * 刷新token（获取一个新的，旧的存在过期时间）
     *
     * @param oldToken 旧的token
     */
    String refresh(String oldToken);

    /**
     * 校验token
     */
    void validate(String token);


}
