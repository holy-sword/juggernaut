package com.lzx.service.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lzx.web.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 角色
 *
 * @author lzx
 * @since 2018/12/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role")
public class Role extends BaseEntity<Long> {

    //默认角色
    public static final long DEFAULT_ROLE_ID = 1;

    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 角色
     */
    private String name;
    /**
     * 描述
     */
    private String description;

    /**
     * 角色拥有的权限id列表
     */
    @TableField(exist = false)
    private List<Long> hasPermissionIdList;
}
