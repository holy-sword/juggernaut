package com.lzx.service.user.service.impl;

import com.lzx.service.user.dao.RoleMenuMapper;
import com.lzx.service.user.entity.RoleMenu;
import com.lzx.service.user.service.RoleMenuService;
import com.lzx.web.service.CommonServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author lzx
 * @since 2020/6/3
 */
@Service
public class RoleMenuServiceImpl extends CommonServiceImpl<RoleMenuMapper, RoleMenu, Long> implements RoleMenuService {

}
