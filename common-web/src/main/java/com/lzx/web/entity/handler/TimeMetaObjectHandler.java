package com.lzx.web.entity.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.lzx.web.entity.CreateTimeEntity;
import com.lzx.web.entity.UpdateTimeEntity;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 时间对象自动填充写入（为实体自动更新创建时间与更新时间）
 *
 * @author lzx
 * @since 2020/6/3
 */
@Component
public class TimeMetaObjectHandler implements MetaObjectHandler {


    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        // 填充创建时间
        this.strictInsertFill(metaObject, CreateTimeEntity.CREATE_TIME_FIELD_NAME, LocalDateTime.class, now);
        // 第一次也需要填充更新时间
        this.strictInsertFill(metaObject, UpdateTimeEntity.UPDATE_TIME_FIELD_NAME, LocalDateTime.class, now);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 填充更新时间
        this.strictUpdateFill(metaObject, UpdateTimeEntity.UPDATE_TIME_FIELD_NAME, LocalDateTime.class, LocalDateTime.now());
    }
}
