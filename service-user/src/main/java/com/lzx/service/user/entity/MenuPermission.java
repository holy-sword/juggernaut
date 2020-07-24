package com.lzx.service.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lzx.web.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lzx
 * @since 2020/6/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_menu_permission")
public class MenuPermission extends BaseEntity<Long> {

    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 菜单ID
     */
    private Long menuId;
    /**
     * 接口权限ID
     */
    private Long permissionId;


}
