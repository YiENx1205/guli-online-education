package com.atguigu.ucenterservice.controller;

import com.atguigu.commonutils.JwtUtil;
import com.atguigu.servicebase.handler.exception.CustomException;
import com.atguigu.ucenterservice.entity.UcenterMember;
import com.atguigu.ucenterservice.service.UcenterMemberService;
import com.atguigu.ucenterservice.utils.HttpClientUtil;
import com.atguigu.ucenterservice.utils.WeChatOpenConstantUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * @author quan
 * @date 2021-08-06
 */
@Controller
@Slf4j
@RequestMapping("/ucenterservice/wx")
public class UcenterWxController {
    @Resource
    private UcenterMemberService memberService;

    @GetMapping("/scanLogin")
    public String scanLogin() {
        // 微信开放平台授权baseUrl
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";
        String redirectUrl = WeChatOpenConstantUtil.WX_OPEN_REDIRECT_URL;
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, String.valueOf(StandardCharsets.UTF_8));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String qrcodeUrl = String.format(baseUrl, WeChatOpenConstantUtil.WX_OPEN_APP_ID, redirectUrl, "random_state");
        return "redirect:" + qrcodeUrl;
    }

    @GetMapping("/callback")
    public String callback(String code, String state) {
        log.info("code: {}, state: {}", code, state);

        try {
            // 向认证服务器发送请求换取access_token
            String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                    "?appid=%s" +
                    "&secret=%s" +
                    "&code=%s" +
                    "&grant_type=authorization_code";
            String accessTokenUrl = String.format(baseAccessTokenUrl,
                    WeChatOpenConstantUtil.WX_OPEN_APP_ID,
                    WeChatOpenConstantUtil.WX_OPEN_APP_SECRET,
                    code);
            String result;
            result = HttpClientUtil.get(accessTokenUrl);
            log.info("get access_token result Object: {}", result);
            // 解析json字符串
            Gson gson = new Gson();
            HashMap map = gson.fromJson(result, HashMap.class);
            String accessToken = (String) map.get("access_token");
            String openid = (String) map.get("openid");
            log.info("accessToken: {}, openid: {}", accessToken, openid);

            // 查询数据库：根据openid查询当前用用户是否曾经使用过微信登录
            UcenterMember member = memberService.getByOpenid(openid);
            if (member == null) {
                // 访问微信的资源服务器，获取用户信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                //再次拼接微信地址
                String userInfoUrl = String.format(baseUserInfoUrl, accessToken, openid);

                String userInfo = HttpClientUtil.get(userInfoUrl);
                log.info("userInfo: {}", userInfo);
                // 获取的微信个人信息json信息进行转换
                HashMap userInfoMap = gson.fromJson(userInfo, HashMap.class);
                String nickname = (String) userInfoMap.get("nickname");   // 昵称
                String headimgurl = (String) userInfoMap.get("headimgurl");  // 头像
                int sex = Double.valueOf(userInfoMap.get("sex").toString()).intValue();  // 性别

                // 把微信信息注册到数据库中
                member = new UcenterMember();
                member.setNickname(nickname);
                member.setOpenid(openid);
                member.setAvatar(headimgurl);
                member.setSex(sex);
                // 这里save会把mybatisPlus生成的id回填， 所以下面getId能获取到
                memberService.save(member);
            }

            //使用jwt生成token字符串
            String jwtToken = JwtUtil.getJwtToken(member.getId(), member.getNickname());
            //返回首页面
            return "redirect:http://localhost:3000?token=" + jwtToken;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(20001, "登录失败");
        }
    }
}
