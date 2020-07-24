package com.lzx.web.entity;

import java.io.Serializable;

/**
 * @author lzx
 * @since 2019/4/26
 */
public interface CommonEntity<D> extends CreateTimeEntity, UpdateTimeEntity, IDEntity<D>, Serializable {

}
