package com.lzx.service.user.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Objects;

/**
 * swagger 文档配置类
 * 在配置文件中设置swagger.enable = true 进行开启swagger-ui
 * 访问地址为 http://项目实际地址/swagger-ui.html
 *
 * @author lzx
 * @since 2019/5/28
 */
@Configuration
@EnableSwagger2
@ConditionalOnProperty(prefix = "swagger", name = "enable", havingValue = "true")
public class Swagger2Config {

    /**
     * 创建Swagger文档对象
     * apiInfo() 增加API相关信息
     * 通过select()函数返回一个ApiSelectorBuilder实例,用来控制哪些接口暴露给Swagger来展现，
     */
    @Bean
    public Docket createRestApi() {
        // 设置文档类型为 SWAGGER_2
        return new Docket(DocumentationType.SWAGGER_2)
                //添加文档显示描述信息
                .apiInfo(apiInfo())
                //启用swagger插件
                .enable(true)
                //获取ApiSelectorBuilder实例,用来控制接口暴露规则
                .select()
                // 配置扫描的包路径
                .apis(RequestHandlerSelectors.basePackage("com.lzx.user.controller"))
                // 配置文档显示的接口路径（可直接使用官方提供的工具类springfox.documentation.builders.PathSelectors）
                // 以下为只暴露以 /api 开头的接口
                .paths(path -> Objects.requireNonNull(path).startsWith("/api"))
                .build();
    }

    /**
     * 创建文档相关显示描述信息（这些基本信息会展现在文档页面中）
     * 访问地址：http://项目实际地址/swagger-ui.html
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("springboot利用swagger构建api文档")
                .description("简单优雅的restful风格")
                .version("1.0")
                .build();
    }

}
