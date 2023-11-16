package com.lzx.service.user.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageInfo;
import com.lzx.common.Result;
import com.lzx.service.user.entity.Department;
import com.lzx.service.user.entity.DepartmentUser;
import com.lzx.service.user.service.DepartmentService;
import com.lzx.service.user.service.DepartmentUserService;
import com.lzx.service.user.service.UserService;
import com.lzx.web.controller.AbstractController;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author lzx
 * @since 2020/5/22
 */
@RestController
@RequestMapping("api/admin/user/department")
public class AdminDepartmentController extends AbstractController<DepartmentService, Department, Long> {

    @Autowired
    private DepartmentUserService departmentUserService;
    @Autowired
    private UserService userService;

    /**
     * 获取部门下的用户列表
     *
     * @param departmentId 部门ID
     */
    @GetMapping("{departmentId}/user")
    @SuppressWarnings("all")
    public Result<?> getUserList(@PathVariable String departmentId, HttpServletRequest request) {
        //设置查询条件
        LambdaQueryWrapper<DepartmentUser> wrapper = buildQueryWrapperFromRequest(request, DepartmentUser.class).lambda()
                .select(DepartmentUser::getUserId)
                .eq(DepartmentUser::getDepartmentId, departmentId);
        //获得用户ID
        PageInfo pageInfo = selectPageInfoByISelect(request, () -> departmentUserService.list(wrapper));
        //是否有数据
        if (pageInfo.getSize() >= 1) {
            //转换用户对象
            List<DepartmentUser> list = pageInfo.getList();
            pageInfo.setList(userService.listByIds(list.stream().map(DepartmentUser::getUserId).distinct().collect(Collectors.toList())));
        }

        return Result.ok(pageInfo);
    }

    /**
     * 将用户添加进部门
     *
     * @param departmentId 部门ID
     * @param userIds      用户ID数组
     */
    @PostMapping("{departmentId}/user")
    public Result<?> addUser(@PathVariable Long departmentId, @RequestBody Long[] userIds) {
        if (userIds.length < 1) {
            return Result.fail("用户ID不能为空");
        }
        // 根据用户ID集合获取该部门关联表中内容
        Set<Long> userIdSet = departmentUserService
                .list(new LambdaQueryWrapper<DepartmentUser>()
                        .eq(DepartmentUser::getDepartmentId, departmentId)
                        .in(DepartmentUser::getUserId, Arrays.stream(userIds).collect(Collectors.toList())))
                .stream()
                .map(DepartmentUser::getUserId)
                .collect(Collectors.toSet());

        //添加用户部门关系
        departmentUserService.saveBatch(
                Arrays.stream(userIds)
                        // 过滤掉已经在该部门的用户
                        .filter(userId -> !userIdSet.contains(userId))
                        // 转化成用户部门关系对象
                        .map(userId -> new DepartmentUser() {{
                            setDepartmentId(departmentId);
                            setUserId(userId);
                        }})
                        .collect(Collectors.toList()));
        return Result.ok();
    }

    /**
     * 删除部门中的用户
     *
     * @param departmentId 部门ID
     * @param userIds      用户ID数组
     */
    @DeleteMapping("{departmentId}/user")
    public Result<?> deleteUser(@PathVariable String departmentId, @RequestBody String[] userIds) {
        if (userIds.length < 1) {
            return Result.fail("用户ID不能为空");
        }
        //删除用户部门关系
        LambdaQueryWrapper<DepartmentUser> wrapper = new LambdaQueryWrapper<DepartmentUser>()
                .eq(DepartmentUser::getDepartmentId, departmentId)
                .in(DepartmentUser::getUserId, Arrays.asList(userIds));
        departmentUserService.remove(wrapper);
        return Result.ok();
    }

}
