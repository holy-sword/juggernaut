package com.lzx.service.user.service;


import com.lzx.service.user.entity.Department;
import com.lzx.service.user.entity.User;
import com.lzx.web.service.CommonService;

import java.util.List;

/**
 * @author lzx
 * @since 2019/3/15
 */
public interface DepartmentService extends CommonService<Department, Long> {

    /**
     * 根据部门ID获取该部门下的用户列表
     *
     * @param departmentId 部门ID
     */
    List<User> listUserByDepartmentId(long departmentId);
}
