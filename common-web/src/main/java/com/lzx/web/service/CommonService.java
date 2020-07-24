package com.lzx.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzx.web.entity.CommonEntity;

import java.io.Serializable;

/**
 * 通用service层
 *
 * @param <Entity>实体
 * @param <D>主键类型
 * @author lzx
 * @since 2019/6/21
 */
public interface CommonService<Entity extends CommonEntity<D>, D extends Serializable> extends IService<Entity> {
}
