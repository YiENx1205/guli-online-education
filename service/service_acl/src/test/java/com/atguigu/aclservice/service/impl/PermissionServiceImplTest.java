package com.atguigu.aclservice.service.impl;

import com.atguigu.aclservice.entity.Permission;
import com.atguigu.aclservice.service.PermissionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author quan
 * @date 2021-08-12
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PermissionServiceImplTest {
    @Resource
    private PermissionService permissionService;

    @Test
    public void testGetAllMenu() {
        List<Permission> list = permissionService.getAllMenu();
        for (Permission permission : list) {
            System.out.println(permission);
        }
    }

    @Test
    public void removeChildById() {
        permissionService.removeChildById("111");
    }

    @Test
    public void saveRolePermissionRelationship() {
        permissionService.saveRolePermissionRelationship("r1", new String[]{"1", "2", "3"});
    }
}