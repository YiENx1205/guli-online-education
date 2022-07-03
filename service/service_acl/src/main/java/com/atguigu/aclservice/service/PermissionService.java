package com.atguigu.aclservice.service;

import com.atguigu.aclservice.entity.Permission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 权限 服务类
 * </p>
 *
 * @author Quan
 * @since 2021-08-12
 */
public interface PermissionService extends IService<Permission> {

    List<Permission> getAllMenu();

    void removeChildById(String id);

    void saveRolePermissionRelationship(String roleId, String[] permissions);
}
