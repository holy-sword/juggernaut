package com.lzx.service.user.entity;


import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName("sys_data_permission")
public class DataPermission extends BaseEntity<Long> {

    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 资源路径（支持/*一级匹配、/**多级匹配）
     */
    private String url;
    /**
     * 请求类型（类型为 HttpMethod，支持 * 匹配所有）
     */
    private String httpMethod;
    /**
     * 数据权限
     */
    private String permission;
    /**
     * 描述信息
     */
    private String description;
    /**
     * 类别
     */
    private String category;
    /**
     * 排序，越小越靠前
     */
    private Integer ordered;
}
