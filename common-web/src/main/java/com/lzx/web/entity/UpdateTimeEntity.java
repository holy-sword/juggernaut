package com.lzx.web.entity;

import java.time.LocalDateTime;

/**
 * 更新时间
 *
 * @author lzx
 * @since 2019/1/9
 */
public interface UpdateTimeEntity {

    String UPDATE_TIME_FIELD_NAME = "updateTime";

    LocalDateTime getUpdateTime();

    void setUpdateTime(LocalDateTime updateTime);


}
