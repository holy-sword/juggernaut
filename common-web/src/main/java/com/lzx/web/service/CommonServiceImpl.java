package com.lzx.web.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzx.web.entity.CommonEntity;

import java.io.Serializable;

/**
 * 通用service层实现
 *
 * @param <Mapper>mapper接口
 * @param <Entity>实体
 * @param <D>主键类型
 * @author lzx
 * @since 2019/6/21
 */
public class CommonServiceImpl<Mapper extends BaseMapper<Entity>, Entity extends CommonEntity<D>, D extends Serializable> extends ServiceImpl<Mapper, Entity> implements CommonService<Entity, D> {
}
