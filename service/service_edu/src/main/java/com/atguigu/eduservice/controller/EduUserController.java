package com.atguigu.eduservice.controller;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.vo.EduUserLoginQuery;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * @author quan
 * @date 2021-08-01
 */
@RestController
@RequestMapping("/eduservice/user")
public class EduUserController {
    @PostMapping("/login")
    public R login(@RequestBody EduUserLoginQuery eduUserLoginQuery) {
        System.out.println(eduUserLoginQuery);
        return R.ok().data("token", "admin-token");
    }

    @GetMapping("/info")
    public R info() {
        HashMap<String, Object> infoMap = new HashMap<>();
        infoMap.put("roles", new String[]{"admin"});
        infoMap.put("introduction", "I am a super administrator");
        infoMap.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        infoMap.put("name", "Super Admin");
        return R.ok().data(infoMap);
    }

    @PostMapping("/logout")
    public R logout() {
        return R.ok();
    }
}
