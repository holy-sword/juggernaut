package com.lzx.service.user.service;


import com.lzx.service.user.entity.Menu;
import com.lzx.web.service.CommonService;

import java.util.List;

/**
 * @author lzx
 * @since 2020/6/3
 */
public interface MenuService extends CommonService<Menu, Long> {

    /**
     * 更新角色的权限信息，并删除原有权限信息（先删除原有再更新）
     *
     * @param menuId           菜单ID
     * @param permissionIdList 权限ID列表
     */
    void saveMenuPermissionAndDeleteOld(long menuId, List<Long> permissionIdList);

}
