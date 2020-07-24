package com.lzx.service.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lzx.web.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lzx
 * @since 2020/5/12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role_data_permission")
public class RoleDataPermission extends BaseEntity<Long> {

    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 角色ID
     */
    private Long roleId;
    /**
     * 权限ID
     */
    private Long permissionId;
    /**
     * 数据权限信息
     */
    @TableField(exist = false)
    private String permission;
    /**
     * 权限资源路径（支持/*一级匹配、/**多级匹配）
     */
    @TableField(exist = false)
    private String permissionUrl;
    /**
     * 权限请求类型（类型为 HttpMethod，支持 * 匹配所有）
     */
    @TableField(exist = false)
    private String permissionHttpMethod;
    /**
     * 权限描述信息
     */
    @TableField(exist = false)
    private String permissionDescription;
    /**
     * 角色
     */
    @TableField(exist = false)
    private String roleName;
    /**
     * 角色描述信息
     */
    @TableField(exist = false)
    private String roleDescription;
}
