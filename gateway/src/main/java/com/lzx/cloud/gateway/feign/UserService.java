package com.lzx.cloud.gateway.feign;

import com.lzx.cloud.gateway.feign.dto.Permission;
import com.lzx.cloud.gateway.feign.fallback.UserServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 获取用户服务中的接口
 *
 * @author lzx
 * @since 2020/5/20
 */
@FeignClient(value = "service-user", fallback = UserServiceFallback.class)
public interface UserService {

    /**
     * 获取所有权限资源列表
     */
    @RequestMapping(method = RequestMethod.GET, value = "permission")
    List<Permission> getAllPermission();

    /**
     * 获取某个用户全部的权限资源
     */
    @RequestMapping(method = RequestMethod.GET, value = "permission/user/{userId}")
    List<Permission> getPermissionByUserId(@PathVariable("userId") Long userId);

    /**
     * 获取某个用户对应接口的权限资源
     *
     * @param userId     用户ID
     * @param url        接口地址
     * @param httpMethod 请求方式
     */
    @RequestMapping(method = RequestMethod.POST, value = "permission/data")
    String getDataPermissionByUserIdAndPermission(@RequestParam Long userId, @RequestParam String httpMethod, @RequestParam String url);
}
