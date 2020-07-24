package com.lzx.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lzx.common.Result;
import com.lzx.web.entity.CommonEntity;
import com.lzx.web.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Arrays;

/**
 * 实体通用restful接口Controller
 * 继承使用@RestController
 *
 * @param <S>实体顶级Service
 * @param <E>实体
 * @param <D>主键类型
 * @author lzx
 * @since 2019/6/18
 */
public abstract class AbstractController<S extends CommonService<E, D>, E extends CommonEntity<D>, D extends Serializable> extends BaseController<E, D> {

    @Autowired
    protected S service;

    /**
     * 获取某个资源
     *
     * @param id 主键ID
     */
    @GetMapping(value = "{id}")
    public Result<?> getById(@PathVariable("id") D id) {
        return Result.ok(service.getById(id));
    }


    /**
     * 通用分页查询
     *
     * @param request {必须有 pageNum：当前页码，pageSize：每页数量。可选有自定义的查询参数，默认查询参数使用前缀 {@link BaseController#DEFAULT_SEARCH_PREFIX}}
     */
    @GetMapping
    public Result<?> list(HttpServletRequest request) {
        LambdaQueryWrapper<E> wrapper = buildQueryWrapperFromRequest(request).lambda();
        //分页查询
        PageInfo<?> pageInfo = selectPageInfoByISelect(request, () -> service.list(wrapper));
        return Result.ok(pageInfo);
    }


    /**
     * 添加或者更新
     *
     * @param entity 实体
     * @return 返回主键ID
     */
    @PostMapping
    public Result<?> add(@RequestBody E entity) {
        service.saveOrUpdate(entity);
        return Result.ok(entity.getId());
    }


    /**
     * 更新
     *
     * @param entity 更新对象
     */
    @PutMapping
    public Result<?> update(@RequestBody E entity) {
        return Result.ok(service.updateById(entity));
    }


    /**
     * 删除
     *
     * @param ids 主键ID
     */
    @DeleteMapping
    public Result<?> delete(@RequestBody D[] ids) {
        return Result.ok(service.removeByIds(Arrays.asList(ids)));
    }


    /**
     * 分页查询
     *
     * @param request 分页参数直接从request中取
     * @param select  调用的查询方法
     */
    protected PageInfo<?> selectPageInfoByISelect(HttpServletRequest request, ISelect select) {
        return PageHelper.startPage(request).doSelectPageInfo(select);
    }

    /**
     * 分页查询
     *
     * @param pageNum  页数
     * @param pageSize 每页数量
     * @param select   调用的查询方法
     */
    protected PageInfo<?> selectPageInfoByISelect(int pageNum, int pageSize, ISelect select) {
        return PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(select);
    }
}
