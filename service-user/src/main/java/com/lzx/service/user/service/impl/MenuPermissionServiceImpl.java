package com.lzx.service.user.service.impl;

import com.lzx.service.user.dao.MenuPermissionMapper;
import com.lzx.service.user.entity.MenuPermission;
import com.lzx.service.user.service.MenuPermissionService;
import com.lzx.web.service.CommonServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author lzx
 * @since 2020/6/3
 */
@Service
public class MenuPermissionServiceImpl extends CommonServiceImpl<MenuPermissionMapper, MenuPermission, Long> implements MenuPermissionService {

}
