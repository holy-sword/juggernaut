package com.lzx.cloud.gateway.feign.dto;

import lombok.Data;

/**
 * @author lzx
 * @since 2020/5/20
 */
@Data
public class Permission {

    /**
     * 资源路径（支持/*一级匹配、/**多级匹配）
     */
    private String url;
    /**
     * 请求类型（类型为 HttpMethod，支持 * 匹配所有）
     */
    private String httpMethod;

}
