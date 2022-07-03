package com.atguigu.aclservice.service.impl;

import com.atguigu.aclservice.entity.RolePermission;
import com.atguigu.aclservice.mapper.RolePermissionMapper;
import com.atguigu.aclservice.service.RolePermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色与权限关联表 服务实现类
 * </p>
 *
 * @author Quan
 * @since 2021-08-12
 */
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements RolePermissionService {

}
