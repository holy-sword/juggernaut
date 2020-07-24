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
@TableName("sys_menu")
public class Menu extends BaseEntity<Long> {

    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 上级菜单ID
     */
    private Long parentId;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 菜单名称
     */
    private String name;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 图标
     */
    private String icon;
    /**
     * 路径
     */
    private String path;
    /**
     * 缓存
     */
    private String keepAlive;
    /**
     * 菜单类型
     */
    private String type;
    /**
     * 权限字段
     */
    private String permission;


}
