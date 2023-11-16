package com.lzx.web.config;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.support.spring6.http.converter.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Spring MVC 配置类（注入FastJson）
 *
 * @author lzx
 * @since 18/12/10.
 */
@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 注入restTemplate（移除jackson并设置fastJson作为解析）
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate() {{
            List<HttpMessageConverter<?>> messageConverters = getMessageConverters();
            messageConverters.removeIf(next -> next instanceof MappingJackson2HttpMessageConverter);
            AbstractHttpMessageConverter<?> messageConverter = fastJsonConverter();
            messageConverter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
            messageConverters.add(messageConverter);
        }};
    }

    /**
     * 设置编码，解决中文乱码
     */
    @Bean
    public StringHttpMessageConverter stringHttpMessageConverter() {
        return new StringHttpMessageConverter(StandardCharsets.UTF_8);
    }

    /**
     * 添加到内置转换器之后（移除jackson，使用fastJson）
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.removeIf(next -> next instanceof MappingJackson2HttpMessageConverter);
        converters.add(fastJsonConverter());
        //为了适配某些模块使用了特定的Jackson所以添加在最后默认解析fastjson没有处理完的特殊json格式
        converters.add(new MappingJackson2HttpMessageConverter());
    }

    /**
     * 配置fastJson
     */
    private AbstractHttpMessageConverter<?> fastJsonConverter() {
        final FastJsonHttpMessageConverter fastJsonConverter = new FastJsonHttpMessageConverter();
        final FastJsonConfig config = new FastJsonConfig();
        config.setWriterFeatures(JSONWriter.Feature.WriteNullStringAsEmpty, JSONWriter.Feature.IgnoreErrorGetter);
        config.setCharset(StandardCharsets.UTF_8);
        config.setDateFormat(DEFAULT_DATE_FORMAT);
        fastJsonConverter.setFastJsonConfig(config);
        //spring要求Content-Type不能含有通配符，fastJson默认为MediaType.ALL不可使用，这应该是一种保护机制,并强制用户自己配置MediaType
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        fastJsonConverter.setSupportedMediaTypes(supportedMediaTypes);

        return fastJsonConverter;
    }


}
