package com.lzx.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 主键自增基础实体
 *
 * @author lzx
 * @since 2020/6/16
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class IdAutoBaseEntity extends BaseEntity<Long> {

    @TableId(type = IdType.AUTO)
    private Long id;

}
