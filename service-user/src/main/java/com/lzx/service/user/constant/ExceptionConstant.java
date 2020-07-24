package com.lzx.service.user.constant;

/**
 * 业务异常状态码
 *
 * @author lzx
 * @since 2019/2/11
 */
public interface ExceptionConstant {

    //与微信连接失败
    int WECHAT_CONNECTION_FAIL = -2;

    //用户未绑定微信账号
    int USER_UNBOUND_WECHAT_ACCOUNT = -3;

}
