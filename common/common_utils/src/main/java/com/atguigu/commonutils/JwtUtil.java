package com.atguigu.commonutils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author quan
 * @date 2021-08-06
 */
public class JwtUtil {
    /**
     * token过期时间
     */
    public static final long EXPIRE = 1000 * 60 * 60 * 24;

    /**
     * token密钥
     */
    public static final String APP_SECRET = "F6SmW75xy5qV9UCkckc1KTm8DsrQC6";

    public static String getJwtToken(String id, String nickname) {
        String jwtToken;
        jwtToken = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .setSubject("guli-user")
                .setIssuedAt(new Date())
                // 设置过期时间：当前时间+上面设置的EXPIRE
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                // .claim设置主体部分,存储用户信息
                .claim("id", id)
                .claim("nickname", nickname)
                .signWith(SignatureAlgorithm.HS256, APP_SECRET)
                .compact();
        return jwtToken;
    }

    /**
     * 判断token是否存在与有效
     *
     * @param jwtToken token字符串
     * @return 是否有效
     */
    public static boolean checkToken(String jwtToken) {
        if (StringUtils.isEmpty(jwtToken)) {
            return false;
        }
        // 验证token是否合法有效， parser解析token，如果Exception说明解析失败不合法
        try {
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 判断token是否存在与有效
     *
     * @param request Servlet对象
     * @return 是否有效
     */
    public static boolean checkToken(HttpServletRequest request) {
        try {
            String jwtToken = request.getHeader("token");
            if (StringUtils.isEmpty(jwtToken)) {
                return false;
            }
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 根据token获取会员id
     *
     * @param request Servlet对象
     * @return 主体里的用户id值
     */
    public static String getMemberIdByJwtToken(HttpServletRequest request) {
        String jwtToken = request.getHeader("token");
        if (StringUtils.isEmpty(jwtToken)) {
            return "";
        }
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        Claims claims = claimsJws.getBody();
        return (String) claims.get("id");
    }

    public static String getMemberIdByCookieToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String jwtToken = null;
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if ("guli_token".equals(cookie.getName())) {
                    jwtToken = cookie.getValue();
                }
            }
        }
        if (StringUtils.isEmpty(jwtToken)) {
            return "";
        }
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        Claims claims = claimsJws.getBody();
        return (String) claims.get("id");
    }
}