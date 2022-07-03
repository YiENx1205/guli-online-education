package com.atguigu.aclservice.controller;


import com.atguigu.aclservice.entity.Permission;
import com.atguigu.aclservice.service.PermissionService;
import com.atguigu.commonutils.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 权限 前端控制器
 * </p>
 *
 * @author Quan
 * @since 2021-08-12
 */
@RestController
@RequestMapping("/aclservice/permission")
public class PermissionController {
    @Resource
    private PermissionService permissionService;

    // 获取全部菜单
    @ApiOperation(value = "查询所有菜单")
    @GetMapping
    public R list() {
        List<Permission> list = permissionService.getAllMenu();
        return R.ok().data("list", list);
    }

    @ApiOperation(value = "递归删除菜单")
    @DeleteMapping("/remove/{id}")
    public R remove(@PathVariable String id) {
        permissionService.removeChildById(id);
        return R.ok();
    }

    @ApiOperation(value = "给角色分配菜单")
    @PostMapping("/saveRolePermissionRelationship")
    public R saveRolePermissionRelationship(String roleId, String[] permissions) {
        permissionService.saveRolePermissionRelationship(roleId, permissions);
        return R.ok();
    }
}

