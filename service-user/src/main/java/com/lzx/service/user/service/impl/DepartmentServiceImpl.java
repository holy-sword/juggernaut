package com.lzx.service.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lzx.service.user.dao.DepartmentMapper;
import com.lzx.service.user.dao.DepartmentUserMapper;
import com.lzx.service.user.dao.UserMapper;
import com.lzx.service.user.entity.Department;
import com.lzx.service.user.entity.DepartmentUser;
import com.lzx.service.user.entity.User;
import com.lzx.service.user.service.DepartmentService;
import com.lzx.web.exception.ServiceException;
import com.lzx.web.service.CommonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author lzx
 * @since 2019/3/15
 */
@Service
public class DepartmentServiceImpl extends CommonServiceImpl<DepartmentMapper, Department, Long> implements DepartmentService {

    @Autowired
    private DepartmentUserMapper userDepartmentMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * 重写保存，校验名称是否重复
     */
    @Override
    public boolean save(Department entity) {
        String name = entity.getName();
        Objects.requireNonNull(name, "部门名称不能为空");
        Department one = this.getOne(new LambdaQueryWrapper<Department>().eq(Department::getName, name));
        if (one != null) throw new ServiceException("部门名称不能重复，已存在名称：" + name);
        return super.save(entity);
    }

    @Override
    public List<User> listUserByDepartmentId(long departmentId) {
        //只查询出所有的用户ID
        List<DepartmentUser> departmentUserList = userDepartmentMapper.selectList(
                new LambdaQueryWrapper<DepartmentUser>()
                        .select(DepartmentUser::getUserId)
                        .eq(DepartmentUser::getDepartmentId, departmentId));
        //空直接返回
        if (departmentUserList.isEmpty()) {
            return Collections.emptyList();
        }
        //用户ID去重并查询用户列表
        return userMapper.selectBatchIds(
                departmentUserList.stream()
                        .map(DepartmentUser::getUserId)
                        .distinct()
                        .collect(Collectors.toList())
        );
    }
}
