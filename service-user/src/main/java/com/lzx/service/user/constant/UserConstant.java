package com.lzx.service.user.constant;

/**
 * 用户常量
 *
 * @author lzx
 * @since 2018/12/19
 */
public interface UserConstant {

    //用户登录账号校验正则
    String USER_LOGIN_NAME_VALIDATE_REGEX = "[a-z0-9]+";
    //用户登录账号校验正则描述信息
    String USER_LOGIN_NAME_VALIDATE_REGEX_MESSAGE = "登录账号只能使用小写字母以及数字组成";
    //密码加密盐
    int PASSWORD_ENCODER_SALT = 12;
    //用于随机生成用户名的拼接字符数组
    char[] USER_NAME_CHAR_ARRAY = "QWERTYUIOPLKJHGFDSAZXCVBNMqwertyuioplkjhgfdsamnbvcxz0123456789".toCharArray();
}
