## swagger 简单使用教程

### 关于swagger

Swagger 是一个规范和完整的框架，用于生成、描述、调用和可视化 RESTful 风格的 Web 服务。  
总体目标是使客户端和文件系统作为服务器以同样的速度来更新。  
文件的方法，参数和模型紧密集成到服务器端的代码，允许API来始终保持同步。

#### 作用
    1. 接口的文档在线自动生成。
    
    2. 接口的模拟数据在线测试，可直观的看见真实的返回数据等。

### 如何在spring boot中使用

#### 引入相应架包（以maven项目为例）
```xml
<project>
    <properties>
        <swagger2.version>2.9.2</swagger2.version>
    </properties>
    <dependency>
        <groupId>io.springfox</groupId>
        <artifactId>springfox-swagger2</artifactId>
        <version>${swagger2.version}</version>
    </dependency>
    <dependency>
        <groupId>io.springfox</groupId>
        <artifactId>springfox-swagger-ui</artifactId>
        <version>${swagger2.version}</version>
    </dependency>
</project>
```

#### 创建相应的swagger配置类
```java
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
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(true)
                .select()
                // 配置扫描的包路径
                .apis(RequestHandlerSelectors.basePackage("com.lzx.controller"))
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
```

#### 配置文件中启用swagger
```yaml
# 开启swagger文档
swagger:
  enable: true
```

#### 启动服务
直接启动spring boot 项目  
`swagger` 访问地址 `http://项目实际地址/swagger-ui.html`  
打开即可看见对应的文档

### 使用中注解介绍
官方文档地址：[注解](https://github.com/swagger-api/swagger-core/wiki/Annotations-1.5.X#quick-annotation-overview)  

常用注解举例（具体使用例子请参考上面官方文档）

注解 | 描述
|----|----|
@Api | 用于类上，说明该类的作用
@ApiImplicitParam |	用于方法上，表示单独的请求参数
@ApiImplicitParams | 用于方法上，里面包含多个 @ApiImplicitParam
@ApiModel | 用在entity类上，表示对类进行说明，用于参数用实体类接收 
@ApiModelProperty | 用于方法，字段上，表示对model属性的说明或者数据操作更改 
@ApiOperation | 用于方法上，描述针对特定路径的操作或通常是HTTP方法。
@ApiParam | 用于方法参数上，为操作参数添加额外的元数据。
@ApiResponse | 用在方法上，描述操作的可能响应。
@ApiResponses | 用于方法上，里面包含多个 @ApiResponse
@ResponseHeader | 用于 @@ApiResponse 中，表示可以作为响应的一部分提供的请求头


### swagger-ui界面