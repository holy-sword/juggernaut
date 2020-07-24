package com.lzx.web.entity;

/**
 * 主键ID
 *
 * @author lzx
 * @since 2019/1/9
 */
public interface IDEntity<D> {

    D getId();

    void setId(D id);
}
