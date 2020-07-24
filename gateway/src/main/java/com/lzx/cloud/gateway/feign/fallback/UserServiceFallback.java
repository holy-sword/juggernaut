package com.lzx.cloud.gateway.feign.fallback;

import com.lzx.cloud.gateway.feign.UserService;
import com.lzx.cloud.gateway.feign.dto.Permission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author lzx
 * @since 2020/5/20
 */
@Component
@Slf4j
public class UserServiceFallback implements UserService {
    @Override
    public List<Permission> getAllPermission() {
        log.error("没有获取权限");
        return Collections.emptyList();
    }

    @Override
    public List<Permission> getPermissionByUserId(Long userId) {
        log.error("没有获取到用户权限");
        return Collections.emptyList();
    }

    @Override
    public String getDataPermissionByUserIdAndPermission(Long userId, String httpMethod, String url) {
        return "";
    }
}
