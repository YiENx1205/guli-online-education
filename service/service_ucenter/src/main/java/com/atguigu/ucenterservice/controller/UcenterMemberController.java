package com.atguigu.ucenterservice.controller;


import com.atguigu.commonutils.JwtUtil;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.vo.UcenterMemberVo;
import com.atguigu.servicebase.handler.exception.CustomException;
import com.atguigu.ucenterservice.entity.UcenterMember;
import com.atguigu.ucenterservice.entity.vo.LoginVo;
import com.atguigu.ucenterservice.entity.vo.RegisterVo;
import com.atguigu.ucenterservice.service.UcenterMemberService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Member;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author Quan
 * @since 2021-08-06
 */
@RestController
@RequestMapping("/ucenterservice/member")
public class UcenterMemberController {
    @Resource
    private UcenterMemberService memberService;

    @ApiOperation(value = "会员登录")
    @PostMapping("/login")
    public R login(@RequestBody LoginVo loginVo) {
        String token = memberService.login(loginVo);
        return R.ok().data("token", token);
    }

    @ApiOperation(value = "会员注册")
    @PostMapping("/register")
    public R register(@RequestBody RegisterVo registerVo) {
        memberService.register(registerVo);
        return R.ok();
    }

    @ApiOperation(value = "根据token获取用户信息")
    @GetMapping("/info")
    public R info(HttpServletRequest request) {
        String memberId = JwtUtil.getMemberIdByJwtToken(request);
        if (StringUtils.isEmpty(memberId)) {
            memberId = JwtUtil.getMemberIdByCookieToken(request);
            if (StringUtils.isEmpty(memberId)) {
                return R.error().message("token不存在");
            }
        }
        UcenterMember member = memberService.getById(memberId);
        member.setPassword("");
        return R.ok().data("member", member);
    }

    @ApiOperation(value = "根据id获取用户信息")
    @GetMapping("/getUcenterMember/{memberId}")
    public UcenterMemberVo getUcenterMember(@PathVariable String memberId) {
        if (StringUtils.isEmpty(memberId)) {
            throw new CustomException(20001, "memberId不能为空");
        }
        UcenterMember member = memberService.getById(memberId);
        member.setPassword("");
        UcenterMemberVo ucenterMemberVo = new UcenterMemberVo();
        BeanUtils.copyProperties(member, ucenterMemberVo);
        return ucenterMemberVo;
    }

    @GetMapping("/countRegisterDay/{day}")
    public Integer countRegisterDay(@PathVariable String day) {
        return memberService.countRegisterDay(day);
    }
}

