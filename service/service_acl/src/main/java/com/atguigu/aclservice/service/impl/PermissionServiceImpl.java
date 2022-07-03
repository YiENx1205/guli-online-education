package com.atguigu.aclservice.service.impl;

import com.atguigu.aclservice.entity.Permission;
import com.atguigu.aclservice.entity.RolePermission;
import com.atguigu.aclservice.mapper.PermissionMapper;
import com.atguigu.aclservice.service.PermissionService;
import com.atguigu.aclservice.service.RolePermissionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 权限 服务实现类
 * </p>
 *
 * @author Quan
 * @since 2021-08-12
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {
    @Resource
    private RolePermissionService rolePermissionService;

    private List<Permission> permissionList;

    @Override
    public List<Permission> getAllMenu() {
        // 原来的sql语句递归
        // List<Permission> list = getDataByRecursion("0", 1);
        // return list;

        // 初始化菜单大列表
        this.permissionList = baseMapper.selectList(null);
        // 返回数据的一个集合
        List<Permission> res = new ArrayList<>();
        // 开始递归封装
        for (Permission current : permissionList) {
            if ("0".equals(current.getPid())) {
                current.setLevel(1);
                Permission children = selectChildren(current);
                res.add(children);
            }
        }
        return res;
    }

    @Override
    public void removeChildById(String id) {
        List<String> ids = new ArrayList<>();
        selectChildrenIdById(id, ids);
        ids.add(id);
        baseMapper.deleteBatchIds(ids);
    }

    @Override
    public void saveRolePermissionRelationship(String roleId, String[] permissions) {
        List<RolePermission> list = new ArrayList<>();
        for (String permission : permissions) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permission);
            list.add(rolePermission);
        }
        rolePermissionService.saveBatch(list);
    }

    private void selectChildrenIdById(String id, List<String> ids) {
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pid", id);
        queryWrapper.select("id");
        List<Permission> list = baseMapper.selectList(queryWrapper);
        list.forEach((item) -> {
            ids.add(item.getId());
            selectChildrenIdById(item.getId(), ids);
        });
    }

    // 通过一次性查询全部数据，然后递归在内存里筛选出子菜单
    private Permission selectChildren(Permission current) {
        current.setChildren(new ArrayList<>());
        for (Permission it : this.permissionList) {
            if (it.getPid().equals(current.getId())) {
                it.setLevel(current.getLevel() + 1);
                current.getChildren().add(selectChildren(it));
            }
        }
        return current;
    }

    // 递归，通过sql语句查询子菜单，有点频繁执行数据库
    private List<Permission> getDataByRecursion(String pid, Integer level) {
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pid", pid);
        List<Permission> list = baseMapper.selectList(queryWrapper);
        for (Permission permission : list) {
            permission.setLevel(level);
            List<Permission> children = getDataByRecursion(permission.getId(), level + 1);
            permission.setChildren(children);
        }
        return list;
    }
}
