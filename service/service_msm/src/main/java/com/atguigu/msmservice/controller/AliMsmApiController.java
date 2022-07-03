package com.atguigu.msmservice.controller;

import com.atguigu.commonutils.R;
import com.atguigu.commonutils.RandomUtil;
import com.atguigu.msmservice.service.MsmService;
import com.atguigu.msmservice.utils.AliSmsConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author quan
 * @date 2021-08-06
 */
@RestController
@RequestMapping("/msmservice/msm")
@Slf4j
public class AliMsmApiController {

    @Resource
    private MsmService msmService;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping(value = "/send/{phone}")
    public R code(@PathVariable String phone) {
        String code = redisTemplate.opsForValue().get(phone);
        if(!StringUtils.isEmpty(code)) {
            return R.ok();
        }
        code = RandomUtil.getSixBitRandom();
        Map<String,Object> param = new HashMap<>();
        param.put("code", code);
        log.info("验证码是：{}", code);
        // 阿里云短信服务暂时用不了，这里先注释掉
        boolean isSend = msmService.send(phone, param);
        // boolean isSend = true;
        if(isSend) {
            // redis中key的格式可以设置为功能::手机号，防止在其它业务功能中冲突，比如一边注册一边重置密码
            String key = "register::" + phone;
            // 有效期5分钟
            redisTemplate.opsForValue().set(key, code,5, TimeUnit.MINUTES);
            return R.ok();
        } else {
            return R.error().message("发送短信失败");
        }
    }
}
