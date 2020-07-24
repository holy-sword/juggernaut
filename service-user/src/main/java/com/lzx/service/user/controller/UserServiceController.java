package com.lzx.service.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lzx.service.user.entity.DataPermission;
import com.lzx.service.user.entity.Permission;
import com.lzx.service.user.entity.User;
import com.lzx.service.user.service.DataPermissionService;
import com.lzx.service.user.service.PermissionService;
import com.lzx.service.user.service.UserService;
import com.lzx.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * 服务内调用接口
 *
 * @author lzx
 * @since 2020/5/21
 */
@RestController
public class UserServiceController extends BaseController<User, Long> {

    @Autowired
    private UserService userService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private DataPermissionService dataPermissionService;

    /**
     * 获取所有权限资源列表
     */
    @GetMapping("permission")
    public List<Permission> getAllPermission() {
        return permissionService.list();
    }


    /**
     * 获取某个用户全部的权限资源
     */
    @GetMapping("permission/user/{userId}")
    public List<Permission> getPermissionByUserId(@PathVariable("userId") Long userId) {
        return permissionService.getByUserId(userId);
    }

    /**
     * 根据条件查询用户列表
     *
     * @param request {必须有 pageNum：当前页码，pageSize：每页数量。可选有自定义的查询参数，默认查询参数使用前缀 {@link BaseController#DEFAULT_SEARCH_PREFIX}}
     */
    @GetMapping("user")
    public Page<User> list(HttpServletRequest request) {
        QueryWrapper<User> wrapper = buildQueryWrapperFromRequest(request);
        return PageHelper.startPage(request).doSelectPage(() -> userService.list(wrapper));
    }

    /**
     * 根据ID获取用户信息
     *
     * @param id 用户ID
     */
    @GetMapping("user/{id}")
    public User getById(@PathVariable String id) {
        return userService.getById(id);
    }


    /**
     * 根据用户ID以及接口权限信息获取数据权限
     *
     * @param userId     用户ID
     * @param url        接口地址
     * @param httpMethod 接口请求方式
     * @return 数据权限
     */
    @PostMapping("permission/data")
    public String getDataPermissionByUserIdAndPermission(Long userId, String httpMethod, String url) {
        List<DataPermission> list = dataPermissionService.getByUserId(userId);
        //寻找存在并且最接近的数据权限
        Optional<DataPermission> first = list.stream()
                //过滤请求方式（httpMethod）
                .filter(p -> matchHttpMethod(httpMethod, p.getHttpMethod()))
                //过滤请求url
                .filter(p -> matchUrl(url, p.getUrl()))
                //获取排序后最优先的数据权限
                .min(Comparator.comparingInt(DataPermission::getOrdered));

        return first.isPresent() ? first.get().getPermission() : "";
    }


    /**
     * httpMethod 匹配
     *
     * @param method      需要匹配的请求方式
     * @param matchMethod 匹配的目标方式（其中包含*）
     */
    private boolean matchHttpMethod(String method, String matchMethod) {
        if (StringUtils.isEmpty(method)) {
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
     * 解析URL成正则表达式（其中存在/*、/**）
     *
     * @return 正则表达式
     */
    private String parseUrl2Regex(String url) {
        //先处理/**（必须以**结尾）
        if (url.matches(".*\\*\\*$")) {
            url = url.substring(0, url.length() - 2) + "[\\w\\/]+";
        } else {
            //处理/*，可能存在/*/xxx的地址，所以替换*号即可（replaceAll方法问题，替换需要4个反斜杠才能替换成[\w]+）
            url = url.replaceAll("\\*", "[\\\\w]+");
        }
        //添加开始与结束正则
        url = "^" + url + "$";
        return url;
    }
}
