package com.lzx.service.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lzx.web.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户实体
 *
 * @author lzx
 * @since 2018/12/17 16:44
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
public class User extends BaseEntity<Long> {

    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 用户名称
     */
    private String name;
    /**
     * 别名
     */
    private String alias;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 手机号码
     */
    private String tel;
    /**
     * 性别
     */
    private String sex;
    /**
     * 生日
     */
    private String birthday;
    /**
     * 地址
     */
    private String address;
    /**
     * 证件号
     */
    private String cardNo;
    /**
     * 证件类型
     */
    private String cardType;
    /**
     * 登录账号
     */
    private String loginName;
    /**
     * 登录密码
     */
    private String loginPassword;
    /**
     * 是否冻结
     */
    @TableField("is_frozen")
    private Boolean frozen;
    /**
     * 用户角色ID（多个以英文逗号","分隔）
     */
    @TableField(exist = false)
    private String role;
}
