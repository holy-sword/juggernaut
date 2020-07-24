package com.lzx.service.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lzx.service.user.dao.MenuMapper;
import com.lzx.service.user.entity.Menu;
import com.lzx.service.user.entity.MenuPermission;
import com.lzx.service.user.service.MenuPermissionService;
import com.lzx.service.user.service.MenuService;
import com.lzx.web.service.CommonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lzx
 * @since 2020/6/3
 */
@Service
public class MenuServiceImpl extends CommonServiceImpl<MenuMapper, Menu, Long> implements MenuService {

    @Autowired
    private MenuPermissionService menuPermissionService;

    @Override
    public void saveMenuPermissionAndDeleteOld(long menuId, List<Long> permissionIdList) {
        //删除原先角色权限数据
        menuPermissionService.remove(new LambdaQueryWrapper<MenuPermission>().eq(MenuPermission::getMenuId, menuId));
        //保存新的角色权限数据
        permissionIdList.forEach(permissionId ->
                menuPermissionService.save(new MenuPermission() {{
                    setMenuId(menuId);
                    setPermissionId(permissionId);
                }})
        );
    }
}
