package com.lzx.web.entity;

import java.time.LocalDateTime;

/**
 * 创建时间
 *
 * @author lzx
 * @since 2019/1/9
 */
public interface CreateTimeEntity {

    String CREATE_TIME_FIELD_NAME = "createTime";

    LocalDateTime getCreateTime();

    void setCreateTime(LocalDateTime createTime);
}
