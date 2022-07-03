package com.atguigu.ucenterservice.service.impl;

import com.alibaba.nacos.common.util.Md5Utils;
import com.atguigu.commonutils.JwtUtil;
import com.atguigu.servicebase.handler.exception.CustomException;
import com.atguigu.ucenterservice.entity.UcenterMember;
import com.atguigu.ucenterservice.entity.vo.LoginVo;
import com.atguigu.ucenterservice.entity.vo.RegisterVo;
import com.atguigu.ucenterservice.mapper.UcenterMemberMapper;
import com.atguigu.ucenterservice.service.UcenterMemberService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author Quan
 * @since 2021-08-06
 */
@Service
@Slf4j
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private UcenterMemberMapper memberMapper;

    @Override
    public String login(LoginVo loginVo) {
        log.info("login: {}", loginVo);
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        //校验参数
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
            throw new CustomException(20001, "参数错误");
        }
        //获取会员
        UcenterMember member = baseMapper.selectOne(new QueryWrapper<UcenterMember>().eq("mobile", mobile));
        if (null == member) {
            throw new CustomException(20001, "账号不存在");
        }
        //校验密码
        if (!Md5Utils.getMD5(password.getBytes(StandardCharsets.UTF_8)).equals(member.getPassword())) {
            throw new CustomException(20001, "密码不正确");
        }
        //校验是否被禁用
        if (member.getIsDisabled()) {
            throw new CustomException(20001, "账号已被禁用");
        }
        //使用JWT生成token字符串
        String token;
        token = JwtUtil.getJwtToken(member.getId(), member.getNickname());
        return token;
    }

    @Override
    public void register(RegisterVo registerVo) {
        log.info("register: {}", registerVo);
        String nickname = registerVo.getNickname();
        String mobile = registerVo.getMobile();
        String password = registerVo.getPassword();
        String code = registerVo.getCode();

        // 验证码密码格式
        if (StringUtils.isEmpty(password)) {
            throw new CustomException(20001, "密码不能为空");
        }
        // 验证验证码
        String key = "register::" + mobile;
        String redisCode = redisTemplate.opsForValue().get(key);
        if (!code.equals(redisCode)) {
            throw new CustomException(20001, "验证码不正确");
        }
        // 判断手机号是否重复
        QueryWrapper<UcenterMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile", mobile);
        Integer count = memberMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new CustomException(20001, "改手机号已注册");
        }
        // 开始注册
        UcenterMember ucenterMember = new UcenterMember();
        BeanUtils.copyProperties(registerVo, ucenterMember);
        // md5加密
        ucenterMember.setPassword(Md5Utils.getMD5(password.getBytes()));
        this.save(ucenterMember);
    }

    @Override
    public UcenterMember getByOpenid(String openid) {
        QueryWrapper<UcenterMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid", openid);
        return this.getOne(queryWrapper);
    }

    @Override
    public Integer countRegisterDay(String day) {
        return baseMapper.countRegisterDay(day);
    }
}
