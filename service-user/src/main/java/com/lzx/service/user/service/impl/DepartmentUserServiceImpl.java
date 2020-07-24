package com.lzx.service.user.service.impl;

import com.lzx.service.user.dao.DepartmentUserMapper;
import com.lzx.service.user.entity.DepartmentUser;
import com.lzx.service.user.service.DepartmentUserService;
import com.lzx.web.service.CommonServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author lzx
 * @since 2019/3/27
 */
@Service
public class DepartmentUserServiceImpl extends CommonServiceImpl<DepartmentUserMapper, DepartmentUser, Long> implements DepartmentUserService {
}
