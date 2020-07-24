package com.lzx.cloud.gateway.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * feign 配置（主要更改解析json方式同其他服务一致）
 *
 * @author lzx
 * @since 2020/6/8
 */
@Configuration
public class FeignConfig {

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 配置fastJson
     */
    private AbstractHttpMessageConverter<?> fastJsonConverter() {
        final FastJsonHttpMessageConverter fastJsonConverter = new FastJsonHttpMessageConverter();
        final FastJsonConfig config = new FastJsonConfig();
        config.setSerializerFeatures(SerializerFeature.IgnoreErrorGetter, SerializerFeature.WriteNullStringAsEmpty);
        config.setCharset(StandardCharsets.UTF_8);
        config.setDateFormat(DEFAULT_DATE_FORMAT);
        fastJsonConverter.setFastJsonConfig(config);
        //spring要求Content-Type不能含有通配符，fastJson默认为MediaType.ALL不可使用，这应该是一种保护机制,并强制用户自己配置MediaType
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        fastJsonConverter.setSupportedMediaTypes(supportedMediaTypes);

        return fastJsonConverter;
    }

    /**
     * feign 编码（若未使用feign，此处请删除）
     */
    @Bean
    public Encoder feignEncoder() {
        return new SpringEncoder(() -> new HttpMessageConverters(fastJsonConverter()));
    }

    /**
     * feign 解码（若未使用feign，此处请删除）
     */
    @Bean
    public Decoder feignDecoder() {
        return new SpringDecoder(() -> new HttpMessageConverters(fastJsonConverter()));
    }

}
