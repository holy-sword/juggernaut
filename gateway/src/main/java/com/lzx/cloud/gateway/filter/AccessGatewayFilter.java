package com.lzx.cloud.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.lzx.cloud.gateway.feign.UserService;
import com.lzx.cloud.gateway.feign.dto.Permission;
import com.lzx.cloud.gateway.util.JwtTokenUtil;
import com.lzx.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

/**
 * 登录权限拦截器（全局过滤器）
 *
 * @author lzx
 * @since 2020/5/20
 */
@Configuration
@Slf4j
public class AccessGatewayFilter implements GlobalFilter {


    /**
     * 网关忽略请求列表，多个以英文逗号 "," 分隔
     */
    @Value("${gateway.ignore.start-with}")
    private String startWith;
    /**
     * 约定的接口请求前缀，匹配权限时默认清除
     */
    private static final String GATE_WAY_PREFIX = "/api";
    /**
     * 放入header中转发其他服务的数据权限名称
     */
    private static final String DATA_PERMISSION_HEADER = "Data-Permission";
    /**
     * token解析异常码（前端判断使用）
     */
    private static final int TOKEN_EXCEPTION_CODE = -500;
    /**
     * 没有权限访问异常码（前端判断使用）
     */
    private static final int AUTHORITY_EXCEPTION_CODE = -501;

    @Autowired
    private UserService userService;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        LinkedHashSet<URI> requiredAttribute = exchange.getRequiredAttribute(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
        ServerHttpRequest request = exchange.getRequest();
        //获取请求原始路径
        String requestUri = request.getPath().pathWithinApplication().value();
        // 获取请求方式
        final String method = request.getMethodValue();
        log.info("开始检测 token 与 用户权限，请求路径：【{}】{}", method, requestUri);
        //清除接口请求约定的前缀
        for (URI next : requiredAttribute) {
            if (next.getPath().startsWith(GATE_WAY_PREFIX)) {
                requestUri = next.getPath().substring(GATE_WAY_PREFIX.length());
            }
        }
        ServerHttpRequest.Builder mutate = request.mutate();
        // 不进行拦截的地址
        if (isStartWith(requestUri)) {
            return chain.filter(exchange);
        }

        JwtTokenUtil.UserInfo user;
        try {
            user = getJWTUser(request, mutate);
        } catch (Exception e) {
            log.warn("用户Token异常", e);
            return getVoidMono(exchange, Result.fail(TOKEN_EXCEPTION_CODE, new RuntimeException("用户token异常")));
        }
        //经过去请求头之后的URI
        String filterRequestUri = requestUri;
        List<Permission> permissionList = userService.getAllPermission();
        // 判断资源是否启用权限约束（资源若不在约束的资源列表中直接放行）
        if (permissionList
                .parallelStream()
                .anyMatch(permission -> filterRequestUri.matches(parseUrl2Regex(permission.getUrl())))) {
            //处理需要权限约束的资源

            //获取该用户的权限资源
            List<Permission> permissionByUserList = userService.getPermissionByUserId(user.getId());
            if (permissionByUserList
                    .parallelStream()
                    .noneMatch(permission -> matchUrl(filterRequestUri, permission.getUrl()) && matchHttpMethod(method, permission.getHttpMethod()))) {
                //没有一个匹配则没有权限访问
                log.warn("用户 {} 没有权限访问路径 {}【{}】 ", user.getLoginName(), filterRequestUri, method);
                return getVoidMono(exchange, Result.fail(AUTHORITY_EXCEPTION_CODE, new RuntimeException(user.getLoginName() + " 用户没有权限访问路径：" + filterRequestUri)));
            }
        }

        //获取数据权限，转发到下游服务
        mutate.header(DATA_PERMISSION_HEADER, userService.getDataPermissionByUserIdAndPermission(user.getId(), method, filterRequestUri));

        //添加用户ID放入请求头转发其他服务
        mutate.header(JwtTokenUtil.USER_ID, String.valueOf(user.getId()));
        //添加用户名称放入请求头，因名称有中文，需要使用URL编码
        mutate.header(JwtTokenUtil.NAME, URLEncoder.encode(user.getName(), StandardCharsets.UTF_8));

        ServerHttpRequest build = mutate.build();
        return chain.filter(exchange.mutate().request(build).build());
    }


    /**
     * URI是否以什么打头
     *
     * @param requestUri 请求路径
     */
    private boolean isStartWith(String requestUri) {
        for (String s : startWith.split(",")) {
            if (requestUri.startsWith(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取header中或者参数中名为"token"的用户信息
     *
     * @param request ServerHttpRequest
     * @param ctx     ServerHttpRequest.Builder
     */
    private JwtTokenUtil.UserInfo getJWTUser(ServerHttpRequest request, ServerHttpRequest.Builder ctx) {
        List<String> strings = request.getHeaders().get(HttpHeaders.AUTHORIZATION);
        String authToken = null;
        if (strings != null) {
            authToken = strings.get(0);
        }
        if (!StringUtils.hasText(authToken)) {
            strings = request.getQueryParams().get("token");
            if (strings == null) {
                throw new RuntimeException("获取不到jwt相关信息");
            }
            authToken = strings.get(0);
            ctx.header(HttpHeaders.AUTHORIZATION, Objects.requireNonNull(authToken));

        }
        return JwtTokenUtil.getUserFromToken(authToken.substring(JwtTokenUtil.TOKEN_PREFIX.length()));
    }


    /**
     * httpMethod 匹配
     *
     * @param method      需要匹配的请求方式
     * @param matchMethod 匹配的目标方式（其中包含*）
     */
    private boolean matchHttpMethod(String method, String matchMethod) {
        if (!StringUtils.hasText(method)) {
            return false;
        }
        if ("*".equals(matchMethod)) {
            return true;
        }
        return method.equalsIgnoreCase(matchMethod);
    }


    /**
     * URL匹配
     *
     * @param url      需要匹配的URL
     * @param matchUrl 匹配的目标URL
     */
    private boolean matchUrl(String url, String matchUrl) {
        return url.matches(parseUrl2Regex(matchUrl));
    }


    /**
     * 网关抛异常，将result对象转换成json输出
     *
     * @param result 统一返回对象
     */
    private Mono<Void> getVoidMono(ServerWebExchange serverWebExchange, Result<?> result) {
        ServerHttpResponse response = serverWebExchange.getResponse();
        // 设置 http status
        response.setStatusCode(HttpStatus.OK);
        // 设置 header
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        byte[] bytes = JSONObject.toJSONString(result).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Flux.just(buffer));
    }


    /**
     * 解析URL成正则表达式（其中存在/*、/**）
     * 正则 \w 包括[A-Za-z0-9_]，因为 url 中还有 - ，所以采用 \w- 进行匹配
     * 对于其他特殊的字符比如 % . ~ ！ 等暂时不处理（路径中通常情况下不会存在如此特殊的字符，若存在请使用其他方式替代）
     *
     * @return 正则表达式
     */
    private String parseUrl2Regex(String url) {
        //先处理/**（必须以**结尾）
        if (url.matches(".*\\*\\*$")) {
            url = url.substring(0, url.length() - 2) + "[\\w-\\/]+";
        }
        //处理/*，可能存在/*/xxx的地址，所以替换*号即可（replaceAll方法问题，替换需要4个反斜杠才能替换成[\w]+）
        url = url.replaceAll("\\*", "[\\\\w-]+");
        //添加开始与结束正则
        url = "^" + url + "$";
        return url;
    }
}
