package com.lzx.web.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 基础实体
 *
 * @param <D>主键类型
 * @author lzx
 * @since 2018/12/14 10:47
 */
@Data
public abstract class BaseEntity<D> implements CommonEntity<D> {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    protected LocalDateTime createTime;
    /**
     * 最近更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected LocalDateTime updateTime;


}
