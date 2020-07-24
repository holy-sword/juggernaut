package com.lzx.service.user.util;


import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 用户工具类（主要用于获取当前请求的用户ID）
 *
 * @author lzx
 * @since 2020/05/20
 */
public class UserUtil {

    /**
     * 用户ID
     */
    private static final String USER_ID = "X-User-Id";
    /**
     * 用户名称
     */
    private static final String USER_NAME = "X-User-Name";
    /**
     * 数据权限
     */
    private static final String DATA_PERMISSION_HEADER = "Data-Permission";

    /**
     * 获取来自网关转发请求中的用户ID
     *
     * @return userId
     */
    public static long getCurrentUserId() {
        return Long.parseLong(Objects.requireNonNull(getCurrentRequest().getHeader(USER_ID), "获取用户ID失败，请确认请求来源"));
    }

    /**
     * 获取来自网关转发请求中的用户名称
     *
     * @return userName
     */
    public static String getCurrentUserName() {
        return URLDecoder.decode(Objects.requireNonNull(getCurrentRequest().getHeader(USER_NAME), "获取用户名称失败，请确认请求来源"), StandardCharsets.UTF_8);
    }


    /**
     * 获取来自网关转发请求中的数据权限信息
     *
     * @return dataPermission
     */
    public static String getCurrentDataPermission() {
        return Objects.requireNonNull(getCurrentRequest().getHeader(DATA_PERMISSION_HEADER), "获取数据权限失败，请确认请求来源");
    }


    /**
     * 获取当前request
     */
    public static HttpServletRequest getCurrentRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes(), "请确认当前处于web环境中")).getRequest();
    }


    private UserUtil() {
    }

}
