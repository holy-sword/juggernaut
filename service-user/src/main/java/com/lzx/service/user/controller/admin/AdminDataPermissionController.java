package com.lzx.service.user.controller.admin;

import com.lzx.service.user.entity.DataPermission;
import com.lzx.service.user.service.DataPermissionService;
import com.lzx.web.controller.AbstractController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lzx
 * @since 2019/3/15
 */
@RestController
@RequestMapping("api/admin/user/permission/data")
public class AdminDataPermissionController extends AbstractController<DataPermissionService, DataPermission, Long> {

}
