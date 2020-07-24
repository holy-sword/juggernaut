package com.lzx.web.controller;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lzx.web.entity.CommonEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 基础Controller（封装解析参数通用方法）
 *
 * @param <E>实体
 * @param <D>主键类型
 * @author lzx
 * @since 2019/3/15
 */
@Slf4j
public abstract class BaseController<E extends CommonEntity<D>, D extends Serializable> {

    /**
     * 默认的搜索参数前缀
     */
    protected final String DEFAULT_SEARCH_PREFIX = "s_";


    /**
     * 从请求中 {@link HttpServletRequest} 中构建查询条件 {@link QueryWrapper}
     * 查询条件使用默认的查询前缀参数 {@link BaseController#DEFAULT_SEARCH_PREFIX}
     * 查询的实体类型使用当前泛型中的实体类型 {@link E}
     *
     * @param request request
     */
    protected QueryWrapper<E> buildQueryWrapperFromRequest(HttpServletRequest request) {
        return buildQueryWrapperFromRequest(request, DEFAULT_SEARCH_PREFIX, getEntityClass());
    }

    /**
     * 从请求中 {@link HttpServletRequest} 中构建查询条件 {@link QueryWrapper}
     * 查询的实体类型使用当前泛型中的实体类型 {@link E}
     *
     * @param request request
     * @param prefix  查询条件前缀
     */
    protected QueryWrapper<E> buildQueryWrapperFromRequest(HttpServletRequest request, String prefix) {
        return buildQueryWrapperFromRequest(request, prefix, getEntityClass());
    }

    /**
     * 从请求中 {@link HttpServletRequest} 中构建查询条件 {@link QueryWrapper}
     * 查询的实体类型使用当前泛型中的实体类型 {@link E}
     *
     * @param request request
     * @param clazz   查询的实体类型
     */
    protected <T> QueryWrapper<T> buildQueryWrapperFromRequest(HttpServletRequest request, Class<T> clazz) {
        return buildQueryWrapperFromRequest(request, DEFAULT_SEARCH_PREFIX, clazz);
    }

    /**
     * 从请求中 {@link HttpServletRequest} 中构建查询条件 {@link QueryWrapper}
     *
     * @param request request
     * @param prefix  查询条件前缀
     * @param clazz   查询的实体类型
     */
    protected <T> QueryWrapper<T> buildQueryWrapperFromRequest(HttpServletRequest request, String prefix, Class<T> clazz) {
        Map<String, Object> map = WebUtils.getParametersStartingWith(request, prefix);

        return buildQueryWrapperFromMap(map, clazz);
    }


    /**
     * 从查询Map<String,Object> 中构建查询条件 {@link QueryWrapper}
     *
     * @param map   查询Map
     * @param clazz 需要映射的实体
     */
    public <T> QueryWrapper<T> buildQueryWrapperFromMap(Map<String, Object> map, Class<T> clazz) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();

        map.forEach((k, v) -> {
            if (StringUtils.isEmpty(v))
                return;
            String[] keyArray = k.split("_");
            if (keyArray.length < 2)
                return;

            //获取数据库中字段名称
            String columnName = parseColumnName(clazz, keyArray[1]);
            if (StringUtils.isEmpty(columnName))
                return;
            //保存排序顺序Map<顺序（自然升序，小的先处理）,Map<列名,是否升序>>
            Map<Integer, Map<String, Boolean>> sortMap = new TreeMap<>();

            //关键字转换小写进行匹配
            switch (keyArray[0].toLowerCase()) {
                case "like":
                    //出于性能考虑，like只使用右半边like走索引
                    wrapper.likeRight(columnName, v);
                    break;
                case "eq":
                    //等于 =
                    wrapper.eq(columnName, v);
                    break;
                case "ne":
                    //不等于 <>
                    wrapper.ne(columnName, v);
                    break;
                case "gt":
                    //大于 >
                    wrapper.gt(columnName, v);
                    break;
                case "ge":
                    //大于等于 >=
                    wrapper.ge(columnName, v);
                    break;
                case "lt":
                    //小于 <
                    wrapper.lt(columnName, v);
                    break;
                case "le":
                    //小于等于 <=
                    wrapper.le(columnName, v);
                    break;
                case "between":
                    //between val1 and val2
                    if (v instanceof List) {
                        //处理集合
                        wrapper.between(columnName, ((List<?>) v).get(0), ((List<?>) v).get(1));
                    } else if (v instanceof String) {
                        //处理英文逗号 "," 分隔
                        String[] arr = ((String) v).split(",");
                        wrapper.between(columnName, arr[0], arr[1]);
                    }
                    break;
                case "notbetween":
                    //not between val1 and val2
                    if (v instanceof List) {
                        //处理集合
                        wrapper.notBetween(columnName, ((List<?>) v).get(0), ((List<?>) v).get(1));
                    } else if (v instanceof String) {
                        //处理英文逗号 "," 分隔
                        String[] arr = ((String) v).split(",");
                        wrapper.notBetween(columnName, arr[0], arr[1]);
                    }
                    break;
                case "isnull":
                    //field is null
                    wrapper.isNull(columnName);
                    break;
                case "isnotnull":
                    //field is not null
                    wrapper.isNotNull(columnName);
                    break;
                case "in":
                    //field in (   )
                    if (v instanceof List) {
                        //处理集合
                        wrapper.in(columnName, (List<?>) v);
                    }
                    break;
                case "sort": {
                    //排序，当值为忽略大小写的 "desc" 或者数字1时，降序排列，其余升序
                    boolean asc = !("desc".equalsIgnoreCase(String.valueOf(v)) || v.equals(1));
                    Map<String, Boolean> columnNameMap = new HashMap<>();
                    columnNameMap.put(columnName, asc);
                    //默认排序 0
                    sortMap.put(keyArray.length > 2 ? Integer.parseInt(keyArray[2]) : 0, columnNameMap);
                    break;
                }
            }

            //添加排序
            sortMap.forEach((order, columnNameMap) -> {
                for (Map.Entry<String, Boolean> entry : columnNameMap.entrySet()) {
                    wrapper.orderBy(true, entry.getValue(), entry.getKey());
                }
            });


        });

        return wrapper;
    }


    /**
     * 解析列名
     * 处理数据库中列名中的特殊符号
     *
     * @param columnName 需要解析的列名
     * @return 数据库字段名
     */
    private String parseColumnName(Class<?> clazz, String columnName) {
        Field field = ReflectionUtils.findField(clazz, columnName);
        if (field == null)
            return "";

        TableField annotation = field.getAnnotation(TableField.class);
        if (annotation == null || "".equals(annotation.value())) {
            //驼峰转下划线处理
            return camelToUnderline(columnName);
        } else {
            return annotation.value();
        }
    }


    /**
     * 字符串驼峰转下划线格式
     *
     * @param param 需要转换的字符串
     * @return 转换好的字符串
     */
    public static String camelToUnderline(String param) {
        if (StringUtils.isEmpty(param)) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append('_');
                c = Character.toLowerCase(c);
            }
            sb.append(c);
        }
        return sb.toString();
    }


    /**
     * 从泛型中获取实际Entity的Class对象
     */
    @SuppressWarnings("unchecked")
    protected final Class<E> getEntityClass() {
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        for (Type typeArgument : type.getActualTypeArguments()) {
            if (ClassUtils.isAssignable(CommonEntity.class, (Class<?>) typeArgument)) {
                return (Class<E>) typeArgument;
            }
        }
        throw new RuntimeException("无法获取到实体对象的Class，请检查泛型声明");
    }
}